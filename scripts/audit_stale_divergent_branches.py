#!/usr/bin/env python3
"""Read-only inventory of stale divergent branches with diff statistics."""
import datetime,json,os,subprocess
BASE=os.environ["PRODUCTION_BRANCH"]
def git(*args):
 p=subprocess.run(["git",*args],capture_output=True,text=True)
 return p.returncode,p.stdout.strip()
code,raw=git("for-each-ref","--format=%(refname:strip=3)\t%(committerdate:iso8601-strict)","refs/remotes/origin")
if code:raise RuntimeError(raw)
cutoff=datetime.datetime.now(datetime.timezone.utc)-datetime.timedelta(days=3)
items=[]
for line in raw.splitlines():
 name,stamp=line.split("\t",1)
 if name in ("HEAD","master","main","develop",BASE) or name.startswith(("backup/","chore/safe-merged-branch-cleanup")):continue
 if datetime.datetime.fromisoformat(stamp)>cutoff:continue
 base="refs/remotes/origin/"+BASE
 head="refs/remotes/origin/"+name
 _,counts=git("rev-list","--left-right","--count",base+"..."+head)
 a,b=map(int,counts.split())
 if b==0:continue
 _,stat=git("diff","--shortstat",base+"..."+head)
 _,files=git("diff","--name-only",base+"..."+head)
 paths=files.splitlines()
 items.append({"branch":name,"exclusive_commits":b,"behind":a,"changed_files":len(paths),"categories":sorted(set(p.split("/")[0] for p in paths))[:8],"stat":stat})
items.sort(key=lambda x:(x["exclusive_commits"],x["changed_files"],x["branch"]))
print(json.dumps({"base":BASE,"stale_divergent":len(items),"smallest":items[:60],"notes":"read-only inventory; diff equivalence requires manual/semantic review"},indent=2))

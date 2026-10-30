#!/usr/bin/env python3
"""Read-only: find branches whose unique commits change only whitespace relative to current reference."""
import datetime,json,os,subprocess
BASE=os.environ["PRODUCTION_BRANCH"]
def git(*args):
 p=subprocess.run(["git",*args],capture_output=True,text=True)
 return p.returncode,p.stdout.strip()
_,refs=git("for-each-ref","--format=%(refname:strip=3)\t%(committerdate:iso8601-strict)","refs/remotes/origin")
cutoff=datetime.datetime.now(datetime.timezone.utc)-datetime.timedelta(days=3)
hits=[];examined=0
for row in refs.splitlines():
 name,stamp=row.split("\t",1)
 if name in ("HEAD","develop","master","main",BASE) or name.startswith(("backup/","chore/safe-merged-branch-cleanup")):continue
 if datetime.datetime.fromisoformat(stamp)>=cutoff:continue
 ref="refs/remotes/origin/"+name
 _,n=git("rev-list","--count","refs/remotes/origin/"+BASE+".."+ref)
 if not n.isdigit() or not 1<=int(n)<=8:continue
 _,paths=git("diff","--name-only","refs/remotes/origin/"+BASE+"..."+ref)
 if not paths or len(paths.splitlines())>8:continue
 examined+=1
 # Compare full file content after stripping all whitespace, not merely branch patches.
 equal=True
 for path in paths.splitlines():
  a=git("show","refs/remotes/origin/"+BASE+":"+path)
  b=git("show",ref+":"+path)
  if a[0] or b[0] or "".join(a[1].split())!="".join(b[1].split()):
   equal=False;break
 if equal:hits.append({"branch":name,"files":paths.splitlines()})
print(json.dumps({"base":BASE,"examined":examined,"whitespace_equivalent":len(hits),"candidates":hits,"deletions":0},indent=2))

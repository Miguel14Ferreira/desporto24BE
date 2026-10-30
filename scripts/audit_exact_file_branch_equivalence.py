#!/usr/bin/env python3
"""Read-only audit: find old branch commits whose changed files now match production exactly."""
import datetime,json,os,subprocess
BASE=os.environ["PRODUCTION_BRANCH"]
def git(*args):
 r=subprocess.run(["git",*args],capture_output=True,text=True)
 return r.returncode,r.stdout.strip()
_,refs=git("for-each-ref","--format=%(refname:strip=3)\t%(committerdate:iso8601-strict)","refs/remotes/origin")
cutoff=datetime.datetime.now(datetime.timezone.utc)-datetime.timedelta(days=3)
matches=[];review=[];checked=0
for line in refs.splitlines():
 name,date=line.split("\t",1)
 if name in ("HEAD","develop","master","main",BASE) or name.startswith(("backup/","chore/safe-merged-branch-cleanup")):continue
 if datetime.datetime.fromisoformat(date)>=cutoff:continue
 ref="refs/remotes/origin/"+name
 _,raw=git("rev-list","--count","refs/remotes/origin/"+BASE+".."+ref)
 n=int(raw)
 if n<1 or n>5:continue
 # Determine files affected by unique commits, not entire tree's divergence.
 _,paths=git("diff","--name-only","refs/remotes/origin/"+BASE+"..."+ref)
 paths=[p for p in paths.splitlines() if p]
 if not paths or len(paths)>6:continue
 checked+=1
 equivalent=all((lambda a,b: a[0]==0 and b[0]==0 and a[1]==b[1])(git("rev-parse","refs/remotes/origin/"+BASE+":"+p),git("rev-parse",ref+":"+p)) for p in paths)
 obj={"branch":name,"exclusive_commits":n,"files":paths}
 (matches if equivalent else review).append(obj)
print(json.dumps({"reference":BASE,"checked_small_branches":checked,"exact_file_content_matches":len(matches),"matches":matches,"needs_review":review[:25],"note":"read only: compare exact blob IDs at branch tips for files touched since merge base"},indent=2))

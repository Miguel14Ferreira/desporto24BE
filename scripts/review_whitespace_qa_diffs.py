#!/usr/bin/env python3
"""Read-only inspect exact byte differences on known whitespace-only QA branches."""
import os,subprocess,json,difflib
BASE=os.environ["PRODUCTION_BRANCH"]
names=os.environ["CANDIDATES"].split(",")
def git(*a):
 p=subprocess.run(["git",*a],text=True,capture_output=True)
 return p.returncode,p.stdout
results=[]
for name in names:
 base="refs/remotes/origin/"+BASE
 ref="refs/remotes/origin/"+name
 status,raw=git("diff","--name-only",base+"..."+ref)
 if status:continue
 files=raw.splitlines()
 changes=[]
 for path in files:
  a=git("show",base+":"+path)
  b=git("show",ref+":"+path)
  if a[0] or b[0]:continue
  lines=list(difflib.unified_diff(a[1].splitlines(keepends=True),b[1].splitlines(keepends=True),n=2))
  changes.append({"path":path,"diff":"".join(lines)[:3500]})
 results.append({"branch":name,"changes":changes})
print(json.dumps(results,indent=2))

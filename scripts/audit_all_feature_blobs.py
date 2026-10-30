#!/usr/bin/env python3
"""Read-only exact blob equivalence audit for all older fix/feature branches.
Compare every file touched since merge base with current production at tip.
"""
import datetime,json,os,subprocess
BASE=os.environ["PRODUCTION_BRANCH"]
def git(*args):
 return subprocess.run(["git",*args],capture_output=True,text=True)
refs=git("for-each-ref","--format=%(refname:strip=3)\t%(committerdate:iso8601-strict)","refs/remotes/origin")
if refs.returncode:raise RuntimeError(refs.stderr)
cutoff=datetime.datetime.now(datetime.timezone.utc)-datetime.timedelta(days=3)
match=[];unmatched=0;checked=0
for line in refs.stdout.splitlines():
 name,date=line.split("\t",1)
 if not name.startswith(("fix/","feat/","feature/","perf/","ux/","design/")) or name==BASE:continue
 if datetime.datetime.fromisoformat(date)>=cutoff:continue
 ref="refs/remotes/origin/"+name;base="refs/remotes/origin/"+BASE
 ahead=git("rev-list","--count",base+".."+ref)
 if ahead.returncode or not ahead.stdout.strip().isdigit() or int(ahead.stdout.strip())<1:continue
 diff=git("diff","--name-only",base+"..."+ref)
 if diff.returncode:continue
 paths=[p for p in diff.stdout.splitlines() if p]
 if not paths:continue
 checked+=1
 equal=True
 for p in paths:
  a=git("rev-parse",base+":"+p);b=git("rev-parse",ref+":"+p)
  if a.returncode or b.returncode or a.stdout.strip()!=b.stdout.strip():equal=False;break
 if equal:match.append({"branch":name,"files":len(paths),"commits":int(ahead.stdout.strip())})
 else:unmatched+=1
print(json.dumps({"base":BASE,"checked":checked,"exact_blob_equivalent":len(match),"unmatched":unmatched,"candidates":match},indent=2))

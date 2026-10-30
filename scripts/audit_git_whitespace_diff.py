#!/usr/bin/env python3
"""Read-only verify whitespace-equivalent branches by Git's whitespace-aware diff.
Only exact Git semantic whitespace equivalence (git diff -w), not stripping all
whitespace from strings. This excludes paths changed by whitespace inside strings.
"""
import datetime,json,os,subprocess
BASE=os.environ["PRODUCTION_BRANCH"]
def git(*args):
 return subprocess.run(["git",*args],capture_output=True,text=True)
refs=git("for-each-ref","--format=%(refname:strip=3)\t%(committerdate:iso8601-strict)","refs/remotes/origin")
if refs.returncode:raise RuntimeError(refs.stderr)
cutoff=datetime.datetime.now(datetime.timezone.utc)-datetime.timedelta(days=3)
checked=0;confirmed=[]
for line in refs.stdout.splitlines():
 name,date=line.split("\t",1)
 if name in ("HEAD","develop","master","main",BASE) or name.startswith(("backup/","chore/safe-merged-branch-cleanup")):continue
 if datetime.datetime.fromisoformat(date)>=cutoff:continue
 ref="refs/remotes/origin/"+name; base="refs/remotes/origin/"+BASE
 ahead=git("rev-list","--count",base+".."+ref)
 if ahead.returncode or not ahead.stdout.strip().isdigit() or not 1<=int(ahead.stdout.strip())<=8:continue
 changed=git("diff","--name-only",base+"..."+ref)
 if changed.returncode:continue
 paths=changed.stdout.splitlines()
 if not paths or len(paths)>8:continue
 checked+=1
 # Git diff -w compares all branch-tip file content, not only old patches.
 # Require files exist on both refs, and compare individual files to avoid
 # falsely claiming equality from cancellations across files.
 good=True
 for path in paths:
  for rev in (base,ref):
   if git("cat-file","-e",rev+":"+path).returncode:good=False;break
  if not good:break
  d=git("diff","-w","--exit-code",base,ref,"--",path)
  if d.returncode:good=False;break
 if good:confirmed.append({"branch":name,"paths":paths})
print(json.dumps({"reference":BASE,"checked":checked,"git_whitespace_equivalent":len(confirmed),"candidates":confirmed},indent=2))

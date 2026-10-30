#!/usr/bin/env python3
"""Read-only triage of stale branches for patch-equivalent commits.
Never deletes branches. Uses git cherry to recognize patches already applied,
including cherry-picks/rebases. Squash merges may still appear unmatched.
"""
import datetime, json, os, re, subprocess
PRODUCTION=os.environ.get("PRODUCTION_BRANCH","preview/render-fullstack")
MAX=int(os.environ.get("MAX_BRANCHES","800"))
CUTOFF=datetime.datetime.now(datetime.timezone.utc)-datetime.timedelta(days=3)
def git(*args):
    return subprocess.run(["git",*args],capture_output=True,text=True)
base="refs/remotes/origin/"+PRODUCTION
refs=git("for-each-ref","--format=%(refname:strip=3)\t%(committerdate:iso8601-strict)","refs/remotes/origin")
if refs.returncode: raise RuntimeError(refs.stderr)
groups={"ancestor":[],"patch_equivalent":[],"unmatched":[],"error":[]}
for raw in refs.stdout.splitlines():
    name,stamp=raw.split("\t",1)
    if name in ("HEAD","develop","master","main",PRODUCTION) or name.startswith("backup/"):continue
    if datetime.datetime.fromisoformat(stamp)>=CUTOFF:continue
    ref="refs/remotes/origin/"+name
    if git("merge-base","--is-ancestor",ref,base).returncode==0:
        groups["ancestor"].append(name)
        continue
    result=git("cherry",base,ref)
    if result.returncode:
        groups["error"].append(name)
    elif result.stdout.strip() and all(line.startswith("- ") for line in result.stdout.splitlines()):
        groups["patch_equivalent"].append(name)
    else:
        groups["unmatched"].append(name)
    if sum(map(len,groups.values()))>=MAX:break
print(json.dumps({"reference":PRODUCTION,"counts":{k:len(v) for k,v in groups.items()},"patch_equivalent_candidates":groups["patch_equivalent"][:150],"notes":"READ ONLY; patch equivalence is heuristic; do not delete without separate verification"},indent=2))

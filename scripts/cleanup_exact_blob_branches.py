#!/usr/bin/env python3
"""Remove stale exact-blob-equivalent branches, after creating recovery tags. Dry run by default."""
import datetime,json,os,subprocess,re
from urllib.request import Request,urlopen
from urllib.parse import quote
ROOT=os.environ["GITHUB_REPOSITORY"]; TOKEN=os.environ["GITHUB_TOKEN"]
BASE=os.environ["PRODUCTION_BRANCH"]; DRY=os.environ.get("DRY_RUN","true").lower()!="false"
CUTOFF=datetime.datetime.now(datetime.timezone.utc)-datetime.timedelta(days=3)
PROTECTED={"main","master","develop",BASE}
def git(*args):
    return subprocess.run(["git",*args],capture_output=True,text=True)
def api(path,method="GET",body=None):
    data=None if body is None else json.dumps(body).encode()
    req=Request("https://api.github.com"+path,data=data,method=method,headers={"Authorization":"Bearer "+TOKEN,"Accept":"application/vnd.github+json","Content-Type":"application/json"})
    with urlopen(req,timeout=25) as res:
        raw=res.read();return json.loads(raw) if raw else {}
open_pr=set()
for page in range(1,30):
    ps=api(f"/repos/{ROOT}/pulls?state=open&per_page=100&page={page}")
    open_pr.update(p["head"]["ref"] for p in ps if p.get("head",{}).get("repo") and p["head"]["repo"].get("full_name")==ROOT)
    if len(ps)<100:break
else:raise RuntimeError("PR pagination limit reached")
refs=git("for-each-ref","--format=%(refname:strip=3)\t%(committerdate:iso8601-strict)","refs/remotes/origin")
if refs.returncode:raise RuntimeError(refs.stderr)
candidates=[];removed=[];errors=[]
for line in refs.stdout.splitlines():
    name,stamp=line.split("\t",1)
    if name in PROTECTED or name in open_pr or name.startswith("backup/") or name=="HEAD" or name==os.getenv("GITHUB_REF_NAME"):continue
    if not re.match(r"^(audit|feat|feature|fix|chore|design|docs|ci|data|build|draft|refactor|test|tests|style|hotfix|cleanup)/",name):continue
    if datetime.datetime.fromisoformat(stamp)>=CUTOFF:continue
    head=git("rev-parse","refs/remotes/origin/"+name).stdout.strip()
    base="refs/remotes/origin/"+BASE
    divergent=git("rev-list","--count",base+"..refs/remotes/origin/"+name)
    if divergent.returncode or not divergent.stdout.strip().isdigit() or not 1<=int(divergent.stdout.strip())<=5:continue
    diff=git("diff","--name-only",base+"...refs/remotes/origin/"+name)
    if diff.returncode:continue
    paths=[p for p in diff.stdout.splitlines() if p]
    if not paths or len(paths)>6:continue
    def same_blob(path):
        a=git("rev-parse",base+":"+path)
        b=git("rev-parse","refs/remotes/origin/"+name+":"+path)
        return a.returncode==0 and b.returncode==0 and a.stdout.strip()==b.stdout.strip()
    if not all(same_blob(p) for p in paths):continue
    candidates.append(name)
    if DRY:continue
    try:
        # Recovery tags preserve each exact branch head; never overwrite tags.
        tag="archive/exact-blob-cleanup-20261030/"+name
        api(f"/repos/{ROOT}/git/refs","POST",{"ref":"refs/tags/"+tag,"sha":head})
        # Confirm head hasn't moved since audit, before deleting.
        current=api(f"/repos/{ROOT}/git/ref/heads/{quote(name,safe='/')}")
        if current["object"]["sha"]!=head:raise RuntimeError("branch moved")
        api(f"/repos/{ROOT}/git/refs/heads/{quote(name,safe='/')}","DELETE")
        removed.append(name)
    except Exception as e:
        errors.append({"branch":name,"error":str(e)[:200]})
print(json.dumps({"base":BASE,"dry_run":DRY,"candidates":len(candidates),"deleted":len(removed),"errors":errors[:20],"names":candidates[:200]},indent=2))
if errors:raise SystemExit(1)

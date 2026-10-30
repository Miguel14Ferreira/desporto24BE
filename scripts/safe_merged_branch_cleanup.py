#!/usr/bin/env python3
"""Delete only old branches proven to be Git ancestors of the canonical production branch."""
import os, subprocess, re, json, datetime
from urllib.request import Request, urlopen
from urllib.error import HTTPError
ROOT = os.environ["GITHUB_REPOSITORY"]
TOKEN = os.environ["GITHUB_TOKEN"]
PRODUCTION = os.environ["PRODUCTION_BRANCH"]
CUTOFF = datetime.datetime.now(datetime.timezone.utc) - datetime.timedelta(days=int(os.environ.get("MIN_AGE_DAYS", "3")))
PROTECT = {"main", "master", "develop", PRODUCTION}
ALLOWED = re.compile(r"^(audit|feat|feature|fix|chore|design|docs|ci|data|build|draft|refactor|test|tests|style|hotfix|cleanup)/")
def api(path, method="GET"):
    req = Request("https://api.github.com" + path, method=method, headers={"Authorization": "Bearer " + TOKEN, "Accept": "application/vnd.github+json", "X-GitHub-Api-Version": "2022-11-28"})
    try:
        with urlopen(req, timeout=20) as res:
            return json.loads(res.read().decode() or "{}")
    except HTTPError as e:
        print(f"HTTP {e.code} for {method} {path}: {e.read().decode()[:400]}")
        return None
def git(*args):
    return subprocess.run(["git", *args], capture_output=True, text=True)
open_heads = set()
for page in range(1, 30):
    prs = api(f"/repos/{ROOT}/pulls?state=open&per_page=100&page={page}")
    if prs is None: raise RuntimeError("Could not list open pull requests; abort")
    open_heads.update(p["head"]["ref"] for p in prs if p["head"]["repo"] and p["head"]["repo"]["full_name"] == ROOT)
    if len(prs) < 100: break
else: raise RuntimeError("Unexpected PR pagination; abort")
res = git("for-each-ref", "--format=%(refname:strip=3)\t%(committerdate:iso8601-strict)", "refs/remotes/origin")
if res.returncode: raise RuntimeError(res.stderr)
deleted, skipped, candidates = [], [], []
for line in res.stdout.splitlines():
    name, date = line.split("\t", 1)
    if name in PROTECT or name in open_heads or not ALLOWED.match(name) or name.startswith("backup/") or name == "HEAD":
        continue
    if name == os.environ.get("GITHUB_REF_NAME"): continue
    stamp = datetime.datetime.fromisoformat(date)
    if stamp >= CUTOFF: continue
    if git("merge-base", "--is-ancestor", "refs/remotes/origin/" + name, "refs/remotes/origin/" + PRODUCTION).returncode:
        continue
    candidates.append(name)
    if os.environ.get("DRY_RUN", "true").lower() != "false": continue
    from urllib.parse import quote
    result = api(f"/repos/{ROOT}/git/refs/heads/{quote(name, safe='/')}", "DELETE")
    if result is None:
        # Successful GitHub delete returns 204 with empty body; check remote again later.
        deleted.append(name)
print(json.dumps({"repository": ROOT, "production": PRODUCTION, "dry_run": os.environ.get("DRY_RUN", "true"), "candidates":len(candidates), "deleted_attempts":len(deleted), "candidate_names":candidates[:150]}, indent=2))

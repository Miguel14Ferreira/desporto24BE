#!/usr/bin/env python3
"""Delete only old branches proven to be Git ancestors of the canonical production branch."""
import os, subprocess, re, json, datetime
import urllib.request
from urllib.request import Request
from urllib.parse import quote
from urllib.error import HTTPError, URLError
ROOT = os.environ["GITHUB_REPOSITORY"]
TOKEN = os.environ["GITHUB_TOKEN"]
PRODUCTION = os.environ["PRODUCTION_BRANCH"]
CUTOFF = datetime.datetime.now(datetime.timezone.utc) - datetime.timedelta(days=int(os.environ.get("MIN_AGE_DAYS", "3")))
PROTECT = {"main", "master", "develop", PRODUCTION, "feature/desporto24-preview-deploy"}
ALLOWED = re.compile(r"^(audit|feat|feature|fix|chore|design|docs|ci|data|build|draft|refactor|test|tests|style|hotfix|cleanup)/")
def api(path, method="GET"):
    req = Request("https://api.github.com" + path, method=method, headers={"Authorization": "Bearer " + TOKEN, "Accept": "application/vnd.github+json", "X-GitHub-Api-Version": "2022-11-28"})
    try:
        with urllib.request.urlopen(req, timeout=20) as res:
            body = res.read().decode()
            return {"status": res.status, "data": json.loads(body) if body else None}
    except (HTTPError, URLError) as e:
        raise RuntimeError(f"GitHub API failed for {method} {path}: {e}") from e
def git(*args):
    return subprocess.run(["git", *args], capture_output=True, text=True)
open_heads = set()
for page in range(1, 30):
    prs = api(f"/repos/{ROOT}/pulls?state=open&per_page=100&page={page}")
    if prs["status"] != 200: raise RuntimeError("Unexpected pull request response; abort")
    prs = prs["data"]
    open_heads.update(p["head"]["ref"] for p in prs if p["head"]["repo"] and p["head"]["repo"]["full_name"] == ROOT)
    if len(prs) < 100: break
else: raise RuntimeError("Unexpected PR pagination; abort")
res = git("for-each-ref", "--format=%(refname:strip=3)\t%(committerdate:iso8601-strict)", "refs/remotes/origin")
if res.returncode: raise RuntimeError(res.stderr)
deleted, skipped, candidates, content_equivalent = [], [], [], []
for line in res.stdout.splitlines():
    name, date = line.split("\t", 1)
    if name in PROTECT or name in open_heads or not ALLOWED.match(name) or name.startswith("backup/") or name == "HEAD":
        continue
    if name == os.environ.get("GITHUB_REF_NAME"): continue
    stamp = datetime.datetime.fromisoformat(date)
    if stamp >= CUTOFF: continue
    if git("merge-base", "--is-ancestor", "refs/remotes/origin/" + name, "refs/remotes/origin/" + PRODUCTION).returncode:
        same_tree = git("diff", "--quiet", "refs/remotes/origin/" + name, "refs/remotes/origin/" + PRODUCTION)
        if same_tree.returncode == 0:
            content_equivalent.append(name)
        elif same_tree.returncode != 1:
            raise RuntimeError(f"Cannot compare trees safely: {name}: {same_tree.stderr}")
        continue
    candidates.append(name)
    if os.environ.get("DRY_RUN", "true").lower() != "false": continue
    local_sha = git("rev-parse", "refs/remotes/origin/" + name)
    if local_sha.returncode:
        raise RuntimeError(f"Cannot resolve candidate SHA: {name}")
    encoded_name = quote(name, safe="/")
    remote_ref = api(f"/repos/{ROOT}/git/ref/heads/{encoded_name}")
    if remote_ref["status"] != 200 or remote_ref["data"]["object"]["sha"] != local_sha.stdout.strip():
        raise RuntimeError(f"Candidate changed remotely; aborting: {name}")
    result = api(f"/repos/{ROOT}/git/refs/heads/{encoded_name}", "DELETE")
    if result["status"] != 204:
        raise RuntimeError(f"Unexpected branch deletion status: {result['status']} for {name}")
    deleted.append(name)
print(json.dumps({"repository": ROOT, "production": PRODUCTION, "dry_run": os.environ.get("DRY_RUN", "true"), "candidates":len(candidates), "deleted_attempts":len(deleted), "candidate_names":candidates[:150], "content_equivalent_read_only":content_equivalent[:150], "content_equivalent_count":len(content_equivalent)}, indent=2))

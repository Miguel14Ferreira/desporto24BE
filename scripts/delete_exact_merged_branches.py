#!/usr/bin/env python3
"""One-off, fail-closed removal of an exact allowlist of merged branches."""
import os
import sys
import json
import urllib.request
import urllib.error

OWNER = "Miguel14Ferreira"
REPO = "desporto24BE"
ALLOWLIST = [
    "fix/account-security-notice-preference-visibility-20261028",
    "fix/braganca-legacy-pavilion-dedup-20261028",
    "fix/community-guest-existing-account-20261027",
    "fix/braganca-preflight-atomic-report-20261028"
]
PROTECTED = {"develop", "master", "main", "preview/render-fullstack", "feature/desporto24-preview-deploy"}

def github(method, path, token, body=None):
    request = urllib.request.Request(
        "https://api.github.com/repos/%s/%s/%s" % (OWNER, REPO, path),
        data=json.dumps(body).encode() if body is not None else None,
        headers={"Authorization": "Bearer " + token,
                 "Accept": "application/vnd.github+json",
                 "X-GitHub-Api-Version": "2022-11-28"},
        method=method,
    )
    with urllib.request.urlopen(request, timeout=20) as response:
        data = response.read()
        return response.status, json.loads(data) if data else None

def main():
    token = os.getenv("GH_TOKEN", "")
    confirmation = os.getenv("CONFIRM_DELETE", "")
    if not token:
        raise RuntimeError("GH_TOKEN required")
    if any(x in PROTECTED or x.startswith(("backup/", "release/")) for x in ALLOWLIST):
        raise RuntimeError("Protected branch in allowlist")
    if len(ALLOWLIST) != len(set(ALLOWLIST)):
        raise RuntimeError("Duplicate allowlist entry")
    base_status, base = github("GET", "git/ref/heads/develop", token)
    if base_status != 200:
        raise RuntimeError("Cannot read develop")
    for branch in ALLOWLIST:
        refpath = "git/ref/heads/" + branch
        try:
            _, before = github("GET", refpath, token)
        except urllib.error.HTTPError as error:
            if error.code == 404:
                print("MISSING", branch)
                continue
            raise
        sha = before["object"]["sha"]
        # Native compare uses the commit DAG, so 'behind' or 'identical' means
        # the exact branch tip is an ancestor of the inspected develop.
        _, comparison = github("GET", "compare/" + base["object"]["sha"] + "..." + sha, token)
        if comparison["ahead_by"] != 0 or comparison["status"] not in ("behind", "identical"):
            raise RuntimeError("Branch not an ancestor of develop: " + branch)
        # Reject active PR heads regardless of their base.
        _, prs = github("GET", "pulls?state=open&head=" + OWNER + ":" + branch.replace("/", "%2F") + "&per_page=100", token)
        if prs:
            raise RuntimeError("Open PR references branch: " + branch)
        _, current = github("GET", refpath, token)
        if current["object"]["sha"] != sha:
            raise RuntimeError("Branch tip changed: " + branch)
        if confirmation != "DELETE_EXACT_ALLOWLIST":
            print("DRY_RUN", branch, sha)
            continue
        # Protection/rules remain enforced by GitHub. No force option.
        try:
            status, _ = github("DELETE", refpath, token)
            if status != 204:
                raise RuntimeError("Unexpected delete status %s for %s" % (status, branch))
            print("DELETED", branch, sha)
        except urllib.error.HTTPError:
            print("DELETE_REJECTED", branch)
            raise

if __name__ == "__main__":
    main()

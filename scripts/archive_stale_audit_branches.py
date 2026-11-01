#!/usr/bin/env python3
"""Archive SHA-pinned, obsolete audit branches before deletion; fail closed."""
import datetime
import json
import os
import urllib.error
import urllib.parse
import urllib.request

OWNER = "Miguel14Ferreira"
REPO = "desporto24BE"
EXPECTED = {
    "analysis/live-venues-readonly-20261020": "56a0ca816ebf81b82d8bcdc5be76e1aa11f86511",
    "analysis/national-venue-source-staging-20261020": "cd713cf0f841da147550c8fef8651c302b2a8de9",
    "analysis/snid-national-coverage-20261020": "03b9de3ed485f8649278b019266327c477de75b5",
    "audit/ci-test-contracts-20261019": "550877122b13a4fb5ead925abd0b7c48722b47dd",
    "audit/osm-address-quality-20261018": "8227d693d57bea10636ea9621c5c9af7c712ee84",
    "audit/osm-import-safety-report-20261018": "e2d055d423cf4e7e2f58d3fdb61a180fa9948d6b",
    "audit/osm-offline-fixture-dry-run-20261018": "f3da54baaa2ed899a69ea960d2fb22f201b44d0c",
    "audit/osm-verified-district-20261018": "f60193349bf79cbeee94d2051e83c2c387b54fc5",
    "audit/repair-backend-regression-tests-20261019": "518d6561af96cf98c5a7de02b0239b0b3ca25db9",
    "audit/repair-existing-java-tests-20261019": "d1bd5f7e129ca819800ff0438dba1d46fc7dc05e",
    "audit/venue-district-coverage-report-20261018": "d174dde8f940a6ee425e616eed595cbe1c92b6d1",
    "audit/venue-national-review-plan-20261018": "f515b47568d4e265c16fffc22e7ffd1b8ffdaf9e",
}
ARCHIVE_PREFIX = "archive/stale-audit-20261101/"
MIN_AGE_DAYS = 7

def api(method, path, token, body=None):
    req = urllib.request.Request(
        "https://api.github.com/repos/%s/%s/%s" % (OWNER, REPO, path),
        data=json.dumps(body).encode("utf-8") if body is not None else None,
        headers={"Authorization": "Bearer " + token,
                 "Accept": "application/vnd.github+json",
                 "X-GitHub-Api-Version": "2022-11-28"}, method=method)
    try:
        with urllib.request.urlopen(req, timeout=30) as response:
            raw = response.read()
            return response.status, json.loads(raw) if raw else None
    except urllib.error.HTTPError as error:
        if error.code == 404:
            return 404, None
        raise

def ensure_branch_safe(name, sha, token):
    if not name.startswith(("audit/", "analysis/")):
        raise RuntimeError("Not an approved obsolete-audit namespace: " + name)
    if len(sha) != 40 or any(c not in "0123456789abcdef" for c in sha):
        raise RuntimeError("Invalid pinned SHA: " + name)
    ref = "git/ref/heads/" + urllib.parse.quote(name, safe="/")
    status, current = api("GET", ref, token)
    if status == 404:
        print("MISSING", name)
        return None
    if status != 200 or current["object"]["sha"] != sha:
        raise RuntimeError("Branch SHA mismatch; refuse deletion: " + name)
    _, prs = api("GET", "pulls?state=open&head=" + urllib.parse.quote(OWNER + ":" + name, safe="") + "&per_page=100", token)
    if prs:
        print("SKIP_OPEN_PR", name)
        return None
    status, commit = api("GET", "commits/" + sha, token)
    if status != 200:
        raise RuntimeError("Cannot verify commit date: " + name)
    stamp = datetime.datetime.fromisoformat(commit["commit"]["committer"]["date"].replace("Z", "+00:00"))
    if datetime.datetime.now(datetime.timezone.utc) - stamp < datetime.timedelta(days=MIN_AGE_DAYS):
        print("SKIP_RECENT", name)
        return None
    return ref

def archive_and_delete(name, sha, token):
    tag_path = ARCHIVE_PREFIX + name
    tag_ref = "git/ref/tags/" + urllib.parse.quote(tag_path, safe="/")
    status, existing = api("GET", tag_ref, token)
    if status == 404:
        status, _ = api("POST", "git/refs", token, {"ref": "refs/tags/" + tag_path, "sha": sha})
        if status != 201:
            raise RuntimeError("Could not create recovery tag: " + name)
    elif status != 200 or existing["object"]["sha"] != sha:
        raise RuntimeError("Recovery tag collision: " + name)
    status, verified = api("GET", tag_ref, token)
    if status != 200 or verified["object"]["sha"] != sha:
        raise RuntimeError("Recovery tag verification failed: " + name)
    if ensure_branch_safe(name, sha, token) is None:
        print("SKIP_CHANGED_AFTER_ARCHIVE", name)
        return
    status, _ = api("DELETE", "git/refs/heads/" + urllib.parse.quote(name, safe="/"), token)
    if status != 204:
        raise RuntimeError("GitHub refused deletion: " + name)
    print("DELETED", name, sha, "RECOVERY_TAG", tag_path)

def main():
    token = os.environ.get("GH_TOKEN")
    if not token:
        raise RuntimeError("GH_TOKEN required")
    delete = os.environ.get("CONFIRM_DELETE") == "ARCHIVE_AND_DELETE_EXACT_AUDITS"
    for name, sha in EXPECTED.items():
        if ensure_branch_safe(name, sha, token) is None:
            continue
        if delete:
            archive_and_delete(name, sha, token)
        else:
            print("DRY_RUN", name, sha)

if __name__ == "__main__":
    main()

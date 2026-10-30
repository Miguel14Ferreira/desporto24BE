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
    "fix/direct-call-pending-invite-recovery-20261028",
    "fix/session-meeting-point-notification-db-constraint-20261028",
    "fix/venue-remove-startup-import-20261028",
    "fix/venue-street-from-photon-20261027",
    "feat/community-session-creation-policy-20261028",
    "feat/confirmed-guest-private-meeting-point-20261028",
    "feat/conversation-notification-mutes-20261028",
    "feat/guest-receipt-session-chat-20261029",
    "feat/guest-self-cancel-reservation-20261028",
    "feat/session-meeting-point-change-notifications-20261028",
    "feat/session-meeting-point-edit-20261028",
    "feat/session-minimum-players-20261029",
    "feat/session-roster-profile-photo-20261028",
    "feat/session-private-meeting-instructions-20261028",
    "feat/signed-guest-confirmed-roster-20261028",
    "feat/unified-session-create-communities-20261027",
    "feat/chat-media-notification-previews-20261028",
    "feat/national-venue-coverage-offline-plan-20261028",
    "feat/braganca-internal-readonly-preflight-20261028",
    "integration/auth-security-20261020",
    "integration/validated-offline-venues-20261020",
    "integration/validated-preview-merge-20261020",
    "qa/community-role-matrix-20261020",
    "qa/sprint-two-up-official-decision-regression-20261025",
    "qa/track-sprint-authenticated-pg16-20261025",
    "qa/track-sprint-chromium-real-backend-20261025",
    "qa/track-sprint-pg16-integrity-20261025",
    "qa/venue-concurrent-admin-decisions-20261025",
    "qa/venue-moderation-real-http-pg16-20261025",
    "test/account-lock-recovery-isolated-http-20261028",
    "test/braganca-synthetic-pg16-readonly-preflight-20261028",
    "test/guest-balance-ten-players-20261028",
    "validation/be-auth-integration-20261020",
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
        # Archive exact historical tip before destructive action; fail closed if tag exists.
        recovery = "git/refs"
        tag = "refs/tags/archive/allowlisted-merged-20261030/" + branch
        try:
            archived_status, _ = github("POST", recovery, token, {"ref": tag, "sha": sha})
            if archived_status != 201:
                raise RuntimeError("Recovery ref was not created: " + branch)
        except urllib.error.HTTPError as error:
            if error.code != 422:
                raise
            _, archived = github("GET", "git/ref/tags/archive/allowlisted-merged-20261030/" + branch, token)
            if archived["object"]["sha"] != sha:
                raise RuntimeError("Recovery tag exists with different SHA: " + branch)
        # Check the tip once more after the archive write.
        _, final_ref = github("GET", refpath, token)
        if final_ref["object"]["sha"] != sha:
            raise RuntimeError("Branch moved after recovery tag: " + branch)
        # GitHub branch protections remain enforced; never force the deletion.
        try:
            status, _ = github("DELETE", "git/refs/heads/" + branch, token)
            if status != 204:
                raise RuntimeError("Unexpected delete status %s for %s" % (status, branch))
            print("DELETED", branch, sha)
        except urllib.error.HTTPError:
            print("DELETE_REJECTED", branch)
            raise

if __name__ == "__main__":
    main()

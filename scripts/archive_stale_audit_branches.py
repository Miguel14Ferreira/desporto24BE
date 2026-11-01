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
    "audit/national-18-live-public-count-20261022": "90671fc04db5737900a128eed181071a79fee6c8",
    "audit/venues-national-live-public-reconcile-20261021": "21ed8dcf89fd5ca4ee057d328163005f4ee76ff7",
}
EXPECTED.update({
    "chore/allowlisted-be-two-merged-20261101": "7c61de274ef0ceafc95169b8044bc34f407cd44f",
    "chore/archive-stale-audits-be-20261101": "608c0ec08e6a129cd37aac9db49d7dc2d71e852c",
    "chore/be-cleanup-next-integrated-20261030": "27a40d46e3a011c25e3fc2591d8283633cad24bf",
    "chore/be-integrated-qa-cleanup-20261030": "538a7be332bf03845aef497758caa25b9a476bc9",
    "chore/be-next-verified-cleanup-20261030": "936592ad99e7bf09740321bb2b8b360b0ed1b160",
    "chore/cleanup-verified-batch2-20261030": "df28e3a30d01400824fd72f5d4aa138152aa3379",
    "chore/expand-verified-branch-allowlist-20261030": "334ea330b3f803c56589878e1b09c0eabade6bf3",
    "chore/extend-be-verified-cleanup-20261030": "71c1af3711a31c34334e6c3753bc798e0218c23e",
    "chore/verified-release-cleanup-20261030": "0c6f585f93e8cb6b7b725c4890fea89aa1b0a132",
})
EXPECTED.update({
    "chore/allowlisted-branch-removal-20261030": "fd7653c16304130f10c202253369e6f29b377949",
    "chore/consolidated-cleanup-audit-20261030": "bcc6893645e01d46c00387f4c5f98365d99d2c7c",
    "chore/content-equivalence-audit-20261030": "f771a1fdaf510908f961e0d9d4c8c293a945d09e",
    "chore/recoverable-manual-branch-cleanup-20261030": "56f0d661e3b64f1a0c9516e51d8b93e265abf215",
})
EXPECTED.update({
    "release/community-group-knockout-live-20261022": "b7dccf142f2c11d77d5d20be9b84c03aedbad4ca",
    "release/jynlo-gps-only-address-20261021": "38ca928e6bdc765bc876ccb1af4d60316366f9b9",
    "release/national-18-map-live-safe-20261022": "06efabb38b1e4fb4b0db2581a86d5500f398bd15",
    "release/organizer-fair-play-exclusions-live-safe-20261022": "83e5958dc75db5872bf8693f71966bc57216d5c0",
    "release/postgame-player-compliments-live-safe-20261022": "b6365b9c62b3661ffbb4b10950e1f44138045d33",
    "release/postgame-venue-stars-live-safe-20261022": "48f15c53663f3e884a01cccbed753911443ce381",
    "release/venue-data-37-isolated-20261021": "dd3f3054cc8ad1c0373cf8a4c3612b2e060484df",
    "release/venues-leiria-setubal-braganca-20261021": "6ff2051667789f492f467d1f0daa835686e02c41",
})
EXPECTED.update({
    "fix/allowlisted-delete-ref-path-20261030": "a961d5926a4ccb63d1fb04edf7e27aa9da9b9df9",
    "fix/community-cancelled-sessions-20261028": "b52730bce1e44e9ec8e7997585ce6589e1f0ac29",
    "fix/guest-instant-confirmed-attendance-20261028": "f1bb2ba6d42cccdd059a5e85397265372da0a99f",
    "fix/season-game-single-community-20261028": "22305cd2cfa42776dc9829cf6551c1d2dec2d073",
    "fix/session-invite-private-community-20261029": "55ed6ddb4f8cad879c3319ea80586b34d0381470",
    "fix/session-preset-objectmapper-runtime-20261028": "84e4e69b4fcb723e2717c09a670cb341fe31b663",
})
EXPECTED.update({
    "chore/archive-merged-release-branches-be-20261101": "48e1de3d5050ab62d98f25f0d39de4bee6442fac",
    "chore/archive-national-audit-branches-be-20261101": "12ffc8035c6c6bd8eb5ab321afb5a41c7c47e8c1",
    "chore/archive-six-merged-fixes-be-20261101": "db2f7d89bcc85e2eea2717b9601a3b875201d0f2",
    "feat/admin-gps-braga-rodovia-fields-288-289-20261027": "24bab04735bcf26ee8931672ba995c5a82e149d7",
    "feat/admin-gps-evora-official-map-link-20261027": "ca9ba241e0c08b777a4d8efcb4bc2a42992cd762",
    "feat/admin-gps-facility-context-285-286-20261027": "07d979cfc4941ae9c35e6139f9fc887fbf599b6b",
    "feat/admin-gps-outeiro-283-curated-20261027": "5f3f2a153f51872f65cbd214840b50c7d8744260",
    "feat/admin-gps-rodovia-290-294-context-20261027": "da3490781cb6467df6b67fa519946d6911f6e0af",
    "feat/admin-gps-rodovia-map-crosscheck-20261027": "c0af8eb26dd39427ca2036e22f9d11d59e03ae18",
    "feat/admin-gps-rodovia-numbered-sign-afb-20261027": "dcff0b0df6a1708ac79415c6ef1bb3f167d22921",
    "feat/admin-gps-rodovia-tree-coordinate-geosig-20261027": "004506b2b9e7127dd59e4323cdd06284f46991fe",
    "feat/community-default-venue-20261028": "3d1617326331efcbdb11c9bf64ba8c917333b8b3",
    "feat/community-session-quick-results-20261027": "19fc9a7477504f0d7e0b06c7d842e926b77ec50a",
    "feat/guest-session-invites-20261027": "e47f7064e788ab9315909e05f921014c1ec1e132",
})
EXPECTED.update({
    "feat/community-habitual-location-3-methods-20261028": "05809e9ac13bfc30b7693f73b10ec6f89fde7319",
    "feat/jynlo-connect-group-invites-20261030": "b9a3f339d6fb962cba424c37ee3d1f0ad554c9c3",
    "feat/member-only-multi-community-sessions-20261028": "6743b7c6bf51c0e9e24338d50dad9cf742368298",
    "feat/post-game-community-join-20261028": "7767d1d8800f3b49565269787154ef1bf229e491",
    "feat/session-invite-flow-context-20261028": "9c48c925a6fd730453bb5b80b8c86e2072a9c4a0",
    "feat/session-season-games-20261028": "d487cc9cdd91c853889202d88f2b5b316c89c605",
    "feat/user-session-presets-api-20261028": "9a49f7c4aec784d6b77a73bb59c8c1b62fa7b1c7",
    "fix/venue-pilot-official-website-provenance-20261021": "098404f4300513fecb5b7b98f23544cc44de7488",
    "fix/venue-response-district-20261020": "ba62cffb48ff34669db774a68b34d3394010cc72",
    "fix/venue-suggestion-jackson2-injection-20261025": "76787945acd1ca2ca98ef63a9d645eb5672c5e56",
})

EXPECTED.update({
    "chore/archive-10-more-merged-feat-fix-be-20261101": "1061bc878b25337653785b621dbe08711d82450d",
    "chore/archive-14-verified-merged-be-20261101": "933e83bc2ef21ae1765ba5073c3c259b2a423584",
    "chore/archive-workflow-confirmation-choice-be-20261101": "788e957fc8c0b17b9f2a32728e4a9d11c710e7c2",
    "chore/pin-archive-workflow-sha-be-20261101": "b0d4fcf3a095a2601d6967cafc2a6ff2a9c61467",
})
FEAT_ARCHIVE_PREFIX = "archive/verified-merged-feat-20261101/"
FIX_ARCHIVE_PREFIX = "archive/merged-fix-20261101/"
RELEASE_ARCHIVE_PREFIX = "archive/merged-release-20261101/"
EXPECTED.update({
    "chore/next-safe-cleanup-be-20261101": "1effa7966f628d93016a3fd50f0e71b8fc01cc1e",
})
EXPECTED.update({
    "chore/archive-next-cleanup-be-20261101": "dc1a792b139d6788ea598124acb112e47a4d087a",
})
EXPECTED.update({
    "chore/archive-batch-be-20261101": "4167f95b6d5a7b6fe002d0d05eb9a6de3f943f02",
    "chore/bulk-merged-branch-audit-be-20261101": "39fe12fa51c05e5ed759ed701f0846ee3f6137b1",
    "fix/venues-directory-39-per-page-20261026": "a237c8c76b406b9bed3d84fc5032936fe3dddec7",
})
CHORE_ARCHIVE_PREFIX = "archive/cleanup-scaffolding-20261101/"
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
    if not name.startswith(("audit/", "analysis/", "chore/", "release/", "fix/", "feat/")):
        raise RuntimeError("Not an approved obsolete-audit namespace: " + name)
    if name.startswith("chore/") and name not in EXPECTED:
        raise RuntimeError("Unpinned chore branch: " + name)
    if name.startswith("release/") and name not in EXPECTED:
        raise RuntimeError("Unpinned release branch: " + name)
    if name.startswith("fix/") and name not in EXPECTED:
        raise RuntimeError("Unpinned fix branch: " + name)
    if name.startswith("feat/") and name not in EXPECTED:
        raise RuntimeError("Unpinned feat branch: " + name)
    if len(sha) != 40 or any(c not in "0123456789abcdef" for c in sha):
        raise RuntimeError("Invalid pinned SHA: " + name)
    ref = "git/ref/heads/" + urllib.parse.quote(name, safe="/")
    status, current = api("GET", ref, token)
    if status == 404:
        print("MISSING", name)
        return None
    if status != 200 or current["object"]["sha"] != sha:
        raise RuntimeError("Branch SHA mismatch; refuse deletion: " + name)
    pr_status, prs = api("GET", "pulls?state=open&head=" + urllib.parse.quote(OWNER + ":" + name, safe="") + "&per_page=100", token)
    if pr_status != 200 or not isinstance(prs, list):
        raise RuntimeError("Cannot verify open PRs: " + name)
    if prs:
        print("SKIP_OPEN_PR", name)
        return None
    status, commit = api("GET", "commits/" + sha, token)
    if status != 200:
        raise RuntimeError("Cannot verify commit date: " + name)
    stamp = datetime.datetime.fromisoformat(commit["commit"]["committer"]["date"].replace("Z", "+00:00"))
    if datetime.datetime.now(datetime.timezone.utc) - stamp < datetime.timedelta(days=0 if name.startswith(("chore/", "fix/", "feat/")) else MIN_AGE_DAYS):
        print("SKIP_RECENT", name)
        return None
    if name.startswith(("release/", "fix/", "feat/", "chore/")):
        closed_status, closed_prs = api("GET", "pulls?state=closed&head=" + urllib.parse.quote(OWNER + ":" + name, safe="") + "&per_page=100", token)
        if closed_status != 200 or not isinstance(closed_prs, list):
            raise RuntimeError("Cannot verify merged PR: " + name)
        matching = [p for p in closed_prs if p.get("merged_at") and p.get("head", {}).get("sha") == sha and p.get("merge_commit_sha")]
        if not matching:
            raise RuntimeError("No merged PR with exact %s head SHA: %s" % ("release" if name.startswith("release/") else "fix" if name.startswith("fix/") else "feat" if name.startswith("feat/") else "chore", name))
        merged_into_develop = False
        for pr in matching:
            cmp_status, cmp = api("GET", "compare/develop..." + pr["merge_commit_sha"], token)
            if cmp_status == 200 and cmp.get("ahead_by") == 0 and cmp.get("status") in ("behind", "identical"):
                merged_into_develop = True
                break
        if not merged_into_develop:
            raise RuntimeError("Merged PR commit is not an ancestor of develop: " + name)
    return ref

def archive_and_delete(name, sha, token):
    tag_path = (FEAT_ARCHIVE_PREFIX if name.startswith("feat/") else FIX_ARCHIVE_PREFIX if name.startswith("fix/") else RELEASE_ARCHIVE_PREFIX if name.startswith("release/") else CHORE_ARCHIVE_PREFIX if name.startswith("chore/") else ARCHIVE_PREFIX) + name
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


# Discover only exact merged PR heads that are already ancestors of develop.
# This is an audit report, never an implicit deletion allowlist.
def discover_merged_candidates(token):
    protected = {"develop", "master", "feature/desporto24-preview-deploy"}
    page = 1
    found = []
    while True:
        status, branches = api("GET", "branches?per_page=100&page=" + str(page), token)
        if status != 200 or not isinstance(branches, list):
            raise RuntimeError("Cannot enumerate branches")
        for branch in branches:
            name = branch["name"]
            if name in protected or name.startswith(("backup/", "archive/")) or name in EXPECTED:
                continue
            if not name.startswith(("chore/", "feat/", "fix/", "release/")):
                continue
            sha = branch["commit"]["sha"]
            status, prs = api("GET", "pulls?state=closed&head=" + urllib.parse.quote(OWNER + ":" + name, safe="") + "&per_page=100", token)
            if status != 200 or not isinstance(prs, list):
                raise RuntimeError("Cannot verify PRs for " + name)
            for pr in prs:
                if not (pr.get("merged_at") and pr.get("head", {}).get("sha") == sha and pr.get("merge_commit_sha")):
                    continue
                status, comparison = api("GET", "compare/develop..." + pr["merge_commit_sha"], token)
                if status == 200 and comparison.get("ahead_by") == 0 and comparison.get("status") in ("behind", "identical"):
                    found.append((name, sha, pr["number"]))
                    break
        if len(branches) < 100:
            break
        page += 1
    for name, sha, number in sorted(found):
        print("AUDIT_CANDIDATE", name, sha, "MERGED_PR", number)
    print("AUDIT_CANDIDATES_TOTAL", len(found))

def main():
    token = os.environ.get("GH_TOKEN")
    if not token:
        raise RuntimeError("GH_TOKEN required")
    if os.environ.get("DISCOVER_MERGED_CANDIDATES") == "1":
        discover_merged_candidates(token)
        return
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

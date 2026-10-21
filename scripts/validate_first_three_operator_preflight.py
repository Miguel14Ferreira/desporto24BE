#!/usr/bin/env python3
"""Validate owner-supplied LIVE read-only venue preflight, without granting approvals."""
import json
from pathlib import Path

from finalize_official_venue_pilot import fingerprint, load_decisions, load_pilot

ROOT = Path(__file__).resolve().parent.parent
EVIDENCE = ROOT / "docs/venue-imports/first-three-operator-preflight-result-20261022.json"
PROPOSALS = ROOT / "docs/venue-imports/first-three-proposals-unapproved-20261021.jsonl"
DECISIONS = ROOT / "docs/venue-imports/first-three-decisions-pending-20261021.json"
EXPECTED_QUERY = "src/main/resources/db/changelog/venue-imports/preflight-first-three-municipal-live-READONLY.sql"


def validate_evidence(document, proposals, decisions):
    if not isinstance(document, dict) or document.get("schemaVersion") != 1:
        raise ValueError("Unsupported operator preflight evidence")
    if document.get("evidenceType") != "operator_supplied_readonly_psql_output":
        raise ValueError("Preflight provenance must remain owner supplied")
    if document.get("queryPath") != EXPECTED_QUERY:
        raise ValueError("Unexpected SQL preflight query")
    if document.get("executionDateVerified") is not False or document.get("executedAt") is not None:
        raise ValueError("Timestamp cannot be invented from a pasted psql output")
    if document.get("allProposalsApproved") is not False:
        raise ValueError("Read-only output is not a release approval")
    total = document.get("catalogueRows")
    if isinstance(total, bool) or not isinstance(total, int) or total < 0:
        raise ValueError("Invalid observed catalogue row count")
    observations = document.get("records")
    if not isinstance(observations, list) or len(observations) != 3:
        raise ValueError("Expected exactly three independent preflight records")
    refs = [row.get("sourceReference") for row in observations if isinstance(row, dict)]
    if len(refs) != 3 or len(set(refs)) != 3 or set(refs) != set(proposals):
        raise ValueError("Preflight refs do not match immutable municipal pilot")
    review = {}
    for row in observations:
        ref = row["sourceReference"]
        p, d = proposals[ref], decisions[ref]
        if (row.get("name") != p["name"] or row.get("city") != p["city"]
                or d.get("candidateFingerprint") != fingerprint(p)):
            raise ValueError("Preflight candidates are not signed to the proposed identities")
        if row.get("approvalDecision") != "PENDING" or d.get("decision") != "PENDING":
            raise ValueError("Operator-supplied evidence cannot approve imports")
        collision = row.get("potentialCollisionCount")
        neighbors = row.get("nearby100To150MetresCount")
        matches = row.get("potentialMatches")
        if any(isinstance(x, bool) or not isinstance(x, int) or x < 0
               for x in (collision, neighbors)):
            raise ValueError("Invalid preflight count")
        if not isinstance(matches, list) or len(matches) != collision + neighbors:
            raise ValueError("Preflight counts disagree with detailed matches")
        seen = set()
        blockers, advisories = 0, 0
        for m in matches:
            if not isinstance(m, dict):
                raise ValueError("Invalid preflight neighbor")
            identity = m.get("id")
            if not isinstance(identity, int) or isinstance(identity, bool) or identity < 1 or identity in seen:
                raise ValueError("Duplicate or invalid existing venue ID")
            seen.add(identity)
            radius = m.get("distanceMetres")
            if radius is not None and (isinstance(radius, bool) or not isinstance(radius, (int,float)) or radius < 0):
                raise ValueError("Invalid matched venue distance")
            exact = m.get("sameRef") is True
            alias = m.get("sameNameOrAliasCity") is True
            near = m.get("within100m") is True
            advisory = m.get("neighbor100to150m") is True
            if exact or alias or near:
                blockers += 1
                if advisory:
                    raise ValueError("Duplicate cannot be classified only as an advisory neighbor")
            elif advisory:
                if radius is None or not (100 < radius <= 150):
                    raise ValueError("Advisory distance is outside 100-150m")
                advisories += 1
            else:
                raise ValueError("Match without collision or neighbor classification")
        if blockers != collision or advisories != neighbors:
            raise ValueError("Preflight match classifications disagree with counts")
        review[ref] = {
            "name": p["name"], "potentialCollisionCount": collision,
            "nearby100To150MetresCount": neighbors,
            "technicalStatus": "COLLISION_REVIEW_REQUIRED" if collision else "NO_COLLISION_BY_QUERY_CRITERIA",
            "publicationStatus": "PENDING_NAMED_REVIEW_AND_CURRENT_ACCESS_VALIDATION",
        }
    return review


def main():
    evidence = json.loads(EVIDENCE.read_text(encoding="utf-8"))
    result = validate_evidence(evidence, load_pilot(PROPOSALS), load_decisions(DECISIONS))
    print(json.dumps({"catalogueRows": evidence["catalogueRows"],
                      "executionDateVerified": False,
                      "approvedForImport": 0,
                      "records": result}, ensure_ascii=False, indent=2))


if __name__ == "__main__":
    main()

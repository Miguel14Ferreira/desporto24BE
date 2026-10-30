#!/usr/bin/env python3
"""Evidence gate for first national venue review batch: NO auto-approvals.

All 50 venue rows retain their provisional OSM attributes. Official sources
may corroborate identity or public facility information, never free access.
"""
import argparse
from collections import Counter
import csv
from datetime import date
import json
from pathlib import Path
from urllib.parse import urlparse

REVIEW_STATUSES = {
    "IDENTITY_AND_ADDRESS", "IDENTITY_ONLY", "IDENTITY_WITH_CONFLICT",
    "HISTORICAL_OR_SEASONAL", "SPECIALIZED_USE"
}
ACCESS = {
    "AUTHORIZATION_REQUIRED", "PUBLIC_PROGRAMME_CONDITIONAL",
    "ACCESS_NOT_CONFIRMED", "SPECIALIZED_ACCESS"
}
OUTPUT_COLUMNS = [
    "priority_order", "osm_reference", "name", "city", "district",
    "official_identity_status", "official_source", "official_address",
    "access_conditions", "address_conflict", "review_state",
    "opening_or_booking_claim", "independently_approved_for_import"
]


def read_rows(path):
    with Path(path).open("r", encoding="utf-8-sig", newline="") as stream:
        rows = list(csv.DictReader(stream, delimiter=";"))
    required = {"priority_order", "osm_reference", "name", "city",
                "district", "address_osm_unverified", "decision"}
    if not rows or not required.issubset(rows[0]):
        raise ValueError("Wrong or empty 50-candidate review CSV")
    references = [row["osm_reference"] for row in rows]
    if len(references) != len(set(references)):
        raise ValueError("Repeated candidate OSM reference")
    if any(row["decision"] not in {"UNDECIDED", "REVIEW_REQUIRED", ""} for row in rows):
        raise ValueError("Unexpected already approved/rejected CSV: require fresh audit")
    return rows


def validate_evidence(record, candidate, observed):
    if record.get("name") != candidate["name"] or record.get("city") != candidate["city"]:
        raise ValueError("Evidence name/city mismatches original candidate")
    url = record.get("officialUrl")
    u = urlparse(url if isinstance(url, str) else "")
    if (u.scheme != "https" or not u.hostname or not u.hostname.endswith(".pt")
            or u.username or u.password):
        raise ValueError("Evidence requires an HTTPS official .pt URL")
    if record.get("identity") not in REVIEW_STATUSES:
        raise ValueError("Unknown corroboration status")
    if record.get("access") not in ACCESS:
        raise ValueError("Unknown access policy")
    try:
        checked = date.fromisoformat(record["observedOn"])
    except (KeyError, TypeError, ValueError) as exc:
        raise ValueError("Evidence needs a real verification date") from exc
    if checked > observed:
        raise ValueError("Evidence check cannot be in the future")
    notes = record.get("notes")
    if not isinstance(notes, str) or len(notes.strip()) < 18:
        raise ValueError("Evidence needs substantive notes")
    if (record.get("addressConflict") is True
            and record["identity"] != "IDENTITY_WITH_CONFLICT"):
        raise ValueError("Address conflict must not be treated as corroborated")
    if record.get("approvedForImport") is True:
        raise ValueError("Unreviewed official evidence is NOT approval")
    return u.geturl()


def assess(candidates, evidence, observed=date(2026, 10, 21)):
    if not isinstance(evidence, list):
        raise ValueError("Evidence must be a JSON array")
    by_ref = {row["osm_reference"]: row for row in candidates}
    if len(by_ref) != len(candidates):
        raise ValueError("Duplicate candidate references")
    indexed = {}
    for item in evidence:
        ref = item.get("osmReference")
        if ref not in by_ref or ref in indexed:
            raise ValueError("Unmatched/duplicate evidence reference " + str(ref))
        validate_evidence(item, by_ref[ref], observed)
        indexed[ref] = item
    output, count = [], Counter()
    for row in candidates:
        ref = row["osm_reference"]
        ev = indexed.get(ref)
        if ev is None:
            status = "AWAITING_OFFICIAL_SOURCE"
        elif ev["identity"] == "IDENTITY_WITH_CONFLICT" or ev.get("addressConflict") is True:
            status = "ADDRESS_CONFLICT_HOLD"
        elif ev["identity"] == "HISTORICAL_OR_SEASONAL":
            status = "CURRENT_OPERATION_UNVERIFIED"
        elif ev["identity"] == "SPECIALIZED_USE":
            status = "SPECIALIZED_ACCESS_REVIEW"
        elif ev["identity"] == "IDENTITY_ONLY":
            status = "ADDRESS_IDENTITY_REVIEW"
        elif ev["access"] == "AUTHORIZATION_REQUIRED":
            status = "PERMISSION_NEEDED"
        else:
            status = "IDENTITY_DOCUMENTED_ACCESS_UNVERIFIED"
        count[status] += 1
        output.append({
            "priority_order": row["priority_order"], "osm_reference": ref,
            "name": row["name"], "city": row["city"], "district": row["district"],
            "official_identity_status": ev["identity"] if ev else "",
            "official_source": ev["officialUrl"] if ev else "",
            "official_address": ev.get("officialAddress", "") if ev else "",
            "access_conditions": ev["access"] if ev else "NOT_CHECKED",
            "address_conflict": str(ev.get("addressConflict", False)).lower() if ev else "",
            "review_state": status,
            "opening_or_booking_claim": "NOT_VERIFIED",
            "independently_approved_for_import": "NO",
        })
    return {
        "batch_size": len(candidates),
        "official_evidence_records": len(indexed),
        "no_official_evidence": len(candidates)-len(indexed),
        "review_status_counts": dict(sorted(count.items())),
        "approved_for_import": 0,
        "database_changes": False,
        "notes": "Official venue identity is not proof of free use. Always confirm access and operator rules."
    }, output


def main():
    p = argparse.ArgumentParser(description=__doc__)
    p.add_argument("--first-50-csv", required=True, type=Path)
    p.add_argument("--evidence-json", required=True, type=Path)
    p.add_argument("--output-dir", required=True, type=Path)
    a = p.parse_args()
    candidates = read_rows(a.first_50_csv)
    evidence = json.loads(a.evidence_json.read_text(encoding="utf-8"))
    report, output = assess(candidates, evidence, observed=date.today())
    a.output_dir.mkdir(parents=True, exist_ok=True)
    (a.output_dir / "official-review-summary.json").write_text(
        json.dumps(report, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")
    with (a.output_dir / "official-review-queue.csv").open(
            "w", newline="", encoding="utf-8-sig") as h:
        writer = csv.DictWriter(h, fieldnames=OUTPUT_COLUMNS, delimiter=";")
        writer.writeheader()
        writer.writerows(output)
    print(json.dumps(report, ensure_ascii=False, indent=2))


if __name__ == "__main__":
    main()

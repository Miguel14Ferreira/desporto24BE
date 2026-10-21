#!/usr/bin/env python3
"""Page immutable national OSM review queues without republishing reviewed records.

Reuses the established first-50 selector to keep all eligibility rules identical.
Each subsequent page excludes EVERY prior page's original OSM references.
This script never approves, imports, geocodes, downloads, or modifies venues.
"""
import argparse
from collections import Counter, defaultdict
import csv
import gzip
import hashlib
import json
from pathlib import Path

from prepare_first_national_review_batch import (
    COLUMNS, choose, reason, score, osm_url,
)

EXPECTED_SOURCE_SHA256 = "e218744531f63bc94c537467d51fb77214e60132b20178877a13b52214a0701a"
MAX_PAGE = 15


def balanced_next(eligible, *, limit=50, district_cap=7):
    """Apply per-district quota first, then soften it only to fill sparse pages."""
    if limit < 1 or district_cap < 1:
        raise ValueError("limit and district_cap must be positive")
    buckets = defaultdict(list)
    for item in eligible:
        if reason(item) != "eligible_for_manual_review":
            raise ValueError("Ineligible candidate in balanced review pool")
        buckets[item["district"]].append(item)
    for group in buckets.values():
        group.sort(key=lambda r: (-score(r), r["name"].casefold(), r["sourceReference"]))
    selected, used = [], Counter()

    def sweep(max_from_district):
        while len(selected) < limit:
            active = [d for d in buckets if buckets[d] and used[d] < max_from_district]
            if not active:
                break
            for district in sorted(active, key=lambda d: (used[d], d)):
                if len(selected) == limit:
                    break
                selected.append(buckets[district].pop(0))
                used[district] += 1

    sweep(district_cap)
    if len(selected) < limit:
        sweep(limit)
    return selected


def get_batch(rows, *, page, limit=50, district_cap=7, expected=3514):
    if page < 1 or page > MAX_PAGE:
        raise ValueError("page must be in 1..15")
    # The first-batch implementation remains the source of truth, so page 1
    # cannot drift unnoticed when newer pagination code is modified.
    first, original = choose(rows, limit=limit, district_cap=district_cap, expected=expected)
    if page == 1:
        return first, {"page": 1, "previous_refs": 0, "eligible_total": original["eligible_for_review"]}
    seen = {r["sourceReference"] for r in first}
    current = []
    for target_page in range(2, page + 1):
        pool = [
            r for r in rows
            if r["sourceReference"] not in seen and reason(r) == "eligible_for_manual_review"
        ]
        current = balanced_next(pool, limit=limit, district_cap=district_cap)
        overlap = seen.intersection(r["sourceReference"] for r in current)
        if overlap:
            raise AssertionError("Already-reviewed reference repeated on later page")
        if not current:
            break
        seen.update(r["sourceReference"] for r in current)
    return current, {
        "page": page, "previous_refs": len(seen)-len(current),
        "eligible_total": original["eligible_for_review"],
        "remaining_eligible_after_this_page": original["eligible_for_review"]-len(seen)
    }


def generate(full_details, out_dir, *, page=2, limit=50, district_cap=7):
    full_details = Path(full_details)
    source_digest = hashlib.sha256(full_details.read_bytes()).hexdigest()
    if source_digest != EXPECTED_SOURCE_SHA256:
        raise ValueError("National snapshot SHA256 changed; refuse pagination")
    with gzip.open(full_details, "rt", encoding="utf-8") as data:
        records = [json.loads(line) for line in data if line.strip()]
    selected, report = get_batch(records, page=page, limit=limit, district_cap=district_cap)
    if not selected:
        raise ValueError("All eligible sources already accounted for in earlier pages")
    report["selected_for_manual_review"] = len(selected)
    report["selected_districts"] = dict(sorted(Counter(r["district"] for r in selected).items()))
    report["source_sha256"] = source_digest
    report["approved_for_import"] = 0
    report["database_writes"] = False
    report["message"] = (
        "Candidates only; OSM address does not prove current status, legal access "
        "or independent identity. Quota is softened only if few districts remain."
    )
    out_dir = Path(out_dir)
    out_dir.mkdir(parents=True, exist_ok=True)
    with (out_dir / f"national-review-page-{page}.csv").open("w", encoding="utf-8-sig", newline="") as dest:
        writer = csv.DictWriter(dest, fieldnames=COLUMNS, delimiter=";")
        writer.writeheader()
        for i, item in enumerate(selected, 1):
            writer.writerow({
                "priority_order": i,
                "priority_score": score(item),
                "district": item["district"],
                "city": item["city"],
                "name": item["name"],
                "sports": item["sports"],
                "venue_type": item["venueType"],
                "address_osm_unverified": item["address"],
                "latitude": item["latitude"],
                "longitude": item["longitude"],
                "osm_reference": item["sourceReference"],
                "osm_source_url": osm_url(item["sourceReference"]),
                "review_status": "AWAITING_INDEPENDENT_EVIDENCE",
                "verified_venue_url": "",
                "current_operation_confirmed": "",
                "access_confirmed": "",
                "reviewer": "",
                "review_date": "",
                "verification_notes": "",
                "decision": "UNDECIDED",
            })
    (out_dir / f"national-review-page-{page}-summary.json").write_text(
        json.dumps(report, ensure_ascii=False, indent=2)+"\n", encoding="utf-8")
    return report


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--full-details", required=True, type=Path)
    parser.add_argument("--out-dir", required=True, type=Path)
    parser.add_argument("--page", type=int, default=2)
    parser.add_argument("--limit", type=int, default=50)
    parser.add_argument("--district-cap", type=int, default=7)
    args = parser.parse_args()
    print(json.dumps(generate(args.full_details, args.out_dir, page=args.page,
                              limit=args.limit, district_cap=args.district_cap),
                     ensure_ascii=False, indent=2))


if __name__ == "__main__":
    main()

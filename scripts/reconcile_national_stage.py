#!/usr/bin/env python3
"""Offline, read-only review of national OSM candidates against a CSV export of existing venues."""
import argparse
import csv
import gzip
import json
import math
from collections import Counter, defaultdict
from pathlib import Path
from audit_national_stage import norm_name, metres

FIELDS = {"id", "name", "city", "district", "address", "latitude", "longitude", "source_reference"}
RESULTS = ["district", "city", "name", "source_reference", "sports",
           "status", "existing_id", "existing_name", "reason", "latitude", "longitude"]


def num(value):
    if value is None or str(value).strip() == "":
        return None
    number = float(value)
    if not math.isfinite(number):
        raise ValueError("Invalid coordinate in export")
    return number


def grid(lat, lon):
    return round(lat * 1000), round(lon * 1000)


def read_export(filename):
    with Path(filename).open(newline="", encoding="utf-8-sig") as handle:
        reader = csv.DictReader(handle)
        missing = FIELDS - set(reader.fieldnames or ())
        if missing:
            raise ValueError("Missing read-only CSV columns: " + repr(sorted(missing)))
        rows = list(reader)
    for row in rows:
        if not row["id"] or not row["name"]:
            raise ValueError("Invalid existing venue id/name")
        row["latitude"] = num(row["latitude"])
        row["longitude"] = num(row["longitude"])
    return rows


def indices(rows):
    refs = defaultdict(list)
    names = defaultdict(list)
    nearby = defaultdict(list)
    for item in rows:
        if item["source_reference"]:
            refs[item["source_reference"]].append(item)
        names[(norm_name(item["name"]), norm_name(item["city"]))].append(item)
        if item["latitude"] is not None and item["longitude"] is not None:
            nearby[grid(item["latitude"], item["longitude"])].append(item)
    return refs, names, nearby


def compare(candidate, lookups):
    refs, names, nearby = lookups
    ref = candidate["sourceReference"]
    if refs.get(ref):
        return "EXISTING_REFERENCE", refs[ref][0], "same original OSM reference"
    key = norm_name(candidate["name"]), norm_name(candidate["city"])
    if names.get(key):
        return "POSSIBLE_DUPLICATE", names[key][0], "same name and municipality"
    lat, lon = candidate.get("latitude"), candidate.get("longitude")
    if all(isinstance(v, (int, float)) and not isinstance(v, bool) and math.isfinite(v)
           for v in (lat, lon)):
        y, x = grid(lat, lon)
        for yy in range(y - 2, y + 3):
            for xx in range(x - 2, x + 3):
                for existing in nearby.get((yy, xx), ()):
                    if metres((lat, lon), (existing["latitude"], existing["longitude"])) <= 100:
                        return "POSSIBLE_DUPLICATE", existing, "other existing facility within 100 m"
    return "UNMATCHED_REQUIRES_REVIEW", None, "heuristic only: aliases remain possible"


def reconcile(candidate_file, live_rows):
    lookup = indices(live_rows)
    results = []
    refs = set()
    open_file = gzip.open if str(candidate_file).endswith(".gz") else open
    with open_file(candidate_file, "rt", encoding="utf-8") as src:
        for lineno, line in enumerate(src, 1):
            if not line.strip():
                continue
            candidate = json.loads(line)
            ref = candidate["sourceReference"]
            if not isinstance(ref, str) or not ref.startswith("OSM:") or ref in refs:
                raise ValueError(f"Invalid or repeated source reference at line {lineno}")
            refs.add(ref)
            status, existing, reason = compare(candidate, lookup)
            results.append({
                "district": candidate["district"], "city": candidate["city"],
                "name": candidate["name"], "source_reference": ref,
                "sports": candidate.get("sports") or "",
                "status": status, "existing_id": existing["id"] if existing else "",
                "existing_name": existing["name"] if existing else "",
                "reason": reason, "latitude": candidate.get("latitude"),
                "longitude": candidate.get("longitude")})
    results.sort(key=lambda r: (r["district"], r["city"], r["name"], r["source_reference"]))
    counts = Counter(row["status"] for row in results)
    summary = {"live_rows": len(live_rows), "source_candidates": len(results),
               "counts": dict(sorted(counts.items())), "approved": 0,
               "message": "All unmatched candidates still require manual approval; this tool NEVER imports."}
    return summary, results


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--candidates", required=True, type=Path)
    parser.add_argument("--existing-csv", required=True, type=Path)
    parser.add_argument("--report", required=True, type=Path)
    parser.add_argument("--review-csv", required=True, type=Path)
    args = parser.parse_args()
    summary, rows = reconcile(args.candidates, read_export(args.existing_csv))
    args.report.parent.mkdir(parents=True, exist_ok=True)
    args.report.write_text(json.dumps(summary, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")
    args.review_csv.parent.mkdir(parents=True, exist_ok=True)
    with args.review_csv.open("w", encoding="utf-8-sig", newline="") as out:
        writer = csv.DictWriter(out, fieldnames=RESULTS, delimiter=";")
        writer.writeheader()
        writer.writerows(rows)
    print(json.dumps(summary, ensure_ascii=False, indent=2))


if __name__ == "__main__":
    main()

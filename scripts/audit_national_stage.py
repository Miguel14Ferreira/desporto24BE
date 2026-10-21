#!/usr/bin/env python3
"""Conservative cross-district QA of OSM candidate staging. NEVER imports venues."""
from __future__ import annotations

import argparse
from collections import Counter
import hashlib
import json
import math
from pathlib import Path
import re
import unicodedata

from import_osm_venues_national import DISTRICTS

OSM_REF = re.compile(r"^OSM:(NODE|WAY|RELATION):[1-9][0-9]*$")
# Two distinct OSM objects with the same normalized name and a <=70m
# separation are suspicious, not confirmed duplicates.
NEARBY_METRES = 70.0


def norm_name(value):
    return "".join(c for c in unicodedata.normalize("NFKD", value.casefold())
                   if c.isalnum() and not unicodedata.combining(c))


def metres(a, b):
    lat1, lon1 = map(math.radians, a)
    lat2, lon2 = map(math.radians, b)
    dl, dp = lon2 - lon1, lat2 - lat1
    return 6371000 * 2 * math.asin(min(1.0, math.sqrt(
        math.sin(dp / 2) ** 2 + math.cos(lat1) * math.cos(lat2) * math.sin(dl / 2) ** 2)))


def sha256(path):
    return hashlib.sha256(path.read_bytes()).hexdigest()


def inspect(cache, *, strict=True):
    """Return QA metrics, never treat any OSM candidate as production-approved.

    Strict mode requires all 18 official CAOP-checked extracts to be complete.
    Missing districts retain explicit failure rows rather than false zeros.
    """
    report = json.loads((cache / "national-report.json").read_text(encoding="utf-8"))
    if not isinstance(report, dict):
        raise ValueError("Invalid national report")
    metrics = {"districts_expected": 18, "districts_processed": 0,
               "districts_failed_or_missing": [], "candidate_map_objects": 0,
               "ready_for_manual_review": 0, "quarantined_source_records": 0,
               "possible_duplicate_groups": [], "duplicate_source_references": [],
               "by_district": {}, "live_db_compared": False,
               "source": "OpenStreetMap © contributors (ODbL); DGT CAOP2025 (CC BY 4.0)",
               "approved_for_publication": 0,
               "notice": "These are map objects, NOT distinct verified sports facilities. "
                         "No facility has been approved, imported, or checked against the live database."}
    refs = {}
    cells = {}
    nearby_pairs = set()
    candidate_rows = []
    for slug, name in DISTRICTS.items():
        d = report.get(slug)
        if not isinstance(d, dict) or d.get("status") != "processed" or not d.get("boundary_fingerprint"):
            metrics["districts_failed_or_missing"].append(slug)
            metrics["by_district"][name] = {"status": d.get("status", "invalid") if isinstance(d, dict) else "missing"}
            continue
        ready_path = cache / f"{slug}-venues.jsonl"
        review_path = cache / f"{slug}-review.jsonl"
        if (not ready_path.is_file() or not review_path.is_file()
                or d.get("sha256") != sha256(ready_path)
                or d.get("review_sha256") != sha256(review_path)):
            metrics["districts_failed_or_missing"].append(slug)
            metrics["by_district"][name] = {"status": "checksum_error"}
            continue
        actual_ready = 0
        with ready_path.open(encoding="utf-8") as stream:
            for line_no, line in enumerate(stream, 1):
                if not line.strip():
                    continue
                try:
                    item = json.loads(line)
                    reference = item["sourceReference"]
                    if not OSM_REF.fullmatch(reference):
                        raise ValueError("invalid sourceReference")
                    if item["district"] != name:
                        raise ValueError("incorrect district")
                    if not isinstance(item["name"], str) or not item["name"].strip():
                        raise ValueError("missing name")
                    coords = (item["latitude"], item["longitude"])
                    if (any(not isinstance(x, (int, float)) or isinstance(x, bool)
                            or not math.isfinite(x) for x in coords)
                            or not (36.8 <= coords[0] <= 42.3 and -9.6 <= coords[1] <= -6)):
                        raise ValueError("invalid WGS84 mainland coordinates")
                except (KeyError, ValueError, TypeError, json.JSONDecodeError) as exc:
                    raise ValueError(f"{ready_path.name}:{line_no}: {exc}") from exc
                actual_ready += 1
                if reference in refs:
                    metrics["duplicate_source_references"].append({
                        "reference": reference,
                        "first_district": refs[reference], "second_district": name})
                else:
                    refs[reference] = name
                key = norm_name(item["name"])
                cell = (round(coords[0] * 1000), round(coords[1] * 1000))
                # 3x3 cell query catches >=70m neighbours in either dimension;
                # add margin to avoid boundary misses from rounding.
                for y in range(cell[0] - 2, cell[0] + 3):
                    for x in range(cell[1] - 2, cell[1] + 3):
                        for prior in cells.get((key, y, x), []):
                            if prior["sourceReference"] != reference and metres(
                                    (prior["latitude"], prior["longitude"]), coords) <= NEARBY_METRES:
                                pair = tuple(sorted((prior["sourceReference"], reference)))
                                if pair not in nearby_pairs:
                                    nearby_pairs.add(pair)
                                    if len(metrics["possible_duplicate_groups"]) < 300:
                                        metrics["possible_duplicate_groups"].append({
                                            "references": pair, "name": item["name"],
                                            "districts": sorted({name, prior["district"]})})
                cells.setdefault((key, *cell), []).append(item)
                candidate_rows.append({"sourceReference": reference, "name": item["name"],
                                       "city": item["city"], "district": item["district"],
                                       "sports": item.get("sports"), "latitude": coords[0],
                                       "longitude": coords[1], "status": "REQUIRES_LIVE_DB_AND_HUMAN_APPROVAL"})
        if actual_ready != d.get("import_ready"):
            raise ValueError(f"{slug}: importer count mismatch ({actual_ready} != {d.get('import_ready')})")
        metrics["districts_processed"] += 1
        metrics["candidate_map_objects"] += d.get("candidates", 0)
        metrics["ready_for_manual_review"] += actual_ready
        metrics["quarantined_source_records"] += d.get("review_required", 0)
        metrics["by_district"][name] = {
            "status": "processed", "candidate_map_objects": d.get("candidates", 0),
            "ready_for_manual_review": actual_ready,
            "needs_source_review": d.get("review_required", 0)}
    metrics["possible_duplicate_pair_count"] = len(nearby_pairs)
    metrics["repeated_source_reference_count"] = len(metrics["duplicate_source_references"])
    metrics["status"] = "complete" if metrics["districts_processed"] == len(DISTRICTS) else "partial"
    if strict and metrics["status"] != "complete":
        # Persist partial result in calling main() before returning a failure.
        metrics["publication_allowed"] = False
    else:
        metrics["publication_allowed"] = False  # Always false; even a complete report is not approval.
    return metrics, candidate_rows


def main(argv=None):
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--cache-dir", required=True, type=Path)
    parser.add_argument("--output", required=True, type=Path)
    parser.add_argument("--preview", required=True, type=Path)
    args = parser.parse_args(argv)
    metrics, rows = inspect(args.cache_dir)
    args.output.parent.mkdir(parents=True, exist_ok=True)
    args.output.write_text(json.dumps(metrics, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")
    args.preview.parent.mkdir(parents=True, exist_ok=True)
    with args.preview.open("w", encoding="utf-8") as output:
        for item in rows:
            output.write(json.dumps(item, ensure_ascii=False) + "\n")
    print(json.dumps({k: metrics[k] for k in (
        "status", "districts_processed", "districts_failed_or_missing",
        "candidate_map_objects", "ready_for_manual_review",
        "quarantined_source_records", "possible_duplicate_pair_count",
        "repeated_source_reference_count", "approved_for_publication")}, ensure_ascii=False, indent=2))
    return 0 if metrics["status"] == "complete" else 2


if __name__ == "__main__":
    raise SystemExit(main())

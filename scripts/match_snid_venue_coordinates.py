#!/usr/bin/env python3
"""Read-only, explainable matching of 27 Jynlo draft venues with official SNID.

Never imports/updates venues. An SNID match is a proposed coordinate to REVIEW,
not confirmed field-level GPS. Distinct pitches within one sports complex
must never inherit the common entrance/centroid position.
"""
from __future__ import annotations

import argparse
import csv
from difflib import SequenceMatcher
import json
from pathlib import Path
import re

from snid_district_source_audit import (
    DISTRICTS, coordinates, fetch_official_source, fold, iter_json_objects, source_district,
)

COLUMNS = [
    "source_reference", "name", "city", "district", "status",
    "match_count", "top_match_name", "top_source_municipality",
    "top_latitude", "top_longitude", "top_similarity",
]


def name_key(name):
    normalized = fold(name)
    return re.sub(r"[^a-z0-9]+", " ", normalized).strip()


def get_municipality(record):
    address = record.get("address") or {}
    if isinstance(address, dict):
        text = address.get("addressLocality")
        if isinstance(text, str) and text.strip():
            return text.strip()
    info = record.get("municipalityInfo")
    if isinstance(info, str):
        return info.strip()
    if isinstance(info, dict):
        for k in ("name", "municipality", "municipio", "concelho", "designacao"):
            value = info.get(k)
            if isinstance(value, str) and value.strip():
                return value.strip()
    return ""


def is_venue(record):
    category = record.get("category") or ""
    if isinstance(category, dict):
        category = category.get("name") or category.get("designation") or ""
    return "instituicao desportiva" not in fold(category)


def load_official_catalogue(source):
    """Keep only minimal public facility attributes, not entire source records."""
    by_district = {name: [] for name in DISTRICTS}
    scanned = 0
    located = 0
    for record in iter_json_objects(source):
        scanned += 1
        point = coordinates(record)
        district = source_district(record)
        if not is_venue(record) or not point or not district:
            continue
        name = record.get("name")
        if not isinstance(name, str) or not name.strip():
            continue
        # Conservative geographic prefilter; official CAOP geometry is still needed.
        lat, lon = point
        if not (36.8 <= lat <= 42.3 and -9.6 <= lon <= -6.0):
            continue
        located += 1
        by_district[district].append({
            "name": name.strip(),
            "key": name_key(name),
            "municipality": get_municipality(record),
            "district": district,
            "latitude": lat,
            "longitude": lon,
            "snid_id": str(record.get("id") or ""),
        })
    return by_district, {"snid_records_scanned": scanned,
                         "named_located_district_records": located}


def candidate_matches(candidate, records, max_candidates=3):
    target_name = name_key(candidate["name"])
    target_city = name_key(candidate["city"])
    possible = []
    for item in records:
        item_name = item["key"]
        if not item_name:
            continue
        exact_name = item_name == target_name
        municipality_agrees = bool(target_city and
                                   name_key(item["municipality"]) == target_city)
        if exact_name:
            confidence = "EXACT_NAME_CITY_AND_DISTRICT_REVIEW" if municipality_agrees \
                else "EXACT_NAME_AND_DISTRICT_REVIEW"
            score = 1.0
        else:
            # Suggestions ONLY, constrained to district; never use for import GPS.
            # Ignore common shared words like "campo", "municipal", or "piscinas".
            useful = {s for s in target_name.split() if len(s) >= 5 and s not in {
                "campo", "campos", "municipal", "municipais", "desportivo",
                "desportiva", "complexo", "pavilhao", "piscina", "piscinas"}}
            if not useful or not (useful & set(item_name.split())):
                continue
            score = SequenceMatcher(None, target_name, item_name).ratio()
            if score < 0.78:
                continue
            confidence = "SIMILAR_NAME_REVIEW"
        possible.append((exact_name, municipality_agrees, score, {
            "name": item["name"],
            "municipality": item["municipality"],
            "district": item["district"],
            "latitude": item["latitude"],
            "longitude": item["longitude"],
            "snid_id": item["snid_id"],
            "match_type": confidence,
            "name_similarity": round(score, 3),
        }))
    possible.sort(key=lambda x: (x[0], x[1], x[2]), reverse=True)
    matches = [entry for _, _, _, entry in possible[:max_candidates]]
    exact_count = sum(1 for exact, _, _, _ in possible if exact)
    status = ("REVIEW_EXACT_NAME" if exact_count == 1
              else "REVIEW_MULTIPLE_EXACT" if exact_count > 1
              else "REVIEW_SIMILAR_NAME" if possible
              else "NO_PLAUSIBLE_SNID_NAME_MATCH")
    return status, matches, exact_count


def analyse(manifest, source):
    candidates = manifest.get("candidates")
    if not isinstance(candidates, list) or len(candidates) != 27:
        raise ValueError("Expected exactly the 27 missing-GPS draft candidates")
    identifiers = [v["sourceReference"] for v in candidates]
    if len(set(identifiers)) != 27:
        raise ValueError("Duplicate source identifiers in candidate manifest")
    by_district, meta = load_official_catalogue(source)
    results = []
    for candidate in candidates:
        if candidate["district"] not in by_district:
            raise ValueError("Unknown candidate district")
        status, matches, exact_count = candidate_matches(
            candidate, by_district[candidate["district"]])
        results.append({
            "source_reference": candidate["sourceReference"],
            "name": candidate["name"],
            "city": candidate["city"],
            "district": candidate["district"],
            "official_source_url": candidate["sourceUrl"],
            "status": status,
            "exact_match_count": exact_count,
            "suggestions": matches,
        })
    meta["candidates_checked"] = len(results)
    meta["with_one_exact_name_match"] = sum(
        r["status"] == "REVIEW_EXACT_NAME" for r in results)
    meta["with_multiple_exact_name_matches"] = sum(
        r["status"] == "REVIEW_MULTIPLE_EXACT" for r in results)
    meta["with_similar_name_only"] = sum(
        r["status"] == "REVIEW_SIMILAR_NAME" for r in results)
    meta["without_name_match"] = sum(
        r["status"] == "NO_PLAUSIBLE_SNID_NAME_MATCH" for r in results)
    meta["source"] = "SNID (IPDJ); point coordinates from public source"
    meta["policy"] = (
        "REVIEW ONLY. Source label + source district is NOT CAOP polygon proof; "
        "shared complex/entrance coordinates must never be copied to numbered "
        "courts; do not generate SQL or modify database from this report."
    )
    return results, meta


def report(manifest_file, snid_file, output_folder, provenance=None):
    manifest = json.loads(Path(manifest_file).read_text(encoding="utf-8"))
    records, metadata = analyse(manifest, snid_file)
    if metadata["snid_records_scanned"] == 0:
        raise ValueError("Empty official source cannot yield a trustworthy audit")
    if provenance:
        metadata["provenance"] = provenance
    folder = Path(output_folder)
    folder.mkdir(parents=True, exist_ok=True)
    with (folder / "snid-coordinate-review-27.csv").open(
            "w", encoding="utf-8-sig", newline="") as output:
        writer = csv.DictWriter(output, fieldnames=COLUMNS, delimiter=";")
        writer.writeheader()
        for record in records:
            top = record["suggestions"][0] if record["suggestions"] else {}
            writer.writerow({
                "source_reference": record["source_reference"],
                "name": record["name"], "city": record["city"],
                "district": record["district"], "status": record["status"],
                "match_count": record["exact_match_count"],
                "top_match_name": top.get("name", ""),
                "top_source_municipality": top.get("municipality", ""),
                "top_latitude": top.get("latitude", ""),
                "top_longitude": top.get("longitude", ""),
                "top_similarity": top.get("name_similarity", ""),
            })
    (folder / "snid-coordinate-review-27.json").write_text(
        json.dumps({"summary": metadata, "candidates": records},
                   ensure_ascii=False, indent=2), encoding="utf-8")
    return records, metadata


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--manifest", type=Path, required=True)
    inputs = parser.add_mutually_exclusive_group(required=True)
    inputs.add_argument("--snid-json", type=Path)
    inputs.add_argument("--fetch-official", action="store_true")
    parser.add_argument("--output-dir", type=Path, required=True)
    args = parser.parse_args()
    source = args.snid_json
    provenance = None
    if args.fetch_official:
        source = args.output_dir / "snid-source-private.json"
        provenance = fetch_official_source(source)
    try:
        _, summary = report(args.manifest, source, args.output_dir, provenance)
    finally:
        if args.fetch_official:
            source.unlink(missing_ok=True)
    print(json.dumps(summary, ensure_ascii=False, indent=2))
    print("No production writes; no source coordinates are automatically approved.")


if __name__ == "__main__":
    main()

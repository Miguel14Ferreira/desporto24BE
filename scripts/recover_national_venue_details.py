#!/usr/bin/env python3
"""Recover full OSM fields for prior national preview, read-only. Never approves imports."""
import argparse
import csv
import gzip
import hashlib
import json
from collections import Counter
from pathlib import Path
from import_osm_venues_national import DISTRICTS

ALREADY_LIVE = frozenset([
    "OSM:WAY:429474882", "OSM:WAY:429475622", "OSM:WAY:429476238",
    "OSM:WAY:1348549266", "OSM:WAY:1275651401", "OSM:WAY:157099522",
    "OSM:WAY:305711614", "OSM:WAY:429477908",
    "OSM:WAY:426295154", "OSM:WAY:426296888",
])
FULL_FIELDS = ("sourceReference", "name", "city", "district", "address",
               "sports", "venueType", "environmentType", "latitude", "longitude")


def sha(path):
    return hashlib.sha256(path.read_bytes()).hexdigest()


def read_jsonl(path):
    opener = gzip.open if str(path).endswith(".gz") else open
    with opener(path, "rt", encoding="utf-8") as f:
        for line in f:
            if line.strip():
                yield json.loads(line)


def recover(cache, original, qa_path, public_csv, districts=DISTRICTS):
    original_rows = {}
    for item in read_jsonl(original):
        ref = item["sourceReference"]
        if ref in original_rows:
            raise ValueError("Repeated original OSM ref: " + ref)
        original_rows[ref] = item
    qa = json.loads(qa_path.read_text(encoding="utf-8"))
    if len(districts) == 18:
        if len(original_rows) != 3514 or qa.get("ready_for_manual_review") != 3514:
            raise ValueError("Expected original 3514 reviewed OSM candidates")
        if not ALREADY_LIVE.issubset(original_rows):
            raise ValueError("The 10 verified live refs must be in original candidates")
    duplicates = set()
    for group in qa.get("possible_duplicate_groups", ()):
        duplicates.update(group["references"])
    public = {}
    with public_csv.open(encoding="utf-8-sig", newline="") as f:
        reader = csv.DictReader(f, delimiter=";")
        if not {"source_reference", "status"}.issubset(reader.fieldnames or []):
            raise ValueError("Invalid public comparison schema")
        for row in reader:
            ref = row["source_reference"]
            if ref in public:
                raise ValueError("Repeated public comparison OSM ref")
            public[ref] = row["status"]
    if set(public) != set(original_rows):
        raise ValueError("Public LIVE comparison is not for the same original OSM references")
    report = json.loads((cache / "national-report.json").read_text(encoding="utf-8"))
    replay = {}
    district_rows = {}
    for slug, district in districts.items():
        item = report.get(slug)
        file = cache / (slug + "-venues.jsonl")
        quarantine = cache / (slug + "-review.jsonl")
        if not isinstance(item, dict) or item.get("status") != "processed":
            raise ValueError("Missing processed CAOP district: " + slug)
        if not item.get("boundary_fingerprint") or not file.exists() or not quarantine.exists():
            raise ValueError("CAOP or source files missing: " + slug)
        if item.get("sha256") != sha(file) or item.get("review_sha256") != sha(quarantine):
            raise ValueError("Bad source checksums for " + slug)
        count = 0
        for record in read_jsonl(file):
            ref = record["sourceReference"]
            if ref in replay or record.get("district") != district:
                raise ValueError("Repeated/misclassified source reference: " + ref)
            if not all(field in record for field in FULL_FIELDS):
                raise ValueError("Missing full source fields: " + ref)
            replay[ref] = record
            count += 1
        if count != item.get("import_ready"):
            raise ValueError("Source record-count mismatch: " + slug)
        district_rows[district] = dict(district=district, new_snapshot_ready=count,
                                       original_candidates=0, rehydrated=0,
                                       needs_address_review=0, suspected_duplicates=0,
                                       confirmed_live_reference=0)
    summary_counts = Counter()
    drift = []
    missing = []
    enriched = []
    for ref, old in sorted(original_rows.items()):
        district = old["district"]
        if district not in district_rows:
            raise ValueError("Invalid original district: " + district)
        dst = district_rows[district]
        dst["original_candidates"] += 1
        if ref not in replay:
            missing.append(ref)
            continue
        new = replay[ref]
        changes = [field for field in ("name", "city", "district", "sports", "latitude", "longitude")
                   if old.get(field) != new.get(field)]
        if changes:
            drift.append({"reference": ref, "fields": changes})
        address = new["address"] or ""
        weak_address = not address or "por confirmar" in address.casefold()
        possible_duplicate = (ref in duplicates or public[ref] == "POSSIBLE_DUPLICATE")
        already_live = ref in ALREADY_LIVE
        if already_live:
            gate = "CONFIRMED_EXISTING_LIVE_EXCLUDE"
        elif possible_duplicate:
            gate = "POSSIBLE_DUPLICATE_REVIEW"
        elif weak_address:
            gate = "ADDRESS_REVIEW"
        else:
            gate = "SOURCE_REQUIRES_INDEPENDENT_REVIEW"
        summary_counts[gate] += 1
        dst["rehydrated"] += 1
        dst["needs_address_review"] += int(weak_address)
        dst["suspected_duplicates"] += int(possible_duplicate)
        dst["confirmed_live_reference"] += int(already_live)
        enriched.append(dict(new, importGate=gate, approvedForImport=False,
                             independentIdentityVerified=False,
                             accessAndCurrentOperationVerified=False,
                             addressVerifiedIndependently=False,
                             sourceFieldsChangedFromOriginal=changes,
                             originalLiveHeuristic=public[ref],
                             originalSourceReference=ref))
    new_refs = sorted(set(replay) - set(original_rows))
    summary = dict(original_candidates=len(original_rows), original_districts=len(districts),
                   processed_districts=len(district_rows), rehydrated=len(enriched),
                   missing_from_new_snapshot=len(missing), new_not_in_original=len(new_refs),
                   source_field_changes=len(drift),
                   verified_existing_source_refs=sum(x["importGate"] == "CONFIRMED_EXISTING_LIVE_EXCLUDE" for x in enriched),
                   needing_address_review=sum("por confirmar" in (x["address"] or "").casefold() or not x["address"] for x in enriched),
                   gate_counts=dict(summary_counts), approvedForImport=0,
                   complete=(len(missing) == 0), anyDatabaseWrites=False,
                   note="Source OSM is not independent proof of venue status, opening, access or reservability.")
    return summary, enriched, dict(missing=missing, new=new_refs, changed=drift), list(district_rows.values())


def main():
    p = argparse.ArgumentParser(description=__doc__)
    p.add_argument("--cache-dir", required=True, type=Path)
    p.add_argument("--original", required=True, type=Path)
    p.add_argument("--qa", required=True, type=Path)
    p.add_argument("--public-review", required=True, type=Path)
    p.add_argument("--out", required=True, type=Path)
    args = p.parse_args()
    args.out.mkdir(parents=True, exist_ok=True)
    summary, rows, drift, districts = recover(
        args.cache_dir, args.original, args.qa, args.public_review)
    (args.out / "quality-summary.json").write_text(
        json.dumps(summary, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")
    (args.out / "source-drift.json").write_text(
        json.dumps(drift, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")
    with gzip.open(args.out / "national-venue-details-3514.jsonl.gz", "wt", encoding="utf-8") as out:
        for row in rows:
            out.write(json.dumps(row, ensure_ascii=False) + "\n")
    with (args.out / "district-quality.csv").open("w", encoding="utf-8-sig", newline="") as out:
        w = csv.DictWriter(out, fieldnames=list(districts[0]), delimiter=";")
        w.writeheader()
        w.writerows(districts)
    print(json.dumps(summary, ensure_ascii=False, indent=2))
    return 0 if summary["complete"] and summary["verified_existing_source_refs"] == 10 else 2


if __name__ == "__main__":
    raise SystemExit(main())

#!/usr/bin/env python3
"""Select a first, unapproved review queue from cached 18-district OSM data.

No network, DB, API credentials, or migrations. Never equate OSM tags to
public access or confirmed operation.
"""
import argparse
from collections import Counter, defaultdict
import csv
import gzip
import hashlib
import json
from pathlib import Path
import re

FOCUS = {"Futebol", "Futsal", "Basquetebol", "Andebol", "Voleibol",
         "Voleibol de Praia", "Padel", "Ténis", "Ténis de mesa",
         "Rugby", "Atletismo", "Hóquei", "Skate", "BMX", "Pickleball",
         "Natação", "Canoagem", "Escalada"}
TYPES = {"Campo desportivo", "Complexo desportivo", "Estádio",
         "Pavilhão desportivo", "Piscina", "Pista desportiva"}
MUNICIPAL_PENDING = {"Setúbal", "Leiria", "Bragança"}
LIVE_REFS = frozenset({
    "OSM:WAY:429474882", "OSM:WAY:429475622", "OSM:WAY:429476238",
    "OSM:WAY:1348549266", "OSM:WAY:1275651401", "OSM:WAY:157099522",
    "OSM:WAY:305711614", "OSM:WAY:429477908",
    "OSM:WAY:426295154", "OSM:WAY:426296888"})
REF = re.compile(r"OSM:(NODE|WAY|RELATION):([1-9][0-9]*)\Z")
WEAK_NAMES = {"upa!", "piscinas municipais", "piscina municipal",
              "complexo desportivo", "pavilhão municipal", "campo desportivo"}
UNSAFE_NAME_PARTS = ("encerrad", "desativad", "desactivad",
    "temporariamente", "hotel", "resort", "garden", "spa ",
    "piscina privada", "ping-pong", "clínica", "fitness", "health club",
    "crossfit", "gimno", "wowfit", "my fit", "lemon fit", "aquaparque")
COLUMNS = ["priority_order", "priority_score", "district", "city",
    "name", "sports", "venue_type", "address_osm_unverified",
    "latitude", "longitude", "osm_reference", "osm_source_url",
    "review_status", "verified_venue_url", "current_operation_confirmed",
    "access_confirmed", "reviewer", "review_date", "verification_notes", "decision"]


def osm_url(ref):
    match = REF.fullmatch(ref or "")
    if not match:
        raise ValueError("Invalid OSM source reference: " + str(ref))
    return ("https://www.openstreetmap.org/" + match[1].lower() + "/" + match[2])


def reason(row):
    source = row.get("sourceReference")
    osm_url(source)
    if source in LIVE_REFS or row.get("importGate") == "CONFIRMED_EXISTING_LIVE_EXCLUDE":
        return "already_in_live"
    if (row.get("importGate") == "POSSIBLE_DUPLICATE_REVIEW"
            or row.get("originalLiveHeuristic") == "POSSIBLE_DUPLICATE"):
        return "duplicate_review"
    if row.get("district") in MUNICIPAL_PENDING:
        return "pending_municipal_reconciliation"
    if row.get("importGate") != "SOURCE_REQUIRES_INDEPENDENT_REVIEW":
        return "address_or_other_source_review"
    if row.get("venueType") not in TYPES:
        return "not_selected_venue_type"
    if not set((row.get("sports") or "").split(";")).intersection(FOCUS):
        return "not_selected_sport"
    address = row.get("address") or ""
    if not address or "por confirmar" in address.casefold() or address.startswith("Localização no mapa:"):
        return "address_missing"
    name = (row.get("name") or "").casefold().strip()
    if name in WEAK_NAMES:
        return "generic_identity_review"
    if any(bad in name for bad in UNSAFE_NAME_PARTS):
        return "operation_or_commercial_review"
    if row.get("approvedForImport") is True or row.get("independentIdentityVerified") is True:
        raise ValueError("Unexpected approval state in untrusted source")
    for key in ("latitude", "longitude"):
        if isinstance(row.get(key), bool) or not isinstance(row.get(key), (float, int)):
            return "invalid_coordinates"
    return "eligible_for_manual_review"


def score(row):
    sports = set((row.get("sports") or "").split(";"))
    teams = {"Futebol", "Futsal", "Basquetebol", "Andebol", "Voleibol",
             "Padel", "Ténis", "Pickleball", "Rugby", "Hóquei"}
    points = 8 if sports.intersection(teams) else 4
    points += min(3, max(0, len(sports)-1))
    if row.get("venueType") in {"Pavilhão desportivo", "Complexo desportivo"}:
        points += 3
    n = (row.get("name") or "").casefold()
    if any(word in n for word in (
            "municipal", "pavilhão", "polidesportivo", "estádio", "campo de futebol")):
        points += 3  # This is NOT evidence of permission for public use.
    address = row.get("address") or ""
    if re.search(r"\b\d{4}-\d{3}\b", address):
        points += 2
    if "rua " in address.casefold() or "avenida " in address.casefold():
        points += 1
    return points


def choose(rows, limit=50, district_cap=7, expected=3514):
    if expected != len(rows) or limit < 1 or district_cap < 1:
        raise ValueError("Invalid source count or selection constraints")
    refs = [x.get("sourceReference") for x in rows]
    if len(refs) != len(set(refs)):
        raise ValueError("Duplicate original OSM source")
    if expected == 3514 and not LIVE_REFS.issubset(refs):
        raise ValueError("All ten DB-verified existing refs are required")
    excluded, buckets, totals = Counter(), defaultdict(list), Counter()
    for row in rows:
        label = reason(row)
        if label != "eligible_for_manual_review":
            excluded[label] += 1
        else:
            buckets[row["district"]].append(row)
            totals[row["district"]] += 1
    for district in buckets:
        buckets[district].sort(key=lambda r: (-score(r), r["name"].casefold(), r["sourceReference"]))
    selected, counts = [], Counter()
    while len(selected) < limit:
        districts = [d for d in buckets if buckets[d] and counts[d] < district_cap]
        if not districts:
            break
        for d in sorted(districts, key=lambda name: (counts[name], name)):
            if len(selected) >= limit:
                break
            selected.append(buckets[d].pop(0))
            counts[d] += 1
    meta = {"original_records": len(rows), "eligible_for_review": sum(totals.values()),
            "selected_for_review": len(selected), "selected_districts": dict(sorted(counts.items())),
            "deferred_by_reason": dict(sorted(excluded.items())),
            "approved_for_import": 0, "any_live_database_writes": False}
    return selected, meta


def generate(source_path, out_dir, limit=50, quota=7, expected=3514):
    source_path, out_dir = Path(source_path), Path(out_dir)
    open_data = gzip.open if source_path.suffix == ".gz" else open
    with open_data(source_path, "rt", encoding="utf-8") as file:
        rows = [json.loads(line) for line in file if line.strip()]
    selected, meta = choose(rows, limit=limit, district_cap=quota, expected=expected)
    meta["source_sha256"] = hashlib.sha256(source_path.read_bytes()).hexdigest()
    out_dir.mkdir(parents=True, exist_ok=True)
    with (out_dir / "review-queue.csv").open("w", encoding="utf-8-sig", newline="") as f:
        writer = csv.DictWriter(f, fieldnames=COLUMNS, delimiter=";")
        writer.writeheader()
        for i, row in enumerate(selected, 1):
            writer.writerow({
                "priority_order": i, "priority_score": score(row),
                "district": row["district"], "city": row["city"],
                "name": row["name"], "sports": row["sports"],
                "venue_type": row["venueType"], "address_osm_unverified": row["address"],
                "latitude": row["latitude"], "longitude": row["longitude"],
                "osm_reference": row["sourceReference"],
                "osm_source_url": osm_url(row["sourceReference"]),
                "review_status": "AWAITING_INDEPENDENT_EVIDENCE",
                "verified_venue_url": "", "current_operation_confirmed": "",
                "access_confirmed": "", "reviewer": "", "review_date": "",
                "verification_notes": "", "decision": "UNDECIDED"})
    with (out_dir / "unapproved-venue-data.jsonl").open("w", encoding="utf-8") as f:
        fields = ("sourceReference", "name", "city", "district", "address",
                  "sports", "venueType", "environmentType", "latitude", "longitude")
        for row in selected:
            value = {key: row.get(key) for key in fields}
            value["approved"] = False
            f.write(json.dumps(value, ensure_ascii=False) + "\n")
    (out_dir / "review-summary.json").write_text(
        json.dumps(meta, ensure_ascii=False, indent=2)+"\n", encoding="utf-8")
    return meta


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--full-details", type=Path, required=True)
    parser.add_argument("--out-dir", type=Path, required=True)
    parser.add_argument("--limit", type=int, default=50)
    parser.add_argument("--district-cap", type=int, default=7)
    args = parser.parse_args()
    print(json.dumps(generate(args.full_details, args.out_dir, args.limit, args.district_cap),
                     ensure_ascii=False, indent=2))


if __name__ == "__main__":
    main()

#!/usr/bin/env python3
"""Rank GPS-only national sports facilities for review, across ALL 18 districts.

Never invent postal addresses, infer facility opening/access or approve imports.
Uses only the SHA256-pinned, already archived 3514-candidate OSM snapshot.
"""
from collections import Counter, defaultdict
import argparse
import csv
import gzip
import hashlib
import json
import math
from pathlib import Path
import re
import unicodedata

from prepare_first_national_review_batch import osm_url

EXPECTED_SOURCE_SHA256 = "e218744531f63bc94c537467d51fb77214e60132b20178877a13b52214a0701a"
DISTRICTS = (
    "Aveiro", "Beja", "Braga", "Bragança", "Castelo Branco", "Coimbra",
    "Évora", "Faro", "Guarda", "Leiria", "Lisboa", "Portalegre",
    "Porto", "Santarém", "Setúbal", "Viana do Castelo", "Vila Real", "Viseu",
)
FOCUS_SPORTS = {
    "Futebol", "Futsal", "Basquetebol", "Andebol", "Voleibol",
    "Voleibol de Praia", "Padel", "Ténis", "Rugby", "Atletismo",
    "Hóquei", "Skate", "BMX", "Pickleball", "Natação", "Canoagem",
    "Escalada", "Ténis de mesa", "Surf",
}
VENUE_TYPES = {
    "Campo desportivo", "Complexo desportivo", "Estádio",
    "Pavilhão desportivo", "Piscina", "Pista desportiva",
}
BLOCKED_WORDS = (
    "antigo ", "antiga ", "desativad", "desactivad", "inativ",
    "encerrad", "abandonad", "privado", "privada", "proibido",
    "ruína", "ruina", "hotel ", "resort", "acampamento",
    "militar", "clínica", "escola básica", "escola secundária",
    "quartel", "temporariamente", "country club", "pavilhão escolar", "pavilhao escolar",
)
GENERIC_NAMES = {
    "campo de futebol", "campo de ténis", "campo de futsal",
    "campo de padel", "campo de futebol 11", "campo de futebol 7",
    "campo de futebol 5", "pavilhão", "polidesportivo",
    "campo desportivo", "estádio municipal", "estádio",
    "piscina municipal", "piscinas municipais", "piscina",
    "skatepark", "skate park", "ringue", "campo",
    "sintetico", "campo municipal", "campo de jogos",
    "campo polidesportivo", "campo de futebol em terra", "pavilhão 1",
    "pavilhão 2", "pavilhão gimnodesportivo",
}
NEEDS_MUNICIPAL_ALIAS_REVIEW = {"Bragança", "Leiria", "Setúbal"}
CSV_FIELDS = (
    "priority", "district", "city", "name", "sports", "venue_type",
    "latitude", "longitude", "osm_reference", "osm_reference_url",
    "openstreetmap_pin_url", "postal_address_status",
    "gps_source_status", "nearby_osm_candidates_150m",
    "pending_32_municipal_alias_check", "independent_identity_verified",
    "current_operation_verified", "access_and_restrictions_verified",
    "approved_for_import", "review_status", "review_notes",
)


def norm(value):
    value = unicodedata.normalize("NFKD", (value or "").casefold())
    return "".join(c for c in value if unicodedata.category(c) != "Mn").strip()


def meters(a, b):
    lat1, lat2 = math.radians(float(a["latitude"])), math.radians(float(b["latitude"]))
    dlat = lat2 - lat1
    dlon = math.radians(float(b["longitude"]) - float(a["longitude"]))
    h = math.sin(dlat/2)**2 + math.cos(lat1)*math.cos(lat2)*math.sin(dlon/2)**2
    return 2*6371000*math.asin(min(1.0, math.sqrt(h)))


def rejection(row):
    if row.get("importGate") != "ADDRESS_REVIEW":
        return "not_clean_gps_only"
    if row.get("originalLiveHeuristic") != "UNMATCHED_REQUIRES_REVIEW":
        return "duplicate_risk"
    if row.get("approvedForImport") is not False:
        raise ValueError("Unexpected approved review source: " + str(row.get("sourceReference")))
    if row.get("venueType") not in VENUE_TYPES:
        return "not_suitable_type"
    if not set((row.get("sports") or "").split(";")) & FOCUS_SPORTS:
        return "not_a_session_sport"
    name = norm(row.get("name"))
    if len(name) < 9 or "sem nome" in name or "por confirmar" in name or "(osm #" in name:
        return "weak_or_generated_name"
    if any(word in name for word in BLOCKED_WORDS):
        return "closed_private_or_specialized_name_review"
    if name in {norm(n) for n in GENERIC_NAMES}:
        return "generic_unresolved_identity"
    if "morada por confirmar" not in (row.get("address") or "").casefold():
        raise ValueError("GPS-only address unexpectedly looks verified")
    lat, lon = row.get("latitude"), row.get("longitude")
    if (not isinstance(lat, (int,float)) or isinstance(lat,bool)
            or not isinstance(lon,(int,float)) or isinstance(lon,bool)
            or not math.isfinite(lat) or not math.isfinite(lon)
            or not 36.8 <= lat <= 42.3 or not -9.6 <= lon <= -6):
        return "invalid_mainland_gps"
    if row.get("district") not in DISTRICTS:
        return "unknown_district"
    if not row.get("city"):
        return "unknown_municipality"
    osm_url(row.get("sourceReference"))
    return "eligible_for_independent_review"


def priority(row):
    name = norm(row["name"])
    sports = set(row["sports"].split(";"))
    team = {"Futebol", "Futsal", "Basquetebol", "Andebol", "Voleibol",
            "Padel", "Ténis", "Rugby", "Hóquei", "Pickleball"}
    result = 8 if sports & team else 5
    result += min(3, max(0, len(sports)-1))
    if row["venueType"] in ("Complexo desportivo", "Pavilhão desportivo"):
        result += 3
    if any(token in name for token in (
        "municipal", "polidesportivo", "campo de futebol", "campo de jogos",
        "pavilhao", "complexo desportivo", "piscinas municipais")):
        result += 4
    if any(token in name for token in ("estadio", "clube desportivo", "futebol clube")):
        result += 2
    # Score is review priority only, never a claim of public or free access.
    return result


def choose_gps(rows, *, per_district=5, require_18=True):
    if per_district < 1 or per_district > 30:
        raise ValueError("per_district must be in 1..30")
    refs = [x["sourceReference"] for x in rows]
    if len(refs) != len(set(refs)):
        raise ValueError("Duplicate OSM source reference in snapshot")
    counts = Counter()
    buckets = defaultdict(list)
    for row in rows:
        why = rejection(row)
        counts[why] += 1
        if why == "eligible_for_independent_review":
            buckets[row["district"]].append(row)
    selected = []
    per_d = {}
    for district in DISTRICTS:
        choices = sorted(buckets[district], key=lambda x: (
            -priority(x), norm(x["city"]), norm(x["name"]), x["sourceReference"]))
        picks = []
        used_city = set()
        used_city_name = set()
        def add_candidates(require_new_city):
            for row in choices:
                if len(picks) >= per_district:
                    break
                city = norm(row["city"])
                signature = (city, norm(row["name"]))
                if signature in used_city_name or (require_new_city and city in used_city):
                    continue
                if any(meters(row, other) < 120 for other in picks):
                    continue  # Avoid stacking one complex; keep for later review.
                picks.append(row)
                used_city.add(city)
                used_city_name.add(signature)
        add_candidates(require_new_city=True)
        add_candidates(require_new_city=False)
        per_d[district] = len(picks)
        if require_18 and len(picks) != per_district:
            raise ValueError(f"Insufficient geographic GPS candidates for {district}: {len(picks)}")
        selected.extend(picks)
    if len(set(r["sourceReference"] for r in selected)) != len(selected):
        raise AssertionError("Duplicated selected OSM refs")
    return selected, dict(
        source_total=len(rows), gps_only_candidates=counts["eligible_for_independent_review"]
                              + sum(v for k,v in counts.items() if k!="not_clean_gps_only"
                                    and k!="eligible_for_independent_review"),
        gps_review_eligible=counts["eligible_for_independent_review"],
        rejection_counts=dict(sorted(counts.items())),
        district_quota=per_d, districts_covered=sum(bool(v) for v in per_d.values()),
        selected_for_review=len(selected), approved_for_import=0,
        any_database_write=False,
    )


def generate(source, out_dir, *, per_district=5):
    source = Path(source)
    digest=hashlib.sha256(source.read_bytes()).hexdigest()
    if digest != EXPECTED_SOURCE_SHA256:
        raise ValueError("Source snapshot differs from SHA-verified 3514-candidate archive")
    with gzip.open(source, "rt", encoding="utf-8") as stream:
        rows=[json.loads(line) for line in stream if line.strip()]
    if len(rows) != 3514:
        raise ValueError("Incomplete national snapshot")
    selected, report = choose_gps(rows, per_district=per_district)
    report["snapshot_sha256"] = digest
    report["no_postal_address_invented"] = True
    report["notes"] = ("GPS pins are original OSM points; address, facility operation, "
                       "independent identity, sub-venue relationship and use conditions "
                       "are NOT verified. No automatic approval or publication.")
    output_dir = Path(out_dir)
    output_dir.mkdir(parents=True, exist_ok=True)
    with (output_dir / "national-gps-only-review.csv").open(
             "w", newline="", encoding="utf-8-sig") as dest:
        writer = csv.DictWriter(dest, delimiter=";", fieldnames=CSV_FIELDS)
        writer.writeheader()
        for i, item in enumerate(selected, 1):
            lat,lon=item["latitude"],item["longitude"]
            # This count is a review hint: nearby features might be courts
            # belonging to one sports complex, not necessarily duplicates.
            neighbors=sum(1 for other in rows if other["sourceReference"] != item["sourceReference"]
                          and meters(item, other) <= 150)
            writer.writerow({
                "priority": i, "district": item["district"], "city": item["city"],
                "name": item["name"], "sports": item["sports"],
                "venue_type": item["venueType"], "latitude": lat, "longitude": lon,
                "osm_reference": item["sourceReference"],
                "osm_reference_url": osm_url(item["sourceReference"]),
                "openstreetmap_pin_url": f"https://www.openstreetmap.org/?mlat={lat:.6f}&mlon={lon:.6f}#map=17/{lat:.6f}/{lon:.6f}",
                "postal_address_status": "UNKNOWN_GPS_ONLY",
                "gps_source_status": "OSM_NOT_INDEPENDENTLY_CONFIRMED",
                "nearby_osm_candidates_150m": neighbors,
                "pending_32_municipal_alias_check": str(item["district"] in NEEDS_MUNICIPAL_ALIAS_REVIEW).lower(),
                "independent_identity_verified": "NO",
                "current_operation_verified": "NO",
                "access_and_restrictions_verified": "NO",
                "approved_for_import": "NO",
                "review_status": "AWAITING_INDEPENDENT_EVIDENCE",
                "review_notes": "",
            })
    (output_dir / "national-gps-only-summary.json").write_text(
        json.dumps(report, ensure_ascii=False, indent=2)+"\n", encoding="utf-8")
    return report


def main():
    p=argparse.ArgumentParser(description=__doc__)
    p.add_argument("--full-details", type=Path, required=True)
    p.add_argument("--output-dir", type=Path, required=True)
    p.add_argument("--per-district", type=int, default=5)
    args=p.parse_args()
    print(json.dumps(generate(args.full_details,args.output_dir,
                              per_district=args.per_district),
                     ensure_ascii=False, indent=2))


if __name__ == "__main__":
    main()

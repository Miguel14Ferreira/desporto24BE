#!/usr/bin/env python3
"""Read-only LIVE Jynlo venue distribution by district, without contact export.

Counts catalogue entries, not Portugal's real-world coverage or availability.
"""
from __future__ import annotations

import argparse
from collections import Counter, defaultdict
import json
from pathlib import Path

from audit_venue_public_contacts import (
    DISTRICTS, DISTRICT_LOOKUP, check_catalogue, fold, load_public_catalogue,
)

ISLANDS = {"acores": "Açores", "madeira": "Madeira"}
DISTRICT_NAMES = (*DISTRICTS, "Açores", "Madeira", "(por confirmar)")


def district_of(text):
    folded = fold(text)
    if folded in DISTRICT_LOOKUP:
        return DISTRICT_LOOKUP[folded]
    return ISLANDS.get(folded, "(por confirmar)")


def analyze(catalogue):
    check_catalogue(catalogue)
    buckets = {name: Counter() for name in DISTRICT_NAMES}
    cities = defaultdict(Counter)
    for item in catalogue:
        region = district_of(item.get("district"))
        buckets[region]["total"] += 1
        if not str(item.get("sports") or "").strip():
            buckets[region]["withoutSports"] += 1
        lat, lon = item.get("latitude"), item.get("longitude")
        if not isinstance(lat, (int, float)) or not isinstance(lon, (int, float)):
            buckets[region]["withoutGps"] += 1
        city = str(item.get("city") or "").strip()
        if city:
            cities[region][city] += 1
        else:
            buckets[region]["withoutCity"] += 1
    rows = []
    for district in DISTRICT_NAMES:
        row = {"district": district, "total": buckets[district]["total"],
               "withoutSports": buckets[district]["withoutSports"],
               "withoutGps": buckets[district]["withoutGps"],
               "withoutCity": buckets[district]["withoutCity"],
               "listedCities": len(cities[district]),
               "mostCommonCities": [
                   {"city": name, "count": count}
                   for name, count in sorted(cities[district].items(),
                                             key=lambda pair: (-pair[1], pair[0]))[:5]
               ]}
        rows.append(row)
    assert sum(row["total"] for row in rows) == len(catalogue)
    return {
        "total": len(catalogue), "districts": rows,
        "lowestMainlandCounts": sorted(
            ({"district": row["district"], "total": row["total"]} for row in rows
             if row["district"] in DISTRICTS),
            key=lambda entry: (entry["total"], entry["district"]))[:8],
        "note": "Public catalogue counts only; no completeness denominator and no proof of access/bookability",
    }


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    g = parser.add_mutually_exclusive_group(required=True)
    g.add_argument("--live-public-api", action="store_true")
    g.add_argument("--venues-json", type=Path)
    parser.add_argument("--output-json", type=Path, required=True)
    args = parser.parse_args()
    catalogue = (load_public_catalogue() if args.live_public_api else
                 json.loads(args.venues_json.read_text(encoding="utf-8")))
    result = analyze(catalogue)
    args.output_json.parent.mkdir(parents=True, exist_ok=True)
    args.output_json.write_text(
        json.dumps(result, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")
    print("LIVE total:", result["total"])
    for row in result["districts"]:
        print(row["district"], "|", row["total"], "| without sports:",
              row["withoutSports"], "| without GPS:", row["withoutGps"],
              "| cities:", row["listedCities"])
    print("Lowest 8 mainland catalogue counts:", result["lowestMainlandCounts"])
    print("This does NOT measure percentage coverage of all real installations.")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())

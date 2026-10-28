#!/usr/bin/env python3
"""Strict offline aggregate review plan from the Jynlo public venue catalogue."""
from __future__ import annotations

import argparse
import json
from pathlib import Path

from audit_live_venue_district_coverage import DISTRICTS, DISTRICT_NAMES

FIELDS = ("total", "withoutSports", "withoutGps", "withoutCity", "listedCities")
REGIONS = tuple(DISTRICT_NAMES[:-1])
LIMIT_BYTES = 5_000_000


def metric(row, name, region):
    value = row.get(name)
    if isinstance(value, bool) or not isinstance(value, int) or value < 0:
        raise ValueError(f"{region}.{name} must be a nonnegative integer")
    return value


def build_plan(snapshot, pilot=None):
    """Reject missing regions or misleading counts; never export venue-level data."""
    if not isinstance(snapshot, dict) or not isinstance(snapshot.get("districts"), list):
        raise ValueError("Expected JSON from audit_live_venue_district_coverage.py")
    total = snapshot.get("total")
    if isinstance(total, bool) or not isinstance(total, int) or total < 0:
        raise ValueError("total must be a nonnegative integer")
    if len(snapshot["districts"]) != len(DISTRICT_NAMES):
        raise ValueError("All mainland, island and unconfirmed rows must be present")

    by_region = {}
    for item in snapshot["districts"]:
        if not isinstance(item, dict):
            raise ValueError("Invalid district row")
        region = item.get("district")
        if not isinstance(region, str) or region not in DISTRICT_NAMES:
            raise ValueError(f"Unexpected district: {region!r}")
        if region in by_region:
            raise ValueError(f"Duplicate district: {region}")
        values = {key: metric(item, key, region) for key in FIELDS}
        if any(values[key] > values["total"] for key in FIELDS[1:]):
            raise ValueError(f"{region}: subset count exceeds total")
        by_region[region] = {
            "district": region, "catalogueCount": values["total"],
            "withoutSports": values["withoutSports"], "withoutGps": values["withoutGps"],
            "withoutCity": values["withoutCity"], "listedCities": values["listedCities"]
        }

    if set(by_region) != set(DISTRICT_NAMES):
        raise ValueError("Missing district or island")
    if sum(row["catalogueCount"] for row in by_region.values()) != total:
        raise ValueError("District counts do not match total")
    if pilot is not None and pilot not in REGIONS:
        raise ValueError("Pilot must be an explicitly selected district or island")

    rows = [by_region[name] for name in DISTRICT_NAMES]
    lowest = sorted(
        ({"district": name, "catalogueCount": by_region[name]["catalogueCount"]}
         for name in DISTRICTS),
        key=lambda row: (row["catalogueCount"], row["district"]))[:8]
    quality = sorted(
        ({"district": row["district"], "withoutSports": row["withoutSports"],
          "withoutGps": row["withoutGps"], "withoutCity": row["withoutCity"]}
         for row in rows
         if row["withoutSports"] or row["withoutGps"] or row["withoutCity"]),
        key=lambda row: (-max(row["withoutSports"], row["withoutGps"],
                              row["withoutCity"]), row["district"]))[:8]

    return {
        "kind": "JYNLO_NATIONAL_VENUE_REVIEW_PLAN",
        "source": "public-catalogue-aggregate-audit",
        "scope": {"mainlandDistricts": 18, "islandRegions": 2,
                  "unconfirmedDistrictGroup": 1},
        "catalogueTotal": total,
        "regions": rows,
        "lowestMainlandCatalogueCounts": lowest,
        "dataQualityReview": quality,
        "pilot": ({
            "district": pilot, "catalogueCount": by_region[pilot]["catalogueCount"],
            "bbox": None, "approvalForImport": False, "status": "PREPARATION_ONLY"
        } if pilot else None),
        "authorization": "READ_ONLY_NO_IMPORT",
        "warnings": [
            "Public catalogue rows are not all real venues or confirmed public access.",
            "Zero catalogue rows never proves that no real venues exist.",
            "Missing sports/GPS/city counts overlap and cannot be added as unique venues.",
            "A selected pilot never authorizes OSM network collection or database writes.",
            "No contacts, venue names, city names, addresses or GPS are exported."
        ]
    }


def main(argv=None):
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--coverage", type=Path, required=True)
    parser.add_argument("--out", type=Path, required=True)
    parser.add_argument("--pilot", choices=REGIONS)
    args = parser.parse_args(argv)
    if args.coverage.resolve() == args.out.resolve():
        parser.error("Cannot overwrite source coverage data")
    if not args.coverage.is_file() or args.coverage.stat().st_size > LIMIT_BYTES:
        parser.error("Missing or oversized coverage JSON (max 5 MB)")
    try:
        result = build_plan(json.loads(args.coverage.read_text(encoding="utf-8")),
                            args.pilot)
    except (ValueError, UnicodeError) as exc:
        parser.error(str(exc))
    args.out.parent.mkdir(parents=True, exist_ok=True)
    args.out.write_text(json.dumps(result, ensure_ascii=False, indent=2) + "\n",
                        encoding="utf-8")
    print(f"Offline plan created for {result['catalogueTotal']} catalogue records.")
    print("No API calls, no OSM import, no GPS lookup, no database access.")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())

#!/usr/bin/env python3
"""Braganca documented complex proximity review against Jynlo's public catalogue.

READ-ONLY: uses the independent documented complex point to check nearby names,
not to assign GPS to sports venues. No imports, updates, contact or GPS exports.
"""
from __future__ import annotations

import argparse
import json
from pathlib import Path
from urllib.parse import urlsplit

from audit_venue_public_contacts import check_catalogue, load_public_catalogue, fold
from audit_live_venue_gps_evidence import distance_m, valid_gps

ROOT = Path(__file__).resolve().parents[1]
EVIDENCE = ROOT / "docs/venue-imports/braganca-arnaldo-complex-gps-review-20261028.json"
MAX_DISTANCE_METERS = 250
MAX_MATCHES = 8
TRUSTED_SOURCE_HOST = "arquitecturaaqui.eu"


def validate(data):
    if not isinstance(data, dict) or data.get("approvedForImport") is not False:
        raise ValueError("Approved or malformed evidence is forbidden")
    points = data.get("points")
    if not isinstance(points, list) or len(points) != 1:
        raise ValueError("Expected precisely one research-site pin")
    point = points[0]
    if not isinstance(point, dict) or point.get("approvedForImport") is not False:
        raise ValueError("Research point must not be approved")
    if (point.get("name") != "Pavilhão Municipal Arnaldo Pereira"
            or point.get("city") != "Bragança" or point.get("district") != "Bragança"
            or not valid_gps(point.get("latitude"), point.get("longitude"))):
        raise ValueError("Unexpected identity or invalid GPS evidence")
    if not (41.75 < point["latitude"] < 41.85 and
            -6.85 < point["longitude"] < -6.70):
        raise ValueError("Site research point is outside Bragança")
    u = urlsplit(point.get("locationSourceUrl", ""))
    if (u.scheme != "https" or u.hostname != TRUSTED_SOURCE_HOST or
            u.username is not None or u.password is not None or
            u.port is not None or bool(u.query) or bool(u.fragment) or
            u.path != "/pt/obras/4824/pavilhao-municipal-arnaldo-pereira-braganca"):
        raise ValueError("Invalid independent research evidence URL")
    if data.get("precision") != "DOCUMENTED_COMPLEX_POINT_NOT_ENTRANCE":
        raise ValueError("Exact-entrance inference is forbidden")
    return point


def analyze(catalogue, evidence):
    check_catalogue(catalogue)
    point = validate(evidence)
    close = []
    names_in_district = []
    for item in catalogue:
        if fold(item.get("district")) != "braganca":
            continue
        if fold(item.get("name")) == fold(point["name"]):
            names_in_district.append({"id": item["id"],
                                      "name": item["name"],
                                      "city": str(item.get("city") or "")})
        lat, lon = item.get("latitude"), item.get("longitude")
        if not valid_gps(lat, lon):
            continue
        meters = distance_m(point["latitude"], point["longitude"], lat, lon)
        if meters <= MAX_DISTANCE_METERS:
            close.append((round(meters, 1), item))
    close.sort(key=lambda pair: (pair[0], pair[1]["id"]))
    nearby = [{
        "id": item["id"],
        "name": str(item.get("name") or ""),
        "city": str(item.get("city") or ""),
        "distanceFromResearchSiteMeters": dist
    } for dist, item in close[:MAX_MATCHES]]
    # Same-name matches may have no GPS, or may be catalogued under a parish.
    # Never assume a venue is new just because its coordinates are missing.
    return {
        "catalogueCount": len(catalogue),
        "candidate": point["name"],
        "district": point["district"],
        "sourceUrl": point["locationSourceUrl"],
        "precision": "RESEARCH_COMPLEX_POINT_NOT_VENUE_ENTRANCE",
        "radiusMeters": MAX_DISTANCE_METERS,
        "nearbyCatalogueCount": len(close),
        "nearbyCatalogue": nearby,
        "truncated": len(close) > MAX_MATCHES,
        "sameNameAcrossDistrict": sorted(names_in_district, key=lambda x: x["id"]),
        "status": ("NEARBY_FACILITIES_REVIEW" if close
                   else "NO_NEARBY_GPS_NOT_PROOF_OF_NEW_VENUE"),
        "approvalForImport": False,
        "writeActions": 0,
        "warnings": [
            "A short radius cannot prove that no duplicate venue exists.",
            "Distinct sports facilities can share the municipal complex and nearby GPS.",
            "Incomplete or incorrect public GPS and parish/city labels require review.",
            "Research pin is NOT an approved coordinate for the Jynlo map.",
            "No individual venue coordinates, contacts or addresses exported."
        ]
    }


def main(argv=None):
    parser = argparse.ArgumentParser(description=__doc__)
    group = parser.add_mutually_exclusive_group(required=True)
    group.add_argument("--live-public-api", action="store_true")
    group.add_argument("--venues-json", type=Path)
    parser.add_argument("--evidence", type=Path, default=EVIDENCE)
    parser.add_argument("--out", type=Path, required=True)
    args = parser.parse_args(argv)
    if args.evidence.resolve() == args.out.resolve() or (
            args.venues_json is not None and
            args.venues_json.resolve() == args.out.resolve()):
        parser.error("Output must not overwrite inputs")
    venue_data = (load_public_catalogue() if args.live_public_api else
                  json.loads(args.venues_json.read_text(encoding="utf-8")))
    result = analyze(venue_data,
                     json.loads(args.evidence.read_text(encoding="utf-8")))
    args.out.parent.mkdir(parents=True, exist_ok=True)
    args.out.write_text(json.dumps(result, ensure_ascii=False, indent=2) + "\n",
                        encoding="utf-8")
    print("JYNLO_CATALOGUE:", result["catalogueCount"])
    print("BRAGANCA_NEARBY:", json.dumps(result["nearbyCatalogue"], ensure_ascii=False))
    print("BRAGANCA_SAME_NAME:", json.dumps(result["sameNameAcrossDistrict"],
                                          ensure_ascii=False))
    print("BRAGANCA_REVIEW_STATUS:", result["status"])
    print("No GPS copied to live records, no import approval, no database writes.")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())

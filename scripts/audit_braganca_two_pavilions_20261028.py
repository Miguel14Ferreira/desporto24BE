#!/usr/bin/env python3
"""Research-only GPS comparison for two documented Braganca municipal pavilions.

Reads Jynlo public venue JSON, never uses admin credentials or writes to API/DB.
Source points are building/complex research locations, NOT approved entrances.
"""
from __future__ import annotations

import argparse
import json
from pathlib import Path
from urllib.parse import urlsplit

from audit_venue_public_contacts import check_catalogue, fold, load_public_catalogue
from audit_live_venue_gps_evidence import distance_m, valid_gps

ROOT = Path(__file__).resolve().parents[1]
EVIDENCE = ROOT / "docs/venue-imports/braganca-pavilions-two-sites-review-20261028.json"
NAMES = ("Pavilhão Municipal Arnaldo Pereira", "Pavilhão Polidesportivo da Coxa")
RADIUS_METERS = 250
MAX_RESULTS = 12
# City/parish scope is essential: a generic "Pavilhão Municipal" in another
# municipality of the Bragança district must not be treated as this pavilion.
LOCAL_CITIES = frozenset((
    "braganca", "se", "santa maria", "meixedo",
    "uniao das freguesias de se, santa maria e meixedo",
))
ARNALDO_HISTORICAL_NAMES = frozenset((
    "pavilhao municipal", "pavilhao municipal de braganca",
    "pavilhao gimnodesportivo", "pavilhao gimnodesportivo de braganca",
))
URL_HOSTS = frozenset(("arquitecturaaqui.eu", "www.alnconstrucoes.com",
                       "diariodarepublica.pt", "estrelasbrigantinas.com"))
EXPECTED_PRECISION = {
    NAMES[0]: "ARCHITECTURAL_COMPLEX_POINT_NOT_ENTRANCE",
    NAMES[1]: "CONTRACTOR_DOCUMENTED_BUILDING_SITE_NOT_ENTRANCE",
}


def validate_source_url(value):
    if not isinstance(value, str):
        return False
    parts = urlsplit(value)
    return (parts.scheme == "https" and parts.hostname in URL_HOSTS
            and parts.username is None and parts.password is None
            and parts.port is None and not parts.fragment and not parts.query
            and parts.path not in ("", "/"))


def validate(manifest):
    if (not isinstance(manifest, dict)
            or manifest.get("approvedForImport") is not False
            or manifest.get("status") != "SITE_POINTS_REQUIRE_DEDUP_AND_GPS_ENTRANCE_REVIEW"):
        raise ValueError("Review-only manifest required")
    venues = manifest.get("venues")
    if not isinstance(venues, list) or len(venues) != 2:
        raise ValueError("Exactly two reviewed pavilions required")
    if set(item.get("name") for item in venues if isinstance(item, dict)) != set(NAMES):
        raise ValueError("Pavilion identity mismatch or duplicate")
    for item in venues:
        if (item.get("approvedForImport") is not False
                or item.get("city") != "Bragança"
                or item.get("district") != "Bragança"
                or item.get("precision") != EXPECTED_PRECISION[item["name"]]
                or not valid_gps(item.get("latitude"), item.get("longitude"))):
            raise ValueError("Invalid location, approval or precision")
        if not (41.75 < item["latitude"] < 41.85
                and -6.85 < item["longitude"] < -6.70):
            raise ValueError("Point outside Bragança city region")
        if not all(validate_source_url(item.get(key))
                   for key in ("coordinateSource", "officialIdentitySource",
                               "sportsContextSource")):
            raise ValueError("Missing traceable official/source evidence")
    # Arrival evidence must never be silently promoted to a GPS entrance,
    # operational transport service, or a reservation.
    by_name = {v["name"]: v for v in venues}
    arnaldo = by_name[NAMES[0]].get("visitorArrivalEvidence")
    if (not isinstance(arnaldo, dict)
            or arnaldo.get("status") != "OFFICIAL_NAMED_BUS_STOP_NOT_ENTRANCE"
            or arnaldo.get("officialTransportUrl") !=
               "https://bus.cm-braganca.pt/routes"
            or arnaldo.get("stopName") != "Pavilhão Arnaldo Pereira"
            or arnaldo.get("listedRouteCodes") != ["R1", "R3", "R5", "R7"]
            or any(arnaldo.get(key) is not False for key in (
                "stopCoordinatesVerified", "pavilionDoorCoordinatesVerified",
                "timetablesVerified", "transportServiceGuaranteed",
                "venueBookingGuaranteed"))):
        raise ValueError("Official public transit evidence is not a certified arrival")
    coxa = by_name[NAMES[1]].get("visitorArrivalEvidence")
    if (not isinstance(coxa, dict)
            or coxa.get("status") != "PUBLIC_ENTRANCE_UNVERIFIED"
            or coxa.get("contractorFacadePhotoUrl") !=
               "https://www.alnconstrucoes.com/obras/2019/pavilhao-polidesportivo-da-coxa-braganca"
            or any(coxa.get(key) is not False for key in (
                "facadePhotoProvesCurrentPublicEntrance",
                "pavilionDoorCoordinatesVerified",
                "transportServiceGuaranteed", "venueBookingGuaranteed"))):
        raise ValueError("Contractor construction photo is not arrival proof")
    if distance_m(venues[0]["latitude"], venues[0]["longitude"],
                  venues[1]["latitude"], venues[1]["longitude"]) < 100:
        raise ValueError("The two sports halls cannot share the same point")
    return venues


def possible_alias(record, source_name):
    """Flag only plausible city/parish aliases; evidence, never identity proof."""
    district = fold(record.get("district"))
    if district not in ("", "braganca") or fold(record.get("city")) not in LOCAL_CITIES:
        return False
    name = fold(record.get("name"))
    if not name:
        return False
    # An exact name without district is also a candidate for human review.
    if name == fold(source_name):
        return district == ""
    if source_name == NAMES[0]:
        return name in ARNALDO_HISTORICAL_NAMES or "arnaldo pereira" in name
    return ("coxa" in name
            and any(term in name for term in ("pavilh", "polidesport", "gimnodesport")))


def analyze(catalogue, manifest):
    check_catalogue(catalogue)
    sources = validate(manifest)
    by_district = [v for v in catalogue if fold(v.get("district")) == "braganca"]
    report = []
    for source in sources:
        same_name = [
            {"id": v["id"], "name": str(v.get("name") or ""),
             "city": str(v.get("city") or "")}
            for v in by_district
            if fold(v.get("name")) == fold(source["name"])]
        aliases = [
            {"id": v["id"], "name": str(v.get("name") or ""),
             "city": str(v.get("city") or "")}
            for v in catalogue if possible_alias(v, source["name"])
        ]
        aliases.sort(key=lambda item: item["id"])
        nearby = []
        for v in by_district:
            lat, lon = v.get("latitude"), v.get("longitude")
            if not valid_gps(lat, lon):
                continue
            d = distance_m(source["latitude"], source["longitude"], lat, lon)
            if d <= RADIUS_METERS:
                nearby.append((round(d, 1), v))
        nearby.sort(key=lambda pair: (pair[0], pair[1]["id"]))
        nearby_results = [
            {"id": v["id"], "name": str(v.get("name") or ""),
             "city": str(v.get("city") or ""),
             "distanceFromSourceSiteMeters": d}
            for d, v in nearby[:MAX_RESULTS]]
        status = ("EXACT_NAME_REVIEW" if same_name
                  else "POSSIBLE_ALIAS_REVIEW" if aliases
                  else "NEARBY_DISTINCT_FACILITIES_REVIEW" if nearby
                  else "NO_NEARBY_GPS_NOT_PROOF_OF_ABSENCE")
        report.append({
            "candidate": source["name"], "city": source["city"],
            "district": source["district"],
            "documentedSiteSource": source["coordinateSource"],
            "sourcePrecision": source["precision"],
            "arrivalEvidenceStatus": source["visitorArrivalEvidence"]["status"],
            "documentedBusStopName": (
                source["visitorArrivalEvidence"].get("stopName")),
            "documentedRouteCodes": (
                source["visitorArrivalEvidence"].get("listedRouteCodes", [])),
            "arrivalPointVerified": False,
            "sameNameInDistrict": sorted(same_name, key=lambda v: v["id"]),
            "possibleAliasCount": len(aliases),
            "possibleAliases": aliases[:MAX_RESULTS],
            "aliasResultsTruncated": len(aliases) > MAX_RESULTS,
            "nearbyCount": len(nearby), "nearby": nearby_results,
            "resultsTruncated": len(nearby) > MAX_RESULTS,
            "radiusMeters": RADIUS_METERS, "status": status,
            "approvedForImport": False,
            "caution": ("Shared sports complex does not prove identity; "
                        "exact entrance and existing aliases must be verified.")
        })
    return {"catalogueTotal": len(catalogue),
            "reviewedSites": len(report),
            "candidates": report,
            "approvedCount": 0, "writeActions": 0,
            "warnings": [
                "Official tender proves separate sites; a close coordinate cannot identify a building.",
                "Research and contractor points are NOT certified entrances for Jynlo sessions.",
                "Municipal emergency plan historic Arnaldo latitude conflicts with independent evidence.",
                "Nearby venues may have incorrect/missing GPS or alternative names.",
                "Potential aliases are review leads, not verified duplicate identities.",
                "Municipal bus stop names are not certified venue doors or guaranteed services.",
                "Construction photographs are not reliable 2026 visitor access instructions.",
                "No individual coordinates, addresses, contacts or source points copied to output."
            ]}


def main(argv=None):
    parser = argparse.ArgumentParser(description=__doc__)
    p = parser.add_mutually_exclusive_group(required=True)
    p.add_argument("--live-public-api", action="store_true")
    p.add_argument("--venues-json", type=Path)
    parser.add_argument("--evidence", type=Path, default=EVIDENCE)
    parser.add_argument("--out", type=Path, required=True)
    args = parser.parse_args(argv)
    if args.evidence.resolve() == args.out.resolve() or (
            args.venues_json is not None and args.venues_json.resolve() == args.out.resolve()):
        parser.error("Output must not replace evidence or public catalogue")
    venues = (load_public_catalogue() if args.live_public_api else
              json.loads(args.venues_json.read_text(encoding="utf-8")))
    report = analyze(venues, json.loads(args.evidence.read_text(encoding="utf-8")))
    args.out.parent.mkdir(parents=True, exist_ok=True)
    args.out.write_text(json.dumps(report, ensure_ascii=False, indent=2) + "\n",
                        encoding="utf-8")
    print("PUBLIC_CATALOGUE_TOTAL:", report["catalogueTotal"])
    for row in report["candidates"]:
        print("PAVILION:", row["candidate"], "|", row["status"],
              "| nearby:", row["nearbyCount"])
        print("NEARBY:", json.dumps(row["nearby"], ensure_ascii=False))
        print("SAME_NAME:", json.dumps(row["sameNameInDistrict"], ensure_ascii=False))
        print("POSSIBLE_ALIASES:", json.dumps(row["possibleAliases"], ensure_ascii=False))
        print("POSSIBLE_ALIAS_COUNT:", row["possibleAliasCount"])
    print("Read only. No contacts, addresses or individual venue GPS exported.")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())

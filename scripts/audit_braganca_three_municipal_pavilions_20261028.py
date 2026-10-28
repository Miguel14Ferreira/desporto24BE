#!/usr/bin/env python3
"""Research-only check of three Bragança-district municipal sports halls.

Reads fixed public Jynlo GET or local JSON. Historical guide locations are
NOT approved entrances; no API writes, database access or file overwrites.
"""
from __future__ import annotations
import argparse
import json
from pathlib import Path
from urllib.parse import urlsplit

from audit_venue_public_contacts import check_catalogue, fold, load_public_catalogue
from audit_live_venue_gps_evidence import distance_m, valid_gps

ROOT = Path(__file__).resolve().parents[1]
EVIDENCE = ROOT / "docs/venue-imports/braganca-three-municipal-pavilions-review-20261028.json"
EXPECTED = {
    "Freixo de Espada à Cinta": (41.087873, -6.809408, 46),
    "Macedo de Cavaleiros": (41.5385759, -6.96886618, 54),
    "Vila Flor": (41.307304, -7.152449, 88),
}
GUIDE = "https://www.cm-vilaflor.pt/uploads/writer_file/document/1736/GUIA_BRAGAN_A.pdf"
ALLOWED_EVIDENCE_HOSTS = frozenset(("www.canaln.tv", "zerozero.football",
                                     "escolasvilaflor.net"))
MAX_RESULTS = 10
RADIUS_M = 250

def _safe_https(url, expected_host=None):
    if not isinstance(url, str):
        return False
    u = urlsplit(url)
    return (u.scheme == "https" and bool(u.path.strip("/")) and
            u.username is None and u.password is None and u.port is None and
            not u.query and not u.fragment and
            (u.hostname == expected_host if expected_host
             else u.hostname in ALLOWED_EVIDENCE_HOSTS))

def validate(data):
    if (not isinstance(data, dict)
            or data.get("status") != "THREE_DISTRICT_PAVILIONS_REVIEW_ONLY"
            or data.get("authorization") != "READ_ONLY_NO_IMPORT"
            or data.get("approvedForImport") is not False
            or data.get("guideSurveyPeriod") != "2016"
            or data.get("guideSourceUrl") != GUIDE):
        raise ValueError("Only historical, read-only evidence is accepted")
    sources = data.get("sites")
    if not isinstance(sources, list) or len(sources) != 3:
        raise ValueError("Three candidates required")
    cities = set()
    for v in sources:
        if not isinstance(v, dict):
            raise ValueError("Malformed candidate")
        city = v.get("city")
        if (city not in EXPECTED or city in cities
                or v.get("district") != "Bragança"
                or v.get("approvedForImport") is not False
                or not isinstance(v.get("name"), str)
                or not fold(v["name"]).startswith("pavilh")
                or v.get("coordinatePrecision") != "2016_GUIDE_FACILITY_SITE_NOT_ENTRANCE"
                or v.get("coordinateSourceUrl") != GUIDE
                or (v.get("latitude"), v.get("longitude"),
                    v.get("guidePrintedPage")) != EXPECTED[city]
                or not valid_gps(v.get("latitude"), v.get("longitude"))
                or not _safe_https(v.get("independentSourceUrl"))):
            raise ValueError("Unverified identity, historic GPS or source")
        if city == "Macedo de Cavaleiros":
            reg = v.get("historicalRegulation")
            if (not isinstance(reg, dict)
                    or not _safe_https(reg.get("sourceUrl"),
                         "www.cm-macedodecavaleiros.pt")
                    or reg.get("sourceType") != "HISTORICAL_MUNICIPAL_REGULATION_DATE_NOT_VERIFIED"
                    or reg.get("regulatedVenueName") !=
                    "Pavilhão Desportivo de Macedo de Cavaleiros"
                    or reg.get("describedLocation") !=
                    "School complex of Macedo de Cavaleiros"
                    or reg.get("priorWrittenAuthorizationSpecified") is not True
                    or any(reg.get(key) is not False for key in (
                        "currentApplicabilityVerified",
                        "samePhysicalBuildingAs2016GuideVerified",
                        "currentBookingRulesVerified",
                        "currentAvailabilityVerified"))):
                raise ValueError("Historical municipal access limits are unverified")
        if city == "Vila Flor":
            if (v.get("independentSourceDate") is not None
                    or v.get("evidenceKind") != "SCHOOL_MUNICIPAL_USE_PAGE_UNDATED"):
                raise ValueError("Vila Flor has no dated 2026 corroboration")
        elif (v.get("independentSourceDate") not in ("2026-10-18", "2026-10-24")
              or v.get("evidenceKind") != "RECENT_REPORTED_SPORTS_EVENT"):
            raise ValueError("Recent sports event must be dated and qualified")
        cities.add(city)
    return sources

def analyze(catalogue, manifest):
    check_catalogue(catalogue)
    sites = validate(manifest)
    results = []
    for site in sites:
        # District may be empty in older rows. Only include an unclassified
        # row when its city explicitly agrees with the reviewed municipality.
        district_rows = [
            v for v in catalogue
            if fold(v.get("district")) == "braganca"
            or (not fold(v.get("district"))
                and fold(v.get("city")) == fold(site["city"]))
        ]
        local = [v for v in district_rows
                 if fold(v.get("city")) == fold(site["city"])]
        # A district-wide identical name might refer to another municipality
        # or to a parish label. Record it, but never claim local identity.
        exact_local = [v for v in local
                       if fold(v.get("name")) == fold(site["name"])]
        district_name_other_city = [
            v for v in district_rows if v not in local
            and fold(v.get("name")) == fold(site["name"])]
        local_named = [
            v for v in local
            if any(part in fold(v.get("name"))
                   for part in ("pavilh", "gimnodesport", "polidesport"))
        ]
        missing_district = sum(not fold(v.get("district")) for v in local_named)
        near = []
        for v in district_rows:
            lat, lon = v.get("latitude"), v.get("longitude")
            if valid_gps(lat, lon):
                meters = distance_m(site["latitude"], site["longitude"], lat, lon)
                if meters <= RADIUS_M:
                    near.append((round(meters, 1), v))
        local_named.sort(key=lambda v: v["id"])
        near.sort(key=lambda pair: (pair[0], pair[1]["id"]))
        status = ("EXACT_LOCAL_NAME_REVIEW" if exact_local
                  else "LOCAL_PAVILION_ALIAS_REVIEW" if local_named
                  else "DISTRICT_NAME_COLLISION_REVIEW" if district_name_other_city
                  else "NEARBY_FACILITIES_REVIEW" if near
                  else "NO_LOCAL_MATCH_NOT_PROOF_OF_NEW_SITE")
        results.append({
            "candidate": site["name"], "city": site["city"], "district": "Bragança",
            "status": status,
            "exactLocalNameIds": sorted(v["id"] for v in exact_local)[:MAX_RESULTS],
            "districtOtherCityNameCount": len(district_name_other_city),
            "districtOtherCityNameIds": sorted(
                v["id"] for v in district_name_other_city)[:MAX_RESULTS],
            "localPavilionCount": len(local_named),
            "localPavilionsWithMissingDistrict": missing_district,
            "localPavilions": [{"id": v["id"], "name": str(v.get("name") or "")}
                               for v in local_named[:MAX_RESULTS]],
            "nearbyCount": len(near),
            "nearby": [{"id": v["id"], "name": str(v.get("name") or ""),
                        "distanceFromHistoricalSiteMeters": meters}
                       for meters, v in near[:MAX_RESULTS]],
            "resultsTruncated": (
                len(local_named) > MAX_RESULTS
                or len(near) > MAX_RESULTS
                or len(exact_local) > MAX_RESULTS
                or len(district_name_other_city) > MAX_RESULTS),
            "historicalCoordinateSource": GUIDE,
            "sourcePrecision": "2016_GUIDE_FACILITY_SITE_NOT_ENTRANCE",
            "approvedForImport": False,
        })
    return {"catalogueTotal": len(catalogue), "reviewedSites": 3,
            "results": results, "approvedCount": 0, "writeActions": 0,
            "warnings": ["Historic guide site coordinates are not certified entrances.",
                         "Nearby venues and aliases require human inspection.",
                         "Matching names in another town are review leads, never confirmed identities.",
                         "Missing district is accepted only for the same named municipality.",
                         "No individual GPS, street address or contact is exported.",
                         "No import or production database access has been authorized."]}

def main(argv=None):
    p = argparse.ArgumentParser(description=__doc__)
    group = p.add_mutually_exclusive_group(required=True)
    group.add_argument("--live-public-api", action="store_true")
    group.add_argument("--venues-json", type=Path)
    p.add_argument("--evidence", type=Path, default=EVIDENCE)
    p.add_argument("--out", type=Path, required=True)
    a = p.parse_args(argv)
    if a.out.resolve() == a.evidence.resolve() or (
            a.venues_json and a.out.resolve() == a.venues_json.resolve()):
        p.error("Refusing to overwrite a source file")
    data = load_public_catalogue() if a.live_public_api else json.loads(
        a.venues_json.read_text(encoding="utf-8"))
    report = analyze(data, json.loads(a.evidence.read_text(encoding="utf-8")))
    a.out.parent.mkdir(parents=True, exist_ok=True)
    a.out.write_text(json.dumps(report, ensure_ascii=False, indent=2) + "\n",
                     encoding="utf-8")
    print("CATALOGUE_TOTAL:", report["catalogueTotal"])
    for row in report["results"]:
        print("SITE:", row["city"], "|", row["status"],
              "| aliases:", row["localPavilionCount"], "| nearby:", row["nearbyCount"])
        print("LOCAL:", json.dumps(row["localPavilions"], ensure_ascii=False))
        print("NEARBY:", json.dumps(row["nearby"], ensure_ascii=False))
    print("READ ONLY: no import, contact or GPS correction")
    return 0

if __name__ == "__main__":
    raise SystemExit(main())

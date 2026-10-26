#!/usr/bin/env python3
"""Read-only compare 33 pre-reviewed Portalegre OSM candidates with LIVE Jynlo.

Sources are *historical documentary evidence*, not permission to import.
Never opens a DB connection, calls POST, exports contact data or approves venues.
"""
from __future__ import annotations

import argparse
from collections import Counter
import json
import math
from pathlib import Path
import re
import unicodedata
from urllib.parse import urlsplit

from audit_venue_public_contacts import check_catalogue, load_public_catalogue

OSM_ID = re.compile(r"OSM:(NODE|WAY|RELATION):[1-9][0-9]*\Z")
GENERIC = frozenset({
    "de", "do", "da", "dos", "das", "e", "em", "n", "municipal", "municipais",
    "complexo", "desportivo", "desportiva", "gimnodesportivo", "gimnodesportiva",
    "estadio", "pavilhao", "piscina", "piscinas", "cobertas", "descobertas",
    "campo", "campos", "parque", "aquatico", "jogos", "futebol", "ginásio", "ginasio",
    "polidesportivo", "atletismo", "atletica", "tenis", "relvado", "sintetico",
    "pista", "recinto", "multiusos",
})
TYPES = {
    "swimming": ("piscin", "aquatic"),
    "stadium": ("estadi",),
    "pavilion": ("pavilh", "gimnodesportiv"),
    "sports_field": ("campo", "polidesportiv", "multiusos"),
    "tennis": ("tenis",),
    "gym": ("ginasio",),
}
MAX_DISPLAY = 6
NEARBY_METERS = 200


def normalized(s):
    value = unicodedata.normalize("NFKD", str(s or "").casefold())
    value = "".join(c for c in value if not unicodedata.combining(c))
    return re.sub(r"[^a-z0-9]+", " ", value).strip()


def tokens(text):
    return {t for t in normalized(text).split()
            if len(t) >= 4 and t not in GENERIC}


def kind(name):
    n = normalized(name)
    for label, variants in TYPES.items():
        if any(t in n for t in variants):
            return label
    return None


def valid_https(url):
    if not isinstance(url, str):
        return False
    try:
        p = urlsplit(url)
        return (p.scheme == "https" and bool(p.hostname) and p.port is None
                and p.username is None and p.password is None
                and p.hostname.count(".") >= 1 and not p.fragment)
    except ValueError:
        return False


def records_from_manifests(second_batch, official_eleven, first_pilot):
    inputs = [
        ("portalegre_batch2_13", second_batch.get("venues"), 13),
        ("portalegre_official_11", official_eleven.get("venues"), 11),
        ("portalegre_pilot_9", first_pilot.get("shortlisted"), 9),
    ]
    if second_batch.get("approved") != 0 or second_batch.get("imported") != 0:
        raise ValueError("13-candidate batch unexpectedly marked approved/imported")
    if official_eleven.get("recordCount") != 11:
        raise ValueError("11-candidate source count changed")
    if first_pilot.get("reviewStatus") != "INDEPENDENT_SOURCES_FOUND__AWAITING_OPERATOR_APPROVAL":
        raise ValueError("First-pilot review status changed")
    result = []
    seen = set()
    for scope, entries, expected_count in inputs:
        if not isinstance(entries, list) or len(entries) != expected_count:
            raise ValueError(f"{scope}: expected {expected_count} entries")
        for entry in entries:
            if not isinstance(entry, dict):
                raise ValueError("Candidate must be a JSON object")
            if scope == "portalegre_batch2_13":
                reference = entry.get("osmReference")
                name, city = entry.get("originalName"), entry.get("municipality")
                url = entry.get("independentSource")
                if entry.get("approvedForLiquibase") is not False:
                    raise ValueError("Batch 2 proposal is not approved")
            elif scope == "portalegre_official_11":
                reference = entry.get("sourceReference")
                name, city = entry.get("name"), entry.get("city")
                url = entry.get("independentSource")
                if entry.get("approvedForImport") is not False:
                    raise ValueError("11-source candidate is not approved")
            else:
                reference = entry.get("osmReference")
                name, city = entry.get("name"), entry.get("city")
                url = entry.get("sourceUrl")
            if not isinstance(reference, str) or not OSM_ID.fullmatch(reference):
                raise ValueError("Invalid OSM identifier")
            if reference in seen:
                raise ValueError(f"Repeated OSM identifier across sources: {reference}")
            seen.add(reference)
            if not isinstance(name, str) or not name.strip() or len(name) > 160:
                raise ValueError("Invalid facility name")
            if not isinstance(city, str) or not city.strip() or len(city) > 120:
                raise ValueError("Invalid facility locality")
            if not valid_https(url):
                raise ValueError(f"{reference}: HTTPS documentary evidence required")
            geo = entry.get("location") or {}
            if not isinstance(geo, dict):
                raise ValueError("Invalid OSM location")
            lat, lon = geo.get("latitude"), geo.get("longitude")
            if (lat is None) != (lon is None):
                raise ValueError("Coordinates must be paired or absent")
            if lat is not None and (
                type(lat) not in (float, int) or type(lon) not in (float, int)
                or not all(math.isfinite(x) for x in (lat, lon))
                or not 36.8 <= lat <= 42.3 or not -9.6 <= lon <= -6.0
            ):
                raise ValueError("Coordinates must be finite and within mainland bounds")
            result.append({
                "sourceBatch": scope, "sourceReference": reference,
                "name": name.strip(), "city": city.strip(), "district": "Portalegre",
                "sourceUrl": url,
                "latitude": lat, "longitude": lon,
                "approvedForImport": False,
            })
    if len(result) != 33:
        raise ValueError("Expected 33 different candidate references")
    return result


def meters_between(left, venue):
    if left.get("latitude") is None:
        return None
    lat, lon = venue.get("latitude"), venue.get("longitude")
    if (type(lat) not in (float, int) or type(lon) not in (float, int)
            or not all(math.isfinite(x) for x in (lat, lon))):
        return None
    la, lb = math.radians(left["latitude"]), math.radians(lat)
    dp = math.radians(lat - left["latitude"])
    dl = math.radians(lon - left["longitude"])
    h = math.sin(dp / 2) ** 2 + math.cos(la) * math.cos(lb) * math.sin(dl / 2) ** 2
    return round(12742000 * math.asin(min(1, math.sqrt(h))))


def evaluate(candidate, catalogue):
    city_name = normalized(candidate["city"])
    title_name = normalized(candidate["name"])
    distinctive = tokens(candidate["name"]) - tokens(candidate["city"])
    ctype = kind(candidate["name"])
    matches = []
    for venue in catalogue:
        if normalized(venue.get("district")) != "portalegre":
            continue
        venue_name = normalized(venue.get("name"))
        same_city = normalized(venue.get("city")) == city_name
        exact = venue_name == title_name
        overlap = len(distinctive & (tokens(venue.get("name")) - tokens(candidate["city"])))
        vtype = kind(venue.get("name"))
        dist = meters_between(candidate, venue)
        near = dist is not None and dist <= NEARBY_METERS
        # Same name in different locality may be a district-level alias, but
        # is NOT a proven duplicate. Nearby OSM points may be distinct pitches.
        plausible = (
            exact or near or
            (same_city and overlap >= 1 and (ctype == vtype or overlap >= 2)) or
            (same_city and not distinctive and ctype is not None and ctype == vtype) or
            (not same_city and overlap >= 2 and ctype == vtype)
        )
        if plausible:
            matches.append({
                "id": venue["id"], "name": venue.get("name") or "",
                "city": venue.get("city") or "",
                "sameCity": same_city, "exactName": exact,
                "distanceMeters": dist, "nameTokenOverlap": overlap,
                "sameKind": bool(ctype and ctype == vtype),
            })
    matches.sort(key=lambda v: (
        not (v["sameCity"] and v["exactName"]),
        not (v["distanceMeters"] is not None and v["distanceMeters"] <= NEARBY_METERS),
        -v["nameTokenOverlap"],
        not v["sameKind"],
        v["id"],
    ))
    exact = [m for m in matches if m["exactName"] and m["sameCity"]]
    if len(exact) == 1:
        status = "EXACT_NAME_CITY_EXISTING"
    elif len(exact) > 1:
        status = "MULTIPLE_EXACT_REVIEW"
    elif matches:
        status = "POSSIBLE_ALIAS_OR_GPS_REVIEW"
    else:
        status = "NO_PLAUSIBLE_PUBLIC_MATCH_REVIEW"
    return {
        **{k: candidate[k] for k in (
            "sourceBatch", "sourceReference", "name", "city", "district", "sourceUrl")},
        "status": status, "possibleMatches": len(matches),
        "matchesTruncated": len(matches) > MAX_DISPLAY,
        "matches": matches[:MAX_DISPLAY],
        "sourceReferenceCheckedInDatabase": False,
        "approvedForImport": False,
    }


def audit(catalogue, source_rows):
    check_catalogue(catalogue)
    rows = [evaluate(row, catalogue) for row in source_rows]
    return {
        "catalogueTotal": len(catalogue),
        "sourceCandidateTotal": len(rows),
        "countByStatus": dict(sorted(Counter(row["status"] for row in rows).items())),
        "scope": "Public GET + documentary evidence only. NOT live database duplicate confirmation.",
        "safety": "No contact details, no import approvals or writes; OSM ODbL attribution required.",
        "candidates": rows,
    }


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    root = Path(__file__).resolve().parent.parent
    parser.add_argument("--manifest-dir", type=Path, default=root / "docs/venue-imports")
    group = parser.add_mutually_exclusive_group(required=True)
    group.add_argument("--live-public-api", action="store_true")
    group.add_argument("--venues-json", type=Path)
    parser.add_argument("--output-json", type=Path, required=True)
    args = parser.parse_args()
    sources = []
    for name in (
        "portalegre-2026-10-19-batch2-corroborated.json",
        "portalegre-11-municipal-sources-20261019.json",
        "portalegre-2026-10-19-source-review.json",
    ):
        sources.append(json.loads((args.manifest_dir / name).read_text(encoding="utf-8")))
    candidates = records_from_manifests(*sources)
    catalogue = (load_public_catalogue() if args.live_public_api else
                 json.loads(args.venues_json.read_text(encoding="utf-8")))
    result = audit(catalogue, candidates)
    args.output_json.parent.mkdir(parents=True, exist_ok=True)
    args.output_json.write_text(json.dumps(result, ensure_ascii=False, indent=2) + "\n",
                                encoding="utf-8")
    print("Catalogue LIVE count:", result["catalogueTotal"])
    print("Portalegre candidate count:", len(candidates))
    print("Status distribution:", result["countByStatus"])
    for row in result["candidates"]:
        print(row["sourceReference"], "|", row["name"], "|", row["status"],
              "| potential existing IDs:", [m["id"] for m in row["matches"]])
    print("No production insert, private contact export or booking claim.")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())

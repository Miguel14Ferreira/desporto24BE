#!/usr/bin/env python3
"""Read-only known Jynlo venue identity/GPS collision audit.

This is NOT deduplication, geocoding, GPS verification, or an import pipeline.
The public API can expose an OSM pin for a complex or a homonymous venue
without proving the entrance of a different sports facility.
"""
from __future__ import annotations

import argparse
import json
import math
from pathlib import Path

from audit_venue_public_contacts import check_catalogue, fold, load_public_catalogue

ROOT = Path(__file__).resolve().parents[1]
MANIFEST = ROOT / "docs/venue-imports/2026-10-27-known-venue-gps-collision-review.json"
RELATIONS = {
    "POTENTIAL_DUPLICATE_POOL": "POTENTIAL_DUPLICATE_REQUIRES_IDENTITY_REVIEW",
    "COMPLEX_VS_SPECIFIC_FACILITY": "COMPLEX_GPS_NOT_SUBFACILITY_ENTRANCE",
    "DIFFERENT_DISTRICT_NAME_COLLISION": "CROSS_DISTRICT_FALSE_MATCH_REJECT",
}


def validate_identity_evidence(case):
    """Evidence only upgrades human triage, never site identity or GPS approval."""
    evidence = case.get("identityEvidence")
    if evidence is None:
        return None
    if (case["relation"] != "POTENTIAL_DUPLICATE_POOL"
            or case["target"]["id"] != 299 or case["related"]["id"] != 447):
        raise ValueError("Source crosswalk is only reviewed for the São Jacinto pool pair")
    if not isinstance(evidence, dict):
        raise ValueError("Invalid official/OSM identity evidence")
    if (evidence.get("sourceDoesNotProveCurrentEntrance") is not True or
            evidence.get("sourceDoesNotProveSamePhysicalFacility") is not True):
        raise ValueError("Official aliases/OSM geometries do not prove an entrance or duplicate")
    if fold(evidence.get("officialSingularName")) != fold(case["target"]["name"]):
        raise ValueError("Official singular name does not match municipal venue")
    if fold(evidence.get("officialPluralName")) != fold("Piscinas Municipais de São Jacinto"):
        raise ValueError("Unreviewed official plural alias")
    if fold(evidence.get("parishStreet")) != fold("Avenida Ria-Mar"):
        raise ValueError("Unreviewed parish location")
    if evidence.get("officialPluralPublicationDate") != "2026-07-24":
        raise ValueError("Unreviewed date of official plural naming")
    way = evidence.get("osmWayId")
    if type(way) is not int or way != 152241544:
        raise ValueError("Unknown OSM geometry; explicit source identity required")
    if (evidence.get("osmGeometry") != "LEISURE_SWIMMING_POOL_POLYGON_NOT_ENTRANCE"
            or "Bing" not in str(evidence.get("osmMappedFrom", ""))):
        raise ValueError("OSM pool polygon provenance not validated")
    for key, prefix in (
        ("officialSingularSourceUrl", "https://cm-aveiro.pt/"),
        ("officialPluralSourceUrl", "https://www.cm-aveiro.pt/"),
        ("parishSourceUrl", "https://www.jf-saojacinto.pt/"),
        ("osmWayUrl", "https://www.openstreetmap.org/way/"),
    ):
        value = evidence.get(key)
        if not isinstance(value, str) or not value.startswith(prefix):
            raise ValueError("Unexpected provenance URL: " + key)
        if value not in case["sourceUrls"]:
            raise ValueError("Evidence URL not listed in the verified source collection")
    if evidence["osmWayUrl"] != "https://www.openstreetmap.org/way/" + str(way):
        raise ValueError("OSM URL does not match identified polygon")
    return evidence


def load_cases(data):
    """Validate manually curated known public IDs; reject approval or write flags."""
    if not isinstance(data, dict) or data.get("schemaVersion") != 1:
        raise ValueError("Unsupported collision review manifest")
    if (data.get("approvedForGps") is not False or
            data.get("approvedForMerge") is not False or
            data.get("writeActions") != 0):
        raise ValueError("Only explicitly unapproved, no-write review manifests are accepted")
    cases = data.get("cases")
    if not isinstance(cases, list) or not 0 < len(cases) <= 64:
        raise ValueError("Invalid number of review cases")
    seen = set()
    for case in cases:
        if not isinstance(case, dict) or not isinstance(case.get("caseId"), str):
            raise ValueError("Invalid collision case")
        case_id = case["caseId"]
        if not case_id or case_id in seen:
            raise ValueError("Empty or duplicate case ID")
        seen.add(case_id)
        if case.get("relation") not in RELATIONS:
            raise ValueError("Unknown collision relation")
        source_urls = case.get("sourceUrls")
        if (not isinstance(source_urls, list) or not source_urls or
                not all(isinstance(url, str) and url.startswith("https://")
                        and len(url) < 1024 for url in source_urls)):
            raise ValueError("Missing HTTPS evidence")
        if not isinstance(case.get("reviewReason"), str) or not case["reviewReason"].strip():
            raise ValueError("Review reason required")
        for role in ("target", "related"):
            item = case.get(role)
            if not isinstance(item, dict) or type(item.get("id")) is not int or item["id"] <= 0:
                raise ValueError("Invalid existing venue identity")
            if not all(isinstance(item.get(k), str) and item[k].strip()
                       for k in ("name", "city", "district")):
                raise ValueError("Venue name, city and district required")
        if case["target"]["id"] == case["related"]["id"]:
            raise ValueError("Target and related cannot be the same ID")
        validate_identity_evidence(case)
        target_district = fold(case["target"]["district"])
        related_district = fold(case["related"]["district"])
        if case["relation"] == "DIFFERENT_DISTRICT_NAME_COLLISION":
            if target_district == related_district:
                raise ValueError("Cross-district case is not cross-district")
        elif target_district != related_district:
            raise ValueError("Related complex or pool must share the stated district")
    return cases


def gps_state(venue):
    """Do not expose any coordinate: only classify whether both sides exist."""
    lat, lon = venue.get("latitude"), venue.get("longitude")
    if lat is None and lon is None:
        return "MISSING"
    if (type(lat) in (int, float) and type(lon) in (int, float)
            and math.isfinite(lat) and math.isfinite(lon)
            and -90 <= lat <= 90 and -180 <= lon <= 180):
        return "PRESENT_NOT_VERIFIED"
    return "INVALID_OR_PARTIAL_REVIEW"


def identity_is_exact(venue, expected):
    return (venue.get("id") == expected["id"]
            and all(fold(venue.get(k)) == fold(expected[k])
                    for k in ("name", "city", "district")))


def audit(catalogue, cases):
    """Inspect specific IDs and reject stale, cross-facility or ambiguous GPS reuse."""
    by_id = check_catalogue(catalogue)
    cases = load_cases(cases)
    rows = []
    for case in cases:
        target = by_id.get(case["target"]["id"])
        related = by_id.get(case["related"]["id"])
        if target is None or related is None:
            status = "KNOWN_ID_MISSING_REVIEW"
        elif not identity_is_exact(target, case["target"]) or not identity_is_exact(related, case["related"]):
            status = "KNOWN_ID_IDENTITY_CHANGED_REVIEW"
        elif gps_state(target) != "MISSING":
            status = "TARGET_GPS_CHANGED_RECHECK_REQUIRED"
        elif gps_state(related) != "PRESENT_NOT_VERIFIED":
            status = "RELATED_GPS_UNAVAILABLE_OR_INVALID_REVIEW"
        else:
            status = RELATIONS[case["relation"]]
        identity_evidence = case.get("identityEvidence")
        source_identity_fresh = status == "POTENTIAL_DUPLICATE_REQUIRES_IDENTITY_REVIEW"
        rows.append({
            "reviewPriority": ("HIGH_OFFICIAL_ALIAS_PLUS_OSM_AREA"
                               if identity_evidence and source_identity_fresh else "NORMAL"),
            "identityEvidenceStatus": (
                "OFFICIAL_NAMES_AND_HISTORIC_OSM_POLYGON_STILL_UNVERIFIED"
                if identity_evidence and source_identity_fresh else
                "SOURCE_IDENTITY_CHANGED_RECHECK" if identity_evidence else
                "NO_CURATED_IDENTITY_EVIDENCE"),
            "samePhysicalFacilityVerified": False,
            "currentEntranceVerified": False,
            "osmWayIdForHumanReview": identity_evidence["osmWayId"] if identity_evidence else None,
            "caseId": case["caseId"],
            "targetVenueId": case["target"]["id"],
            "relatedVenueId": case["related"]["id"],
            "relation": case["relation"],
            "status": status,
            "targetHasGps": gps_state(target) == "PRESENT_NOT_VERIFIED" if target else None,
            "relatedHasGps": gps_state(related) == "PRESENT_NOT_VERIFIED" if related else None,
            "officialOrPublicSourceUrls": case["sourceUrls"],
            "manualReviewRequired": True,
            "approvedForGps": False,
            "approvedForMerge": False,
            "note": "No coordinates, imports, updates, merges or automatic deduplication",
        })
    return {"reviewCases": rows, "approvedGpsCount": 0, "approvedMergeCount": 0,
            "writeActions": 0, "mergeActions": 0, "source": "Public catalogue identity-only audit"}


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    source = parser.add_mutually_exclusive_group(required=True)
    source.add_argument("--live-public-api", action="store_true")
    source.add_argument("--venues-json", type=Path)
    parser.add_argument("--output", type=Path, required=True)
    args = parser.parse_args()
    catalogue = (load_public_catalogue() if args.live_public_api
                 else json.loads(args.venues_json.read_text(encoding="utf-8")))
    manifest = json.loads(MANIFEST.read_text(encoding="utf-8"))
    result = audit(catalogue, manifest)
    args.output.parent.mkdir(parents=True, exist_ok=True)
    args.output.write_text(json.dumps(result, indent=2, ensure_ascii=False) + "\n", encoding="utf-8")
    print(f"Reviewed {len(result['reviewCases'])} known identity collisions; zero GPS or merges approved")


if __name__ == "__main__":
    main()

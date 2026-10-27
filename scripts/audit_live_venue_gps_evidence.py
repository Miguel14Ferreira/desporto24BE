#!/usr/bin/env python3
"""READ-ONLY source vs public Jynlo GPS audit. Never edits, imports or approves venues."""
from __future__ import annotations
import argparse
import json
import math
from pathlib import Path

from audit_venue_public_contacts import check_catalogue, fold, load_public_catalogue

ROOT = Path(__file__).resolve().parents[1]
MANIFESTS = (
    "docs/venue-imports/viseu-guarda-3-municipal-gps-pending-caop2025.json",
    "docs/venue-imports/braga-evora-2-official-site-gps-review-20261021.json",
    "docs/venue-imports/santarem-pavilhao-gps-source-conflict-20261027.json",
)

def valid_gps(lat, lon):
    return (type(lat) in (int, float) and type(lon) in (int, float)
            and math.isfinite(lat) and math.isfinite(lon)
            and -90 <= lat <= 90 and -180 <= lon <= 180)

def distance_m(lat1, lon1, lat2, lon2):
    p1, p2 = math.radians(lat1), math.radians(lat2)
    dp = p2 - p1
    dl = math.radians(lon2 - lon1)
    v = math.sin(dp/2)**2 + math.cos(p1)*math.cos(p2)*math.sin(dl/2)**2
    return 12742000 * math.asin(min(1.0, math.sqrt(v)))

def load_evidence(paths):
    points, seen = [], set()
    for path in paths:
        data = json.loads(Path(path).read_text(encoding="utf-8"))
        if data.get("approved_for_import") is not False:
            raise ValueError("Only non-approved review manifests are permitted")
        for point in data["points"]:
            ref = point["source_reference"]
            if ref in seen:
                raise ValueError("Duplicate evidence sourceReference")
            seen.add(ref)
            if not valid_gps(point.get("latitude"), point.get("longitude")):
                raise ValueError("Invalid evidence coordinate")
            url = point.get("location_source_url")
            if not isinstance(url, str) or not url.startswith("https://"):
                raise ValueError("Invalid evidence URL")
            points.append(point)
    return points

def analyze(catalogue, evidence):
    """Only exact normalized name+city+district matching; aliases remain human review."""
    check_catalogue(catalogue)
    result = []
    # Independent sources for the same named facility must not silently be treated
    # as separate approved candidate coordinates. A 75 m gap triggers human review.
    # This is a triage threshold, never a GPS accuracy certification.
    groups = {}
    for point in evidence:
        key = (fold(point["candidate_name"]),
               fold(point["expected_municipality"]), fold(point["expected_district"]))
        groups.setdefault(key, []).append(point)
    separations = {}
    for key, group in groups.items():
        separations[key] = max(
            (distance_m(a["latitude"], a["longitude"], b["latitude"], b["longitude"])
             for index, a in enumerate(group) for b in group[index+1:]),
            default=0.0
        )
    for point in evidence:
        key = (fold(point["candidate_name"]),
               fold(point["expected_municipality"]), fold(point["expected_district"]))
        separation = separations[key]
        source_conflict = separation > 75.0
        matches = [v for v in catalogue
                   if fold(v.get("name")) == fold(point["candidate_name"])
                   and fold(v.get("city")) == fold(point["expected_municipality"])
                   and fold(v.get("district")) == fold(point["expected_district"])]
        if not matches:
            status, venue, gap = "NO_EXACT_LIVE_MATCH_REVIEW", None, None
        elif len(matches) != 1:
            status, venue, gap = "MULTIPLE_LIVE_MATCHES_REVIEW", None, None
        else:
            venue = matches[0]
            if not valid_gps(venue.get("latitude"), venue.get("longitude")):
                status, gap = "EXISTING_VENUE_WITHOUT_GPS", None
            else:
                gap = round(distance_m(venue["latitude"], venue["longitude"],
                                       point["latitude"], point["longitude"]), 1)
                status = ("GPS_ALREADY_MATCHES_SOURCE" if gap <= 15
                          else "EXISTING_DIFFERENT_GPS_REVIEW")
        if source_conflict and len(matches) == 1:
            status = "SOURCE_COORDINATE_CONFLICT_REVIEW"
        warnings = []
        if source_conflict:
            warnings.append("CONFLICTING_DOCUMENTED_SITE_POINTS_REVIEW")
        related_name = point.get("related_venue_name")
        related_id = point.get("related_venue_id")
        if related_name:
            other = [v for v in catalogue if v.get("id") == related_id
                     and fold(v.get("name")) == fold(related_name)
                     and fold(v.get("city")) == fold(point["expected_municipality"])]
            warnings.append("RELATED_DISTINCT_VENUE_REVIEW" if other
                            else "RELATED_VENUE_IDENTITY_UNCONFIRMED_REVIEW")
        if point.get("source_address_conflict"):
            warnings.append("HISTORICAL_POSTAL_CONFLICT_REVIEW")
        precision = point.get("point_precision")
        if precision in ("EVENT_TRACK_WAYPOINT_NOT_ENTRANCE",
                         "STADIUM_SITE_NOT_TRACK_OR_ENTRANCE",
                         "EVENT_SITE_WAYPOINT_NOT_EXACT_BUILDING",
                         "MUNICIPAL_DIRECTIONS_POINT_NOT_BUILDING_PROVEN"):
            warnings.append(precision)
        result.append({
            "sourceReference": point["source_reference"],
            "name": point["candidate_name"],
            "municipality": point["expected_municipality"],
            "district": point["expected_district"],
            "existingVenueId": venue["id"] if venue is not None else None,
            "status": status,
            "distanceFromDocumentedPointMeters": gap,
            "maxSourceSeparationMeters": round(separation, 1) if source_conflict else None,
            "warnings": warnings,
            "officialEvidenceUrl": point["location_source_url"],
            "note": "Read-only evidence triage, NOT approval or an insert/update instruction",
        })
    return sorted(result, key=lambda r: (r["district"], r["name"]))

def main():
    parser = argparse.ArgumentParser(description=__doc__)
    group = parser.add_mutually_exclusive_group(required=True)
    group.add_argument("--live-public-api", action="store_true")
    group.add_argument("--venues-json", type=Path)
    parser.add_argument("--output", type=Path, required=True)
    args = parser.parse_args()
    catalogue = (load_public_catalogue() if args.live_public_api
                 else json.loads(args.venues_json.read_text(encoding="utf-8")))
    evidence = load_evidence(ROOT / path for path in MANIFESTS)
    result = {"writeActions": 0, "approvalCount": 0, "reviewCandidates": analyze(catalogue, evidence)}
    args.output.parent.mkdir(parents=True, exist_ok=True)
    args.output.write_text(json.dumps(result, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")
    print(f"Reviewed {len(result['reviewCandidates'])} documented GPS leads; no database changes")

if __name__ == "__main__":
    main()

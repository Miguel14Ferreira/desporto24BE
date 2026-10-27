#!/usr/bin/env python3
"""Offline/read-only municipal GIS point cross-check against existing Jynlo venues.

A GIS point may identify a generic facility, never necessarily its entrance or field.
No geocoding, HTTP, authentication, DB access, GPS approval or API mutations.
"""
import argparse
import json
import math
from pathlib import Path
from audit_venue_public_contacts import check_catalogue, fold

SOURCES = {
    "lisboa": "https://services.arcgis.com/1dSrzEWVQn5kHHyK/arcgis/rest/services/Desporto_Instalacoes/FeatureServer/0",
    "loule": "https://geoloule.cm-loule.pt/server/rest/services/MapasOnline/MO_Desporto/FeatureServer/0",
}
SOURCE_DISTRICTS = {"lisboa": "Lisboa", "loule": "Faro"}


def point(feature):
    """Only real WGS84 Point geometry; never derive polygon centres."""
    geo = feature.get("geometry")
    if not isinstance(geo, dict) or geo.get("type") != "Point":
        return None
    coords = geo.get("coordinates")
    if not isinstance(coords, list) or len(coords) != 2:
        return None
    lon, lat = coords
    if any(type(v) not in (int, float) or not math.isfinite(v) for v in (lat, lon)):
        return None
    if not (-9.8 < lon < -6.0 and 36.8 < lat < 42.4):
        return None
    return (lat, lon)


def validate_geojson(data):
    if not isinstance(data, dict) or data.get("type") != "FeatureCollection":
        raise ValueError("Expected GeoJSON FeatureCollection")
    crs = data.get("crs")
    if crs is not None and (not isinstance(crs, dict) or
            crs.get("properties", {}).get("name") not in
            ("EPSG:4326", "urn:ogc:def:crs:OGC:1.3:CRS84")):
        raise ValueError("Explicit WGS84 export required; no auto-conversion")
    features = data.get("features")
    if not isinstance(features, list) or len(features) > 5000:
        raise ValueError("Invalid or oversized GIS feature list")
    rows = []
    for feat in features:
        if not isinstance(feat, dict) or feat.get("type") != "Feature":
            raise ValueError("Invalid GIS feature")
        props = feat.get("properties")
        if not isinstance(props, dict):
            raise ValueError("Missing GIS properties")
        name = props.get("DESIGNACAO") or props.get("DESIG")
        feature_id = props.get("OBJECTID")
        if not isinstance(name, str) or not name.strip() or type(feature_id) is not int:
            raise ValueError("Missing official feature name or OBJECTID")
        rows.append({"id": feature_id, "name": name.strip(), "point": point(feat)})
    if len({p["id"] for p in rows}) != len(rows):
        raise ValueError("Duplicate municipal OBJECTID")
    return rows


def audit(catalogue, geojson, municipality, district, source_key):
    """Only exact name and jurisdiction; all suggested source points remain unapproved."""
    check_catalogue(catalogue)
    if source_key not in SOURCES:
        raise ValueError("Unreviewed municipality GIS source")
    if fold(municipality) != source_key or fold(district) != fold(SOURCE_DISTRICTS[source_key]):
        raise ValueError("Municipality/district mismatch for official source")
    features = validate_geojson(geojson)
    by_name = {}
    for feature in features:
        by_name.setdefault(fold(feature["name"]), []).append(feature)
    name_counts = {}
    for venue in catalogue:
        if fold(venue.get("district")) == fold(district) and fold(venue.get("city")) == fold(municipality):
            name = fold(venue.get("name"))
            name_counts[name] = name_counts.get(name, 0) + 1
    rows = []
    for venue in catalogue:
        if fold(venue.get("district")) != fold(district) or fold(venue.get("city")) != fold(municipality):
            continue
        if venue.get("latitude") is not None or venue.get("longitude") is not None:
            continue
        matches = by_name.get(fold(venue.get("name")), [])
        valid = [m for m in matches if m["point"] is not None]
        if name_counts[fold(venue.get("name"))] > 1:
            status = "MULTIPLE_EXISTING_JYNLO_NAMES_REVIEW"
        elif not matches:
            status = "NO_EXACT_GIS_NAME_MATCH"
        elif len(matches) != 1:
            status = "MULTIPLE_OFFICIAL_GIS_FEATURES_REVIEW"
        elif not valid:
            status = "INVALID_GIS_POINT_REVIEW"
        else:
            status = "SINGLE_OFFICIAL_GIS_POINT_REVIEW"
        rows.append({
            "venueId": venue["id"],
            "venueName": venue.get("name"),
            "status": status,
            "gisObjectIds": [m["id"] for m in matches],
            "gisPointEvidence": [
                {"objectId": m["id"], "latitude": m["point"][0], "longitude": m["point"][1]}
                for m in valid
            ],
            "approvedForGPS": False,
            "reviewNote": "Municipal GIS point is evidence, NOT proven field or entrance",
        })
    return {"municipality": municipality, "district": district,
            "sourceUrl": SOURCES[source_key], "writeActions": 0, "approvedCount": 0,
            "results": sorted(rows, key=lambda row: row["venueId"])}


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--venues-json", type=Path, required=True)
    parser.add_argument("--gis-geojson", type=Path, required=True)
    parser.add_argument("--source", choices=sorted(SOURCES), required=True)
    parser.add_argument("--municipality", required=True)
    parser.add_argument("--district", required=True)
    parser.add_argument("--output", type=Path, required=True)
    args = parser.parse_args()
    result = audit(json.loads(args.venues_json.read_text(encoding="utf-8")),
                   json.loads(args.gis_geojson.read_text(encoding="utf-8")),
                   args.municipality, args.district, args.source)
    args.output.parent.mkdir(parents=True, exist_ok=True)
    args.output.write_text(json.dumps(result, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")
    print(f"Reviewed {len(result['results'])} existing venues; no GPS approvals or DB writes")


if __name__ == "__main__":
    main()

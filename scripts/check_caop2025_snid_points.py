#!/usr/bin/env python3
"""Read-only point-in-polygon validation against DGT CAOP2025 municipalities.

Coordinates are unapproved SNID suggestions. A municipality match ONLY proves
administrative location, NOT the sporting facility/entrance/pitch identity.
"""
from __future__ import annotations

import argparse
import csv
import json
import re
import unicodedata
from pathlib import Path

import geopandas as gpd
from shapely.geometry import Point

FIELDS = [
    "source_reference", "candidate_name", "snid_name",
    "expected_municipality", "expected_district",
    "snid_latitude", "snid_longitude", "caop_municipality",
    "caop_district", "status", "facility_gps_approved",
]


def norm(text):
    return re.sub(
        r"\s+", " ", "".join(
            ch for ch in unicodedata.normalize("NFKD", str(text or "").casefold())
            if not unicodedata.combining(ch)
        )).strip()


def label(frame, candidates):
    for name in candidates:
        if name in frame.columns:
            return name
    raise ValueError(f"Official CAOP layer lacks expected columns: {candidates}")


def validate_layers(municipalities, districts):
    if len(municipalities) != 278 or len(districts) != 18:
        raise ValueError(
            f"Expected full mainland CAOP 278 municipalities and 18 districts; "
            f"received {len(municipalities)} and {len(districts)}"
        )
    if municipalities.crs is None or districts.crs is None:
        raise ValueError("Missing CRS in official CAOP geometries")
    if municipalities.geometry.isna().any() or districts.geometry.isna().any():
        raise ValueError("Incomplete official CAOP geometries")


def analyse(points, municipalities, districts):
    validate_layers(municipalities, districts)
    if not isinstance(points, list) or len(points) != 10:
        raise ValueError("Expected exactly 10 source suggestions")
    muni_col = label(municipalities, ("municipio", "MUNICIPIO"))
    dist_col = label(districts, ("distrito_ilha", "distrito", "DISTRITO", "nome"))
    seen_municipalities = set()
    output = []

    for point in points:
        lat, lon = point["latitude"], point["longitude"]
        if not (-9.8 < lon < -6.0 and 36.8 < lat < 42.4):
            raise ValueError(f"Suspicious Portugal continental point: {point['source_reference']}")
        # Reproject WGS84 geolocation to the official CAOP layer's declared CRS.
        geo_pt = gpd.GeoSeries([Point(lon, lat)], crs="EPSG:4326")
        municipality_pt = geo_pt.to_crs(municipalities.crs).iloc[0]
        district_pt = geo_pt.to_crs(districts.crs).iloc[0]
        ms = municipalities.loc[municipalities.geometry.intersects(municipality_pt)]
        ds = districts.loc[districts.geometry.intersects(district_pt)]
        official_muni = str(ms.iloc[0][muni_col]) if len(ms) == 1 else ""
        official_dist = str(ds.iloc[0][dist_col]) if len(ds) == 1 else ""
        matches_expected_municipality = (
            len(ms) == 1 and norm(official_muni) == norm(point["expected_municipality"])
        )
        matches_expected_district = (
            len(ds) == 1 and norm(official_dist) == norm(point["expected_district"])
        )
        if len(ms) != 1 or len(ds) != 1:
            status = "OUTSIDE_OR_ADMIN_BOUNDARY_AMBIGUOUS"
        elif not matches_expected_municipality or not matches_expected_district:
            status = "REJECT_WRONG_MUNICIPALITY_OR_DISTRICT"
        else:
            status = "CAOP_MUNICIPALITY_MATCH_FACILITY_UNVERIFIED"
        out = dict(point)
        out.update({
            "caop_municipality": official_muni,
            "caop_district": official_dist,
            "status": status,
            "facility_gps_approved": False,
        })
        output.append(out)
        seen_municipalities.add(official_muni)
    assert len(output) == 10
    summary = {
        "checked_points": len(output),
        "caop_municipality_match": sum(
            r["status"] == "CAOP_MUNICIPALITY_MATCH_FACILITY_UNVERIFIED" for r in output
        ),
        "caop_wrong_municipality": sum(
            r["status"] == "REJECT_WRONG_MUNICIPALITY_OR_DISTRICT" for r in output
        ),
        "outside_or_boundary_ambiguous": sum(
            r["status"] == "OUTSIDE_OR_ADMIN_BOUNDARY_AMBIGUOUS" for r in output
        ),
        "distinct_official_municipalities": len(seen_municipalities - {""}),
        "points_approved_for_import": 0,
        "review_only": True,
        "source": "CAOP2025 (DGT), mainland municipalities/districts; SNID/IPDJ tentative GPS",
        "limitation": (
            "Point-in-polygon only verifies municipality/district; does not identify a "
            "specific facility, pitch, address, owner, public access or booking availability."
        ),
    }
    return output, summary


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--gpkg", type=Path, required=True)
    parser.add_argument("--candidates", type=Path, required=True)
    parser.add_argument("--output", type=Path, required=True)
    args = parser.parse_args()

    import pyogrio
    layers = {name for name, _ in pyogrio.list_layers(args.gpkg)}
    required = {"cont_municipios", "cont_distritos"}
    if not required.issubset(layers):
        raise SystemExit(f"Expected {required}, found {sorted(layers)}")
    municipalities = gpd.read_file(args.gpkg, layer="cont_municipios", engine="pyogrio")
    districts = gpd.read_file(args.gpkg, layer="cont_distritos", engine="pyogrio")
    input_data = json.loads(args.candidates.read_text(encoding="utf-8"))
    if input_data.get("approved_for_import") is not False:
        raise ValueError("Source candidates must explicitly remain unapproved")
    records, summary = analyse(input_data["points"], municipalities, districts)

    args.output.mkdir(parents=True, exist_ok=True)
    (args.output / "caop2025-snid-10-points.json").write_text(
        json.dumps({"summary": summary, "points": records},
                   indent=2, ensure_ascii=False) + "\n", encoding="utf-8"
    )
    with (args.output / "caop2025-snid-10-points.csv").open(
        "w", newline="", encoding="utf-8-sig"
    ) as output:
        writer = csv.DictWriter(output, fieldnames=FIELDS, delimiter=";", extrasaction="ignore")
        writer.writeheader()
        writer.writerows(records)
    print(json.dumps(summary, indent=2, ensure_ascii=False))
    print("No production DB access, no coordinates approved or applied.")


if __name__ == "__main__":
    main()

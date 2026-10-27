"""Offline safety regressions for municipal GIS-vs-Jynlo GPS review."""
import json
import sys
import unittest
from pathlib import Path

sys.path.insert(0, str(Path(__file__).resolve().parent))
from audit_official_municipal_gis import audit, point, validate_geojson


def feature(fid, name, lon=-9.15, lat=38.72, kind="Point"):
    return {"type": "Feature", "properties": {"OBJECTID": fid, "DESIGNACAO": name},
            "geometry": {"type": kind, "coordinates": [lon, lat]}}


def venue(vid, name, latitude=None, longitude=None):
    return {"id": vid, "name": name, "city": "Lisboa", "district": "Lisboa",
            "latitude": latitude, "longitude": longitude, "contact": "private-not-in-audit"}


class MunicipalGISReadOnlyTest(unittest.TestCase):
    def data(self, *features):
        return {"type": "FeatureCollection", "features": list(features)}

    def test_single_official_point_still_requires_human_review(self):
        result = audit([venue(77001, "Campo Chicago")],
                       self.data(feature(2026, "Campo Chicago")), "Lisboa", "Lisboa", "lisboa")
        row = result["results"][0]
        self.assertEqual("SINGLE_OFFICIAL_GIS_POINT_REVIEW", row["status"])
        self.assertEqual([2026], row["gisObjectIds"])
        self.assertFalse(row["approvedForGPS"])
        self.assertEqual(0, result["writeActions"])
        self.assertEqual(0, result["approvedCount"])
        self.assertNotIn("private-not-in-audit", json.dumps(result))

    def test_real_lisbon_duplicate_named_official_features_never_select_one(self):
        gis = self.data(feature(2027, "Campo das Amoreiras", -9.1434027134, 38.7871320437),
                        feature(2028, "Campo das Amoreiras", -9.1427775686, 38.7866833920))
        row = audit([venue(123, "Campo das Amoreiras")], gis, "Lisboa", "Lisboa", "lisboa")["results"][0]
        self.assertEqual("MULTIPLE_OFFICIAL_GIS_FEATURES_REVIEW", row["status"])
        self.assertEqual([2027, 2028], row["gisObjectIds"])
        self.assertFalse(row["approvedForGPS"])

    def test_duplicate_live_venue_names_are_never_treated_as_unique_matches(self):
        gis = self.data(feature(10, "Campo Chicago"))
        result = audit([venue(30, "Campo Chicago"), venue(31, "Campo Chicago")],
                       gis, "Lisboa", "Lisboa", "lisboa")
        self.assertEqual(2, len(result["results"]))
        self.assertTrue(all(row["status"] == "MULTIPLE_EXISTING_JYNLO_NAMES_REVIEW"
                            and row["approvedForGPS"] is False for row in result["results"]))

    def test_never_override_existing_or_match_different_identity(self):
        gis = self.data(feature(1, "Piscina de Arroios"))
        result = audit([venue(284, "Polidesportivo de Arroios — Joaquim Campos"),
                        venue(1840, "Piscina de Arroios", 38.72, -9.15)],
                       gis, "Lisboa", "Lisboa", "lisboa")
        self.assertEqual([284], [r["venueId"] for r in result["results"]])
        self.assertEqual("NO_EXACT_GIS_NAME_MATCH", result["results"][0]["status"])
        self.assertEqual([], result["results"][0]["gisPointEvidence"])

    def test_reject_polygons_bad_coordinates_wrong_crs_and_duplicate_ids(self):
        self.assertIsNone(point(feature(1, "A", kind="Polygon")))
        self.assertIsNone(point(feature(1, "A", 999, 38)))
        self.assertIsNone(point(feature(1, "A", True, 38)))
        with self.assertRaises(ValueError):
            validate_geojson({**self.data(feature(1, "A")), "crs": {"properties": {"name": "EPSG:3763"}}})
        with self.assertRaises(ValueError):
            validate_geojson(self.data(feature(1, "A"), feature(1, "B")))

    def test_unreviewed_source_and_wrong_municipality_refused(self):
        with self.assertRaises(ValueError):
            audit([], self.data(), "Lisboa", "Lisboa", "untrusted")
        with self.assertRaises(ValueError):
            audit([], self.data(), "Loulé", "Faro", "lisboa")


if __name__ == "__main__":
    unittest.main()

"""Disposable geometry tests for read-only mainland CAOP checks."""
import unittest

import geopandas as gpd
from shapely.geometry import box

from check_caop2025_snid_points import analyse, norm, validate_layers


class CaopPointReviewTests(unittest.TestCase):
    @staticmethod
    def fixture():
        # Simulated CAOP layers with the correct cardinalities; NOT official data.
        ms = [box(-8.6, 40.0, -8.2, 40.3)]
        ds = [box(-8.9, 39.8, -8.0, 40.5)]
        for i in range(277):
            ms.append(box(1 + i, 1, 1.1 + i, 1.1))
        for i in range(17):
            ds.append(box(1 + i, 1, 2 + i, 2))
        municipalities = gpd.GeoDataFrame(
            {"municipio": ["Coimbra"] + [f"Other {i}" for i in range(277)]},
            geometry=ms, crs="EPSG:4326",
        )
        districts = gpd.GeoDataFrame(
            {"distrito_ilha": ["Coimbra"] + [f"Other {i}" for i in range(17)]},
            geometry=ds, crs="EPSG:4326",
        )
        points = [
            {
                "source_reference": f"TEST:{i}", "candidate_name": f"Venue {i}",
                "expected_municipality": "Coimbra", "expected_district": "Coimbra",
                "snid_name": f"Venue {i}", "latitude": 40.1,
                "longitude": -8.4, "match_kind": "EXACT_NAME",
            } for i in range(10)
        ]
        return points, municipalities, districts

    def test_norm_accents(self):
        self.assertEqual(norm("Évora"), "evora")
        self.assertEqual(norm("CASTELO   BRANCO"), "castelo branco")

    def test_real_point_requires_matching_municipality_and_district(self):
        points, ms, ds = self.fixture()
        results, stats = analyse(points, ms, ds)
        self.assertEqual(10, stats["caop_municipality_match"])
        self.assertEqual(0, stats["points_approved_for_import"])
        self.assertTrue(all(not x["facility_gps_approved"] for x in results))

    def test_wrong_city_and_district_boundary_are_flagged(self):
        points, ms, ds = self.fixture()
        points[0]["expected_municipality"] = "Porto"
        points[1]["longitude"] = -7.0
        results, stats = analyse(points, ms, ds)
        self.assertEqual("REJECT_WRONG_MUNICIPALITY_OR_DISTRICT", results[0]["status"])
        self.assertEqual("OUTSIDE_OR_ADMIN_BOUNDARY_AMBIGUOUS", results[1]["status"])
        self.assertEqual(8, stats["caop_municipality_match"])

    def test_three_official_site_points_and_bounded_batch(self):
        points, municipalities, districts = self.fixture()
        records, summary = analyse(points[:3], municipalities, districts, expected_count=3)
        self.assertEqual(3, summary["checked_points"])
        self.assertEqual(0, summary["points_approved_for_import"])
        self.assertTrue(all(record["facility_gps_approved"] is False for record in records))
        with self.assertRaisesRegex(ValueError, "Expected exactly 3"):
            analyse(points[:2], municipalities, districts, expected_count=3)
        with self.assertRaisesRegex(ValueError, "between 1 and 100"):
            analyse(points, municipalities, districts, expected_count=0)

    def test_malformed_layers_fail_closed(self):
        points, ms, ds = self.fixture()
        with self.assertRaisesRegex(ValueError, "278 municipalities"):
            validate_layers(ms.iloc[:2], ds)
        with self.assertRaisesRegex(ValueError, "18 districts"):
            validate_layers(ms, ds.iloc[:3])
        with self.assertRaisesRegex(ValueError, "10 source suggestions"):
            analyse(points[:2], ms, ds)


if __name__ == "__main__":
    unittest.main()

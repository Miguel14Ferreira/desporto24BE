"""Offline tests for public-only three-venue duplicate hints; no HTTP calls."""
import unittest

from live_public_venue_pilot_preflight import (
    API_ORIGIN, EXPECTED_REFS, check_one, distance_metres, public_get
)

CANDIDATE = {
    "sourceReference": "OSM:WAY:145943204", "name": "Pavilhão Marialvas",
    "city": "Cantanhede", "latitude": 40.344028, "longitude": -8.587633
}


class PublicVenueAuditTest(unittest.TestCase):
    def test_location_distance_and_public_match_cannot_approve(self):
        seen = []
        def fake(path, params):
            seen.append((path, params))
            if path == "/api/venues":
                return [{"id": 7, "name": "Marialvas",
                         "city": "Cantanhede", "latitude": 40.344028,
                         "longitude": -8.587633}]
            return {"venues": [{"id": 7, "name": "Marialvas", "city": "Cantanhede"}],
                    "page": 0, "hasMore": False}
        result = check_one(CANDIDATE, fetch=fake)
        self.assertEqual(result["publicMatchCount"], 1)
        self.assertEqual(result["possiblePublicMatches"][0]["distanceMetres"], 0)
        self.assertFalse(result["publicationApproved"])
        self.assertTrue(result["sqlPreflightStillRequired"])
        self.assertEqual(len(seen), 2)
        self.assertEqual(seen[0][1]["radiusKm"], 0.15)

    def test_no_public_matches_is_not_an_import_approval(self):
        def fake(endpoint, params):
            if endpoint.endswith("suggest"):
                return {"venues": [], "page": 0, "hasMore": False}
            return []
        r = check_one(CANDIDATE, fetch=fake)
        self.assertEqual(r["publicMatchCount"], 0)
        self.assertFalse(r["publicationApproved"])
        self.assertTrue(r["sourceReferenceNotExposedByPublicApi"])

    def test_alias_and_spatial_candidates_combine_without_duplicate_id(self):
        def fake(endpoint, params):
            if endpoint.endswith("suggest"):
                return {"venues": [
                    {"id": 11, "name": "Pavilhão Os Marialvas", "city": "Cantanhede"},
                    {"id": 22, "name": "Pavilhão Antigo", "city": "Cantanhede"}]}
            return [{"id": 11, "name": "Pavilhão Os Marialvas",
                     "city": "Cantanhede", "latitude": 40.34405,
                     "longitude": -8.58763}]
        r = check_one(CANDIDATE, fetch=fake)
        self.assertEqual(r["publicMatchCount"], 2)
        self.assertEqual(r["possiblePublicMatches"][0]["reason"], "WITHIN_150M")
        self.assertEqual(r["possiblePublicMatches"][1]["reason"], "POSSIBLE_NAME_ALIAS")

    def test_bad_api_shape_fails_closed(self):
        bad = [
            ("/api/venues/suggest", []),
            ("/api/venues", {"venues": []}),
        ]
        for endpoint, response in bad:
            def fake(path, params):
                if path == endpoint:
                    return response
                return [] if path == "/api/venues" else {"venues": []}
            with self.subTest(endpoint=endpoint), self.assertRaises(ValueError):
                check_one(CANDIDATE, fetch=fake)
        with self.assertRaisesRegex(ValueError, "Unexpected endpoint"):
            public_get("/api/admin/venues", {})

    def test_fixed_host_and_offline_math(self):
        self.assertEqual(API_ORIGIN, "https://api.d24sports.com")
        self.assertEqual(len(EXPECTED_REFS), 3)
        self.assertEqual(distance_metres(CANDIDATE, CANDIDATE), 0)


if __name__ == "__main__":
    unittest.main()

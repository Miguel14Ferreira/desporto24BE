"""Offline regressions for read-only public catalogue GPS reconciliation."""
import json
import sys
import tempfile
import unittest
from pathlib import Path

sys.path.insert(0, str(Path(__file__).resolve().parent))
from audit_live_venue_gps_evidence import analyze, load_evidence, MANIFESTS, ROOT

class GpsEvidenceAuditTest(unittest.TestCase):
    @classmethod
    def setUpClass(cls):
        cls.evidence = load_evidence(ROOT / name for name in MANIFESTS)

    def test_five_manifest_points_never_approved(self):
        self.assertEqual(5, len(self.evidence))
        self.assertTrue(all(p.get("latitude") is not None for p in self.evidence))

    def test_already_published_gps_is_not_flagged_for_update(self):
        p = self.evidence[0]
        live = [{"id": 296, "name": p["candidate_name"], "city": p["expected_municipality"],
                 "district": p["expected_district"], "latitude": p["latitude"],
                 "longitude": p["longitude"], "contact": "do-not-export"}]
        row = analyze(live, [p])[0]
        self.assertEqual("GPS_ALREADY_MATCHES_SOURCE", row["status"])
        self.assertEqual(296, row["existingVenueId"])
        self.assertEqual(0.0, row["distanceFromDocumentedPointMeters"])
        self.assertNotIn("do-not-export", json.dumps(row))

    def test_existing_no_gps_never_generates_coordinates_to_apply(self):
        p = self.evidence[0]
        live = [{"id": 900, "name": p["candidate_name"], "city": p["expected_municipality"],
                 "district": p["expected_district"], "latitude": None, "longitude": None}]
        row = analyze(live, [p])[0]
        self.assertEqual("EXISTING_VENUE_WITHOUT_GPS", row["status"])
        self.assertNotIn("latitude", row)
        self.assertNotIn("longitude", row)

    def test_changed_gps_and_duplicate_names_require_review(self):
        p = self.evidence[0]
        base = {"id": 7, "name": p["candidate_name"], "city": p["expected_municipality"],
                "district": p["expected_district"], "latitude": 41.0, "longitude": -7.0}
        self.assertEqual("EXISTING_DIFFERENT_GPS_REVIEW", analyze([base], [p])[0]["status"])
        self.assertEqual("MULTIPLE_LIVE_MATCHES_REVIEW",
                         analyze([base, {**base, "id": 8}], [p])[0]["status"])
        self.assertEqual("NO_EXACT_LIVE_MATCH_REVIEW", analyze([], [p])[0]["status"])

    def test_historical_conflicts_and_approximate_site_points_are_retained(self):
        rows = analyze([], self.evidence)
        by_city = {r["municipality"]: r for r in rows}
        self.assertIn("HISTORICAL_POSTAL_CONFLICT_REVIEW", by_city["Guarda"]["warnings"])
        self.assertIn("STADIUM_SITE_NOT_TRACK_OR_ENTRANCE", by_city["Braga"]["warnings"])
        self.assertIn("EVENT_TRACK_WAYPOINT_NOT_ENTRANCE", by_city["Évora"]["warnings"])

    def test_refuse_manifest_mislabelled_as_approved(self):
        sample = json.loads((ROOT / MANIFESTS[0]).read_text(encoding="utf-8"))
        sample["approved_for_import"] = True
        with tempfile.TemporaryDirectory() as tmp:
            fp = Path(tmp) / "evidence.json"
            fp.write_text(json.dumps(sample), encoding="utf-8")
            with self.assertRaisesRegex(ValueError, "non-approved"):
                load_evidence([fp])

if __name__ == "__main__":
    unittest.main()

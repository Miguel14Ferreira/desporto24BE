"""Offline regressions for read-only public catalogue GPS reconciliation."""
import json
import sys
import tempfile
import unittest
from pathlib import Path

sys.path.insert(0, str(Path(__file__).resolve().parent))
from audit_live_venue_gps_evidence import analyze, load_evidence, distance_m, MANIFESTS, ROOT

class GpsEvidenceAuditTest(unittest.TestCase):
    @classmethod
    def setUpClass(cls):
        cls.evidence = load_evidence(ROOT / name for name in MANIFESTS)

    def test_nine_manifest_points_never_approved(self):
        self.assertEqual(9, len(self.evidence))
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

    def test_two_santarem_sources_disagree_and_require_manual_resolution(self):
        fixture = [
            {"id": 277, "name": "Pavilhão Desportivo Municipal", "city": "Santarém",
             "district": "Santarém", "address": "Largo Comendador Paulino da Cunha e Silva",
             "latitude": None, "longitude": None},
            {"id": 278, "name": "Nave Desportiva Municipal", "city": "Santarém",
             "district": "Santarém", "address": "Largo Comendador Paulino da Cunha e Silva",
             "latitude": None, "longitude": None}
        ]
        leads = [p for p in self.evidence if p["expected_municipality"] == "Santarém"]
        self.assertEqual(2, len(leads))
        rows = analyze(fixture, leads)
        self.assertEqual(2, len(rows))
        for row in rows:
            self.assertEqual(277, row["existingVenueId"])
            self.assertEqual("SOURCE_COORDINATE_CONFLICT_REVIEW", row["status"])
            self.assertAlmostEqual(379.5, row["maxSourceSeparationMeters"], delta=2)
            self.assertIn("CONFLICTING_DOCUMENTED_SITE_POINTS_REVIEW", row["warnings"])
            self.assertIn("RELATED_DISTINCT_VENUE_REVIEW", row["warnings"])
            self.assertNotIn("latitude", row)
            self.assertNotIn("longitude", row)

    def test_even_gps_matching_one_source_does_not_override_documentary_conflict(self):
        leads = [p for p in self.evidence if p["expected_municipality"] == "Santarém"]
        sample = leads[0]
        venue = {"id": 277, "name": sample["candidate_name"],
                 "city": "Santarém", "district": "Santarém",
                 "latitude": sample["latitude"], "longitude": sample["longitude"]}
        rows = analyze([venue], leads)
        self.assertTrue(all(r["status"] == "SOURCE_COORDINATE_CONFLICT_REVIEW" for r in rows))

    def test_compatible_secondary_points_do_not_force_false_conflict(self):
        lead = self.evidence[0].copy()
        secondary = {**lead, "source_reference": "INDEPENDENT:POINT",
                     "latitude": lead["latitude"] + 0.00001}
        venue = {"id": 296, "name": lead["candidate_name"],
                 "city": lead["expected_municipality"], "district": lead["expected_district"],
                 "latitude": None, "longitude": None}
        self.assertTrue(all(row["status"] == "EXISTING_VENUE_WITHOUT_GPS"
                            for row in analyze([venue], [lead, secondary])))

    def test_outeiro_osm_polygon_is_unapproved_existing_venue_research_only(self):
        lead = next(p for p in self.evidence
                    if p["source_reference"] == "OSM:WAY:1298753441")
        self.assertEqual(283, lead["expected_existing_venue_id"])
        self.assertFalse(lead["facility_verified_by_official_source"])
        self.assertEqual("OSM_SITE_POLYGON_CENTRE_NOT_ENTRANCE", lead["point_precision"])
        venue = {"id": 283, "name": "Campo Municipal do Outeiro",
                 "city": "Porto", "district": "Porto",
                 "address": "Rua D. Afonso Henriques, Paranhos, Porto",
                 "latitude": None, "longitude": None}
        row = analyze([venue], [lead])[0]
        self.assertEqual(283, row["existingVenueId"])
        self.assertEqual("EXISTING_VENUE_WITHOUT_GPS", row["status"])
        self.assertIn("OSM_SITE_POLYGON_CENTRE_NOT_ENTRANCE", row["warnings"])
        self.assertIn("MUNICIPAL_AND_CLUB_ADDRESS_REVIEW", row["warnings"])
        self.assertIn("MAP_NEIGHBORHOOD_DISCREPANCY_REVIEW", row["warnings"])
        self.assertNotIn("latitude", row)
        self.assertNotIn("longitude", row)

    def test_outeiro_previously_populated_gps_is_not_blindly_overwritten(self):
        lead = next(p for p in self.evidence
                    if p["source_reference"] == "OSM:WAY:1298753441")
        live = {"id": 283, "name": "Campo Municipal do Outeiro",
                "city": "Porto", "district": "Porto",
                "latitude": lead["latitude"], "longitude": lead["longitude"]}
        row = analyze([live], [lead])[0]
        self.assertEqual("GPS_ALREADY_MATCHES_SOURCE", row["status"])
        self.assertIn("OSM_SITE_POLYGON_CENTRE_NOT_ENTRANCE", row["warnings"])
        self.assertNotIn("latitude", row)

    def test_outeiro_expected_id_mismatch_cannot_be_approved(self):
        lead = next(p for p in self.evidence
                    if p["source_reference"] == "OSM:WAY:1298753441")
        wrong = {"id": 9333, "name": "Campo Municipal do Outeiro",
                 "city": "Porto", "district": "Porto",
                 "latitude": None, "longitude": None}
        row = analyze([wrong], [lead])[0]
        self.assertEqual("EXISTING_VENUE_IDENTITY_MISMATCH_REVIEW", row["status"])
        self.assertEqual(9333, row["existingVenueId"])
        self.assertNotIn("latitude", row)

    def test_braga_federation_pin_is_stadium_field_not_track_location(self):
        points = [p for p in self.evidence if p["expected_municipality"] == "Braga"]
        self.assertEqual(2, len(points))
        gap = distance_m(points[0]["latitude"], points[0]["longitude"],
                         points[1]["latitude"], points[1]["longitude"])
        self.assertGreater(gap, 20.0)
        self.assertLess(gap, 75.0)
        self.assertEqual(287, points[1]["expected_existing_venue_id"])
        assert points[1]["point_precision"] == "FEDERATION_STADIUM_FOOTBALL_PITCH_PIN_NOT_TRACK_OR_ENTRANCE"
        fixture = [{"id": 287, "name": "Pista de Atletismo do Estádio 1.º de Maio",
                    "city": "Braga", "district": "Braga",
                    "latitude": None, "longitude": None}]
        rows = analyze(fixture, points)
        self.assertEqual(2, len(rows))
        self.assertTrue(all(row["status"] == "EXISTING_VENUE_WITHOUT_GPS" for row in rows))
        self.assertTrue(all(row["maxSourceSeparationMeters"] is None for row in rows))
        fpa = next(row for row in rows if row["sourceReference"].startswith("JYNLO:FPA:"))
        self.assertEqual(287, fpa["existingVenueId"])
        self.assertIn("FEDERATION_STADIUM_FOOTBALL_PITCH_PIN_NOT_TRACK_OR_ENTRANCE", fpa["warnings"])
        self.assertNotIn("latitude", fpa)
        self.assertNotIn("longitude", fpa)

    def test_braga_wrong_existing_id_requires_identity_review(self):
        point = next(p for p in self.evidence if p["source_reference"].startswith("JYNLO:FPA:"))
        wrong = [{"id": 999, "name": point["candidate_name"],
                  "city": "Braga", "district": "Braga",
                  "latitude": None, "longitude": None}]
        row = analyze(wrong, [point])[0]
        self.assertEqual("EXISTING_VENUE_IDENTITY_MISMATCH_REVIEW", row["status"])

    def test_refuse_manifest_mislabelled_as_approved(self):
        sample = json.loads((ROOT / MANIFESTS[0]).read_text(encoding="utf-8"))
        sample["approved_for_import"] = True
        with tempfile.TemporaryDirectory() as tmp:
            fp = Path(tmp) / "evidence.json"
            fp.write_text(json.dumps(sample), encoding="utf-8")
            with self.assertRaisesRegex(ValueError, "non-approved"):
                load_evidence([fp])

    def test_outeiro_expected_id_validation(self):
        manifest = json.loads((ROOT / MANIFESTS[-1]).read_text(encoding="utf-8"))
        manifest["points"][0]["expected_existing_venue_id"] = "283"
        with tempfile.TemporaryDirectory() as tmp:
            fp = Path(tmp) / "invalid-id.json"
            fp.write_text(json.dumps(manifest), encoding="utf-8")
            with self.assertRaisesRegex(ValueError, "expected existing venue ID"):
                load_evidence([fp])

if __name__ == "__main__":
    unittest.main()

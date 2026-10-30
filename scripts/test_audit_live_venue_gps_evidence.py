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

    def test_sixteen_manifest_points_never_approved(self):
        self.assertEqual(16, len(self.evidence))
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
        by_city = {}
        for row in rows:
            by_city.setdefault(row["municipality"], []).append(row)
        self.assertTrue(any("HISTORICAL_POSTAL_CONFLICT_REVIEW" in row["warnings"]
                            for row in by_city["Guarda"]))
        self.assertTrue(any("STADIUM_SITE_NOT_TRACK_OR_ENTRANCE" in row["warnings"]
                            for row in by_city["Braga"]))
        self.assertTrue(any("FEDERATION_STADIUM_FOOTBALL_PITCH_PIN_NOT_TRACK_OR_ENTRANCE"
                            in row["warnings"] for row in by_city["Braga"]))
        self.assertTrue(any("EVENT_TRACK_WAYPOINT_NOT_ENTRANCE" in row["warnings"]
                            for row in by_city["Évora"]))

    def test_two_santarem_sources_disagree_and_require_manual_resolution(self):
        fixture = [
            {"id": 277, "name": "Pavilhão Desportivo Municipal", "city": "Santarém",
             "district": "Santarém", "address": "Largo Comendador Paulino da Cunha e Silva",
             "latitude": None, "longitude": None},
            {"id": 278, "name": "Nave Desportiva Municipal", "city": "Santarém",
             "district": "Santarém", "address": "Largo Comendador Paulino da Cunha e Silva",
             "latitude": None, "longitude": None}
        ]
        leads = [p for p in self.evidence if p["candidate_name"] == "Pavilhão Desportivo Municipal"]
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
        leads = [p for p in self.evidence if p["candidate_name"] == "Pavilhão Desportivo Municipal"]
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

    def test_evora_existing_272_two_source_types_require_manual_review(self):
        points = [p for p in self.evidence if p["candidate_name"] == "Complexo Desportivo de Évora"]
        self.assertEqual(2, len(points))
        self.assertTrue(all(p["expected_existing_venue_id"] == 272 for p in points))
        self.assertTrue(all(p.get("facility_verified_by_official_source") is not None for p in points))
        gap = distance_m(points[0]["latitude"], points[0]["longitude"],
                         points[1]["latitude"], points[1]["longitude"])
        self.assertGreater(gap, 125.0)
        self.assertLess(gap, 200.0)
        catalogue = [
            {"id": 272, "name": "Complexo Desportivo de Évora", "city": "Évora",
             "district": "Évora", "address": "Estrada das Alcáçovas (N380)",
             "latitude": None, "longitude": None},
            {"id": 3588, "name": "Campo Estrela", "city": "Évora", "district": "Évora",
             "latitude": 38.562232, "longitude": -7.9178504}
        ]
        results = analyze(catalogue, points)
        self.assertEqual(2, len(results))
        for row in results:
            self.assertEqual(272, row["existingVenueId"])
            self.assertEqual("SOURCE_COORDINATE_CONFLICT_REVIEW", row["status"])
            self.assertIn("CONFLICTING_DOCUMENTED_SITE_POINTS_REVIEW", row["warnings"])
            self.assertAlmostEqual(gap, row["maxSourceSeparationMeters"], delta=0.1)
            self.assertNotIn("latitude", row)
            self.assertNotIn("longitude", row)
        polygon = next(r for r in results if r["sourceReference"] == "OSM:WAY:1266229401")
        self.assertIn("OSM_SITE_POLYGON_CENTRE_NOT_ENTRANCE", polygon["warnings"])
        self.assertIn("RELATED_DISTINCT_VENUE_REVIEW", polygon["warnings"])

    def test_evora_matched_name_under_unexpected_id_fails_venue_identity(self):
        points = [p for p in self.evidence if p["candidate_name"] == "Complexo Desportivo de Évora"]
        wrong = [{"id": 999, "name": "Complexo Desportivo de Évora",
                  "city": "Évora", "district": "Évora",
                  "latitude": None, "longitude": None}]
        rows = analyze(wrong, points)
        self.assertEqual(2, len(rows))
        self.assertTrue(all(r["status"] == "EXISTING_VENUE_IDENTITY_MISMATCH_REVIEW"
                            for r in rows))

    def test_evora_pools_both_sources_support_same_existing_venue_but_not_approval(self):
        points = [p for p in self.evidence if p["candidate_name"] == "Piscinas Municipais de Évora"]
        self.assertEqual(2, len(points))
        self.assertTrue(all(p["expected_existing_venue_id"] == 273 for p in points))
        gap = distance_m(points[0]["latitude"], points[0]["longitude"],
                         points[1]["latitude"], points[1]["longitude"])
        self.assertGreater(gap, 15)
        self.assertLess(gap, 40)
        venue = {"id": 273, "name": "Piscinas Municipais de Évora",
                 "city": "Évora", "district": "Évora",
                 "latitude": None, "longitude": None}
        rows = analyze([venue], points)
        self.assertEqual(2, len(rows))
        self.assertTrue(all(r["status"] == "EXISTING_VENUE_WITHOUT_GPS" for r in rows))
        self.assertTrue(all(r["existingVenueId"] == 273 for r in rows))
        self.assertTrue(all(r["maxSourceSeparationMeters"] is None for r in rows))
        for row in rows:
            self.assertTrue(row["warnings"])
            self.assertNotIn("latitude", row)
            self.assertNotIn("longitude", row)

    def test_evora_pools_bad_id_is_not_treated_as_correct_venue(self):
        point = next(p for p in self.evidence
                     if p["source_reference"] == "JYNLO:MUNICIPAL:EVORA:PISCINAS:OFFICIAL_MAP")
        wrong = [{"id": 5273, "name": point["candidate_name"], "city": "Évora",
                  "district": "Évora", "latitude": None, "longitude": None}]
        row = analyze(wrong, [point])[0]
        self.assertEqual("EXISTING_VENUE_IDENTITY_MISMATCH_REVIEW", row["status"])

    def test_sacapeito_sources_match_specific_existing_venue_without_suggesting_write(self):
        sources = [p for p in self.evidence if p["candidate_name"] == "Piscinas Municipais do Sacapeito"]
        self.assertEqual(2, len(sources))
        self.assertTrue(all(p["expected_existing_venue_id"] == 276 for p in sources))
        distance = distance_m(sources[0]["latitude"], sources[0]["longitude"],
                              sources[1]["latitude"], sources[1]["longitude"])
        self.assertGreater(distance, 2.0)
        self.assertLess(distance, 6.0)
        record = {"id": 276, "name": "Piscinas Municipais do Sacapeito",
                  "city": "Santarém", "district": "Santarém",
                  "latitude": None, "longitude": None}
        rows = analyze([record], sources)
        self.assertEqual(2, len(rows))
        for row in rows:
            self.assertEqual(276, row["existingVenueId"])
            self.assertEqual("EXISTING_VENUE_WITHOUT_GPS", row["status"])
            self.assertIsNone(row["maxSourceSeparationMeters"])
            self.assertNotIn("latitude", row)
            self.assertNotIn("longitude", row)
            self.assertTrue(row["warnings"])

    def test_sacapeito_wrong_id_can_never_inherit_this_lead(self):
        points = [p for p in self.evidence if p["candidate_name"] == "Piscinas Municipais do Sacapeito"]
        wrong = [{"id": 8276, "name": "Piscinas Municipais do Sacapeito",
                  "city": "Santarém", "district": "Santarém",
                  "latitude": None, "longitude": None}]
        rows = analyze(wrong, points)
        self.assertEqual(2, len(rows))
        self.assertTrue(all(row["status"] == "EXISTING_VENUE_IDENTITY_MISMATCH_REVIEW"
                            for row in rows))

    def test_porto_choupos_site_point_is_existing_unapproved_and_not_an_entrance(self):
        source = next(p for p in self.evidence if p["source_reference"] == "OSM:WAY:449975768")
        self.assertEqual(281, source["expected_existing_venue_id"])
        self.assertEqual("OSM_SITE_POLYGON_CENTRE_NOT_ENTRANCE", source["point_precision"])
        self.assertFalse(source["facility_verified_by_official_source"])
        original = {"id": 281, "name": "Campo Municipal dos Choupos",
                    "city": "Porto", "district": "Porto", "latitude": None, "longitude": None}
        row = analyze([original], [source])[0]
        self.assertEqual(281, row["existingVenueId"])
        self.assertEqual("EXISTING_VENUE_WITHOUT_GPS", row["status"])
        self.assertIn("OSM_SITE_POLYGON_CENTRE_NOT_ENTRANCE", row["warnings"])
        self.assertNotIn("latitude", row)
        self.assertNotIn("longitude", row)

    def test_choupos_wrong_id_is_not_falsely_reconciled(self):
        point = next(p for p in self.evidence if p["source_reference"] == "OSM:WAY:449975768")
        false_match = {"id": 8281, "name": point["candidate_name"],
                       "city": point["expected_municipality"], "district": point["expected_district"],
                       "latitude": None, "longitude": None}
        row = analyze([false_match], [point])[0]
        self.assertEqual("EXISTING_VENUE_IDENTITY_MISMATCH_REVIEW", row["status"])
        self.assertNotIn("latitude", row)

    def test_arroios_cml_2016_gis_is_an_unapproved_pitch_point_not_pool_or_current_entrance(self):
        point = next(p for p in self.evidence
                     if p["source_reference"] == "JYNLO:CML:DESPORTO_INSTALACOES:OBJECTID:2954")
        self.assertEqual(284, point["expected_existing_venue_id"])
        self.assertEqual(2954, point["source_feature_id"])
        self.assertEqual("Pequeno Campo de Jogos", point["source_typology"])
        self.assertEqual("2016-07-06", point["source_data_date"])
        self.assertEqual("Forno do Tijolo - Arroios", point["source_name"])
        self.assertEqual(2955, point["related_municipal_feature_id"])
        self.assertEqual("Piscina", point["related_municipal_feature_typology"])
        gap = distance_m(point["latitude"], point["longitude"],
                         point["related_municipal_feature_latitude"],
                         point["related_municipal_feature_longitude"])
        self.assertGreater(gap, 20.0)
        self.assertLess(gap, 25.0)
        catalogue = [
            {"id": 284, "name": "Polidesportivo de Arroios — Joaquim Campos",
             "city": "Lisboa", "district": "Lisboa", "latitude": None, "longitude": None,
             "address": "Rua do Saco 1A, Mercado do Forno Tijolo, Lisboa"},
            {"id": 1840, "name": "Piscina de Arroios",
             "city": "Lisboa", "district": "Lisboa",
             "latitude": 38.720942, "longitude": -9.1323798,
             "contact": "not-to-export"}
        ]
        result = analyze(catalogue, [point])
        self.assertEqual(1, len(result))
        row = result[0]
        self.assertEqual(284, row["existingVenueId"])
        self.assertEqual("EXISTING_VENUE_WITHOUT_GPS", row["status"])
        self.assertIn("HISTORICAL_MUNICIPAL_FACILITY_POINT_NOT_CURRENT_ENTRANCE", row["warnings"])
        self.assertIn("SOURCE_OBSERVATION_DATE_MUST_BE_RECHECKED", row["warnings"])
        self.assertIn("OFFICIAL_GIS_ALIAS_VS_JYNLO_NAME_REVIEW", row["warnings"])
        self.assertIn("HISTORICAL_POSTAL_CONFLICT_REVIEW", row["warnings"])
        self.assertIn("RELATED_DISTINCT_VENUE_REVIEW", row["warnings"])
        self.assertNotIn("latitude", row)
        self.assertNotIn("longitude", row)
        self.assertNotIn("approved", json.dumps(row).lower())
        self.assertNotIn("not-to-export", json.dumps(row))

    def test_arroios_cml_field_wrong_jynlo_id_cannot_be_considered_verified(self):
        point = next(p for p in self.evidence
                     if p["source_reference"] == "JYNLO:CML:DESPORTO_INSTALACOES:OBJECTID:2954")
        wrong = {"id": 1840, "name": point["candidate_name"],
                 "city": "Lisboa", "district": "Lisboa", "latitude": None, "longitude": None}
        row = analyze([wrong], [point])[0]
        self.assertEqual("EXISTING_VENUE_IDENTITY_MISMATCH_REVIEW", row["status"])
        self.assertEqual(1840, row["existingVenueId"])
        self.assertIn("HISTORICAL_MUNICIPAL_FACILITY_POINT_NOT_CURRENT_ENTRANCE", row["warnings"])

    def test_arroios_official_pool_cannot_inherit_field_evidence(self):
        point = next(p for p in self.evidence
                     if p["source_reference"] == "JYNLO:CML:DESPORTO_INSTALACOES:OBJECTID:2954")
        pool = {"id": 1840, "name": "Piscina de Arroios",
                "city": "Lisboa", "district": "Lisboa",
                "latitude": 38.720942, "longitude": -9.1323798}
        row = analyze([pool], [point])[0]
        self.assertEqual("NO_EXACT_LIVE_MATCH_REVIEW", row["status"])
        self.assertIsNone(row["existingVenueId"])
        self.assertNotIn("latitude", row)
        self.assertNotIn("longitude", row)

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

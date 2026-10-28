"""Offline tests for GPS collision review; no network, DB or mutation operations."""
import copy
import json
import sys
import unittest
from pathlib import Path

sys.path.insert(0, str(Path(__file__).resolve().parent))
from audit_known_venue_gps_collisions import MANIFEST, audit, gps_state, load_cases


def fixture():
    return [
        {"id": 299, "name": "Piscina Municipal de São Jacinto",
         "city": "São Jacinto", "district": "Aveiro", "latitude": None, "longitude": None,
         "contact": "private-data-not-in-report"},
        {"id": 447, "name": "Piscinas de São Jacinto",
         "city": "Aveiro", "district": "Aveiro",
         "latitude": 40.6668823, "longitude": -8.7391574},
        {"id": 295, "name": "Piscina Climatizada do Complexo Desportivo de Maximinos",
         "city": "Braga", "district": "Braga", "latitude": None, "longitude": None},
        {"id": 912, "name": "Complexo Desportivo de Maximinos",
         "city": "Braga", "district": "Braga",
         "latitude": 41.5415045, "longitude": -8.4402703},
        {"id": 286, "name": "Campo de Padel do Fojo",
         "city": "Mira", "district": "Coimbra", "latitude": None, "longitude": None},
        {"id": 2377, "name": "Golfe da Quinta do Fojo",
         "city": "Vila Nova de Gaia", "district": "Porto",
         "latitude": 41.1237003, "longitude": -8.6383088},
    ]


class KnownGpsCollisionsTest(unittest.TestCase):
    @classmethod
    def setUpClass(cls):
        cls.manifest = json.loads(MANIFEST.read_text(encoding="utf-8"))

    def test_official_alias_and_osm_way_increase_priority_without_approving_duplicate(self):
        result = audit(fixture(), self.manifest)
        review = {row["targetVenueId"]: row for row in result["reviewCases"]}
        sao_jacinto = review[299]
        self.assertEqual("HIGH_OFFICIAL_ALIAS_PLUS_OSM_AREA", sao_jacinto["reviewPriority"])
        self.assertEqual("OFFICIAL_NAMES_AND_HISTORIC_OSM_POLYGON_STILL_UNVERIFIED",
                         sao_jacinto["identityEvidenceStatus"])
        self.assertEqual(152241544, sao_jacinto["osmWayIdForHumanReview"])
        self.assertFalse(sao_jacinto["samePhysicalFacilityVerified"])
        self.assertFalse(sao_jacinto["currentEntranceVerified"])
        self.assertFalse(sao_jacinto["approvedForGps"])
        self.assertFalse(sao_jacinto["approvedForMerge"])
        self.assertEqual("NORMAL", review[295]["reviewPriority"])
        self.assertEqual("NORMAL", review[286]["reviewPriority"])
        self.assertIsNone(review[295]["osmWayIdForHumanReview"])
        self.assertNotIn("latitude", json.dumps(result))
        self.assertNotIn("longitude", json.dumps(result))
        self.assertNotIn("private-data-not-in-report", json.dumps(result))

    def test_alias_evidence_cannot_survive_a_changed_venue_identity_as_high_priority(self):
        original = fixture()
        original[1]["name"] = "A different pool"
        rows = audit(original, self.manifest)["reviewCases"]
        row = next(x for x in rows if x["targetVenueId"] == 299)
        self.assertEqual("KNOWN_ID_IDENTITY_CHANGED_REVIEW", row["status"])
        self.assertEqual("NORMAL", row["reviewPriority"])
        self.assertEqual("SOURCE_IDENTITY_CHANGED_RECHECK", row["identityEvidenceStatus"])
        self.assertFalse(row["samePhysicalFacilityVerified"])
        original = fixture()
        original[0]["latitude"], original[0]["longitude"] = 40.67, -8.73
        row = next(x for x in audit(original, self.manifest)["reviewCases"]
                   if x["targetVenueId"] == 299)
        self.assertEqual("TARGET_GPS_CHANGED_RECHECK_REQUIRED", row["status"])
        self.assertEqual("NORMAL", row["reviewPriority"])

    def test_provenance_requires_unapproved_geometry_both_review_guards_and_matching_osm_url(self):
        for key, badvalue in (
                ("sourceDoesNotProveCurrentEntrance", False),
                ("sourceDoesNotProveSamePhysicalFacility", False),
                ("osmWayId", 152241545),
                ("osmWayUrl", "https://www.openstreetmap.org/way/152241545"),
                ("osmGeometry", "ENTRANCE_COORDINATE_VERIFIED"),
                ("officialPluralName", "Piscinas Municipais de Ílhavo"),
                ("officialPluralPublicationDate", "2027-01-01"),
                ("parishStreet", "Praia de São Jacinto"),
                ("osmMappedFrom", "Authoritative municipal 2026 GPS"),
        ):
            invalid = copy.deepcopy(self.manifest)
            invalid["cases"][0]["identityEvidence"][key] = badvalue
            with self.subTest(key=key), self.assertRaises(ValueError):
                audit(fixture(), invalid)

    def test_provenance_cannot_be_attached_to_unrelated_facility_or_removed_from_sources(self):
        unrelated = copy.deepcopy(self.manifest)
        unrelated["cases"][1]["identityEvidence"] = copy.deepcopy(
            unrelated["cases"][0]["identityEvidence"])
        with self.assertRaises(ValueError):
            audit(fixture(), unrelated)
        bad = copy.deepcopy(self.manifest)
        bad["cases"][0]["sourceUrls"].remove(
            bad["cases"][0]["identityEvidence"]["osmWayUrl"])
        with self.assertRaises(ValueError):
            audit(fixture(), bad)
        no_evidence = copy.deepcopy(self.manifest)
        no_evidence["cases"][0].pop("identityEvidence")
        result = audit(fixture(), no_evidence)
        row = next(x for x in result["reviewCases"] if x["targetVenueId"] == 299)
        self.assertEqual("NORMAL", row["reviewPriority"])
        self.assertEqual("NO_CURATED_IDENTITY_EVIDENCE", row["identityEvidenceStatus"])
        self.assertFalse(row["samePhysicalFacilityVerified"])

    def test_three_actual_live_identity_collisions_remain_review_only(self):
        result = audit(fixture(), self.manifest)
        actual = {row["targetVenueId"]: row for row in result["reviewCases"]}
        self.assertEqual({286, 295, 299}, set(actual))
        self.assertEqual("POTENTIAL_DUPLICATE_REQUIRES_IDENTITY_REVIEW", actual[299]["status"])
        self.assertEqual(447, actual[299]["relatedVenueId"])
        self.assertEqual("COMPLEX_GPS_NOT_SUBFACILITY_ENTRANCE", actual[295]["status"])
        self.assertEqual(912, actual[295]["relatedVenueId"])
        self.assertEqual("CROSS_DISTRICT_FALSE_MATCH_REJECT", actual[286]["status"])
        self.assertEqual(2377, actual[286]["relatedVenueId"])
        self.assertTrue(all(row["manualReviewRequired"] is True and
                            row["approvedForGps"] is False and
                            row["approvedForMerge"] is False for row in actual.values()))
        self.assertEqual(0, result["writeActions"])
        self.assertEqual(0, result["mergeActions"])
        self.assertEqual(0, result["approvedGpsCount"])
        self.assertEqual(0, result["approvedMergeCount"])
        self.assertNotIn("latitude", json.dumps(result))
        self.assertNotIn("longitude", json.dumps(result))
        self.assertNotIn("private-data-not-in-report", json.dumps(result))

    def test_identity_changes_override_possible_duplicate_or_homonym_classification(self):
        changed = fixture()
        changed[0]["name"] = "Different municipal pool"
        changed[2]["district"] = "Viana do Castelo"
        changed[5]["city"] = "Mira"
        statuses = {row["targetVenueId"]: row["status"]
                    for row in audit(changed, self.manifest)["reviewCases"]}
        self.assertEqual({299, 295, 286}, set(statuses))
        self.assertTrue(all(status == "KNOWN_ID_IDENTITY_CHANGED_REVIEW"
                            for status in statuses.values()))

    def test_related_gps_is_only_a_human_lead_even_when_identical_address(self):
        changed = fixture()
        changed[1]["address"] = changed[0].get("address")
        changed[3]["address"] = "Rua da Naia, 4710-057 Braga"
        cases = {row["targetVenueId"]: row for row in audit(changed, self.manifest)["reviewCases"]}
        self.assertEqual("POTENTIAL_DUPLICATE_REQUIRES_IDENTITY_REVIEW", cases[299]["status"])
        self.assertEqual("COMPLEX_GPS_NOT_SUBFACILITY_ENTRANCE", cases[295]["status"])
        self.assertFalse(cases[299]["approvedForGps"])
        self.assertFalse(cases[295]["approvedForMerge"])

    def test_missing_or_partial_coordinates_are_not_silently_accepted(self):
        changed = fixture()
        changed[1]["longitude"] = None
        changed[3]["latitude"] = False
        status = {row["targetVenueId"]: row["status"]
                  for row in audit(changed, self.manifest)["reviewCases"]}
        self.assertEqual("RELATED_GPS_UNAVAILABLE_OR_INVALID_REVIEW", status[299])
        self.assertEqual("RELATED_GPS_UNAVAILABLE_OR_INVALID_REVIEW", status[295])
        self.assertEqual("INVALID_OR_PARTIAL_REVIEW", gps_state(changed[1]))
        self.assertEqual("INVALID_OR_PARTIAL_REVIEW", gps_state(changed[3]))

    def test_existing_target_gps_never_gets_overwritten_or_auto_merged(self):
        changed = fixture()
        changed[0]["latitude"], changed[0]["longitude"] = 40.66, -8.73
        result = audit(changed, self.manifest)
        row = next(row for row in result["reviewCases"] if row["targetVenueId"] == 299)
        self.assertEqual("TARGET_GPS_CHANGED_RECHECK_REQUIRED", row["status"])
        self.assertFalse(row["approvedForGps"])
        self.assertFalse(row["approvedForMerge"])

    def test_catalogue_missing_record_cannot_inherit_another_id_coordinates(self):
        changed = [row for row in fixture() if row["id"] != 447]
        row = next(row for row in audit(changed, self.manifest)["reviewCases"]
                   if row["targetVenueId"] == 299)
        self.assertEqual("KNOWN_ID_MISSING_REVIEW", row["status"])
        self.assertIsNone(row["relatedHasGps"])

    def test_wrong_manifest_safety_flags_and_unrecognised_relations_fail_closed(self):
        cases = load_cases(self.manifest)
        self.assertEqual(3, len(cases))
        for key, value in (("approvedForGps", True), ("approvedForMerge", True),
                           ("writeActions", 1)):
            wrong = copy.deepcopy(self.manifest)
            wrong[key] = value
            with self.assertRaises(ValueError):
                audit(fixture(), wrong)
        bad = copy.deepcopy(self.manifest)
        bad["cases"][0]["relation"] = "MERGE_AUTOMATICALLY"
        with self.assertRaises(ValueError):
            load_cases(bad)

    def test_manifest_identity_and_district_integrity_enforced(self):
        bad = copy.deepcopy(self.manifest)
        bad["cases"][2]["related"]["district"] = "Coimbra"
        with self.assertRaises(ValueError):
            load_cases(bad)
        bad = copy.deepcopy(self.manifest)
        bad["cases"][0]["related"]["id"] = bad["cases"][0]["target"]["id"]
        with self.assertRaises(ValueError):
            load_cases(bad)
        bad = copy.deepcopy(self.manifest)
        bad["cases"].append(copy.deepcopy(bad["cases"][0]))
        with self.assertRaises(ValueError):
            load_cases(bad)


if __name__ == "__main__":
    unittest.main()

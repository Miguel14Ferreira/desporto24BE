"""Unit tests for the no-network Portugal coverage planner."""
import unittest

from plan_national_venue_coverage import AREAS, build_plan

class NationalCoveragePlanTests(unittest.TestCase):
    def test_all_districts_and_autonomous_regions_without_inventing_records(self):
        report = {"totalVenues": 228, "withoutDistrict": 70, "districts": [
            {"district": "(sem distrito)", "venues": 70, "missingCoordinates": 5,
             "missingSports": 8, "openStreetMapVenues": 20},
            {"district": "LISBOA", "venues": 158, "missingCoordinates": 4,
             "missingSports": 2, "openStreetMapVenues": 100},
        ]}
        plan = build_plan(report, "Lisboa")
        self.assertEqual(len(AREAS), 20)
        self.assertEqual(len(plan["areas"]), 20)
        self.assertEqual(plan["unclassified"]["catalogVenues"], 70)
        self.assertEqual(plan["pilot"]["existingCatalogVenues"], 158)
        self.assertIsNone(plan["pilot"]["bbox"])
        self.assertFalse(plan["pilot"]["authorizeProductionWrite"])
        self.assertFalse(any(area["complete"] for area in plan["areas"]))
        self.assertEqual(next(a for a in plan["areas"] if a["area"] == "Porto")["catalogVenues"], 0)

    def test_does_not_guess_district_from_a_city(self):
        report = {"totalVenues": 3, "withoutDistrict": 0, "districts": [
            {"district": "Cascais", "venues": 3, "missingCoordinates": 0,
             "missingSports": 0, "openStreetMapVenues": 0}
        ]}
        plan = build_plan(report)
        self.assertIn("Cascais", plan["unrecognizedDistrictLabels"])
        self.assertEqual(plan["unclassified"]["catalogVenues"], 3)
        self.assertEqual(next(a for a in plan["areas"] if a["area"] == "Lisboa")["catalogVenues"], 0)

    def test_island_region_aliases_and_accents(self):
        report = {"totalVenues": 4, "withoutDistrict": 0, "districts": [
            {"district": "Açores", "venues": 2, "missingCoordinates": 0, "missingSports": 0,
             "openStreetMapVenues": 2},
            {"district": "Regiao Autonoma da Madeira", "venues": 2,
             "missingCoordinates": 0, "missingSports": 1, "openStreetMapVenues": 1}
        ]}
        plan = build_plan(report, "Madeira")
        island = [a for a in plan["areas"] if "Região Autónoma" in a["area"]]
        self.assertEqual([a["catalogVenues"] for a in island], [2, 2])
        self.assertEqual(plan["pilot"]["area"], "Região Autónoma da Madeira")

    def test_rejects_malformed_totals_and_unknown_pilot(self):
        report = {"totalVenues": 228, "withoutDistrict": 0, "districts": []}
        with self.assertRaises(ValueError):
            build_plan(report)
        report["totalVenues"] = 0
        with self.assertRaises(ValueError):
            build_plan(report, "Cascais")

if __name__ == "__main__":
    unittest.main()

"""No-network tests for national catalogue reporting and safe pilot planning."""
import json
from pathlib import Path
import tempfile
import unittest

from audit_live_venue_district_coverage import DISTRICT_NAMES
from plan_national_venue_coverage import build_plan, main


def fixture():
    rows = [{"district": name, "total": 0, "withoutSports": 0,
             "withoutGps": 0, "withoutCity": 0, "listedCities": 0}
            for name in DISTRICT_NAMES]
    by = {r["district"]: r for r in rows}
    by["Lisboa"].update(total=7, withoutSports=3, withoutGps=2, listedCities=2,
                        contact="private@example.test",
                        mostCommonCities=[{"city": "Cacém", "count": 4}])
    by["Açores"].update(total=2, withoutGps=1, listedCities=1)
    by["(por confirmar)"].update(total=1, withoutCity=1)
    return {"total": 10, "districts": rows, "private": "secret@example.test"}


class NationalVenuePlanTest(unittest.TestCase):
    def test_complete_region_set_and_no_automatic_pilot(self):
        plan = build_plan(fixture())
        self.assertEqual(21, len(plan["regions"]))
        self.assertEqual(18, plan["scope"]["mainlandDistricts"])
        self.assertEqual(2, plan["scope"]["islandRegions"])
        self.assertEqual(10, plan["catalogueTotal"])
        self.assertIsNone(plan["pilot"])
        self.assertEqual("READ_ONLY_NO_IMPORT", plan["authorization"])
        self.assertEqual(0, next(x["catalogueCount"] for x in plan["regions"]
                                 if x["district"] == "Portalegre"))

    def test_explicit_pilot_has_no_bbox_or_import_authorization(self):
        result = build_plan(fixture(), "Lisboa")
        self.assertEqual("Lisboa", result["pilot"]["district"])
        self.assertIsNone(result["pilot"]["bbox"])
        self.assertFalse(result["pilot"]["approvalForImport"])
        self.assertEqual("PREPARATION_ONLY", result["pilot"]["status"])
        self.assertEqual("Açores", build_plan(fixture(), "Açores")["pilot"]["district"])
        for name in ("(por confirmar)", "Atlantis", "Lisboa, Porto"):
            with self.assertRaises(ValueError):
                build_plan(fixture(), name)

    def test_incomplete_duplicate_and_unknown_regions_rejected(self):
        modifications = [
            lambda x: x["districts"].pop(),
            lambda x: x["districts"].__setitem__(1, x["districts"][0].copy()),
            lambda x: x["districts"][0].update(district="Atlantis"),
        ]
        for modification in modifications:
            data = fixture()
            modification(data)
            with self.subTest(modification=modification), self.assertRaises(ValueError):
                build_plan(data)

    def test_impossible_aggregates_rejected(self):
        changes = [
            lambda x: x.update(total=9),
            lambda x: x.update(total=True),
            lambda x: x["districts"][0].update(total=-1),
            lambda x: x["districts"][0].update(withoutSports="1"),
            lambda x: x["districts"][0].update(withoutGps=True),
            lambda x: x["districts"][0].update(listedCities=4),
        ]
        for change in changes:
            data = fixture()
            change(data)
            with self.subTest(change=change), self.assertRaises(ValueError):
                build_plan(data)

    def test_output_cannot_leak_contacts_cities_or_venue_locations(self):
        data = fixture()
        data["districts"][0].update(latitude=39.25, longitude=-8.0,
                                    name="Pessoa Privada", address="Rua Segredo")
        result = json.dumps(build_plan(data), ensure_ascii=False)
        for marker in ("private@example.test", "secret@example.test", "Cacém",
                       "Pessoa Privada", "Rua Segredo", "39.25"):
            self.assertNotIn(marker, result)

    def test_cli_is_offline_and_does_not_replace_input(self):
        with tempfile.TemporaryDirectory() as temp:
            source = Path(temp) / "coverage.json"
            output = Path(temp) / "plan.json"
            source.write_text(json.dumps(fixture()), encoding="utf-8")
            self.assertEqual(0, main(["--coverage", str(source),
                                      "--out", str(output), "--pilot", "Madeira"]))
            self.assertEqual("Madeira", json.loads(output.read_text())["pilot"]["district"])
            self.assertEqual(10, json.loads(source.read_text())["total"])
            with self.assertRaises(SystemExit):
                main(["--coverage", str(source), "--out", str(source)])


if __name__ == "__main__":
    unittest.main()

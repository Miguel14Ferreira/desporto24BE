"""Offline, no-network tests for two Braganca municipal pavilion site points."""
import json
from pathlib import Path
import tempfile
import unittest

from audit_braganca_two_pavilions_20261028 import (
    EVIDENCE, NAMES, MAX_RESULTS, RADIUS_METERS, analyze, main, validate,
)


def manifest():
    return json.loads(EVIDENCE.read_text(encoding="utf-8"))


def venue(ident, name, point=None, *, city="Bragança", district="Bragança"):
    return {
        "id": ident, "name": name, "city": city, "district": district,
        "latitude": None if point is None else point[0],
        "longitude": None if point is None else point[1],
        "contact": "private@example.test", "address": "Private address",
    }


class BragancaTwoPavilionsTests(unittest.TestCase):
    def test_two_distinct_pavilions_with_source_evidence_and_no_approval(self):
        points = validate(manifest())
        self.assertEqual(2, len(points))
        self.assertEqual(set(NAMES), {point["name"] for point in points})
        self.assertTrue(all(point["approvedForImport"] is False
                            and point["precision"].endswith("NOT_ENTRANCE")
                            for point in points))

    def test_neighbouring_pool_and_stadium_do_not_count_as_same_pavilion(self):
        point = manifest()["venues"][0]
        p = [point["latitude"], point["longitude"]]
        data = [
            venue(1141, "Piscinas Municipais", p),
            venue(1139, "Estádio Municipal de Bragança",
                  [p[0] - .0014, p[1]]),
        ]
        report = analyze(data, manifest())
        arnaldo = report["candidates"][0]
        self.assertEqual("NEARBY_DISTINCT_FACILITIES_REVIEW", arnaldo["status"])
        self.assertEqual(2, arnaldo["nearbyCount"])
        self.assertEqual([], arnaldo["sameNameInDistrict"])
        self.assertEqual(0, report["approvedCount"])
        self.assertEqual(0, report["writeActions"])

    def test_an_exact_name_with_parish_in_city_is_still_flagged(self):
        p = manifest()["venues"][1]
        record = venue(7, p["name"], None, city="Sé", district="Bragança")
        result = analyze([record], manifest())
        coxa = result["candidates"][1]
        self.assertEqual("EXACT_NAME_REVIEW", coxa["status"])
        self.assertEqual([7], [x["id"] for x in coxa["sameNameInDistrict"]])
        self.assertEqual(0, coxa["nearbyCount"])

    def test_missing_gps_and_other_district_never_assumed_absent(self):
        p = manifest()["venues"][0]
        report = analyze([
            venue(1, "X"),
            venue(2, "Other", [p["latitude"], p["longitude"]],
                  district="Guarda"),
        ], manifest())
        self.assertEqual(0, report["candidates"][0]["nearbyCount"])
        self.assertEqual("NO_NEARBY_GPS_NOT_PROOF_OF_ABSENCE",
                         report["candidates"][0]["status"])

    def test_bounded_neighbours_and_no_contact_address_or_coordinates_leak(self):
        p = manifest()["venues"][1]
        nearby = [
            venue(i + 1, f"Nearby {i}",
                  [p["latitude"] + i * .00001, p["longitude"]])
            for i in range(MAX_RESULTS + 3)]
        row = analyze(nearby, manifest())["candidates"][1]
        self.assertEqual(MAX_RESULTS + 3, row["nearbyCount"])
        self.assertEqual(MAX_RESULTS, len(row["nearby"]))
        self.assertTrue(row["resultsTruncated"])
        self.assertLessEqual(row["nearby"][0]["distanceFromSourceSiteMeters"],
                             RADIUS_METERS)
        serialized = json.dumps(analyze(nearby, manifest()))
        for sensitive in ("private@example.test", "Private address",
                          str(p["latitude"]), str(p["longitude"])):
            self.assertNotIn(sensitive, serialized)


    def test_arnaldo_historic_name_without_gps_is_flagged(self):
        report = analyze([
            venue(4, "Pavilhão Municipal"),
            venue(5, "Pavilhão da Bancada"),
            venue(6, "Piscinas Municipais"),
            venue(7, "Pavilhão Municipal", city="Mirandela"),
            venue(8, "Pavilhão Municipal", district="Guarda"),
        ], manifest())
        arnaldo = report["candidates"][0]
        self.assertEqual("POSSIBLE_ALIAS_REVIEW", arnaldo["status"])
        self.assertEqual([4], [x["id"] for x in arnaldo["possibleAliases"]])
        self.assertEqual(1, arnaldo["possibleAliasCount"])
        self.assertEqual(0, arnaldo["nearbyCount"])
        self.assertFalse(arnaldo["approvedForImport"])

    def test_coxa_alias_in_parish_and_missing_district_are_flagged(self):
        report = analyze([
            venue(11, "Pavilhão da Coxa", city="Sé"),
            venue(12, "Polidesportivo da Coxa", city="Bragança", district=None),
            venue(13, "Piscinas da Coxa"),
            venue(14, "Pavilhão da Coxa", district="Guarda"),
        ], manifest())
        coxa = report["candidates"][1]
        self.assertEqual("POSSIBLE_ALIAS_REVIEW", coxa["status"])
        self.assertEqual([11, 12], [v["id"] for v in coxa["possibleAliases"]])
        self.assertEqual(2, coxa["possibleAliasCount"])
        self.assertFalse(coxa["aliasResultsTruncated"])

    def test_bounded_alias_report_does_not_export_private_fields(self):
        catalogue = [
            venue(i + 1, "Pavilhão Municipal") for i in range(MAX_RESULTS + 4)
        ]
        row = analyze(catalogue, manifest())["candidates"][0]
        self.assertEqual(MAX_RESULTS + 4, row["possibleAliasCount"])
        self.assertEqual(MAX_RESULTS, len(row["possibleAliases"]))
        self.assertTrue(row["aliasResultsTruncated"])
        serialized = json.dumps(row)
        self.assertNotIn("Private address", serialized)
        self.assertNotIn("private@example.test", serialized)

    def test_invalid_evidence_is_rejected(self):
        changes = [
            lambda d: d.update(approvedForImport=True),
            lambda d: d["venues"][0].update(approvedForImport=True),
            lambda d: d["venues"][0].update(latitude=True),
            lambda d: d["venues"][1].update(latitude=40.111),
            lambda d: d["venues"][0].update(
                coordinateSource="https://arquitecturaaqui.eu.evil.org/x"),
            lambda d: d["venues"][0].update(
                officialIdentitySource="http://diariodarepublica.pt/x"),
            lambda d: d["venues"][0].update(
                precision="CONFIRMED_ENTRANCE"),
            lambda d: d["venues"][1].update(name=d["venues"][0]["name"]),
            lambda d: d["venues"][1].update(
                latitude=d["venues"][0]["latitude"],
                longitude=d["venues"][0]["longitude"]),
        ]
        for modify in changes:
            x = manifest()
            modify(x)
            with self.subTest(modify=modify), self.assertRaises(ValueError):
                validate(x)

    def test_reject_duplicate_public_ids(self):
        with self.assertRaises(ValueError):
            analyze([venue(1, "A"), venue(1, "B")], manifest())

    def test_cli_offline_does_not_modify_input(self):
        with tempfile.TemporaryDirectory() as root:
            inp = Path(root) / "venues.json"
            out = Path(root) / "report.json"
            inp.write_text(json.dumps([venue(1, "Example")]), encoding="utf-8")
            self.assertEqual(0, main(["--venues-json", str(inp), "--out", str(out)]))
            self.assertEqual(1, json.loads(out.read_text())["catalogueTotal"])
            self.assertEqual(1, len(json.loads(inp.read_text())))
            with self.assertRaises(SystemExit):
                main(["--venues-json", str(inp), "--out", str(inp)])


if __name__ == "__main__":
    unittest.main()

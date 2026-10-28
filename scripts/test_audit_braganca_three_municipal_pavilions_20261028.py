"""Offline tests for three district Bragança pavilion candidates."""
import json
from pathlib import Path
import tempfile
import unittest
from audit_braganca_three_municipal_pavilions_20261028 import EVIDENCE, MAX_RESULTS, analyze, main, validate

def evidence():
    return json.loads(EVIDENCE.read_text(encoding="utf-8"))

def venue(id_, name, city="Vila Flor", district="Bragança", coords=None):
    return {"id": id_, "name": name, "city": city, "district": district,
            "latitude": coords[0] if coords else None,
            "longitude": coords[1] if coords else None,
            "contact": "private@example.test", "address": "Private address"}

class ThreePavilionsTests(unittest.TestCase):
    def test_identifies_three_distinct_cities_historical_gps_only(self):
        rows = validate(evidence())
        self.assertEqual({"Vila Flor", "Macedo de Cavaleiros",
                          "Freixo de Espada à Cinta"}, {x["city"] for x in rows})
        self.assertTrue(all(x["approvedForImport"] is False for x in rows))

    def test_exact_name_in_district_found_even_when_city_is_parish(self):
        site = evidence()["sites"][1]
        result = analyze([venue(3, site["name"], city="Parish")], evidence())
        self.assertEqual("EXACT_NAME_REVIEW", result["results"][1]["status"])
        self.assertEqual([3], result["results"][1]["exactNameIds"])

    def test_local_pavilion_without_gps_is_reviewed_not_imported(self):
        r = analyze([venue(9, "Pavilhão Municipal")], evidence())["results"][2]
        self.assertEqual("LOCAL_PAVILION_ALIAS_REVIEW", r["status"])
        self.assertEqual(1, r["localPavilionCount"])
        self.assertEqual(0, r["nearbyCount"])
        self.assertFalse(r["approvedForImport"])

    def test_different_municipality_does_not_become_same_named_alias(self):
        r = analyze([venue(8, "Pavilhão Municipal", city="Mirandela")],
                    evidence())["results"][2]
        self.assertEqual("NO_LOCAL_MATCH_NOT_PROOF_OF_NEW_SITE", r["status"])

    def test_nearby_other_facility_not_equated_with_pavilion(self):
        site = evidence()["sites"][0]
        r = analyze([venue(2, "Piscina Municipal",
                           city=site["city"], coords=(site["latitude"], site["longitude"]))],
                    evidence())["results"][0]
        self.assertEqual("NEARBY_FACILITIES_REVIEW", r["status"])
        self.assertEqual(1, r["nearbyCount"])
        self.assertEqual(0, r["localPavilionCount"])

    def test_bounded_results_do_not_export_contact_address_or_coordinates(self):
        site = evidence()["sites"][2]
        data = [venue(i + 1, "Pavilhão Municipal",
                      coords=(site["latitude"], site["longitude"]))
                for i in range(MAX_RESULTS + 2)]
        result = analyze(data, evidence())
        row = result["results"][2]
        self.assertEqual(MAX_RESULTS + 2, row["localPavilionCount"])
        self.assertEqual(MAX_RESULTS, len(row["localPavilions"]))
        self.assertTrue(row["resultsTruncated"])
        dumped = json.dumps(result)
        for forbidden in ("private@example.test", "Private address",
                          str(site["latitude"]), str(site["longitude"])):
            self.assertNotIn(forbidden, dumped)

    def test_no_approval_even_if_no_match(self):
        result = analyze([], evidence())
        self.assertEqual(0, result["approvedCount"])
        self.assertEqual(0, result["writeActions"])
        self.assertTrue(all(row["approvedForImport"] is False for row in result["results"]))

    def test_corrupted_evidence_rejected(self):
        edits = [
            lambda d: d.update(approvedForImport=True),
            lambda d: d["sites"][0].update(approvedForImport=True),
            lambda d: d["sites"][0].update(longitude=True),
            lambda d: d["sites"][1].update(latitude=40.5),
            lambda d: d["sites"][2].update(independentSourceDate="2026-10-28"),
            lambda d: d["sites"][0].update(coordinatePrecision="CERTIFIED_ENTRANCE"),
            lambda d: d["sites"][0].update(independentSourceUrl="https://www.canaln.tv.evil.org/x"),
            lambda d: d["sites"][1].update(city=d["sites"][0]["city"]),
        ]
        for f in edits:
            data = evidence()
            f(data)
            with self.subTest(f=f), self.assertRaises(ValueError):
                validate(data)

    def test_cli_offline_preserves_source_and_rejects_overwrite(self):
        with tempfile.TemporaryDirectory() as temp:
            inp = Path(temp) / "venues.json"
            out = Path(temp) / "report.json"
            inp.write_text("[]", encoding="utf-8")
            self.assertEqual(0, main(["--venues-json", str(inp), "--out", str(out)]))
            self.assertEqual(3, json.loads(out.read_text())["reviewedSites"])
            self.assertEqual("[]", inp.read_text())
            with self.assertRaises(SystemExit):
                main(["--venues-json", str(inp), "--out", str(inp)])

if __name__ == "__main__":
    unittest.main()

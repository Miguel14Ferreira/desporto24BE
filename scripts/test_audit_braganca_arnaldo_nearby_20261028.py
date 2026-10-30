"""Offline-only regressions for GPS-aware municipal complex preflight."""
import json
from pathlib import Path
import tempfile
import unittest

from audit_braganca_arnaldo_nearby_20261028 import (
    EVIDENCE, analyze, main, validate, MAX_MATCHES,
)

def evidence():
    return json.loads(EVIDENCE.read_text(encoding="utf-8"))

def venue(ident, name, *, lat=None, lon=None, city="Bragança", district="Bragança"):
    return {"id": ident, "name": name, "city": city, "district": district,
            "latitude": lat, "longitude": lon,
            "contact": "private@example.test", "address": "NOT EXPORT"}

class BragancaNearbyAuditTests(unittest.TestCase):
    def test_real_independent_source_is_review_only(self):
        p = validate(evidence())
        self.assertEqual("Bragança", p["city"])
        self.assertEqual("https://arquitecturaaqui.eu/pt/obras/4824/pavilhao-municipal-arnaldo-pereira-braganca",
                         p["locationSourceUrl"])
        self.assertFalse(p["approvedForImport"])

    def test_nearby_not_equivalent_to_same_facility(self):
        p = evidence()["points"][0]
        report = analyze([
            venue(7, "Piscina Municipal", lat=p["latitude"] + .0002,
                  lon=p["longitude"] + .0002),
            venue(8, "Pavilhão da Bancada", lat=p["latitude"] + .004,
                  lon=p["longitude"] + .004),
            venue(9, "Pavilhão Municipal Arnaldo Pereira", city="Sé"),
        ], evidence())
        self.assertEqual(1, report["nearbyCatalogueCount"])
        self.assertEqual("Piscina Municipal", report["nearbyCatalogue"][0]["name"])
        self.assertEqual([9], [x["id"] for x in report["sameNameAcrossDistrict"]])
        self.assertFalse(report["approvalForImport"])
        self.assertEqual(0, report["writeActions"])

    def test_no_nearby_is_not_permission_to_create(self):
        report = analyze([], evidence())
        self.assertEqual("NO_NEARBY_GPS_NOT_PROOF_OF_NEW_VENUE", report["status"])
        self.assertFalse(report["approvalForImport"])

    def test_wrong_district_and_missing_gps_are_not_nearby(self):
        p = evidence()["points"][0]
        rows = [venue(10, "B", lat=p["latitude"], lon=p["longitude"], district="Porto"),
                venue(11, "C")]
        self.assertEqual(0, analyze(rows, evidence())["nearbyCatalogueCount"])

    def test_bounded_order_by_distance_and_no_contact_or_gps_leak(self):
        p = evidence()["points"][0]
        data = [venue(i+1, f"Recinto {i}", lat=p["latitude"] + .00001*i,
                      lon=p["longitude"]) for i in range(20)]
        report = analyze(data, evidence())
        self.assertEqual(20, report["nearbyCatalogueCount"])
        self.assertEqual(MAX_MATCHES, len(report["nearbyCatalogue"]))
        self.assertTrue(report["truncated"])
        self.assertEqual(1, report["nearbyCatalogue"][0]["id"])
        serialized = json.dumps(report)
        for private in ("private@example.test", "NOT EXPORT",
                        str(p["latitude"]), str(p["longitude"])):
            self.assertNotIn(private, serialized)

    def test_reject_unsafe_evidence(self):
        for transform in (
            lambda x: x.update(approvedForImport=True),
            lambda x: x["points"][0].update(approvedForImport=True),
            lambda x: x["points"][0].update(latitude=True),
            lambda x: x["points"][0].update(latitude=1.0),
            lambda x: x["points"][0].update(
                locationSourceUrl="https://arquitecturaaqui.eu.evil.com/page"),
            lambda x: x["points"][0].update(name="Outro recinto"),
            lambda x: x.update(precision="ENTRANCE_EXACT"),
        ):
            data = evidence()
            transform(data)
            with self.subTest(transform=transform), self.assertRaises(ValueError):
                validate(data)

    def test_cli_does_not_overwrite_inputs(self):
        with tempfile.TemporaryDirectory() as d:
            directory = Path(d)
            data = directory / "venues.json"
            output = directory / "report.json"
            data.write_text(json.dumps([venue(1, "Arnaldo Pereira")]),
                            encoding="utf-8")
            self.assertEqual(0, main(["--venues-json", str(data),
                                      "--out", str(output)]))
            self.assertEqual(1, json.loads(output.read_text())["catalogueCount"])
            self.assertEqual(1, len(json.loads(data.read_text())))
            with self.assertRaises(SystemExit):
                main(["--venues-json", str(data), "--out", str(data)])

if __name__ == "__main__":
    unittest.main()

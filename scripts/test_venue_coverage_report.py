"""No-network tests for CSV tracking over all 18 districts."""
import json
from pathlib import Path
import tempfile
import unittest

from venue_coverage_report import COLUMNS, extract, write_report


class VenueCoverageReportTest(unittest.TestCase):
    def test_missing_districts_stay_visible_and_cannot_be_mistaken_as_zero(self):
        rows = extract({"portalegre": {
            "candidates": 2079, "prepared": 1992,
            "import_ready": 0, "review_required": 1992,
            "district_to_verify": 1992, "skipped_restricted_access": 87
        }})
        self.assertEqual(18, len(rows))
        self.assertEqual(1, sum(r["estado"] != "não processado" for r in rows))
        portalegre = next(r for r in rows if r["distrito"] == "Portalegre")
        self.assertEqual("distrito oficial por confirmar", portalegre["estado"])
        self.assertEqual(2079, portalegre["candidatos_osm"])
        self.assertEqual(0, portalegre["aptos_para_revisao_final"])
        self.assertEqual("", next(r for r in rows if r["distrito"] == "Lisboa")["candidatos_osm"])

    def test_rejects_inconsistent_counts_or_invalid_district(self):
        for payload in (
            {"portalegre": {"prepared": 10, "import_ready": 2, "review_required": 2}},
            {"portalegre": {"candidates": -1}},
            {"porto": {"candidates": True}},
            {"azores": {"candidates": 10}},
        ):
            with self.assertRaises(ValueError):
                extract(payload)

    def test_writes_utf8_excel_friendly_semicolon_csv(self):
        with tempfile.TemporaryDirectory() as folder:
            source = Path(folder) / "report.json"
            output = Path(folder) / "quality.csv"
            source.write_text(json.dumps({"coimbra": {
                "candidates": 10, "prepared": 8,
                "import_ready": 3, "review_required": 5
            }}), encoding="utf-8")
            number, saved = write_report(source, output)
            self.assertEqual(1, number)
            self.assertEqual(output, saved)
            csv = output.read_text(encoding="utf-8-sig")
            self.assertTrue(csv.startswith(";".join(COLUMNS)))
            self.assertIn("Coimbra;pré-visualização", csv)


if __name__ == "__main__":
    unittest.main()

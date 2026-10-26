"""Offline tests for truthful read-only region counts."""
import json
from pathlib import Path
import sys
import unittest
sys.path.insert(0, str(Path(__file__).resolve().parent))
from audit_live_venue_district_coverage import analyze


class VenueCoverageTest(unittest.TestCase):
    def test_regions_and_islands_and_unknown(self):
        sample = [
            {"id": 1, "name": "A", "district": "Lisboa", "city": "Sintra",
             "sports": "Futebol", "latitude": 38.0, "longitude": -9.0,
             "contact": "999111222"},
            {"id": 2, "name": "B", "district": "Lisboa", "city": "Cacém",
             "sports": None, "latitude": None, "longitude": None,
             "contact": "private@example.test"},
            {"id": 3, "name": "C", "district": "Açores", "city": "Horta",
             "sports": "Natação"},
            {"id": 4, "name": "D", "district": "Madeira", "city": "Funchal",
             "sports": "Futebol"},
            {"id": 5, "name": "E", "district": None, "city": None},
        ]
        report = analyze(sample)
        self.assertEqual(5, report["total"])
        by = {row["district"]: row for row in report["districts"]}
        self.assertEqual(2, by["Lisboa"]["total"])
        self.assertEqual(1, by["Lisboa"]["withoutSports"])
        self.assertEqual(1, by["Lisboa"]["withoutGps"])
        self.assertEqual(1, by["Açores"]["total"])
        self.assertEqual(1, by["Madeira"]["total"])
        self.assertEqual(1, by["(por confirmar)"]["total"])
        self.assertEqual(1, by["(por confirmar)"]["withoutCity"])
        self.assertEqual(18, len(report["lowestMainlandCounts"]) + 10)
        self.assertNotIn("999111222", json.dumps(report))
        self.assertNotIn("private@example.test", json.dumps(report))

    def test_empty_counts_are_not_missing_data(self):
        data = analyze([])
        self.assertEqual(0, data["total"])
        self.assertEqual(21, len(data["districts"]))
        self.assertTrue(all(r["total"] == 0 for r in data["districts"]))

    def test_duplicate_ids_rejected(self):
        with self.assertRaises(ValueError):
            analyze([{"id": 1}, {"id": 1}])


if __name__ == "__main__":
    unittest.main()

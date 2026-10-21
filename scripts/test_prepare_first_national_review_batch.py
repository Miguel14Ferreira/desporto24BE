"""Offline, zero-network first-batch safety tests."""
import json
from pathlib import Path
import sys
import tempfile
import unittest

sys.path.insert(0, str(Path(__file__).parent))
from prepare_first_national_review_batch import choose, generate, osm_url

def candidate(ref, **kw):
    row = dict(sourceReference=ref, name="Campo de Futebol de Teste",
        district="Aveiro", city="Aveiro", sports="Futebol",
        venueType="Campo desportivo", address="Rua Municipal 10, 3810-001 Aveiro",
        latitude=40.6, longitude=-8.6, environmentType="OUTDOOR",
        importGate="SOURCE_REQUIRES_INDEPENDENT_REVIEW",
        originalLiveHeuristic="UNMATCHED_REQUIRES_REVIEW", approvedForImport=False)
    row.update(kw)
    return row

class ReviewSafety(unittest.TestCase):
    def test_source_reference_strict(self):
        self.assertEqual(osm_url("OSM:WAY:123"), "https://www.openstreetmap.org/way/123")
        with self.assertRaises(ValueError):
            osm_url("OSM:WAY:123;DROP TABLE venue")

    def test_excludes_live_and_municipal_and_duplicates(self):
        rows = [candidate("OSM:WAY:429474882"),
                candidate("OSM:WAY:20", district="Leiria"),
                candidate("OSM:WAY:21", importGate="POSSIBLE_DUPLICATE_REVIEW"),
                candidate("OSM:WAY:22"),
                candidate("OSM:WAY:23", name="Piscina Municipal", sports="Natação")]
        chosen, report = choose(rows, expected=5)
        self.assertEqual([r["sourceReference"] for r in chosen], ["OSM:WAY:22"])
        self.assertEqual(report["approved_for_import"], 0)

    def test_bad_input_and_generic_status(self):
        with self.assertRaises(ValueError):
            choose([candidate("OSM:WAY:50")], expected=2)
        with self.assertRaises(ValueError):
            choose([candidate("OSM:WAY:51")]*2, expected=2)

    def test_outputs_remain_unapproved(self):
        with tempfile.TemporaryDirectory() as folder:
            parent = Path(folder)
            source = parent/"data.jsonl"
            source.write_text(json.dumps(candidate("OSM:WAY:77"))+"\n")
            summary = generate(source, parent/"reports", expected=1)
            self.assertEqual(summary["selected_for_review"], 1)
            saved = json.loads((parent/"reports/unapproved-venue-data.jsonl").read_text())
            self.assertIs(saved["approved"], False)
            self.assertFalse(summary["any_live_database_writes"])

if __name__ == "__main__":
    unittest.main()

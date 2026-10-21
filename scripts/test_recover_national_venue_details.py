import csv
import gzip
import hashlib
import json
import sys
import tempfile
import unittest
from pathlib import Path
sys.path.insert(0, str(Path(__file__).resolve().parent))
from recover_national_venue_details import ALREADY_LIVE, recover


class RecoveryTest(unittest.TestCase):
    def fixture(self, root, missing=False):
        p = Path(root)
        old = dict(sourceReference="OSM:WAY:123456", name="Pista A", city="Lisboa",
                   district="Lisboa", sports="Atletismo", latitude=38.7, longitude=-9.2)
        f = p / "old.jsonl.gz"
        with gzip.open(f, "wt", encoding="utf-8") as out:
            out.write(json.dumps(old) + "\n")
        qa = p / "qa.json"
        qa.write_text(json.dumps({"possible_duplicate_groups": []}))
        live = p / "live.csv"
        with live.open("w", newline="") as out:
            w = csv.DictWriter(out, fieldnames=["source_reference", "status"], delimiter=";")
            w.writeheader()
            w.writerow(dict(source_reference=old["sourceReference"], status="UNMATCHED_REQUIRES_REVIEW"))
        full = dict(old, address="Coordenadas (morada por confirmar)",
                    venueType="Pista", environmentType=None)
        sf = p / "lisbon-venues.jsonl"
        sf.write_text("" if missing else json.dumps(full) + "\n")
        rev = p / "lisbon-review.jsonl"
        rev.write_text("")
        (p / "national-report.json").write_text(json.dumps({"lisbon": {
            "status": "processed", "boundary_fingerprint": "test",
            "sha256": hashlib.sha256(sf.read_bytes()).hexdigest(),
            "review_sha256": hashlib.sha256(rev.read_bytes()).hexdigest(),
            "import_ready": 0 if missing else 1}}))
        return f, qa, live

    def test_full_fields_preserved_but_not_approved(self):
        with tempfile.TemporaryDirectory() as t:
            old, qa, live = self.fixture(t)
            stats, rows, drift, districts = recover(Path(t), old, qa, live, districts={"lisbon": "Lisboa"})
            self.assertEqual(stats["rehydrated"], 1)
            self.assertEqual(rows[0]["venueType"], "Pista")
            self.assertFalse(rows[0]["approvedForImport"])
            self.assertEqual(rows[0]["importGate"], "ADDRESS_REVIEW")
            self.assertTrue(stats["complete"])

    def test_missing_source_record_never_claims_complete(self):
        with tempfile.TemporaryDirectory() as t:
            old, qa, live = self.fixture(t, missing=True)
            stats, rows, drift, districts = recover(Path(t), old, qa, live, districts={"lisbon": "Lisboa"})
            self.assertFalse(stats["complete"])
            self.assertEqual(stats["missing_from_new_snapshot"], 1)

    def test_wrong_live_comparison_refused(self):
        with tempfile.TemporaryDirectory() as t:
            old, qa, live = self.fixture(t)
            live.write_text("source_reference;status\n")
            with self.assertRaisesRegex(ValueError, "not for the same"):
                recover(Path(t), old, qa, live, districts={"lisbon": "Lisboa"})

    def test_reference_blacklist(self):
        self.assertEqual(len(ALREADY_LIVE), 10)
        self.assertIn("OSM:WAY:429474882", ALREADY_LIVE)


if __name__ == "__main__":
    unittest.main()

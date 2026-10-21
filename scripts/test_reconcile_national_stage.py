import csv
import gzip
import json
import sys
import tempfile
import unittest
from pathlib import Path
sys.path.insert(0, str(Path(__file__).resolve().parent))
from reconcile_national_stage import read_export, reconcile, compare, indices

def existing(i, name, city, lat=None, lon=None, ref=""):
    return dict(id=str(i), name=name, city=city, district="Lisboa", address="Morada",
                latitude=lat, longitude=lon, source_reference=ref)

def candidate(ref, name, city, lat=38.75, lon=-9.1):
    return dict(sourceReference=ref, name=name, city=city, district="Lisboa",
                sports="Futebol", latitude=lat, longitude=lon)

class ReconcileTests(unittest.TestCase):
    def test_same_reference_overrides_name(self):
        s, x, _ = compare(candidate("OSM:WAY:7", "outro", "Porto"),
                          indices([existing(1, "campo", "Lisboa", ref="OSM:WAY:7")]))
        self.assertEqual(s, "EXISTING_REFERENCE")
        self.assertEqual(x["id"], "1")

    def test_accent_name_match(self):
        s, _, _ = compare(candidate("OSM:WAY:8", "Estadio Municipal", "Évora"),
                          indices([existing(2, "Estádio Municipal", "Évora")]))
        self.assertEqual(s, "POSSIBLE_DUPLICATE")

    def test_nearby_is_review_only(self):
        s, _, _ = compare(candidate("OSM:WAY:9", "Piscinas", "Lisboa"),
                          indices([existing(3, "Campo", "Lisboa", 38.75001, -9.10001)]))
        self.assertEqual(s, "POSSIBLE_DUPLICATE")

    def test_distant_is_never_approved(self):
        s, _, _ = compare(candidate("OSM:WAY:10", "Piscina", "Coimbra", 40.2, -8.4),
                          indices([existing(4, "Campo", "Lisboa", 38.75, -9.1)]))
        self.assertEqual(s, "UNMATCHED_REQUIRES_REVIEW")

    def test_csv_and_gzip(self):
        with tempfile.TemporaryDirectory() as temp:
            folder = Path(temp)
            csvfile = folder / "existing.csv"
            with csvfile.open("w", encoding="utf-8", newline="") as h:
                wr = csv.DictWriter(h, fieldnames=list(existing(1, "", "")))
                wr.writeheader()
                wr.writerow(existing(1, "Campo", "Lisboa"))
            live = read_export(csvfile)
            inp = folder / "preview.jsonl.gz"
            with gzip.open(inp, "wt", encoding="utf-8") as h:
                h.write(json.dumps(candidate("OSM:WAY:11", "Campo", "Lisboa"))+"\n")
            summary, rows = reconcile(inp, live)
            self.assertEqual(summary["approved"], 0)
            self.assertEqual(summary["counts"]["POSSIBLE_DUPLICATE"], 1)
            self.assertEqual(rows[0]["existing_id"], "1")
            with gzip.open(inp, "at", encoding="utf-8") as h:
                h.write(json.dumps(candidate("OSM:WAY:11", "Outro", "Porto"))+"\n")
            with self.assertRaisesRegex(ValueError, "repeated"):
                reconcile(inp, live)

    def test_missing_fields(self):
        with tempfile.TemporaryDirectory() as temp:
            path = Path(temp)/"bad.csv"
            path.write_text("id,name,city\n1,a,b\n", encoding="utf-8")
            with self.assertRaisesRegex(ValueError, "Missing"):
                read_export(path)

if __name__ == "__main__":
    unittest.main()

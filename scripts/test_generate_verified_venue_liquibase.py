"""Offline tests: safe Liquibase seed generation requires approval and no DB."""
import importlib.util
import json
from pathlib import Path
import tempfile
import unittest

p = Path(__file__).with_name("generate_verified_venue_liquibase.py")
spec = importlib.util.spec_from_file_location("venue_seed_gen", p)
gen = importlib.util.module_from_spec(spec)
spec.loader.exec_module(gen)

def sample(**kwargs):
    row = {
        "approved": True,
        "approvedBy": "venue-review",
        "verificationNotes": "Reviewed location and public access independently.",
        "sourceReference": "OSM:WAY:12345",
        "name": "Campo d'Água",
        "city": "Portalegre",
        "district": "Portalegre",
        "address": "Rua do Campo, Portalegre",
        "sports": "Futebol;Futsal",
        "venueType": "Campo desportivo",
        "environmentType": "OUTDOOR",
        "latitude": 39.295,
        "longitude": -7.431,
    }
    row.update(kwargs)
    return row

class VenueSeedGeneratorTest(unittest.TestCase):
    def test_requires_individually_approved_and_named_venues(self):
        with self.assertRaisesRegex(ValueError, "approved=true"):
            gen.validate(sample(approved=False))
        with self.assertRaisesRegex(ValueError, "Unverified/generated"):
            gen.validate(sample(name="Piscina (OSM #17)"))
        with self.assertRaisesRegex(ValueError, "Address still marked"):
            gen.validate(sample(address="morada por confirmar"))

    def test_rejects_invalid_location_or_unrecognized_district(self):
        for values in (
            {"district": "Funchal"}, {"latitude": 32.5}, {"longitude": -16.8},
            {"sourceReference": "OSM:WAY:1;DROP TABLE venue"}
        ):
            with self.assertRaises(ValueError):
                gen.validate(sample(**values))

    def test_no_raw_importer_output_can_pass_approval_gate(self):
        candidate = {key: value for key, value in sample().items()
                     if key not in ("approved", "approvedBy", "verificationNotes")}
        with self.assertRaises(ValueError):
            gen.validate(candidate)

    def test_quoted_name_is_safe_and_inserts_preserve_existing_venues(self):
        sql = gen.render_sql([gen.validate(sample())])
        self.assertIn("'Campo d''Água'", sql)
        self.assertIn("WHERE NOT EXISTS", sql)
        self.assertIn("ON CONFLICT DO NOTHING", sql)
        self.assertNotIn("UPDATE ", sql)
        self.assertNotIn("DELETE FROM", sql)

    def test_generated_seed_blocks_same_city_duplicate_without_gps(self):
        import sqlite3
        db = sqlite3.connect(':memory:')
        db.create_function('BTRIM', 1, lambda text: None if text is None else text.strip())
        db.execute("ATTACH DATABASE ':memory:' AS desporto24")
        db.executescript("""
            CREATE TABLE desporto24.venue (
                name TEXT, city TEXT, district TEXT, address TEXT, sports TEXT,
                venue_type TEXT, environment_type TEXT, latitude REAL, longitude REAL,
                information_source TEXT, source_reference TEXT,
                indoor BOOLEAN, changing_rooms BOOLEAN, parking BOOLEAN,
                lighting BOOLEAN, created_at TEXT,
                UNIQUE(information_source, source_reference)
            );
        """)
        item = gen.validate(sample())
        sql = gen.render_sql([item])
        db.execute("INSERT INTO desporto24.venue(name,city,information_source) "
                   "VALUES (?, ?, 'COMMUNITY')", (item["name"], item["city"]))
        db.executescript(sql)
        self.assertEqual(1, db.execute("SELECT COUNT(*) FROM desporto24.venue").fetchone()[0])
        db.execute("DELETE FROM desporto24.venue")
        db.execute("INSERT INTO desporto24.venue(name,city,information_source) "
                   "VALUES (?, ?, 'COMMUNITY')", (item["name"], "Coimbra"))
        db.executescript(sql)
        self.assertEqual(2, db.execute("SELECT COUNT(*) FROM desporto24.venue").fetchone()[0])

    def test_migration_is_opt_in_and_content_addressed(self):
        with tempfile.TemporaryDirectory() as directory:
            file = Path(directory) / "approved.jsonl"
            file.write_text(json.dumps(sample(), ensure_ascii=False) + "\n",
                            encoding="utf-8")
            output = Path(directory) / "migrations"
            sql_path, yaml_path, count = gen.generate(
                file, output, "portalegre-reviewed-01")
            self.assertEqual(count, 1)
            self.assertTrue(sql_path.exists())
            self.assertIn(sql_path.name, yaml_path.read_text(encoding="utf-8"))
            self.assertIn("runInTransaction: true",
                          yaml_path.read_text(encoding="utf-8"))
            with self.assertRaisesRegex(ValueError, "already exists"):
                gen.generate(file, output, "portalegre-reviewed-01")

    def test_rejects_duplicates_in_approved_batch(self):
        with tempfile.TemporaryDirectory() as directory:
            file = Path(directory) / "dups.jsonl"
            two = sample(sourceReference="OSM:WAY:98765", latitude=39.2950001)
            file.write_text(
                json.dumps(sample()) + "\n" + json.dumps(two) + "\n",
                encoding="utf-8")
            with self.assertRaisesRegex(ValueError, "Similar named venue"):
                gen.load_approved(file)

    def test_rejects_empty_or_unreviewed_batch(self):
        with tempfile.TemporaryDirectory() as directory:
            file = Path(directory) / "empty.jsonl"
            file.write_text("", encoding="utf-8")
            with self.assertRaisesRegex(ValueError, "No approved"):
                gen.load_approved(file)

if __name__ == "__main__":
    unittest.main()

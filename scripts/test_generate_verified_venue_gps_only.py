"""Offline regression cases for opt-in GPS-only venue Liquibase generation."""
import json
from pathlib import Path
import tempfile
import unittest

from test_generate_verified_venue_liquibase import gen, sample


def gps_only(**updates):
    row = sample(
        address=None,
        postalAddressStatus="UNKNOWN_GPS_ONLY",
        gpsVerified=True,
        gpsEvidenceUrl="https://www.cm-cantanhede.pt/mcsite/Entidade/1680/pavilhao-marialvas",
        gpsVerificationNotes=("The municipality lists the exact venue and GPS. "
                              "Independent identity and access review are required."),
    )
    row.update(updates)
    return row


class GpsOnlyApprovedSeedTest(unittest.TestCase):
    def test_null_postal_address_is_preserved_in_opt_in_sql(self):
        approved = gen.validate(gps_only())
        self.assertIsNone(approved["address"])
        sql = gen.render_sql([approved])
        self.assertIn("NULL", sql)
        self.assertIn("WHERE NOT EXISTS", sql)
        self.assertIn("ON CONFLICT DO NOTHING", sql)
        with tempfile.TemporaryDirectory() as tmp:
            src = Path(tmp) / "approved.jsonl"
            src.write_text(json.dumps(gps_only()) + "\n", encoding="utf-8")
            sql_file, yaml_file, count = gen.generate(
                src, Path(tmp) / "seed", "gps-only-verified"
            )
            self.assertEqual(1, count)
            self.assertIn("NULL", sql_file.read_text())
            self.assertIn("runInTransaction: true", yaml_file.read_text())

    def test_gps_only_sql_runs_twice_without_duplicates(self):
        import sqlite3
        db = sqlite3.connect(":memory:")
        db.create_function("BTRIM", 1, lambda v: None if v is None else v.strip())
        db.execute("ATTACH DATABASE ':memory:' AS desporto24")
        db.executescript("""
            CREATE TABLE desporto24.venue (
                name TEXT, city TEXT, district TEXT, address TEXT, sports TEXT,
                venue_type TEXT, environment_type TEXT, latitude REAL, longitude REAL,
                information_source TEXT, source_reference TEXT, website TEXT,
                indoor BOOLEAN, changing_rooms BOOLEAN, parking BOOLEAN,
                lighting BOOLEAN, created_at TEXT,
                UNIQUE(information_source, source_reference),
                CHECK (NULLIF(TRIM(address), '') IS NOT NULL OR
                       (latitude BETWEEN 36.8 AND 42.3 AND longitude BETWEEN -9.6 AND -6))
            );
        """)
        postal = gen.validate(sample())
        gps = gen.validate(gps_only(
            sourceReference="OSM:WAY:54321", name="Recinto GPS auditado",
            latitude=40.344028, longitude=-8.587633, city="Cantanhede",
            district="Coimbra",
        ))
        sql = gen.render_sql([postal, gps])
        db.executescript(sql)
        db.executescript(sql)
        self.assertEqual(2, db.execute("SELECT COUNT(*) FROM desporto24.venue").fetchone()[0])
        self.assertEqual(1, db.execute("SELECT COUNT(*) FROM desporto24.venue WHERE address IS NULL").fetchone()[0])

    def test_missing_or_unverified_evidence_cannot_be_imported(self):
        cases = [
            {"address": ""},
            {"address": "morada por confirmar"},
            {"postalAddressStatus": None},
            {"gpsVerified": False},
            {"gpsVerificationNotes": "only OSM"},
            {"gpsEvidenceUrl": "http://municipio.example/"},
            {"gpsEvidenceUrl": "https://www.openstreetmap.org/way/123"},
            {"approved": False},
            {"latitude": None},
            {"longitude": -20},
        ]
        for patch in cases:
            with self.subTest(patch=patch), self.assertRaises(ValueError):
                gen.validate(gps_only(**patch))
        missing = gps_only()
        missing.pop("address")
        with self.assertRaisesRegex(ValueError, "explicit null"):
            gen.validate(missing)

    def test_postal_address_import_still_works(self):
        self.assertEqual("Rua do Campo, Portalegre", gen.validate(sample())["address"])
        with self.assertRaises(ValueError):
            gen.validate(sample(postalAddressStatus="UNKNOWN_GPS_ONLY"))


if __name__ == "__main__":
    unittest.main()

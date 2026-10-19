"""Offline smoke test for opt-in historic venue district backfill."""
from pathlib import Path
import sqlite3
import unittest

ROOT = Path(__file__).resolve().parent.parent
SQL_FILE = ROOT / "src/main/resources/db/changelog/venue-imports/drafts/2026-10-19-backfill-known-districts-DRAFT.sql"
MASTER = ROOT / "src/main/resources/db/changelog/db.changelog-master.yaml"


def db():
    con = sqlite3.connect(":memory:")
    # PostgreSQL provides BTRIM; this test-only SQLite connection does not.
    con.create_function("BTRIM", 1, lambda s: None if s is None else s.strip())
    con.execute("ATTACH DATABASE ':memory:' AS desporto24")
    con.execute("CREATE TABLE desporto24.venue (city TEXT, district TEXT, latitude REAL, longitude REAL)")
    return con


class VenueDistrictBackfillSmokeTest(unittest.TestCase):
    @classmethod
    def setUpClass(cls):
        cls.sql = SQL_FILE.read_text(encoding="utf-8")

    def test_never_overwrites_district_or_uses_unvalidated_ambiguous_city(self):
        con = db()
        rows = [
            ("Cascais", None, 38.70, -9.40),
            ("Parede", "", 38.70, -9.34),
            ("Parede", None, 41.50, -8.40), # do not guess Lisbon elsewhere
            ("Mora", None, 38.94, -8.16),  # municipality belongs to Évora
            ("Elvas", None, 38.88, -7.16),
            ("Sousel", "Lisboa", 38.96, -7.67), # known value must never change
            ("Terrugem", None, 38.85, -9.35), # ambiguous; no safe city-only match
            ("Lugar Desconhecido", None, 40.00, -8.0)
        ]
        con.executemany("INSERT INTO desporto24.venue VALUES (?,?,?,?)", rows)
        con.executescript(self.sql)
        result = con.execute("SELECT city, district FROM desporto24.venue ORDER BY rowid").fetchall()
        self.assertEqual([
            ("Cascais", "Lisboa"),
            ("Parede", "Lisboa"),
            ("Parede", None),
            ("Mora", "Évora"),
            ("Elvas", "Portalegre"),
            ("Sousel", "Lisboa"),
            ("Terrugem", None),
            ("Lugar Desconhecido", None),
        ], result)
        con.executescript(self.sql)
        self.assertEqual(result, con.execute("SELECT city, district FROM desporto24.venue ORDER BY rowid").fetchall())

    def test_draft_not_active(self):
        self.assertNotIn(SQL_FILE.name, MASTER.read_text(encoding="utf-8"))
        self.assertEqual(2, self.sql.count("UPDATE desporto24.venue AS v"))
        self.assertNotIn("DELETE FROM", self.sql)
        self.assertNotIn("INSERT INTO", self.sql)


if __name__ == "__main__":
    unittest.main()

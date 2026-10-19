"""Offline checks for 13 municipal venue INSERT candidates, no external DB connection."""
from pathlib import Path
import re
import sqlite3
import unittest

ROOT = Path(__file__).resolve().parents[1]
BASE = ROOT / "src/main/resources/db/changelog/changes"
SQL = BASE / "2026-10-20-national-municipal-batch-13.sql"
YAML = SQL.with_suffix(".yaml")
MASTER = BASE / "db.changelog-master.yaml"
EVIDENCE = ROOT / "docs/venue-imports/national-municipal-batch-13-20261020.json"

def database():
    con = sqlite3.connect(":memory:")
    con.create_function("BTRIM", 1, lambda x: x.strip() if x is not None else None)
    con.execute("ATTACH DATABASE ':memory:' AS desporto24")
    con.execute("""CREATE TABLE desporto24.venue (
      id INTEGER PRIMARY KEY, name TEXT, city TEXT, district TEXT,
      address TEXT, sports TEXT, venue_type TEXT, environment_type TEXT,
      latitude REAL, longitude REAL, information_source TEXT,
      source_reference TEXT, website TEXT, indoor BOOLEAN,
      changing_rooms BOOLEAN, parking BOOLEAN, lighting BOOLEAN,
      created_at TEXT, UNIQUE(information_source, source_reference))""")
    return con

class NationalMunicipalBatch13Tests(unittest.TestCase):
    @classmethod
    def setUpClass(cls):
        cls.sql = SQL.read_text(encoding="utf-8")
        cls.yaml = YAML.read_text(encoding="utf-8")

    def test_liquibase_changelog_references_actual_sql_file(self):
        self.assertIn("file: db/changelog/changes/" + YAML.name, MASTER.read_text(encoding="utf-8"))
        self.assertIn("path: " + SQL.name, self.yaml)
        self.assertIn("relativeToChangelogFile: true", self.yaml)
        self.assertIn("runInTransaction: true", self.yaml)
        self.assertIn("dbms: postgresql", self.yaml)

    def test_only_13_conditional_inserts(self):
        body = re.sub(r"(?m)^--[^\n]*\n?", "", self.sql)
        self.assertEqual(body.count("INSERT INTO desporto24.venue"), 13)
        self.assertEqual(body.count("WHERE NOT EXISTS"), 13)
        self.assertEqual(body.count("ON CONFLICT DO NOTHING"), 13)
        self.assertNotRegex(body, r"\b(?:UPDATE|DELETE|TRUNCATE|ALTER|DROP)\b")
        self.assertEqual(body.count("NULL, NULL, 'OFFICIAL'"), 13)

    def test_idempotency_and_community_row_preservation(self):
        con = database()
        con.execute("""INSERT INTO desporto24.venue
          (name,city,information_source,address,sports)
          VALUES ('Existing community venue', 'Braga', 'COMMUNITY', 'Community space', 'Futsal')""")
        con.executescript(self.sql)
        self.assertEqual(con.execute("SELECT COUNT(*) FROM desporto24.venue").fetchone()[0], 14)
        con.executescript(self.sql)
        self.assertEqual(con.execute("SELECT COUNT(*) FROM desporto24.venue").fetchone()[0], 14)
        self.assertEqual(con.execute("""SELECT name,address,sports FROM desporto24.venue
          WHERE information_source='COMMUNITY'""").fetchone(),
          ('Existing community venue', 'Community space', 'Futsal'))
        self.assertEqual(con.execute("""SELECT COUNT(DISTINCT district) FROM desporto24.venue
          WHERE information_source='OFFICIAL'""").fetchone()[0], 4)

    def test_existing_community_alias_blocks_same_name_city(self):
        con = database()
        con.execute("""INSERT INTO desporto24.venue (name,city,information_source,address)
          VALUES ('Piscinas Municipais de Viseu','Viseu','COMMUNITY','Original address')""")
        con.executescript(self.sql)
        self.assertEqual(con.execute("SELECT COUNT(*) FROM desporto24.venue").fetchone()[0], 13)
        self.assertEqual(con.execute("""SELECT address FROM desporto24.venue WHERE
          information_source='COMMUNITY'""").fetchone()[0], 'Original address')

    def test_evidence_set_and_no_unverified_gps(self):
        import json
        data = json.loads(EVIDENCE.read_text(encoding="utf-8"))
        self.assertEqual(data["count"], 13)
        self.assertEqual(len(data["records"]), 13)
        self.assertEqual(set(data["districts"]), {"Braga", "Aveiro", "Guarda", "Viseu"})
        self.assertEqual(len({v["sourceReference"] for v in data["records"]}), 13)
        for venue in data["records"]:
            self.assertIsNone(venue["latitude"])
            self.assertIsNone(venue["longitude"])
            self.assertFalse(venue["productionDuplicateChecked"])
            self.assertIn(venue["sourceReference"], self.sql)
            self.assertIn(venue["name"].replace("'", "''"), self.sql)

if __name__ == "__main__":
    unittest.main()

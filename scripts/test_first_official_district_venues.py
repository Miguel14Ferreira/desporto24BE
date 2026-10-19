"""Offline regression checks for municipal-source venue Liquibase release SQL."""
from pathlib import Path
import re
import sqlite3
import unittest

ROOT=Path(__file__).resolve().parent.parent
PATH=ROOT / "src/main/resources/db/changelog/changes/2026-10-19-first-official-district-venues.sql"
YAML=PATH.with_suffix(".yaml")
MASTER=ROOT / "src/main/resources/db/changelog/db.changelog-master.yaml"

def database():
    con=sqlite3.connect(":memory:")
    con.create_function("BTRIM",1, lambda x: x.strip() if x else x)
    con.execute("ATTACH DATABASE ':memory:' AS desporto24")
    con.execute("""CREATE TABLE desporto24.venue (
        id INTEGER PRIMARY KEY, name TEXT, city TEXT, district TEXT,
        address TEXT, sports TEXT, venue_type TEXT, environment_type TEXT,
        latitude REAL, longitude REAL, information_source TEXT,
        source_reference TEXT, website TEXT, indoor BOOLEAN, changing_rooms BOOLEAN,
        parking BOOLEAN, lighting BOOLEAN, created_at TEXT,
        UNIQUE(information_source, source_reference))""")
    return con

class FirstDistrictsTest(unittest.TestCase):
    @classmethod
    def setUpClass(cls):
        cls.sql=PATH.read_text(encoding="utf-8")
    def test_changeset_resolves_correct_classpath_resource(self):
        self.assertIn("file: db/changelog/changes/"+YAML.name,
                      MASTER.read_text(encoding="utf-8"))
        text=YAML.read_text(encoding="utf-8")
        self.assertIn("path: "+PATH.name, text)
        self.assertIn("relativeToChangelogFile: true", text)
        self.assertIn("runInTransaction: true", text)
    def test_eight_additive_inserts_no_mutations(self):
        body=re.sub(r"(?m)^--[^\n]*\n?", "", self.sql)
        self.assertEqual(body.count("INSERT INTO desporto24.venue"), 8)
        self.assertEqual(body.count("WHERE NOT EXISTS"), 8)
        self.assertEqual(body.count("ON CONFLICT DO NOTHING"), 8)
        self.assertNotRegex(body, r"\b(?:UPDATE|DELETE|DROP|ALTER|TRUNCATE)\b")
    def test_idempotent_and_community_venue_untouched(self):
        con=database()
        con.execute("""INSERT INTO desporto24.venue
            (name,city,information_source,source_reference,address)
            VALUES ('Existing independent club venue','Évora','COMMUNITY',NULL,'Club address')""")
        con.executescript(self.sql)
        self.assertEqual(con.execute("SELECT COUNT(*) FROM desporto24.venue").fetchone()[0], 9)
        con.executescript(self.sql)
        self.assertEqual(con.execute("SELECT COUNT(*) FROM desporto24.venue").fetchone()[0], 9)
        self.assertEqual(con.execute(
            "SELECT name,address FROM desporto24.venue WHERE information_source='COMMUNITY'"
        ).fetchall(),[("Existing independent club venue","Club address")])
    def test_existing_alias_by_exact_name_city_skips_insertion(self):
        con=database()
        con.execute("""INSERT INTO desporto24.venue
          (name,city,information_source,address)
          VALUES ('Pavilhão Desportivo Municipal','Santarém','COMMUNITY','Original')""")
        con.executescript(self.sql)
        self.assertEqual(con.execute("SELECT COUNT(*) FROM desporto24.venue").fetchone()[0],8)
        self.assertEqual(con.execute(
           "SELECT address FROM desporto24.venue WHERE information_source='COMMUNITY'"
        ).fetchone()[0],"Original")

if __name__=="__main__":
    unittest.main()

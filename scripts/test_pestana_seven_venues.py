"""Static+SQLite safety smoke test for 7 Pestana inventory venues."""
import re
import sqlite3
import unittest
from pathlib import Path

ROOT = Path(__file__).resolve().parent.parent
NAME = "2026-10-19-pestana-7-source-corroborated"
SQL = ROOT / ("src/main/resources/db/changelog/changes/" + NAME + ".sql")
YAML = ROOT / ("src/main/resources/db/changelog/changes/" + NAME + ".yaml")
MASTER = ROOT / "src/main/resources/db/changelog/db.changelog-master.yaml"

class PestanaVenueSeedTest(unittest.TestCase):
    def setUp(self):
        self.sql = SQL.read_text(encoding="utf8")
    def test_paths_and_seven_statements(self):
        self.assertIn("file: db/changelog/changes/"+YAML.name,MASTER.read_text())
        self.assertIn("path: "+SQL.name,YAML.read_text())
        self.assertEqual(self.sql.count("INSERT INTO desporto24.venue"),7)
        self.assertEqual(self.sql.count("WHERE NOT EXISTS"),7)
        self.assertEqual(self.sql.count("ON CONFLICT DO NOTHING"),7)
        body=re.sub(r"(?m)^--[^\n]*\n?", "", self.sql)
        self.assertNotRegex(body,r"\b(?:UPDATE|DELETE|DROP|TRUNCATE|ALTER)\b")
    def test_idempotency_and_preserve_community(self):
        con=sqlite3.connect(":memory:")
        con.create_function("BTRIM",1,lambda x:None if x is None else x.strip())
        con.execute("ATTACH DATABASE ':memory:' AS desporto24")
        con.execute("""CREATE TABLE desporto24.venue(
          id INTEGER PRIMARY KEY, name TEXT, city TEXT, district TEXT, address TEXT,
          sports TEXT, venue_type TEXT, environment_type TEXT, latitude REAL,
          longitude REAL, information_source TEXT, source_reference TEXT,
          website TEXT, indoor BOOLEAN, changing_rooms BOOLEAN, parking BOOLEAN,
          lighting BOOLEAN, created_at TEXT,
          UNIQUE(information_source, source_reference))""")
        con.execute("""INSERT INTO desporto24.venue (name,city,information_source,address)
          VALUES('Campo Municipal do Viso','Porto','COMMUNITY','Morada original')""")
        con.executescript(self.sql)
        self.assertEqual(con.execute("SELECT COUNT(*) FROM desporto24.venue").fetchone()[0],7)
        con.executescript(self.sql)
        self.assertEqual(con.execute("SELECT COUNT(*) FROM desporto24.venue").fetchone()[0],7)
        self.assertEqual(con.execute("""SELECT name,address FROM desporto24.venue
          WHERE information_source='COMMUNITY'""").fetchone(),
          ("Campo Municipal do Viso","Morada original"))
if __name__=="__main__":
    unittest.main()

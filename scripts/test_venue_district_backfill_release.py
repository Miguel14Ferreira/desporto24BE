"""Release-only checks for district backfill of pre-existing venues."""
import re
import unittest
from pathlib import Path
from test_venue_district_backfill_draft import db

ROOT=Path(__file__).resolve().parent.parent
RELEASE=ROOT / "src/main/resources/db/changelog/changes/2026-10-19-backfill-known-districts.sql"
DRAFT=ROOT / "src/main/resources/db/changelog/venue-imports/drafts/2026-10-19-backfill-known-districts-DRAFT.sql"
YAML=RELEASE.with_suffix(".yaml")
MASTER=ROOT / "src/main/resources/db/changelog/db.changelog-master.yaml"

class DistrictBackfillReleaseTest(unittest.TestCase):
    def test_master_paths_and_change_set(self):
        self.assertIn("file: db/changelog/changes/"+YAML.name,MASTER.read_text())
        self.assertIn("path: "+RELEASE.name,YAML.read_text())
        self.assertIn("relativeToChangelogFile: true",YAML.read_text())
    def test_release_and_original_are_identical(self):
        self.assertEqual(DRAFT.read_text(),RELEASE.read_text())
        sql=RELEASE.read_text()
        self.assertEqual(sql.count("UPDATE desporto24.venue AS v"),2)
        self.assertNotIn("DELETE FROM",sql)
        self.assertNotIn("INSERT INTO",sql)
    def test_existing_district_is_never_replaced_and_script_is_idempotent(self):
        con=db()
        con.executemany("INSERT INTO desporto24.venue VALUES (?,?,?,?)",[
            ("Cascais",None,38.7,-9.4),
            ("Sousel","Lisboa",38.96,-7.67),
            ("Terrugem",None,38.85,-9.35),
            ("Mora",None,38.94,-8.16)
        ])
        sql=RELEASE.read_text()
        con.executescript(sql)
        result=con.execute("SELECT city,district FROM desporto24.venue ORDER BY rowid").fetchall()
        self.assertEqual(result,[
            ("Cascais","Lisboa"),("Sousel","Lisboa"),("Terrugem",None),("Mora","Évora")
        ])
        con.executescript(sql)
        self.assertEqual(con.execute(
            "SELECT city,district FROM desporto24.venue ORDER BY rowid"
        ).fetchall(),result)
if __name__=="__main__":
    unittest.main()

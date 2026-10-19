"""Offline safety tests for the nine-venue release migration.

These verify the release resources, not just the original inactive DRAFT.
SQLite is a compatibility smoke test, not proof of PostgreSQL execution.
"""
from __future__ import annotations
from pathlib import Path
import re
import unittest

from test_portalegre_liquibase_draft import sample_database

ROOT = Path(__file__).resolve().parent.parent
CHANGES = ROOT / "src/main/resources/db/changelog/changes"
MASTER = ROOT / "src/main/resources/db/changelog/db.changelog-master.yaml"
DRAFT = ROOT / "src/main/resources/db/changelog/venue-imports/drafts/2026-10-19-portalegre-source-checked-DRAFT.sql"
SQL = CHANGES / "2026-10-19-portalegre-source-checked.sql"
YAML = CHANGES / "2026-10-19-portalegre-source-checked.yaml"

def executable(sql):
    return re.sub(r"(?m)^--[^\n]*\n?", "", sql).strip()

class FirstPortalegreReleaseTest(unittest.TestCase):
    def test_liquibase_resource_paths_resolve_from_classpath(self):
        master = MASTER.read_text(encoding="utf-8")
        yml = YAML.read_text(encoding="utf-8")
        expected = "db/changelog/changes/2026-10-19-portalegre-source-checked.yaml"
        self.assertIn("file: " + expected, master)
        self.assertNotIn("file: src/main/resources/" + expected, master)
        self.assertEqual(master.count("file: " + expected), 1)
        self.assertIn("path: 2026-10-19-portalegre-source-checked.sql", yml)
        self.assertIn("relativeToChangelogFile: true", yml)
        self.assertIn("runInTransaction: true", yml)
        self.assertIn("dbms: postgresql", yml)

    def test_release_statements_equal_source_reviewed_draft(self):
        sql = SQL.read_text(encoding="utf-8")
        draft = DRAFT.read_text(encoding="utf-8")
        self.assertEqual(executable(draft), executable(sql))
        self.assertEqual(sql.count("INSERT INTO desporto24.venue"), 9)
        self.assertEqual(sql.count("WHERE NOT EXISTS"), 9)
        self.assertEqual(sql.count("ON CONFLICT DO NOTHING"), 9)
        self.assertNotRegex(executable(sql), r"\b(?:UPDATE|DELETE|DROP|ALTER|TRUNCATE)\b")

    def test_release_is_idempotent_and_preserves_prior_venues(self):
        sql = SQL.read_text(encoding="utf-8")
        connection = sample_database()
        connection.execute("""
            INSERT INTO desporto24.venue
              (name, city, address, information_source, source_reference, created_at)
            VALUES ('Previous venue name', 'Elvas', 'Community supplied',
                    'COMMUNITY', NULL, CURRENT_TIMESTAMP)
        """)
        original = connection.execute(
            "SELECT name, city, address FROM desporto24.venue WHERE information_source='COMMUNITY'"
        ).fetchall()
        connection.executescript(sql)
        self.assertEqual(connection.execute(
            "SELECT COUNT(*) FROM desporto24.venue"
        ).fetchone()[0], 10)
        connection.executescript(sql)
        self.assertEqual(connection.execute(
            "SELECT COUNT(*) FROM desporto24.venue"
        ).fetchone()[0], 10)
        self.assertEqual(original, connection.execute(
            "SELECT name, city, address FROM desporto24.venue WHERE information_source='COMMUNITY'"
        ).fetchall())

if __name__ == "__main__":
    unittest.main()

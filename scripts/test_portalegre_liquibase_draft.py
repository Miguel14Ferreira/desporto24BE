"""Offline simulation of the nine-record Portalegre Liquibase SQL draft.

Uses the Python standard-library sqlite3 engine to validate row-level
idempotency and duplicate guards without connecting to PostgreSQL/Render.
This is a compatibility smoke test, NOT a PostgreSQL/Liquibase integration test
and NOT authorization to activate a production migration.
"""
from __future__ import annotations

import json
from pathlib import Path
import re
import sqlite3
import unittest

ROOT = Path(__file__).resolve().parent.parent
DRAFTS = ROOT / "src/main/resources/db/changelog/venue-imports/drafts"
SQL = DRAFTS / "2026-10-19-portalegre-source-checked-DRAFT.sql"
CHANGELOG = DRAFTS / "2026-10-19-portalegre-source-checked-DRAFT.yaml"
MANIFEST = ROOT / "docs/venue-imports/portalegre-2026-10-19-source-review.json"
MASTER = ROOT / "src/main/resources/db/changelog/db.changelog-master.yaml"


def sample_database():
    con = sqlite3.connect(":memory:")
    # PostgreSQL provides BTRIM; this test-only SQLite connection does not.
    con.create_function("BTRIM", 1, lambda s: None if s is None else s.strip())
    con.execute("ATTACH DATABASE ':memory:' AS desporto24")
    con.execute("""
        CREATE TABLE desporto24.venue (
            id INTEGER PRIMARY KEY,
            name TEXT NOT NULL,
            city TEXT NOT NULL,
            district TEXT,
            address TEXT NOT NULL,
            sports TEXT,
            venue_type TEXT,
            environment_type TEXT,
            latitude REAL,
            longitude REAL,
            information_source TEXT NOT NULL DEFAULT 'COMMUNITY',
            source_reference TEXT,
            indoor BOOLEAN NOT NULL DEFAULT 0,
            changing_rooms BOOLEAN NOT NULL DEFAULT 0,
            parking BOOLEAN NOT NULL DEFAULT 0,
            lighting BOOLEAN NOT NULL DEFAULT 0,
            created_at TEXT NOT NULL,
            UNIQUE(information_source, source_reference)
        )
    """)
    return con


def rows(con):
    return con.execute(
        "SELECT name, information_source, source_reference, latitude, longitude "
        "FROM desporto24.venue ORDER BY name"
    ).fetchall()


class PortalegreDraftTest(unittest.TestCase):
    @classmethod
    def setUpClass(cls):
        cls.sql = SQL.read_text(encoding="utf-8")
        cls.yaml = CHANGELOG.read_text(encoding="utf-8")
        cls.manifest = json.loads(MANIFEST.read_text(encoding="utf-8"))

    def test_is_opt_in_and_contains_exactly_the_referenced_nine(self):
        self.assertEqual(9, self.sql.count("INSERT INTO desporto24.venue"))
        self.assertEqual(9, self.sql.count("ON CONFLICT DO NOTHING;"))
        self.assertEqual(9, self.sql.count("WHERE NOT EXISTS"))
        self.assertEqual(9, len(self.manifest["shortlisted"]))
        self.assertNotIn(CHANGELOG.name, MASTER.read_text(encoding="utf-8"))
        self.assertNotIn(SQL.name, MASTER.read_text(encoding="utf-8"))
        self.assertIn(SQL.name, self.yaml)
        self.assertIn("runInTransaction: true", self.yaml)
        for item in self.manifest["shortlisted"]:
            self.assertEqual("Portalegre", item["district"])
            reference = item["osmReference"]
            self.assertEqual(2, self.sql.count("'" + reference + "'"))
        self.assertNotIn("OSM:WAY:383543126", self.sql)  # Mora belongs to Évora.

    def test_duplicate_preflight_detects_legacy_names_without_coordinates(self):
        preflight = (DRAFTS / "2026-10-19-portalegre-preflight-READONLY.sql").read_text(
            encoding="utf-8")
        self.assertEqual(9, preflight.count("('OSM:WAY:"))
        self.assertIn("SAME_NAME_CITY_MISSING_GPS_REVIEW", preflight)
        self.assertIn("NO_MATCH_FOUND_REVIEW_BEFORE_INSERT", preflight)
        self.assertIn("LOWER(BTRIM(v.city))", preflight)
        self.assertNotRegex(
            re.sub(r"(?m)^--[^\n]*", "", preflight),
            r"\b(?:INSERT|UPDATE|DELETE|DROP|TRUNCATE|ALTER)\b"
        )

    def test_never_mutates_existing_venue_records(self):
        executable = re.sub(r"(?m)^--[^\n]*", "", self.sql)
        for disallowed in (
            r"\bUPDATE\s+", r"\bDELETE\s+FROM\b",
            r"\bDROP\s+", r"\bTRUNCATE\s+", r"\bALTER\s+",
        ):
            self.assertNotRegex(executable, disallowed)
        self.assertRegex(executable, r"INSERT\s+INTO\s+desporto24\.venue")

    def test_applies_once_and_can_be_rerun_without_duplicates(self):
        con = sample_database()
        con.executescript(self.sql)
        first = rows(con)
        self.assertEqual(9, len(first))
        self.assertTrue(all(r[1] == "OPENSTREETMAP" for r in first))
        con.executescript(self.sql)
        self.assertEqual(first, rows(con))

    def test_existing_osm_source_reference_remains_untouched(self):
        con = sample_database()
        con.execute("""
            INSERT INTO desporto24.venue
            (name, city, address, information_source, source_reference, created_at)
            VALUES ('Nome mantido do utilizador', 'Elvas', 'Rua Original',
                    'OPENSTREETMAP', 'OSM:WAY:1348549266', CURRENT_TIMESTAMP)
        """)
        con.executescript(self.sql)
        self.assertEqual(9, len(rows(con)))  # 1 old + 8 newly added
        original = con.execute(
            "SELECT name, address FROM desporto24.venue "
            "WHERE source_reference='OSM:WAY:1348549266'"
        ).fetchone()
        self.assertEqual(("Nome mantido do utilizador", "Rua Original"), original)

    def test_legacy_venue_without_gps_is_not_duplicated(self):
        con = sample_database()
        con.execute("""
            INSERT INTO desporto24.venue
            (name, city, address, information_source, created_at)
            VALUES ('Estádio Patalino', 'Elvas', 'Morada comunitária antiga',
                    'COMMUNITY', CURRENT_TIMESTAMP)
        """)
        con.executescript(self.sql)
        self.assertEqual(9, len(rows(con)))  # old record + 8 new
        unchanged = con.execute("""
            SELECT address, latitude, longitude FROM desporto24.venue
            WHERE name='Estádio Patalino'
        """).fetchone()
        self.assertEqual(('Morada comunitária antiga', None, None), unchanged)
        con.executescript(self.sql)
        self.assertEqual(9, len(rows(con)))

    def test_unrelated_city_with_same_name_does_not_block_a_venue(self):
        con = sample_database()
        con.execute("""
            INSERT INTO desporto24.venue
            (name, city, address, information_source, created_at)
            VALUES ('Estádio Patalino', 'Coimbra', 'Recinto homónimo',
                    'COMMUNITY', CURRENT_TIMESTAMP)
        """)
        con.executescript(self.sql)
        self.assertEqual(10, len(rows(con)))

    def test_existing_community_location_and_name_is_not_overwritten(self):
        con = sample_database()
        con.execute("""
            INSERT INTO desporto24.venue
            (name, city, address, information_source, latitude, longitude, created_at)
            VALUES ('Estádio Patalino', 'Elvas', 'Morada da comunidade',
                    'COMMUNITY', 38.8772886, -7.1577876, CURRENT_TIMESTAMP)
        """)
        con.executescript(self.sql)
        self.assertEqual(9, len(rows(con)))  # 1 existing + 8 new
        existing = con.execute(
            "SELECT address, information_source FROM desporto24.venue "
            "WHERE name='Estádio Patalino'"
        ).fetchone()
        self.assertEqual(("Morada da comunidade", "COMMUNITY"), existing)


if __name__ == "__main__":
    unittest.main()

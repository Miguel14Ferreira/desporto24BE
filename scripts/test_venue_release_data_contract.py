"""Standard-library regression checks for the first 24 Liquibase venue proposals.

These tests inspect the source SQL and Java entity/catalogue. They do NOT verify
production duplicates or replace executing Liquibase on PostgreSQL 16.
"""
from pathlib import Path
import re
import unittest

ROOT = Path(__file__).resolve().parent.parent
CHANGES = ROOT / "src/main/resources/db/changelog/changes"
CATALOG = ROOT / "src/main/java/com/example/desporto24/constant/SportsCatalog.java"
BATCHES = [
    ("2026-10-19-portalegre-source-checked.sql", 9),
    ("2026-10-19-first-official-district-venues.sql", 8),
    ("2026-10-19-pestana-7-source-corroborated.sql", 7),
]

ROW = re.compile(
    r"\bSELECT\s+'((?:''|[^'])*)'\s*,\s*'((?:''|[^'])*)'\s*,\s*"
    r"'((?:''|[^'])*)'\s*,\s*'((?:''|[^'])*)'\s*,\s*"
    r"'((?:''|[^'])*)'\s*,\s*'((?:''|[^'])*)'",
    re.IGNORECASE,
)
REF = re.compile(r"(?:OSM:WAY:[0-9]+|JYNLO:(?:DOCUMENTED|CATALOG):[A-Z0-9:_-]+)")

DISTRICTS = {
    "Aveiro", "Beja", "Braga", "Bragança", "Castelo Branco",
    "Coimbra", "Évora", "Faro", "Guarda", "Leiria",
    "Lisboa", "Portalegre", "Porto", "Santarém", "Setúbal",
    "Viana do Castelo", "Vila Real", "Viseu",
}


class ReleaseVenueDataContractTest(unittest.TestCase):
    @classmethod
    def setUpClass(cls):
        cls.records = []
        cls.refs = set()
        for filename, expected_count in BATCHES:
            sql = (CHANGES / filename).read_text(encoding="utf-8")
            statements = sql.split("INSERT INTO desporto24.venue")[1:]
            if len(statements) != expected_count:
                raise AssertionError(f"{filename}: expected {expected_count} rows")
            for statement in statements:
                values = ROW.search(statement)
                if not values:
                    raise AssertionError(f"{filename}: INSERT values cannot be parsed")
                refs = set(REF.findall(statement))
                if len(refs) != 1:
                    raise AssertionError(f"{filename}: missing or ambiguous source reference")
                ref = next(iter(refs))
                if ref in cls.refs:
                    raise AssertionError(f"duplicate source reference {ref}")
                cls.refs.add(ref)
                cls.records.append(tuple(s.replace("''", "'") for s in values.groups()))

        catalog_java = CATALOG.read_text(encoding="utf-8")
        cls.allowed_sports = set(re.findall(r'^\s*"([^"]+)",?\s*$', catalog_java, re.M))

    def test_every_new_venue_uses_known_sports(self):
        self.assertEqual(24, len(self.records))
        for name, city, district, address, sports, venue_type in self.records:
            with self.subTest(name=name):
                self.assertTrue(sports)
                self.assertTrue(set(sports.split(";")).issubset(self.allowed_sports))

    def test_districts_and_entity_column_limits(self):
        for name, city, district, address, sports, venue_type in self.records:
            with self.subTest(name=name):
                self.assertIn(district, DISTRICTS)
                self.assertLessEqual(len(name), 140)
                self.assertLessEqual(len(city), 120)
                self.assertLessEqual(len(address), 255)
                self.assertLessEqual(len(sports), 255)
                self.assertLessEqual(len(venue_type), 80)

    def test_each_venue_has_a_unique_source_id(self):
        self.assertEqual(24, len(self.refs))
        self.assertEqual(9, sum(ref.startswith("OSM:WAY:") for ref in self.refs))
        self.assertEqual(15, sum(ref.startswith("JYNLO:") for ref in self.refs))


if __name__ == "__main__":
    unittest.main()

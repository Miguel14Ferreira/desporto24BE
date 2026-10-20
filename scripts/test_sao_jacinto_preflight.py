"""Safety/manifest contract for the corrected São Jacinto read-only duplicate check.

Pure standard-library tests: no credentials, network, live database or writes.
PostgreSQL behaviour is exercised separately in the disposable PG16 GitHub CI.
"""
import json
from pathlib import Path
import re
import unittest

ROOT = Path(__file__).resolve().parents[1]
VENUES = ROOT / "src/main/resources/db/changelog"
PREFLIGHT = VENUES / "venue-imports/preflight-sao-jacinto-READONLY.sql"
BATCH = VENUES / "changes/2026-10-20-national-municipal-batch-13.sql"
MASTER = VENUES / "db.changelog-master.yaml"
MANIFEST = ROOT / "docs/venue-imports/national-municipal-batch-13-20261020.json"
SOURCE_REFERENCE = "JYNLO:DOCUMENTED:AVEIRO:PISCINAS_SAO_JACINTO"


class SaoJacintoPreflightTest(unittest.TestCase):
    @classmethod
    def setUpClass(cls):
        cls.sql = PREFLIGHT.read_text(encoding="utf-8")
        cls.manifest = json.loads(MANIFEST.read_text(encoding="utf-8"))
        cls.entry = next(
            record for record in cls.manifest["records"]
            if record["sourceReference"] == SOURCE_REFERENCE
        )

    def test_select_only_and_not_an_active_liquibase_changeset(self):
        # Removing SQL comments prevents a safety comment containing "INSERT"
        # from being mistaken for an actual SQL mutation.
        body = re.sub(r"(?m)^\s*--[^\n]*(?:\n|$)", "", self.sql)
        self.assertRegex(body, r"^\s*WITH\s+proposed\s+AS\s*\(")
        self.assertRegex(body, r"\bSELECT\b")
        self.assertNotRegex(
            body,
            r"\b(?:INSERT|UPDATE|DELETE|CREATE|ALTER|DROP|TRUNCATE|GRANT|REVOKE|CALL|DO)\b",
        )
        self.assertNotIn(PREFLIGHT.name, MASTER.read_text(encoding="utf-8"))

    def test_reference_and_corrected_official_name_match_production_candidate(self):
        self.assertEqual(self.entry["name"], "Piscina Municipal de São Jacinto")
        self.assertEqual(self.entry["city"], "São Jacinto")
        self.assertEqual(self.entry["district"], "Aveiro")
        self.assertEqual(self.entry["address"], "São Jacinto, Aveiro")
        self.assertFalse(self.entry["productionDuplicateChecked"])
        self.assertIsNone(self.entry["latitude"])
        self.assertIsNone(self.entry["longitude"])
        batch = BATCH.read_text(encoding="utf-8")
        for value in (
            SOURCE_REFERENCE, self.entry["name"], self.entry["city"],
            self.entry["district"],
        ):
            self.assertIn(value, self.sql)
            self.assertIn(value, batch)
        self.assertNotIn("Avenida Ria-Mar", self.sql)

    def test_alias_detection_is_review_only_and_bounded(self):
        self.assertIn("POTENTIAL_POOL_ALIAS_REVIEW", self.sql)
        self.assertIn("SAME_NAME_CITY_REVIEW", self.sql)
        self.assertIn("SAME_SOURCE_REFERENCE_REVIEW", self.sql)
        self.assertIn("NO_MATCH_FOUND", self.sql)
        self.assertIn("LIMIT 50", self.sql)
        self.assertIn("desporto24.venue", self.sql)
        # A no-match result is deliberately not a claim that publication is safe.
        self.assertIn("other spellings/aliases manually", self.sql)


if __name__ == "__main__":
    unittest.main()

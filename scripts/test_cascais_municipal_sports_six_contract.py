#!/usr/bin/env python3
"""Offline contract checks for the six-source-reviewed Cascais draft; no database."""
from pathlib import Path
import re
import unittest

ROOT = Path(__file__).resolve().parents[1]
BASE = ROOT / "src/main/resources/db/changelog/venue-imports"
MASTER = ROOT / "src/main/resources/db/changelog/db.changelog-master.yaml"

class CascaisMunicipalSportsSixContractTest(unittest.TestCase):
    def test_draft_is_not_registered_in_liquibase_master(self):
        master = MASTER.read_text(encoding="utf-8")
        self.assertNotIn("cascais-municipal-sports-6", master)

    def test_mutations_remain_guarded_and_limited(self):
        text = (BASE / "draft-cascais-municipal-sports-6-NOT-ACTIVE.sql").read_text(encoding="utf-8")
        self.assertEqual(text.count("CASCAIS:SPORTS:equipamento_desportivo."), 6)
        self.assertIn("SET sports = p.sport", text)
        self.assertIn("v.id = p.id", text)
        self.assertIn("v.source_reference = p.source_reference", text)
        self.assertIn("v.name = p.name", text)
        self.assertIn("v.information_source = 'OFFICIAL'", text)
        self.assertIn("v.sports IS NULL OR BTRIM(v.sports) = ''", text)
        self.assertNotRegex(text, r"(?im)^\s*(?:DELETE|INSERT|ALTER|DROP|TRUNCATE)\s")

    def test_preflight_has_no_mutations(self):
        text = (BASE / "preflight-cascais-municipal-sports-6-READONLY.sql").read_text(encoding="utf-8")
        code = "\n".join(line.split("--", 1)[0] for line in text.splitlines())
        self.assertEqual(code.count("CASCAIS:SPORTS:equipamento_desportivo."), 6)
        self.assertNotRegex(code, r"(?im)^\s*(?:UPDATE|DELETE|INSERT|ALTER|DROP|TRUNCATE)\s")
        self.assertIn("ELIGIBLE_PENDING_APPROVAL", text)

if __name__ == "__main__":
    unittest.main()

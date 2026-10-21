"""Verify the exact READ ONLY pilot SQL stays pinned to approved-free proposals."""
import json
from pathlib import Path
import re
import unittest

ROOT = Path(__file__).resolve().parent.parent
SQL_FILE = ROOT / "src/main/resources/db/changelog/venue-imports/preflight-first-three-municipal-live-READONLY.sql"
PROPOSALS = ROOT / "docs/venue-imports/first-three-proposals-unapproved-20261021.jsonl"
DECISIONS = ROOT / "docs/venue-imports/first-three-decisions-pending-20261021.json"


class FirstThreeReadOnlyGateTests(unittest.TestCase):
    def test_sql_is_read_only_and_bound_to_exact_candidates(self):
        sql = SQL_FILE.read_text(encoding="utf-8")
        rows = [json.loads(line) for line in PROPOSALS.read_text(encoding="utf-8").splitlines()
                if line.strip()]
        self.assertEqual(len(rows), 3)
        self.assertEqual(sql.count("'OSM:WAY:"), 3)
        for item in rows:
            self.assertFalse(item["approved"])
            self.assertIn("'" + item["sourceReference"] + "'", sql)
            self.assertIn("'" + item["name"] + "'", sql)
            self.assertIn("'" + item["city"] + "'", sql)
            self.assertIn(f'{item["latitude"]:.7f}', sql)
            self.assertIn(f'{item["longitude"]:.7f}', sql)
        decisions = json.loads(DECISIONS.read_text(encoding="utf-8"))
        self.assertEqual({d["sourceReference"] for d in decisions},
                         {p["sourceReference"] for p in rows})
        self.assertTrue(all(d["decision"] == "PENDING" for d in decisions))
        executable = re.sub(r"(?m)^--[^\\n]*", "", sql)
        self.assertIn("BEGIN TRANSACTION READ ONLY;", executable)
        self.assertIn("SET LOCAL statement_timeout", executable)
        self.assertIn("ROLLBACK;", executable)
        self.assertNotRegex(executable, r"(?i)\\b(?:INSERT|UPDATE|DELETE|MERGE|"
                            r"ALTER|TRUNCATE|DROP|GRANT|REVOKE|CREATE|COPY|DO)\\b")
        self.assertIn("distance_metres<=100", executable)
        self.assertIn("distance_metres>100 AND distance_metres<=150", executable)
        self.assertIn("name_or_alias_city", executable)
        self.assertIn("exact_ref", executable)
        self.assertIn("potential_collision_count", executable)
        self.assertIn("nearby_100_150m_count", executable)


if __name__ == "__main__":
    unittest.main()

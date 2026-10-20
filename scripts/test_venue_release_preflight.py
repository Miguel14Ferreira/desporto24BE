"""No-db safety checks for the release-wide *read-only* 24-venue preflight."""
from pathlib import Path
import re
import unittest

ROOT = Path(__file__).resolve().parent.parent
RELEASE = ROOT / "src/main/resources/db/changelog/changes"
PREFLIGHT = ROOT / "src/main/resources/db/changelog/venue-imports/preflight-24-venues-READONLY.sql"
BATCHES = [
    "2026-10-19-portalegre-source-checked.sql",
    "2026-10-19-first-official-district-venues.sql",
    "2026-10-19-pestana-7-source-corroborated.sql",
]

REF = re.compile(r"(?:OSM:WAY:[0-9]+|JYNLO:(?:DOCUMENTED|CATALOG):[A-Z0-9:_-]+)")


class ReleaseVenuePreflightTest(unittest.TestCase):
    def test_exactly_the_24_released_references_are_checked(self):
        statements = "\n".join((RELEASE / name).read_text(encoding="utf-8")
                               for name in BATCHES)
        source_refs = set(REF.findall(statements))
        preflight = PREFLIGHT.read_text(encoding="utf-8")
        preview_refs = set(REF.findall(preflight))
        self.assertEqual(24, len(source_refs))
        self.assertEqual(source_refs, preview_refs)
        self.assertEqual(24, len(re.findall(
            r"(?m)^  \('(OSM:WAY:|JYNLO:)", preflight)))

    def test_sql_is_read_only_and_proximity_requires_verified_coordinates(self):
        raw = PREFLIGHT.read_text(encoding="utf-8")
        sql = re.sub(r"(?m)^--[^\n]*", "", raw)
        self.assertTrue(sql.lstrip().startswith("WITH proposed("))
        self.assertIn("LEFT JOIN LATERAL", sql)
        self.assertIn("LIMIT 20", sql)
        self.assertIn("p.latitude IS NOT NULL", sql)
        self.assertIn("p.longitude IS NOT NULL", sql)
        self.assertNotRegex(sql, r"\b(?:INSERT|UPDATE|DELETE|DROP|TRUNCATE|ALTER|CREATE|GRANT|REVOKE|DO|CALL)\b")

if __name__ == "__main__":
    unittest.main()

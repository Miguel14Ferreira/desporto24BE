"""Static parity between the district backfill migration and SELECT-only preview."""
from pathlib import Path
import re
import unittest

ROOT=Path(__file__).resolve().parent.parent
SQL=ROOT / "src/main/resources/db/changelog/changes/2026-10-19-backfill-known-districts.sql"
PREVIEW=ROOT / "src/main/resources/db/changelog/venue-imports/preflight-district-backfill-READONLY.sql"

class DistrictPreviewTest(unittest.TestCase):
    def test_all_source_mappings_are_identical_in_preview(self):
        source=SQL.read_text(encoding="utf-8")
        preview=PREVIEW.read_text(encoding="utf-8")
        for name in ("district_by_city", "lisboa_localities"):
            pattern=rf"WITH {name}\(city_key, district_value\) AS \(\s*VALUES([\s\S]*?)\n\)"
            src=re.search(pattern,source)
            dst=re.search(pattern,preview)
            self.assertIsNotNone(src, name)
            self.assertIsNotNone(dst, name)
            self.assertEqual(src.group(1).strip(),dst.group(1).strip(),name)

    def test_read_only_preview_matches_source_geo_restrictions(self):
        preview=PREVIEW.read_text()
        sql=re.sub(r"(?m)^--[^\n]*", "", preview)
        self.assertEqual(len(re.findall(r"(?m)^SELECT\b", sql)),2)
        self.assertNotRegex(sql,r"\b(?:UPDATE|INSERT|DELETE|DROP|TRUNCATE|ALTER)\b")
        for check in (
            "v.district IS NULL OR BTRIM(v.district) = ''",
            "v.latitude BETWEEN 38.60 AND 38.91",
            "v.longitude BETWEEN -9.62 AND -9.04",
        ):
            self.assertIn(check,sql)

if __name__=="__main__":
    unittest.main()

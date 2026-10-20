"""Offline guarantee: Portalegre batch-2 document cannot accidentally be treated as an approved import."""
import json
from pathlib import Path
import re
import unittest

ROOT = Path(__file__).resolve().parent.parent
SECOND = ROOT / "docs/venue-imports/portalegre-2026-10-19-batch2-corroborated.json"
FIRST = ROOT / "docs/venue-imports/portalegre-2026-10-19-source-review.json"
MASTER = ROOT / "src/main/resources/db/changelog/db.changelog-master.yaml"

class PortalegreBatchTwoTest(unittest.TestCase):
    def test_no_approved_venues_or_overlapping_osm_ids(self):
        obj = json.loads(SECOND.read_text(encoding="utf-8"))
        older = json.loads(FIRST.read_text(encoding="utf-8"))
        records = obj["venues"]
        self.assertEqual(13, len(records))
        self.assertEqual(0, obj["approved"])
        ids = [r["osmReference"] for r in records]
        self.assertEqual(len(ids), len(set(ids)))
        self.assertTrue(all(re.fullmatch(r"OSM:(NODE|WAY|RELATION):[1-9][0-9]*", x) for x in ids))
        self.assertFalse(set(ids) & {x["osmReference"] for x in older["shortlisted"]})
        for candidate in records:
            self.assertTrue(candidate["independentSource"].startswith("https://"))
            self.assertFalse(candidate["approvedForLiquibase"])
            self.assertFalse(candidate["streetAddressVerified"])
            self.assertFalse(candidate["checkedAgainstProductionDatabase"])

    def test_unapproved_manifest_is_not_referenced_by_liquibase(self):
        master = MASTER.read_text(encoding="utf-8")
        self.assertNotIn("portalegre-2026-10-19-batch2-corroborated", master)
        self.assertNotIn("venue-imports/drafts", master)

if __name__ == "__main__":
    unittest.main()

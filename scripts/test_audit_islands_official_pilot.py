"""Offline source and privacy tests for first Madeira/Azores candidates."""
import copy
import json
from pathlib import Path
import sys
import unittest

sys.path.insert(0, str(Path(__file__).resolve().parent))
from audit_islands_official_pilot import (
    catalog_sports, compare_public, validate_manifest,
)


ROOT = Path(__file__).resolve().parent.parent


def source_data():
    p = ROOT / "docs/venue-imports/islands-first-six-official-sources-20261026.json"
    return json.loads(p.read_text(encoding="utf-8"))


def allowed_sports():
    p = ROOT / "src/main/java/com/example/desporto24/constant/SportsCatalog.java"
    return catalog_sports(p.read_text(encoding="utf-8"))


class IslandVenueAuditTest(unittest.TestCase):
    def test_all_six_have_official_sources_no_phone_gps_or_import_approval(self):
        rows = validate_manifest(source_data(), allowed_sports())
        self.assertEqual(6, len(rows))
        self.assertEqual(2, len([r for r in rows if r["district"] == "Açores"]))
        self.assertEqual(4, len([r for r in rows if r["district"] == "Madeira"]))
        self.assertTrue(all(r["contact"] is None for r in rows))

    def test_unknown_sport_rejected_instead_of_guessing(self):
        src = source_data()
        src["venues"][0]["sports"] = ["Futsal inventado"]
        with self.assertRaises(ValueError):
            validate_manifest(src, allowed_sports())

    def test_unverified_access_cannot_be_marked_true(self):
        src = source_data()
        src["venues"][0]["currentAccessConfirmed"] = True
        with self.assertRaises(ValueError):
            validate_manifest(src, allowed_sports())

    def test_official_https_domain_confusion_is_rejected(self):
        src = source_data()
        src["venues"][1]["sourceUrls"] = [
            "https://www.madeira.gov.pt.evil.test/venue"
        ]
        with self.assertRaises(ValueError):
            validate_manifest(src, allowed_sports())

    def test_duplicate_ids_and_gps_without_source_are_rejected(self):
        src = source_data()
        src["venues"][1]["sourceId"] = src["venues"][0]["sourceId"]
        with self.assertRaises(ValueError):
            validate_manifest(src, allowed_sports())
        src = source_data()
        src["venues"][0]["latitude"] = 37.75
        with self.assertRaises(ValueError):
            validate_manifest(src, allowed_sports())

    def test_catalogue_exact_name_city_and_different_city(self):
        src = source_data()
        rows = validate_manifest(src, allowed_sports())
        a = {"id": 92, "name": rows[0]["name"], "city": rows[0]["city"],
             "district": "Faro", "contact": "PRIVATE-CONTACT"}
        b = {"id": 93, "name": rows[1]["name"], "city": "Braga",
             "district": "Braga", "contact": "PRIVATE-CONTACT"}
        report = compare_public([a, b], rows)
        self.assertEqual("EXACT_PUBLIC_NAME_AND_CITY", report["results"][0]["status"])
        self.assertEqual("NAME_IN_DIFFERENT_CITY_REVIEW", report["results"][1]["status"])
        self.assertNotIn("PRIVATE-CONTACT", json.dumps(report))
        self.assertTrue(all(not r["approvedForImport"] for r in report["results"]))

    def test_duplicate_live_ids_fail(self):
        with self.assertRaises(ValueError):
            compare_public([{"id": 1}, {"id": 1}], source_data()["venues"])


if __name__ == "__main__":
    unittest.main()

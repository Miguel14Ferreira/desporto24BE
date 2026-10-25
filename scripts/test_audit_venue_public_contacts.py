"""Offline regressions for public venue contact inventory and review-only proposals."""
import json
from pathlib import Path
import tempfile
import unittest

from audit_venue_public_contacts import (
    DISTRICTS, audit, contact_kind, https_evidence, normalized_phone,
    review_candidates, write_csv,
)


def venue(vid, contact=None, name="Pavilhão Municipal", city="Porto", district="Porto"):
    return {"id": vid, "name": name, "city": city, "district": district, "contact": contact}


def proposal(**changes):
    result = {
        "venueId": 12, "venueName": "Pavilhão Municipal", "city": "Porto",
        "district": "Porto", "phone": "+351 22 345 67 89",
        "sourceType": "municipality", "sourceUrl": "https://www.cm-porto.pt/desporto",
        "sourceLicense": "Explicit permission for reuse", "reusePermissionChecked": True,
        "observedAt": "2026-10-25",
    }
    return {**result, **changes}


class ContactAuditTest(unittest.TestCase):
    def test_exact_single_phone_only(self):
        self.assertEqual("+351223456789", normalized_phone("Telefone: 22 345 67 89"))
        self.assertEqual("+351912345678", normalized_phone("912 345 678"))
        for item in ("911 222 333; 222 333 444", "info@example.pt",
                     "https://example.com", "112", "00351223456789",
                     "javascript:alert(1)", "912345678 ext 2", "9-1234", None):
            self.assertIsNone(normalized_phone(item), item)

    def test_classifies_existing_contact_without_guessing(self):
        self.assertEqual("without_contact", contact_kind(" "))
        self.assertEqual("phone", contact_kind("22 345 67 89"))
        self.assertEqual("email", contact_kind("desporto@example.pt"))
        self.assertEqual("web", contact_kind("https://example.pt"))
        self.assertEqual("other", contact_kind("Contactar a secretaria"))

    def test_all_18_districts_and_unknown_are_reported(self):
        rows, totals = audit([
            venue(1, "223456789", district="Porto"),
            venue(2, "", district="Évora"),
            venue(3, "info@recinto.pt", district=None),
            venue(4, "unknown", district="Porto"),
        ])
        self.assertEqual(19, len(rows))
        self.assertEqual(18, len(DISTRICTS))
        self.assertEqual({"total": 4, "without_contact": 1, "phone": 1,
                          "email": 1, "web": 0, "other": 1}, totals)
        self.assertEqual(1, next(r for r in rows if r["district"] == "Por confirmar")["email"])
        self.assertEqual(0, next(r for r in rows if r["district"] == "Bragança")["total"])

    def test_incomplete_catalogue_is_not_silently_counted(self):
        for record in ([{"id": 1}, {"id": 1}], [{"id": "1"}], {"venues": []}):
            with self.assertRaises(ValueError):
                audit(record)

    def test_candidate_never_is_approved_or_applied(self):
        current = [venue(12)]
        rows = review_candidates(current, [proposal()])
        self.assertEqual("REVIEW_REQUIRED", rows[0]["status"])
        self.assertEqual("+351223456789", rows[0]["suggested_phone"])
        self.assertIsNone(current[0]["contact"])

    def test_matching_requires_id_name_city_district_exactly(self):
        base = [venue(12)]
        cases = [
            ({"venueId": 14}, "NO_EXACT_VENUE_ID"),
            ({"venueName": "Estádio do Dragão"}, "NAME_MISMATCH"),
            ({"city": "Lisboa"}, "CITY_MISMATCH"),
            ({"district": "Lisboa"}, "DISTRICT_MISMATCH"),
            ({"phone": "112"}, "INVALID_PHONE"),
        ]
        for override, expected in cases:
            with self.subTest(override=override):
                self.assertEqual(expected, review_candidates(base, [proposal(**override)])[0]["status"])

    def test_never_overwrites_existing_contact(self):
        rows = review_candidates([venue(12, "contacto@pavilhao.pt")], [proposal()])
        self.assertEqual("EXISTING_CONTACT_REVIEW", rows[0]["status"])

    def test_conflicting_sources_referred_to_person(self):
        rows = review_candidates([venue(12)], [
            proposal(), proposal(phone="+351 91 222 33 44")
        ])
        self.assertEqual(["CONFLICTING_SOURCE_PHONES"] * 2, [r["status"] for r in rows])

    def test_license_and_source_not_inferred(self):
        rows = review_candidates([venue(12)], [
            proposal(sourceUrl="http://example.com"),
            proposal(reusePermissionChecked=False),
        ])
        self.assertEqual("SOURCE_EVIDENCE_INCOMPLETE", rows[0]["status"])
        self.assertEqual("REUSE_PERMISSION_UNCONFIRMED", rows[1]["status"])
        self.assertFalse(https_evidence("http://example.pt"))
        self.assertFalse(https_evidence("https://user:pass@example.pt/path"))
        self.assertTrue(https_evidence("https://www.cm-porto.pt/instalacoes"))

    def test_review_csv_excel_safe_and_summary_does_not_contain_phones(self):
        with tempfile.TemporaryDirectory() as folder:
            output = Path(folder) / "private-review.csv"
            row = review_candidates([venue(12)], [proposal(venueName="=SUM(1+1)")])[0]
            write_csv(output, [row], tuple(row.keys()))
            csv_text = output.read_text(encoding="utf-8-sig")
            self.assertNotIn(";=SUM(", csv_text)
            self.assertNotIn(";+351223456789", csv_text)
            _, totals = audit([venue(12, "223456789")])
            self.assertNotIn("223456789", json.dumps(totals))


if __name__ == "__main__":
    unittest.main()

"""Offline regression cases for opt-in GPS-only venue Liquibase generation."""
import json
from pathlib import Path
import tempfile
import unittest

from test_generate_verified_venue_liquibase import gen, sample


def gps_only(**updates):
    row = sample(
        address=None,
        postalAddressStatus="UNKNOWN_GPS_ONLY",
        gpsVerified=True,
        gpsEvidenceUrl="https://www.cm-cantanhede.pt/mcsite/Entidade/1680/pavilhao-marialvas",
        gpsVerificationNotes=("The municipality lists the exact venue and GPS. "
                              "Independent identity and access review are required."),
    )
    row.update(updates)
    return row


class GpsOnlyApprovedSeedTest(unittest.TestCase):
    def test_null_postal_address_is_preserved_in_opt_in_sql(self):
        approved = gen.validate(gps_only())
        self.assertIsNone(approved["address"])
        sql = gen.render_sql([approved])
        self.assertIn("NULL", sql)
        self.assertIn("WHERE NOT EXISTS", sql)
        self.assertIn("ON CONFLICT DO NOTHING", sql)
        with tempfile.TemporaryDirectory() as tmp:
            src = Path(tmp) / "approved.jsonl"
            src.write_text(json.dumps(gps_only()) + "\n", encoding="utf-8")
            sql_file, yaml_file, count = gen.generate(
                src, Path(tmp) / "seed", "gps-only-verified"
            )
            self.assertEqual(1, count)
            self.assertIn("NULL", sql_file.read_text())
            self.assertIn("runInTransaction: true", yaml_file.read_text())

    def test_missing_or_unverified_evidence_cannot_be_imported(self):
        cases = [
            {"address": ""},
            {"address": "morada por confirmar"},
            {"postalAddressStatus": None},
            {"gpsVerified": False},
            {"gpsVerificationNotes": "only OSM"},
            {"gpsEvidenceUrl": "http://municipio.example/"},
            {"gpsEvidenceUrl": "https://www.openstreetmap.org/way/123"},
            {"approved": False},
            {"latitude": None},
            {"longitude": -20},
        ]
        for patch in cases:
            with self.subTest(patch=patch), self.assertRaises(ValueError):
                gen.validate(gps_only(**patch))
        missing = gps_only()
        missing.pop("address")
        with self.assertRaisesRegex(ValueError, "explicit null"):
            gen.validate(missing)

    def test_postal_address_import_still_works(self):
        self.assertEqual("Rua do Campo, Portalegre", gen.validate(sample())["address"])
        with self.assertRaises(ValueError):
            gen.validate(sample(postalAddressStatus="UNKNOWN_GPS_ONLY"))


if __name__ == "__main__":
    unittest.main()

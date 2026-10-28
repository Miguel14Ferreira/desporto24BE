"""No-network, no-write tests for a 10-site official municipal review shortlist."""
import json
from pathlib import Path
import tempfile
import unittest

from preflight_municipal_venues_20261028 import (
    compare, is_official_link, main, validate_manifest,
)

MANIFEST = (Path(__file__).resolve().parents[1] / "docs" / "venue-imports" /
            "municipal-braganca-guarda-shortlist-20261028.json")


def source():
    return json.loads(MANIFEST.read_text(encoding="utf-8"))


def venues():
    return [
        {"id": 1, "name": "Pavilhão Municipal Arnaldo Pereira",
         "city": "Bragança", "district": "Bragança", "contact": "private@example.test",
         "latitude": 41.8124, "longitude": -6.7777},
        {"id": 2, "name": "Complexo Desportivo da Coxa",
         "city": "Bragança", "district": "Bragança"},
        {"id": 3, "name": "Pavilhão Municipal de São Miguel",
         "city": "Guarda", "district": "Guarda"},
        {"id": 4, "name": "Pavilhão Gimnodesportivo do INATEL",
         "city": "Porto", "district": "Porto"},
    ]


class OfficialMunicipalPreflightTest(unittest.TestCase):
    def test_manifest_is_bounded_and_all_candidates_remain_unapproved(self):
        rows = validate_manifest(source())
        self.assertEqual(10, len(rows))
        self.assertEqual({"Bragança", "Guarda"}, {r["district"] for r in rows})
        self.assertTrue(all(r["approvedForImport"] is False for r in rows))

    def test_exact_alias_and_unknown_are_never_auto_imported(self):
        report = compare(venues(), source())
        by_name = {r["candidate"]: r for r in report["results"]}
        self.assertEqual("EXACT_EXISTING",
                         by_name["Pavilhão Municipal Arnaldo Pereira"]["status"])
        self.assertEqual("POSSIBLE_ALIAS_REVIEW",
                         by_name["Pavilhão da Coxa"]["status"])
        self.assertEqual("POSSIBLE_ALIAS_REVIEW",
                         by_name["Pavilhão Desportivo Municipal de São Miguel"]["status"])
        self.assertEqual("NO_LOCAL_MATCH_REVIEW",
                         by_name["Campo de Jogos do Zambito"]["status"])
        self.assertEqual("NO_LOCAL_MATCH_REVIEW",
                         by_name["Pavilhão Gimnodesportivo do INATEL"]["status"])
        self.assertEqual("READ_ONLY_NO_IMPORT", report["authorization"])
        self.assertTrue(all(not r["approvedForImport"] for r in report["results"]))

    def test_names_in_other_district_are_not_silently_considered_exact(self):
        report = compare(venues(), source())
        by_name = {r["candidate"]: r for r in report["results"]}
        self.assertEqual(0, by_name["Pavilhão Gimnodesportivo do INATEL"]["possibleMatchCount"])

    def test_blocks_malicious_unsupported_evidence_and_approvals(self):
        for address in (
            "http://www.cm-braganca.pt/venues", "https://evil.cm-braganca.pt/venue",
            "https://www.mun-guarda.pt.evil.org/venue",
            "https://name:secret@www.mun-guarda.pt/venue",
            "https://www.mun-guarda.pt:444/venue",
            "https://www.mun-guarda.pt/path#section",
        ):
            with self.subTest(address=address):
                self.assertFalse(is_official_link(address))
        data = source()
        data["venues"][0]["approvedForImport"] = True
        with self.assertRaises(ValueError):
            compare([], data)

    def test_rejects_duplicate_manifest_and_duplicate_public_ids(self):
        data = source()
        data["venues"][1] = dict(data["venues"][0])
        with self.assertRaises(ValueError):
            validate_manifest(data)
        with self.assertRaises(ValueError):
            compare([venues()[0], venues()[0]], source())

    def test_output_never_copies_contacts_coordinates_or_addresses(self):
        report = json.dumps(compare(venues(), source()), ensure_ascii=False)
        self.assertNotIn("private@example.test", report)
        self.assertNotIn("41.8124", report)
        self.assertNotIn("6.7777", report)
        self.assertNotIn("Porto", report)

    def test_cli_offline_and_inputs_remain_unchanged(self):
        with tempfile.TemporaryDirectory() as root:
            directory = Path(root)
            catalogue = directory / "catalogue.json"
            output = directory / "report.json"
            catalogue.write_text(json.dumps(venues()), encoding="utf-8")
            self.assertEqual(0, main([
                "--venues-json", str(catalogue), "--sources-json", str(MANIFEST),
                "--output-json", str(output)]))
            self.assertEqual(10, json.loads(output.read_text())["candidateCount"])
            self.assertEqual(4, len(json.loads(catalogue.read_text())))
            with self.assertRaises(SystemExit):
                main(["--venues-json", str(catalogue),
                      "--sources-json", str(MANIFEST),
                      "--output-json", str(catalogue)])


if __name__ == "__main__":
    unittest.main()

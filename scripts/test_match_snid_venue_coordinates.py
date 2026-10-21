"""Offline fixtures for source-backed, non-writing geolocation candidate matching."""
from pathlib import Path
import csv
import json
import tempfile
import unittest

from match_snid_venue_coordinates import analyse, candidate_matches, name_key, report


def venue(name, city, district, reference):
    return {"name": name, "city": city, "district": district,
            "sourceReference": reference, "address": f"{city}, Portugal",
            "sourceUrl": "https://dados.gov.pt/example"}


def snid(name, district, municipality, lat=38.7, lon=-9.15):
    return {"id": name + "_official", "name": name,
            "address": {"district": district, "addressLocality": municipality},
            "location": json.dumps({"type": "Point", "coordinates": [lon, lat]})}


def fixture_manifest():
    candidates = [
        venue("Piscinas Municipais de Évora", "Évora", "Évora", "REF-EVORA"),
        venue("Campo Municipal dos Choupos", "Porto", "Porto", "REF-CH"),
        venue("Piscina Municipal de São Jacinto", "São Jacinto", "Aveiro", "REF-SJ"),
        venue("Pavilhão Municipal da Escola", "Braga", "Braga", "REF-PAV"),
    ]
    candidates += [venue("Instalação Desportiva Não Referenciada " + str(i),
                         "Braga", "Braga", "REF-" + str(i)) for i in range(23)]
    return {"candidates": candidates}


class VenueCoordinateMatchTests(unittest.TestCase):
    def test_normalizes_accents_and_punctuation(self):
        self.assertEqual(name_key("Pavilhão N.º 2 — Évora"),
                         name_key("Pavilhao N.º 2 - Evora"))

    def test_matches_only_within_officially_labeled_district_and_never_auto_approves(self):
        records = [
            snid("Piscinas Municipais de Évora", "Évora", "Évora"),
            snid("Campo Municipal dos Choupos do Porto", "Porto", "Porto", 41.16, -8.60),
            snid("Piscina Municipal de São Jacinto", "Lisboa", "São Jacinto"),
            snid("Pavilhão Municipal da Escola", "Braga", "Braga"),
            snid("Pavilhão Municipal da Escola", "Braga", "Braga", 41.6, -8.5),
            # Same physical point repeated must not increase exact count.
            snid("Pavilhão Municipal da Escola", "Braga", "Braga", 41.6, -8.5),
            snid("Pavilhão Municipal da Escola", "Braga", "Braga", 36.0, -8.5),
        ]
        with tempfile.TemporaryDirectory() as folder:
            source = Path(folder) / "source.json"
            source.write_text(json.dumps(records, ensure_ascii=False), encoding="utf-8")
            results, stats = analyse(fixture_manifest(), source)
        self.assertEqual(27, len(results))
        self.assertEqual("REVIEW_EXACT_NAME", results[0]["status"])
        self.assertEqual("EXACT_NAME_CITY_AND_DISTRICT_REVIEW",
                         results[0]["suggestions"][0]["match_type"])
        self.assertEqual("REVIEW_SIMILAR_NAME", results[1]["status"])
        self.assertEqual("NO_PLAUSIBLE_SNID_NAME_MATCH", results[2]["status"])
        self.assertEqual("REVIEW_MULTIPLE_EXACT", results[3]["status"])
        self.assertEqual(2, results[3]["exact_match_count"])
        self.assertEqual(1, stats["with_one_exact_name_match"])
        self.assertEqual(1, stats["with_multiple_exact_name_matches"])
        self.assertEqual(1, stats["with_similar_name_only"])

    def test_same_source_site_repeated_is_one_location_to_review(self):
        repeated = [
            snid("Piscinas Municipais de Évora", "Évora", "Évora"),
            snid("Piscinas Municipais de Évora", "Évora", "Évora"),
            snid("Piscinas Municipais de Évora", "Évora", "Évora"),
        ]
        with tempfile.TemporaryDirectory() as folder:
            source = Path(folder) / "source.json"
            source.write_text(json.dumps(repeated, ensure_ascii=False), encoding="utf-8")
            results, summary = analyse(fixture_manifest(), source)
        self.assertEqual("REVIEW_EXACT_NAME", results[0]["status"])
        self.assertEqual(1, results[0]["exact_match_count"])
        self.assertEqual(2, summary["duplicate_name_points_ignored"])
        self.assertEqual(1, summary["named_distinct_points_with_district"])

    def test_no_guess_for_common_words_or_complex_centroid(self):
        records = [{
            "name": "Complexo Desportivo da Rodovia",
            "key": name_key("Complexo Desportivo da Rodovia"),
            "municipality": "Braga", "district": "Braga",
            "latitude": 41.55, "longitude": -8.42, "snid_id": "complex"
        }]
        candidate = venue("Campo de Futebol N.º1 do Complexo Desportivo da Rodovia",
                          "Braga", "Braga", "REF-FOOT")
        status, matches, _ = candidate_matches(candidate, records)
        self.assertNotEqual("REVIEW_EXACT_NAME", status)
        # Even plausible coordinates are suggestions, never injected into draft SQL.
        self.assertTrue(status.endswith("REVIEW") or status == "NO_PLAUSIBLE_SNID_NAME_MATCH")
        self.assertTrue(all(match["match_type"] != "EXACT_NAME_CITY_AND_DISTRICT_REVIEW"
                            for match in matches))

    def test_report_has_27_records_and_no_sql_or_production_operations(self):
        with tempfile.TemporaryDirectory() as folder:
            path = Path(folder)
            manifest = path / "manifest.json"
            source = path / "snid.json"
            manifest.write_text(json.dumps(fixture_manifest()), encoding="utf-8")
            source.write_text(json.dumps([snid("Piscinas Municipais de Évora",
                                              "Évora", "Évora")]), encoding="utf-8")
            result, metadata = report(manifest, source, path / "output")
            self.assertEqual(27, len(result))
            self.assertEqual(27, metadata["candidates_checked"])
            csv_file = path / "output" / "snid-coordinate-review-27.csv"
            with csv_file.open(encoding="utf-8-sig", newline="") as file:
                rows = list(csv.DictReader(file, delimiter=";"))
            self.assertEqual(27, len(rows))
            self.assertEqual("REVIEW_EXACT_NAME", rows[0]["status"])
            self.assertFalse(any("APPROVED" in row["status"] for row in rows))
            self.assertTrue((path / "output" / "snid-coordinate-review-27.json").exists())

    def test_refuses_wrong_manifest_and_duplicate_identifiers(self):
        with tempfile.TemporaryDirectory() as folder:
            source = Path(folder) / "source.json"
            source.write_text("[]", encoding="utf-8")
            bad = fixture_manifest()
            bad["candidates"].pop()
            with self.assertRaisesRegex(ValueError, "27"):
                analyse(bad, source)
            bad = fixture_manifest()
            bad["candidates"][1]["sourceReference"] = "REF-EVORA"
            with self.assertRaisesRegex(ValueError, "Duplicate"):
                analyse(bad, source)


if __name__ == "__main__":
    unittest.main()

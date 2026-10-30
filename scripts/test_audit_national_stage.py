#!/usr/bin/env python3
"""Offline contract tests for national source-only QA."""
import hashlib
import json
from pathlib import Path
import sys
import tempfile
import unittest

sys.path.insert(0, str(Path(__file__).resolve().parent))
from audit_national_stage import inspect, norm_name, metres
from import_osm_venues_national import DISTRICTS


class NationalStageQaTest(unittest.TestCase):
    def test_accents(self):
        self.assertEqual(norm_name("Estádio A"), norm_name("Estadio A"))
        self.assertLess(metres((38.72, -9.15), (38.7201, -9.15)), 20)

    def test_complete_two_osm_objects_flagged_not_approved(self):
        with tempfile.TemporaryDirectory() as tmp:
            work = Path(tmp)
            report = {}
            slugs = list(DISTRICTS)
            for index, slug in enumerate(slugs):
                name = DISTRICTS[slug]
                venue = {"sourceReference": f"OSM:WAY:{1000 + index}",
                         "name": "Estádio da Cidade" if index < 2 else f"Estádio {name}",
                         "city": name, "district": name, "sports": "Futebol",
                         "latitude": 40.0 if index < 2 else 39.0 + index * 0.1,
                         "longitude": -8.0}
                # The two objects have different districts but same geometry.
                content = json.dumps(venue, ensure_ascii=False) + "\n"
                venue_path = work / f"{slug}-venues.jsonl"
                review_path = work / f"{slug}-review.jsonl"
                venue_path.write_text(content, encoding="utf-8")
                review_path.write_text("", encoding="utf-8")
                report[slug] = {"status": "processed", "boundary_fingerprint": "CAOP-TEST",
                                "candidates": 2, "import_ready": 1, "review_required": 0,
                                "sha256": hashlib.sha256(venue_path.read_bytes()).hexdigest(),
                                "review_sha256": hashlib.sha256(review_path.read_bytes()).hexdigest()}
            (work / "national-report.json").write_text(json.dumps(report), encoding="utf-8")
            results, rows = inspect(work)
            self.assertEqual(results["districts_processed"], 18)
            self.assertEqual(results["status"], "complete")
            self.assertEqual(results["possible_duplicate_pair_count"], 1)
            self.assertFalse(results["publication_allowed"])
            self.assertEqual(results["approved_for_publication"], 0)
            self.assertEqual(len(rows), 18)

    def test_missing_district_does_not_claim_full_coverage(self):
        with tempfile.TemporaryDirectory() as tmp:
            work = Path(tmp)
            (work / "national-report.json").write_text("{}", encoding="utf-8")
            stats, rows = inspect(work)
            self.assertEqual(stats["status"], "partial")
            self.assertEqual(len(stats["districts_failed_or_missing"]), 18)
            self.assertEqual(rows, [])

    def test_rejects_stale_output_checksum(self):
        with tempfile.TemporaryDirectory() as tmp:
            work = Path(tmp)
            slug, name = next(iter(DISTRICTS.items()))
            (work / f"{slug}-venues.jsonl").write_text("{}\n", encoding="utf-8")
            (work / f"{slug}-review.jsonl").write_text("", encoding="utf-8")
            (work / "national-report.json").write_text(json.dumps({
                slug: {"status": "processed", "boundary_fingerprint": "digest",
                       "sha256": "WRONG", "review_sha256": "x"}}), encoding="utf-8")
            stats, rows = inspect(work)
            self.assertEqual(stats["by_district"][name]["status"], "checksum_error")
            self.assertEqual(stats["status"], "partial")
            self.assertEqual(rows, [])


if __name__ == "__main__":
    unittest.main()

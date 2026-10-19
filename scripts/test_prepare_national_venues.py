"""Offline unit tests for national venue resumable staging; zero internet needed."""
from __future__ import annotations
import hashlib
import json
from pathlib import Path
from tempfile import TemporaryDirectory
import unittest

from import_osm_venues_national import DISTRICTS
from prepare_national_venues import stage_districts, boundary_fingerprint


def fake_stage(slug, cache, refresh, municipalities, districts):
    if slug == "evora":
        raise OSError("Simulated download unavailable")
    for suffix, content in (("-venues.jsonl", '{"name":"A"}\\n'),
                            ("-review.jsonl", '{"quality":"pending"}\\n')):
        (cache / f"{slug}{suffix}").write_text(content, encoding="utf-8")
    (cache / f"{slug}-latest.osm.pbf").write_bytes(b"x" * 1_000_001)
    return cache / f"{slug}-venues.jsonl", {
        "candidates": 2, "prepared": 2, "import_ready": 1, "review_required": 1,
        "sha256": hashlib.sha256((cache / f"{slug}-venues.jsonl").read_bytes()).hexdigest(),
        "review_sha256": hashlib.sha256((cache / f"{slug}-review.jsonl").read_bytes()).hexdigest(),
    }


class ResumableNationalStagingTest(unittest.TestCase):
    def test_success_and_failure_are_checkpointed_independently(self):
        with TemporaryDirectory() as tmp:
            cache = Path(tmp)
            result = stage_districts(["portalegre", "evora"], cache, object(),
                                     object(), stage_fn=fake_stage)
            self.assertEqual(result, 1)
            data = json.loads((cache / "national-report.json").read_text())
            self.assertEqual(data["portalegre"]["status"], "processed")
            self.assertEqual(data["evora"]["status"], "failed")
            self.assertEqual(data["summary"]["districts_processed"], 1)
            self.assertNotEqual(data["summary"]["status"], "complete")

    def test_valid_snapshot_skipped_without_network(self):
        with TemporaryDirectory() as tmp:
            cache = Path(tmp)
            self.assertEqual(stage_districts(["portalegre"], cache, None, None,
                                             stage_fn=fake_stage), 0)
            def should_not_download(*args):
                raise AssertionError("Network should not be called")
            self.assertEqual(stage_districts(["portalegre"], cache, None, None,
                                             stage_fn=should_not_download), 0)

    def test_official_caop_change_invalidates_cached_classification(self):
        with TemporaryDirectory() as tmp:
            cache = Path(tmp)
            self.assertEqual(stage_districts(
                ["portalegre"], cache, None, None, stage_fn=fake_stage,
                boundary_fingerprint_value="caop-2025-first"), 0)
            def should_not_download(*args):
                raise AssertionError("Already validated with same official boundaries")
            self.assertEqual(stage_districts(
                ["portalegre"], cache, None, None,
                stage_fn=should_not_download,
                boundary_fingerprint_value="caop-2025-first"), 0)
            self.assertEqual(stage_districts(
                ["portalegre"], cache, None, None,
                stage_fn=fake_stage,
                boundary_fingerprint_value="caop-2025-updated"), 0)
            report = json.loads((cache / "national-report.json").read_text())
            self.assertEqual(report["portalegre"]["boundary_fingerprint"],
                             "caop-2025-updated")

    def test_fingerprint_uses_both_official_geometry_files(self):
        with TemporaryDirectory() as tmp:
            root = Path(tmp)
            municipalities = root / "municipalities.geojson"
            districts = root / "districts.geojson"
            municipalities.write_text("municipalities-v1")
            districts.write_text("districts-v1")
            old = boundary_fingerprint(municipalities, districts)
            self.assertEqual(old, boundary_fingerprint(municipalities, districts))
            districts.write_text("districts-v2")
            self.assertNotEqual(old, boundary_fingerprint(municipalities, districts))
            districts.write_text("districts-v1")
            municipalities.write_text("municipalities-v2")
            self.assertNotEqual(old, boundary_fingerprint(municipalities, districts))

    def test_corrupted_snapshot_restages(self):
        with TemporaryDirectory() as tmp:
            cache = Path(tmp)
            stage_districts(["portalegre"], cache, None, None, stage_fn=fake_stage)
            (cache / "portalegre-venues.jsonl").write_text("damaged", encoding="utf-8")
            self.assertEqual(stage_districts(["portalegre"], cache, None, None,
                                             stage_fn=fake_stage), 0)
            self.assertEqual(json.loads((cache / "national-report.json").read_text())
                             ["portalegre"]["status"], "processed")

    def test_legacy_unknown_status_never_claims_processed(self):
        with TemporaryDirectory() as tmp:
            cache = Path(tmp)
            (cache / "national-report.json").write_text(
                json.dumps({"portalegre": {"candidates": 99, "status": "unknown"}}))
            stage_districts(["evora"], cache, None, None, stage_fn=fake_stage)
            report = json.loads((cache / "national-report.json").read_text())
            self.assertEqual(report["portalegre"]["status"], "legacy_unverified")
            self.assertEqual(report["summary"]["districts_processed"], 0)


if __name__ == "__main__":
    unittest.main()

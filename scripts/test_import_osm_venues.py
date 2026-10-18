"""Offline regressions for the OSM importer. No Render or Overpass requests."""
import contextlib
import io
import json
import os
import pathlib
import tempfile
import unittest
from unittest.mock import patch

import import_osm_venues as importer


class OsmImportSafetyTests(unittest.TestCase):
    def test_default_is_dry_run(self):
        # Default must remain true without an environment override.
        self.assertTrue(importer.DRY_RUN)

    def test_write_requires_explicit_confirmation_before_fetch(self):
        with patch.object(importer, "BBOX", "38.70,-9.30,38.80,-9.10"), \
             patch.object(importer, "DRY_RUN", False), \
             patch.object(importer, "ADMIN_TOKEN", "placeholder"), \
             patch.object(importer, "CONFIRM_IMPORT", False), \
             patch.object(importer, "fetch_osm") as fetch:
            with self.assertRaises(SystemExit):
                importer.main()
            fetch.assert_not_called()

    def test_rejects_malformed_bbox_before_fetch(self):
        with patch.object(importer, "BBOX", "39,-9,38,-8"), \
             patch.object(importer, "fetch_osm") as fetch:
            with self.assertRaises(SystemExit):
                importer.main()
            fetch.assert_not_called()

    def test_dry_run_reports_rejected_venues_without_writing_to_api(self):
        elements = [
            {"type": "node", "id": 1, "lat": 38.7, "lon": -9.1,
             "tags": {"name": "Pavilhão A", "addr:city": "Lisboa", "sport": "basketball;volleyball"}},
            {"type": "node", "id": 2, "tags": {"addr:city": "Lisboa"}},
            {"type": "node", "id": 3, "tags": {"name": "Campo sem localidade"}},
        ]
        with tempfile.TemporaryDirectory() as temp_dir:
            report_path = str(pathlib.Path(temp_dir) / "report.json")
            with patch.object(importer, "BBOX", "38.70,-9.30,38.80,-9.10"), \
                 patch.object(importer, "DRY_RUN", True), \
                 patch.object(importer, "REPORT_PATH", report_path), \
                 patch.object(importer, "fetch_osm", return_value={"elements": elements}), \
                 patch.object(importer, "post_chunk") as post:
                with contextlib.redirect_stdout(io.StringIO()):
                    self.assertEqual(importer.main(), 0)
                post.assert_not_called()
            data = json.loads(pathlib.Path(report_path).read_text(encoding="utf-8"))
            self.assertEqual(data["osmElements"], 3)
            self.assertEqual(data["importable"], 1)
            self.assertEqual(data["rejected"]["missing_name"], 1)
            self.assertEqual(data["rejected"]["missing_city"], 1)
            self.assertEqual(data["sample"][0]["sports"], "Basquetebol;Voleibol")


if __name__ == "__main__":
    unittest.main()

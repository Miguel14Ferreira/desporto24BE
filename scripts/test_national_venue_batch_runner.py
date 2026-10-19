"""No-network, no-DB tests for nationwide offline staging and reports."""
import json
from pathlib import Path
import tempfile
import unittest
from national_venue_batch_runner import run

class NationalVenueBatchRunnerTest(unittest.TestCase):
    def test_checkpoints_report_every_district_and_does_not_fetch(self):
        with tempfile.TemporaryDirectory() as d:
            folder = Path(d)
            (folder/'portalegre-latest.osm.pbf').write_bytes(b'0' * 1_000_001)
            def fake_stage(slug, *args):
                return folder/'dummy.jsonl', {
                    'candidates': 20, 'prepared': 9, 'import_ready': 4,
                    'review_required': 5,
                }
            status = run(['beja', 'portalegre', 'evora'], folder, None, None, fake_stage)
            self.assertEqual(1, status)
            report = json.loads((folder/'national-report.json').read_text())
            self.assertEqual('failed', report['beja']['status'])
            self.assertEqual('processed', report['portalegre']['status'])
            self.assertEqual(1, report['summary']['districts_processed'])
            self.assertEqual(['beja','evora'], report['summary']['districts_failed'])
            self.assertEqual(4, report['summary']['import_ready_needs_approval'])
            self.assertEqual('partial', report['summary']['status'])

    def test_partial_runs_preserve_prior_districts(self):
        with tempfile.TemporaryDirectory() as d:
            folder = Path(d)
            for name in ('portalegre', 'evora'):
                (folder/f'{name}-latest.osm.pbf').write_bytes(b'0' * 1_000_001)
            def stage_fn(slug, *args):
                return folder/'ready.jsonl', {
                    'candidates': 10, 'prepared': 3, 'import_ready': 1,
                    'review_required': 2}
            self.assertEqual(0, run(['portalegre'], folder, None, None, stage_fn))
            self.assertEqual(0, run(['evora'], folder, None, None, stage_fn))
            data = json.loads((folder/'national-report.json').read_text())
            self.assertEqual('processed', data['portalegre']['status'])
            self.assertEqual('processed', data['evora']['status'])
            self.assertEqual(2, data['summary']['districts_processed'])
            self.assertEqual(2, data['summary']['import_ready_needs_approval'])

    def test_legacy_report_not_counted_as_officially_verified(self):
        with tempfile.TemporaryDirectory() as d:
            folder = Path(d)
            (folder/'national-report.json').write_text(json.dumps({
                'portalegre': {'candidates': 2079, 'import_ready': 13},
                'summary': {'districts': 1}
            }))
            (folder/'evora-latest.osm.pbf').write_bytes(b'0' * 1_000_001)
            status = run(['evora'], folder, None, None, lambda *args: (
                folder/'x', {'candidates': 1, 'import_ready': 0,
                             'review_required': 1}))
            self.assertEqual(0, status)
            report = json.loads((folder/'national-report.json').read_text())
            self.assertEqual(1, report['summary']['districts_processed'])
            self.assertEqual('legacy_unverified', report['portalegre']['status'])

    def test_legacy_report_with_unknown_status_is_quarantined(self):
        # Previous reports must not override the official verification marker.
        with tempfile.TemporaryDirectory() as d:
            folder = Path(d)
            (folder/'national-report.json').write_text(json.dumps({
                'portalegre': {'status': 'old_import', 'candidates': 1992,
                               'import_ready': 13}
            }), encoding='utf-8')
            (folder/'evora-latest.osm.pbf').write_bytes(b'0' * 1_000_001)
            result = run(['evora'], folder, None, None, lambda *args: (
                folder/'ready.jsonl', {'candidates': 5, 'import_ready': 0,
                                       'review_required': 5}))
            self.assertEqual(0, result)
            report = json.loads((folder/'national-report.json').read_text())
            self.assertEqual('legacy_unverified', report['portalegre']['status'])
            self.assertEqual(['portalegre'],
                             report['summary']['districts_legacy_unverified'])
            self.assertEqual(5, report['summary']['candidates_osm'])
            self.assertEqual(0, report['summary']['import_ready_needs_approval'])

    def test_completed_without_any_failure(self):
        with tempfile.TemporaryDirectory() as d:
            folder = Path(d)
            (folder/'portalegre-latest.osm.pbf').write_bytes(b'0' * 1_000_001)
            status = run(['portalegre'], folder, None, None, lambda *args: (
                folder/'dummy', {'candidates': 1, 'import_ready': 0,
                                 'review_required': 1}))
            self.assertEqual(0, status)
            report = json.loads((folder/'national-report.json').read_text())
            self.assertEqual('partial', report['summary']['status'])
            self.assertEqual(17, len(report['summary']['districts_unprocessed']))

if __name__ == '__main__':
    unittest.main()

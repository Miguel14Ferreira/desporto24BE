#!/usr/bin/env python3
"""Read-only nationwide venue candidate staging from cached OSM district PBFs.

No API writes, no Render or GitHub Actions required. Maintains a partial
report after each district; a missing extract never counts as zero venues.
"""
from __future__ import annotations
import argparse
import json
from pathlib import Path

from import_osm_venues_national import DISTRICTS, MunicipalityLookup, DistrictLookup, stage


def checkpoint(report, selected, destination):
    finished = [v for v in report.values() if v.get('status') == 'processed']
    failed = [slug for slug, v in report.items() if v.get('status') == 'failed']
    data = dict(report)
    data['summary'] = {
        'districts_target': len(DISTRICTS),
        'districts_requested_this_run': len(selected),
        'districts_processed': len(finished),
        'districts_failed': failed,
        'districts_unprocessed': [slug for slug in DISTRICTS if slug not in report],
        'districts_legacy_unverified': [slug for slug, v in report.items()
                                        if v.get('status') == 'legacy_unverified'],
        'candidates_osm': sum(v.get('candidates', 0) for v in finished),
        'import_ready_needs_approval': sum(v.get('import_ready', 0) for v in finished),
        'review_required': sum(v.get('review_required', 0) for v in finished),
        'status': 'complete' if len(finished) == len(DISTRICTS) else 'partial',
        'note': 'Read-only candidate report. No venues approved or imported.',
    }
    tmp = destination.with_name(destination.name + '.tmp')
    tmp.write_text(json.dumps(data, ensure_ascii=False, indent=2), encoding='utf-8')
    tmp.replace(destination)
    return data['summary']


def run(selected, cache, municipalities, districts, stage_fn=stage):
    """Process all available cached PBFs; isolate failures without hiding them."""
    cache.mkdir(parents=True, exist_ok=True)
    out = cache / 'national-report.json'
    if out.exists():
        previous = json.loads(out.read_text(encoding='utf-8'))
        report = {}
        for slug in DISTRICTS:
            record = previous.get(slug)
            if isinstance(record, dict):
                report[slug] = (record if record.get('status') in
                                ('processed', 'failed') else
                                {**record, 'status': 'legacy_unverified'})
    else:
        report = {}
    for slug in selected:
        pbf = cache / f'{slug}-latest.osm.pbf'
        try:
            # No-network guarantee even if stage() could download missing data.
            if not pbf.is_file() or pbf.stat().st_size <= 1_000_000:
                raise FileNotFoundError(f'PBF not cached (offline): {pbf.name}')
            _, counters = stage_fn(slug, cache, False, municipalities, districts)
            report[slug] = {'status': 'processed', **counters}
        except (OSError, ValueError, RuntimeError) as exc:
            report[slug] = {'status': 'failed', 'error': type(exc).__name__,
                            'detail': str(exc)[:200]}
        stats = checkpoint(report, selected, out)
        print(f'{slug}: {report[slug]["status"]}; processed '
              f'{stats["districts_processed"]}/{len(DISTRICTS)}', flush=True)
    return 1 if any(report[slug]['status'] == 'failed' for slug in selected) else 0


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--district', choices=['all', *DISTRICTS], default='all')
    parser.add_argument('--cache-dir', type=Path, default=Path('.cache/jynlo-venues'))
    parser.add_argument('--municipalities-geojson', type=Path, required=True)
    parser.add_argument('--districts-geojson', type=Path, required=True)
    args = parser.parse_args()
    municipalities = MunicipalityLookup(args.municipalities_geojson)
    districts = DistrictLookup(args.districts_geojson)
    selected = list(DISTRICTS) if args.district == 'all' else [args.district]
    raise SystemExit(run(selected, args.cache_dir, municipalities, districts))


if __name__ == '__main__':
    main()

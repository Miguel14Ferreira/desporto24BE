#!/usr/bin/env python3
"""Resumable, read-only OSM district extraction using official CAOP2025 polygons.

It downloads only missing district PBFs and stages local JSONL. This tool
never calls the Jynlo API, never modifies the production DB and has no token.
"""
from __future__ import annotations

import argparse
import hashlib
import json
from pathlib import Path

from import_osm_venues_national import (
    DISTRICTS, DistrictLookup, MunicipalityLookup, stage,
)
from national_venue_batch_runner import checkpoint


def valid_cached_output(cache: Path, slug: str, previous: dict) -> bool:
    """Skip only complete local results whose content still matches the checkpoint."""
    if previous.get("status") != "processed":
        return False
    for suffix, digest_key in (("-venues.jsonl", "sha256"),
                               ("-review.jsonl", "review_sha256")):
        path = cache / f"{slug}{suffix}"
        checksum = previous.get(digest_key)
        if not path.is_file() or not checksum:
            return False
        if hashlib.sha256(path.read_bytes()).hexdigest() != checksum:
            return False
    pbf = cache / f"{slug}-latest.osm.pbf"
    return pbf.is_file() and pbf.stat().st_size > 1_000_000


def stage_districts(selected, cache, municipalities, districts, *, refresh=False,
                    stage_fn=stage):
    cache.mkdir(parents=True, exist_ok=True)
    path = cache / "national-report.json"
    old = json.loads(path.read_text(encoding="utf-8")) if path.exists() else {}
    if not isinstance(old, dict):
        raise ValueError("The previous report must be a JSON object")
    # Never count old snapshots without a validated status as processed.
    report = {}
    for slug in DISTRICTS:
        entry = old.get(slug)
        if isinstance(entry, dict):
            report[slug] = (entry if entry.get("status") in ("processed", "failed")
                            else {**entry, "status": "legacy_unverified"})
    failed = []
    for slug in selected:
        previous = report.get(slug, {})
        if not refresh and valid_cached_output(cache, slug, previous):
            print(f"[{slug}] Already processed and checksums match; skipping.", flush=True)
            checkpoint(report, selected, path)
            continue
        try:
            # Unlike the offline-only runner, stage() downloads a missing PBF
            # from OpenStreetMap FR and reuses a cached snapshot by default.
            _, counters = stage_fn(slug, cache, refresh, municipalities, districts)
            report[slug] = {"status": "processed", **counters}
        except (OSError, RuntimeError, ValueError) as exc:
            failed.append(slug)
            report[slug] = {"status": "failed",
                            "error": type(exc).__name__,
                            "detail": str(exc)[:200]}
        state = checkpoint(report, selected, path)
        print(f"[{slug}] {report[slug]['status']}; CAOP-checked districts: "
              f"{state['districts_processed']}/{len(DISTRICTS)}", flush=True)
    return 1 if failed else 0


def main(argv=None):
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--district", choices=["all", *DISTRICTS], default="all")
    parser.add_argument("--cache-dir", type=Path, default=Path(".cache/jynlo-venues"))
    parser.add_argument("--municipalities-geojson", type=Path, required=True)
    parser.add_argument("--districts-geojson", type=Path, required=True)
    parser.add_argument("--refresh", action="store_true",
                        help="Force redownload and restaging (not recommended)")
    args = parser.parse_args(argv)
    municipalities = MunicipalityLookup(args.municipalities_geojson)
    districts = DistrictLookup(args.districts_geojson)
    selected = list(DISTRICTS) if args.district == "all" else [args.district]
    return stage_districts(selected, args.cache_dir, municipalities, districts,
                           refresh=args.refresh)


if __name__ == "__main__":
    raise SystemExit(main())

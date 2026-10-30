#!/usr/bin/env python3
"""Three official-source candidate proposals + PostgreSQL read-only duplicate preflight.

This tool never marks a venue approved and never writes to a database.
Original data must be precisely the 3514-candidate SHA-pinned national audit.
"""
import argparse
import gzip
import hashlib
import json
import math
from pathlib import Path
import re
from urllib.parse import urlparse

SOURCE_SHA256 = "e218744531f63bc94c537467d51fb77214e60132b20178877a13b52214a0701a"
LIVE_REFS = frozenset({
    "OSM:WAY:429474882", "OSM:WAY:429475622", "OSM:WAY:429476238",
    "OSM:WAY:1348549266", "OSM:WAY:1275651401", "OSM:WAY:157099522",
    "OSM:WAY:305711614", "OSM:WAY:429477908",
    "OSM:WAY:426295154", "OSM:WAY:426296888",
})
PILOT_REFS = frozenset({
    "OSM:WAY:145943204", "OSM:WAY:122685516", "OSM:WAY:1056855283",
})
OFFICIAL_DOMAINS = {"cm-cantanhede.pt", "www.cm-cantanhede.pt", "www.cm-coimbra.pt"}
REQUIRED = ("sourceReference", "name", "city", "district", "address",
            "sports", "venueType", "environmentType", "sourceUrl", "lastReviewed", "notes")


def sql_text(value):
    return "'" + str(value).replace("'", "''") + "'"


def load_candidates(path, *, count=3514, digest=SOURCE_SHA256):
    raw = Path(path).read_bytes()
    if digest is not None and hashlib.sha256(raw).hexdigest() != digest:
        raise ValueError("National source SHA-256 mismatch; refusing unchecked snapshot")
    payload = gzip.decompress(raw).decode("utf-8") if str(path).endswith(".gz") else raw.decode("utf-8")
    lines = [json.loads(line) for line in payload.splitlines() if line.strip()]
    if len(lines) != count:
        raise ValueError(f"Expected {count} OSM records; got {len(lines)}")
    index = {}
    for item in lines:
        ref = item["sourceReference"]
        if ref in index:
            raise ValueError("Repeated OSM reference: " + ref)
        index[ref] = item
    return index


def build_pilot(osm, official, *, exact_three=True):
    if not isinstance(official, list):
        raise ValueError("Official evidence must be a JSON array")
    if exact_three and len(official) != 3:
        raise ValueError("The pilot must have exactly three official records")
    proposals, refs = [], set()
    for rec in official:
        if not isinstance(rec, dict) or any(not rec.get(key) for key in REQUIRED):
            raise ValueError("Incomplete official evidence")
        ref = rec["sourceReference"]
        if ref in refs or ref in LIVE_REFS or (exact_three and ref not in PILOT_REFS):
            raise ValueError("Repeated/blocked/unexpected OSM reference: " + ref)
        refs.add(ref)
        if ref not in osm:
            raise ValueError("Missing OSM candidate from checked source: " + ref)
        old = osm[ref]
        if old["importGate"] != "SOURCE_REQUIRES_INDEPENDENT_REVIEW":
            raise ValueError("Existing or suspected duplicate cannot be in this pilot")
        if old["originalLiveHeuristic"] != "UNMATCHED_REQUIRES_REVIEW":
            raise ValueError("Public LIVE heuristic does not allow this pilot candidate")
        if (old["name"], old["city"], old["district"]) != (
                rec["name"], rec["city"], rec["district"]):
            raise ValueError("OSM venue identity disagrees with official proposal: " + ref)
        url = urlparse(rec["sourceUrl"])
        if url.scheme != "https" or url.hostname not in OFFICIAL_DOMAINS or url.username:
            raise ValueError("Official venue proof must be on a whitelisted municipal domain")
        if "por confirmar" in rec["address"].casefold():
            raise ValueError("Invalid official address")
        if not re.fullmatch(r"20[0-9]{2}-[0-9]{2}-[0-9]{2}", rec["lastReviewed"]):
            raise ValueError("Expected ISO date of evidence review")
        lat, lon = old["latitude"], old["longitude"]
        if not all(isinstance(v, (int, float)) and not isinstance(v, bool)
                   and math.isfinite(v) for v in (lat, lon)):
            raise ValueError("Invalid OSM coordinates")
        if not (36.8 <= lat <= 42.3 and -9.6 <= lon <= -6):
            raise ValueError("Coordinates outside mainland Portugal")
        if (len(rec["address"]) > 255 or len(rec["name"]) > 140
                or len(rec["sports"]) > 255 or len(rec["venueType"]) > 80):
            raise ValueError("Venue exceeds DB column limit")
        if rec["environmentType"] not in ("INDOOR", "OUTDOOR", "BOTH"):
            raise ValueError("Invalid venue environment")
        if len(rec["notes"]) < 35 or len(rec["notes"]) > 1000:
            raise ValueError("Verification notes missing")
        proposals.append({
            "sourceReference": ref,
            "name": rec["name"], "city": rec["city"], "district": rec["district"],
            "address": rec["address"], "sports": rec["sports"],
            "venueType": rec["venueType"], "environmentType": rec["environmentType"],
            "latitude": lat, "longitude": lon,
            "informationSource": "OPENSTREETMAP",
            "evidenceUrl": rec["sourceUrl"], "evidenceReviewedOn": rec["lastReviewed"],
            "verificationNotes": rec["notes"],
            "accessForUserOrganizedSessionsConfirmed": False,
            "independentFinalApproval": False,
            "approved": False,
            "publicationStatus": "DRAFT_PREFLIGHT_REQUIRED",
        })
    if exact_three and refs != PILOT_REFS:
        raise ValueError("One or more expected pilot references is absent")
    return sorted(proposals, key=lambda p: p["sourceReference"])


def render_preflight(proposals):
    if not proposals or any(row["approved"] is not False for row in proposals):
        raise ValueError("Preflight accepts only unapproved draft proposals")
    values = ",\n    ".join(
        "(" + ", ".join([sql_text(p["sourceReference"]), sql_text(p["name"]),
                       sql_text(p["city"]), f'{p["latitude"]:.7f}',
                       f'{p["longitude"]:.7f}']) + ")"
        for p in proposals
    )
    return f"""-- JYNLO: read-only pilot preflight: {len(proposals)} OFFICIAL-SOURCE PROPOSALS
-- Shows exact OSM ref, same normalized name/city and <=100m potential duplicates.
-- This is NOT permission to publish or proof of public access.
BEGIN TRANSACTION READ ONLY;
SET LOCAL statement_timeout = '30000ms';
SELECT COUNT(*) AS existing_venue_rows FROM desporto24.venue;
WITH proposed (source_reference, name, city, latitude, longitude) AS (
    VALUES
    {values}
), detected AS (
    SELECT p.source_reference, p.name, p.city,
           v.id AS possible_existing_id, v.name AS possible_existing_name,
           CASE
             WHEN v.information_source = 'OPENSTREETMAP'
                  AND v.source_reference = p.source_reference THEN 'EXACT_OSM_REFERENCE'
             WHEN LOWER(BTRIM(v.name)) = LOWER(BTRIM(p.name))
                  AND LOWER(BTRIM(v.city)) = LOWER(BTRIM(p.city)) THEN 'SAME_NAME_AND_CITY'
             ELSE 'WITHIN_100_METRES'
           END AS reason
    FROM proposed p
    JOIN desporto24.venue v
      ON (v.information_source = 'OPENSTREETMAP'
          AND v.source_reference = p.source_reference)
      OR (LOWER(BTRIM(v.name)) = LOWER(BTRIM(p.name))
          AND LOWER(BTRIM(v.city)) = LOWER(BTRIM(p.city)))
      OR (v.latitude IS NOT NULL AND v.longitude IS NOT NULL
          AND 111320.0 * SQRT(
              POWER(v.latitude - p.latitude, 2)
              + POWER((v.longitude - p.longitude)
                      * COS(RADIANS(p.latitude)), 2)
          ) <= 100)
)
SELECT p.source_reference, p.name, p.city,
       COUNT(d.possible_existing_id) AS possible_match_count,
       STRING_AGG(
          d.possible_existing_id::text || ': ' || d.possible_existing_name
          || ' [' || d.reason || ']', ' | '
          ORDER BY d.possible_existing_id
       ) AS possible_existing_venues
FROM proposed p
LEFT JOIN detected d USING (source_reference, name, city)
GROUP BY p.source_reference, p.name, p.city
ORDER BY p.source_reference;
ROLLBACK;
"""


def build_outputs(osm_file, manifest_file, directory):
    osm = load_candidates(osm_file)
    manifest = json.loads(Path(manifest_file).read_text(encoding="utf-8"))
    pilot = build_pilot(osm, manifest)
    directory.mkdir(parents=True, exist_ok=True)
    with (directory / "pilot-proposals-unapproved.jsonl").open("w", encoding="utf-8") as output:
        for item in pilot:
            output.write(json.dumps(item, ensure_ascii=False) + "\n")
    (directory / "pilot-readonly-preflight.sql").write_text(
        render_preflight(pilot), encoding="utf-8")
    summary = {
        "national_candidates": len(osm),
        "pilot_references": [p["sourceReference"] for p in pilot],
        "proposals": len(pilot),
        "approved_for_import": 0,
        "verified_access_for_external_sessions": 0,
        "liquibase_included_in_master": False,
        "database_writes": False,
        "source_archive_sha256": SOURCE_SHA256,
        "next_gate": "Run READ-ONLY SQL; independent operator/access verification; explicit per-venue approval",
    }
    (directory / "pilot-summary.json").write_text(
        json.dumps(summary, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")
    return summary


def main():
    p = argparse.ArgumentParser(description=__doc__)
    p.add_argument("--national-full-details", required=True, type=Path)
    p.add_argument("--municipal-evidence", required=True, type=Path)
    p.add_argument("--output-dir", required=True, type=Path)
    args = p.parse_args()
    print(json.dumps(build_outputs(args.national_full_details, args.municipal_evidence,
                                   args.output_dir), ensure_ascii=False, indent=2))


if __name__ == "__main__":
    main()

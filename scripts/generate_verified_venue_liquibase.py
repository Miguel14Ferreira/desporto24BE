#!/usr/bin/env python3
"""Generate an opt-in Liquibase SQL seed from *human-approved* OSM venues.

No network, no database access and no production write. This deliberately refuses
the raw importer's -review.jsonl and -venues.jsonl without explicit approvals.
"""
from __future__ import annotations

import argparse
import hashlib
import json
import math
from pathlib import Path
import re
import unicodedata
from urllib.parse import urlsplit

DISTRICTS = {
    "Aveiro", "Beja", "Braga", "Bragança", "Castelo Branco", "Coimbra",
    "Évora", "Faro", "Guarda", "Leiria", "Lisboa", "Portalegre",
    "Porto", "Santarém", "Setúbal", "Viana do Castelo", "Vila Real", "Viseu",
}
OSM_REF = re.compile(r"OSM:(NODE|WAY|RELATION):[1-9][0-9]*\Z")
MIGRATION_ID = re.compile(r"[a-z][a-z0-9-]{3,60}\Z")
ENVIRONMENTS = {"INDOOR", "OUTDOOR", "BOTH"}
MAX_ROWS = 500

def literal(value):
    """Escape SQL text without ever accepting user-provided SQL fragments."""
    if value is None:
        return "NULL"
    return "'" + str(value).replace("'", "''") + "'"

def normalized_name(value):
    return "".join(c for c in unicodedata.normalize("NFD", value.casefold())
                   if c.isalnum() and unicodedata.category(c) != "Mn")

def distance_km(a, b):
    phi1, phi2 = math.radians(a["latitude"]), math.radians(b["latitude"])
    dp = phi2 - phi1
    dl = math.radians(b["longitude"] - a["longitude"])
    v = math.sin(dp / 2) ** 2 + math.cos(phi1) * math.cos(phi2) * math.sin(dl / 2) ** 2
    return 6371.0 * 2 * math.asin(min(1.0, math.sqrt(v)))

def required_text(item, key, limit):
    value = item.get(key)
    if not isinstance(value, str) or not value.strip() or len(value.strip()) > limit:
        raise ValueError(f"{key} must be non-empty text (max {limit})")
    if any(ord(c) < 32 for c in value):
        raise ValueError(f"{key} contains a control character")
    return value.strip()

def validate(item):
    if not isinstance(item, dict):
        raise ValueError("Each JSONL line must be a JSON object")
    if item.get("approved") is not True:
        raise ValueError("Each venue needs explicit approved=true after human review")
    required_text(item, "approvedBy", 100)
    notes = required_text(item, "verificationNotes", 1000)
    if len(notes) < 12:
        raise ValueError("verificationNotes must explain the independent review")
    source = required_text(item, "sourceReference", 255)
    if not OSM_REF.fullmatch(source):
        raise ValueError(f"Invalid OSM source reference: {source}")
    name = required_text(item, "name", 140)
    if re.search(r"\(OSM\s*#|por confirmar|sem nome", name, re.I):
        raise ValueError(f"Unverified/generated venue name: {name}")
    city = required_text(item, "city", 120)
    district = required_text(item, "district", 120)
    if district not in DISTRICTS:
        raise ValueError(f"Unknown mainland district: {district}")
    # GPS-only is a real, supported LIVE schema state (Oct 21 migration), not
    # permission to smuggle OSM placeholder addresses into public data.
    address = item.get("address")
    if address is None:
        if "address" not in item or item.get("postalAddressStatus") != "UNKNOWN_GPS_ONLY":
            raise ValueError("GPS-only location requires explicit null address and UNKNOWN_GPS_ONLY status")
        if item.get("gpsVerified") is not True:
            raise ValueError("GPS-only location requires independent GPS verification")
        gps_notes = required_text(item, "gpsVerificationNotes", 1000)
        if len(gps_notes) < 40:
            raise ValueError("GPS-only location requires substantial independent verification notes")
        evidence = required_text(item, "gpsEvidenceUrl", 500)
        url = urlsplit(evidence)
        host = (url.hostname or "").lower()
        if (url.scheme != "https" or not host or url.username or url.password
                or host in {"openstreetmap.org", "www.openstreetmap.org", "osm.org",
                            "www.osm.org", "localhost"}
                or host.endswith(".openstreetmap.org")):
            raise ValueError("GPS-only verification needs an independent HTTPS evidence URL, not OSM")
    else:
        address = required_text(item, "address", 255)
        if "por confirmar" in address.casefold():
            raise ValueError("Address still marked as unverified")
        if item.get("postalAddressStatus") == "UNKNOWN_GPS_ONLY":
            raise ValueError("GPS-only status conflicts with supplied postal address")
    website = item.get("website")
    if website is not None:
        website = required_text(item, "website", 255)
        try:
            parsed_website = urlsplit(website)
            website_host = (parsed_website.hostname or "").lower()
            website_port = parsed_website.port
        except ValueError as error:
            raise ValueError("Invalid venue website") from error
        if (parsed_website.scheme != "https" or not website_host
                or parsed_website.username or parsed_website.password
                or website_port not in (None, 443)
                or website_host in {"openstreetmap.org", "www.openstreetmap.org",
                                    "localhost", "osm.org", "www.osm.org"}
                or website_host.endswith(".openstreetmap.org")):
            raise ValueError("Venue website requires an independent HTTPS operator page")
    sports = required_text(item, "sports", 255)
    venue_type = required_text(item, "venueType", 80)
    environment = item.get("environmentType")
    if environment is not None and environment not in ENVIRONMENTS:
        raise ValueError("Invalid environmentType")
    for key, low, high in (("latitude", 36.8, 42.3), ("longitude", -9.6, -6.0)):
        value = item.get(key)
        if isinstance(value, bool) or not isinstance(value, (int, float)):
            raise ValueError(f"{key} must be a number")
        if not math.isfinite(value) or not low <= value <= high:
            raise ValueError(f"{key} must be in mainland Portugal")
    return dict(sourceReference=source, name=name, city=city, district=district,
                address=address, website=website, sports=sports, venueType=venue_type,
                environmentType=environment,
                latitude=round(float(item["latitude"]), 7),
                longitude=round(float(item["longitude"]), 7))

def load_approved(path):
    rows = []
    seen_sources = set()
    with Path(path).open(encoding="utf-8") as handle:
        for number, line in enumerate(handle, 1):
            if not line.strip():
                continue
            try:
                row = validate(json.loads(line))
                if row["sourceReference"] in seen_sources:
                    raise ValueError("Duplicate source reference")
                if any(normalized_name(prior["name"]) == normalized_name(row["name"])
                       and distance_km(prior, row) <= 0.030 for prior in rows):
                    raise ValueError("Similar named venue within 30 metres; manual review required")
            except (ValueError, json.JSONDecodeError) as ex:
                raise ValueError(f"{path}:{number}: {ex}") from ex
            seen_sources.add(row["sourceReference"])
            rows.append(row)
            if len(rows) > MAX_ROWS:
                raise ValueError("Too many venues for one migration; split into batches of 500")
    if not rows:
        raise ValueError("No approved venues to import; refusing empty migration")
    return sorted(rows, key=lambda row: (row["district"], row["sourceReference"]))

def render_sql(rows):
    header = ("-- Generated from individually approved OSM venues. Review the SQL in a PR.\n"
              "-- Explicit NULL postal address means independently verified GPS-only; never a fake street.\n"
              "-- Idempotent inserts: never update, replace or delete existing venue records.\n"
              "-- Does not prove an OSM facility is available for public booking.\n\n")
    statements = []
    for row in rows:
        values = [
            row["name"], row["city"], row["district"], row["address"], row["sports"],
            row["venueType"], row["environmentType"], row["latitude"], row["longitude"],
            row["sourceReference"], row["website"]
        ]
        vals = ", ".join(literal(x) for x in values[:7])
        lat = f'{values[7]:.7f}'
        lon = f'{values[8]:.7f}'
        ref = literal(values[9])
        website = literal(values[10])
        indoors = "TRUE" if row["environmentType"] == "INDOOR" else "FALSE"
        # Individual INSERTs mean later statements can observe previously inserted
        # rows in the same Liquibase transaction, unlike one bulk INSERT snapshot.
        statements.append(f"""-- {row['sourceReference']} / {row['district']}
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference, website,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT {vals}, {lat}, {lon}, 'OPENSTREETMAP', {ref},
       {website}, {indoors}, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue existing
  WHERE (existing.information_source = 'OPENSTREETMAP'
         AND existing.source_reference = {ref})
     OR (LOWER(BTRIM(existing.name)) = LOWER(BTRIM({literal(row['name'])}))
         AND ((LOWER(BTRIM(existing.city)) = LOWER(BTRIM({literal(row['city'])}))
               AND (existing.latitude IS NULL OR existing.longitude IS NULL))
              OR (existing.latitude BETWEEN {row['latitude'] - 0.00030:.7f} AND {row['latitude'] + 0.00030:.7f}
                  AND existing.longitude BETWEEN {row['longitude'] - 0.00040:.7f} AND {row['longitude'] + 0.00040:.7f})))
)
ON CONFLICT DO NOTHING;""")
    return header + "\n\n".join(statements) + "\n"

def render_changelog(filename, changeset_id, digest):
    return f"""databaseChangeLog:
  # Import is OPT-IN. This changelog must NOT be included by db.changelog-master.yaml
  # until the generated SQL and its approval evidence pass code review.
  - changeSet:
      id: {changeset_id}-{digest[:12]}
      author: jynlo-verified-venues
      dbms: postgresql
      runInTransaction: true
      preConditions:
        onFail: HALT
        tableExists:
          schemaName: desporto24
          tableName: venue
      changes:
        - sqlFile:
            path: {filename}
            relativeToChangelogFile: true
            encoding: UTF-8
            splitStatements: true
            endDelimiter: ;
"""

def generate(input_path, output_dir, migration_id):
    if not MIGRATION_ID.fullmatch(migration_id):
        raise ValueError("Migration id must be a lowercase slug, 4 to 61 characters")
    rows = load_approved(input_path)
    sql = render_sql(rows)
    digest = hashlib.sha256(sql.encode("utf-8")).hexdigest()
    directory = Path(output_dir)
    directory.mkdir(parents=True, exist_ok=True)
    filename = f"{migration_id}-{digest[:12]}.sql"
    sql_path = directory / filename
    yaml_path = directory / f"{migration_id}-{digest[:12]}.yaml"
    if sql_path.exists() or yaml_path.exists():
        raise ValueError("Migration already exists: review and retain it; use a new id for new data")
    sql_path.write_text(sql, encoding="utf-8")
    yaml_path.write_text(render_changelog(filename, migration_id, digest), encoding="utf-8")
    return sql_path, yaml_path, len(rows)

def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--approved-jsonl", required=True, type=Path)
    parser.add_argument("--output-dir", required=True, type=Path)
    parser.add_argument("--migration-id", required=True)
    args = parser.parse_args()
    try:
        sql, yaml, count = generate(args.approved_jsonl, args.output_dir,
                                    args.migration_id)
    except ValueError as e:
        parser.error(str(e))
    print(f"Generated {count} approved venues in {sql} and {yaml}")
    print("NOT included in master changelog; no database changes performed.")

if __name__ == "__main__":
    main()

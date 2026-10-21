#!/usr/bin/env python3
"""Build nationwide, map-sourced *unverified* OSM venue SQL in one offline pass.

NOT the human-approved municipal import path. The input is the pinned OSM
snapshot, publication requires an explicit separate release decision and a
current production backup. This script does no DB/network writes.
"""
import argparse
from collections import Counter, defaultdict
import gzip
import hashlib
import json
import math
from pathlib import Path
import re
import unicodedata
from audit_national_stage import metres
from generate_verified_venue_liquibase import DISTRICTS, OSM_REF, literal

SOURCE_SHA = "e218744531f63bc94c537467d51fb77214e60132b20178877a13b52214a0701a"
ROOT = Path(__file__).resolve().parent.parent
SOURCE = ROOT / "docs/venue-imports/datasets/national-3514-reviewed-20261021.jsonl.gz"
ALLOWED_GATES = {"ADDRESS_REVIEW", "SOURCE_REQUIRES_INDEPENDENT_REVIEW"}
REVIEW_ONLY_GATES = {"CONFIRMED_EXISTING_LIVE_EXCLUDE", "POSSIBLE_DUPLICATE_REVIEW"}
SUSPICIOUS = ("encerrad", "desativad", "desactivad", "abandonad", "demolid",
              "privad", "proibido", "militar", "quartel", "hotel", "resort",
              "escola ", "escolar", "ruina", "ruína", "antigo ", "antiga ",
              "temporariamente", "construction", "inativo")
UNVERIFIED = "Unverified map-sourced OSM venue. No assumption of access or booking."
ENVIRONMENTS = {"INDOOR", "OUTDOOR", "BOTH", None}
SPORT_PATTERN = re.compile(r'^\s*"([^"]+)"\s*,?$', re.M)


def norm(text):
    text = unicodedata.normalize("NFKD", text.casefold())
    return "".join(ch for ch in text if ch.isalnum() and not unicodedata.combining(ch))


def sport_catalog():
    java = (ROOT / "src/main/java/com/example/desporto24/constant/SportsCatalog.java").read_text()
    body = java.split("Set.of(", 1)[1].split(");", 1)[0]
    return set(SPORT_PATTERN.findall(body))


def read_snapshot(source=SOURCE):
    raw = Path(source).read_bytes()
    if hashlib.sha256(raw).hexdigest() != SOURCE_SHA:
        raise ValueError("Refusing unpinned source snapshot")
    rows = [json.loads(line) for line in gzip.decompress(raw).decode("utf-8").splitlines()
            if line.strip()]
    if len(rows) != 3514 or len({x.get("sourceReference") for x in rows}) != 3514:
        raise ValueError("National 18-district snapshot must have 3514 unique references")
    if len({x.get("district") for x in rows}) != 18:
        raise ValueError("Input does not cover all 18 districts")
    return rows


def classify(row, sports):
    if row.get("approvedForImport") is not False:
        raise ValueError("Unexpected approval flag in pinned OSM archive")
    gate = row.get("importGate")
    if gate in REVIEW_ONLY_GATES:
        return "existing_or_possible_live_duplicate"
    if gate not in ALLOWED_GATES or row.get("originalLiveHeuristic") != "UNMATCHED_REQUIRES_REVIEW":
        return "other_unverified_source_gate"
    ref, name, city, district = (row.get(k) for k in
                                 ("sourceReference", "name", "city", "district"))
    if not isinstance(ref, str) or not OSM_REF.fullmatch(ref):
        return "invalid_osm_ref"
    if (not isinstance(name, str) or not name.strip() or len(name) > 140
            or "(OSM #" in name or "por confirmar" in name.casefold()
            or "sem nome" in name.casefold()):
        return "missing_real_venue_name"
    if any(term in name.casefold() for term in SUSPICIOUS):
        return "restricted_closed_or_school_review"
    if (not isinstance(city, str) or not city.strip() or len(city) > 120
            or district not in DISTRICTS):
        return "invalid_city_or_district"
    raw_sports = row.get("sports")
    disciplines = raw_sports.split(";") if isinstance(raw_sports, str) else []
    if not disciplines or any(value not in sports for value in disciplines):
        return "missing_or_unsupported_sport"
    if len(raw_sports) > 255:
        return "too_many_sports"
    kind = row.get("venueType")
    if not isinstance(kind, str) or not kind.strip() or len(kind) > 80:
        return "missing_venue_type"
    if row.get("environmentType") not in ENVIRONMENTS:
        return "invalid_environment"
    lat, lon = row.get("latitude"), row.get("longitude")
    if (any(isinstance(v, bool) or not isinstance(v, (int, float))
            or not math.isfinite(v) for v in (lat, lon))
            or not (36.8 <= lat <= 42.3 and -9.6 <= lon <= -6)):
        return "invalid_mainland_gps"
    return "map_sourced_unverified"


def nearby_duplicate(candidate, accepted):
    key = (norm(candidate["name"]), norm(candidate["city"]))
    for prior in accepted.get(key, []):
        if metres((candidate["latitude"], candidate["longitude"]),
                  (prior["latitude"], prior["longitude"])) <= 75:
            return prior["sourceReference"]
    return None


def partition(rows, *, sports=None):
    sports = sports or sport_catalog()
    reasons = Counter()
    buckets = defaultdict(list)
    for row in rows:
        status = classify(row, sports)
        reasons[status] += 1
        if status == "map_sourced_unverified":
            buckets[row["district"]].append(row)
    output = defaultdict(list)
    conflicts = []
    accepted = defaultdict(list)
    for district in sorted(DISTRICTS):
        for item in sorted(buckets[district], key=lambda r: (
                0 if r.get("importGate") == "SOURCE_REQUIRES_INDEPENDENT_REVIEW" else 1,
                norm(r["city"]), norm(r["name"]), r["sourceReference"])):
            prior = nearby_duplicate(item, accepted)
            if prior:
                reasons["intra_snapshot_possible_duplicate"] += 1
                conflicts.append({"ref": item["sourceReference"], "otherRef": prior,
                                  "district": district, "reason": "same name/city within 75m"})
                continue
            accepted[(norm(item["name"]), norm(item["city"]))].append(item)
            output[district].append(item)
    return output, reasons, conflicts


def statement(row):
    # NEVER promote the archived OSM postal placeholder to an address.
    # Even existing OSM postal text is unverified: store NULL and show GPS.
    lat, lon = (float(row[k]) for k in ("latitude", "longitude"))
    ref, name, city = (literal(row[k]) for k in ("sourceReference", "name", "city"))
    env = row.get("environmentType")
    indoors = "TRUE" if env == "INDOOR" else "FALSE"
    return f"""-- {row['sourceReference']} (map-sourced / access UNKNOWN)
INSERT INTO desporto24.venue
  (name, city, district, address, sports, venue_type, environment_type,
   latitude, longitude, information_source, source_reference,
   indoor, changing_rooms, parking, lighting, created_at)
SELECT {name}, {city}, {literal(row["district"])}, NULL,
       {literal(row["sports"])}, {literal(row["venueType"])}, {literal(env)},
       {lat:.7f}, {lon:.7f}, 'OPENSTREETMAP', {ref},
       {indoors}, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM desporto24.venue v
  WHERE (v.information_source='OPENSTREETMAP' AND v.source_reference={ref})
     OR (LOWER(BTRIM(v.city))=LOWER(BTRIM({city}))
         AND LOWER(BTRIM(v.name))=LOWER(BTRIM({name}))
         AND (v.latitude IS NULL OR v.longitude IS NULL
           OR (ABS(v.latitude-{lat:.7f}) <= 0.00070
               AND ABS(v.longitude-{lon:.7f}) <= 0.00100)))
)
ON CONFLICT DO NOTHING;"""


def generate(source, output):
    rows = read_snapshot(source)
    groups, counts, conflicts = partition(rows)
    out = Path(output)
    out.mkdir(parents=True, exist_ok=True)
    sql_dir = out / "sql"
    sql_dir.mkdir(exist_ok=True)
    manifest = {
        "sourceSha256": SOURCE_SHA, "sourceCount": 3514,
        "districtsInput": 18, "districtsOutput": len([g for g in groups.values() if g]),
        "releasePolicy": "OPT_IN_ONLY_NOT_INCLUDED_IN_LIQUIBASE_MASTER",
        "allInputRecordsPreviouslyApproved": False,
        "humanApprovalsFabricated": False,
        "mapSourcedAccessStatus": "UNVERIFIED",
        "source": "© OpenStreetMap contributors (ODbL); districts from DGT CAOP2025 (CC BY 4.0)",
        "noExistingRowsUpdatedOrDeleted": True,
        "addressPolicy": "NULL_GPS_ONLY_UNVERIFIED",
        "reasonCounts": dict(sorted(counts.items())),
        "excludedOrReviewRequired": sum(v for k,v in counts.items()
                  if k != "map_sourced_unverified"),
        "intraSnapshotPossibleDuplicateExamples": conflicts[:100],
        "districts": {},
    }
    for district in sorted(DISTRICTS):
        items = groups[district]
        name = "".join(c if c.isascii() and c.isalnum() else "-" for c in
                       unicodedata.normalize("NFKD",district).encode("ascii","ignore").decode()).strip("-").lower()
        name = re.sub("-+", "-", name)
        paths=[]
        for i in range(0, len(items), 200):
            part = items[i:i+200]
            filename=f"national-map-osm-{name}-{i//200+1:02d}.sql"
            sql="-- UNVERIFIED OSM map candidates. No proof of entry, public access or bookings.\n"
            sql+="-- OPT-IN only. PostgreSQL 16. Never manually mark as OFFICIAL.\n"
            sql+="\n\n".join(statement(row) for row in part)+"\n"
            (sql_dir/filename).write_text(sql, encoding="utf-8")
            paths.append({"file":filename,"rows":len(part),
                          "sha256":hashlib.sha256(sql.encode("utf-8")).hexdigest()})
        manifest["districts"][district]={"candidates":len(items),"sql":paths}
    manifest["totalMapSourcedCandidates"]=sum(x["candidates"] for x in manifest["districts"].values())
    manifest["totalReviewOrExcluded"]=3514-manifest["totalMapSourcedCandidates"]
    if manifest["totalMapSourcedCandidates"] < 100 or manifest["districtsOutput"] != 18:
        raise ValueError("National bulk queue unexpectedly incomplete; refusing SQL approval")
    (out/"national-plan.json").write_text(json.dumps(manifest,ensure_ascii=False,indent=2)+"\n",encoding="utf-8")
    return manifest


if __name__ == "__main__":
    p=argparse.ArgumentParser(description=__doc__)
    p.add_argument("--source",type=Path,default=SOURCE)
    p.add_argument("--out-dir",type=Path,required=True)
    a=p.parse_args()
    m=generate(a.source,a.out_dir)
    print(json.dumps({"input":m["sourceCount"],"districts":m["districtsOutput"],
                      "mapCandidates":m["totalMapSourcedCandidates"],
                      "excludedOrReview":m["totalReviewOrExcluded"],
                      "gates":m["reasonCounts"]},ensure_ascii=False,indent=2))

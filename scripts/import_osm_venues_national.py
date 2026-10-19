#!/usr/bin/env python3
"""National OSM sports-venue catalogue, safe by default.

Streams 18 district PBF extracts into JSONL, then optionally sends bounded batches
to the existing authenticated Jynlo API. No write occurs without --apply and a
separate confirmation. Requires: python -m pip install osmium
"""
from __future__ import annotations

import argparse
from collections import Counter
import hashlib
import json
import os
from pathlib import Path
import sys
import time
import urllib.error
import urllib.request

try:
    import osmium
except ImportError:
    osmium = None

BASE_URL = "https://download.openstreetmap.fr/extracts/europe/portugal"
DISTRICTS = {
    "aveiro": "Aveiro", "beja": "Beja", "braga": "Braga",
    "braganca": "Bragança", "castelo_branco": "Castelo Branco",
    "coimbra": "Coimbra", "evora": "Évora", "faro": "Faro",
    "guarda": "Guarda", "leiria": "Leiria", "lisbon": "Lisboa",
    "portalegre": "Portalegre", "porto": "Porto", "santarem": "Santarém",
    "setubal": "Setúbal", "viana_do_castelo": "Viana do Castelo",
    "vila_real": "Vila Real", "viseu": "Viseu",
}
LEISURE = {
    "sports_centre": "Complexo desportivo",
    "pitch": "Campo desportivo",
    "stadium": "Estádio",
    "track": "Pista desportiva",
    "swimming_pool": "Piscina",
    "fitness_centre": "Ginásio",
    "golf_course": "Campo de golfe",
    "horse_riding": "Centro hípico",
    "ice_rink": "Pista de gelo",
}
SPORTS = {
    "soccer": "Futebol", "football": "Futebol", "futsal": "Futsal",
    "basketball": "Basquetebol", "basketball_3x3": "Basquetebol 3x3",
    "tennis": "Ténis", "padel": "Padel", "volleyball": "Voleibol",
    "beachvolleyball": "Voleibol de Praia", "handball": "Andebol",
    "swimming": "Natação", "athletics": "Atletismo",
    "rugby": "Rugby", "rugby_union": "Rugby",
    "badminton": "Badminton", "table_tennis": "Ténis de mesa",
    "golf": "Golfe", "climbing": "Escalada", "bouldering": "Escalada",
    "skateboard": "Skate", "skating": "Patinagem",
    "gymnastics": "Ginástica", "gym": "Ginásio", "fitness": "Ginásio",
    "weightlifting": "Halterofilismo", "boxing": "Boxe",
    # The generic OSM 'martial_arts' tag is not a specific Chinese discipline.
    "karate": "Karaté", "judo": "Judo", "taekwondo": "Taekwondo",
    "squash": "Squash", "pickleball": "Pickleball",
    "cycling": "Ciclismo", "bmx": "BMX", "karting": "Karting",
    "rowing": "Remo", "canoe": "Canoagem", "kayak": "Canoagem",
    "surfing": "Surf", "surf": "Surf", "sailing": "Vela",
    "water_polo": "Polo Aquático", "field_hockey": "Hóquei em Campo",
    "hockey": "Hóquei", "ice_hockey": "Hóquei no Gelo",
    "baseball": "Basebol", "cricket": "Cricket",
    "american_football": "Futebol Americano",
    "archery": "Tiro com Arco", "shooting": "Tiro",
    "equestrian": "Atletismo",  # overwritten below with no inference
    "orienteering": "Orientação", "running": "Corrida",
    "dancing": "Dança Desportiva", "chess": "Xadrez",
    "petanque": "Petanca", "billiards": "Bilhar",
    "softball": "Softbol", "triathlon": "Triatlo",
}
SPORTS.pop("equestrian")  # Not part of the current allowed Jynlo sport catalogue.
INFER = {"swimming_pool": "Natação", "fitness_centre": "Ginásio", "golf_course": "Golfe"}
DENY_ACCESS = {"private", "no", "residents"}
DENY_STATUS = {"demolished", "abandoned", "construction", "proposed", "disused"}


def build_venue(tags, osm_type, osm_id, coords, district):
    """Pure converter. Preserve unknown information as unknown, never invent modalities."""
    if (tags.get("access") or "").lower() in DENY_ACCESS:
        return None, "restricted_access"
    if any(tags.get(k) for k in DENY_STATUS):
        return None, "inactive"
    leisure = (tags.get("leisure") or "").strip().lower()
    building = (tags.get("building") or "").strip().lower()
    kind = LEISURE.get(leisure)
    if kind is None and building == "sports_hall":
        kind = "Pavilhão desportivo"
    if not kind:
        return None, "non_sport_venue"
    if not coords:
        return None, "missing_coordinates"
    lat, lon = coords
    if not (36.8 <= lat <= 42.3 and -9.6 <= lon <= -6.0):
        return None, "outside_mainland"
    name = (tags.get("name:pt") or tags.get("name") or "").strip()
    missing_name = not name
    if not name:
        name = f"{kind} (OSM #{osm_id})"
    city = next((str(tags[k]).strip() for k in (
        "addr:city", "addr:town", "addr:village", "addr:municipality",
        "is_in:city", "addr:suburb") if tags.get(k) and str(tags[k]).strip()), None)
    missing_city = city is None
    # District placeholder is honest at source level, and remains searchable.
    city = city or district
    street = " ".join(str(tags.get(k) or "").strip() for k in
                      ("addr:street", "addr:housenumber")).strip()
    postal = (tags.get("addr:postcode") or "").strip()
    address = ", ".join(v for v in (street, postal, city) if v) if street else (
        f"Localização no mapa: {lat:.5f}, {lon:.5f} (morada por confirmar)")
    raw = (tags.get("sport") or "").replace(",", ";")
    sports = list(dict.fromkeys(SPORTS[key.strip().lower()]
              for key in raw.split(";") if key.strip().lower() in SPORTS))
    if not sports and leisure in INFER:
        sports = [INFER[leisure]]
    indoors = (tags.get("indoor") or "").lower()
    env = "INDOOR" if indoors in ("yes", "true", "1") or building == "sports_hall" else (
          "OUTDOOR" if indoors in ("no", "false", "0") else None)
    result = {
        "sourceReference": f"OSM:{osm_type.upper()}:{osm_id}",
        "name": name[:140], "city": city[:120], "district": district,
        "address": address[:255], "sports": ";".join(sports) or None,
        "venueType": kind[:80], "environmentType": env,
        "latitude": round(lat, 7), "longitude": round(lon, 7)
    }
    flags = []
    if missing_name:
        flags.append("name_to_verify")
    if missing_city:
        flags.append("municipality_to_verify")
    if not street:
        flags.append("address_to_verify")
    if not sports:
        flags.append("sports_to_verify")
    return result, ",".join(flags) or "ok"


def _inside_ring(lon, lat, ring):
    """Ray casting on GeoJSON lon/lat coordinates."""
    inside = False
    j = len(ring) - 1
    for i in range(len(ring)):
        xi, yi = ring[i][:2]
        xj, yj = ring[j][:2]
        if ((yi > lat) != (yj > lat)):
            intersection_x = (xj - xi) * (lat - yi) / (yj - yi) + xi
            if lon < intersection_x:
                inside = not inside
        j = i
    return inside


def _contains_polygon(lon, lat, polygon):
    return bool(polygon) and _inside_ring(lon, lat, polygon[0]) and not any(
        _inside_ring(lon, lat, hole) for hole in polygon[1:])


class PolygonLookup:
    """Official WGS84 GeoJSON lookup; polygons are tested including interior holes."""
    LABEL_FIELDS = ()

    def __init__(self, path):
        raw = json.loads(Path(path).read_text(encoding="utf-8"))
        if raw.get("type") != "FeatureCollection":
            raise ValueError("Expected a WGS84 GeoJSON FeatureCollection")
        self.municipalities = []
        for feature in raw.get("features", []):
            props = feature.get("properties") or {}
            name = next((str(props[k]).strip() for k in self.LABEL_FIELDS
                         if props.get(k)), None)
            geom = feature.get("geometry") or {}
            if not name or geom.get("type") not in {"Polygon", "MultiPolygon"}:
                continue
            polygons = ([geom["coordinates"]] if geom["type"] == "Polygon"
                        else geom["coordinates"])
            for poly in polygons:
                if not poly or not poly[0]:
                    continue
                xs = [pt[0] for pt in poly[0]]
                ys = [pt[1] for pt in poly[0]]
                self.municipalities.append((min(xs), min(ys), max(xs), max(ys),
                                            name, poly))
        if not self.municipalities:
            raise ValueError("No named polygons in official boundary data")

    def find(self, lat, lon):
        for minx, miny, maxx, maxy, name, poly in self.municipalities:
            if minx <= lon <= maxx and miny <= lat <= maxy and _contains_polygon(lon, lat, poly):
                return name
        return None


class MunicipalityLookup(PolygonLookup):
    LABEL_FIELDS = ("municipio", "Municipio", "MUNICIPIO", "Município",
                    "concelho", "Concelho", "name", "nome", "NAME_2")


class DistrictLookup(PolygonLookup):
    LABEL_FIELDS = ("distrito", "Distrito", "DISTRITO", "distrito_ilha",
                    "DISTRITO_ILHA", "name", "nome", "NAME_1")

    def __init__(self, path):
        super().__init__(path)
        unexpected = {name for _, _, _, _, name, _ in self.municipalities
                      if name not in DISTRICTS.values()}
        if unexpected:
            raise ValueError(f"Unexpected CAOP district names: {sorted(unexpected)}")


def element_coordinates(obj, is_node):
    try:
        if is_node:
            loc = obj.location
            return (float(loc.lat), float(loc.lon)) if loc.valid() else None
        # pyosmium adds node locations in apply_file(..., locations=True)
        coords = [(float(n.location.lat), float(n.location.lon))
                  for n in obj.nodes if n.location.valid()]
        if not coords:
            return None
        return (sum(x for x, _ in coords) / len(coords),
                sum(y for _, y in coords) / len(coords))
    except (ValueError, RuntimeError, AttributeError):
        return None


def is_import_ready(quality):
    """Name, municipality and sport must be known. A venue with an explicit
    geo point may be published with address clearly marked 'to confirm'."""
    return set(quality.split(",")).issubset({"ok", "address_to_verify"})


class VenueHandler(osmium.SimpleHandler if osmium else object):
    def __init__(self, output, review, district, municipality_lookup=None, district_lookup=None):
        super().__init__()
        self.output = output
        self.review = review
        self.district = district
        self.municipality_lookup = municipality_lookup
        self.district_lookup = district_lookup
        self.counts = Counter()

    def record(self, obj, kind):
        tags = {str(tag.k): str(tag.v) for tag in obj.tags}
        if (tags.get("leisure") or "").strip().lower() not in LEISURE and tags.get("building") != "sports_hall":
            return
        self.counts["candidates"] += 1
        facility_type = (tags.get("leisure") or tags.get("building") or "unknown").strip().lower()
        self.counts["type_" + facility_type + "_candidates"] += 1
        coords = element_coordinates(obj, kind == "node")
        district = self.district_lookup.find(*coords) if (
            coords and self.district_lookup) else None
        if district and district != self.district:
            # OSM extract bounds can include a municipality from a neighbouring district.
            self.counts["skipped_other_district"] += 1
            return
        municipality = self.municipality_lookup.find(*coords) if (
            coords and self.municipality_lookup) else None
        if municipality:
            # CAOP geometry is more reliable than an OSM town/locality text tag.
            tags["addr:city"] = municipality
        venue, quality = build_venue(tags, kind, int(obj.id), coords, self.district)
        if venue:
            flags = [] if quality == "ok" else quality.split(",")
            if not district and "district_to_verify" not in flags:
                flags.append("district_to_verify")
            if not municipality and "municipality_to_verify" not in flags:
                flags.append("municipality_to_verify")
            quality = ",".join(flags) or "ok"
        if venue is None:
            self.counts["skipped_" + quality] += 1
            return
        self.counts["prepared"] += 1
        if is_import_ready(quality):
            self.output.write(json.dumps(venue, ensure_ascii=False) + "\n")
            self.counts["import_ready"] += 1
            self.counts["type_" + facility_type + "_ready"] += 1
        else:
            self.review.write(json.dumps({"venue": venue, "quality": quality},
                                         ensure_ascii=False) + "\n")
            self.counts["review_required"] += 1
            self.counts["type_" + facility_type + "_review"] += 1
        for flag in quality.split(","):
            self.counts[flag] += 1

    def node(self, obj):
        self.record(obj, "node")

    def way(self, obj):
        self.record(obj, "way")

    # OSM relations are deliberately not mapped to false centroids:
    # a later geometry-aware importer must handle them independently.


def download(slug, path, force=False):
    if path.is_file() and path.stat().st_size > 1_000_000 and not force:
        return "cached"
    url = f"{BASE_URL}/{slug}-latest.osm.pbf"
    req = urllib.request.Request(url, headers={"User-Agent": "Jynlo-national-venues/1.0 (offline import)"})
    temporary = path.with_suffix(".downloading")
    path.parent.mkdir(parents=True, exist_ok=True)
    for attempt in range(3):
        try:
            with urllib.request.urlopen(req, timeout=90) as response, temporary.open("wb") as out:
                size = 0
                while True:
                    block = response.read(256 * 1024)
                    if not block:
                        break
                    size += len(block)
                    if size > 130_000_000:
                        raise ValueError("Unexpected extract size; refusing download")
                    out.write(block)
            if size < 1_000_000:
                raise ValueError("Truncated district extract")
            temporary.replace(path)
            return "downloaded"
        except (OSError, ValueError, urllib.error.URLError):
            temporary.unlink(missing_ok=True)
            if attempt == 2:
                raise
            time.sleep(2 ** attempt)
    raise AssertionError("unreachable")


def stage(slug, workdir, refresh=False, municipality_lookup=None, district_lookup=None):
    if osmium is None:
        raise SystemExit("Install the parser first: python -m pip install osmium")
    pbf = workdir / f"{slug}-latest.osm.pbf"
    download(slug, pbf, force=refresh)
    target = workdir / f"{slug}-venues.jsonl"
    review_path = workdir / f"{slug}-review.jsonl"
    # Snapshot reproducibility: output and quarantine are always replaced together.
    tmp = target.with_suffix(".jsonl.tmp")
    review_tmp = review_path.with_suffix(".jsonl.tmp")
    try:
        with tmp.open("w", encoding="utf-8") as stream, \
                review_tmp.open("w", encoding="utf-8") as review:
            handler = VenueHandler(stream, review, DISTRICTS[slug], municipality_lookup, district_lookup)
            handler.apply_file(str(pbf), locations=True)
        tmp.replace(target)
        review_tmp.replace(review_path)
    finally:
        tmp.unlink(missing_ok=True)
        review_tmp.unlink(missing_ok=True)
    stats = dict(sorted(handler.counts.items()))
    stats["source_bytes"] = pbf.stat().st_size
    stats["source"] = f"{BASE_URL}/{slug}-latest.osm.pbf"
    stats["sha256"] = hashlib.sha256(target.read_bytes()).hexdigest()
    stats["review_sha256"] = hashlib.sha256(review_path.read_bytes()).hexdigest()
    return target, stats


def send_batch(api, token, venues):
    data = json.dumps(venues, ensure_ascii=False).encode("utf-8")
    req = urllib.request.Request(
        api.rstrip("/") + "/api/admin/venues/imports/openstreetmap",
        data=data,
        headers={"Authorization": "Bearer " + token, "Content-Type": "application/json",
                 "User-Agent": "Jynlo-national-venues/1.0"},
        method="POST"
    )
    for retry in range(3):
        try:
            with urllib.request.urlopen(req, timeout=90) as response:
                imported = json.load(response)
            if not isinstance(imported, list) or len(imported) != len(venues):
                raise RuntimeError("Unexpected API response count, manual verification required")
            return len(imported)
        except urllib.error.HTTPError as error:
            # 4xx errors must not be retried blindly; the operator must inspect the batch.
            if error.code < 500 or retry == 2:
                raise RuntimeError(f"API rejected import: HTTP {error.code}") from error
        except (OSError, TimeoutError):
            if retry == 2:
                raise
        time.sleep(2 ** retry)
    raise RuntimeError("Batch failed")


def apply_batches(api, token, path, batch_size, workdir):
    progress = workdir / f"{path.stem}.progress.json"
    digest = hashlib.sha256(path.read_bytes()).hexdigest()
    done = 0
    if progress.exists():
        state = json.loads(progress.read_text(encoding="utf-8"))
        if state["sha256"] != digest:
            raise SystemExit(f"Staged data changed; archive {progress} before a fresh import")
        done = int(state["submitted"])
    count = 0
    sent = done
    batch = []
    with path.open(encoding="utf-8") as f:
        for line in f:
            count += 1
            if count <= done:
                continue
            batch.append(json.loads(line))
            if len(batch) == batch_size:
                send_batch(api, token, batch)
                sent += len(batch)
                progress.write_text(json.dumps({"sha256": digest, "submitted": sent}), encoding="utf-8")
                batch.clear()
        if batch:
            send_batch(api, token, batch)
            sent += len(batch)
            progress.write_text(json.dumps({"sha256": digest, "submitted": sent}), encoding="utf-8")
    return sent


def main(argv=None):
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--district", choices=["all", *DISTRICTS], default="all")
    parser.add_argument("--cache-dir", type=Path, default=Path(".cache/jynlo-venues"))
    parser.add_argument("--municipalities-geojson", type=Path,
                        help="Official CAOP2025 municipality polygons in EPSG:4326 GeoJSON")
    parser.add_argument("--districts-geojson", type=Path,
                        help="Official CAOP2025 district polygons in EPSG:4326 GeoJSON")
    parser.add_argument("--refresh", action="store_true",
                        help="Download a fresh snapshot instead of reusing cached extracts")
    parser.add_argument("--apply", action="store_true", help="Write to API (off by default)")
    parser.add_argument("--confirm", default="", help="Must be IMPORTAR_RECINTOS to enable writes")
    parser.add_argument("--api-url", default=os.environ.get("D24_API_URL", "https://api.d24sports.com"))
    parser.add_argument("--batch-size", type=int, default=50)
    args = parser.parse_args(argv)
    if not 1 <= args.batch_size <= 100:
        parser.error("batch size must be between 1 and 100")
    if args.apply and args.confirm != "IMPORTAR_RECINTOS":
        parser.error("Writes require --apply --confirm IMPORTAR_RECINTOS")
    token = os.environ.get("D24_ADMIN_TOKEN")
    if args.apply and not token:
        parser.error("D24_ADMIN_TOKEN is required for authenticated imports")
    if args.apply and (not args.municipalities_geojson or not args.districts_geojson):
        parser.error("API writes require BOTH official CAOP municipality and district boundaries")
    slugs = list(DISTRICTS) if args.district == "all" else [args.district]
    args.cache_dir.mkdir(parents=True, exist_ok=True)
    municipality_lookup = (
        MunicipalityLookup(args.municipalities_geojson)
        if args.municipalities_geojson else None)
    district_lookup = (
        DistrictLookup(args.districts_geojson)
        if args.districts_geojson else None)
    report = {}
    for slug in slugs:
        print(f"[{slug}] Preparing district snapshot", flush=True)
        staged, stats = stage(slug, args.cache_dir, args.refresh, municipality_lookup, district_lookup)
        report[slug] = stats
        print(f"[{slug}] {stats.get('prepared', 0)} candidates: "
              f"{stats.get('import_ready', 0)} ready, "
              f"{stats.get('review_required', 0)} quarantined; "
              f"name={stats.get('name_to_verify', 0)}, "
              f"municipality={stats.get('municipality_to_verify', 0)}, "
              f"sports={stats.get('sports_to_verify', 0)}", flush=True)
        if args.apply:
            sent = apply_batches(args.api_url, token, staged, args.batch_size, args.cache_dir)
            report[slug]["submitted"] = sent
            print(f"[{slug}] {sent} submitted (includes existing records)", flush=True)
    report["summary"] = {
        "districts": len(slugs),
        "prepared": sum(v.get("prepared", 0) for v in report.values()),
        "import_ready": sum(v.get("import_ready", 0) for v in report.values()),
        "review_required": sum(v.get("review_required", 0) for v in report.values()),
        "submitted": sum(v.get("submitted", 0) for v in report.values()),
        "mode": "apply" if args.apply else "dry-run",
        "districts_verified": district_lookup is not None,
        "municipalities_verified": municipality_lookup is not None,
        "note": "No venue is import-ready without official CAOP district geometry; missing municipality/sport require review. Relation-only geometry is not yet supported."
    }
    path = args.cache_dir / "national-report.json"
    path.write_text(json.dumps(report, indent=2, ensure_ascii=False), encoding="utf-8")
    print(f"Report: {path}")
    return 0


if __name__ == "__main__":
    sys.exit(main())

#!/usr/bin/env python3
import json
import os
import sys
import urllib.parse
import urllib.request

API_URL = os.getenv("DESPORTO24_API_URL", "https://desporto24-preview-api.onrender.com").rstrip("/")
ADMIN_TOKEN = os.getenv("DESPORTO24_ADMIN_TOKEN")
BBOX = os.getenv("D24_OSM_BBOX")
OVERPASS_URL = os.getenv("D24_OVERPASS_URL", "https://overpass-api.de/api/interpreter")
CHUNK_SIZE = int(os.getenv("D24_IMPORT_CHUNK_SIZE", "100"))
# No write is permitted by default. A real import needs BOTH explicit switches.
DRY_RUN = os.getenv("D24_DRY_RUN", "true").lower() in {"1", "true", "yes"}
CONFIRM_IMPORT = os.getenv("D24_CONFIRM_IMPORT", "") == "I_UNDERSTAND"
REPORT_PATH = os.getenv("D24_IMPORT_REPORT_PATH")

SPORT_MAP = {
    "soccer": "Futebol",
    "futsal": "Futsal",
    "basketball": "Basquetebol",
    "tennis": "Ténis",
    "padel": "Padel",
    "volleyball": "Voleibol",
    "handball": "Andebol",
    "swimming": "Natação",
    "athletics": "Atletismo",
    "rugby": "Rugby",
    "gymnastics": "Ginástica",
    "martial_arts": "Artes Marciais",
}

# Only administrative area labels explicitly supplied by OSM are accepted.
# Never infer a district from the city, BBOX or proximity.
import unicodedata

DISTRICTS = (
    "Aveiro", "Beja", "Braga", "Bragança", "Castelo Branco", "Coimbra",
    "Évora", "Faro", "Guarda", "Leiria", "Lisboa", "Portalegre",
    "Porto", "Santarém", "Setúbal", "Viana do Castelo", "Vila Real", "Viseu",
)
REGIONS = ("Região Autónoma dos Açores", "Região Autónoma da Madeira")

def normalize_area(value):
    decomposed = unicodedata.normalize("NFKD", str(value or "").strip())
    return " ".join("".join(ch for ch in decomposed if not unicodedata.combining(ch)).casefold().split())

AREA_ALIASES = {normalize_area(area): area for area in DISTRICTS + REGIONS}
AREA_ALIASES.update({"acores": REGIONS[0], "madeira": REGIONS[1]})

def verified_district(tags):
    for key in ("addr:district", "addr:province", "addr:state"):
        value = AREA_ALIASES.get(normalize_area(tags.get(key)))
        if value:
            return value
    return None


VENUE_TYPES = {
    "sports_centre": "Complexo desportivo",
    "pitch": "Campo desportivo",
    "stadium": "Estádio",
    "track": "Pista",
    "swimming_pool": "Piscina",
    "sports_hall": "Pavilhão desportivo",
}


def require_env():
    if not BBOX:
        raise SystemExit("Missing D24_OSM_BBOX (south,west,north,east)")
    try:
        coords = [float(x.strip()) for x in BBOX.split(",")]
        if len(coords) != 4:
            raise ValueError("Expected four bbox values")
        south, west, north, east = coords
        if not (-90 <= south < north <= 90 and -180 <= west < east <= 180):
            raise ValueError("Bounding box values out of range")
    except ValueError as exc:
        raise SystemExit("Invalid D24_OSM_BBOX: use south,west,north,east") from exc
    if not 1 <= CHUNK_SIZE <= 1000:
        raise SystemExit("D24_IMPORT_CHUNK_SIZE must be between 1 and 1000")
    if not DRY_RUN:
        if not ADMIN_TOKEN:
            raise SystemExit("Missing DESPORTO24_ADMIN_TOKEN")
        if not CONFIRM_IMPORT:
            raise SystemExit(
                'Real imports require D24_CONFIRM_IMPORT=I_UNDERSTAND (and D24_DRY_RUN=false)'
            )


def overpass_query():
    return f"""
[out:json][timeout:120];
(
  nwr["leisure"~"sports_centre|pitch|stadium|track|swimming_pool"]({BBOX});
  nwr["building"="sports_hall"]({BBOX});
);
out center tags;
""".strip()


def fetch_osm():
    data = urllib.parse.urlencode({"data": overpass_query()}).encode()
    req = urllib.request.Request(
        OVERPASS_URL,
        data=data,
        headers={
            "User-Agent": "Desporto24-venue-import/1.0",
            "Content-Type": "application/x-www-form-urlencoded",
        },
        method="POST",
    )
    with urllib.request.urlopen(req, timeout=180) as response:
        return json.load(response)


def sport_names(tags):
    values = []
    for raw in (tags.get("sport") or "").split(";"):
        key = raw.strip().lower()
        if not key:
            continue
        values.append(SPORT_MAP.get(key, key.replace("_", " ").title()))
    return ";".join(dict.fromkeys(values)) or None


def venue_type(tags):
    leisure = tags.get("leisure")
    if leisure in VENUE_TYPES:
        return VENUE_TYPES[leisure]
    if tags.get("building") == "sports_hall":
        return VENUE_TYPES["sports_hall"]
    return "Recinto desportivo"


def environment_type(tags):
    indoor = (tags.get("indoor") or "").lower()
    if indoor in {"yes", "true", "1"} or tags.get("building") == "sports_hall":
        return "INDOOR"
    if indoor in {"no", "false", "0"}:
        return "OUTDOOR"
    return None


def coordinates(element):
    if "lat" in element and "lon" in element:
        return element["lat"], element["lon"]
    center = element.get("center") or {}
    return center.get("lat"), center.get("lon")


def address(tags):
    street = (tags.get("addr:street") or "").strip()
    number = (tags.get("addr:housenumber") or "").strip()
    place = (tags.get("addr:place") or "").strip()
    full = (tags.get("addr:full") or "").strip()
    postcode = (tags.get("addr:postcode") or "").strip()
    city = (
        tags.get("addr:city")
        or tags.get("addr:town")
        or tags.get("addr:village")
        or tags.get("is_in:city")
        or ""
    ).strip()

    # City alone (or city + postcode) is not a usable venue address.
    # Never manufacture a postal address from the venue name or coordinates.
    location = full or (" ".join(part for part in (street, number) if part) or place)
    suffix = " ".join(part for part in (postcode, city) if part)
    complete = ", ".join(part for part in (location, suffix) if part) if location else ""
    return city, complete


def to_payload(element):
    tags = element.get("tags") or {}
    name = (tags.get("name") or "").strip()
    city, full_address = address(tags)
    lat, lon = coordinates(element)

    # The BE requires a meaningful name, city and address. A city by itself
    # is not a postal address, even when OSM includes a postcode.
    if not name or not city or not full_address:
        return None

    return {
        "sourceReference": f"OSM:{element['type']}/{element['id']}",
        "name": name,
        "city": city,
        "district": verified_district(tags),
        "address": full_address,
        "sports": sport_names(tags),
        "venueType": venue_type(tags),
        "environmentType": environment_type(tags),
        "latitude": lat,
        "longitude": lon,
    }


def post_chunk(chunk):
    body = json.dumps(chunk).encode()
    req = urllib.request.Request(
        f"{API_URL}/api/admin/venues/imports/openstreetmap",
        data=body,
        headers={
            "Authorization": f"Bearer {ADMIN_TOKEN}",
            "Content-Type": "application/json",
            "User-Agent": "Desporto24-venue-import/1.0",
        },
        method="POST",
    )
    with urllib.request.urlopen(req, timeout=180) as response:
        return json.load(response)


def main():
    require_env()
    osm = fetch_osm()
    elements = osm.get("elements", [])
    payload = []
    rejected = {"missing_name": 0, "missing_city": 0, "missing_address": 0}
    for element in elements:
        candidate = to_payload(element)
        if candidate:
            payload.append(candidate)
        else:
            tags = element.get("tags") or {}
            city, full_address = address(tags)
            reason = ("missing_name" if not (tags.get("name") or "").strip()
                      else "missing_city" if not city else "missing_address")
            rejected[reason] += 1

    report = {
        "bbox": BBOX,
        "dryRun": DRY_RUN,
        "osmElements": len(elements),
        "importable": len(payload),
        "withVerifiedDistrict": sum(bool(item.get("district")) for item in payload),
        "withoutVerifiedDistrict": sum(not item.get("district") for item in payload),
        "rejected": rejected,
        "sample": payload[:5],
    }
    if REPORT_PATH:
        with open(REPORT_PATH, "w", encoding="utf-8") as report_file:
            json.dump(report, report_file, ensure_ascii=False, indent=2)
            report_file.write("\n")
        print(f"Review report written to {REPORT_PATH}")

    print(f"Found {len(payload)}/{len(elements)} importable OSM elements; rejected {rejected}")
    if not payload:
        print("No importable venues found. Check bbox/data completeness.")
        return 0

    if DRY_RUN:
        print("DRY RUN: nothing was sent to the Jynlo API.")
        print(json.dumps(payload[:5], ensure_ascii=False, indent=2))
        return 0

    imported = 0
    for start in range(0, len(payload), CHUNK_SIZE):
        chunk = payload[start:start + CHUNK_SIZE]
        result = post_chunk(chunk)
        imported += len(result)
        print(f"Imported {imported}/{len(payload)}")

    print(f"Done. Imported or updated {imported} venues.")
    return 0


if __name__ == "__main__":
    sys.exit(main())

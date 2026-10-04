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
DRY_RUN = os.getenv("D24_DRY_RUN", "false").lower() in {"1", "true", "yes"}

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

VENUE_TYPES = {
    "sports_centre": "Complexo desportivo",
    "pitch": "Campo desportivo",
    "stadium": "Estádio",
    "track": "Pista",
    "swimming_pool": "Piscina",
    "sports_hall": "Pavilhão desportivo",
}


def require_env():
    if not DRY_RUN and not ADMIN_TOKEN:
        raise SystemExit("Missing DESPORTO24_ADMIN_TOKEN")
    if not BBOX:
        raise SystemExit("Missing D24_OSM_BBOX (south,west,north,east)")


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
    postcode = (tags.get("addr:postcode") or "").strip()
    city = (
        tags.get("addr:city")
        or tags.get("addr:town")
        or tags.get("addr:village")
        or tags.get("is_in:city")
        or ""
    ).strip()

    street_line = " ".join(part for part in (street, number) if part)
    suffix = " ".join(part for part in (postcode, city) if part)
    full = ", ".join(part for part in (street_line, suffix) if part)
    return city, full


def to_payload(element):
    tags = element.get("tags") or {}
    name = (tags.get("name") or "").strip()
    city, full_address = address(tags)
    lat, lon = coordinates(element)

    # The BE currently requires a meaningful name, city and address.
    if not name or not city or not full_address:
        return None

    return {
        "sourceReference": f"OSM:{element['type']}/{element['id']}",
        "name": name,
        "city": city,
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
    payload = [item for item in (to_payload(e) for e in osm.get("elements", [])) if item]

    if not payload:
        print("No importable venues found. Check bbox/data completeness.")
        return 0

    if DRY_RUN:
        print(f"Dry run: {len(payload)} importable venues found.")
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

#!/usr/bin/env python3
"""Read-only public HTTP preflight for the three unapproved municipal venue candidates.

Queries fixed Jynlo GET endpoints only, with no login, imports, database access,
or POST. Public results are *hints*, NOT a substitute for live SQL preflight.
"""
import json
import math
from pathlib import Path
from urllib.error import HTTPError, URLError
from urllib.parse import urlencode
from urllib.request import HTTPRedirectHandler, Request, build_opener

API_ORIGIN = "https://api.d24sports.com"
PROPOSALS = Path(__file__).resolve().parent.parent / (
    "docs/venue-imports/first-three-proposals-unapproved-20261021.jsonl"
)
EXPECTED_REFS = frozenset({
    "OSM:WAY:145943204", "OSM:WAY:122685516", "OSM:WAY:1056855283"
})
RADIUS_KM = 0.15
MAX_BYTES = 1_000_000


class NoRedirect(HTTPRedirectHandler):
    def redirect_request(self, req, fp, code, msg, headers, newurl):
        raise ValueError("Public venue API unexpectedly redirected; refusing other hosts")


def distance_metres(a, b):
    lat1, lon1 = math.radians(float(a["latitude"])), math.radians(float(a["longitude"]))
    lat2, lon2 = math.radians(float(b["latitude"])), math.radians(float(b["longitude"]))
    h = math.sin((lat2-lat1)/2)**2 + math.cos(lat1)*math.cos(lat2)*math.sin((lon2-lon1)/2)**2
    return 2 * 6371008.8 * math.asin(min(1.0, math.sqrt(h)))


def load_proposals(path=PROPOSALS):
    rows = [json.loads(line) for line in Path(path).read_text(encoding="utf-8").splitlines()
            if line.strip()]
    if len(rows) != 3 or {r["sourceReference"] for r in rows} != EXPECTED_REFS:
        raise ValueError("Pilot candidates are not the pinned three OSM references")
    if any(r.get("approved") is not False for r in rows):
        raise ValueError("This public API audit accepts only unapproved proposals")
    for row in rows:
        for key in ("latitude", "longitude"):
            val = row.get(key)
            if isinstance(val, bool) or not isinstance(val, (int, float)) or not math.isfinite(val):
                raise ValueError("Missing finite candidate GPS")
    return sorted(rows, key=lambda r: r["sourceReference"])


def public_get(endpoint, params, *, opener=None):
    if endpoint not in {"/api/venues", "/api/venues/suggest"}:
        raise ValueError("Unexpected endpoint")
    url = API_ORIGIN + endpoint + "?" + urlencode(params)
    opener = opener or build_opener(NoRedirect)
    request = Request(url, headers={"Accept": "application/json",
                                    "User-Agent": "Jynlo-Venue-ReadOnly-Preflight/1.0"},
                      method="GET")
    try:
        with opener.open(request, timeout=15) as response:
            if response.status != 200:
                raise ValueError("GET venue returned non-200 HTTP")
            if "json" not in response.headers.get("Content-Type", "").lower():
                raise ValueError("Public endpoint did not return JSON")
            data = response.read(MAX_BYTES + 1)
            if len(data) > MAX_BYTES:
                raise ValueError("Public API returned an unexpectedly large response")
            return json.loads(data)
    except (HTTPError, URLError) as error:
        raise RuntimeError("Public venue GET unavailable: " + str(error)) from error


def validate_venues(rows, *, context):
    if not isinstance(rows, list) or len(rows) > 500:
        raise ValueError("Unexpected " + context + " venue array")
    for row in rows:
        if not isinstance(row, dict) or not isinstance(row.get("name"), str) or not isinstance(row.get("city"), str):
            raise ValueError("Unexpected " + context + " venue object")
    return rows


def check_one(candidate, *, fetch=public_get):
    # A 150 m radius deliberately includes neighbours in adjacent municipalities.
    nearby = validate_venues(fetch("/api/venues", {
        "latitude": candidate["latitude"], "longitude": candidate["longitude"],
        "radiusKm": RADIUS_KM,
    }), context="nearby")
    suggestion = fetch("/api/venues/suggest", {
        "name": candidate["name"], "city": candidate["city"], "page": 0
    })
    if not isinstance(suggestion, dict) or not isinstance(suggestion.get("venues"), list):
        raise ValueError("Unexpected /suggest response; do not assert zero duplicates")
    named = validate_venues(suggestion["venues"], context="suggestion")
    possible = {}
    for row in nearby:
        if row.get("latitude") is None or row.get("longitude") is None:
            raise ValueError("Geo endpoint returned venue without coordinates")
        distance = round(distance_metres(candidate, row))
        if distance > 155:
            raise ValueError("Geo endpoint returned venue outside requested radius")
        possible[str(row.get("id"))] = {
            "id": row.get("id"), "name": row["name"], "city": row["city"],
            "reason": "WITHIN_150M", "distanceMetres": distance
        }
    for row in named:
        key = str(row.get("id"))
        if key not in possible:
            possible[key] = {
                "id": row.get("id"), "name": row["name"], "city": row["city"],
                "reason": "POSSIBLE_NAME_ALIAS"
            }
    return {
        "sourceReference": candidate["sourceReference"], "name": candidate["name"],
        "city": candidate["city"], "possiblePublicMatches": list(possible.values()),
        "publicMatchCount": len(possible),
        "publicApiReadOnly": True, "publicationApproved": False,
        "sourceReferenceNotExposedByPublicApi": True,
        "sqlPreflightStillRequired": True,
    }


def main():
    rows = load_proposals()
    checks = [check_one(row) for row in rows]
    print(json.dumps({"checked": len(checks), "publicApi": API_ORIGIN,
                      "approvedForImport": 0, "dbWrites": False,
                      "notEquivalentToFreshSqlPreflight": True,
                      "results": checks}, ensure_ascii=False, indent=2))


if __name__ == "__main__":
    main()

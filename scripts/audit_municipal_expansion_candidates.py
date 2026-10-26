#!/usr/bin/env python3
"""Read-only candidate-versus-LIVE comparison for Jynlo's municipal venues.

Never modifies a venue, opens a database connection, infers public access,
copies contacts, or converts a candidate into an approved import.
"""
from __future__ import annotations

import argparse
from collections import Counter
import json
import math
from pathlib import Path
import re
import unicodedata
from urllib.parse import urlsplit

from audit_venue_public_contacts import check_catalogue, load_public_catalogue

GENERIC = frozenset({
    "de", "da", "do", "dos", "das", "e", "municipal", "municipais",
    "complexo", "desportivo", "desportiva", "pavilhao", "pavilhoes",
    "piscina", "piscinas", "estadio", "campo", "campos", "centro",
    "praia", "instalacoes", "instalacao",
})
TYPES = {
    "piscina": ("piscin", "aquatic"),
    "estadio": ("estadi",),
    "pavilhao": ("pavilh", "nave desportiv"),
    "atletismo": ("atletism", "pista"),
}
MAX_MATCHES_DISPLAYED = 5
MAX_CANDIDATES = 100
MANIFEST_URL_HOSTS = frozenset({
    "www.cm-evora.pt", "www.cm-castelobranco.pt", "viversantarem.pt",
})


def normalized(value):
    value = unicodedata.normalize("NFKD", str(value or "").casefold())
    value = "".join(c for c in value if not unicodedata.combining(c))
    return re.sub(r"[^a-z0-9]+", " ", value).strip()


def distinctive(name):
    return {word for word in normalized(name).split()
            if len(word) >= 4 and word not in GENERIC}


def category(name):
    value = normalized(name)
    for key, patterns in TYPES.items():
        if any(pattern in value for pattern in patterns):
            return key
    return None


def valid_reference(url):
    if not isinstance(url, str):
        return False
    parsed = urlsplit(url)
    approved_query = (
        not parsed.query or (
            parsed.hostname == "www.cm-castelobranco.pt"
            and parsed.path == "/municipe/noticias/detalhe-noticia/"
            and re.fullmatch(r"id=[1-9][0-9]{0,8}", parsed.query) is not None
        )
    )
    return (parsed.scheme == "https" and parsed.hostname in MANIFEST_URL_HOSTS
            and parsed.username is None and parsed.password is None
            and parsed.port is None and bool(parsed.path)
            and approved_query and not parsed.fragment)


def read_manifest(manifest):
    if not isinstance(manifest, dict) or manifest.get("recordCount") != 13:
        raise ValueError("Expected the 13 previously documented municipal candidates")
    entries = manifest.get("venues")
    if not isinstance(entries, list) or len(entries) != 13:
        raise ValueError("Municipal manifest must contain exactly 13 candidates")
    for candidate in entries:
        if not isinstance(candidate, dict):
            raise ValueError("Invalid municipal candidate")
        if candidate.get("approvedForImport") is not False:
            raise ValueError("Candidates are not approved for import")
        if not all(isinstance(candidate.get(k), str) and candidate[k].strip()
                   for k in ("name", "city", "district")):
            raise ValueError("Candidate requires name, city and district")
        if not valid_reference(candidate.get("sourceUrl")):
            raise ValueError("Candidate requires existing HTTPS municipal evidence")
    return entries


def distance_meters(left, right):
    loc = left.get("location") or {}
    try:
        a, b = float(loc["latitude"]), float(loc["longitude"])
        c, d = float(right["latitude"]), float(right["longitude"])
    except (KeyError, TypeError, ValueError):
        return None
    if not all(map(math.isfinite, (a, b, c, d))):
        return None
    lat1, lat2 = math.radians(a), math.radians(c)
    dl = math.radians(d - b)
    dp = math.radians(c - a)
    aa = math.sin(dp / 2) ** 2 + math.cos(lat1) * math.cos(lat2) * math.sin(dl / 2) ** 2
    return round(12742000 * math.asin(min(1, math.sqrt(aa))))


def compare(catalogue, manifest):
    # This validator intentionally rejects duplicate IDs, malformed API payloads.
    check_catalogue(catalogue)
    entries = read_manifest(manifest)
    rows = []
    for entry in entries:
        same_place = [
            venue for venue in catalogue
            if normalized(venue.get("district")) == normalized(entry["district"])
            and normalized(venue.get("city")) == normalized(entry["city"])
        ]
        exact = [v for v in same_place
                 if normalized(v.get("name")) == normalized(entry["name"])]
        if exact:
            matches = exact
            status = "EXACT_EXISTING" if len(exact) == 1 else "DUPLICATE_EXACT_REVIEW"
        else:
            words = distinctive(entry["name"])
            kind = category(entry["name"])
            # Conservative, recall-oriented: same city and same sport facility type,
            # two distinctive full words, or <=250 m from an official verified point.
            # These are *possible* aliases, NOT proven duplicates.
            ranked = []
            for venue in same_place:
                overlap = len(words & distinctive(venue.get("name")))
                proximity = distance_meters(entry, venue)
                same_type = bool(kind and kind == category(venue.get("name")))
                if overlap >= 2 or same_type or (
                        proximity is not None and proximity <= 250):
                    ranked.append((int(overlap >= 2), int(same_type),
                                   int(proximity is not None and proximity <= 250), venue))
            ranked.sort(key=lambda t: (-t[0], -t[1], -t[2], t[3]["id"]))
            matches = [t[3] for t in ranked[:MAX_CANDIDATES]]
            status = ("POSSIBLE_ALIAS_REVIEW" if ranked
                      else "NO_NAME_MATCH_REVIEW")
        rows.append({
            "name": entry["name"], "city": entry["city"],
            "district": entry["district"], "sourceUrl": entry["sourceUrl"],
            "status": status, "candidate_count": len(matches),
            "candidates_truncated": len(matches) > MAX_MATCHES_DISPLAYED,
            "existing": [
                {"id": venue["id"], "name": venue.get("name") or ""}
                for venue in matches[:MAX_MATCHES_DISPLAYED]
            ],
            "approvedForImport": False,
        })
    return {
        "catalogue_total": len(catalogue),
        "source_candidate_total": len(entries),
        "status_counts": dict(sorted(Counter(x["status"] for x in rows).items())),
        "note": "Public catalogue comparison only; no candidate is approved or inserted",
        "results": rows,
    }


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    group = parser.add_mutually_exclusive_group(required=True)
    group.add_argument("--live-public-api", action="store_true")
    group.add_argument("--venues-json", type=Path)
    parser.add_argument("--sources-json", type=Path, required=True)
    parser.add_argument("--output-json", type=Path, required=True)
    args = parser.parse_args()
    catalogue = (load_public_catalogue() if args.live_public_api else
                 json.loads(args.venues_json.read_text(encoding="utf-8")))
    manifest = json.loads(args.sources_json.read_text(encoding="utf-8"))
    result = compare(catalogue, manifest)
    args.output_json.parent.mkdir(parents=True, exist_ok=True)
    args.output_json.write_text(json.dumps(result, ensure_ascii=False, indent=2) + "\n",
                                encoding="utf-8")
    print("Jynlo LIVE catalogue:", result["catalogue_total"])
    print("Municipal evidence candidates:", result["source_candidate_total"])
    for row in result["results"]:
        ids = [v["id"] for v in row["existing"]]
        print(row["name"], "|", row["status"], "| possible IDs:", ids)
    print("No contact details exported; no imports or database writes.")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())

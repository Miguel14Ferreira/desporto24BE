#!/usr/bin/env python3
"""Read-only audit of six official island sports facilities against Jynlo LIVE.

Do not use it to approve inserts: backend has mainland-only GPS validation,
mainland district search whitelist, and a mainland-only database CHECK constraint.
"""
from __future__ import annotations

import argparse
from collections import Counter
import json
from pathlib import Path
import re
import unicodedata
from urllib.parse import urlsplit

from audit_venue_public_contacts import check_catalogue, load_public_catalogue

EXPECTED = {"Açores": 2, "Madeira": 4}
OFFICIAL_HOSTS = {
    "portal.azores.gov.pt", "jo.azores.gov.pt", "clubes.fpx.pt",
    "www.cm-pontadelgada.pt", "www.madeira.gov.pt",
}


def normalized(text):
    word = unicodedata.normalize("NFD", str(text or "").casefold())
    return " ".join(re.sub(r"[^a-z0-9]+", " ", "".join(
        c for c in word if unicodedata.category(c) != "Mn")).split())


def official_https(url):
    if not isinstance(url, str):
        return False
    try:
        p = urlsplit(url)
        return (p.scheme == "https" and p.hostname in OFFICIAL_HOSTS
                and p.port is None and p.username is None and p.password is None
                and bool(p.path) and not p.fragment)
    except ValueError:
        return False


def catalog_sports(java_source):
    # Read the repository's ACTUAL Java allowed catalogue rather than invent names.
    values = re.findall(r'^\s*"([^"]+)"\s*,?\s*$', java_source, re.MULTILINE)
    if len(values) < 100:
        raise ValueError("Jynlo sports catalogue not detected safely")
    return set(values)


def validate_manifest(manifest, allowed_sports):
    if not isinstance(manifest, dict) or manifest.get("approvedForImport") is not False:
        raise ValueError("Manifest must explicitly remain unapproved")
    entries = manifest.get("venues")
    if not isinstance(entries, list) or len(entries) != 6:
        raise ValueError("Exactly six first-island pilot source candidates expected")
    seen, counts = set(), Counter()
    for venue in entries:
        if not isinstance(venue, dict) or venue.get("approvedForImport") is not False:
            raise ValueError("No candidate may be approved for import")
        if venue.get("currentAccessConfirmed") is not False:
            raise ValueError("No current public access may be assumed")
        if venue.get("contact") is not None:
            raise ValueError("Contact is optional; do not track phone data in source file")
        if venue.get("latitude") is not None or venue.get("longitude") is not None:
            raise ValueError("Do not invent island GPS in this documentation pilot")
        for key in ("sourceId", "name", "city", "district", "address", "venueType"):
            if not isinstance(venue.get(key), str) or not venue[key].strip():
                raise ValueError(f"{key}: nonempty source-backed text required")
        if venue["sourceId"] in seen:
            raise ValueError("Duplicate island sourceId")
        seen.add(venue["sourceId"])
        if venue["district"] not in EXPECTED:
            raise ValueError("Only Açores and Madeira in this pilot")
        counts[venue["district"]] += 1
        sports = venue.get("sports")
        if (not isinstance(sports, list) or
                any(not isinstance(s, str) or s not in allowed_sports for s in sports)):
            raise ValueError("All sports must match existing Jynlo catalogue labels")
        urls = venue.get("sourceUrls")
        if not isinstance(urls, list) or not urls or any(
                not official_https(u) for u in urls):
            raise ValueError("Requires official HTTPS documentary evidence")
    if dict(counts) != EXPECTED:
        raise ValueError("Expected two Açores and four Madeira source proposals")
    return entries


def compare_public(catalogue, candidates):
    check_catalogue(catalogue)
    result = []
    for entry in candidates:
        name = normalized(entry["name"])
        city = normalized(entry["city"])
        exact = [v for v in catalogue
                 if normalized(v.get("name")) == name and normalized(v.get("city")) == city]
        other = [v for v in catalogue
                 if normalized(v.get("name")) == name and normalized(v.get("city")) != city]
        status = ("EXACT_PUBLIC_NAME_AND_CITY" if exact
                  else "NAME_IN_DIFFERENT_CITY_REVIEW" if other
                  else "NO_EXACT_PUBLIC_MATCH_REVIEW")
        matches = exact + other
        result.append({
            "sourceId": entry["sourceId"], "name": entry["name"],
            "city": entry["city"], "district": entry["district"],
            "status": status,
            "candidateExistingIds": [x["id"] for x in matches[:6]],
            "matchesTruncated": len(matches) > 6,
            "approvedForImport": False,
        })
    return {
        "catalogueCount": len(catalogue), "islandSourceCandidates": len(result),
        "counts": dict(sorted(Counter(x["status"] for x in result).items())),
        "results": result,
        "warning": "Public name/city matches do not rule out aliases or prove island availability",
        "productionApproved": False,
    }


def main():
    root = Path(__file__).resolve().parent.parent
    parser = argparse.ArgumentParser(description=__doc__)
    group = parser.add_mutually_exclusive_group(required=True)
    group.add_argument("--live-public-api", action="store_true")
    group.add_argument("--venues-json", type=Path)
    parser.add_argument("--manifest", type=Path, default=
                        root / "docs/venue-imports/islands-first-six-official-sources-20261026.json")
    parser.add_argument("--sports-catalog", type=Path, default=
                        root / "src/main/java/com/example/desporto24/constant/SportsCatalog.java")
    parser.add_argument("--output-json", type=Path, required=True)
    args = parser.parse_args()
    manifest = json.loads(args.manifest.read_text(encoding="utf-8"))
    allowed = catalog_sports(args.sports_catalog.read_text(encoding="utf-8"))
    rows = validate_manifest(manifest, allowed)
    catalogue = (load_public_catalogue() if args.live_public_api else
                 json.loads(args.venues_json.read_text(encoding="utf-8")))
    report = compare_public(catalogue, rows)
    args.output_json.parent.mkdir(parents=True, exist_ok=True)
    args.output_json.write_text(json.dumps(report, ensure_ascii=False, indent=2) + "\n",
                                encoding="utf-8")
    print("Jynlo LIVE catalogue:", report["catalogueCount"])
    print("Pilot candidate distribution: Açores 2 / Madeira 4")
    print("Result counts:", report["counts"])
    for row in report["results"]:
        print(row["sourceId"], "|", row["status"], "| existing IDs:", row["candidateExistingIds"])
    print("NO database writes, no island GPS, no contact exports, no import approval.")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())

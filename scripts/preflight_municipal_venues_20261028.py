#!/usr/bin/env python3
"""Read-only municipality source shortlist versus the public Jynlo venue catalogue.

Reports exact matches, likely naming aliases and unresolved candidates.
Never imports, approves, edits, fetches source websites, accesses Postgres,
or writes identifiable venue-level data beyond names/IDs already public.
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

ALLOWED_HOSTS = frozenset(("www.cm-braganca.pt", "www.mun-guarda.pt"))
EXPECTED_DISTRICTS = frozenset(("Bragança", "Guarda"))
GENERIC = frozenset(("pavilhao", "municipal", "municipais", "desportivo",
                     "desportiva", "piscina", "piscinas", "estadio",
                     "campo", "campos", "atletismo", "de", "do", "da", "dos", "das"))
MAX_MATCHES = 5


def normalize(value):
    value = unicodedata.normalize("NFKD", str(value or "").casefold())
    value = "".join(x for x in value if not unicodedata.combining(x))
    return re.sub(r"[^a-z0-9]+", " ", value).strip()


def distinctive(name, city):
    return {part for part in normalize(name).split()
            if len(part) >= 4 and part not in GENERIC
            and part not in normalize(city).split()}


def is_official_link(url):
    if not isinstance(url, str):
        return False
    parsed = urlsplit(url)
    return (parsed.scheme == "https" and parsed.hostname in ALLOWED_HOSTS
            and parsed.username is None and parsed.password is None
            and parsed.port is None and parsed.path not in ("", "/")
            and not parsed.query and not parsed.fragment)


def validate_manifest(manifest):
    if not isinstance(manifest, dict):
        raise ValueError("Manifest must be an object")
    rows = manifest.get("venues")
    if (manifest.get("status") != "SOURCE_CANDIDATES_ONLY_NOT_APPROVED"
            or not isinstance(rows, list) or len(rows) != 10
            or type(manifest.get("recordCount")) is not int
            or manifest["recordCount"] != len(rows)):
        raise ValueError("Expected exactly ten unapproved municipal candidates")
    seen = set()
    for row in rows:
        if not isinstance(row, dict):
            raise ValueError("Invalid municipal candidate")
        if row.get("approvedForImport") is not False:
            raise ValueError("Any approved candidate is forbidden in this audit")
        if (not all(isinstance(row.get(k), str) and row[k].strip()
                    for k in ("name", "city", "district"))
                or row["district"] not in EXPECTED_DISTRICTS
                or not is_official_link(row.get("sourceUrl"))):
            raise ValueError("Missing official source, name or approved district")
        key = (normalize(row["name"]), normalize(row["city"]),
               normalize(row["district"]))
        if key in seen:
            raise ValueError("Duplicate candidate in manifest")
        seen.add(key)
    return rows


def compare(catalogue, manifest):
    check_catalogue(catalogue)
    candidates = validate_manifest(manifest)
    reviewed = []
    for item in candidates:
        local = [
            row for row in catalogue
            if normalize(row.get("district")) == normalize(item["district"])
            and normalize(row.get("city")) == normalize(item["city"])]
        exact = [row for row in local
                 if normalize(row.get("name")) == normalize(item["name"])]
        if exact:
            potential = exact
            status = "EXACT_EXISTING"
        else:
            # Facility type alone (e.g. "pavilhão") does NOT establish an
            # alias: unrelated sports halls exist in the same municipality.
            words = distinctive(item["name"], item["city"])
            matches = []
            for row in local:
                overlap = len(words & distinctive(row.get("name"), item["city"]))
                if overlap:
                    matches.append((overlap, row))
            matches.sort(key=lambda pair: (-pair[0], pair[1]["id"]))
            potential = [m[1] for m in matches]
            status = ("POSSIBLE_ALIAS_REVIEW" if potential
                      else "NO_LOCAL_MATCH_REVIEW")
        reviewed.append({
            "candidate": item["name"], "city": item["city"],
            "district": item["district"], "sourceUrl": item["sourceUrl"],
            "status": status,
            "possibleMatchCount": len(potential),
            "existing": [{"id": x["id"], "name": str(x.get("name") or "")}
                         for x in potential[:MAX_MATCHES]],
            "matchesTruncated": len(potential) > MAX_MATCHES,
            "approvedForImport": False
        })
    return {
        "catalogueTotal": len(catalogue),
        "candidateCount": len(reviewed),
        "statusCounts": dict(sorted(Counter(row["status"]
                                           for row in reviewed).items())),
        "results": reviewed,
        "authorization": "READ_ONLY_NO_IMPORT",
        "note": "No candidate approved. No exact match alone proves a new venue."
    }


def main(argv=None):
    parser = argparse.ArgumentParser(description=__doc__)
    group = parser.add_mutually_exclusive_group(required=True)
    group.add_argument("--live-public-api", action="store_true")
    group.add_argument("--venues-json", type=Path)
    parser.add_argument("--sources-json", type=Path, required=True)
    parser.add_argument("--output-json", type=Path, required=True)
    args = parser.parse_args(argv)
    if args.venues_json is not None and args.venues_json.resolve() == args.output_json.resolve():
        parser.error("Cannot overwrite catalogue input")
    if args.sources_json.resolve() == args.output_json.resolve():
        parser.error("Cannot overwrite source manifest")
    catalogue = (load_public_catalogue() if args.live_public_api else
                 json.loads(args.venues_json.read_text(encoding="utf-8")))
    manifest = json.loads(args.sources_json.read_text(encoding="utf-8"))
    report = compare(catalogue, manifest)
    args.output_json.parent.mkdir(parents=True, exist_ok=True)
    args.output_json.write_text(json.dumps(report, ensure_ascii=False, indent=2) + "\n",
                                encoding="utf-8")
    print("PUBLIC CATALOGUE:", report["catalogueTotal"])
    print("CANDIDATE PREFLIGHT:", json.dumps(report["statusCounts"], ensure_ascii=False))
    for row in report["results"]:
        print(row["district"], "|", row["candidate"], "|", row["status"],
              "| matches:", row["possibleMatchCount"])
    print("No database connection, import, approval or venue modification.")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())

#!/usr/bin/env python3
"""Match a small allowlisted set of official Porto venue contact pages to Jynlo.

Read-only. Never fetches source websites, extracts phone numbers, submits a
suggestion, updates a venue, or assumes a public page permits bulk reuse.
This identifies *possible records to review*; it is not a contact importer.
"""
from __future__ import annotations

import argparse
from collections import Counter
import json
from pathlib import Path
import re
from urllib.parse import urlsplit

from audit_venue_public_contacts import (
    check_catalogue, contact_kind, fold, load_public_catalogue,
)

OFFICIAL_HOSTS = frozenset(("www.agoraporto.pt",))
MAX_FACILITIES = 10


def normalized_name(value):
    """Conservative punctuation-insensitive match, never fuzzy similarity."""
    return re.sub(r"[^a-z0-9]+", " ", fold(value)).strip()


def verify_manifest(manifest):
    if not isinstance(manifest, dict):
        raise ValueError("Source manifest must be a JSON object")
    sources = manifest.get("facilities")
    if not isinstance(sources, list) or not 1 <= len(sources) <= MAX_FACILITIES:
        raise ValueError("Expected between 1 and 10 manually checked facility references")
    if not manifest.get("reviewed_on") or not manifest.get("reuse_status"):
        raise ValueError("Missing review date or reuse disclosure")
    seen_urls = set()
    seen_names = set()
    for item in sources:
        if not isinstance(item, dict):
            raise ValueError("Invalid facility reference")
        if item.get("city") != "Porto" or item.get("district") != "Porto":
            raise ValueError("Pilot may only include facilities in Porto city and district")
        name = normalized_name(item.get("name"))
        if not name or name in seen_names:
            raise ValueError("Duplicate/invalid official venue name")
        seen_names.add(name)
        parsed = urlsplit(str(item.get("source_url") or ""))
        if parsed.scheme != "https" or parsed.hostname not in OFFICIAL_HOSTS or \
                parsed.username or parsed.password or parsed.port or \
                not parsed.path or parsed.query or parsed.fragment:
            raise ValueError("Source must be an exact official HTTPS facility URL")
        if item["source_url"] in seen_urls:
            raise ValueError("Duplicate official source URL")
        seen_urls.add(item["source_url"])
        if not item.get("street_hint") or not item.get("source_label"):
            raise ValueError("Manual facility street/name provenance is required")
        aliases = item.get("aliases", [])
        if not isinstance(aliases, list) or len(aliases) > 3 or \
                any(not isinstance(x, str) or not normalized_name(x) for x in aliases):
            raise ValueError("Aliases must be limited to explicitly reviewed facility names")
        if len({name, *(normalized_name(x) for x in aliases)}) != len(aliases) + 1:
            raise ValueError("Duplicate venue aliases")
        if any(key.lower() in ("phone", "contact", "telephone", "mobile", "venueid")
               for key in item):
            raise ValueError("Do not store numbers or guessed Jynlo IDs in the source manifest")
    return sources


def check_street(address, hint):
    """Does not infer a street from coordinates, and never certifies an address."""
    if not isinstance(address, str) or not address.strip() or \
            "confirmar" in fold(address) or "localizacao no mapa" in fold(address):
        return "MORADA_NAO_CONFIRMADA"
    # Punctuation-insensitive, but intentionally no fuzzy address matching.
    if normalized_name(hint) not in normalized_name(address):
        return "MORADA_DIVERGENTE_REVER"
    return "MORADA_COMPATIVEL_REVER"


def pilot(catalogue, manifest):
    check_catalogue(catalogue)
    sources = verify_manifest(manifest)
    name_index = {}
    for venue in catalogue:
        key = normalized_name(venue.get("name"))
        if key:
            name_index.setdefault(key, []).append(venue)
    rows = []
    for item in sources:
        aliases = (item["name"], *item.get("aliases", []))
        candidates = {v["id"]: v for alias in aliases
                      for v in name_index.get(normalized_name(alias), [])}
        status = "SEM_NOME_EXATO_NA_JYNLO"
        venue_id = None
        address_status = "NAO_AVALIADA"
        if len(candidates) > 1:
            status = "NOME_DUPLICADO_REVER"
        elif len(candidates) == 1:
            match = next(iter(candidates.values()))
            if fold(match.get("city")) != fold(item["city"]) or \
                    fold(match.get("district")) != fold(item["district"]):
                status = "LOCALIDADE_DIVERGENTE_REVER"
            else:
                venue_id = match["id"]
                address_status = check_street(match.get("address"), item["street_hint"])
                if contact_kind(match.get("contact")) != "without_contact":
                    status = "CONTACTO_EXISTENTE_NAO_SOBRESCREVER"
                elif address_status != "MORADA_COMPATIVEL_REVER":
                    status = address_status
                else:
                    status = "POSSIVEL_MATCH_REVISAO_HUMANA"
        rows.append({
            "official_name": item["name"],
            "jynlo_venue_id": venue_id,
            "status": status,
            "address_status": address_status,
            "official_source": item["source_url"],
            "reviewed_on": manifest["reviewed_on"],
        })
    return {
        "warning": "Only official source references, NOT phone numbers, approvals or reusable bulk data",
        "catalogue_count": len(catalogue),
        "status_counts": dict(sorted(Counter(row["status"] for row in rows).items())),
        "results": rows,
    }


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    group = parser.add_mutually_exclusive_group(required=True)
    group.add_argument("--venues-json", type=Path)
    group.add_argument("--live-public-api", action="store_true")
    parser.add_argument("--sources-json", type=Path, required=True)
    parser.add_argument("--output-json", type=Path, required=True)
    args = parser.parse_args()
    catalogue = (load_public_catalogue() if args.live_public_api
                 else json.loads(args.venues_json.read_text(encoding="utf-8")))
    manifest = json.loads(args.sources_json.read_text(encoding="utf-8"))
    result = pilot(catalogue, manifest)
    args.output_json.parent.mkdir(parents=True, exist_ok=True)
    args.output_json.write_text(json.dumps(result, ensure_ascii=False, indent=2) + "\n",
                                encoding="utf-8")
    print("Porto municipality source-reference pilot:", result["status_counts"])
    print("No contact extracted, copied, approved or changed.")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())

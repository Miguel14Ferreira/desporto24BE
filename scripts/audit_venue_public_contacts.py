#!/usr/bin/env python3
"""Read-only audit and human-review proposals for Jynlo venue public contacts.

No DB access, login, POST, contact updates, or third-party scraping.
A source candidate is NEVER an approved phone number.
"""
from __future__ import annotations

import argparse
from collections import Counter, defaultdict
import csv
import json
from pathlib import Path
import re
import unicodedata
from urllib.request import Request, build_opener, HTTPRedirectHandler

API_URL = "https://api.d24sports.com/api/venues"
MAX_BYTES = 48 * 1024 * 1024
DISTRICTS = (
    "Aveiro", "Beja", "Braga", "Bragança", "Castelo Branco", "Coimbra",
    "Évora", "Faro", "Guarda", "Leiria", "Lisboa", "Portalegre", "Porto",
    "Santarém", "Setúbal", "Viana do Castelo", "Vila Real", "Viseu",
)
COUNT_KEYS = ("total", "without_contact", "phone", "email", "web", "other")
REVIEW_COLUMNS = (
    "venue_id", "venue_name", "city", "district", "suggested_phone",
    "source_type", "source_url", "source_license", "observed_at", "status",
)
PHONE = re.compile(r"^(?:[289][0-9]{8}|\+351[289][0-9]{8})$")


def fold(value):
    value = unicodedata.normalize("NFD", str(value or "").strip())
    value = "".join(char for char in value if unicodedata.category(char) != "Mn")
    return re.sub(r"\s+", " ", value).casefold()


DISTRICT_LOOKUP = {fold(name): name for name in DISTRICTS}


def normalized_phone(value):
    """Only a single plausible Portuguese number; never a personal-data claim."""
    if not isinstance(value, str):
        return None
    candidate = re.sub(r"^(?:tel(?:efone)?|contacto|phone)\s*:\s*", "", value.strip(), flags=re.I)
    if not candidate or re.search(r"[@/?#;]|https?:|[a-z]", candidate, flags=re.I):
        return None
    if not re.fullmatch(r"\+?[0-9\s().-]+", candidate):
        return None
    compact = re.sub(r"[\s().-]", "", candidate)
    if not PHONE.fullmatch(compact):
        return None
    return compact if compact.startswith("+351") else "+351" + compact


def contact_kind(value):
    if not isinstance(value, str) or not value.strip():
        return "without_contact"
    if normalized_phone(value):
        return "phone"
    if "@" in value:
        return "email"
    if re.search(r"https?://|www\.", value, flags=re.I):
        return "web"
    return "other"


class NoRedirect(HTTPRedirectHandler):
    def redirect_request(self, req, fp, code, msg, headers, newurl):
        raise ValueError("Unexpected API redirect: refusing to follow it")


def load_public_catalogue():
    """Only read the fixed public Jynlo endpoint, never user-supplied URLs."""
    opener = build_opener(NoRedirect())
    request = Request(API_URL, headers={
        "User-Agent": "Jynlo-public-venue-contact-audit/1.0",
        "Accept": "application/json",
    })
    with opener.open(request, timeout=25) as response:
        if response.status != 200:
            raise ValueError("Venue API did not return HTTP 200")
        raw = response.read(MAX_BYTES + 1)
        if len(raw) > MAX_BYTES:
            raise ValueError("Public catalogue exceeds read-only size limit")
        return json.loads(raw)


def check_catalogue(data):
    if not isinstance(data, list):
        raise ValueError("Expected a JSON array of public /api/venues records")
    by_id = {}
    for item in data:
        if not isinstance(item, dict) or type(item.get("id")) is not int or item["id"] <= 0:
            raise ValueError("Invalid venue record or venue ID")
        if item["id"] in by_id:
            raise ValueError("Duplicate venue ID in catalogue")
        by_id[item["id"]] = item
    return by_id


def audit(catalogue):
    """Return aggregate counts only, without names or telephone numbers."""
    check_catalogue(catalogue)
    grouped = {name: Counter() for name in DISTRICTS}
    grouped["Por confirmar"] = Counter()
    for venue in catalogue:
        district = DISTRICT_LOOKUP.get(fold(venue.get("district")), "Por confirmar")
        bucket = grouped[district]
        bucket["total"] += 1
        bucket[contact_kind(venue.get("contact"))] += 1
    rows = [{"district": district, **{key: grouped[district][key] for key in COUNT_KEYS}}
            for district in (*DISTRICTS, "Por confirmar")]
    totals = {key: sum(row[key] for row in rows) for key in COUNT_KEYS}
    if totals["total"] != sum(totals[key] for key in COUNT_KEYS if key != "total"):
        raise AssertionError("Contacts must be counted in exactly one category")
    return rows, totals


def https_evidence(value):
    if not isinstance(value, str) or not value.startswith("https://"):
        return False
    from urllib.parse import urlsplit
    parsed = urlsplit(value)
    return bool(parsed.hostname and "." in parsed.hostname and
                not parsed.username and not parsed.password and not parsed.port)


def review_candidates(catalogue, proposals):
    """Proposals are evidence for a person to review, never insert payloads."""
    by_id = check_catalogue(catalogue)
    if not isinstance(proposals, list):
        raise ValueError("Source proposals must be a JSON array")
    conflicts = defaultdict(set)
    for source in proposals:
        if not isinstance(source, dict):
            raise ValueError("Invalid source proposal")
        source_id = source.get("venueId")
        if type(source_id) is int:
            phone = normalized_phone(source.get("phone"))
            if phone:
                conflicts[source_id].add(phone)
    result = []
    for source in proposals:
        venue_id = source.get("venueId")
        venue = by_id.get(venue_id) if type(venue_id) is int else None
        phone = normalized_phone(source.get("phone"))
        status = "REVIEW_REQUIRED"
        if venue is None:
            status = "NO_EXACT_VENUE_ID"
        elif not source.get("venueName") or fold(source["venueName"]) != fold(venue.get("name")):
            status = "NAME_MISMATCH"
        elif not source.get("district") or fold(source["district"]) != fold(venue.get("district")):
            status = "DISTRICT_MISMATCH"
        elif not source.get("city") or fold(source["city"]) != fold(venue.get("city")):
            status = "CITY_MISMATCH"
        elif contact_kind(venue.get("contact")) != "without_contact":
            status = "EXISTING_CONTACT_REVIEW"
        elif not phone:
            status = "INVALID_PHONE"
        elif len(conflicts[venue_id]) > 1:
            status = "CONFLICTING_SOURCE_PHONES"
        elif not https_evidence(source.get("sourceUrl")) or not source.get("sourceLicense"):
            status = "SOURCE_EVIDENCE_INCOMPLETE"
        elif source.get("reusePermissionChecked") is not True:
            status = "REUSE_PERMISSION_UNCONFIRMED"
        result.append({
            "venue_id": venue_id if type(venue_id) is int else "",
            "venue_name": str(source.get("venueName") or ""),
            "city": str(source.get("city") or ""),
            "district": str(source.get("district") or ""),
            "suggested_phone": phone or "",
            "source_type": str(source.get("sourceType") or ""),
            "source_url": str(source.get("sourceUrl") or ""),
            "source_license": str(source.get("sourceLicense") or ""),
            "observed_at": str(source.get("observedAt") or ""),
            "status": status,
        })
    return result


def write_csv(path, rows, columns):
    path = Path(path)
    path.parent.mkdir(parents=True, exist_ok=True)
    with path.open("w", newline="", encoding="utf-8-sig") as file:
        writer = csv.DictWriter(file, fieldnames=columns, delimiter=";")
        writer.writeheader()
        # A source name or URL may be untrusted. Neutralise spreadsheet formulas,
        # including +351 phone strings. Review CSV is private, never published.
        for row in rows:
            writer.writerow({key: ("'" + value if isinstance(value, str)
                                   and value.lstrip().startswith(("=", "+", "-", "@", "\t", "\r"))
                                   else value)
                             for key, value in row.items()})


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    venue_input = parser.add_mutually_exclusive_group(required=True)
    venue_input.add_argument("--venues-json", type=Path, help="Saved public /api/venues JSON array")
    venue_input.add_argument("--live-public-api", action="store_true", help="GET only; no credentials")
    parser.add_argument("--summary-json", type=Path, required=True)
    parser.add_argument("--districts-csv", type=Path, required=True)
    parser.add_argument("--proposals-json", type=Path, help="Manually curated, rights-checked source evidence")
    parser.add_argument("--review-csv", type=Path, help="Private review worksheet; NEVER upload publicly")
    args = parser.parse_args()
    if bool(args.proposals_json) != bool(args.review_csv):
        parser.error("--proposals-json and --review-csv must be used together")
    catalogue = (load_public_catalogue() if args.live_public_api
                 else json.loads(args.venues_json.read_text(encoding="utf-8")))
    rows, totals = audit(catalogue)
    review = None
    if args.proposals_json:
        proposals = json.loads(args.proposals_json.read_text(encoding="utf-8"))
        review = review_candidates(catalogue, proposals)
        write_csv(args.review_csv, review, REVIEW_COLUMNS)
    args.summary_json.parent.mkdir(parents=True, exist_ok=True)
    summary = {"scope": "public API snapshot; not a completeness claim",
               "totals": totals, "districts": len(DISTRICTS),
               "review_status_counts": dict(sorted(Counter(row["status"] for row in review).items())) if review is not None else {}}
    args.summary_json.write_text(json.dumps(summary, ensure_ascii=False, indent=2) + "\n",
                                 encoding="utf-8")
    write_csv(args.districts_csv, rows, ("district", *COUNT_KEYS))
    print("Read-only public venue contact audit:", totals)
    if review is not None:
        print("Private proposals generated for HUMAN review; no contact updated:", len(review))
    return 0


if __name__ == "__main__":
    raise SystemExit(main())

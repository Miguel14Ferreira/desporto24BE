#!/usr/bin/env python3
"""Read-only national SNID source audit (18 mainland districts).

Produces aggregate coverage *candidates*, never approved/available venues.
Does not access Jynlo accounts, APIs or databases. Downloads only official
public dados.gov.pt resources with a byte cap and no credentials.
"""
from __future__ import annotations

import argparse
import csv
from datetime import datetime, timezone
import hashlib
import json
import math
from pathlib import Path
import re
import unicodedata
from urllib.parse import urlparse
from urllib.request import Request, HTTPRedirectHandler, build_opener

DISTRICTS = (
    "Aveiro", "Beja", "Braga", "Bragança", "Castelo Branco", "Coimbra",
    "Évora", "Faro", "Guarda", "Leiria", "Lisboa", "Portalegre",
    "Porto", "Santarém", "Setúbal", "Viana do Castelo", "Vila Real", "Viseu",
)
DATASET_SEARCH = "https://dados.gov.pt/api/1/datasets/?q=SNID"
MAX_DOWNLOAD = 150 * 1024 * 1024
MAX_RECORD = 2 * 1024 * 1024
COLUMNS = (
    "distrito", "registos_com_distrito_declarado", "com_nome_e_coordenadas",
    "com_nome_coordenadas_e_modalidade", "por_rever", "estado",
)


def fold(value):
    value = unicodedata.normalize("NFD", str(value).strip())
    value = "".join(ch for ch in value if unicodedata.category(ch) != "Mn")
    return re.sub(r"\s+", " ", value.replace("_", " ").replace("-", " ")).lower()


DISTRICT_BY_FOLD = {fold(name): name for name in DISTRICTS}


class TrustedDadosRedirects(HTTPRedirectHandler):
    def redirect_request(self, req, fp, code, msg, headers, newurl):
        require_official_url(newurl)
        return super().redirect_request(req, fp, code, msg, headers, newurl)


def require_official_url(url):
    parsed = urlparse(url)
    if parsed.scheme != "https" or parsed.hostname != "dados.gov.pt" or parsed.username:
        raise ValueError("Only public HTTPS dados.gov.pt resources are permitted")
    return url


def official_opener():
    return build_opener(TrustedDadosRedirects())


def choose_source(dataset_response):
    """Do not mistake blank or old demo files for the latest actual export."""
    matches = []
    for dataset in dataset_response.get("data", []):
        if fold(dataset.get("title") or "") != "sistema nacional de informacao desportiva":
            continue
        for resource in dataset.get("resources", []):
            title = str(resource.get("title") or "")
            if not title.startswith("snid-localizacao") or fold(resource.get("format") or "") != "json":
                continue
            size = resource.get("filesize") or 0
            if not isinstance(size, (int, float)) or size <= 0:
                continue
            url = str(resource.get("url") or "")
            require_official_url(url)
            matches.append((title, url, int(size)))
    if not matches:
        raise ValueError("No nonempty official SNID location JSON resource found")
    return max(matches, key=lambda item: item[0])


def fetch_official_source(destination):
    opener = official_opener()
    with opener.open(Request(DATASET_SEARCH, headers={"User-Agent": "Jynlo-source-audit/1.0"}), timeout=30) as response:
        metadata = json.load(response)
    title, url, size = choose_source(metadata)
    if size > MAX_DOWNLOAD:
        raise ValueError("SNID resource exceeds safe download cap")
    destination.parent.mkdir(parents=True, exist_ok=True)
    tmp = destination.with_suffix(".downloading")
    total = 0
    sha256 = hashlib.sha256()
    try:
        with opener.open(Request(url, headers={"User-Agent": "Jynlo-source-audit/1.0"}), timeout=45) as stream, tmp.open("wb") as output:
            while chunk := stream.read(64 * 1024):
                total += len(chunk)
                if total > MAX_DOWNLOAD:
                    raise ValueError("SNID resource exceeded the download cap")
                output.write(chunk)
                sha256.update(chunk)
        if total == 0:
            raise ValueError("Downloaded source was empty")
        tmp.replace(destination)
    finally:
        if tmp.exists():
            tmp.unlink()
    return {"resource": title, "source_url": url, "bytes": total, "sha256": sha256.hexdigest()}


def iter_json_objects(path):
    """Bounded incremental JSON array / adjacent-object parser (also handles NDJSON)."""
    decoder = json.JSONDecoder()
    with Path(path).open("r", encoding="utf-8") as stream:
        data = ""
        eof = False
        started = False
        array = False
        seen = False
        expect_separator = False
        while True:
            data = data.lstrip()
            if not data and not eof:
                part = stream.read(64 * 1024)
                eof = not part
                data += part
                continue
            if not started and data:
                started = True
                if data.startswith("["):
                    array = True
                    data = data[1:]
                    continue
            if array and data.startswith("]"):
                if data[1:].strip() or stream.read(1).strip():
                    raise ValueError("Trailing content after SNID JSON array")
                return
            if not data:
                if eof and not array:
                    return
                raise ValueError("Truncated SNID JSON array")
            if array and expect_separator:
                if data.startswith(","):
                    data = data[1:]
                    expect_separator = False
                    continue
                if not eof and len(data) < 2:
                    data += stream.read(64 * 1024)
                    continue
                raise ValueError("Missing delimiter between SNID records")
            try:
                record, end = decoder.raw_decode(data)
            except json.JSONDecodeError as ex:
                if eof:
                    raise ValueError("Invalid or truncated SNID record") from ex
                if len(data) > MAX_RECORD:
                    raise ValueError("Oversized or invalid SNID record") from ex
                part = stream.read(64 * 1024)
                eof = not part
                data += part
                continue
            if not isinstance(record, dict):
                raise ValueError("SNID records must be JSON objects")
            yield record
            data = data[end:]
            seen = True
            expect_separator = array


def source_district(record):
    address = record.get("address")
    if not isinstance(address, dict):
        address = {}
    raw = address.get("district") or record.get("district")
    if isinstance(raw, dict):
        raw = raw.get("name") or raw.get("designacao")
    return DISTRICT_BY_FOLD.get(fold(raw)) if raw is not None else None


def coordinates(record):
    location = record.get("location")
    if isinstance(location, str):
        try:
            location = json.loads(location)
        except json.JSONDecodeError:
            return None
    if not isinstance(location, dict) or location.get("type") != "Point":
        return None
    point = location.get("coordinates")
    if not isinstance(point, list) or len(point) != 2:
        return None
    lon, lat = point
    if any(isinstance(x, bool) or not isinstance(x, (float, int))
           or not math.isfinite(x) for x in (lat, lon)):
        return None
    if not (-180 <= lon <= 180 and -90 <= lat <= 90):
        return None
    return lat, lon


def has_source_sports(record):
    sports = record.get("sports")
    if isinstance(sports, str):
        return bool(sports.strip())
    if isinstance(sports, list):
        return any(bool(value) for value in sports)
    if isinstance(sports, dict):
        return any(bool(value) for value in sports.values())
    return False


def analyse(records):
    counts = {name: dict.fromkeys(COLUMNS[1:5], 0) for name in DISTRICTS}
    summary = dict(total_records=0, named_with_coordinates=0, mainland_bbox_candidates=0,
                   outside_mainland_bbox=0, missing_or_unknown_district=0,
                   sports_in_named_geolocated=0, excluded_institutions=0)
    for record in records:
        summary["total_records"] += 1
        category = record.get("category")
        if isinstance(category, dict):
            category = category.get("name", "")
        if "instituicao desportiva" in fold(category or ""):
            summary["excluded_institutions"] += 1
            continue
        point = coordinates(record)
        named = bool(str(record.get("name") or "").strip())
        if named and point:
            summary["named_with_coordinates"] += 1
            if has_source_sports(record):
                summary["sports_in_named_geolocated"] += 1
        # This rectangular screening is NOT a CAOP polygon / municipality match.
        if not point or not (36.8 <= point[0] <= 42.3 and -9.6 <= point[1] <= -6.0):
            summary["outside_mainland_bbox"] += 1
            continue
        summary["mainland_bbox_candidates"] += 1
        district = source_district(record)
        if district is None:
            summary["missing_or_unknown_district"] += 1
            continue
        row = counts[district]
        row["registos_com_distrito_declarado"] += 1
        if named:
            row["com_nome_e_coordenadas"] += 1
            if has_source_sports(record):
                row["com_nome_coordenadas_e_modalidade"] += 1
                continue
        row["por_rever"] += 1
    rows = []
    for district in DISTRICTS:
        rows.append({"distrito": district, **counts[district],
                     "estado": "candidatos SNID; distrito declarado, não validado CAOP"})
    return rows, summary


def audit(source, output_dir, provenance=None):
    rows, summary = analyse(iter_json_objects(source))
    if summary["total_records"] == 0:
        raise ValueError("SNID data is empty; refusing to publish an all-zero coverage report")
    output_dir = Path(output_dir)
    output_dir.mkdir(parents=True, exist_ok=True)
    with (output_dir / "snid-distritos.csv").open("w", encoding="utf-8-sig", newline="") as handle:
        writer = csv.DictWriter(handle, fieldnames=COLUMNS, delimiter=";")
        writer.writeheader()
        writer.writerows(rows)
    summary.update({
        "source_kind": "official SNID location JSON",
        "source_license": "CC BY 4.0 — IPDJ",
        "scope": "read-only source audit; coordinates screened by a bounding box only",
        "caveat": "Not a count of confirmed, publicly accessible or imported Jynlo venues.",
        "generated_at": datetime.now(timezone.utc).isoformat(),
    })
    if provenance:
        summary["provenance"] = provenance
    (output_dir / "snid-resumo.json").write_text(
        json.dumps(summary, ensure_ascii=False, indent=2), encoding="utf-8")
    return rows, summary


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--input", type=Path, help="A previously downloaded SNID JSON")
    parser.add_argument("--fetch-official", action="store_true",
                        help="Download latest nonempty SNID JSON from dados.gov.pt")
    parser.add_argument("--output-dir", required=True, type=Path)
    args = parser.parse_args()
    if bool(args.input) == bool(args.fetch_official):
        parser.error("Choose exactly one of --input or --fetch-official")
    provenance = None
    source = args.input
    if args.fetch_official:
        source = args.output_dir / "snid-source-private.json"
        provenance = fetch_official_source(source)
    rows, summary = audit(source, args.output_dir, provenance)
    if args.fetch_official:
        source.unlink(missing_ok=True)
    print(f"SNID source records: {summary['total_records']}; mainland bbox candidates: "
          f"{summary['mainland_bbox_candidates']}; "
          f"district labels to review: {summary['missing_or_unknown_district']}")
    print(f"Source-only coverage output: {args.output_dir}; 18 rows; no Jynlo writes")


if __name__ == "__main__":
    main()

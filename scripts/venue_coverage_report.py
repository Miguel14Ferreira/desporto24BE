#!/usr/bin/env python3
"""Create a district-by-district CSV from a national OSM dry-run report.

Only reports source coverage and data quality; it never claims that every venue
is catalogued or that a venue has been approved, imported or is open to public.
No network, cloud provider or database credentials required.
"""
from __future__ import annotations

import argparse
import csv
import json
from pathlib import Path

from import_osm_venues_national import DISTRICTS

COLUMNS = [
    "distrito", "estado", "candidatos_osm", "identificados",
    "aptos_para_revisao_final", "em_revisao", "sem_nome",
    "sem_concelho", "sem_modalidade", "distrito_por_confirmar",
    "noutro_distrito", "excluidos_acesso_restrito",
]
COUNT_FIELDS = {
    "candidatos_osm": "candidates",
    "identificados": "prepared",
    "aptos_para_revisao_final": "import_ready",
    "em_revisao": "review_required",
    "sem_nome": "name_to_verify",
    "sem_concelho": "municipality_to_verify",
    "sem_modalidade": "sports_to_verify",
    "distrito_por_confirmar": "district_to_verify",
    "noutro_distrito": "skipped_other_district",
    "excluidos_acesso_restrito": "skipped_restricted_access",
}


def extract(data):
    if not isinstance(data, dict):
        raise ValueError("Report must be a JSON object")
    result = []
    unknown = set(data) - set(DISTRICTS) - {"summary"}
    if unknown:
        raise ValueError(f"Unknown district slugs in report: {sorted(unknown)}")
    for slug, display_name in DISTRICTS.items():
        info = data.get(slug)
        if info is None:
            result.append(dict.fromkeys(COLUMNS, ""))
            result[-1].update(distrito=display_name, estado="não processado")
            continue
        if not isinstance(info, dict):
            raise ValueError(f"Invalid district stats: {slug}")
        if info.get("status") in ("failed", "legacy_unverified"):
            row = dict.fromkeys(COLUMNS, "")
            row.update(distrito=display_name, estado=(
                "falha na recolha" if info["status"] == "failed"
                else "recolha antiga, sem verificação CAOP"))
            result.append(row)
            continue
        values = {}
        for column, key in COUNT_FIELDS.items():
            value = info.get(key, 0)
            if isinstance(value, bool) or not isinstance(value, int) or value < 0:
                raise ValueError(f"{slug}.{key} must be a nonnegative integer")
            values[column] = value
        if values["identificados"] != values["aptos_para_revisao_final"] + values["em_revisao"]:
            raise ValueError(f"{slug}: invalid prepared/review counts")
        if values["candidatos_osm"] < values["identificados"]:
            raise ValueError(f"{slug}: candidate count below prepared count")
        status = "pré-visualização (aguarda confirmação e aprovação)"
        if values["distrito_por_confirmar"]:
            status = "distrito oficial por confirmar"
        result.append({"distrito": display_name, "estado": status, **values})
    return result


def write_report(source, destination):
    data = json.loads(Path(source).read_text(encoding="utf-8"))
    rows = extract(data)
    out = Path(destination)
    out.parent.mkdir(parents=True, exist_ok=True)
    with out.open("w", encoding="utf-8-sig", newline="") as handle:
        writer = csv.DictWriter(handle, fieldnames=COLUMNS, delimiter=";")
        writer.writeheader()
        writer.writerows(rows)
    return sum(
        isinstance(data.get(slug), dict)
        and data[slug].get("status", "processed") == "processed"
        for slug in DISTRICTS), out


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--input", type=Path, required=True,
                        help="national-report.json created by the OSM importer")
    parser.add_argument("--output", type=Path, required=True,
                        help="CSV destination; does not alter source or database")
    args = parser.parse_args()
    count, path = write_report(args.input, args.output)
    print(f"Prepared district quality report: {count}/18 districts checked -> {path}")
    print("Caution: candidates are raw OSM map objects, NOT verified or imported venues.")


if __name__ == "__main__":
    main()

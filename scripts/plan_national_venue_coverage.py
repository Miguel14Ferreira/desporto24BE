#!/usr/bin/env python3
"""Offline coverage checklist for Portugal. This script makes NO network calls."""
import argparse
import json
import unicodedata
from pathlib import Path

DISTRICTS = (
    "Aveiro", "Beja", "Braga", "Bragança", "Castelo Branco", "Coimbra",
    "Évora", "Faro", "Guarda", "Leiria", "Lisboa", "Portalegre",
    "Porto", "Santarém", "Setúbal", "Viana do Castelo", "Vila Real", "Viseu",
)
REGIONS = ("Região Autónoma dos Açores", "Região Autónoma da Madeira")
AREAS = DISTRICTS + REGIONS
UNKNOWN = "(sem distrito)"

def normalized(text):
    plain = unicodedata.normalize("NFKD", str(text or "").strip())
    without_accents = "".join(c for c in plain if not unicodedata.combining(c))
    return " ".join(without_accents.casefold().split())

ALIASES = {normalized(name): name for name in AREAS}
ALIASES.update({"acores": REGIONS[0], "madeira": REGIONS[1]})

def count(record, field):
    value = record.get(field)
    if type(value) is not int or value < 0:
        raise ValueError(f"Invalid '{field}' count: {value!r}")
    return value

def build_plan(coverage, pilot_area=None):
    if not isinstance(coverage, dict) or not isinstance(coverage.get("districts"), list):
        raise ValueError("Expected JSON from GET /api/admin/venues/coverage")
    total = count(coverage, "totalVenues")
    grouped = {area: {"venues": 0, "missingCoordinates": 0, "missingSports": 0,
                      "openStreetMapVenues": 0, "rawLabels": []} for area in AREAS}
    unknown = []
    unclassified = {"venues": 0, "missingCoordinates": 0, "missingSports": 0,
                    "openStreetMapVenues": 0, "rawLabels": []}
    seen = 0
    for record in coverage["districts"]:
        raw = str(record.get("district") or "").strip()
        label = ALIASES.get(normalized(raw))
        if label is None:
            bucket = unclassified
            if raw and raw != UNKNOWN and raw not in unknown:
                unknown.append(raw)
        else:
            bucket = grouped[label]
        for field in ("venues", "missingCoordinates", "missingSports", "openStreetMapVenues"):
            value = count(record, field)
            if field != "venues" and value > count(record, "venues"):
                raise ValueError(f"{field} exceeds venue count in {raw!r}")
            bucket[field] += value
        bucket["rawLabels"].append(raw or UNKNOWN)
        seen += count(record, "venues")
    if seen != total:
        raise ValueError(f"District totals ({seen}) differ from overall catalog ({total})")
    if unclassified["venues"] != count(coverage, "withoutDistrict"):
        # It may also include noncanonical district labels. Keep both visible
        # rather than silently assigning cities to administrative regions.
        unclassified["reportedWithoutDistrict"] = count(coverage, "withoutDistrict")
    def entry(name, group):
        return {
            "area": name,
            "catalogVenues": group["venues"],
            "missingCoordinates": group["missingCoordinates"],
            "missingSports": group["missingSports"],
            "osmVenues": group["openStreetMapVenues"],
            "rawLabels": group["rawLabels"],
            "review": "Verificar fontes e qualidade" if group["venues"] else "Sem registos classificados; investigar",
            "complete": False,  # Zero or many catalog rows never proves nationwide coverage.
        }
    chosen = None
    if pilot_area is not None:
        chosen = ALIASES.get(normalized(pilot_area))
        if chosen is None:
            raise ValueError("Pilot area must be an explicit district or autonomous region")
    return {
        "source": "GET /api/admin/venues/coverage (saved JSON; no network)",
        "catalogTotal": total,
        "classifiedTotal": total - unclassified["venues"],
        "unclassified": entry("Sem distrito ou designação não reconhecida", unclassified),
        "unrecognizedDistrictLabels": unknown,
        "areas": [entry(name, grouped[name]) for name in AREAS],
        "pilot": None if chosen is None else {
            "area": chosen,
            "status": "APENAS PREPARAÇÃO",
            "bbox": None,
            "existingCatalogVenues": grouped[chosen]["venues"],
            "requiresBeforeAnyFetch": [
                "Validar fontes e licenças por concelho",
                "Definir/validar BBOX pequeno por fonte cartográfica",
                "Rever duplicados e campos obrigatórios",
                "Executar simulação OSM sem credenciais de escrita",
                "Avaliar plano SQL e validação de carga em PostgreSQL de teste",
            ],
            "authorizeProductionWrite": False
        },
        "warning": "Isto não prova cobertura real de Portugal; 0 registos não significa 0 recintos."
    }

def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--coverage", type=Path, required=True, help="JSON guardado do endpoint admin de leitura")
    parser.add_argument("--out", type=Path, required=True, help="Destino local JSON (não faz uploads)")
    parser.add_argument("--pilot", default=None, help="Distrito ou região autónoma, sem escolha automática")
    args = parser.parse_args()
    data = json.loads(args.coverage.read_text(encoding="utf-8"))
    plan = build_plan(data, args.pilot)
    args.out.write_text(json.dumps(plan, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")
    print(f"Plano offline: {len(plan['areas'])} áreas, {plan['catalogTotal']} recintos registados.")
    print(f"Sem classificação fiável: {plan['unclassified']['catalogVenues']}.")
    print("Não houve pedidos HTTP, Overpass, importações ou escritas à base de dados.")
    return 0

if __name__ == "__main__":
    raise SystemExit(main())

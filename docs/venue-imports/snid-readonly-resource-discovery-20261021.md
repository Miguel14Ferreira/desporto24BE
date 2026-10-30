# SNID read-only resource discovery hardening (issue #155)

The official dados.gov.pt page for “Sistema Nacional de Informação Desportiva” (IPDJ) was updated **9 October 2026**, but its existence does **not** prove the presence of the specific public `snid-localizacao*.json` resource. A live backend startup on 21 October logged `SNID location JSON resource not found`.

This patch makes only the *metadata selection/validation* more robust:
- Normalize accents, case, punctuation and separators when matching known SNID dataset/resource titles.
- Accept missing file-size metadata, but reject a resource explicitly reporting zero/negative size.
- Require genuine JSON format/MIME/extension and **HTTPS on dados.gov.pt exactly**; no user info, port exceptions, fragments, third-party mirrors, demos, or `part-*.json` fallback.
- If no official location resource is identified, return a clear **`SOURCE_NOT_AVAILABLE`**, `available:false`, `readOnly:true` result to the read-only validation endpoint/startup instead of treating it as application failure.
- **No import flags changed**. `desporto.venues.snid.import-enabled` continues off by default. No data writes, no recintos seeded, no migrations or Render deployment.
- Synthetic JUnit tests cover missing/malformed metadata, aliases, empty resources, domain and scheme filtering and explicit unavailable status, with no network usage.

**Limitations:** This does not prove that an official SNID download currently exists. If the portal now only exposes chunked data, new data sources or a different format, adapt ingestion only after reviewing provenance, license, JSON structure and duplicate handling. The OpenStreetMap candidate review process remains independent.

Official catalog: https://dados.gov.pt/pt/datasets/sistema-nacional-de-informacao-desportiva

Avoid another paid Render build solely for a nonfatal source-validation warning.

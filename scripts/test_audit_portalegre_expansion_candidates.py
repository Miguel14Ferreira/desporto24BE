"""Offline regression tests for 33 documented Portalegre candidate sources."""
import json
from pathlib import Path
import sys
import unittest

sys.path.insert(0, str(Path(__file__).resolve().parent))
from audit_portalegre_expansion_candidates import (
    audit, evaluate, records_from_manifests,
)


def source(name="Estádio Municipal de Gáfete", city="Crato", lat=None, lon=None):
    return {
        "name": name, "city": city, "district": "Portalegre",
        "sourceBatch": "test", "sourceReference": "OSM:WAY:1",
        "sourceUrl": "https://cm-crato.pt/locais/estadio-municipal-do-gafete/",
        "latitude": lat, "longitude": lon, "approvedForImport": False,
    }


def live(id_, name, city="Crato", district="Portalegre", **extra):
    return {
        "id": id_, "name": name, "city": city, "district": district,
        "contact": "+351999111999", **extra,
    }


class PortalegreNationalCoverageTest(unittest.TestCase):
    def test_repository_official_sources_are_33_distinct_unapproved(self):
        directory = Path(__file__).resolve().parent.parent / "docs/venue-imports"
        manifests = [json.loads((directory / n).read_text(encoding="utf-8")) for n in (
            "portalegre-2026-10-19-batch2-corroborated.json",
            "portalegre-11-municipal-sources-20261019.json",
            "portalegre-2026-10-19-source-review.json",
        )]
        result = records_from_manifests(*manifests)
        self.assertEqual(33, len(result))
        self.assertEqual(33, len(set(x["sourceReference"] for x in result)))
        self.assertTrue(all(x["approvedForImport"] is False for x in result))

    def test_exact_name_and_same_city_is_existing(self):
        row = evaluate(source(), [live(18, "Estádio Municipal de Gáfete")])
        self.assertEqual("EXACT_NAME_CITY_EXISTING", row["status"])
        self.assertEqual([18], [x["id"] for x in row["matches"]])

    def test_same_name_other_city_is_not_exact(self):
        row = evaluate(source(), [live(18, "Estádio Municipal de Gáfete",
                                        city="Portalegre")])
        self.assertEqual("POSSIBLE_ALIAS_OR_GPS_REVIEW", row["status"])

    def test_other_stadium_same_city_is_not_automatically_existing(self):
        row = evaluate(source(), [live(18, "Estádio Municipal do Crato")])
        self.assertNotEqual("EXACT_NAME_CITY_EXISTING", row["status"])
        self.assertFalse(row["approvedForImport"])

    def test_nearby_different_names_are_reviewed_not_merged(self):
        candidate = source("Campo de Jogos João Manuel Nabeiro", "Campo Maior",
                           lat=39.0, lon=-7.0)
        row = evaluate(candidate, [live(
            3, "Complexo desportivo Campo Maior", city="Campo Maior",
            latitude=39.0005, longitude=-7.0)])
        self.assertEqual("POSSIBLE_ALIAS_OR_GPS_REVIEW", row["status"])
        self.assertIsInstance(row["matches"][0]["distanceMeters"], int)

    def test_clear_unmatched_not_approved(self):
        row = evaluate(source(), [live(1, "Ginásio ABC", city="Avis")])
        self.assertEqual("NO_PLAUSIBLE_PUBLIC_MATCH_REVIEW", row["status"])
        self.assertFalse(row["approvedForImport"])
        self.assertFalse(row["sourceReferenceCheckedInDatabase"])

    def test_never_expose_contact_and_reject_duplicate_venue_ids(self):
        result = audit([live(9, "Estádio Municipal de Gáfete")], [source()])
        self.assertNotIn("+351999111999", json.dumps(result))
        with self.assertRaises(ValueError):
            audit([live(9, "A"), live(9, "B")], [source()])

    def test_district_mismatch_does_not_match(self):
        row = evaluate(source(), [live(20, "Estádio Municipal de Gáfete",
                                       district="Braga")])
        self.assertEqual("NO_PLAUSIBLE_PUBLIC_MATCH_REVIEW", row["status"])

    def test_live_catalogue_cannot_approve_any_candidate(self):
        result = audit([], [source("Campo de Futebol de Cano", "Cano")])
        self.assertEqual(0, result["catalogueTotal"])
        self.assertFalse(result["candidates"][0]["approvedForImport"])


if __name__ == "__main__":
    unittest.main()

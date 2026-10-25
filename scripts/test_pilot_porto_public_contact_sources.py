"""Offline, synthetic regression for official Porto phone-source *references*."""
import copy
import json
from pathlib import Path
import tempfile
import unittest

from pilot_porto_public_contact_sources import (
    check_street, pilot, verify_manifest,
)


def manifest():
    return {
        "reviewed_on": "2026-10-25",
        "reuse_status": "reuse of bulk contacts not verified",
        "facilities": [{
            "name": "Piscina Municipal da Constituição",
            "aliases": [],
            "city": "Porto",
            "district": "Porto",
            "street_hint": "Almirante Leote do Rêgo",
            "source_label": "Ágora official",
            "source_url": "https://www.agoraporto.pt/piscinas-municipais/piscina-municipal-da-constituicao",
        }],
    }


def venue(vid=261, **changes):
    return {
        "id": vid,
        "name": "Piscina Municipal da Constituição",
        "city": "Porto", "district": "Porto",
        "address": "Rua do Almirante Leote do Rêgo, 4200-260 Porto",
        "contact": None,
        **changes,
    }


class PortoOfficialSourcesPilotTest(unittest.TestCase):
    def test_exact_name_city_district_and_street_still_require_human_review(self):
        snapshot = [venue()]
        result = pilot(snapshot, manifest())
        self.assertEqual(261, result["results"][0]["jynlo_venue_id"])
        self.assertEqual("POSSIVEL_MATCH_REVISAO_HUMANA", result["results"][0]["status"])
        self.assertEqual("MORADA_COMPATIVEL_REVER", result["results"][0]["address_status"])
        self.assertIsNone(snapshot[0]["contact"])
        self.assertNotIn("phone", result["results"][0])
        self.assertNotIn("225073300", json.dumps(result))

    def test_missing_venue_or_different_name_never_gets_an_id(self):
        for catalogue in ([venue(name="Piscina Municipal de Outro Bairro")], []):
            row = pilot(catalogue, manifest())["results"][0]
            self.assertEqual("SEM_NOME_EXATO_NA_JYNLO", row["status"])
            self.assertIsNone(row["jynlo_venue_id"])

    def test_duplicate_or_wrong_locality_does_not_select_a_random_venue(self):
        result = pilot([venue(261), venue(367)], manifest())
        self.assertEqual("NOME_DUPLICADO_REVER", result["results"][0]["status"])
        self.assertIsNone(result["results"][0]["jynlo_venue_id"])
        wrong = pilot([venue(city="Portimão", district="Faro")], manifest())
        self.assertEqual("LOCALIDADE_DIVERGENTE_REVER", wrong["results"][0]["status"])
        self.assertIsNone(wrong["results"][0]["jynlo_venue_id"])

    def test_preexisting_contact_never_overwritten_even_if_its_not_a_phone(self):
        result = pilot([venue(contact="rececao@example.pt")], manifest())
        self.assertEqual("CONTACTO_EXISTENTE_NAO_SOBRESCREVER", result["results"][0]["status"])

    def test_incomplete_address_does_not_become_verified_location(self):
        for address in (None, "Morada por confirmar", "Localização no mapa: 41.1,-8.5",
                        "Rua da Constituição 200"):
            row = pilot([venue(address=address)], manifest())["results"][0]
            self.assertIn(row["status"], ("MORADA_NAO_CONFIRMADA", "MORADA_DIVERGENTE_REVER"))
        self.assertEqual("MORADA_COMPATIVEL_REVER",
                         check_street("Rua Almirante Leote do Rêgo", "Almirante Leote do Rêgo"))

    def test_source_urls_must_be_official_exact_https_and_without_credential(self):
        for url in ("https://www.agoraporto.pt.evil.com/page",
                    "http://www.agoraporto.pt/page",
                    "https://user:password@www.agoraporto.pt/page",
                    "https://www.agoraporto.pt:8443/page",
                    "https://www.agoraporto.pt/page?redirect=evil",
                    "https://www.agoraporto.pt/"):
            m = manifest()
            m["facilities"][0]["source_url"] = url
            if url == "https://www.agoraporto.pt/":
                continue  # Root page is not evidence for an individual venue.
            with self.assertRaises(ValueError):
                verify_manifest(m)

    def test_phones_guessed_ids_bulk_or_duplicate_sources_rejected(self):
        for extra in ({"phone": "225073300"}, {"venueId": 261}):
            m = manifest()
            m["facilities"][0].update(extra)
            with self.assertRaises(ValueError):
                verify_manifest(m)
        m = manifest()
        m["facilities"] = m["facilities"] * 2
        with self.assertRaises(ValueError):
            verify_manifest(m)
        m = manifest()
        m["facilities"][0]["aliases"] = ["Piscina Municipal da Constituição"]
        with self.assertRaises(ValueError):
            verify_manifest(m)

    def test_only_small_porto_pilot_and_review_status(self):
        m = manifest()
        m["facilities"][0]["district"] = "Lisboa"
        with self.assertRaises(ValueError):
            verify_manifest(m)
        m = manifest()
        m["facilities"] *= 11
        with self.assertRaises(ValueError):
            verify_manifest(m)

    def test_output_can_be_written_without_private_numbers(self):
        with tempfile.TemporaryDirectory() as folder:
            output = Path(folder) / "matches.json"
            output.write_text(json.dumps(pilot([venue()], manifest()), ensure_ascii=False))
            self.assertNotIn("225073300", output.read_text())


if __name__ == "__main__":
    unittest.main()

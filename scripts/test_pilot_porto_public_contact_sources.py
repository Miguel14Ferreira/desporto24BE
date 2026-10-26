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


    def test_manifest_rejects_unexpected_contact_fields_at_every_level(self):
        for field in ("telefone", "contact_phone", "contact:phone", "phone_number",
                      "contactPhone", "venueId"):
            m = manifest()
            m["facilities"][0][field] = "not-to-be-stored"
            with self.subTest(field=field), self.assertRaises(ValueError):
                verify_manifest(m)
        m = manifest()
        m["telephone"] = "not-to-be-stored"
        with self.assertRaises(ValueError):
            verify_manifest(m)

    def test_secondary_source_requires_official_https_without_credentials(self):
        m = manifest()
        m["facilities"][0]["secondary_source_url"] = (
            "https://desporto.agoraporto.pt/Desporto/Centers")
        self.assertEqual(m["facilities"], verify_manifest(m))
        for url in (
            "http://desporto.agoraporto.pt/Desporto/Centers",
            "https://desporto.agoraporto.pt.evil.test/Desporto/Centers",
            "https://user:secret@desporto.agoraporto.pt/Desporto/Centers",
            "https://desporto.agoraporto.pt:8443/Desporto/Centers",
            "https://desporto.agoraporto.pt/Desporto/Centers?q=1",
            "https://desporto.agoraporto.pt/Desporto/Centers#location",
        ):
            with self.subTest(url=url):
                bad = copy.deepcopy(m)
                bad["facilities"][0]["secondary_source_url"] = url
                with self.assertRaises(ValueError):
                    verify_manifest(bad)

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

    def test_distinctive_name_hints_are_read_only_and_not_assigned(self):
        m = manifest()
        m["facilities"][0].update({
            "name": "Piscina Municipal Eng.º Armando Pimentel",
            "aliases": ["Piscina Municipal Engenheiro Armando Pimentel"],
            "street_hint": "Diogo Botelho",
        })
        candidate = venue(404, name="Centro de Natação Armando Pimentel",
                          address="Rua Diogo Botelho, Porto")
        result = pilot([candidate], m)
        row = result["results"][0]
        self.assertEqual("SEM_NOME_EXATO_NA_JYNLO", row["status"])
        self.assertIsNone(row["jynlo_venue_id"])
        self.assertEqual([{"id": 404, "name": candidate["name"],
                           "street_review": "MORADA_COMPATIVEL_REVER",
                           "has_existing_contact": False}], row["manual_name_hints"])
        self.assertNotIn("225073300", json.dumps(result))

    def test_name_hints_reject_wrong_city_and_too_many_ambiguous_matches(self):
        m = manifest()
        m["facilities"][0].update({"name": "Piscina Eng.º Armando Pimentel",
                                   "aliases": []})
        same = venue(100, name="Clube Armando Pimentel", address=None)
        foreign = venue(200, name="Clube Armando Pimentel",
                        city="Vila Nova de Gaia", district="Porto")
        row = pilot([same, foreign], m)["results"][0]
        self.assertEqual([{"id": 100, "name": same["name"],
                           "street_review": "MORADA_NAO_CONFIRMADA",
                           "has_existing_contact": False}], row["manual_name_hints"])
        too_many = [venue(500 + i, name="Piscina Armando Pimentel " + str(i))
                    for i in range(4)]
        row = pilot(too_many, m)["results"][0]
        self.assertEqual([], row["manual_name_hints"])
        self.assertIsNone(row["jynlo_venue_id"])

    def test_generic_names_never_produce_name_hints(self):
        row = pilot([venue(999, name="Piscina Municipal do Porto")], manifest())["results"][0]
        self.assertEqual([], row["manual_name_hints"])

    def test_name_hints_never_expose_existing_contact_values(self):
        m = manifest()
        m["facilities"][0].update({
            "name": "Piscina Eng.º Armando Pimentel",
            "aliases": [], "street_hint": "Diogo Botelho",
        })
        candidate = venue(2655, name="Piscinas Engenheiro Armando Pimentel",
                          address="Rua de Diogo Botelho, 4150-262 Porto",
                          contact="226179598")
        result = pilot([candidate], m)
        row = result["results"][0]
        self.assertIsNone(row["jynlo_venue_id"])
        self.assertEqual("SEM_NOME_EXATO_NA_JYNLO", row["status"])
        self.assertEqual("MORADA_COMPATIVEL_REVER",
                         row["manual_name_hints"][0]["street_review"])
        self.assertTrue(row["manual_name_hints"][0]["has_existing_contact"])
        self.assertNotIn("226179598", json.dumps(result))
        self.assertEqual("226179598", candidate["contact"])  # source unchanged

    def test_name_hints_leave_location_unverified_with_gps_placeholder(self):
        m = manifest()
        m["facilities"][0].update({"name": "Piscina Armando Pimentel",
                                    "aliases": [], "street_hint": "Diogo Botelho"})
        candidate = venue(2655, name="Piscinas Engenheiro Armando Pimentel",
                          address="Localização no mapa: 41.2,-8.7")
        row = pilot([candidate], m)["results"][0]
        self.assertIsNone(row["jynlo_venue_id"])
        self.assertEqual("MORADA_NAO_CONFIRMADA",
                         row["manual_name_hints"][0]["street_review"])


if __name__ == "__main__":
    unittest.main()

"""Offline safeguards for the 13-facility municipal coverage audit."""
import json
from pathlib import Path
import sys
import unittest

sys.path.insert(0, str(Path(__file__).resolve().parent))
from audit_municipal_expansion_candidates import compare, distance_meters, read_manifest


def entry(name, city="Santarém", district="Santarém", **extra):
    return {
        "name": name, "city": city, "district": district,
        "sourceUrl": "https://viversantarem.pt/instalacoes-desportivas/",
        "approvedForImport": False, **extra,
    }


def manifest(one):
    return {"recordCount": 13, "venues": [one, *[
        entry(f"Outro recinto fictício {i}") for i in range(12)
    ]]}


def venue(id_, name, city="Santarém", district="Santarém", **extra):
    return {
        "id": id_, "name": name, "city": city, "district": district,
        "contact": "912345678", **extra,
    }


class MunicipalExpansionAuditTest(unittest.TestCase):
    def test_exact_match_is_existing_not_approved(self):
        source = entry("Piscinas Municipais do Sacapeito")
        result = compare([venue(276, "Piscinas Municipais do Sacapeito")], manifest(source))
        first = result["results"][0]
        self.assertEqual("EXACT_EXISTING", first["status"])
        self.assertEqual([276], [x["id"] for x in first["existing"]])
        self.assertFalse(first["approvedForImport"])
        self.assertNotIn("912345678", json.dumps(result))

    def test_alias_same_city_is_held_for_review(self):
        source = entry("Estádio Municipal de Castelo Branco",
                       city="Castelo Branco", district="Castelo Branco",
                       sourceUrl="https://www.cm-castelobranco.pt/visitante/contactos-uteis/")
        result = compare([venue(1186, "Estádio Municipal Vale do Romeiro",
                                city="Castelo Branco", district="Castelo Branco")],
                         manifest(source))
        self.assertEqual("POSSIBLE_ALIAS_REVIEW", result["results"][0]["status"])

    def test_different_city_district_not_duplicate(self):
        result = compare([venue(50, "Piscinas Municipais do Sacapeito",
                                city="Porto", district="Porto")],
                         manifest(entry("Piscinas Municipais do Sacapeito")))
        self.assertEqual("NO_NAME_MATCH_REVIEW", result["results"][0]["status"])

    def test_close_coordinates_are_only_a_review_hint(self):
        source = entry("Outra Designação", location={
            "latitude": 39.2641, "longitude": -8.66945,
        })
        result = compare([venue(275, "Centro Aquático", latitude=39.2641,
                                longitude=-8.66945)], manifest(source))
        self.assertEqual("POSSIBLE_ALIAS_REVIEW", result["results"][0]["status"])

    def test_bad_input_fails_closed(self):
        m = manifest(entry("Recinto"))
        m["venues"][0]["approvedForImport"] = True
        with self.assertRaises(ValueError):
            read_manifest(m)
        m["venues"][0]["approvedForImport"] = False
        m["venues"][0]["sourceUrl"] = "https://viversantarem.pt.evil.test/path"
        with self.assertRaises(ValueError):
            read_manifest(m)
        with self.assertRaises(ValueError):
            compare([venue(1, "Test"), venue(1, "Other")],
                    manifest(entry("Recinto")))

    def test_real_source_list_remains_unapproved_and_no_contact_needed(self):
        path = Path(__file__).resolve().parent.parent / "docs/venue-imports/other-districts-13-official-facilities-20261019.json"
        src = json.loads(path.read_text(encoding="utf-8"))
        self.assertEqual(13, len(read_manifest(src)))
        self.assertTrue(all(x["approvedForImport"] is False for x in src["venues"]))
        self.assertIsNone(distance_meters({"location": None}, venue(1, "Unknown")))


if __name__ == "__main__":
    unittest.main()

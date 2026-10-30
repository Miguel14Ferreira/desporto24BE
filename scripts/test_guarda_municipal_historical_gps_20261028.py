"""Offline immutable-source checks for Guarda 2017 historical municipal site GPS."""
import json
from pathlib import Path
import math
import unittest

ROOT = Path(__file__).resolve().parents[1]
SOURCE = ROOT / "docs/venue-imports/guarda-municipal-historical-gps-evidence-20261028.json"
ORIGINAL = {
    "Piscinas Municipais da Guarda": (40.531324, -7.254329),
    "Estádio Municipal da Guarda": (40.536435, -7.266712),
    "Pavilhão Desportivo Municipal de São Miguel": (40.548919, -7.252073),
}
SOURCE_URL = "https://cdn.mun-guarda.pt/WS/03-154889474611829"

def haversine(a, b, c, d):
    p1, p2 = math.radians(a), math.radians(c)
    x, y = math.radians(c - a), math.radians(d - b)
    return 2 * 6371000 * math.asin(math.sqrt(
        math.sin(x / 2) ** 2 + math.cos(p1) * math.cos(p2) * math.sin(y / 2) ** 2
    ))

class HistoricalGuardaGpsTests(unittest.TestCase):
    def test_exact_historical_source_and_no_import_authorization(self):
        data = json.loads(SOURCE.read_text(encoding="utf-8"))
        self.assertEqual(SOURCE_URL, data["sourceUrl"])
        self.assertEqual("2017-07-10", data["sourceMeetingDate"])
        self.assertEqual("HISTORICAL_SITE_LOCATION_EVIDENCE_ONLY", data["status"])
        self.assertIs(data["approvedForImport"], False)
        self.assertIs(data["coordinatesAreEntrances"], False)
        self.assertEqual(3, len(data["venues"]))
        self.assertEqual(set(ORIGINAL), {v["name"] for v in data["venues"]})
        for venue in data["venues"]:
            with self.subTest(venue=venue["name"]):
                self.assertEqual(ORIGINAL[venue["name"]],
                                 (venue["latitude"], venue["longitude"]))
                self.assertEqual("2017_MUNICIPAL_ROUND_SITE_NOT_ENTRANCE",
                                 venue["precision"])
                self.assertNotIn("approvedForImport", venue)
                self.assertTrue(40.5 < venue["latitude"] < 40.6)
                self.assertTrue(-7.3 < venue["longitude"] < -7.2)

    def test_public_pool_location_conflict_must_not_be_silently_resolved(self):
        data = json.loads(SOURCE.read_text(encoding="utf-8"))
        pools = next(v for v in data["venues"] if "Piscinas" in v["name"])
        tourism = pools["independentTourismLocation"]
        self.assertEqual("https://www.visitguarda.pt/en/ponto_de_interesse/piscina-municipal-da-guarda/",
                         tourism["url"])
        distance = haversine(pools["latitude"], pools["longitude"],
                             tourism["latitude"], tourism["longitude"])
        self.assertTrue(55 < distance < 57)
        self.assertAlmostEqual(distance, tourism["distanceMetersApprox"], delta=1)

    def test_zambito_and_inatel_are_not_given_fabricated_gps(self):
        data = json.loads(SOURCE.read_text(encoding="utf-8"))
        self.assertEqual({"Campo de Jogos do Zambito",
                          "Pavilhão Gimnodesportivo do INATEL"},
                         set(data["unlocatedInSource"]))
        self.assertEqual("EXACT_EXISTING", data["venues"][0]["relatedCatalogueStatus"])
        self.assertTrue(all("NO_LOCAL_MATCH_REVIEW" == v["relatedCatalogueStatus"]
                            for v in data["venues"][1:]))

if __name__ == "__main__":
    unittest.main()

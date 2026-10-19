"""Safe unit tests for the converter, with no network/DB/pyosmium dependency."""
import importlib.util
from pathlib import Path
import unittest

path = Path(__file__).with_name("import_osm_venues_national.py")
spec = importlib.util.spec_from_file_location("national_venue_importer", path)
national = importlib.util.module_from_spec(spec)
spec.loader.exec_module(national)


class NationalVenueImportTests(unittest.TestCase):
    def test_all_continental_districts(self):
        self.assertEqual(18, len(national.DISTRICTS))
        self.assertEqual("Lisboa", national.DISTRICTS["lisbon"])
        self.assertNotIn("madeira", national.DISTRICTS)
        self.assertNotIn("azores", national.DISTRICTS)

    def test_missing_city_and_address_are_flagged_not_silently_dropped(self):
        venue, quality = national.build_venue(
            {"leisure": "pitch", "sport": "soccer;basketball", "name": "Campo Municipal"},
            "way", 123, (40.1234, -8.5432), "Coimbra")
        self.assertEqual("Coimbra", venue["district"])
        self.assertEqual("Coimbra", venue["city"])
        self.assertEqual("Futebol;Basquetebol", venue["sports"])
        self.assertIn("municipality_to_verify", quality)
        self.assertIn("morada por confirmar", venue["address"])
        self.assertEqual("OSM:WAY:123", venue["sourceReference"])

    def test_no_invented_multisport_for_generic_pavilion(self):
        venue, quality = national.build_venue(
            {"building": "sports_hall", "name": "Pavilhão"},
            "way", 44, (38.78, -9.15), "Lisboa")
        self.assertIsNone(venue["sports"])
        self.assertIn("sports_to_verify", quality)
        self.assertEqual("INDOOR", venue["environmentType"])

    def test_reject_private_or_non_mainland(self):
        self.assertEqual("restricted_access", national.build_venue(
            {"leisure": "pitch", "access": "private"}, "way", 5,
            (38.7, -9.1), "Lisboa")[1])
        self.assertEqual("outside_mainland", national.build_venue(
            {"leisure": "pitch"}, "way", 5, (32.7, -16.9), "Faro")[1])

    def test_unnamed_are_explicitly_tentative(self):
        venue, quality = national.build_venue(
            {"leisure": "swimming_pool"}, "node", 9,
            (39.3, -8.5), "Santarém")
        self.assertEqual("Piscina (OSM #9)", venue["name"])
        self.assertEqual("Natação", venue["sports"])
        self.assertIn("name_to_verify", quality)


if __name__ == "__main__":
    unittest.main()

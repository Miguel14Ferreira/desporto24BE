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

    def test_official_municipality_lookup_resolves_missing_city(self):
        import tempfile
        import json
        with tempfile.TemporaryDirectory() as temp:
            path = Path(temp) / "municipios.geojson"
            path.write_text(json.dumps({
                "type": "FeatureCollection", "features": [{
                    "type": "Feature",
                    "properties": {"Municipio": "Portalegre"},
                    "geometry": {"type": "Polygon", "coordinates": [[
                        [-7.6, 39.1], [-7.1, 39.1], [-7.1, 39.5],
                        [-7.6, 39.5], [-7.6, 39.1]
                    ]]}
                }]
            }), encoding="utf-8")
            lookup = national.MunicipalityLookup(path)
            self.assertEqual("Portalegre", lookup.find(39.3, -7.35))
            self.assertIsNone(lookup.find(40.1, -7.35))

    def test_admin_boundaries_prevent_cross_district_import(self):
        import io
        import json
        import tempfile
        from types import SimpleNamespace
        with tempfile.TemporaryDirectory() as directory:
            p = Path(directory) / "districts.geojson"
            p.write_text(json.dumps({"type": "FeatureCollection", "features": [
                {"type": "Feature", "properties": {"distrito": "Portalegre"},
                 "geometry": {"type": "Polygon", "coordinates": [[
                     [-8.4, 39.0], [-7.5, 39.0], [-7.5, 40.0],
                     [-8.4, 40.0], [-8.4, 39.0]]]}},
                {"type": "Feature", "properties": {"distrito": "Évora"},
                 "geometry": {"type": "Polygon", "coordinates": [[
                     [-8.4, 38.0], [-7.5, 38.0], [-7.5, 39.0],
                     [-8.4, 39.0], [-8.4, 38.0]]]}}
            ]}), encoding="utf-8")
            lookup = national.DistrictLookup(p)
            self.assertEqual("Portalegre", lookup.find(39.5, -8.0))
            self.assertEqual("Évora", lookup.find(38.8, -8.0))
            self.assertIsNone(lookup.find(40.3, -8.0))
            ready, review = io.StringIO(), io.StringIO()
            handler = national.VenueHandler(ready, review, "Portalegre",
                                           district_lookup=lookup)
            class Position:
                def __init__(self, lat, lon):
                    self.lat, self.lon = lat, lon
                def valid(self): return True
            def fixture(id, lat, lon):
                return SimpleNamespace(
                    id=id, location=Position(lat, lon),
                    tags=[SimpleNamespace(k=k, v=v) for k, v in {
                        "leisure": "pitch", "name": "Campo Verde", "sport": "soccer",
                        "addr:city": "Portalegre", "addr:street": "Rua A"
                    }.items()])
            handler.node(fixture(1, 39.5, -8.0))
            handler.node(fixture(2, 38.8, -8.0))
            handler.node(fixture(3, 40.3, -8.0))
            self.assertEqual(1, len(ready.getvalue().splitlines()))
            self.assertEqual(1, len(review.getvalue().splitlines()))
            self.assertIn("district_to_verify", review.getvalue())
            self.assertEqual(1, handler.counts["skipped_other_district"])

    def test_without_official_district_polygons_every_venue_needs_review(self):
        import io
        from types import SimpleNamespace
        ready, review = io.StringIO(), io.StringIO()
        handler = national.VenueHandler(ready, review, "Portalegre")
        class Position:
            lat, lon = 39.5, -8.0
            def valid(self): return True
        tags = [SimpleNamespace(k=k, v=v) for k, v in {
            "leisure": "pitch", "name": "Campo", "sport": "soccer",
            "addr:city": "Elvas", "addr:street": "Rua A"
        }.items()]
        handler.node(SimpleNamespace(id=5, location=Position(), tags=tags))
        self.assertEqual("", ready.getvalue())
        self.assertIn("district_to_verify", review.getvalue())

    def test_quality_gate_quarantines_incomplete_venues(self):
        self.assertFalse(national.is_import_ready("name_to_verify"))
        self.assertFalse(national.is_import_ready("sports_to_verify"))
        self.assertFalse(national.is_import_ready("municipality_to_verify"))
        self.assertTrue(national.is_import_ready("address_to_verify"))
        self.assertTrue(national.is_import_ready("ok"))

    def test_sufficiently_tagged_venue_is_importable(self):
        venue, quality = national.build_venue(
            {"leisure": "pitch", "sport": "soccer", "name": "Campo Verde",
             "addr:city": "Portalegre", "addr:street": "Rua da Escola"},
            "way", 888, (39.294, -7.432), "Portalegre")
        self.assertEqual("ok", quality)
        self.assertTrue(national.is_import_ready(quality))
        self.assertEqual("Futebol", venue["sports"])

    def test_unnamed_are_explicitly_tentative(self):
        venue, quality = national.build_venue(
            {"leisure": "swimming_pool"}, "node", 9,
            (39.3, -8.5), "Santarém")
        self.assertEqual("Piscina (OSM #9)", venue["name"])
        self.assertEqual("Natação", venue["sports"])
        self.assertIn("name_to_verify", quality)


if __name__ == "__main__":
    unittest.main()

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
            municipalities = Path(directory) / "municipalities.geojson"
            municipalities.write_text(json.dumps({
                "type": "FeatureCollection", "features": [{
                    "type": "Feature", "properties": {"municipio": "Portalegre"},
                    "geometry": {"type": "Polygon", "coordinates": [[
                        [-8.4, 39.0], [-7.5, 39.0], [-7.5, 40.0],
                        [-8.4, 40.0], [-8.4, 39.0]
                    ]]}
                }]
            }), encoding="utf-8")
            city_lookup = national.MunicipalityLookup(municipalities)
            self.assertEqual("Portalegre", lookup.find(39.5, -8.0))
            self.assertEqual("Évora", lookup.find(38.8, -8.0))
            self.assertIsNone(lookup.find(40.3, -8.0))
            ready, review = io.StringIO(), io.StringIO()
            handler = national.VenueHandler(ready, review, "Portalegre",
                                           municipality_lookup=city_lookup,
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

    def test_unverified_osm_city_is_quarantined_even_if_district_is_known(self):
        import io
        import json
        import tempfile
        from types import SimpleNamespace
        with tempfile.TemporaryDirectory() as folder:
            municipality_path = Path(folder) / "municipios.geojson"
            municipality_path.write_text(json.dumps({
                "type": "FeatureCollection", "features": [{
                    "properties": {"municipio": "Mora"}, "type": "Feature",
                    "geometry": {"type": "Polygon", "coordinates": [[
                        [-8.4, 38.5], [-7.5, 38.5], [-7.5, 39.0],
                        [-8.4, 39.0], [-8.4, 38.5]]]}}]
            }), encoding="utf-8")
            district_path = Path(folder) / "distritos.geojson"
            district_path.write_text(json.dumps({
                "type": "FeatureCollection", "features": [{
                    "properties": {"distrito": "Portalegre"}, "type": "Feature",
                    "geometry": {"type": "Polygon", "coordinates": [[
                        [-8.4, 39.0], [-7.5, 39.0], [-7.5, 40.0],
                        [-8.4, 40.0], [-8.4, 39.0]]]}}]
            }), encoding="utf-8")
            ready, review = io.StringIO(), io.StringIO()
            handler = national.VenueHandler(
                ready, review, "Portalegre",
                municipality_lookup=national.MunicipalityLookup(municipality_path),
                district_lookup=national.DistrictLookup(district_path))
            class Position:
                lat, lon = 39.5, -8.0
                def valid(self): return True
            tags = [SimpleNamespace(k=k, v=v) for k, v in {
                "leisure": "pitch", "name": "Campo", "sport": "soccer",
                "addr:city": "Elvas", "addr:street": "Rua Exemplo"
            }.items()]
            handler.node(SimpleNamespace(id=10, location=Position(), tags=tags))
            self.assertEqual("", ready.getvalue())
            self.assertIn("municipality_to_verify", review.getvalue())

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

    def test_numbered_or_generic_venues_are_quarantined(self):
        for name in ("01", "02", "03", "04", "Campo", "Piscina",
                     "Campo de Futebol", "Pavilhão Desportivo"):
            with self.subTest(name=name):
                venue, quality = national.build_venue(
                    {"leisure": "pitch", "sport": "soccer", "name": name,
                     "addr:city": "Portalegre", "addr:street": "Rua da Escola"},
                    "way", 123, (39.3, -7.4), "Portalegre")
                self.assertEqual(name, venue["name"])
                self.assertIn("name_to_verify", quality)
                self.assertFalse(national.is_import_ready(quality))
        venue, quality = national.build_venue(
            {"leisure": "pitch", "sport": "soccer", "name": "Campo de Futebol de Sousel",
             "addr:city": "Sousel", "addr:street": "Rua do Campo"},
            "way", 455, (39.3, -7.4), "Portalegre")
        self.assertNotIn("name_to_verify", quality)

    def test_numbered_pitches_do_not_enter_future_published_imports(self):
        for name in ("Campo 3", "Campo 4", "Campo 5", "Campo 6", "Campo 7",
                     "Campo de Futebol 5", "Campo de Jogos 12",
                     "Polidesportivo 3", "Pavilhão n.º 2", "Piscina Municipal 4"):
            with self.subTest(name=name):
                venue, quality = national.build_venue(
                    {"leisure": "pitch", "sport": "soccer", "name": name,
                     "addr:city": "Lisboa", "addr:street": "Avenida Exemplo"},
                    "way", 49290574, (38.755536, -9.1625579), "Lisboa")
                self.assertEqual(name, venue["name"])
                self.assertEqual("OSM:WAY:49290574", venue["sourceReference"])
                self.assertIn("name_to_verify", quality)
                self.assertFalse(national.is_import_ready(quality))

    def test_distinct_venue_names_with_field_numbers_remain_importable(self):
        for name in ("Estádio Universitário de Lisboa — Campo 3",
                     "Campo da Mata", "Campo Municipal de Sousel",
                     "Parque Desportivo do Seixal — Campo 2"):
            with self.subTest(name=name):
                venue, quality = national.build_venue(
                    {"leisure": "pitch", "sport": "soccer", "name": name,
                     "addr:city": "Lisboa", "addr:street": "Rua de Exemplo"},
                    "way", 1234, (38.7555, -9.1625), "Lisboa")
                self.assertNotIn("name_to_verify", quality)
                self.assertTrue(national.is_import_ready(quality))

    def test_lisbon_venue_review_manifest_matches_import_source_ids(self):
        import json
        root = Path(__file__).resolve().parents[1]
        review = json.loads((root / "docs/venue-imports/lisboa-eul-generic-names-review-20261026.json")
                            .read_text(encoding="utf-8"))
        sql = (root / "src/main/resources/db/changelog/venue-imports/drafts/"
               "national-20261022/sql/national-map-osm-lisboa-02.sql").read_text(
               encoding="utf-8")
        rows = review["venues"]
        self.assertEqual(7, len(rows))
        self.assertEqual(7, len(set(x["venueId"] for x in rows)))
        self.assertEqual(7, len(set(x["osmReference"] for x in rows)))
        for row in rows:
            with self.subTest(id=row["venueId"]):
                self.assertIn(row["osmReference"], sql)
                self.assertIn(row["existingName"], sql)
                self.assertIsNone(row["approvedName"])
                self.assertIsNone(row["approvedPostalAddress"])
                self.assertEqual("PENDING_MANUAL_REVIEW", row["status"])
                self.assertTrue(row["osmUrl"].startswith("https://www.openstreetmap.org/way/"))
        self.assertNotEqual(rows[2]["osmReference"], rows[3]["osmReference"],
                            "The two Campo 5 OSM ways must not be merged")

    def test_unnamed_are_explicitly_tentative(self):
        venue, quality = national.build_venue(
            {"leisure": "swimming_pool"}, "node", 9,
            (39.3, -8.5), "Santarém")
        self.assertEqual("Piscina (OSM #9)", venue["name"])
        self.assertEqual("Natação", venue["sports"])
        self.assertIn("name_to_verify", quality)


if __name__ == "__main__":
    unittest.main()

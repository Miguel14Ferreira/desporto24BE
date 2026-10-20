"""Offline regression checks for the 18-district SNID source-quality report."""
import json
from pathlib import Path
import tempfile
import unittest

from snid_district_source_audit import (
    analyse, audit, choose_source, coordinates, iter_json_objects, require_official_url,
)


def sample(name, district, lat=38.70, lon=-9.15, sports=None, category="Instalação"):
    return {
        "name": name, "category": {"name": category}, "sports": sports,
        "address": {"district": district},
        "location": json.dumps({"type": "Point", "coordinates": [lon, lat]}),
    }


class SnidDistrictAuditTests(unittest.TestCase):
    def test_streams_array_and_adjacent_objects_without_loading_entire_file(self):
        records = [sample("Campo A", "Lisboa"), sample("Campo B", "Setúbal")]
        with tempfile.TemporaryDirectory() as folder:
            path = Path(folder) / "records.json"
            path.write_text(json.dumps(records, ensure_ascii=False), encoding="utf-8")
            self.assertEqual(2, len(list(iter_json_objects(path))))
            path.write_text("\n".join(json.dumps(r, ensure_ascii=False)
                                      for r in records), encoding="utf-8")
            self.assertEqual(2, len(list(iter_json_objects(path))))

    def test_18_districts_and_unknown_district_not_mistaken_for_empty(self):
        records = [
            sample("Campo de Treino", "Lisboa", sports=["Futebol"]),
            sample("Campo B", "SETUBAL"),
            sample("Campo C", "Madeira"),
            sample("Campo D", "Faro", lat=32.7, lon=-16.9, sports="Futebol"),
            sample("Clube de Treino", "Lisboa", category="Instituição Desportiva"),
        ]
        rows, summary = analyse(records)
        self.assertEqual(18, len(rows))
        lisboa = next(r for r in rows if r["distrito"] == "Lisboa")
        self.assertEqual(1, lisboa["com_nome_coordenadas_e_modalidade"])
        setubal = next(r for r in rows if r["distrito"] == "Setúbal")
        self.assertEqual(1, setubal["por_rever"])
        self.assertEqual(1, summary["missing_or_unknown_district"])
        self.assertEqual(1, summary["outside_mainland_bbox"])
        self.assertEqual(1, summary["excluded_institutions"])

    def test_works_with_object_geometry_and_does_not_invent_a_point(self):
        rec = sample("Pavilhão", "Évora")
        rec["location"] = {"type": "Point", "coordinates": [-7.91, 38.57]}
        self.assertEqual((38.57, -7.91), coordinates(rec))
        rec["location"] = {"type": "Point", "coordinates": [False, -7.91]}
        self.assertIsNone(coordinates(rec))

    def test_refuses_malformed_json_and_empty_source_without_false_zero_counts(self):
        with tempfile.TemporaryDirectory() as folder:
            source = Path(folder) / "source.json"
            source.write_text("[]", encoding="utf-8")
            with self.assertRaisesRegex(ValueError, "empty"):
                audit(source, Path(folder) / "report")
            for invalid in ('[{"name":"Campo"}, 15]', '[{"name":"Campo"}',
                            '[{"name":"Campo"} x]'):
                source.write_text(invalid, encoding="utf-8")
                with self.assertRaises(ValueError):
                    list(iter_json_objects(source))

    def test_reports_in_excel_friendly_csv_without_publishing_raw_venue_data(self):
        with tempfile.TemporaryDirectory() as folder:
            source = Path(folder) / "source.json"
            source.write_text(json.dumps([sample("Campo A", "Lisboa", sports="Ténis")]),
                              encoding="utf-8")
            rows, summary = audit(source, Path(folder) / "out")
            self.assertEqual(18, len(rows))
            self.assertEqual(1, summary["total_records"])
            report = (Path(folder) / "out" / "snid-distritos.csv").read_text(encoding="utf-8-sig")
            self.assertIn("Lisboa;1;1;1;0;", report)
            self.assertNotIn("Campo A", report)
            self.assertIn("não validado CAOP", report)

    def test_official_resource_only_and_no_empty_demo_file(self):
        self.assertEqual("https://dados.gov.pt/a", require_official_url("https://dados.gov.pt/a"))
        for url in ("http://dados.gov.pt/a", "https://dados.gov.pt.attacker/a",
                    "https://example.org/a"):
            with self.assertRaises(ValueError):
                require_official_url(url)
        data = {"data": [{"title": "Sistema Nacional de Informação Desportiva",
                          "resources": [
                              {"title": "snid-localizacao-20260901.json", "format": "json",
                               "filesize": 0, "url": "https://dados.gov.pt/empty"},
                              {"title": "snid-demo.json", "format": "json",
                               "filesize": 123, "url": "https://dados.gov.pt/demo"},
                              {"title": "snid-localizacao-20260818.json", "format": "json",
                               "filesize": 400, "url": "https://dados.gov.pt/valid"},
                          ]}]}
        self.assertEqual("snid-localizacao-20260818.json", choose_source(data)[0])


if __name__ == "__main__":
    unittest.main()

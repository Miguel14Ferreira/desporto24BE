"""No mocked approval: test OSM map-source staging, gates and exact national snapshot."""
import json
from pathlib import Path
import re
import sys
import tempfile
import unittest

sys.path.insert(0,str(Path(__file__).resolve().parent))
from generate_national_map_venue_batches import (
    SOURCE, SOURCE_SHA, classify, generate, partition, read_snapshot, sport_catalog, statement
)


def row(**override):
    record=dict(sourceReference="OSM:WAY:200",name="Campo Municipal de Aljezur",
                city="Aljezur", district="Faro", sports="Futebol;Futsal",
                venueType="Campo desportivo", environmentType="OUTDOOR",
                latitude=37.322, longitude=-8.800,
                address="Localização no mapa: 37.322, -8.8 (morada por confirmar)",
                importGate="ADDRESS_REVIEW",
                originalLiveHeuristic="UNMATCHED_REQUIRES_REVIEW",
                approvedForImport=False)
    record.update(override)
    return record


class NationalBulkMapVenuesTests(unittest.TestCase):
    def test_archived_source_immutable_18_districts(self):
        src=read_snapshot(SOURCE)
        self.assertEqual(3514,len(src))
        self.assertEqual(18,len(set(x["district"] for x in src)))

    def test_preview_all_districts_without_any_auto_approval(self):
        with tempfile.TemporaryDirectory() as folder:
            report=generate(SOURCE,Path(folder))
            self.assertEqual(SOURCE_SHA,report["sourceSha256"])
            self.assertEqual(18,report["districtsOutput"])
            self.assertEqual(3514,report["sourceCount"])
            self.assertTrue(100 <= report["totalMapSourcedCandidates"] <= 3514)
            self.assertEqual(report["totalReviewOrExcluded"],
                             3514-report["totalMapSourcedCandidates"])
            self.assertFalse(report["humanApprovalsFabricated"])
            self.assertEqual(report["releasePolicy"],
                             "OPT_IN_ONLY_NOT_INCLUDED_IN_LIQUIBASE_MASTER")
            sqls=list((Path(folder)/"sql").glob("*.sql"))
            self.assertGreaterEqual(len(sqls),18)
            self.assertEqual(sum(p.read_text().count("INSERT INTO desporto24.venue")
                                 for p in sqls),report["totalMapSourcedCandidates"])
            self.assertTrue(all("address, sports" in p.read_text()
                                and "'OPENSTREETMAP'" in p.read_text()
                                for p in sqls))
            master=(Path(__file__).resolve().parent.parent/
                    "src/main/resources/db/changelog/db.changelog-master.yaml").read_text()
            self.assertNotIn("national-map-osm",master)

    def test_existing_duplicate_and_restricted_are_quarantined(self):
        sports=sport_catalog()
        for example in (
            row(importGate="CONFIRMED_EXISTING_LIVE_EXCLUDE"),
            row(importGate="POSSIBLE_DUPLICATE_REVIEW"),
            row(name="Antigo Campo do Centro"),
            row(name="Pavilhão Escolar"),
            row(name="Campo do Hotel"),row(name="Campo sem nome"),
            row(name="Campo (OSM #123)"),
            row(sports=""),
            row(latitude=50),
            row(district="Funchal"),
        ):
            self.assertNotEqual(classify(example,sports),"map_sourced_unverified")
        with self.assertRaisesRegex(ValueError,"approval"):
            classify(row(approvedForImport=True),sports)

    def test_keep_only_one_identical_nearby_osm_object(self):
        first=row()
        second=row(sourceReference="OSM:WAY:201",latitude=37.32201)
        groups,reasons,conflicts=partition([first,second])
        self.assertEqual(len(groups["Faro"]),1)
        self.assertEqual(len(conflicts),1)
        self.assertEqual(reasons["intra_snapshot_possible_duplicate"],1)

    def test_quotes_and_postal_placeholders_never_become_verified_streets(self):
        sql=statement(row(name="Campo d'Água",address="Rua Suposta, 1"))
        self.assertIn("Campo d''Água",sql)
        self.assertIn("NULL",sql)
        self.assertNotIn("Rua Suposta",sql)
        self.assertNotIn("UPDATE desporto24",sql.upper())
        self.assertNotIn("DELETE FROM desporto24",sql.upper())
        self.assertIn("WHERE NOT EXISTS",sql)
        self.assertIn("ON CONFLICT DO NOTHING",sql)
        self.assertIn("OPENSTREETMAP",sql)


if __name__=="__main__":
    unittest.main()

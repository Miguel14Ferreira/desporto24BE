import csv
import json
from pathlib import Path
import sys
import tempfile
import unittest
sys.path.insert(0, str(Path(__file__).parent))
from prepare_national_gps_only_review import choose_gps, generate, rejection, meters, DISTRICTS

ROOT = Path(__file__).resolve().parent.parent
SOURCE = ROOT/"docs/venue-imports/datasets/national-3514-reviewed-20261021.jsonl.gz"


def candidate(ref="OSM:WAY:123", **updates):
    x = dict(sourceReference=ref, name="Campo de Futebol da Serra",
             city="Seia", district="Guarda", venueType="Campo desportivo",
             sports="Futebol",
             address="Localização no mapa: 40.40000, -7.70000 (morada por confirmar)",
             latitude=40.4, longitude=-7.7, importGate="ADDRESS_REVIEW",
             originalLiveHeuristic="UNMATCHED_REQUIRES_REVIEW",
             approvedForImport=False, independentIdentityVerified=False)
    x.update(updates)
    return x

class GpsOnlyReviewTest(unittest.TestCase):
    def test_gps_is_not_postal_address_or_approval(self):
        with tempfile.TemporaryDirectory() as temp:
            result=generate(SOURCE,Path(temp),per_district=5)
            self.assertEqual(result["source_total"],3514)
            self.assertEqual(result["selected_for_review"],90)
            self.assertEqual(result["districts_covered"],18)
            self.assertEqual(set(result["district_quota"]),set(DISTRICTS))
            self.assertEqual(result["approved_for_import"],0)
            with (Path(temp)/"national-gps-only-review.csv").open(
                    encoding="utf-8-sig",newline="") as f:
                items=list(csv.DictReader(f,delimiter=";"))
            self.assertEqual(len(set(row["osm_reference"] for row in items)),90)
            self.assertTrue(all(row["postal_address_status"]=="UNKNOWN_GPS_ONLY"
                                and row["approved_for_import"]=="NO"
                                and row["independent_identity_verified"]=="NO"
                                and row["review_status"]=="AWAITING_INDEPENDENT_EVIDENCE"
                                for row in items))
            self.assertEqual(len(set(row["district"] for row in items)),18)

    def test_no_duplicate_or_known_live_reference(self):
        real=candidate()
        other=candidate()
        with self.assertRaisesRegex(ValueError,"Duplicate OSM"):
            choose_gps([real,other],require_18=False)
        live=candidate(importGate="CONFIRMED_EXISTING_LIVE_EXCLUDE")
        self.assertEqual(rejection(live),"not_clean_gps_only")
        risk=candidate(importGate="POSSIBLE_DUPLICATE_REVIEW")
        self.assertEqual(rejection(risk),"not_clean_gps_only")

    def test_rejects_closed_ambiguous_or_commercial(self):
        for row in (
            candidate(name="Antigo Campo Desportivo"),
            candidate(name="Campo de Futebol"),
            candidate(name="Pavilhão 1"),
            candidate(name="Campo Polidesportivo"),
            candidate(name="Country Club Covilhã"),
            candidate(name="Pavilhão Escolar"),
            candidate(venueType="Ginásio"),
            candidate(sports="Golfe"),
        ):
            self.assertNotEqual(rejection(row),"eligible_for_independent_review")
        self.assertEqual(rejection(candidate()),"eligible_for_independent_review")

    def test_coordinates_must_be_finite_mainland(self):
        for lat in (None,float("nan"),0,43.0):
            self.assertNotEqual(rejection(candidate(latitude=lat)),"eligible_for_independent_review")

    def test_distance_same_place(self):
        a=candidate()
        self.assertLess(meters(a,candidate(ref="OSM:WAY:124",latitude=40.4001)),30)

if __name__=="__main__":
    unittest.main()

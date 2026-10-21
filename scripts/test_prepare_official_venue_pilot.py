import gzip
import hashlib
import json
from pathlib import Path
import sys
import tempfile
import unittest
sys.path.insert(0, str(Path(__file__).parent))
from prepare_official_venue_pilot import (
    SOURCE_SHA256, build_pilot, load_candidates, render_preflight)

def example(ref="OSM:WAY:145943204"):
    return {
      "sourceReference": ref, "name": "Pavilhão Marialvas", "city": "Cantanhede",
      "district": "Coimbra", "latitude": 40.3440287, "longitude": -8.5876799,
      "importGate": "SOURCE_REQUIRES_INDEPENDENT_REVIEW",
      "originalLiveHeuristic": "UNMATCHED_REQUIRES_REVIEW"
    }

def proof(ref="OSM:WAY:145943204"):
    return {
      "sourceReference": ref, "name": "Pavilhão Marialvas",
      "city": "Cantanhede", "district": "Coimbra",
      "address": "Rua Luís de Camões, 19, 3060-183 Cantanhede",
      "sports": "Futsal;Andebol", "venueType": "Pavilhão desportivo",
      "environmentType": "INDOOR",
      "sourceUrl": "https://cm-cantanhede.pt/mcsite/entidade/1680/pavilhao-marialvas",
      "lastReviewed": "2026-10-21",
      "notes": "A Câmara confirma identidade e morada. Uso depende de regras próprias."
    }

class PilotTests(unittest.TestCase):
    def test_candidate_never_auto_approved(self):
        row = build_pilot({example()["sourceReference"]:example()}, [proof()], exact_three=False)[0]
        self.assertIs(row["approved"], False)
        self.assertIs(row["independentFinalApproval"], False)
        self.assertIs(row["accessForUserOrganizedSessionsConfirmed"], False)
        sql = render_preflight([row])
        self.assertIn("BEGIN TRANSACTION READ ONLY", sql)
        self.assertIn("ROLLBACK;", sql)
        self.assertNotIn("INSERT INTO", sql.upper())
        self.assertNotIn("DELETE FROM", sql.upper())

    def test_blocked_ref_fails(self):
        item = example(ref="OSM:WAY:429474882")
        with self.assertRaisesRegex(ValueError, "blocked"):
            build_pilot({item["sourceReference"]:item}, [proof(ref=item["sourceReference"])], exact_three=False)

    def test_live_duplicate_and_wrong_city_fail(self):
        item = example()
        item["importGate"] = "POSSIBLE_DUPLICATE_REVIEW"
        with self.assertRaises(ValueError):
            build_pilot({item["sourceReference"]:item}, [proof()], exact_three=False)
        with self.assertRaises(ValueError):
            build_pilot({example()["sourceReference"]:example()}, [dict(proof(),city="Lisboa")], exact_three=False)

    def test_bad_url_cannot_be_official_evidence(self):
        for url in ("https://evil.example/venue", "http://cm-cantanhede.pt/venue"):
            with self.assertRaisesRegex(ValueError, "whitelisted"):
                build_pilot({example()["sourceReference"]:example()},
                            [dict(proof(), sourceUrl=url)], exact_three=False)

    def test_source_hash_required(self):
        with tempfile.TemporaryDirectory() as tmp:
            p=Path(tmp)/"source.jsonl.gz"
            with gzip.open(p,"wt",encoding="utf-8") as stream:
                stream.write(json.dumps(example())+"\n")
            with self.assertRaisesRegex(ValueError,"SHA"):
                load_candidates(p,count=1)
            rows=load_candidates(p,count=1,
                                 digest=hashlib.sha256(p.read_bytes()).hexdigest())
            self.assertEqual(len(rows), 1)

    def test_existing_generator_rejects_unapproved_pilot(self):
        from generate_verified_venue_liquibase import validate
        draft=build_pilot({example()["sourceReference"]:example()},[proof()],exact_three=False)[0]
        with self.assertRaisesRegex(ValueError,"approved=true"):
            validate(draft)

    def test_preflight_refuses_manually_approved_rows(self):
        draft=build_pilot({example()["sourceReference"]:example()},[proof()],exact_three=False)[0]
        draft["approved"]=True
        with self.assertRaisesRegex(ValueError,"unapproved"):
            render_preflight([draft])

if __name__ == "__main__":
    unittest.main()

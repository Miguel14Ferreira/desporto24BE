"""Prevent an operator-supplied read-only result from silently becoming import approval."""
from copy import deepcopy
import json
from pathlib import Path
import sys
import unittest

sys.path.insert(0, str(Path(__file__).resolve().parent))
from finalize_official_venue_pilot import load_decisions, load_pilot
from validate_first_three_operator_preflight import (
    EVIDENCE, DECISIONS, PROPOSALS, validate_evidence
)


class OperatorReportedPreflightTests(unittest.TestCase):
    @classmethod
    def setUpClass(cls):
        cls.payload = json.loads(EVIDENCE.read_text(encoding="utf-8"))
        cls.proposals = load_pilot(PROPOSALS)
        cls.decisions = load_decisions(DECISIONS)

    def validate(self, payload):
        return validate_evidence(payload, self.proposals, self.decisions)

    def test_real_operator_output_is_three_pending_no_collisions(self):
        result = self.validate(self.payload)
        self.assertEqual(3, len(result))
        self.assertEqual(265, self.payload["catalogueRows"])
        self.assertTrue(all(row["potentialCollisionCount"] == 0
                            for row in result.values()))
        self.assertTrue(all(row["publicationStatus"].startswith("PENDING_")
                            for row in result.values()))
        olympic = result["OSM:WAY:1056855283"]
        self.assertEqual(1, olympic["nearby100To150MetresCount"])
        mario = self.payload["records"][0]["potentialMatches"][0]
        self.assertEqual(262, mario["id"])
        self.assertEqual("OSM:WAY:58257179", mario["sourceReference"])
        self.assertEqual(107.7, mario["distanceMetres"])

    def test_cannot_pretend_that_publication_or_date_was_approved(self):
        for field, value in (
            ("allProposalsApproved", True),
            ("executionDateVerified", True),
            ("executedAt", "2026-10-22T00:00:00Z"),
            ("evidenceType", "live_database_automatically_verified"),
        ):
            tampered = deepcopy(self.payload)
            tampered[field] = value
            with self.subTest(field=field), self.assertRaises(ValueError):
                self.validate(tampered)
        altered = deepcopy(self.payload)
        altered["records"][0]["approvalDecision"] = "APPROVED"
        with self.assertRaises(ValueError):
            self.validate(altered)

    def test_rejects_collisions_hidden_as_neighbors(self):
        altered = deepcopy(self.payload)
        altered["records"][0]["potentialMatches"][0]["sameRef"] = True
        with self.assertRaises(ValueError):
            self.validate(altered)
        altered = deepcopy(self.payload)
        altered["records"][0]["nearby100To150MetresCount"] = 0
        with self.assertRaises(ValueError):
            self.validate(altered)
        altered = deepcopy(self.payload)
        altered["records"][0]["potentialMatches"][0]["distanceMetres"] = 100.0
        with self.assertRaises(ValueError):
            self.validate(altered)

    def test_fails_on_unexpected_candidate_or_fingerprint_drift(self):
        altered = deepcopy(self.payload)
        altered["records"][1]["sourceReference"] = "OSM:WAY:100"
        with self.assertRaises(ValueError):
            self.validate(altered)
        altered = deepcopy(self.payload)
        altered["records"][1]["city"] = "Porto"
        with self.assertRaises(ValueError):
            self.validate(altered)
        decisions = deepcopy(self.decisions)
        decisions["OSM:WAY:145943204"]["candidateFingerprint"] = "bad"
        with self.assertRaises(ValueError):
            validate_evidence(self.payload, self.proposals, decisions)

    def test_evidence_not_in_release_master(self):
        master = (Path(__file__).resolve().parent.parent /
                  "src/main/resources/db/changelog/db.changelog-master.yaml").read_text(encoding="utf-8")
        self.assertNotIn(EVIDENCE.name, master)
        self.assertTrue(all(d["decision"] == "PENDING" for d in self.decisions.values()))


if __name__ == "__main__":
    unittest.main()

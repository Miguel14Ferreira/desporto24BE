"""Review-only gates and opt-in Liquibase SQL for the 3 municipal proposals."""
from copy import deepcopy
from datetime import date
import json
from pathlib import Path
import sys
import tempfile
import unittest

sys.path.insert(0, str(Path(__file__).parent))
from finalize_official_venue_pilot import (
    load_pilot, load_decisions, fingerprint, review, release, PILOT_REFS,
)

ROOT = Path(__file__).resolve().parent.parent
PROPOSALS = ROOT / "docs/venue-imports/first-three-proposals-unapproved-20261021.jsonl"
DECISIONS = ROOT / "docs/venue-imports/first-three-decisions-pending-20261021.json"
TODAY = date(2026, 10, 21)


def fake_signed_reviews(items):
    decisions = deepcopy(items)
    for r in decisions.values():
        r.update(dict(
            decision="APPROVED", approvedBy="INTEGRATION_FIXTURE_NOT_A_REAL_REVIEWER",
            approvedOn="2026-10-21", identityVerified=True, addressVerified=True,
            sportsVerified=True, currentlyOperatingVerified=True,
            accessRestrictionsReviewed=True, restrictionsDisplayReviewed=True,
            preflightNoDuplicates=True, preflightMatchCount=0,
            preflightCheckedOn="2026-10-21", accessPolicy="AUTHORIZATION_REQUIRED",
            verificationNotes="Synthetic integration fixture ONLY; tests the release gate, not actual approvals.",
        ))
    return decisions


class ExplicitApprovals(unittest.TestCase):
    @classmethod
    def setUpClass(cls):
        cls.proposals = load_pilot(PROPOSALS)
        cls.decisions = load_decisions(DECISIONS)

    def test_exact_pilot_fingerprints_and_missing_approvals(self):
        self.assertEqual(set(self.proposals), PILOT_REFS)
        self.assertEqual(set(self.decisions), PILOT_REFS)
        for ref in PILOT_REFS:
            self.assertEqual(self.decisions[ref]["candidateFingerprint"],
                             fingerprint(self.proposals[ref]))
            self.assertEqual(self.decisions[ref]["decision"], "PENDING")
        with tempfile.TemporaryDirectory() as tmp:
            out = Path(tmp) / "changes"
            with self.assertRaisesRegex(ValueError, "Pending review"):
                release(PROPOSALS, DECISIONS, out, "three-venue-pilot", today=TODAY)
            self.assertFalse(out.exists(), "Pending approvals must never produce SQL/YAML")

    def test_approval_requires_reviewer_and_every_technical_gate(self):
        verified = fake_signed_reviews(self.decisions)
        for field in ("approvedBy", "identityVerified", "addressVerified",
                      "sportsVerified", "currentlyOperatingVerified",
                      "accessRestrictionsReviewed", "restrictionsDisplayReviewed",
                      "preflightNoDuplicates", "preflightMatchCount",
                      "preflightCheckedOn", "accessPolicy", "verificationNotes"):
            changed = deepcopy(verified)
            ref = sorted(PILOT_REFS)[0]
            changed[ref][field] = None
            with self.subTest(field=field), self.assertRaises(ValueError):
                review(self.proposals, changed, today=TODAY)

    def test_official_proposal_cannot_be_changed_after_review(self):
        reviews = fake_signed_reviews(self.decisions)
        proposals = deepcopy(self.proposals)
        ref = sorted(PILOT_REFS)[0]
        proposals[ref]["address"] = "Rua substituída"
        with self.assertRaisesRegex(ValueError, "fingerprint"):
            review(proposals, reviews, today=TODAY)

    def test_municipal_source_cannot_be_changed_without_fingerprint_mismatch(self):
        decisions = fake_signed_reviews(self.decisions)
        reference = sorted(PILOT_REFS)[0]
        for field, replacement in (
            ("evidenceUrl", "https://www.cm-coimbra.pt/areas/viver/desporto/other"),
            ("evidenceReviewedOn", "2026-10-20"),
        ):
            changed = deepcopy(self.proposals)
            changed[reference][field] = replacement
            with self.subTest(field=field), self.assertRaisesRegex(ValueError, "fingerprint"):
                review(changed, decisions, today=TODAY)

    def test_non_municipal_evidence_is_rejected_by_pilot_loader(self):
        with tempfile.TemporaryDirectory() as tmp:
            path = Path(tmp) / "changed.jsonl"
            for source in ("https://example.org/venue", "http://www.cm-coimbra.pt/venue",
                           "https://user:password@www.cm-coimbra.pt/venue"):
                changed = deepcopy(list(self.proposals.values()))
                changed[0]["evidenceUrl"] = source
                path.write_text("".join(json.dumps(x) + "\n" for x in changed), encoding="utf-8")
                with self.subTest(source=source), self.assertRaisesRegex(ValueError, "municipal"):
                    load_pilot(path)

    def test_stale_or_future_db_preflight_is_rejected(self):
        for checked in ("2026-10-18", "2026-10-22", "garbled"):
            reviewed = fake_signed_reviews(self.decisions)
            reviewed[sorted(PILOT_REFS)[0]]["preflightCheckedOn"] = checked
            with self.subTest(checked=checked), self.assertRaises(ValueError):
                review(self.proposals, reviewed, today=TODAY)

    def test_exclusion_is_allowed_but_unreviewed_is_not(self):
        decisions = fake_signed_reviews(self.decisions)
        ref = sorted(PILOT_REFS)[0]
        decisions[ref]["decision"] = "EXCLUDED"
        approved, pending = review(self.proposals, decisions, today=TODAY)
        self.assertEqual(len(approved), 2)
        self.assertEqual(pending, [])
        decisions[ref]["verificationNotes"] = ""
        with self.assertRaisesRegex(ValueError, "requires a reason"):
            review(self.proposals, decisions, today=TODAY)

    def test_synthetic_signoffs_generate_opt_in_but_not_master_inclusion(self):
        synthetic = fake_signed_reviews(self.decisions)
        with tempfile.TemporaryDirectory() as tmp:
            folder = Path(tmp)
            decision_file = folder / "synthetic-decisions.json"
            decision_file.write_text(json.dumps(list(synthetic.values())), encoding="utf-8")
            out = folder / "migrations"
            sql, yaml, n = release(PROPOSALS, decision_file, out,
                                   "three-venue-pilot", today=TODAY)
            self.assertEqual(n, 3)
            text = sql.read_text(encoding="utf-8")
            self.assertEqual(text.count("INSERT INTO desporto24.venue"), 3)
            self.assertEqual(text.count("source_reference, website,"), 3)
            for proposal in self.proposals.values():
                self.assertIn(proposal["evidenceUrl"], text)
            self.assertIn("ON CONFLICT DO NOTHING", text)
            self.assertIn("WHERE NOT EXISTS", text)
            self.assertNotIn("DELETE FROM desporto24.venue", text)
            self.assertIn("runInTransaction: true", yaml.read_text(encoding="utf-8"))
            self.assertNotIn(yaml.name, (ROOT / "src/main/resources/db/changelog/db.changelog-master.yaml").read_text())
            self.assertFalse((out / "approved-pilot.jsonl").exists(),
                             "Reviewer identities must not be kept as temporary SQL sources")

    def test_bad_proposal_status_or_ref_is_rejected(self):
        with tempfile.TemporaryDirectory() as tmp:
            altered = Path(tmp)/"bad.jsonl"
            rows = list(self.proposals.values())
            rows[0] = dict(rows[0], approved=True)
            altered.write_text("".join(json.dumps(x)+"\n" for x in rows))
            with self.assertRaisesRegex(ValueError, "Untrusted"):
                load_pilot(altered)
        with tempfile.TemporaryDirectory() as tmp:
            changed = Path(tmp)/"bad-decisions.json"
            entries = deepcopy(list(self.decisions.values()))
            entries[0]["sourceReference"] = "OSM:WAY:429474882"
            changed.write_text(json.dumps(entries))
            with self.assertRaisesRegex(ValueError, "Invalid"):
                load_decisions(changed)


if __name__ == "__main__":
    unittest.main()

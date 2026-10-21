"""Review-paging tests: stable first batch, no overlaps, source SHA and gates."""
from pathlib import Path
import sys
import unittest
sys.path.insert(0, str(Path(__file__).parent))
from prepare_subsequent_venue_review_batches import (
    balanced_next, get_batch, generate, EXPECTED_SOURCE_SHA256,
)

ROOT = Path(__file__).resolve().parent.parent
DATA = ROOT / "docs/venue-imports/datasets/national-3514-reviewed-20261021.jsonl.gz"


def row(ref, district="Lisboa"):
    return dict(
        sourceReference=f"OSM:WAY:{ref}", name=f"Campo de Futebol {ref}",
        city="Lisboa", district=district, sports="Futebol",
        venueType="Campo desportivo", address="Rua da Bola, 1000-200 Lisboa",
        latitude=38.75+int(ref)/1000000, longitude=-9.1,
        importGate="SOURCE_REQUIRES_INDEPENDENT_REVIEW",
        originalLiveHeuristic="UNMATCHED_REQUIRES_REVIEW",
        approvedForImport=False, independentIdentityVerified=False
    )


class NextPageTest(unittest.TestCase):
    def test_sparse_districts_fill_without_duplicates(self):
        candidates=[row(i,d) for i in range(100,130)
                    for d in ("Porto", "Lisboa")]
        first, _ = get_batch(candidates, page=1, limit=15, district_cap=3, expected=60)
        second, info = get_batch(candidates, page=2, limit=15, district_cap=3, expected=60)
        self.assertEqual(len(second), 15)
        self.assertEqual(len({x["sourceReference"] for x in second}), 15)
        self.assertFalse(set(x["sourceReference"] for x in first)
                         & set(x["sourceReference"] for x in second))
        self.assertEqual(info["previous_refs"], 15)

    def test_ineligible_candidate_rejected_for_direct_balancing(self):
        bad=row(99)
        bad["importGate"]="POSSIBLE_DUPLICATE_REVIEW"
        with self.assertRaisesRegex(ValueError, "Ineligible"):
            balanced_next([bad])

    def test_page_zero_rejected(self):
        with self.assertRaisesRegex(ValueError,"page"):
            get_batch([row(91)],page=0,expected=1)

    def test_actual_immutable_snapshot_page_two_regression(self):
        import csv
        import hashlib
        import tempfile
        with tempfile.TemporaryDirectory() as tmp:
            report=generate(DATA,Path(tmp),page=2)
            self.assertEqual(report["source_sha256"],EXPECTED_SOURCE_SHA256)
            self.assertEqual(report["selected_for_manual_review"],50)
            self.assertEqual(report["previous_refs"],50)
            self.assertEqual(report["remaining_eligible_after_this_page"],28)
            self.assertEqual(report["approved_for_import"],0)
            with (Path(tmp)/"national-review-page-2.csv").open(encoding="utf-8-sig") as f:
                rows=list(csv.DictReader(f,delimiter=";"))
            refs="".join(row["osm_reference"]+"\n" for row in rows)
            self.assertEqual(hashlib.sha256(refs.encode()).hexdigest(),
                             "a85a67af282eb0997375799d46ec7f73e8fc95ab1f4c36a1b47e8e28b446cd06")

if __name__ == "__main__":
    unittest.main()

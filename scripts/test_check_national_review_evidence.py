import csv
from datetime import date
import json
from pathlib import Path
import sys
import tempfile
import unittest
sys.path.insert(0, str(Path(__file__).parent))
from check_national_review_evidence import assess, read_rows

def row(reference="OSM:WAY:42", name="Piscina Central", city="Lisboa"):
    return dict(priority_order="1", osm_reference=reference, name=name,
                city=city, district="Lisboa", address_osm_unverified="Rua 12",
                decision="UNDECIDED")

def evidence(**changes):
    item = dict(osmReference="OSM:WAY:42", name="Piscina Central", city="Lisboa",
                identity="IDENTITY_AND_ADDRESS",
                access="PUBLIC_PROGRAMME_CONDITIONAL",
                officialUrl="https://www.lisboa.pt/pontos-de-interesse/detalhe/exemplo",
                officialAddress="Rua 12", addressConflict=False,
                observedOn="2026-10-21",
                notes="A ficha da autarquia documenta identidade; uso sujeito a regras.")
    item.update(changes)
    return item

class EvidenceGateTest(unittest.TestCase):
    def test_official_identity_never_auto_approves(self):
        summary, results = assess([row()], [evidence()])
        self.assertEqual(summary["approved_for_import"], 0)
        self.assertEqual(results[0]["independently_approved_for_import"], "NO")
        self.assertEqual(results[0]["review_state"], "IDENTITY_DOCUMENTED_ACCESS_UNVERIFIED")

    def test_authorization_required_is_explicit(self):
        summary, results = assess([row()], [evidence(access="AUTHORIZATION_REQUIRED")])
        self.assertEqual(results[0]["review_state"], "PERMISSION_NEEDED")

    def test_conflicts_are_not_silenced(self):
        _, results = assess([row()], [evidence(
            identity="IDENTITY_WITH_CONFLICT", addressConflict=True)])
        self.assertEqual(results[0]["review_state"], "ADDRESS_CONFLICT_HOLD")
        with self.assertRaisesRegex(ValueError, "Address conflict"):
            assess([row()], [evidence(addressConflict=True)])

    def test_missing_evidence_not_presented_as_verified(self):
        summary, results = assess([row()], [])
        self.assertEqual(summary["no_official_evidence"], 1)
        self.assertEqual(results[0]["review_state"], "AWAITING_OFFICIAL_SOURCE")

    def test_rejects_wrong_identity_fake_web_and_future(self):
        for bad in (dict(name="Outro recinto"), dict(officialUrl="http://localhost/"),
                    dict(observedOn="2030-01-01"), dict(approvedForImport=True)):
            with self.assertRaises(ValueError):
                assess([row()], [evidence(**bad)], observed=date(2026,10,21))

    def test_no_reference_reuse(self):
        with self.assertRaises(ValueError):
            assess([row()], [evidence(), evidence()])

    def test_csv_rejects_preapproved_data(self):
        with tempfile.TemporaryDirectory() as d:
            p=Path(d)/"source.csv"
            with p.open("w",encoding="utf-8",newline="") as h:
                wr=csv.DictWriter(h,fieldnames=list(row()),delimiter=";")
                wr.writeheader()
                wr.writerow({**row(),"decision":"APPROVED"})
            with self.assertRaisesRegex(ValueError,"already approved"):
                read_rows(p)

if __name__=="__main__":
    unittest.main()

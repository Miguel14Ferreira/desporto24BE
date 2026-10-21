#!/usr/bin/env python3
"""Explicit approval gate for the 3 OSM municipal-source pilot venues.

Never approves a source automatically or contacts a database. Generates opt-in
Liquibase SQL/YAML ONLY after a human fills all release sign-offs.
"""
import argparse
from datetime import date
import hashlib
import json
from pathlib import Path
from generate_verified_venue_liquibase import generate

PILOT_REFS = frozenset({
    "OSM:WAY:1056855283", "OSM:WAY:122685516", "OSM:WAY:145943204"
})
IDENTITY_KEYS = ("sourceReference", "name", "city", "district", "address",
                 "sports", "venueType", "environmentType", "latitude", "longitude")
ACCESS_POLICIES = {"AUTHORIZATION_REQUIRED", "CONDITIONAL_PUBLIC_PROGRAMME",
                   "PUBLIC_ACCESS_CONFIRMED", "OTHER_RESTRICTIONS"}


def fingerprint(proposal):
    fields = {key: proposal[key] for key in IDENTITY_KEYS}
    return hashlib.sha256(
        json.dumps(fields, ensure_ascii=False, sort_keys=True,
                   separators=(",", ":")).encode("utf-8")
    ).hexdigest()


def load_pilot(source_file):
    records = {}
    with Path(source_file).open(encoding="utf-8") as src:
        for line in src:
            if not line.strip():
                continue
            row = json.loads(line)
            ref = row.get("sourceReference")
            if (ref not in PILOT_REFS or ref in records
                    or row.get("approved") is not False
                    or row.get("independentFinalApproval") is not False
                    or row.get("publicationStatus") != "DRAFT_PREFLIGHT_REQUIRED"):
                raise ValueError("Untrusted/missing/approved/duplicate pilot proposal: " + str(ref))
            for key in IDENTITY_KEYS:
                if key not in row:
                    raise ValueError("Missing candidate field: " + key)
            records[ref] = row
    if set(records) != PILOT_REFS:
        raise ValueError("Pilot must contain exactly the three reference-checked proposals")
    return records


def load_decisions(path):
    raw = json.loads(Path(path).read_text(encoding="utf-8"))
    if not isinstance(raw, list):
        raise ValueError("Decisions must be an array")
    entries = {}
    for row in raw:
        ref = row.get("sourceReference") if isinstance(row, dict) else None
        if ref not in PILOT_REFS or ref in entries:
            raise ValueError("Invalid/duplicated pilot decision: " + str(ref))
        entries[ref] = row
    if set(entries) != PILOT_REFS:
        raise ValueError("Require explicit decision entry for all three references")
    return entries


def parse_date(value, field, today):
    try:
        parsed = date.fromisoformat(value)
    except (TypeError, ValueError):
        raise ValueError(field + " must be ISO date YYYY-MM-DD")
    if parsed > today or (today - parsed).days > 1:
        raise ValueError(field + " must be current (today/yesterday) and not future")
    return parsed


def review(proposals, decisions, *, today=None):
    today = today or date.today()
    verified = []
    pending = []
    for ref in sorted(PILOT_REFS):
        d = decisions[ref]
        p = proposals[ref]
        if d.get("candidateFingerprint") != fingerprint(p):
            raise ValueError("Candidate fingerprint mismatch: " + ref)
        decision = d.get("decision")
        if decision not in ("PENDING", "EXCLUDED", "APPROVED"):
            raise ValueError("Unknown decision for " + ref)
        if decision == "PENDING":
            pending.append(ref)
            continue
        if decision == "EXCLUDED":
            if len(str(d.get("verificationNotes") or "").strip()) < 20:
                raise ValueError("Excluded candidate requires a reason: " + ref)
            continue
        # An approval must be externally signed, current, and backed by a fresh
        # LIVE read-only preflight; an OSM record or municipal URL alone is insufficient.
        for field in ("identityVerified", "addressVerified", "sportsVerified",
                      "currentlyOperatingVerified", "accessRestrictionsReviewed",
                      "preflightNoDuplicates", "restrictionsDisplayReviewed"):
            if d.get(field) is not True:
                raise ValueError("Missing signed approval gate " + field + " for " + ref)
        if d.get("accessPolicy") not in ACCESS_POLICIES:
            raise ValueError("Access policy is not documented for " + ref)
        if not isinstance(d.get("approvedBy"), str) or not d["approvedBy"].strip():
            raise ValueError("Missing approving reviewer for " + ref)
        if len(str(d.get("verificationNotes") or "").strip()) < 45:
            raise ValueError("Missing independent verification notes for " + ref)
        if not isinstance(d.get("sourceUrl"), str) or d["sourceUrl"] != p["evidenceUrl"]:
            raise ValueError("Official source differs from pinned proposal for " + ref)
        if d.get("preflightMatchCount") != 0:
            raise ValueError("Fresh production preflight must report zero matches: " + ref)
        parse_date(d.get("approvedOn"), "approvedOn", today)
        parse_date(d.get("preflightCheckedOn"), "preflightCheckedOn", today)
        approved = {key: p[key] for key in IDENTITY_KEYS}
        approved.update({
            "approved": True,
            "approvedBy": d["approvedBy"].strip(),
            "verificationNotes": d["verificationNotes"].strip(),
        })
        verified.append(approved)
    return verified, pending


def release(source_file, decision_file, out_dir, migration_id, *, today=None):
    proposals = load_pilot(source_file)
    decisions = load_decisions(decision_file)
    approved, pending = review(proposals, decisions, today=today)
    if pending:
        raise ValueError("Pending review entries block this pilot release: " + ", ".join(pending))
    if not approved:
        raise ValueError("Zero individually approved venues; no migration generated")
    out_dir = Path(out_dir)
    # Do not create any SQL file until all input gates are satisfied.
    payload = out_dir / "approved-pilot.jsonl"
    if out_dir.exists() and any(out_dir.iterdir()):
        raise ValueError("Output directory must be empty to prevent stale approval files")
    out_dir.mkdir(parents=True, exist_ok=True)
    try:
        payload.write_text("".join(json.dumps(v, ensure_ascii=False)+"\n"
                                   for v in approved), encoding="utf-8")
        sql, yml, count = generate(payload, out_dir, migration_id)
        # Intermediate approvals contain reviewer names; never leave them on disk.
    finally:
        payload.unlink(missing_ok=True)
    return sql, yml, count


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--pilot-proposals", required=True, type=Path)
    parser.add_argument("--decisions", required=True, type=Path)
    parser.add_argument("--out-dir", required=True, type=Path)
    parser.add_argument("--migration-id", default="jynlo-three-municipal-pilot")
    args = parser.parse_args()
    try:
        sql, yml, total = release(args.pilot_proposals, args.decisions,
                                  args.out_dir, args.migration_id)
    except ValueError as exc:
        parser.error(str(exc))
    print(f"Generated {total} authorized venue inserts: {sql.name}, {yml.name}")
    print("NOT linked from db.changelog-master.yaml; deployment still requires manual review.")


if __name__ == "__main__":
    main()

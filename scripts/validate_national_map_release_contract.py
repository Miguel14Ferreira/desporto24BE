#!/usr/bin/env python3
"""Release branch gate: do not activate national OSM imports unless all 27 assets match."""
import hashlib
import json
from pathlib import Path
import re

ROOT = Path(__file__).resolve().parent.parent
DIR = ROOT / "src/main/resources/db/changelog/venue-imports/drafts/national-20261022"
MASTER = ROOT / "src/main/resources/db/changelog/db.changelog-master.yaml"


def verify():
    import yaml  # checked in dedicated CI only; no production dependency

    manifest=json.loads((DIR/"national-plan.json").read_text(encoding="utf-8"))
    plan=yaml.safe_load((DIR/"national-18-districts-OPT-IN.yaml").read_text(encoding="utf-8"))
    entries=plan["databaseChangeLog"]
    assert manifest["sourceCount"]==3514
    assert manifest["districtsOutput"]==18
    assert manifest["totalMapSourcedCandidates"]==3331
    assert manifest["totalReviewOrExcluded"]==183
    assert len(entries)==27
    master=MASTER.read_text(encoding="utf-8")
    name="db/changelog/venue-imports/drafts/national-20261022/national-18-districts-OPT-IN.yaml"
    assert master.count(name)==1
    references={}
    n=0
    for district,record in manifest["districts"].items():
        assert record["candidates"]>0, district
        for chunk in record["sql"]:
            references["sql/"+chunk["file"]]=chunk
            n+=chunk["rows"]
    assert n==3331
    assert len(references)==27
    ids=set()
    for entry in entries:
        ch=entry["changeSet"]
        assert ch["dbms"]=="postgresql" and ch["runInTransaction"] is True
        assert ch["id"] not in ids
        ids.add(ch["id"])
        pc=ch["preConditions"]
        assert {"onFail":"HALT"} in pc
        assert {"tableExists":{"schemaName":"desporto24","tableName":"venue"}} in pc
        assert len(ch["changes"])==1
        file=ch["changes"][0]["sqlFile"]
        assert file["relativeToChangelogFile"] is True
        assert file["splitStatements"] is True
        assert file["endDelimiter"]==";"
        path=file["path"]
        assert path in references
        item=references.pop(path)
        raw=(DIR/path).read_bytes()
        assert hashlib.sha256(raw).hexdigest()==item["sha256"]
        sql=raw.decode("utf-8")
        assert sql.count("INSERT INTO desporto24.venue")==item["rows"]
        assert sql.count("WHERE NOT EXISTS")==item["rows"]
        assert sql.count("ON CONFLICT DO NOTHING")==item["rows"]
        # Search only at SQL statement starts: venue names/data may contain verbs.
        executable=re.sub(r"(?m)^\\s*--[^\\n]*$", "", sql)
        assert re.search(r"(?im)^\\s*(?:UPDATE|DELETE|DROP|TRUNCATE|ALTER)\\s",executable) is None
        assert "address, sports" in sql
        assert "'OPENSTREETMAP'" in sql
        assert "NULL" in sql
    assert not references
    print("READY FOR RESTORE GATE: 27 content-addressed chunks, 3331 OSM map candidates, 18 districts")
    print("This test does NOT verify a production backup or authorize a deployment.")


if __name__=="__main__":
    verify()

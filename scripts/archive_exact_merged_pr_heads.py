#!/usr/bin/env python3
"""Conservatively archive up to 25 branch tips exactly matching merged PR head SHA."""
import json,os,subprocess,urllib.request,urllib.parse,datetime,collections
ROOT=os.environ["GITHUB_REPOSITORY"];TOKEN=os.environ["GITHUB_TOKEN"];PROD=os.environ["PRODUCTION_BRANCH"]
DRY=os.getenv("DRY_RUN","true").lower()!="false";LIMIT=min(int(os.getenv("LIMIT","25")),25)
def api(path,method="GET",body=None):
 data=None if body is None else json.dumps(body).encode()
 req=urllib.request.Request("https://api.github.com"+path,method=method,data=data,headers={"Authorization":"Bearer "+TOKEN,"Accept":"application/vnd.github+json","Content-Type":"application/json"})
 with urllib.request.urlopen(req,timeout=30) as r:
  b=r.read();return json.loads(b) if b else {}
def prs(state):
 result=[]
 for page in range(1,31):
  p=api(f"/repos/{ROOT}/pulls?state={state}&per_page=100&page={page}")
  result.extend(p)
  if len(p)<100:return result
 raise RuntimeError("PR pagination incomplete")
open_heads={p["head"]["ref"] for p in prs("open")}
merged=collections.defaultdict(set)
for p in prs("closed"):
 h=p.get("head") or {}
 if p.get("merged_at") and (h.get("repo") or {}).get("full_name")==ROOT and h.get("sha"):
  merged[h["ref"]].add(h["sha"])
lines=subprocess.check_output(["git","for-each-ref","--format=%(refname:strip=3)\t%(objectname)\t%(committerdate:iso8601-strict)","refs/remotes/origin"],text=True)
cutoff=datetime.datetime.now(datetime.timezone.utc)-datetime.timedelta(days=3)
candidates=[]
for line in lines.splitlines():
 name,sha,date=line.split("\t",2)
 if name in {"HEAD","main","master","develop",PROD} or name in open_heads or name.startswith(("backup/","chore/","release/")):continue
 if datetime.datetime.fromisoformat(date)>=cutoff:continue
 if sha in merged.get(name,set()):candidates.append((name,sha))
candidates=sorted(candidates)[:LIMIT]
deleted=[];errors=[]
for name,sha in candidates:
 if DRY:continue
 try:
  tag="archive/merged-pr-head-20261030/"+name
  api(f"/repos/{ROOT}/git/refs","POST",{"ref":"refs/tags/"+tag,"sha":sha})
  path=urllib.parse.quote(name,safe="/")
  current=api(f"/repos/{ROOT}/git/ref/heads/{path}")
  if current["object"]["sha"]!=sha:raise RuntimeError("branch moved")
  api(f"/repos/{ROOT}/git/refs/heads/{path}","DELETE")
  deleted.append(name)
 except Exception as exc:errors.append({"branch":name,"error":str(exc)[:150]})
print(json.dumps({"dry_run":DRY,"limit":LIMIT,"eligible_in_selected_batch":len(candidates),"deleted":len(deleted),"errors":errors,"branches":[n for n,_ in candidates]},indent=2))
if errors:raise SystemExit(1)

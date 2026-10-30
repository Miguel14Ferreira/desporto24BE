#!/usr/bin/env python3
"""Read-only: require current branch tip == historical merged PR head SHA."""
import json,os,subprocess,urllib.request,collections,datetime
repo=os.environ["GITHUB_REPOSITORY"];token=os.environ["GITHUB_TOKEN"]
def api(path):
 req=urllib.request.Request("https://api.github.com"+path,headers={"Authorization":"Bearer "+token,"Accept":"application/vnd.github+json"})
 with urllib.request.urlopen(req,timeout=30) as r:return json.load(r)
raw=subprocess.check_output(["git","for-each-ref","--format=%(refname:strip=3)\t%(objectname)\t%(committerdate:iso8601-strict)","refs/remotes/origin"],text=True)
branches={}
cutoff=datetime.datetime.now(datetime.timezone.utc)-datetime.timedelta(days=3)
for row in raw.splitlines():
 n,sha,date=row.split("\t",2)
 if n in {"HEAD","main","master","develop",os.environ["PRODUCTION_BRANCH"]} or n.startswith(("backup/","chore/")):continue
 if datetime.datetime.fromisoformat(date)>=cutoff:continue
 branches[n]=sha
prs=collections.defaultdict(list)
for page in range(1,31):
 data=api(f"/repos/{repo}/pulls?state=closed&per_page=100&page={page}")
 for p in data:
  head=p.get("head") or {}
  if p.get("merged_at") and head.get("sha") and (head.get("repo") or {}).get("full_name")==repo:
   prs[head["ref"]].append({"number":p["number"],"sha":head["sha"],"base":p["base"]["ref"]})
 if len(data)<100:break
else:raise RuntimeError("PR pagination incomplete")
matched=[{"branch":n,"sha":sha,"merged_prs":[p["number"] for p in prs.get(n,[]) if p["sha"]==sha]} for n,sha in branches.items() if any(p["sha"]==sha for p in prs.get(n,[]))]
print(json.dumps({"old_branches_checked":len(branches),"exact_head_sha_matches":len(matched),"candidates":matched,"note":"read only; merged PR exact head SHA indicates no subsequent branch updates, pending protection checks"},indent=2))

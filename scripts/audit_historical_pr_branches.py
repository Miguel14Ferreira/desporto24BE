#!/usr/bin/env python3
"""Read-only: correlate current branch refs with GitHub merged PR heads."""
import json,os,subprocess,urllib.request,collections
repo=os.environ["GITHUB_REPOSITORY"]; token=os.environ["GITHUB_TOKEN"]
def api(path):
 req=urllib.request.Request("https://api.github.com"+path,headers={"Authorization":"Bearer "+token,"Accept":"application/vnd.github+json"})
 with urllib.request.urlopen(req,timeout=30) as r:return json.load(r)
refs=subprocess.check_output(["git","for-each-ref","--format=%(refname:strip=3)","refs/remotes/origin"],text=True).splitlines()
protected={"HEAD","main","master","develop",os.environ["PRODUCTION_BRANCH"]}
branches={r for r in refs if r not in protected and not r.startswith("backup/")}
pr_by_head=collections.defaultdict(list)
pages=0
for page in range(1,31):
 prs=api(f"/repos/{repo}/pulls?state=closed&per_page=100&page={page}")
 for p in prs:
  if p.get("merged_at") and p.get("head",{}).get("repo",{}):
   pr_by_head[p["head"]["ref"]].append({"number":p["number"],"merged_at":p["merged_at"],"base":p["base"]["ref"]})
 pages=page
 if len(prs)<100:break
else:raise RuntimeError("Pagination incomplete: cannot conclude")
candidates=[{"branch":b,"prs":pr_by_head[b]} for b in sorted(branches) if b in pr_by_head]
never=[b for b in branches if b not in pr_by_head]
print(json.dumps({"pages":pages,"current_refs":len(branches),"merged_pr_head_matches":len(candidates),"candidates":candidates[:100],"no_matching_merged_pr":len(never),"warning":"historical PR association is not proof of same branch head; no deletions"},indent=2))

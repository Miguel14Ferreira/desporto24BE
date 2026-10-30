import json,os,subprocess,urllib.request,urllib.parse
ROOT=os.environ["GITHUB_REPOSITORY"];BASE=os.environ["PRODUCTION_BRANCH"];TOKEN=os.environ["GITHUB_TOKEN"]
NAMES=os.environ["CANDIDATES"].split(",")
def git(*args):
 return subprocess.run(["git",*args],capture_output=True,text=True)
def api(path,method="GET",body=None):
 data=None if body is None else json.dumps(body).encode()
 req=urllib.request.Request("https://api.github.com"+path,data=data,method=method,headers={"Authorization":"Bearer "+TOKEN,"Accept":"application/vnd.github+json","Content-Type":"application/json"})
 with urllib.request.urlopen(req,timeout=30) as r:
  b=r.read()
  return json.loads(b) if b else {}
openheads=set()
for page in range(1,10):
 prs=api(f"/repos/{ROOT}/pulls?state=open&per_page=100&page={page}")
 openheads.update(p["head"]["ref"] for p in prs)
 if len(prs)<100:break
done=[]
for name in NAMES:
 if name in openheads or name in ("main","master","develop",BASE):raise RuntimeError("protected "+name)
 ref="refs/remotes/origin/"+name;baseref="refs/remotes/origin/"+BASE
 head=git("rev-parse",ref)
 if head.returncode:raise RuntimeError(name+" missing")
 files=git("diff","--name-only",baseref+"..."+ref)
 if files.returncode or not files.stdout.strip():raise RuntimeError("bad files "+name)
 for path in files.stdout.splitlines():
  a=git("rev-parse",baseref+":"+path);b=git("rev-parse",ref+":"+path)
  if a.returncode or b.returncode or a.stdout.strip()!=b.stdout.strip():raise RuntimeError("not exact "+name+" "+path)
 tag="archive/qa-exact-20261030/"+name
 api(f"/repos/{ROOT}/git/refs","POST",{"ref":"refs/tags/"+tag,"sha":head.stdout.strip()})
 current=api(f"/repos/{ROOT}/git/ref/heads/{urllib.parse.quote(name,safe='/')}")
 if current["object"]["sha"]!=head.stdout.strip():raise RuntimeError("moved "+name)
 api(f"/repos/{ROOT}/git/refs/heads/{urllib.parse.quote(name,safe='/')}","DELETE")
 done.append(name)
print(json.dumps({"deleted":done,"count":len(done)}))

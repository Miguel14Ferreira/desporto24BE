#!/usr/bin/env python3
"""Unauthenticated, bounded GET-only validation of the published Jynlo catalogue.

Runs only after a successful Render deploy; changes nothing in production.
"""
import json
import time
from urllib.error import HTTPError, URLError
from urllib.parse import urlencode
from urllib.request import HTTPRedirectHandler, Request, build_opener

ROOT = "https://api.d24sports.com"
DISTRICTS = ("Aveiro", "Beja", "Braga", "Bragança", "Castelo Branco",
             "Coimbra", "Évora", "Faro", "Guarda", "Leiria", "Lisboa",
             "Portalegre", "Porto", "Santarém", "Setúbal",
             "Viana do Castelo", "Vila Real", "Viseu")
PAGE_SIZE = 40
MAX_PAGES_PER_DISTRICT = 60

class RefuseRedirect(HTTPRedirectHandler):
    def redirect_request(self, req, fp, code, msg, headers, newurl):
        raise RuntimeError("API unexpectedly redirected: refusing to send requests to new host")


def read_public_page(district, page, *, opener=None):
    if district not in DISTRICTS or not 0 <= page < MAX_PAGES_PER_DISTRICT:
        raise ValueError("Unexpected district/page")
    url=ROOT+"/api/venues/suggest?"+urlencode({"district":district,"page":page})
    opener=opener or build_opener(RefuseRedirect)
    request=Request(url,headers={"Accept":"application/json",
            "User-Agent":"Jynlo-National-ReadOnly-Catalogue-Audit/1.0"},method="GET")
    for attempt in range(3):
        try:
            with opener.open(request,timeout=20) as response:
                if response.status!=200 or "json" not in response.headers.get("Content-Type","").lower():
                    raise RuntimeError("Bad HTTP or Content-Type for "+district)
                raw=response.read(512_001)
                if len(raw)>512_000:
                    raise RuntimeError("Public page too large")
            value=json.loads(raw)
            if not isinstance(value,dict) or not isinstance(value.get("venues"),list) or not isinstance(value.get("hasMore"),bool) or value.get("page") != page:
                raise RuntimeError("Unexpected venue suggestion schema")
            rows=value["venues"]
            if len(rows)>PAGE_SIZE or (not rows and value["hasMore"]):
                raise RuntimeError("Unexpected pagination result")
            return value
        except (HTTPError,URLError) as error:
            if attempt==2:
                raise RuntimeError("Public venue GET failed for "+district+", page "+str(page)) from error
            time.sleep(1+attempt)


def report(fetch=read_public_page, pause=0.08):
    ids=set()
    by_district={}
    all_osm=0
    for district in DISTRICTS:
        count=0
        osm=0
        for page in range(MAX_PAGES_PER_DISTRICT):
            data=fetch(district,page)
            for row in data["venues"]:
                if not isinstance(row,dict) or not isinstance(row.get("id"),int) or row.get("id") in ids:
                    raise RuntimeError("Missing or duplicate id in public district listing")
                if row.get("district") != district:
                    raise RuntimeError("Record returned for incorrect district")
                ids.add(row["id"])
                count+=1
                if row.get("informationSource")=="OPENSTREETMAP":
                    osm+=1
                    if not str(row.get("sourceReference") or "").startswith("OSM:"):
                        raise RuntimeError("Missing OSM provenance for a map-sourced venue")
            if not data["hasMore"]:
                break
            if page==MAX_PAGES_PER_DISTRICT-1:
                raise RuntimeError("Unexpected >60 public pages in district "+district)
            if pause: time.sleep(pause)
        by_district[district]={"total":count,"openStreetMap":osm}
        all_osm+=osm
        print(district+" "+str(count)+" (OSM "+str(osm)+")",flush=True)
    total=sum(row["total"] for row in by_district.values())
    result={"districtsChecked":len(by_district),"venuesWithRecognizedMainlandDistrict":total,
            "openStreetMapVenues":all_osm,
            "mapListingThresholdReached":all_osm>=3000 and all(v["openStreetMap"]>=1 for v in by_district.values()),
            "byDistrict":by_district,
            "api":ROOT,"method":"GET_ONLY","databaseModified":False}
    print("JYNLO_NATIONAL_LIVE_PUBLIC_AUDIT="+json.dumps(result,ensure_ascii=False,sort_keys=True),flush=True)
    if not result["mapListingThresholdReached"]:
        raise RuntimeError("Nationwide OSM catalogue not yet live in all districts")
    return result


if __name__=="__main__":
    report()

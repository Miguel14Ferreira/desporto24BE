'use strict';

/**
 * Real browser -> local Angular -> real JWT/Spring controllers -> disposable
 * PostgreSQL 16. No HTTP requests reach Render, production or outside localhost.
 * Run only from CommunityTrackSprintAuthenticatedPgIT fixture.
 */
const assert = require('node:assert/strict');
const fs = require('node:fs');
const path = require('node:path');
const { chromium } = require(path.join(process.env.JYNLO_PLAYWRIGHT_QA_ROOT || '', 'node_modules', 'playwright'));

const conf = {
  port: process.env.JYNLO_SPRINT_PORT || '',
  community: process.env.JYNLO_SPRINT_COMMUNITY_ID || '',
  active: process.env.JYNLO_SPRINT_ACTIVE_SEASON_ID || '',
  closed: process.env.JYNLO_SPRINT_CLOSED_SEASON_ID || '',
  bracket: process.env.JYNLO_SPRINT_BRACKET_ID || '',
  owner: process.env.JYNLO_SPRINT_OWNER_TOKEN || '',
  member: process.env.JYNLO_SPRINT_MEMBER_TOKEN || ''
};
assert.match(conf.port, /^[1-9][0-9]{3,4}$/);
for (const field of ['community', 'active', 'closed', 'bracket'])
  assert.match(conf[field], /^[1-9][0-9]*$/);
for (const field of ['owner', 'member']) assert.ok(conf[field].split('.').length === 3);
assert.notEqual(conf.active, conf.closed);

const FRONT = 'http://127.0.0.1:4200';
const API = 'http://127.0.0.1:' + conf.port;
const safePath = '/menu/communities/' + conf.community + '?tab=sessions';
const forbiddenWrites = [];
const apiReads = new Set();

async function newFixturePage(browser, token, language) {
  const context = await browser.newContext({ viewport: {width: 1440,height: 900}, serviceWorkers: 'block' });
  await context.addInitScript(({t,lang}) => {
    localStorage.setItem('t', t);
    localStorage.setItem('d24-language', lang);
  }, { t: token, lang: language });
  const page = await context.newPage();
  await page.route('**/*', async route => {
    const req = route.request(), u = new URL(req.url());
    const method = req.method();
    if (u.origin === FRONT) return route.continue();
    // Angular dev config points to localhost:8080; forward WITHOUT mocks to
    // the genuine random-port Spring Boot application in this same JUnit run.
    if ((u.hostname === 'localhost' || u.hostname === '127.0.0.1')
        && Number(u.port) === 8080 && u.pathname.startsWith('/api/')) {
      if (!['GET','OPTIONS','HEAD'].includes(method)) {
        forbiddenWrites.push(method + ' ' + u.pathname);
        return route.fulfill({status: 403, contentType:'application/json',
          body: JSON.stringify({message:'Read-only browser integration fixture'})});
      }
      apiReads.add(method + ' ' + u.pathname);
      try {
        const result = await route.fetch({ url: API + u.pathname + u.search, timeout: 30000 });
        return route.fulfill({response: result});
      } catch(e) {
        console.error('[SPRINT-BROWSER] local API forwarding failed', method, u.pathname, String(e));
        return route.abort();
      }
    }
    // Includes all third-party origins and any accidental calls to the real API.
    return route.abort('blockedbyclient');
  });
  page.on('pageerror', err => console.error('[SPRINT-BROWSER] Angular pageerror:',err.message.slice(0,300)));
  return { page, context };
}

async function inspect(browser, token, language, isOwner) {
  const {page,context} = await newFixturePage(browser,token,language);
  try {
    await page.goto(FRONT + safePath, {waitUntil:'domcontentloaded',timeout:30000});
    const select=page.locator('.season-filter select').first();
    await select.waitFor({state:'visible',timeout:30000});
    await select.selectOption({index:1});
    const label=language==='en'?'Sprint brackets':'Quadros de Sprint';
    const area=page.locator('section.round-editor[aria-label="' + label + '"]');
    try {
      await area.waitFor({state:'visible',timeout:30000});
      await area.getByText('Olympic 24 QA').waitFor({state:'visible',timeout:15000});
    } catch (e) {
      console.error('[SPRINT-BROWSER] debug',JSON.stringify({
        language,role:isOwner?'owner':'member',url:page.url(),
        body:(await page.locator('body').innerText().catch(()=>'' )).slice(0,1400),
        api:[...apiReads].slice(-30)
      }));
      throw e;
    }
    const cards=area.locator('.racket-result-card');
    assert.equal(await cards.count(),45, 'All 45 Olympic bracket match cards render from real PostgreSQL');
    for(const slot of ['A1','U1','V1','W1','Q1','S1','G','B','F58']) {
      const heads=area.locator('.racket-result-header strong');
      const labels=await heads.allTextContents();
      assert.ok(labels.some(s => s.trim().endsWith('· '+slot)), 'Missing real slot '+slot+' in '+language);
    }
    const text=await area.innerText();
    assert.ok(text.includes('fixture_track_01'), 'Real seeded rider is missing in '+language);
    assert.ok(text.includes('5.º') && text.includes('8.º'), 'Real 5th–8th rankings not visible in '+language);
    assert.ok(text.includes(language==='en'?'Series completed':'Série concluída'),
      'Real finished heat status not localized in '+language);
    if (isOwner) assert.ok(await area.locator('input').count()>0,'Owner must see bracket editor');
    else assert.equal(await area.locator('input').count(),0,'Member must not see owner editor');
    assert.ok(apiReads.has('GET /api/communities/'+conf.community+'/seasons/'+conf.active+'/track-sprint-brackets'),
      'Browser must read real bracket endpoint, not mocked data');
    // Switching to a historical season must never leave the current bracket.
    await select.selectOption({index:2});
    await area.getByText('Olympic 24 QA').waitFor({state:'hidden',timeout:15000});
    assert.ok(!((await area.innerText()).includes('Olympic 24 QA')),'Current results leaked to historical season');
    assert.ok(apiReads.has('GET /api/communities/'+conf.community+'/seasons/'+conf.closed+'/track-sprint-brackets'),
      'Browser must fetch independent historical season');
    console.log('[SPRINT-BROWSER] PASS role='+ (isOwner?'OWNER':'MEMBER') +
      ' lang='+ language + ' matches=45 seasonIsolation=ok browserBackend=real');
  } finally {
    await context.close();
  }
}

(async()=>{
  const browser=await chromium.launch({channel:'chrome',headless:true,args:['--no-sandbox']});
  try {
    await inspect(browser,conf.member,'pt',false);
    await inspect(browser,conf.owner,'en',true);
    assert.deepEqual(forbiddenWrites,[],'Browser may not mutate even disposable data');
    console.log('[SPRINT-BROWSER] PASS genuine Spring/JWT/PostgreSQL16, PT+EN, owner/member');
  } finally { await browser.close(); }
})().catch(e=>{console.error('[SPRINT-BROWSER] FAIL',e.stack||String(e));process.exitCode=1;});

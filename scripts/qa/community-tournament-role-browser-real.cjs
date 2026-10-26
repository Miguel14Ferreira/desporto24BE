'use strict';

/**
 * Real Angular, real signed JWT and real Spring Boot/PostgreSQL 16 reads.
 * Browser contexts are independent. No API mocks for community or tournament
 * data. Local-only port forwarding is the sole transformation.
 *
 * No page interaction is permitted to mutate tournaments, even the isolated
 * database. Any unexpected write is blocked before Spring receives it.
 */
const assert = require('node:assert/strict');
const path = require('node:path');
const { chromium } = require(path.join(
  process.env.JYNLO_PLAYWRIGHT_QA_ROOT || '/tmp/jynlo-tournament-roles-browser',
  'node_modules', 'playwright'));

const env = {
  springPort: process.env.JYNLO_ROLES_SPRING_PORT || '',
  communityId: process.env.JYNLO_ROLES_COMMUNITY_ID || '',
  tournamentId: process.env.JYNLO_ROLES_TOURNAMENT_ID || '',
  ownerJwt: process.env.JYNLO_ROLES_OWNER_JWT || '',
  memberJwt: process.env.JYNLO_ROLES_MEMBER_JWT || '',
  outsiderJwt: process.env.JYNLO_ROLES_OUTSIDER_JWT || ''
};
assert.match(env.springPort, /^[1-9]\d{3,4}$/);
for (const id of ['communityId', 'tournamentId']) assert.match(env[id], /^[1-9]\d*$/);
for (const jwt of ['ownerJwt', 'memberJwt', 'outsiderJwt'])
  assert.equal(env[jwt].split('.').length, 3, 'Signed JWT fixture is required');

const front = 'http://127.0.0.1:4200';
const spring = 'http://127.0.0.1:' + env.springPort;
const detailPath = '/menu/communities/' + env.communityId + '?tab=tournaments';
const writes = [];
const reads = new Set();
let blockedUnexpectedHosts = 0;
const localDomains = new Set(['localhost', '127.0.0.1']);

async function session(browser, token, language, width) {
  const context = await browser.newContext({
    viewport: { width, height: width === 390 ? 844 : 900 },
    serviceWorkers: 'block',
    locale: language === 'pt' ? 'pt-PT' : 'en-GB'
  });
  await context.addInitScript(({jwt, lang}) => {
    localStorage.setItem('t', jwt);
    localStorage.setItem('d24-language', lang);
  }, { jwt: token, lang: language });
  const page = await context.newPage();
  const observedErrors = [];
  page.on('pageerror', error => observedErrors.push(error.message.slice(0,140)));
  await page.route('**/*', async route => {
    const req = route.request(), url = new URL(req.url()), method = req.method();
    if (url.origin === front) return route.continue();
    if (localDomains.has(url.hostname) && url.port === '8080'
        && url.pathname.startsWith('/api/')) {
      // Angular telemetry is immaterial and not part of the community test.
      if (method === 'POST' && ['/api/pilot/events/access','/api/presence/heartbeat'].includes(url.pathname))
        return route.fulfill({status:200,contentType:'application/json',body:'{}'});
      if (!['GET','HEAD','OPTIONS'].includes(method)) {
        writes.push(method + ' ' + url.pathname);
        return route.fulfill({status:403,contentType:'application/json',body:'{}'});
      }
      reads.add(method + ' ' + url.pathname);
      try {
        const response = await route.fetch({url: spring + url.pathname + url.search, timeout:30000});
        return route.fulfill({response});
      } catch {
        return route.abort('failed');
      }
    }
    blockedUnexpectedHosts++;
    return route.abort('blockedbyclient');
  });
  return { context, page, observedErrors };
}

async function inspect(browser, {role,token,width,language}) {
  const {context,page,observedErrors} = await session(browser, token, language, width);
  try {
    await page.goto(front + detailPath, { waitUntil:'domcontentloaded', timeout:35000 });
    const heading = page.locator('.tournament-heading h4');
    await heading.waitFor({state:'visible',timeout:35000});
    // Select by fixture name, independent of the order returned by PostgreSQL.
    const active = page.locator('.tournament-sidebar .tournament-option')
      .filter({hasText:'Active QA'});
    await active.waitFor({state:'visible', timeout:18000});
    await active.click();
    await heading.filter({hasText:'Active QA'}).waitFor({state:'visible',timeout:18000});
    const details = page.locator('section.tournaments');
    await details.locator('.match-card').first().waitFor({state:'visible',timeout:18000});

    assert.equal(await heading.innerText(),'Active QA');
    const scoreInputs = await details.locator('.match-card .score-edit input').count();
    const deleteControls = await details.locator('.tournament-heading-actions .t-btn-danger-outline').count();
    const createControls = await details.locator('.competition-hero-actions .t-btn-primary').count();
    const standings = details.locator('table.standings-table').last();
    await standings.waitFor({state:'visible',timeout:18000});
    const tableRows = await standings.locator('tbody tr').count();
    assert.equal(tableRows, 2, 'Two real PostgreSQL standings rows must render');

    if (role === 'OWNER') {
      assert.equal(scoreInputs, 2, 'Only manager sees actual result editor');
      assert.ok(deleteControls > 0, 'Manager can see delete affordance');
      assert.ok(createControls > 0, 'Manager can see create affordance');
      const form = details.locator('.match-card .score-edit').first();
      await form.locator('input[type=number]').first().fill('3');
      await form.locator('input[type=number]').last().fill('1');
      assert.equal(await form.locator('button').first().isEnabled(), true,
        'Manager can draft a valid score without submitting');
    } else {
      assert.equal(scoreInputs, 0, 'Member must not see editable result inputs');
      assert.equal(deleteControls, 0, 'Member must not see delete controls');
      assert.equal(createControls, 0, 'Member must not see create controls');
    }
    const geo = await page.evaluate(() => ({
      width: innerWidth,
      scrollWidth: document.documentElement.scrollWidth,
      tournamentRight: document.querySelector('section.tournaments').getBoundingClientRect().right
    }));
    assert.ok(geo.scrollWidth <= width+2, 'Horizontal page overflow '+JSON.stringify(geo));
    assert.ok(geo.tournamentRight <= width+2, 'Tournament extends outside viewport');
    assert.deepEqual(observedErrors, [], 'Angular runtime errors');
    const needed = '/api/communities/' + env.communityId + '/tournaments';
    assert.ok([...reads].some(r => r.startsWith('GET ' + needed)),
      'UI must read real authenticated tournament API');
    console.log('[TOURNAMENT-BROWSER] PASS role='+role+' language='+language+
      ' width='+width+' rows='+tableRows+' backend=real');
  } finally {
    await context.close();
  }
}

(async () => {
  const browser = await chromium.launch({ headless: true, args:['--no-sandbox'] });
  try {
    for (const width of [390, 1440]) {
      for (const language of ['pt','en']) {
        await inspect(browser,{role:'OWNER',token:env.ownerJwt,width,language});
        await inspect(browser,{role:'MEMBER',token:env.memberJwt,width,language});
      }
    }
    assert.deepEqual(writes, [], 'Browser must not perform any business mutation');
    console.log('[TOURNAMENT-BROWSER] PASS actual Angular + signed JWT + Spring + PG16');
  } finally {
    await browser.close();
  }
})().catch(error => {
  // Exception stacks can contain URLs; do not log JWTs or browser content.
  console.error('[TOURNAMENT-BROWSER] FAIL',error.message?.slice(0,450) || 'UNKNOWN');
  process.exitCode = 1;
});

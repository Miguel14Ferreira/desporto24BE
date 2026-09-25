# Desporto24 Relaunch — MVP Design

**Date:** 2026-09-25  
**Status:** Approved design, pending implementation plan  
**Repositories:** `Miguel14Ferreira/desporto24BE` and `Miguel14Ferreira/desporto24FE`  
**Working branch:** `feature/desporto24-relaunch`

## 1. Product goal

Relaunch Desporto24 as a real, public product where people can discover nearby sports sessions, join them, create their own sessions, and communicate with other participants.

The core user problem is:

> “I want to practise a sport. Is there anyone near me I can play with?”

The MVP must work well on desktop and mobile from one codebase. The frontend will be responsive and installable as a PWA.

## 2. MVP principles

- Reuse the existing Spring Boot, PostgreSQL, JWT authentication, profile, email verification, optional MFA, friends, notifications and chat foundations where they are still useful.
- Modernise rather than rewrite the whole product.
- Keep one Angular frontend for desktop and mobile.
- Prefer simple flows over feature breadth.
- Do not introduce payments, ratings, player skill levels, tournaments, clubs or premium subscriptions in the MVP.
- Keep price informational only.
- Protect the exact session location until a user successfully joins.

## 3. Main user journeys

### 3.1 Registration and login

The existing account model remains conceptually intact:

1. User creates an account with email and password.
2. User confirms the account by email.
3. User logs in and receives a JWT.
4. MFA remains optional and can be enabled by the user.
5. Password recovery remains available.

MFA must not become mandatory.

### 3.2 Discover sessions

The main screen is a sports-session browser inspired by a classic game server browser.

Desktop presentation:

- dense sortable list/table;
- persistent session list;
- selected session details open in a panel on the right.

Mobile presentation:

- compact vertical cards;
- session detail opens fullscreen.

Primary discovery fields:

- sport;
- approximate zone/city;
- date and time;
- distance;
- current participants / maximum participants;
- informational price;
- public/private status;
- availability status.

The default ordering is proximity when location permission is available.

### 3.3 Location

The frontend asks for browser location permission.

If granted:

- use latitude/longitude to search and order nearby sessions;
- show approximate distance such as `2.4 km`.

If denied or unavailable:

- the user can select a city/zone manually;
- the application remains fully usable.

Before joining, users see only the approximate zone/city.

After successfully joining, participants can see the exact address.

### 3.4 Create a session

A logged-in user can create a session with:

- sport;
- date/time;
- city/zone;
- exact address;
- maximum number of participants;
- informational price or free;
- optional photo;
- public/private flag;
- password when private.

Sports use the existing predefined sport list plus an `Other` option.

Player skill level is intentionally excluded from the MVP.

### 3.5 Join public session

For a public session with free capacity:

1. User presses **Join**.
2. Membership is created immediately.
3. Exact address becomes visible.
4. Session chat becomes available.

A user cannot join the same session twice.

### 3.6 Join private session

Private sessions are visible in discovery with a lock indicator.

The password must never be returned by public session APIs.

Flow:

1. User opens the private session.
2. User enters the session password.
3. Backend validates the password.
4. If correct and capacity exists, membership is created immediately.
5. Exact address and session chat become available.

The password is the access control mechanism for private sessions; creator approval is not additionally required.

### 3.7 Full session and waitlist

When a session is full:

1. A new join attempt places the user on the waitlist instead of the participant list.
2. Waitlist order is FIFO.
3. If a participant leaves or is removed, the first eligible waitlisted user is promoted automatically.
4. The promoted user receives a notification.

A user cannot simultaneously be a participant and be on the waitlist for the same session.

### 3.8 Leave, remove and cancel

Participants can leave a session before it occurs.

The creator can:

- remove a participant;
- cancel the session.

When a participant leaves or is removed:

- a waiting user is promoted when applicable;
- affected users receive a notification.

When the creator cancels:

- the session becomes cancelled;
- participants and waitlisted users receive a notification;
- joining is disabled.

Historical records should not be physically deleted merely because a session is cancelled.

### 3.9 Session chat

Session chat is separate from the existing direct/friend chat.

Only active session participants and the creator may read or post messages in that session chat.

Users who only discover a session cannot access its chat.

Users removed from or no longer participating in a session must lose access to new session messages.

The existing friend/direct-message feature may remain as a separate feature.

## 4. Desktop UX

The primary desktop discovery layout is a server-browser style list.

Example columns:

| Sport | Zone | When | Distance | Players | Price | Access | Status |
|---|---|---|---:|---:|---:|---|---|
| Football | Cacém | Today 20:30 | 1.8 km | 8/10 | 5 € | Public | Open |
| Padel | Sintra | Today 21:00 | 6.4 km | 3/4 | 7 € | Private 🔒 | Open |
| Futsal | Odivelas | Sat 16:00 | 18 km | 10/10 | 4 € | Public | Full |

Selecting a row opens a right-side panel while keeping the list visible.

Filters should support at least:

- sport;
- radius;
- date;
- availability;
- public/private;
- free/paid informational price.

Sorting should support at least:

- distance;
- date/time;
- vacancies;
- price.

## 5. Mobile UX and PWA

The same Angular application serves mobile.

Mobile discovery uses compact cards rather than the desktop table.

The navigation should be designed around:

- Discover;
- Map/location-aware discovery where appropriate;
- Create;
- Chats;
- Profile.

The application should be installable as a PWA with:

- application manifest;
- icons;
- standalone display mode;
- service worker/cache configuration suitable for the app shell.

The PWA is not a separate product or codebase.

## 6. Domain model changes

The current `Session.players` string cannot represent real membership and must no longer be the source of truth for capacity.

### 6.1 Session

Expected core fields:

- `id`;
- `creatorProfileId`;
- `sport`;
- `gameDate`;
- `city` / approximate zone;
- `address` / exact location;
- `latitude`;
- `longitude`;
- `maxParticipants`;
- `price` as a numeric monetary value or null/zero for free;
- `privateGame`;
- private-session password hash;
- `gamePhoto`;
- `status` such as OPEN, FULL, CANCELLED, COMPLETED;
- creation/update timestamps.

Do not store private session passwords in plaintext.

### 6.2 SessionParticipant

Represents real membership.

Expected fields:

- `id`;
- `sessionId`;
- `profileId`;
- `joinedAt`;
- membership state if needed.

A database uniqueness rule must prevent duplicate membership for the same session/profile pair.

### 6.3 SessionWaitlist

Expected fields:

- `id`;
- `sessionId`;
- `profileId`;
- `createdAt`;
- ordering position derived from creation/order semantics.

A database uniqueness rule must prevent duplicate waitlist entries for the same session/profile pair.

### 6.4 SessionMessage

Expected fields:

- `id`;
- `sessionId`;
- `senderProfileId`;
- message body;
- timestamp.

Access is authorised from session membership, not from user-supplied profile IDs.

## 7. API direction

The new UI should use coherent session APIs under `/api/sessions` rather than continue adding new `/menu/*` endpoints.

Target API shape:

- `GET /api/sessions` — discovery/search;
- `GET /api/sessions/{id}` — session detail with fields filtered by caller membership;
- `POST /api/sessions` — create session;
- `POST /api/sessions/{id}/join` — join public session or join private session with password;
- `POST /api/sessions/{id}/leave`;
- `POST /api/sessions/{id}/cancel`;
- `DELETE /api/sessions/{id}/participants/{participantId}` — creator removal;
- `GET /api/sessions/{id}/participants`;
- `GET /api/sessions/{id}/waitlist` where authorised;
- `GET /api/sessions/{id}/messages`;
- `POST /api/sessions/{id}/messages`.

Exact request/response DTOs will be defined in the implementation plan.

The backend must derive the acting user from authenticated identity/JWT rather than trusting a username/profile ID sent by the client for protected actions.

## 8. Discovery and distance

Discovery accepts caller coordinates and an optional radius.

The backend calculates distance for filtering/ordering or uses a suitable database/geospatial approach if justified during implementation.

The MVP only requires accurate enough nearby-session ordering and radius filtering. It does not require a full GIS subsystem.

Exact address must not be serialized to unauthorised/non-participant callers.

## 9. Security and privacy

Before public deployment:

- stop tracking real `.env`, `.env.dev`, `.env.prod`, and `.env.test` secret files;
- add a safe `.env.example`;
- move runtime secrets to hosting environment variables/secrets;
- rotate credentials/secrets that have been committed historically where applicable;
- remove hardcoded sensitive secrets from `application.properties`;
- hash private-session passwords;
- enforce session permissions in the backend;
- do not rely on frontend hiding for address/chat/private-session security;
- validate uploaded images by type and size;
- restrict CORS to deployed frontend origins in production.

Any public deployment must happen only after this security cleanup.

## 10. Modernisation direction

The relaunch should modernise the existing stack rather than replace it with a different architecture.

Frontend:

- upgrade Angular from the existing Angular 15 baseline to a currently supported Angular version through a safe migration path;
- retain Angular unless an upgrade blocker proves disproportionate;
- reorganise large legacy page components into focused feature components/services.

Backend:

- upgrade Spring Boot from the existing 3.0 baseline to a supported release compatible with the chosen Java runtime;
- remove obsolete/conflicting API documentation dependencies where necessary;
- preserve PostgreSQL and JPA;
- keep JWT authentication but tighten identity handling for new protected endpoints.

Version numbers and migration sequence will be pinned in the implementation plan after build compatibility is verified.

## 11. Data migration and compatibility

Existing profiles/accounts must be preserved where feasible.

Existing session records may require migration because the old `players` string does not model actual participant membership.

The implementation should prefer additive schema changes first, then migrate/deprecate old fields when safe.

Legacy frontend endpoints can temporarily coexist during migration, but the new relaunch UI must use the new coherent API.


## 12. Attendance reliability and no-show penalties

The MVP must discourage users from reserving a place and then failing to attend.

### 12.1 Attendance outcome

After a session has finished, the creator can mark each participant as:

- attended;
- did not attend.

The system must keep an auditable attendance record per participant/session.

### 12.2 Cancellation window

A participant can leave a session without penalty until **2 hours before the scheduled start time**.

If the participant leaves less than 2 hours before the start time, the departure is recorded as a **late cancellation**.

A participant who remains enrolled and does not attend is recorded as a **no-show**.

A no-show has a stronger negative effect than a late cancellation.

### 12.3 Progressive consequences

The initial policy is intentionally moderate and should be implemented so thresholds can be changed later.

Recent reliability events should drive consequences approximately as follows:

- 0–1 recent no-shows: no restriction;
- 2 recent no-shows: warning shown to the user;
- 3 recent no-shows: reduced priority where session demand or waitlists require prioritisation;
- 4 or more recent no-shows: temporary restriction on joining new sessions.

Late cancellations contribute less than no-shows.

Exact scoring weights, lookback window and restriction duration must be configurable rather than hardcoded into UI logic.

### 12.4 Decay over time

Negative reliability events must lose weight over time so old mistakes do not permanently damage a profile.

The implementation plan must define a simple time-based lookback/decay policy suitable for the MVP.

### 12.5 Abuse protection

A creator must not be able to arbitrarily damage another user's reputation without traceability.

The system must retain who recorded an attendance outcome and when.

A user marked as a no-show must be able to dispute the outcome.

For the MVP, a dispute may be represented as a reviewable status/record rather than requiring a full moderation back office.

Repeated suspicious attendance reporting by the same creator should be detectable from stored history.

### 12.6 Profile presentation

Reliability should be presented neutrally, for example:

- attendance rate;
- number of completed sessions;
- recent no-shows.

Avoid demeaning labels.

Example:

> Attendance: 94% · 12 completed sessions · 1 no-show

Positive reliable participation should also be visible rather than exposing only penalties.


## 13. Notifications

Reuse the existing notification concept where practical.

The MVP needs notifications for at least:

- waitlist promotion;
- removal from a session;
- session cancellation.

Additional notification types are optional unless needed by an implemented flow.

## 14. Testing requirements

Backend tests must cover at least:

- public join;
- private join with correct password;
- private join with incorrect password;
- duplicate join prevention;
- full-session waitlist placement;
- FIFO promotion after a vacancy;
- creator-only removal;
- creator-only cancellation;
- hidden exact address for non-participants;
- exact address for participants;
- chat denied to non-participants;
- chat allowed to participants;
- penalty-free leave at least 2 hours before start;
- late-cancellation recording inside the 2-hour window;
- no-show recording after the session;
- reliability consequence thresholds;
- time decay/lookback of negative reliability events;
- creator identity/audit data for attendance reports;
- dispute creation for a no-show.

Frontend tests must cover the critical presentation/state transitions for:

- discovery;
- public/private join;
- full/waitlisted state;
- participant-only address visibility;
- responsive desktop/mobile behaviour where practical.

A build of both repositories must pass before any merge.

## 15. Deployment target

The MVP will be publicly accessible.

Target topology:

- Angular/PWA hosted as a static web app;
- Spring Boot API hosted separately;
- managed PostgreSQL database;
- runtime secrets stored outside Git.

The exact providers are intentionally not fixed in this design. Provider choice should optimise for low initial cost, straightforward environment-variable management, TLS, deploy-from-Git, and easy rollback.

## 16. Explicitly out of scope for MVP

- in-app payments;
- MB Way/Stripe integration;
- player skill levels or rankings;
- peer ratings;
- tournaments;
- leagues;
- club/team administration;
- paid subscriptions;
- native Android/iOS codebases;
- creator approval in addition to a private-session password;
- exposing exact addresses before joining.

## 17. Delivery decomposition

Implementation should be performed as vertical, testable slices rather than one large rewrite:

1. **Foundation and security** — supported stack baseline, secrets cleanup, build health.
2. **Session domain** — real participant/waitlist model and secure APIs.
3. **Discovery UI** — desktop server-browser list and mobile cards with proximity.
4. **Join/private/waitlist flows** — end-to-end integration.
5. **Session management and notifications** — leave, remove, cancel, promotion.
6. **Session chat** — participant-only chat.
7. **PWA and public deployment** — installability, production configuration and release verification.

Each slice must preserve a runnable application and be independently reviewable.

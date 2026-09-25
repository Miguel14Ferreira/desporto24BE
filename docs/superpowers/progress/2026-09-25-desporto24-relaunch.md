# SDD ledger — plan: docs/superpowers/plans/2026-09-25-desporto24-relaunch.md

Execution mode: native/inline because this harness exposes no subagent execution tool.

Pre-flight ruling: the GitHub feature branches are the isolated workspace. Local git worktrees cannot be created because the container has no network route to the private GitHub repositories. Cost if wrong: repository edits are still isolated on feature/desporto24-relaunch and can be reverted by commit.

Pre-flight ruling: feature-branch GitHub Actions workflows are used as the test runner because the local container cannot clone the private repositories. No task is marked complete without a successful CI run or an explicit recorded limitation. Cost if wrong: CI behavior could differ from a developer workstation, but it provides reproducible clean-environment compilation/tests.

Pre-flight shared interfaces:
- Tasks 1→2: Spring Boot/Security baseline consumed by password/JWT/MFA hardening — compatible; Task 2 builds on upgraded SecurityConfiguration.
- Tasks 2→4/5/7/8: authenticated identity/JWT is consumed by all protected session APIs — compatible; acting user is Authentication.getName().
- Tasks 3→4→5→6→7→8: session domain entities are successively consumed by discovery, join, lifecycle, reliability and chat — compatible; no conflicting type names found.
- Tasks 4/5→10/11/12: backend DTO/API contracts are consumed by typed Angular session services and UI — compatible.
- Task 7→12/13: reliability endpoints/state are consumed by creator attendance and profile/dispute UI — compatible.
- Task 8→13: session chat/notifications are consumed by frontend chat/notification center — compatible.
- Tasks 9→10→11→12→13: Angular 22 baseline is consumed by all new frontend feature modules/components — compatible.
- Task 14→15→16: media/storage and schema changes are consumed by Liquibase/production deployment — compatible.


Task 1 baseline:
- Backend baseline CI run 36120438172: PASS on Java 17 with ./mvnw -q test.
- Frontend baseline CI run 36120439320: FAIL before tests at npm ci because @ngneat/query@2.0.0-beta.8 requires @angular/core >=16 <18 while the repo is Angular 15.2.0. This is pre-existing and is assigned to Task 9; no --legacy-peer-deps bypass will be used.

Task 1: Ruling: Spring Boot 4 removed the old com.sun.mail SMTP implementation from the dependency path; replace the implementation-specific SMTPTransport usage with standard jakarta.mail.Transport — preserves behavior while decoupling from the mail provider — cost if wrong: email transport behavior would fail at runtime and must be covered by later mail integration testing.

Task 1 verification:
- Upgrade commit b4f091e4c794e1a9ae0a45f40c3e5e3ac00d3b02 initially failed CI run 36120638694 at compilation with missing com.sun.mail.smtp.
- Fix commit 003dcb3b9bc7a0cc44cde56ca8521a0860389657 passed CI run 36120793781.
- Final clean verification at commit 44483cdd459a1edeaca4ab18d50af7a9d545f47c: CI run 36120887661 PASS using Java 21 and ./mvnw -q clean test.

Task 1: complete (commits bb874bd..44483cd, tests: ./mvnw -q clean test → PASS in GitHub Actions run 36120887661)


Task 2: Ruling: MFA challenge consumption must be atomic before JWT issuance, not only sequentially single-use — repository conditional UPDATE closes concurrent replay window — cost if wrong: a race could otherwise issue more than one JWT for the same MFA challenge.

Task 2: Ruling: remove tracked runtime secrets now, but do not rotate AES_ENCRYPTION_KEY blindly before a data re-encryption migration exists — existing encrypted phone/address values depend on the old key — cost if wrong: rotating it directly would make existing encrypted PII unreadable.

Task 2 verification:
- Existing relaunch security work already present on the feature branch covered BCrypt password migration, MFA challenge DTOs, secret externalization, tracked .env deletion, and legacy JWT removal.
- CI run 36124184888 passed the complete suite after removing an obsolete Mockito stub.
- Added an atomic MFA replay regression test at commit 6a2fc4712f9550d4f343f3ad6640b5069fda7dd9; CI run 36124322057 failed at test compilation because confirmIfPending did not yet exist (expected RED).
- Implemented repository-level conditional MFA confirmation and consumed the challenge before JWT issuance.
- Final clean verification at commit 73ad84d77c8d2ebc4badccbba8ff3c5089a74227: CI run 36124476936 PASS using Java 21 and ./mvnw -q clean test.

Task 2: complete (commits 5d14bcb..73ad84d, tests: ./mvnw -q clean test → PASS in GitHub Actions run 36124476936)


Task 3: Ruling: use Spring Boot 4's focused spring-boot-data-jpa-test module plus H2 in test scope for repository constraint tests — production remains PostgreSQL — cost if wrong: H2 cannot prove PostgreSQL-specific DDL semantics, which are covered separately by Task 15's real PostgreSQL migration tests.

Task 3: Ruling: H2 constraint tests use LEGACY compatibility mode rather than PostgreSQL mode because the existing legacy Profile.statusId ordinal mapping produces TINYINT, which H2 PostgreSQL mode rejects before the new domain tables can be tested — no production mapping is changed just to satisfy the test database — cost if wrong: a DB-specific mismatch could escape this slice, again covered by Task 15.

Task 3 verification:
- RED CI run 36124729672 failed because the new session entities/enums/repositories did not exist.
- Full-domain CI run 36125038459 reached Hibernate schema generation but failed because H2 PostgreSQL mode rejected legacy TINYINT on Profile.statusId; root cause confirmed from generated DDL.
- Final clean verification at commit 78d80cbf69b537ecb95092156163dd1dac515d11: CI run 36125336867 PASS using Java 21 and ./mvnw -q clean test.
- SessionParticipant and SessionWaitlist enforce unique (session_id, profile_id) membership; AttendanceDispute and SessionMessage mappings are present; Session keeps legacy columns while adding relaunch fields.

Task 3: complete (commits 18ef188..78d80cb, tests: ./mvnw -q clean test → PASS in GitHub Actions run 36125336867)


Task 4 verification:
- RED CI run 36125898287 failed because SessionViewMapper did not yet exist (expected RED from the privacy test).
- Implemented SessionViewMapper, authenticated creation/detail, proximity discovery, /api/sessions/mine, creator membership, password hashing for private-session creation, and repository queries at commit c7335d2e0ffbef6b6c26f88c75e910b387cfb1c9.
- CI run 36169896746 exposed a Java generic-inference compile error in the vacancies comparator; root cause was an untyped lambda inferred as Object.
- Typed the comparator lambda at commit aa5626ebbf7a7f0380d2ecf0b000fda748ed7e3e.
- Final verification: CI run 36169955621 PASS using Java 21 and ./mvnw -q clean test.

Task 4: complete (commits a205307..aa5626e, tests: ./mvnw -q clean test → PASS in GitHub Actions run 36169955621)


Task 5 verification:
- RED commit 65bcf76c8a49adc339fd30d52bc27deb9af38897 added integration coverage for public/private join, wrong password, duplicate join, full-session waitlist and two concurrent joins for one final slot.
- RED CI run 36170207002 failed because JoinState/JoinSessionRequest/JoinSessionResponse did not exist (expected).
- Implemented pessimistic session-row locking, idempotent duplicate joins, BCrypt private-session password validation, participant creation, FULL status and FIFO waitlist insertion at commit 755d9b454280ac34e3f326d93e79e16d86e234e5.
- Final verification: CI run 36170399460 PASS using Java 21 and ./mvnw -q clean test, including the two-thread last-slot integration test.

Task 5: complete (commits 65bcf76..755d9b4, tests: ./mvnw -q clean test → PASS in GitHub Actions run 36170399460)


Task 6 verification:
- RED commit ab7156a1f0143460e857b07965282a31acd20ef5 added lifecycle, authorization, FIFO promotion and rejoin/reactivation tests.
- RED CI run 36170622884 failed because WaitlistPromotionService did not exist (expected).
- Implemented WaitlistPromotionService, leave/cancel/remove endpoints, creator authorization, history-preserving cancellation, promotion, and participant-row reactivation at commit cbbd9145d31784cf57e9245f6d1492cc1eb22191.
- CI run 36170810924 exposed a test-slice wiring failure: SessionJoinServiceTest imported SessionServiceImpl but not the new WaitlistPromotionService dependency. Production code compiled.
- Test context fixed at commit a22a210d7af660d64bf6b90018a5f1393007bc7a.
- Final verification: CI run 36171025430 PASS using Java 21 and ./mvnw -q clean test.

Task 6: complete (commits ab7156a..a22a210, tests: ./mvnw -q clean test → PASS in GitHub Actions run 36171025430)


Task 2B verification — MFA gating:
- POST /api/auth/login returns HTTP 202 for MFA-enabled accounts with challengeId and no JWT.
- Wrong MFA code does not issue JWT.
- Expired MFA challenge does not issue JWT.
- MFA challenge confirmation is single-use through atomic confirmIfPending.
- Fresh CI verification: GitHub Actions run 36171714471 attempt 2 → PASS on current feature branch.


Task 2C verification — JWT consolidation:
- Legacy src/main/java/com/example/desporto24/security/JwtUtils.java is absent from the feature branch.
- Legacy io.jsonwebtoken:jjwt dependency is absent from pom.xml.
- Active authentication filter delegates token parsing/validation to utility/JWTokenProvider (Auth0 java-jwt).
- Fresh CI verification at branch head 6c6aca27e53c83e8280612bc9c842c139ff4f864: GitHub Actions run 36174283569 → PASS.


Task 2D verification — runtime secrets:
- Only .env.example remains tracked; real .env/.env.dev/.env.prod/.env.test files are absent from the feature branch.
- application.properties uses environment placeholders for database credentials, JWT secret and AES key.
- SMTP and Twilio credentials are obtained through RuntimeSecrets from environment variables.
- Added RuntimeConfigurationSecretsTest to reject literal POSTGRES_USERNAME, POSTGRES_PASSWORD and JWT_SECRET values in application-dev/prod/test.properties.
- Red phase: CI run 36174539669 failed as expected because application-dev.properties still contained a literal POSTGRES_USERNAME.
- Fix commit 9b1150b905b03433c0503ca1494b598fe81e20ca removed the literal sensitive values from application-dev/prod/test.properties.
- Green phase: CI run 36174681275 → PASS.


Task 2E final verification — security hardening:
- 2A Passwords: BCrypt for new/reset/changed passwords; legacy AES passwords upgrade on successful login; stored password is not exposed in ProfileDTO.
- 2B MFA: MFA-enabled login returns challenge only; wrong/expired/replayed challenges do not issue JWT; confirmation is single-use.
- 2C JWT: legacy JwtUtils and io.jsonwebtoken:jjwt are absent; JWTokenProvider (Auth0 java-jwt) is the single JWT implementation.
- 2D Secrets: only .env.example is tracked; DB/JWT/AES/mail/Twilio secrets are runtime-provided; profile property files contain no literal POSTGRES_USERNAME, POSTGRES_PASSWORD or JWT_SECRET values.
- Final fresh verification: GitHub Actions run 36174878993 attempt 2 → PASS on ./mvnw -q clean test.

Task 2: complete.


Task 3A verification — session domain base:
- Session includes additive relaunch fields for maxParticipants, numeric price, passwordHash, latitude/longitude, status and updatedAt while legacy columns remain temporarily for compatibility.
- SessionParticipant enforces unique (session_id, profile_id), tracks ACTIVE/LEFT/REMOVED state, attendance status and attendance audit fields.
- SessionWaitlist enforces unique (session_id, profile_id), stores FIFO createdAt and configurable priorityTier.
- SessionStatus, ParticipantState, AttendanceStatus and DisputeStatus enums match the approved design.
- Repository tests cover duplicate participant and duplicate waitlist membership constraints.
- Fresh branch verification: GitHub Actions run 36175251529 → PASS on ./mvnw -q clean test.

Task 3A: complete.


Task 3B verification — disputes and session messages:
- AttendanceDispute contains participant, reporter profile, disputing profile, reason, DisputeStatus, createdAt and resolvedAt.
- SessionMessage contains session, sender profile, body and createdAt; body is constrained to 2000 characters.
- AttendanceDisputeRepository and SessionMessageRepository are present.
- No extra uniqueness constraint was added because the approved Task 3 plan does not require one for disputes or messages.
- Fresh verification: GitHub Actions run 36175381028 attempt 2 → PASS on the current feature branch.

Task 3B: complete.


Task 3C final verification — relaunch session domain:
- 3A domain base verified: additive Session fields, membership/waitlist entities, required enums, repositories and uniqueness constraints.
- 3B dispute/message domain verified: AttendanceDispute and SessionMessage match the approved fields and repository layer.
- Creator is inserted as an ACTIVE participant with PENDING attendance during relaunch session creation and therefore counts toward maxParticipants.
- Final review found one gap: relaunch create() still wrote the legacy Session.username column, contrary to the migration rule that username/players/price/password remain read-only legacy fields.
- RED: SessionCreationServiceTest at commit 3dd651264afdc522887a109eaac23c1292f976ea failed in GitHub Actions run 36175842660 because username was "miguel" instead of null.
- GREEN: commit b687236676d44c68c60ee030e0d6110baecf2b44 removed only the legacy username write; GitHub Actions run 36175997853 → PASS on ./mvnw -q clean test.
- Final scan of SessionServiceImpl shows no setUsername/setPlayers/setPrice/setPassword legacy writes.

Task 3: complete.


Task 4A verification — secure creation and detail:
- POST /api/sessions uses Authentication.getName() and never accepts a creator username from the browser payload.
- Private sessions reject blank passwords and store only passwordHash via PasswordHashService.encode; legacy plaintext Session.password remains unwritten.
- SessionDetailResponse and SessionSummaryResponse expose neither password nor passwordHash.
- SessionViewMapper hides exact address from non-participants and reveals it only to the creator or an ACTIVE participant.
- Existing SessionViewMapperPrivacyTest covers hidden address for a non-participant and visible address for the creator.
- Fresh verification: GitHub Actions run 36176117680 attempt 2 → PASS on ./mvnw -q clean test.

Task 4A: complete.


Task 4B verification — discovery:
- Added SessionDiscoveryServiceTest to characterize the approved discovery behavior.
- Coordinates with no explicit radius apply the 25 km default; sessions beyond 25 km are excluded and remaining results sort by distance.
- A radius is not applied when user coordinates are absent.
- Discovery filters cover sport, partial city match, from/to dates, public/private, free-only and availability.
- Sorting is verified for distance/default, vacancies and price; unknown/explicit date sorting falls back to gameDate.
- Existing candidate repository query limits discovery to future OPEN/FULL sessions ordered by gameDate.
- Characterization tests passed against the existing implementation without production-code changes.
- Fresh verification: GitHub Actions run 36176590995 → PASS on ./mvnw -q clean test.

Task 4B: complete.


Task 4C verification — My Sessions:
- GET /api/sessions/mine uses Authentication.getName() and accepts no browser-supplied username.
- Added SessionMineServiceTest covering created, joined and waitlisted grouping.
- A session created by the user is excluded from the joined group even though the creator is also an ACTIVE participant.
- Joined contains ACTIVE participant sessions not created by the user; waitlisted contains sessions from the user's waitlist entries.
- Characterization test passed against the existing implementation without production-code changes.
- Fresh verification: GitHub Actions run 36176794211 → PASS on ./mvnw -q clean test.

Task 4C: complete.


Task 4D final verification — secure session API:
- 4A creation/detail verified: authenticated creator identity, hashed private password, no plaintext password writes, address hidden unless creator/ACTIVE participant.
- 4B discovery verified: future OPEN/FULL candidates, filters, 25 km default radius with coordinates, no radius filter without coordinates, sorting by distance/vacancies/price/date.
- 4C My Sessions verified: authenticated identity only, correct created/joined/waitlisted grouping, no duplicate creator sessions in joined.
- Final review found one access gap: GET /api/sessions was still authenticated-only, contrary to the approved public discovery flow.
- RED: SecurityConstantTest at commit d9c28272de348e24d33393dae8bd0f319569da04 failed in GitHub Actions run 36177033312 because PUBLIC_GET_URLS did not exist.
- GREEN: SecurityConstant now defines only "/api/sessions" as a public GET path and SecurityConfiguration permits it with requestMatchers(HttpMethod.GET, PUBLIC_GET_URLS), leaving POST /api/sessions and /api/sessions/** actions authenticated.
- Final verification: GitHub Actions run 36177165145 → PASS on ./mvnw -q clean test.
- Final scan confirms the new controller exposes GET /api/sessions, GET /api/sessions/{id}, POST /api/sessions and GET /api/sessions/mine; SessionDetailResponse contains no password/passwordHash field.

Task 4: complete.


Task 5A verification — basic and private join:
- POST /api/sessions/{id}/join uses Authentication.getName() and does not trust a browser-supplied username.
- Public session with capacity creates an ACTIVE participant.
- Private session with the correct password creates a participant; wrong password raises BadCredentialsException and creates neither participant nor waitlist entry.
- Duplicate join is idempotent and never creates a duplicate participant row.
- Existing integration tests exercise the real repositories/service; added controller coverage for authenticated join identity.
- Fresh verification: GitHub Actions run 36177417680 → PASS on ./mvnw -q clean test.

Task 5A: complete.


Task 5B verification — capacity and waitlist:
- Joining the last available slot transitions the session to FULL.
- Joining while capacity still remains keeps the session OPEN.
- A full session places the user on the waitlist and returns position 1.
- Multiple waitlisted users with the same priority tier receive FIFO positions 1, then 2 according to createdAt ordering.
- Session remains FULL while users are waitlisted.
- Fresh verification: GitHub Actions run 36177736635 → PASS on ./mvnw -q clean test.

Task 5B: complete.


Task 5C verification — last-slot concurrency:
- SessionServiceImpl.join is transactional and obtains the session through SessionRepository.findByIdForUpdate before counting active participants.
- SessionRepository.findByIdForUpdate uses @Lock(LockModeType.PESSIMISTIC_WRITE).
- The integration concurrency test starts two threads simultaneously against one final slot.
- Expected/verified result: exactly one join returns PARTICIPANT, one returns WAITLISTED, active participant count equals maxParticipants, and exactly one waitlist row is created.
- Fresh verification: GitHub Actions run 36177844241 → PASS on ./mvnw -q clean test.

Task 5C: complete.


Task 5D final verification — secure join, capacity and waitlist:
- 5A verified public/private join, authenticated identity, wrong-password rejection and idempotent duplicate join.
- 5B verified OPEN/FULL transitions, waitlist insertion and FIFO positions within the same priority tier.
- 5C verified pessimistic locking and the concurrent last-slot scenario with exactly one participant and one waitlisted user.
- POST /api/sessions/{id}/join returns only JoinState and waitlistPosition; neither password nor passwordHash is exposed in the response or exception text.
- Join is @Transactional and obtains the session through findByIdForUpdate(), which uses PESSIMISTIC_WRITE before capacity is counted.
- Final fresh verification: GitHub Actions run 36177989611 attempt 2 → PASS on ./mvnw -q clean test.

Task 5: complete.


Task 6A verification — session lifecycle authorization:
- POST /api/sessions/{id}/leave deactivates the authenticated participant by setting ParticipantState.LEFT and exitedAt; the creator cannot use participant leave and must cancel instead.
- DELETE /api/sessions/{id}/participants/{participantId} requires the session creator; successful removal retains the membership record as ParticipantState.REMOVED.
- POST /api/sessions/{id}/cancel requires the creator, sets SessionStatus.CANCELLED and retains participants/waitlist/history instead of deleting records.
- Added explicit coverage proving a non-creator cannot cancel and the session remains OPEN after the rejected attempt.
- Controller tests verify leave/cancel/remove use Authentication.getName().
- Fresh verification: GitHub Actions run 36178352966 → PASS on ./mvnw -q clean test.

Task 6A: complete.


Task 6B verification — waitlist promotion:
- WaitlistPromotionService promotes the first entry ordered by priorityTier ASC and createdAt ASC.
- Normal priority tier 0 is promoted before reduced priority tier 1 even when the reduced-priority user entered the queue earlier.
- Within the same priority tier, createdAt preserves FIFO behavior.
- Promotion deletes the waitlist entry, activates or reactivates the user's existing SessionParticipant, resets attendance to PENDING and clears exit/attendance audit fields.
- After promotion the session status is recomputed from active participant count: FULL when capacity is reached, OPEN when capacity remains.
- When a vacancy exists and the waitlist is empty, no participant is created and the session is recomputed to OPEN.
- CANCELLED/COMPLETED sessions do not promote waitlist entries.
- Fresh verification: GitHub Actions run 36178567569 → PASS on ./mvnw -q clean test.

Task 6B: complete.

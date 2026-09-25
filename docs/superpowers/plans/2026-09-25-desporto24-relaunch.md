# Desporto24 Relaunch Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Relaunch Desporto24 as a secure, responsive sports-session discovery product with proximity search, public/private joining, waitlists, attendance reliability, participant-only chat, PWA support, and production-ready configuration.

**Architecture:** Evolve the existing Spring Boot/PostgreSQL backend and Angular frontend rather than rewriting them. The backend remains the source of truth for identity, session membership, exact-address visibility, waitlists, attendance penalties and chat permissions; the frontend becomes a responsive server-browser-style PWA that consumes coherent `/api/sessions` APIs. New relaunch code is introduced in focused components/services while legacy routes remain available until each flow is replaced.

**Tech Stack:** Java 21, Spring Boot 4.1.1, Spring Security 7, Spring Data JPA, PostgreSQL 15+, springdoc-openapi 3.1.1, JUnit 5, Mockito, Angular 22, TypeScript 6, RxJS 7, Jasmine/Karma, PWA service worker.

**Spec:** `docs/superpowers/specs/2026-09-25-desporto24-relaunch-design.md`

## Global Constraints

- Work only on `feature/desporto24-relaunch`; never implement directly on `develop` or `master`.
- Preserve existing registration, email confirmation, password recovery, profile, friends, notifications and optional MFA behavior while replacing insecure internals.
- MFA remains optional.
- Exact session address is hidden until the caller is the creator or an active participant.
- Public sessions join immediately while capacity exists.
- Private sessions are visible but require the correct password to join; private-session passwords are never returned by the API and are stored only as BCrypt hashes.
- Full sessions place users on a FIFO waitlist, with reliability priority applied as defined below.
- Creator can remove participants and cancel a session.
- Leaving at least 2 hours before the start is penalty-free; leaving inside 2 hours is a late cancellation; remaining enrolled and not attending is a no-show.
- Price is informational only; no in-app payment processing.
- Player skill levels are out of scope.
- One responsive Angular application serves desktop and mobile and is installable as a PWA.
- Use Angular 22.x with a Node version compatible with Angular 22; use Node 22.x at or above 22.22.3 for the final build.
- Use Spring Boot 4.1.1 with Java 21.
- Production secrets must not be committed to Git.
- New backend authorization derives the acting user from Spring Security `Authentication`, never from a username supplied by the browser.
- Every task ends with passing focused tests and a commit in the repository it changes.

## Review Focus

1. **Two users join the last free place concurrently:** exactly one becomes a participant; the other becomes waitlisted; capacity is never exceeded. Covered in Task 5.
2. **Unauthenticated/non-participant opens a private session with a wrong password or direct API request:** exact address and password hash remain absent. Covered in Tasks 4 and 5.
3. **User leaves exactly on the two-hour boundary:** `>= 2h` remaining is penalty-free; `< 2h` is late cancellation. Covered in Task 7.
4. **A no-show is disputed:** an OPEN dispute suspends that no-show from the reliability score until resolved. Covered in Task 7.
5. **Browser geolocation is denied/unavailable:** discovery remains usable through manual city/zone search and never throws a UI error. Covered in Tasks 10 and 11.

---

## Task 1: Establish Supported Build Baselines

**Files:**
- Modify BE: `pom.xml`
- Modify BE: `src/main/java/com/example/desporto24/configuration/SecurityConfiguration.java`
- Modify BE: `Dockerfile`
- Modify FE: `package.json`
- Modify FE: `package-lock.json`
- Test: BE full Maven build
- Test: FE full Angular test/build

**Interfaces:**
- Consumes: current applications without behavior changes.
- Produces: backend building on Java 21/Spring Boot 4.1.1 and a documented frontend upgrade path that Task 9 completes.

- [ ] **Step 1: Record the current build on the feature branches**

Run in BE:
```bash
./mvnw -q test
```

Run in FE:
```bash
npm ci
npm test -- --watch=false --browsers=ChromeHeadless
npm run build
```

Expected: either PASS, or any pre-existing failures are captured in the task notes before dependency edits. Do not silently classify a new failure as pre-existing.

- [ ] **Step 2: Upgrade the backend parent and Java baseline**

Change the relevant `pom.xml` sections to:

```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>4.1.1</version>
    <relativePath/>
</parent>

<properties>
    <java.version>21</java.version>
</properties>
```

Remove:
```xml
<dependency>
    <groupId>io.springfox</groupId>
    <artifactId>springfox-boot-starter</artifactId>
    <version>3.0.0</version>
</dependency>
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-ui</artifactId>
    <version>1.7.0</version>
</dependency>
<dependency>
    <groupId>org.eclipse.microprofile.openapi</groupId>
    <artifactId>microprofile-openapi-api</artifactId>
    <version>2.0.1</version>
</dependency>
```

Add:
```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>3.1.1</version>
</dependency>
```

Remove the explicit version from `spring-boot-starter-websocket` so it follows the Spring Boot BOM.

- [ ] **Step 3: Migrate Spring Security configuration to the Spring Security 7 DSL**

Use this shape in `SecurityConfiguration.configure`:

```java
@Bean
SecurityFilterChain configure(HttpSecurity http) throws Exception {
    return http
        .csrf(csrf -> csrf.disable())
        .cors(Customizer.withDefaults())
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(auth -> auth
            .requestMatchers(PUBLIC_URLS).permitAll()
            .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
            .anyRequest().authenticated())
        .exceptionHandling(ex -> ex
            .accessDeniedHandler(jwtAccessDeniedHandler)
            .authenticationEntryPoint(jwtAuthenticationEntryPoint))
        .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)
        .build();
}
```

Keep the existing OpenAPI bean, and remove Springfox imports/beans.

- [ ] **Step 4: Make the backend Docker image use Java 21**

Use a Java 21 build/runtime image and keep the service listening on `server.port`:

```dockerfile
FROM eclipse-temurin:21-jdk AS build
WORKDIR /workspace
COPY . .
RUN ./mvnw -q -DskipTests package

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /workspace/target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app/app.jar"]
```

- [ ] **Step 5: Run the backend build**

Run:
```bash
./mvnw clean test
```

Expected: PASS on Java 21.

- [ ] **Step 6: Commit backend baseline**

```bash
git add pom.xml Dockerfile src/main/java/com/example/desporto24/configuration/SecurityConfiguration.java
git commit -m "build: upgrade backend runtime baseline"
```

Do not upgrade Angular in this task beyond dependency metadata needed to keep the current FE build reproducible; the sequential Angular migration is Task 9.

---

## Task 2: Remove Committed Secrets and Migrate User Passwords to BCrypt

**Files:**
- Modify BE: `.gitignore`
- Delete BE: `.env`
- Delete BE: `.env.dev`
- Delete BE: `.env.prod`
- Delete BE: `.env.test`
- Create BE: `.env.example`
- Modify BE: `src/main/resources/application.properties`
- Create BE: `src/main/java/com/example/desporto24/security/PasswordHashService.java`
- Modify BE: `src/main/java/com/example/desporto24/service/LoginAttemptService.java`
- Modify BE: `src/main/java/com/example/desporto24/mapper/ProfileMapperImpl.java`
- Modify BE: `src/main/java/com/example/desporto24/service/impl/AuthenticationServiceImpl.java`
- Modify BE: `src/main/java/com/example/desporto24/service/impl/ProfileServiceImpl.java`
- Test BE: `src/test/java/com/example/desporto24/security/PasswordHashServiceTest.java`
- Test BE: `src/test/java/com/example/desporto24/mapper/ProfileMapperImplTest.java`

**Interfaces:**
- Produces: `PasswordHashService.matchesAndUpgrade(Profile, String): boolean`; new registrations and password changes persist BCrypt; legacy AES password is transparently upgraded after a successful login.
- Existing AES encryption remains only for legacy encrypted PII such as phone/address and its key comes exclusively from `AES_ENCRYPTION_KEY`.

- [ ] **Step 1: Write password migration tests**

Create:

```java
class PasswordHashServiceTest {
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    private ProfileRepository repository;
    private PasswordHashService service;

    @BeforeEach
    void setUp() {
        repository = mock(ProfileRepository.class);
        service = new PasswordHashService(repository, encoder);
    }

    @Test
    void matchesBcryptWithoutRewritingIt() {
        Profile p = new Profile();
        p.setPassword(encoder.encode("secret"));
        assertTrue(service.matchesAndUpgrade(p, "secret"));
        verifyNoInteractions(repository);
    }

    @Test
    void rejectsWrongBcryptPassword() {
        Profile p = new Profile();
        p.setPassword(encoder.encode("secret"));
        assertFalse(service.matchesAndUpgrade(p, "wrong"));
        verifyNoInteractions(repository);
    }
}
```

Add a legacy-AES test using a generated `userId` and the existing `AES.encryptString`; assert successful validation rewrites `profile.password` to a string beginning with `$2` and calls `repository.save(profile)`.

- [ ] **Step 2: Run the test to verify the new service is missing**

Run:
```bash
./mvnw -Dtest=PasswordHashServiceTest test
```

Expected: FAIL because `PasswordHashService` does not exist.

- [ ] **Step 3: Implement `PasswordHashService`**

```java
@Service
@RequiredArgsConstructor
public class PasswordHashService {
    private final ProfileRepository profileRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public boolean matchesAndUpgrade(Profile profile, String rawPassword) {
        String stored = profile.getPassword();
        if (stored != null && stored.startsWith("$2")) {
            return passwordEncoder.matches(rawPassword, stored);
        }

        String legacy = AES.decryptString(stored, profile.getUserId());
        boolean matches = MessageDigest.isEqual(
            legacy.getBytes(StandardCharsets.UTF_8),
            rawPassword.getBytes(StandardCharsets.UTF_8)
        );
        if (matches) {
            profile.setPassword(passwordEncoder.encode(rawPassword));
            profileRepository.save(profile);
        }
        return matches;
    }

    public String encode(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }
}
```

Expose one `BCryptPasswordEncoder` bean from `SecurityConfiguration`.

- [ ] **Step 4: Replace password encryption call sites**

In `LoginAttemptService.handleLoginAttempt`, replace AES decryption comparison with:

```java
if (!profile.getUsername().equals(username) || !passwordHashService.matchesAndUpgrade(profile, password)) {
    handleInvalidLogin(profile);
}
```

In `ProfileMapperImpl.createNewProfile`:

```java
profile.setPassword(passwordHashService.encode(profileDTO.getPassword()));
```

In `AuthenticationServiceImpl.updatePassword` and `ProfileServiceImpl.changeUsernameAndPassword`, encode the new password with `PasswordHashService.encode`.

In `ProfileMapperImpl.toDto`, do not copy the stored password:

```java
profileDTO.setPassword(null);
```

- [ ] **Step 5: Remove secrets from tracked files**

Append to `.gitignore`:

```gitignore
.env
.env.*
!.env.example
```

Create `.env.example` containing names only:

```dotenv
ACTIVE_PROFILE=dev
UI_APP_URL=http://localhost:4200
POSTGRES_HOST=localhost
POSTGRES_PORT=5432
POSTGRES_DATABASE=desporto24
POSTGRES_USERNAME=postgres
POSTGRES_PASSWORD=
JWT_SECRET=
AES_ENCRYPTION_KEY=
VERIFY_EMAIL_HOST=http://localhost:4200
CONTAINER_PORT=8080
```

Change `application.properties` secrets to:

```properties
desporto.app.jwtSecret=${JWT_SECRET}
aes.encryption.key=${AES_ENCRYPTION_KEY}
```

Remove the duplicate `jwt.secret` property if no runtime code reads it.

Delete the tracked `.env*` files listed above from Git.

- [ ] **Step 6: Run focused and full backend tests**

```bash
./mvnw -Dtest=PasswordHashServiceTest,ProfileMapperImplTest test
./mvnw test
```

Expected: PASS.

- [ ] **Step 7: Commit**

```bash
git add .gitignore .env.example pom.xml src/main src/test
git rm --cached .env .env.dev .env.prod .env.test
git commit -m "security: migrate passwords and externalize secrets"
```

After this commit, rotate the previously committed JWT/AES/database/provider secrets before any public deployment. Do not print their previous values in logs or PR text.

---

## Task 3: Add the Relaunch Session Domain Model

**Files:**
- Modify BE: `src/main/java/com/example/desporto24/model/Session.java`
- Create BE: `src/main/java/com/example/desporto24/model/session/SessionParticipant.java`
- Create BE: `src/main/java/com/example/desporto24/model/session/SessionWaitlist.java`
- Create BE: `src/main/java/com/example/desporto24/model/session/AttendanceDispute.java`
- Create BE: `src/main/java/com/example/desporto24/model/session/SessionMessage.java`
- Create BE: `src/main/java/com/example/desporto24/enumeration/SessionStatus.java`
- Create BE: `src/main/java/com/example/desporto24/enumeration/ParticipantState.java`
- Create BE: `src/main/java/com/example/desporto24/enumeration/AttendanceStatus.java`
- Create BE: `src/main/java/com/example/desporto24/enumeration/DisputeStatus.java`
- Create BE repositories under `src/main/java/com/example/desporto24/repository/session/`
- Test BE: `src/test/java/com/example/desporto24/repository/session/SessionDomainRepositoryTest.java`

**Interfaces:**
- Produces: relational participant/waitlist/message/dispute records linked by `Session` and `Profile`.
- Creator is automatically a participant when creating a session and counts toward `maxParticipants`.

- [ ] **Step 1: Write repository constraints test**

Use `@DataJpaTest` and save one profile, one session, then two `SessionParticipant` rows with the same session/profile pair.

```java
@Test
void participantMembershipIsUniquePerSessionAndProfile() {
    participantRepository.saveAndFlush(participant(session, profile));
    assertThrows(DataIntegrityViolationException.class,
        () -> participantRepository.saveAndFlush(participant(session, profile)));
}
```

Repeat the same assertion for `SessionWaitlist`.

- [ ] **Step 2: Run the repository test**

```bash
./mvnw -Dtest=SessionDomainRepositoryTest test
```

Expected: FAIL because the new entities/repositories do not exist.

- [ ] **Step 3: Add enums**

```java
public enum SessionStatus { OPEN, FULL, CANCELLED, COMPLETED }
public enum ParticipantState { ACTIVE, LEFT, REMOVED }
public enum AttendanceStatus { PENDING, ATTENDED, NO_SHOW, LATE_CANCELLED }
public enum DisputeStatus { OPEN, ACCEPTED, REJECTED }
```

- [ ] **Step 4: Extend `Session` without dropping legacy columns yet**

Add fields:

```java
@Column(name = "max_participants")
private Integer maxParticipants;

@Column(name = "price_amount", precision = 10, scale = 2)
private BigDecimal priceAmount;

@Column(name = "password_hash")
private String passwordHash;

private Double latitude;
private Double longitude;

@Enumerated(EnumType.STRING)
private SessionStatus status;

@Column(name = "updated_at")
private Instant updatedAt;
```

Keep legacy `players`, `price`, `password`, `username` columns temporarily for migration compatibility, but new relaunch code must not write them.

- [ ] **Step 5: Add participant and waitlist entities**

`SessionParticipant` must include:

```java
@Entity
@Table(name = "session_participant",
       uniqueConstraints = @UniqueConstraint(
           name = "uk_session_participant_session_profile",
           columnNames = {"session_id", "profile_id"}))
public class SessionParticipant {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    private Session session;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", nullable = false)
    private Profile profile;

    @Enumerated(EnumType.STRING)
    private ParticipantState state;

    @Enumerated(EnumType.STRING)
    private AttendanceStatus attendanceStatus;

    private Instant joinedAt;
    private Instant exitedAt;
}
```

`SessionWaitlist` uses the same unique-key pattern and fields `createdAt` plus `priorityTier` where `0` is normal and `1` is reduced priority.

- [ ] **Step 6: Add attendance-dispute and session-message entities**

`AttendanceDispute` fields: `id`, participant relation, reporter profile, disputing profile, `reason`, `status`, `createdAt`, `resolvedAt`.

`SessionMessage` fields: `id`, session relation, sender profile relation, `body` limited to 2000 characters, `createdAt`.

- [ ] **Step 7: Add repositories**

Required methods:

```java
Optional<SessionParticipant> findBySessionIdAndProfileIdAndState(Long sessionId, Long profileId, ParticipantState state);
long countBySessionIdAndState(Long sessionId, ParticipantState state);
boolean existsBySessionIdAndProfileIdAndState(Long sessionId, Long profileId, ParticipantState state);
List<SessionParticipant> findBySessionIdAndStateOrderByJoinedAtAsc(Long sessionId, ParticipantState state);
```

```java
Optional<SessionWaitlist> findBySessionIdAndProfileId(Long sessionId, Long profileId);
Optional<SessionWaitlist> findFirstBySessionIdOrderByPriorityTierAscCreatedAtAsc(Long sessionId);
List<SessionWaitlist> findBySessionIdOrderByPriorityTierAscCreatedAtAsc(Long sessionId);
```

- [ ] **Step 8: Run repository tests**

```bash
./mvnw -Dtest=SessionDomainRepositoryTest test
```

Expected: PASS.

- [ ] **Step 9: Commit**

```bash
git add src/main/java/com/example/desporto24/model src/main/java/com/example/desporto24/enumeration src/main/java/com/example/desporto24/repository src/test
git commit -m "feat: add session membership domain"
```

---

## Task 4: Build Secure Session Creation, Detail and Discovery APIs

**Files:**
- Modify BE: `src/main/java/com/example/desporto24/controllers/SessionController.java`
- Replace BE legacy contract: `src/main/java/com/example/desporto24/service/interfaces/SessionService.java`
- Refactor BE: `src/main/java/com/example/desporto24/service/impl/SessionServiceImpl.java`
- Create BE DTOs under `src/main/java/com/example/desporto24/model/dto/session/`
- Create BE: `src/main/java/com/example/desporto24/service/SessionViewMapper.java`
- Create BE: `src/main/java/com/example/desporto24/service/SessionDistanceCalculator.java`
- Modify BE: `src/main/java/com/example/desporto24/repository/SessionRepository.java`
- Test BE: `src/test/java/com/example/desporto24/service/SessionDistanceCalculatorTest.java`
- Test BE: `src/test/java/com/example/desporto24/controllers/SessionControllerTest.java`

**Interfaces:**
- Produces:
  - `GET /api/sessions`
  - `GET /api/sessions/{id}`
  - `POST /api/sessions`
  - `SessionSummaryResponse`, `SessionDetailResponse`, `CreateSessionRequest`, `SessionSearchCriteria`
- Exact address returned only when `canSeeExactAddress=true`.

- [ ] **Step 1: Write distance and privacy tests**

Distance unit test:

```java
@Test
void calculatesZeroForSamePoint() {
    assertEquals(0.0, calculator.kilometres(38.766, -9.297, 38.766, -9.297), 0.001);
}
```

Controller/service privacy test must assert:

```java
assertNull(response.address());
assertNotNull(response.city());
assertFalse(response.canSeeExactAddress());
```

for a non-participant, and non-null address for creator/active participant.

- [ ] **Step 2: Run tests**

```bash
./mvnw -Dtest=SessionDistanceCalculatorTest,SessionControllerTest test
```

Expected: FAIL because the new DTOs and mapper are missing.

- [ ] **Step 3: Create request/response DTO contracts**

```java
public record CreateSessionRequest(
    @NotBlank String sport,
    @NotNull Instant gameDate,
    @NotBlank String city,
    @NotBlank String address,
    @NotNull @Min(2) @Max(100) Integer maxParticipants,
    @DecimalMin("0.00") BigDecimal priceAmount,
    boolean privateGame,
    String password,
    Double latitude,
    Double longitude
) {}
```

```java
public record SessionSummaryResponse(
    Long id,
    String sport,
    String city,
    Instant gameDate,
    Double distanceKm,
    int participantCount,
    int maxParticipants,
    BigDecimal priceAmount,
    boolean privateGame,
    SessionStatus status
) {}
```

`SessionDetailResponse` contains the summary fields plus creator public identity, optional `address`, `canSeeExactAddress`, `joined`, `waitlisted`, `waitlistPosition`, and image URL. It contains neither `password` nor `passwordHash`.

- [ ] **Step 4: Implement creation from authenticated identity**

Controller signature:

```java
@PostMapping
public ResponseEntity<SessionDetailResponse> create(
        @Valid @RequestBody CreateSessionRequest request,
        Authentication authentication) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(sessionService.create(request, authentication.getName()));
}
```

The service:
1. loads creator by authenticated username;
2. validates private session has non-blank password;
3. encodes private password using `PasswordHashService.encode`;
4. saves session as `OPEN`;
5. creates creator `SessionParticipant` with `ACTIVE/PENDING`;
6. never writes legacy plaintext password.

- [ ] **Step 5: Implement detail privacy**

`SessionViewMapper.toDetail(session, viewer)` computes:

```java
boolean creator = session.getProfile().getId().equals(viewer.getId());
boolean participant = participantRepository
    .existsBySessionIdAndProfileIdAndState(session.getId(), viewer.getId(), ParticipantState.ACTIVE);
boolean canSeeAddress = creator || participant;
String address = canSeeAddress ? session.getAddress() : null;
```

Unauthenticated discovery may return summaries only. Authenticated detail uses the rules above.

- [ ] **Step 6: Implement discovery**

`GET /api/sessions` query parameters:
`sport`, `city`, `from`, `to`, `latitude`, `longitude`, `radiusKm`, `availability`, `privateGame`, `freeOnly`, `sort`.

Repository candidate query returns future `OPEN/FULL` sessions ordered by `gameDate`. Service applies `SessionDistanceCalculator` when coordinates are supplied and filters `distance <= radiusKm`.

Default `radiusKm` is 25 when coordinates are present; no radius filter is applied when coordinates are absent.

- [ ] **Step 7: Run tests**

```bash
./mvnw -Dtest=SessionDistanceCalculatorTest,SessionControllerTest test
```

Expected: PASS, including hidden address for non-participants.

- [ ] **Step 8: Commit**

```bash
git add src/main/java/com/example/desporto24/controllers/SessionController.java src/main/java/com/example/desporto24/service src/main/java/com/example/desporto24/model/dto/session src/main/java/com/example/desporto24/repository/SessionRepository.java src/test
git commit -m "feat: add secure session discovery api"
```

---

## Task 5: Implement Join, Private Password, Capacity Locking and Waitlist

**Files:**
- Modify BE: `SessionController.java`
- Modify BE: `SessionService.java`
- Modify BE: `SessionServiceImpl.java`
- Modify BE: `SessionRepository.java`
- Create BE: `src/main/java/com/example/desporto24/model/dto/session/JoinSessionRequest.java`
- Create BE: `src/main/java/com/example/desporto24/model/dto/session/JoinSessionResponse.java`
- Create BE: `src/main/java/com/example/desporto24/enumeration/JoinState.java`
- Test BE: `src/test/java/com/example/desporto24/service/SessionJoinServiceTest.java`

**Interfaces:**
- Produces: `POST /api/sessions/{id}/join` and `JoinState.PARTICIPANT|WAITLISTED`.
- Uses a pessimistic database lock on the session row during join/promotion.

- [ ] **Step 1: Write join tests**

Cover:
- public session with capacity -> participant;
- private correct password -> participant;
- private incorrect password -> exception and no membership;
- duplicate join -> idempotent response or domain conflict, never duplicate row;
- full session -> waitlist;
- two concurrent joins for one final slot -> one participant, one waitlist entry.

Concurrency assertion:

```java
assertEquals(maxParticipants,
    participantRepository.countBySessionIdAndState(sessionId, ParticipantState.ACTIVE));
assertEquals(1, waitlistRepository.findBySessionIdOrderByPriorityTierAscCreatedAtAsc(sessionId).size());
```

- [ ] **Step 2: Run tests**

```bash
./mvnw -Dtest=SessionJoinServiceTest test
```

Expected: FAIL.

- [ ] **Step 3: Add locked repository lookup**

```java
@Lock(LockModeType.PESSIMISTIC_WRITE)
@Query("select s from Session s where s.id = :id")
Optional<Session> findByIdForUpdate(@Param("id") Long id);
```

- [ ] **Step 4: Implement transactional join**

```java
@Transactional
public JoinSessionResponse join(Long sessionId, JoinSessionRequest request, String username) {
    Session session = sessionRepository.findByIdForUpdate(sessionId)
        .orElseThrow(() -> new SessionNotFound("Session not found"));
    Profile profile = profileRepository.findUserByUsername(username)
        .orElseThrow(() -> new UserNotFoundException(NO_USER_FOUND_BY_USERNAME));

    joinPolicy.assertCanJoin(session, profile);

    if (session.getPrivateGame()
        && !passwordEncoder.matches(request.password(), session.getPasswordHash())) {
        throw new BadCredentialsException("Invalid session password");
    }

    long active = participantRepository.countBySessionIdAndState(sessionId, ParticipantState.ACTIVE);
    if (active < session.getMaxParticipants()) {
        return addParticipant(session, profile);
    }
    return addToWaitlist(session, profile);
}
```

Never include password/hash in the response or exception.

- [ ] **Step 5: Update status after join**

When active count reaches max capacity, set `SessionStatus.FULL`; otherwise keep `OPEN`.

- [ ] **Step 6: Run tests including concurrency test**

```bash
./mvnw -Dtest=SessionJoinServiceTest test
```

Expected: PASS.

- [ ] **Step 7: Commit**

```bash
git add src/main src/test
git commit -m "feat: add secure join and waitlist flow"
```

---

## Task 6: Implement Leave, Participant Removal, Cancellation and FIFO Promotion

**Files:**
- Modify BE session controller/service/repositories
- Create BE: `src/main/java/com/example/desporto24/service/WaitlistPromotionService.java`
- Test BE: `src/test/java/com/example/desporto24/service/WaitlistPromotionServiceTest.java`
- Test BE: `src/test/java/com/example/desporto24/controllers/SessionManagementControllerTest.java`

**Interfaces:**
- Produces:
  - `POST /api/sessions/{id}/leave`
  - `POST /api/sessions/{id}/cancel`
  - `DELETE /api/sessions/{id}/participants/{participantId}`
- `WaitlistPromotionService.promoteNext(Session, Instant): Optional<SessionParticipant>`.

- [ ] **Step 1: Write promotion and authorization tests**

Assert:
- leaving an OPEN/FULL session deactivates membership;
- creator cannot use participant leave to orphan ownership; creator uses cancel;
- removing participant as non-creator returns forbidden;
- creator removal works;
- first waitlisted eligible profile is promoted;
- cancelled session cannot promote or accept joins.

- [ ] **Step 2: Run tests**

```bash
./mvnw -Dtest=WaitlistPromotionServiceTest,SessionManagementControllerTest test
```

Expected: FAIL.

- [ ] **Step 3: Implement FIFO promotion**

```java
@Transactional
public Optional<SessionParticipant> promoteNext(Session session, Instant now) {
    return waitlistRepository
        .findFirstBySessionIdOrderByPriorityTierAscCreatedAtAsc(session.getId())
        .map(entry -> {
            waitlistRepository.delete(entry);
            SessionParticipant p = SessionParticipant.builder()
                .session(session)
                .profile(entry.getProfile())
                .state(ParticipantState.ACTIVE)
                .attendanceStatus(AttendanceStatus.PENDING)
                .joinedAt(now)
                .build();
            return participantRepository.save(p);
        });
}
```

After promotion, recompute `OPEN/FULL`.

- [ ] **Step 4: Implement creator authorization**

Use a single helper:

```java
private void requireCreator(Session session, Profile actor) {
    if (!session.getProfile().getId().equals(actor.getId())) {
        throw new AccessDeniedException("Only the session creator can perform this action");
    }
}
```

- [ ] **Step 5: Implement cancellation**

Cancellation sets `SessionStatus.CANCELLED`; it does not delete the session, participants, waitlist or messages.

- [ ] **Step 6: Run tests**

```bash
./mvnw -Dtest=WaitlistPromotionServiceTest,SessionManagementControllerTest test
```

Expected: PASS.

- [ ] **Step 7: Commit**

```bash
git add src/main src/test
git commit -m "feat: add session lifecycle management"
```

---

## Task 7: Add Attendance Reliability, Two-Hour Cancellation Rule and Disputes

**Files:**
- Create BE: `src/main/java/com/example/desporto24/configuration/ReliabilityPolicyProperties.java`
- Create BE: `src/main/java/com/example/desporto24/service/ReliabilityService.java`
- Create BE DTOs: `ReliabilitySummaryResponse.java`, `AttendanceUpdateRequest.java`, `AttendanceDisputeRequest.java`
- Modify BE participant/dispute repositories
- Modify BE session service/controller
- Create BE: `src/main/java/com/example/desporto24/controllers/ReliabilityController.java`
- Modify BE: `src/main/resources/application.properties`
- Test BE: `src/test/java/com/example/desporto24/service/ReliabilityServiceTest.java`
- Test BE: `src/test/java/com/example/desporto24/controllers/ReliabilityControllerTest.java`

**Interfaces:**
- Produces:
  - penalty window = 90 days;
  - late-cancellation weight = 0.5;
  - no-show weight = 1.0;
  - warning threshold = 2.0;
  - reduced waitlist priority threshold = 3.0;
  - temporary join restriction threshold = 4.0;
  - restriction duration = 7 days.
- Produces attendance/dispute endpoints and neutral reliability summary.

- [ ] **Step 1: Add fixed configuration**

```properties
desporto.reliability.free-cancel-hours=2
desporto.reliability.lookback-days=90
desporto.reliability.late-cancellation-weight=0.5
desporto.reliability.no-show-weight=1.0
desporto.reliability.warning-threshold=2.0
desporto.reliability.reduced-priority-threshold=3.0
desporto.reliability.restriction-threshold=4.0
desporto.reliability.restriction-days=7
```

Map these with `@ConfigurationProperties(prefix = "desporto.reliability")`.

- [ ] **Step 2: Write boundary and scoring tests**

Boundary:

```java
@Test
void exactlyTwoHoursBeforeStartIsPenaltyFree() {
    Instant start = Instant.parse("2026-10-01T20:00:00Z");
    assertEquals(AttendanceStatus.PENDING,
        service.leaveAttendanceStatus(start.minus(Duration.ofHours(2)), start));
}

@Test
void oneSecondInsideTwoHoursIsLateCancellation() {
    Instant start = Instant.parse("2026-10-01T20:00:00Z");
    assertEquals(AttendanceStatus.LATE_CANCELLED,
        service.leaveAttendanceStatus(start.minus(Duration.ofHours(2)).plusSeconds(1), start));
}
```

Score test: two no-shows => warning; three => reduced priority; four => restricted.

Dispute test: a participant with a `NO_SHOW` and an OPEN dispute contributes zero no-show weight until resolved.

- [ ] **Step 3: Run tests**

```bash
./mvnw -Dtest=ReliabilityServiceTest test
```

Expected: FAIL.

- [ ] **Step 4: Implement reliability calculation**

```java
public ReliabilitySummaryResponse summary(Profile profile, Instant now) {
    Instant from = now.minus(Duration.ofDays(properties.getLookbackDays()));
    List<SessionParticipant> history =
        participantRepository.findReliabilityHistory(profile.getId(), from, now);

    double score = history.stream()
        .filter(p -> !disputeRepository.existsOpenByParticipantId(p.getId()))
        .mapToDouble(this::penaltyWeight)
        .sum();

    return toSummary(history, score, now);
}
```

`penaltyWeight` returns `1.0` for `NO_SHOW`, `0.5` for `LATE_CANCELLED`, otherwise `0.0`.

- [ ] **Step 5: Integrate leave rule and join restriction**

When leaving, set `LATE_CANCELLED` only when the leave instant is strictly later than `gameDate - 2h`.

Before joining, call:

```java
reliabilityService.requireJoinAllowed(profile, Instant.now(clock));
```

Waitlist entries use `priorityTier=1` when score is at least 3.0, otherwise `0`.

- [ ] **Step 6: Add creator attendance endpoint**

```http
PUT /api/sessions/{sessionId}/participants/{participantId}/attendance
{"status":"ATTENDED"}
```

or:

```json
{"status":"NO_SHOW"}
```

Only the creator may set attendance, and only after the session start time. The service records reporter identity and timestamp.

- [ ] **Step 7: Add dispute endpoint**

```http
POST /api/attendance-disputes
{"participantId":42,"reason":"I attended and the creator marked me absent."}
```

Only the participant may dispute their own `NO_SHOW`. One OPEN dispute per participant attendance record.

- [ ] **Step 8: Add reliability summary endpoint**

```http
GET /api/profiles/{username}/reliability
```

Response example:

```json
{
  "attendanceRate": 94.0,
  "completedSessions": 12,
  "noShows": 1,
  "lateCancellations": 0,
  "warning": false,
  "joinRestricted": false
}
```

- [ ] **Step 9: Run tests**

```bash
./mvnw -Dtest=ReliabilityServiceTest,ReliabilityControllerTest test
```

Expected: PASS.

- [ ] **Step 10: Commit**

```bash
git add src/main src/test
git commit -m "feat: add attendance reliability policy"
```

---

## Task 8: Add Session Notifications and Participant-Only Chat

**Files:**
- Modify BE: `src/main/java/com/example/desporto24/register/Notifications/Notifications.java`
- Modify BE notification repository/service/controller
- Create BE: `src/main/java/com/example/desporto24/enumeration/NotificationType.java`
- Create BE: `src/main/java/com/example/desporto24/service/SessionNotificationService.java`
- Create BE: `src/main/java/com/example/desporto24/controllers/SessionChatController.java`
- Create BE: `src/main/java/com/example/desporto24/service/SessionChatService.java`
- Use BE: `SessionMessage` repository from Task 3
- Test BE: `src/test/java/com/example/desporto24/service/SessionChatServiceTest.java`
- Test BE: `src/test/java/com/example/desporto24/service/SessionNotificationServiceTest.java`

**Interfaces:**
- Produces notification types `WAITLIST_PROMOTED`, `SESSION_REMOVED`, `SESSION_CANCELLED`.
- Produces `GET/POST /api/sessions/{id}/messages`.
- Direct/friend chat remains separate.

- [ ] **Step 1: Write chat authorization tests**

Assert non-participant read/write throws `AccessDeniedException`; creator and active participant can read/write; removed participant cannot post.

- [ ] **Step 2: Run tests**

```bash
./mvnw -Dtest=SessionChatServiceTest,SessionNotificationServiceTest test
```

Expected: FAIL.

- [ ] **Step 3: Extend notifications minimally**

Add fields while keeping legacy booleans:

```java
@Enumerated(EnumType.STRING)
private NotificationType type;

private Long sessionId;

private boolean read;
```

Add `GET /api/notifications/me`, deriving username from `Authentication`.

- [ ] **Step 4: Emit notifications**

Call `SessionNotificationService` from:
- waitlist promotion;
- creator removal;
- session cancellation.

The service saves one notification per affected profile.

- [ ] **Step 5: Implement session chat authorization**

```java
private void requireChatAccess(Session session, Profile actor) {
    boolean creator = session.getProfile().getId().equals(actor.getId());
    boolean participant = participantRepository.existsBySessionIdAndProfileIdAndState(
        session.getId(), actor.getId(), ParticipantState.ACTIVE);
    if (!creator && !participant) {
        throw new AccessDeniedException("Session chat is limited to participants");
    }
}
```

Limit message body to 2000 characters and trim leading/trailing whitespace.

- [ ] **Step 6: Run tests**

```bash
./mvnw -Dtest=SessionChatServiceTest,SessionNotificationServiceTest test
```

Expected: PASS.

- [ ] **Step 7: Commit**

```bash
git add src/main src/test
git commit -m "feat: add session chat and notifications"
```

---

## Task 9: Upgrade the Frontend Sequentially to Angular 22

**Files:**
- Modify FE: `package.json`
- Modify FE: `package-lock.json`
- Modify FE: `angular.json`
- Modify FE only compiler-identified compatibility files
- Test FE: all existing `*.spec.ts`

**Interfaces:**
- Produces: Angular 22 build on Node 22.22.3+ without changing product behavior.

- [ ] **Step 1: Start on Node 18 and update major-by-major through Angular 18**

```bash
nvm install 18
nvm use 18
npm ci
npx ng update @angular/core@16 @angular/cli@16
npx ng update @angular/core@17 @angular/cli@17
npx ng update @angular/core@18 @angular/cli@18
```

After each command run:
```bash
npm run build
```

Expected: PASS before moving to the next major.

- [ ] **Step 2: Move to Node 20 and update through Angular 21**

```bash
nvm install 20
nvm use 20
npx ng update @angular/core@19 @angular/cli@19
npm run build
npx ng update @angular/core@20 @angular/cli@20
npm run build
npx ng update @angular/core@21 @angular/cli@21
npm run build
```

Remove `@nestjs/ng-universal` unless an active import proves it is used; the current frontend has no SSR entry point.

- [ ] **Step 3: Move to Node 22 and update Angular 22**

```bash
nvm install 22
nvm use 22
node --version
npx ng update @angular/core@22 @angular/cli@22
npm run build
```

Expected Node version: at least `v22.22.3`. Expected build: PASS.

- [ ] **Step 4: Run frontend tests**

```bash
npm test -- --watch=false --browsers=ChromeHeadless
```

Expected: PASS.

- [ ] **Step 5: Commit**

```bash
git add package.json package-lock.json angular.json src
git commit -m "build: upgrade frontend to angular 22"
```

---

## Task 10: Add Typed Session API and Location Services to the Frontend

**Files:**
- Create FE: `src/app/features/sessions/models/session.models.ts`
- Create FE: `src/app/features/sessions/services/session-api.service.ts`
- Create FE: `src/app/features/sessions/services/session-api.service.spec.ts`
- Create FE: `src/app/features/sessions/services/location.service.ts`
- Create FE: `src/app/features/sessions/services/location.service.spec.ts`
- Create FE: `src/app/features/sessions/constants/sports.ts`
- Modify FE: `src/environment/environment.ts`
- Modify FE: `src/environment/environment.prod.ts`

**Interfaces:**
- Produces typed methods `discover`, `getById`, `create`, `join`, `leave`, `cancel`, `removeParticipant`, `getMessages`, `sendMessage`.
- Produces `LocationService.currentOrNull(): Promise<Coordinates | null>`.

- [ ] **Step 1: Define frontend models matching backend responses**

```ts
export type SessionStatus = 'OPEN' | 'FULL' | 'CANCELLED' | 'COMPLETED';
export type JoinState = 'PARTICIPANT' | 'WAITLISTED';

export interface SessionSummary {
  id: number;
  sport: string;
  city: string;
  gameDate: string;
  distanceKm: number | null;
  participantCount: number;
  maxParticipants: number;
  priceAmount: number;
  privateGame: boolean;
  status: SessionStatus;
}

export interface SessionDetail extends SessionSummary {
  address: string | null;
  canSeeExactAddress: boolean;
  joined: boolean;
  waitlisted: boolean;
  waitlistPosition: number | null;
  creatorUsername: string;
  gamePhoto: string | null;
}
```

- [ ] **Step 2: Write HTTP service tests**

Example:

```ts
it('discovers sessions using coordinates and radius', () => {
  service.discover({ latitude: 38.766, longitude: -9.297, radiusKm: 25 }).subscribe();

  const req = http.expectOne(r =>
    r.url === environment.apiUrl + '/api/sessions' &&
    r.params.get('radiusKm') === '25'
  );
  expect(req.request.method).toBe('GET');
  req.flush([]);
});
```

- [ ] **Step 3: Implement `SessionApiService`**

Use only `/api/sessions` and related relaunch endpoints. Do not add new `/menu/*` calls.

- [ ] **Step 4: Write geolocation fallback tests**

Mock `navigator.geolocation.getCurrentPosition` to invoke the error callback and assert:

```ts
await expectAsync(service.currentOrNull()).toBeResolvedTo(null);
```

- [ ] **Step 5: Implement `LocationService`**

Use browser geolocation with `enableHighAccuracy: false`, `timeout: 5000`, `maximumAge: 300000`. Resolve `null` rather than throwing on denial/unavailability.

- [ ] **Step 6: Preserve the existing sports list**

```ts
export const SPORTS = [
  'Futebol',
  'Voleibol',
  'Basquetebol',
  'Andebol',
  'Ténis',
  'Padel',
  'Karts',
  'Outro'
] as const;
```

- [ ] **Step 7: Run tests**

```bash
npm test -- --watch=false --browsers=ChromeHeadless
```

Expected: PASS.

- [ ] **Step 8: Commit**

```bash
git add src/app/features src/environment
git commit -m "feat: add typed session and location services"
```

---

## Task 11: Build the Desktop Server-Browser Discovery and Mobile Cards

**Files:**
- Create FE: `src/app/features/sessions/pages/discover-sessions/`
- Create FE: `src/app/features/sessions/components/session-browser-table/`
- Create FE: `src/app/features/sessions/components/session-mobile-card/`
- Create FE: `src/app/features/sessions/components/session-detail-panel/`
- Create FE: `src/app/features/sessions/components/session-filters/`
- Create FE: `src/app/layout/app-shell/`
- Modify FE: `src/app/app-routing.module.ts`
- Modify FE: `src/app/app.module.ts`
- Modify FE: `src/app/services/authethication.service.ts`
- Test FE: component specs beside each component

**Interfaces:**
- Produces route `/app/discover`.
- Desktop >= 900px: dense sortable list plus right-side detail panel.
- Mobile < 900px: vertical cards plus fullscreen detail route/panel.
- Exact address DOM element is absent when `canSeeExactAddress=false`.

- [ ] **Step 1: Write discovery rendering tests**

Desktop test:

```ts
expect(fixture.nativeElement.querySelector('[data-testid="session-row-1"]')).not.toBeNull();
expect(fixture.nativeElement.textContent).toContain('Futebol');
expect(fixture.nativeElement.textContent).toContain('1.8 km');
```

Privacy test:

```ts
component.session = { ...detail, address: null, canSeeExactAddress: false };
fixture.detectChanges();
expect(fixture.nativeElement.querySelector('[data-testid="exact-address"]')).toBeNull();
```

- [ ] **Step 2: Implement app shell**

Desktop navigation: `Descobrir | As minhas sessões | Criar | Chats | Perfil`.

Mobile bottom navigation: `Descobrir | Criar | Chats | Perfil`.

Keep existing authentication pages outside the app shell.

- [ ] **Step 3: Implement filters**

Controls:
- sport;
- radius 5/10/25/50/all;
- date;
- availability;
- public/private;
- free/paid.

On first load call `LocationService.currentOrNull()`. If it returns null, show city/zone input and do not block discovery.

- [ ] **Step 4: Implement desktop table**

Columns:
`Desporto | Zona | Quando | Distância | Jogadores | Preço | Acesso | Estado`.

Clicking a row selects it and loads detail into the right panel without navigating away from the list.

- [ ] **Step 5: Implement mobile cards**

Show sport, date/time, zone, distance, participant count, price, lock icon, status and a `Ver sessão` action.

- [ ] **Step 6: Wire route and login destination**

Add:
```ts
{ path: 'app', component: AppShellComponent, canActivate: [AuthenticationGuard], children: [
  { path: 'discover', component: DiscoverSessionsComponent },
  { path: '', redirectTo: 'discover', pathMatch: 'full' }
]}
```

After successful login navigate to `/app/discover` instead of the legacy `/menu`.

- [ ] **Step 7: Run tests/build**

```bash
npm test -- --watch=false --browsers=ChromeHeadless
npm run build
```

Expected: PASS.

- [ ] **Step 8: Commit**

```bash
git add src/app
git commit -m "feat: add responsive session browser"
```

---

## Task 12: Build Create, Join, Waitlist and Session Management UI

**Files:**
- Create FE: `src/app/features/sessions/pages/create-session/`
- Create FE: `src/app/features/sessions/pages/my-sessions/`
- Create FE: `src/app/features/sessions/components/private-session-password-dialog/`
- Create FE: `src/app/features/sessions/components/participant-list/`
- Modify FE detail panel/card components
- Modify FE routes/module
- Test FE corresponding specs

**Interfaces:**
- Produces `/app/create` and `/app/my-sessions`.
- Uses backend status as source of truth; never predicts membership locally.

- [ ] **Step 1: Write create form validation tests**

Assert:
- sport required;
- future date required;
- city/address required;
- max participants 2–100;
- price >= 0;
- private session requires password;
- photo optional.

- [ ] **Step 2: Implement create form**

Use Reactive Forms:

```ts
this.form = this.fb.group({
  sport: ['', Validators.required],
  gameDate: ['', Validators.required],
  city: ['', Validators.required],
  address: ['', Validators.required],
  maxParticipants: [10, [Validators.required, Validators.min(2), Validators.max(100)]],
  priceAmount: [0, [Validators.required, Validators.min(0)]],
  privateGame: [false],
  password: [''],
  latitude: [null],
  longitude: [null]
});
```

When `privateGame=true`, add `Validators.required` to password.

- [ ] **Step 3: Write join UI tests**

Public open: clicking `Juntar-me` calls `join(id,{password:null})`.

Private: clicking opens password dialog and sends typed password.

Full: successful join response `WAITLISTED` shows `Lista de espera · posição N`.

- [ ] **Step 4: Implement join states**

After every join/leave/remove/cancel operation, reload session detail from the server so address visibility and counts are server-authoritative.

- [ ] **Step 5: Implement creator management**

Creator detail shows:
- participant list;
- remove action;
- cancel session action;
- attendance action after session start.

Non-creators never see creator-only controls.

- [ ] **Step 6: Replace legacy event entry points**

Redirect:
- `/menu/events` -> `/app/discover`
- `/menu/createEvent` and `/menu/createEvents` -> `/app/create`

Keep the old components temporarily in source until the new flow passes acceptance testing, then remove their routes.

- [ ] **Step 7: Run tests/build**

```bash
npm test -- --watch=false --browsers=ChromeHeadless
npm run build
```

Expected: PASS.

- [ ] **Step 8: Commit**

```bash
git add src/app
git commit -m "feat: add session creation and membership ui"
```

---

## Task 13: Add Reliability, Session Chat, Notifications and PWA UX

**Files:**
- Create FE: `src/app/features/reliability/`
- Create FE: `src/app/features/session-chat/`
- Create FE: `src/app/features/notifications/`
- Modify FE: profile page/component to render reliability
- Modify FE: app shell navigation
- Modify FE: `package.json`, `angular.json`
- Create/modify FE PWA files generated by `ng add @angular/pwa`
- Test FE all new specs

**Interfaces:**
- Produces neutral reliability UI, participant-only session chat, in-app session notifications and installable PWA.

- [ ] **Step 1: Write reliability UI tests**

Given:

```ts
{ attendanceRate: 94, completedSessions: 12, noShows: 1, lateCancellations: 0 }
```

render:

```text
94% presença · 12 sessões · 1 falta
```

Do not render insulting labels or a numeric social score.

- [ ] **Step 2: Implement attendance/dispute UX**

Creator can mark `Compareceu` or `Não compareceu` after session start.

A participant with `NO_SHOW` sees `Contestar falta`, submits a reason, and sees `Contestação em análise` while OPEN.

- [ ] **Step 3: Write session chat tests**

Assert chat route/component loads only when session detail says `joined=true` or the current user is creator. Backend remains authoritative; a direct route attempt that receives 403 shows `Este chat é apenas para participantes.`.

- [ ] **Step 4: Implement chat polling**

Use RxJS `timer(0, 5000)` with `switchMap` to load messages while the component is active. Stop with `takeUntilDestroyed`.

Posting a message immediately refreshes the message list.

- [ ] **Step 5: Implement notification center**

Load `GET /api/notifications/me`. Session notifications link to the relevant session detail.

- [ ] **Step 6: Add PWA**

Run:

```bash
npx ng add @angular/pwa --project desporto24
```

Set manifest:
- name: `Desporto24`;
- short name: `Desporto24`;
- display: `standalone`;
- start URL: `/app/discover`.

Cache the app shell/static assets, but do not cache authenticated API responses containing exact addresses or chat data.

- [ ] **Step 7: Run tests/build**

```bash
npm test -- --watch=false --browsers=ChromeHeadless
npm run build
```

Expected: PASS and generated build includes `manifest.webmanifest` and service worker config.

- [ ] **Step 8: Commit**

```bash
git add package.json package-lock.json angular.json src
git commit -m "feat: add reliability chat notifications and pwa"
```

---

## Task 14: Replace Local Session Media Storage with a Storage Boundary

**Files:**
- Create BE: `src/main/java/com/example/desporto24/storage/MediaStorage.java`
- Create BE: `src/main/java/com/example/desporto24/storage/LocalMediaStorage.java`
- Create BE: `src/main/java/com/example/desporto24/storage/S3MediaStorage.java`
- Create BE: `src/main/java/com/example/desporto24/configuration/MediaStorageProperties.java`
- Modify BE: session/profile image save paths
- Modify BE: `pom.xml`
- Modify BE: `application.properties`
- Test BE: `src/test/java/com/example/desporto24/storage/MediaStorageTest.java`

**Interfaces:**
- Produces `MediaStorage.store(String key, MultipartFile file): URI`.
- `local` profile for development; `s3` profile for production.
- Compatible with AWS S3 and S3-compatible services such as Cloudflare R2.

- [ ] **Step 1: Define and test image validation**

Accept JPEG, PNG, WEBP; maximum 5 MB. Reject empty files and mismatched content types.

- [ ] **Step 2: Implement storage interface**

```java
public interface MediaStorage {
    URI store(String key, MultipartFile file);
    void delete(String key);
}
```

Use keys such as `sessions/{sessionId}/{uuid}.webp` and `profiles/{profileId}/{uuid}.webp`.

- [ ] **Step 3: Implement local storage**

Enabled by:
```properties
desporto.media.provider=local
desporto.media.local-root=./data/media
```

- [ ] **Step 4: Implement S3-compatible storage**

Add AWS SDK v2 S3 dependency and properties:

```properties
desporto.media.provider=${MEDIA_PROVIDER:local}
desporto.media.s3.bucket=${MEDIA_S3_BUCKET:}
desporto.media.s3.region=${MEDIA_S3_REGION:auto}
desporto.media.s3.endpoint=${MEDIA_S3_ENDPOINT:}
desporto.media.s3.access-key=${MEDIA_S3_ACCESS_KEY:}
desporto.media.s3.secret-key=${MEDIA_S3_SECRET_KEY:}
desporto.media.public-base-url=${MEDIA_PUBLIC_BASE_URL:}
```

- [ ] **Step 5: Replace direct filesystem writes**

Session/profile services call `MediaStorage`; controllers return stored public URI. Session photos remain optional.

- [ ] **Step 6: Run tests**

```bash
./mvnw -Dtest=MediaStorageTest test
./mvnw test
```

Expected: PASS.

- [ ] **Step 7: Commit**

```bash
git add pom.xml src/main src/test .env.example
git commit -m "feat: add durable media storage boundary"
```

---

## Task 15: Introduce Database Migration Control and Production Runtime Checks

**Files:**
- Modify BE: `pom.xml`
- Create BE: `src/main/resources/db/changelog/db.changelog-master.yaml`
- Create BE: `src/main/resources/db/changelog/000-legacy-baseline.sql`
- Create BE: `src/main/resources/db/changelog/001-relaunch-schema.yaml`
- Modify BE: application properties
- Modify BE: docker-compose to keep only development services required by default
- Test BE: fresh PostgreSQL boot and migration test

**Interfaces:**
- Produces reproducible schema for a fresh database and safe baseline behavior for an existing legacy schema.

- [ ] **Step 1: Generate a clean legacy schema snapshot from the pre-relaunch entity baseline**

Use a temporary PostgreSQL container, not the developer's existing `postgres_data` volume:

```bash
docker run --rm --name desporto24-schema-pg   -e POSTGRES_PASSWORD=postgres   -e POSTGRES_DB=desporto24   -p 55432:5432 -d postgres:15

docker exec desporto24-schema-pg   psql -U postgres -d desporto24 -c 'CREATE SCHEMA IF NOT EXISTS desporto24;'
```

Run the application against this temporary database with Hibernate schema creation enabled, then dump only the `desporto24` schema:

```bash
docker exec desporto24-schema-pg   pg_dump -U postgres --schema-only --no-owner --no-privileges   --schema=desporto24 desporto24   > src/main/resources/db/changelog/000-legacy-baseline.sql
```

Stop the temporary container:

```bash
docker stop desporto24-schema-pg
```

- [ ] **Step 2: Add Liquibase**

Add:
```xml
<dependency>
    <groupId>org.liquibase</groupId>
    <artifactId>liquibase-core</artifactId>
</dependency>
```

Master changelog:

```yaml
databaseChangeLog:
  - changeSet:
      id: 000-legacy-baseline
      author: desporto24
      preConditions:
        - onFail: MARK_RAN
        - not:
            - tableExists:
                schemaName: desporto24
                tableName: profile
      changes:
        - sqlFile:
            path: db/changelog/000-legacy-baseline.sql
  - include:
      file: db/changelog/001-relaunch-schema.yaml
```

`001-relaunch-schema.yaml` contains the additive columns/tables introduced in Tasks 3–8 and unique constraints matching the JPA model.

- [ ] **Step 3: Switch production schema mode**

```properties
spring.liquibase.change-log=classpath:/db/changelog/db.changelog-master.yaml
spring.jpa.hibernate.ddl-auto=validate
```

Development may temporarily use `update` only until its local database has been brought under Liquibase; the committed production profile uses `validate`.

- [ ] **Step 4: Add Actuator health**

Add `spring-boot-starter-actuator`, expose only health/info, and verify:

```bash
curl -f http://localhost:8080/actuator/health
```

Expected HTTP 200 with `"status":"UP"`.

- [ ] **Step 5: Verify clean-database boot**

Create a fresh PostgreSQL 15 container, start the application with `ddl-auto=validate`, and assert Liquibase applies baseline plus relaunch schema successfully.

- [ ] **Step 6: Commit**

```bash
git add pom.xml src/main/resources docker-compose.yml
git commit -m "build: add controlled database migrations"
```

---

## Task 16: Production Configuration, End-to-End Acceptance and Public Deployment

**Files:**
- Modify BE: `Dockerfile`, `.env.example`, production properties/CORS config
- Modify FE: production environment/config
- Create BE: `render.yaml` only if Render is selected at execution time
- Create deployment runbook: `docs/deployment.md`
- Test both repositories

**Interfaces:**
- Produces one public HTTPS frontend URL and one HTTPS API URL.
- Provider selection is made at execution time from current pricing/status using the design criteria: low initial cost, Git deploy, TLS, environment secrets, managed PostgreSQL, rollback. Static FE can use Vercel/Render; BE can use Render or an equivalent container service; PostgreSQL must be persistent; media uses the S3-compatible provider from Task 14.

- [ ] **Step 1: Run complete local acceptance suite**

BE:
```bash
./mvnw clean test
```

FE:
```bash
npm ci
npm test -- --watch=false --browsers=ChromeHeadless
npm run build
```

Expected: PASS.

- [ ] **Step 2: Run manual acceptance flows against local Docker/PostgreSQL**

Verify in order:
1. register and confirm email;
2. login without MFA;
3. enable/use optional MFA;
4. create public session;
5. create private session;
6. discover by location;
7. confirm exact address hidden before join;
8. join public;
9. join private with wrong then correct password;
10. fill session and join waitlist;
11. leave and verify promotion;
12. late-cancel inside 2h;
13. creator records no-show;
14. participant disputes;
15. session chat denied before join and allowed after join;
16. creator removes participant;
17. creator cancels session;
18. PWA installs and reopens to `/app/discover`.

- [ ] **Step 3: Choose current providers using official pricing/docs**

Document the selected services and why in `docs/deployment.md`. Do not use Render free Postgres for persistent production because its free databases expire after 30 days. A free backend instance may be used for beta with documented cold-start behavior; production data must live in persistent managed PostgreSQL.

- [ ] **Step 4: Configure production CORS and secrets**

Allowed origin is exactly the deployed frontend origin plus localhost only in dev. Set JWT, AES, DB, mail/SMS and media credentials in provider secret settings; none are committed.

- [ ] **Step 5: Deploy backend and database**

Run the provider health check against:
```text
/actuator/health
```

Expected: UP before frontend rollout.

- [ ] **Step 6: Deploy frontend**

Set `environment.prod.ts`/runtime configuration to the public API URL and deploy the production Angular build.

- [ ] **Step 7: Run public smoke tests**

From a clean browser profile:
- register/login;
- discovery with location allowed and denied;
- create/join one session;
- confirm exact-address privacy with a second account;
- send one session message;
- verify HTTPS and PWA manifest/service worker.

- [ ] **Step 8: Open pull requests**

Backend PR:
```text
feature/desporto24-relaunch -> develop
```

Frontend PR:
```text
feature/desporto24-relaunch -> master
```

PR descriptions must include:
- spec link;
- plan link;
- migration notes;
- secret rotation reminder;
- deployment URLs;
- automated test results;
- manual acceptance results.

Do not merge until the complete branch review is clean.

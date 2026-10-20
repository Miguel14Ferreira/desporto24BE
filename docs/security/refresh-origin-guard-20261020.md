# Refresh cookie origin control — security review, 20 October 2026

Scope: only POST `/api/auth/refresh`. Review initiated from Miguel Pestana's 20-Oct assessment of login/registration/contact security. Standalone draft PR; no production deployment.

## Verified current behaviour
- The frontend uses browser HttpClient POST and `withCredentials: true` to renew session.
- The backend's refresh cookie has `Secure`, `HttpOnly`, `SameSite=None`, `Path=/api/auth`.
- Spring Security currently disables CSRF across the stateless API. CORS has an explicit `desporto.cors.allowed-origins` list, but CORS alone is not an authorization policy for cookie-using endpoints.
- The existing `LoginAttemptService` already limits password attempts for **existing** profiles to five, then saves `notLocked=false` to PostgreSQL. The login attempts cache built in its constructor is not assigned to a field. This has account-denial/indefinite-lock risk and does not rate-limit unknown usernames. **Do not layer a new throttle on without reviewing its recovery flow.**

## Implementation
- Introduce `RefreshOriginValidator`, injected into the refresh controller.
- Require an **exact** Origin from the existing configured list (trim trailing slash on config tokens only).
- Deny absent/opaque `Origin: null`, scheme- and port-mismatch, and lookalike domains with HTTP 403, **before reading/rotating the cookie**.
- No new secret, no blanket wildcard. Other JWT endpoints are unchanged. CORS itself remains configured separately.
- JavaScript `fetch` from the Jynlo SPA to the API has a browser-sent Origin. Native HTTP clients without an Origin will now be rejected, deliberately.
- This hardens only refresh and does not constitute global CSRF protection. Cross-site cookie compatibility and logout/login flows require E2E browser tests.

## Validation gates (not yet passed)
- New isolated validator unit tests and controller regression tests written but **not executed**.
- Run Maven targeted tests and full build; verify legitimate `https://d24sports.com`/configured origins, expired token, cross-origin forged request, and frontend refresh in a real browser.
- Inspect all environments' `desporto.cors.allowed-origins` before any deployment; no change in production until owner approves.
- Subsequent separate work: design safe failed-login throttling (avoid permanent victim lockout), account enumeration and per-IP reverse-proxy trust model.

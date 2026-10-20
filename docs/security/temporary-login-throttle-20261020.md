# Bounded login throttling instead of permanent password-failure lock — 20 October 2026

Findings in the current production base `LoginAttemptService`:
- It checks `profile.loginAttempts >= 5` and saves `profile.notLocked=false`, permanently blocking the user even if an attacker supplied all five incorrect passwords.
- A Guava expiry cache is allocated in the constructor but never assigned or used.
- Usernames that do not exist are looked up and rejected before this guard, bypassing any attempt count and leaking a different error path.

Draft solution:
- Bounded 10,000-key Guava in-memory cache with a 15-minute expiration since the last failure.
- Maximum five failed passwords per normalized username; subsequent attempts return 429 until the window expires.
- Legitimate successful password check clears the cache for that username. Never change `notLocked`: existing admin/moderation lockout still enforced by AuthenticationServiceImpl.
- Unknown usernames are counted using the same throttle and return a generic BadCredentialsException instead of revealing whether the username exists.
- `loginAttempts` DB field remains diagnostic with a capped 1..5 failure count; no schema change.
- The current in-memory control is **per instance**, NOT effective across multiple replicas or restarts; IP-based protections and trusted proxy configuration are outside scope.
- MFA code verification has its own risk surface and must be independently rate-limited after browser/anti-lockout tests.

**Compatibility / release gating**
- Before deployment: check existing `notLocked=false` accounts — older ones remain administratively locked until reviewed, this change does not silently unlock them.
- Verify error translation 429 and 401 on the frontend, correct-password flow, 5 failures, recovery after 15 minutes, Google login, MFA and registration/email confirmation.
- Unit tests use a fake clock, no sleeping. All tests are authored but NOT run. No live production changes, no merge/deploy.

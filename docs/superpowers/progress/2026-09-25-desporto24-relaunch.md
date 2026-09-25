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

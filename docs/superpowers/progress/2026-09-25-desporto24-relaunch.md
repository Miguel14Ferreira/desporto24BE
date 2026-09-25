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

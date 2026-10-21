# Jynlo 37-venue release — executable restore gate (21 Oct 2026)

**Release PR #136 remains DRAFT until a REAL local archive passes an isolated restoration, alias-review holds, and an explicit publication decision is recorded.** The public catalogue has not changed.

## Already evidenced (do not rerun by default)
- [GitHub Actions PG16 + Liquibase #39585648500](https://github.com/Miguel14Ferreira/desporto24BE/actions/runs/39585648500): five real changesets, 37 conditional INSERT statements (one fixture duplicate skipped), backfill, second run idempotent. Java/container [#39585655102](https://github.com/Miguel14Ferreira/desporto24BE/actions/runs/39585655102) green.
- User previously ran the read-only production preflight for 24 + 13 proposed rows, and São Jacinto as an **additional alias recheck** (it is among the 13, not candidate #38). **NO_MATCH_FOUND** for tested predicates on 21 Oct 2026. Names/aliases without GPS still have residual duplicate risk; verify again only if the catalogue changed.
- Castelo Branco facility GPS and Viseu/Guarda GPS were municipality-checked through PRs #139 and #140. Audit #141 adds Braga stadium-point and Évora event-waypoint evidence, **not** automatically approved GPS for those facilities.

## Real backup (owner already created in WSL)
The local archive was reportedly produced with `pg_dump -W -Fc --no-owner --no-acl --schema=desporto24 --file="$BACKUP"` and passed `pg_restore --list`. That is **not** a restore test. Never upload this archive or credentials to GitHub.

Run in WSL **from the local backend checkout** (Docker Desktop/Podman must be accessible from WSL):
```bash
cd /mnt/c/Asseco/desporto24BE  # replace with actual local backend checkout if elsewhere
find "$HOME/jynlo-backups" -maxdepth 1 -type f -name 'jynlo-*.dump' -printf '%TY-%Tm-%Td %TH:%TM %p\n'
# Select the backup created on 21 October 2026, using the real filename:
BACKUP="$HOME/jynlo-backups/<actual-backup-name>.dump"
bash scripts/verify_venue_backup_restore.sh "$BACKUP"
```
If the checkout is elsewhere, use its actual path; do not assume the example exists. If `bash scripts/verify_venue_backup_restore.sh` is not in local checkout yet, fetch the PR #136 branch first. The script verifies archive readability, restores the **actual file** inside an isolated `postgres:16` container with `--network none` and no exposed ports, checks restored venue rows (default minimum 228), verifies Liquibase history and destroys the disposable container on completion or error. It NEVER connects to Render or modifies live PostgreSQL. The CI also proves the same script can restore a synthetic PostgreSQL 16 fixture, but **CI fixture success does not prove the user's own archive can be restored**.

## Other mandatory checks
1. Confirm backup is from the right database, date and schema; record its SHA256 and successful test output privately. The archive excludes objects outside `desporto24`, so it is an **application schema recovery test**, not a full cluster/disaster-recovery guarantee.
2. If the production catalogue changed since the 21 Oct preflights, re-run the **three existing read-only** SQL files, review aliases, spatial proximity and address sharing. Different fields of Braga Rodovia share a complex; do not merge them automatically.
3. Keep **Braga stadium centre** and **Évora 2022 waypoint** out of exact track GPS. Keep the rejected Santarém/Entroncamento SNID point out of Santarém.
4. Review seasonal/restricted facilities and avoid asserting public access. The 37 entries are catalogue references, not reservations.
5. Require explicit approval before merging PR #136 and moving deploy branch `feature/desporto24-preview-deploy`; confirm the running commit, backup, and venue count before and after any controlled rollout.

No databases, credentials or production connections are used by GitHub CI.

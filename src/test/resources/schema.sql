-- Test-only SQL initialization, executed for embedded H2 before Hibernate DDL.
-- ChatMessageReaction maps explicitly to schema desporto24, while H2-backed
-- repository/security tests use isolated in-memory databases and Liquibase off.
-- This does not run in production PostgreSQL; no production schema migration.
CREATE SCHEMA IF NOT EXISTS desporto24;

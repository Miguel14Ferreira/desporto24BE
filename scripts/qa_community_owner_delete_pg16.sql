\set ON_ERROR_STOP on
CREATE SCHEMA desporto24;
CREATE TABLE desporto24.profile (id bigint PRIMARY KEY, username text NOT NULL);
CREATE TABLE desporto24.community (
 id bigint PRIMARY KEY, name text NOT NULL,
 owner_profile_id bigint NOT NULL REFERENCES desporto24.profile(id)
);
CREATE TABLE desporto24.community_member (
 id bigint PRIMARY KEY, community_id bigint REFERENCES desporto24.community(id),
 profile_id bigint REFERENCES desporto24.profile(id)
);
CREATE TABLE desporto24.community_invite (
 id bigint PRIMARY KEY, community_id bigint REFERENCES desporto24.community(id),
 invited_profile_id bigint REFERENCES desporto24.profile(id)
);
CREATE TABLE desporto24.community_season (
 id bigint PRIMARY KEY, community_id bigint REFERENCES desporto24.community(id)
);
CREATE TABLE desporto24.community_round (
 id bigint PRIMARY KEY,
 community_id bigint REFERENCES desporto24.community(id),
 season_id bigint REFERENCES desporto24.community_season(id)
);
CREATE TABLE desporto24.community_tournament (
 id bigint PRIMARY KEY, community_id bigint REFERENCES desporto24.community(id)
);
CREATE TABLE desporto24.community_tournament_participant (
 id bigint PRIMARY KEY, tournament_id bigint REFERENCES desporto24.community_tournament(id),
 profile_id bigint REFERENCES desporto24.profile(id)
);
CREATE TABLE desporto24.community_tournament_match (
 id bigint PRIMARY KEY, tournament_id bigint REFERENCES desporto24.community_tournament(id),
 home_participant_id bigint REFERENCES desporto24.community_tournament_participant(id),
 away_participant_id bigint REFERENCES desporto24.community_tournament_participant(id)
);
CREATE TABLE desporto24.community_standings_archive (
 id bigint PRIMARY KEY, community_id bigint REFERENCES desporto24.community(id),
 season_id bigint REFERENCES desporto24.community_season(id)
);
CREATE TABLE desporto24.community_standings_archive_row (
 id bigint PRIMARY KEY, archive_id bigint REFERENCES desporto24.community_standings_archive(id)
);
CREATE TABLE desporto24.community_timeline_entry (
 id bigint PRIMARY KEY, community_id bigint REFERENCES desporto24.community(id),
 author_profile_id bigint REFERENCES desporto24.profile(id)
);
CREATE TABLE desporto24.social_story (
 id bigint PRIMARY KEY, community_id bigint REFERENCES desporto24.community(id)
);
CREATE TABLE desporto24.session (
 id bigint PRIMARY KEY, community_id bigint REFERENCES desporto24.community(id)
);
CREATE TABLE desporto24.session_participant (
 id bigint PRIMARY KEY, session_id bigint REFERENCES desporto24.session(id),
 profile_id bigint REFERENCES desporto24.profile(id)
);
CREATE TABLE desporto24.social_post (
 id bigint PRIMARY KEY, session_id bigint REFERENCES desporto24.session(id)
);
INSERT INTO desporto24.profile VALUES (10,'owner'),(20,'other');
INSERT INTO desporto24.community VALUES (1,'Cacem',10),(2,'Other',20);
INSERT INTO desporto24.community_member VALUES (11,1,10),(12,1,20),(13,2,20);
INSERT INTO desporto24.community_invite VALUES (14,1,20);
INSERT INTO desporto24.community_season VALUES (21,1);
INSERT INTO desporto24.community_round VALUES (22,1,21);
INSERT INTO desporto24.community_tournament VALUES (31,1);
INSERT INTO desporto24.community_tournament_participant VALUES (32,31,10),(33,31,20);
INSERT INTO desporto24.community_tournament_match VALUES (34,31,32,33);
INSERT INTO desporto24.community_standings_archive VALUES (41,1,21);
INSERT INTO desporto24.community_standings_archive_row VALUES (42,41);
INSERT INTO desporto24.community_timeline_entry VALUES (51,1,10);
INSERT INTO desporto24.social_story VALUES (52,1);
INSERT INTO desporto24.session VALUES (61,1),(62,2);
INSERT INTO desporto24.session_participant VALUES (63,61,10),(64,62,20);
INSERT INTO desporto24.social_post VALUES (65,61),(66,62);
\i src/main/resources/db/changelog/changes/2026-10-25-community-owner-subtree-delete.sql
SELECT desporto24.delete_community_subtree(1);
DO $qa$
BEGIN
 IF EXISTS (SELECT 1 FROM desporto24.community WHERE id=1) THEN
   RAISE EXCEPTION 'target community was not removed';
 END IF;
 IF EXISTS (SELECT 1 FROM desporto24.community_tournament_match WHERE id=34)
    OR EXISTS (SELECT 1 FROM desporto24.community_tournament_participant WHERE id IN(32,33))
    OR EXISTS (SELECT 1 FROM desporto24.community_standings_archive_row WHERE id=42)
    OR EXISTS (SELECT 1 FROM desporto24.session_participant WHERE id=63)
    OR EXISTS (SELECT 1 FROM desporto24.social_post WHERE id=65)
    OR EXISTS (SELECT 1 FROM desporto24.community_timeline_entry WHERE id=51)
    OR EXISTS (SELECT 1 FROM desporto24.social_story WHERE id=52) THEN
   RAISE EXCEPTION 'dependent community records remain';
 END IF;
 IF (SELECT count(*) FROM desporto24.profile) <> 2
    OR (SELECT count(*) FROM desporto24.community) <> 1
    OR NOT EXISTS(SELECT 1 FROM desporto24.session WHERE id=62)
    OR NOT EXISTS(SELECT 1 FROM desporto24.social_post WHERE id=66)
    OR NOT EXISTS(SELECT 1 FROM desporto24.community_member WHERE id=13) THEN
   RAISE EXCEPTION 'unrelated community or user data changed';
 END IF;
END
$qa$;
-- Guard against a future FK from a shared profile to the community tree.
ALTER TABLE desporto24.profile ADD COLUMN recent_community_id bigint
  REFERENCES desporto24.community(id);
UPDATE desporto24.profile SET recent_community_id=2 WHERE id=20;
DO $qa$
DECLARE rejected boolean := false;
BEGIN
 BEGIN
   PERFORM desporto24.delete_community_subtree(2);
 EXCEPTION WHEN OTHERS THEN
   IF position('shared resource' in SQLERRM) > 0 THEN
     rejected := true;
   ELSE
     RAISE;
   END IF;
 END;
 IF NOT rejected OR NOT EXISTS(SELECT 1 FROM desporto24.community WHERE id=2) THEN
   RAISE EXCEPTION 'shared account dependency protection failed';
 END IF;
END
$qa$;

-- Runs only after the API has authenticated an OWNER and checked the exact community name.
-- All affected rows are scoped by foreign-key links to one community; user profiles,
-- sports catalog and unrelated community rows are not deleted.
-- No globally altered ON DELETE rules: unrelated deletions retain their original semantics.
CREATE OR REPLACE FUNCTION desporto24.delete_community_subtree(p_community_id bigint)
RETURNS integer
LANGUAGE plpgsql
SET search_path = pg_catalog, desporto24
AS $fn$
DECLARE
    parent_row tid;
    fk record;
    child_fields text;
    newly_found integer;
    pass_total integer;
    round_number integer := 0;
    leaf oid;
    deleted_count integer;
    cross_boundary_reference boolean;
BEGIN
    -- An exclusive row lock serializes deletion with ownership changes and
    -- FK inserts that refer directly to the community.
    SELECT c.ctid INTO parent_row
      FROM desporto24.community c WHERE c.id = p_community_id FOR UPDATE;
    IF NOT FOUND THEN
        RAISE EXCEPTION 'Community not found' USING ERRCODE = 'P0002';
    END IF;

    CREATE TEMP TABLE IF NOT EXISTS _jynlo_delete_community_rows (
        relation_id oid NOT NULL,
        row_id tid NOT NULL,
        PRIMARY KEY(relation_id, row_id)
    ) ON COMMIT DROP;
    TRUNCATE pg_temp._jynlo_delete_community_rows;
    INSERT INTO pg_temp._jynlo_delete_community_rows VALUES
        ('desporto24.community'::regclass::oid, parent_row);

    -- Discover descendants before touching any persistent table.
    LOOP
        round_number := round_number + 1;
        IF round_number > 64 THEN
            RAISE EXCEPTION 'Community reference graph exceeds safe recursion limit';
        END IF;
        pass_total := 0;

        FOR fk IN
            SELECT f.*, child.relname AS child_name
            FROM pg_constraint f
            JOIN pg_class child ON child.oid = f.conrelid
            JOIN pg_namespace ns ON ns.oid = child.relnamespace
            WHERE f.contype = 'f' AND ns.nspname = 'desporto24'
              AND EXISTS (SELECT 1 FROM pg_temp._jynlo_delete_community_rows r
                          WHERE r.relation_id = f.confrelid)
        LOOP
            -- Account identities and shared catalogues can never be deleted
            -- merely because they acquire a new FK to community data.
            IF fk.child_name IN ('profile', 'users', 'account', 'venue',
                                 'sport', 'roles', 'authority') THEN
                RAISE EXCEPTION 'Refusing to delete shared resource %', fk.child_name;
            END IF;

            SELECT string_agg(format('child.%I = parent.%I',
                                    child_column.attname, parent_column.attname), ' AND ')
            INTO child_fields
            FROM unnest(fk.conkey, fk.confkey) columns(child_attnum, parent_attnum)
            JOIN pg_attribute child_column ON child_column.attrelid = fk.conrelid
                                          AND child_column.attnum = columns.child_attnum
            JOIN pg_attribute parent_column ON parent_column.attrelid = fk.confrelid
                                           AND parent_column.attnum = columns.parent_attnum;
            IF child_fields IS NULL THEN
                RAISE EXCEPTION 'Unsupported FK relationship %', fk.conname;
            END IF;

            EXECUTE format(
                'INSERT INTO pg_temp._jynlo_delete_community_rows (relation_id,row_id)
                 SELECT %s::oid, child.ctid FROM %s child
                 WHERE EXISTS (SELECT 1 FROM %s parent
                      JOIN pg_temp._jynlo_delete_community_rows marked
                        ON marked.relation_id = %s::oid AND marked.row_id = parent.ctid
                      WHERE %s)
                 ON CONFLICT DO NOTHING',
                fk.conrelid, fk.conrelid::regclass, fk.confrelid::regclass,
                fk.confrelid, child_fields);
            GET DIAGNOSTICS newly_found = ROW_COUNT;
            pass_total := pass_total + newly_found;
        END LOOP;
        EXIT WHEN pass_total = 0;
    END LOOP;

    -- Protect unrelated communities even if pre-existing bad data points
    -- across tenant boundaries. A selected child must not have a parent in
    -- the selected community graph that belongs to another group.
    FOR fk IN
        SELECT f.* FROM pg_constraint f
        WHERE f.contype = 'f'
          AND EXISTS (SELECT 1 FROM pg_temp._jynlo_delete_community_rows r
                      WHERE r.relation_id = f.conrelid)
          AND EXISTS (SELECT 1 FROM pg_temp._jynlo_delete_community_rows r
                      WHERE r.relation_id = f.confrelid)
    LOOP
        SELECT string_agg(format('child.%I = parent.%I',
                                child_column.attname, parent_column.attname), ' AND ')
        INTO child_fields
        FROM unnest(fk.conkey, fk.confkey) columns(child_attnum, parent_attnum)
        JOIN pg_attribute child_column ON child_column.attrelid = fk.conrelid
                                      AND child_column.attnum = columns.child_attnum
        JOIN pg_attribute parent_column ON parent_column.attrelid = fk.confrelid
                                       AND parent_column.attnum = columns.parent_attnum;
        EXECUTE format(
            'SELECT EXISTS (
                SELECT 1 FROM %s child JOIN %s parent ON %s
                WHERE EXISTS(SELECT 1 FROM pg_temp._jynlo_delete_community_rows marked
                             WHERE marked.relation_id = %s::oid AND marked.row_id = child.ctid)
                  AND NOT EXISTS(SELECT 1 FROM pg_temp._jynlo_delete_community_rows marked
                                 WHERE marked.relation_id = %s::oid AND marked.row_id = parent.ctid)
             )', fk.conrelid::regclass, fk.confrelid::regclass,
             child_fields, fk.conrelid, fk.confrelid)
          INTO STRICT cross_boundary_reference;
        IF cross_boundary_reference THEN
            RAISE EXCEPTION 'Cross-community dependency % prevents safe deletion', fk.conname;
        END IF;
    END LOOP;

    -- Delete leaves before their parents. Fail closed on cyclic cross-table
    -- FKs instead of disabling constraints or deleting unrelated data.
    LOOP
        SELECT targets.relation_id INTO leaf
        FROM (SELECT DISTINCT relation_id
              FROM pg_temp._jynlo_delete_community_rows) targets
        WHERE NOT EXISTS (
            SELECT 1 FROM pg_constraint constraint_fk
            WHERE constraint_fk.contype = 'f'
              AND constraint_fk.confrelid = targets.relation_id
              AND constraint_fk.conrelid <> targets.relation_id
              AND EXISTS (
                  SELECT 1 FROM pg_temp._jynlo_delete_community_rows child_rows
                  WHERE child_rows.relation_id = constraint_fk.conrelid
              )
        )
        ORDER BY targets.relation_id
        LIMIT 1;

        EXIT WHEN NOT FOUND;
        EXECUTE format(
            'DELETE FROM %s record WHERE EXISTS (
                 SELECT 1 FROM pg_temp._jynlo_delete_community_rows selected
                 WHERE selected.relation_id = %s::oid AND selected.row_id = record.ctid)',
            leaf::regclass, leaf);
        GET DIAGNOSTICS deleted_count = ROW_COUNT;
        DELETE FROM pg_temp._jynlo_delete_community_rows WHERE relation_id = leaf;
    END LOOP;
    IF EXISTS(SELECT 1 FROM pg_temp._jynlo_delete_community_rows) THEN
        RAISE EXCEPTION 'Cyclic community dependencies cannot be safely deleted';
    END IF;
    RETURN 1;
END
$fn$;

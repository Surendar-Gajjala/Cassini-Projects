/* Wrap everything in a transaction */
BEGIN TRANSACTION;

/* Begin schema setup */
\set schemaName '$schemaName'

DROP SCHEMA IF EXISTS :schemaName CASCADE;
CREATE SCHEMA :schemaName;
SET SEARCH_PATH TO :schemaName;
/* End schema setup */



/* Begin other schema files */
\ir ../../../../cassini.platform/src/main/db/schema.sql
\i schema-enums.sql
/*\i schema-sequences.sql*/
\i schema-tms.sql
/*\i data/data.sql*/
/* End other schema files */

/* Import the permissions list */
\COPY PERMISSION FROM 'data/permissions.csv' DELIMITER ',' CSV;

/* Alter schema owner */
ALTER SCHEMA :schemaName OWNER TO CASSINISYS;


/* Function to execute sql statements */
CREATE FUNCTION exec(text) returns text language plpgsql volatile
AS $f$
    BEGIN
        EXECUTE $1;
        RETURN $1;
    END;
$f$;



/* Change onwership of all tables */
SELECT exec('ALTER TABLE '|| schemaname || '.' || tablename ||' OWNER TO CASSINISYS;')
FROM pg_tables WHERE schemaname = :'schemaName'
ORDER BY schemaname, tablename;

/* Change ownership of all sequences */
SELECT exec('ALTER SEQUENCE '|| sequence_schema || '.' || sequence_name ||' OWNER TO CASSINISYS;')
FROM information_schema.sequences WHERE sequence_schema = :'schemaName'
ORDER BY sequence_schema, sequence_name;

/* Change onwership of all views */
SELECT exec('ALTER VIEW '|| table_schema || '.' || table_name ||' OWNER TO CASSINISYS;')
FROM information_schema.views WHERE table_schema = :'schemaName'
ORDER BY table_schema, table_name;

 \i data/roles-data.sql

/* Commit everything */
COMMIT TRANSACTION;
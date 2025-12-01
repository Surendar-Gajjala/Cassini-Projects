/* Wrap everything in a transaction */
BEGIN TRANSACTION;

/* Begin schema setup */
\set schemaName '$schemaName'

DROP SCHEMA IF EXISTS :schemaName CASCADE;
CREATE SCHEMA :schemaName;
SET SEARCH_PATH TO :schemaName;
/* End schema setup */


/* Begin extensions */
CREATE EXTENSION IF NOT EXISTS cube WITH SCHEMA pg_catalog;
CREATE EXTENSION IF NOT EXISTS earthdistance WITH SCHEMA pg_catalog;
/* End extensions */


/* Begin other schema files */
\i schema-enums.sql
\i schema-sequences.sql
\i schema-core.sql
\i schema-security.sql
\i schema-hrm.sql
\i schema-production.sql
\i schema-crm.sql
\i schema-collaboration.sql
/* End other schema files */

/* Import the permissions list */
\COPY ERP_PERMISSION FROM 'data/permissions.csv' DELIMITER ',' CSV;


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



/*  Run the data creation script */
\i data/core-data.sql
\i data/roles-data.sql


/* Commit everything */
COMMIT TRANSACTION;
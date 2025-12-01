/* Wrap everything in a transaction */
BEGIN TRANSACTION;

/* Begin schema setup */
\set schemaName '$schemaName'
\set password '$hashPwd'
DROP SCHEMA IF EXISTS :schemaName CASCADE;
CREATE SCHEMA :schemaName;
SET SEARCH_PATH TO :schemaName;
/* End schema setup */


/* Begin other schema files */
\ir ../../../../cassini.platform/src/main/db/schema.sql
\i schema-functions.sql
\i schema-enums.sql
\i schema-sequences.sql
\i schema-plm.sql
\i schema-pdm.sql
\i schema-cm.sql
\i schema-sourcing.sql
\i schema-wf.sql
\i schema-pm.sql
\i schema-rm.sql
\i schema-req.sql
\i schema-pqm.sql
\i schema-mes.sql
\i schema-mro.sql
\i schema-pgc.sql
\i schema-common.sql
\i data/data.sql
/* End other schema files */

/* Import the permissions list */
/*\COPY PERMISSION FROM 'data/permissions.csv' DELIMITER ',' CSV;*/
\COPY APP_NAVIGATION FROM 'data/navigation.csv' DELIMITER ',' CSV;
\COPY SECURITY_PERMISSION (NAME, DESCRIPTION, OBJECT_TYPE, SUB_TYPE, PRIVILEGE, MODULE, ATTRIBUTE, CRITERIA) FROM 'data/security_permissions.csv' DELIMITER ',' CSV;

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

UPDATE :schemaName.login SET password=:password WHERE login_name='admin';

/*\i data/data-roles.sql*/
\i data/data-securityRoles.sql
/* Commit everything */
COMMIT TRANSACTION;
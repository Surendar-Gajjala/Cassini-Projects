/* Begin enums */

DO $$
DECLARE
	enumTypes TEXT[];
BEGIN

enumTypes := ARRAY [
    'TESTSCENARIO',
    'TESTPLAN',
    'TESTSUITE',
    'TESTCASE',
    'TESTRUN',
    'TESTRUNCONFIGURATION',
    'RCSUITE',
    'RCRUN',
    'RCCASE',
    'RCPLAN',
    'RCSCENARIO',
    'RCRUNCONFIGURATION',
    'RUNSCENARIO',
    'RUNPLAN',
    'RUNSUITE',
    'RUNCASE',
    'RUNEXECUTION',
    'FILE'
];

CREATE FUNCTION createEnums(eArray TEXT[]) RETURNS VOID LANGUAGE PLPGSQL VOLATILE
AS $f$
    DECLARE
        t TEXT;
    BEGIN
        FOREACH t IN ARRAY eArray LOOP
            EXECUTE 'ALTER TYPE OBJECT_TYPE ADD VALUE ' || quote_literal(t);
            END loop;
        END;
    $f$;


PERFORM createEnums(enumTypes);


CREATE TYPE RUNSTATUS AS ENUM (
    'PENDING',
    'INPROGRESS',
    'FINISHED'
);

CREATE TYPE EXECUTION_TYPE AS ENUM (
    'PROGRAM',
    'SCRIPT'
);

CREATE TYPE SCRIPT_LANGUAGE AS ENUM (
    'GROOVY',
    'PYTHON'
);


END $$;
/* End enums */

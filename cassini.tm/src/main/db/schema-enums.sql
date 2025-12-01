/* Begin enums */

DO $$
DECLARE
	enumTypes TEXT[];
BEGIN

enumTypes := ARRAY [
    'PROJECT',
    'PROJECTTASK',
    'DEPARTMENT',
    'LAYOUTDRAWING'
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

CREATE TYPE TASK_STATUS AS ENUM (
    'ASSIGNED',
    'FINISHEDPENDING',
    'FINISHED',
    'VERIFIEDPENDING',
    'VERIFIED',
    'APPROVEDPENDING',
    'APPROVED',
    'REJECTED'
);

END $$;
/* End enums */

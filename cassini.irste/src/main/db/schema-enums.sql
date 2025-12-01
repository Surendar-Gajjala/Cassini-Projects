/* Begin enums */

DO $$
DECLARE
	enumTypes TEXT[];
BEGIN

enumTypes := ARRAY [
    'COMPLAINT',
    'LOCATION',
    'UTILITY'
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


CREATE TYPE COMPLAINT_STATUS AS ENUM (
    'NEW',
    'INPROGRESS',
    'AT_ASSISTOR',
    'ASSISTED',
    'AT_FACILITATOR',
    'FACILITATED',
    'COMPLETED'
);

END $$;
/* End enums */

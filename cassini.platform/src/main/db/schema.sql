\ir ../../../../cassini.platform/src/main/db/schema-enums.sql
\ir ../../../../cassini.platform/src/main/db/schema-sequences.sql
\ir ../../../../cassini.platform/src/main/db/schema-core.sql
\ir ../../../../cassini.platform/src/main/db/schema-common.sql
\ir ../../../../cassini.platform/src/main/db/schema-col.sql
\ir ../../../../cassini.platform/src/main/db/schema-wfm.sql
\ir ../../../../cassini.platform/src/main/db/schema-security.sql
\ir ../../../../cassini.platform/src/main/db/schema-custom.sql


ALTER TABLE CASSINI_OBJECT ADD COLUMN CREATED_BY INTEGER REFERENCES PERSON (PERSON_ID) ON DELETE CASCADE;
ALTER TABLE CASSINI_OBJECT ADD COLUMN MODIFIED_BY INTEGER REFERENCES PERSON (PERSON_ID) ON DELETE CASCADE;
ALTER TABLE ACTIVITY_STREAM ADD COLUMN SESSION INTEGER REFERENCES SESSION (SESSION_ID) ON DELETE CASCADE;

/*  Run the data creation script */
\ir ../../../../cassini.platform/src/main/db/data/core-data.sql

/*Import Quantities of Measurement */
\COPY MEASUREMENT(id, name) FROM '../../../../cassini.platform/src/main/db/data/qoms.csv' DELIMITER ',' CSV;

/*Import Units of Measurement */
\COPY MEASUREMENT_UNIT(measurement, name, symbol, conversion_factor, base_unit) FROM '../../../../cassini.platform/src/main/db/data/uoms.csv' DELIMITER ',' CSV;
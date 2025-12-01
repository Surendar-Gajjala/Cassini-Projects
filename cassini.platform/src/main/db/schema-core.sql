/* Begin core tables */
CREATE TABLE CASSINI_OBJECT (
    OBJECT_ID                   INTEGER             NOT NULL PRIMARY KEY,
    CREATED_DATE                TIMESTAMP           NOT NULL DEFAULT now(),
    /* CREATED_BY COLUMN IS ADDED LATER */
    MODIFIED_DATE               TIMESTAMP           NOT NULL DEFAULT now(),
    /* MODIFIED_BY COLUMN IS ADDED LATER */
    OBJECT_TYPE                 OBJECT_TYPE         NOT NULL
);

CREATE TABLE LOV (
    LOV_ID                      INTEGER             NOT NULL PRIMARY KEY,
    TYPE                        TEXT                ,
    NAME                        TEXT                NOT NULL,
    DESCRIPTION                 TEXT                NOT NULL,
    VALUES                      TEXT[]              NOT NULL,
    DEFAULT_VALUE               TEXT                NOT NULL
);

CREATE TABLE OBJECTTYPEATTRIBUTE (
    ATTRIBUTE_ID                INTEGER             NOT NULL PRIMARY KEY,
    DATA_TYPE                   DATATYPE            NOT NULL,
    NAME                        TEXT                NOT NULL,
    DESCRIPTION                 TEXT                ,
    REQUIRED                    BOOLEAN             ,
    REF_TYPE                    OBJECT_TYPE         ,
    REF_SUB_TYPE                INTEGER             ,
    LIST_MULTIPLE               BOOLEAN             DEFAULT FALSE,
    LOV                         INTEGER             REFERENCES LOV (LOV_ID) ON DELETE CASCADE,
    OBJECT_TYPE                 OBJECT_TYPE         NOT NULL,
    DEFAULT_TEXTVALUE           TEXT                ,
    DEFAULT_LISTVALUE           TEXT                ,
    VISIBLE                     BOOLEAN             DEFAULT TRUE,
    FORMULA                     TEXT                ,
    VALIDATIONS                 TEXT                ,
    ATTRIBUTE_GROUP             TEXT                ,
    PLUGIN                      BOOLEAN             DEFAULT FALSE
);

CREATE TABLE CURRENCY (
  CURRENCY_ID                 INTEGER             NOT NULL PRIMARY KEY,
  COUNTRY                     TEXT                NOT NULL,
  NAME                        TEXT                NOT NULL,
  SYMBOL                      TEXT                NOT NULL,
  CODE                        TEXT                NOT NULL
);

CREATE TABLE OBJECTATTRIBUTE (
    OBJECT_ID                   INTEGER             NOT NULL REFERENCES CASSINI_OBJECT (OBJECT_ID) ON DELETE CASCADE,
    ATTRIBUTEDEF                INTEGER             NOT NULL REFERENCES OBJECTTYPEATTRIBUTE (ATTRIBUTE_ID) ON DELETE CASCADE,
    TEXT_VALUE                  TEXT                ,
    INTEGER_VALUE               INTEGER             ,
    DOUBLE_VALUE                DOUBLE PRECISION    ,
    DATE_VALUE                  TIMESTAMP           ,
    TIME_VALUE                  TIME                ,
    TIMESTAMP_VALUE             TIMESTAMP           ,
    REF_VALUE                   INTEGER             REFERENCES CASSINI_OBJECT (OBJECT_ID) ON DELETE CASCADE,
    BOOLEAN_VALUE               BOOLEAN             ,
    LIST_VALUE                  TEXT                ,
    MLIST_VALUE                 TEXT[]              ,
    IMAGE_VALUE                 BYTEA               ,
    CURRENCY_VALUE              DOUBLE PRECISION    ,
    CURRENCY_TYPE               INTEGER             REFERENCES CURRENCY (CURRENCY_ID) ON DELETE CASCADE,
    LONGTEXT_VALUE              TEXT                ,
    RICHTEXT_VALUE              TEXT                ,
    HYPERLINK_VALUE             TEXT                ,
    FORMULA_VALUE               TEXT                ,
    LOCATION_VALUE              DOUBLE PRECISION[]  ,
    /*ATTACHEMNT_VALUE column added later in col schema */
    UNIQUE (OBJECT_ID, ATTRIBUTEDEF)
);

CREATE TABLE CUSTOMTABLE (
    TABLE_ID                    INTEGER             NOT NULL PRIMARY KEY,
    NAME                        TEXT                NOT NULL,
    DESCRIPTION                 TEXT
);

CREATE TABLE CUSTOMTABLECOLUMN (
    COLUMN_ID                   INTEGER             NOT NULL PRIMARY KEY,
    TABLE_ID                    INTEGER             NOT NULL REFERENCES CUSTOMTABLE (TABLE_ID) ON DELETE CASCADE,
    FOREIGN KEY (COLUMN_ID)     REFERENCES          OBJECTTYPEATTRIBUTE (ATTRIBUTE_ID) ON DELETE CASCADE
);

CREATE TABLE CUSTOMTABLEROW (
    ROW_ID                      INTEGER             NOT NULL PRIMARY KEY,
    TABLE_ID                    INTEGER             NOT NULL REFERENCES CUSTOMTABLE (TABLE_ID) ON DELETE CASCADE,
    FOREIGN KEY (ROW_ID)        REFERENCES          CASSINI_OBJECT (OBJECT_ID) ON DELETE CASCADE
);

CREATE TABLE STATICTABLE (
    TABLE_ID                    INTEGER             NOT NULL PRIMARY KEY,
    FOREIGN KEY (TABLE_ID)      REFERENCES          CUSTOMTABLE (TABLE_ID) ON DELETE CASCADE
);

CREATE TABLE TABLEATTRIBUTE (
    TABLE_ID                    INTEGER             NOT NULL PRIMARY KEY,
    ATTRIBUTE                   INTEGER             NOT NULL REFERENCES OBJECTTYPEATTRIBUTE (ATTRIBUTE_ID) ON DELETE CASCADE,
    FOREIGN KEY (TABLE_ID)      REFERENCES          CUSTOMTABLE (TABLE_ID) ON DELETE CASCADE
);

ALTER TABLE OBJECTTYPEATTRIBUTE ADD COLUMN TABLE_REF  INTEGER  REFERENCES TABLEATTRIBUTE (TABLE_ID) ON DELETE CASCADE;

CREATE TABLE OBJECTTABLEROW (
    ROW_ID                      INTEGER             NOT NULL PRIMARY KEY,
    OBJECT_ID                   INTEGER             NOT NULL REFERENCES CASSINI_OBJECT (OBJECT_ID) ON DELETE CASCADE,
    FOREIGN KEY (ROW_ID)        REFERENCES          CUSTOMTABLEROW (ROW_ID) ON DELETE CASCADE
);


CREATE TABLE AUTONUMBER (
    AUTONUMBER_ID           INTEGER             NOT NULL PRIMARY KEY,
    NAME                    TEXT                NOT NULL,
    DESCRIPTION             TEXT                ,
    NUMBERS                 INTEGER             NOT NULL,
    START                   INTEGER             NOT NULL,
    INCREMENT               INTEGER             NOT NULL,
    NEXT_NUMBER             INTEGER             NOT NULL,
    PADWITH                 VARCHAR(32)         ,
    PREFIX                  VARCHAR(32)         ,
    SUFFIX                  VARCHAR(32)         ,
    UNIQUE (NAME)
);

CREATE TABLE REFERENCED_OBJECT (
    ID                      INTEGER             NOT NULL PRIMARY KEY,
    REFERRING_OBJECT_TYPE   OBJECT_TYPE         NOT NULL,
    REFERRING_OBJECT_ID     INTEGER             NOT NULL REFERENCES CASSINI_OBJECT (OBJECT_ID) ON DELETE CASCADE,
    REFERRED_OBJECT_TYPE    OBJECT_TYPE         NOT NULL,
    REFERRED_OBJECT_ID      INTEGER             NOT NULL REFERENCES CASSINI_OBJECT (OBJECT_ID) ON DELETE CASCADE,
    REFERENCED_ON           TIMESTAMP           NOT NULL DEFAULT now()
);


CREATE TABLE APPLICATION_DETAILS (
  ID            INTEGER   NOT NULL PRIMARY KEY,
  OPTION_KEY    INTEGER   NOT NULL,
  OPTION_NAME   TEXT      NOT NULL,
  CREATED_DATE  TIMESTAMP NOT NULL,
  MODIFIED_DATE TIMESTAMP NOT NULL,
  VALUE         TEXT,
  UNIQUE (OPTION_KEY)
);

CREATE TABLE USER_ATTEMPTS (
    ID            INTEGER PRIMARY KEY  NOT NULL,
    USERNAME      TEXT                 NOT NULL,
    ATTEMPTS      INTEGER              NOT NULL,
    LAST_MODIFIED TIMESTAMP            NOT NULL
);


CREATE TABLE MEASUREMENT(
    ID                INTEGER       NOT NULL PRIMARY KEY DEFAULT nextval('MEASUREMENT_ID_SEQ'),
    NAME              TEXT          NOT NULL
);

CREATE TABLE MEASUREMENT_UNIT(
    ID                INTEGER               NOT NULL PRIMARY KEY DEFAULT nextval('MEASUREMENT_ID_SEQ'),
    MEASUREMENT       INTEGER               NOT NULL REFERENCES MEASUREMENT(ID) ON DELETE CASCADE,
    BASE_UNIT         BOOLEAN               DEFAULT FALSE,
    NAME              TEXT                  NOT NULL,
    SYMBOL            TEXT                  NOT NULL,
    CONVERSION_FACTOR DOUBLE PRECISION      NOT NULL,
    DEFAULT_VALUE     BOOLEAN               DEFAULT TRUE
);

ALTER TABLE OBJECTTYPEATTRIBUTE ADD COLUMN MEASUREMENT                   INTEGER           DEFAULT NULL;

ALTER TABLE OBJECTATTRIBUTE ADD COLUMN MEASUREMENT_UNIT INTEGER REFERENCES MEASUREMENT_UNIT(ID) ON DELETE CASCADE;

CREATE TABLE PORTAL_ACCOUNT (
    ID                INTEGER               NOT NULL PRIMARY KEY DEFAULT 1,
    EMAIL             TEXT                  NOT NULL,
    PASSWORD          TEXT                  NOT NULL,
    AUTH_KEY          TEXT                  NOT NULL
);

/* End core tables */


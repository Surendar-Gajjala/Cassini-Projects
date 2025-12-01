/* Begin core erp tables */
CREATE TABLE ERP_OBJECT (
    OBJECT_ID                   INTEGER             NOT NULL PRIMARY KEY,
    CREATED_DATE                TIMESTAMP           NOT NULL DEFAULT now(),
    /* CREATED_BY IS ADDED AFTER LOGIN TABLE IS CREATED */
    MODIFIED_DATE               TIMESTAMP           NOT NULL DEFAULT now(),
    /* MODIFIED_BY IS ADDED AFTER LOGIN TABLE IS CREATED */
    OBJECT_TYPE                 OBJECT_TYPE         NOT NULL
);

CREATE TABLE ERP_LOV (
    LOV_ID                      INTEGER             NOT NULL PRIMARY KEY,
    TYPE                        TEXT                ,
    NAME                        TEXT                NOT NULL,
    DESCRIPTION                 TEXT                ,
    VALUES                      TEXT                NOT NULL
);

CREATE TABLE ERP_OBJECTTYPEATTRIBUTE (
    ATTRIBUTE_ID                INTEGER             NOT NULL PRIMARY KEY,
    DATA_TYPE                   DATATYPE            NOT NULL,
    NAME                        TEXT                NOT NULL,
    DESCRIPTION                 TEXT                ,
    REQUIRED                    BOOLEAN             ,
    OBJECT_TYPE                 OBJECT_TYPE         NOT NULL,
    UNIQUE (OBJECT_TYPE, NAME)
);

CREATE TABLE ERP_ATTRIBUTELOV (
    ATTRIBUTE_ID                INTEGER             NOT NULL REFERENCES ERP_OBJECTTYPEATTRIBUTE (ATTRIBUTE_ID) ON DELETE CASCADE,
    LOV_ID                      INTEGER             NOT NULL REFERENCES ERP_LOV (LOV_ID) ON DELETE CASCADE,
    UNIQUE(ATTRIBUTE_ID, LOV_ID)
);


CREATE TABLE ERP_OBJECTATTRIBUTE (
    OBJECT_ID                   INTEGER             NOT NULL REFERENCES ERP_OBJECT (OBJECT_ID) ON DELETE CASCADE,
    ATTRIBUTEDEF                INTEGER             NOT NULL REFERENCES ERP_OBJECTTYPEATTRIBUTE (ATTRIBUTE_ID) ON DELETE CASCADE,
    STRING_VALUE                TEXT                ,
    INTEGER_VALUE               INTEGER             ,
    DOUBLE_VALUE                DOUBLE PRECISION    ,
    DATE_VALUE                  TIMESTAMP           ,
    BOOLEAN_VALUE               BOOLEAN             ,
    SINGLELIST_VALUE            TEXT                ,
    MULTILIST_VALUE             TEXT                ,
    UNIQUE (OBJECT_ID, ATTRIBUTEDEF)
);

CREATE TABLE ERP_AUTONUMBER (
    AUTONUMBER_ID           INTEGER             NOT NULL PRIMARY KEY,
    NAME                    TEXT                NOT NULL,
    DESCRIPTION             TEXT                ,
    NUMBERS                 INTEGER             NOT NULL,
    START                   INTEGER             NOT NULL,
    INCREMENT               INTEGER             NOT NULL,
    NEXT_NUMBER             INTEGER             NOT NULL,
    PADWITH                 VARCHAR(32)         ,
    PREFIX                  VARCHAR(32)         ,
    SUFFIX                  VARCHAR(32)
);

CREATE TABLE ERP_COUNTRY (
    COUNTRY_ID                  INTEGER             NOT NULL PRIMARY KEY,
    NAME                        VARCHAR(100)        NOT NULL UNIQUE,
    SHORT_NAME                  VARCHAR(10)         ,
    FOREIGN KEY (COUNTRY_ID)    REFERENCES          ERP_OBJECT (OBJECT_ID)
);

CREATE TABLE ERP_STATE (
    STATE_ID                    INTEGER             NOT NULL PRIMARY KEY,
    COUNTRY                     INTEGER             NOT NULL REFERENCES ERP_COUNTRY (COUNTRY_ID) ON DELETE CASCADE,
    NAME                        VARCHAR(100)        NOT NULL UNIQUE,
    SHORT_NAME                  VARCHAR(10)         ,
    FOREIGN KEY (STATE_ID)      REFERENCES          ERP_OBJECT (OBJECT_ID)
);

CREATE TABLE ERP_DISTRICT (
    DISTRICT_ID                 INTEGER             NOT NULL PRIMARY KEY,
    STATE                       INTEGER             NOT NULL REFERENCES ERP_STATE (STATE_ID) ON DELETE CASCADE,
    NAME                        VARCHAR(100)        NOT NULL UNIQUE,
    FOREIGN KEY (DISTRICT_ID)   REFERENCES          ERP_OBJECT (OBJECT_ID) ON DELETE CASCADE
);

CREATE TABLE ERP_ADDRESSTYPE (
    TYPE_ID                     INTEGER             NOT NULL PRIMARY KEY,
    NAME                        TEXT                NOT NULL UNIQUE,
    DESCRIPTION                 TEXT
);

CREATE TABLE ERP_ADDRESS (
    ADDRESS_ID                  INTEGER             NOT NULL PRIMARY KEY,
    ADDRESS_TYPE                INTEGER             NOT NULL REFERENCES ERP_ADDRESSTYPE (TYPE_ID) ON DELETE CASCADE,
    ADDRESS_TEXT                TEXT                NOT NULL,
    CITY                        VARCHAR(100)        ,
    DISTRICT                    VARCHAR(100)        ,
    STATE                       INTEGER             NOT NULL REFERENCES ERP_STATE (STATE_ID) ON DELETE CASCADE,
    COUNTRY                     INTEGER             NOT NULL REFERENCES ERP_COUNTRY (COUNTRY_ID) ON DELETE CASCADE,
    PINCODE                     VARCHAR(20)         ,
    FOREIGN KEY (ADDRESS_ID)    REFERENCES          ERP_OBJECT (OBJECT_ID) ON DELETE CASCADE
);

CREATE TABLE ERP_PERSONTYPE (
    TYPE_ID                     INTEGER             NOT NULL PRIMARY KEY,
    NAME                        TEXT                NOT NULL UNIQUE,
    DESCRIPTION                 TEXT
);


CREATE TABLE ERP_MOBILEDEVICE (
    DEVICE_ID                   TEXT                NOT NULL PRIMARY KEY,
    SENDER_ID                   TEXT                NOT NULL,
    OS                          TEXT                ,
    OS_VERSION                  TEXT                ,
    DEVICE_INFO                 TEXT                ,
    DISABLE_PUSHNOTIFICATION    BOOLEAN             DEFAULT FALSE
);


CREATE TABLE ERP_PERSON (
    PERSON_ID                   INTEGER             NOT NULL PRIMARY KEY,
    PERSON_TYPE                 INTEGER             NOT NULL REFERENCES ERP_PERSONTYPE (TYPE_ID) ON DELETE CASCADE,
    TITLE                       VARCHAR(10)         ,
    FIRST_NAME                  VARCHAR(32)         ,
    LAST_NAME                   VARCHAR(32)         ,
    MIDDLE_NAME                 VARCHAR(32)         ,
    PHONE_OFFICE                VARCHAR(100)        ,
    PHONE_MOBILE                VARCHAR(100)        ,
    EMAIL                       VARCHAR(100)        ,
    MOBILE_DEVICE               TEXT                REFERENCES ERP_MOBILEDEVICE (DEVICE_ID) ON DELETE SET NULL,
    FOREIGN KEY (PERSON_ID)     REFERENCES          ERP_OBJECT (OBJECT_ID) ON DELETE CASCADE
);

CREATE TABLE ERP_PERSONADDRESS (
    PERSON_ID                   INTEGER             NOT NULL REFERENCES ERP_PERSON (PERSON_ID) ON DELETE CASCADE,
    ADDRESS_ID                  INTEGER             NOT NULL REFERENCES ERP_ADDRESS (ADDRESS_ID) ON DELETE CASCADE,
    UNIQUE (PERSON_ID, ADDRESS_ID)
);

CREATE TABLE ERP_LOCATIONAWAREOBJECT (
    OBJECT_ID                   INTEGER             NOT NULL PRIMARY KEY,
    FOREIGN KEY (OBJECT_ID)     REFERENCES          ERP_OBJECT (OBJECT_ID)
);

CREATE TABLE ERP_OBJECTGEOLOCATION (
    OBJECT_ID                   INTEGER             NOT NULL PRIMARY KEY REFERENCES ERP_LOCATIONAWAREOBJECT (OBJECT_ID) ON DELETE CASCADE,
    OBJECT_TYPE                 OBJECT_TYPE         NOT NULL,
    LATITUDE                    DOUBLE PRECISION    NOT NULL,
    LONGITUDE                   DOUBLE PRECISION    NOT NULL,
    UNIQUE (OBJECT_ID, OBJECT_TYPE)
);


/* End login tables */

ALTER TABLE ERP_OBJECT ADD COLUMN CREATED_BY INTEGER REFERENCES ERP_PERSON (PERSON_ID) ON DELETE CASCADE;
ALTER TABLE ERP_OBJECT ADD COLUMN MODIFIED_BY INTEGER REFERENCES ERP_PERSON (PERSON_ID) ON DELETE CASCADE;



CREATE TYPE WORK_PRIORITY AS ENUM (
    'LOW',
    'MEDIUM',
    'HIGH',
    'CRITICAL'
);

CREATE TYPE WORK_REQUEST_STATUS AS ENUM (
    'PENDING',
    'FINISH'
);

CREATE TYPE WORK_ORDER_STATUS AS ENUM (
    'OPEN',
    'INPROGRESS',
    'ONHOLD',
    'FINISH'
);

CREATE TYPE METER_TYPE AS ENUM (
    'CONTINUOUS',
    'GUAGE'
);

CREATE TYPE METER_READING_TYPE AS ENUM (
    'ABSOLUTE',
    'CHANGE'
);

CREATE TYPE PART_DISPOSITION AS ENUM (
    'REPLACE',
    'REPAIR'
);

CREATE TYPE OPERATION_PARAM_VALUE_TYPE AS ENUM (
    'NONE',
    'TEXT',
    'INTEGER',
    'DECIMAL',
    'BOOLEAN',
    'LIST'
);

CREATE TYPE WORKORDER_TYPE AS ENUM (
    'REPAIR',
    'MAINTENANCE'
);

CREATE TYPE OPERATION_RESULT AS ENUM (
    'NONE',
    'PASS',
    'FAIL'
);

CREATE TABLE MRO_OBJECT_TYPE (
    ID                          INTEGER             NOT NULL PRIMARY KEY,
    NAME                        TEXT                NOT NULL,
    DESCRIPTION                 TEXT                ,
    PARENT_TYPE                 INTEGER             REFERENCES MRO_OBJECT_TYPE (ID) ON DELETE CASCADE,
    AUTONUMBER_SOURCE           INTEGER             REFERENCES AUTONUMBER (AUTONUMBER_ID) ON DELETE CASCADE,
    FOREIGN KEY (ID)            REFERENCES          CASSINI_OBJECT (OBJECT_ID) ON DELETE CASCADE
);

CREATE TABLE MRO_OBJECT_TYPE_ATTRIBUTE (
    ATTRIBUTE_ID                INTEGER             NOT NULL PRIMARY KEY,
    TYPE                        INTEGER             NOT NULL REFERENCES MRO_OBJECT_TYPE (ID) ON DELETE CASCADE,
    SEQ                         INTEGER             ,
    FOREIGN KEY (ATTRIBUTE_ID)  REFERENCES          OBJECTTYPEATTRIBUTE (ATTRIBUTE_ID) ON DELETE CASCADE
);

CREATE TABLE MRO_OBJECT (
    ID                          INTEGER                 NOT NULL PRIMARY KEY,
    NAME                        TEXT                    NOT NULL,
    NUMBER                      TEXT                    NOT NULL,
    DESCRIPTION                 TEXT                    ,
    FOREIGN KEY (ID)            REFERENCES              CASSINI_OBJECT (OBJECT_ID) ON DELETE CASCADE
);

CREATE TABLE MRO_OBJECT_ATTRIBUTE (
    OBJECT                        INTEGER             NOT NULL REFERENCES MRO_OBJECT (ID) ON DELETE CASCADE,
    ATTRIBUTE                     INTEGER             NOT NULL REFERENCES MRO_OBJECT_TYPE_ATTRIBUTE (ATTRIBUTE_ID) ON DELETE CASCADE,
    FOREIGN KEY (OBJECT, ATTRIBUTE)  REFERENCES       OBJECTATTRIBUTE (OBJECT_ID, ATTRIBUTEDEF)  ON DELETE CASCADE,
    UNIQUE (OBJECT, ATTRIBUTE)
);

CREATE TABLE MRO_OBJECT_FILE (
    ID                          INTEGER             NOT NULL PRIMARY KEY,
    OBJECT                      INTEGER             NOT NULL REFERENCES MRO_OBJECT (ID) ON DELETE CASCADE,
    FOREIGN KEY (ID)            REFERENCES          PLM_FILE (FILE_ID) ON DELETE CASCADE
);

CREATE TABLE MRO_ASSET_TYPE (
    ID                          INTEGER                 NOT NULL PRIMARY KEY,
    FOREIGN KEY (ID)            REFERENCES              MRO_OBJECT_TYPE (ID) ON DELETE CASCADE
);

CREATE TABLE MRO_METER_TYPE (
    ID                          INTEGER                 NOT NULL PRIMARY KEY,
    FOREIGN KEY (ID)            REFERENCES              MRO_OBJECT_TYPE (ID) ON DELETE CASCADE
);

CREATE TABLE MRO_ASSET (
    ID                          INTEGER                 NOT NULL PRIMARY KEY,
    TYPE                        INTEGER                 REFERENCES MRO_ASSET_TYPE(ID) ON DELETE CASCADE,
    METERED                     BOOLEAN                 NOT NULL DEFAULT FALSE,
    RESOURCE                    INTEGER                 NOT NULL REFERENCES MES_OBJECT (ID) ON DELETE CASCADE,
    RESOURCE_TYPE               MES_TYPE        NOT NULL,
    FOREIGN KEY (ID)            REFERENCES              MRO_OBJECT (ID) ON DELETE CASCADE
);


CREATE TABLE MRO_METER (
    ID                          INTEGER                 NOT NULL PRIMARY KEY,
    TYPE                        INTEGER                 REFERENCES MRO_METER_TYPE(ID) ON DELETE CASCADE,
    METER_TYPE                  METER_TYPE              NOT NULL DEFAULT 'CONTINUOUS',
    READING_TYPE                METER_READING_TYPE      NOT NULL DEFAULT 'ABSOLUTE',
    QOM                         INTEGER                 NOT NULL REFERENCES MEASUREMENT (ID) ON DELETE CASCADE,
    UOM                         INTEGER                 NOT NULL REFERENCES MEASUREMENT_UNIT (ID) ON DELETE CASCADE,
    FOREIGN KEY (ID)            REFERENCES              MRO_OBJECT (ID) ON DELETE CASCADE
);

CREATE TABLE MRO_ASSET_METER (
    ID                          INTEGER                 NOT NULL PRIMARY KEY,
    ASSET                       INTEGER                 REFERENCES MRO_ASSET (ID) ON DELETE CASCADE,
    METER                       INTEGER                 NOT NULL REFERENCES MRO_METER (ID) ON DELETE CASCADE
);

CREATE TABLE MRO_METER_READING (
    ID                          INTEGER                 NOT NULL PRIMARY KEY,
    TIMESTAMP                   TIMESTAMP               NOT NULL DEFAULT CURRENT_TIMESTAMP,
    ASSET_METER                 INTEGER                 NOT NULL REFERENCES MRO_ASSET_METER(ID) ON DELETE CASCADE,
    VALUE                       DOUBLE PRECISION
);

CREATE TABLE MRO_SPAREPART_TYPE (
    ID                          INTEGER                 NOT NULL PRIMARY KEY,
    FOREIGN KEY (ID)            REFERENCES              MRO_OBJECT_TYPE (ID) ON DELETE CASCADE
);

CREATE TABLE MRO_WORKORDER_TYPE (
    ID                          INTEGER                 NOT NULL PRIMARY KEY,
    TYPE                        WORKORDER_TYPE          NOT NULL,
    FOREIGN KEY (ID)            REFERENCES              MRO_OBJECT_TYPE (ID) ON DELETE CASCADE
);

CREATE TABLE MRO_WORKREQUEST_TYPE (
    ID                          INTEGER                 NOT NULL PRIMARY KEY,
    FOREIGN KEY (ID)            REFERENCES              MRO_OBJECT_TYPE (ID) ON DELETE CASCADE
);

CREATE TABLE MRO_SPAREPART (
    ID                          INTEGER                 NOT NULL PRIMARY KEY,
    TYPE                        INTEGER                 REFERENCES MRO_SPAREPART_TYPE(ID) ON DELETE CASCADE,
    FOREIGN KEY (ID)            REFERENCES              MRO_OBJECT (ID) ON DELETE CASCADE
);

CREATE TABLE MRO_ASSET_SPAREPART (
    ID                          INTEGER                 NOT NULL PRIMARY KEY,
    ASSET                       INTEGER                 REFERENCES MRO_ASSET (ID) ON DELETE CASCADE,
    PART                        INTEGER                 REFERENCES MRO_SPAREPART (ID) ON DELETE CASCADE
);

CREATE TABLE MRO_MAINTENANCE_PLAN (
    ID                          INTEGER                 NOT NULL PRIMARY KEY,
    ASSET                       INTEGER                 NOT NULL REFERENCES MRO_ASSET (ID) ON DELETE CASCADE,
    FOREIGN KEY (ID)            REFERENCES              MRO_OBJECT (ID) ON DELETE CASCADE
);

CREATE TABLE MRO_MAINTENANCE_OPERATION (
    ID                          INTEGER                 NOT NULL PRIMARY KEY,
    MAINTENANCE_PLAN            INTEGER                 NOT NULL REFERENCES MRO_MAINTENANCE_PLAN (ID) ON DELETE CASCADE,
    NAME                        TEXT                    NOT NULL,
    DESCRIPTION                 TEXT                    ,
    PARAM_NAME                  TEXT                    ,
    PARAM_TYPE                  OPERATION_PARAM_VALUE_TYPE   NOT NULL DEFAULT 'NONE',
    LOV                         INTEGER                 REFERENCES LOV (LOV_ID) ON DELETE SET NULL,
    MODIFIED_DATE               TIMESTAMP               NOT NULL DEFAULT now()
);

CREATE TABLE MRO_WORK_REQUEST (
    ID                          INTEGER                 NOT NULL PRIMARY KEY,
    TYPE                        INTEGER                 REFERENCES MRO_WORKREQUEST_TYPE(ID) ON DELETE CASCADE,
    ASSET                       INTEGER                 NOT NULL REFERENCES MRO_ASSET (ID) ON DELETE CASCADE,
    REQUESTOR                   INTEGER                 NOT NULL REFERENCES PERSON (PERSON_ID) ON DELETE CASCADE,
    PRIORITY                    WORK_PRIORITY           NOT NULL DEFAULT 'LOW',
    STATUS                      WORK_REQUEST_STATUS     NOT NULL DEFAULT 'PENDING',
    ATTACHMENTS                 INTEGER[]               ,
    NOTES                       TEXT                    ,
    FOREIGN KEY (ID)            REFERENCES              MRO_OBJECT (ID) ON DELETE CASCADE
);

CREATE TABLE MRO_WORK_ORDER (
    ID                          INTEGER                 NOT NULL PRIMARY KEY,
    TYPE                        INTEGER                 REFERENCES MRO_WORKORDER_TYPE(ID) ON DELETE CASCADE,
    ASSET                       INTEGER                 NOT NULL REFERENCES MRO_ASSET (ID) ON DELETE CASCADE,
    REQUEST                     INTEGER                 REFERENCES MRO_WORK_REQUEST(ID) ON DELETE SET NULL,
    PLAN                        INTEGER                 REFERENCES MRO_MAINTENANCE_PLAN(ID) ON DELETE SET NULL,
    ASSIGNED_TO                 INTEGER                 NOT NULL REFERENCES PERSON (PERSON_ID) ON DELETE CASCADE,
    PRIORITY                    WORK_PRIORITY           NOT NULL DEFAULT 'LOW',
    STATUS                      WORK_ORDER_STATUS       NOT NULL DEFAULT 'OPEN',
    NOTES                       TEXT                    ,
    FOREIGN KEY (ID)            REFERENCES              MRO_OBJECT (ID) ON DELETE CASCADE
);

CREATE TABLE MRO_WORK_ORDER_RESOURCE (
    ID                          INTEGER                 NOT NULL PRIMARY KEY,
    WORK_ORDER                  INTEGER                 NOT NULL REFERENCES MRO_WORK_ORDER (ID) ON DELETE CASCADE,
    RESOURCE_TYPE               OBJECT_TYPE             NOT NULL,
    RESOURCE_ID                 INTEGER                 NOT NULL
);

ALTER TABLE MRO_WORK_ORDER ADD COLUMN WORKFLOW INTEGER REFERENCES PLM_WORKFLOW (ID) ON DELETE SET NULL;

CREATE TABLE MRO_WORKORDER_OPERATION (
    ID                          INTEGER                 NOT NULL PRIMARY KEY,
    WORKORDER                   INTEGER                 NOT NULL REFERENCES MRO_WORK_ORDER (ID) ON DELETE CASCADE,
    NAME                        TEXT                    NOT NULL,
    DESCRIPTION                 TEXT                    ,
    PARAM_NAME                  TEXT                    ,
    PARAM_TYPE                  OPERATION_PARAM_VALUE_TYPE   NOT NULL DEFAULT 'NONE',
    LOV                         INTEGER                 REFERENCES LOV (LOV_ID) ON DELETE SET NULL,
    TEXT_VALUE                  TEXT                    ,
    INTEGER_VALUE               INTEGER                 ,
    DECIMAL_VALUE               DOUBLE PRECISION        ,
    LIST_VALUE                  TEXT                    ,
    BOOLEAN_VALUE               BOOLEAN                 ,
    RESULT                      OPERATION_RESULT   NOT NULL DEFAULT 'PASS',
    NOTES                       TEXT
);

CREATE TABLE MRO_WORKORDER_PART (
    ID                          INTEGER                 NOT NULL PRIMARY KEY,
    WORK_ORDER                  INTEGER                 NOT NULL REFERENCES MRO_WORK_ORDER (ID) ON DELETE CASCADE,
    PART                        INTEGER                 NOT NULL REFERENCES MRO_SPAREPART (ID) ON DELETE CASCADE,
    QUANTITY                    INTEGER                 NOT NULL DEFAULT 1,
    SERIAL_NUMBERS              TEXT[]                  ,
    DISPOSITION                 PART_DISPOSITION        NOT NULL DEFAULT 'REPLACE',
    NOTES                       TEXT                    ,
    MODIFIED_DATE               TIMESTAMP               NOT NULL DEFAULT now()
);

CREATE TABLE MRO_WORKORDER_INSTRUCTIONS (
    ID                          INTEGER                 NOT NULL PRIMARY KEY,
    WORKORDER                  INTEGER                 NOT NULL REFERENCES MRO_WORK_ORDER (ID) ON DELETE CASCADE,
    INSTRUCTIONS                   TEXT                  NOT NULL

);
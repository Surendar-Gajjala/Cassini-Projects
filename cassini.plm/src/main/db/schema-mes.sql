CREATE TYPE JIG_FIXTURE_TYPE AS ENUM (
    'JIG',
    'FIXTURE'
);
CREATE TYPE MES_BOP_PLAN_TYPE AS ENUM (
    'OPERATION',
    'PHANTOM'
);
CREATE TYPE OPERATION_PART_TYPE AS ENUM (
  'CONSUMED',
  'PRODUCED'
);

CREATE TYPE MES_BOM_ITEM_TYPE AS ENUM (
    'NORMAL',
    'PHANTOM'
);

CREATE TABLE MES_PHANTOM (
    ID                          INTEGER                 NOT NULL PRIMARY KEY,
    NAME                        TEXT                    NOT NULL,
    NUMBER                      TEXT                    NOT NULL,
    DESCRIPTION                 TEXT                    ,
    FOREIGN KEY (ID)            REFERENCES              CASSINI_OBJECT (OBJECT_ID) ON DELETE CASCADE
);

CREATE TABLE MES_OBJECT_TYPE (
    ID                          INTEGER             NOT NULL PRIMARY KEY,
    NAME                        TEXT                NOT NULL,
    DESCRIPTION                 TEXT                ,
    PARENT_TYPE                 INTEGER             REFERENCES MES_OBJECT_TYPE (ID) ON DELETE CASCADE,
    AUTONUMBER_SOURCE           INTEGER             REFERENCES AUTONUMBER (AUTONUMBER_ID) ON DELETE CASCADE,
    FOREIGN KEY (ID)            REFERENCES          CASSINI_OBJECT (OBJECT_ID) ON DELETE CASCADE
);
CREATE TABLE MES_OBJECT_TYPE_ATTRIBUTE (
    ATTRIBUTE_ID                INTEGER             NOT NULL PRIMARY KEY,
    TYPE                        INTEGER             NOT NULL REFERENCES MES_OBJECT_TYPE (ID) ON DELETE CASCADE,
    SEQ                         INTEGER             ,
    FOREIGN KEY (ATTRIBUTE_ID)  REFERENCES          OBJECTTYPEATTRIBUTE (ATTRIBUTE_ID) ON DELETE CASCADE
);
CREATE TABLE MES_PLANT_TYPE (
    ID                          INTEGER             NOT NULL PRIMARY KEY,
    FOREIGN KEY (ID)            REFERENCES          MES_OBJECT_TYPE (ID) ON DELETE CASCADE
);
CREATE TABLE MES_ASSEMBLYLINE_TYPE (
    ID                          INTEGER                 NOT NULL PRIMARY KEY,
    FOREIGN KEY (ID)            REFERENCES              MES_OBJECT_TYPE (ID) ON DELETE CASCADE
);
CREATE TABLE MES_WORKCENTER_TYPE (
    ID                          INTEGER             NOT NULL PRIMARY KEY,
    FOREIGN KEY (ID)            REFERENCES          MES_OBJECT_TYPE (ID) ON DELETE CASCADE
);
CREATE TABLE MES_MACHINE_TYPE (
    ID                          INTEGER             NOT NULL PRIMARY KEY,
    FOREIGN KEY (ID)            REFERENCES          MES_OBJECT_TYPE (ID) ON DELETE CASCADE
);
CREATE TABLE MES_OPERATION_TYPE (
    ID                          INTEGER             NOT NULL PRIMARY KEY,
    FOREIGN KEY (ID)            REFERENCES          MES_OBJECT_TYPE (ID) ON DELETE CASCADE
);
CREATE TABLE MES_PRODUCTION_ORDER_TYPE (
    ID                          INTEGER             NOT NULL PRIMARY KEY,
    LIFECYCLE                   INTEGER             REFERENCES PLM_LIFECYCLE (ID) ON DELETE CASCADE,
    FOREIGN KEY (ID)            REFERENCES          MES_OBJECT_TYPE (ID) ON DELETE CASCADE
);
CREATE TABLE MES_SERVICE_ORDER_TYPE (
    ID                          INTEGER             NOT NULL PRIMARY KEY,
    LIFECYCLE                   INTEGER             REFERENCES PLM_LIFECYCLE (ID) ON DELETE CASCADE,
    FOREIGN KEY (ID)            REFERENCES          MES_OBJECT_TYPE (ID) ON DELETE CASCADE
);
CREATE TABLE MES_MATERIAL_TYPE (
    ID                          INTEGER             NOT NULL PRIMARY KEY,
    FOREIGN KEY (ID)            REFERENCES          MES_OBJECT_TYPE (ID) ON DELETE CASCADE
);
CREATE TABLE MES_JIGS_FIXTURES_TYPE (
    ID                          INTEGER             NOT NULL PRIMARY KEY,
    FOREIGN KEY (ID)            REFERENCES          MES_OBJECT_TYPE (ID) ON DELETE CASCADE
);
CREATE TABLE MES_TOOL_TYPE (
    ID                          INTEGER             NOT NULL PRIMARY KEY,
    FOREIGN KEY (ID)            REFERENCES           MES_OBJECT_TYPE (ID) ON DELETE CASCADE
);
CREATE TABLE MES_EQUIPMENT_TYPE (
    ID                          INTEGER             NOT NULL PRIMARY KEY,
    FOREIGN KEY (ID)            REFERENCES           MES_OBJECT_TYPE (ID) ON DELETE CASCADE
);
CREATE TABLE MES_INSTRUMENT_TYPE (
    ID                          INTEGER             NOT NULL PRIMARY KEY,
    FOREIGN KEY (ID)            REFERENCES           MES_OBJECT_TYPE (ID) ON DELETE CASCADE
);
CREATE TABLE MES_MANPOWER_TYPE (
    ID                          INTEGER             NOT NULL PRIMARY KEY,
    FOREIGN KEY (ID)            REFERENCES          MES_OBJECT_TYPE (ID) ON DELETE CASCADE
);
CREATE TABLE MES_OBJECT (
     ID                         INTEGER                 NOT NULL PRIMARY KEY,
     NAME                       TEXT                    NOT NULL,
     NUMBER                     TEXT                    NOT NULL,
     DESCRIPTION                TEXT                    ,
     FOREIGN KEY (ID)           REFERENCES              CASSINI_OBJECT (OBJECT_ID) ON DELETE CASCADE
);
CREATE TABLE MES_OBJECT_ATTRIBUTE (
    OBJECT                        INTEGER             NOT NULL REFERENCES MES_OBJECT (ID) ON DELETE CASCADE,
    ATTRIBUTE                     INTEGER             NOT NULL REFERENCES MES_OBJECT_TYPE_ATTRIBUTE (ATTRIBUTE_ID) ON DELETE CASCADE,
    FOREIGN KEY (OBJECT, ATTRIBUTE)  REFERENCES       OBJECTATTRIBUTE (OBJECT_ID, ATTRIBUTEDEF)  ON DELETE CASCADE,
    UNIQUE (OBJECT, ATTRIBUTE)
);
CREATE TABLE MES_OBJECT_FILE (
    ID                          INTEGER             NOT NULL PRIMARY KEY,
    OBJECT                      INTEGER             NOT NULL REFERENCES MES_OBJECT (ID) ON DELETE CASCADE,
    FOREIGN KEY (ID)            REFERENCES          PLM_FILE (FILE_ID) ON DELETE CASCADE
);
CREATE TABLE MES_PLANT (
     ID                         INTEGER                 NOT NULL PRIMARY KEY,
     TYPE                       INTEGER                 REFERENCES MES_PLANT_TYPE (ID) ON DELETE CASCADE,
     ADDRESS                    TEXT                    ,
     CITY                       TEXT                    ,
     COUNTRY                    TEXT                    ,
     POSTAL_CODE                TEXT                    ,
     PHONE_NUMBER               TEXT                    ,
     MOBILE_NUMBER              TEXT                    ,
     FAX_NUMBER                 TEXT                    ,
     EMAIL                      TEXT                    ,
     PLANT_MANAGER              INTEGER                 REFERENCES PERSON(PERSON_ID) ON DELETE CASCADE,
     NOTES                      TEXT                    ,
     REQUIRES_MAINTENANCE       BOOLEAN                 NOT NULL DEFAULT TRUE,
     FOREIGN KEY (ID)           REFERENCES              MES_OBJECT (ID) ON DELETE CASCADE
);
CREATE TABLE MES_ASSEMBLYLINE (
     ID                         INTEGER                 NOT NULL PRIMARY KEY,
     TYPE                       INTEGER                 REFERENCES MES_ASSEMBLYLINE_TYPE (ID) ON DELETE CASCADE,
     PLANT                      INTEGER                 REFERENCES MES_PLANT(ID) ON DELETE CASCADE,
     FOREIGN KEY (ID)           REFERENCES              MES_OBJECT (ID) ON DELETE CASCADE
);
CREATE TABLE MES_WORKCENTER (
     ID                         INTEGER                 NOT NULL PRIMARY KEY,
     TYPE                       INTEGER                 REFERENCES MES_WORKCENTER_TYPE (ID) ON DELETE CASCADE,
     PLANT                      INTEGER                 NOT NULL REFERENCES MES_PLANT(ID) ON DELETE CASCADE,
     ASSEMBLY_LINE              INTEGER                 REFERENCES MES_ASSEMBLYLINE(ID) ON DELETE SET NULL,
     ACTIVE                     BOOLEAN                 NOT NULL DEFAULT TRUE,
     LOCATION                   TEXT                    ,
     REQUIRES_MAINTENANCE       BOOLEAN                 NOT NULL DEFAULT TRUE,
     FOREIGN KEY (ID)           REFERENCES              MES_OBJECT (ID) ON DELETE CASCADE
);

CREATE TABLE MES_WORKCENTER_RESOURCE (
     ID                         INTEGER                 NOT NULL PRIMARY KEY,
     WORK_CENTER                INTEGER                 NOT NULL REFERENCES MES_WORKCENTER (ID) ON DELETE CASCADE,
     OBJECT_ID                  INTEGER                 NOT NULL REFERENCES MES_OBJECT (ID) ON DELETE CASCADE,
     MES_OBJECT_TYPE            OBJECT_TYPE             NOT NULL
--      SHIFT                      INTEGER                 REFERENCES MES_SHIFT (ID) ON DELETE CASCADE /*ADDED after shift table*/
);

CREATE TABLE MES_MACHINE (
     ID                         INTEGER                 NOT NULL PRIMARY KEY,
     TYPE                       INTEGER                 REFERENCES MES_MACHINE_TYPE (ID) ON DELETE CASCADE,
     REQUIRES_MAINTENANCE       BOOLEAN                 NOT NULL DEFAULT TRUE,
     ACTIVE                     BOOLEAN                 NOT NULL DEFAULT TRUE,
     WORK_CENTER                INTEGER                 REFERENCES MES_WORKCENTER (ID) ON DELETE CASCADE,
     IMAGE                      BYTEA                   ,
     FOREIGN KEY (ID)           REFERENCES              MES_OBJECT (ID) ON DELETE CASCADE
);

CREATE TABLE MES_EQUIPMENT (
     ID                         INTEGER                 NOT NULL PRIMARY KEY,
     TYPE                       INTEGER                 REFERENCES MES_EQUIPMENT_TYPE (ID) ON DELETE CASCADE,
     REQUIRES_MAINTENANCE       BOOLEAN                 NOT NULL DEFAULT TRUE,
     ACTIVE                     BOOLEAN                 NOT NULL DEFAULT TRUE,
     IMAGE                      BYTEA                   ,
     FOREIGN KEY (ID)           REFERENCES              MES_OBJECT (ID) ON DELETE CASCADE
);

CREATE TABLE MES_INSTRUMENT (
     ID                         INTEGER                 NOT NULL PRIMARY KEY,
     TYPE                       INTEGER                 REFERENCES MES_INSTRUMENT_TYPE (ID) ON DELETE CASCADE,
     REQUIRES_MAINTENANCE       BOOLEAN                 NOT NULL DEFAULT TRUE,
     ACTIVE                     BOOLEAN                 NOT NULL DEFAULT TRUE,
     IMAGE                      BYTEA                   ,
     FOREIGN KEY (ID)           REFERENCES              MES_OBJECT (ID) ON DELETE CASCADE
);

CREATE TABLE MES_TOOL (
     ID                         INTEGER                 NOT NULL PRIMARY KEY,
     TYPE                       INTEGER                 REFERENCES MES_TOOL_TYPE (ID) ON DELETE CASCADE,
     REQUIRES_MAINTENANCE       BOOLEAN                 NOT NULL DEFAULT TRUE,
     ACTIVE                     BOOLEAN                 NOT NULL DEFAULT TRUE,
     IMAGE                      BYTEA                   ,
     FOREIGN KEY (ID)           REFERENCES              MES_OBJECT (ID) ON DELETE CASCADE
);

CREATE TABLE MES_JIGS_FIXTURE (
     ID                         INTEGER                 NOT NULL PRIMARY KEY,
     TYPE                       INTEGER                 REFERENCES MES_JIGS_FIXTURES_TYPE (ID) ON DELETE CASCADE,
     REQUIRES_MAINTENANCE       BOOLEAN                 NOT NULL DEFAULT TRUE,
     ACTIVE                     BOOLEAN                 NOT NULL DEFAULT TRUE,
     DEVICE_TYPE                JIG_FIXTURE_TYPE        NOT NULL DEFAULT 'JIG',
     IMAGE                      BYTEA                   ,
     FOREIGN KEY (ID)           REFERENCES              MES_OBJECT (ID) ON DELETE CASCADE
);

CREATE TABLE MES_MATERIAL (
     ID                         INTEGER                 NOT NULL PRIMARY KEY,
     TYPE                       INTEGER                 REFERENCES MES_MATERIAL_TYPE (ID) ON DELETE CASCADE,
     QOM                        INTEGER                 REFERENCES MEASUREMENT (ID) ON DELETE CASCADE,
     UOM                        INTEGER                 REFERENCES MEASUREMENT_UNIT (ID) ON DELETE CASCADE,
     IMAGE                      BYTEA                   ,
     FOREIGN KEY (ID)           REFERENCES              MES_OBJECT (ID) ON DELETE CASCADE
);

CREATE TABLE MES_MANPOWER (
     ID                         INTEGER                 NOT NULL PRIMARY KEY,
     TYPE                       INTEGER                 REFERENCES MES_MANPOWER_TYPE (ID) ON DELETE CASCADE,
    --  PERSON                     INTEGER                 NOT NULL REFERENCES PERSON (PERSON_ID) ON DELETE CASCADE,
    --  ACTIVE                     BOOLEAN                 NOT NULL DEFAULT TRUE,
     FOREIGN KEY (ID)           REFERENCES              MES_OBJECT (ID) ON DELETE CASCADE
);

CREATE TABLE MES_MANPOWER_CONTACT (
     ID                         INTEGER                 NOT NULL PRIMARY KEY,
     MANPOWER                   INTEGER                 REFERENCES MES_MANPOWER (ID) ON DELETE CASCADE,
     CONTACT                    INTEGER                 NOT NULL REFERENCES PERSON (PERSON_ID) ON DELETE CASCADE,
     ACTIVE                     BOOLEAN                 NOT NULL DEFAULT TRUE
);

CREATE TABLE MES_SHIFT (
     ID                         INTEGER                 NOT NULL PRIMARY KEY,
     START_TIME                 TIME                    NOT NULL,
     END_TIME                   TIME                    NOT NULL,
     FOREIGN KEY (ID)           REFERENCES              MES_OBJECT (ID) ON DELETE CASCADE
);

ALTER TABLE MES_WORKCENTER_RESOURCE ADD COLUMN SHIFT                      INTEGER                 REFERENCES MES_SHIFT (ID) ON DELETE CASCADE;

CREATE TABLE MES_SHIFT_PERSON (
     ID                         INTEGER                 NOT NULL PRIMARY KEY,
     SHIFT                      INTEGER                 REFERENCES MES_SHIFT (ID) ON DELETE CASCADE,
     PERSON                     INTEGER                 NOT NULL REFERENCES PERSON (PERSON_ID) ON DELETE CASCADE,
     NOTES                      TEXT
);

CREATE TABLE MES_OPERATION (
     ID                         INTEGER                 NOT NULL PRIMARY KEY,
     TYPE                       INTEGER                 REFERENCES MES_OPERATION_TYPE (ID) ON DELETE CASCADE,
     FOREIGN KEY (ID)           REFERENCES              MES_OBJECT (ID) ON DELETE CASCADE
);

CREATE TABLE MES_OPERATION_RESOURCES (
     ID                         INTEGER                 NOT NULL PRIMARY KEY,
     OPERATION                  INTEGER                 NOT NULL REFERENCES MES_OPERATION (ID) ON DELETE CASCADE,
     RESOURCE                   TEXT                    ,
     RESOURCE_TYPE              INTEGER                 ,
     QUANTITY                   INTEGER                 ,
     DESCRIPTION                TEXT
);

CREATE TABLE MES_MANUFACTURER_DATA (
    OBJECT                      INTEGER                 NOT NULL PRIMARY KEY REFERENCES CASSINI_OBJECT (OBJECT_ID) ON DELETE CASCADE,
    MFR_NAME                    TEXT                    ,
    MFR_DESCRIPTION             TEXT                    ,
    MFR_MODEL_NUMBER            TEXT                    ,
    MFR_PART_NUMBER             TEXT                    ,
    MFR_SERIAL_NUMBER           TEXT                    ,
    MFR_LOT_NUMBER              TEXT                    ,
    MFR_DATE                    DATE
);

CREATE TABLE MES_MBOM_TYPE (
    ID                          INTEGER                 NOT NULL PRIMARY KEY,
    LIFECYCLE                   INTEGER                 NOT NULL REFERENCES PLM_LIFECYCLE(ID) ON DELETE SET NULL,
    REVISION_SEQUENCE           INTEGER                 REFERENCES LOV (LOV_ID) ON DELETE CASCADE,
    FOREIGN KEY (ID)            REFERENCES              MES_OBJECT_TYPE (ID) ON DELETE CASCADE
);

CREATE TABLE MES_MBOM (
    ID                          INTEGER                 NOT NULL PRIMARY KEY,
    TYPE                        INTEGER                 REFERENCES MES_MBOM_TYPE (ID) ON DELETE CASCADE,
    ITEM                        INTEGER                 NOT NULL REFERENCES PLM_ITEM(ITEM_ID) ON DELETE CASCADE,
    FOREIGN KEY (ID)            REFERENCES              MES_OBJECT (ID) ON DELETE CASCADE
);

CREATE TABLE MES_MBOMREVISION (
    ID                          INTEGER                 NOT NULL PRIMARY KEY,
    MASTER                      INTEGER                 NOT NULL REFERENCES MES_MBOM(ID) ON DELETE CASCADE,
    ITEM_REVISION               INTEGER                 NOT NULL REFERENCES PLM_ITEMREVISION(ITEM_ID) ON DELETE CASCADE,
    REVISION                    TEXT                    NOT NULL,
    LIFECYCLE_PHASE             INTEGER                 NOT NULL REFERENCES PLM_LIFECYCLEPHASE (ID),
    RELEASED_DATE               TIMESTAMP               ,
    WORKFLOW                    INTEGER                 REFERENCES PLM_WORKFLOW (ID) ON DELETE SET NULL,
    RELEASED                    BOOLEAN                 NOT NULL DEFAULT FALSE,
    REJECTED                    BOOLEAN                 NOT NULL DEFAULT FALSE,
    FOREIGN KEY (ID)            REFERENCES              CASSINI_OBJECT (OBJECT_ID) ON DELETE CASCADE
);

CREATE TABLE MES_MBOMREVISIONSTATUSHISTORY (
    ID                          INTEGER             NOT NULL PRIMARY KEY,
    MBOMREVISION                INTEGER             NOT NULL REFERENCES MES_MBOMREVISION (ID) ON DELETE CASCADE,
    OLD_STATUS                  INTEGER             REFERENCES PLM_LIFECYCLEPHASE (ID),
    NEW_STATUS                  INTEGER             REFERENCES PLM_LIFECYCLEPHASE (ID),
    TIMESTAMP                   TIMESTAMP           NOT NULL,
    UPDATED_BY                  INTEGER             NOT NULL REFERENCES PERSON (PERSON_ID) ON DELETE CASCADE
);

ALTER TABLE MES_MBOM ADD COLUMN LATEST_REVISION INTEGER REFERENCES MES_MBOMREVISION(ID) ON DELETE CASCADE;
ALTER TABLE MES_MBOM ADD COLUMN LATEST_RELEASED_REVISION INTEGER REFERENCES MES_MBOMREVISION(ID) ON DELETE CASCADE;

CREATE TABLE MES_BOMITEM (
    ID                          INTEGER                 NOT NULL PRIMARY KEY,
    MBOM_REVISION               INTEGER                 NOT NULL REFERENCES MES_MBOMREVISION(ID) ON DELETE CASCADE,
    BOMITEM                     INTEGER                 REFERENCES PLM_BOM(BOMITEM_ID) ON DELETE CASCADE,
    PHANTOM                     INTEGER                 REFERENCES MES_PHANTOM(ID) ON DELETE CASCADE,
    PARENT                      INTEGER                 REFERENCES MES_BOMITEM(ID) ON DELETE CASCADE,
    TYPE                        MES_BOM_ITEM_TYPE       NOT NULL DEFAULT 'NORMAL',
    QUANTITY                    INTEGER                 NOT NULL DEFAULT 1,
    MANUFACTURER_PART           INTEGER                 REFERENCES PLM_MANUFACTURERPART (MFRPART_ID) ON DELETE SET NULL,
    FOREIGN KEY (ID)            REFERENCES              CASSINI_OBJECT (OBJECT_ID) ON DELETE CASCADE
);

-- CREATE TABLE MES_BOMITEM_MFRPART (
--     ID                          INTEGER             NOT NULL PRIMARY KEY,
--     BOMITEM                     INTEGER             NOT NULL REFERENCES MES_BOMITEM (ID) ON DELETE CASCADE,
--     MANUFACTURER_PART           INTEGER             NOT NULL REFERENCES PLM_MANUFACTURERPART (MFRPART_ID) ON DELETE CASCADE,
--     UNIQUE (BOMITEM, MANUFACTURER_PART)
-- );

CREATE TABLE MES_MBOMFILE (
    ID                          INTEGER                 NOT NULL PRIMARY KEY,
    MBOM_REVISION               INTEGER                 NOT NULL REFERENCES MES_MBOMREVISION(ID) ON DELETE CASCADE,
    FOREIGN KEY (ID)            REFERENCES              PLM_FILE (FILE_ID) ON DELETE CASCADE
);

CREATE TABLE MES_BOP_TYPE (
    ID                          INTEGER                 NOT NULL PRIMARY KEY,
    LIFECYCLE                   INTEGER                 NOT NULL REFERENCES PLM_LIFECYCLE(ID) ON DELETE SET NULL,
    REVISION_SEQUENCE           INTEGER                 REFERENCES LOV (LOV_ID) ON DELETE CASCADE,
    FOREIGN KEY (ID)            REFERENCES              MES_OBJECT_TYPE (ID) ON DELETE CASCADE
);

CREATE TABLE MES_BOP (
    ID                          INTEGER                 NOT NULL PRIMARY KEY,
    TYPE                        INTEGER                 REFERENCES MES_BOP_TYPE (ID) ON DELETE CASCADE,
    FOREIGN KEY (ID)            REFERENCES              MES_OBJECT (ID) ON DELETE CASCADE
);

CREATE TABLE MES_BOPREVISION (
    ID                          INTEGER                 NOT NULL PRIMARY KEY,
    MASTER                      INTEGER                 NOT NULL REFERENCES MES_BOP(ID) ON DELETE CASCADE,
    MBOM_REVISION               INTEGER                 NOT NULL REFERENCES MES_MBOMREVISION(ID) ON DELETE CASCADE,
    REVISION                    TEXT                    NOT NULL,
    LIFECYCLE_PHASE             INTEGER                 NOT NULL REFERENCES PLM_LIFECYCLEPHASE (ID),
    RELEASED_DATE               TIMESTAMP               ,
    WORKFLOW                    INTEGER                 REFERENCES PLM_WORKFLOW (ID) ON DELETE SET NULL,
    RELEASED                    BOOLEAN                 NOT NULL DEFAULT FALSE,
    REJECTED                    BOOLEAN                 NOT NULL DEFAULT FALSE,
    STATUS_TYPE                 WORKFLOW_STATUS_TYPE    NOT NULL,
    STATUS                      TEXT                    NOT NULL DEFAULT 'NONE',
    OLD_REVISION                TEXT                    ,
    FOREIGN KEY (ID)            REFERENCES              CASSINI_OBJECT (OBJECT_ID) ON DELETE CASCADE
);

ALTER TABLE MES_BOP ADD COLUMN LATEST_REVISION INTEGER REFERENCES MES_BOPREVISION(ID) ON DELETE CASCADE;
ALTER TABLE MES_BOP ADD COLUMN LATEST_RELEASED_REVISION INTEGER REFERENCES MES_BOPREVISION(ID) ON DELETE CASCADE;

CREATE TABLE MES_BOP_FILE (
    ID                          INTEGER                 NOT NULL PRIMARY KEY,
    BOP                         INTEGER                 NOT NULL REFERENCES MES_BOPREVISION (ID) ON DELETE CASCADE,
    FOREIGN KEY (ID)            REFERENCES              PLM_FILE (FILE_ID) ON DELETE CASCADE
);

CREATE TABLE MES_BOPREVISIONSTATUSHISTORY (
    ID                          INTEGER             NOT NULL PRIMARY KEY,
    BOPREVISION                 INTEGER             NOT NULL REFERENCES MES_BOPREVISION (ID) ON DELETE CASCADE,
    OLD_STATUS                  INTEGER             REFERENCES PLM_LIFECYCLEPHASE (ID),
    NEW_STATUS                  INTEGER             REFERENCES PLM_LIFECYCLEPHASE (ID),
    TIMESTAMP                   TIMESTAMP           NOT NULL,
    UPDATED_BY                  INTEGER             NOT NULL REFERENCES PERSON (PERSON_ID) ON DELETE CASCADE
);

CREATE TABLE MES_BOP_ROUTE_OPERATION (
    ID                          INTEGER                 NOT NULL PRIMARY KEY,
    SEQUENCE_NUMBER             TEXT                    NOT NULL,
    BOP                         INTEGER                 NOT NULL REFERENCES MES_BOPREVISION (ID) ON DELETE CASCADE,
    OPERATION                   INTEGER                 REFERENCES MES_OPERATION (ID) ON DELETE CASCADE,
    PHANTOM                     INTEGER                 REFERENCES MES_PHANTOM(ID) ON DELETE CASCADE,
    PARENT                      INTEGER                 REFERENCES MES_BOP_ROUTE_OPERATION(ID) ON DELETE CASCADE,
    TYPE                        MES_BOP_PLAN_TYPE       NOT NULL DEFAULT 'OPERATION',
    SETUP_TIME                  INTEGER                 ,
    CYCLE_TIME                  INTEGER                 ,
    FOREIGN KEY (ID)            REFERENCES              CASSINI_OBJECT (OBJECT_ID) ON DELETE CASCADE
);

CREATE TABLE MES_BOP_OPERATION_FILE(
    ID                          INTEGER                 NOT NULL PRIMARY KEY,
    BOP_OPERATION               INTEGER                 NOT NULL REFERENCES MES_BOP_ROUTE_OPERATION(ID) ON DELETE CASCADE,
    FOREIGN KEY (ID)            REFERENCES              PLM_FILE (FILE_ID) ON DELETE CASCADE
);

CREATE TABLE MES_BOP_OPERATION_INSTRUCTIONS(
    ID                          INTEGER                 NOT NULL PRIMARY KEY,
    BOP_OPERATION               INTEGER                 NOT NULL REFERENCES MES_BOP_ROUTE_OPERATION(ID) ON DELETE CASCADE,
    INSTRUCTIONS                TEXT
);

CREATE TABLE MES_BOP_OPERATION_RESOURCE(
    ID                          INTEGER                 NOT NULL PRIMARY KEY,
    BOP_OPERATION               INTEGER                 NOT NULL REFERENCES MES_BOP_ROUTE_OPERATION(ID) ON DELETE CASCADE,
    OPERATION                   INTEGER                 NOT NULL REFERENCES MES_OPERATION(ID) ON DELETE CASCADE,
    TYPE                        TEXT                    NOT NULL,
    RESOURCE_TYPE               INTEGER                 ,
    RESOURCE                    INTEGER                 ,
    NOTES                       TEXT
);

CREATE TABLE MES_BOP_OPERATION_PART(
    ID                          INTEGER                 NOT NULL PRIMARY KEY,
    BOP_OPERATION               INTEGER                 NOT NULL REFERENCES MES_BOP_ROUTE_OPERATION(ID) ON DELETE CASCADE,
    MBOM_ITEM                   INTEGER                 NOT NULL REFERENCES MES_BOMITEM(ID) ON DELETE CASCADE,
    QUANTITY                    INTEGER                 NOT NULL DEFAULT 1,
    TYPE                        OPERATION_PART_TYPE     NOT NULL DEFAULT 'CONSUMED',
    NOTES                       TEXT
);

CREATE TABLE MES_PRODUCTION_ORDER (
    ID                          INTEGER                 NOT NULL PRIMARY KEY,
    TYPE                        INTEGER                 REFERENCES MES_PRODUCTION_ORDER_TYPE (ID) ON DELETE CASCADE,
    PLANT                       INTEGER                 REFERENCES MES_PLANT(ID) ON DELETE CASCADE,
    WORKFLOW                    INTEGER                 REFERENCES PLM_WORKFLOW (ID) ON DELETE SET NULL,
    STATUS_TYPE                 WORKFLOW_STATUS_TYPE    NOT NULL,
    PLANNED_STARTDATE           TIMESTAMP               ,
    PLANNED_FINISHDATE          TIMESTAMP               ,
    ACTUAL_STARTDATE            TIMESTAMP               ,
    ACTUAL_FINISHDATE           TIMESTAMP               ,
    STATUS                      TEXT                    NOT NULL DEFAULT 'NONE',
    ASSIGNED_TO                 INTEGER                 REFERENCES PERSON (PERSON_ID) ON DELETE CASCADE,
    SHIFT                       INTEGER                 REFERENCES MES_SHIFT (ID) ON DELETE CASCADE,
    LIFECYCLE_PHASE             INTEGER                 NOT NULL REFERENCES PLM_LIFECYCLEPHASE (ID),
    APPROVED                    BOOLEAN                 NOT NULL DEFAULT FALSE,
    REJECTED                    BOOLEAN                 NOT NULL DEFAULT FALSE,
    APPROVED_DATE               TIMESTAMP               ,
    FOREIGN KEY (ID)            REFERENCES              MES_OBJECT (ID) ON DELETE CASCADE
);

CREATE TABLE MES_PRODUCTION_ORDER_ITEM (
    ID                          INTEGER                 NOT NULL PRIMARY KEY,
    PRODUCTION_ORDER            INTEGER                 NOT NULL REFERENCES MES_PRODUCTION_ORDER(ID) ON DELETE CASCADE,
    MBOM_REVISION               INTEGER                 NOT NULL REFERENCES MES_MBOMREVISION(ID) ON DELETE CASCADE,
    BOP_REVISION                INTEGER                 NOT NULL REFERENCES MES_BOPREVISION(ID) ON DELETE CASCADE,
    QUANTITY_PRODUCED           INTEGER                 NOT NULL DEFAULT 1,
    FOREIGN KEY (ID)            REFERENCES              CASSINI_OBJECT (OBJECT_ID) ON DELETE CASCADE
);

CREATE TABLE MES_MBOMINSTANCE (
    ID                          INTEGER                 NOT NULL PRIMARY KEY,
    PRODUCTION_ORDER_ITEM       INTEGER                 NOT NULL REFERENCES MES_PRODUCTION_ORDER_ITEM(ID) ON DELETE CASCADE,
    NUMBER                      TEXT                    NOT NULL,
    SERIAL_NUMBER               TEXT                    ,
    NAME                        TEXT                    ,
    DESCRIPTION                 TEXT                    ,
    MFG_DATE                    DATE                    ,
    BATCH_NUMBER                TEXT                    ,
    MBOM_REVISION               INTEGER                 NOT NULL REFERENCES MES_MBOMREVISION(ID) ON DELETE CASCADE,
    STATUS                      TEXT                    ,
    RELEASED                    BOOLEAN                 NOT NULL DEFAULT FALSE,
    REJECTED                    BOOLEAN                 NOT NULL DEFAULT FALSE,
    REJECTED_DATE               DATE                    ,
    REJECTED_REASON             TEXT                    ,
    START_DATE                  TIMESTAMP               ,
    FINISH_DATE                 TIMESTAMP               ,
    FOREIGN KEY (ID)            REFERENCES              CASSINI_OBJECT (OBJECT_ID) ON DELETE CASCADE
);

CREATE TABLE MES_MBOMINSTANCE_ITEM (
    ID                          INTEGER                 NOT NULL PRIMARY KEY,
    MBOMINSTANCE                INTEGER                 NOT NULL REFERENCES MES_MBOMINSTANCE(ID) ON DELETE CASCADE,
    MBOMITEM                    INTEGER                 NOT NULL REFERENCES MES_BOMITEM(ID) ON DELETE CASCADE,
    PARENT                      INTEGER                 REFERENCES MES_MBOMINSTANCE_ITEM(ID) ON DELETE CASCADE,
    FOREIGN KEY (ID)            REFERENCES              CASSINI_OBJECT (OBJECT_ID) ON DELETE CASCADE
);

CREATE TABLE MES_MBOM_ITEM_INSTANCE (
    ID                          INTEGER                 NOT NULL PRIMARY KEY,
    MBOMINSTANCE_ITEM           INTEGER                 NOT NULL REFERENCES MES_MBOMINSTANCE_ITEM(ID) ON DELETE CASCADE,
    FOREIGN KEY (ID)            REFERENCES              CASSINI_OBJECT (OBJECT_ID) ON DELETE CASCADE
);

CREATE TABLE MES_MBOMINSTANCE_FILE (
    ID                          INTEGER                 NOT NULL PRIMARY KEY,
    MBOM_INSTANCE               INTEGER                 NOT NULL REFERENCES MES_MBOMINSTANCE(ID) ON DELETE CASCADE,
    FOREIGN KEY (ID)            REFERENCES              PLM_FILE (FILE_ID) ON DELETE CASCADE
);

CREATE TABLE MES_BOPINSTANCE(
    ID                          INTEGER                 NOT NULL PRIMARY KEY,
    MBOMINSTANCE                INTEGER                 NOT NULL REFERENCES MES_MBOMINSTANCE(ID) ON DELETE CASCADE,
    BOP_REVISION                INTEGER                 NOT NULL REFERENCES MES_BOPREVISION(ID) ON DELETE CASCADE,
    FOREIGN KEY (ID)            REFERENCES              CASSINI_OBJECT (OBJECT_ID) ON DELETE CASCADE
);

CREATE TABLE MES_BOPINSTANCE_ROUTE_OPERATION(
    ID                          INTEGER                 NOT NULL PRIMARY KEY,
    SEQUENCE_NUMBER             TEXT                    NOT NULL,
    BOPINSTANCE                 INTEGER                 NOT NULL REFERENCES MES_BOPINSTANCE (ID) ON DELETE CASCADE,
    OPERATION                   INTEGER                 REFERENCES MES_OPERATION (ID) ON DELETE CASCADE,
    PHANTOM                     INTEGER                 REFERENCES MES_PHANTOM(ID) ON DELETE CASCADE,
    PARENT                      INTEGER                 REFERENCES MES_BOPINSTANCE_ROUTE_OPERATION(ID) ON DELETE CASCADE,
    TYPE                        MES_BOP_PLAN_TYPE       NOT NULL DEFAULT 'OPERATION',
    SETUP_TIME                  INTEGER                 ,
    CYCLE_TIME                  INTEGER                 ,
    STATUS                      TEXT                    ,
    START_DATE                  TIMESTAMP               ,
    FINISH_DATE                 TIMESTAMP               ,
    FOREIGN KEY (ID)            REFERENCES              CASSINI_OBJECT (OBJECT_ID) ON DELETE CASCADE
);

CREATE TABLE MES_BOPINSTANCE_OPERATION_FILE(
    ID                          INTEGER                 NOT NULL PRIMARY KEY,
    BOP_OPERATION               INTEGER                 NOT NULL REFERENCES MES_BOPINSTANCE_ROUTE_OPERATION(ID) ON DELETE CASCADE,
    FOREIGN KEY (ID)            REFERENCES              PLM_FILE (FILE_ID) ON DELETE CASCADE
);

CREATE TABLE MES_BOPINSTANCE_OPERATION_INSTRUCTIONS(
    ID                          INTEGER                 NOT NULL PRIMARY KEY,
    BOP_OPERATION               INTEGER                 NOT NULL REFERENCES MES_BOPINSTANCE_ROUTE_OPERATION(ID) ON DELETE CASCADE,
    INSTRUCTIONS                TEXT
);

CREATE TABLE MES_BOPINSTANCE_OPERATION_RESOURCE(
    ID                          INTEGER                 NOT NULL PRIMARY KEY,
    BOP_OPERATION               INTEGER                 NOT NULL REFERENCES MES_BOPINSTANCE_ROUTE_OPERATION(ID) ON DELETE CASCADE,
    OPERATION                   INTEGER                 NOT NULL REFERENCES MES_OPERATION(ID) ON DELETE CASCADE,
    TYPE                        TEXT                    NOT NULL,
    RESOURCE_TYPE               INTEGER                 ,
    RESOURCE                    INTEGER                 ,
    NOTES                       TEXT
);
CREATE TABLE MES_BOPINSTANCE_OPERATION_PART(
    ID                          INTEGER                 NOT NULL PRIMARY KEY,
    BOP_OPERATION               INTEGER                 NOT NULL REFERENCES MES_BOPINSTANCE_ROUTE_OPERATION(ID) ON DELETE CASCADE,
    MBOMINSTANCE_ITEM           INTEGER                 NOT NULL REFERENCES MES_MBOMINSTANCE_ITEM(ID) ON DELETE CASCADE,
    QUANTITY                    INTEGER                 NOT NULL DEFAULT 1,
    TYPE                        OPERATION_PART_TYPE     NOT NULL DEFAULT 'CONSUMED',
    NOTES                       TEXT
);

CREATE TABLE MES_SERVICE_ORDER (
     ID                         INTEGER                 NOT NULL PRIMARY KEY,
     TYPE                       INTEGER                 REFERENCES MES_SERVICE_ORDER_TYPE (ID) ON DELETE CASCADE,
     FOREIGN KEY (ID)           REFERENCES              MES_OBJECT (ID) ON DELETE CASCADE
);

CREATE TABLE MES_WORKCENTER_OPERATION(
     ID                         INTEGER                 NOT NULL PRIMARY KEY,
     WORK_CENTER                INTEGER                 NOT NULL REFERENCES MES_WORKCENTER (ID) ON DELETE CASCADE,
     OPERATION                  INTEGER                 NOT NULL REFERENCES MES_OPERATION (ID) ON DELETE CASCADE,
     NOTES                      TEXT                    ,
     UNIQUE(WORK_CENTER,OPERATION)
);

ALTER TABLE PLM_MCO_PRODUCT_AFFECTEDITEM ADD COLUMN ITEM                    INTEGER                 NOT NULL REFERENCES MES_MBOMREVISION (ID) ON DELETE CASCADE;
ALTER TABLE PLM_MCO_PRODUCT_AFFECTEDITEM ADD COLUMN TO_ITEM                 INTEGER                 REFERENCES MES_MBOMREVISION (ID) ON DELETE CASCADE;

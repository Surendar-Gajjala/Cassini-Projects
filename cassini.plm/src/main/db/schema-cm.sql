CREATE TYPE MCO_TYPE AS ENUM (
    'ITEMMCO',
    'OEMPARTMCO'
);

CREATE TYPE REQUESTER_TYPE AS ENUM (
    'INTERNAL',
    'CUSTOMER'
);
CREATE TABLE PLM_CHANGETYPE (
    TYPE_ID                     INTEGER             NOT NULL PRIMARY KEY,
    NAME                        TEXT                NOT NULL,
    DESCRIPTION                 TEXT                ,
    CHANGE_REASON_TYPES         INTEGER             REFERENCES LOV(LOV_ID) ON DELETE CASCADE,
    PARENT_TYPE                 INTEGER             REFERENCES PLM_CHANGETYPE (TYPE_ID) ON DELETE CASCADE,
    AUTONUMBER_SOURCE           INTEGER             REFERENCES AUTONUMBER (AUTONUMBER_ID) ON DELETE CASCADE,
    FOREIGN KEY (TYPE_ID)       REFERENCES          CASSINI_OBJECT (OBJECT_ID) ON DELETE CASCADE
);

CREATE TABLE PLM_CHANGETYPEATTRIBUTE (
    ATTRIBUTE_ID                  INTEGER           NOT NULL PRIMARY KEY,
    CHANGE_TYPE                   INTEGER           NOT NULL REFERENCES PLM_CHANGETYPE (TYPE_ID) ON DELETE CASCADE,
    SEQ                           INTEGER           ,
    FOREIGN KEY (ATTRIBUTE_ID)    REFERENCES        OBJECTTYPEATTRIBUTE (ATTRIBUTE_ID) ON DELETE CASCADE
);

CREATE TABLE PLM_CHANGE (
    CHANGE_ID               INTEGER                 NOT NULL PRIMARY KEY,
    CHANGE_TYPE             CHANGE_TYPE             NOT NULL,
    CHANGE_CLASS            INTEGER                 REFERENCES PLM_CHANGETYPE(TYPE_ID) on DELETE CASCADE,
    CHANGE_REASON_TYPE      TEXT                    ,
    /*WORKFLOW_STATUS         INTEGER                 REFERENCES PLM_WORKFLOWSTATUS(ID) ON DELETE SET NULL,*/
    REVISION_CREATION_TYPE  REVISION_CREATION_TYPE  ,
    REVISIONS_CREATED       BOOLEAN                 DEFAULT FALSE,
    FOREIGN KEY (CHANGE_ID) REFERENCES              CASSINI_OBJECT (OBJECT_ID) ON DELETE CASCADE
);

CREATE TABLE PLM_CHANGEATTRIBUTE (
    CHANGE                        INTEGER             NOT NULL REFERENCES PLM_CHANGE (CHANGE_ID) ON DELETE CASCADE,
    ATTRIBUTE                   INTEGER             NOT NULL REFERENCES PLM_CHANGETYPEATTRIBUTE (ATTRIBUTE_ID) ON DELETE CASCADE,
    FOREIGN KEY (CHANGE, ATTRIBUTE)          REFERENCES          OBJECTATTRIBUTE (OBJECT_ID, ATTRIBUTEDEF)  ON DELETE CASCADE,
    UNIQUE (CHANGE, ATTRIBUTE)
);


CREATE TABLE PLM_CHANGEFILE (
    FILE_ID                     INTEGER             NOT NULL PRIMARY KEY,
    CHANGE                      INTEGER             NOT NULL REFERENCES PLM_CHANGE (CHANGE_ID) ON DELETE CASCADE,
    FOREIGN KEY (FILE_ID)       REFERENCES          PLM_FILE (FILE_ID) ON DELETE CASCADE
);


CREATE TABLE PLM_AFFECTEDITEM (
    ROWID                   INTEGER                 NOT NULL PRIMARY KEY,
    CHANGE                  INTEGER                 NOT NULL REFERENCES PLM_CHANGE (CHANGE_ID) ON DELETE CASCADE,
    ITEM                    INTEGER                 NOT NULL REFERENCES PLM_ITEMREVISION (ITEM_ID) ON DELETE CASCADE,
    TO_ITEM                 INTEGER                 REFERENCES PLM_ITEMREVISION (ITEM_ID) ON DELETE CASCADE,
    FROM_REVISION           TEXT                    NOT NULL,
    TO_REVISION             TEXT                    NOT NULL,
    EFFECTIVE_DATE          TIMESTAMP               ,
    CHANGE_REQUESTS         INTEGER[]               ,
    NOTES                   TEXT
);

CREATE TABLE PLM_CHANGE_RELATED_ITEM (
  ROWID                 INTEGER                   NOT NULL PRIMARY KEY,
  CHANGE                INTEGER                   NOT NULL REFERENCES PLM_CHANGE (CHANGE_ID) ON DELETE CASCADE,
  ITEM                  INTEGER                   NOT NULL REFERENCES PLM_ITEMREVISION (ITEM_ID) ON DELETE CASCADE
);

CREATE TABLE PLM_ECO (
    ECO_ID                      INTEGER             NOT NULL PRIMARY KEY,
    ECO_TYPE                    INTEGER             REFERENCES PLM_CHANGETYPE (TYPE_ID),
    ECO_NUMBER                  TEXT                NOT NULL,
    TITLE                       TEXT                NOT NULL,
    DESCRIPTION                 TEXT                ,
    REASON_FOR_CHANGE           TEXT                ,
    CHANGE_ANALYST              INTEGER             REFERENCES PERSON (PERSON_ID) ON DELETE CASCADE,
    STATUS                      TEXT                NOT NULL,
    IS_RELEASED                 BOOLEAN             NOT NULL DEFAULT FALSE,
    RELEASED_DATE               TIMESTAMP           ,
    IS_CANCELLED                BOOLEAN             NOT NULL DEFAULT FALSE,
    CANCELLED_DATE              TIMESTAMP           ,
    FOREIGN KEY (ECO_ID)        REFERENCES          PLM_CHANGE (CHANGE_ID) ON DELETE CASCADE
);

CREATE TABLE PLM_ECO_TYPE (
    ID                          INTEGER             NOT NULL PRIMARY KEY,
    FOREIGN KEY (ID)            REFERENCES          PLM_CHANGETYPE (TYPE_ID) ON DELETE CASCADE
);

CREATE TYPE CR_STATUS AS ENUM (
    'NONE',
    'APPROVE',
    'REJECT',
    'HOLD',
    'CANCEL'
);

CREATE TABLE PLM_ECR_TYPE (
    ID                          INTEGER             NOT NULL PRIMARY KEY,
    FOREIGN KEY (ID)            REFERENCES          PLM_CHANGETYPE (TYPE_ID) ON DELETE CASCADE
);

CREATE TYPE CHANGE_REQUEST_URGENCY AS ENUM (
    'HIGH',
    'MEDIUM',
    'LOW',
    'CRITICAL'
);

CREATE TABLE PLM_CHANGE_REQUEST (
    ID                          INTEGER             NOT NULL PRIMARY KEY,
    CR_TYPE                     INTEGER             REFERENCES PLM_CHANGETYPE (TYPE_ID),
    CR_NUMBER                   TEXT                NOT NULL,
    TITLE                       TEXT                NOT NULL,
    DESCRIPTION_OF_CHANGE       TEXT                ,
    REASON_FOR_CHANGE           TEXT                ,
    PROPOSED_CHANGES            TEXT                ,
    URGENCY                     TEXT                NOT NULL,
    ORIGINATOR                  INTEGER             REFERENCES PERSON (PERSON_ID) ON DELETE CASCADE,
    REQUESTED_BY                INTEGER             ,
    REQUESTED_DATE              DATE                ,
    REQUESTER_TYPE              REQUESTER_TYPE      NOT NULL DEFAULT 'INTERNAL',
    OTHER_REQUESTED             TEXT                ,
    STATUS                      TEXT                ,
    CHANGE_ANALYST              INTEGER             NOT NULL REFERENCES PERSON (PERSON_ID) ON DELETE CASCADE,
    IS_APPROVED                 BOOLEAN             NOT NULL DEFAULT FALSE,
    APPROVED_DATE               TIMESTAMP           ,
    REJECTION_REASON            TEXT                ,
    NOTES                       TEXT                ,
    IS_IMPLEMENTED              BOOLEAN             DEFAULT FALSE,
    IMPLEMENTED_DATE            TIMESTAMP           ,
    FOREIGN KEY (ID)            REFERENCES          PLM_CHANGE (CHANGE_ID) ON DELETE CASCADE
);

CREATE TABLE PLM_ECR (
    ID                          INTEGER             NOT NULL PRIMARY KEY,
    IMPACT_ANALYSIS             TEXT                ,
    FOREIGN KEY (ID)            REFERENCES          PLM_CHANGE_REQUEST (ID) ON DELETE CASCADE
);

CREATE TABLE PLM_ECO_ECR (
    ID                          INTEGER             NOT NULL PRIMARY KEY,
    ECO                         INTEGER             NOT NULL REFERENCES PLM_ECO (ECO_ID) ON DELETE CASCADE,
    ECR                         INTEGER             NOT NULL REFERENCES PLM_ECR (ID) ON DELETE CASCADE
);

CREATE TABLE PLM_CHANGE_REQUEST_AFFECTEDITEM (
    ID                      INTEGER                 NOT NULL PRIMARY KEY,
    ITEM                    INTEGER                 NOT NULL REFERENCES PLM_ITEMREVISION (ITEM_ID) ON DELETE CASCADE,
    NOTES                   TEXT                    ,
    IS_IMPLEMENTED          BOOLEAN                 DEFAULT FALSE,
    IMPLEMENTED_DATE        TIMESTAMP               ,
    FOREIGN KEY (ID)        REFERENCES              CASSINI_OBJECT (OBJECT_ID) ON DELETE CASCADE
);


CREATE TABLE PLM_ECR_AFFECTEDITEM (
    ID                      INTEGER                 NOT NULL PRIMARY KEY,
    ECR                     INTEGER                 NOT NULL REFERENCES PLM_ECR (ID) ON DELETE CASCADE,
    FOREIGN KEY (ID)       REFERENCES               PLM_CHANGE_REQUEST_AFFECTEDITEM (ID) ON DELETE CASCADE
);

CREATE TABLE PLM_DCR_TYPE (
    ID                          INTEGER             NOT NULL PRIMARY KEY,
    FOREIGN KEY (ID)            REFERENCES          PLM_CHANGETYPE (TYPE_ID) ON DELETE CASCADE
);

CREATE TABLE PLM_DCR (
    ID                          INTEGER             NOT NULL PRIMARY KEY,
    FOREIGN KEY (ID)            REFERENCES          PLM_CHANGE_REQUEST (ID) ON DELETE CASCADE
);


CREATE TABLE PLM_DCR_AFFECTEDITEM (
    ID                      INTEGER                 NOT NULL PRIMARY KEY,
    DCR                     INTEGER                 NOT NULL REFERENCES PLM_DCR (ID) ON DELETE CASCADE,
    FOREIGN KEY (ID)       REFERENCES               PLM_CHANGE_REQUEST_AFFECTEDITEM (ID) ON DELETE CASCADE
);

CREATE TABLE PLM_DCO_TYPE (
    ID                          INTEGER             NOT NULL PRIMARY KEY,
    FOREIGN KEY (ID)            REFERENCES          PLM_CHANGETYPE (TYPE_ID) ON DELETE CASCADE
);

CREATE TABLE PLM_DCO (
    ID                          INTEGER             NOT NULL PRIMARY KEY,
    DCO_TYPE                    INTEGER             REFERENCES PLM_CHANGETYPE (TYPE_ID),
    DCO_NUMBER                  TEXT                NOT NULL,
    TITLE                       TEXT                NOT NULL,
    DESCRIPTION                 TEXT                ,
    REASON_FOR_CHANGE           TEXT                ,
    CHANGE_ANALYST              INTEGER             NOT NULL REFERENCES PERSON (PERSON_ID) ON DELETE CASCADE,
    STATUS                      TEXT                NOT NULL,
    IS_RELEASED                 BOOLEAN             NOT NULL DEFAULT FALSE,
    RELEASED_DATE               TIMESTAMP           ,
    FOREIGN KEY (ID)            REFERENCES          PLM_CHANGE (CHANGE_ID) ON DELETE CASCADE
);

CREATE TABLE PLM_DCO_AFFECTEDITEM (
    ID                      INTEGER                 NOT NULL PRIMARY KEY,
    DCO                     INTEGER                 NOT NULL REFERENCES PLM_DCO (ID) ON DELETE CASCADE,
    FOREIGN KEY (ID)       REFERENCES               PLM_AFFECTEDITEM (ROWID) ON DELETE CASCADE
);

CREATE TABLE PLM_DCO_DCR (
    ID                          INTEGER             NOT NULL PRIMARY KEY,
    DCO                         INTEGER             NOT NULL REFERENCES PLM_DCO (ID) ON DELETE CASCADE,
    DCR                         INTEGER             NOT NULL REFERENCES PLM_DCR (ID) ON DELETE CASCADE
);

CREATE TABLE PLM_MCO_TYPE (
    ID                          INTEGER             NOT NULL PRIMARY KEY,
    MCO_TYPE                   MCO_TYPE               ,
    FOREIGN KEY (ID)            REFERENCES          PLM_CHANGETYPE (TYPE_ID) ON DELETE CASCADE
);

CREATE TABLE PLM_MCO (
    MCO_ID                      INTEGER             NOT NULL PRIMARY KEY,
    MCO_TYPE                    INTEGER             REFERENCES PLM_CHANGETYPE (TYPE_ID),
    MCO_NUMBER                  TEXT                NOT NULL,
    TITLE                       TEXT                NOT NULL,
    DESCRIPTION                 TEXT                ,
    REASON_FOR_CHANGE           TEXT                ,
    CHANGE_ANALYST              INTEGER             REFERENCES PERSON (PERSON_ID) ON DELETE CASCADE,
    STATUS                      TEXT                NOT NULL,
    IS_RELEASED                 BOOLEAN             NOT NULL DEFAULT FALSE,
    REJECTED                    BOOLEAN             NOT NULL DEFAULT FALSE,
    RELEASED_DATE               TIMESTAMP           ,
    FOREIGN KEY (MCO_ID)        REFERENCES          PLM_CHANGE (CHANGE_ID) ON DELETE CASCADE
);

CREATE TYPE MCO_CHANGE_TYPE AS ENUM (
    'REMOVED',
    'REPLACED'
);

CREATE TABLE PLM_MCO_AFFECTEDITEM (
    ID                      INTEGER                 NOT NULL PRIMARY KEY,
    MCO                     INTEGER                 NOT NULL REFERENCES PLM_MCO (MCO_ID) ON DELETE CASCADE,
    CHANGE_TYPE             MCO_CHANGE_TYPE         NOT NULL DEFAULT 'REPLACED',
    /* AFFECTED MATERIAL IS ADDED IN MFR SCHEMA */
    NOTES                   TEXT                    ,
    FOREIGN KEY (ID)       REFERENCES               CASSINI_OBJECT (OBJECT_ID) ON DELETE CASCADE
);

CREATE TABLE PLM_MCO_PRODUCT_AFFECTEDITEM (
    ID                      INTEGER                 NOT NULL PRIMARY KEY,
    MCO                     INTEGER                 NOT NULL REFERENCES PLM_MCO (MCO_ID) ON DELETE CASCADE,
   /*  ITEM                    INTEGER                 NOT NULL REFERENCES MES_MBOMREVISION (ID) ON DELETE CASCADE,
    TO_ITEM                 INTEGER                 REFERENCES MES_MBOMREVISION (ID) ON DELETE CASCADE,*/
    FROM_REVISION           TEXT                    ,
    TO_REVISION             TEXT                    ,
    EFFECTIVE_DATE          TIMESTAMP               ,
    NOTES                   TEXT
);

CREATE TABLE PLM_MCO_RELATEDITEM (
    ID                      INTEGER                 NOT NULL PRIMARY KEY,
    MCO                     INTEGER                 NOT NULL REFERENCES PLM_MCO (MCO_ID) ON DELETE CASCADE,
    /* PART IS ADDED IN MFR SCHEMA */
    NOTES                   TEXT                    ,
    FOREIGN KEY (ID)       REFERENCES               CASSINI_OBJECT (OBJECT_ID) ON DELETE CASCADE
);

CREATE TYPE VARIANCE_TYPE AS ENUM (
    'DEVIATION',
    'WAIVER'
);

CREATE TYPE VARIANCE_EFFECTIVITY_TYPE AS ENUM (
    'DURATION',
    'QUANTITY'
);

CREATE TABLE PLM_DEVIATION_TYPE (
    ID                          INTEGER             NOT NULL PRIMARY KEY,
    FOREIGN KEY (ID)            REFERENCES          PLM_CHANGETYPE (TYPE_ID) ON DELETE CASCADE
);

CREATE TABLE PLM_WAIVER_TYPE (
    ID                          INTEGER             NOT NULL PRIMARY KEY,
    FOREIGN KEY (ID)            REFERENCES          PLM_CHANGETYPE (TYPE_ID) ON DELETE CASCADE
);

CREATE TYPE VARIANCE_FOR AS ENUM (
    'ITEMS',
    'MATERIALS'
);

CREATE TABLE PLM_VARIANCE (
    ID                      INTEGER                 NOT NULL PRIMARY KEY,
    VARIANCE_TYPE           VARIANCE_TYPE           NOT NULL DEFAULT 'DEVIATION',
    VARIANCE_NUMBER         TEXT                    NOT NULL,
    VARIANCE_FOR            VARIANCE_FOR            NOT NULL DEFAULT 'ITEMS',
    TITLE                   TEXT                    NOT NULL,
    DESCRIPTION             TEXT                    NOT NULL,
    REASON_FOR_VARIANCE     TEXT                    NOT NULL,
    CURRENT_REQUIREMENT     TEXT                    NOT NULL,
    REQUIREMENT_DEVIATION   TEXT                    NOT NULL,
    STATUS                  TEXT                    NOT NULL,
    IS_RECURRING            BOOLEAN                 NOT NULL DEFAULT FALSE,
    ORIGINATOR              INTEGER                 NOT NULL REFERENCES PERSON (PERSON_ID) ON DELETE CASCADE,
    EFFECTIVITY_TYPE        VARIANCE_EFFECTIVITY_TYPE   NOT NULL DEFAULT 'QUANTITY',
    EFFECTIVE_FROM          DATE                    ,
    EFFECTIVE_TO            DATE                    ,
    CORRECTIVE_ACTION       TEXT                    ,
    PREVENTIVE_ACTION       TEXT                    ,
    NOTES                   TEXT                    ,
    FOREIGN KEY (ID)        REFERENCES              PLM_CHANGE (CHANGE_ID) ON DELETE CASCADE
);

CREATE TABLE PLM_VARIANCE_AFFECTED_OBJECT (
    ID                      INTEGER                 NOT NULL PRIMARY KEY,
    VARIANCE                INTEGER                 NOT NULL REFERENCES PLM_VARIANCE (ID) ON DELETE CASCADE,
    IS_RECURRING            BOOLEAN                 NOT NULL DEFAULT FALSE,
    QUANTITY                INTEGER                 ,
    SERIALS_OR_LOTS         TEXT                    ,
    NOTES                   TEXT                    ,
    FOREIGN KEY (ID)        REFERENCES               CASSINI_OBJECT (OBJECT_ID) ON DELETE CASCADE
);

CREATE TABLE PLM_VARIANCE_AFFECTED_ITEM (
    ID                      INTEGER                 NOT NULL PRIMARY KEY,
    ITEM                    INTEGER                 NOT NULL REFERENCES PLM_ITEMREVISION (ITEM_ID) ON DELETE CASCADE,
    FOREIGN KEY (ID)        REFERENCES               PLM_VARIANCE_AFFECTED_OBJECT (ID) ON DELETE CASCADE
);

CREATE TABLE PLM_VARIANCE_AFFECTED_MATERIAL (
    ID                      INTEGER                 NOT NULL PRIMARY KEY,
    /* MATERIAL ADDED AFTER MFR SCHEMA */
    FOREIGN KEY (ID)        REFERENCES               PLM_VARIANCE_AFFECTED_OBJECT (ID) ON DELETE CASCADE
);

CREATE TABLE PLM_ITEM_MCO (
  ID   INTEGER NOT NULL PRIMARY KEY,
  ITEM INTEGER REFERENCES PLM_ITEMREVISION (ITEM_ID) ON DELETE CASCADE,
  FOREIGN KEY (ID) REFERENCES PLM_MCO (MCO_ID) ON DELETE CASCADE
);

CREATE TABLE PLM_MANUFACTURER_MCO (
  ID       INTEGER NOT NULL PRIMARY KEY,
  FOREIGN KEY (ID) REFERENCES PLM_MCO (MCO_ID) ON DELETE CASCADE
);

ALTER TABLE PLM_ITEMREVISION ADD COLUMN CHANGE_ORDER INTEGER REFERENCES PLM_CHANGE(CHANGE_ID) ON DELETE SET NULL;
-- <editor-fold desc="Common Tables">
CREATE TABLE FILE (
    ID                          INTEGER             NOT NULL PRIMARY KEY,
    NAME                        TEXT                NOT NULL,
    DESCRIPTION                 TEXT                ,
    SIZE                        BIGINT              NOT NULL,
    VERSION                     INTEGER             NOT NULL DEFAULT 1,
    LATEST                      BOOLEAN             DEFAULT TRUE,
    LOCKED                      BOOLEAN             NOT NULL DEFAULT FALSE,
    LOCKED_BY                   INTEGER             REFERENCES PERSON (PERSON_ID) ON DELETE CASCADE,
    LOCKED_DATE                 TIMESTAMP           ,
    FOREIGN KEY (ID)            REFERENCES          CASSINI_OBJECT (OBJECT_ID) ON DELETE CASCADE
);

-- </editor-fold>

-- <editor-fold desc="Item Tables">
CREATE TABLE ITEMTYPE (
    ID                          INTEGER                 NOT NULL PRIMARY KEY,
    NAME                        TEXT                    NOT NULL,
    DESCRIPTION                 TEXT                    ,
    TYPE_CODE                   TEXT                    ,
    PARENT_TYPE                 INTEGER                 REFERENCES ITEMTYPE (ID) ON DELETE CASCADE,
    ITEMNUMBER_SOURCE           INTEGER                 REFERENCES AUTONUMBER (AUTONUMBER_ID) ON DELETE CASCADE,
    REVISION_SEQUENCE           INTEGER                 REFERENCES LOV (LOV_ID) ON DELETE CASCADE,
    UNITS                       TEXT                    NOT NULL DEFAULT 'Nos',
    HAS_LOTS                    BOOLEAN                 NOT NULL DEFAULT FALSE,
    HAS_SPEC                    BOOLEAN                 NOT NULL DEFAULT FALSE,
    HAS_BOM                     BOOLEAN                 NOT NULL DEFAULT FALSE,
    PARENT_NODE                 BOOLEAN                 DEFAULT FALSE,
    SEQUENCE_NUMBER             TEXT                    DEFAULT 00,
    FOREIGN KEY (ID)           REFERENCES              CASSINI_OBJECT (OBJECT_ID) ON DELETE CASCADE
);

CREATE TABLE ITEMTYPE_SPECS (
    ID                          INTEGER                 NOT NULL PRIMARY KEY,
    ITEMTYPE                    INTEGER                 NOT NULL REFERENCES ITEMTYPE(ID) ON DELETE CASCADE,
    SPEC_NAME                   TEXT                    NOT NULL
);

CREATE TABLE ITEMTYPEATTRIBUTE (
    ID                          INTEGER                 NOT NULL PRIMARY KEY,
    ITEM_TYPE                   INTEGER                 NOT NULL REFERENCES ITEMTYPE (ID) ON DELETE CASCADE,
    REVISION_SPECIFIC           BOOLEAN                 NOT NULL DEFAULT FALSE,
    CHANGE_CONTROLLED           BOOLEAN                 NOT NULL DEFAULT FALSE,
    FOREIGN KEY (ID)            REFERENCES              OBJECTTYPEATTRIBUTE (ATTRIBUTE_ID) ON DELETE CASCADE
);
CREATE TABLE ITEM (
    ID                          INTEGER             NOT NULL PRIMARY KEY,
    ITEM_TYPE                   INTEGER             NOT NULL REFERENCES ITEMTYPE (ID) ON DELETE CASCADE,
    ITEM_NUMBER                 TEXT                NOT NULL,
    ITEM_NAME                   TEXT                NOT NULL DEFAULT '',
    ITEM_CODE                   TEXT                ,
    PART_SPEC                   INTEGER             ,
    DESCRIPTION                 TEXT                ,
    THUMBNAIL                   BYTEA               ,
    LOCKED                      BOOLEAN             DEFAULT FALSE,
    LOCKED_DATE                 TIMESTAMP           ,
    LOCKED_BY                   INTEGER             REFERENCES PERSON(PERSON_ID) ON DELETE CASCADE,
    MATERIAL                    TEXT                ,
    FOREIGN KEY (ID)           REFERENCES          CASSINI_OBJECT (OBJECT_ID) ON DELETE CASCADE,
    UNIQUE(ITEM_TYPE, ITEM_NUMBER),
    UNIQUE(ITEM_CODE,PART_SPEC)
);
CREATE TABLE ITEMATTRIBUTEVALUE (
    ITEM                        INTEGER             NOT NULL REFERENCES ITEM (ID) ON DELETE CASCADE,
    ATTRIBUTE                   INTEGER             NOT NULL REFERENCES ITEMTYPEATTRIBUTE (ID) ON DELETE CASCADE,
    FOREIGN KEY (ITEM, ATTRIBUTE) REFERENCES        OBJECTATTRIBUTE (OBJECT_ID, ATTRIBUTEDEF)  ON DELETE CASCADE
);

CREATE TABLE ITEMREVISION (
    ID                          INTEGER             NOT NULL PRIMARY KEY,
    ITEM_MASTER                 INTEGER             NOT NULL REFERENCES ITEM (ID) ON DELETE CASCADE,
    REVISION                    TEXT                NOT NULL,
    HAS_BOM                     BOOLEAN             NOT NULL DEFAULT FALSE,
    HAS_FILES                   BOOLEAN             NOT NULL DEFAULT FALSE,
    DRAWING_NUMBER              TEXT                ,
    FOREIGN KEY (ID)            REFERENCES          CASSINI_OBJECT (OBJECT_ID) ON DELETE CASCADE
);

ALTER TABLE ITEM ADD COLUMN LATEST_REVISION INTEGER REFERENCES ITEMREVISION(ID) ON DELETE CASCADE;

CREATE TABLE ITEMREVISIONATTRIBUTEVALUE (
    ITEM                        INTEGER             NOT NULL REFERENCES ITEMREVISION (ID) ON DELETE CASCADE,
    ATTRIBUTE                   INTEGER             NOT NULL REFERENCES ITEMTYPEATTRIBUTE (ID) ON DELETE CASCADE,
    FOREIGN KEY (ITEM, ATTRIBUTE)          REFERENCES          OBJECTATTRIBUTE (OBJECT_ID, ATTRIBUTEDEF)  ON DELETE CASCADE,
    UNIQUE (ITEM, ATTRIBUTE)
);

CREATE TABLE ITEMFILE (
    ID                          INTEGER             NOT NULL PRIMARY KEY,
    ITEM                        INTEGER             NOT NULL REFERENCES ITEMREVISION (ID) ON DELETE CASCADE,
    FOREIGN KEY (ID)            REFERENCES          FILE (ID) ON DELETE CASCADE
);

CREATE TABLE ITEMINSTANCE (
    ID                          INTEGER             NOT NULL PRIMARY KEY,
    ITEM                        INTEGER             NOT NULL REFERENCES ITEMREVISION (ID) ON DELETE CASCADE,
    INSTANCE_NAME               TEXT                 ,
    OEM_NUMBER                  TEXT                 ,
    UPN_NUMBER                  TEXT                 ,
    LOT_NUMBER                  TEXT                 ,
    LOT_SIZE                    DOUBLE PRECISION     ,
    STATUS                      ITEMINSTANCE_STATUS NOT NULL DEFAULT 'NEW',
    REASON                      TEXT                ,
    RETURN_BY                   INTEGER             ,
    INITIAL_UPN                 TEXT                ,
    REVIEW                      BOOLEAN             DEFAULT FALSE,
    PROVISIONAL_ACCEPT          BOOLEAN             DEFAULT FALSE,
    HAS_FAILED                  BOOLEAN             DEFAULT FALSE,
    PRESENT_STATUS              TEXT                NOT NULL,
    RETURNING_PART              BOOLEAN             NOT NULL DEFAULT FALSE,
    UNIQUE_CODE                 TEXT                ,
    EXPIRY_DATE                 DATE                ,
    ROOTCARD_NO                 TEXT                ,
    FOREIGN KEY (ID)           REFERENCES          CASSINI_OBJECT (OBJECT_ID) ON DELETE CASCADE
);

CREATE TABLE LOT_INSTANCE (
    ID                          INTEGER             NOT NULL PRIMARY KEY,
    INSTANCE                    INTEGER             NOT NULL REFERENCES ITEMINSTANCE(ID) ON DELETE CASCADE,
    LOT_QTY                     DOUBLE PRECISION    NOT NULL,
    UPN_NUMBER                  TEXT                NOT NULL,
    STATUS                      ITEMINSTANCE_STATUS NOT NULL,
    HAS_FAILED                  BOOLEAN             DEFAULT FALSE,
    PRESENT_STATUS              TEXT                NOT NULL,
    SEQUENCE                    INTEGER             ,
--     ISSUE_ITEM                  INTEGER             NOT NULL REFERENCES ISSUEITEM(ID) ON DELETE CASCADE,
    FOREIGN KEY (ID)            REFERENCES          CASSINI_OBJECT (OBJECT_ID) ON DELETE CASCADE
);

CREATE TABLE LOT_INSTANCEHISTORY (
    ID                          INTEGER             NOT NULL PRIMARY KEY,
    LOTINSTANCE                 INTEGER             NOT NULL REFERENCES LOT_INSTANCE (ID) ON DELETE CASCADE,
    STATUS                      ITEMINSTANCE_STATUS NOT NULL,
    TIMESTAMP                   TIMESTAMP           NOT NULL,
    BY_USER                     INTEGER             NOT NULL REFERENCES PERSON (PERSON_ID) ON DELETE CASCADE,
    COMMENT                     TEXT
);

CREATE TABLE ITEMINSTANCESTATUSHISTORY (
    ID                          INTEGER             NOT NULL PRIMARY KEY,
    ITEMINSTANCE                INTEGER             NOT NULL REFERENCES ITEMINSTANCE (ID) ON DELETE CASCADE,
    STATUS                      ITEMINSTANCE_STATUS NOT NULL,
    PRESENT_STATUS              TEXT                NOT NULL,
    TIMESTAMP                   TIMESTAMP           NOT NULL,
    BY_USER                     INTEGER             NOT NULL REFERENCES PERSON (PERSON_ID) ON DELETE CASCADE,
    COMMENT                     TEXT
);

CREATE TABLE BOM (
    ID                          INTEGER             NOT NULL PRIMARY KEY,
    ITEM                        INTEGER             NOT NULL REFERENCES ITEMREVISION (ID) ON DELETE CASCADE,
    FOREIGN KEY (ID)           REFERENCES          CASSINI_OBJECT (OBJECT_ID) ON DELETE CASCADE
);

ALTER TABLE ITEMREVISION ADD COLUMN DEFAULT_BOM INTEGER REFERENCES BOM (ID) ON DELETE CASCADE;

CREATE TABLE BOMGROUP (
    ID                          INTEGER             NOT NULL PRIMARY KEY,
    TYPE                        BOMITEMTYPE         NOT NULL,
    NAME                        TEXT                NOT NULL,
    CODE                        TEXT                NOT NULL,
    VERSITY                     BOOLEAN             DEFAULT FALSE,
    FOREIGN KEY (ID)            REFERENCES          CASSINI_OBJECT(OBJECT_ID) ON DELETE CASCADE,
    UNIQUE (TYPE,CODE)
);

ALTER TABLE ITEMINSTANCE ADD COLUMN     BOM         INTEGER     REFERENCES BOM(ID) ON DELETE CASCADE;
ALTER TABLE ITEMINSTANCE ADD COLUMN     SECTION     INTEGER     REFERENCES BOMGROUP(ID) ON DELETE CASCADE;

CREATE TABLE BOMITEM (
    ID                          INTEGER             NOT NULL PRIMARY KEY,
    PARENT                      INTEGER             REFERENCES BOMITEM (ID) ON DELETE CASCADE,
    BOM                         INTEGER             REFERENCES BOM (ID) ON DELETE CASCADE,
    ITEM                        INTEGER             REFERENCES ITEMREVISION (ID) ON DELETE CASCADE,
    QUANTITY                    INTEGER             ,
    WORK_CENTER                 TEXT                ,
    FRACTIONAL_QUANTITY         DOUBLE PRECISION    ,
    BOMITEM_TYPE                BOMITEMTYPE         NOT NULL,
    TYPEREF                     INTEGER             REFERENCES BOMGROUP(ID) ON DELETE CASCADE,
    HIERARCHICAL_CODE           TEXT                NOT NULL,
    UNIQUE_CODE                 TEXT                , /* THIS IS WHERE THE 5 DIGIT PATH IS STORED */
    FOREIGN KEY (ID)            REFERENCES          CASSINI_OBJECT (OBJECT_ID) ON DELETE CASCADE
);
CREATE TABLE BOMINSTANCE (
    ID                          INTEGER             NOT NULL PRIMARY KEY,
    BOM                         INTEGER             NOT NULL REFERENCES BOM (ID) ON DELETE CASCADE,
    ITEM                        INTEGER             NOT NULL REFERENCES ITEMINSTANCE (ID) ON DELETE CASCADE,
    HAS_PARTTRACKING            BOOLEAN             DEFAULT FALSE,
    PERCENTAGE                  DOUBLE PRECISION    DEFAULT 0,
    STATUS                      TEXT                ,
    FOREIGN KEY (ID)           REFERENCES          CASSINI_OBJECT (OBJECT_ID) ON DELETE CASCADE
);
CREATE TABLE BOMINSTANCEITEM (
    ID                          INTEGER             NOT NULL PRIMARY KEY,
    PARENT                      INTEGER             REFERENCES BOMINSTANCEITEM (ID) ON DELETE CASCADE,
    BOM                         INTEGER             REFERENCES BOMINSTANCE (ID) ON DELETE CASCADE,
    ITEM                        INTEGER             REFERENCES ITEMREVISION (ID) ON DELETE CASCADE,
    BOMITEM                     INTEGER             REFERENCES BOMITEM(ID) ON DELETE CASCADE,
    QUANTITY                    INTEGER             ,
    FRACTIONAL_QUANTITY         DOUBLE PRECISION    ,
    BOMITEM_TYPE                BOMITEMTYPE         NOT NULL,
    TYPEREF                     INTEGER             REFERENCES BOMGROUP(ID) ON DELETE CASCADE,
    HIERARCHICAL_CODE           TEXT                NOT NULL,
    UNIQUE_CODE                 TEXT                , /* THIS IS WHERE THE 5 DIGIT PATH IS STORED */
    ID_PATH                     TEXT                ,
    NAME_PATH                   TEXT                ,
    HAS_PARTTRACKING            BOOLEAN             DEFAULT FALSE,
    PERCENTAGE                  DOUBLE PRECISION    DEFAULT 0,
    STATUS                      TEXT                ,
    WORK_CENTER                 TEXT                ,
    FOREIGN KEY (ID)           REFERENCES           CASSINI_OBJECT (OBJECT_ID) ON DELETE CASCADE
);

CREATE TABLE BOMITEMINSTANCE (
    ID                          INTEGER             NOT NULL PRIMARY KEY,
    BOMINSTANCEITEM             INTEGER             NOT NULL REFERENCES BOMINSTANCEITEM (ID) ON DELETE CASCADE,
    ITEMINSTANCE                INTEGER             NOT NULL REFERENCES ITEMINSTANCE (ID) ON DELETE CASCADE
);
-- </editor-fold>



-- <editor-fold desc="Inward Tables">

CREATE TABLE GATEPASS(
    ID                          INTEGER               NOT NULL PRIMARY KEY,
    GATEPASS_NUMBER             TEXT                  NOT NULL,
    GATEPASS                    INTEGER               REFERENCES FILE (ID) ON DELETE CASCADE,
    GATEPASS_DATE               DATE                  NOT NULL,
    FINISH                      BOOLEAN               DEFAULT FALSE,
    FOREIGN KEY (ID)            REFERENCES            CASSINI_OBJECT (OBJECT_ID) ON DELETE CASCADE
);

CREATE TABLE INWARD (
  ID                          INTEGER               NOT NULL PRIMARY KEY,
  NUMBER                      TEXT                  NOT NULL,
  GATEPASS                    INTEGER               NOT NULL REFERENCES GATEPASS (ID) ON DELETE CASCADE,
  NOTES                       TEXT                   ,
  BOM                         INTEGER               REFERENCES BOM (ID) ON DELETE CASCADE,
  STATUS                      INWARD_STATUS         NOT NULL DEFAULT 'SECURITY',
  UNDER_REVIEW                BOOLEAN               NOT NULL DEFAULT FALSE,
  FOREIGN KEY (ID)           REFERENCES            CASSINI_OBJECT (OBJECT_ID) ON DELETE CASCADE,
  UNIQUE(NUMBER)
);

CREATE TABLE INWARDITEM (
    ID                          INTEGER             NOT NULL PRIMARY KEY,
    INWARD                      INTEGER             NOT NULL REFERENCES INWARD (ID) ON DELETE CASCADE,
    BOM_ITEM                    INTEGER             NOT NULL REFERENCES BOMITEM (ID) ON DELETE CASCADE,
    QUANTITY                    INTEGER             ,
    FRACTIONAL_QUANTITY         DOUBLE PRECISION    ,
    INSTANCES_CREATED           BOOLEAN             NOT NULL DEFAULT FALSE,
    SECTION                     INTEGER             REFERENCES BOMGROUP(ID) ON DELETE CASCADE,
    FOREIGN KEY (ID)            REFERENCES          CASSINI_OBJECT (OBJECT_ID) ON DELETE CASCADE
);

CREATE TABLE INWARDITEMINSTANCE (
    ID                          INTEGER             NOT NULL PRIMARY KEY,
    INWARDITEM                  INTEGER             NOT NULL REFERENCES INWARDITEM (ID) ON DELETE CASCADE,
    ITEM                        INTEGER             NOT NULL REFERENCES ITEMINSTANCE (ID) ON DELETE CASCADE,
    LATEST                      BOOLEAN             NOT NULL DEFAULT TRUE,
    HAS_RETURN_STORAGE          BOOLEAN             DEFAULT FALSE
);

CREATE TABLE INWARDSTATUSHISTORY (
    ID                          INTEGER             NOT NULL PRIMARY KEY,
    INWARD                      INTEGER             NOT NULL REFERENCES INWARD (ID) ON DELETE CASCADE,
    OLD_STATUS                  INWARD_STATUS       NOT NULL,
    NEW_STATUS                  INWARD_STATUS        NOT NULL,
    TIMESTAMP                   TIMESTAMP           NOT NULL,
    BY_USER                        INTEGER             NOT NULL REFERENCES PERSON (PERSON_ID) ON DELETE CASCADE
);
-- </editor-fold>

-- <editor-fold desc="Request Tables">
CREATE TABLE REQUEST (
    ID                      INTEGER                 NOT NULL PRIMARY KEY,
    REQ_NUMBER              TEXT                    NOT NULL,
    BOM_INSTANCE            INTEGER                 NOT NULL REFERENCES BOMINSTANCE (ID) ON DELETE CASCADE,
    REQUESTED_BY            INTEGER                 NOT NULL REFERENCES PERSON (PERSON_ID) ON DELETE CASCADE,
    REQUESTED_DATE          TIMESTAMP               NOT NULL DEFAULT current_timestamp,
    STATUS                  REQUEST_STATUS          NOT NULL DEFAULT 'BDL_EMPLOYEE',
    NOTES                   TEXT                    ,
    ISSUED                  BOOLEAN                 DEFAULT FALSE,
    REASON                  TEXT                    ,
    VERSITY                 BOOLEAN                 DEFAULT FALSE,
    FOREIGN KEY (ID)        REFERENCES          CASSINI_OBJECT (OBJECT_ID) ON DELETE CASCADE,
    UNIQUE (REQ_NUMBER)
);

CREATE TABLE REQUESTITEM (
    ID                      INTEGER                 NOT NULL PRIMARY KEY,
    REQUEST                 INTEGER                 NOT NULL REFERENCES REQUEST (ID) ON DELETE CASCADE,
    ITEM                    INTEGER                 NOT NULL REFERENCES BOMINSTANCEITEM (ID) ON DELETE CASCADE,
    QUANTITY                INTEGER                 DEFAULT 0,
    FRACTIONAL_QUANTITY     DOUBLE PRECISION        DEFAULT 0.0,
    REJECTQTY               INTEGER                 DEFAULT 0,
    FRACTIONAL_REJECTQTY    DOUBLE PRECISION        DEFAULT 0.0,
    FAILUREQTY              INTEGER                 DEFAULT 0,
    FRACTIONAL_FAILUREQTY   DOUBLE PRECISION        DEFAULT 0.0,
    REPLACEDQTY             INTEGER                 DEFAULT 0,
    FRACTIONAL_REPLACEDQTY  DOUBLE PRECISION        DEFAULT 0.0,
    STATUS                  REQUESTITEM_STATUS      DEFAULT 'PENDING',
    REASON                  TEXT                    ,
    ACCEPTED                BOOLEAN                 DEFAULT FALSE,
    APPROVED                BOOLEAN                 DEFAULT FALSE
);

CREATE TABLE REQUESTSTATUSHISTORY (
    ID                          INTEGER             NOT NULL PRIMARY KEY,
    REQUEST                     INTEGER             NOT NULL REFERENCES REQUEST (ID) ON DELETE CASCADE,
    OLD_STATUS                  REQUEST_STATUS      NOT NULL,
    NEW_STATUS                  REQUEST_STATUS      NOT NULL,
    RESULT                      BOOLEAN             NOT NULL DEFAULT TRUE,
    TIMESTAMP                   TIMESTAMP           NOT NULL,
    BY_USER                     INTEGER             NOT NULL REFERENCES PERSON (PERSON_ID) ON DELETE CASCADE
);
-- </editor-fold>

-- <editor-fold desc="Issue Tables">
CREATE TABLE ISSUE (
    ID                          INTEGER                 NOT NULL PRIMARY KEY,
    NUMBER                      TEXT                    NOT NULL,
    REQUEST                     INTEGER                 NOT NULL REFERENCES REQUEST (ID) ON DELETE CASCADE,
    BOMINSTANCE                 INTEGER                 NOT NULL REFERENCES BOMINSTANCE (ID) ON DELETE CASCADE,
    ISSUED_TO                   INTEGER                 REFERENCES PERSON (PERSON_ID) ON DELETE CASCADE,
    NOTES                       TEXT                    ,
    STATUS                      ISSUE_STATUS            DEFAULT 'NEW',
    VERSITY                     BOOLEAN                 DEFAULT FALSE,
    FOREIGN KEY (ID)         REFERENCES          CASSINI_OBJECT (OBJECT_ID) ON DELETE CASCADE,
    UNIQUE(NUMBER)
);

CREATE TABLE ISSUE_HISTORY (
    ID                          INTEGER                 NOT NULL PRIMARY KEY,
    ISSUE                       INTEGER                 NOT NULL REFERENCES ISSUE(ID) ON DELETE CASCADE,
    STATUS                      ISSUE_STATUS            NOT NULL,
    UPDATED_BY                  INTEGER                 NOT NULL REFERENCES PERSON(PERSON_ID) ON DELETE CASCADE,
    UPDATED_DATE                TIMESTAMP
);

CREATE TABLE ISSUEITEM (
    ID                          INTEGER                 NOT NULL PRIMARY KEY,
    ISSUE                       INTEGER                 NOT NULL REFERENCES ISSUE (ID) ON DELETE CASCADE,
    BOMITEMINSTANCE             INTEGER                 NOT NULL REFERENCES BOMITEMINSTANCE (ID) ON DELETE CASCADE,
    QUANTITY                    INTEGER                 NOT NULL DEFAULT 0,
    FRACTIONAL_QUANTITY         DOUBLE PRECISION        DEFAULT 0.0,
    APPROVED                    BOOLEAN                 DEFAULT FALSE,
    STATUS                      ISSUEITEM_STATUS        ,
    APPROVED_BY                 INTEGER                 REFERENCES PERSON (PERSON_ID) ON DELETE CASCADE,
    REQUEST_ITEM                INTEGER                 NOT NULL REFERENCES REQUESTITEM (ID) ON DELETE CASCADE,
    APPROVED_DATE               TIMESTAMP               ,
    RECEIVED_DATE               TIMESTAMP
);

CREATE TABLE BDL_RECEIVE (
    ID                          INTEGER                 NOT NULL PRIMARY KEY,
    ISSUE                       INTEGER                 NOT NULL REFERENCES ISSUE(ID) ON DELETE CASCADE,
    RECEIVE_SEQUENCE            INTEGER                 NOT NULL,
    FOREIGN KEY (ID)            REFERENCES              CASSINI_OBJECT (OBJECT_ID) ON DELETE CASCADE
);

CREATE TABLE BDL_RECEIVEITEM (
    ID                          INTEGER                 NOT NULL PRIMARY KEY,
    RECEIVE                     INTEGER                 NOT NULL REFERENCES BDL_RECEIVE(ID) ON DELETE CASCADE,
    ISSUE_ITEM                  INTEGER                 NOT NULL REFERENCES ISSUEITEM(ID) ON DELETE CASCADE
);

ALTER TABLE LOT_INSTANCE ADD COLUMN ISSUE_ITEM  INTEGER NOT NULL REFERENCES ISSUEITEM(ID) ON DELETE CASCADE;
-- </editor-fold>

-- <editor-fold desc="Inventory Tables">
CREATE TABLE INVENTORY (
    ID                          INTEGER                 NOT NULL PRIMARY KEY,
    BOM                         INTEGER                 NOT NULL REFERENCES BOM (ID) ON DELETE CASCADE,
    ITEM                        INTEGER                 NOT NULL REFERENCES ITEMREVISION (ID) ON DELETE CASCADE,
    UNIQUE_CODE                 TEXT                    NOT NULL, /* THIS IS WHERE THE 5 DIGIT PATH IS STORED */
    SECTION                     INTEGER                 REFERENCES BOMGROUP(ID) ON DELETE CASCADE,
    QTY_ONHAND                  INTEGER                 NOT NULL DEFAULT 0,
    FRACTIONALQTY_ONHAND        DOUBLE PRECISION        NOT NULL DEFAULT 0.0,
    QTY_BUFFERED                INTEGER                 NOT NULL DEFAULT 0,
    FRACTIONALQTY_BUFFERED      DOUBLE PRECISION        NOT NULL DEFAULT 0.0,
    QTY_REQUESTED               INTEGER                 NOT NULL DEFAULT 0,
    FRACTIONALQTY_REQUESTED     DOUBLE PRECISION        NOT NULL DEFAULT 0.0,
    QTY_ISSUED                  INTEGER                 NOT NULL DEFAULT 0,
    FRACTIONALQTY_ISSUED        DOUBLE PRECISION        NOT NULL DEFAULT 0.0,
    QTY_ALLOCATED               INTEGER                 NOT NULL DEFAULT 0,
    FRACTIONALQTY_ALLOCATED     DOUBLE PRECISION        NOT NULL DEFAULT 0.0,
    UNIQUE (BOM, ITEM, UNIQUE_CODE,SECTION)
);
-- </editor-fold>

-- <editor-fold desc="Planning Tables">
CREATE TABLE ITEMALLOCATION (
    ID                            INTEGER               NOT NULL PRIMARY KEY,
    BOM                           INTEGER               NOT NULL REFERENCES BOM (ID),
    BOMINSTANCE                   INTEGER               NOT NULL REFERENCES BOMINSTANCE (ID) ON DELETE CASCADE,
    BOMINSTANCEITEM               INTEGER               NOT NULL REFERENCES BOMINSTANCEITEM (ID) ON DELETE CASCADE,
    ALLOCATE_QTY                  DOUBLE PRECISION      NOT NULL DEFAULT 0.0,
    ISSUED_QTY                    DOUBLE PRECISION      NOT NULL DEFAULT 0.0,
    FAILED_QTY                    DOUBLE PRECISION      NOT NULL DEFAULT 0.0,
    ISSUEPROCESS_QTY              DOUBLE PRECISION      NOT NULL DEFAULT 0.0,
    SECTION                       INTEGER               REFERENCES BOMITEM(ID) ON DELETE CASCADE,
    FOREIGN KEY (ID)              REFERENCES            CASSINI_OBJECT (OBJECT_ID) ON DELETE CASCADE,
    UNIQUE (BOM, BOMINSTANCE, BOMINSTANCEITEM)
);
-- </editor-fold>

-- <editor-fold desc="Storage Tables">
CREATE TABLE STORAGE (
    ID                          INTEGER                 NOT NULL PRIMARY KEY,
    TYPE                        STORAGE_TYPE            NOT NULL,
    NAME                        TEXT                    NOT NULL,
    DESCRIPTION                 TEXT                    ,
    PARENT                      INTEGER                 REFERENCES STORAGE (ID) ON DELETE CASCADE,
    BOM                         INTEGER                 REFERENCES BOM(ID) ON DELETE SET NULL,
    IS_LEAFNODE                 BOOLEAN                 DEFAULT FALSE,
    CAPACITY                    DOUBLE PRECISION        DEFAULT 0.0,
    REMAINING_CAPACITY          DOUBLE PRECISION        DEFAULT 0.0,
    ON_HOLD                     BOOLEAN                 DEFAULT FALSE,
    RETURNED                    BOOLEAN                 DEFAULT FALSE,
    FOREIGN KEY (ID)            REFERENCES              CASSINI_OBJECT (OBJECT_ID) ON DELETE CASCADE
);

ALTER TABLE ITEMINSTANCE ADD COLUMN STORAGE INTEGER REFERENCES STORAGE(ID) ON DELETE CASCADE;

CREATE TABLE STORAGEITEMTYPE (
    ID                          INTEGER                 NOT NULL PRIMARY KEY,
    STORAGE                     INTEGER                 NOT NULL REFERENCES STORAGE(ID) ON DELETE CASCADE,
    ITEM_TYPE                   INTEGER                 NOT NULL REFERENCES ITEMTYPE (ID) ON DELETE CASCADE
);

CREATE TABLE STORAGEITEM (
    ID                          INTEGER                 NOT NULL PRIMARY KEY,
    STORAGE                     INTEGER                 NOT NULL REFERENCES STORAGE (ID) ON DELETE CASCADE,
    UNIQUE_CODE                 TEXT                    NOT NULL,
    ITEM                        INTEGER                 NOT NULL REFERENCES BOMITEM(ID) ON DELETE CASCADE,
    SECTION                     INTEGER                 REFERENCES BOMGROUP(ID) ON DELETE CASCADE
);

-- </editor-fold>

-- <editor-fold desc="Return Tables">

-- </editor-fold>

-- <editor-fold desc="Dispatch Tables">

CREATE TABLE DISPATCH(
    ID                          INTEGER                 NOT NULL PRIMARY KEY,
    NUMBER                      TEXT                    NOT NULL,
    BOM                         INTEGER                 NOT NULL REFERENCES BOM(ID) ON DELETE CASCADE,
    GATEPASS_NUMBER             TEXT                    ,
    STATUS                      DISPATCH_STATUS         NOT NULL,
    DISPATCH_DATE               TIMESTAMP               NOT NULL,
    TYPE                        DISPATCH_TYPE           NOT NULL DEFAULT 'REJECTED',
    FOREIGN KEY (ID)            REFERENCES              CASSINI_OBJECT (OBJECT_ID) ON DELETE CASCADE
);

CREATE TABLE DISPATCH_ITEM (
    ID                          INTEGER                 NOT NULL PRIMARY KEY,
    DISPATCH                    INTEGER                 NOT NULL REFERENCES DISPATCH(ID) ON DELETE CASCADE,
    ITEM                        INTEGER                 NOT NULL REFERENCES ITEMINSTANCE(ID) ON DELETE CASCADE
);

-- </editor-fold>

-- <editor-fold desc="Supplier Tables">

CREATE TABLE SUPPLIER(
    ID                          INTEGER                 NOT NULL PRIMARY KEY,
    SUPPLIER_NAME               TEXT                    NOT NULL,
    SUPPLIER_CODE               TEXT                    NOT NULL,
    DESCRIPTION                 TEXT                    ,
    CONTACT_PERSON              TEXT                    NOT NULL,
    PHONE_NUMBER                TEXT                    ,
    EMAIL                       TEXT                    ,
    ADDRESS                     INTEGER                 NOT NULL REFERENCES ADDRESS(ADDRESS_ID) ON DELETE CASCADE,
    FOREIGN KEY (ID)            REFERENCES              CASSINI_OBJECT(OBJECT_ID) ON DELETE CASCADE
);

ALTER TABLE INWARD ADD COLUMN SUPPLIER  INTEGER     REFERENCES SUPPLIER(ID) ON DELETE CASCADE;

CREATE TABLE MANUFACTURER (
    ID                          INTEGER                 NOT NULL PRIMARY KEY,
    NAME                        TEXT                    NOT NULL,
    DESCRIPTION                 TEXT                    ,
    MFR_CODE                    TEXT                    ,
    PHONE_NUMBER                TEXT                    ,
    EMAIL                       TEXT                    ,
    FOREIGN KEY (ID)            REFERENCES              CASSINI_OBJECT(OBJECT_ID) ON DELETE CASCADE
);

ALTER TABLE ITEMINSTANCE ADD COLUMN MANUFACTURER    INTEGER     REFERENCES MANUFACTURER(ID) ON DELETE CASCADE;
ALTER TABLE ITEMINSTANCE ADD CONSTRAINT itemInstance_item_manufacturer_oem_number UNIQUE (ITEM,MANUFACTURER,OEM_NUMBER);
-- </editor-fold>

-- <editor-fold desc="Purchasing Tables">

-- </editor-fold>

-- <editor-fold desc="Part Tracking Tables">
CREATE TABLE PARTTRACKING (
    LIST_ID                     INTEGER             NOT NULL PRIMARY KEY,
    NAME                        TEXT                NOT NULL,
    DESCRIPTION                 TEXT
);

CREATE TABLE PARTTRACKINGSTEPS (
    ID                          INTEGER             NOT NULL PRIMARY KEY,
    SERIAL_NO                   INTEGER             NOT NULL,
    PARTTRACKING                INTEGER             NOT NULL,
    STATUS                      TEXT                NOT NULL,
    DESCRIPTION                 TEXT                NOT NULL,
    BDL                         BOOLEAN             DEFAULT TRUE,
    SSQAG                       BOOLEAN             DEFAULT TRUE,
    CAS                         BOOLEAN             DEFAULT TRUE,
    SCAN                        BOOLEAN             DEFAULT FALSE,
    ATTACHMENT                  BOOLEAN             DEFAULT FALSE,
    PERCENTAGE                  DOUBLE PRECISION    NOT NULL
);

CREATE TABLE PARTTRACKING_ITEMS (
  ID                          INTEGER             NOT NULL PRIMARY KEY,
  ITEM_ID                     INTEGER             NOT NULL,
  PARTTRACKING_STEP           INTEGER             NOT NULL REFERENCES PARTTRACKINGSTEPS(ID) ON DELETE  CASCADE,
  SERIAL_NUMBER               INTEGER             NOT NULL,
  STATUS                      TEXT
);

CREATE TABLE MBOMPARTTRACKING (
    ID                          INTEGER             NOT NULL PRIMARY KEY,
    ITEM                        INTEGER             NOT NULL REFERENCES ITEM (ID) ON DELETE CASCADE,
    PARENT                      INTEGER             NOT NULL REFERENCES ITEM (ID) ON DELETE CASCADE,
    HASPARTTRACKING             BOOLEAN             DEFAULT FALSE,
    STATUS                      TEXT                ,
    PERCENTAGE                  DOUBLE PRECISION    DEFAULT 0
);

CREATE TABLE TRACKVALUE (
  ID                            INTEGER        NOT NULL PRIMARY KEY,
  PARTTRACKING                  INTEGER        NOT NULL REFERENCES PARTTRACKING_ITEMS(ID) ON DELETE CASCADE,
  CHECKED                       BOOLEAN,
  COMMENT                       TEXT,
  ATTACHMENT                    INTEGER,
  SCAN                          BOOLEAN        DEFAULT FALSE,
  CHECKED_DEPT                  TEXT           NOT NULL
);

CREATE TABLE PARTTRACKING_SCANNED_UPN(
  ID                            INTEGER        NOT NULL PRIMARY KEY,
  TRACK_ID                      INTEGER        NOT NULL,
  UPN                           TEXT,
  FAIL                          BOOLEAN
);

CREATE TABLE FAILURELIST (
    LIST_ID                     INTEGER             NOT NULL PRIMARY KEY,
    NAME                        TEXT                NOT NULL,
    DESCRIPTION                 TEXT
);

CREATE TABLE FAILURESTEPS (
    ID                          INTEGER             NOT NULL PRIMARY KEY,
    SERIAL_NO                   INTEGER             NOT NULL,
    FAILURELIST                 INTEGER             NOT NULL,
    STATUS                      TEXT                NOT NULL,
    DATATYPE                    TEXT                NOT NULL
);

CREATE TABLE DRDO_FAILVALUELIST (
    ID                          INTEGER             NOT NULL PRIMARY KEY,
    BOMINSTANCEITEM             INTEGER             NOT NULL REFERENCES BOMINSTANCEITEM (ID) ON DELETE  CASCADE,
    ITEMINSTANCE                INTEGER             NOT NULL REFERENCES ITEMINSTANCE (ID) ON DELETE  CASCADE,
    LOTINSTANCE                 INTEGER             REFERENCES LOT_INSTANCE (ID) ON DELETE  CASCADE,
    FAILURESTEP                 INTEGER             NOT NULL,
    SNO                         INTEGER             NOT NULL,
    VALUE                       TEXT,
    CHECKED_DATE                TIMESTAMP,
    CHECKED_BY                  INTEGER,
    UNIQUE (ITEMINSTANCE, FAILURESTEP),
    FOREIGN KEY (ID)            REFERENCES          CASSINI_OBJECT (OBJECT_ID) ON DELETE CASCADE
);

CREATE TABLE DRDO_UPDATES (
    ID                          INTEGER             NOT NULL PRIMARY KEY,
    MESSAGE                     TEXT                ,
    PERSON                      INTEGER             NOT NULL REFERENCES PERSON(PERSON_ID) ON DELETE CASCADE,
    DATE                        TIMESTAMP           NOT NULL,
    UPDATED_BY                  INTEGER             REFERENCES PERSON(PERSON_ID) ON DELETE CASCADE,
    READ                        BOOLEAN             DEFAULT FALSE,
    OBJECT_TYPE                 TEXT
);

-- </editor-fold>
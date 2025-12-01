CREATE TABLE PDM_ITEMTYPE (
    TYPE_ID                     INTEGER             NOT NULL PRIMARY KEY DEFAULT NEXTVAL('VAULT_ID_SEQ'),
    NAME                        TEXT                NOT NULL,
    DESCRIPTION                 TEXT                ,
    PARENT_TYPE                 INTEGER             REFERENCES PDM_ITEMTYPE (TYPE_ID) ON DELETE CASCADE,
    ITEMNUMBER_SOURCE           INTEGER             REFERENCES AUTONUMBER (AUTONUMBER_ID) ON DELETE CASCADE,
    REVISION_SEQUENCE           INTEGER             REFERENCES LOV (LOV_ID) ON DELETE CASCADE,
    LIFECYCLE_STATES            INTEGER             REFERENCES LOV (LOV_ID) ON DELETE CASCADE,
    FOREIGN KEY (TYPE_ID)       REFERENCES          CASSINI_OBJECT (OBJECT_ID) ON DELETE CASCADE
);

CREATE TABLE PDM_ITEMTYPEATTRIBUTE (
    ATTRIBUTE_ID                  INTEGER           NOT NULL PRIMARY KEY,
    ITEM_TYPE                     INTEGER           NOT NULL REFERENCES PDM_ITEMTYPE (TYPE_ID) ON DELETE CASCADE,
    FOREIGN KEY (ATTRIBUTE_ID)    REFERENCES        OBJECTTYPEATTRIBUTE (ATTRIBUTE_ID) ON DELETE CASCADE
);


CREATE TABLE PDM_ITEM (
    ITEM_ID                     INTEGER             NOT NULL PRIMARY KEY,
    ITEM_TYPE                   INTEGER             NOT NULL REFERENCES PDM_ITEMTYPE (TYPE_ID) ON DELETE CASCADE,
    ITEM_NUMBER                 TEXT                NOT NULL,
    DESCRIPTION                 TEXT                ,
    REVISION                    TEXT                ,
    STATUS                      TEXT                ,
    ACTIVE                      BOOLEAN             NOT NULL,
    UNITS                       TEXT                NOT NULL DEFAULT 'Each',
    UNIT_PRICE                  DOUBLE PRECISION    ,
    UNIT_COST                   DOUBLE PRECISION    ,
    PICTURE                     BYTEA               ,
    HAS_BOM                     BOOLEAN             NOT NULL,
    HAS_FILES                   BOOLEAN             NOT NULL,
    FOREIGN KEY (ITEM_ID)       REFERENCES          CASSINI_OBJECT (OBJECT_ID) ON DELETE CASCADE,
    UNIQUE(ITEM_TYPE, ITEM_NUMBER, REVISION)
);

CREATE TABLE PDM_VAULT (
    VAULT_ID                    INTEGER             NOT NULL PRIMARY KEY,
    NAME                        TEXT                NOT NULL,
    DESCRIPTION                 TEXT                ,
    FOREIGN KEY (VAULT_ID)      REFERENCES          CASSINI_OBJECT (OBJECT_ID) ON DELETE CASCADE
);


CREATE TABLE PDM_COMMIT (
  ID                        INTEGER                PRIMARY KEY,
  SHA                       TEXT                   ,
  COMMENTS                  TEXT                    NOT NULL,
  UNIQUE (SHA),
  FOREIGN KEY (ID)         REFERENCES             CASSINI_OBJECT (OBJECT_ID) ON DELETE CASCADE
);


CREATE TABLE PDM_LOCKABLE (
    LOCKABLE_ID                 INTEGER             NOT NULL PRIMARY KEY,
    LOCKED                      BOOLEAN             NOT NULL DEFAULT FALSE,
    LOCKED_BY                   INTEGER             REFERENCES PERSON (PERSON_ID) ON DELETE CASCADE,
    LOCKED_DATE                 TIMESTAMP           ,
    FOREIGN KEY (LOCKABLE_ID)   REFERENCES          CASSINI_OBJECT (OBJECT_ID) ON DELETE CASCADE
);

CREATE TABLE PDM_VERSIONEDOBJECT (
    VERSIONED_ID                INTEGER             NOT NULL PRIMARY KEY,
    NAME                        TEXT                NOT NULL,
    DESCRIPTION                 TEXT                ,
    COMMIT                      INTEGER             ,
    VERSION                     INTEGER             NOT NULL DEFAULT 1,
    LATEST                      BOOLEAN             DEFAULT TRUE,
    TIMESTAMP                   TIMESTAMP           NOT NULL,
    PATH                        TEXT                ,
    IDPATH                      TEXT                ,
    FOREIGN KEY (VERSIONED_ID)     REFERENCES          PDM_LOCKABLE (LOCKABLE_ID) ON DELETE CASCADE
);

CREATE TABLE PDM_FOLDER (
    FOLDER_ID                   INTEGER             NOT NULL PRIMARY KEY,
    PARENT                      INTEGER             REFERENCES PDM_FOLDER (FOLDER_ID) ON DELETE CASCADE,
    VAULT                       INTEGER             REFERENCES PDM_VAULT (VAULT_ID) ON DELETE CASCADE,
    FOREIGN KEY (FOLDER_ID)     REFERENCES          PDM_VERSIONEDOBJECT (VERSIONED_ID) ON DELETE CASCADE
);

CREATE TABLE PDM_FOLDERATTRIBUTE (
    FOLDER                      INTEGER             NOT NULL REFERENCES PDM_ITEM (ITEM_ID) ON DELETE CASCADE,
    ATTRIBUTE                   INTEGER             NOT NULL REFERENCES PDM_ITEMTYPEATTRIBUTE (ATTRIBUTE_ID) ON DELETE CASCADE,
    FOREIGN KEY (FOLDER, ATTRIBUTE)          REFERENCES          OBJECTATTRIBUTE (OBJECT_ID, ATTRIBUTEDEF)  ON DELETE CASCADE,
    UNIQUE (FOLDER, ATTRIBUTE)
);

CREATE TABLE PDM_FILE (
    FILE_ID                     INTEGER             NOT NULL PRIMARY KEY,
    SIZE                        BIGINT              NOT NULL,
    FOLDER                      INTEGER             REFERENCES PDM_FOLDER (FOLDER_ID)  ON DELETE CASCADE,
    VAULT                       INTEGER             REFERENCES PDM_VAULT (VAULT_ID) ON DELETE CASCADE,
    THUMBNAIL                   BYTEA               ,
    FOREIGN KEY (FILE_ID)       REFERENCES          PDM_VERSIONEDOBJECT (VERSIONED_ID) ON DELETE CASCADE
);

CREATE TABLE PDM_FILEREFERENCE (
    REF_ID                      INTEGER             NOT NULL PRIMARY KEY,
    PARENT                      INTEGER             NOT NULL REFERENCES PDM_FILE(FILE_ID),
    REFERENCE                   INTEGER             NOT NULL REFERENCES PDM_FILE(FILE_ID)
);

CREATE TABLE PDM_FILEATTRIBUTE (
    FILE                        INTEGER             NOT NULL REFERENCES PDM_ITEM (ITEM_ID) ON DELETE CASCADE,
    ATTRIBUTE                   INTEGER             NOT NULL REFERENCES PDM_ITEMTYPEATTRIBUTE (ATTRIBUTE_ID) ON DELETE CASCADE,
    FOREIGN KEY (FILE, ATTRIBUTE)          REFERENCES          OBJECTATTRIBUTE (OBJECT_ID, ATTRIBUTEDEF)  ON DELETE CASCADE,
    UNIQUE (FILE, ATTRIBUTE)
);

CREATE TABLE PDM_ITEMREFERENCE (
    ROWID                   INTEGER                 NOT NULL PRIMARY KEY,
    ITEM                    INTEGER                 NOT NULL REFERENCES PDM_ITEM(ITEM_ID) ON DELETE CASCADE,
    REFERENCE               INTEGER                 NOT NULL REFERENCES PDM_ITEM (ITEM_ID) ON DELETE CASCADE,
    REVISION                TEXT                    NOT NULL,
    STATUS                  TEXT                    NOT NULL,
    NOTES                   TEXT
);

CREATE TABLE PDM_ITEMSTATUSHISTORY (
    ROWID                       INTEGER             NOT NULL PRIMARY KEY,
    ITEM                        INTEGER             NOT NULL REFERENCES PDM_ITEM (ITEM_ID) ON DELETE CASCADE,
    OLD_STATUS                  TEXT                NOT NULL,
    NEW_STATUS                  TEXT                NOT NULL,
    TIMESTAMP                   TIMESTAMP           NOT NULL,
    UPDATED_BY                  INTEGER             NOT NULL REFERENCES PERSON (PERSON_ID) ON DELETE CASCADE
);

CREATE TABLE PDM_ITEMREVISIONHISTORY (
    ROWID                       INTEGER             NOT NULL PRIMARY KEY,
    ITEM                        INTEGER             NOT NULL REFERENCES PDM_ITEM (ITEM_ID) ON DELETE CASCADE,
    OLD_REVISION                TEXT                NOT NULL,
    NEW_REVISION                TEXT                NOT NULL,
    TIMESTAMP                   TIMESTAMP           NOT NULL,
    UPDATED_BY                  INTEGER             NOT NULL REFERENCES PERSON (PERSON_ID) ON DELETE CASCADE
);

CREATE TABLE PDM_ITEMFILE (
    FILE_ID                     INTEGER             NOT NULL PRIMARY KEY,
    ITEM                        INTEGER             NOT NULL REFERENCES PDM_ITEM (ITEM_ID) ON DELETE CASCADE,
    FOREIGN KEY (FILE_ID)       REFERENCES          PDM_FILE (FILE_ID) ON DELETE CASCADE
);

CREATE TABLE PDM_ITEMFILEVERSIONHISTORY (
    ROWID                       INTEGER             NOT NULL PRIMARY KEY,
    FILE                        INTEGER             NOT NULL REFERENCES PDM_ITEMFILE (FILE_ID) ON DELETE CASCADE,
    ITEM                        INTEGER             NOT NULL REFERENCES PDM_ITEM(ITEM_ID) ON DELETE CASCADE,
    FILE_NAME                   TEXT                NOT NULL,
    OLD_VERSION                 INTEGER             NOT NULL,
    NEW_VERSION                 INTEGER             NOT NULL,
    TIMESTAMP                   TIMESTAMP           NOT NULL,
    UPDATED_BY                  INTEGER             NOT NULL REFERENCES PERSON (PERSON_ID) ON DELETE CASCADE
);


CREATE TABLE PDM_ITEMATTRIBUTE (
    ITEM                        INTEGER             NOT NULL REFERENCES PDM_ITEM (ITEM_ID) ON DELETE CASCADE,
    ATTRIBUTE                   INTEGER             NOT NULL REFERENCES PDM_ITEMTYPEATTRIBUTE (ATTRIBUTE_ID) ON DELETE CASCADE,
    FOREIGN KEY (ITEM, ATTRIBUTE)          REFERENCES          OBJECTATTRIBUTE (OBJECT_ID, ATTRIBUTEDEF)  ON DELETE CASCADE,
    UNIQUE (ITEM, ATTRIBUTE)
);

CREATE TABLE PDM_BOM (
    BOMITEM_ID                  INTEGER             NOT NULL PRIMARY KEY,
    ITEM                        INTEGER             NOT NULL REFERENCES PDM_ITEM (ITEM_ID) ON DELETE CASCADE,
    PARENT                      INTEGER             NOT NULL REFERENCES PDM_ITEM (ITEM_ID) ON DELETE CASCADE,
    QUANTITY                    INTEGER             NOT NULL,
    REFDES                      TEXT                ,
    NOTES                       TEXT                ,
    FOREIGN KEY (BOMITEM_ID)    REFERENCES          CASSINI_OBJECT (OBJECT_ID) ON DELETE CASCADE
);

CREATE TABLE PDM_OBJECTPERMISSION(
    ROW_ID                          INTEGER             NOT NULL PRIMARY KEY ,
    OBJECTTYPE                      OBJECT_TYPE         NOT NULL,
    OBJECT_ID                       INTEGER             NOT NULL,
    PERMISSION_LEVEL                PERMISSION_LEVEL    NOT NULL,
    PERMISSION_ASSIGNEDTO           INTEGER             NOT NULL,
    ACTION_TYPES                     TEXT[]              NOT NULL
);
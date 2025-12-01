CREATE TABLE PLM_LIFECYCLE (
    ID                          INTEGER             NOT NULL PRIMARY KEY,
    NAME                        TEXT                NOT NULL,
    DESCRIPTION                 TEXT,
    UNIQUE (NAME)
);

CREATE TABLE PLM_LIFECYCLEPHASE (
    ID                          INTEGER             NOT NULL PRIMARY KEY,
    LIFECYCLE                   INTEGER             NOT NULL REFERENCES PLM_LIFECYCLE (ID) ON DELETE CASCADE,
    PHASE                       TEXT                NOT NULL,
    PHASE_TYPE                  LIFECYCLE_PHASE_TYPE         NOT NULL,
    UNIQUE(LIFECYCLE, PHASE)
);

CREATE TABLE PLM_ITEMTYPE (
    TYPE_ID                     INTEGER             NOT NULL PRIMARY KEY,
    NAME                        TEXT                NOT NULL,
    DESCRIPTION                 TEXT                ,
    PARENT_TYPE                 INTEGER             REFERENCES PLM_ITEMTYPE (TYPE_ID) ON DELETE CASCADE,
    ITEMNUMBER_SOURCE           INTEGER             REFERENCES AUTONUMBER (AUTONUMBER_ID) ON DELETE CASCADE,
    REVISION_SEQUENCE           INTEGER             REFERENCES LOV (LOV_ID) ON DELETE CASCADE,
    LIFECYCLE                   INTEGER             REFERENCES PLM_LIFECYCLE (ID) ON DELETE CASCADE,
    EXCLRULES                   TEXT                DEFAULT NULL,
    TABS                        TEXT[]              ,
    REQUIRED_ECO                BOOLEAN             DEFAULT TRUE,
    ITEM_CLASS                  ITEM_CLASS          ,
    IS_SOFTWARE_TYPE            BOOLEAN             DEFAULT FALSE,
    FOREIGN KEY (TYPE_ID)       REFERENCES          CASSINI_OBJECT (OBJECT_ID) ON DELETE CASCADE
);

CREATE TABLE PLM_ITEMTYPEATTRIBUTE (
    ATTRIBUTE_ID                  INTEGER           NOT NULL PRIMARY KEY,
    ITEM_TYPE                     INTEGER           NOT NULL REFERENCES PLM_ITEMTYPE (TYPE_ID) ON DELETE CASCADE,
    REVISION_SPECIFIC             BOOLEAN           NOT NULL DEFAULT FALSE,
    CHANGE_CONTROLLED             BOOLEAN           NOT NULL DEFAULT FALSE,
    SEQ                           INTEGER           ,
    CONFIGURABLE                  BOOLEAN           DEFAULT FALSE,
    ALLOW_EDIT_AFTER_RELEASE      BOOLEAN           DEFAULT FALSE,
    FOREIGN KEY (ATTRIBUTE_ID)    REFERENCES        OBJECTTYPEATTRIBUTE (ATTRIBUTE_ID) ON DELETE CASCADE
);

CREATE TABLE PLM_FILE (
    FILE_ID                     INTEGER             NOT NULL PRIMARY KEY,
    FILE_NO                     TEXT                NOT NULL ,
    NAME                        TEXT                NOT NULL,
    DESCRIPTION                 TEXT                ,
    SIZE                        BIGINT              NOT NULL,
    VERSION                     INTEGER             NOT NULL DEFAULT 1,
    LATEST                      BOOLEAN             DEFAULT TRUE,
    LOCKED                      BOOLEAN             NOT NULL DEFAULT FALSE,
    LOCKED_BY                   INTEGER             REFERENCES PERSON (PERSON_ID) ON DELETE CASCADE,
    LOCKED_DATE                 TIMESTAMP           ,
    REPLACEFILENAME             TEXT                ,
    FILE_URN                    TEXT                ,
    THUMBNAIL                   TEXT                ,
    PARENT_FILE                 INTEGER             REFERENCES PLM_FILE(FILE_ID) ON DELETE CASCADE,
    FILE_TYPE                   TEXT                NOT NULL DEFAULT 'FILE',
    FOREIGN KEY (FILE_ID)       REFERENCES          CASSINI_OBJECT (OBJECT_ID) ON DELETE CASCADE
);

CREATE TABLE PLM_DOCUMENT (
    ID                          INTEGER             NOT NULL PRIMARY KEY,
    REVISION                    TEXT                NOT NULL,
    LIFECYCLE_PHASE             INTEGER             NOT NULL REFERENCES PLM_LIFECYCLEPHASE (ID),
    FOREIGN KEY (ID)            REFERENCES          PLM_FILE (FILE_ID) ON DELETE CASCADE
);

CREATE TABLE PLM_OBJECT_DOCUMENT (
    ID                          INTEGER             NOT NULL PRIMARY KEY,
    OBJECT                      INTEGER             NOT NULL REFERENCES CASSINI_OBJECT (OBJECT_ID) ON DELETE CASCADE,
    DOCUMENT                    INTEGER             NOT NULL REFERENCES PLM_DOCUMENT(ID) ON DELETE CASCADE,
    FOLDER                      INTEGER             REFERENCES PLM_FILE(FILE_ID) ON DELETE CASCADE,
    DOCUMENT_TYPE               TEXT                DEFAULT 'FILE',
    FOREIGN KEY (ID)            REFERENCES          CASSINI_OBJECT (OBJECT_ID) ON DELETE CASCADE
);

CREATE TYPE DOCUMENT_APPROVAL_STATUS AS ENUM (
    'NONE',
    'APPROVED',
    'REJECTED',
    'REVIEWED'
);

CREATE TABLE PLM_DOCUMENT_REVIEWER(
    ID                          INTEGER             NOT NULL PRIMARY KEY,
    DOCUMENT                    INTEGER             NOT NULL REFERENCES PLM_FILE(FILE_ID) ON DELETE CASCADE,
    REVIEWER                    INTEGER             NOT NULL REFERENCES PERSON(PERSON_ID) ON DELETE CASCADE,
    APPROVER                    BOOLEAN             NOT NULL DEFAULT FALSE,
    VOTE                        DOCUMENT_APPROVAL_STATUS NOT NULL DEFAULT 'NONE',
    VOTE_TIMESTAMP              TIMESTAMP           ,
    NOTES                       TEXT
);

CREATE TABLE PLM_ITEM (
    ITEM_ID                     INTEGER             NOT NULL PRIMARY KEY,
    ITEM_TYPE                   INTEGER             NOT NULL REFERENCES PLM_ITEMTYPE (TYPE_ID) ON DELETE CASCADE,
    ITEM_NUMBER                 TEXT                NOT NULL,
    ITEM_NAME                   TEXT                NOT NULL DEFAULT '',
    DESCRIPTION                 TEXT                ,
    THUMBNAIL                   BYTEA               ,
    UNITS                       TEXT                NOT NULL DEFAULT 'EA',
    MAKE_OR_BUY                 MAKE_OR_BUY         NOT NULL DEFAULT 'BUY',
    LOCK_OBJECT                 BOOLEAN             DEFAULT FALSE,
    LOCKED_DATE                 TIMESTAMP           ,
    LOCKED_BY                   INTEGER             REFERENCES PERSON(PERSON_ID) ON DELETE CASCADE,
    ITEM_FILE                   INTEGER             REFERENCES PLM_FILE(FILE_ID) ON DELETE SET NULL,
    CONFIGURABLE                BOOLEAN             DEFAULT FALSE,
    CONFIGURED                  BOOLEAN             DEFAULT FALSE,
    INSTANCE                    INTEGER             REFERENCES PLM_ITEM (ITEM_ID) ON DELETE CASCADE,
    REQUIRE_COMPLIANCE          BOOLEAN             DEFAULT FALSE,
    HAS_ALTERNATES              BOOLEAN             DEFAULT FALSE,
    IS_TEMPORARY                BOOLEAN             DEFAULT FALSE,
    FOREIGN KEY (ITEM_ID)       REFERENCES          CASSINI_OBJECT (OBJECT_ID) ON DELETE CASCADE,
    UNIQUE(ITEM_TYPE, ITEM_NUMBER)
);

CREATE TABLE PLM_ITEMREVISION (
    ITEM_ID                     INTEGER             NOT NULL PRIMARY KEY,
    ITEM_MASTER                 INTEGER             NOT NULL REFERENCES PLM_ITEM (ITEM_ID) ON DELETE CASCADE,
    REVISION                    TEXT                NOT NULL,
    LIFECYCLE_PHASE             INTEGER             NOT NULL REFERENCES PLM_LIFECYCLEPHASE (ID),
    IS_RELEASED                 BOOLEAN             NOT NULL DEFAULT FALSE,
    IS_REJECTED                 BOOLEAN             NOT NULL DEFAULT FALSE,
    RELEASED_DATE               TIMESTAMP           ,
    HAS_BOM                     BOOLEAN             NOT NULL DEFAULT FALSE,
    HAS_FILES                   BOOLEAN             NOT NULL DEFAULT FALSE,
    INSTANCE                    INTEGER             REFERENCES PLM_ITEMREVISION (ITEM_ID) ON DELETE CASCADE,
    BOMINCLRULES                TEXT                DEFAULT NULL,
    ITEMEXCLRULES               TEXT                DEFAULT NULL,
    ATTRIBUTE_EXCLUSIONRULES    TEXT                DEFAULT NULL,
    ITEM_EXCLUSIONS             TEXT                DEFAULT NULL,
    BOM_CONFIGURATION           INTEGER             ,
    EFFECTIVE_FROM              TIMESTAMP           ,
    EFFECTIVE_TO                TIMESTAMP           ,
    FROM_REVISION               TEXT                ,
    INCORPORATE                 BOOLEAN             DEFAULT FALSE,
    INCORPORATE_DATE            TIMESTAMP           ,
    FOREIGN KEY (ITEM_ID)       REFERENCES          CASSINI_OBJECT (OBJECT_ID) ON DELETE CASCADE
);

ALTER TABLE PLM_ITEM ADD COLUMN LATEST_REVISION INTEGER REFERENCES PLM_ITEMREVISION(ITEM_ID) ON DELETE CASCADE;
ALTER TABLE PLM_ITEM ADD COLUMN LATEST_RELEASED_REVISION INTEGER REFERENCES PLM_ITEMREVISION(ITEM_ID) ON DELETE CASCADE;

CREATE TABLE PLM_ITEMREFERENCE (
    ROWID                   INTEGER                 NOT NULL PRIMARY KEY,
    PARENT                  INTEGER                 NOT NULL REFERENCES PLM_ITEM (ITEM_ID) ON DELETE CASCADE,
    ITEM                    INTEGER                 NOT NULL REFERENCES PLM_ITEM (ITEM_ID) ON DELETE CASCADE,
    REVISION                TEXT                    NOT NULL,
    STATUS                  TEXT                    NOT NULL,
    NOTES                   TEXT
);

CREATE TABLE PLM_ITEMSTATUSHISTORY (
    ROWID                       INTEGER             NOT NULL PRIMARY KEY,
    ITEM                        INTEGER             NOT NULL REFERENCES PLM_ITEMREVISION (ITEM_ID) ON DELETE CASCADE,
    OLD_STATUS                  TEXT                NOT NULL,
    NEW_STATUS                  TEXT                NOT NULL,
    TIMESTAMP                   TIMESTAMP           NOT NULL,
    UPDATED_BY                  INTEGER             NOT NULL REFERENCES PERSON (PERSON_ID) ON DELETE CASCADE
);

CREATE TABLE PLM_ITEMREVISIONSTATUSHISTORY (
    ROWID                       INTEGER             NOT NULL PRIMARY KEY,
    ITEM                        INTEGER             NOT NULL REFERENCES PLM_ITEMREVISION (ITEM_ID) ON DELETE CASCADE,
    OLD_STATUS                  INTEGER             REFERENCES PLM_LIFECYCLEPHASE (ID),
    NEW_STATUS                  INTEGER             REFERENCES PLM_LIFECYCLEPHASE (ID),
    TIMESTAMP                   TIMESTAMP           NOT NULL,
    UPDATED_BY                  INTEGER             NOT NULL REFERENCES PERSON (PERSON_ID) ON DELETE CASCADE
);

CREATE TABLE PLM_ITEMFILE (
    FILE_ID                     INTEGER             NOT NULL PRIMARY KEY,
    ITEM                        INTEGER             NOT NULL REFERENCES PLM_ITEMREVISION (ITEM_ID) ON DELETE CASCADE,
    PARENT                      INTEGER             REFERENCES PLM_ITEMFILE(FILE_ID) ON DELETE CASCADE,
    TYPE                        TEXT                NOT NULL DEFAULT 'FILE',
    FOREIGN KEY (FILE_ID)       REFERENCES          PLM_FILE (FILE_ID) ON DELETE CASCADE
);

CREATE TABLE PLM_FILEVERSIONHISTORY (
    ROWID                       INTEGER             NOT NULL PRIMARY KEY,
    FILE                        INTEGER             NOT NULL REFERENCES PLM_ITEMFILE (FILE_ID) ON DELETE CASCADE,
    OLD_VERSION                 INTEGER             NOT NULL,
    NEW_VERSION                 INTEGER             NOT NULL,
    TIMESTAMP                   TIMESTAMP           NOT NULL,
    UPDATED_BY                  INTEGER             NOT NULL REFERENCES PERSON (PERSON_ID) ON DELETE CASCADE
);


CREATE TABLE PLM_ITEMATTRIBUTE (
    ITEM                        INTEGER             NOT NULL REFERENCES PLM_ITEM (ITEM_ID) ON DELETE CASCADE,
    ATTRIBUTE                   INTEGER             NOT NULL REFERENCES PLM_ITEMTYPEATTRIBUTE (ATTRIBUTE_ID) ON DELETE CASCADE,
    FOREIGN KEY (ITEM, ATTRIBUTE)          REFERENCES          OBJECTATTRIBUTE (OBJECT_ID, ATTRIBUTEDEF)  ON DELETE CASCADE,
    UNIQUE (ITEM, ATTRIBUTE)
);

CREATE TABLE PLM_ITEMREVISIONATTRIBUTE (
    ITEM                        INTEGER             NOT NULL REFERENCES PLM_ITEMREVISION (ITEM_ID) ON DELETE CASCADE,
    ATTRIBUTE                   INTEGER             NOT NULL REFERENCES PLM_ITEMTYPEATTRIBUTE (ATTRIBUTE_ID) ON DELETE CASCADE,
    FOREIGN KEY (ITEM, ATTRIBUTE)          REFERENCES          OBJECTATTRIBUTE (OBJECT_ID, ATTRIBUTEDEF)  ON DELETE CASCADE,
    UNIQUE (ITEM, ATTRIBUTE)
);

CREATE TABLE PLM_BOM (
    BOMITEM_ID                  INTEGER             NOT NULL PRIMARY KEY,
    PARENT                      INTEGER             NOT NULL REFERENCES PLM_ITEMREVISION (ITEM_ID) ON DELETE CASCADE,
    ITEM                        INTEGER             NOT NULL REFERENCES PLM_ITEM (ITEM_ID) ON DELETE CASCADE,
    HAS_SUBSTITUTES             BOOLEAN             NOT NULL DEFAULT FALSE,
    QUANTITY                    INTEGER             NOT NULL,
    SUBSTITUTE_ITEM             INTEGER             ,
    REFDES                      TEXT                ,
    NOTES                       TEXT                ,
    AS_RELEASED_REVISION        INTEGER             REFERENCES PLM_ITEMREVISION(ITEM_ID) ON DELETE CASCADE,
    SEQUENCE                    INTEGER             ,
    EFFECTIVE_FROM              TIMESTAMP           ,
    EFFECTIVE_TO                TIMESTAMP           ,
    FOREIGN KEY (BOMITEM_ID)    REFERENCES          CASSINI_OBJECT (OBJECT_ID) ON DELETE CASCADE
);

CREATE TABLE PLM_FOLDER (
    FOLDER_ID                   INTEGER             NOT NULL PRIMARY KEY,
    NAME                        TEXT                NOT NULL,
    DESCRIPTION                 TEXT                ,
    OWNER                       INTEGER             NOT NULL REFERENCES PERSON (PERSON_ID) ON DELETE CASCADE,
    PARENT                      INTEGER             REFERENCES PLM_FOLDER (FOLDER_ID) ON DELETE CASCADE,
    TYPE                        FOLDER_TYPE         DEFAULT 'PUBLIC',
    FOREIGN KEY (FOLDER_ID)     REFERENCES          CASSINI_OBJECT (OBJECT_ID) ON DELETE CASCADE
);

CREATE TABLE PLM_SHAREDFOLDER (
    ROWID                       INTEGER             NOT NULL PRIMARY KEY,
    FOLDER                      INTEGER             NOT NULL REFERENCES PLM_FOLDER (FOLDER_ID) ON DELETE CASCADE,
    SHARED_ON                   TIMESTAMP           NOT NULL,
    SHARED_WITH                 INTEGER             NOT NULL REFERENCES PERSON (PERSON_ID) ON DELETE CASCADE,
    SHARED_BY                   INTEGER             NOT NULL REFERENCES PERSON (PERSON_ID) ON DELETE CASCADE,
    SHARE_SUBFOLDERS            BOOLEAN             NOT NULL
);

CREATE TABLE PLM_FOLDEROBJECT (
    ROWID                       INTEGER             NOT NULL PRIMARY KEY,
    FOLDER                      INTEGER             NOT NULL,
    OBJECT_TYPE                 OBJECT_TYPE         NOT NULL,
    OBJECT_ID                   INTEGER             NOT NULL,
    DATE_ADDED                  TIMESTAMP           NOT NULL
);

CREATE TABLE PLM_LIBRARY (
    LIBRARY_ID                  INTEGER             NOT NULL PRIMARY KEY,
    NAME                        TEXT                NOT NULL,
    DESCRIPTION                 TEXT                ,
    PARENT                      INTEGER             REFERENCES PLM_LIBRARY (LIBRARY_ID) ON DELETE CASCADE,
    FOREIGN KEY (LIBRARY_ID)    REFERENCES          CASSINI_OBJECT (OBJECT_ID) ON DELETE CASCADE
);

CREATE TABLE PLM_LIBRARYITEM (
    ROWID                       INTEGER             NOT NULL PRIMARY KEY,
    LIBRARY                     INTEGER             NOT NULL,
    ITEM_TYPE                   INTEGER             NOT NULL REFERENCES PLM_ITEMTYPE (TYPE_ID) ON DELETE CASCADE,
    ITEM_ID                     INTEGER             NOT NULL REFERENCES PLM_ITEM (ITEM_ID) ON DELETE CASCADE,
    DATE_ADDED                  TIMESTAMP           NOT NULL
);

CREATE TABLE PLM_SAVEDSEARCH (
    SEARCH_ID                   INTEGER             NOT NULL PRIMARY KEY,
    NAME                        TEXT                NOT NULL,
    DESCRIPTION                 TEXT                ,
    SEARCH_TYPE                 TEXT                ,
    QUERY                       TEXT                NOT NULL,
    OBJECT_TYPE                 OBJECT_TYPE         NOT NULL,
    OWNER                       INTEGER             REFERENCES PERSON (PERSON_ID) ON DELETE CASCADE,
    TYPE                        TEXT                NOT NULL DEFAULT 'PUBLIC',
    FOREIGN KEY (SEARCH_ID)     REFERENCES          CASSINI_OBJECT (OBJECT_ID) ON DELETE CASCADE
);

CREATE TABLE PLM_SHAREDOBJECT (
    SHARE_ID                    INTEGER             NOT NULL PRIMARY KEY,
    SHARED_OBJECT_TYPE          OBJECT_TYPE         NOT NULL,
    OBJECT_ID                   INTEGER             NOT NULL REFERENCES CASSINI_OBJECT (OBJECT_ID) ON DELETE CASCADE,
    SHARE_TYPE                  SHARE_TYPE          NOT NULL,
    SHARED_TO                   INTEGER             NOT NULL,
    SHARED_BY                   INTEGER             NOT NULL REFERENCES PERSON (PERSON_ID),
    PERMISSION                  SHARE_PERMISSION    NOT NULL,
    PARENT_OBJECT_ID            INTEGER             REFERENCES CASSINI_OBJECT (OBJECT_ID) ON DELETE CASCADE,
    PARENT_OBJECT_TYPE          OBJECT_TYPE         ,
    FOREIGN KEY (SHARE_ID)      REFERENCES          CASSINI_OBJECT (OBJECT_ID) ON DELETE CASCADE
);

CREATE TABLE PLM_EVENTS (
    ID                          INTEGER             NOT NULL PRIMARY KEY,
    EVENT                       TEXT                NOT NULL,
    TARGET                      INTEGER             NOT NULL REFERENCES CASSINI_OBJECT (OBJECT_ID) ON DELETE CASCADE,
    SCRIPT                      TEXT                NOT NULL,
    UNIQUE (EVENT, TARGET)
);

CREATE TABLE PLM_RELATIONSHIP (
    ID                          INTEGER             NOT NULL PRIMARY KEY,
    NAME                        TEXT                NOT NULL,
    DESCRIPTION                 TEXT                ,
    FROM_TYPE                   INTEGER             NOT NULL REFERENCES PLM_ITEMTYPE(TYPE_ID) ON DELETE CASCADE,
    TO_TYPE                     INTEGER             NOT NULL REFERENCES PLM_ITEMTYPE(TYPE_ID) ON DELETE CASCADE,
    FOREIGN KEY (ID)            REFERENCES          CASSINI_OBJECT (OBJECT_ID) ON DELETE CASCADE
);

CREATE TABLE PLM_RELATIONSHIPATTRIBUTE (
    ATTRIBUTE_ID                  INTEGER           NOT NULL PRIMARY KEY,
    RELATIONSHIP                  INTEGER           NOT NULL REFERENCES PLM_RELATIONSHIP (ID) ON DELETE CASCADE,
    FOREIGN KEY (ATTRIBUTE_ID)    REFERENCES        OBJECTTYPEATTRIBUTE (ATTRIBUTE_ID) ON DELETE CASCADE
);

CREATE TABLE PLM_RELATEDITEM (
    ID                          INTEGER             NOT NULL PRIMARY KEY,
    FROM_ITEM                   INTEGER             NOT NULL REFERENCES PLM_ITEMREVISION(ITEM_ID) ON DELETE CASCADE,
    TO_ITEM                     INTEGER             NOT NULL REFERENCES PLM_ITEMREVISION(ITEM_ID) ON DELETE CASCADE,
    RELATIONSHIP                INTEGER             NOT NULL REFERENCES PLM_RELATIONSHIP(ID) ON DELETE CASCADE,
    NOTES                       TEXT                ,
    FOREIGN KEY (ID)            REFERENCES          CASSINI_OBJECT(OBJECT_ID) ON DELETE CASCADE
);

CREATE TABLE PLM_RELATEDITEMATTRIBUTE (
    RELATEDITEM                 INTEGER             NOT NULL REFERENCES PLM_RELATEDITEM (ID) ON DELETE CASCADE,
    ATTRIBUTE                   INTEGER             NOT NULL REFERENCES PLM_RELATIONSHIPATTRIBUTE (ATTRIBUTE_ID) ON DELETE CASCADE,
    FOREIGN KEY (RELATEDITEM, ATTRIBUTE)          REFERENCES          OBJECTATTRIBUTE (OBJECT_ID, ATTRIBUTEDEF)  ON DELETE CASCADE,
    UNIQUE (RELATEDITEM, ATTRIBUTE)
);

CREATE TABLE PLM_FILEDOWNLOADHISTORY (
    ID                          INTEGER             NOT NULL PRIMARY KEY,
    FILE_ID                     INTEGER             NOT NULL REFERENCES PLM_FILE(FILE_ID) ON DELETE CASCADE,
    PERSON                      INTEGER             NOT NULL REFERENCES PERSON(PERSON_ID) ON DELETE CASCADE,
    DOWNLOAD_DATE               TIMESTAMP
);

CREATE TABLE PLM_SUBSCRIBE (
  ID                          INTEGER             NOT NULL PRIMARY KEY ,
  OBJECT_ID                   INTEGER             NOT NULL REFERENCES CASSINI_OBJECT (OBJECT_ID) ON DELETE CASCADE,
  OBJECT_TYPE                 TEXT                NOT NULL,
  PERSON                      INTEGER             REFERENCES PERSON(PERSON_ID) ON DELETE CASCADE,
  SUBSCRIBE                   BOOLEAN             DEFAULT TRUE
);

CREATE TABLE EMAIL_TEMPLATE_CONFIGURATION (
  ID                          INTEGER             NOT NULL PRIMARY KEY ,
  TEMPLATE_NAME               TEXT                ,
  TEMPLATE_SOURCECODE         TEXT
);

CREATE TABLE BOM_CONFIGURATION (
  ID                          INTEGER             NOT NULL PRIMARY KEY,
  ITEM                        INTEGER             NOT NULL REFERENCES PLM_ITEMREVISION (ITEM_ID) ON DELETE CASCADE,
  NAME                        TEXT                NOT NULL,
  DESCRIPTION                 TEXT                NOT NULL,
  RULES                       TEXT                DEFAULT NULL,
  FOREIGN KEY (ID)            REFERENCES          CASSINI_OBJECT (OBJECT_ID) ON DELETE CASCADE
);

CREATE TABLE ITEM_CONFIGURABLEATTRIBUTES(
    ID                          BIGINT              NOT NULL PRIMARY KEY,
    ITEM                        INTEGER             NOT NULL REFERENCES PLM_ITEMREVISION(ITEM_ID) ON DELETE CASCADE,
    ATTRIBUTE                   INTEGER             NOT NULL REFERENCES PLM_ITEMTYPEATTRIBUTE(ATTRIBUTE_ID) ON DELETE CASCADE,
    VALUES                      TEXT[]
);

/* Alternate and Substitute Parts */
CREATE TYPE REPLACEMENT_TYPE AS ENUM (
    'ONEWAY',
    'TWOWAY'
);

CREATE TABLE PLM_ALTERNATEPART (
    ID                          INTEGER             NOT NULL PRIMARY KEY,
    PART                        INTEGER             NOT NULL REFERENCES PLM_ITEM (ITEM_ID) ON DELETE CASCADE,
    REPLACEMENT_PART            INTEGER             NOT NULL REFERENCES PLM_ITEM (ITEM_ID) ON DELETE CASCADE,
    DIRECTION                   REPLACEMENT_TYPE    NOT NULL DEFAULT 'ONEWAY',
    MODIFIED_DATE               TIMESTAMP               NOT NULL DEFAULT now()
);

CREATE TABLE PLM_SUBSTITUTEPART (
    ID                          INTEGER             NOT NULL PRIMARY KEY,
    PARENT                      INTEGER             NOT NULL REFERENCES PLM_ITEM (ITEM_ID) ON DELETE CASCADE,
    PART                        INTEGER             NOT NULL REFERENCES PLM_ITEM (ITEM_ID) ON DELETE CASCADE,
    REPLACEMENT_PART            INTEGER             NOT NULL REFERENCES PLM_ITEM (ITEM_ID) ON DELETE CASCADE
);

/* New Parts Request */

CREATE TYPE NPR_STATUS AS ENUM (
    'OPEN',
    'PENDING',
    'HOLD',
    'APPROVED',
    'REJECTED'
);

CREATE TABLE PLM_NPR (
    ID                          INTEGER             NOT NULL PRIMARY KEY,
    NUMBER                      TEXT                NOT NULL,
    REQUESTER                   INTEGER             NOT NULL REFERENCES PERSON (PERSON_ID) ON DELETE SET NULL,
    DESCRIPTION                 TEXT                NOT NULL,
    REASON_FOR_REQUEST          TEXT                NOT NULL,
    STATUS                      NPR_STATUS          NOT NULL DEFAULT 'OPEN',
    REJECT_REASON               TEXT                ,
    NOTES                       TEXT                ,
    FOREIGN KEY (ID)            REFERENCES          CASSINI_OBJECT (OBJECT_ID) ON DELETE CASCADE
);

CREATE TABLE PLM_NPRITEM (
    ID                          INTEGER             NOT NULL PRIMARY KEY,
    NPR                         INTEGER             NOT NULL REFERENCES PLM_NPR (ID) ON DELETE CASCADE,
    NOTES                       TEXT                ,
    ASSIGNED_NUMBER             BOOLEAN             DEFAULT FALSE,
    TEMPORARY_NUMBER            TEXT                ,
    FOREIGN KEY (ID)            REFERENCES          PLM_ITEM (ITEM_ID) ON DELETE SET NULL
);
CREATE TABLE PLM_NPR_FILE (
    ID                          INTEGER             NOT NULL PRIMARY KEY,
    NPR                         INTEGER             NOT NULL REFERENCES PLM_NPR (ID) ON DELETE CASCADE,
    FOREIGN KEY (ID)            REFERENCES          PLM_FILE (FILE_ID) ON DELETE CASCADE
);

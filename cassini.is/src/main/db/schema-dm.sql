/* Begin document management tables */
CREATE TABLE IS_FOLDER (
    FOLDER_ID                   INTEGER             NOT NULL PRIMARY KEY,
    NAME                        TEXT                NOT NULL,
    DESCRIPTION                 TEXT                ,
    FOLDER_TYPE					DOC_TYPE			NOT NULL,
    PARENT_FOLDER               INTEGER             REFERENCES IS_FOLDER (FOLDER_ID) ON DELETE CASCADE,
    IS_LOCKED                   BOOLEAN             NOT NULL DEFAULT FALSE,
    LOCKED_BY                   INTEGER             REFERENCES PERSON (PERSON_ID) ON DELETE CASCADE,
    LOCKED_DATE                 TIMESTAMP           ,
    FOREIGN KEY (FOLDER_ID)     REFERENCES          CASSINI_OBJECT (OBJECT_ID) ON DELETE CASCADE
);

CREATE TABLE IS_BIDFOLDER (
    FOLDER_ID                   INTEGER             NOT NULL PRIMARY KEY,
    BID                         INTEGER             NOT NULL REFERENCES IS_BID (BID_ID) ON DELETE CASCADE,
    FOREIGN KEY (FOLDER_ID)     REFERENCES          IS_FOLDER (FOLDER_ID) ON DELETE CASCADE
);

CREATE TABLE IS_PROJECTFOLDER (
    FOLDER_ID                   INTEGER             NOT NULL PRIMARY KEY,
    PROJECT                     INTEGER             NOT NULL REFERENCES IS_PROJECT (PROJECT_ID) ON DELETE CASCADE,
    FOREIGN KEY (FOLDER_ID)     REFERENCES          IS_FOLDER (FOLDER_ID) ON DELETE CASCADE
);

CREATE TABLE IS_DOCUMENT (
    DOCUMENT_ID                 INTEGER             NOT NULL PRIMARY KEY,
    NAME                        TEXT                NOT NULL,
    DESCRIPTION                 TEXT                ,
    DOCUMENT_TYPE				        DOC_TYPE			NOT NULL,
    FOLDER                      INTEGER             REFERENCES IS_FOLDER (FOLDER_ID) ON DELETE CASCADE,
    SIZE                        BIGINT              NOT NULL,
    VERSION                     INTEGER             NOT NULL DEFAULT 1,
    LATEST                      BOOLEAN             DEFAULT TRUE,
    IS_LOCKED                   BOOLEAN             NOT NULL DEFAULT FALSE,
    LOCKED_BY                   INTEGER             REFERENCES PERSON (PERSON_ID) ON DELETE CASCADE,
    LOCKED_DATE                 TIMESTAMP           ,
    FILE_URN                    TEXT                ,
    THUMBNAIL                   TEXT                ,
    FOREIGN KEY (DOCUMENT_ID)   REFERENCES          CASSINI_OBJECT (OBJECT_ID) ON DELETE CASCADE
);

CREATE TABLE IS_BIDDOCUMENT (
    DOCUMENT_ID                 INTEGER             NOT NULL PRIMARY KEY,
    BID                         INTEGER             NOT NULL REFERENCES IS_BID (BID_ID) ON DELETE CASCADE,
    FOREIGN KEY (DOCUMENT_ID)   REFERENCES          IS_DOCUMENT (DOCUMENT_ID) ON DELETE CASCADE
);


CREATE TABLE IS_PROJECTDOCUMENT (
    DOCUMENT_ID                 INTEGER             NOT NULL PRIMARY KEY,
    PROJECT                     INTEGER             NOT NULL REFERENCES IS_PROJECT (PROJECT_ID) ON DELETE CASCADE,
    FOREIGN KEY (DOCUMENT_ID)   REFERENCES          IS_DOCUMENT (DOCUMENT_ID) ON DELETE CASCADE
);

CREATE TABLE IS_OBJECTPERMISSION(
    ROW_ID                          INTEGER             NOT NULL PRIMARY KEY ,
    OBJECTTYPE                      OBJECT_TYPE         NOT NULL,
    OBJECT_ID                       INTEGER             NOT NULL,
    PERMISSION_LEVEL                PERMISSION_LEVEL    NOT NULL,
    PERMISSION_ASSIGNEDTO           INTEGER             NOT NULL,
    ACTION_TYPES                     TEXT[]              NOT NULL
);

CREATE TABLE IS_TOPFOLDER (
    FOLDER_ID                   INTEGER             NOT NULL PRIMARY KEY,
    NAME                        TEXT                NOT NULL,
    DESCRIPTION                 TEXT                ,
    FOLDER_TYPE					        DOC_TYPE			      NOT NULL,
    PARENT_FOLDER               INTEGER             REFERENCES IS_TOPFOLDER (FOLDER_ID) ON DELETE CASCADE,
    IS_LOCKED                   BOOLEAN             NOT NULL DEFAULT FALSE,
    LOCKED_BY                   INTEGER             REFERENCES PERSON (PERSON_ID) ON DELETE CASCADE,
    LOCKED_DATE                 TIMESTAMP           ,
    FOREIGN KEY (FOLDER_ID)     REFERENCES          CASSINI_OBJECT (OBJECT_ID) ON DELETE CASCADE
);

CREATE TABLE IS_TOPDOCUMENT (
    DOCUMENT_ID                 INTEGER             NOT NULL PRIMARY KEY,
    NAME                        TEXT                NOT NULL,
    DESCRIPTION                 TEXT                ,
    DOCUMENT_TYPE				        DOC_TYPE			      NOT NULL,
    FOLDER                      INTEGER             REFERENCES IS_TOPFOLDER (FOLDER_ID) ON DELETE CASCADE,
    SIZE                        BIGINT              NOT NULL,
    VERSION                     INTEGER             NOT NULL DEFAULT 1,
    LATEST                      BOOLEAN             DEFAULT TRUE,
    IS_LOCKED                   BOOLEAN             NOT NULL DEFAULT FALSE,
    LOCKED_BY                   INTEGER             REFERENCES PERSON (PERSON_ID) ON DELETE CASCADE,
    LOCKED_DATE                 TIMESTAMP           ,
    FILE_URN                    TEXT                ,
    THUMBNAIL                   TEXT                ,
    FOREIGN KEY (DOCUMENT_ID)   REFERENCES          CASSINI_OBJECT (OBJECT_ID) ON DELETE CASCADE
);
/* End document management tables */
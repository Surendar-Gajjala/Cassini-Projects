CREATE TABLE DM_FOLDER (
    FOLDER_ID                   INTEGER             NOT NULL PRIMARY KEY,
    NAME                        TEXT                NOT NULL,
    DESCRIPTION                 TEXT                ,
    PARENT_FOLDER               INTEGER             REFERENCES DM_FOLDER (FOLDER_ID) ON DELETE CASCADE,
    IS_LOCKED                   BOOLEAN             NOT NULL DEFAULT FALSE,
    LOCKED_BY                   INTEGER             REFERENCES PERSON (PERSON_ID),
    LOCKED_DATE                 TIMESTAMP           ,
    FOREIGN KEY (FOLDER_ID)     REFERENCES          CASSINI_OBJECT (OBJECT_ID) ON DELETE CASCADE
);

CREATE TABLE DM_DOCUMENT (
    DOCUMENT_ID                 INTEGER             NOT NULL PRIMARY KEY,
    NAME                        TEXT                NOT NULL,
    DESCRIPTION                 TEXT                ,
    FOLDER                      INTEGER             REFERENCES DM_FOLDER (FOLDER_ID) ON DELETE CASCADE,
    SIZE                        BIGINT              NOT NULL,
    VERSION                     INTEGER             NOT NULL DEFAULT 1,
    LATEST                      BOOLEAN             DEFAULT TRUE,
    IS_LOCKED                   BOOLEAN             NOT NULL DEFAULT FALSE,
    LOCKED_BY                   INTEGER             REFERENCES PERSON (PERSON_ID),
    LOCKED_DATE                 TIMESTAMP           ,
    FOREIGN KEY (DOCUMENT_ID)   REFERENCES          CASSINI_OBJECT (OBJECT_ID) ON DELETE CASCADE
);

CREATE TABLE DM_OBJECTPERMISSION(
    ROW_ID                          INTEGER             NOT NULL PRIMARY KEY ,
    OBJECT_ID                       INTEGER             NOT NULL,
    PERMISSION_ASSIGNEDTO           INTEGER             NOT NULL,
    ACTION_TYPES                    TEXT[]              NOT NULL
);
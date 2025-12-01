/* Project management tables */
CREATE TABLE IS_CUSTOMERTYPE (
    TYPE_ID                     INTEGER             NOT NULL PRIMARY KEY,
    TYPE_CODE                   VARCHAR(10)         NOT NULL,
    NAME                        TEXT                NOT NULL UNIQUE,
    DESCRIPTION                 TEXT
);

CREATE TABLE IS_CUSTOMER (
    CUSTOMER_ID                 INTEGER             NOT NULL PRIMARY KEY,
    CUSTOMER_TYPE               INTEGER             NOT NULL REFERENCES IS_CUSTOMERTYPE (TYPE_ID),
    NAME                        TEXT                NOT NULL,
    DESCRIPTION                 TEXT                ,
    OFFICE_PHONE                TEXT                ,
    OFFICE_FAX                  TEXT                ,
    OFFICE_EMAIL                TEXT                ,
    CONTACT_PERSON              INTEGER             NOT NULL REFERENCES PERSON (PERSON_ID),
    ADDRESS                     INTEGER             NOT NULL REFERENCES ADDRESS (ADDRESS_ID),
    FOREIGN KEY (CUSTOMER_ID)   REFERENCES          CASSINI_OBJECT (OBJECT_ID) ON DELETE CASCADE
);

CREATE TABLE IS_FILE (
    FILE_ID                     INTEGER             NOT NULL PRIMARY KEY,
    NAME                        TEXT                NOT NULL,
    DESCRIPTION                 TEXT                ,
    SIZE                        BIGINT              NOT NULL,
    VERSION                     INTEGER             NOT NULL DEFAULT 1,
    LATEST                      BOOLEAN             DEFAULT TRUE,
    LOCKED                      BOOLEAN             NOT NULL DEFAULT FALSE,
    LOCKED_BY                   INTEGER             REFERENCES PERSON (PERSON_ID) ON DELETE CASCADE,
    LOCKED_DATE                 TIMESTAMP           ,
    FILE_URN                    TEXT                ,
    THUMBNAIL                   TEXT                ,
    FOREIGN KEY (FILE_ID)       REFERENCES          CASSINI_OBJECT (OBJECT_ID) ON DELETE CASCADE
);

CREATE TABLE IS_ORGANIZATION (
    ORGANIZATION_ID             INTEGER             NOT NULL PRIMARY KEY,
    NAME                        TEXT                NOT NULL,
    DESCRIPTION                 TEXT                ,
    FOREIGN KEY (ORGANIZATION_ID)        REFERENCES          CASSINI_OBJECT (OBJECT_ID) ON DELETE CASCADE
);

CREATE TABLE IS_BID (
    BID_ID                      INTEGER             NOT NULL PRIMARY KEY,
    CUSTOMER                    INTEGER             NOT NULL REFERENCES IS_CUSTOMER (CUSTOMER_ID),
    NAME                        TEXT                NOT NULL,
    DESCRIPTION                 TEXT                ,
    STATUS                      TEXT                NOT NULL,
    STATUS_LOV                  INTEGER             NOT NULL REFERENCES LOV (LOV_ID) ON DELETE CASCADE,
    FOREIGN KEY (BID_ID)        REFERENCES          CASSINI_OBJECT (OBJECT_ID) ON DELETE CASCADE
);


CREATE TABLE IS_BIDSTATUSHISTORY (
    ROWID                       INTEGER             NOT NULL PRIMARY KEY,
    BID                         INTEGER             NOT NULL REFERENCES IS_BID (BID_ID) ON DELETE CASCADE,
    MODIFIED_DATE               TIMESTAMP           NOT NULL,
    MODIFIED_BY                 INTEGER             NOT NULL REFERENCES PERSON (PERSON_ID) ON DELETE CASCADE,
    OLD_STATUS                  TEXT                ,
    NEW_STATUS                  TEXT                NOT NULL
);

CREATE TABLE IS_PORTFOLIO (
    PORTFOLIO_ID                INTEGER             NOT NULL PRIMARY KEY,
    NAME                        TEXT                NOT NULL,
    DESCRIPTION                 TEXT                NOT NULL
);

CREATE TABLE IS_PROJECT (
    PROJECT_ID                  INTEGER             NOT NULL PRIMARY KEY,
    NAME                        TEXT                NOT NULL,
    DESCRIPTION                 TEXT                NOT NULL,
    PLANNED_STARTDATE           TIMESTAMP           ,
    PLANNED_FINISHDATE          TIMESTAMP           ,
    ACTUAL_STARTDATE            TIMESTAMP           ,
    ACTUAL_FINISHDATE           TIMESTAMP           ,
    STATUS                      TEXT                ,
    PROJECT_OWNER               INTEGER             NOT NULL REFERENCES PERSON (PERSON_ID) ON DELETE CASCADE,
    PORTFOLIO                   INTEGER             REFERENCES IS_PORTFOLIO (PORTFOLIO_ID) ON DELETE CASCADE,
    LOCKED                      BOOLEAN             DEFAULT FALSE,
    FOREIGN KEY (PROJECT_ID)    REFERENCES          CASSINI_OBJECT (OBJECT_ID) ON DELETE CASCADE
);

CREATE TABLE IS_PROJECTEMAIL (
    ID                          INTEGER             NOT NULL PRIMARY KEY,
    PROJECT                     INTEGER             NOT NULL REFERENCES IS_PROJECT (PROJECT_ID) ON DELETE CASCADE,
    MAILHOST                    TEXT                NOT NULL,
    SENDER_USER                 TEXT                NOT NULL,
    SENDER_PASSWORD             TEXT                NOT NULL,
    ENABLED                     BOOLEAN             NOT NULL DEFAULT TRUE
);

CREATE TABLE IS_PROJECTRESOURCE (
    RESOURCE_ID                 INTEGER               NOT NULL PRIMARY KEY,
    PROJECT                     INTEGER               NOT NULL REFERENCES IS_PROJECT(PROJECT_ID) ON DELETE CASCADE,
    TASK                        INTEGER               NOT NULL,
    REFERENCE_ID                INTEGER               NOT NULL,
    QUANTITY                    DOUBLE PRECISION      NOT NULL DEFAULT 0,
    UNITS                       TEXT                  NOT NULL,
    ISSUED_QUANTITY             DOUBLE PRECISION      NOT NULL DEFAULT 0,
    RESOURCETYPE                RESOURCE_TYPE         NOT NULL,
    UNIQUE (PROJECT,RESOURCE_ID,TASK , RESOURCETYPE),
    FOREIGN KEY (RESOURCE_ID)   REFERENCES            CASSINI_OBJECT (OBJECT_ID) ON DELETE CASCADE
);

CREATE TABLE IS_WBSRESOURCE (
    RESOURCE_ID                 INTEGER               NOT NULL REFERENCES IS_PROJECTRESOURCE(RESOURCE_ID) ON DELETE CASCADE,
    FOREIGN KEY (RESOURCE_ID)   REFERENCES            IS_PROJECTRESOURCE(RESOURCE_ID) ON DELETE CASCADE
);

CREATE TABLE IS_WBS (
    WBS_ID                      INTEGER             NOT NULL PRIMARY KEY,
    WBS_ITEM                    INTEGER                 ,
    PROJECT                     INTEGER             NOT NULL REFERENCES IS_PROJECT (PROJECT_ID) ON DELETE CASCADE,
    PARENT_WBS                  INTEGER             REFERENCES IS_WBS (WBS_ID) ON DELETE CASCADE,
    NAME                        TEXT                NOT NULL,
    DESCRIPTION                 TEXT                     ,
    WEIGHTAGE                   DOUBLE PRECISION    NOT NULL DEFAULT 0,
    DURATION                    INTEGER                 ,
    PREDECESSORS                TEXT                    ,
    PLANNED_START_DATE          TIMESTAMP                ,
    PLANNED_FINISH_DATE         TIMESTAMP                ,
    ACTUAL_START_DATE           TIMESTAMP                ,
    ACTUAL_FINISH_DATE          TIMESTAMP                ,
    PERCENTAGECOMPLETE          DOUBLE PRECISION     NOT NULL DEFAULT 0,
    ASSIGNED_TO                 INTEGER              REFERENCES PERSON (PERSON_ID) ON DELETE CASCADE ,
    FOREIGN KEY (WBS_ID)        REFERENCES          CASSINI_OBJECT (OBJECT_ID) ON DELETE CASCADE
);

CREATE TABLE IS_LINKS (
    LINK_ID                      INTEGER             NOT NULL PRIMARY KEY,
    DEPENDENCY                    TEXT                 ,
    PROJECT                     INTEGER             NOT NULL REFERENCES IS_PROJECT (PROJECT_ID) ON DELETE CASCADE
);

CREATE TABLE IS_BIDWBS (
    WBS_ID                          INTEGER             NOT NULL REFERENCES IS_WBS(WBS_ID) ON DELETE CASCADE,
    BID                             INTEGER             NOT NULL REFERENCES IS_BID(BID_ID) ON DELETE CASCADE,
    FOREIGN KEY (WBS_ID)            REFERENCES          IS_WBS(WBS_ID) ON DELETE CASCADE
);

CREATE TABLE IS_PROJECTWBS (
    WBS_ID                      INTEGER             NOT NULL REFERENCES IS_WBS (WBS_ID) ON DELETE CASCADE,
    PROJECT                     INTEGER             NOT NULL REFERENCES IS_PROJECT(PROJECT_ID) ON DELETE CASCADE,
    FOREIGN KEY (WBS_ID)        REFERENCES          IS_WBS(WBS_ID) ON DELETE CASCADE
);

CREATE TABLE IS_PROJECTSTATUSHISTORY (
    ROWID                       INTEGER             NOT NULL PRIMARY KEY,
    PROJECT                     INTEGER             NOT NULL REFERENCES IS_PROJECT (PROJECT_ID) ON DELETE CASCADE,
    MODIFIED_DATE               TIMESTAMP           NOT NULL,
    MODIFIED_BY                 INTEGER             NOT NULL REFERENCES PERSON (PERSON_ID),
    OLD_STATUS                  TEXT                ,
    NEW_STATUS                  TEXT                NOT NULL
);

CREATE TABLE IS_PROJECTROLE (
    ROWID                       INTEGER             NOT NULL PRIMARY KEY,
    ROLE                        TEXT                NOT NULL,
    DESCRIPTION                 TEXT                NOT NULL,
    PROJECT                     INTEGER             NOT NULL REFERENCES IS_PROJECT (PROJECT_ID) ON DELETE CASCADE,
    UNIQUE (PROJECT,ROLE),
    FOREIGN KEY (ROWID)        REFERENCES           CASSINI_OBJECT (OBJECT_ID) ON DELETE CASCADE
);

CREATE TABLE IS_PROJECTPERSON (
    ROWID                       INTEGER             NOT NULL PRIMARY KEY,
    PROJECT                     INTEGER             NOT NULL REFERENCES IS_PROJECT (PROJECT_ID) ON DELETE CASCADE ,
    PERSON                      INTEGER             NOT NULL REFERENCES PERSON (PERSON_ID) ON DELETE CASCADE ,
    UNIQUE (PROJECT,PERSON)
);

CREATE TABLE IS_PERSONROLE (
    ROWID                       INTEGER             NOT NULL PRIMARY KEY,
    PERSON                      INTEGER             NOT NULL REFERENCES PERSON (PERSON_ID) ON DELETE CASCADE,
    PROJECT                     INTEGER             NOT NULL REFERENCES IS_PROJECT (PROJECT_ID) ON DELETE CASCADE ,
    ROLE                        INTEGER             NOT NULL REFERENCES  IS_PROJECTROLE (ROWID) ON DELETE CASCADE,
    UNIQUE (PERSON,ROLE)
);

CREATE TABLE IS_PROJECTSITE (
    SITE_ID                     INTEGER             NOT NULL PRIMARY KEY,
    NAME                        TEXT                NOT NULL,
    DESCRIPTION                 TEXT                NOT NULL,
    PROJECT                     INTEGER             NOT NULL REFERENCES IS_PROJECT (PROJECT_ID) ON DELETE CASCADE,
    UNIQUE (PROJECT, NAME),
    FOREIGN KEY (SITE_ID) REFERENCES LOCATIONAWAREOBJECT (OBJECT_ID) ON DELETE CASCADE
);

CREATE TABLE PROJECT_PREFERENCE (
    ID                          INTEGER             NOT NULL PRIMARY KEY,
    PROJECT                     INTEGER             NOT NULL REFERENCES IS_PROJECT (PROJECT_ID) ON DELETE CASCADE,
    FOREIGN KEY (ID)            REFERENCES          PREFERENCE (ID) ON DELETE CASCADE
);
CREATE TABLE IS_PROJECTPERSON_ORGANIZATION (
  ROWID                       INTEGER             NOT NULL PRIMARY KEY,
  PROJECT                     INTEGER             NOT NULL REFERENCES IS_PROJECT (PROJECT_ID) ON DELETE CASCADE ,
  PERSON                      INTEGER             REFERENCES PERSON (PERSON_ID) ON DELETE CASCADE ,
  PROJECTROLE                 INTEGER             REFERENCES IS_PROJECTROLE (ROWID) ON DELETE CASCADE ,
  NODE                        INTEGER             ,
  PARENT                      INTEGER             ,
  NODE_NAME                   TEXT
);

CREATE TABLE IS_PROJECT_PERSONROLE (
    ROWID                       INTEGER             NOT NULL PRIMARY KEY,
    PERSON                      INTEGER             NOT NULL REFERENCES PERSON (PERSON_ID) ON DELETE CASCADE,
    PROJECT                     INTEGER             NOT NULL REFERENCES IS_PROJECT (PROJECT_ID) ON DELETE CASCADE ,
    ROLE                        INTEGER             NOT NULL REFERENCES  IS_PROJECTROLE (ROWID) ON DELETE CASCADE,
    UNIQUE (PERSON,ROLE)
);
CREATE TABLE IS_PROJECTBOQATTACHMENT (
   ID                    INTEGER             NOT NULL PRIMARY KEY,
   PROJECT               INTEGER             NOT NULL REFERENCES IS_PROJECT (PROJECT_ID) ON DELETE CASCADE,
   NAME                  TEXT                NOT NULL,
   SIZE                  BIGINT              NOT NULL,
   DESCRIPTION           TEXT,
  FOREIGN KEY (ID) REFERENCES CASSINI_OBJECT (OBJECT_ID) ON DELETE CASCADE
);
/* End project management tables */
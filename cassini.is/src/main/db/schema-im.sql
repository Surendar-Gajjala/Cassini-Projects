/* Begin issue management tables */
CREATE TABLE IS_ISSUETYPE (
    TYPE_ID                     INTEGER             NOT NULL PRIMARY KEY,
    LABEL                       TEXT                NOT NULL,
    DESCRIPTION                 TEXT                NOT NULL
);

/* Issue can be attached to any object like bid, project, task, etc. */
CREATE TABLE IS_ISSUE (
    ISSUE_ID                    INTEGER             NOT NULL PRIMARY KEY,
    TARGET_OBJECT_TYPE          OBJECT_TYPE         NOT NULL,
    TARGET_OBJECT_ID            INTEGER             NOT NULL,
    TYPE                        INTEGER             REFERENCES IS_ISSUETYPE (TYPE_ID) ON DELETE CASCADE,
    PRIORITY                    ISSUE_PRIORITY      NOT NULL,
    TITLE                       TEXT                NOT NULL,
    DESCRIPTION                 TEXT                ,
    STATUS                      ISSUE_STATUS        NOT NULL,
    TASK                        INTEGER             REFERENCES IS_TASK (TASK_ID) ON DELETE CASCADE NULL,
    ASSIGNED_TO                 INTEGER             REFERENCES PERSON (PERSON_ID) ON DELETE CASCADE,
    RESOLUTION                  TEXT                ,
    FOREIGN KEY (ISSUE_ID)      REFERENCES          CASSINI_OBJECT (OBJECT_ID) ON DELETE CASCADE
);

CREATE TABLE IS_ISSUESTATUSHISTORY (
  	ROWID                       INTEGER             NOT NULL PRIMARY KEY,
    ISSUE                       INTEGER             NOT NULL REFERENCES IS_ISSUE (ISSUE_ID),
    MODIFIED_DATE               TIMESTAMP           NOT NULL,
    MODIFIED_BY                 INTEGER             NOT NULL REFERENCES PERSON (PERSON_ID),
    OLD_STATUS                  ISSUE_STATUS        ,
    NEW_STATUS                  ISSUE_STATUS        NOT NULL
);

/* End issue management tables */
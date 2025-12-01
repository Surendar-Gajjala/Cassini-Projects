/* Begin task management tables */
CREATE TABLE IS_TASK (
    TASK_ID                     INTEGER             NOT NULL PRIMARY KEY,
    SITE                        INTEGER             NOT NULL REFERENCES IS_PROJECTSITE (SITE_ID) ON DELETE CASCADE,
    PERSON                      INTEGER             REFERENCES PERSON (PERSON_ID) ON DELETE CASCADE ,
    NAME                        TEXT                NOT NULL,
    DESCRIPTION                 TEXT                ,
    PLANNED_STARTDATE           DATE                NOT NULL,
    PLANNED_FINISHDATE          DATE                NOT NULL,
    ACTUAL_STARTDATE            DATE                ,
    ACTUAL_FINISHDATE           DATE                ,
    STATUS                      TASK_STATUS         NOT NULL,
    PERCENT_COMPLETE            DOUBLE PRECISION    NOT NULL DEFAULT 0,
    WBS_ITEM                    INTEGER             REFERENCES IS_WBS(WBS_ID) ON DELETE SET NULL,
    UNIT_OF_WORK                TEXT                ,
    TOTAL_UNITS                 DOUBLE PRECISION    NOT NULL DEFAULT 0,
    INSPECTED_BY                INTEGER             REFERENCES PERSON (PERSON_ID) ON DELETE CASCADE,
    SUBCONTRACT                 BOOLEAN             NOT NULL DEFAULT FALSE,
    INSPECTED_ON                DATE                ,
    INSPECTION_RESULT           INSPECTION_RESULT   ,
    INSPECTION_REMARKS          TEXT                ,
    WORKFLOW_STATUS             TEXT                ,
    FOREIGN KEY (TASK_ID)       REFERENCES          CASSINI_OBJECT (OBJECT_ID) ON DELETE CASCADE
);

CREATE TABLE IS_TASKFILES (
  ROWID                       INTEGER             NOT NULL PRIMARY KEY,
  TASK                        INTEGER             NOT NULL REFERENCES IS_TASK (TASK_ID) ON DELETE CASCADE ,
  TYPE                        ATTACHMENT_TYPE    NOT NULL,
  REFITEM                     INTEGER             NOT NULL
);

CREATE TABLE IS_BIDTASK (
    TASK_ID                     INTEGER             NOT NULL PRIMARY KEY,
    BID                         INTEGER             NOT NULL REFERENCES IS_BID (BID_ID) ON DELETE CASCADE ,
    FOREIGN KEY (TASK_ID)       REFERENCES          IS_TASK (TASK_ID)
);

CREATE TABLE IS_PROJECTTASK (
    TASK_ID                     INTEGER             NOT NULL PRIMARY KEY,
    PROJECT                     INTEGER             NOT NULL REFERENCES IS_PROJECT (PROJECT_ID) ON DELETE CASCADE ,
    FOREIGN KEY (TASK_ID)       REFERENCES          IS_TASK (TASK_ID)
);

CREATE TABLE IS_TASKFILE (
    FILE_ID                     INTEGER             NOT NULL PRIMARY KEY,
    TASK_ID                     INTEGER             NOT NULL REFERENCES IS_PROJECTTASK (TASK_ID) ON DELETE CASCADE,
    FOREIGN KEY (FILE_ID)       REFERENCES          IS_FILE (FILE_ID) ON DELETE CASCADE
);

CREATE TABLE IS_TASKASSIGNEDTO (
    TASK                        INTEGER             NOT NULL REFERENCES IS_TASK (TASK_ID) ON DELETE CASCADE ,
    PERSON                      INTEGER             NOT NULL REFERENCES PERSON (PERSON_ID) ON DELETE CASCADE ,
    UNIQUE (TASK, PERSON)
);

CREATE TABLE IS_TASKOBSERVER (
    TASK                        INTEGER             NOT NULL REFERENCES IS_TASK (TASK_ID) ON DELETE CASCADE ,
    PERSON                      INTEGER             NOT NULL REFERENCES PERSON (PERSON_ID) ON DELETE CASCADE ,
    UNIQUE(TASK, PERSON)
);

CREATE TABLE IS_TASKSTATUSHISTORY (
    ROWID                       INTEGER             NOT NULL PRIMARY KEY,
    TASK                        INTEGER             NOT NULL REFERENCES IS_TASK (TASK_ID) ON DELETE CASCADE ,
    MODIFIED_DATE               TIMESTAMP           NOT NULL,
    MODIFIED_BY                 INTEGER             NOT NULL REFERENCES PERSON (PERSON_ID) ON DELETE CASCADE ,
    OLD_STATUS                  TEXT                ,
    NEW_STATUS                  TEXT                NOT NULL
);


CREATE TABLE IS_TASKRESOURCE (
  ROWID                       INTEGER             NOT NULL PRIMARY KEY,
  TASK                        INTEGER             NOT NULL REFERENCES IS_TASK (TASK_ID) ON DELETE CASCADE ,
  RESOURCETYPE                RESOURCE_TYPE       NOT NULL ,
  UNITS                       TEXT                NOT NULL ,
  QUANTITY                    DOUBLE PRECISION    NOT NULL DEFAULT 0,
  RESOURCE_ID                 INTEGER             NOT NULL

);
/* End task management tables */

CREATE TABLE IS_TASKCOMPLETIONHISTORY(
  ID                           INTEGER            NOT NULL PRIMARY KEY,
  TASK                         INTEGER            NOT NULL REFERENCES IS_TASK (TASK_ID) ON DELETE CASCADE,
  TIMESTAMP                    TIMESTAMP          NOT NULL,
  COMPLETION                   DOUBLE PRECISION   NOT NULL DEFAULT 0,
  UNITS_COMPLETED              DOUBLE PRECISION   NOT NULL DEFAULT 0,
  COMPLETED_BY                 INTEGER             NOT NULL REFERENCES PERSON(PERSON_ID) ON DELETE CASCADE ,
  NOTES                        TEXT

);

CREATE TABLE IS_PROJECTTASKIMAGE (
  ID                          INTEGER             NOT NULL PRIMARY KEY,
  TASK                        INTEGER             NOT NULL REFERENCES IS_PROJECTTASK(TASK_ID) ON DELETE CASCADE,
  IMAGE                       BYTEA               NOT NULL,
  IMAGE_NAME                  TEXT                ,
  IMAGE_SIZE                  BIGINT              ,
  UPLOAD_DATE                 TIMESTAMP
);


CREATE TABLE IS_TASKCOMPLETIONRESOURCE (
  ID                           INTEGER            NOT NULL PRIMARY KEY,
  TASK                         INTEGER            NOT NULL REFERENCES IS_PROJECTTASK(TASK_ID) ON DELETE CASCADE,
  TASK_HISTORY                 INTEGER            NOT NULL REFERENCES IS_TASKCOMPLETIONHISTORY(ID) ON DELETE CASCADE,
  QUANTITY                     INTEGER            ,
  RESOUTCE_TYPE                TEXT               ,
  RESOUTCE_ID                  INTEGER
)
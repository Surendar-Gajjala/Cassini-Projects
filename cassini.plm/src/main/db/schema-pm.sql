CREATE TYPE PROJECTACTIVITY_STATUS AS ENUM (
    'PENDING',
    'INPROGRESS',
    'FINISHED'
);

CREATE TYPE PROJECTTASK_STATUS AS ENUM (
    'PENDING',
    'INPROGRESS',
    'FINISHED'
);

CREATE TYPE PM_TYPE AS ENUM (
    'PROGRAM',
    'PROJECT',
    'PROJECTPHASEELEMENT',
    'PROJECTACTIVITY',
    'PROJECTTASK',
    'PROJECTMILESTONE'
);

CREATE TYPE PROGRAM_RESOURCE_TYPE AS ENUM (
    'PERSON'
);

CREATE TYPE PROGRAM_GROUP_TYPE AS ENUM (
    'GROUP',
    'PROJECT'
);

CREATE TABLE PLM_PMOBJECTTYPE(
    ID                          INTEGER             NOT NULL PRIMARY KEY,
    PARENT                      INTEGER             REFERENCES PLM_PMOBJECTTYPE (ID) ON DELETE CASCADE,
    NAME                        TEXT                NOT NULL,
    DESCRIPTION                 TEXT                ,
    NUMBER_SOURCE               INTEGER             REFERENCES AUTONUMBER (AUTONUMBER_ID) ON DELETE CASCADE,
    TYPE                        PM_TYPE             ,
    TABS                        TEXT[]              ,
    FOREIGN KEY (ID)            REFERENCES          CASSINI_OBJECT (OBJECT_ID) ON DELETE CASCADE
);
CREATE TABLE PLM_PMOBJECTTYPEATTRIBUTE(
    ID                          INTEGER             NOT NULL PRIMARY KEY,
    TYPE                        INTEGER             NOT NULL REFERENCES PLM_PMOBJECTTYPE(ID) ON DELETE CASCADE,
    SEQUENCE                    INTEGER             NOT NULL DEFAULT 1,
    FOREIGN KEY (ID)            REFERENCES          OBJECTTYPEATTRIBUTE (ATTRIBUTE_ID) ON DELETE CASCADE
);

CREATE TABLE PLM_PROGRAM (
    ID                          INTEGER             NOT NULL PRIMARY KEY,
    NAME                        TEXT                NOT NULL,
    TYPE                        INTEGER             REFERENCES PLM_PMOBJECTTYPE (ID) ON DELETE CASCADE,
    DESCRIPTION                 TEXT                ,
    PROGRAM_MANAGER             INTEGER             NOT NULL REFERENCES PERSON (PERSON_ID),
    WORKFLOW                    INTEGER             REFERENCES PLM_WORKFLOW (ID) ON DELETE SET NULL,
    FOREIGN KEY (ID)            REFERENCES          CASSINI_OBJECT (OBJECT_ID) ON DELETE CASCADE
);

CREATE TABLE PLM_PROJECT (
    ID                          INTEGER             NOT NULL PRIMARY KEY,
    NAME                        TEXT                NOT NULL,
    TYPE                        INTEGER             REFERENCES PLM_PMOBJECTTYPE (ID) ON DELETE CASCADE,
    DESCRIPTION                 TEXT                ,
    PLANNED_STARTDATE           TIMESTAMP           ,
    PLANNED_FINISHDATE          TIMESTAMP           ,
    ACTUAL_STARTDATE            TIMESTAMP           ,
    ACTUAL_FINISHDATE           TIMESTAMP           ,
    PROJECT_MANAGER             INTEGER             NOT NULL REFERENCES PERSON (PERSON_ID),
    WORKFLOW                    INTEGER             REFERENCES PLM_WORKFLOW (ID) ON DELETE SET NULL,
    MAKE_CONVERSATION_PRIVATE   BOOLEAN             NOT NULL DEFAULT FALSE,
    PROGRAM                     INTEGER             REFERENCES PLM_PROGRAM(ID) ON DELETE CASCADE,
    FOREIGN KEY (ID)            REFERENCES          CASSINI_OBJECT (OBJECT_ID) ON DELETE CASCADE
);

CREATE TABLE PLM_PROJECTMEMBER (
    ID                          INTEGER             NOT NULL PRIMARY KEY,
    PROJECT                     INTEGER             NOT NULL REFERENCES PLM_PROJECT (ID) ON DELETE CASCADE,
    PERSON                      INTEGER             NOT NULL REFERENCES PERSON (PERSON_ID) ON DELETE CASCADE ,
    ROLE                        TEXT                ,
    UNIQUE (PROJECT,PERSON)
);

CREATE TABLE PLM_PROGRAM_RESOURCE (
    ID                          INTEGER             NOT NULL PRIMARY KEY,
    PROGRAM                     INTEGER             NOT NULL REFERENCES PLM_PROGRAM (ID) ON DELETE CASCADE,
    PERSON                      INTEGER             REFERENCES PERSON (PERSON_ID) ON DELETE CASCADE,
    TYPE                        PROGRAM_RESOURCE_TYPE       NOT NULL DEFAULT 'PERSON',
    ROLE                        TEXT
);

CREATE TABLE PLM_PROGRAM_PROJECT (
    ID                          INTEGER             NOT NULL PRIMARY KEY,
    PROGRAM                     INTEGER             NOT NULL REFERENCES PLM_PROGRAM(ID) ON DELETE CASCADE,
    NAME                        TEXT                ,
    DESCRIPTION                 TEXT                ,
    PARENT                      INTEGER             REFERENCES PLM_PROGRAM_PROJECT(ID) ON DELETE CASCADE,
    PROJECT                     INTEGER             REFERENCES PLM_PROJECT(ID) ON DELETE CASCADE,
    TYPE                        PROGRAM_GROUP_TYPE  NOT NULL DEFAULT 'GROUP'
);

CREATE TABLE PLM_PROGRAM_FILE(
    ID                          INTEGER             NOT NULL PRIMARY KEY,
    PROGRAM                     INTEGER             NOT NULL REFERENCES PLM_PROGRAM(ID) ON DELETE CASCADE,
    FOREIGN KEY (ID)            REFERENCES          PLM_FILE(FILE_ID) ON DELETE CASCADE
);

CREATE TABLE PROGRAM_TEMPLATE (
    ID                          INTEGER             NOT NULL PRIMARY KEY,
    NAME                        TEXT                NOT NULL,
    DESCRIPTION                 TEXT                ,
    FOREIGN KEY (ID)            REFERENCES          CASSINI_OBJECT (OBJECT_ID) ON DELETE CASCADE
);

CREATE TABLE PROGRAM_TEMPLATE_RESOURCE (
    ID                          INTEGER             NOT NULL PRIMARY KEY,
    TEMPLATE                    INTEGER             NOT NULL REFERENCES PROGRAM_TEMPLATE (ID) ON DELETE CASCADE,
    PERSON                      INTEGER             REFERENCES PERSON (PERSON_ID) ON DELETE CASCADE,
    TYPE                        PROGRAM_RESOURCE_TYPE       NOT NULL DEFAULT 'PERSON',
    ROLE                        TEXT
);

CREATE TABLE PROGRAM_TEMPLATE_PROJECT (
    ID                          INTEGER             NOT NULL PRIMARY KEY,
    TEMPLATE                    INTEGER             NOT NULL REFERENCES PROGRAM_TEMPLATE(ID) ON DELETE CASCADE,
    NAME                        TEXT                ,
    DESCRIPTION                 TEXT                ,
    PARENT                      INTEGER             REFERENCES PROGRAM_TEMPLATE_PROJECT(ID) ON DELETE CASCADE,
--     PROJECT_TEMPLATE            INTEGER             REFERENCES PROJECT_TEMPLATE(ID) ON DELETE CASCADE,
    TYPE                        PROGRAM_GROUP_TYPE  NOT NULL DEFAULT 'GROUP'
);

CREATE TABLE PROGRAM_TEMPLATE_FILE(
    ID                          INTEGER             NOT NULL PRIMARY KEY,
    TEMPLATE                    INTEGER             NOT NULL REFERENCES PROGRAM_TEMPLATE(ID) ON DELETE CASCADE,
    FOREIGN KEY (ID)            REFERENCES          PLM_FILE(FILE_ID) ON DELETE CASCADE
);

CREATE TABLE PLM_WBSELEMENT (
    ID                          INTEGER             NOT NULL PRIMARY KEY,
    PROJECT                     INTEGER             NOT NULL REFERENCES PLM_PROJECT (ID) ON DELETE CASCADE,
    PARENT                      INTEGER             REFERENCES PLM_WBSELEMENT (ID) ON DELETE CASCADE,
    NAME                        TEXT                NOT NULL,
    DESCRIPTION                 TEXT                ,
    PLANNED_STARTDATE           TIMESTAMP           ,
    PLANNED_FINISHDATE          TIMESTAMP           ,
    SEQUENCE_NUMBER             INTEGER             ,
    FOREIGN KEY (ID)            REFERENCES          CASSINI_OBJECT (OBJECT_ID) ON DELETE CASCADE
);

CREATE TABLE PLM_ACTIVITY (
    ID                          INTEGER             NOT NULL PRIMARY KEY,
    WBS                         INTEGER             NOT NULL REFERENCES PLM_WBSELEMENT (ID) ON DELETE CASCADE,
    TYPE                        INTEGER             REFERENCES PLM_PMOBJECTTYPE (ID) ON DELETE CASCADE,
    NAME                        TEXT                NOT NULL,
    DESCRIPTION                 TEXT                ,
    DURATION                    INTEGER             ,
    SEQUENCE_NUMBER             INTEGER             ,
    PREDECESSORS                INTEGER[]           ,
    PLANNED_STARTDATE           TIMESTAMP           ,
    PLANNED_FINISHDATE          TIMESTAMP           ,
    ACTUAL_STARTDATE            TIMESTAMP           ,
    ACTUAL_FINISHDATE           TIMESTAMP           ,
    ASSIGNED_TO                 INTEGER             REFERENCES PERSON (PERSON_ID),
    PERCENT_COMPLETE            DOUBLE PRECISION    DEFAULT 0,
    STATUS                      PROJECTACTIVITY_STATUS      DEFAULT 'PENDING',
    WORKFLOW                    INTEGER             REFERENCES PLM_WORKFLOW (ID) ON DELETE SET NULL,
    FOREIGN KEY (ID)            REFERENCES          CASSINI_OBJECT (OBJECT_ID) ON DELETE CASCADE
);

CREATE TABLE PLM_MILESTONE (
    ID                          INTEGER             NOT NULL PRIMARY KEY,
    WBS                         INTEGER             NOT NULL REFERENCES PLM_WBSELEMENT (ID) ON DELETE CASCADE,
    NAME                        TEXT                NOT NULL,
    DESCRIPTION                 TEXT                ,
    SEQUENCE_NUMBER             INTEGER             ,
    PREDECESSORS                INTEGER[]           ,
    PLANNED_FINISHDATE          TIMESTAMP           ,
    ACTUAL_FINISHDATE           TIMESTAMP           ,
    ASSIGNED_TO                 INTEGER             REFERENCES PERSON (PERSON_ID),
    STATUS                      PROJECTACTIVITY_STATUS     DEFAULT 'PENDING',
    FOREIGN KEY (ID)            REFERENCES          CASSINI_OBJECT (OBJECT_ID) ON DELETE CASCADE
);

CREATE TABLE PLM_TASK (
    ID                          INTEGER             NOT NULL PRIMARY KEY,
    ACTIVITY                    INTEGER             NOT NULL REFERENCES PLM_ACTIVITY (ID) ON DELETE CASCADE,
    TYPE                        INTEGER             REFERENCES PLM_PMOBJECTTYPE (ID) ON DELETE CASCADE,
    NAME                        TEXT                NOT NULL,
    DESCRIPTION                 TEXT                ,
    DURATION                    INTEGER             ,
    PLANNED_STARTDATE           TIMESTAMP           ,
    PLANNED_FINISHDATE          TIMESTAMP           ,
    ACTUAL_STARTDATE            TIMESTAMP           ,
    ACTUAL_FINISHDATE           TIMESTAMP           ,
    REQUIRED                    BOOLEAN             NOT NULL DEFAULT FALSE,
    ASSIGNED_TO                 INTEGER             REFERENCES PERSON (PERSON_ID),
    PERCENT_COMPLETE            DOUBLE PRECISION    NOT NULL DEFAULT 0,
    STATUS                      PROJECTTASK_STATUS  NOT NULL DEFAULT 'PENDING',
    SEQUENCE_NUMBER             INTEGER             ,
    WORKFLOW                    INTEGER             REFERENCES PLM_WORKFLOW (ID) ON DELETE SET NULL,
    FOREIGN KEY (ID)            REFERENCES          CASSINI_OBJECT (OBJECT_ID) ON DELETE CASCADE
);

CREATE TYPE DELIVERABLE_STATUS AS ENUM (
    'PENDING',
    'FINISHED'
);

CREATE TABLE PLM_DELIVERABLE (
    ID                          INTEGER             NOT NULL PRIMARY KEY,
    ITEM                        INTEGER             NOT NULL REFERENCES PLM_ITEMREVISION (ITEM_ID) ON DELETE CASCADE,
    DELIVARY_STATUS             DELIVERABLE_STATUS  NOT NULL DEFAULT 'PENDING',
    CRITERIA                    TEXT                ,
    SCRIPT                      TEXT
);

CREATE TABLE PLM_PROJECTDELIVERABLE (
    ID                          INTEGER             NOT NULL PRIMARY KEY,
    PROJECT                     INTEGER             NOT NULL REFERENCES PLM_PROJECT (ID) ON DELETE CASCADE,
    FOREIGN KEY (ID)            REFERENCES          PLM_DELIVERABLE (ID) ON DELETE CASCADE
);

CREATE TABLE PLM_ACTIVITYDELIVERABLE (
    ID                          INTEGER             NOT NULL PRIMARY KEY,
    ACTIVITY                    INTEGER             NOT NULL REFERENCES PLM_ACTIVITY (ID) ON DELETE CASCADE,
    FOREIGN KEY (ID)            REFERENCES          PLM_DELIVERABLE (ID) ON DELETE CASCADE
);

CREATE TABLE PLM_TASKDELIVERABLE (
    ID                          INTEGER             NOT NULL PRIMARY KEY,
    TASK                        INTEGER             NOT NULL REFERENCES PLM_TASK (ID) ON DELETE CASCADE,
    FOREIGN KEY (ID)            REFERENCES          PLM_DELIVERABLE (ID) ON DELETE CASCADE
);

CREATE TABLE PLM_PROJECTFILE (
    FILE_ID                     INTEGER             NOT NULL PRIMARY KEY,
    PROJECT                     INTEGER             NOT NULL REFERENCES PLM_PROJECT (ID) ON DELETE CASCADE,
    FOREIGN KEY (FILE_ID)       REFERENCES          PLM_FILE (FILE_ID) ON DELETE CASCADE
);

CREATE TABLE PLM_ACTIVITYFILE (
    FILE_ID                     INTEGER             NOT NULL PRIMARY KEY,
    ACTIVITY                    INTEGER             NOT NULL REFERENCES PLM_ACTIVITY (ID) ON DELETE CASCADE,
    FOREIGN KEY (FILE_ID)       REFERENCES          PLM_FILE (FILE_ID) ON DELETE CASCADE
);

CREATE TABLE PLM_TASKFILE (
    FILE_ID                     INTEGER             NOT NULL PRIMARY KEY,
    TASK                        INTEGER             NOT NULL REFERENCES PLM_TASK (ID) ON DELETE CASCADE,
    FOREIGN KEY (FILE_ID)       REFERENCES          PLM_FILE (FILE_ID) ON DELETE CASCADE
);

CREATE TABLE PLM_PROJECTITEMREFERENCE(
    ID                          INTEGER             NOT NULL PRIMARY KEY,
    PROJECT                     INTEGER             NOT NULL REFERENCES PLM_PROJECT(ID) ON DELETE CASCADE,
    ITEM                        INTEGER             NOT NULL REFERENCES PLM_ITEMREVISION(ITEM_ID) ON DELETE CASCADE
);

CREATE TABLE PLM_ACTIVITYITEMREFERENCE(
    ID                          INTEGER             NOT NULL PRIMARY KEY,
    ACTIVITY                    INTEGER             NOT NULL REFERENCES PLM_ACTIVITY(ID) ON DELETE CASCADE,
    ITEM                        INTEGER             NOT NULL REFERENCES PLM_ITEMREVISION(ITEM_ID) ON DELETE CASCADE
);

CREATE TABLE PLM_TASKITEMREFERENCE(
    ID                          INTEGER             NOT NULL PRIMARY KEY,
    TASK                        INTEGER             NOT NULL REFERENCES PLM_TASK(ID) ON DELETE CASCADE,
    ITEM                        INTEGER             NOT NULL REFERENCES PLM_ITEMREVISION(ITEM_ID) ON DELETE CASCADE
);

CREATE TABLE PROJECT_TEMPLATE(
    ID                         INTEGER             NOT NULL PRIMARY KEY,
    NAME                       TEXT                NOT NULL,
    DESCRIPTION                TEXT                ,
    PROGRAM_TEMPLATE           INTEGER             REFERENCES PROGRAM_TEMPLATE(ID) ON DELETE CASCADE,
    MANAGER                    INTEGER             REFERENCES PERSON(PERSON_ID) ON DELETE SET NULL,
    FOREIGN KEY (ID)           REFERENCES          CASSINI_OBJECT (OBJECT_ID) ON DELETE CASCADE
);

ALTER TABLE PROGRAM_TEMPLATE_PROJECT ADD COLUMN PROJECT_TEMPLATE            INTEGER             REFERENCES PROJECT_TEMPLATE(ID) ON DELETE CASCADE;

CREATE TABLE PROJECT_TEMPLATEWBS(
    ID                         INTEGER             NOT NULL PRIMARY KEY,
    NAME                       TEXT                NOT NULL,
    DESCRIPTION                TEXT                ,
    SEQUENCE_NUMBER             INTEGER             ,
    TEMPLATE                   INTEGER             NOT NULL REFERENCES PROJECT_TEMPLATE (ID) ON DELETE CASCADE,
    FOREIGN KEY (ID)           REFERENCES          CASSINI_OBJECT (OBJECT_ID) ON DELETE CASCADE
);

CREATE TABLE PROJECT_TEMPLATEACTIVITY(
    ID                         INTEGER             NOT NULL PRIMARY KEY,
    NAME                       TEXT                NOT NULL,
    DESCRIPTION                TEXT                ,
    SEQUENCE_NUMBER             INTEGER             ,
    WBS                        INTEGER             NOT NULL REFERENCES PROJECT_TEMPLATEWBS (ID) ON DELETE CASCADE,
    ASSIGNED_TO                INTEGER             REFERENCES PERSON (PERSON_ID),
    FOREIGN KEY (ID)           REFERENCES          CASSINI_OBJECT (OBJECT_ID) ON DELETE CASCADE
);

CREATE TABLE PROJECT_TEMPLATEMILESTONE(
    ID                         INTEGER             NOT NULL PRIMARY KEY,
    NAME                       TEXT                NOT NULL,
    DESCRIPTION                TEXT                ,
    SEQUENCE_NUMBER             INTEGER             ,
    WBS                        INTEGER             NOT NULL REFERENCES PROJECT_TEMPLATEWBS (ID) ON DELETE CASCADE,
    ASSIGNED_TO                INTEGER             REFERENCES PERSON (PERSON_ID),
    FOREIGN KEY (ID)           REFERENCES          CASSINI_OBJECT (OBJECT_ID) ON DELETE CASCADE
);
CREATE TABLE PROJECT_TEMPLATETASK (
    ID                          INTEGER             NOT NULL PRIMARY KEY,
    ACTIVITY                    INTEGER             NOT NULL REFERENCES PROJECT_TEMPLATEACTIVITY (ID) ON DELETE CASCADE,
    NAME                        TEXT                NOT NULL,
    DESCRIPTION                 TEXT                ,
    SEQUENCE_NUMBER             INTEGER             ,
    ASSIGNED_TO                 INTEGER             REFERENCES PERSON (PERSON_ID),
    FOREIGN KEY (ID)            REFERENCES          CASSINI_OBJECT (OBJECT_ID) ON DELETE CASCADE
);

CREATE TABLE PROJECT_TEMPLATE_FILE(
    ID                          INTEGER             NOT NULL PRIMARY KEY,
    TEMPLATE                    INTEGER             NOT NULL REFERENCES PROJECT_TEMPLATE(ID) ON DELETE CASCADE,
    FOREIGN KEY (ID)            REFERENCES          PLM_FILE(FILE_ID) ON DELETE CASCADE
);

CREATE TABLE PROJECT_TEMPLATEACTIVITY_FILE(
    ID                          INTEGER             NOT NULL PRIMARY KEY,
    ACTIVITY                    INTEGER             NOT NULL REFERENCES PROJECT_TEMPLATEACTIVITY(ID) ON DELETE CASCADE,
    FOREIGN KEY (ID)            REFERENCES          PLM_FILE(FILE_ID) ON DELETE CASCADE
);

CREATE TABLE PROJECT_TEMPLATETASK_FILE(
    ID                          INTEGER             NOT NULL PRIMARY KEY,
    TASK                        INTEGER             NOT NULL REFERENCES PROJECT_TEMPLATETASK(ID) ON DELETE CASCADE,
    FOREIGN KEY (ID)            REFERENCES          PLM_FILE(FILE_ID) ON DELETE CASCADE
);

CREATE TABLE PROJECT_EMAILSETTINGS (
    PROJECT                    INTEGER             NOT NULL PRIMARY KEY REFERENCES PLM_PROJECT (ID) ON DELETE CASCADE,
    USERNAME                    TEXT                NOT NULL,
    EMAIL                       TEXT                NOT NULL,
    PASSWORD                    TEXT                NOT NULL,
    IMAP_SERVER                 TEXT                NOT NULL,
    IMAP_PORT                   INTEGER             NOT NULL DEFAULT 993, /* IMAP Server */
    SMTP_SERVER                 TEXT                NOT NULL,
    SMTP_PORT                   INTEGER             NOT NULL DEFAULT 587 /* SMTP Server */
);

CREATE TABLE PLM_LINKS (
  LINK_ID                      INTEGER             NOT NULL PRIMARY KEY,
  DEPENDENCY                    TEXT                 ,
  PROJECT                     INTEGER             NOT NULL REFERENCES PLM_PROJECT (ID) ON DELETE CASCADE
);

CREATE TABLE PROJECT_TEMPLATEMEMBER (
    ID                          INTEGER             NOT NULL PRIMARY KEY,
    TEMPLATE                    INTEGER             NOT NULL REFERENCES PROJECT_TEMPLATE (ID) ON DELETE CASCADE,
    PERSON                      INTEGER             NOT NULL REFERENCES PERSON (PERSON_ID) ON DELETE CASCADE ,
    ROLE                        TEXT                ,
    UNIQUE (TEMPLATE,PERSON)
);

ALTER TABLE PROJECT_TEMPLATE ADD COLUMN WORKFLOW      INTEGER            REFERENCES PLM_WORKFLOW (ID) ON DELETE SET NULL;
ALTER TABLE PROGRAM_TEMPLATE ADD COLUMN WORKFLOW      INTEGER            REFERENCES PLM_WORKFLOW (ID) ON DELETE SET NULL;
ALTER TABLE PROJECT_TEMPLATEACTIVITY ADD COLUMN WORKFLOW      INTEGER            REFERENCES PLM_WORKFLOW (ID) ON DELETE SET NULL;
ALTER TABLE PROJECT_TEMPLATETASK ADD COLUMN WORKFLOW      INTEGER            REFERENCES PLM_WORKFLOW (ID) ON DELETE SET NULL;
CREATE TABLE PROJECT (
    PROJECT_ID              INTEGER                 PRIMARY KEY,
    NAME                    TEXT                    NOT NULL,
    DESCRIPTION             TEXT                    NOT NULL,
    FOREIGN KEY (PROJECT_ID)  REFERENCES            CASSINI_OBJECT (OBJECT_ID) ON DELETE CASCADE
);


CREATE TABLE PENDING_REASON (
    REASON_ID               INTEGER                 PRIMARY KEY,
    REASON                  TEXT                    NOT NULL,
    DESCRIPTION             TEXT
);

CREATE TABLE SHIFT (
    SHIFT_ID                INTEGER                 PRIMARY KEY,
    NAME                    TEXT                    NOT NULL,
    START_TIME              TIME                    NOT NULL,
    END_TIME                TIME                    NOT NULL
);

CREATE TABLE PROJECT_TASK (
    TASK_ID                 INTEGER                 PRIMARY KEY,
    PROJECT                 INTEGER                 NOT NULL REFERENCES PROJECT (PROJECT_ID) ON DELETE CASCADE,
    NAME                    TEXT                    NOT NULL,
    DESCRIPTION             TEXT                    NOT NULL,
    SHIFT                   INTEGER                 NOT NULL REFERENCES SHIFT (SHIFT_ID) ON DELETE CASCADE,
    STATUS                  TASK_STATUS             NOT NULL,
    LOCATION                TEXT                    NOT NULL,
    PENDING_REASON          INTEGER                 REFERENCES PENDING_REASON (REASON_ID) ON DELETE CASCADE,
    ASSIGNED_DATE           TIMESTAMP               NOT NULL,
    ASSIGNED_TO             INTEGER                 NOT NULL REFERENCES PERSON (PERSON_ID) ON DELETE CASCADE,
    VERIFIED_BY             INTEGER                 REFERENCES PERSON (PERSON_ID) ON DELETE CASCADE,
    APPROVED_BY             INTEGER                 REFERENCES PERSON (PERSON_ID) ON DELETE CASCADE,
    FOREIGN KEY (TASK_ID)   REFERENCES              CASSINI_OBJECT (OBJECT_ID) ON DELETE CASCADE
);

CREATE TABLE TASK_IMAGE (
    ROWID                   INTEGER                 PRIMARY KEY,
    TASK                    INTEGER                 NOT NULL REFERENCES PROJECT_TASK (TASK_ID) ON DELETE CASCADE,
    IMAGE_DATA              TEXT                    NOT NULL
);

CREATE TABLE TASK_HISTORY (
    ROWID                   INTEGER                 PRIMARY KEY,
    TASK                    INTEGER                 NOT NULL REFERENCES PROJECT_TASK (TASK_ID) ON DELETE CASCADE,
    TIMESTAMP               TIMESTAMP               NOT NULL,
    UPDATED_BY              INTEGER                 NOT NULL REFERENCES PERSON (PERSON_ID) ON DELETE CASCADE,
    OLD_STATUS              TASK_STATUS             NOT NULL,
    NEW_STATUS              TASK_STATUS             NOT NULL
);

CREATE TABLE EMERGENCY_CONTACT (
    CONTACT_ID              INTEGER                 PRIMARY KEY,
    PERSON                  INTEGER                 NOT NULL REFERENCES PERSON (PERSON_ID) ON DELETE CASCADE,
    RELATION                TEXT                    NOT NULL,
    FOREIGN KEY (CONTACT_ID) REFERENCES             PERSON (PERSON_ID) ON DELETE CASCADE
);

CREATE TABLE DEPARTMENT (
    DEPT_ID                 INTEGER                 NOT NULL PRIMARY KEY,
    NAME                    TEXT                    NOT NULL,
    DESCRIPTION             TEXT                    ,
    FOREIGN KEY (DEPT_ID)   REFERENCES              CASSINI_OBJECT (OBJECT_ID) ON DELETE CASCADE
);

CREATE TABLE PERSON_OTHERINFO (
    ROWID                               INTEGER                 PRIMARY KEY,
    ROLE                                TEXT                    NOT NULL,
    PERSON                              INTEGER                 NOT NULL REFERENCES PERSON (PERSON_ID) ON DELETE CASCADE,
    DEPARTMENT                          INTEGER                 NOT NULL REFERENCES DEPARTMENT (DEPT_ID) ON DELETE CASCADE,
    DESIGNATION                          TEXT                   NOT NULL,
    DIVISION                             TEXT                    ,
    PARENT_UNIT                          TEXT                    ,
    CONTROLLING_OFFICER                  TEXT                    ,
    CONTROLLING_OFFICER_CONTACT          TEXT                    ,
    BLOOD_GROUP                          TEXT                    ,
    MEDICAL_PROBLEMS                     TEXT
);

CREATE TABLE SHIFT_PERSON (
    ROWID                   INTEGER                 PRIMARY KEY,
    SHIFT                   INTEGER                 NOT NULL REFERENCES SHIFT (SHIFT_ID) ON DELETE CASCADE,
    PERSON                  INTEGER                 NOT NULL REFERENCES PERSON (PERSON_ID) ON DELETE CASCADE,
    UNIQUE (SHIFT, PERSON)
);

CREATE TABLE DEPARTMENT_PERSON (
    ROWID                   INTEGER                 NOT NULL PRIMARY KEY,
    DEPARTMENT              INTEGER                 NOT NULL REFERENCES DEPARTMENT (DEPT_ID) ON DELETE CASCADE,
    PERSON                  INTEGER                 NOT NULL REFERENCES PERSON (PERSON_ID) ON DELETE CASCADE,
    UNIQUE (DEPARTMENT, PERSON)
);

CREATE TABLE ACCOMMODATION (
    ACCOM_ID                INTEGER                 NOT NULL PRIMARY KEY,
    NAME                    TEXT                    NOT NULL,
    DESCRIPTION             TEXT
);

CREATE TABLE SUITE (
    SUITE_ID                INTEGER                 NOT NULL PRIMARY KEY,
    ACCOMMODATION           INTEGER                 NOT NULL REFERENCES ACCOMMODATION (ACCOM_ID) ON DELETE CASCADE,
    NAME                    TEXT                    NOT NULL,
    DESCRIPTION             TEXT
);

CREATE TABLE BED (
    BED_ID                  INTEGER                 NOT NULL PRIMARY KEY,
    SUITE                   INTEGER                 NOT NULL REFERENCES SUITE (SUITE_ID) ON DELETE CASCADE,
    NAME                    TEXT                    NOT NULL,
    PERSON                  INTEGER                 REFERENCES PERSON (PERSON_ID) ON DELETE CASCADE,
    UNIQUE (PERSON)
);

CREATE TABLE LAYOUT_DRAWING (
    LAYOUT_ID               INTEGER                 NOT NULL PRIMARY KEY,
    PROJECT                 INTEGER                 NOT NULL REFERENCES PROJECT (PROJECT_ID),
    DATE                    DATE                    NOT NULL,
    FOREIGN KEY (LAYOUT_ID)  REFERENCES             CASSINI_OBJECT (OBJECT_ID) ON DELETE CASCADE
);
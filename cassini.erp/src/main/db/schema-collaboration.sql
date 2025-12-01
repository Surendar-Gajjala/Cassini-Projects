/* Begin collaboration tables */
CREATE TABLE ERP_ATTACHMENT (
    ATTACHMENT_ID               INTEGER             NOT NULL PRIMARY KEY,
    OBJECT_TYPE                 OBJECT_TYPE         NOT NULL,
    OBJECT_ID                   INTEGER             NOT NULL,
    NAME                        TEXT                NOT NULL,
    SIZE                        INTEGER             NOT NULL DEFAULT 0,
    ADDED_ON                    TIMESTAMP           NOT NULL DEFAULT now(),
    ADDED_BY                    INTEGER             NOT NULL REFERENCES ERP_PERSON (PERSON_ID) ON DELETE CASCADE
);

CREATE TABLE ERP_COMMENT (
    COMMENT_ID                  INTEGER             NOT NULL PRIMARY KEY,
    COMMENTED_DATE              TIMESTAMP           NOT NULL DEFAULT now(),
    COMMENTED_BY                INTEGER             NOT NULL REFERENCES ERP_PERSON (PERSON_ID) ON DELETE CASCADE,
    COMMENT                     TEXT                NOT NULL,
    OBJECT_TYPE                 OBJECT_TYPE         NOT NULL,
    OBJECT_ID                   INTEGER             NOT NULL,
    REPLY_TO                    INTEGER             REFERENCES ERP_COMMENT (COMMENT_ID) ON DELETE CASCADE
);

CREATE TABLE ERP_MESSAGE (
    MESSAGE_ID                  INTEGER             NOT NULL PRIMARY KEY,
    TITLE                       TEXT                NOT NULL,
    MESSAGE                     TEXT                NOT NULL,
    SENT_DATE                   TIMESTAMP           NOT NULL,
    SENT_BY                     INTEGER             NOT NULL REFERENCES ERP_PERSON (PERSON_ID) ON DELETE CASCADE
);

CREATE TABLE ERP_MESSAGESENTTO (
    ROWID                       INTEGER             NOT NULL PRIMARY KEY,
    MESSAGE                     INTEGER             NOT NULL REFERENCES ERP_MESSAGE (MESSAGE_ID) ON DELETE CASCADE,
    PERSON                      INTEGER             NOT NULL REFERENCES ERP_PERSON (PERSON_ID) ON DELETE CASCADE
);


CREATE TABLE ERP_TODOLIST (
    TODOLIST_ID                 INTEGER             NOT NULL PRIMARY KEY,
    NAME                        TEXT                NOT NULL,
    DESCRIPTION                 TEXT                ,
    DISABLED                    BOOLEAN             NOT NULL DEFAULT FALSE,
    FOREIGN KEY (TODOLIST_ID)   REFERENCES          ERP_OBJECT (OBJECT_ID) ON DELETE CASCADE
);

CREATE TABLE ERP_TODOLISTITEM (
    TODOITEM_ID                 INTEGER             NOT NULL PRIMARY KEY,
    TODO_LIST                   INTEGER             NOT NULL REFERENCES ERP_TODOLIST (TODOLIST_ID) ON DELETE CASCADE,
    TEXT                        TEXT                NOT NULL,
    COMPLETED                   BOOLEAN             NOT NULL DEFAULT FALSE,
    COMPLETED_DATE              TIMESTAMP
);

/* Note can be attached to many objects */
CREATE TABLE ERP_NOTE (
    NOTE_ID                     INTEGER             NOT NULL PRIMARY KEY,
    OBJECT_TYPE                 OBJECT_TYPE         NOT NULL,
    OBJECT_ID                   INTEGER             NOT NULL,
    TITLE                       TEXT                NOT NULL,
    DETAILS                     TEXT                ,
    FOREIGN KEY (NOTE_ID)       REFERENCES          ERP_OBJECT (OBJECT_ID) ON DELETE CASCADE
);

CREATE TABLE ERP_REMINDER (
    REMINDER_ID                 INTEGER             NOT NULL PRIMARY KEY,
    PERSON_ID                   INTEGER             NOT NULL REFERENCES ERP_PERSON (PERSON_ID) ON DELETE CASCADE,
    DATE                        TIMESTAMP                NOT NULL,
    TITLE                       TEXT                NOT NULL,
    DETAILS                     TEXT                ,
    DISABLED                    BOOLEAN             NOT NULL DEFAULT FALSE
);
/* End collaboration tables */

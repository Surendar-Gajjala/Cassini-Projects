/* Begin collaboration tables */
CREATE TABLE ATTACHMENT ( /* Attachment can be on many items */
    ATTACHMENT_ID               INTEGER             NOT NULL PRIMARY KEY,
    OBJECT_TYPE                 OBJECT_TYPE         NOT NULL,
    OBJECT_ID                   INTEGER             NOT NULL,
    NAME                        TEXT                NOT NULL,
    EXTENSION                   TEXT                ,
    SIZE                        INTEGER             NOT NULL DEFAULT 0,
    ADDED_ON                    TIMESTAMP           NOT NULL DEFAULT now(),
    ADDED_BY                    INTEGER             NOT NULL REFERENCES PERSON (PERSON_ID) ON DELETE CASCADE
);

CREATE TABLE ATTRIBUTE_ATTACHMENT (
    ID                          INTEGER             NOT NULL PRIMARY KEY,
    ATTRIBUTEDEF                INTEGER             NOT NULL REFERENCES OBJECTTYPEATTRIBUTE (ATTRIBUTE_ID) ON DELETE CASCADE,
    FOREIGN KEY (ID) REFERENCES ATTACHMENT (ATTACHMENT_ID)
);

ALTER TABLE OBJECTATTRIBUTE ADD COLUMN ATTACHMENT_VALUE INTEGER[]/* REFERENCES ATTACHMENT (ATTACHMENT_ID) ON DELETE CASCADE*/;

CREATE TABLE COMMENT (
    COMMENT_ID                  INTEGER             NOT NULL PRIMARY KEY,
    COMMENTED_DATE              TIMESTAMP           NOT NULL DEFAULT now(),
    COMMENTED_BY                INTEGER             NOT NULL REFERENCES PERSON (PERSON_ID) ON DELETE CASCADE,
    COMMENT                     TEXT                NOT NULL,
    OBJECT_TYPE                 OBJECT_TYPE         ,
    OBJECT_ID                   INTEGER             ,
    REPLY_TO                    INTEGER             REFERENCES COMMENT (COMMENT_ID) ON DELETE CASCADE
);

CREATE TABLE USER_READ_COMMENT (
    ID                          INTEGER             NOT NULL PRIMARY KEY,
    COMMENT                     INTEGER             NOT NULL REFERENCES COMMENT(COMMENT_ID) ON DELETE CASCADE,
    PERSON                      INTEGER             NOT NULL REFERENCES PERSON (PERSON_ID) ON DELETE CASCADE,
    READ                        BOOLEAN             DEFAULT FALSE,
    UNIQUE (COMMENT,PERSON)
);

CREATE TABLE MESSAGE (
    MESSAGE_ID                  INTEGER             NOT NULL PRIMARY KEY,
    TITLE                       TEXT                NOT NULL,
    MESSAGE                     TEXT                NOT NULL,
    SENT_DATE                   TIMESTAMP           NOT NULL,
    SENT_BY                     INTEGER             NOT NULL REFERENCES PERSON (PERSON_ID) ON DELETE CASCADE
);

CREATE TABLE MESSAGESENTTO (
    MESSAGE_ID                  INTEGER             NOT NULL PRIMARY KEY,
    PERSON                      INTEGER             NOT NULL REFERENCES PERSON (PERSON_ID) ON DELETE CASCADE
);


CREATE TABLE MEETING (
    MEETING_ID                  INTEGER             NOT NULL PRIMARY KEY,
    TITLE                       TEXT                NOT NULL,
    DESCRIPTION                 TEXT                NOT NULL,
    START_DATE                  DATE                NOT NULL,
    START_TIME                  TIME                NOT NULL,
    END_DATE                    DATE                NOT NULL,
    END_TIME                    TIME                NOT NULL,
    MINUTES                     TEXT                ,
    FOREIGN KEY (MEETING_ID)    REFERENCES          CASSINI_OBJECT (OBJECT_ID) ON DELETE CASCADE
);


CREATE TABLE RECURRINGMEETING (
    MEETING_ID                  INTEGER             NOT NULL PRIMARY KEY,
    FREQUENCY                   FREQUENCY_TYPE      NOT NULL,
    FOREIGN KEY (MEETING_ID)    REFERENCES          MEETING (MEETING_ID) ON DELETE CASCADE
);

/*CREATE TABLE MEETINGATTENDEE (
    ATTENDEE_ID                 INTEGER             NOT NULL PRIMARY KEY,
    MEETING_ID                  INTEGER             NOT NULL REFERENCES MEETING (MEETING_ID) ON DELETE CASCADE,
    OPTIONAL                    BOOLEAN             NOT NULL DEFAULT TRUE,
    FOREIGN KEY (ATTENDEE_ID)   REFERENCES          PERSON (PERSON_ID) ON DELETE CASCADE
);*/

CREATE TABLE MEETINGATTENDEE (
    PERSON                     INTEGER               NOT NULL REFERENCES PERSON (PERSON_ID) ON DELETE CASCADE,
    MEETING                     INTEGER              NOT NULL REFERENCES MEETING (MEETING_ID) ON DELETE CASCADE,
    UNIQUE (PERSON, MEETING)
);

CREATE TABLE TODOLIST (
    TODOLIST_ID                 INTEGER             NOT NULL PRIMARY KEY,
    NAME                        TEXT                NOT NULL,
    DESCRIPTION                 TEXT                ,
    FOREIGN KEY (TODOLIST_ID)   REFERENCES          CASSINI_OBJECT (OBJECT_ID) ON DELETE CASCADE
);

CREATE TABLE TODOITEM (
    TODOITEM_ID                 INTEGER             NOT NULL PRIMARY KEY,
    TODO_LIST                   INTEGER             NOT NULL REFERENCES TODOLIST (TODOLIST_ID) ON DELETE CASCADE,
    COMPLETED                   BOOLEAN             NOT NULL DEFAULT FALSE,
    COMPLETED_DATE              TIMESTAMP
);

CREATE TABLE NOTE (
    NOTE_ID                     INTEGER             NOT NULL PRIMARY KEY,
    OBJECT_TYPE                 OBJECT_TYPE         NOT NULL,
    OBJECT_ID                   INTEGER             NOT NULL,
    TITLE                       TEXT                NOT NULL,
    DETAILS                     TEXT                ,
    FOREIGN KEY (NOTE_ID)       REFERENCES          CASSINI_OBJECT (OBJECT_ID) ON DELETE CASCADE
);


CREATE TABLE USERINBOX (
    INBOX_ID                    INTEGER             NOT NULL PRIMARY KEY,
    USER_ID                     INTEGER             NOT NULL REFERENCES PERSON (PERSON_ID) ON DELETE CASCADE,
    MESSAGE_TYPE                TEXT                NOT NULL,
    MESSAGE                     TEXT                NOT NULL,
    MESSAGE_READ                BOOLEAN             NOT NULL DEFAULT FALSE,
    TIMESTAMP                   TIMESTAMP           NOT NULL,
    OBJECT_TYPE                 OBJECT_TYPE         ,
    OBJECT_ID                   INTEGER
);


CREATE TABLE MESSAGEGROUP (
    MG_ID                      INTEGER             NOT NULL PRIMARY KEY,
    NAME                       TEXT                NOT NULL,
    GROUP_ICON                 BYTEA               ,
    DESCRIPTION                TEXT                NULL,
    CTX_OBJECT_TYPE            OBJECT_TYPE         NULL,
    CTX_OBJECT_ID              INTEGER             NULL,
    FOREIGN KEY (MG_ID)   REFERENCES               CASSINI_OBJECT (OBJECT_ID) ON DELETE CASCADE
);

CREATE TABLE MESSAGEGROUPMEMBER (
    ROWID                       INTEGER             NOT NULL PRIMARY KEY,
    MSG_GROUP_ID                INTEGER             NOT NULL REFERENCES MESSAGEGROUP (MG_ID) ON DELETE CASCADE,
    PERSON                      INTEGER             NOT NULL REFERENCES PERSON (PERSON_ID) ON DELETE CASCADE,
    STATUS                      GRP_MEMBER_STATUS   NOT NULL,
    IS_ADMIN                    BOOLEAN             NOT NULL DEFAULT FALSE,
    CTX_OBJECT_TYPE            OBJECT_TYPE         NULL,
    CTX_OBJECT_ID              INTEGER             NULL,
    FOREIGN KEY (ROWID)         REFERENCES        CASSINI_OBJECT (OBJECT_ID)  ON DELETE CASCADE
);

CREATE TABLE GROUPMESSAGE (
    GRP_MSG_ID                  INTEGER             NOT NULL PRIMARY KEY,
    MSG_GROUP_ID                INTEGER             NOT NULL REFERENCES MESSAGEGROUP (MG_ID) ON DELETE CASCADE,
    MESSAGE_TEXT                TEXT                NOT NULL,
    POSTED_DATE                 TIMESTAMP           NOT NULL,
    POSTED_BY                   INTEGER             NOT NULL REFERENCES MESSAGEGROUPMEMBER (ROWID) ON DELETE CASCADE,
    CTX_OBJECT_TYPE            OBJECT_TYPE         NULL,
    CTX_OBJECT_ID              INTEGER             NULL,
    FOREIGN KEY (GRP_MSG_ID)    REFERENCES          CASSINI_OBJECT (OBJECT_ID) ON DELETE CASCADE

);

CREATE TABLE DISCUSSIONGROUP (
    GROUP_ID                    INTEGER             NOT NULL PRIMARY KEY,
    NAME                        TEXT                NOT NULL,
    DESCRIPTION                 TEXT                ,
    MODERATOR                   INTEGER             NOT NULL REFERENCES PERSON (PERSON_ID) ON DELETE CASCADE,
    IS_ACTIVE                   BOOLEAN             NOT NULL DEFAULT TRUE,
    CTX_OBJECT_TYPE            OBJECT_TYPE         NULL,
    CTX_OBJECT_ID              INTEGER             NULL,
    FOREIGN KEY (GROUP_ID)   REFERENCES               CASSINI_OBJECT (OBJECT_ID) ON DELETE CASCADE
);


CREATE TABLE DISCUSSIONGROUPMESSAGE (
    D_GRP_MSG_ID                INTEGER             NOT NULL PRIMARY KEY,
    D_GRP_ID                    INTEGER             REFERENCES DISCUSSIONGROUP (GROUP_ID) ON DELETE CASCADE,
    COMMENTED_DATE              TIMESTAMP           NOT NULL DEFAULT now(),
    COMMENTED_BY                INTEGER             NOT NULL REFERENCES PERSON (PERSON_ID) ON DELETE CASCADE,
    COMMENT                     TEXT                NOT NULL,
    REPLY_TO                    INTEGER             REFERENCES DISCUSSIONGROUPMESSAGE (D_GRP_MSG_ID) ON DELETE CASCADE,
    CTX_OBJECT_TYPE            OBJECT_TYPE         NULL,
    CTX_OBJECT_ID              INTEGER             NULL,
    FOREIGN KEY (D_GRP_MSG_ID)  REFERENCES          CASSINI_OBJECT (OBJECT_ID) ON DELETE CASCADE
);

CREATE TABLE LOCATION (
    ID                          INTEGER             NOT NULL PRIMARY KEY,
    LATITUDE                    DOUBLE PRECISION    NOT NULL,
    LONGITUDE                   DOUBLE PRECISION    NOT NULL,
    UPLOAD_FROM                 TEXT
);

CREATE TABLE MEDIA (
    MEDIA_ID                    INTEGER             NOT NULL PRIMARY KEY ,
    FILENAME                    TEXT                NOT NULL,
    TYPE                        MEDIA_TYPE          NOT NULL ,
    EXTENSION                   TEXT                ,
    DATA                        BYTEA               ,
    OBJECT_ID                   INTEGER             REFERENCES CASSINI_OBJECT (OBJECT_ID) ON DELETE CASCADE,
    LOCATION                    INTEGER             REFERENCES LOCATION(ID) ON DELETE CASCADE,
    DESCRIPTION                 TEXT                ,
    FOREIGN KEY (MEDIA_ID)      REFERENCES          CASSINI_OBJECT (OBJECT_ID) ON DELETE CASCADE
);

CREATE TABLE APPNOTIFICATION (
    ID                          INTEGER             NOT NULL PRIMARY KEY,
    EVENT_NAME                  TEXT                NOT NULL,
    EVENT_DESCRIPTION           TEXT                NOT NULL,
    ENABLED                     BOOLEAN             NOT NULL DEFAULT FALSE
);

CREATE TABLE NOTIFICATIONTO(
    ID                          INTEGER             NOT NULL PRIMARY KEY,
    NOTIFICATION                INTEGER             NOT NULL REFERENCES APPNOTIFICATION (ID) ON DELETE CASCADE,
    NOTIFIEDTYPE                NOTIFIED_TYPE       NOT NULL,
    NOTIFIEDTO                  INTEGER             NOT NULL
);

CREATE TABLE MAILSERVER (
    ID                          INTEGER             NOT NULL PRIMARY KEY,
    NAME                        TEXT                NOT NULL,
    SMTP_SERVER                 TEXT                NOT NULL,
    SMTP_PORT                   INTEGER             NOT NULL,
    IMAP_SERVER                 TEXT                NOT NULL,
    IMAP_PORT                   INTEGER             NOT NULL
);


CREATE TABLE OBJECT_MAILSETTINGS (
    OBJECTID                    INTEGER             NOT NULL PRIMARY KEY REFERENCES CASSINI_OBJECT (OBJECT_ID) ON DELETE CASCADE,
    MAILSERVER                  INTEGER             NOT NULL REFERENCES MAILSERVER (ID) ON DELETE CASCADE,
    RECEIVER_USER               TEXT                NOT NULL,
    RECEIVER_EMAIL              TEXT                NOT NULL,
    RECEIVER_PASSWORD           TEXT                NOT NULL,
    SENDER_USER                 TEXT                NOT NULL,
    SENDER_EMAIL                TEXT                NOT NULL,
    SENDER_PASSWORD             TEXT                NOT NULL
);


/* End collaboration tables */
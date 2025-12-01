/* Begin security tables */
CREATE TABLE LOGIN (
    LOGIN_ID                    INTEGER             NOT NULL PRIMARY KEY,
    LOGIN_NAME                  VARCHAR(50)         NOT NULL UNIQUE,
    PERSON_ID                   INTEGER             NOT NULL UNIQUE REFERENCES PERSON (PERSON_ID) ON DELETE CASCADE,
    PASSWORD                    TEXT                NOT NULL,
    FINGERPRINTDATA             TEXT                ,
    IS_ACTIVE                   BOOLEAN             NOT NULL,
    IS_LOCKED                   BOOLEAN             DEFAULT FALSE,
    IS_SUPERUSER                BOOLEAN             DEFAULT FALSE,
    EXTERNAL                    BOOLEAN             DEFAULT FALSE,
    IS_ADMIN                    BOOLEAN             DEFAULT FALSE,
    FOREIGN KEY (LOGIN_ID)      REFERENCES          CASSINI_OBJECT (OBJECT_ID) ON DELETE CASCADE
);


ALTER TABLE USER_PREFERENCE ADD COLUMN LOGIN  INTEGER  NOT NULL REFERENCES LOGIN (LOGIN_ID) ON DELETE CASCADE;
ALTER TABLE USER_PREFERENCE ADD UNIQUE (LOGIN);

CREATE TABLE SESSION (
    SESSION_ID                  INTEGER             NOT NULL PRIMARY KEY,
    LOGIN_ID                    INTEGER             NOT NULL REFERENCES LOGIN (LOGIN_ID) ON DELETE CASCADE,
    IP_ADDRESS                  VARCHAR(32)         NOT NULL,
    LOGIN_TIME                  TIMESTAMP           NOT NULL,
    LOGOUT_TIME                 TIMESTAMP           ,
    MOBILE_DEVICE               TEXT                ,
    USER_AGENT                  TEXT                ,
    UNIQUE (SESSION_ID, LOGIN_ID)
);

CREATE TABLE ROLE (
    ROLE_ID                 INTEGER             NOT NULL PRIMARY KEY,
    NAME                    TEXT                NOT NULL,
    DESCRIPTION             TEXT                ,
    UNIQUE (ROLE_ID, NAME)
);

CREATE TABLE PERSONROLE (
    ROWID                       INTEGER             NOT NULL PRIMARY KEY,
    PERSON                      INTEGER             NOT NULL REFERENCES PERSON (PERSON_ID) ON DELETE CASCADE,
    ROLE                        INTEGER             NOT NULL REFERENCES ROLE (ROLE_ID) ON DELETE CASCADE
);

CREATE TABLE PERSONGROUPROLE (
    ROWID                       INTEGER             NOT NULL PRIMARY KEY,
    GROUP_ID                    INTEGER             NOT NULL REFERENCES PERSONGROUP (GROUP_ID) ON DELETE CASCADE,
    ROLE                        INTEGER             NOT NULL REFERENCES ROLE (ROLE_ID) ON DELETE CASCADE
);

CREATE TABLE LOGINROLE (
    LOGIN                   INTEGER             NOT NULL REFERENCES LOGIN (LOGIN_ID) ON DELETE CASCADE,
    ROLE                    INTEGER             NOT NULL REFERENCES ROLE (ROLE_ID) ON DELETE CASCADE,
    UNIQUE (LOGIN, ROLE)
);

CREATE TABLE PERMISSION (
    PERMISSION_ID           TEXT                NOT NULL PRIMARY KEY,
    NAME                    TEXT                NOT NULL,
    DESCRIPTION             TEXT                ,
    UNIQUE (PERMISSION_ID, NAME)
);

CREATE TABLE ROLEPERMISSION (
    ROLE                    INTEGER             NOT NULL REFERENCES ROLE (ROLE_ID) ON DELETE CASCADE,
    PERMISSION              TEXT                NOT NULL REFERENCES PERMISSION (PERMISSION_ID) ON DELETE CASCADE,
    UNIQUE (ROLE, PERMISSION)
);

CREATE TABLE LOGINPERMISSION (
    LOGIN                   INTEGER             NOT NULL REFERENCES LOGIN (LOGIN_ID) ON DELETE CASCADE,
    PERMISSION              TEXT                NOT NULL REFERENCES PERMISSION (PERMISSION_ID) ON DELETE CASCADE,
    UNIQUE (LOGIN, PERMISSION)
);


CREATE TABLE GROUPPERMISSION (
    PERSONGROUP                   INTEGER             NOT NULL REFERENCES  PERSONGROUP (GROUP_ID) ON DELETE CASCADE,
    PERMISSION              TEXT                NOT NULL REFERENCES PERMISSION (PERMISSION_ID) ON DELETE CASCADE,
    UNIQUE (PERSONGROUP, PERMISSION)
);

CREATE TABLE LOGINGROUP (
    LOGIN                   INTEGER             NOT NULL REFERENCES LOGIN (LOGIN_ID) ON DELETE CASCADE,
    PERSONGROUP                   INTEGER            NOT NULL REFERENCES PERSONGROUP (GROUP_ID) ON DELETE CASCADE,
    UNIQUE (LOGIN, PERSONGROUP)
);

CREATE TABLE ONETIMEPASSWORD (
    ID                      INTEGER             NOT NULL PRIMARY KEY,
    LOGIN                   INTEGER             NOT NULL REFERENCES LOGIN(LOGIN_ID) ON DELETE CASCADE,
    OTP                     TEXT                NOT NULL,
    EXPIRES                 TIMESTAMP           NOT NULL,
    VERIFIED                BOOLEAN             NOT NULL DEFAULT FALSE,
    APPLIED                 BOOLEAN             NOT NULL DEFAULT FALSE,
    FOREIGN KEY (ID)        REFERENCES          CASSINI_OBJECT (OBJECT_ID) ON DELETE CASCADE
);

CREATE TABLE SECURITY_PERMISSION (
    PERMISSION_ID           SERIAL           NOT NULL PRIMARY KEY,
    NAME                    TEXT                NOT NULL,
    DESCRIPTION             TEXT                ,
    OBJECT_TYPE             TEXT                ,
    SUB_TYPE                TEXT                ,
    SUB_TYPE_ID             INTEGER             ,
    PRIVILEGE               TEXT                ,
    MODULE                  MODULE_TYPE         NOT NULL DEFAULT 'PLATFORM',
    ATTRIBUTE               TEXT                ,
    ATTRIBUTE_GROUP         TEXT                ,
    CRITERIA                TEXT                ,
    PRIVILEGE_TYPE          PRIVILEGE_TYPE      NOT NULL DEFAULT 'GRANTED',
    UNIQUE (PERMISSION_ID, NAME)
);

CREATE TABLE GROUP_SECURITY_PERMISSION (
    PERSONGROUP             INTEGER                 NOT NULL REFERENCES  PERSONGROUP (GROUP_ID) ON DELETE CASCADE,
    SECURITY_PERMISSION              INTEGER               NOT NULL REFERENCES SECURITY_PERMISSION (PERMISSION_ID) ON DELETE CASCADE,
    UNIQUE (PERSONGROUP, SECURITY_PERMISSION)
);

CREATE TABLE LOGIN_SECURITY_PERMISSION (
    PERMISSION_ID           INTEGER           NOT NULL PRIMARY KEY,
    PERSON                  INTEGER             NOT NULL REFERENCES PERSON (PERSON_ID) ON DELETE CASCADE,
    NAME                    TEXT                NOT NULL,
    DESCRIPTION             TEXT                ,
    OBJECT_TYPE             TEXT                ,
    SUB_TYPE                TEXT                ,
    SUB_TYPE_ID             INTEGER             ,
    PRIVILEGE               TEXT                ,
    MODULE                  MODULE_TYPE         NOT NULL DEFAULT 'PLATFORM',
    ATTRIBUTE               TEXT                ,
    ATTRIBUTE_GROUP         TEXT                ,
    CRITERIA                TEXT                ,
    PRIVILEGE_TYPE          PRIVILEGE_TYPE      NOT NULL DEFAULT 'GRANTED',
    UNIQUE (PERMISSION_ID)
);

CREATE TABLE DM_FOLDER_PERMISSION (
    PERMISSION_ID           INTEGER           NOT NULL PRIMARY KEY,
    FOLDER_ID               INTEGER           NOT NULL,
    GROUP_ID                INTEGER           NOT NULL,
    ACTIONS                 TEXT              ,
    IS_SUB_FOLDER           BOOLEAN           NOT NULL,
    UNIQUE (PERMISSION_ID)
);

CREATE TABLE PRIVILEGE (
    PRIVILEGE_ID            INTEGER                 NOT NULL PRIMARY KEY,
    NAME                    TEXT                NOT NULL,
    DESCRIPTION             TEXT                ,
    UNIQUE (PRIVILEGE_ID, NAME)
);
/* End security tables */
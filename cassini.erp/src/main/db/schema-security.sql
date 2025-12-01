/* Begin security tables */
CREATE TABLE ERP_LOGIN (
    LOGIN_ID                    INTEGER             NOT NULL PRIMARY KEY,
    LOGIN_NAME                  VARCHAR(50)         NOT NULL UNIQUE,
    PERSON_ID                   INTEGER             NOT NULL UNIQUE REFERENCES ERP_PERSON (PERSON_ID) ON DELETE CASCADE,
    PASSWORD                    TEXT                NOT NULL,
    IS_ACTIVE                   BOOLEAN             NOT NULL,
    IS_SUPERUSER                BOOLEAN             DEFAULT FALSE,
    FOREIGN KEY (LOGIN_ID)      REFERENCES          ERP_OBJECT (OBJECT_ID) ON DELETE CASCADE
);

CREATE TABLE ERP_SESSION (
    SESSION_ID                  INTEGER             NOT NULL PRIMARY KEY,
    LOGIN_ID                    INTEGER             NOT NULL REFERENCES ERP_LOGIN (LOGIN_ID) ON DELETE CASCADE,
    IP_ADDRESS                  VARCHAR(32)         NOT NULL,
    LOGIN_TIME                  TIMESTAMP           NOT NULL,
    LOGOUT_TIME                 TIMESTAMP           ,
    MOBILE_DEVICE               TEXT                ,
    UNIQUE (SESSION_ID, LOGIN_ID)
);

CREATE TABLE ERP_ROLE (
    ROLE_ID                 INTEGER             NOT NULL PRIMARY KEY,
    NAME                    TEXT                NOT NULL,
    DESCRIPTION             TEXT                ,
    UNIQUE (ROLE_ID, NAME)
);


CREATE TABLE ERP_LOGINROLE (
    LOGIN                   INTEGER             NOT NULL REFERENCES ERP_LOGIN (LOGIN_ID) ON DELETE CASCADE,
    ROLE                    INTEGER             NOT NULL REFERENCES ERP_ROLE (ROLE_ID) ON DELETE CASCADE,
    UNIQUE (LOGIN, ROLE)
);

CREATE TABLE ERP_PERMISSION (
    PERMISSION_ID           TEXT                NOT NULL PRIMARY KEY,
    NAME                    TEXT                NOT NULL,
    DESCRIPTION             TEXT                ,
    UNIQUE (PERMISSION_ID, NAME)
);

CREATE TABLE ERP_ROLEPERMISSION (
    ROLE                    INTEGER             NOT NULL REFERENCES ERP_ROLE (ROLE_ID) ON DELETE CASCADE,
    PERMISSION              TEXT                NOT NULL REFERENCES ERP_PERMISSION (PERMISSION_ID) ON DELETE CASCADE,
    UNIQUE (ROLE, PERMISSION)
);
/* End security tables */
/* Wrap everything in a transaction */
BEGIN TRANSACTION;

/* Begin schema setup */
DROP SCHEMA IF EXISTS cassini_platform CASCADE;
CREATE SCHEMA cassini_platform;
SET SEARCH_PATH TO cassini_platform;

CREATE TABLE DATABASE (
    ID                  BIGINT          NOT NULL PRIMARY KEY,
    HOST                TEXT            NOT NULL,
    PORT                INTEGER         NOT NULL,
    DB_NAME             TEXT            NOT NULL,
    DB_USER             TEXT            NOT NULL,
    DB_PASSWORD         TEXT            NOT NULL
);

CREATE TABLE TENANT (
    ID                  TEXT            NOT NULL PRIMARY KEY,
    DB                  BIGINT          NOT NULL REFERENCES DATABASE (ID)
);

/*
INSERT INTO DATABASE VALUES (1, 'localhost', 5432, 'cassiniapps', 'cassinisys', 'cassinisys');
INSERT INTO TENANT VALUES ('cassini_plm', 1);
 */
/* Commit everything */
COMMIT TRANSACTION;
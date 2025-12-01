-- region TEST DEFINITION
CREATE TABLE TEST_SCENARIO (
    ID                  INTEGER                     NOT NULL PRIMARY KEY,
    NAME                TEXT                        NOT NULL,
    DESCRIPTION         TEXT                        ,
    FOREIGN KEY (ID)    REFERENCES                  CASSINI_OBJECT (OBJECT_ID) ON DELETE CASCADE
);

CREATE TABLE TEST_PLAN (
    ID                  INTEGER                     NOT NULL PRIMARY KEY,
    NAME                TEXT                        NOT NULL,
    DESCRIPTION         TEXT                        ,
    SCENARIO            INTEGER                     NOT NULL REFERENCES TEST_SCENARIO (ID) ON DELETE CASCADE,
    FOREIGN KEY (ID)    REFERENCES                  CASSINI_OBJECT (OBJECT_ID) ON DELETE CASCADE
);

CREATE TABLE TEST_SUITE (
    ID                  INTEGER                     NOT NULL PRIMARY KEY,
    NAME                TEXT                        NOT NULL,
    DESCRIPTION         TEXT                        ,
    PLAN                INTEGER                     NOT NULL REFERENCES TEST_PLAN (ID) ON DELETE CASCADE,
    FOREIGN KEY (ID)    REFERENCES                  CASSINI_OBJECT (OBJECT_ID) ON DELETE CASCADE
);

CREATE TABLE TEST_EXECUTION (
    ID                  INTEGER                     NOT NULL PRIMARY KEY,
    TYPE                EXECUTION_TYPE              NOT NULL
);

CREATE TABLE TEST_PROGRAMEXECUTION (
    ID                  INTEGER                     NOT NULL PRIMARY KEY,
    PROGRAM             TEXT                        NOT NULL,
    PARAMS              TEXT                        ,
    WORKING_DIR         TEXT                        ,
    FOREIGN KEY (ID)    REFERENCES                  TEST_EXECUTION (ID) ON DELETE CASCADE
);

CREATE TABLE TEST_SCRIPTEXECUTION (
    ID                  INTEGER                     NOT NULL PRIMARY KEY,
    LANGUAGE            SCRIPT_LANGUAGE             NOT NULL,
    SCRIPT              TEXT                        NOT NULL,
    FOREIGN KEY (ID)    REFERENCES                  TEST_EXECUTION (ID) ON DELETE CASCADE
);

CREATE TABLE TEST_CASE (
    ID                  INTEGER                     NOT NULL PRIMARY KEY,
    NAME                TEXT                        NOT NULL,
    DESCRIPTION         TEXT                        ,
    SUITE               INTEGER                     NOT NULL REFERENCES TEST_SUITE(ID) ON DELETE CASCADE,
    EXECUTION           INTEGER                     REFERENCES TEST_EXECUTION(ID) ON DELETE CASCADE,
    FOREIGN KEY (ID)    REFERENCES                  CASSINI_OBJECT (OBJECT_ID) ON DELETE CASCADE
);


CREATE TABLE TEST_INPUTPARAM (
    ID                          INTEGER             NOT NULL PRIMARY KEY,
    TCASE                       INTEGER             NOT NULL REFERENCES TEST_CASE (ID) ON DELETE CASCADE,
    DATA_TYPE                   DATATYPE            NOT NULL,
    NAME                        TEXT                NOT NULL,
    DESCRIPTION                 TEXT
);

CREATE TABLE TEST_OUTPUTPARAM (
    ID                          INTEGER             NOT NULL PRIMARY KEY,
    TCASE                       INTEGER             NOT NULL REFERENCES TEST_CASE (ID) ON DELETE CASCADE,
    DATA_TYPE                   DATATYPE            NOT NULL,
    NAME                        TEXT                NOT NULL,
    DESCRIPTION                 TEXT
);
-- endregion


-- region RUN CONFIGURATION
CREATE TABLE TEST_RUNCONFIGURATION (
    ID                  INTEGER                     NOT NULL PRIMARY KEY,
    NAME                TEXT                        NOT NULL,
    DESCRIPTION         TEXT                        NOT NULL,
    SCENARIO            INTEGER                     REFERENCES TEST_SCENARIO (ID) ON DELETE CASCADE,
    FOREIGN KEY (ID)    REFERENCES                  CASSINI_OBJECT (OBJECT_ID) ON DELETE CASCADE
);


CREATE TABLE TEST_RCINPUTPARAMVALUE (
    ID                          INTEGER             NOT NULL PRIMARY KEY,
    CONFIG                      INTEGER             NOT NULL REFERENCES TEST_RUNCONFIGURATION (ID) ON DELETE CASCADE,
    TCASE                       INTEGER             NOT NULL REFERENCES TEST_CASE (ID) ON DELETE CASCADE,
    INPUTPARAM                  INTEGER             NOT NULL REFERENCES TEST_INPUTPARAM (ID) ON DELETE CASCADE,
    STRING_VALUE                TEXT                ,
    INTEGER_VALUE               INTEGER             ,
    DOUBLE_VALUE                DOUBLE PRECISION    ,
    DATE_VALUE                  TIMESTAMP           ,
    TIME_VALUE                  TIME                ,
    TIMESTAMP_VALUE             TIMESTAMP           ,
    BOOLEAN_VALUE               BOOLEAN             ,
    UNIQUE (CONFIG,TCASE, INPUTPARAM)
);

CREATE TABLE TEST_RCOUTPUTPARAMEXPECTEDVALUE (
    ID                          INTEGER             NOT NULL PRIMARY KEY,
    CONFIG                      INTEGER             NOT NULL REFERENCES TEST_RUNCONFIGURATION (ID) ON DELETE CASCADE,
    TCASE                       INTEGER             NOT NULL REFERENCES TEST_CASE (ID) ON DELETE CASCADE,
    OUTPUTPARAM                 INTEGER             NOT NULL REFERENCES TEST_OUTPUTPARAM (ID) ON DELETE CASCADE,
    STRING_VALUE                TEXT                ,
    INTEGER_VALUE               INTEGER             ,
    DOUBLE_VALUE                DOUBLE PRECISION    ,
    DATE_VALUE                  TIMESTAMP           ,
    TIME_VALUE                  TIME                ,
    TIMESTAMP_VALUE             TIMESTAMP           ,
    BOOLEAN_VALUE               BOOLEAN             ,
    UNIQUE (CONFIG,TCASE, OUTPUTPARAM)
);

CREATE TABLE TEST_RUNSCHEDULE (
    ID                          INTEGER                     NOT NULL PRIMARY KEY,
    RUNCONFIG                   INTEGER                     NOT NULL REFERENCES TEST_RUNCONFIGURATION (ID) ON DELETE CASCADE,
    SUNDAY                      TIME,
    MONDAY                      TIME,
    TUESDAY                     TIME,
    WEDNESDAY                   TIME,
    THURSDAY                    TIME,
    FRIDAY                      TIME,
    SATURDAY                    TIME

);
-- endregion


-- region TEST RUN
CREATE TABLE TEST_RUN (
    ID                  INTEGER                     NOT NULL PRIMARY KEY,
    RUN_CONFIG          INTEGER                     NOT NULL REFERENCES TEST_RUNCONFIGURATION (ID) ON DELETE CASCADE,
    START_TIME          TIMESTAMP                   NOT NULL,
    FINISH_TIME         TIMESTAMP                   ,
    STATUS              RUNSTATUS                   NOT NULL,
    TOTAL               INTEGER                     ,
    PASSED              INTEGER                     ,
    FAILED              INTEGER                     ,
    FOREIGN KEY (ID)    REFERENCES                  CASSINI_OBJECT (OBJECT_ID) ON DELETE CASCADE
);

CREATE TABLE TEST_RUNSCENARIO (
    ID                  INTEGER                     NOT NULL PRIMARY KEY,
    NAME                TEXT                        NOT NULL,
    DESCRIPTION         TEXT                        ,
    RUN                 INTEGER                     NOT NULL REFERENCES TEST_RUN (ID)  ON DELETE CASCADE,
    FOREIGN KEY (ID)    REFERENCES                  CASSINI_OBJECT (OBJECT_ID) ON DELETE CASCADE
);

CREATE TABLE TEST_RUNPLAN (
    ID                  INTEGER                     NOT NULL PRIMARY KEY,
    NAME                TEXT                        NOT NULL,
    DESCRIPTION         TEXT                        ,
    SCENARIO            INTEGER                     NOT NULL REFERENCES TEST_RUNSCENARIO (ID) ON DELETE CASCADE,
    FOREIGN KEY (ID)    REFERENCES                  CASSINI_OBJECT (OBJECT_ID) ON DELETE CASCADE
);

CREATE TABLE TEST_RUNSUITE (
    ID                  INTEGER                     NOT NULL PRIMARY KEY,
    NAME                TEXT                        NOT NULL,
    DESCRIPTION         TEXT                        ,
    PLAN                INTEGER                     NOT NULL REFERENCES TEST_RUNPLAN (ID) ON DELETE CASCADE,
    FOREIGN KEY (ID)    REFERENCES                  CASSINI_OBJECT (OBJECT_ID) ON DELETE CASCADE
);

CREATE TABLE TEST_RUNEXECUTION (
    ID                  INTEGER                     NOT NULL PRIMARY KEY,
    TYPE                EXECUTION_TYPE              NOT NULL,
    FOREIGN KEY (ID)    REFERENCES                  CASSINI_OBJECT (OBJECT_ID) ON DELETE CASCADE
);

CREATE TABLE TEST_RUNPROGRAMEXECUTION (
    ID                  INTEGER                     NOT NULL PRIMARY KEY,
    PROGRAM             TEXT                        NOT NULL,
    PARAMS              TEXT                        ,
    WORKING_DIR         TEXT                        ,
    FOREIGN KEY (ID)    REFERENCES                  TEST_RUNEXECUTION (ID) ON DELETE CASCADE
);

CREATE TABLE TEST_RUNSCRIPTEXECUTION (
    ID                  INTEGER                     NOT NULL PRIMARY KEY,
    LANGUAGE            SCRIPT_LANGUAGE             NOT NULL,
    SCRIPT              TEXT                        NOT NULL,
    FOREIGN KEY (ID)    REFERENCES                  TEST_RUNEXECUTION (ID) ON DELETE CASCADE
);

CREATE TABLE TEST_RUNCASE (
    ID                  INTEGER                     NOT NULL PRIMARY KEY,
    NAME                TEXT                        NOT NULL,
    DESCRIPTION         TEXT                        ,
    RESULT              BOOLEAN                     DEFAULT FALSE ,
    SUITE               INTEGER                     NOT NULL REFERENCES TEST_RUNSUITE(ID) ON DELETE CASCADE,
    EXECUTION           INTEGER                     REFERENCES TEST_RUNEXECUTION(ID) ON DELETE CASCADE,
    FOREIGN KEY (ID)    REFERENCES                  CASSINI_OBJECT (OBJECT_ID) ON DELETE CASCADE,
    TESTCASEID          INTEGER
);


CREATE TABLE TEST_RUNINPUTPARAM (
    ID                          INTEGER             NOT NULL PRIMARY KEY,
    TCASE                       INTEGER             NOT NULL REFERENCES TEST_RUNCASE (ID) ON DELETE CASCADE,
    DATA_TYPE                   DATATYPE            NOT NULL,
    NAME                        TEXT                NOT NULL,
    DESCRIPTION                 TEXT
);

CREATE TABLE TEST_RUNOUTPUTPARAM (
    ID                          INTEGER             NOT NULL PRIMARY KEY,
    TCASE                       INTEGER             NOT NULL REFERENCES TEST_RUNCASE (ID) ON DELETE CASCADE,
    DATA_TYPE                   DATATYPE            NOT NULL,
    NAME                        TEXT                NOT NULL,
    DESCRIPTION                 TEXT
);

CREATE TABLE TEST_RUNINPUTPARAMVALUE (
    ID                          INTEGER             NOT NULL PRIMARY KEY,
    TCASE                       INTEGER             NOT NULL REFERENCES TEST_RUNCASE (ID) ON DELETE CASCADE,
    INPUTPARAM                  INTEGER             NOT NULL REFERENCES TEST_RUNINPUTPARAM (ID) ON DELETE CASCADE,
    STRING_VALUE                TEXT                ,
    INTEGER_VALUE               INTEGER             ,
    DOUBLE_VALUE                DOUBLE PRECISION    ,
    DATE_VALUE                  TIMESTAMP           ,
    TIME_VALUE                  TIME                ,
    TIMESTAMP_VALUE             TIMESTAMP           ,
    BOOLEAN_VALUE               BOOLEAN             ,
    UNIQUE (TCASE, INPUTPARAM)
);

CREATE TABLE TEST_RUNOUTPUTPARAMEXPECTEDVALUE (
    ID                          INTEGER             NOT NULL PRIMARY KEY,
    TCASE                       INTEGER             NOT NULL REFERENCES TEST_RUNCASE (ID) ON DELETE CASCADE,
    OUTPUTPARAM                 INTEGER             NOT NULL REFERENCES TEST_RUNOUTPUTPARAM (ID) ON DELETE CASCADE,
    STRING_VALUE                TEXT                ,
    INTEGER_VALUE               INTEGER             ,
    DOUBLE_VALUE                DOUBLE PRECISION    ,
    DATE_VALUE                  TIMESTAMP           ,
    TIME_VALUE                  TIME                ,
    TIMESTAMP_VALUE             TIMESTAMP           ,
    BOOLEAN_VALUE               BOOLEAN             ,
    UNIQUE (TCASE, OUTPUTPARAM)
);

CREATE TABLE TEST_RUNOUTPUTPARAMACTUALVALUE (
    ID                          INTEGER             NOT NULL PRIMARY KEY,
    TCASE                       INTEGER             NOT NULL REFERENCES TEST_RUNCASE (ID) ON DELETE CASCADE,
    OUTPUTPARAM                 INTEGER             NOT NULL REFERENCES TEST_RUNOUTPUTPARAM (ID) ON DELETE CASCADE,
    STRING_VALUE                TEXT                ,
    INTEGER_VALUE               INTEGER             ,
    DOUBLE_VALUE                DOUBLE PRECISION    ,
    DATE_VALUE                  TIMESTAMP           ,
    TIME_VALUE                  TIME                ,
    TIMESTAMP_VALUE             TIMESTAMP           ,
    BOOLEAN_VALUE               BOOLEAN             ,
    UNIQUE (TCASE, OUTPUTPARAM)
);

CREATE TABLE TEST_RUNOUTPUTLOG (
    ID                          INTEGER             NOT NULL PRIMARY KEY,
    RUN                         INTEGER             NOT NULL REFERENCES TEST_RUN (ID) ON DELETE CASCADE,
    LOG                         TEXT
);

CREATE TABLE TEST_FILE (
    FILE_ID                     INTEGER             NOT NULL PRIMARY KEY,
    NAME                        TEXT                NOT NULL,
    DESCRIPTION                 TEXT                ,
    VERSION                     INTEGER             DEFAULT 1,
    LATEST                      BOOLEAN             DEFAULT TRUE,
    SIZE                        BIGINT              NOT NULL,
    FOREIGN KEY (FILE_ID)       REFERENCES          CASSINI_OBJECT (OBJECT_ID) ON DELETE CASCADE
);

CREATE TABLE TEST_RUNFILE (
    FILE_ID                     INTEGER             NOT NULL PRIMARY KEY,
    TEST_RUN                    INTEGER             NOT NULL REFERENCES TEST_RUN (ID) ON DELETE CASCADE,
    FOREIGN KEY (FILE_ID)       REFERENCES          TEST_FILE (FILE_ID) ON DELETE CASCADE
);
CREATE TABLE TEST_RUNCASEFILE (
    FILE_ID                     INTEGER             NOT NULL PRIMARY KEY,
    TEST_RUNCASE                INTEGER             NOT NULL REFERENCES TEST_RUNCASE (ID) ON DELETE CASCADE,
    FOREIGN KEY (FILE_ID)       REFERENCES          TEST_FILE (FILE_ID) ON DELETE CASCADE
);
-- endregion

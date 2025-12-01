-- <editor-fold desc="Requirements Management Tables">
CREATE TABLE RM_OBJECTTYPE (
    ID                          INTEGER             NOT NULL PRIMARY KEY,
    NAME                        TEXT                 NOT NULL,
    DESCRIPTION                 TEXT                 ,
    NUMBER_SOURCE               INTEGER             REFERENCES AUTONUMBER (AUTONUMBER_ID) ON DELETE CASCADE,
    REVISION_SEQUENCE           INTEGER             REFERENCES LOV (LOV_ID) ON DELETE CASCADE,
    LIFECYCLE                   INTEGER             REFERENCES PLM_LIFECYCLE (ID) ON DELETE CASCADE,
    FOREIGN KEY (ID)            REFERENCES          CASSINI_OBJECT (OBJECT_ID) ON DELETE CASCADE
);

CREATE TABLE RM_SPECIFICATIONTYPE (
    ID                          INTEGER             NOT NULL PRIMARY KEY,
    PARENTTYPE                  INTEGER          NULL REFERENCES RM_SPECIFICATIONTYPE (ID) ON DELETE CASCADE,
    FOREIGN KEY (ID)            REFERENCES          RM_OBJECTTYPE (ID) ON DELETE CASCADE
);

CREATE TABLE RM_REQUIREMENTTYPE (
    ID                          INTEGER             NOT NULL PRIMARY KEY,
    PARENTTYPE                      INTEGER          NULL REFERENCES RM_REQUIREMENTTYPE (ID) ON DELETE CASCADE,
    FOREIGN KEY (ID)            REFERENCES          RM_OBJECTTYPE (ID) ON DELETE CASCADE
);

CREATE TABLE RM_OBJECTTYPEATTRIBUTE (
    ATTRIBUTE_ID                 INTEGER           NOT NULL PRIMARY KEY,
    RM_OBJECTTYPE                  INTEGER           NOT NULL REFERENCES RM_OBJECTTYPE (ID) ON DELETE CASCADE,
    REVISION_SPECIFIC             BOOLEAN           NOT NULL DEFAULT FALSE,
    CHANGE_CONTROLLED             BOOLEAN           NOT NULL DEFAULT FALSE,
    SEQ                           INTEGER           ,
    FOREIGN KEY (ATTRIBUTE_ID)    REFERENCES        OBJECTTYPEATTRIBUTE (ATTRIBUTE_ID) ON DELETE CASCADE
);

CREATE TABLE RM_OBJECT (
    ID                            INTEGER           NOT NULL PRIMARY KEY,
    OBJECT_NUMBER                 TEXT              NOT NULL,
    NAME                          TEXT              NOT NULL DEFAULT '',
    DESCRIPTION                   TEXT              ,
    IS_RELEASED                   BOOLEAN            NOT NULL DEFAULT FALSE,
    RELEASED_DATE                 TIMESTAMP           ,
    LATEST                        BOOLEAN             DEFAULT FALSE,
    LATEST_REVISION               INTEGER             REFERENCES RM_OBJECT(ID) ON DELETE CASCADE,
    LATEST_RELEASED_REVISION      INTEGER REFERENCES   RM_OBJECT(ID) ON DELETE CASCADE,
    FOREIGN KEY (ID)              REFERENCES          CASSINI_OBJECT (OBJECT_ID) ON DELETE CASCADE
);

CREATE TABLE RM_SPECIFICATION (
    ID                            INTEGER           NOT NULL PRIMARY KEY,
    REVISION                      TEXT              NOT NULL,
    LIFECYCLE_PHASE               INTEGER           NOT NULL REFERENCES PLM_LIFECYCLEPHASE (ID),
    TYPE                          INTEGER           NOT NULL REFERENCES RM_SPECIFICATIONTYPE (ID) ON DELETE CASCADE,
    WORKFLOW                      INTEGER           REFERENCES PLM_WORKFLOW (ID) ON DELETE SET NULL,
    FOREIGN KEY (ID)              REFERENCES         RM_OBJECT (ID) ON DELETE CASCADE
);

CREATE TABLE RM_REQUIREMENT (
    ID                            INTEGER           NOT NULL PRIMARY KEY,
    TYPE                          INTEGER            NOT NULL REFERENCES RM_REQUIREMENTTYPE (ID) ON DELETE CASCADE,
    SPECIFICATION                 INTEGER           NOT NULL REFERENCES RM_SPECIFICATION (ID) ON DELETE CASCADE,
    VERSION                       INTEGER           NOT NULL,
    ASSIGNED_TO                   INTEGER           REFERENCES PERSON (PERSON_ID) ON DELETE CASCADE,
    STATUS                       REQUIREMENT_STATUS     NOT NULL DEFAULT 'NONE',
    PLANNED_FINISH_DATE          TIMESTAMP           ,
    ACTUAL_FINISH_DATE           TIMESTAMP           ,
    WORKFLOW                     INTEGER             REFERENCES PLM_WORKFLOW (ID) ON DELETE SET NULL,
    FOREIGN KEY (ID)             REFERENCES          RM_OBJECT (ID) ON DELETE CASCADE
);

CREATE TABLE RM_REQUIREMENTEDIT (
    ID                              INTEGER           NOT NULL PRIMARY KEY,
    REQUIREMENT                     INTEGER         NOT NULL REFERENCES RM_REQUIREMENT (ID) ON DELETE CASCADE,
    STATUS                          REQUIREMENT_EDIT_STATUS NOT NULL DEFAULT 'NEW',
    EDITED_NAME                     TEXT            NOT NULL,
    EDITED_DESCRIPTION              TEXT            ,
    VERSION                         INTEGER         NOT NULL,
    EDIT_NOTES                      TEXT            ,
    EDITED_BY                       INTEGER         NOT NULL REFERENCES PERSON (PERSON_ID) ON DELETE CASCADE,
    EDITED_DATE                     TIMESTAMP       NOT NULL,
    REJECTED_DATE                   TIMESTAMP       ,
    ACCEPTED_DATE                   TIMESTAMP       ,
    FINAL_DATE                      TIMESTAMP       ,
    LATEST                          BOOLEAN         NOT NULL DEFAULT FALSE
);


CREATE TABLE RM_OBJECTATTRIBUTE (
    OBJECT                        INTEGER             NOT NULL REFERENCES RM_OBJECT (ID) ON DELETE CASCADE,
    ATTRIBUTE                   INTEGER             NOT NULL REFERENCES RM_OBJECTTYPEATTRIBUTE (ATTRIBUTE_ID) ON DELETE CASCADE,
    FOREIGN KEY (OBJECT, ATTRIBUTE)          REFERENCES          OBJECTATTRIBUTE (OBJECT_ID, ATTRIBUTEDEF)  ON DELETE CASCADE,
    UNIQUE (OBJECT, ATTRIBUTE)
);

CREATE TABLE RM_OBJECTFILE (
    ID                     INTEGER             NOT NULL PRIMARY KEY,
    OBJECT                        INTEGER             NOT NULL REFERENCES RM_OBJECT (ID) ON DELETE CASCADE,
    FOREIGN KEY (ID)       REFERENCES          PLM_FILE (FILE_ID) ON DELETE CASCADE
);


CREATE TABLE RM_RELATIONSHIP (
    ID                          INTEGER             NOT NULL PRIMARY KEY,
    NAME                        TEXT                NOT NULL,
    DESCRIPTION                 TEXT                ,
    FROM_TYPE                   INTEGER             NOT NULL REFERENCES RM_OBJECTTYPE(ID) ON DELETE CASCADE,
    TO_TYPE                     INTEGER             NOT NULL REFERENCES RM_OBJECTTYPE(ID) ON DELETE CASCADE,
    FOREIGN KEY (ID)            REFERENCES          CASSINI_OBJECT (OBJECT_ID) ON DELETE CASCADE
);

CREATE TABLE RM_RELATEDITEM (
    ID                          INTEGER             NOT NULL PRIMARY KEY,
    FROM_OBJECT                   INTEGER             NOT NULL REFERENCES RM_OBJECT(ID) ON DELETE CASCADE,
    TO_OBJECT                     INTEGER             NOT NULL REFERENCES RM_OBJECT(ID) ON DELETE CASCADE,
    RELATIONSHIP                INTEGER             NOT NULL REFERENCES RM_RELATIONSHIP(ID) ON DELETE CASCADE,
    NOTES                       TEXT                ,
    FOREIGN KEY (ID)            REFERENCES          CASSINI_OBJECT(OBJECT_ID) ON DELETE CASCADE
);

CREATE TABLE RM_SPECELEMENT (
    ID                      INTEGER             NOT NULL PRIMARY KEY,
    SEQ_NUMBER              TEXT                NOT NULL,
    SPECIFICATION           INTEGER             NOT NULL REFERENCES RM_SPECIFICATION (ID) ON DELETE CASCADE,
    TYPE                    SPEC_ELEMENT_TYPE    NOT NULL DEFAULT 'SECTION',
    PARENT                  INTEGER             REFERENCES RM_SPECELEMENT (ID) ON DELETE CASCADE,
    FOREIGN KEY (ID)       REFERENCES          CASSINI_OBJECT(OBJECT_ID) ON DELETE CASCADE,
    UNIQUE (SPECIFICATION,SEQ_NUMBER)
);

CREATE TABLE RM_SPECSECTION (
    ID                      INTEGER             NOT NULL PRIMARY KEY,
    NAME                    TEXT                NOT NULL,
    DESCRIPTION             TEXT                ,
    CANADDSECTION           BOOLEAN             NOT NULL DEFAULT TRUE ,
    CANADDREQUIREMENT       BOOLEAN             NOT NULL DEFAULT TRUE,
    FOREIGN KEY (ID)       REFERENCES          RM_SPECELEMENT (ID) ON DELETE CASCADE
);

CREATE TABLE RM_SPECREQUIREMENT (
    ID                      INTEGER             NOT NULL PRIMARY KEY,
    REQUIREMENT             INTEGER             NOT NULL REFERENCES RM_REQUIREMENT (ID) ON DELETE CASCADE,
    FOREIGN KEY (ID)       REFERENCES          RM_SPECELEMENT (ID) ON DELETE CASCADE
);
CREATE TABLE RM_SPECIFICATIONDELIVERABLE (
    ID                          INTEGER             NOT NULL PRIMARY KEY,
    OBJECT_ID                   INTEGER             NOT NULL,
    OBJECT_TYPE                 TEXT                NOT NULL,
    SPECIFICATION               INTEGER             NOT NULL REFERENCES RM_OBJECT (ID) ON DELETE CASCADE
);
CREATE TABLE RM_REQUIREMENTDELIVERABLE (
    ID                          INTEGER             NOT NULL PRIMARY KEY,
    OBJECT_ID                   INTEGER             NOT NULL,
    OBJECT_TYPE                 TEXT                NOT NULL,
    REQUIREMENT                 INTEGER             NOT NULL REFERENCES RM_OBJECT (ID) ON DELETE CASCADE
);

CREATE TABLE RM_REQUIREMENTREVISIONHISTORY (
    ID                          INTEGER             NOT NULL PRIMARY KEY,
    REQUIREMENT                 INTEGER             NOT NULL REFERENCES RM_OBJECT (ID) ON DELETE CASCADE,
    FROM_REVISION               TEXT                NOT NULL,
    TO_REVISION                 TEXT                NOT NULL,
    TIMESTAMP                   TIMESTAMP           NOT NULL,
    UPDATED_BY                  INTEGER             NOT NULL REFERENCES PERSON (PERSON_ID) ON DELETE CASCADE
);

CREATE TABLE RM_REQUIREMENTREVISIONSTATUSHISTORY (
    ID                          INTEGER             NOT NULL PRIMARY KEY,
    REQUIREMENT                 INTEGER             NOT NULL REFERENCES RM_OBJECT (ID) ON DELETE CASCADE,
    REVISION                    TEXT                NOT NULL,
    FROM_STATUS                 INTEGER             NOT NULL REFERENCES PLM_LIFECYCLEPHASE (ID),
    TO_STATUS                   INTEGER             NOT NULL REFERENCES PLM_LIFECYCLEPHASE (ID),
    TIMESTAMP                   TIMESTAMP           NOT NULL,
    UPDATED_BY                  INTEGER             NOT NULL REFERENCES PERSON (PERSON_ID) ON DELETE CASCADE,
    NOTES                       TEXT
);


CREATE TABLE RM_SPECIFICATIONREVISIONHISTORY (
    ID                          INTEGER             NOT NULL PRIMARY KEY,
    SPECIFICATION               INTEGER             NOT NULL REFERENCES RM_OBJECT (ID) ON DELETE CASCADE,
    FROM_REVISION               TEXT                NOT NULL,
    TO_REVISION                 TEXT                NOT NULL,
    TIMESTAMP                   TIMESTAMP           NOT NULL,
    UPDATED_BY                  INTEGER             NOT NULL REFERENCES PERSON (PERSON_ID) ON DELETE CASCADE
);

CREATE TABLE RM_SPECIFICATIONREVISIONSTATUSHISTORY (
    ID                          INTEGER             NOT NULL PRIMARY KEY,
    SPECIFICATION               INTEGER             NOT NULL REFERENCES RM_OBJECT (ID) ON DELETE CASCADE,
    REVISION                    TEXT                NOT NULL,
    FROM_STATUS                 INTEGER             NOT NULL REFERENCES PLM_LIFECYCLEPHASE (ID),
    TO_STATUS                   INTEGER             NOT NULL REFERENCES PLM_LIFECYCLEPHASE (ID),
    TIMESTAMP                   TIMESTAMP           NOT NULL,
    UPDATED_BY                  INTEGER             NOT NULL REFERENCES PERSON (PERSON_ID) ON DELETE CASCADE,
    NOTES                       TEXT
);


CREATE TABLE RM_SPECPERMISSION (
    ID                           INTEGER             NOT NULL PRIMARY KEY,
    SPECIFICATION                INTEGER             NOT NULL REFERENCES RM_SPECIFICATION (ID) ON DELETE CASCADE,
    SPECUSER                     INTEGER             NOT NULL REFERENCES PERSON (PERSON_ID) ON DELETE CASCADE,
    EDIT_PERMISSION              BOOLEAN             NOT NULL DEFAULT FALSE,
    DELETE_PERMISSION            BOOLEAN             NOT NULL DEFAULT FALSE,
    ACCEPT_REJECT_PERMISSION     BOOLEAN             NOT NULL DEFAULT FALSE,
    STATUS_CHANGE_PERMISSION     BOOLEAN             NOT NULL DEFAULT FALSE,
    IMPORT_PERMISSION            BOOLEAN             NOT NULL DEFAULT FALSE,
    EXPORT_PERMISSION            BOOLEAN             NOT NULL DEFAULT FALSE
);

-- </editor-fold>

-- <editor-fold desc="Glossary Tables">
CREATE TABLE PLM_GLOSSARYLANGUAGES (
   ID                          INTEGER            NOT NULL PRIMARY KEY ,
   CODE                        TEXT               ,
   LANGUAGE                    TEXT               NOT NULL,
   CREATED_DATE                TIMESTAMP          DEFAULT CURRENT_TIMESTAMP,
   DEFAULT_LANGUAGE            BOOLEAN            DEFAULT FALSE,
   UNIQUE (LANGUAGE)
);

CREATE TABLE PLM_GLOSSARY (
    ID                          INTEGER                 NOT NULL PRIMARY KEY,
    GLOSSARY_NUMBER             TEXT                    NOT NULL,
    NAME                        TEXT                    ,
    DESCRIPTION                 TEXT                    ,
    REVISION                    TEXT                    NOT NULL,
    LIFECYCLE_PHASE             INTEGER                 NOT NULL REFERENCES PLM_LIFECYCLEPHASE (ID),
    IS_RELEASED                 BOOLEAN                 NOT NULL DEFAULT FALSE,
    RELEASED_DATE               TIMESTAMP               ,
    LATEST                      BOOLEAN                 DEFAULT FALSE,
    DEFAULT_LANGUAGE            INTEGER                 REFERENCES PLM_GLOSSARYLANGUAGES(ID) ON DELETE CASCADE,
    FOREIGN KEY (ID)            REFERENCES              CASSINI_OBJECT (OBJECT_ID) ON DELETE CASCADE
);

CREATE TABLE PLM_GLOSSARYREVISIONHISTORY (
    ID                          INTEGER             NOT NULL PRIMARY KEY,
    GLOSSARY                    INTEGER             NOT NULL REFERENCES PLM_GLOSSARY (ID) ON DELETE CASCADE,
    FROM_REVISION               TEXT                NOT NULL,
    TO_REVISION                 TEXT                NOT NULL,
    TIMESTAMP                   TIMESTAMP           NOT NULL,
    UPDATED_BY                  INTEGER             NOT NULL REFERENCES PERSON (PERSON_ID) ON DELETE CASCADE
);

CREATE TABLE PLM_GLOSSARYREVISIONSTATUSHISTORY (
    ID                          INTEGER             NOT NULL PRIMARY KEY,
    GLOSSARY                    INTEGER             NOT NULL REFERENCES PLM_GLOSSARY (ID) ON DELETE CASCADE,
    REVISION                    TEXT                NOT NULL,
    FROM_STATUS                 INTEGER             NOT NULL REFERENCES PLM_LIFECYCLEPHASE (ID),
    TO_STATUS                   INTEGER             NOT NULL REFERENCES PLM_LIFECYCLEPHASE (ID),
    TIMESTAMP                   TIMESTAMP           NOT NULL,
    UPDATED_BY                  INTEGER             NOT NULL REFERENCES PERSON (PERSON_ID) ON DELETE CASCADE,
    NOTES                       TEXT
);

CREATE TABLE PLM_GLOSSARYENTRY (
    ID                          INTEGER                 NOT NULL PRIMARY KEY,
    NAME                        TEXT                    ,
    DESCRIPTION                 TEXT                    ,
    VERSION                     INTEGER                 NOT NULL,
    LATEST                      BOOLEAN                 DEFAULT FALSE,
    DEFAULT_NAME                TEXT                    ,
    DEFAULT_DESCRIPTION         TEXT                    ,
    NOTES                       TEXT                    ,
    FOREIGN KEY (ID)            REFERENCES              CASSINI_OBJECT (OBJECT_ID) ON DELETE CASCADE
);

CREATE TABLE PLM_GLOSSARYENTRYEDIT (
    ID                          INTEGER                 NOT NULL PRIMARY KEY,
    ENTRY                       INTEGER                 NOT NULL REFERENCES PLM_GLOSSARYENTRY (ID) ON DELETE CASCADE,
    EDITED_DESCRIPTION          TEXT                    NOT NULL,
    EDITED_NOTES                TEXT                    ,
    EDITED_BY                   INTEGER                 NOT NULL REFERENCES PERSON(PERSON_ID) ON DELETE CASCADE,
    STATUS                      GLOSSARY_ENTRY_EDIT_STATUS  NOT NULL DEFAULT 'NONE',
    LANGUAGE                    INTEGER                 NOT NULL REFERENCES PLM_GLOSSARYLANGUAGES(ID) ON DELETE CASCADE,
    UPDATED_DATE                TIMESTAMP               NOT NULL DEFAULT CURRENT_TIMESTAMP,
    ACCEPTED_DATE               TIMESTAMP               ,
    EDIT_VERSION                INTEGER                 ,
    LATEST                      BOOLEAN                 DEFAULT FALSE
);

CREATE TABLE PLM_GLOSSARYENTRYHISTORY (
    ID                          INTEGER             NOT NULL PRIMARY KEY,
    ENTRY                       INTEGER             NOT NULL REFERENCES PLM_GLOSSARYENTRY (ID) ON DELETE CASCADE,
    FROM_VERSION                INTEGER             ,
    TO_VERSION                  INTEGER             NOT NULL,
    TIMESTAMP                   TIMESTAMP           NOT NULL,
    UPDATED_BY                  INTEGER             NOT NULL REFERENCES PERSON (PERSON_ID) ON DELETE CASCADE,
    NOTES                       TEXT
);

CREATE TABLE PLM_GLOSSARYENTRYITEM (
    ID                          INTEGER                 NOT NULL PRIMARY KEY,
    GLOSSARY                    INTEGER                 NOT NULL REFERENCES PLM_GLOSSARY (ID) ON DELETE CASCADE,
    ENTRY                       INTEGER                 NOT NULL REFERENCES PLM_GLOSSARYENTRY (ID) ON DELETE CASCADE,
    NOTES                       TEXT
);

CREATE TABLE PLM_GLOSSARYFILE (
    FILE_ID                     INTEGER             NOT NULL PRIMARY KEY,
    GLOSSARY                    INTEGER             NOT NULL REFERENCES PLM_GLOSSARY (ID) ON DELETE CASCADE,
    FOREIGN KEY (FILE_ID)       REFERENCES          PLM_FILE (FILE_ID) ON DELETE CASCADE
);
CREATE TABLE PLM_GLOSSARYDELIVERABLE (
    ID                          INTEGER             NOT NULL PRIMARY KEY,
    OBJECT_ID                   INTEGER             NOT NULL,
    OBJECT_TYPE                 TEXT                NOT NULL,
    GLOSSARY                    INTEGER             NOT NULL REFERENCES PLM_GLOSSARY (ID)
);

CREATE TABLE PLM_GLOSSARYDETAILS (
    ID                          INTEGER             NOT NULL PRIMARY KEY,
    LANGUAGE                    INTEGER             NOT NULL REFERENCES PLM_GLOSSARYLANGUAGES (ID) ON DELETE CASCADE,
    NAME                        TEXT                NOT NULL,
    DESCRIPTION                 TEXT                ,
    GLOSSARY                    INTEGER             NOT NULL REFERENCES PLM_GLOSSARY (ID) ON DELETE CASCADE
);

ALTER TABLE PLM_GLOSSARY ADD COLUMN DEFAULT_DETAIL            INTEGER                 REFERENCES PLM_GLOSSARYDETAILS (ID) ON DELETE CASCADE;

CREATE TABLE PLM_GLOSSARYENTRYDETAILS(
    ID                          INTEGER             NOT NULL PRIMARY KEY,
    LANGUAGE                    INTEGER             NOT NULL REFERENCES PLM_GLOSSARYLANGUAGES (ID) ON DELETE CASCADE ,
    NAME                        TEXT                NOT NULL,
    DESCRIPTION                 TEXT                ,
    NOTES                       TEXT                ,
    GLOSSARYENTRY               INTEGER             NOT NULL REFERENCES PLM_GLOSSARYENTRY (ID) ON DELETE CASCADE
);

ALTER TABLE PLM_GLOSSARYENTRY ADD COLUMN DEFAULT_DETAIL            INTEGER                 REFERENCES PLM_GLOSSARYENTRYDETAILS (ID) ON DELETE CASCADE;

CREATE TABLE RECENTLY_VISITED (
    ID                          INTEGER             NOT NULL PRIMARY KEY,
    OBJECT_ID                   INTEGER             NOT NULL  REFERENCES CASSINI_OBJECT (OBJECT_ID) ON DELETE CASCADE,
    OBJECT_TYPE                 TEXT                NOT NULL,
    PERSON                      INTEGER             REFERENCES PERSON (PERSON_ID) ON DELETE CASCADE,
    VISITED_DATE                TIMESTAMP           NOT NULL DEFAULT CURRENT_DATE
);

CREATE TABLE PLM_GLOSSARYENTRYPERMISSION (
    ID                          INTEGER             NOT NULL PRIMARY KEY,
    GLOSSARY                    INTEGER             NOT NULL REFERENCES PLM_GLOSSARY (ID) ON DELETE CASCADE,
    GLOSSARYUSER                INTEGER             NOT NULL REFERENCES PERSON (PERSON_ID) ON DELETE CASCADE,
    EDIT_PERMISSION             BOOLEAN             NOT NULL DEFAULT FALSE,
    DELETE_PERMISSION           BOOLEAN             NOT NULL DEFAULT FALSE,
    ACCEPT_REJECT_PERMISSION    BOOLEAN             NOT NULL DEFAULT FALSE,
    STATUS_CHANGE_PERMISSION    BOOLEAN             NOT NULL DEFAULT FALSE,
    IMPORT_PERMISSION           BOOLEAN             NOT NULL DEFAULT FALSE,
    EXPORT_PERMISSION           BOOLEAN             NOT NULL DEFAULT FALSE
);
-- </editor-fold>
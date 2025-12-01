CREATE TABLE ERP_STORE (
    STORE_ID                    INTEGER             NOT NULL PRIMARY KEY,
    STORE_NAME                  TEXT                NOT NULL,
    DESCRIPTION                 TEXT                ,
    LOCATION_NAME               TEXT                ,
    FOREIGN KEY (STORE_ID)      REFERENCES          CASSINI_OBJECT (OBJECT_ID) ON DELETE CASCADE,
    UNIQUE(STORE_NAME)
);


CREATE TABLE ERP_INVENTORY (
    ID                          INTEGER             NOT NULL PRIMARY KEY,
    STORE                       INTEGER             NOT NULL REFERENCES ERP_STORE (STORE_ID) ON DELETE CASCADE,
    STOCK_ON_HAND               INTEGER             ,
    ITEM                        INTEGER             NOT NULL,
    STOCK_ON_ORDER              INTEGER
);

CREATE TABLE ERP_STOCKMOVEMENT (
    ROWID                       INTEGER             NOT NULL PRIMARY KEY,
    BOQITEM                     INTEGER             NOT NULL REFERENCES ERP_MATERIAL (MATERIAL_ID) ON DELETE CASCADE,
    ITEMNUMBER                  TEXT                ,
    STORE                       INTEGER             NOT NULL REFERENCES ERP_STORE (STORE_ID) ON DELETE CASCADE,
    MOVEMENT                    INVENTORYRECORD_TYPE       NOT NULL,
    QUANTITY                    INTEGER             NOT NULL,
    TIMESTAMP                   TIMESTAMP           NOT NULL,
    RECORDED_BY                 INTEGER             NOT NULL REFERENCES ERP_PERSON (PERSON_ID) ON DELETE CASCADE,
    NOTES                       TEXT
);

CREATE TABLE ERP_STOCKRECEIVE (
    ID                          INTEGER             NOT NULL PRIMARY KEY ,
    NAME                        TEXT                ,
    NOTES                       TEXT                ,
    STORE                       INTEGER             NOT NULL REFERENCES ERP_STORE (STORE_ID) ON DELETE CASCADE ,
    RECEIVENUMBER_SOURCE        TEXT                ,
    FOREIGN KEY (ID)            REFERENCES          ERP_OBJECT (OBJECT_ID) ON DELETE CASCADE
);

CREATE TABLE ERP_STOCKISSUE (
    ID                          INTEGER             NOT NULL PRIMARY KEY ,
    NAME                        TEXT                ,
    NOTES                       TEXT                ,
    STORE                       INTEGER             NOT NULL REFERENCES ERP_STORE (STORE_ID) ON DELETE CASCADE ,
    ISSUENUMBER_SOURCE          TEXT                ,
    ISSUED_TO                   INTEGER             REFERENCES ERP_PERSON (PERSON_ID) ON DELETE CASCADE,
    ISSUE_DATE                  TIMESTAMP           ,
    FOREIGN KEY (ID)            REFERENCES          CASSINI_OBJECT (OBJECT_ID) ON DELETE CASCADE
);

CREATE TABLE ERP_STOCKISSUEITEM (
    ROWID                       INTEGER             NOT NULL PRIMARY KEY ,
    ISSUE                       INTEGER             REFERENCES ERP_STOCKISSUE (ID) ON DELETE CASCADE ,
    ISSUED_BY                   INTEGER             NOT NULL REFERENCES ERP_PERSON (PERSON_ID) ON DELETE CASCADE,
    FOREIGN KEY (ROWID)         REFERENCES          ERP_STOCKMOVEMENT (ROWID) ON DELETE CASCADE
);

CREATE TABLE ERP_STOCKRECEIVEITEM (
    ROWID                       INTEGER             NOT NULL PRIMARY KEY,
    RECEIVE                     INTEGER             REFERENCES ERP_STOCKRECEIVE (ID) ON DELETE CASCADE ,
    RECEIVED_BY                 INTEGER             NOT NULL REFERENCES ERP_PERSON (PERSON_ID) ON DELETE CASCADE,
    FOREIGN KEY (ROWID)         REFERENCES          ERP_STOCKMOVEMENT (ROWID) ON DELETE CASCADE
);
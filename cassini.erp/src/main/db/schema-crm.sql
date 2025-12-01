/* Begin CRM tables */
CREATE TABLE ERP_SALESREP (
    SALESREP_ID                 INTEGER             NOT NULL PRIMARY KEY,
    FOREIGN KEY (SALESREP_ID)   REFERENCES          ERP_EMPLOYEE (EMPLOYEE_ID) ON DELETE CASCADE
);

CREATE TABLE ERP_SALESREGION (
    REGION_ID                   INTEGER             NOT NULL PRIMARY KEY,
    NAME                        TEXT                NOT NULL,
    DISTRICT                    TEXT                NOT NULL,
    STATE_ID                    INTEGER             NOT NULL REFERENCES ERP_STATE (STATE_ID) ON DELETE CASCADE,
    COUNTRY_ID                  INTEGER             NOT NULL REFERENCES ERP_COUNTRY (COUNTRY_ID) ON DELETE CASCADE,
    FOREIGN KEY (REGION_ID)     REFERENCES          ERP_OBJECT (OBJECT_ID) ON DELETE CASCADE
);

CREATE TABLE ERP_CUSTOMERTYPE (
    TYPE_ID                     INTEGER             NOT NULL PRIMARY KEY,
    NAME                        TEXT                NOT NULL UNIQUE,
    DESCRIPTION                 TEXT
);


CREATE TABLE ERP_CUSTOMER (
    CUSTOMER_ID                 INTEGER             NOT NULL PRIMARY KEY,
    BUSINESS_UNIT               INTEGER             REFERENCES ERP_BUSINESSUNIT(BUSINESSUNIT_ID),
    NAME                        TEXT                NOT NULL,
    CUSTOMER_TYPE               INTEGER             NOT NULL REFERENCES ERP_CUSTOMERTYPE (TYPE_ID),
    REGION                      INTEGER             NOT NULL REFERENCES ERP_SALESREGION (REGION_ID),
    SALESREP                    INTEGER             REFERENCES ERP_SALESREP (SALESREP_ID),
    CONTACT_PERSON              INTEGER             REFERENCES ERP_PERSON (PERSON_ID),
    OFFICE_PHONE                TEXT                ,
    OFFICE_FAX                  TEXT                ,
    OFFICE_EMAIL                TEXT                ,
    BLACKLISTED                 BOOLEAN             DEFAULT FALSE,
    FOREIGN KEY (CUSTOMER_ID)   REFERENCES          ERP_OBJECT (OBJECT_ID) ON DELETE CASCADE
);

CREATE TABLE ERP_CUSTOMERADDRESS (
    CUSTOMER_ID                 INTEGER             NOT NULL REFERENCES ERP_CUSTOMER (CUSTOMER_ID),
    ADDRESS_ID                  INTEGER             NOT NULL REFERENCES ERP_ADDRESS (ADDRESS_ID),
    UNIQUE (CUSTOMER_ID, ADDRESS_ID)
);

CREATE TABLE ERP_CUSTOMERORDER (
    ORDER_ID                    INTEGER             NOT NULL PRIMARY KEY,
    ORDER_TYPE                  ORDER_TYPE          NOT NULL,
    ORDER_NUMBER                VARCHAR(50)         NOT NULL UNIQUE,
    CUSTOMER_ID                 INTEGER             NOT NULL REFERENCES ERP_CUSTOMER (CUSTOMER_ID),
    ORDERED_DATE                TIMESTAMP           NOT NULL,
    ORDERED_BY                  INTEGER             NOT NULL REFERENCES ERP_PERSON (PERSON_ID),
    DELIVERY_DATE               DATE                ,
    ORDER_TOTAL                 DOUBLE PRECISION    NOT NULL,
    STATUS                      CUSTOMERORDER_STATUS  NOT NULL,
    APPLIED_DISCOUNT            DOUBLE PRECISION    ,
    NET_TOTAL                   DOUBLE PRECISION    ,
    SHIP_TO                     TEXT                ,
    SHIPPING_ADDRESS            INTEGER             REFERENCES ERP_ADDRESS (ADDRESS_ID) ON DELETE CASCADE,
    BILLING_ADDRESS             INTEGER             REFERENCES ERP_ADDRESS (ADDRESS_ID) ON DELETE CASCADE,
    CONTACT_PHONE               TEXT                ,
    PO_NUMBER                   TEXT                ,
    REFERENCE                   TEXT                ,
    NOTES                       TEXT                ,
    STARRED                     BOOLEAN             DEFAULT FALSE,
    ONHOLD                      BOOLEAN             DEFAULT FALSE,
    FOREIGN KEY (ORDER_ID)      REFERENCES          ERP_OBJECT (OBJECT_ID) ON DELETE CASCADE
);

CREATE TABLE ERP_CUSTOMERORDERSTATUSHISTORY (
    ROWID                       INTEGER             NOT NULL PRIMARY KEY,
    ORDER_ID                    INTEGER             NOT NULL REFERENCES ERP_CUSTOMERORDER (ORDER_ID),
    MODIFIED_DATE               TIMESTAMP           NOT NULL,
    MODIFIED_BY                 INTEGER             NOT NULL REFERENCES ERP_PERSON (PERSON_ID),
    OLD_STATUS                  CUSTOMERORDER_STATUS,
    NEW_STATUS                  CUSTOMERORDER_STATUS NOT NULL
);

CREATE TABLE ERP_CUSTOMERORDERDETAILS (
    ROWID                       INTEGER             NOT NULL PRIMARY KEY,
    SERIAL_NUMBER               INTEGER             ,
    ORDER_ID                    INTEGER             NOT NULL REFERENCES ERP_CUSTOMERORDER (ORDER_ID),
    PRODUCT_ID                  INTEGER             NOT NULL REFERENCES ERP_PRODUCT (PRODUCT_ID),
    QUANTITY                    INTEGER             NOT NULL,
    QUANTITY_SHIPPED            INTEGER             ,
    BOXES                       INTEGER             ,
    UNIT_PRICE                  DOUBLE PRECISION    NOT NULL,
    DISCOUNT                    DOUBLE PRECISION    ,
    ITEM_TOTAL                  DOUBLE PRECISION
);

CREATE TABLE ERP_ORDERVERIFICATION (
    VERIFICATION_ID             INTEGER             NOT NULL PRIMARY KEY,
    ORDER_ID                    INTEGER             NOT NULL,
    ASSIGNED_DATE               TIMESTAMP           NOT NULL,
    ASSIGNED_BY                 INTEGER             REFERENCES ERP_EMPLOYEE (EMPLOYEE_ID) ON DELETE CASCADE,
    VERIFIED_DATE               TIMESTAMP           ,
    VERIFIED_BY                 INTEGER             REFERENCES ERP_EMPLOYEE (EMPLOYEE_ID) ON DELETE CASCADE,
    STATUS                      ORDERVERIFICATION_STATUS NOT NULL
);


CREATE TABLE ERP_SHIPPER (
    SHIPPER_ID                  INTEGER             NOT NULL PRIMARY KEY,
    NAME                        TEXT                NOT NULL,
    ADDRESS                     INTEGER             REFERENCES ERP_ADDRESS (ADDRESS_ID) ON DELETE CASCADE,
    OFFICE_PHONE                TEXT                ,
    OFFICE_FAX                  TEXT                ,
    OFFICE_EMAIL                TEXT                ,
    FOREIGN KEY (SHIPPER_ID)    REFERENCES          ERP_OBJECT (OBJECT_ID) ON DELETE CASCADE
);


CREATE TABLE ERP_VEHICLE (
    VEHICLE_ID                  INTEGER             NOT NULL PRIMARY KEY,
    NUMBER                      TEXT                NOT NULL,
    DESCRIPTION                 TEXT                ,
    UNIQUE (NUMBER)                                 ,
    FOREIGN KEY (VEHICLE_ID)    REFERENCES          ERP_OBJECT (OBJECT_ID) ON DELETE CASCADE
);

CREATE TABLE ERP_CONSIGNMENT (
    CONSIGNMENT_ID              INTEGER             NOT NULL PRIMARY KEY,
    NUMBER                      TEXT                NOT NULL,
    CONSIGNEE                   TEXT                NOT NULL,
    PAID_BY                     PAID_BY             ,
    DELIVERY_MODE               DELIVERY_MODE       ,
    SHIPPING_COST               DOUBLE PRECISION    ,
    SHIPPING_ADDRESS            INTEGER             NOT NULL REFERENCES ERP_ADDRESS (ADDRESS_ID) ON DELETE CASCADE,
    SHIPPER                     INTEGER             NOT NULL REFERENCES ERP_SHIPPER (SHIPPER_ID) ON DELETE CASCADE,
    STATUS                      CONSIGNMENT_STATUS  NOT NULL,
    VALUE                       DOUBLE PRECISION    NOT NULL,
    CONTENTS                    TEXT                ,
    DESCRIPTION                 TEXT                ,
    MOBILE_PHONE                TEXT                ,
    OFFICE_PHONE                TEXT                ,
    THROUGH                     TEXT                ,
    SHIPPED_DATE                TIMESTAMP           ,
    SHIPPED_BY                  INTEGER             REFERENCES ERP_EMPLOYEE (EMPLOYEE_ID) ON DELETE CASCADE,
    CONFIRMATION_NUMBER         TEXT                ,
    VEHICLE                     INTEGER             REFERENCES ERP_VEHICLE (VEHICLE_ID) ON DELETE CASCADE,
    DRIVER                      INTEGER             REFERENCES ERP_EMPLOYEE (EMPLOYEE_ID) ON DELETE CASCADE,
    FOREIGN KEY (CONSIGNMENT_ID)     REFERENCES     ERP_OBJECT (OBJECT_ID) ON DELETE CASCADE
);

CREATE TABLE ERP_CUSTOMERORDERSHIPMENT (
    SHIPMENT_ID                 INTEGER             NOT NULL PRIMARY KEY,
    ORDER_ID                    INTEGER             NOT NULL REFERENCES ERP_CUSTOMERORDER (ORDER_ID) ON DELETE CASCADE,
    STATUS                      SHIPMENT_STATUS     ,
    DISCOUNT                    DOUBLE PRECISION    ,
    SPECIAL_DISCOUNT            DOUBLE PRECISION    ,
    INVOICE_AMOUNT              DOUBLE PRECISION    NOT NULL,
    INVOICE_NUMBER              TEXT                ,
    SHIPPING_ADDRESS            INTEGER             REFERENCES ERP_ADDRESS (ADDRESS_ID) ON DELETE CASCADE,
    BILLING_ADDRESS             INTEGER             REFERENCES ERP_ADDRESS (ADDRESS_ID) ON DELETE CASCADE,
    NOTES                       TEXT                ,
    SHIPPER                     INTEGER             REFERENCES ERP_SHIPPER (SHIPPER_ID) ON DELETE CASCADE,
    CONSIGNMENT                 INTEGER             REFERENCES ERP_CONSIGNMENT (CONSIGNMENT_ID) ON DELETE CASCADE,
    FOREIGN KEY (SHIPMENT_ID)   REFERENCES          ERP_OBJECT (OBJECT_ID) ON DELETE CASCADE
);

CREATE TABLE ERP_CUSTOMERORDERSHIPMENTDETAILS (
    ROWID                       INTEGER             NOT NULL PRIMARY KEY,
    SERIAL_NUMBER               INTEGER             ,
    SHIPMENT                    INTEGER             NOT NULL REFERENCES ERP_CUSTOMERORDERSHIPMENT(SHIPMENT_ID) ON DELETE CASCADE,
    PRODUCT_ID                  INTEGER             NOT NULL REFERENCES ERP_PRODUCT (PRODUCT_ID) ON DELETE CASCADE,
    QUANTITY_SHIPPED            INTEGER             ,
    BOXES                       INTEGER             ,
    UNIT_PRICE                  DOUBLE PRECISION    NOT NULL,
    DISCOUNT                    DOUBLE PRECISION    ,
    ITEM_TOTAL                  DOUBLE PRECISION
);

CREATE TABLE ERP_CUSTOMERORDERCANCELLATION (
    ROWID                       INTEGER             NOT NULL PRIMARY KEY,
    ORDER_ID                    INTEGER             NOT NULL REFERENCES ERP_CUSTOMERORDER (ORDER_ID) ON DELETE CASCADE,
    REASON                      TEXT                NOT NULL,
    TIMESTAMP                   TIMESTAMP           NOT NULL
);

CREATE TABLE ERP_CUSTOMERRETURN (
    RETURN_ID                   INTEGER             NOT NULL PRIMARY KEY,
    CUSTOMER_ID                 INTEGER             NOT NULL REFERENCES ERP_CUSTOMER (CUSTOMER_ID) ON DELETE CASCADE,
    RETURN_DATE                 TIMESTAMP           NOT NULL,
    STATUS                      RETURN_STATUS       NOT NULL,
    REASON                      TEXT                NOT NULL,
    FOREIGN KEY (RETURN_ID)     REFERENCES          ERP_OBJECT (OBJECT_ID) ON DELETE CASCADE
);

CREATE TABLE ERP_CUSTOMERRETURNDETAILS (
    ROWID                       INTEGER             NOT NULL PRIMARY KEY,
    RETURN_ID                   INTEGER             NOT NULL REFERENCES ERP_CUSTOMERRETURN (RETURN_ID) ON DELETE CASCADE,
    PRODUCT_ID                  INTEGER             NOT NULL,
    QUANTITY                    INTEGER             NOT NULL
);

CREATE TABLE ERP_CUSTOMERODERREMINDER (
    REMINDER_ID                 INTEGER             NOT NULL PRIMARY KEY,
    CUSTOMER_ID                 INTEGER             NOT NULL REFERENCES ERP_CUSTOMER (CUSTOMER_ID) ON DELETE CASCADE,
    NOTE                        TEXT                NOT NULL,
    TIMESTAMP                   TIMESTAMP           NOT NULL,
    DISABLED                    BOOLEAN             NOT NULL DEFAULT FALSE
);

CREATE TABLE ERP_SALESREPFIELDREPORT (
    REPORT_ID                   INTEGER             NOT NULL PRIMARY KEY,
    SALESREP                    INTEGER             NOT NULL REFERENCES ERP_SALESREP(SALESREP_ID) ON DELETE CASCADE,
    CUSTOMER                    INTEGER             NOT NULL REFERENCES ERP_CUSTOMER (CUSTOMER_ID) ON DELETE CASCADE,
    NOTES                       TEXT                NOT NULL,
    TIMESTAMP                   TIMESTAMP           NOT NULL,
    FOREIGN KEY (REPORT_ID)     REFERENCES          ERP_OBJECT (OBJECT_ID) ON DELETE CASCADE
);

/* End CRM tables */


/* Create indexes */
/*CREATE INDEX CUSTOMER_LOCATION_INDEX
    ON ERP_CUSTOMERGEOLOCATION USING gist(ll_to_earth(LATITUDE, LONGITUDE));
*/
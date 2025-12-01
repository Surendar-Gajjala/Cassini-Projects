CREATE TABLE TMS_TRIP(
 TRIP_ID                  INTEGER                   PRIMARY KEY,
 TRIP_DATE                DATE                      NOT  NULL,
 TRIP_NOTES               TEXT                      NOT NULL,
 FOREIGN KEY(TRIP_ID)     REFERENCES                CASSINI_OBJECT(OBJECT_ID) ON  DELETE CASCADE
 );
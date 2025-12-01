DO $$
DECLARE
    partSource       INTEGER;
    lovSequence      INTEGER;
    groupSequence    INTEGER;
    personTypeSeq    INTEGER;

BEGIN

    partSource        := nextval('AUTONUMBER_ID_SEQ');



    /* Autonumbers */

    INSERT INTO AUTONUMBER (AUTONUMBER_ID, NAME, DESCRIPTION, NUMBERS, START, INCREMENT, NEXT_NUMBER, PADWITH, PREFIX, SUFFIX)
    VALUES (partSource, 'Default Complaint Number', 'Default Complaint Number', 5, 1, 1, 1, '0', 'CMP', '');

 lovSequence     := nextval('LOV_ID_SEQ');

INSERT INTO LOV (LOV_ID, TYPE, NAME, DESCRIPTION, VALUES, DEFAULT_VALUE)
VALUES (lovSequence, 'Utilities', 'Utilities', 'Utilities',
        '{AC/Cooler/Fan,Geyser,Lighting,Water Cooler,ChalkBoard,Table/Desk,Seating,Cot & Linen,Cutlery,PA System,PC,Projector,Wifi/Net,Plumbing,Sanitation,Vegtation,Signage}', '');
/*
lovSequence     := nextval('LOV_ID_SEQ');

 INSERT INTO LOV (LOV_ID, TYPE, NAME, DESCRIPTION, VALUES, DEFAULT_VALUE)
    VALUES (lovSequence, 'Services', 'Services', 'Services',
            '{Civil,Electronic,Sanitary}', 'Civil');*/

personTypeSeq := nextval('PERSONTYPE_ID_SEQ');
INSERT INTO PERSONTYPE (TYPE_ID, NAME, DESCRIPTION)
    VALUES (personTypeSeq, 'Trainee', 'Trainee');

personTypeSeq := nextval('PERSONTYPE_ID_SEQ');
INSERT INTO PERSONTYPE (TYPE_ID, NAME, DESCRIPTION)
    VALUES (personTypeSeq, 'Staff', 'Staff');

personTypeSeq := nextval('PERSONTYPE_ID_SEQ');
INSERT INTO PERSONTYPE (TYPE_ID, NAME, DESCRIPTION)
    VALUES (personTypeSeq, 'Faculty', 'Faculty');

personTypeSeq := nextval('PERSONTYPE_ID_SEQ');
INSERT INTO PERSONTYPE (TYPE_ID, NAME, DESCRIPTION)
    VALUES (personTypeSeq, 'Responder', 'Responder');

    personTypeSeq := nextval('PERSONTYPE_ID_SEQ');
INSERT INTO PERSONTYPE (TYPE_ID, NAME, DESCRIPTION)
    VALUES (personTypeSeq, 'Assistor', 'Assistor');

    personTypeSeq := nextval('PERSONTYPE_ID_SEQ');
INSERT INTO PERSONTYPE (TYPE_ID, NAME, DESCRIPTION)
    VALUES (personTypeSeq, 'Facilitator', 'Facilitator');

groupSequence := nextval('OBJECT_ID_SEQ');
INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, OBJECT_TYPE)
    VALUES (groupSequence, default, 1, default, 'LOCATION');
INSERT INTO irste_grouplocation (group_id, name) VALUES (groupSequence, 'Campus');

INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, OBJECT_TYPE)
    VALUES (nextval('OBJECT_ID_SEQ'), default, 1, default, 'LOCATION');
INSERT INTO irste_location (id, group_id, name) VALUES (currval('OBJECT_ID_SEQ'), groupSequence, 'Lawns');

INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Sanitation');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Vegtation');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Signage');


INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, OBJECT_TYPE)
    VALUES (nextval('OBJECT_ID_SEQ'), default, 1, default, 'LOCATION');
INSERT INTO irste_location (id, group_id, name) VALUES (currval('OBJECT_ID_SEQ'), groupSequence, 'OutDoor Lab');

INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Sanitation');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Vegtation');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Signage');


INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, OBJECT_TYPE)
    VALUES (nextval('OBJECT_ID_SEQ'), default, 1, default, 'LOCATION');
INSERT INTO irste_location (id, group_id, name) VALUES (currval('OBJECT_ID_SEQ'), groupSequence, 'Rose Garden');

INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Sanitation');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Vegtation');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Signage');


INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, OBJECT_TYPE)
    VALUES (nextval('OBJECT_ID_SEQ'), default, 1, default, 'LOCATION');
INSERT INTO irste_location (id, group_id, name) VALUES (currval('OBJECT_ID_SEQ'), groupSequence, 'Hostel 1');

INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Sanitation');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Vegtation');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Signage');


INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, OBJECT_TYPE)
    VALUES (nextval('OBJECT_ID_SEQ'), default, 1, default, 'LOCATION');
INSERT INTO irste_location (id, group_id, name) VALUES (currval('OBJECT_ID_SEQ'), groupSequence, 'Hostel 3');

INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Sanitation');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Vegtation');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Signage');


INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, OBJECT_TYPE)
    VALUES (nextval('OBJECT_ID_SEQ'), default, 1, default, 'LOCATION');
INSERT INTO irste_location (id, group_id, name) VALUES (currval('OBJECT_ID_SEQ'), groupSequence, 'Hostel 4');

INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Sanitation');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Vegtation');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Signage');


INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, OBJECT_TYPE)
    VALUES (nextval('OBJECT_ID_SEQ'), default, 1, default, 'LOCATION');
INSERT INTO irste_location (id, group_id, name) VALUES (currval('OBJECT_ID_SEQ'), groupSequence, 'Hostel 2');


INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Sanitation');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Vegtation');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Signage');


INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, OBJECT_TYPE)
    VALUES (nextval('OBJECT_ID_SEQ'), default, 1, default, 'LOCATION');
INSERT INTO irste_location (id, group_id, name) VALUES (currval('OBJECT_ID_SEQ'), groupSequence, 'Parking');

INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Sanitation');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Vegtation');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Signage');


INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, OBJECT_TYPE)
    VALUES (nextval('OBJECT_ID_SEQ'), default, 1, default, 'LOCATION');
INSERT INTO irste_location (id, group_id, name) VALUES (currval('OBJECT_ID_SEQ'), groupSequence, 'Roads');

INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'PA System');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Sanitation');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Vegtation');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Signage');


groupSequence := nextval('OBJECT_ID_SEQ');
INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, OBJECT_TYPE)
    VALUES (groupSequence, default, 1, default, 'LOCATION');
INSERT INTO irste_grouplocation (group_id, name) VALUES (groupSequence, 'Corridor');

INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, OBJECT_TYPE)
    VALUES (nextval('OBJECT_ID_SEQ'), default, 1, default, 'LOCATION');
INSERT INTO irste_location (id, group_id, name) VALUES (currval('OBJECT_ID_SEQ'), groupSequence, 'Hostel 1 GF');

INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Lighting');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Water Cooler');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Wifi/Net');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Sanitation');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Signage');


INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, OBJECT_TYPE)
    VALUES (nextval('OBJECT_ID_SEQ'), default, 1, default, 'LOCATION');
INSERT INTO irste_location (id, group_id, name) VALUES (currval('OBJECT_ID_SEQ'), groupSequence, 'Hostel 1 FF');

INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Lighting');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Water Cooler');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Wifi/Net');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Sanitation');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Signage');


INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, OBJECT_TYPE)
    VALUES (nextval('OBJECT_ID_SEQ'), default, 1, default, 'LOCATION');
INSERT INTO irste_location (id, group_id, name) VALUES (currval('OBJECT_ID_SEQ'), groupSequence, 'Hostel 2 GF');

INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Lighting');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Water Cooler');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Wifi/Net');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Sanitation');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Signage');


INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, OBJECT_TYPE)
    VALUES (nextval('OBJECT_ID_SEQ'), default, 1, default, 'LOCATION');
INSERT INTO irste_location (id, group_id, name) VALUES (currval('OBJECT_ID_SEQ'), groupSequence, 'Hostel 2 FF');

INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Lighting');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Water Cooler');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Wifi/Net');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Sanitation');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Signage');


INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, OBJECT_TYPE)
    VALUES (nextval('OBJECT_ID_SEQ'), default, 1, default, 'LOCATION');
INSERT INTO irste_location (id, group_id, name) VALUES (currval('OBJECT_ID_SEQ'), groupSequence, 'Hostel 3 GF');

INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Lighting');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Water Cooler');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Wifi/Net');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Sanitation');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Signage');


INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, OBJECT_TYPE)
    VALUES (nextval('OBJECT_ID_SEQ'), default, 1, default, 'LOCATION');
INSERT INTO irste_location (id, group_id, name) VALUES (currval('OBJECT_ID_SEQ'), groupSequence, 'Hostel 3 FF');

INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Lighting');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Water Cooler');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Wifi/Net');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Sanitation');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Signage');


INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, OBJECT_TYPE)
    VALUES (nextval('OBJECT_ID_SEQ'), default, 1, default, 'LOCATION');
INSERT INTO irste_location (id, group_id, name) VALUES (currval('OBJECT_ID_SEQ'), groupSequence, 'Hostel 4 GF');

INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Lighting');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Water Cooler');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Wifi/Net');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Sanitation');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Signage');


INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, OBJECT_TYPE)
    VALUES (nextval('OBJECT_ID_SEQ'), default, 1, default, 'LOCATION');
INSERT INTO irste_location (id, group_id, name) VALUES (currval('OBJECT_ID_SEQ'), groupSequence, 'Hostel 4 FF');

INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Lighting');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Water Cooler');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Wifi/Net');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Sanitation');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Signage');


INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, OBJECT_TYPE)
    VALUES (nextval('OBJECT_ID_SEQ'), default, 1, default, 'LOCATION');
INSERT INTO irste_location (id, group_id, name) VALUES (currval('OBJECT_ID_SEQ'), groupSequence, 'Hostel 4 TF');

INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Lighting');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Water Cooler');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Wifi/Net');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Sanitation');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Signage');


INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, OBJECT_TYPE)
    VALUES (nextval('OBJECT_ID_SEQ'), default, 1, default, 'LOCATION');
INSERT INTO irste_location (id, group_id, name) VALUES (currval('OBJECT_ID_SEQ'), groupSequence, 'MainBlock SF');

INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Lighting');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Water Cooler');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Wifi/Net');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Sanitation');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Signage');


INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, OBJECT_TYPE)
    VALUES (nextval('OBJECT_ID_SEQ'), default, 1, default, 'LOCATION');
INSERT INTO irste_location (id, group_id, name) VALUES (currval('OBJECT_ID_SEQ'), groupSequence, 'MainBlock BS');

INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Lighting');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Water Cooler');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Wifi/Net');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Sanitation');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Signage');


INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, OBJECT_TYPE)
    VALUES (nextval('OBJECT_ID_SEQ'), default, 1, default, 'LOCATION');
INSERT INTO irste_location (id, group_id, name) VALUES (currval('OBJECT_ID_SEQ'), groupSequence, 'MainBlock GF');

INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Lighting');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Water Cooler');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Wifi/Net');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Sanitation');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Signage');


INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, OBJECT_TYPE)
    VALUES (nextval('OBJECT_ID_SEQ'), default, 1, default, 'LOCATION');
INSERT INTO irste_location (id, group_id, name) VALUES (currval('OBJECT_ID_SEQ'), groupSequence, 'MainBlock FF');

INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Lighting');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Water Cooler');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Wifi/Net');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Sanitation');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Signage');




groupSequence := nextval('OBJECT_ID_SEQ');
INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, OBJECT_TYPE)
    VALUES (groupSequence, default, 1, default, 'LOCATION');
INSERT INTO irste_grouplocation (group_id, name) VALUES (groupSequence, 'Gym');

INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, OBJECT_TYPE)
    VALUES (nextval('OBJECT_ID_SEQ'), default, 1, default, 'LOCATION');
INSERT INTO irste_location (id, group_id, name) VALUES (currval('OBJECT_ID_SEQ'), groupSequence, 'Hostel 1');

INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Lighting');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Water Cooler');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Wifi/Net');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Sanitation');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Signage');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'AC/Cooler/Fan');

INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, OBJECT_TYPE)
    VALUES (nextval('OBJECT_ID_SEQ'), default, 1, default, 'LOCATION');
INSERT INTO irste_location (id, group_id, name) VALUES (currval('OBJECT_ID_SEQ'), groupSequence, 'Hostel 2');

INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Lighting');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Water Cooler');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Wifi/Net');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Sanitation');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Signage');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'AC/Cooler/Fan');


INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, OBJECT_TYPE)
    VALUES (nextval('OBJECT_ID_SEQ'), default, 1, default, 'LOCATION');
INSERT INTO irste_location (id, group_id, name) VALUES (currval('OBJECT_ID_SEQ'), groupSequence, 'Hostel 3');

INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Lighting');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Water Cooler');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Wifi/Net');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Sanitation');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Signage');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'AC/Cooler/Fan');


INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, OBJECT_TYPE)
    VALUES (nextval('OBJECT_ID_SEQ'), default, 1, default, 'LOCATION');
INSERT INTO irste_location (id, group_id, name) VALUES (currval('OBJECT_ID_SEQ'), groupSequence, 'Hostel 4');

INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Lighting');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Water Cooler');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Wifi/Net');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Sanitation');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Signage');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'AC/Cooler/Fan');


INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, OBJECT_TYPE)
    VALUES (nextval('OBJECT_ID_SEQ'), default, 1, default, 'LOCATION');
INSERT INTO irste_location (id, group_id, name) VALUES (currval('OBJECT_ID_SEQ'), groupSequence, 'Badminton Court');

INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Lighting');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Water Cooler');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Wifi/Net');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Sanitation');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Signage');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'AC/Cooler/Fan');



groupSequence := nextval('OBJECT_ID_SEQ');
INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, OBJECT_TYPE)
    VALUES (groupSequence, default, 1, default, 'LOCATION');
INSERT INTO irste_grouplocation (group_id, name) VALUES (groupSequence, 'Lab');

INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, OBJECT_TYPE)
    VALUES (nextval('OBJECT_ID_SEQ'), default, 1, default, 'LOCATION');
INSERT INTO irste_location (id, group_id, name) VALUES (currval('OBJECT_ID_SEQ'), groupSequence, 'Main Block');

INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'AC/Cooler/Fan');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Lighting');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'ChalkBoard');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Table/Desk');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Seating');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Projector');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'PC');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Wifi/Net');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Sanitation');


INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, OBJECT_TYPE)
    VALUES (nextval('OBJECT_ID_SEQ'), default, 1, default, 'LOCATION');
INSERT INTO irste_location (id, group_id, name) VALUES (currval('OBJECT_ID_SEQ'), groupSequence, 'New Block');

INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'AC/Cooler/Fan');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Lighting');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'ChalkBoard');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Table/Desk');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Seating');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Projector');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'PC');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Wifi/Net');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Sanitation');


groupSequence := nextval('OBJECT_ID_SEQ');
INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, OBJECT_TYPE)
    VALUES (groupSequence, default, 1, default, 'LOCATION');
INSERT INTO irste_grouplocation (group_id, name) VALUES (groupSequence, 'Dining Room');


INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, OBJECT_TYPE)
    VALUES (nextval('OBJECT_ID_SEQ'), default, 1, default, 'LOCATION');
INSERT INTO irste_location (id, group_id, name) VALUES (currval('OBJECT_ID_SEQ'), groupSequence, 'Hostel 1');

INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'AC/Cooler/Fan');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Water Cooler');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Cutlery');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Wifi/Net');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Plumbing');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Sanitation');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Signage');


INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, OBJECT_TYPE)
    VALUES (nextval('OBJECT_ID_SEQ'), default, 1, default, 'LOCATION');
INSERT INTO irste_location (id, group_id, name) VALUES (currval('OBJECT_ID_SEQ'), groupSequence, 'Hostel 2');

INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'AC/Cooler/Fan');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Water Cooler');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Cutlery');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Wifi/Net');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Plumbing');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Sanitation');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Signage');


INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, OBJECT_TYPE)
    VALUES (nextval('OBJECT_ID_SEQ'), default, 1, default, 'LOCATION');
INSERT INTO irste_location (id, group_id, name) VALUES (currval('OBJECT_ID_SEQ'), groupSequence, 'Hostel 3');

INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'AC/Cooler/Fan');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Water Cooler');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Cutlery');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Wifi/Net');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Plumbing');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Sanitation');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Signage');



INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, OBJECT_TYPE)
    VALUES (nextval('OBJECT_ID_SEQ'), default, 1, default, 'LOCATION');
INSERT INTO irste_location (id, group_id, name) VALUES (currval('OBJECT_ID_SEQ'), groupSequence, 'Hostel 4');

INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'AC/Cooler/Fan');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Water Cooler');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Cutlery');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Wifi/Net');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Plumbing');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Sanitation');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Signage');



groupSequence := nextval('OBJECT_ID_SEQ');
INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, OBJECT_TYPE)
    VALUES (groupSequence, default, 1, default, 'LOCATION');
INSERT INTO irste_grouplocation (group_id, name) VALUES (groupSequence, 'Pantry');

INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, OBJECT_TYPE)
    VALUES (nextval('OBJECT_ID_SEQ'), default, 1, default, 'LOCATION');
INSERT INTO irste_location (id, group_id, name) VALUES (currval('OBJECT_ID_SEQ'), groupSequence, 'Conference');

INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Lighting');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Cutlery');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Plumbing');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Sanitation');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Signage');


INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, OBJECT_TYPE)
    VALUES (nextval('OBJECT_ID_SEQ'), default, 1, default, 'LOCATION');
INSERT INTO irste_location (id, group_id, name) VALUES (currval('OBJECT_ID_SEQ'), groupSequence, 'Hostel 1');

INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Lighting');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Cutlery');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Plumbing');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Sanitation');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Signage');


INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, OBJECT_TYPE)
    VALUES (nextval('OBJECT_ID_SEQ'), default, 1, default, 'LOCATION');
INSERT INTO irste_location (id, group_id, name) VALUES (currval('OBJECT_ID_SEQ'), groupSequence, 'Hostel 2');

INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Lighting');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Cutlery');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Plumbing');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Sanitation');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Signage');


INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, OBJECT_TYPE)
    VALUES (nextval('OBJECT_ID_SEQ'), default, 1, default, 'LOCATION');
INSERT INTO irste_location (id, group_id, name) VALUES (currval('OBJECT_ID_SEQ'), groupSequence, 'Hostel 3');

INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Lighting');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Cutlery');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Plumbing');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Sanitation');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Signage');


INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, OBJECT_TYPE)
    VALUES (nextval('OBJECT_ID_SEQ'), default, 1, default, 'LOCATION');
INSERT INTO irste_location (id, group_id, name) VALUES (currval('OBJECT_ID_SEQ'), groupSequence, 'Hostel 4');

INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Lighting');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Cutlery');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Plumbing');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Sanitation');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Signage');



groupSequence := nextval('OBJECT_ID_SEQ');
INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, OBJECT_TYPE)
    VALUES (groupSequence, default, 1, default, 'LOCATION');
INSERT INTO irste_grouplocation (group_id, name) VALUES (groupSequence, 'Wash Room');

INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, OBJECT_TYPE)
    VALUES (nextval('OBJECT_ID_SEQ'), default, 1, default, 'LOCATION');
INSERT INTO irste_location (id, group_id, name) VALUES (currval('OBJECT_ID_SEQ'), groupSequence, 'Hostel 1 GF');

INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Geyser');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Lighting');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Plumbing');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Sanitation');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Signage');


INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, OBJECT_TYPE)
    VALUES (nextval('OBJECT_ID_SEQ'), default, 1, default, 'LOCATION');
INSERT INTO irste_location (id, group_id, name) VALUES (currval('OBJECT_ID_SEQ'), groupSequence, 'Hostel 3 GF');

INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Geyser');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Lighting');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Plumbing');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Sanitation');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Signage');

INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, OBJECT_TYPE)
    VALUES (nextval('OBJECT_ID_SEQ'), default, 1, default, 'LOCATION');
INSERT INTO irste_location (id, group_id, name) VALUES (currval('OBJECT_ID_SEQ'), groupSequence, 'Hostel 2 GF');

INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Geyser');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Lighting');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Plumbing');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Sanitation');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Signage');

INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, OBJECT_TYPE)
    VALUES (nextval('OBJECT_ID_SEQ'), default, 1, default, 'LOCATION');
INSERT INTO irste_location (id, group_id, name) VALUES (currval('OBJECT_ID_SEQ'), groupSequence, 'Hostel 4');

INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Geyser');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Lighting');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Plumbing');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Sanitation');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Signage');

INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, OBJECT_TYPE)
    VALUES (nextval('OBJECT_ID_SEQ'), default, 1, default, 'LOCATION');
INSERT INTO irste_location (id, group_id, name) VALUES (currval('OBJECT_ID_SEQ'), groupSequence, 'Hostel 1 FF');

INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Geyser');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Lighting');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Plumbing');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Sanitation');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Signage');

INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, OBJECT_TYPE)
    VALUES (nextval('OBJECT_ID_SEQ'), default, 1, default, 'LOCATION');
INSERT INTO irste_location (id, group_id, name) VALUES (currval('OBJECT_ID_SEQ'), groupSequence, 'Hostel 2 FF');

INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Geyser');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Lighting');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Plumbing');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Sanitation');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Signage');

INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, OBJECT_TYPE)
    VALUES (nextval('OBJECT_ID_SEQ'), default, 1, default, 'LOCATION');
INSERT INTO irste_location (id, group_id, name) VALUES (currval('OBJECT_ID_SEQ'), groupSequence, 'Badminton Court');

INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Geyser');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Lighting');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Plumbing');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Sanitation');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Signage');

INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, OBJECT_TYPE)
    VALUES (nextval('OBJECT_ID_SEQ'), default, 1, default, 'LOCATION');
INSERT INTO irste_location (id, group_id, name) VALUES (currval('OBJECT_ID_SEQ'), groupSequence, 'West Gate');

INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Geyser');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Lighting');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Plumbing');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Sanitation');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Signage');

INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, OBJECT_TYPE)
    VALUES (nextval('OBJECT_ID_SEQ'), default, 1, default, 'LOCATION');
INSERT INTO irste_location (id, group_id, name) VALUES (currval('OBJECT_ID_SEQ'), groupSequence, 'Hostel 3 FF');

INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Geyser');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Lighting');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Plumbing');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Sanitation');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Signage');



groupSequence := nextval('OBJECT_ID_SEQ');
INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, OBJECT_TYPE)
    VALUES (groupSequence, default, 1, default, 'LOCATION');
INSERT INTO irste_grouplocation (group_id, name) VALUES (groupSequence, 'Class Room');

INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, OBJECT_TYPE)
    VALUES (nextval('OBJECT_ID_SEQ'), default, 1, default, 'LOCATION');
INSERT INTO irste_location (id, group_id, name) VALUES (currval('OBJECT_ID_SEQ'), groupSequence, '301');

INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'AC/Cooler/Fan');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Lighting');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'ChalkBoard');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Table/Desk');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Seating');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'PA System');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'PC');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Wifi/Net');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Sanitation');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Signage');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Projector');

INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, OBJECT_TYPE)
    VALUES (nextval('OBJECT_ID_SEQ'), default, 1, default, 'LOCATION');
INSERT INTO irste_location (id, group_id, name) VALUES (currval('OBJECT_ID_SEQ'), groupSequence, '304');

INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'AC/Cooler/Fan');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Lighting');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'ChalkBoard');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Table/Desk');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Seating');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'PA System');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'PC');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Wifi/Net');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Sanitation');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Signage');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Projector');


INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, OBJECT_TYPE)
    VALUES (nextval('OBJECT_ID_SEQ'), default, 1, default, 'LOCATION');
INSERT INTO irste_location (id, group_id, name) VALUES (currval('OBJECT_ID_SEQ'), groupSequence, '306');

INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'AC/Cooler/Fan');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Lighting');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'ChalkBoard');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Table/Desk');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Seating');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'PA System');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'PC');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Wifi/Net');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Sanitation');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Signage');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Projector');


INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, OBJECT_TYPE)
    VALUES (nextval('OBJECT_ID_SEQ'), default, 1, default, 'LOCATION');
INSERT INTO irste_location (id, group_id, name) VALUES (currval('OBJECT_ID_SEQ'), groupSequence, '307');

INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'AC/Cooler/Fan');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Lighting');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'ChalkBoard');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Table/Desk');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Seating');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'PA System');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'PC');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Wifi/Net');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Sanitation');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Signage');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Projector');


INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, OBJECT_TYPE)
    VALUES (nextval('OBJECT_ID_SEQ'), default, 1, default, 'LOCATION');
INSERT INTO irste_location (id, group_id, name) VALUES (currval('OBJECT_ID_SEQ'), groupSequence, '101');

INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'AC/Cooler/Fan');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Lighting');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'ChalkBoard');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Table/Desk');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Seating');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'PA System');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'PC');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Wifi/Net');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Sanitation');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Signage');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Projector');


INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, OBJECT_TYPE)
    VALUES (nextval('OBJECT_ID_SEQ'), default, 1, default, 'LOCATION');
INSERT INTO irste_location (id, group_id, name) VALUES (currval('OBJECT_ID_SEQ'), groupSequence, '102');

INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'AC/Cooler/Fan');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Lighting');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'ChalkBoard');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Table/Desk');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Seating');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'PA System');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'PC');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Wifi/Net');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Sanitation');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Signage');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Projector');


INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, OBJECT_TYPE)
    VALUES (nextval('OBJECT_ID_SEQ'), default, 1, default, 'LOCATION');
INSERT INTO irste_location (id, group_id, name) VALUES (currval('OBJECT_ID_SEQ'), groupSequence, '201');

INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'AC/Cooler/Fan');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Lighting');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'ChalkBoard');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Table/Desk');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Seating');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'PA System');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'PC');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Wifi/Net');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Sanitation');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Signage');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Projector');


INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, OBJECT_TYPE)
    VALUES (nextval('OBJECT_ID_SEQ'), default, 1, default, 'LOCATION');
INSERT INTO irste_location (id, group_id, name) VALUES (currval('OBJECT_ID_SEQ'), groupSequence, '202');

INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'AC/Cooler/Fan');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Lighting');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'ChalkBoard');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Table/Desk');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Seating');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'PA System');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'PC');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Wifi/Net');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Sanitation');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Signage');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Projector');


INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, OBJECT_TYPE)
    VALUES (nextval('OBJECT_ID_SEQ'), default, 1, default, 'LOCATION');
INSERT INTO irste_location (id, group_id, name) VALUES (currval('OBJECT_ID_SEQ'), groupSequence, '305');

INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'AC/Cooler/Fan');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Lighting');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'ChalkBoard');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Table/Desk');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Seating');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'PA System');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'PC');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Wifi/Net');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Sanitation');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Signage');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Projector');


INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, OBJECT_TYPE)
    VALUES (nextval('OBJECT_ID_SEQ'), default, 1, default, 'LOCATION');
INSERT INTO irste_location (id, group_id, name) VALUES (currval('OBJECT_ID_SEQ'), groupSequence, '303');

INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'AC/Cooler/Fan');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Lighting');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'ChalkBoard');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Table/Desk');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Seating');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'PA System');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'PC');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Wifi/Net');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Sanitation');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Signage');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Projector');


INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, OBJECT_TYPE)
    VALUES (nextval('OBJECT_ID_SEQ'), default, 1, default, 'LOCATION');
INSERT INTO irste_location (id, group_id, name) VALUES (currval('OBJECT_ID_SEQ'), groupSequence, '308');

INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'AC/Cooler/Fan');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Lighting');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'ChalkBoard');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Table/Desk');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Seating');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'PA System');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'PC');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Wifi/Net');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Sanitation');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Signage');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Projector');


INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, OBJECT_TYPE)
    VALUES (nextval('OBJECT_ID_SEQ'), default, 1, default, 'LOCATION');
INSERT INTO irste_location (id, group_id, name) VALUES (currval('OBJECT_ID_SEQ'), groupSequence, '302');

INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'AC/Cooler/Fan');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Lighting');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'ChalkBoard');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Table/Desk');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Seating');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'PA System');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'PC');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Wifi/Net');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Sanitation');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Signage');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Projector');



groupSequence := nextval('OBJECT_ID_SEQ');
INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, OBJECT_TYPE)
    VALUES (groupSequence, default, 1, default, 'LOCATION');
INSERT INTO irste_grouplocation (group_id, name) VALUES (groupSequence, 'Hostel Room');

INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, OBJECT_TYPE)
    VALUES (nextval('OBJECT_ID_SEQ'), default, 1, default, 'LOCATION');
INSERT INTO irste_location (id, group_id, name) VALUES (currval('OBJECT_ID_SEQ'), groupSequence, 'Hostel 1');

INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'AC/Cooler/Fan');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Lighting');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Cot & Linen');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Cutlery');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Wifi/Net');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Sanitation');

INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, OBJECT_TYPE)
    VALUES (nextval('OBJECT_ID_SEQ'), default, 1, default, 'LOCATION');
INSERT INTO irste_location (id, group_id, name) VALUES (currval('OBJECT_ID_SEQ'), groupSequence, 'Hostel 2');

INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'AC/Cooler/Fan');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Lighting');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Cot & Linen');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Cutlery');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Wifi/Net');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Sanitation');


INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, OBJECT_TYPE)
    VALUES (nextval('OBJECT_ID_SEQ'), default, 1, default, 'LOCATION');
INSERT INTO irste_location (id, group_id, name) VALUES (currval('OBJECT_ID_SEQ'), groupSequence, 'Hostel 3');

INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'AC/Cooler/Fan');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Lighting');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Cot & Linen');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Cutlery');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Wifi/Net');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Sanitation');


INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, OBJECT_TYPE)
    VALUES (nextval('OBJECT_ID_SEQ'), default, 1, default, 'LOCATION');
INSERT INTO irste_location (id, group_id, name) VALUES (currval('OBJECT_ID_SEQ'), groupSequence, 'Hostel 4');

INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'AC/Cooler/Fan');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Lighting');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Cot & Linen');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Cutlery');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Wifi/Net');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Sanitation');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Geyser');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Plumbing');



groupSequence := nextval('OBJECT_ID_SEQ');
INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, OBJECT_TYPE)
    VALUES (groupSequence, default, 1, default, 'LOCATION');
 INSERT INTO irste_grouplocation (group_id, name) VALUES (groupSequence, 'Sports');

INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, OBJECT_TYPE)
    VALUES (nextval('OBJECT_ID_SEQ'), default, 1, default, 'LOCATION');
INSERT INTO irste_location (id, group_id, name) VALUES (currval('OBJECT_ID_SEQ'), groupSequence, 'Cricket');

INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Lighting');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Water Cooler');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Sanitation');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Signage');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Vegtation');

INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, OBJECT_TYPE)
    VALUES (nextval('OBJECT_ID_SEQ'), default, 1, default, 'LOCATION');
INSERT INTO irste_location (id, group_id, name) VALUES (currval('OBJECT_ID_SEQ'), groupSequence, 'FootBall');

INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Lighting');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Water Cooler');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Sanitation');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Signage');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Vegtation');


INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, OBJECT_TYPE)
    VALUES (nextval('OBJECT_ID_SEQ'), default, 1, default, 'LOCATION');
INSERT INTO irste_location (id, group_id, name) VALUES (currval('OBJECT_ID_SEQ'), groupSequence, 'BasketBall');

INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Lighting');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Water Cooler');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Sanitation');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Signage');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Vegtation');


INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, OBJECT_TYPE)
    VALUES (nextval('OBJECT_ID_SEQ'), default, 1, default, 'LOCATION');
INSERT INTO irste_location (id, group_id, name) VALUES (currval('OBJECT_ID_SEQ'), groupSequence, 'Badminton');

INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Lighting');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Water Cooler');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Sanitation');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Signage');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Vegtation');


groupSequence := nextval('OBJECT_ID_SEQ');
INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, OBJECT_TYPE)
    VALUES (groupSequence, default, 1, default, 'LOCATION');
 INSERT INTO irste_grouplocation (group_id, name) VALUES (groupSequence, 'General Areas');

INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, OBJECT_TYPE)
    VALUES (nextval('OBJECT_ID_SEQ'), default, 1, default, 'LOCATION');
INSERT INTO irste_location (id, group_id, name) VALUES (currval('OBJECT_ID_SEQ'), groupSequence, 'Telepresence Room');

INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'AC/Cooler/Fan');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Lighting');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'PA System');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'PC');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Projector');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Wifi/Net');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Sanitation');


INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, OBJECT_TYPE)
    VALUES (nextval('OBJECT_ID_SEQ'), default, 1, default, 'LOCATION');
INSERT INTO irste_location (id, group_id, name) VALUES (currval('OBJECT_ID_SEQ'), groupSequence, 'Conference Room');

INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'AC/Cooler/Fan');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Lighting');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'PA System');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'PC');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Projector');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Wifi/Net');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Sanitation');


INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, OBJECT_TYPE)
    VALUES (nextval('OBJECT_ID_SEQ'), default, 1, default, 'LOCATION');
INSERT INTO irste_location (id, group_id, name) VALUES (currval('OBJECT_ID_SEQ'), groupSequence, 'Library');

INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Lighting');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'PA System');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'PC');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Projector');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Wifi/Net');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Sanitation');

INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, OBJECT_TYPE)
    VALUES (nextval('OBJECT_ID_SEQ'), default, 1, default, 'LOCATION');
INSERT INTO irste_location (id, group_id, name) VALUES (currval('OBJECT_ID_SEQ'), groupSequence, 'Canteen');

INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Lighting');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'PA System');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'PC');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Projector');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Wifi/Net');
INSERT INTO irste_utilitylocation (location_id, utility) VALUES (currval('OBJECT_ID_SEQ'), 'Sanitation');

INSERT INTO irste_grouputility (group_name, utility) VALUES ('Electrical', 'Geyser');
INSERT INTO irste_grouputility (group_name, utility) VALUES ('Electrical', 'AC/Cooler/Fan');
INSERT INTO irste_grouputility (group_name, utility) VALUES ('Electrical', 'Lighting');
INSERT INTO irste_grouputility (group_name, utility) VALUES ('Electrical', 'Water Cooler');
INSERT INTO irste_grouputility (group_name, utility) VALUES ('Teaching Aids', 'ChalkBoard');
INSERT INTO irste_grouputility (group_name, utility) VALUES ('Teaching Aids', 'Table/Desk');
INSERT INTO irste_grouputility (group_name, utility) VALUES ('Teaching Aids', 'Seating');
INSERT INTO irste_grouputility (group_name, utility) VALUES ('Lodging', 'Cot & Linen');
INSERT INTO irste_grouputility (group_name, utility) VALUES ('Lodging', 'Cutlery');
INSERT INTO irste_grouputility (group_name, utility) VALUES ('Public Systems', 'Projector');
INSERT INTO irste_grouputility (group_name, utility) VALUES ('Public Systems', 'PC');
INSERT INTO irste_grouputility (group_name, utility) VALUES ('Public Systems', 'PA System');
INSERT INTO irste_grouputility (group_name, utility) VALUES ('Public Systems', 'Wifi/ Net');
INSERT INTO irste_grouputility (group_name, utility) VALUES ('Service', 'Plumbing');
INSERT INTO irste_grouputility (group_name, utility) VALUES ('Service', 'Sanitation');
INSERT INTO irste_grouputility (group_name, utility) VALUES ('Service', 'Vegetation');


END $$;
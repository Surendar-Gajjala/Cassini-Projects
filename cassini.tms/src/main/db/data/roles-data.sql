DO $$
DECLARE
    groupId INTEGER;

BEGIN


 groupId := nextval('PERSONGROUP_ID_SEQ');
    INSERT INTO PERSONGROUP (GROUP_ID, NAME, DESCRIPTION)
        VALUES (groupId, 'Administrator', 'Manages all aspects of TM');
    INSERT INTO GROUPPERMISSION (PERSONGROUP, PERMISSION)
        VALUES (groupId, 'permission.admin.all');

     /* BOM Manager Group */
    groupId := nextval('PERSONGROUP_ID_SEQ');
    INSERT INTO PERSONGROUP (GROUP_ID, NAME, DESCRIPTION)
        VALUES (groupId, 'Trips Manager', 'Handles Trips');
    INSERT INTO GROUPPERMISSION (PERSONGROUP, PERMISSION)
        VALUES (groupId, 'permission.home.all');
    INSERT INTO GROUPPERMISSION (PERSONGROUP, PERMISSION)
        VALUES (groupId, 'permission.home.view');
    INSERT INTO GROUPPERMISSION (PERSONGROUP, PERMISSION)
        VALUES (groupId, 'permission.trips.all');
    INSERT INTO GROUPPERMISSION (PERSONGROUP, PERMISSION)
        VALUES (groupId, 'permission.trips.view');
    INSERT INTO GROUPPERMISSION (PERSONGROUP, PERMISSION)
        VALUES (groupId, 'permission.trips.add');



END $$;
DO $$
DECLARE
    groupId INTEGER;

BEGIN


 groupId := nextval('PERSONGROUP_ID_SEQ');
    INSERT INTO PERSONGROUP (GROUP_ID, NAME, DESCRIPTION)
        VALUES (groupId, 'Administrator', 'Manages all aspects of TM');
    INSERT INTO GROUPPERMISSION (PERSONGROUP, PERMISSION)
        VALUES (groupId, 'permission.admin.all');

    /*Inventory Manager Group */
    groupId := nextval('PERSONGROUP_ID_SEQ');
    INSERT INTO PERSONGROUP (GROUP_ID, NAME, DESCRIPTION)
        VALUES (groupId, 'Task Manager', 'Manages Tasks');
    INSERT INTO GROUPPERMISSION (PERSONGROUP, PERMISSION)
        VALUES (groupId, 'permission.tasks.all');
    INSERT INTO GROUPPERMISSION (PERSONGROUP, PERMISSION)
        VALUES (groupId, 'permission.tasks.view');
    INSERT INTO GROUPPERMISSION (PERSONGROUP, PERMISSION)
        VALUES (groupId, 'permission.tasks.add');


     /* BOM Manager Group */
    groupId := nextval('PERSONGROUP_ID_SEQ');
    INSERT INTO PERSONGROUP (GROUP_ID, NAME, DESCRIPTION)
        VALUES (groupId, 'Project Manager', 'Handles Projects');
    INSERT INTO GROUPPERMISSION (PERSONGROUP, PERMISSION)
        VALUES (groupId, 'permission.home.all');
    INSERT INTO GROUPPERMISSION (PERSONGROUP, PERMISSION)
        VALUES (groupId, 'permission.home.view');
    INSERT INTO GROUPPERMISSION (PERSONGROUP, PERMISSION)
        VALUES (groupId, 'permission.projects.all');
    INSERT INTO GROUPPERMISSION (PERSONGROUP, PERMISSION)
        VALUES (groupId, 'permission.projects.view');
    INSERT INTO GROUPPERMISSION (PERSONGROUP, PERMISSION)
        VALUES (groupId, 'permission.projects.add');
    INSERT INTO GROUPPERMISSION (PERSONGROUP, PERMISSION)
        VALUES (groupId, 'permission.tasks.all');
    INSERT INTO GROUPPERMISSION (PERSONGROUP, PERMISSION)
        VALUES (groupId, 'permission.tasks.view');
    INSERT INTO GROUPPERMISSION (PERSONGROUP, PERMISSION)
        VALUES (groupId, 'permission.tasks.add');

/*
    *//* System Administrator Role *//*
    groupId := nextval('PERSONGROUP_ID_SEQ');
    INSERT INTO PERSONGROUP (GROUP_ID, NAME, DESCRIPTION)
        VALUES (groupId, 'System Administrator', 'Manages logins, roles and security');
    INSERT INTO GROUPPERMISSION (PERSONGROUP, PERMISSION)
        VALUES (groupId, 'permission.security.all');

   */


END $$;
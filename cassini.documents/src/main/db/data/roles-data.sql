DO $$
DECLARE
    groupId INTEGER;

BEGIN

    groupId := nextval('PERSONGROUP_ID_SEQ');
    INSERT INTO PERSONGROUP (GROUP_ID, NAME, DESCRIPTION, IS_ACTIVE, EXTERNAL)
        VALUES (groupId, 'Administrator', 'Manages all aspects of DM', TRUE, FALSE);
    INSERT INTO GROUPPERMISSION (PERSONGROUP, PERMISSION)
        VALUES (groupId, 'permission.admin.all');

    /*Inventory Manager Group */
    groupId := nextval('PERSONGROUP_ID_SEQ');
    INSERT INTO PERSONGROUP (GROUP_ID, NAME, DESCRIPTION, IS_ACTIVE, EXTERNAL)
        VALUES (groupId, 'Project Manager', 'Manages Dash Board', TRUE, FALSE);

END $$;
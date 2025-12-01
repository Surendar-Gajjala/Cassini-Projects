DO $$
DECLARE
    groupId INTEGER;

BEGIN

    groupId := nextval('PERSONGROUP_ID_SEQ');

    INSERT INTO PERSONGROUP (GROUP_ID, NAME, DESCRIPTION,PARENT,IS_ACTIVE,EXTERNAL)
        VALUES (groupId, 'Administrator', 'Manages all aspects of IM',NULL,TRUE,FALSE);
    INSERT INTO GROUPPERMISSION (PERSONGROUP, PERMISSION)
        VALUES (groupId, 'permission.admin.all');

END $$;
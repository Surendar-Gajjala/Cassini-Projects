DO $$
DECLARE
    groupId INTEGER;
    roleId INTEGER;
    personRoleId INTEGER;
    issueId INTEGER;
    itemNumberSource INTEGER;
    indentNumberSource INTEGER;
    rowId INTEGER;
BEGIN

 groupId := nextval('PERSONGROUP_ID_SEQ');
 rowId := nextval('GROUPMEMBER_ID_SEQ');
 itemNumberSource := nextval('AUTONUMBER_ID_SEQ');

 indentNumberSource := nextval('AUTONUMBER_ID_SEQ');


  INSERT INTO AUTONUMBER (AUTONUMBER_ID, NAME, DESCRIPTION, NUMBERS, START, INCREMENT, NEXT_NUMBER, PADWITH, PREFIX, SUFFIX)
    VALUES (itemNumberSource, 'Default Material Item Number Source', 'Auto material item number', 5, 1, 1, 1, '0', 'MAT-', '');

itemNumberSource := nextval('AUTONUMBER_ID_SEQ');
INSERT INTO AUTONUMBER (AUTONUMBER_ID, NAME, DESCRIPTION, NUMBERS, START, INCREMENT, NEXT_NUMBER, PADWITH, PREFIX, SUFFIX)
    VALUES (itemNumberSource, 'Default Machine Item Number Source', 'Auto machine item number', 5, 1, 1, 1, '0', 'MAC-', '');

itemNumberSource := nextval('AUTONUMBER_ID_SEQ');
    INSERT INTO AUTONUMBER (AUTONUMBER_ID, NAME, DESCRIPTION, NUMBERS, START, INCREMENT, NEXT_NUMBER, PADWITH, PREFIX, SUFFIX)
    VALUES (itemNumberSource, 'Default Manpower Item Number Source', 'Auto Manpower item number', 5, 1, 1, 1, '0', 'MAN-', '');

itemNumberSource := nextval('AUTONUMBER_ID_SEQ');
    INSERT INTO AUTONUMBER (AUTONUMBER_ID, NAME, DESCRIPTION, NUMBERS, START, INCREMENT, NEXT_NUMBER, PADWITH, PREFIX, SUFFIX)
    VALUES (itemNumberSource, 'Default Stock Receive Number Source', 'Auto Receive item number', 5, 1, 1, 1, '0', 'REC-', '');

itemNumberSource := nextval('AUTONUMBER_ID_SEQ');
    INSERT INTO AUTONUMBER (AUTONUMBER_ID, NAME, DESCRIPTION, NUMBERS, START, INCREMENT, NEXT_NUMBER, PADWITH, PREFIX, SUFFIX)
    VALUES (itemNumberSource, 'Default Stock Issue Number Source', 'Auto Issue item number', 5, 1, 1, 1, '0', 'ISU-', '');

itemNumberSource := nextval('AUTONUMBER_ID_SEQ');
    INSERT INTO AUTONUMBER (AUTONUMBER_ID, NAME, DESCRIPTION, NUMBERS, START, INCREMENT, NEXT_NUMBER, PADWITH, PREFIX, SUFFIX)
    VALUES (itemNumberSource, 'Default Loan Number Source', 'Auto Loan number', 5, 1, 1, 1, '0', 'LOAN-', '');

itemNumberSource := nextval('AUTONUMBER_ID_SEQ');
    INSERT INTO AUTONUMBER (AUTONUMBER_ID, NAME, DESCRIPTION, NUMBERS, START, INCREMENT, NEXT_NUMBER, PADWITH, PREFIX, SUFFIX)
    VALUES (itemNumberSource, 'Default Scrap Number Source', 'Auto Scrap number', 5, 1, 1, 1, '0', 'SCRAP-', '');

 itemNumberSource := nextval('AUTONUMBER_ID_SEQ');
    INSERT INTO AUTONUMBER (AUTONUMBER_ID, NAME, DESCRIPTION, NUMBERS, START, INCREMENT, NEXT_NUMBER, PADWITH, PREFIX, SUFFIX)
    VALUES (itemNumberSource, 'Default Requisition Number Source', 'Auto Requisition number', 5, 1, 1, 1, '0', 'REQ-', '');

    INSERT INTO AUTONUMBER (AUTONUMBER_ID, NAME, DESCRIPTION, NUMBERS, START, INCREMENT, NEXT_NUMBER, PADWITH, PREFIX, SUFFIX)
    VALUES (indentNumberSource, 'Default Indent Number Source', 'Auto Indent number', 5, 1, 1, 1, '0', 'IND-', '');

indentNumberSource := nextval('AUTONUMBER_ID_SEQ');
    INSERT INTO AUTONUMBER (AUTONUMBER_ID, NAME, DESCRIPTION, NUMBERS, START, INCREMENT, NEXT_NUMBER, PADWITH, PREFIX, SUFFIX)
    VALUES (indentNumberSource, 'Default Purchase Order Number Source', 'Auto Purchase Order number', 5, 1, 1, 1, '0', 'PO-', '');

indentNumberSource := nextval('AUTONUMBER_ID_SEQ');
    INSERT INTO AUTONUMBER (AUTONUMBER_ID, NAME, DESCRIPTION, NUMBERS, START, INCREMENT, NEXT_NUMBER, PADWITH, PREFIX, SUFFIX)
    VALUES (indentNumberSource, 'Default Road Challan Number Source', 'Auto Road Challan number', 5, 1, 1, 1, '0', 'RC-', '');

itemNumberSource := nextval('AUTONUMBER_ID_SEQ');
    INSERT INTO AUTONUMBER (AUTONUMBER_ID, NAME, DESCRIPTION, NUMBERS, START, INCREMENT, NEXT_NUMBER, PADWITH, PREFIX, SUFFIX)
    VALUES (itemNumberSource, 'Default Stock Return Number Source', 'Auto Stock Return number', 5, 1, 1, 1, '0', 'RET-', '');

itemNumberSource := nextval('AUTONUMBER_ID_SEQ');
    INSERT INTO AUTONUMBER (AUTONUMBER_ID, NAME, DESCRIPTION, NUMBERS, START, INCREMENT, NEXT_NUMBER, PADWITH, PREFIX, SUFFIX)
    VALUES (itemNumberSource, 'Default Work Order Number Source', 'Auto Work Order number', 5, 1, 1, 1, '0', 'WO-', '');

roleId := nextval('ROLE_ID_SEQ');
    INSERT INTO ROLE (ROLE_ID, NAME, DESCRIPTION)
        VALUES (roleId, 'Administrator', 'Manages all aspects of IS');
    INSERT INTO ROLEPERMISSION (ROLE, PERMISSION)
        VALUES (roleId, 'permission.admin.all');

    INSERT INTO PERSONGROUP (GROUP_ID, NAME, DESCRIPTION,PARENT,IS_ACTIVE,EXTERNAL)
        VALUES (groupId, 'Administrator', 'Manages all aspects of IM',NULL,TRUE,FALSE);
    INSERT INTO GROUPPERMISSION (PERSONGROUP, PERMISSION)
        VALUES (groupId, 'permission.admin.all');

    UPDATE PERSON SET DEFAULT_GROUP=groupId where PERSON_ID = 1;
    INSERT INTO GROUPMEMBER (ROWID, GROUP_ID, PERSON)
        VALUES (rowId, groupId, 1);


  personRoleId := nextval('PERSONROLE_ID_SEQ');

 INSERT INTO PERSONROLE (ROWID, PERSON, ROLE)
        VALUES (personRoleId, 1, roleId);
    /*Inventory Manager Group */
    roleId := nextval('ROLE_ID_SEQ');
    INSERT INTO ROLE (ROLE_ID, NAME, DESCRIPTION)
        VALUES (roleId, 'Project Manager', 'Manages Dash Board');
    INSERT INTO ROLEPERMISSION (ROLE, PERMISSION)
        VALUES (roleId, 'permission.dashboard.all');
    INSERT INTO ROLEPERMISSION (ROLE, PERMISSION)
        VALUES (roleId, 'permission.procurement.all');
    INSERT INTO ROLEPERMISSION (ROLE, PERMISSION)
        VALUES (roleId, 'permission.home.all');
    INSERT INTO ROLEPERMISSION (ROLE, PERMISSION)
        VALUES (roleId, 'permission.home.view');
    INSERT INTO ROLEPERMISSION (ROLE, PERMISSION)
        VALUES (roleId, 'permission.wbs.all');
    INSERT INTO ROLEPERMISSION (ROLE, PERMISSION)
        VALUES (roleId, 'permission.wbs.view');
    INSERT INTO ROLEPERMISSION (ROLE, PERMISSION)
        VALUES (roleId, 'permission.wbs.create');
    INSERT INTO ROLEPERMISSION (ROLE, PERMISSION)
        VALUES (roleId, 'permission.bom.all');
    INSERT INTO ROLEPERMISSION (ROLE, PERMISSION)
        VALUES (roleId, 'permission.bom.view');
    INSERT INTO ROLEPERMISSION (ROLE, PERMISSION)
        VALUES (roleId, 'permission.tasks.all');
    INSERT INTO ROLEPERMISSION (ROLE, PERMISSION)
        VALUES (roleId, 'permission.tasks.view');
    INSERT INTO ROLEPERMISSION (ROLE, PERMISSION)
        VALUES (roleId, 'permission.issues.all');
    INSERT INTO ROLEPERMISSION (ROLE, PERMISSION)
        VALUES (roleId, 'permission.issues.view');
    INSERT INTO ROLEPERMISSION (ROLE, PERMISSION)
        VALUES (roleId, 'permission.files.all');
    INSERT INTO ROLEPERMISSION (ROLE, PERMISSION)
        VALUES (roleId, 'permission.files.view');
        INSERT INTO ROLEPERMISSION (ROLE, PERMISSION)
        VALUES (roleId, 'permission.meetings.all');
    INSERT INTO ROLEPERMISSION (ROLE, PERMISSION)
        VALUES (roleId, 'permission.meetings.view');


    roleId := nextval('ROLE_ID_SEQ');
    INSERT INTO ROLE (ROLE_ID, NAME, DESCRIPTION)
        VALUES (roleId, 'Executive Engineer', 'Manages Engineering');
     INSERT INTO ROLEPERMISSION (ROLE, PERMISSION)
        VALUES (roleId, 'permission.dashboard.all');
    INSERT INTO ROLEPERMISSION (ROLE, PERMISSION)
        VALUES (roleId, 'permission.procurement.all');
    INSERT INTO ROLEPERMISSION (ROLE, PERMISSION)
        VALUES (roleId, 'permission.home.all');
    INSERT INTO ROLEPERMISSION (ROLE, PERMISSION)
        VALUES (roleId, 'permission.home.view');
    INSERT INTO ROLEPERMISSION (ROLE, PERMISSION)
        VALUES (roleId, 'permission.wbs.all');
    INSERT INTO ROLEPERMISSION (ROLE, PERMISSION)
        VALUES (roleId, 'permission.wbs.view');
    INSERT INTO ROLEPERMISSION (ROLE, PERMISSION)
        VALUES (roleId, 'permission.wbs.create');
    INSERT INTO ROLEPERMISSION (ROLE, PERMISSION)
        VALUES (roleId, 'permission.bom.all');
    INSERT INTO ROLEPERMISSION (ROLE, PERMISSION)
        VALUES (roleId, 'permission.bom.view');


     roleId := nextval('ROLE_ID_SEQ');
    INSERT INTO ROLE (ROLE_ID, NAME, DESCRIPTION)
        VALUES (roleId, 'Accountant', 'Manages Accounting');
     INSERT INTO ROLEPERMISSION (ROLE, PERMISSION)
        VALUES (roleId, 'permission.dashboard.all');
    INSERT INTO ROLEPERMISSION (ROLE, PERMISSION)
        VALUES (roleId, 'permission.procurement.all');
    INSERT INTO ROLEPERMISSION (ROLE, PERMISSION)
        VALUES (roleId, 'permission.inventory.all');

    /*Inventory Manager Group */
    groupId := nextval('PERSONGROUP_ID_SEQ');
    INSERT INTO PERSONGROUP (GROUP_ID, NAME, DESCRIPTION, IS_ACTIVE, EXTERNAL)
        VALUES (groupId, 'Project Manager', 'Manages Dash Board', TRUE, FALSE);
    INSERT INTO GROUPPERMISSION (PERSONGROUP, PERMISSION)
        VALUES (groupId, 'permission.home.all');
    INSERT INTO GROUPPERMISSION (PERSONGROUP, PERMISSION)
        VALUES (groupId, 'permission.home.view');
    INSERT INTO GROUPPERMISSION (PERSONGROUP, PERMISSION)
        VALUES (groupId, 'permission.home.create');
    INSERT INTO GROUPPERMISSION (PERSONGROUP, PERMISSION)
        VALUES (groupId, 'permission.home.editBasic');
    INSERT INTO GROUPPERMISSION (PERSONGROUP, PERMISSION)
        VALUES (groupId, 'permission.home.Tasks');
    INSERT INTO GROUPPERMISSION (PERSONGROUP, PERMISSION)
        VALUES (groupId, 'permission.wbs.all');
    INSERT INTO GROUPPERMISSION (PERSONGROUP, PERMISSION)
        VALUES (groupId, 'permission.wbs.view');
    INSERT INTO GROUPPERMISSION (PERSONGROUP, PERMISSION)
        VALUES (groupId, 'permission.wbs.create');
    INSERT INTO GROUPPERMISSION (PERSONGROUP, PERMISSION)
        VALUES (groupId, 'permission.wbs.edit');
    INSERT INTO GROUPPERMISSION (PERSONGROUP, PERMISSION)
        VALUES (groupId, 'permission.wbs.delete');
    INSERT INTO GROUPPERMISSION (PERSONGROUP, PERMISSION)
        VALUES (groupId, 'permission.bom.all');
    INSERT INTO GROUPPERMISSION (PERSONGROUP, PERMISSION)
        VALUES (groupId, 'permission.bom.view');
    INSERT INTO GROUPPERMISSION (PERSONGROUP, PERMISSION)
        VALUES (groupId, 'permission.bom.create');
    INSERT INTO GROUPPERMISSION (PERSONGROUP, PERMISSION)
        VALUES (groupId, 'permission.bom.edit');
    INSERT INTO GROUPPERMISSION (PERSONGROUP, PERMISSION)
        VALUES (groupId, 'permission.bom.import');
    INSERT INTO GROUPPERMISSION (PERSONGROUP, PERMISSION)
        VALUES (groupId, 'permission.bom.export');
    INSERT INTO GROUPPERMISSION (PERSONGROUP, PERMISSION)
        VALUES (groupId, 'permission.tasks.all');
    INSERT INTO GROUPPERMISSION (PERSONGROUP, PERMISSION)
        VALUES (groupId, 'permission.tasks.view');
    INSERT INTO GROUPPERMISSION (PERSONGROUP, PERMISSION)
        VALUES (groupId, 'permission.tasks.create');
    INSERT INTO GROUPPERMISSION (PERSONGROUP, PERMISSION)
        VALUES (groupId, 'permission.tasks.edit');
    INSERT INTO GROUPPERMISSION (PERSONGROUP, PERMISSION)
        VALUES (groupId, 'permission.tasks.delete');
    INSERT INTO GROUPPERMISSION (PERSONGROUP, PERMISSION)
        VALUES (groupId, 'permission.tasks.add');
    INSERT INTO GROUPPERMISSION (PERSONGROUP, PERMISSION)
        VALUES (groupId, 'permission.tasks.addManpower');
    INSERT INTO GROUPPERMISSION (PERSONGROUP, PERMISSION)
        VALUES (groupId, 'permission.tasks.addMaterial');
    INSERT INTO GROUPPERMISSION (PERSONGROUP, PERMISSION)
        VALUES (groupId, 'permission.tasks.addMachine');
    INSERT INTO GROUPPERMISSION (PERSONGROUP, PERMISSION)
        VALUES (groupId, 'permission.tasks.addFiles');
    INSERT INTO GROUPPERMISSION (PERSONGROUP, PERMISSION)
        VALUES (groupId, 'permission.tasks.deleteItem');
    INSERT INTO GROUPPERMISSION (PERSONGROUP, PERMISSION)
        VALUES (groupId, 'permission.issues.all');
    INSERT INTO GROUPPERMISSION (PERSONGROUP, PERMISSION)
        VALUES (groupId, 'permission.issues.view');
    INSERT INTO GROUPPERMISSION (PERSONGROUP, PERMISSION)
        VALUES (groupId, 'permission.issues.create');
    INSERT INTO GROUPPERMISSION (PERSONGROUP, PERMISSION)
        VALUES (groupId, 'permission.issues.delete');
    INSERT INTO GROUPPERMISSION (PERSONGROUP, PERMISSION)
        VALUES (groupId, 'permission.issues.edit');
    INSERT INTO GROUPPERMISSION (PERSONGROUP, PERMISSION)
        VALUES (groupId, 'permission.issues.add');
    INSERT INTO GROUPPERMISSION (PERSONGROUP, PERMISSION)
        VALUES (groupId, 'permission.issues.assign');
    INSERT INTO GROUPPERMISSION (PERSONGROUP, PERMISSION)
        VALUES (groupId, 'permission.files.all');
    INSERT INTO GROUPPERMISSION (PERSONGROUP, PERMISSION)
        VALUES (groupId, 'permission.files.view');
    INSERT INTO GROUPPERMISSION (PERSONGROUP, PERMISSION)
        VALUES (groupId, 'permission.files.addFolder');
    INSERT INTO GROUPPERMISSION (PERSONGROUP, PERMISSION)
        VALUES (groupId, 'permission.files.deleteFolder');
    INSERT INTO GROUPPERMISSION (PERSONGROUP, PERMISSION)
        VALUES (groupId, 'permission.meetings.all');
    INSERT INTO GROUPPERMISSION (PERSONGROUP, PERMISSION)
        VALUES (groupId, 'permission.meetings.view');
    INSERT INTO GROUPPERMISSION (PERSONGROUP, PERMISSION)
        VALUES (groupId, 'permission.meetings.create');
    INSERT INTO GROUPPERMISSION (PERSONGROUP, PERMISSION)
        VALUES (groupId, 'permission.meetings.new');
    INSERT INTO GROUPPERMISSION (PERSONGROUP, PERMISSION)
        VALUES (groupId, 'permission.meetings.save');
    INSERT INTO GROUPPERMISSION (PERSONGROUP, PERMISSION)
        VALUES (groupId, 'permission.meetings.edit');
    INSERT INTO GROUPPERMISSION (PERSONGROUP, PERMISSION)
        VALUES (groupId, 'permission.meetings.delete');
    INSERT INTO GROUPPERMISSION (PERSONGROUP, PERMISSION)
        VALUES (groupId, 'permission.team.all');
    INSERT INTO GROUPPERMISSION (PERSONGROUP, PERMISSION)
        VALUES (groupId, 'permission.team.view');
    INSERT INTO GROUPPERMISSION (PERSONGROUP, PERMISSION)
        VALUES (groupId, 'permission.team.create');
    INSERT INTO GROUPPERMISSION (PERSONGROUP, PERMISSION)
        VALUES (groupId, 'permission.team.deleteRole');
    INSERT INTO GROUPPERMISSION (PERSONGROUP, PERMISSION)
        VALUES (groupId, 'permission.team.addPerson');
    INSERT INTO GROUPPERMISSION (PERSONGROUP, PERMISSION)
        VALUES (groupId, 'permission.team.editTeam');
    INSERT INTO GROUPPERMISSION (PERSONGROUP, PERMISSION)
        VALUES (groupId, 'permission.team.roles');
    INSERT INTO GROUPPERMISSION (PERSONGROUP, PERMISSION)
        VALUES (groupId, 'permission.team.newRole');

    issueId := nextval('ISSUETYPE_ID_SEQ');
    INSERT INTO IS_ISSUETYPE (TYPE_ID, LABEL, DESCRIPTION)
        VALUES (issueId,'ENGINEERING','ENGINEERING');
    issueId := nextval('ISSUETYPE_ID_SEQ');
    INSERT INTO IS_ISSUETYPE (TYPE_ID, LABEL, DESCRIPTION)
        VALUES (issueId,'MECHANICAL','MECHANICAL');
        issueId := nextval('ISSUETYPE_ID_SEQ');
    INSERT INTO IS_ISSUETYPE (TYPE_ID, LABEL, DESCRIPTION)
        VALUES (issueId,'ELECTRICAL','ELECTRICAL');

   /* Application Details Data */

 INSERT INTO APPLICATION_DETAILS (ID, OPTION_KEY, OPTION_NAME, CREATED_DATE, MODIFIED_DATE, VALUE)
    VALUES (NEXTVAL('APPLICATION_SEQUENCE'), 6, 'SHOW_LANGUAGE',CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'false');

END $$;
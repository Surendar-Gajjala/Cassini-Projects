DO $$
DECLARE
    roleId INTEGER;
BEGIN

    /* Sales Administrator Role */
    roleId := nextval('ROLE_ID_SEQ');
    INSERT INTO ERP_ROLE (ROLE_ID, NAME, DESCRIPTION)
        VALUES (roleId, 'Sales Administrator', 'Manages all aspects of sales');
    INSERT INTO ERP_ROLEPERMISSION (ROLE, PERMISSION)
        VALUES (roleId, 'permission.crm.all');

    /* Sales Executive Role */
    roleId := nextval('ROLE_ID_SEQ');
    INSERT INTO ERP_ROLE (ROLE_ID, NAME, DESCRIPTION)
        VALUES (roleId, 'Sales Executive', 'Manages customers in assigned regions');
    INSERT INTO ERP_ROLEPERMISSION (ROLE, PERMISSION)
        VALUES (roleId, 'permission.crm.customer.create');
    INSERT INTO ERP_ROLEPERMISSION (ROLE, PERMISSION)
        VALUES (roleId, 'permission.crm.customer.view');
    INSERT INTO ERP_ROLEPERMISSION (ROLE, PERMISSION)
        VALUES (roleId, 'permission.crm.customer.update');
    INSERT INTO ERP_ROLEPERMISSION (ROLE, PERMISSION)
        VALUES (roleId, 'permission.crm.salesregion.create');
    INSERT INTO ERP_ROLEPERMISSION (ROLE, PERMISSION)
        VALUES (roleId, 'permission.crm.salesregion.view');
    INSERT INTO ERP_ROLEPERMISSION (ROLE, PERMISSION)
        VALUES (roleId, 'permission.crm.salesregion.update');
    INSERT INTO ERP_ROLEPERMISSION (ROLE, PERMISSION)
        VALUES (roleId, 'permission.production.product.view');
    INSERT INTO ERP_ROLEPERMISSION (ROLE, PERMISSION)
        VALUES (roleId, 'permission.crm.order.create');
    INSERT INTO ERP_ROLEPERMISSION (ROLE, PERMISSION)
        VALUES (roleId, 'permission.crm.order.view');
    INSERT INTO ERP_ROLEPERMISSION (ROLE, PERMISSION)
        VALUES (roleId, 'permission.crm.order.update');

    /* Order Entry Operator Role */
    roleId := nextval('ROLE_ID_SEQ');
    INSERT INTO ERP_ROLE (ROLE_ID, NAME, DESCRIPTION)
        VALUES (roleId, 'Order Entry', 'Creates new orders');
    INSERT INTO ERP_ROLEPERMISSION (ROLE, PERMISSION)
        VALUES (roleId, 'permission.crm.customer.create');
    INSERT INTO ERP_ROLEPERMISSION (ROLE, PERMISSION)
        VALUES (roleId, 'permission.crm.customer.view');
    INSERT INTO ERP_ROLEPERMISSION (ROLE, PERMISSION)
        VALUES (roleId, 'permission.crm.order.view');
    INSERT INTO ERP_ROLEPERMISSION (ROLE, PERMISSION)
        VALUES (roleId, 'permission.crm.order.create');
    INSERT INTO ERP_ROLEPERMISSION (ROLE, PERMISSION)
        VALUES (roleId, 'permission.crm.order.update');
    INSERT INTO ERP_ROLEPERMISSION (ROLE, PERMISSION)
        VALUES (roleId, 'permission.production.product.view');

    /* Shipping Manager Role */
    roleId := nextval('ROLE_ID_SEQ');
    INSERT INTO ERP_ROLE (ROLE_ID, NAME, DESCRIPTION)
        VALUES (roleId, 'Shipping Manager', 'Manages all aspects of order shipment');
    INSERT INTO ERP_ROLEPERMISSION (ROLE, PERMISSION)
        VALUES (roleId, 'permission.crm.order.view');
    INSERT INTO ERP_ROLEPERMISSION (ROLE, PERMISSION)
        VALUES (roleId, 'permission.crm.order.dispatch');
    INSERT INTO ERP_ROLEPERMISSION (ROLE, PERMISSION)
        VALUES (roleId, 'permission.production.inventory.view');
    INSERT INTO ERP_ROLEPERMISSION (ROLE, PERMISSION)
        VALUES (roleId, 'permission.production.inventory.update');

    /* Shipping Operator Role */
    roleId := nextval('ROLE_ID_SEQ');
    INSERT INTO ERP_ROLE (ROLE_ID, NAME, DESCRIPTION)
        VALUES (roleId, 'Shipping Operator', 'Processes order shipment');
    INSERT INTO ERP_ROLEPERMISSION (ROLE, PERMISSION)
        VALUES (roleId, 'permission.crm.order.view');
    INSERT INTO ERP_ROLEPERMISSION (ROLE, PERMISSION)
        VALUES (roleId, 'permission.crm.order.dispatch');
    INSERT INTO ERP_ROLEPERMISSION (ROLE, PERMISSION)
        VALUES (roleId, 'permission.production.inventory.view');
    INSERT INTO ERP_ROLEPERMISSION (ROLE, PERMISSION)
        VALUES (roleId, 'permission.production.inventory.update');


    /* HR Manager Role */
    roleId := nextval('ROLE_ID_SEQ');
    INSERT INTO ERP_ROLE (ROLE_ID, NAME, DESCRIPTION)
        VALUES (roleId, 'HR Manager', 'Manages all aspects of HR');
    INSERT INTO ERP_ROLEPERMISSION (ROLE, PERMISSION)
        VALUES (roleId, 'permission.hrm.all');

    /* Payroll Administrator Role */
    roleId := nextval('ROLE_ID_SEQ');
    INSERT INTO ERP_ROLE (ROLE_ID, NAME, DESCRIPTION)
        VALUES (roleId, 'Payroll Administrator', 'Manages all aspects of payroll');
    INSERT INTO ERP_ROLEPERMISSION (ROLE, PERMISSION)
        VALUES (roleId, 'permission.hrm.payroll.all');

    /* Warehouse Manager Role */
    roleId := nextval('ROLE_ID_SEQ');
    INSERT INTO ERP_ROLE (ROLE_ID, NAME, DESCRIPTION)
        VALUES (roleId, 'Warehouse Manager', 'Manages all aspects of dispatch and inventory');
    INSERT INTO ERP_ROLEPERMISSION (ROLE, PERMISSION)
        VALUES (roleId, 'permission.production.product.view');
    INSERT INTO ERP_ROLEPERMISSION (ROLE, PERMISSION)
        VALUES (roleId, 'permission.production.inventory.all');
    INSERT INTO ERP_ROLEPERMISSION (ROLE, PERMISSION)
        VALUES (roleId, 'permission.crm.order.dispatch');
    INSERT INTO ERP_ROLEPERMISSION (ROLE, PERMISSION)
        VALUES (roleId, 'permission.crm.return.all');

    /* Warehouse Operator Role */
    roleId := nextval('ROLE_ID_SEQ');
    INSERT INTO ERP_ROLE (ROLE_ID, NAME, DESCRIPTION)
        VALUES (roleId, 'Warehouse Operator', 'Manages order dispatch, returns and inventory');
    INSERT INTO ERP_ROLEPERMISSION (ROLE, PERMISSION)
        VALUES (roleId, 'permission.production.product.view');
    INSERT INTO ERP_ROLEPERMISSION (ROLE, PERMISSION)
        VALUES (roleId, 'permission.production.inventory.all');
    INSERT INTO ERP_ROLEPERMISSION (ROLE, PERMISSION)
        VALUES (roleId, 'permission.crm.order.dispatch');
    INSERT INTO ERP_ROLEPERMISSION (ROLE, PERMISSION)
        VALUES (roleId, 'permission.crm.return.all');

    /* System Administrator Role */
    roleId := nextval('ROLE_ID_SEQ');
    INSERT INTO ERP_ROLE (ROLE_ID, NAME, DESCRIPTION)
        VALUES (roleId, 'System Administrator', 'Manages logins, roles and security');
    INSERT INTO ERP_ROLEPERMISSION (ROLE, PERMISSION)
        VALUES (roleId, 'permission.security.all');

    /* Production Manager Role */
    roleId := nextval('ROLE_ID_SEQ');
    INSERT INTO ERP_ROLE (ROLE_ID, NAME, DESCRIPTION)
        VALUES (roleId, 'Production Manager', 'Manages all apsects of production');
    INSERT INTO ERP_ROLEPERMISSION (ROLE, PERMISSION)
        VALUES (roleId, 'permission.production.all');

    /* Production Operator Role */
    roleId := nextval('ROLE_ID_SEQ');
    INSERT INTO ERP_ROLE (ROLE_ID, NAME, DESCRIPTION)
        VALUES (roleId, 'Production Operator', 'Processes production orders');
    INSERT INTO ERP_ROLEPERMISSION (ROLE, PERMISSION)
        VALUES (roleId, 'permission.production.product.view');
    INSERT INTO ERP_ROLEPERMISSION (ROLE, PERMISSION)
        VALUES (roleId, 'permission.production.material.view');
    INSERT INTO ERP_ROLEPERMISSION (ROLE, PERMISSION)
        VALUES (roleId, 'permission.production.productionorder.view');
    INSERT INTO ERP_ROLEPERMISSION (ROLE, PERMISSION)
        VALUES (roleId, 'permission.production.productionorder.update');
    INSERT INTO ERP_ROLEPERMISSION (ROLE, PERMISSION)
        VALUES (roleId, 'permission.production.inventory.view');
    INSERT INTO ERP_ROLEPERMISSION (ROLE, PERMISSION)
        VALUES (roleId, 'permission.production.inventory.update');

END $$;
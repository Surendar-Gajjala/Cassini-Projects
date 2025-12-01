DELETE FROM ERP_SESSION;
COPY ERP_LOGINROLE TO '/Users/reddy/Temp/cassinisys/export/erp_loginrole' DELIMITER ';' CSV HEADER;
COPY ERP_LOGIN TO '/Users/reddy/Temp/cassinisys/export/erp_login' DELIMITER ';' CSV HEADER;

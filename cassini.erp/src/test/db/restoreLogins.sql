DELETE FROM ERP_SESSION;
DELETE FROM ERP_LOGINROLE;
DELETE FROM ERP_LOGIN;

COPY ERP_LOGIN FROM '/Users/reddy/Temp/cassinisys/export/erp_login' DELIMITER ';' CSV HEADER;
COPY ERP_LOGINROLE FROM '/Users/reddy/Temp/cassinisys/export/erp_loginrole' DELIMITER ';' CSV HEADER;

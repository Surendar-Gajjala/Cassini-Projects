@echo off
SET /P _pwd= Please enter DB password:
SET /P _host= Please enter host address:
@echo on
call set PGPASSWORD=%_pwd%
call groovy alterSchemaScript.groovy psql %_host%
echo Successfully completed.
timeout /t 30
#!/usr/bin/env groovy
@Grab(group='org.postgresql', module='postgresql', version='9.4-1203-jdbc42')
@GrabConfig(systemClassLoader=true)
import groovy.sql.Sql

if(this.args.length != 2) {
    println "Insufficient arguments"
    return;
}

def url = this.args[0]
def schema = this.args[1]
url += '?currentSchema=' + schema

def user = 'cassinisys'
def password = 'cassinisys'
def driver = 'org.postgresql.Driver'
sql = Sql.newInstance(url, user, password, driver)

executeSql('restoreLogins.sql')

sql.close()

def executeSql(sqlFile) {
    String sqlCommands = new File(sqlFile).getText('UTF-8');
    sql.execute sqlCommands
}
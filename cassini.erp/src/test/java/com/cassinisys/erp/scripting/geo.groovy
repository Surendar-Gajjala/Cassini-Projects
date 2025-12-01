import groovy.sql.Sql

def url = 'jdbc:postgresql://103.241.183.21:5432/cassiniapps'
def schema = 'cassini'
url += '?currentSchema=' + schema

def user = 'cassinisys'
def password = 'cassinisys'
def driver = 'org.postgresql.Driver'
def sql = Sql.newInstance(url, user, password, driver)


sql.eachRow('select * from ERP_GEOTRACKER where rowid > 451') { row ->
    println "{'lat':$row.latitude, 'lng':$row.longitude},"
}
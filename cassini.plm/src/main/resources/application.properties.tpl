# jdbc props
jdbc.driverClassName=org.postgresql.Driver
jdbc.url=jdbc:postgresql://localhost:5432/cassiniapps
#jdbc.url=jdbc:postgresql://103.241.183.21:5432/cassiniapps
jdbc.user=cassinisys
jdbc.pass=cassinisys

# hibernate props
hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
hibernate.show_sql=false
#hibernate.hbm2ddl.auto=validate
hibernate.multiTenancy=SCHEMA
hibernate.default_schema=cassini_plm
hibernate.tenant_identifier_resolver=com.cassinisys.platform.config.CassiniTenantIdentifierResolver
hibernate.multi_tenant_connection_provider=com.cassinisys.platform.config.CassiniTenantConnectionProvider

server.servlet.session.timeout=120m

# cassini properties
cassini.default.tenantid=cassini_plm
cassini.tenants=cassini_plm
cassini.fs.root=C:/Users/sgajjala/temp/cassinisys.apps/cassini.plm
cassini.plugins.dir=/Users/reddy/MyHome/Temp/plugins

#Jwt info
app.jwtSecret=HRlELXqpSB
app.jwtExpiration=3600
app.refreshExpiration=9000000

#Mail properties
mail.username=no-reply@cassinisys.com
mail.password=%_Ca$$ys#
mail.smtp.auth=true
mail.smtp.starttls.enable=true
mail.smtp.host=smtp-mail.outlook.com
mail.smtp.port=587
mail.smtp.ssl.trust=smtp-mail.outlook.com

#license grace period
license.grace.period=10

#Swagger properties
swagger.enable=true
# jdbc props
jdbc.driverClassName=org.postgresql.Driver
jdbc.url=jdbc:postgresql://localhost:5432/cassiniapps
#jdbc.url=jdbc:postgresql://103.241.183.21:5432/cassiniapps
jdbc.user=cassinisys
jdbc.pass=cassinisys
s
# hibernate props
hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
hibernate.show_sql=false
#hibernate.hbm2ddl.auto=validate
hibernate.multiTenancy=SCHEMA
#hibernate.default_schema=dppl
hibernate.tenant_identifier_resolver=com.cassinisys.platform.config.CassiniTenantIdentifierResolver
hibernate.multi_tenant_connection_provider=com.cassinisys.platform.config.CassiniTenantConnectionProvider

# cassini properties
cassini.tenants=cassini_test
cassini.fs.root=C:/Users/Suresh Cassini/cassini/cassinisys.test


#Assembla Access keys
api_key=6f0855fc333792194952
api_key_secret=b72a575110ca2b96d43627d290d540e142238a45

ticket_create_url=https://api.assembla.com/v1/spaces/cassinisys/tickets.json
ticket_update_url=https://api.assembla.com/v1/spaces/cassinisys/tickets/%s
ticket_doc_url=https://api.assembla.com/v1/spaces/cassinisys/documents

mail.username=mailer@cassinimail.com
mail.password=uKnYbqACAM
mail.smtp.auth=true
mail.smtp.starttls.enable=true
mail.smtp.host=mail.cassinimail.com
mail.smtp.port=587
mail.smtp.ssl.trust=mail.cassinimail.com

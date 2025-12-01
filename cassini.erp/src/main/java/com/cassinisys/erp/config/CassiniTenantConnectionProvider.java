package com.cassinisys.erp.config;

import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by reddy on 9/23/15.
 */
@Component
public class CassiniTenantConnectionProvider implements MultiTenantConnectionProvider {
    private Map<String, Connection> connections = new HashMap<>();

    @Autowired
    private Environment env;

    @Autowired
    private DataSource dataSource;

    @Override
    public Connection getAnyConnection() throws SQLException {
        if(TenantManager.get().getTenantId() != null) {
            return getConnection(TenantManager.get().getTenantId());
        }
        else {
            return dataSource.getConnection();
        }
    }

    @Override
    public void releaseAnyConnection(Connection connection) throws SQLException {

    }

    @Override
    public Connection getConnection(String tenantIdentifier) throws SQLException {

        if(tenantIdentifier == null || tenantIdentifier.isEmpty()) {
            return dataSource.getConnection();
        }

        Connection connection = connections.get(tenantIdentifier);
        if(connection == null || connection.isClosed()) {
            connection = dataSource.getConnection();
            //connection.setSchema(tenantIdentifier);
            Statement statement = connection.createStatement();
            try {
                statement.execute("set search_path to '" + tenantIdentifier + "'");
            } finally {
                statement.close();
            }
            connection.setAutoCommit(false);

            connections.put(tenantIdentifier, connection);
        }

        /*
        Connection connection = dataSource.getConnection();
        connection.setSchema(tenantIdentifier);
        connection.setAutoCommit(false);
        */

        return connection;
    }

    private void printTables(Connection connection) {
        try {
            DatabaseMetaData dbmd = connection.getMetaData();
            String[] types = {"TABLE"};
            ResultSet rs = dbmd.getTables(null, null, "%", types);
            while (rs.next()) {
                System.out.println(rs.getString("TABLE_NAME"));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void releaseConnection(String tenantIdentifier, Connection connection) throws SQLException {

    }

    @Override
    public boolean supportsAggressiveRelease() {
        return false;
    }

    @Override
    public boolean isUnwrappableAs(Class unwrapType) {
        return false;
    }

    @Override
    public <T> T unwrap(Class<T> unwrapType) {
        return null;
    }
}

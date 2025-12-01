package com.cassinisys.platform.config;

import com.cassinisys.platform.service.security.SessionWrapper;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
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

    @Autowired
    private SessionWrapper sessionWrapper;

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

        String key = tenantIdentifier;

        try {
            if (sessionWrapper != null && sessionWrapper.getSession() != null) {
                key = sessionWrapper.getSession().getLogin().getLoginName() + ":" + tenantIdentifier;
            }
        } catch (Exception e) {

        }

        Connection connection = connections.get(key);
        if(connection == null || connection.isClosed()) {
            connection = dataSource.getConnection();
            connection.setSchema(tenantIdentifier);
            connection.setAutoCommit(false);

            connections.put(key, connection);
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
                //System.out.println(rs.getString("TABLE_NAME"));
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

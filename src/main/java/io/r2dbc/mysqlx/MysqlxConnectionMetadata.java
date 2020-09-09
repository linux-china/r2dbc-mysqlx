package io.r2dbc.mysqlx;

import io.r2dbc.spi.ConnectionMetadata;

/**
 * MySQL connection metadata
 *
 * @author linux_china
 */
public class MysqlxConnectionMetadata implements ConnectionMetadata {
    private final String version;

    public MysqlxConnectionMetadata(String version) {
        this.version = version;
    }

    @Override
    public String getDatabaseProductName() {
        return "mysql";
    }

    @Override
    public String getDatabaseVersion() {
        return this.version;
    }
}

package io.r2dbc.mysqlx;

import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import io.r2dbc.spi.ConnectionFactoryProvider;

import static io.r2dbc.spi.ConnectionFactoryOptions.DRIVER;

/**
 * Mysql X Protocol R2DBC ConnectionFactoryProvider
 *
 * @author linux_china
 */
public class MysqlxConnectionFactoryProvider implements ConnectionFactoryProvider {
    public static final String MYSQLX_DRIVER = "mysqlx";

    @Override
    public ConnectionFactory create(ConnectionFactoryOptions connectionFactoryOptions) {
        return null;
    }

    @Override
    public boolean supports(ConnectionFactoryOptions connectionFactoryOptions) {
        return MYSQLX_DRIVER.equals(connectionFactoryOptions.getValue(DRIVER));
    }

    @Override
    public String getDriver() {
        return MYSQLX_DRIVER;
    }
}

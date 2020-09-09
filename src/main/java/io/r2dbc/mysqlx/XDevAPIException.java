package io.r2dbc.mysqlx;

import com.mysql.cj.xdevapi.XDevAPIError;
import io.r2dbc.spi.R2dbcException;

/**
 * XDevAPI Exception
 *
 * @author linux_china
 */
public class XDevAPIException extends R2dbcException {

    public XDevAPIException(XDevAPIError error) {
        super(error.getMessage(), error.getSQLState());
    }
}

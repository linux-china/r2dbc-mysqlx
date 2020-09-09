package io.r2dbc.mysqlx.convert;

import com.mysql.cj.xdevapi.Row;

/**
 * Row's named value converter
 *
 * @author linux_china
 */
@FunctionalInterface
public interface RowNamedValueConverter {
    Object convert(Row row, String name);
}

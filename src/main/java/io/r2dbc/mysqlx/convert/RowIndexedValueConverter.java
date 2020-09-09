package io.r2dbc.mysqlx.convert;

import com.mysql.cj.xdevapi.Row;

/**
 *  Row's indexed value converter
 *
 * @author linux_china
 */
@FunctionalInterface
public interface RowIndexedValueConverter {
   Object convert(Row row, Integer index);
}

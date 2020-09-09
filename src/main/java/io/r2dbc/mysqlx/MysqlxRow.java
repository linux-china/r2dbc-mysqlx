package io.r2dbc.mysqlx;


import com.mysql.cj.xdevapi.Row;
import io.r2dbc.mysqlx.convert.RowIndexedValueConverter;
import io.r2dbc.mysqlx.convert.RowNamedValueConverter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

/**
 * MySQL X protocol R2DBC Row
 *
 * @author linux_china
 */
@SuppressWarnings("unchecked")
public class MysqlxRow implements io.r2dbc.spi.Row {
    private static final Map<Class<?>, RowIndexedValueConverter> INDEXED_CONVERTS = new HashMap<>();
    private static final Map<Class<?>, RowNamedValueConverter> NAMED_CONVERTS = new HashMap<>();

    static {
        // int
        INDEXED_CONVERTS.put(int.class, Row::getInt);
        INDEXED_CONVERTS.put(Integer.class, Row::getInt);
        NAMED_CONVERTS.put(int.class, Row::getInt);
        NAMED_CONVERTS.put(Integer.class, Row::getInt);
        // long
        INDEXED_CONVERTS.put(long.class, Row::getLong);
        INDEXED_CONVERTS.put(Long.class, Row::getLong);
        NAMED_CONVERTS.put(long.class, Row::getLong);
        NAMED_CONVERTS.put(Long.class, Row::getLong);
        // boolean
        INDEXED_CONVERTS.put(boolean.class, Row::getBoolean);
        INDEXED_CONVERTS.put(Boolean.class, Row::getBoolean);
        NAMED_CONVERTS.put(boolean.class, Row::getBoolean);
        NAMED_CONVERTS.put(Boolean.class, Row::getBoolean);
        // local date time
        INDEXED_CONVERTS.put(LocalDateTime.class, (row, index) -> row.getTimestamp(index).toLocalDateTime());
        INDEXED_CONVERTS.put(LocalDate.class, (row, index) -> row.getDate(index).toLocalDate());
        INDEXED_CONVERTS.put(LocalTime.class, (row, index) -> row.getTime(index).toLocalTime());
        // String
        INDEXED_CONVERTS.put(String.class, Row::getString);
    }

    private final Row rawRow;
    private final MysqlxRowMetadata rowMetadata;

    public MysqlxRow(Row rawRow, MysqlxRowMetadata rowMetadata) {
        this.rawRow = rawRow;
        this.rowMetadata = rowMetadata;
    }

    @Override
    public <T> T get(int index, Class<T> type) {
        RowIndexedValueConverter converter;
        if (type.equals(Object.class)) {
            converter = INDEXED_CONVERTS.get(rowMetadata.getColumnMetadata(index).getJavaType());
        } else {
            converter = INDEXED_CONVERTS.get(type);
        }
        if (converter == null) {
            return null;
        }
        return (T) converter.convert(rawRow, index);
    }

    @Override
    public <T> T get(String name, Class<T> type) {
        RowNamedValueConverter converter;
        if (type.equals(Object.class)) {
            converter = NAMED_CONVERTS.get(rowMetadata.getColumnMetadata(name).getJavaType());
        } else {
            converter = NAMED_CONVERTS.get(type);
        }
        if (converter == null) {
            return null;
        }
        return (T) NAMED_CONVERTS.get(type).convert(rawRow, name);
    }
}

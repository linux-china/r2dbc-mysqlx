package io.r2dbc.mysqlx;

import com.mysql.cj.xdevapi.Column;
import com.mysql.cj.xdevapi.SqlMultiResult;
import com.mysql.cj.xdevapi.Type;
import io.r2dbc.spi.ColumnMetadata;
import io.r2dbc.spi.RowMetadata;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * MySQL X protocol R2DBC RowMetadata
 *
 * @author linux_china
 */
public class MysqlxRowMetadata implements RowMetadata {
    private final List<SingleColumnMetadata> columns = new ArrayList<>();
    private final Map<String, SingleColumnMetadata> columnMap = new HashMap<>();

    public MysqlxRowMetadata(SqlMultiResult sqlMultiResult) {
        for (Column column : sqlMultiResult.getColumns()) {
            String columnName = column.getColumnName();
            SingleColumnMetadata columnMetadata = new SingleColumnMetadata(columnName, convertToJavaType(column.getType()));
            columns.add(columnMetadata);
            columnMap.put(columnName, columnMetadata);
        }
    }

    @Override
    public ColumnMetadata getColumnMetadata(int index) {
        return this.columns.get(index);
    }

    @Override
    public ColumnMetadata getColumnMetadata(String name) {
        return columnMap.get(name);
    }

    @Override
    public Iterable<? extends ColumnMetadata> getColumnMetadatas() {
        return this.columns;
    }

    @Override
    public Collection<String> getColumnNames() {
        return columns.stream().map(SingleColumnMetadata::getName).collect(Collectors.toList());
    }

    public Class<?> convertToJavaType(Type xType) {
        // R2DBC Data Types: http://r2dbc.io/spec/0.8.2.RELEASE/spec/html/#datatypes  local date
        switch (xType) {
            case BIT:
            case TINYINT:
                return Byte.class;
            case SMALLINT:
            case MEDIUMINT:
            case INT:
                return Integer.class;
            case BIGINT:
                return Long.class;
            case FLOAT:
                return Float.class;
            case DECIMAL:
                return BigDecimal.class;
            case DOUBLE:
                return Double.class;
            case ENUM:
            case GEOMETRY:
            case JSON:
            case STRING:
                return String.class;
            case TIME:
                return LocalTime.class;
            case DATE:
                return LocalDate.class;
            case DATETIME:
            case TIMESTAMP:
                return LocalDateTime.class;
            case BYTES:
            case SET:
                break;
            default:
                return Object.class;
        }
        return Object.class;
    }
}

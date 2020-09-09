package io.r2dbc.mysqlx.update;

import io.r2dbc.mysqlx.SingleColumnMetadata;
import io.r2dbc.spi.ColumnMetadata;
import io.r2dbc.spi.RowMetadata;

import java.util.Collection;
import java.util.Collections;

/**
 * single value row metadata
 *
 * @author linux_china
 */
public class SingleValueRowMetadata implements RowMetadata {
    private final ColumnMetadata columnMetadata;
    private final String columnName;
    private final Class<?> valueType;

    public SingleValueRowMetadata(String columnName, Class<?> valueType) {
        this.columnName = columnName;
        this.valueType = valueType;
        this.columnMetadata = new SingleColumnMetadata(columnName, valueType);
    }

    @Override
    public ColumnMetadata getColumnMetadata(int index) {
        return columnMetadata;
    }

    @Override
    public ColumnMetadata getColumnMetadata(String name) {
        return columnMetadata;
    }

    @Override
    public Iterable<? extends ColumnMetadata> getColumnMetadatas() {
        return Collections.singletonList(columnMetadata);
    }

    @Override
    public Collection<String> getColumnNames() {
        return Collections.singletonList(columnName);
    }
}

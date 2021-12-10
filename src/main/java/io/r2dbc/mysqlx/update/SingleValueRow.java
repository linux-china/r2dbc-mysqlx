package io.r2dbc.mysqlx.update;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;

/**
 * single value row
 *
 * @author linux_china
 */
@SuppressWarnings("unchecked")
public class SingleValueRow<V> implements Row {
    private final V value;
    private final String name;
    private final RowMetadata metadata;

    public SingleValueRow(String name, V value, RowMetadata metadata) {
        this.name = name;
        this.value = value;
        this.metadata = metadata;
    }

    @Override
    public <T> T get(int index, Class<T> type) {
        return (T) value;
    }

    @Override
    public <T> T get(String name, Class<T> type) {
        return (T) value;
    }

    @Override
    public RowMetadata getMetadata() {
        return this.metadata;
    }
}

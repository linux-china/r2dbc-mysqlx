package io.r2dbc.mysqlx.update;

import io.r2dbc.spi.Row;

/**
 * single value row
 *
 * @author linux_china
 */
@SuppressWarnings("unchecked")
public class SingleValueRow<V> implements Row {
    private final V value;
    private final String name;

    public SingleValueRow(String name, V value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public <T> T get(int index, Class<T> type) {
        return (T) value;
    }

    @Override
    public <T> T get(String name, Class<T> type) {
        return (T) value;
    }

}

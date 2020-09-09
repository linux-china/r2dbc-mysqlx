package io.r2dbc.mysqlx;

import io.r2dbc.spi.ColumnMetadata;
import io.r2dbc.spi.Nullability;

/**
 * Single column metadata
 *
 * @author linux_china
 */
public class SingleColumnMetadata implements ColumnMetadata {
    private final String column;
    private final Class<?> javaType;

    public SingleColumnMetadata(String column, Class<?> javaType) {
        this.column = column;
        this.javaType = javaType;
    }

    @Override
    public Class<?> getJavaType() {
        return this.javaType;
    }

    @Override
    public String getName() {
        return column;
    }

    @Override
    public Object getNativeTypeMetadata() {
        return this.javaType;
    }

    @Override
    public Nullability getNullability() {
        return Nullability.NON_NULL;
    }

    @Override
    public Integer getPrecision() {
        return Integer.MAX_VALUE;
    }

    @Override
    public Integer getScale() {
        return null;
    }
}

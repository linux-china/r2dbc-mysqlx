package io.r2dbc.mysqlx;

import io.r2dbc.spi.ColumnMetadata;
import io.r2dbc.spi.Nullability;
import io.r2dbc.spi.R2dbcType;
import io.r2dbc.spi.Type;

/**
 * Single column metadata
 *
 * @author linux_china
 */
public class SingleColumnMetadata implements ColumnMetadata {
    private final String column;
    private final Class<?> javaType;
    private final R2dbcType r2dbcType;

    public SingleColumnMetadata(String column, Class<?> javaType, R2dbcType r2dbcType) {
        this.column = column;
        this.javaType = javaType;
        this.r2dbcType = r2dbcType;
    }

    @Override
    public Class<?> getJavaType() {
        return this.javaType;
    }

    @Override
    public Type getType() {
        return r2dbcType;
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

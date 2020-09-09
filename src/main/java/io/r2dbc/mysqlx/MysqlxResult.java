package io.r2dbc.mysqlx;

import com.mysql.cj.xdevapi.SqlMultiResult;
import com.mysql.cj.xdevapi.SqlResult;
import com.mysql.cj.xdevapi.UpdateResult;
import io.r2dbc.mysqlx.update.SingleValueRow;
import io.r2dbc.mysqlx.update.SingleValueRowMetadata;
import io.r2dbc.spi.Result;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.annotation.Nullable;

import java.util.function.BiFunction;

/**
 * MySQL X R2DBC Result
 *
 * @author linux_china
 */
public class MysqlxResult implements Result {
    private final String sql;
    private final SqlResult sqlResult;
    @Nullable
    private final String autoGeneratedColumn;

    public MysqlxResult(String sql, SqlResult sqlResult, @Nullable String autoGeneratedColumn) {
        this.sql = sql;
        this.sqlResult = sqlResult;
        this.autoGeneratedColumn = autoGeneratedColumn;
    }

    @Override
    public Publisher<Integer> getRowsUpdated() {
        return Mono.just((int) sqlResult.getAffectedItemsCount());
    }

    @Override
    public <T> Publisher<T> map(BiFunction<Row, RowMetadata, ? extends T> mappingFunction) {
        // select
        if (sqlResult instanceof SqlMultiResult) {
            return Flux.fromIterable(sqlResult.fetchAll()).map(row -> {
                return mappingFunction.apply(new MysqlxRow(row), new MysqlxRowMetadata((SqlMultiResult) sqlResult));
            });
        } else if (sqlResult instanceof UpdateResult) { //insert, update, delete
            if (autoGeneratedColumn != null) {
                Long autoIncrementValue = sqlResult.getAutoIncrementValue();
                return Mono.just(mappingFunction.apply(new SingleValueRow<>(autoGeneratedColumn, autoIncrementValue),
                        new SingleValueRowMetadata(autoGeneratedColumn, Long.class)));
            } else {
                return Mono.empty();
            }
        }
        return Flux.empty();
    }

}

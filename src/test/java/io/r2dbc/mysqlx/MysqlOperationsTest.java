package io.r2dbc.mysqlx;

import com.mysql.cj.xdevapi.SqlResult;
import com.mysql.cj.xdevapi.SqlStatement;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;

public class MysqlOperationsTest extends MysqlxBaseTest {
    @Test
    public void testQuery() throws Exception {
        SqlStatement statement = mysqlSession.sql("select * from account");
        CompletableFuture<SqlResult> future = statement.executeAsync();
        Mono.fromFuture(future)
                .flatMapMany(rows -> Flux.fromIterable(rows.fetchAll()))
                .subscribe(row -> {
                    System.out.println(row.getString("nick"));
                });
        Thread.sleep(2000);
    }
}

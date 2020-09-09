package io.r2dbc.mysqlx;

import com.mysql.cj.xdevapi.SqlResult;
import com.mysql.cj.xdevapi.SqlStatement;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class MysqlOperationsTest extends MysqlxBaseTest {
    @Test
    public void testQuery() throws Exception {
        List<Object> binding = new ArrayList<>();
        binding.add(1);
        SqlStatement statement = mysqlSession.sql("select * from account where id = ?").bind(binding);
        CompletableFuture<SqlResult> future = statement.executeAsync();
        Mono.fromFuture(future)
                .flatMapMany(sqlResult -> Flux.fromIterable(sqlResult.fetchAll()))
                .subscribe(row -> {
                    System.out.println(row.getString("nick"));
                });
        Thread.sleep(2000);
    }

    @Test
    public void testInsert() throws Exception {
        mysqlSession.startTransaction();
        SqlStatement statement = mysqlSession.sql("insert into people(nick, created_at) values('dddd',now())");
        CompletableFuture<SqlResult> future = statement.executeAsync();
        Mono.fromFuture(future)
                .map(SqlResult::getAutoIncrementValue)
                .doOnError(throwable -> {
                    System.out.println("rollback");
                    mysqlSession.rollback();
                }).doOnSuccess(aLong -> {
            System.out.println("commit");
            mysqlSession.commit();
        })
                .subscribe(System.out::println);
        Thread.sleep(2000);
    }
}

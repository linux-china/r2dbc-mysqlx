package io.r2dbc.mysqlx;

import io.r2dbc.spi.Connection;
import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.Result;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * MySQL X Protocol connection factory test
 *
 * @author linux_china
 */
@SuppressWarnings("unchecked")
public class MysqlxConnectionFactoryProviderTest {
    private static Connection connection;

    @BeforeAll
    public static void setUp() {
        String r2dcUrl = "r2dbc:mysqlx://root@127.0.0.1:33060/r2dbc";
        ConnectionFactory factory = ConnectionFactories.get(r2dcUrl);
        connection = ((Mono<Connection>) factory.create()).block();
    }

    @Test
    public void testQuery() throws Exception {
        Mono<? extends Result> monoResult = (Mono<? extends Result>) connection.createStatement("select * from people where  id = ?").bind(0, 1).execute();
        monoResult.flatMapMany(result -> {
            return Flux.from(result.map((row, rowMetadata) -> {
                return row.get("nick");
            }));
        }).subscribe(System.out::println);
        Thread.sleep(1000);
    }

    @Test
    public void testExecute() throws Exception {
        Mono<? extends Result> monoResult = (Mono<? extends Result>) connection.createStatement("INSERT INTO people(nick, created_at) VALUES (?,now())")
                .bind(0, "nick1")
                .execute();
        monoResult.flatMap(result -> {
            return Mono.from(result.getRowsUpdated());
        }).subscribe(System.out::println);
        Thread.sleep(1000);
    }

}

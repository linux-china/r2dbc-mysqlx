package io.r2dbc;

import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class R2dbcTest {

    @Test
    public void testOperations() throws Exception {
        String r2dcUrl = "r2dbc:mysqlx://root@127.0.0.1:33060/r2dbc";
        ConnectionFactory factory = ConnectionFactories.get(r2dcUrl);
        List<String> nicks = Mono.from(factory.create())
                .flatMapMany(connection -> connection
                        .createStatement("SELECT *  FROM people")
                        .execute())
                .flatMap(result -> result
                        .map((row, rowMetadata) -> row.get("nick", String.class)))
                .collectList()
                .block();
        assertThat(nicks).isNotEmpty();
    }
}

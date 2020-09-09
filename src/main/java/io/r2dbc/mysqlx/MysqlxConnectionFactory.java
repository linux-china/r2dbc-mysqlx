package io.r2dbc.mysqlx;

import com.mysql.cj.xdevapi.SessionFactory;
import io.r2dbc.spi.Connection;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryMetadata;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

/**
 * MySQL X Protocol R2DBC ConnectionFactory
 *
 * @author linux_china
 */
public class MysqlxConnectionFactory implements ConnectionFactory {
    private final SessionFactory sessionFactory;
    private final String mysqlxUrl;

    public MysqlxConnectionFactory(String mysqlxUrl) {
        this.mysqlxUrl = mysqlxUrl;
        this.sessionFactory = new SessionFactory();
    }

    @Override
    public Publisher<? extends Connection> create() {
        return Mono.just(new MysqlxConnection(this.sessionFactory.getSession(this.mysqlxUrl)));
    }

    @Override
    public ConnectionFactoryMetadata getMetadata() {
        return () -> "mysqlx";
    }
}

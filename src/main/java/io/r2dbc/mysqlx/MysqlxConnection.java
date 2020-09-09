package io.r2dbc.mysqlx;

import com.mysql.cj.xdevapi.Session;
import com.mysql.cj.xdevapi.SqlResult;
import io.r2dbc.spi.*;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;
import reactor.core.publisher.MonoProcessor;

import java.sql.Timestamp;
import java.util.concurrent.CompletableFuture;

/**
 * MySQL X Protocol R2DBC Connection
 *
 * @author linux_china
 */
public class MysqlxConnection implements Connection {
    private final Session mysqlxSession;
    private ConnectionMetadata connectionMetadata;
    private Timestamp lastHealthCheck;

    private final MonoProcessor<Void> onClose = MonoProcessor.create();

    public MysqlxConnection(Session mysqlxSession) {
        this.mysqlxSession = mysqlxSession;
        init();
        // health check with timer
        /* Flux.interval(Duration.ofSeconds(60)).subscribe(timestamp -> {
            healthCheck();
        });*/
    }

    private void init() {
        mysqlxSession
                .sql(" select version() as version")
                .executeAsync()
                .thenAccept(sqlResult -> {
                    String version = sqlResult.fetchOne().getString("version");
                    this.connectionMetadata = new MysqlxConnectionMetadata(version);
                });
    }

    private void healthCheck() {
        CompletableFuture<SqlResult> future = mysqlxSession.sql("select now() as now").executeAsync();
        Mono.fromFuture(future)
                .flatMap(sqlResult -> {
                    return Mono.just(sqlResult.fetchOne());
                })
                .doOnError(throwable -> {
                    this.dispose();
                })
                .subscribe(row -> {
                    this.lastHealthCheck = row.getTimestamp("now");
                });


    }

    @Override
    public Publisher<Void> beginTransaction() {
        return Mono.empty();
    }

    @Override
    public Publisher<Void> close() {
        return this.onClose;
    }

    public void dispose() {
        try {
            mysqlxSession.close();
        } catch (Exception ignore) {

        }
        this.onClose.onComplete();
    }

    @Override
    public Publisher<Void> commitTransaction() {
        return Mono.empty();
    }

    @Override
    public Batch createBatch() {
        throw new UnsupportedOperationException("MySQL X Protocol doesn't support batch!");
    }

    @Override
    public Publisher<Void> createSavepoint(String name) {
        return Mono.empty();
    }

    @Override
    public Statement createStatement(String sql) {
        return new MysqlxStatement(this.mysqlxSession, sql);
    }

    @Override
    public boolean isAutoCommit() {
        return true;
    }

    @Override
    public ConnectionMetadata getMetadata() {
        return this.connectionMetadata;
    }

    @Override
    public IsolationLevel getTransactionIsolationLevel() {
        return IsolationLevel.READ_UNCOMMITTED;
    }

    @Override
    public Publisher<Void> releaseSavepoint(String name) {
        return Mono.empty();
    }

    @Override
    public Publisher<Void> rollbackTransaction() {
        return Mono.empty();
    }

    @Override
    public Publisher<Void> rollbackTransactionToSavepoint(String name) {
        return Mono.empty();
    }

    @Override
    public Publisher<Void> setAutoCommit(boolean autoCommit) {
        return Mono.empty();
    }

    @Override
    public Publisher<Void> setTransactionIsolationLevel(IsolationLevel isolationLevel) {
        return Mono.empty();
    }

    @Override
    public Publisher<Boolean> validate(ValidationDepth depth) {
        return Mono.empty();
    }
}

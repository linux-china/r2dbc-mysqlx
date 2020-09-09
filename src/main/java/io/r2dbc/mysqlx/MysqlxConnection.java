package io.r2dbc.mysqlx;

import com.mysql.cj.xdevapi.Session;
import com.mysql.cj.xdevapi.SqlResult;
import io.r2dbc.spi.*;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.MonoProcessor;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.concurrent.CompletableFuture;

/**
 * MySQL X Protocol R2DBC Connection
 *
 * @author linux_china
 */
public class MysqlxConnection implements Connection {
    private final Session mysqlxSession;
    private ConnectionMetadata connectionMetadata;
    private LocalDateTime lastHealthCheck;

    private final MonoProcessor<Void> onClose = MonoProcessor.create();

    public MysqlxConnection(Session mysqlxSession) {
        this.mysqlxSession = mysqlxSession;
        init();
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
        // health check with timer
        Flux.interval(Duration.ofSeconds(60))
                .flatMap(timestamp -> this.validate(ValidationDepth.REMOTE))
                .doOnError(throwable -> this.dispose())
                .subscribe(result -> {
                    if (result) {
                        this.lastHealthCheck = LocalDateTime.now();
                    } else {
                        this.dispose();
                    }
                });
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
    public Publisher<Void> beginTransaction() {
        mysqlxSession.startTransaction();
        return Mono.empty();
    }

    @Override
    public Publisher<Void> commitTransaction() {
        mysqlxSession.commit();
        return Mono.empty();
    }

    @Override
    public Publisher<Void> rollbackTransaction() {
        mysqlxSession.rollback();
        return Mono.empty();
    }

    @Override
    public Batch createBatch() {
        throw new UnsupportedOperationException("MySQL X Protocol doesn't support batch!");
    }

    @Override
    public Publisher<Void> createSavepoint(String name) {
        mysqlxSession.setSavepoint(name);
        return Mono.empty();
    }

    @Override
    public Publisher<Void> releaseSavepoint(String name) {
        mysqlxSession.releaseSavepoint(name);
        return Mono.empty();
    }

    @Override
    public Publisher<Void> rollbackTransactionToSavepoint(String name) {
        mysqlxSession.rollbackTo(name);
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
        return IsolationLevel.READ_COMMITTED;
    }

    @Override
    public Publisher<Void> setAutoCommit(boolean autoCommit) {
        throw new UnsupportedOperationException("MySQL X Protocol doesn't support auto commit!");
    }

    @Override
    public Publisher<Void> setTransactionIsolationLevel(IsolationLevel isolationLevel) {
        return Mono.empty();
    }

    @Override
    public Publisher<Boolean> validate(ValidationDepth depth) {
        if (depth == ValidationDepth.LOCAL) {
            return Mono.just(true);
        }
        CompletableFuture<SqlResult> future = mysqlxSession.sql("select 1").executeAsync();
        return Mono.fromFuture(future).map(Iterator::hasNext);
    }
}

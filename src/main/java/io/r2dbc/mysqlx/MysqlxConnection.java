package io.r2dbc.mysqlx;

import com.mysql.cj.xdevapi.Session;
import com.mysql.cj.xdevapi.SqlStatement;
import io.r2dbc.spi.*;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;
import reactor.core.publisher.MonoProcessor;

/**
 * MySQL X Protocol R2DBC Connection
 *
 * @author linux_china
 */
public class MysqlxConnection implements Connection {
    private final Session mysqlxSession;
    private ConnectionMetadata connectionMetadata;

    private final MonoProcessor<Void> onClose = MonoProcessor.create();

    public MysqlxConnection(Session mysqlxSession) {
        this.mysqlxSession = mysqlxSession;
        init();
    }

    private void init() {
        SqlStatement statement = mysqlxSession.sql(" select version() as version");
        statement.executeAsync().thenAccept(sqlResult -> {
            String version = sqlResult.fetchOne().getString("version");
            this.connectionMetadata = new MysqlxConnectionMetadata(version);
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
        mysqlxSession.close();
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

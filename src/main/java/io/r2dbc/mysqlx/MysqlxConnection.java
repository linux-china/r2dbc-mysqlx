package io.r2dbc.mysqlx;

import com.mysql.cj.xdevapi.Session;
import com.mysql.cj.xdevapi.SqlStatement;
import io.r2dbc.spi.*;
import org.reactivestreams.Publisher;

/**
 * MySQL X Protocol R2DBC Connection
 *
 * @author linux_china
 */
public class MysqlxConnection implements Connection {
    private final Session mysqlxSession;
    private ConnectionMetadata connectionMetadata;

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
        return null;
    }

    @Override
    public Publisher<Void> close() {
        return null;
    }

    @Override
    public Publisher<Void> commitTransaction() {
        return null;
    }

    @Override
    public Batch createBatch() {
        return null;
    }

    @Override
    public Publisher<Void> createSavepoint(String name) {
        return null;
    }

    @Override
    public Statement createStatement(String sql) {
        return new MysqlxStatement(this.mysqlxSession, sql);
    }

    @Override
    public boolean isAutoCommit() {
        return false;
    }

    @Override
    public ConnectionMetadata getMetadata() {
        return this.connectionMetadata;
    }

    @Override
    public IsolationLevel getTransactionIsolationLevel() {
        return null;
    }

    @Override
    public Publisher<Void> releaseSavepoint(String name) {
        return null;
    }

    @Override
    public Publisher<Void> rollbackTransaction() {
        return null;
    }

    @Override
    public Publisher<Void> rollbackTransactionToSavepoint(String name) {
        return null;
    }

    @Override
    public Publisher<Void> setAutoCommit(boolean autoCommit) {
        return null;
    }

    @Override
    public Publisher<Void> setTransactionIsolationLevel(IsolationLevel isolationLevel) {
        return null;
    }

    @Override
    public Publisher<Boolean> validate(ValidationDepth depth) {
        return null;
    }
}

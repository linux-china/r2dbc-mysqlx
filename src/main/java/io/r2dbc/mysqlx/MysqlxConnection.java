package io.r2dbc.mysqlx;

import com.mysql.cj.xdevapi.Session;
import io.r2dbc.spi.*;
import org.reactivestreams.Publisher;

/**
 * MySQL X Protocol R2DBC Connection
 *
 * @author linux_china
 */
public class MysqlxConnection implements Connection {
    private Session mysqlxSession;

    public MysqlxConnection(Session mysqlxSession) {
        this.mysqlxSession = mysqlxSession;
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
        return null;
    }

    @Override
    public boolean isAutoCommit() {
        return false;
    }

    @Override
    public ConnectionMetadata getMetadata() {
        return null;
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

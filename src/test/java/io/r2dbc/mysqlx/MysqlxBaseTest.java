package io.r2dbc.mysqlx;

import com.mysql.cj.xdevapi.Session;
import com.mysql.cj.xdevapi.SessionFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;


public abstract class MysqlxBaseTest {
    protected static Session mysqlSession;

    @BeforeAll
    public static void setUp() {
        String mysqlxUrl = "mysqlx://127.0.0.1:33060/r2dbc?user=root&password=";
        mysqlSession = new SessionFactory().getSession(mysqlxUrl);
    }

    @AfterAll
    public static void tearDown() {
        if (mysqlSession != null) {
            mysqlSession.close();
        }
    }

}


/**
 *
 * @author Lam Du Thach
 */
public class MysqlConnections {
    public static volatile JDBCConnectionPool connection = null;
    private final String SERVER_HOST = "127.0.0.1";
    private final String DATA_BASE = "TEST";
    private final String USERNAME = "test";
    private final String PASSWORD = "test";
    
    public MysqlConnections() {
        if (connection == null) {
            connection = new JDBCConnectionPool(SERVER_HOST, DATA_BASE, USERNAME, PASSWORD);
        }
    }
}

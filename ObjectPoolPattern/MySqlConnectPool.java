
import org.apache.commons.dbcp2.BasicDataSource;

/**
 *
 * @author Lam Du Thach
 */
public class MySqlConnectPool {

    public static volatile BasicDataSource ds = null;
    private final String SERVER_HOST = "127.0.0.1";
    private final String DATA_BASE = "TEST";
    private final String USERNAME = "test";
    private final String PASSWORD = "test";

    public MySqlConnectPool() {
        if (ds == null) {
            ds = new BasicDataSource();
            ds.setUrl("jdbc:mysql://" + SERVER_HOST + ":3306/" + DATA_BASE + "?autoReconnect=true&useSSL=false&allowMultiQueries=true");
            ds.setUsername(USERNAME);
            ds.setPassword(PASSWORD);
            ds.setMaxTotal(150);
            ds.setMinIdle(30);
            ds.setMaxIdle(70);
            ds.setMaxConnLifetimeMillis(3000);
            ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
            ds.setInitialSize(50);
            ds.setRemoveAbandonedOnBorrow(true);
            ds.setAbandonedUsageTracking(false);
            ds.setLogExpiredConnections(false);
            ds.setLifo(false);
            ds.addConnectionProperty("useUnicode", "true");
            ds.addConnectionProperty("characterEncoding", "UTF-8");
            ds.setRemoveAbandonedOnMaintenance(true);
        }
    }

}


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.apache.commons.lang3.exception.ExceptionUtils;

/**
 *
 * @author Lam Du Thach
 */
public class JDBCConnectionPool extends ObjectPool<Connection> {

    private final String SERVER_HOST, DATA_BASE, USER_NAME, PASSWORD;

    public JDBCConnectionPool(String SERVER_HOST, String DATA_BASE, String USER_NAME, String PASSWORD) {
        super();
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (Exception ex) {
            System.out.println(ExceptionUtils.getStackTrace(ex));
        }
        this.SERVER_HOST = SERVER_HOST;
        this.DATA_BASE = DATA_BASE;
        this.USER_NAME = USER_NAME;
        this.PASSWORD = PASSWORD;
    }

    @Override
    protected Connection create() {
        try {
            return DriverManager.getConnection("jdbc:mysql://" + SERVER_HOST + "/" + DATA_BASE
                    + "?useServerPrepStmts=false&rewriteBatchedStatements=true&user="
                    + USER_NAME + "&password=" + PASSWORD);
        } catch (SQLException ex) {
            System.out.println(ExceptionUtils.getStackTrace(ex));
        }
        return null;
    }

    @Override
    protected boolean validate(Connection conn) {
        try {
            return !conn.isClosed();
        } catch (SQLException ex) {
            System.out.println(ExceptionUtils.getStackTrace(ex));
        }
        return false;
    }

    @Override
    protected void expire(Connection conn) {
        try {
            conn.close();
        } catch (SQLException ex) {
            System.out.println(ExceptionUtils.getStackTrace(ex));
        }
    }
}

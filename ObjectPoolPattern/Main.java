
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.exception.ExceptionUtils;

/**
 *
 * @author Lam Du Thach
 */
public class Main {

    public static void main(String[] args) {
        Connection conn = MysqlConnections.connection.getConn();
        // Do with conn;
        MysqlConnections.connection.releaseConn(conn);
    }

    public List<String> getListAccount(boolean isActive) {
        List<String> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT ACCOUNT FROM ACCOUNT";
            if (isActive) {
                sql += "\nWHERE IS_ACTIVE = 1";
            }
            conn = MysqlConnections.connection.getConn();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(rs.getString("ACCOUNT"));
            }
        } catch (SQLException ex) {
            System.out.println(ExceptionUtils.getStackTrace(ex));
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                MysqlConnections.connection.releaseConn(conn);
            } catch (SQLException ex) {
                System.out.println(ExceptionUtils.getStackTrace(ex));
            }
        }
        return list;
    }
}

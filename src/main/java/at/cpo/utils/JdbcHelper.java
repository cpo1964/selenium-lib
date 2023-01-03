package at.cpo.utils;

//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.HashMap;

/**
 * The type Jdbc helper.
 */
public class JdbcHelper {
    /**
     * The Constant LOGGER.
     */
//    private static final Logger LOGGER = LogManager.getLogger(JdbcHelper.class);

    /**
     * The Url.
     */
    static final String URL = "url";
    /**
     * The Username.
     */
    static final String USERNAME = "username";
    /**
     * The Password.
     */
    static final String PASSWORD = "password";

    /**
     * The Config.
     */
    private static HashMap<String, String> config;

    /**
     * The Driver name.
     */
    private static String drivername;

    /**
     * The constant con.
     */
    private static Connection con;

    private JdbcHelper() {
    }

    /**
     * Sets driver name.
     *
     * @param driverName the driver name
     */
    public static void setDriverName(String driverName) {
        drivername = driverName;
    }

    /**
     * Gets config.
     *
     * @return the config
     */
    private static HashMap<String, String> getConfig() {
        return config;
    }

    /**
     * Sets config.
     *
     * @param config the config
     */
    static void setConfig(HashMap<String, String> config) {
        JdbcHelper.config = config;
    }

    /**
     * Gets connection.
     *
     * @return the connection
     * @throws ClassNotFoundException the class not found exception
     */
    private static Connection getConnection() throws ClassNotFoundException {
        HashMap<String, String> map = getConfig();
        Class.forName(drivername);
        try {
            con = DriverManager.getConnection(map.get(URL), map.get(USERNAME), map.get(PASSWORD));
        } catch (SQLException ex) {
            // log an exception. fro example:
//            LOGGER.debug("Failed to create the database connection.");
        }
        return con;
    }
    /**
     * Gets first value of column.
     *
     * @param sql    the sql
     * @param column the column
     * @return the first value of column
     * @throws SQLException           the sql exception
     * @throws ClassNotFoundException the class not found exception
     */
    static String getFirstValueOfColumn(String sql, String column) throws SQLException, ClassNotFoundException {
        String[] columns = {column};
        return getFirstColumnValues(sql, columns)[0];
    }

    static String[] getFirstColumnValues(String sql, String[] columns) throws SQLException, ClassNotFoundException {
        String[] res = new String[columns.length];
        ResultSet rs = execute(sql);
        if (rs != null && rs.next()) {
            int i = 0;
            for (String column : columns) {
                res[i] = rs.getString(column);
                i++;
            }
        }
        return res;
    }


    /**
     * Execute result set.
     *
     * @param sql the sql
     * @return the result set
     * @throws SQLException           the sql exception
     * @throws ClassNotFoundException the class not found exception
     */
    static ResultSet execute(String sql) throws SQLException, ClassNotFoundException {
        Connection con = getConnection();
        ResultSet rs = null;
        if (con != null) {
            try {
                Statement stmt = con.createStatement();
                rs = stmt.executeQuery(sql);
                con.commit();
            } catch (SQLException e) {
                return null;
            }
        }
        return rs;
    }

}

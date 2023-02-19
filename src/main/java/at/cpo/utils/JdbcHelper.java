/*
 * MIT License
 *
 * Copyright (c) 2018 Elias Nogueira
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package at.cpo.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

/**
 * The type Jdbc helper.
 */
public class JdbcHelper {

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

    /**
     * Instantiates a new jdbc helper.
     */
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
            // log an exception ...
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

    /**
     * Gets the first column values.
     *
     * @param sql the sql
     * @param columns the columns
     * @return the first column values
     * @throws SQLException the SQL exception
     * @throws ClassNotFoundException the class not found exception
     */
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
            try (Statement stmt = con.createStatement()) {
                rs = stmt.executeQuery(sql);
                con.commit();
            } catch (SQLException e) {
                return null;
            }
        }
        return rs;
    }

}

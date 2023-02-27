/*
 * Copyright (C) 2023 Christian Pöcksteiner (christian.poecksteiner@aon.at)
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *         https://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.cpo1964.utils;

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

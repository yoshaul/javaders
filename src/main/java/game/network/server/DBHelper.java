/*
 * This file is part of Javaders.
 * Copyright (c) Yossi Shaul
 *
 * Javaders is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Javaders is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Javaders.  If not, see <http://www.gnu.org/licenses/>.
 */

package game.network.server;

import game.network.server.ejb.JNDINames;

import javax.ejb.EJBException;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Helper class to get and release database connections.
 */
public class DBHelper {

    /**
     * Returns a connection to the game statabase.
     *
     * @return Connection to the game statabase.
     */
    public static Connection getConnection() {
        try {
            InitialContext ic = new InitialContext();
            DataSource ds = (DataSource) ic.lookup(JNDINames.DBName);

            return ds.getConnection();

        } catch (Exception exception) {
            throw new EJBException("Unable to connect to database. " +
                    exception.getMessage());
        }
    }

    /**
     * Closes a connection.
     *
     * @param connection Connection to close.
     */
    public static void releaseConnection(Connection connection) {

        if (connection != null) {
            try {
                connection.close();

            } catch (SQLException sqlException) {
                throw new EJBException("Error in releaseConnection: " +
                        sqlException.getMessage());
            }
        }
    }

}
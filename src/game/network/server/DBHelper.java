package game.network.server;

import game.network.server.ejb.JNDINames;

import java.sql.Connection;
import java.sql.SQLException;

import javax.ejb.EJBException;
import javax.naming.InitialContext;
import javax.sql.DataSource;

/**
 * Helper class to get and release database connections.
 */
public class DBHelper {

    /**
     * Returns a connection to the game statabase.
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
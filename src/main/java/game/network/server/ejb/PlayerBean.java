package game.network.server.ejb;

import game.network.server.DBHelper;

import java.rmi.RemoteException;
import java.sql.*;

import javax.ejb.*;

/**
 * The <code>Player</code> EJB holds the data of a registered player.
 */
public class PlayerBean implements EntityBean {

    private EntityContext entityContext;
//    private Connection connection;
    
    private String userName;
    private String password;
    private String email;
    
    public PlayerBean() {
        // Must implement empty constructor
    }

    /**
     * @see PlayerHome#create(String, String)
     */
    public String ejbCreate(String userName, String password) 
    		throws CreateException {

        return ejbCreate(userName, password, null);
        
    }
    
    /**
     * For the ejbCreate(String, String)
     */
    public void ejbPostCreate(String userName, String password) 
		throws CreateException {
        // do nothing
    }

    /**
     * @see PlayerHome#create(String, String, String)
     */
    public String ejbCreate(String userName, String password, String email) 
			throws CreateException {

        this.userName = userName;
        this.password = password;
        this.email = email;
        
        Connection connection = null;
        try {
            connection = DBHelper.getConnection();
			PreparedStatement ps = connection.prepareStatement(
					"INSERT INTO player " +
					"(user_name, password, email) " +
					"VALUES(?, ?, ?)");
		
			ps.setString(1, userName);
			ps.setString(2, password);
			ps.setString(3, email);
			
			ps.executeUpdate();
			
			ps.close();
			
	        return userName;
			
		}
		catch (SQLException sqlException) {
			throw new CreateException(sqlException.getMessage());
		}
        finally {
            DBHelper.releaseConnection(connection);
        }
        
    }
    
    /**
     * For the ejbCreate(String, String, String)
     */
    public void ejbPostCreate(String userName, String password, 
            String email) throws CreateException {
        // do nothing
    }
    
    /**
     * Set the primary key.
     */
    public void ejbActivate() throws EJBException, RemoteException {
        userName = (String) entityContext.getPrimaryKey();
    }
    
    /**
     * Unset the primary key.
     */
    public void ejbPassivate() throws EJBException, RemoteException {
        userName = null;
    }
    
    /**
     * Load the details from the database.
     */
    public void ejbLoad() throws EJBException, RemoteException {

        Connection connection = null;
        try {
            // Get the primary key
            String userName = (String) entityContext.getPrimaryKey();
            
            connection = DBHelper.getConnection();
			PreparedStatement ps = connection.prepareStatement(
					"SELECT user_name, password, email " +
					"FROM player " +
					"WHERE user_name = ?");
		
			ps.setString(1, userName);
			
			ResultSet rs = ps.executeQuery();
			
			if (rs.next()) {
			    this.userName = rs.getString(1);
			    this.password = rs.getString(2);
			    this.email = rs.getString(3);
			}
			else {
			    throw new EJBException("No such player: " + userName);
			}
			
			rs.close();
			ps.close();
			
		}
		catch (SQLException sqlException) {
			throw new EJBException(sqlException);
		}
        finally {
            DBHelper.releaseConnection(connection);
        }

    }
      
    /**
     * Remove the player from the database.
     */
    public void ejbRemove() throws RemoveException, EJBException,
            RemoteException {

        Connection connection = null;
        try {
            // Get the primary key
            String userName = (String) entityContext.getPrimaryKey();
            
            connection = DBHelper.getConnection();
			PreparedStatement ps = connection.prepareStatement(
					"DELETE FROM player " +
					"WHERE user_name = ?");
		
			ps.setString(1, userName);			
			
			ps.executeUpdate();
			
			ps.close();
			
		}
		catch (SQLException sqlException) {
			throw new RemoveException("Error while trying to remove player " +
			        userName + "\n" + sqlException.getMessage());
		}
        finally {
            DBHelper.releaseConnection(connection);
        }

    }
    
    /**
     * Store the details to the database.
     */
    public void ejbStore() throws EJBException, RemoteException {
        
        Connection connection = null;
        try {
            // Get the primary key
            String userName = (String) entityContext.getPrimaryKey();
            
            connection = DBHelper.getConnection();
			PreparedStatement ps = connection.prepareStatement(
					"UPDATE player " +
					"SET password = ?, email = ? " +
					"WHERE user_name = ?");
		
			ps.setString(1, password);
			ps.setString(2, email);
			ps.setString(3, userName);			
			
			ps.executeUpdate();
			
			ps.close();
			
		}
		catch (SQLException sqlException) {
			throw new EJBException(sqlException);
		}
        finally {
            DBHelper.releaseConnection(connection);
        }
		
    }
    
    public void setEntityContext(EntityContext entityContext) 
    		throws EJBException, RemoteException {

        this.entityContext = entityContext;
    }
    
    public void unsetEntityContext() throws EJBException, RemoteException {

        entityContext = null;

    }
    
    /**
     * Find by primary key (user name).
     */
    public String ejbFindByPrimaryKey(String userName) 
    		throws FinderException {
        
        boolean found = false;
        
        Connection connection = null;
        try {
            connection = DBHelper.getConnection();
			PreparedStatement ps = connection.prepareStatement(
					"SELECT user_name " +
					"FROM player " +
					"WHERE user_name = ?");
		
			ps.setString(1, userName);			
			
			ResultSet rs = ps.executeQuery();
			
			found = rs.next();
			
			rs.close();
			ps.close();
			
			if (found) {
			    return userName;
			}
			else {
			    throw new FinderException("User " + userName + " doesn't exist");
			}
			
		}
		catch (SQLException sqlException) {
			throw new EJBException(sqlException);
		}
        finally {
            DBHelper.releaseConnection(connection);
        }
    }
    
    /* Implement business methods */
    
    public String getUserName() {
        return userName;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
}
package game.network.server.ejb;

import game.GameConstants;

import java.rmi.RemoteException;
import java.sql.*;

import javax.ejb.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class PlayerBean implements EntityBean {

    private EntityContext entityContext;
    private Connection connection;
    
    private String userName;
    private String password;
    private String email;
    
    public PlayerBean() {
        // Must implement empty constructor
    }


    public String ejbCreate(String userName, String password) 
    		throws CreateException {

        return ejbCreate(userName, password, null);
        
    }
    
    public void ejbPostCreate(String userName, String password) 
		throws CreateException {

    }

    public String ejbCreate(String userName, String password, String email) 
			throws CreateException {

        this.userName = userName;
        this.password = password;
        this.email = email;
        
        try {
            
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
        
    }
    
    public void ejbPostCreate(String userName, String password, String email) 
		throws CreateException {

    }
    
    public void ejbActivate() throws EJBException, RemoteException {
        userName = (String) entityContext.getPrimaryKey();
    }
    
    public void ejbPassivate() throws EJBException, RemoteException {
        userName = null;
    }
    
    public void ejbLoad() throws EJBException, RemoteException {

        try {
            // Get the primary key
            String userName = (String) entityContext.getPrimaryKey();
            
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

    }
        
    public void ejbRemove() throws RemoveException, EJBException,
            RemoteException {

        try {
            // Get the primary key
            String userName = (String) entityContext.getPrimaryKey();
            
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

    }
    
    public void ejbStore() throws EJBException, RemoteException {
        
        try {
        
            // Get the primary key
            String userName = (String) entityContext.getPrimaryKey();
            
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
		
    }
    
    public void setEntityContext(EntityContext entityContext) 
    		throws EJBException, RemoteException {

        this.entityContext = entityContext;
        
        // Get connection to the game database
        try {
            InitialContext context = new InitialContext();
            
            DataSource dataSource = (DataSource)
            	context.lookup(GameConstants.DBName);
            
            connection = dataSource.getConnection();
        }
        catch (NamingException ne) {
            throw new EJBException(ne);
        }
        catch (SQLException sqlException) {
            throw new EJBException(sqlException);
        }

    }
    
    public void unsetEntityContext() throws EJBException, RemoteException {

        entityContext = null;
        
        // Close the DataSource connection
        try {
            connection.close();
        }
        catch (SQLException sqlException) {
            throw new EJBException(sqlException);
        }
        finally {
            connection = null;
        }
        
    }
    
    public String ejbFindByPrimaryKey(String userName) 
    		throws FinderException {
        
        boolean found = false;
        
        try {
            
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
    }
    
    
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

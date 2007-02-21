package game.network.server.ejb;

import game.GameConstants;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.EntityBean;
import javax.ejb.EntityContext;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class SequenceFactoryBean implements EntityBean {

    private EntityContext entityContext;
    private Connection connection;

    private String tableName;	// Primary key
    private Long nextID;
    
    public String ejbCreate(String tableName) throws CreateException {
        this.tableName = tableName;
        this.nextID = new Long(1);
        
        try {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO sequence_factory " +
                    "(table_name, next_id) " +
                    "VALUES(?, ?)");
            
            ps.setString(1, tableName);
            ps.setLong(2, nextID.longValue());
            
            ps.executeUpdate();
            
            ps.close();
            
            return tableName;
            
        }
        catch (SQLException sqlException){
            throw new CreateException(sqlException.getMessage());
        }
        
    }
    
    public void ejbPostCreate(String tableName) {
        
    }
    
    public void ejbActivate() throws EJBException, RemoteException {
        this.tableName = (String) entityContext.getPrimaryKey();
    }
    
    public void ejbPassivate() throws EJBException, RemoteException {
        this.tableName = null;
    }
    
    public void ejbLoad() throws EJBException, RemoteException {
        try {
            String tableName = (String) entityContext.getPrimaryKey();
            
            PreparedStatement ps = connection.prepareStatement(
                    "SELECT table_name, next_id " +
                    "FROM sequence_factory " +
                    "WHERE table_name = ?");
            
            ps.setString(1, tableName);
            
            ResultSet rs = ps.executeQuery();
            
			if (rs.next()) {
			    this.tableName = rs.getString(1);
			    this.nextID = new Long(rs.getLong(2));
			}
			else {
			    throw new EJBException("Table name not found: " + tableName);
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
            String tableName = (String) entityContext.getPrimaryKey();
            
            PreparedStatement ps = connection.prepareStatement(
                    "DELETE FROM sequence_factory " +
                    "WHERE table_name = ?");
            
            ps.setString(1, tableName);
            
            ps.executeUpdate();
            
        }
        catch (SQLException sqlException) {
			throw new RemoveException("Error while trying to remove " +
					tableName + " from sequence_factory.\n" + 
					sqlException.getMessage());
        }

    }
    public void ejbStore() throws EJBException, RemoteException {
        try {
            
            PreparedStatement ps = connection.prepareStatement(
                    "UPDATE sequence_factory " +
                    "SET next_id = ? " +
                    "WHERE table_name = ?");
            
            ps.setLong(1, nextID.longValue());
            ps.setString(2, tableName);
            
            ps.executeUpdate();
            
            ps.close();
            
        }
        catch (SQLException sqlException) {
            throw new EJBException(sqlException);
        }

    }
    
    public void setEntityContext(EntityContext entityContext) throws EJBException,
            RemoteException {
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
        this.entityContext = null;
        
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
    
    public String ejbFindByPrimaryKey(String tableName) throws FinderException {
        boolean found = false;
        
        try {
            
			PreparedStatement ps = connection.prepareStatement(
					"SELECT table_name " +
					"FROM sequence_factory " +
					"WHERE table_name = ?");
		
			ps.setString(1, tableName);			
			
			ResultSet rs = ps.executeQuery();
			
			found = rs.next();
			
			rs.close();
			ps.close();
			
			if (found) {
			    return tableName;
			}
			else {
			    throw new FinderException("Table " + tableName + " doesn't exist");
			}
			
		}
		catch (SQLException sqlException) {
			throw new EJBException(sqlException);
		}
    }
    
    public Long getNextID() { 
        
        Long id = new Long(nextID.longValue());
        
        // Increment the id by 1
        nextID = new Long(nextID.longValue() + 1);
        
        return id;
        
    }
    
}

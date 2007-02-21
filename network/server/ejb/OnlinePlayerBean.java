package game.network.server.ejb;

import java.rmi.RemoteException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;

import javax.ejb.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.rmi.PortableRemoteObject;
import javax.sql.DataSource;

public class OnlinePlayerBean implements EntityBean {

    private EntityContext entityContext;
    private Connection connection;
    
    private Long sessionId;		// Primary key
    private String userName;	// Foreign key
    private long sessionStartTime;
    private boolean acceptInvitations;
    
    public OnlinePlayerBean() {
        // Must implement no arguments constructor
    }
    
    public Long ejbCreate(String userName) throws CreateException {

        this.userName = userName;
        this.acceptInvitations = false; /** TODO: default is false? */
        this.sessionStartTime = System.currentTimeMillis();
        
        try {
            makeConnection();
            
	        // Get a unique id from the SequenceFactory bean
	
			// create an initial naming context
			InitialContext initialContext = new InitialContext();
			
			// obtain the environment maming context of the application client
			Context env = (Context) initialContext.lookup("java:comp/env");
			
			// retrieve the object bound to the name ejb/SignInHome
			Object objref = env.lookup("ejb/SequenceFactory");
			
			// narrow the context to a SequenceFactoryHome object
			SequenceFactoryHome sequenceFactoryHome = (SequenceFactoryHome) 
				PortableRemoteObject.narrow(objref, SequenceFactoryHome.class);
			
			// find the sequence factory for online player
			SequenceFactory sequenceFactory = 
			    sequenceFactoryHome.findByPrimaryKey("online_player");
        
			sessionId = sequenceFactory.getNextID();

	        PreparedStatement ps = connection.prepareStatement(
	            "INSERT INTO online_player " +
	        	"(session_id, user_name, session_start_time, accept_invitations) " +
	        	"values(?, ?, ?, ?)");
	        
	        ps.setLong(1, sessionId.longValue());
	        ps.setString(2, userName);
	        ps.setLong(3, sessionStartTime);
	        ps.setBoolean(4, acceptInvitations);
	        
	        ps.executeUpdate();
	        
	        ps.close();
	        
	        return sessionId;
        
        }
        catch (Exception exception) {
            throw new CreateException(exception.getMessage());
        }
        finally {
            releaseConnection();
        }

    }
    
    public void ejbPostCreate(String userName) {
        
    }
    
    public void ejbActivate() throws EJBException, RemoteException {
        this.sessionId = (Long) entityContext.getPrimaryKey();
    }
    
    public void ejbPassivate() throws EJBException, RemoteException {
        sessionId = null;
    }
    
    public void ejbLoad() throws EJBException, RemoteException {
        
        try {
            makeConnection();
            Long sessionId = (Long) entityContext.getPrimaryKey();
            
            PreparedStatement ps = connection.prepareStatement(
                    "SELECT session_id, user_name, " +
                    "session_start_time, accept_invitations " +
                    "FROM online_player " +
                    "WHERE session_id = ?");
            
            ps.setLong(1, sessionId.longValue());
            
            ResultSet rs = ps.executeQuery();
            
			if (rs.next()) {
			    this.sessionId = new Long(rs.getLong(1));
			    this.userName = rs.getString(2);
			    this.sessionStartTime = rs.getLong(3);
			    this.acceptInvitations = rs.getBoolean(4);
			}
			else {
			    throw new EJBException("Session not found: " + sessionId);
			}
			
			rs.close();
			ps.close();
            
        }
        catch (SQLException sqlException) {
            throw new EJBException(sqlException);
        }
        finally {
            releaseConnection();
        }
        
    }

    public void ejbRemove() throws 
    	RemoveException, EJBException, RemoteException {

        try {
            makeConnection();
            Long sessionId = (Long) entityContext.getPrimaryKey();
            
            PreparedStatement ps = connection.prepareStatement(
                    "DELETE FROM online_player " +
                    "WHERE session_id = ?");
            
            ps.setLong(1, sessionId.longValue());
            
            ps.executeUpdate();
            
        }
        catch (SQLException sqlException) {
			throw new RemoveException("Error while trying to remove " +
					"online player id " + sessionId + "\n" + 
					sqlException.getMessage());
        }
        finally {
            releaseConnection();
        }

    }
    public void ejbStore() throws EJBException, RemoteException {
        
        try {
            makeConnection();
            PreparedStatement ps = connection.prepareStatement(
                    "UPDATE online_player " +
                    "SET user_name = ?, session_start_time = ?, " +
                    "accept_invitations = ? " +
                    "WHERE session_id = ?");
            
            ps.setString(1, userName);
            ps.setLong(2, sessionStartTime);
            ps.setBoolean(3, acceptInvitations);
            ps.setLong(4, sessionId.longValue());
            
            ps.executeUpdate();
            
            ps.close();
            
        }
        catch (SQLException sqlException) {
            throw new EJBException(sqlException);
        }
        finally {
            releaseConnection();
        }

    }
    public void setEntityContext(EntityContext entityContext) 
			throws EJBException, RemoteException {

		this.entityContext = entityContext;
//		
//		// Get connection to the game database
//		try {
//		    InitialContext context = new InitialContext();
//		    
//		    DataSource dataSource = (DataSource)
//		    	context.lookup(GameConstants.DBName);
//		    
//		    connection = dataSource.getConnection();
//		}
//		catch (NamingException ne) {
//		    throw new EJBException(ne);
//		}
//		catch (SQLException sqlException) {
//		    throw new EJBException(sqlException);
//		}

    }

	public void unsetEntityContext() throws EJBException, RemoteException {
	
		entityContext = null;
//		
//		// Close the DataSource connection
//		try {
//		    connection.close();
//		}
//		catch (SQLException sqlException) {
//		    throw new EJBException(sqlException);
//		}
//		finally {
//		    connection = null;
//		}
	}
	
    public Long ejbFindByPrimaryKey(Long sessionId) 
			throws FinderException {

		boolean found = false;
		
		try {
		    makeConnection();
			PreparedStatement ps = connection.prepareStatement(
					"SELECT session_id " +
					"FROM online_player " +
					"WHERE session_id = ?");
		
			ps.setLong(1, sessionId.longValue());			
			
			ResultSet rs = ps.executeQuery();
			
			found = rs.next();
			
			rs.close();
			ps.close();
			
			if (found) {
			    return sessionId;
			}
			else {
			    throw new FinderException("Session " + sessionId + " doesn't exist");
			}
			
		}
		catch (SQLException sqlException) {
			throw new EJBException(sqlException);
		}
        finally {
            releaseConnection();
        }
    }
    

    public Collection ejbFindByAcceptInvitations() 
			throws FinderException {

		Collection result = new ArrayList();
		boolean found = false;
		
		try {
		    makeConnection();
			PreparedStatement ps = connection.prepareStatement(
					"SELECT session_id " +
					"FROM online_player " +
					"WHERE accept_invitations = ?");
		
			ps.setBoolean(1, true);			
			
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
			    Long sessionId = new Long(rs.getLong(1));
			    result.add(sessionId);
			}
			
			rs.close();
			ps.close();
			
			return result;
			
		}
		catch (SQLException sqlException) {
			throw new EJBException(sqlException);
		}
        finally {
            releaseConnection();
        }
	}
    
    public OnlinePlayerModel getOnlinePlayerModel() {
        return new OnlinePlayerModel(sessionId, userName, sessionStartTime);
    }
	
    public boolean isAcceptInvitations() {
        return acceptInvitations;
    }
    public void setAcceptInvitations(boolean acceptInvitations) {
        this.acceptInvitations = acceptInvitations;
    }
    public long getSessionStartTime() {
        return sessionStartTime;
    }
    public void setSessionStartTime(long sessionStartTime) {
        this.sessionStartTime = sessionStartTime;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public Long getSessionId() {
        return sessionId;
    }
    
    
    private void makeConnection() {
        try {
            InitialContext ic = new InitialContext();
            DataSource ds = (DataSource) ic.lookup(JNDINames.DBName);

            connection = ds.getConnection();
            
        } catch (Exception exception) {
            throw new EJBException("Unable to connect to database. " +
                exception.getMessage());
        }
    }

    private void releaseConnection() {
        
        if (connection != null) {
            try {
                connection.close();
                connection = null;
                
            } catch (SQLException sqlException) {
                throw new EJBException("Error in releaseConnection: " + 
                        sqlException.getMessage());
            }    
        }
        
    }
    
}


package game.network.server.ejb;

import game.network.InvalidLoginException;

import java.rmi.RemoteException;
import java.sql.*;

import javax.ejb.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;
import javax.sql.DataSource;

/**
 * @author Yossi Shaul
 *
 */
public class SignInBean implements SessionBean {

	private Connection connection;	
	private SessionContext sessionContext;
	
	/* SessionBean class must implement an empty constructor */
	public SignInBean() {}
	
	/* SessionBean interface implementation */
	
	public void ejbActivate() {}
	public void ejbPassivate() {}
	
	public void ejbRemove() {
	    try {
		    if (connection != null) {
			    connection.close();
		    }
		} 
		catch (SQLException sqlException) {
			sqlException.printStackTrace();
		}
	}

	public void setSessionContext(SessionContext sessionContext) {
		
		this.sessionContext = sessionContext;
	}
	
	
	/* SignInHome interface implementation */
	
	public void ejbCreate() throws CreateException {
		try {
			InitialContext initialContext = new InitialContext();

			DataSource ds = (DataSource) initialContext.lookup(JNDINames.DBName);
			connection = ds.getConnection();
		}
		
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/* SignIn interface implementation (business methods) */
	
	public Long login(String userName, String password) 
			throws EJBException, InvalidLoginException {
	    
	    try {
	        InitialContext initialContext = new InitialContext();
	        
			// retrieve the object bound to the name ejb/Player
			Object objref = initialContext.lookup(JNDINames.PLAYER_BEAN);
			
			// narrow the context to a PlayerHome object
			PlayerHome playerHome = (PlayerHome) 
				PortableRemoteObject.narrow(objref, PlayerHome.class);

			// create the enterprise bean instance
			Player player = playerHome.findByPrimaryKey(userName);
			
			String playerPassword = player.getPassword();
			if (!playerPassword.equals(password)) {
			    throw new InvalidLoginException();
			}
	        
			// Validation successful, create session
			objref = initialContext.lookup(JNDINames.ONLINE_PLAYER_BEAN);
			
			OnlinePlayerHome onlineHome = (OnlinePlayerHome)
				PortableRemoteObject.narrow(objref, OnlinePlayerHome.class);
			
			OnlinePlayer onlinePlayer = onlineHome.create(userName);
			
			return (Long) onlinePlayer.getPrimaryKey();
			
	    }
	    catch (NamingException ne) {
	        throw new EJBException(ne.getMessage());
	    }
	    catch (RemoteException re) {
	        throw new EJBException(re.getMessage());
	    }
	    catch (CreateException ce) {
	        throw new EJBException(ce.getMessage());
	    }
	    catch (FinderException fe) {
	        throw new InvalidLoginException();
	    }
	    
	}
	
	/**
	 * Logout - remove the session from the online players table
	 * @param sessionId	Session id to remove
	 */
	public void logout(Long sessionId) { 
	    
	    try {
	        InitialContext initialContext = new InitialContext();
	        
	        Object objref = initialContext.lookup(JNDINames.ONLINE_PLAYER_BEAN);
	        
	        OnlinePlayerHome onlinePlayerHome = (OnlinePlayerHome)
	            PortableRemoteObject.narrow(objref, OnlinePlayerHome.class);
	        
	        OnlinePlayer onlinePlayer = 
	            onlinePlayerHome.findByPrimaryKey(sessionId);
	        
	        onlinePlayer.remove();
	        
	    }
	    catch (Exception exception) {
	        exception.printStackTrace();
	        throw new EJBException(exception.getMessage());
	    }
	    
	}
	
	public void addUser(String userName, String password, 
			String email) throws EJBException {
	    
	    try {
	        InitialContext initialContext = new InitialContext();
	        
			// retrieve the object bound to the name ejb/Player
			Object objref = initialContext.lookup(JNDINames.PLAYER_BEAN);
			
			// narrow the context to a PlayerHome object
			PlayerHome playerHome = (PlayerHome) 
				PortableRemoteObject.narrow(objref, PlayerHome.class);
			
			playerHome.create(userName, password, email);
			
		}
	    catch (CreateException ce) {
	        throw new EJBException("Can't create user " + userName +".\n"
	                + ce.getMessage());
	    }
		catch (Exception exception) {
		    exception.printStackTrace();
			throw new EJBException(exception);
		}
		
	}

}	// end class SignInBean

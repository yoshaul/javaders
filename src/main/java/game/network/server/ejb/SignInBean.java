
package game.network.server.ejb;

import game.network.InvalidLoginException;

import javax.ejb.*;

/**
 * The signin bean is used for users validation, logout and signups.
 */
public class SignInBean implements SessionBean {

	private SessionContext sessionContext;
	
	/* SessionBean class must implement an empty constructor */
	public SignInBean() {}
	
	/* SessionBean interface implementation */
	
	public void ejbActivate() {}
	public void ejbPassivate() {}
	
	public void ejbRemove() {}

	public void setSessionContext(SessionContext sessionContext) {
		
		this.sessionContext = sessionContext;
	}
	
	
	/* SignInHome interface implementation */
	
	public void ejbCreate() throws CreateException {

	}
	
	/* SignIn interface implementation (business methods) */
	
	/**
	 * Login with the supplied username and password.
	 */
	public Long login(String userName, String password) 
			throws EJBException, InvalidLoginException {
	    
	    try {

			PlayerHome playerHome = (PlayerHome) 
				EJBHelper.getEJBHome(JNDINames.PLAYER_BEAN, PlayerHome.class);

			// create the enterprise bean instance
			Player player = playerHome.findByPrimaryKey(userName);
			
			String playerPassword = player.getPassword();
			if (!playerPassword.equals(password)) {
			    throw new InvalidLoginException();
			}
	        
			// Validation successful, create session
			OnlinePlayerHome onlineHome = (OnlinePlayerHome)
				EJBHelper.getEJBHome(JNDINames.ONLINE_PLAYER_BEAN, 
				        OnlinePlayerHome.class);
			
			OnlinePlayer onlinePlayer = onlineHome.create(userName);
			
			return (Long) onlinePlayer.getPrimaryKey();
			
	    }
	    catch (FinderException fe) {
	        throw new InvalidLoginException();
	    }
	    catch (Exception ne) {
	        throw new EJBException(ne.getMessage());
	    }
	}
	
	/**
	 * Logout - remove the session from the online players table
	 * @param sessionId	Session id to remove
	 */
	public void logout(Long sessionId) { 
	    
	    try {
	        
			OnlinePlayerHome onlinePlayerHome = (OnlinePlayerHome)
				EJBHelper.getEJBHome(JNDINames.ONLINE_PLAYER_BEAN, 
			        OnlinePlayerHome.class);
	        
	        OnlinePlayer onlinePlayer = 
	            onlinePlayerHome.findByPrimaryKey(sessionId);
	        
	        onlinePlayer.remove();
	        
	    }
	    catch (Exception exception) {
	        throw new EJBException(exception.getMessage());
	    }
	    
	}
	
	/**
	 * Create a new user accout
	 * @param userName	Username
	 * @param password	Password
	 * @param email		Email
	 */
	public void addUser(String userName, String password, 
			String email) throws EJBException {
	    
	    try {

			PlayerHome playerHome = (PlayerHome) 
				EJBHelper.getEJBHome(JNDINames.PLAYER_BEAN, PlayerHome.class);
			
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
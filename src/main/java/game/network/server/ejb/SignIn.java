
package game.network.server.ejb;

import game.network.InvalidLoginException;

import java.rmi.RemoteException;

import javax.ejb.EJBObject;

/**
 * The signin bean is used for users validation, logout and signups.
 */
public interface SignIn extends EJBObject {
	
    /**
     * Try to log in with the supplied username and password.
     * @param userName	User name
     * @param password	Passwrod
     * @return	Session id if logged in successfully
     * @throws InvalidLoginException If the user or password are wrong.
     */
	public Long login(String userName, String password)
		throws RemoteException, InvalidLoginException;
	
	/**
	 * Logout of the system.
	 * @param sessiondId	Session id to finish.
	 */
	public void logout(Long sessiondId)
		throws RemoteException;
	
	/**
	 * Signup a new user.
	 * @param userName	User name
	 * @param password	Password
	 * @param email		Email of the user (may be empty).
	 * @throws RemoteException
	 */
	public void addUser(String userName, String password, String email)
		throws RemoteException;

}

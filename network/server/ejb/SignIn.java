
package game.network.server.ejb;

import game.network.InvalidLoginException;

import java.rmi.RemoteException;

import javax.ejb.EJBObject;

/**
 * @author Yossi Shaul
 */
public interface SignIn extends EJBObject {
	
	public Long login(String userName, String password)
		throws RemoteException, InvalidLoginException;
	
	public void logout(Long sessiondId)
		throws RemoteException;
	
	public void addUser(String userName, String password, String role)
		throws RemoteException;

}

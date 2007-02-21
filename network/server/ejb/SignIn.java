
package game.network.server.ejb;

import game.network.InvalidLoginException;

import java.rmi.RemoteException;

import javax.ejb.EJBObject;

/**
 * @author Yossi Shaul
 */
public interface SignIn extends EJBObject {
	
	public String validateUser(String userName, String password)
		throws RemoteException, InvalidLoginException;
	
	public String addUser(String userName, String password, String role)
		throws RemoteException;

}

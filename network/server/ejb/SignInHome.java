
package game.network.server.ejb;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;

/**
 * @author Yossi Shaul
 */
public interface SignInHome extends EJBHome {

	public SignIn create() throws RemoteException, CreateException;
	
}

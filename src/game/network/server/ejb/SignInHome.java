
package game.network.server.ejb;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;

/**
 * The signin bean is used for users validation, logout and signups.
 */
public interface SignInHome extends EJBHome {

    /**
     * Create a bean instance.
     */
	public SignIn create() throws RemoteException, CreateException;
	
}

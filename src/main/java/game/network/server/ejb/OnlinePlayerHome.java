
package game.network.server.ejb;

import java.rmi.RemoteException;
import java.util.Collection;

import javax.ejb.*;

public interface OnlinePlayerHome extends EJBHome {

    /**
     * Find online player by primary key (session id).
     */
    public OnlinePlayer findByPrimaryKey(Long pk) 
		throws RemoteException, FinderException;

    /**
     * Find all the online players that accepts invitations. 
     */
    public Collection findByAcceptInvitations()
    	throws RemoteException, FinderException;

    /**
     * Create a new online player. Use the next sequence as
     * the session id.
     * @param userName	User name of the online player. 
     */
    public OnlinePlayer create(String userName) 
		throws RemoteException, CreateException;
    
}

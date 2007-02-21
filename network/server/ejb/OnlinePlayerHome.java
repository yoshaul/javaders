
package game.network.server.ejb;

import java.rmi.RemoteException;
import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;
import javax.ejb.FinderException;

public interface OnlinePlayerHome extends EJBHome {

    public OnlinePlayer findByPrimaryKey(Long pk) 
		throws RemoteException, FinderException;

    public Collection findByAcceptInvitations()
    	throws RemoteException, FinderException;

    public OnlinePlayer create(String userName) 
		throws RemoteException, CreateException;
    
}

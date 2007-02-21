package game.network.server.ejb;

import java.rmi.RemoteException;

import javax.ejb.*;

public interface PlayerHome extends EJBHome {

    public Player findByPrimaryKey(String pk) 
    	throws RemoteException, FinderException;

    public Player create(String pk, String password) 
    	throws RemoteException, CreateException;
    
    public Player create(String pk, String password, String email)
    	throws RemoteException, CreateException;
    
}

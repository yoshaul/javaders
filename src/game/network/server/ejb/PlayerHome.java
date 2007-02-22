package game.network.server.ejb;

import java.rmi.RemoteException;

import javax.ejb.*;

public interface PlayerHome extends EJBHome {

    /**
     * Find player by primary key (user name)
     */
    public Player findByPrimaryKey(String pk) 
    	throws RemoteException, FinderException;

    /**
     * Create a new user.
     * @param pk		User name
     * @param password	Password
     */
    public Player create(String pk, String password) 
    	throws RemoteException, CreateException;
    
    /**
     * Create a new user.
     * @param pk		User name
     * @param password	Password
     * @param email		Email
     */
    public Player create(String pk, String password, String email)
    	throws RemoteException, CreateException;
    
}

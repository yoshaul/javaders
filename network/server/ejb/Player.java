package game.network.server.ejb;

import java.rmi.RemoteException;

import javax.ejb.EJBObject;

/**
 * The <code>Player</code> EJB holds the data of a registered player.
 */
public interface Player extends EJBObject {

    /**
     * Returns the user name (the primary key).
     * @return	The user name.
     */
    public String getUserName() throws RemoteException;
    
    /**
     * Returns the user's passsword.
     * @return The user's passsword.
     */
    public String getPassword() throws RemoteException;
    
    /**
     * Sets the user's password.
     * @param password	Password to set.
     */
    public void setPassword(String password) throws RemoteException;
    
    /**
     * Returns the user's email.
     * @return The user's email.
     */
    public String getEmail() throws RemoteException;
    
    /**
     * Sets the user's email.
     * @param email	Email to set.
     */
    public void setEmail(String email) throws RemoteException;
    

}

package game.network.server.ejb;

import java.rmi.RemoteException;

import javax.ejb.EJBObject;

/**
 * The <code>OnlinePlayer</code> represents an online player 
 * that is logged in the system.
 * Each online player has its own primary pk (the session id).
 */
public interface OnlinePlayer extends EJBObject {

    /**
     * Sets the desire of the online player to allow
     * invitations to be sent to him.
     * The players will only see the online players with the
     * accept flag set to true in the online players list.
     * @param accept	True to accept false not to.
     */
    public void setAcceptInvitations(boolean accept)
    	throws RemoteException;
    
    /**
     * Returns a simple model of the online player which contains
     * its session id, name etc.
     * @return	OnlinePlayerModel of the player.
     * @see OnlinePlayerModel
     */
    public OnlinePlayerModel getOnlinePlayerModel()
    	throws RemoteException;
    
}

package game.network.server.ejb;

import java.rmi.RemoteException;

import javax.ejb.EJBObject;

public interface OnlinePlayer extends EJBObject {

    public void setAcceptInvitations(boolean accept)
    	throws RemoteException;
    
    public OnlinePlayerModel getOnlinePlayerModel()
    	throws RemoteException;
    
}

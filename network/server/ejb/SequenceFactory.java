package game.network.server.ejb;

import java.rmi.RemoteException;

import javax.ejb.EJBObject;

public interface SequenceFactory extends EJBObject {

    public Long getNextID() throws RemoteException;
    
}

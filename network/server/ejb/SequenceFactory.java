package game.network.server.ejb;

import java.rmi.RemoteException;

import javax.ejb.EJBObject;

/**
 * The <code>SequenceFactory</code> is used to generate unique
 * sequence id for each table name (or any name).
 */
public interface SequenceFactory extends EJBObject {

    /**
     * Returns the next dequence id.
     * @return	The ext sequence id.
     */
    public Long getNextID() throws RemoteException;
    
}

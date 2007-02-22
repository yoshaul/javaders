package game.network.server.ejb;

import java.rmi.RemoteException;

import javax.ejb.*;

/**
 * The <code>SequenceFactory</code> is used to generate unique
 * sequence id for each table name (or any name).
 */
public interface SequenceFactoryHome extends EJBHome {

    /**
     * Create a new sequence bean.
     * @param pk	Usually a table name but can be any unique string.
     */
    public SequenceFactory create(String pk) 
    	throws RemoteException, CreateException;
    
    /**
     * Find bean by primary key.
     */
    public SequenceFactory findByPrimaryKey(String pk) 
    		throws RemoteException,	FinderException;
    
}

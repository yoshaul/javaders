package game.network.server.ejb;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;
import javax.ejb.FinderException;

public interface SequenceFactoryHome extends EJBHome {

    public String create(String pk) throws RemoteException, CreateException;
    
    public SequenceFactory findByPrimaryKey(String pk) 
    		throws RemoteException,	FinderException;
    
}

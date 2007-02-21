package game.network.server.ejb;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;

public interface HighScoresHome extends EJBHome {

    public HighScores create() throws RemoteException, CreateException;
    
}

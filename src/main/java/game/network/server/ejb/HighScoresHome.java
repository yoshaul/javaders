package game.network.server.ejb;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;

/**
 * The <code>HighScores</code> EJB manages the scores
 * posted by the game players.
 */
public interface HighScoresHome extends EJBHome {

    /**
     * Create a new high score. 
     */
    public HighScores create() throws RemoteException, CreateException;
    
}

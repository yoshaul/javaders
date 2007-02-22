package game.network.server.ejb;

import game.highscore.HighScore;

import java.rmi.RemoteException;

import javax.ejb.EJBObject;

/**
 * The <code>HighScores</code> EJB manages the scores
 * posted by the game players.
 */
public interface HighScores extends EJBObject {

	/**
	 * Adds the posted score to the high scores table
	 * @param score	New score to add
	 */
    public void postHighScore(HighScore score) throws RemoteException;
    
    /**
     * @return Array of HighScore objects containing the top ten
     * high scores.
     */
	public HighScore[] getTopTenScores() throws RemoteException;
    
	/**
	 * @return Array of HighScore objects containing the scores
	 * ranked between fromRank to toRank.
	 * @throws RemoteException if exception occures or fromRank
	 * is less then 1 or fromRank > toRank.
	 */
	public HighScore[] getHighScores(int fromRank, int toRank) 
		throws RemoteException;
	
}

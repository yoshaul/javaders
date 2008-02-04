
package game.network.server.ejb;

import game.highscore.HighScore;
import game.network.server.DBHelper;

import java.rmi.RemoteException;
import java.sql.*;

import javax.ejb.*;

/**
 * The <code>HighScores</code> EJB manages the scores
 * posted by the game players.
 */
public class HighScoresBean implements SessionBean {

    private SessionContext sessionContext;
    
	public HighScoresBean() {
	    // SessionBean class must implement an empty constructor
	}
    
	/* SessionBean implementation */
	
    public void ejbActivate() throws EJBException, RemoteException {
        // Not in use in stateless session beans
    }

    public void ejbPassivate() throws EJBException, RemoteException {
        // Not in use in stateless session beans
    }

    public void ejbRemove() throws EJBException, RemoteException {
        
    }

	public void setSessionContext(SessionContext sessionContext) {
		
		this.sessionContext = sessionContext;
	}
	
	
	/* Home interface implementation */
	
	public void ejbCreate() throws CreateException {
	    
	}

	
	/* Implement business methods */
	
	/**
	 * Adds the posted score to the high scores table.
	 * @see HighScores#postHighScore(HighScore)
	 */
	public void postHighScore(HighScore score) throws EJBException { 
	    
	    Connection connection = null;
	    try {
	        
			Long scoreId = EJBHelper.getNextSeqId("high_score");

			connection = DBHelper.getConnection();
			
			PreparedStatement ps = connection.prepareStatement(
	                "INSERT INTO high_score " +
	                "(score_id, player_name, score, level) " +
	                "values(?, ?, ?, ?)");
	        
	        ps.setLong(1, scoreId);
	        ps.setString(2, score.getPlayerName());
	        ps.setLong(3, score.getScore());
	        ps.setInt(4, score.getLevel());
	        
	        ps.executeUpdate();
	        
	        ps.close();
	        
	    }
        catch (Exception e) {
            throw new EJBException(e);
        }
        finally {
            DBHelper.releaseConnection(connection);
        }
	    
	}
	
	/**
	 * @see HighScores#getTopTenScores() 
	 */
	public HighScore[] getTopTenScores() {
	    return getHighScores(1, 10);
	}
	
	/**
	 * @see HighScores#getHighScores(int, int)
	 */
	public HighScore[] getHighScores(int fromRank, int toRank)
			throws EJBException {
	    
	    Connection connection = null;
	    try {	    
	        if (fromRank < 1) {
	            throw new IllegalArgumentException("Input out of bounds");
	        }
	        
	        if (fromRank > toRank) {
	            throw new IllegalArgumentException("First argument must be " +
	            		"smaller or equal to the second argument");
	        }

	        HighScore[] highScores = new HighScore[toRank-fromRank+1];
	        
	        connection = DBHelper.getConnection();
	        
		    // Create SQL query that selects all the rows in the
		    // high scores table ordered by score and level descending
		    PreparedStatement ps = connection.prepareStatement(
		            "SELECT player_name, score, level " +
		            "FROM high_score " +
		            "ORDER BY score DESC, level DESC");
		    
		    ResultSet rs = ps.executeQuery();
		    
		    int curRank = 1;
		    int count = 0;
		    while (rs.next() && curRank <= toRank) {
		        if (curRank >= fromRank) {
		            String playerName = rs.getString(1);
		            long score = rs.getLong(2);
		            int level = rs.getInt(3);
		            
		            highScores[count] = 
		                new HighScore(playerName, score, level);
		            count++;
		        }
		    }
		    
		    rs.close();
		    ps.close();
		    
		    return highScores;
		    
	    }
        catch (SQLException sqlException) {
            throw new EJBException(sqlException);
        }
        finally {
            DBHelper.releaseConnection(connection);
        }
	}

}

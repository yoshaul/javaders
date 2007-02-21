package game.network.server.ejb;

import game.highscore.HighScore;
import game.network.server.DBHelper;

import java.rmi.RemoteException;
import java.sql.*;

import javax.ejb.*;
import javax.naming.*;
import javax.rmi.PortableRemoteObject;

public class HighScoresBean implements SessionBean {

    private SessionContext sessionContext;
    private Connection connection;
    

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
        DBHelper.releaseConnection(connection);
    }

	public void setSessionContext(SessionContext sessionContext) {
		
		this.sessionContext = sessionContext;
	}
	
	
	/* SignInHome interface implementation */
	
	public void ejbCreate() throws CreateException {
	    connection = DBHelper.getConnection();
	}
	
	
	/* Implement business methods */
	
	public void postHighScore(HighScore score) throws EJBException { 
	    
	    try {
	        
	        // Get id for the high scores from the sequence bean
			InitialContext initialContext = new InitialContext();
			
			Context env = (Context) initialContext.lookup("java:comp/env");
			Object objref = env.lookup("ejb/SequenceFactory");
			SequenceFactoryHome sequenceFactoryHome = (SequenceFactoryHome) 
				PortableRemoteObject.narrow(objref, SequenceFactoryHome.class);

			// find the sequence factory for high score table
			// If not found create it
			SequenceFactory sequenceFactory = null;
			try {
				sequenceFactory = 
				    sequenceFactoryHome.findByPrimaryKey("high_score");
			}
			catch (FinderException fe) {
			    sequenceFactory = 
				    sequenceFactoryHome.create("high_score");
			}
	        
			Long scoreId = sequenceFactory.getNextID();

			PreparedStatement ps = connection.prepareStatement(
	                "INSERT INTO high_score " +
	                "(score_id, player_name, score, level) " +
	                "values(?, ?, ?, ?)");
	        
	        ps.setLong(1, scoreId.longValue());
	        ps.setString(2, score.getPlayerName());
	        ps.setLong(3, score.getScore());
	        ps.setInt(4, score.getLevel());
	        
	        ps.executeUpdate();
	        
	        ps.close();
	        
	    }
        catch (SQLException sqlException) {
            throw new EJBException(sqlException);
        }
        catch (NamingException ne) {
            throw new EJBException(ne);
        }
        catch (RemoteException re) {
            throw new EJBException(re);
        }
        catch (CreateException ce) {
            throw new EJBException(ce);
        }
	    
	}
	
	public HighScore[] getTopTenScores() {
	    return getHighScores(1, 10);
	}
	
	public HighScore[] getHighScores(int fromRank, int toRank)
			throws EJBException {
	    try {	    
	        if (fromRank < 1) {
	            throw new IllegalArgumentException("Input out of bounds");
	        }
	        
	        if (fromRank > toRank) {
	            throw new IllegalArgumentException("First argument must be " +
	            		"smaller or equal to the second argument");
	        }

	        HighScore[] highScores = new HighScore[toRank-fromRank+1];
	        
	        
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
	    
	}
	

}

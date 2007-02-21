
package game.network.server.ejb;

/**
 * This interface holds static variables with the JNDI
 * names of the EJBs and the database.
 */
public interface JNDINames {

	public final static String DBName = 
		"java:comp/env/jdbc/gameDB";
	
	public final static String PLAYER_BEAN = 
	    "java:comp/env/ejb/Player";
	
	public final static String ONLINE_PLAYER_BEAN = 
	    "java:comp/env/ejb/OnlinePlayer";
	
	public final static String HIGH_SCORES_BEAN = 
	    "java:comp/env/ejb/HighScores";
	
	public final static String SEQUENCE_FACTORY_BEAN =
	    "java:comp/env/ejb/SequenceFactory";
    
}

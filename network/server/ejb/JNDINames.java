package game.network.server.ejb;

public interface JNDINames {

	public final static String DBName = 
		"java:comp/env/jdbc/gameDB";
	
	public final static String PLAYER_BEAN = 
	    "java:comp/env/ejb/Player";
	
	public final static String ONLINE_PLAYER_BEAN = 
	    "java:comp/env/ejb/OnlinePlayer";
	
	public final static String HIGH_SCORES_BEAN = 
	    "java:comp/env/ejb/HighScores";
    
}

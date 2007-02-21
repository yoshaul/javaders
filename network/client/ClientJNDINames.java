
package game.network.client;

/**
 * This interface hold the JNDI names of the ejbs
 * for accessing from standalone client. 
 */
public interface ClientJNDINames {
    
    public final static String SIGN_IN_BEAN = "SignInBean";
    
	public final static String PLAYER_BEAN = "PlayerBean";
	
	public final static String ONLINE_PLAYER_BEAN = "OnlinePlayerBean";
	
	public final static String HIGH_SCORES_BEAN = "HighScoresBean";
	
	public final static String TOPIC_CONNECTION_FACTORY =
	    "jms/TopicConnectionFactory";
	
	public final static String INVITATION_TOPIC =
	    "jms/invitationTopic";
	
}

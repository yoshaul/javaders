package game.network.client;

public interface ClientJNDINames {
    
    public final static String SIGN_IN_BEAN = "SignInBean";
    
	public final static String PLAYER_BEAN = 
	    "java:comp/env/ejb/Player";
	
	public final static String ONLINE_PLAYER_BEAN = 
	    "java:comp/env/ejb/OnlinePlayer";
}

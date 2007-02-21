
package game.network.client;

import java.util.*;

import game.GameMenu;
import game.highscore.HighScore;
import game.network.InvalidLoginException;
import game.network.packet.*;
import game.network.server.ejb.*;
import game.util.Logger;

import javax.jms.*;
import javax.naming.*;
import javax.rmi.PortableRemoteObject;

/**
 * The <code>J2EENetworkManager</code> implements the 
 * <code>NetworkManager</code> interface and uses JMS
 * and J2EE API for the communication.
 */
public class J2EENetworkManager implements NetworkManager {
    
    private GameMenu gameMenu;
    
    private Long sessionId;	 // Hold this user session id
    private Long receiverId; // Session id of the network player
    private String userName;
    private boolean inviter;
    private boolean acceptInvitations;

    private Connection jmsConnection;
    private Session jmsSession;
    
    private JMSMessageHandler jmsInvitationManager;
    private JMSGameMessageHandler jmsGameMessageHandler;
    
    /**
     * Cinstruct the J2EE network manager.
     * @param game	Reference to the game menu.
     */
    public J2EENetworkManager(GameMenu game) {
        this.gameMenu = game;
    }

    /**
     * Sets the invitation acceptance of the current logged user.
     * The user accepts invitation when she logs in and reject invitation
     * when she start playing. We update the online player bean with the new
     * status.
     * @param accept Treu if the user is ready to accept invitations to play.
     */
    public void acceptInvitations(boolean accept) throws NetworkException {
        
        if (this.sessionId == null) {
            throw new NetworkException("User is not logged in");
        }
        
        try {

            this.acceptInvitations = accept;
            
            // Update the online player bean
            OnlinePlayerHome onlineHome = (OnlinePlayerHome)
				EJBHelper.getEJBHome(JNDINames.ONLINE_PLAYER_BEAN, 
				        OnlinePlayerHome.class);
            
    		OnlinePlayer onlinePlayer = onlineHome.findByPrimaryKey(sessionId);
    		
    		onlinePlayer.setAcceptInvitations(accept);
    		
        }
        catch (Exception e) {
            Logger.exception(e);
            throw new NetworkException("Error updating invitation status: " + 
                    e.getMessage());
        }
		
    }
    
    /**
     * @see NetworkManager#sendPacket(Packet)
     */
    public void sendPacket(Packet packet) {
        // Not in use
    }

    /**
     * Login to the server. Find the sign in bean and try to log
     * in with the username and password.
     * @see NetworkManager#login(String, String) 
     */
    public Long login(String userName, String password) 
    		throws NetworkException, InvalidLoginException {
        
		try {
			SignInHome signInHome = (SignInHome) EJBHelper.getEJBHome(
			        ClientJNDINames.SIGN_IN_BEAN, SignInHome.class);
			
			SignIn signIn = signInHome.create();
			
			// Try to login with the supplied user and password
			this.sessionId = signIn.login(userName, password);
			
			// If login was successful set the current logged user,
			// init the jms connection and return the user session id
			initJMSConnection();
			this.userName = userName;
			return sessionId;
		
		}
		catch (InvalidLoginException ile) {
		    throw ile;
		}
		catch (Exception e) {
		    Logger.exception(e);
			throw new NetworkException(e.getMessage());
		}
		
    }
    
    /**
     * Logout from the game server and close the jms connection.
     * @see NetworkManager#logout() 
     */
    public void logout() throws NetworkException {
        
		try {
			SignInHome signInHome = (SignInHome) EJBHelper.getEJBHome(
			        ClientJNDINames.SIGN_IN_BEAN, SignInHome.class);
		    
			SignIn signIn = signInHome.create();
			
		    signIn.logout(sessionId);
		    
		    // Close the jms connection
		    jmsConnection.close();
		    
		}
		catch (Exception e) {
		    Logger.exception(e);
		    throw new NetworkException("Error while trying to logout: " + 
		            e.getMessage());
		}
		finally {
		    this.sessionId = null;
		    this.userName = null;
		    this.jmsConnection = null;
		}
        
    }
    
    /**
     * Register with a new user. Find the sign in bean and signup.
     * @see NetworkManager#signup(String, String, String)
     */
    public void signup(String userName, String password, String email) 
    		throws NetworkException {
        
		try {

			SignInHome signInHome = (SignInHome) EJBHelper.getEJBHome(
			        ClientJNDINames.SIGN_IN_BEAN, SignInHome.class);
			
			SignIn signIn = signInHome.create();
			
			signIn.addUser(userName, password, email);

		}
		catch (Exception e) {
			e.printStackTrace();
			throw new NetworkException(e.getMessage());
		}
    }
    
    /**
     * @see NetworkManager#getAvailablePlayers()
     */
    public List getAvailablePlayers() throws NetworkException{
        List playersModels = new ArrayList();
        try {

            OnlinePlayerHome onlinePlayerHome = (OnlinePlayerHome) 
        		EJBHelper.getEJBHome(JNDINames.ONLINE_PLAYER_BEAN, 
        		        OnlinePlayerHome.class);
            
            // Get collection of available online players
            Collection result = onlinePlayerHome.findByAcceptInvitations(); 

            // Get the online players models
            Iterator itr = result.iterator();
            while (itr.hasNext()) {
                OnlinePlayer onlinePlayer = (OnlinePlayer)
                	PortableRemoteObject.narrow(itr.next(), 
                	        OnlinePlayer.class);
                
                // Add the player if it's not the current player
                if (!onlinePlayer.getPrimaryKey().equals(this.sessionId)) {
                    playersModels.add(onlinePlayer.getOnlinePlayerModel());    
                }
                
            }
            return playersModels;
        }
        catch (Exception e) {
            Logger.exception(e);
            throw new NetworkException("Network error while trying to " +
            		"get available players list. Please try again later.");
        }
    }
    
    /**
     * Handle incoming invitation packets.
     * @see NetworkManager#handlePacket(Packet)
     */
    public void handlePacket(Packet packet) {
        
        if (packet instanceof JMSInvitationPacket) {
            
            handleInvitation((JMSInvitationPacket) packet);
            
        }
        
    }
    
    /**
     * Handle invitation packet.
     * @param invitation	Invitation packet with the details.
     */
    private void handleInvitation(JMSInvitationPacket invitation) {

        if (invitation.cancelled) {
            gameMenu.invitationCancelled();
        }
        
        else if (invitation.isReply) {
            // It is a reply to previously sent invitation sent by this user
            
            if (receiverId == null) {
                // In case the invitation was cancelled or arrived from
                // the wrong user ignore it
                return;
            }
            try {
	            if (invitation.accepted) {
	
	                // Set the destination of the jms handler to be the private
	                // destination of the invitee
	                jmsGameMessageHandler.setDestination(
	                        invitation.replyToDestination);
	                
	                // Set the receiver id
	                this.receiverId = invitation.senderId;
	                // Mark the user as the inviter
	                this.inviter = true;
	                
	            }

	            gameMenu.invitationAccepted(invitation.accepted, invitation.userName);
            }
            catch (JMSException jmse) {
                Logger.exception(jmse);
                Logger.showErrorDialog(gameMenu, "Unable to proccess " +
                		"invitation reply: " + jmse.getMessage());
            }
            
        } else {	// Received invitation from another player
	        
            if (acceptInvitations) {
                // Wait for the response from the user
                gameMenu.invitationArrived(invitation);
            } 
        }
        
    }
    
    /**
     * @see NetworkManager#sendInvitationReply(InvitationPacket, boolean)
     */
    public void sendInvitationReply(InvitationPacket originalInvitation, 
            boolean accepted) throws NetworkException {
        
        try {
	        JMSInvitationPacket  invitation = 
	            (JMSInvitationPacket)originalInvitation;
	        
	        // Create a new JMSInvitationPacket with the reply to address
	        // of the jms gameMenu listener queue
	        JMSInvitationPacket invitationReply = new JMSInvitationPacket(
	                this.sessionId, invitation.senderId, this.userName, 
	                jmsGameMessageHandler.getPrivateQueue());
	        
	        invitationReply.isReply = true;
	        
	        if (accepted) {
	            // user accepted the invitation
	            invitationReply.accepted = true;
	            
	            // Set the JMSGameListener's destination for the online gameMenu
	            // to the private queue of the network player
	            jmsGameMessageHandler.setDestination(invitation.replyToDestination);
	            
	            this.receiverId = invitation.senderId;
	            // Mark this user as invitee
	            this.inviter = false;
	            
	        } else {
	            invitationReply.accepted = false;
	        }
	       
	    	jmsInvitationManager.sendInvitationReply(invitationReply);
	    	gameMenu.setStartMultiplayer(accepted);

        }
        catch (JMSException jmse) {
            Logger.exception(jmse);
            throw new NetworkException(jmse.getMessage());
        }
    }
    
    /**
     * Send invitation to play network gameMenu to the user with
     * session id equals to <code>destinationId</code>
     * @param	destinationId Session id of the target user
     * @see NetworkManager#sendInvitation(Long)
     */
    public void sendInvitation(Long destinationId) 
    		throws NetworkException {

        try {
            this.receiverId = destinationId;
            
            JMSInvitationPacket packet = new JMSInvitationPacket(
                    sessionId, destinationId, userName, 
                    jmsGameMessageHandler.getPrivateQueue());
            
            jmsInvitationManager.sendInvitation(packet);    
        }
        catch (JMSException jmsException) {
            Logger.exception(jmsException);
            throw new NetworkException("Network error while trying to " +
            		"send invitation to play");
        }
        
    }
    
    /**
     * Cancel the last invitation sent by this user. Inform the
     * invitee of the cancellation and set the destination id to null.
     * @see NetworkManager#cancelInvitation()
     */
    public void cancelInvitation() throws NetworkException{
        
        JMSInvitationPacket cancelInvitation = new JMSInvitationPacket(
                this.sessionId, receiverId, this.userName, null);
        cancelInvitation.cancelled = true;
        
        this.receiverId = null;
	        
        jmsInvitationManager.sendPacket(cancelInvitation);
        
    }
    
    /**
     * Initialize the JMS connection. Find the connection factory 
     * and create a connection to the invitation topic.
     */
    private void initJMSConnection() 
    	throws JMSException, NamingException {

        Context context = new InitialContext();

        // Find the connection factory
        ConnectionFactory connectionFactory = 
            (ConnectionFactory) context.lookup(
                    ClientJNDINames.TOPIC_CONNECTION_FACTORY);

        // Creat the connection
        jmsConnection = connectionFactory.createConnection();
        
        // Creat the session
        jmsSession = jmsConnection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        
        // Create the JMS handlers for the game
        jmsInvitationManager = new JMSMessageHandler(this, sessionId, jmsSession);
        jmsGameMessageHandler = new JMSGameMessageHandler(jmsSession);
        
        // Start receiving messages
        jmsConnection.start();
        
    }
    
    /**
     * Returns a new <code>J2EEGameNetworkManager</code> to manage
     * the network in the running game.
     */
    public GameNetworkManager getGameNetworkManager() {
        return new J2EEGameNetworkManager(
                jmsGameMessageHandler, getSenderId(),
                getReceiverId(), isInviter()); 
    }
    
    /**
     * @see NetworkManager#getSenderId()
     */
    public Long getSenderId() {
        return this.sessionId;
    }
    
    /**
     * @see NetworkManager#getReceiverId()
     */
    public Long getReceiverId() {
        return this.receiverId;
    }
    
    /**
     * @see NetworkManager#isInviter()
     */
    public boolean isInviter() {
        return this.inviter;
    }
    
    /**
     * Post the player score to the server using the high scores bean.
     * @see NetworkManager#postHighScore(HighScore)
     */
    public void postHighScore(HighScore score) throws NetworkException {
        try {
            HighScoresHome highScoresHome= (HighScoresHome) 
				EJBHelper.getEJBHome(
			        JNDINames.HIGH_SCORES_BEAN, HighScoresHome.class);  
            
            HighScores highScores = highScoresHome.create();
            
            highScores.postHighScore(score);
            
        }
        catch (Exception e) {
            Logger.exception(e);
            throw new NetworkException(e.getMessage());
        }
        
    }
    
    /**
     * @see NetworkManager#getTopTenScores()
     */
    public HighScore[] getTopTenScores() throws NetworkException {
        
        return getHighScores(1, 10);

    }
    
    /**
     * @see NetworkManager#getHighScores(int, int)
     */
    public HighScore[] getHighScores(int fromRank, int toRank) 
    		throws NetworkException {
        try {
            HighScoresHome highScoresHome= (HighScoresHome) 
    			EJBHelper.getEJBHome(
    		        ClientJNDINames.HIGH_SCORES_BEAN, HighScoresHome.class);
            
            HighScores highScores = highScoresHome.create();
            
            return highScores.getHighScores(fromRank, toRank);
            
        }
        catch (Exception e) {
            Logger.exception(e);
            throw new NetworkException(e.getMessage());
        }
                
    }
    
}	// end class J2EENetworkManager

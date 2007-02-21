package game.network;

import java.rmi.RemoteException;
import java.util.*;

import game.Game;
import game.network.packet.*;
import game.network.server.ejb.*;

import javax.jms.*;
import javax.naming.*;
import javax.rmi.PortableRemoteObject;
import javax.swing.JOptionPane;

// J2EE packages
import javax.ejb.CreateException;

public class J2EENetworkManager implements NetworkManager {
    
    private Game game;
    
    private SignIn signIn;
    private Long sessionId;	// Hold this user session id
    private Long receiverID;
    private String userName;
    private boolean inviter;

    private Connection jmsConnection;
    private Session jmsSession;
    
    private JMSMessageListener jmsInvitationManager;
    private JMSGameListener jmsGameListener;
    
    public J2EENetworkManager(Game game) {
        this.game = game;
    }

    
    public void acceptInvitations(boolean accept, Long sessionId) {
        System.out.println("Accept invitation = " + accept);
        try {
            // First connect or disconnect from the JMS topic
            if (accept) {
                initJMSConnection();
                
                // Start receiving messages
                jmsConnection.start();
            }
            else if (jmsConnection != null) {
                jmsConnection.close();
            }
            
            // Update the online player bean
            InitialContext initialContext = new InitialContext();
    		Object objref = initialContext.lookup(JNDINames.ONLINE_PLAYER_BEAN);
    		
    		OnlinePlayerHome onlineHome = (OnlinePlayerHome)
    			PortableRemoteObject.narrow(objref, OnlinePlayerHome.class);
    		
    		OnlinePlayer onlinePlayer = onlineHome.findByPrimaryKey(sessionId);
    		
    		onlinePlayer.setAcceptInvitations(accept);
            
        }
        catch (Exception e) {
            jmsConnection = null;
            e.printStackTrace();
        }
		
    }
    
    
    public void sendMessage(String message, Long destinationID) {
//        jmsInvitationManager.sendMessage(message, destinationID);
    }
    
    
    public void sendPacket(Packet packet) {
        
    }
    
    public Packet receivePacket() {
        return null;
    }
    
    
    public Long login(String userName, String password) {
        
		try {
			// create an initial naming context
			InitialContext initialContext = new InitialContext();

//System.err.println(initialContext.list("java:comp/env"));
//System.err.println("\n\n\n\n");
//			Object objref = initialContext.lookup("java:comp/env/ejb/SignIn");
//			Object objref = initialContext.lookup("java:comp/env/ejb/SignInBean");
//System.out.println("GOT OBJECT:" + objref.toString());
//System.out.println("CLASS:" + objref.getClass() + "\n\n");
			
			
			// obtain the environment maming context of the application client
			Context env = (Context) initialContext.lookup("java:comp/env");
			
			// retrieve the object bound to the name ejb/SignInHome
			Object objref = env.lookup("ejb/SignIn");
			
			// narrow the context to a SignInHome object
			SignInHome signInHome = (SignInHome) 
				PortableRemoteObject.narrow(objref, SignInHome.class);

			// create the enterprise bean instance
			signIn = signInHome.create();
			
//			statusLabel.setText("SignIn bean created successfully");
		}
		catch (NamingException namingException) {
		    System.err.println(namingException.getCause());
			namingException.printStackTrace();
		}
		catch (CreateException createException) {
			createException.printStackTrace();
		}
		catch (RemoteException remoteException) {
			remoteException.printStackTrace();
		}
        
        if (signIn != null) {
			try {
				this.sessionId = signIn.login(userName, password);
				this.userName = userName;
		
			}
			catch (InvalidLoginException e) {
				System.out.println(e.getMessage());
			}
			catch (RemoteException e) {
			    System.out.println("ERROR: Remote exception occured");
			    e.printStackTrace();
			}
        }
		return sessionId;
		
    }
    
    public void logout(Long sessionId) {
        
		try {
		    this.sessionId = null;
		    this.userName = null;
	        signIn.logout(sessionId);
		}
		catch (RemoteException re) {
		    System.out.println("ERROR: Remote exception occured");
		    re.printStackTrace();
		}
        
    }
    
    public void signup(String userName, String password, String email) {
        String ticket = null;
        
		try {
			// create an initial naming context
			InitialContext initialContext = new InitialContext();
			
			// obtain the environment maming context of the application client
			Context env = (Context) initialContext.lookup("java:comp/env");
			
			// retrieve the object bound to the name ejb/SignInHome
			Object objref = env.lookup("ejb/SignIn");
			
			// narrow the context to a SignInHome object
			SignInHome signInHome = (SignInHome) 
				PortableRemoteObject.narrow(objref, SignInHome.class);

			// create the enterprise bean instance
			signIn = signInHome.create();
			
//			statusLabel.setText("SignIn bean created successfully");
		}
		catch (NamingException namingException) {
			namingException.printStackTrace();
		}
		catch (CreateException createException) {
			createException.printStackTrace();
		}
		catch (RemoteException remoteException) {
			remoteException.printStackTrace();
		}
        
        if (signIn != null) {
			try {
				signIn.addUser(userName, password, email);
		
			}
			catch (RemoteException e) {
			    System.out.println("ERROR: Remote exception occured:");
			    e.printStackTrace();
			}
        }
    }
    
    public List getAvailablePlayers() {
        List playersModels = new ArrayList();
        try {
            InitialContext initialContext = new InitialContext();
            
            Object objref = initialContext.lookup(JNDINames.ONLINE_PLAYER_BEAN);
            
            OnlinePlayerHome onlinePlayerHome = (OnlinePlayerHome) 
            	PortableRemoteObject.narrow(objref, OnlinePlayerHome.class);
            
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
            e.printStackTrace();
            return null; /**TODO: */
        }
    }
    
    public void sendInvitation(Long destinationID) {

        InvitationPacket packet = new InvitationPacket(
                sessionId, destinationID, userName);
        
        jmsInvitationManager.sendInvitation(packet, jmsGameListener.getListeningQueue());
        
    }

    public void handleInvitation(InvitationPacket invitation, 
            Destination destination) {

        InvitationPacket reply = new InvitationPacket(this.sessionId,
                invitation.senderID, this.userName);
        
        if (invitation.isReply) {
            // Reply to previously sent invitation
            System.out.println("received reply for invitation:\n" + invitation);
            
            if (invitation.accepted) {
                /** TODO: open new jms connection */
                JOptionPane.showMessageDialog(null, "User " + 
                        invitation.userName + " accepted your invitation");

                jmsGameListener.setDestination(destination);
                
                // Set the receiver id
                this.receiverID = invitation.senderID;
                // Mark the user as the inviter
                this.inviter = true;
                
                game.setStartMultiplayer(true);
            }
            else {
                JOptionPane.showMessageDialog(null, "User " + 
                        invitation.userName + " rejected your invitation");
            }
            
        } else {
            // Received invitation from another player
	        
	        int result = JOptionPane.showConfirmDialog(null, 
	                "Invitation arrived from " + invitation.userName + 
	                ". Do you want to accept the invitation?",
	                "Invitation", JOptionPane.YES_NO_OPTION);

	        reply.isReply = true;
	        Destination myDestination = null;
	        if (result == 0) {
	            // user accepted the invitation
	            reply.accepted = true;
	            // Create JMSGameListener for the online game
	            jmsGameListener.setDestination(destination);
	            
	            // Send the reply from the JMSGameListener
	            myDestination = jmsGameListener.getListeningQueue();
	            
	            this.receiverID = invitation.senderID;
	            // Mark the user as invitee
	            this.inviter = false;
	            
	            // Start the multiplayer game
	            game.setStartMultiplayer(true);
	            
	        } else {
	            reply.accepted = false;
	        }
	        System.out.println("Sending reply " + reply.toString());
	        jmsInvitationManager.sendInvitationReply(reply, myDestination);
        }
        
    }
    
    private void initJMSConnection() 
    	throws JMSException, NamingException {
        
        Context context = new InitialContext();
        System.err.println("Getting connection factory...");
        ConnectionFactory connectionFactory = 
            (ConnectionFactory) context.lookup("jms/TopicConnectionFactory");
        
        System.err.println("Creating connection...");
        jmsConnection = connectionFactory.createConnection();
        
        System.err.println("Creating session...");
        jmsSession = jmsConnection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        
        jmsInvitationManager = new JMSMessageListener(this, sessionId, jmsSession);
        jmsGameListener = new JMSGameListener(jmsSession);
        
    }
    
    public JMSGameListener getJMSGameListener() {
        return this.jmsGameListener;
    }
    
    public Long getSenderID() {
        return this.sessionId;
    }
    
    public Long getReceiverID() {
        return this.receiverID;
    }
    
    public boolean isInviter() {
        return this.inviter;
    }
    
    
}

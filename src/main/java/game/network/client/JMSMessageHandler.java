
package game.network.client;

import game.network.packet.*;
import game.util.Logger;

import javax.jms.*;
import javax.naming.*;

/**
 * The <code>JMSMessageHandler</code> handles JMS communication
 * for the network manager.
 */
public class JMSMessageHandler implements MessageListener {

    // JMS variables
    private Session session;
    private MessageProducer messageProducer;
    private MessageConsumer messageConsumer;
    
    private NetworkManager networkManager;
    
    /**
     * Construct the jms message handler.
     * @param networkManager	The network manager.
     * @param sessionId		The player session id to be used as the selector
     * @param session		JMS session to the game topic
     */
    public JMSMessageHandler(NetworkManager networkManager, 
            Long sessionId, Session session) throws NamingException, JMSException {
        
        this.networkManager = networkManager;
        this.session = session;
        initJMSConnection(sessionId);
        
    }
    
    /**
     * Find the destination topic and create message consumer and
     * message producer.
     * @param sessionId	Session id of the current logged user to be used
     * as message selector.
     * @throws JMSException		If the creation of the consumer/producer fails
     * @throws NamingException	If the destination topic is not found
     */
    private void initJMSConnection(Long sessionId) 
    	throws JMSException, NamingException {
        
        Context context = new InitialContext();

        Destination topic = (Topic) context.lookup(
                ClientJNDINames.INVITATION_TOPIC);
        
        messageProducer = session.createProducer(topic);

        // The selector accepts only messages destined to the current user
        String selector = "ReceiverID = '" + sessionId + "'";
        messageConsumer = session.createConsumer(topic, selector);
        messageConsumer.setMessageListener(this);
        
    }
    
    /**
     * New message received from the JMS topic designed for this
     * user. Send to the network manager to handle.
     */
    public void onMessage(Message message) {

        if (message instanceof ObjectMessage) {
	        try {
	            
	            Packet packet = (Packet)((ObjectMessage)message).getObject();
//System.out.println("JMSMessageListener received: " + packet);
	            if (packet instanceof JMSInvitationPacket) {
	                // Set the reply to destination before delivering to
	                // the network manager
	                JMSInvitationPacket invitation = (JMSInvitationPacket)packet;
	                invitation.replyToDestination = message.getJMSReplyTo();

	            }
	            
	            networkManager.handlePacket(packet);
	            
	        }
	        catch (JMSException jmsException) {
	            Logger.exception(jmsException);
	        }
        }
    }
    
    /**
     * Send the input packet to the destination topic. Adds the destination
     * user id as "ReceiverID" string property for the message selector. 
     * @param packet	Packet to send.
     */
    public void sendPacket(Packet packet) throws NetworkException {
        try {
            
            Message message = session.createObjectMessage(packet);
            
            // Add the receiver id for the jms message selector
            message.setStringProperty("ReceiverID", packet.receiverId.toString());
            
            messageProducer.send(message);

        }
        catch (JMSException jmsException) {
            Logger.exception(jmsException);
            throw new NetworkException("Error while trying to send packet to " + 
                    packet.receiverId);
        }
    }
    
    /**
     * Send invitation to play.
     * @param packet	Invitation packet.
     */
    public void sendInvitation(JMSInvitationPacket packet) throws JMSException {
        
        Message message = session.createObjectMessage(packet);
            
        // Add the receiver id for the jms message selector
        message.setStringProperty("ReceiverID", packet.receiverId.toString());

        // Set the reply to destination
        message.setJMSReplyTo(packet.replyToDestination);
        
        messageProducer.send(message);
    }
    
    /**
     * Send reply to invitation.
     * @param packet	Packet with the details.
     */
    public void sendInvitationReply(JMSInvitationPacket packet) 
    	throws JMSException {
            
        Message message = session.createObjectMessage(packet);
        
        // Add the receiver id for the jms message selector
        message.setStringProperty("ReceiverID", packet.receiverId.toString());

        if (packet.accepted) {
            message.setJMSReplyTo(packet.replyToDestination);
        }
        
        messageProducer.send(message);

    }
    
}
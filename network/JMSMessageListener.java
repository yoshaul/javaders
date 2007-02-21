package game.network;

import game.network.packet.*;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class JMSMessageListener implements MessageListener {

    // JMS variables
    private Session session;
    private MessageProducer messageProducer;
    private MessageConsumer messageConsumer;
    
    private NetworkManager networkManager;
    
    public JMSMessageListener(NetworkManager networkManager, 
            Long sessionId, Session session) throws NamingException, JMSException {
        
        this.networkManager = networkManager;
        this.session = session;
        initJMSConnection(sessionId);
        
    }
    
    public void onMessage(Message message) {

        if (message instanceof ObjectMessage) {
	        try {
	            
	            Packet packet = (Packet)((ObjectMessage)message).getObject();
System.out.println("JMSMessageListener received: " + packet);	            
	            if (packet instanceof InvitationPacket) {
	                InvitationPacket invitation = (InvitationPacket)packet;

	                ((J2EENetworkManager)networkManager).handleInvitation(
	                        (InvitationPacket)packet, message.getJMSReplyTo());
	                
	            } else { // assume game message
	                
//	                message
	                
	            }
	            
	            
	        }
	        catch (JMSException jmsException) {
	            jmsException.printStackTrace();
	        }
        }
    }
    
    private void initJMSConnection(Long sessionId) 
    	throws JMSException, NamingException {
        
        Context context = new InitialContext();
        System.err.println("Getting destination topic...");
        Destination topic = (Topic) context.lookup("jms/myTopic");
        
        System.err.println("Creating producer...");
        messageProducer = session.createProducer(topic);
        
        System.err.println("Creating consumer...");
        String selector = "ReceiverID = '" + sessionId + "'";
        messageConsumer = session.createConsumer(topic, selector);
        messageConsumer.setMessageListener(this);
        
    }
    
    
    public void sendInvitation(InvitationPacket packet, Destination destination) {
        
        try {
            
            Message message = session.createObjectMessage(packet);
            
            // Add the receiver id for the jms message selector
            message.setStringProperty("ReceiverID", packet.receiverID.toString());

            // Set the reply to destination
            message.setJMSReplyTo(destination);
            
            messageProducer.send(message);

        }
        catch (JMSException jmsException) {
            jmsException.printStackTrace();
        }
    }
    
    public void sendInvitationReply(InvitationPacket packet, 
            Destination destination) {
        
        try {
            
            Message message = session.createObjectMessage(packet);
            
            // Add the receiver id for the jms message selector
            message.setStringProperty("ReceiverID", packet.receiverID.toString());

            if (destination != null) {
                message.setJMSReplyTo(destination);
            }
            
            messageProducer.send(message);
            
        }
        catch (JMSException jmsException) {
            jmsException.printStackTrace();
        }
    }
    
    public void handleInvitation(InvitationPacket invitation) {
        try {
            
            System.out.println("invitation arrived from " + 
                    invitation.userName + " id = " + invitation.senderID + 
                    " to receiver id = " + invitation.receiverID);

            /** TODO: pop joptionpane */

            // Create temporary queue for the online game messages
            System.out.println("Creating temporary queue....");
            TemporaryQueue sessionQueue = session.createTemporaryQueue();
            System.out.println("Queue created name = " + 
                    sessionQueue.getQueueName());

            // Create consumer
            System.out.println("Creating consumer....");
            MessageConsumer gameConsumer = session.createConsumer(sessionQueue);
//            gameConsumer.setMessageListener(new JMSGameListener());

//            InvitationPacket replyInvitation = new InvitationPacket(senderID, )
            
            Message replyMessage = session.createMessage();
            
            // Set the reply to destination
            replyMessage.setJMSReplyTo(sessionQueue);
            
            
            // Send invitation acception from the temp queue
            /** TODO: */
//            messageProducer
            
//            Working with Temporary Destinations
//            As you learned, JMS destinations are normally created as administered 
//            objects. JMS also enables you to create a temporary destination 
//            (TemporaryQueue and TemporaryTopic), which becomes active only during 
//            the session's connection. The JMS provider guarantees that the temporary
//            destination is unique across all connections. You create these 
//            destinations dynamically using the createTemporaryQueue() and 
//            createTemporaryTopic() methods of the corresponding session object. 
//            The following is an example of creating a TemporaryQueue:
//
//            TemporaryQueue tempQ = qSession.createTemporaryQueue("myTempQueue");
//
//            Temporary destinations work in the same fashion as administered 
//            destinations.
//
//            Caution
//
//            If you close the connection that the temporary destination belongs to, 
//            the destination is closed and its contents are lost.
//
//
//
//            When a producer and consumer agree to use a temporary destination, 
//            the producer first creates it, and then passes its reference to the 
//            consumer. This is accomplished by setting the JMSReplyTo message header 
//            field. At the other end, the consumer needs to extract the reference 
//            from the message header before using this destination.


            
            
            
        }
        catch (JMSException jmsException) {
            jmsException.printStackTrace();
        }
    }
    
    public void sendMessage(Packet packet) {
        try {
            ObjectMessage m = session.createObjectMessage();
            
            m.setObject(packet);
            
            // Set receiver id for the selector
            m.setStringProperty("ReceiverID", packet.receiverID.toString());
            
            messageProducer.send(m);
            
        }
        catch (JMSException jmsException) {
            jmsException.printStackTrace();
        }
        
    }
    
    public Session getSession() {
        return this.session;
    }
    
}

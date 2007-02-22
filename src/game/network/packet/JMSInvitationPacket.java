
package game.network.packet;

import javax.jms.Destination;

/**
 * This class extends the <code>InvitationPacket</code>
 * class to add the JMS reply destination. 
 */
public class JMSInvitationPacket extends InvitationPacket {

    // We use the replyToDestination to save the jms reply
    // to destination and pass it from the network manager
    // to the jms connection manager but not over the network 
    transient public Destination replyToDestination;
    
    public JMSInvitationPacket(Long senderID, 
            Long receiverID, String userName, Destination destination) {
        
        super(senderID, receiverID, userName);
        this.replyToDestination = destination;
        
    }
    
}

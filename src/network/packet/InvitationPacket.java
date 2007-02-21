package game.network.packet;

/**
 * <code>InvitationPacket</code> is used to invite player for an
 * online game and to send replies for invitations to play. 
 */
public class InvitationPacket extends Packet {

    public String userName;	 // User name of the sender
    public boolean isReply;  // Is it a reply to invitation
    public boolean accepted; // Is the invitation accepted
    public boolean cancelled; // Is the invitation cancelled
    
    /**
     * Construct a new <code>InvitationPackat</code>
     * @param senderId		Session id of the inviter
     * @param receiverId	Session id of the invitee
     * @param userName		Username of the inviter
     */
    public InvitationPacket(Long senderId, Long receiverId, String userName) {
        super(senderId, receiverId);
        this.userName = userName;
        this.cancelled = false;
    }
    
    public String toString() {
        return super.toString() +
        	" User name: " + userName +
        	" isReply: " + isReply +
        	" accepted: " + accepted + 
        	" cancelled: " + cancelled;
    }
    
}

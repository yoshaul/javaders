package game.network.packet;


public class InvitationPacket extends Packet {

    public String userName;
    /** Is it a reply to invitation */
    public boolean isReply;
    public boolean accepted;
    
    
    public InvitationPacket(Long senderID, Long receiverID, String userName) {
        super(senderID, receiverID);
        this.userName = userName;
    }
    
    public String toString() {
        return super.toString() +
        	" User name: " + userName +
        	" isReply: " + isReply +
        	" accepted: " + accepted;
    }
    
}

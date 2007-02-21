package game.network.packet;

import java.io.Serializable;

public class Packet implements Serializable {

    public static final int INVITATION = 1;
    
//    public String senderName;
    public Long senderID;
    public Long receiverID;
    public int handlerID = -1;
    private boolean consumed = false;
    
    public Packet(Long senderID, Long receiverID, int objectID) {
        this.senderID = senderID;
        this.receiverID = receiverID;
        this.handlerID = objectID;
    }

    public Packet(Long senderID, Long receiverID) {
        this.senderID = senderID;
        this.receiverID = receiverID;
    }
    
    public String toString() {
        
        return "Class: " + getClass() + "Sender ID: " + senderID +
        	" ReceiverID: " + receiverID;
    }
    
    public boolean isConsumed() {
        return consumed;
    }
    
    public void setConsumed(boolean consumed) {
        this.consumed = consumed;
    }
}

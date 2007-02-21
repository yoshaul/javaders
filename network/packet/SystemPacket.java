package game.network.packet;

public class SystemPacket extends Packet {

    public static final int TYPE_READY_TO_PLAY = 1;
    
    private int type;
    
    public SystemPacket(Long senderID, Long receiverID, int type) {
        super(senderID, receiverID);
        this.type = type;
    }
    
    public int getType() {
        return this.type;
    }
    
    public String toString() {
        return super.toString() +
        	" type: " + type; 
        
    }
    
}

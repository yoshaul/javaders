package game.network.packet;

import game.ship.ShipState;

public class ShipPacket extends Packet {

    private ShipState shipState;
    
    public ShipPacket(Long senderID, Long receiverID, 
            int handlerID, ShipState shipState) {
        
        super(senderID, receiverID, handlerID);
        this.shipState = shipState;
        
    }
    
    public ShipState getShipState() {
        return this.shipState;
    }
    
    
}

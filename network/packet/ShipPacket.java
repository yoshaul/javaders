
package game.network.packet;

import game.ship.ShipState;

/**
 * The <code>ShipPacket</code> is sent by ship (enemy or friendly)
 * and encapsulates the ship current state in a <code>ShipState</code> object.
 * 
 * @see game.ship.ShipState
 */
public class ShipPacket extends Packet {

    // Current state of the ship sending this packet
    private ShipState shipState;
    
    /**
     * Constructs a new <code>ShipPacket</code>.
     * @param senderId		Session id of the sender
     * @param receiverId	Session id of the target user
     * @param handlerId		Id of the ship generating this packet
     * @param shipState		The ship's current state
     */
    public ShipPacket(Long senderId, Long receiverId, 
            int handlerId, ShipState shipState) {
        
        super(senderId, receiverId, handlerId);
        this.shipState = shipState;
        
    }
    
    /**
     * Return the ship state object.
     * @return	ShipState object.
     */
    public ShipState getShipState() {
        return this.shipState;
    }
    
}

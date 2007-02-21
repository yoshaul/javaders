package game.network.packet;

import game.ship.weapon.BulletModel;

/**
 * The bullet packet is sent when one of the ship fires
 * a new bullet. The packet contains the bullet details
 * in a <code>BulletModel</code> object.
 * 
 * @see game.ship.weapon.BulletModel
 */
public class BulletPacket extends Packet {

    // Holds the bullt details including the bullet owner
    private BulletModel bulletModel ;
    
    /**
     * Constructs a new <code>BulletPacket</code>.
     * @param senderId		Session id of the sender
     * @param receiverId	Session id of the target user
     * @param firingShipId	Id of the firing ship
     * @param bulletModel	Bullet details
     */
    public BulletPacket(Long senderId, Long receiverId, 
            int firingShipId, BulletModel bulletModel) {

        super(senderId, receiverId, firingShipId);
        this.bulletModel = bulletModel;
    }
    
    public BulletModel getBulletModel() {
        return this.bulletModel;
    }
    
}


package game.network.packet;

/**
 * The <code>PowerUpPacket</code> is sent whenever an enemy
 * ship drops a power up bonus.
 */
public class PowerUpPacket extends Packet {

    public float x, y;	// Location of the powerup sprite
    public int powerUp;	// How much armor the bonus adds to the ship
    
    /**
     * Constructs a new <code>BulletPacket</code>.
     * @param senderId		Session id of the sender
     * @param receiverId	Session id of the target user
     * @param handlerId		Id of the network handler
     */
    public PowerUpPacket(Long senderId, Long receiverId, 
            int handlerId, float x, float y, int powerUp) {

        super(senderId, receiverId, handlerId);
        this.x = x;
        this.y = y;
        this.powerUp = powerUp;
    }
}

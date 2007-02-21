
package game.network.packet;

/**
 * The <code>WeaponUpgradePacket</code> is sent whenever an enemy
 * ship drops a weapon upgrade bonus.
 */
public class WeaponUpgradePacket extends Packet {
    
    public float x, y;	// Location of the sprite
    public int weaponType;
    
    /**
     * Constructs a new <code>BulletPacket</code>.
     * @param senderId		Session id of the sender
     * @param receiverId	Session id of the target user
     * @param handlerId		Id of the network handler
     * @param weaponType	Type of the weapon
     */
    public WeaponUpgradePacket(Long senderId, Long receiverId, 
            int handlerId, float x, float y, int weaponType) {

        super(senderId, receiverId, handlerId);
        this.x = x;
        this.y = y;
        this.weaponType = weaponType;
    }
}
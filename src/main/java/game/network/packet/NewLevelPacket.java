
package game.network.packet;

import java.util.Collection;

/**
 * The <code>NewLevelPacket</code> is created and sent by the 
 * controller player (the player whose computer is controlling
 * the random and special events in a network game) before a new
 * level is started. This packet contains details needed for the new level.
 */
public class NewLevelPacket extends Packet {

    private Collection enemyShipsModels;

    /**
     * Constructs a new <code>NewLevelPacket</code>. The handler id
     * is not set since the levels manager is waiting for this packet
     * on the other side.
     * @param senderId		Session id of the sender
     * @param receiverId	Session id of the target user
     * @param enemyShipsModels	Collection of <code>ShipModel</code> 
     * objects of the enemy ships for the new level. 
     */
    public NewLevelPacket(Long senderId, Long receiverId, 
            Collection enemyShipsModels) {

        super(senderId, receiverId);
        this.enemyShipsModels = enemyShipsModels;
    }
    
    /**
     * Returns the enemy ships models.
     * @return	Collection of enemy ship models.
     */
    public Collection getEnemyShipsModels() {
        return this.enemyShipsModels;
    }
}

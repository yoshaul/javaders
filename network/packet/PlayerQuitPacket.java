
package game.network.packet;

/**
 * The <code>PlayerQuitPacket</code> is sent when the
 * user quits at the middle of an online game.
 */
public class PlayerQuitPacket extends Packet {

    /**
     * Constructs a new <code>PlayerQuitPacket</code>.
     * @param senderId		Session id of the sender
     * @param receiverId	Session id of the target user
     */
    public PlayerQuitPacket(Long senderId, Long receiverId) {
        super(senderId, receiverId);
    }

}

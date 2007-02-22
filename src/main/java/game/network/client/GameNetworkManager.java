
package game.network.client;

import game.gamestate.GameState;
import game.network.packet.Packet;

/**
 * The <code>GameNetworkManager</code> interface defines the methods
 * that a network manager for the <b>running game</b> should implement.
 * The various game components access the network methods through this
 * interface only to make it easy to create different network managers
 * in the future.
 */
public interface GameNetworkManager {

    /**
     * Gather network input relevant to the game state.
     * The network manager should callback the game 
     * state's <code>gatherInput</code> method.
     * @param gameState	Current game state.
     */
    public void gatherInput(GameState gameState);
    
    /**
     * Send packet to the network player.
     * @param packet	Packet to send.
     */
    public void sendPacket(Packet packet);
    
    /**
     * Handle incoming packet. The implementing manager usually
     * queue the incoming packets to be processed by the 
     * <code>GameState</code> in the gather input stage. 
     * @param packet
     */
    public void handlePacket(Packet packet);
    
    /**
     * Returns the first packet in the incomig packets queue
     * and remove it from the queue.
     */
    public Packet getNextPacket();

    /**
     * Returns this player session id.
     */
    public Long getSenderId();

    /**
     * Returns the network player session id.
     */
    public Long getReceiverId();

    /**
     * Returns true if this user initiated the network game (i.e., sent
     * the invitation to play).
     */
    public boolean isInviter();
    
    /**
     * Do some cleanup before exiting.
     */
    public void cleanup();
    
}

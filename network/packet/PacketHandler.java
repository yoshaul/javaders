
package game.network.packet;

import game.network.client.GameNetworkManager;

/**
 * Each object in the game that creates or handles packet
 * should implement this interface.
 */
public interface PacketHandler {

    /**
     * Create and send packet(s) via the network manager.
     * @param netManager	Network manager.
     */
    public void createPacket(GameNetworkManager netManager);
    
    /**
     * Handle the incoming packet.
     * @param packet	Incoming packet.
     */
    public void handlePacket(Packet packet);
    
    /**
     * Returns the object network handler id that handles
     * the packets for this object. It might be the same object
     * or a different object.
     * @return	Id of the packet handlet that should handle 
     * incoming packets.
     */
    public int getHandlerId();
    
}

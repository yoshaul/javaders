package game.network.packet;

public interface PacketHandler {

    public void createPacket();
    
    public void handlePacket(Packet packet);
    
    public int getHandlerID();
    
}

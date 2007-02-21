package game.network;

import game.network.packet.Packet;

import java.util.List;


public interface NetworkManager {

    public void sendPacket(Packet packet);
    
    public Packet receivePacket();
    
    public Long login(String user, String password);
    
    public void logout(Long sessionId);
    
    public void signup(String user, String password, String email);
    
    public void acceptInvitations(boolean accept, Long sessionId);
    
    public void sendInvitation(Long sessionId);
    
    public List getAvailablePlayers();
    
    public Long getSenderID();
    
    public Long getReceiverID();
    
    public boolean isInviter();
    
}

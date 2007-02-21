package game.network;


public interface NetworkManager {

    public void sendPacket(GamePacket packet);
    
    public GamePacket receivePacket();
    
    public String login(String user, String password);
    
    public String signup(String user, String password, String email);
    
}

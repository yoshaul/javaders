package game.network.packet;

import java.util.Collection;

public class NewLevelPacket extends Packet {

    private Collection enemyShipsModels;

    public NewLevelPacket(Long senderID, Long receiverID, 
            Collection enemyShipsModels) {

        super(senderID, receiverID);
        this.enemyShipsModels = enemyShipsModels;
    }
    
    public Collection getEnemyShipsModels() {
        return this.enemyShipsModels;
    }
}

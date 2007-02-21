package game.network.packet;

import game.ship.weapon.BulletModel;

public class BulletPacket extends Packet {

    private BulletModel bulletModel ;
    
    public BulletPacket(Long senderID, Long receiverID, 
            BulletModel bulletModel) {

        super(senderID, receiverID);
        this.bulletModel = bulletModel;
    }
    
    public BulletModel getBulletModel() {
        return this.bulletModel;
    }
    
}

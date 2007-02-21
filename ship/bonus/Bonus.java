package game.ship.bonus;

import game.GameConstants;
import game.network.GameNetworkManager;
import game.network.packet.BulletPacket;
import game.network.packet.Packet;
import game.network.packet.PacketHandler;
import game.ship.Sprite;
import game.ship.Target;

import java.awt.Image;
import java.util.Collection;
import java.util.Iterator;

public abstract class Bonus extends Sprite implements PacketHandler {

    private boolean hit = false;
    
    public Bonus(float x, float y, float dx, float dy) {
        
        super(x, y, dx, dy);
        
    }

    
    public void createPacket() {
//        // Prepare the bullet model
//        BulletModel model = new BulletModel(owner.objectID, 
//                x, y, dx, dy, damage);
//        
//        GameNetworkManager netManager = 
//            owner.shipContainer.getNetworkManager();
//        
//        // Create the BulletPacket
//        Packet packet = new BulletPacket(netManager.getSenderID(), 
//                netManager.getReceiverID(), model); 
//        
//        netManager.sendPacket(packet);
        
    }
    
    public void handlePacket(Packet packet) {
        //
    }
    
    public void processCollisions(Collection targets) {
        int x0 = (int)Math.round(this.getX());
        int y0 = (int)Math.round(this.getY());
        int x1 = x0 + this.getWidth();
        int y1 = y0 + this.getHeight();

        Iterator targetsItr = targets.iterator();
        while (targetsItr.hasNext() && !hit) {
            Target target = (Target) targetsItr.next();
            if (target.isCollision(x0, y0, x1, y1)) {
                target.hit(this);
                hit = true;
            }
        }
    }
    
    public boolean isHit() {
        return hit;
    }
    
    public int getHandlerID() {
        return GameConstants.ENEMY_MANAGER_ID;
    }
    
}

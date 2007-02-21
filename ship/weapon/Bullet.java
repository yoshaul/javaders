package game.ship.weapon;

import game.network.GameNetworkManager;
import game.network.packet.*;
import game.ship.*;

import java.awt.Image;
import java.util.Collection;
import java.util.Iterator;

public abstract class Bullet extends Sprite implements PacketHandler {

    private Ship owner;
    private long damage;
    private boolean hit;

    public Bullet(Ship owner, float x, float y, float dx, float dy, 
            Image image, long damage) {
        
        super(x, y, dx, dy, image);
        this.damage = damage;
        this.owner = owner;
        
    }
    
    public Bullet(Ship owner, float x, float y, float dx, float dy, 
            long damage) {
        
        super(x, y, dx, dy);
        this.damage = damage;
        this.owner = owner;
        
    }
    
    public void createPacket() {
        // Prepare the bullet model
        BulletModel model = new BulletModel(owner.getObjectID(), 
                x, y, dx, dy, damage);
        
        GameNetworkManager netManager = 
            owner.getShipContainer().getNetworkManager();
        
        // Create the BulletPacket
        Packet packet = new BulletPacket(netManager.getSenderID(), 
                netManager.getReceiverID(), model); 
        
        netManager.sendPacket(packet);
        
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
    
    public long getDamage() {
        return this.damage;
    }
    
    public Ship getOwner() {
        return this.owner;
    }
    
    public int getHandlerID() {
        return owner.getShipContainer().getHandlerID();
    }
    
}

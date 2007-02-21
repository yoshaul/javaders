package game.ship.weapon;

import java.io.Serializable;

public class BulletModel implements Serializable {

    public int ownerID, type;
    public float x, y, dx, dy;
    public long damage;
    
    public BulletModel(int ownerID, float x, float y, float dx, float dy, 
            long damage) {
        
        this.ownerID = ownerID;
        this.x = x;
        this.y = y;
        this.dx = dx;
        this.dy = dy;
        this.damage = damage;
        
    }
    
}

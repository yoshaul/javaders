package game.ship.weapon;

import java.io.Serializable;

/**
 * The <code>BulletModel</code> class is used to send a bullet
 * fired in the game details to the network player.
 */
public class BulletModel implements Serializable {

    public Class bulletClass;
    public int ownerId;
    public float x, y, dx, dy;
    public long damage;
    
    /**
     * Construct a new BulletModel.
     * @param bulletClass	Class of the bullet
     * @param ownerId		Id of the ship that fired the bullet
     * @param x				Vertical ocation
     * @param y				Horizontal location
     * @param dx			Vertival velocity
     * @param dy			Horizontal velocity
     * @param damage		Damage this bullet cause
     */
    public BulletModel(Class bulletClass, int ownerId, float x, float y, 
            float dx, float dy, long damage) {
        
        this.bulletClass = bulletClass;
        this.ownerId = ownerId;
        this.x = x;
        this.y = y;
        this.dx = dx;
        this.dy = dy;
        this.damage = damage;
    }
    
}

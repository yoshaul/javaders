package game.ship.weapon;

import game.ship.Ship;

/**
 * A bullet that looks like a fireball.
 */
public class FireBullet extends Bullet {
    
    private final static String imageName = "fireshot.gif";
    private final static double DX = 0.2;
    private final static long damage = 7;
    
    /**
     * @see Bullet#Bullet(Ship, float, float, float, float, String, long) 
     */
    public FireBullet(Ship owner, float x, float y, float dx, float dy) {
        super(owner, x, y, dx, dy, imageName, damage);
        
    }

}

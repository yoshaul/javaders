
package game.ship.weapon;

import game.ship.Ship;

/**
 * Bullet that looks like a ball.
 */
public class BallBullet extends Bullet {

    private final static String imageName = "red_bullet.png";
    private final static long damage = 6;
    
    /**
     * @see Bullet#Bullet(Ship, float, float, float, float, String, long) 
     */
    public BallBullet(Ship owner, float x, float y, float dx, float dy) {
        super(owner, x, y, dx, dy, imageName, damage);
        
    }
    
}

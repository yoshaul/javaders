
package game.ship.weapon;

import game.ship.Ship;

/**
 * Bullet that looks like a ball. Very damaging bullet.
 */
public class BigBallBullet extends Bullet {

    private final static String imageName = "red_ball.png";
    private final static long damage = 20;
    
    /**
     * @see Bullet#Bullet(Ship, float, float, float, float, String, long) 
     */
    public BigBallBullet(Ship owner, float x, float y, float dx, float dy) {
        super(owner, x, y, dx, dy, imageName, damage);
        
    }
    
}
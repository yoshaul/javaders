
package game.ship.weapon;

import game.ship.Ship;

/**
 * The blue laser beam is a kind of laser bullet.
 */
public class BlueLaser extends Bullet {
    
    private final static String imageName = "bluelaser.png";
    private final static double DX = 0.3;
    private final static long damage = 10;
    
    /**
     * @see Bullet#Bullet(Ship, float, float, float, float, String, long) 
     */
    public BlueLaser (Ship owner, float x, float y, float dx, float dy) {
        super(owner, x, y, dx, dy, imageName, damage);
    }

}
package game.ship.weapon;

import game.ship.Ship;

/**
 * The laser beam is a laser bullet.
 */
public class LaserBeam extends Bullet {
    
    private final static String imageName = "laserbeam.png";
    private final static long damage = 5;
    
    /**
     * @see Bullet#Bullet(Ship, float, float, float, float, String, long) 
     */
    public LaserBeam (Ship owner, float x, float y, float dx, float dy) {
        super(owner, x, y, dx, dy, imageName, damage);
    }

}

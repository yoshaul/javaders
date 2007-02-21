package game.ship;

import game.ship.bonus.Bonus;
import game.ship.weapon.Bullet;

/**
 * A <code>Target</code> is an object that can collide with
 * various objects in the game.
 */
public interface Target {

    /**
     * Returns true if this objects collides with the rectangle
     * represented by the point.
     * @param x0	Top left x position
     * @param y0	Top left y position
     * @param x1	Botom right x position
     * @param y1	Botom right y position
     * @return True if this objects collides with the rectangle
     * represented by the point
     */
    public boolean isCollision(int x0, int y0, int x1, int y1);
    
    /**
     * Hit the target and cause some damage.
     * @param damage	Damage to cause.
     */
    public void hit(long damage);
    
    /**
     * Hit the target with the bullet.
     * @param bullet	Bullet object.
     */
    public void hit(Bullet bullet);
    
    /**
     * Hit the target with the bonus.
     * @param bonus	Bonus object.
     */
    public void hit(Bonus bonus);
    
}

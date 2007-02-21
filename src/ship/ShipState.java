
package game.ship;

import java.io.Serializable;

/**
 * The <code>ShipState</code> class holds the state of the ship
 * at the time of this object creation. It is used to send the
 * state to the netwrok player.
 */
public class ShipState implements Serializable {

    public float x, y, dy, dx;
    public long armor;
    public int state;
    
    /**
     * Construct a new ship state. 
     * @param x			Current vertical position.
     * @param y			Current horizontal position.
     * @param dx		Current ship's vertical velocity.
     * @param dy		Current ship's horizontal velocity.
     * @param armor		Current ship's armor.
     * @param state		Current ship's state.
     */
    public ShipState(float x, float y, float dx, float dy, 
            long armor, int state) {
        
        this.x = x;
        this.y = y;
        this.dx = dx;
        this.dy = dy;
        this.armor = armor;
        this.state = state;
        
    }
    
}

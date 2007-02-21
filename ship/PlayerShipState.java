
package game.ship;

/**
 * The player ship state adds some more data needed for a
 * player ship.
 */
public class PlayerShipState extends ShipState {

    public long score;	// The player's score
    
    /**
     * Construct a new ship state. 
     * @param x			Current vertical position.
     * @param y			Current horizontal position.
     * @param dx		Current ship's vertical velocity.
     * @param dy		Current ship's horizontal velocity.
     * @param armor		Current ship's armor.
     * @param state		Current ship's state.
     * @param score		The player's score
     */
    public PlayerShipState(float x, float y, float dx, float dy, long armor,
            int state, long score) {
        super(x, y, dx, dy, armor, state);
        this.score = score;
        
    }

}


package game.input;

/**
 * The <code>GameAction</code> class represents a game
 * action key, like firing, moving left, etc.
 * It holds the state of the game action (e.g., pressed/released).
 */
public class GameAction {

    private final static int STATE_RELEASED = 0;
    private final static int STATE_PRESSED  = 1;
    
    private int state;	// The game action state
    
    /**
     * No parameters constructor.
     */
    public GameAction() {
        reset();
    }
    
    /**
     * Press the game action - mark as pressed.
     */
    public void press() {
        state = STATE_PRESSED;
    }
    
    /**
     * Release the game action - mark as released.
     */
    public void release() {
        state = STATE_RELEASED;
    }
    
    /**
     * Returns true if this <code>GameAction</code> is pressed.
     * @return True if this <code>GameAction</code> is pressed.
     */
    public boolean isPressed() {
        return (state == STATE_PRESSED);
    }
    
    /**
     * Resets this object state. Default is released.
     */
    public void reset() {
        state = STATE_RELEASED;
    }
    
}

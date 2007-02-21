
package game.input;


public class GameAction {

    private final static int STATE_RELEASED = 0;
    private final static int STATE_PRESSED  = 1;
    
    private int state;
    private int amount;
    
    public GameAction() {
        reset();
    }
    
    public void press() {
        state = STATE_PRESSED;
        amount++;
    }
    
    public void release() {
        state = STATE_RELEASED;
    }
    
    public boolean isPressed() {
        return (state == STATE_PRESSED);
    }
    
    public int getAmount() {
        return amount;
    }
    
    public void reset() {
        state = STATE_RELEASED;
        amount = 0; 
    }
    
}

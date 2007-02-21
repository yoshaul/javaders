package game.ship;

import java.io.Serializable;

public class ShipState implements Serializable {

    public float x, y, dy, dx;
    public long armor;
    public int state;
    
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

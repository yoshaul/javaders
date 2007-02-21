package game.ship;

import java.io.Serializable;

public class ShipModel implements Serializable {

    public int objectID, shipType;
    public float x, y, dx, dy;
    
    public ShipModel (int objectID, int shipType, 
            float x, float y, float dx, float dy) {
        
        this.objectID = objectID;
        this.shipType = shipType;
        this.x = x;
        this.y = y;
        this.dx = dx;
        this.dy = dy;
    }
    
}

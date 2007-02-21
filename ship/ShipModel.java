package game.ship;

import java.io.Serializable;

/**
 * This class represents a model of a ship to be sent to the
 * network player when the ship is created (start of new level).
 */
public class ShipModel implements Serializable {

    public int objectId, shipType;
    public float x, y, dx, dy;
    
    /**
     * Construct a new ship model.
     * @param objectId	Id of the ship
     * @param shipType	Type of the ship
     * @param x		Vertical location of the ship (from left)
     * @param y		Horizontal location of the ship (from top)
     * @param dx	Max vertical velocity (pixels/sec)
     * @param dy	Max horizontal velocity (pixels/sec)
     */
    public ShipModel (int objectId, int shipType, 
            float x, float y, float dx, float dy) {
        
        this.objectId = objectId;
        this.shipType = shipType;
        this.x = x;
        this.y = y;
        this.dx = dx;
        this.dy = dy;
    }
    
}

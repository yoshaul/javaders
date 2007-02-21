
package game.ship;

/**
 * Any movable object in the game should implement the movable
 * interface (the base Sprite class implements this interface). 
 */
public interface Movable {

    /**
     * Update the position of the movable object according to the
     * time passed.
     * @param elapsedTime	Time elapsed since the last update 
     * (in milliseconds).
     */
    public void updatePosition(long elapsedTime);
    
}


package game.ship;

import java.awt.Graphics;

/**
 * The active objects in the game should implement this interface.
 * (The base Sprite class implements this interface)
 */
public interface Renderable {

    /**
     * Render the object givet the graphics context.
     * @param g		A <code>Graphics</code> object.
     */
    public void render(Graphics g);
    
}

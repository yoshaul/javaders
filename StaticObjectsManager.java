
package game;

import game.ship.Renderable;

import java.awt.*;

import javax.swing.*;

public class StaticObjectsManager implements Renderable {
    
    
    private Image bgImage;
    private final static String bgFileName = "orion_mpg_big.jpg";

    
    public StaticObjectsManager() {
        
        // Load the background image
        ImageIcon icon = new ImageIcon(GameConstants.IMAGES_DIR+bgFileName);
        bgImage = icon.getImage();
        
    }
    
    public void render(Graphics g) {

        g.drawImage(bgImage, 0, 0, null);
        
    }

}

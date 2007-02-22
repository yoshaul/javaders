
package game;

import game.graphic.GraphicsHelper;
import game.ship.PlayerShip;

import java.awt.*;

/**
 * The <code>StaticObjectsManager</code> handles the static objects
 * that needs to be rendered on the screen (except for the game menus
 * which the <code>GUIManager</code> handles>
 */
public class StaticObjectsManager {
    
    private GameLoop gameLoop;
    
    private Image bgImage;	// Background image
    private final static String defBGImageName = "bg2_1024.jpg";
    
    /**
     * Construct new StaticObjectsManager and load the 
     * default background image.
     * @param gameLoop Reference to the game loop.
     */
    public StaticObjectsManager(GameLoop gameLoop) {
        
        this.gameLoop = gameLoop;
        
        // Load the default background image
        setBackgroundImage(defBGImageName);
        
    }
    
    /**
     * Sets the background image.
     * @param imageName	Name of the image to load (from the images
     * directory).
     */
    public void setBackgroundImage(String imageName) {
        bgImage = gameLoop.getScreenManager().getCompatibleImage(
                imageName, Transparency.OPAQUE);
    }
    
    /**
     * Render static objects on the screen (background image,
     * player score, etc.).
     */
    public void render(Graphics g) {

        Dimension screenDimention = 
            gameLoop.getScreenManager().getScreenDimension();
        
        // Draw background image
        g.drawImage(bgImage, 0, 0, 
                screenDimention.width, screenDimention.height, null);
        
        // Draw player statistics
        PlayerShip player1Ship =
            gameLoop.getPlayerManager().getPlayer1Ship();
        
        PlayerShip player2Ship = 
            gameLoop.getPlayerManager().getPlayer2Ship();
    
        
        g.setFont(new Font(null, Font.BOLD, 12));
        g.setColor(Color.GREEN);

        GraphicsHelper.setAntialiasedText((Graphics2D)g);
        
        int level = gameLoop.getLevelsManager().getCurrentLevel();
        g.drawString("Level " + level, 10, 15);
        
        if (player1Ship != null) {
            g.drawString("SCORE: " + player1Ship.getScore(), 10, screenDimention.height-35);
            g.drawString("POWER: " + player1Ship.getArmor(), 10, screenDimention.height-20);
        }
        
        if (player2Ship != null) {
            
            String scoreText = "SCORE: " + player2Ship.getScore();
            int textWidth = g.getFontMetrics().stringWidth(scoreText);
            int rightAlignment = Math.max(90, textWidth + 10);

            g.drawString(scoreText, 
                    screenDimention.width - rightAlignment, screenDimention.height-35);
            g.drawString("POWER: " + player2Ship.getArmor(), 
                    screenDimention.width - rightAlignment, screenDimention.height-20);
        }
    }
    
}

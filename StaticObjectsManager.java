
package game;

import game.ship.PlayerShip;
import game.ship.Renderable;

import java.awt.*;

import javax.swing.*;

public class StaticObjectsManager implements Renderable {
    
    private GameLoop gameLoop;
    
    private Image bgImage;
    private final static String bgFileName = "orion_mpg_big.jpg";

    
    public StaticObjectsManager(GameLoop gameLoop) {
        
        this.gameLoop = gameLoop;
        
        // Load the background image
        ImageIcon icon = new ImageIcon(GameConstants.IMAGES_DIR+bgFileName);
        bgImage = icon.getImage();
        
    }
    
    public void render(Graphics g) {

        Dimension screenDimention = 
            gameLoop.getScreenManager().getScreenDimension();
        
        g.drawImage(bgImage, 0, 0, 
                screenDimention.width, screenDimention.height, null);
        
        PlayerShip player1Ship =
            gameLoop.getPlayerManager().getPlayer1Ship();
        
        PlayerShip player2Ship = 
            gameLoop.getPlayerManager().getPlayer2Ship();
    
        
        g.setFont(new Font(null, Font.BOLD, 12));
        g.setColor(Color.GREEN);
        
        if (player1Ship != null) {
            g.drawString("SCORE: " + player1Ship.getScore(), 10, screenDimention.height-35);
            g.drawString("POWER: " + player1Ship.getArmor(), 10, screenDimention.height-20);
        }
        
        if (player2Ship != null) {
            g.drawString("SCORE: " + player2Ship.getScore(), 
                    screenDimention.width - 50, screenDimention.height-35);
            g.drawString("POWER: " + player2Ship.getArmor(), 
                    screenDimention.width - 50, screenDimention.height-20);
        }
    }
    
    public void renderLoading(Graphics g) {
        this.render(g);
        
        Dimension screenDimention = 
            gameLoop.getScreenManager().getScreenDimension();
        
        g.setFont(new Font(null, Font.BOLD, 30));
        g.setColor(Color.BLUE);
        
        FontMetrics metrics = g.getFontMetrics();
        
        String loadingStr = "LOADING.... ";
        
        int width = metrics.stringWidth(loadingStr);
        
        int middleScreen = screenDimention.width / 2;
        
        g.drawString(loadingStr , middleScreen - width/2, screenDimention.height/2);
        
    }

}

package game.ship.bonus;

import game.GameConstants;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;

public class PowerUp extends Bonus {

    private final String imageName = "bonus.png";
    private long powerUp;
    
    
    public PowerUp(float x, float y, float dx, float dy, 
            long powerUp) {
        
        super(x, y, dx, dy);
        Image image = new ImageIcon(GameConstants.IMAGES_DIR + imageName).getImage();
        setSpriteImage(image);
        this.powerUp = powerUp;
        
    }
    
    public void render(Graphics g) {
        
        super.render(g);
        
        g.draw3DRect((int)Math.round(x), (int)Math.round(y), 5, 5, true);
        g.drawString(String.valueOf(powerUp), getCenterX(), getCenterY());
        
        
    }
    
    public long getPowerUp() {
        return this.powerUp;
    }
    
}

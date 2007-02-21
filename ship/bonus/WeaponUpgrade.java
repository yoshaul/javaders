package game.ship.bonus;

import game.GameConstants;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;

public class WeaponUpgrade extends Bonus {

    private static final String imageName = "bonus.png";
    private static final float DX = 0.0f;
    private static final float DY = 0.2f;
    
    private int weaponType;
    
    
    public WeaponUpgrade(float x, float y, int weaponType) {
        
        super(x, y, DX, DY);
        Image image = new ImageIcon(GameConstants.IMAGES_DIR + imageName).getImage();
        setSpriteImage(image);
        this.weaponType = weaponType;
        
    }
    
    public void render(Graphics g) {
        
        super.render(g);
        
        g.draw3DRect((int)Math.round(x), (int)Math.round(y), 5, 5, true);
        g.drawString(String.valueOf(weaponType), getCenterX(), getCenterY());
        
        
    }
    
    public int getWeaponType() {
        return this.weaponType;
    }
    
    
    
}

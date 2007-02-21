package game.ship.weapon;

import game.GameConstants;
import game.ship.Ship;

import java.awt.Image;

import javax.swing.ImageIcon;

public class FireBullet extends Bullet {
    
    private final static String imageName = "fireshot.gif";
    private final static double DX = 0.2;
    private final static long damage = 7;
    
    
    public FireBullet(Ship owner, float x, float y, float dx, float dy) {
        super(owner, x, y, dx, dy, damage);
        Image image = new ImageIcon(GameConstants.IMAGES_DIR + imageName).getImage();
        setSpriteImage(image);
    }
    
    public long getDamage() {
        return damage;
    }
}

package game.ship.weapon;

import game.*;
import game.ship.Ship;

import java.awt.Image;

import javax.swing.ImageIcon;


public class LaserBean extends Bullet {
    
    private final static String imageName = "laserbeam.png";
    private final static double DX = 0.3;
    private final static long damage = 5;
    
    
    public LaserBean(Ship owner, float x, float y, float dx, float dy) {
        super(owner, x, y, dx, dy, damage);
        Image image = new ImageIcon(GameConstants.IMAGES_DIR + imageName).getImage();
        setSpriteImage(image);
    }
    
    public long getDamage() {
        return damage;
    }

}

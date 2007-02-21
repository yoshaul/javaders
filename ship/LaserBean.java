package game.ship;

import game.*;
import game.util.Log;

import java.awt.Image;

import javax.swing.ImageIcon;


public class LaserBean extends Sprite {
    
    private final static String imageName = "laserbeam.png";
    private final static double DX = 0.3;
    private final static long damage = 5;
    
    
    public LaserBean(double x, double y, double dx, double dy) {
        super(x, y, dx, dy);
        Image image = new ImageIcon(GameConstants.IMAGES_DIR + imageName).getImage();
        setSpriteImage(image);
    }
    
    public long getDamage() {
        return damage;
    }

}

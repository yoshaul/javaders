package game.ship;

import java.awt.Graphics;
import java.awt.Image;
import java.util.Collection;

public class PlayerShip extends Ship {

    private static final long DAMAGE = 2;
    private static final long ARMOR = 20;
    
    private boolean vulnerable;
    
    public PlayerShip(double x, double y, double dx, double dy,
            Image image, Weapon gun) {
        
        super(x, y, dx, dy, image, gun, ARMOR, DAMAGE);
        vulnerable = true;
        
    }
    
    public void render(Graphics g){
        
        super.render(g);
        
        // If not vulnerable draw a bounding sphere
        if (!vulnerable) {
            g.drawOval((int) Math.round(getX()) - 3, 
                    (int) Math.round(getY()) - 10, getWidth() + 6, getHeight() + 20);
        }
        
    }
        
    public void processCollisions(Collection targets) {

        if (vulnerable) {
            super.processCollisions(targets);
        }
        else {
            // Don't process ship collisions
            if (gun != null) {
                gun.processCollisions(targets);
            }
        }
         
    }
    
    public boolean isCollision(int x0, int y0, int x1, int y1) {
        
        if(vulnerable) {
            return super.isCollision(x0, y0, x1, y1);
        }
        else {
            return false;
        }
        
    }
    
    public boolean isVulnerable() {
        return vulnerable;
    }
    

}

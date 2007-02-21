package game.ship;

import java.awt.Graphics;
import java.util.Collection;

public interface Weapon {
    
    public static final int DIRECTION_UP = -1;
    public static final int DIRECTION_DOWN = 1;
    
    public void shoot(double x, double y);
    
    public void update(long elapsedTime);
    
    public void render(Graphics g);
    
    public void processCollisions(Collection targets);
    
}

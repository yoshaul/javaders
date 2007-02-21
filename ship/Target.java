package game.ship;

import game.ship.bonus.Bonus;
import game.ship.weapon.Bullet;


public interface Target {

    public boolean isCollision(int x0, int y0, int x1, int y1);
    
    public void hit(long damage);
    
    public void hit(Bullet bullet);
    
    public void hit(Bonus bonus);
    
}

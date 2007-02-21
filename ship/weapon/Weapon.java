package game.ship.weapon;

import game.ship.Ship;

import java.io.Serializable;

public interface Weapon extends Serializable {
    
    public static final int DIRECTION_UP = -1;
    public static final int DIRECTION_DOWN = 1;
    
    public void setOwner(Ship owner);
    public Ship getOwner();
    
    public void fire(float x, float y);
    
    public int getWeaponType();
    public int getWeaponLevel();
    
    public void upgradeWeapon();
    
//    public void update(long elapsedTime);
//    
//    public void render(Graphics g);
    
//    public void processCollisions(Collection targets);
    
}

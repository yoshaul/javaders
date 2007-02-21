package game.ship;

import game.ship.bonus.Bonus;
import game.ship.bonus.PowerUp;
import game.ship.bonus.WeaponUpgrade;
import game.ship.weapon.Weapon;
import game.ship.weapon.WeaponFactory;
import game.sound.SoundFactory;

import java.awt.Graphics;
import java.awt.Image;
import java.util.Collection;

public class PlayerShip extends Ship {

    private static final long DAMAGE = 2;
    private static final long ARMOR = 20;
    
    private long score;
    private boolean vulnerable;
    
    public PlayerShip(int objectID, int shipType,
            float x, float y, float dx, float dy,
            Image image, Weapon gun) {
        
        super(objectID, shipType, x, y, dx, dy, image, gun, ARMOR, DAMAGE, 0, 0);
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
//            if (weapon != null) {
//                weapon.processCollisions(targets);
//            }
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
    
    public void addScore(long value) {
        score += value;
    }
    
    public long getScore() {
        return this.score;
    }
    
    
    public void hit(Bonus bonus) {
        if (isNormal()) {
	        
            if (bonus instanceof PowerUp) {
                PowerUp powerUp = (PowerUp)bonus;
                
                SoundFactory.playSound("hit1.wav");
                
                long power = powerUp.getPowerUp();
                armor += power;
    	        
//    	        // Add the score to the hitting player
//    	        if (bullet.getOwner() instanceof PlayerShip){
//    	            long score = (armor<=0) ? destroyScoreValue : hitScoreValue; 
//    	            PlayerShip ship = (PlayerShip) bullet.getOwner();
//    	            ship.addScore(score);
//    	        }
            } else if (bonus instanceof WeaponUpgrade) {
                WeaponUpgrade weaponUpgrade = (WeaponUpgrade)bonus;
                
                SoundFactory.playSound("hit1.wav");
                
                int weaponType = weaponUpgrade.getWeaponType();
                
                if (weaponType == this.weapon.getWeaponType()) {
                    weapon.upgradeWeapon();
                } else {
                    Weapon newWeapon = WeaponFactory.getWeapon(weaponType, 
                            weapon.getWeaponLevel()+1, Weapon.DIRECTION_UP);
                    
                    newWeapon.setOwner(this);
                    
                    this.setWeapon(newWeapon);
                }
            }

        }
    }
    

}

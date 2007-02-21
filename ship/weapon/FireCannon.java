package game.ship.weapon;

import game.ship.Ship;
import game.sound.SoundFactory;

public class FireCannon implements Weapon {

    private final int weaponType = 
        WeaponFactory.TYPE_FIRE_CANNON;
    
    private Ship owner;
    private int direction;
    private long lastFiringTime;
    private final long firingRate = 1200;
    private int weaponLevel;
    
    public FireCannon(int direction, int weaponLevel) {
        this.direction = direction;
        this.weaponLevel = weaponLevel;
        this.lastFiringTime = 0;
    }
    
    public void fire(float x, float y) {

        long now = System.currentTimeMillis();
        long elapsedTime =  now - lastFiringTime;
        
        if (elapsedTime >= firingRate) {
            lastFiringTime = now;
            
            if (weaponLevel >= 1) {
	            // Fire one laser up
	            Bullet shoot = new FireBullet(getOwner(), x, y, 0.0f, 
	                    direction*0.2f);
	            
	            if (owner.getShipContainer().isNetworkGame()) {
	                // Create and send packet
	                shoot.createPacket();
	            }
	            
	            getOwner().getShipContainer().addShoot(shoot);

	            SoundFactory.playAppletClip("laser1.wav");
            }

        }

    }
    
    
    /**
     * Sets the ship owning the weapon.
     */
    public void setOwner(Ship owner) {
        this.owner = owner;
    }
    
    /**
     * Get the ship owning the weapon.
     */
    public Ship getOwner() {
        return this.owner;
    }
    
    /**
     * Add one to the weapon level.
     */
    public void upgradeWeapon() {
        this.weaponLevel++;
    }
    
    public int getWeaponType() {
        return this.weaponType;
    }
    
    public int getWeaponLevel() {
        return this.weaponLevel;
    }

    
    
}

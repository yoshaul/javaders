package game.ship.weapon;

import game.ship.Ship;
import game.sound.SoundFactory;


public class LaserCannon implements Weapon {

    private final int weaponType = 
        WeaponFactory.TYPE_LASER_CANNON;
    
    private Ship owner;
    private int direction;
    private long lastFiringTime;
    private final long firingRate = 1000;
    private int weaponLevel;
    
    
    public LaserCannon(int direction, int weaponLevel) {
        this.direction = direction;
        this.weaponLevel = weaponLevel;
        this.lastFiringTime = 0;
    }
    
    public void fire(float x, float y) {

        long now = System.currentTimeMillis();
        long elapsedTime =  now - lastFiringTime;
        
        if (elapsedTime >= firingRate) {
            lastFiringTime = now;
            
            if (weaponLevel == 1) {
	            // Fire one laser up
	            Bullet shoot = new LaserBean(getOwner(), x, y, 0.0f, 
	                    direction*0.2f);
	            
	            
	            if (owner.getShipContainer().isNetworkGame()) {
	                // Create and send packet
	                shoot.createPacket();
	            }
	            
	            getOwner().getShipContainer().addShoot(shoot);
	
	//          SoundFactory.playSound("laser1.wav");
	//          SoundFactory.playCachedSound();
	          
	//          SoundFactory.playDataSource("laser1.wav");
	            SoundFactory.playAppletClip("laser1.wav");
            }
            else if (weaponLevel == 2) {
	            // Fire two lasers up
	            Bullet shoot1 = new LaserBean(getOwner(), x-6, y, 0.0f, 
	                    direction*0.2f);
	            
	            Bullet shoot2 = new LaserBean(getOwner(), x+6, y, 0.0f, 
	                    direction*0.2f);
	            
	            if (owner.getShipContainer().isNetworkGame()) {
	                // Create and send packet
	                shoot1.createPacket();
	                shoot2.createPacket();
	            }
	            
	            getOwner().getShipContainer().addShoot(shoot1);
	            getOwner().getShipContainer().addShoot(shoot2);
	
	            SoundFactory.playAppletClip("laser1.wav");
            }
            else if (weaponLevel >= 3) {
                // Fire two diagonal lasers and one up
	            Bullet shoot1 = new LaserBean(getOwner(), x-6, y, 0.05f, 
	                    direction*0.2f);
	            
	            Bullet shoot2 = new LaserBean(getOwner(), x+6, y, -0.05f, 
	                    direction*0.2f);
	            
	            Bullet shoot3 = new LaserBean(getOwner(), x, y, 0.0f, 
	                    direction*0.2f);
	            
	            if (owner.getShipContainer().isNetworkGame()) {
	                // Create and send packet
	                shoot1.createPacket();
	                shoot2.createPacket();
	                shoot3.createPacket();
	            }
	            
	            getOwner().getShipContainer().addShoot(shoot1);
	            getOwner().getShipContainer().addShoot(shoot2);
	            getOwner().getShipContainer().addShoot(shoot3);
	
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

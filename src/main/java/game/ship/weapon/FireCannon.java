
package game.ship.weapon;

import game.sound.SoundFactory;

/**
 * The <code>FireCannon</code> weapon mainly fires fire bullets.
 */
public class FireCannon extends AbstractWeapon {

    private static final long BASE_FIRING_RATE = 1100;
    private long lastFiringTime;
    
    /**
     * Construct a new weapon.
     * @param direction		Base horizontal direction of the weapon. 
     * @param weaponLevel	Level of the weapon.
     */
    public FireCannon(int direction, int weaponLevel) {
        super(direction, weaponLevel, 
                WeaponFactory.TYPE_FIRE_CANNON, BASE_FIRING_RATE);
        this.lastFiringTime = 0;
        updateFiringRate();
    }
    
    /**
     * Fire a new bullet(s).
     */
    public void fire(float x, float y) {

        long now = System.currentTimeMillis();
        long elapsedTime =  now - lastFiringTime;
        
        if (elapsedTime >= firingRate) {
            lastFiringTime = now;
            
            if (weaponLevel == 1) {
	            Bullet shoot = new FireBullet(getOwner(), x, y, 0.0f, 
	                    direction*0.2f);
	            
	            if (owner.getShipContainer().isNetworkGame()) {
	                // Create and send packet
	                shoot.createPacket(
	                        owner.getShipContainer().getNetworkManager());
	            }
	            
	            getOwner().getShipContainer().addShot(shoot);

	            SoundFactory.playSound("fire_shot.wav");
            }
            else if (weaponLevel == 2) {
	            Bullet shoot1 = new FireBullet(getOwner(), x-6, y, 0.0f, 
	                    direction*0.2f);
	            
	            Bullet shoot2 = new FireBullet(getOwner(), x+6, y, 0.0f, 
	                    direction*0.2f);
	            
	            if (owner.getShipContainer().isNetworkGame()) {
	                // Create and send packets
	                shoot1.createPacket(
	                        owner.getShipContainer().getNetworkManager());
	                shoot2.createPacket(
	                        owner.getShipContainer().getNetworkManager());
	            }
	            
	            getOwner().getShipContainer().addShot(shoot1);
	            getOwner().getShipContainer().addShot(shoot2);

	            SoundFactory.playSound("fire_shot.wav");
            }
            else if (weaponLevel == 3) {
	            Bullet shoot1 = new FireBullet(getOwner(), x-6, y+10, 0.0f, 
	                    direction*0.2f);
	            
	            Bullet shoot2 = new FireBullet(getOwner(), x+6, y+10, 0.0f, 
	                    direction*0.2f);
	            
	            Bullet shoot3 = new FireBullet(getOwner(), x, y, 0.0f, 
	                    direction*0.2f);
	            
	            if (owner.getShipContainer().isNetworkGame()) {
	                // Create and send packets
	                shoot1.createPacket(
	                        owner.getShipContainer().getNetworkManager());
	                shoot2.createPacket(
	                        owner.getShipContainer().getNetworkManager());
	                shoot3.createPacket(
	                        owner.getShipContainer().getNetworkManager());
	            }
	            
	            getOwner().getShipContainer().addShot(shoot1);
	            getOwner().getShipContainer().addShot(shoot2);
	            getOwner().getShipContainer().addShot(shoot3);

	            SoundFactory.playSound("fire_shot.wav");
            }
            else if (weaponLevel >= 4) {
	            Bullet shoot1 = new FireBullet(getOwner(), x-6, y+10, 0.0f, 
	                    direction*0.2f);
	            
	            Bullet shoot2 = new FireBullet(getOwner(), x+6, y+10, 0.0f, 
	                    direction*0.2f);
	            
	            Bullet shoot3 = new FireBullet(getOwner(), x, y, 0.0f, 
	                    direction*0.2f);
	            
	            Bullet shoot4 = new FireBullet(getOwner(), x, y+20, 0.0f, 
	                    direction*0.2f);
	            
	            if (owner.getShipContainer().isNetworkGame()) {
	                // Create and send packets
	                shoot1.createPacket(
	                        owner.getShipContainer().getNetworkManager());
	                shoot2.createPacket(
	                        owner.getShipContainer().getNetworkManager());
	                shoot3.createPacket(
	                        owner.getShipContainer().getNetworkManager());
	                shoot4.createPacket(
	                        owner.getShipContainer().getNetworkManager());
	            }
	            
	            getOwner().getShipContainer().addShot(shoot1);
	            getOwner().getShipContainer().addShot(shoot2);
	            getOwner().getShipContainer().addShot(shoot3);
	            getOwner().getShipContainer().addShot(shoot4);

	            SoundFactory.playSound("fire_shot.wav");
            }
        }
    }	// end method fire
    
    /**
     * Add one to the weapon level.
     */
    public void upgradeWeapon() {
        super.upgradeWeapon();
        updateFiringRate();

    }
    
    /**
     * Updates the firing rate of the weapon. Makes
     * the weapon faster as the level increases.
     */
    private void updateFiringRate() {
        if (weaponLevel > 1) {
            // Increase the firing rate
            firingRate = BASE_FIRING_RATE - weaponLevel * 7; 
        }
    }
    
}

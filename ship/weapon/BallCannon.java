
package game.ship.weapon;

import game.sound.SoundFactory;

/**
 * The <code>BallCannon</code> weapon fires ball bullets.
 * For now, this weapon is used in the enemy boss ships.
 */
public class BallCannon extends AbstractWeapon {

    private static final long BASE_FIRING_RATE = 1000;
    private long lastFiringTime;
    
    /**
     * Construct a new weapon.
     * @param direction		Base horizontal direction of the weapon. 
     * @param weaponLevel	Level of the weapon.
     */
    public BallCannon(int direction, int weaponLevel) {
        super(direction, weaponLevel, 
                WeaponFactory.TYPE_FIRE_CANNON, BASE_FIRING_RATE);
        this.lastFiringTime = 0;
    }
    
    /**
     * Fire a new bullet(s).
     */
    public void fire(float x, float y) {

        long now = System.currentTimeMillis();
        long elapsedTime =  now - lastFiringTime;
        
        if (elapsedTime >= firingRate) {
            lastFiringTime = now;
            
            float[][] directions = {
                    	{0.0f, -0.2f}, {0.2f, 0.0f},
                    	{0.1f, 0.1f}, {0.0f, 0.2f},
                    	{-0.1f, 0.1f}, {-0.2f, 0.0f}
                };
            
            // Small chance to shoot the big red balls
            boolean bigAmmo = Math.random() > 0.9;
            
            for (int i = 0; i < 6; i++) {
                Bullet shoot; 
                
                if (bigAmmo) {
                    shoot = new BigBallBullet(getOwner(), x, y, 
                            directions[i][0], directions[i][1]);
                }
                else {
                    shoot = new BallBullet(getOwner(), x, y, 
                        directions[i][0], directions[i][1]);
                }
                
	            if (owner.getShipContainer().isNetworkGame()) {
	                // Create and send packet
	                shoot.createPacket(
	                        owner.getShipContainer().getNetworkManager());
	            }
	            
	            getOwner().getShipContainer().addShot(shoot);
                
            }
            
            SoundFactory.playSound("fire_shot.wav");
        }
    }	// end method fire
    
}
package game.ship.weapon;

import game.ship.Ship;

/**
 * The <code>AbstractWeapon</code> class implements some of 
 * the <code>Weapon</code> interface methods and defines some
 * common attributes for weapons.
 */
public abstract class AbstractWeapon implements Weapon {

    // Type of the weapon 
    // (the various types are defined in the WeaponFactory class)
    protected final int weaponType;
    
    // Firing rate of the weapon
    protected long firingRate;
    
    protected Ship owner;
    protected int direction;
    protected int weaponLevel;

    /**
     * Abstract weapon constructor
     * @param direction		General horizontal direction of the bullets
     * fired by this weapon.
     * @param weaponLevel	Level of the weapon
     * @param weaponType	Type of the weapon
     * @param firingRate	Firing rate of the weapon
     */
    public AbstractWeapon (int direction, int weaponLevel,
            int weaponType, long firingRate) {
        this.direction = direction;
        this.weaponLevel = weaponLevel;
        this.weaponType = weaponType;
        this.firingRate = firingRate;
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
    
    /**
     * Returns the weapon type.
     */
    public int getWeaponType() {
        return this.weaponType;
    }
    
    /**
     * Returns the weapon level.
     */
    public int getWeaponLevel() {
        return this.weaponLevel;
    }
    
}

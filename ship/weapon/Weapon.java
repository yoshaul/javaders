package game.ship.weapon;

import game.ship.Ship;

import java.io.Serializable;

/**
 * All the weapons in the game should implment this interface.
 */
public interface Weapon extends Serializable {
    
    /** Horizontal direction of the weapon (usually enemies fire down) */
    public static final int DIRECTION_UP = -1;
    public static final int DIRECTION_DOWN = 1;
    
    /**
     * Sets the owner of the weapon.
     * @param owner	Owning ship of the weapon.
     */
    public void setOwner(Ship owner);
    
    /**
     * Returns the owner of the weapon.
     * @return The owner of the weapon.
     */
    public Ship getOwner();
    
    /**
     * Fire a bullet (or more) from the weapon
     * @param x		Vertical location to fire from
     * @param y		Horizontal location to fire from
     */
    public void fire(float x, float y);
    
    /**
     * Returns the type of the weapon (the weapon types
     * are defined in the <code>WeaponFactory</code> class.
     * @return The type of the weapon
     */
    public int getWeaponType();
    
    /**
     * Returns the level of the weapon. The more advanced is the level
     * the more lethal the weapon's shots.
     * @return	The level of the weapon.
     */
    public int getWeaponLevel();
    
    /**
     * This method is called when the owning ship upgrades it's
     * weapon (received bonus). The weapon should advance to the
     * next level of add damage to the bullets.
     */
    public void upgradeWeapon();
    
}

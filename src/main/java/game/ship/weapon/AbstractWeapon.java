/*
 * This file is part of Javaders.
 * Copyright (c) Yossi Shaul
 *
 * Javaders is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Javaders is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Javaders.  If not, see <http://www.gnu.org/licenses/>.
 */

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
    private final int weaponType;

    // Firing rate of the weapon
    long firingRate;

    Ship owner;
    int direction;
    int weaponLevel;

    /**
     * Abstract weapon constructor
     *
     * @param direction   General horizontal direction of the bullets
     *                    fired by this weapon.
     * @param weaponLevel Level of the weapon
     * @param weaponType  Type of the weapon
     * @param firingRate  Firing rate of the weapon
     */
    AbstractWeapon(int direction, int weaponLevel,
            int weaponType, long firingRate) {
        this.direction = direction;
        this.weaponLevel = weaponLevel;
        this.weaponType = weaponType;
        this.firingRate = firingRate;
    }


    /**
     * Sets the ship owning the weapon.
     */
    @Override
    public void setOwner(Ship owner) {
        this.owner = owner;
    }

    /**
     * Get the ship owning the weapon.
     */
    @Override
    public Ship getOwner() {
        return this.owner;
    }

    /**
     * Add one to the weapon level.
     */
    @Override
    public void upgradeWeapon() {
        this.weaponLevel++;
    }

    /**
     * Returns the weapon type.
     */
    @Override
    public int getWeaponType() {
        return this.weaponType;
    }

    /**
     * Returns the weapon level.
     */
    @Override
    public int getWeaponLevel() {
        return this.weaponLevel;
    }

}

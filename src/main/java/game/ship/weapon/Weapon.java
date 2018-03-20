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

import java.io.Serializable;

/**
 * All the weapons in the game should implment this interface.
 */
public interface Weapon extends Serializable {

    /**
     * Horizontal direction of the weapon (usually enemies fire down)
     */
    int DIRECTION_UP = -1;
    int DIRECTION_DOWN = 1;

    /**
     * Sets the owner of the weapon.
     *
     * @param owner Owning ship of the weapon.
     */
    void setOwner(Ship owner);

    /**
     * Returns the owner of the weapon.
     *
     * @return The owner of the weapon.
     */
    Ship getOwner();

    /**
     * Fire a bullet (or more) from the weapon
     *
     * @param x Vertical location to fire from
     * @param y Horizontal location to fire from
     */
    void fire(float x, float y);

    /**
     * Returns the type of the weapon (the weapon types
     * are defined in the <code>WeaponFactory</code> class.
     *
     * @return The type of the weapon
     */
    int getWeaponType();

    /**
     * Returns the level of the weapon. The more advanced is the level
     * the more lethal the weapon's shots.
     *
     * @return The level of the weapon.
     */
    int getWeaponLevel();

    /**
     * This method is called when the owning ship upgrades it's
     * weapon (received bonus). The weapon should advance to the
     * next level of add damage to the bullets.
     */
    void upgradeWeapon();

}

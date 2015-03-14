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

import java.io.Serializable;

/**
 * The <code>BulletModel</code> class is used to send a bullet
 * fired in the game details to the network player.
 */
public class BulletModel implements Serializable {

    private final Class bulletClass;
    private final int ownerId;
    private final float x, y, dx, dy;
    private final long damage;

    /**
     * Construct a new BulletModel.
     *
     * @param bulletClass Class of the bullet
     * @param ownerId     Id of the ship that fired the bullet
     * @param x           Vertical location
     * @param y           Horizontal location
     * @param dx          Vertical velocity
     * @param dy          Horizontal velocity
     * @param damage      Damage this bullet cause
     */
    public BulletModel(Class bulletClass, int ownerId, float x, float y,
                       float dx, float dy, long damage) {

        this.bulletClass = bulletClass;
        this.ownerId = ownerId;
        this.x = x;
        this.y = y;
        this.dx = dx;
        this.dy = dy;
        this.damage = damage;
    }

    public Class getBulletClass() {
        return bulletClass;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getDx() {
        return dx;
    }

    public float getDy() {
        return dy;
    }

    public long getDamage() {
        return damage;
    }
}

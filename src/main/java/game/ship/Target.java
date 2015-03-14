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

package game.ship;

import game.ship.bonus.Bonus;
import game.ship.weapon.Bullet;

/**
 * A <code>Target</code> is an object that can collide with
 * various objects in the game.
 */
public interface Target {

    /**
     * Returns true if this objects collides with the rectangle
     * represented by the point.
     *
     * @param x0 Top left x position
     * @param y0 Top left y position
     * @param x1 Botom right x position
     * @param y1 Botom right y position
     * @return True if this objects collides with the rectangle
     * represented by the point
     */
    public boolean isCollision(int x0, int y0, int x1, int y1);

    /**
     * Hit the target and cause some damage.
     *
     * @param damage Damage to cause.
     */
    public void hit(long damage);

    /**
     * Hit the target with the bullet.
     *
     * @param bullet Bullet object.
     */
    public void hit(Bullet bullet);

    /**
     * Hit the target with the bonus.
     *
     * @param bonus Bonus object.
     */
    public void hit(Bonus bonus);

}

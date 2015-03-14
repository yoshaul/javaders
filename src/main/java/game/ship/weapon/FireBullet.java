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
 * A bullet that looks like a fireball.
 */
public class FireBullet extends Bullet {

    private final static String imageName = "fireshot.gif";
    private final static long damage = 7;

    /**
     * @see Bullet#Bullet(Ship, float, float, float, float, String, long)
     */
    public FireBullet(Ship owner, float x, float y, float dx, float dy) {
        super(owner, x, y, dx, dy, imageName, damage);

    }

}

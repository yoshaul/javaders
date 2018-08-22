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

/**
 * Any movable object in the game should implement the movable
 * interface (the base Sprite class implements this interface).
 */
@FunctionalInterface
public interface Movable {

    /**
     * Update the position of the movable object according to the
     * time passed.
     *
     * @param elapsedTime Time elapsed since the last update
     *                    (in milliseconds).
     */
    void updatePosition(long elapsedTime);

}

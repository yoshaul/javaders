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

import java.io.Serializable;

/**
 * The <code>ShipState</code> class holds the state of the ship
 * at the time of this object creation. It is used to send the
 * state to the network player.
 */
public class ShipState implements Serializable {

    private final float x;
    private final float y;
    private final float dy;
    private final float dx;
    private final long armor;
    private final int state;

    /**
     * Construct a new ship state.
     *
     * @param x     Current vertical position.
     * @param y     Current horizontal position.
     * @param dx    Current ship's vertical velocity.
     * @param dy    Current ship's horizontal velocity.
     * @param armor Current ship's armor.
     * @param state Current ship's state.
     */
    public ShipState(float x, float y, float dx, float dy, long armor, int state) {
        this.x = x;
        this.y = y;
        this.dx = dx;
        this.dy = dy;
        this.armor = armor;
        this.state = state;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getDy() {
        return dy;
    }

    public float getDx() {
        return dx;
    }

    public long getArmor() {
        return armor;
    }

    public int getState() {
        return state;
    }
}

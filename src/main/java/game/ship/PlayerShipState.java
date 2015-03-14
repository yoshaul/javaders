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
 * The player ship state adds some more data needed for a
 * player ship.
 */
public class PlayerShipState extends ShipState {

    public long score;    // The player's score

    /**
     * Construct a new ship state.
     *
     * @param x     Current vertical position.
     * @param y     Current horizontal position.
     * @param dx    Current ship's vertical velocity.
     * @param dy    Current ship's horizontal velocity.
     * @param armor Current ship's armor.
     * @param state Current ship's state.
     * @param score The player's score
     */
    public PlayerShipState(float x, float y, float dx, float dy, long armor,
                           int state, long score) {
        super(x, y, dx, dy, armor, state);
        this.score = score;

    }

}

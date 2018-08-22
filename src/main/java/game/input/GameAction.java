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

package game.input;

/**
 * The <code>GameAction</code> class represents a game
 * action key, like firing, moving left, etc.
 * It holds the state of the game action (e.g., pressed/released).
 */
public class GameAction {

    private static final int STATE_RELEASED = 0;
    private static final int STATE_PRESSED = 1;

    private int state;    // The game action state

    /**
     * No parameters constructor.
     */
    public GameAction() {
        reset();
    }

    /**
     * Press the game action - mark as pressed.
     */
    public void press() {
        state = STATE_PRESSED;
    }

    /**
     * Release the game action - mark as released.
     */
    public void release() {
        state = STATE_RELEASED;
    }

    /**
     * Returns true if this <code>GameAction</code> is pressed.
     *
     * @return True if this <code>GameAction</code> is pressed.
     */
    public boolean isPressed() {
        return (state == STATE_PRESSED);
    }

    /**
     * Resets this object state. Default is released.
     */
    public void reset() {
        state = STATE_RELEASED;
    }

}

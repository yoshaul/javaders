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

package game.gamestate;

import game.GameLoop;
import game.network.packet.Packet;

/**
 * The <code>GameState</code> interface represents a state in the game,
 * like loading, running etc.
 * Each state encapsulate it's own logic and rendering. The game loop
 * calls the current game state gatherInput, update and render methods
 * in a loop. The current game state marks the next game state when
 * it's finished.
 */
public interface GameState {

    public static final int GAME_STATE_RUNNING = 1;
    public static final int GAME_STATE_LOADING = 2;
    public static final int GAME_STATE_HIGH_SCORE = 3;

    /**
     * This method is called once after the <code>GameState</code>
     * object is instantiated. Here all the one time initialization
     * procedures should be performed.
     */
    public void init();

    /**
     * This method is called after a game state changes.
     * Performs any initializations before starting the state.
     */
    public void start();

    /**
     * Gather relevant input (from the keyboard and network)
     *
     * @param gameLoop    Reference to the <code>GameLoop</code> object
     * @param elapsedTime Time elapsed in milliseconds since the last call
     *                    to this method.
     */
    public void gatherInput(GameLoop gameLoop, long elapsedTime);

    /**
     * Updates the objects and checks for state changes.
     *
     * @param gameLoop    Reference to the <code>GameLoop</code> object
     * @param elapsedTime Time elapsed in milliseconds since the last call
     *                    to this method.
     */
    public void update(GameLoop gameLoop, long elapsedTime);

    /**
     * Render on the screen
     *
     * @param gameLoop Reference to the <code>GameLoop</code> object
     */
    public void render(GameLoop gameLoop);

    /**
     * Handles a packet received from the network player. Usually passes
     * the packet to the appropriate game object.
     *
     * @param packet A packet
     */
    public void handlePacket(Packet packet);

    /**
     * Returns true if the current game state is finished. If so
     * the game loop should switch to the next game state.
     *
     * @return True if the current state is finished
     */
    public boolean isFinished();

    /**
     * Returns the next game state after the current game state is finished
     *
     * @return Next game state
     */
    public int getNextGameState();

    /**
     * Returns the id of the game state
     *
     * @return Id of the game state
     */
    public int getGameStateId();


}

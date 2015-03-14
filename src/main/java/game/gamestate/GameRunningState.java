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

import game.*;
import game.input.InputManager;
import game.network.client.GameNetworkManager;
import game.network.packet.Packet;

import java.awt.*;

/**
 * The <code>GameRunningState</code> is the central game state. In this
 * state the game is in running mode, meaning the player is able to play
 * and game logic is running through the various manager objects.
 */
public class GameRunningState implements GameState {

    private final static int INTERNAL_STATE_NORMAL = 1;
    private final static int INTERNAL_STATE_LEVEL_CLEARED = 2;

    private int nextGameState;
    private ScreenManager screenManager;
    private GUIManager guiManager;
    private InputManager inputManager;
    private GameNetworkManager gameNetworkManager;
    private StaticObjectsManager staticObjectsManager;
    private EnemyShipsManager enemyShipsManager;
    private PlayerManager playerManager;
    private boolean networkGame;
    private boolean finished;
    private int internalState;
    private long timeInState;

    /**
     * Construct the game state.
     *
     * @param gameLoop Reference to the game loop.
     */
    public GameRunningState(GameLoop gameLoop) {
        this.screenManager = gameLoop.getScreenManager();
        this.guiManager = gameLoop.getGUIManager();
        this.inputManager = gameLoop.getInputManager();
        this.gameNetworkManager = gameLoop.getGameNetworkManager();
        this.staticObjectsManager = gameLoop.getStaticObjectsManager();
        this.enemyShipsManager = gameLoop.getEnemyShipsManager();
        this.playerManager = gameLoop.getPlayerManager();
        this.networkGame = gameLoop.isNetworkGame();
    }

    /**
     * Initialize state.
     */
    @Override
    public void init() {
        // Nothing to initialize
    }

    /**
     * This method is called once when this state is set to be
     * the active state.
     *
     * @see game.gamestate.GameState init method
     */
    @Override
    public void start() {
        finished = false;
        screenManager.showCursor(false);
        setInternalState(INTERNAL_STATE_NORMAL);
    }

    /**
     * Gather input from the player and from the network.
     */
    @Override
    public void gatherInput(GameLoop gameLoop, long elapsedTime) {

        inputManager.gatherInput();
        if (!inputManager.isPaused()) {
            playerManager.gatherInput();
        }
        if (networkGame) {
            gameNetworkManager.gatherInput(this);
        }

    }

    /**
     * Update the game state.
     * Most of the game logic starts from here. We call the ships
     * managers update methods.
     */
    @Override
    public void update(GameLoop gameLoop, long elapsedTime) {
        if (!inputManager.isPaused()) {
            timeInState += elapsedTime;
            enemyShipsManager.update(elapsedTime);
            playerManager.update(elapsedTime);
        }

        if (!(internalState == INTERNAL_STATE_LEVEL_CLEARED) &&
                enemyShipsManager.isLevelFinished()) {
            setInternalState(INTERNAL_STATE_LEVEL_CLEARED);
        } else if (playerManager.isGameOver()) {
            finished = true;
            inputManager.setPaused(true);

            nextGameState = GAME_STATE_HIGH_SCORE;

        } else if (internalState == INTERNAL_STATE_LEVEL_CLEARED &&
                timeInState > 2000) {
            finished = true;
            nextGameState = GAME_STATE_LOADING;
        }

    }

    /**
     * Render the game state.
     */
    @Override
    public void render(GameLoop gameLoop) {
        Graphics g = screenManager.getGraphics();

        staticObjectsManager.render(g);
        enemyShipsManager.render(g);
        playerManager.render(g);
        guiManager.render(g);

        g.dispose();

        screenManager.show();

    }

    /**
     * Handle network packet. This callback method is called
     * from the <code>GameNetworkManager</code> in the input
     * gathering part.
     */
    @Override
    public void handlePacket(Packet packet) {
        enemyShipsManager.handlePacket(packet);
        playerManager.handlePacket(packet);
    }

    /**
     * Returns true if the current game state is finished.
     *
     * @return True if the current state is finished
     */
    @Override
    public boolean isFinished() {
        return finished;
    }

    /**
     * Returns the next game state after the current game state is finished.
     *
     * @return Next game state.
     */
    @Override
    public int getNextGameState() {
        return nextGameState;
    }

    /**
     * Returns the id of this game state
     *
     * @return Id of this game state
     */
    @Override
    public int getGameStateId() {
        return GameState.GAME_STATE_RUNNING;
    }

    /**
     * Sets the internal state to the new state and sets the time
     * in state to 0.
     *
     * @param state New internal state
     */
    private void setInternalState(int state) {
        this.internalState = state;
        this.timeInState = 0;
    }

}

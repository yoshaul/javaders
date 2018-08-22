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

import game.GUIManager;
import game.GameLoop;
import game.gui.AddHighScoreDialog;
import game.gui.PostHighScoreDialog;
import game.highscore.HighScore;
import game.highscore.HighScoresManager;
import game.network.packet.Packet;

import java.awt.*;

/**
 * The <code>AddHighScoreState</code> is the last state before the control
 * returns to the game menu. In this state we check if the player scores
 * another high score and ask the player if she wants to post her score.
 */
public class AddHighScoreState implements GameState {

    private static final int INTERNAL_STATE_ADD_HIGH_SCORE = 1;
    private static final int INTERNAL_STATE_POST_SCORE = 2;

    private GameLoop gameLoop;
    private AddHighScoreDialog addHighScoreDialog;
    private PostHighScoreDialog postScoreDialog;
    private HighScoresManager highScoresManager;
    private GUIManager guiManager;
    private String playerName;
    private long timeInState;
    private int nextGameState;
    private int internalState;
    private long playerScore;
    private int level;
    private boolean finished;

    /**
     * Construct the game state.
     *
     * @param gameLoop Reference to the game loop.
     */
    public AddHighScoreState(GameLoop gameLoop) {
        this.gameLoop = gameLoop;
        this.highScoresManager = gameLoop.getHighScoresManager();
        this.guiManager = gameLoop.getGUIManager();
    }

    /**
     * Initialize state; create the state dialogs.
     */
    @Override
    public void init() {

        addHighScoreDialog = new AddHighScoreDialog(gameLoop, this,
                highScoresManager);

        guiManager.addDialog(addHighScoreDialog);

        postScoreDialog = new PostHighScoreDialog(gameLoop,
                highScoresManager);

        guiManager.addDialog(postScoreDialog);
    }

    /**
     * This method is called once when this state is set to be
     * the active state.
     *
     * @see game.gamestate.GameState init method
     */
    @Override
    public void start() {
        timeInState = 0;
        gameLoop.getScreenManager().showCursor(true);
        finished = false;

        playerScore =
                gameLoop.getPlayerManager().getLocalPlayerShip().getScore();

        level = gameLoop.getLevelsManager().getCurrentLevel();

        if (highScoresManager.isHighScore(playerScore, level)) {

            internalState = INTERNAL_STATE_ADD_HIGH_SCORE;

            popAddHighScoreDialog(playerScore, level);
        } else {
            internalState = INTERNAL_STATE_POST_SCORE;
            popPostScoreDialog(new HighScore(playerName, playerScore, level));
        }
    }

    /**
     * This state is gathering input with Swing components.
     */
    @Override
    public void gatherInput(GameLoop gameLoop, long elapsedTime) {
        // The input is gathered by swing TextField
    }

    /**
     * Update the state. If this state is finished the game is
     * over and we exit the game loop.
     */
    @Override
    public void update(GameLoop gameLoop, long elapsedTime) {

        timeInState += elapsedTime;

        if (internalState == INTERNAL_STATE_ADD_HIGH_SCORE &&
                addHighScoreDialog.isFinished()) {

            if (playerName != null) {
                internalState = INTERNAL_STATE_POST_SCORE;
                popPostScoreDialog(
                        new HighScore(playerName, playerScore, level));
            } else {
                gameLoop.getInputManager().setQuit(true);
            }
        }

        if (internalState == INTERNAL_STATE_POST_SCORE &&
                postScoreDialog.isFinished()) {

            gameLoop.getInputManager().setQuit(true);

        }

    }

    /**
     * Render the state data.
     */
    @Override
    public void render(GameLoop gameLoop) {

        Graphics g = gameLoop.getScreenManager().getGraphics();

        gameLoop.getStaticObjectsManager().render(g);
        gameLoop.getEnemyShipsManager().render(g);
        gameLoop.getGUIManager().render(g);

        g.dispose();

        gameLoop.getScreenManager().show();

    }

    /**
     * No packets are handled in this state.
     */
    @Override
    public void handlePacket(Packet packet) {
        // No packets to handle in this state
    }

    /**
     * Returns true if this level is finished.
     */
    @Override
    public boolean isFinished() {
        return finished;
    }

    /**
     * Returns the next game state after this state is finished.
     */
    @Override
    public int getNextGameState() {
        return nextGameState;
    }

    /**
     * Returns this state id.
     */
    @Override
    public int getGameStateId() {
        return GameState.GAME_STATE_HIGH_SCORE;
    }

    /**
     * Callback method from the <code>AddHighScoreDialog</code>
     * to store the player name for the next dialog.
     *
     * @param playerName Name of the player.
     */
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    /**
     * Show the dialog to add high score.
     *
     * @param score New score to add.
     * @param level Level reached by the player.
     */
    private void popAddHighScoreDialog(long score, int level) {
        addHighScoreDialog.addHighScore(score, level);
    }

    /**
     * Show the dialog for posting a high score.
     *
     * @param score HighScore object to send to the server.
     */
    private void popPostScoreDialog(HighScore score) {
        postScoreDialog.popPostHighScore(score);
    }

}

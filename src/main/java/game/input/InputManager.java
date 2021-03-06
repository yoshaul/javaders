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

import game.GameLoop;
import game.gui.QuitDialog;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;

/**
 * The input manager handles the player input from the
 * keyboard while the game is running.
 */
public class InputManager implements KeyListener {

    private final GameLoop gameLoop;
    private final QuitDialog quitDialog;

    // Game actions
    private GameAction exit;
    private GameAction pause;
    private GameAction moveLeft;
    private GameAction moveRight;
    private GameAction moveUp;
    private GameAction moveDown;
    private GameAction fireBullet;
    private GameAction fireMissile;

    private boolean paused = false;
    private boolean quit = false;

    /**
     * Virtual keys to GameAction map
     */
    private Map<Integer, GameAction> keyCodeToGameAction;

    /**
     * Construct the input manager. Create the game actions.
     *
     * @param gameLoop Reference to the GameLoop
     */
    public InputManager(GameLoop gameLoop) {

        this.gameLoop = gameLoop;

        this.quitDialog = new QuitDialog(gameLoop.getScreenManager(), this);
        gameLoop.getGUIManager().addDialog(quitDialog);

        createGameActions();

    }

    /**
     * Gather game wide input.
     */
    public void gatherInput() {
        gatherSpecialInput();
    }

    /**
     * Gather special system input from the player (like pressing on
     * exit or pause game)
     */
    private void gatherSpecialInput() {

        if (pause.isPressed()) {
            setPaused(!paused);
        }

        if (exit.isPressed()) {
            exit.reset();    // Reset since the focus will pass to the dialog
            setPaused(true);
            gameLoop.getScreenManager().showCursor(true);
            quitDialog.setVisible(true);
            quitDialog.requestFocus();
        }
    }

    /**
     * Returns true if the user quitted the game.
     *
     * @return True if user quitted the game.
     */
    public boolean isQuit() {
        return quit;
    }

    /**
     * Set the quit state.
     *
     * @param quit True if quit false otherwise.
     */
    public void setQuit(boolean quit) {
        // Return the focus to the game frame
        gameLoop.getScreenManager().getFullScreenWindow().requestFocus();
        this.quit = quit;
        setPaused(false);
    }

    /**
     * Returns true if the game is paused.
     *
     * @return True if the game is paused.
     */
    public boolean isPaused() {
        return paused;
    }

    /**
     * Set the paused state of the game. In networked game the game
     * cannot be paused.
     *
     * @param paused True to pause, false to continue.
     */
    public void setPaused(boolean paused) {
        // Pause the game if not network game
        if (!gameLoop.isNetworkGame()) {
            this.paused = paused;
            gameLoop.getScreenManager().showCursor(paused);
            pause.reset();
        }
    }

    /* Implement KeyListener interface */

    /**
     * Find the GameAction for the key and if found call
     * its <code>press</code> method.
     */
    @Override
    public void keyPressed(KeyEvent event) {
        int keyCode = event.getKeyCode();
        GameAction gameAction = keyCodeToGameAction.get(keyCode);

        if (gameAction != null) {
            gameAction.press();
        }

        event.consume();

    }

    /**
     * Find the GameAction for the key and if found call
     * its <code>release</code> method.
     */
    @Override
    public void keyReleased(KeyEvent event) {

        int keyCode = event.getKeyCode();
        GameAction gameAction = keyCodeToGameAction.get(keyCode);

        if (gameAction != null) {
            gameAction.release();
        }

        event.consume();

    }

    /**
     * Ignore the key typed events
     */
    @Override
    public void keyTyped(KeyEvent event) {
        event.consume();
    }

    /**
     * Creates and inserts to the map the game actions.
     */
    private void createGameActions() {

        exit = new GameAction();
        pause = new GameAction();
        moveLeft = new GameAction();
        moveRight = new GameAction();
        moveUp = new GameAction();
        moveDown = new GameAction();
        fireBullet = new GameAction();
        fireMissile = new GameAction();

        keyCodeToGameAction = new HashMap<>();

        keyCodeToGameAction.put(KeyEvent.VK_ESCAPE, exit);
        keyCodeToGameAction.put(KeyEvent.VK_P, pause);
        keyCodeToGameAction.put(KeyEvent.VK_LEFT, moveLeft);
        keyCodeToGameAction.put(KeyEvent.VK_RIGHT, moveRight);
        keyCodeToGameAction.put(KeyEvent.VK_UP, moveUp);
        keyCodeToGameAction.put(KeyEvent.VK_DOWN, moveDown);
        keyCodeToGameAction.put(KeyEvent.VK_SPACE, fireBullet);
        keyCodeToGameAction.put(KeyEvent.VK_CONTROL, fireMissile);

    }

    public GameAction getMoveLeft() {
        return moveLeft;
    }

    public GameAction getMoveRight() {
        return moveRight;
    }

    public GameAction getMoveUp() {
        return moveUp;
    }

    public GameAction getMoveDown() {
        return moveDown;
    }

    public GameAction getFireBullet() {
        return fireBullet;
    }
}

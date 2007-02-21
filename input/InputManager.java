
package game.input;

import game.GameLoop;
import game.gui.QuitD;
import game.gui.QuitDialog;
import game.ship.*;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Map;
import java.util.HashMap;

import javax.swing.JLayeredPane;


public class InputManager implements KeyListener, MouseListener {

    private GameLoop gameLoop;
    
    private QuitD quitDialog;
    
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
    
    private Map keyCodeToGameAction;
    
    private Ship player;
    
    public InputManager(GameLoop gameLoop, Ship player) {
        
        this.gameLoop = gameLoop;
        
        this.player = player;
        
        this.quitDialog = new QuitD(gameLoop, false, this);
        
        createGameActions();
        
        gameLoop.getScreenManager().getFullScreenWindow().
        	getLayeredPane().add(quitDialog, JLayeredPane.MODAL_LAYER);
        
    }
    
    public void gatherInput(long elapsedTime) {
        
        gatherSpecialInput();
        
        if (paused) {
            return;
        }
        
        float velocityX = 0;
        float velocityY = 0;
        float maxVelocity = 0.3f;
        
        if (moveLeft.isPressed()) {
            velocityX -= maxVelocity;
        }
        if (moveRight.isPressed()) {
            velocityX += maxVelocity;
        }
        if (moveUp.isPressed()) {
            velocityY -= maxVelocity;
        }
        if (moveDown.isPressed()) {
            velocityY += maxVelocity;
        }
        
        player.setDx(velocityX);
        player.setDy(velocityY);
        
        if (fireBullet.isPressed()) {
            player.shoot();
        }
        
	}
    
    private void gatherSpecialInput() {

        if (pause.isPressed()) {
            setPaused(!paused);
        }
        
        if (exit.isPressed()) {
            setPaused(true);
            exit.reset();
            quitDialog.setVisible(true);
            quitDialog.requestFocus();
        }
    }
    
    public boolean isExited() {
//        return exit.isPressed();
        return quit;
    }
    
    public void setQuit(boolean quit) {
        // Return the focus to the game frame
        gameLoop.getScreenManager().getFullScreenWindow().requestFocus();
        this.quit = quit;
        setPaused(false);
    }
    
    public boolean isPaused() {
        return paused;
    }
    
    public void setPaused(boolean paused) {
        this.paused = paused;
        pause.reset();
    }
    
    /* Implement KeyListener interface */
    
    public void keyPressed(KeyEvent event) {
        
        int keyCode = event.getKeyCode();
        GameAction gameAction = (GameAction) 
            keyCodeToGameAction.get(new Integer(keyCode));
        
        if (gameAction != null) {
            gameAction.press();
        }
        
        event.consume();
        
    }
    
    public void keyReleased(KeyEvent event) {
        
        int keyCode = event.getKeyCode();
        GameAction gameAction = (GameAction) 
            keyCodeToGameAction.get(new Integer(keyCode));
        
        if (gameAction != null) {
            gameAction.release();
        }
        
        event.consume();
        
    }

    public void keyTyped(KeyEvent event) {
        
        event.consume();
        
    }
    
    
    /* Implement MouseListener interface */
    
    public void mouseClicked(MouseEvent event) {
        
    }
    
    public void mousePressed(MouseEvent event) {
        
    }
    
    public void mouseReleased(MouseEvent event) {
        
    }

    public void mouseEntered(MouseEvent event) { }
    
    public void mouseExited(MouseEvent event) { }
    
    
    private void createGameActions() {
        
        exit = new GameAction();
        pause = new GameAction();
        moveLeft = new GameAction();
        moveRight = new GameAction();
        moveUp = new GameAction();
        moveDown = new GameAction();
        fireBullet = new GameAction();
        fireMissile = new GameAction();

        keyCodeToGameAction = new HashMap();
        
        keyCodeToGameAction.put(
                new Integer(KeyEvent.VK_ESCAPE), exit);
        keyCodeToGameAction.put(
                new Integer(KeyEvent.VK_P), pause);
        keyCodeToGameAction.put(
                new Integer(KeyEvent.VK_LEFT), moveLeft);
        keyCodeToGameAction.put(
                new Integer(KeyEvent.VK_RIGHT), moveRight);
        keyCodeToGameAction.put(
                new Integer(KeyEvent.VK_UP), moveUp);
        keyCodeToGameAction.put(
                new Integer(KeyEvent.VK_DOWN), moveDown);
        keyCodeToGameAction.put(
                new Integer(KeyEvent.VK_SPACE), fireBullet);
        keyCodeToGameAction.put(
                new Integer(KeyEvent.VK_CONTROL), fireMissile);
        
    }
}

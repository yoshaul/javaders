
package game.input;

import game.ship.*;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Map;
import java.util.HashMap;


public class InputManager implements KeyListener, MouseListener {

    private GameAction exit;
    private GameAction pause;
    private GameAction moveLeft;
    private GameAction moveRight;
    private GameAction moveUp;
    private GameAction moveDown;
    private GameAction fireBullet;
    private GameAction fireMissile;
    
    private boolean paused = false;
    
    private Map keyCodeToGameAction;
    
    private Ship player;
    
    public InputManager(Ship player) {
        
        this.player = player;
        
        createGameActions();
        
    }
    
    public void gatherInput(long elapsedTime) {
        
        gatherSpecialInput();
        
        if (paused) {
            return;
        }
        
        double velocityX = 0;
        double velocityY = 0;
        double maxVelocity = 0.3;
        
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
            fireBullet.release();
        }
        
	}
    
    private void gatherSpecialInput() {

        if (pause.isPressed()) {
            paused = !paused;
        }
    }
    
    public boolean isExited() {
        return exit.isPressed();
    }
    
    public boolean isPaused() {
        return paused;
    }
    
    public void setPaused(boolean paused) {
        this.paused = paused;
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

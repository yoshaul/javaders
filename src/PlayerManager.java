
package game;

import java.awt.*;
import java.util.*;

import game.input.InputManager;
import game.network.client.GameNetworkManager;
import game.network.packet.*;
import game.ship.*;
import game.ship.bonus.Bonus;
import game.ship.weapon.*;

/**
 * The <code>PlayerManager</code> manage the local player ship and the
 * network player ship (if one exists). 
 * It also implements the <code>ShipContainer</code> interface to allow 
 * the ships to communicate with it.
 */
public class PlayerManager implements ShipContainer, PacketHandler {

    private final int handlerID = GameConstants.PLAYER_MANAGER_ID;
    
    private GameLoop gameLoop;
    private InputManager inputManager;
    
    private PlayerShip player1Ship, player2Ship, localPlayer, networkPlayer;

    /** Collection of active shots fired by the ships */
    private Collection shots;
    /** Collection of targets for the ships (i.e., enemy ships) */
    private Collection targets;
    /** True if the player ship (and network ship) are destroyed */
    private boolean gameOver;
    
    /**
     * Construct the PlayerManager. Creates the player ship(s).
     * @param gameLoop	Reference to the game loop
     */
    public PlayerManager(GameLoop gameLoop) {
        
        this.gameLoop = gameLoop;
        this.inputManager = gameLoop.getInputManager();
        this.shots = new ArrayList();
        this.targets = new ArrayList();
        this.gameOver = false;
        
        Dimension screen = gameLoop.getScreenManager().getScreenDimension();
        
        int x = screen.width / 2;
        int y = screen.height - 60;
        
        player1Ship = new PlayerShip(GameConstants.PLAYER1_ID, 
                ShipProperties.PLAYER_SHIP_TYPE_1, x, y, 
                ShipProperties.getShipProperties(
                        ShipProperties.PLAYER_SHIP_TYPE_1));

        player1Ship.setShipContainer(this);
        
        if (gameLoop.isNetworkGame()) {

            x = screen.width / 3;
            player1Ship.setX(x);	// Change player 1 location
            
            player2Ship = new PlayerShip(GameConstants.PLAYER2_ID, 
                    ShipProperties.PLAYER_SHIP_TYPE_2, 2*x, y, 
                    ShipProperties.getShipProperties(
                            ShipProperties.PLAYER_SHIP_TYPE_2));
            
            player2Ship.setShipContainer(this);
        }
        
        // Set the local player and network player references
        if (gameLoop.isNetworkGame()) {
            if (gameLoop.isController()) {
                localPlayer = player1Ship;
                networkPlayer = player2Ship;
            }
            else {
                localPlayer = player2Ship;
                networkPlayer = player1Ship;
            }
        }
        else {
            localPlayer = player1Ship;
        }
        
    }
    
    /**
     * Get ready for a new level. Clear reminders from previous
     * level and set the enemy ships.
     * @param enemyShips	Collection of enemy ships to be used as targets
     */
    public void newLevel(Collection enemyShips) {
        targets.clear();
        shots.clear();
        addTarget(enemyShips);
    }
    
    /**
     * Adds a new target to the targets collection.
     * @param target	New target to add.
     */
    public void addTarget(Target target) {
        this.targets.add(target);
    }
    
    /**
     * Adds a collection of targets.
     * @param targets	Collection of Target objects.
     */
    public void addTarget(Collection targets) {
        this.targets.addAll(targets);
    }
    
    /**
     * Returns the local player ship.
     * @return	Local player ship
     */
    public PlayerShip getLocalPlayerShip() {
        return localPlayer;
    }
    
    /**
     * Returns the network player ship.
     * @return	Network player ship
     */
    public PlayerShip getNetworkPlayerShip() {
        return networkPlayer;
    }
    
    /**
     * Returns player one ship (the left player)
     * @return	Player 1 ship
     */
    public PlayerShip getPlayer1Ship() {
        return player1Ship;
    }
    
    /**
     * Returns player 2 ship. Null if there is no player 2 ship.
     * @return	Player 2 ship.
     */
    public PlayerShip getPlayer2Ship() {
        return player2Ship;
    }
    
    /**
     * Gather the player input (the input collected by the 
     * <code>InputManager</code> class).
     */
    public void gatherInput() {
        
        float oldDx = localPlayer.getDx();
        float oldDy = localPlayer.getDy();
        
        float velocityX = 0;
        float velocityY = 0;
        
        if (inputManager.moveLeft.isPressed()) {
            velocityX -= localPlayer.getMaxDX();
        }
        if (inputManager.moveRight.isPressed()) {
            velocityX += localPlayer.getMaxDX();
        }
        if (inputManager.moveUp.isPressed()) {
            velocityY -= localPlayer.getMaxDY();
        }
        if (inputManager.moveDown.isPressed()) {
            velocityY += localPlayer.getMaxDY();
        }
        
        if (oldDx != velocityX || oldDy != velocityY) {
            localPlayer.setDx(velocityX);
            localPlayer.setDy(velocityY);
            if (gameLoop.isNetworkGame()) {
                // Force the local ship to send packet
                localPlayer.forcePacket();
            }
        }
        
        if (inputManager.fireBullet.isPressed()) {
            localPlayer.shoot();
        }
    }
    
    /**
     * Updates the state of all the managed objects.
     * @param elapsedTime	Time elapsed since last call to this method 
     * in milliseconds.
     */
    public void update(long elapsedTime) {

        player1Ship.update(elapsedTime);
        if (player1Ship.isActive()) {
            fixPlace(player1Ship);
        }
        
        if (player2Ship != null) {
            player2Ship.update(elapsedTime);
            if (player2Ship.isActive()) {
                fixPlace(player2Ship);
            }
        }
        
        // Update shots
        Iterator shotsItr = shots.iterator();
        while (shotsItr.hasNext()) {
            Sprite shot = (Sprite) shotsItr.next();
            shot.updatePosition(elapsedTime);
            if (isOutOfScreen(shot)) {
                shotsItr.remove();
            }
        }

        ////////////////////////
        // Process Collisions //
        ////////////////////////
        

        // Process ship-to-ship collisions
        player1Ship.processCollisions(targets);
        if (player2Ship != null) {
            player2Ship.processCollisions(targets);
        }
        
        // Process shots to enemy ship collisions
        shotsItr = shots.iterator();
        while (shotsItr.hasNext()) {
            Bullet shot = (Bullet) shotsItr.next();
            shot.processCollisions(targets);
            if (shot.isHit()) {
                shotsItr.remove();
            }
        }
        
        // Check if the game is over
        if (gameLoop.isNetworkGame()) {
            // Check if game over
            if (getLocalPlayerShip().isDestroyed() &&
                    getNetworkPlayerShip().isDestroyed()) {
                gameOver = true;
            }
            
        } 
        else if (getLocalPlayerShip().isDestroyed()) {
            gameOver = true;
        }
        
    }
    
    /**
     * Renders all relevant objects and data (ships, shots, etc.)
     */
    public void render(Graphics g) {
        player1Ship.render(g);
        
        if (player2Ship != null) {
            player2Ship.render(g);
        }
        
        // Render shots
        Iterator shotsItr = shots.iterator();
        while (shotsItr.hasNext()) {
            Sprite shot = (Sprite) shotsItr.next();
            shot.render(g);
        }
    }
    
    /**
     * Fix the ship location and velocity if it is trying to
     * exit the screen bounds.
     * @param ship	Ship to fix its place.
     */
    private void fixPlace(Ship ship) {
        
        Dimension screenDimention = 
            gameLoop.getScreenManager().getScreenDimension();
        
        Insets insets = 
            gameLoop.getScreenManager().getScreenInsets();
        
        // If the ship exits the screen and still in the wrong 
        // direction, change its velocity so it will get back
        if (ship.getX() < insets.left && ship.getDx() < 0) {
            ship.setX(insets.left);
            ship.setDx(0);
        }
        if (ship.getX()+ ship.getWidth() > 
                screenDimention.width - insets.right && 
                ship.getDx() > 0) {
            ship.setX(screenDimention.width - ship.getWidth() - insets.right);
            ship.setDx(0);
        }
        if (ship.getY() < insets.top && ship.getDy() < 0) {
            ship.setY(insets.top);
            ship.setDy(0);
        }
        if (ship.getY() + ship.getHeight() > 
                screenDimention.height - insets.bottom && 
                ship.getDy() > 0) {
            ship.setY(screenDimention.height - 
                    ship.getHeight() - insets.bottom);
            ship.setDy(0);
        }
    }
    
    /**
     * Return true if the sprite is off the screen bounds.
     * @param sprite	Sptite to test.
     * @return	True if the sprite is off the screen bounds
     */
    private boolean isOutOfScreen(Sprite sprite) {
        Dimension screenDimension = 
            gameLoop.getScreenManager().getScreenDimension();
        
        return sprite.getX() + sprite.getWidth() < 0 ||
        	sprite.getX() > screenDimension.width ||
        	sprite.getY() + sprite.getHeight() < 0 ||
        	sprite.getY() > screenDimension.height;
    }
    
    /**
     * Inherited from <code>ShipContainer</code> interface.
     * Not in use for the <code>PlayerManager</code>
     */
    public void addShip(Ship ship) {}

    /**
     * Adds shot to the shots collection.
     * @param shot Shot to add. 
     */
    public void addShot(Bullet shot) {
        shots.add(shot);
    }
    
    /**
     * Returns true if this macine is the controller.
     */
    public boolean isController() {
        return gameLoop.isController();
    }
    
    /**
     * Return true if this is a network game.
     */
    public boolean isNetworkGame() {
        return gameLoop.isNetworkGame();
    }
    
    /**
     * Returns the game network manager. Null if this is not
     * a network game.
     */
    public GameNetworkManager getNetworkManager() {
        return gameLoop.getGameNetworkManager();
    }
    
    /**
     * Returns the network handler id of this object.
     */
    public int getHandlerId() {
        return this.handlerID;
    }
    
    /**
     * Handles incoming packets. If the packet should be handle by 
     * the maneger than handle it otherwise if the network player
     * ship should to handle it.
     * @param packet Packet to handle
     */
    public void handlePacket(Packet packet) {
        
        if (packet instanceof BulletPacket) {
            BulletPacket bulletPacket = (BulletPacket)packet;
            
            BulletModel model = bulletPacket.getBulletModel();
            
            if (packet.handlerId == getNetworkPlayerShip().getHandlerId()) {
                
                Bullet bullet = WeaponFactory.getBullet(model, 
                        getNetworkPlayerShip());
           
                addShot(bullet);                
            }
            
            packet.setConsumed(true);
            
        }
        else if (packet instanceof PlayerQuitPacket) {
            // Network player quit the game. Set this machine as the
            // controller and send no more packets back to the user.
            // Also destroy the network player's ship.
            gameLoop.setController(true);
            gameLoop.setNetworkGame(false);
            
            getNetworkPlayerShip().destroy();
            
            packet.setConsumed(true);
            
        }
        else {
            // Let the ship handle it
            if (packet.handlerId == networkPlayer.getHandlerId()) {
                getNetworkPlayerShip().handlePacket(packet);
            }
        }
        
    }
    
    /**
     * Currently this object doesn't creates it's own packets
     */
    public void createPacket(GameNetworkManager netManager) {
        
    }
    
    /**
     * Returns the network handler id of this object.
     */
    public boolean isGameOver() {
        return gameOver;
    }
    
    /**
     * Player ship doesn't release bonuses
     */
    public void addBonus(Bonus bonus) {
        // Player ship doesn't release bonuses
    }
    
}
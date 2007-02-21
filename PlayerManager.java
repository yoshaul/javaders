package game;

import java.awt.Dimension;
import java.awt.Graphics;
import java.util.*;

import game.network.GameNetworkManager;
import game.network.packet.*;
import game.ship.*;
import game.ship.bonus.Bonus;
import game.ship.weapon.*;

import javax.swing.ImageIcon;

public class PlayerManager implements ShipContainer, PacketHandler {

    private final int handlerID = GameConstants.PLAYER_MANAGER_ID;
    
    private GameLoop gameLoop;
    
    private PlayerShip player1Ship, player2Ship;
    private PlayerShip localPlayer, networkPlayer;
    private Collection shots;
    private Collection targets;
    private boolean gameOver;
    
    public PlayerManager(GameLoop gameLoop) {
        
        this.gameLoop = gameLoop;
        this.shots = new ArrayList();
        
        player1Ship = new PlayerShip(GameConstants.PLAYER1_ID, 
                ShipProperties.PLAYER_SHIP_TYPE,
                200, 540, 0, 0, 
                new ImageIcon(GameConstants.IMAGES_DIR+"player01.png").getImage(), 
                new LaserCanon(Weapon.DIRECTION_UP, 2)
        );

        player1Ship.setShipContainer(this);
        
        if (gameLoop.isNetworkGame()) {
            player2Ship = new PlayerShip(GameConstants.PLAYER2_ID,
                    ShipProperties.PLAYER_SHIP_TYPE,
                    400, 540, 0, 0,
                    new ImageIcon(GameConstants.IMAGES_DIR+"Bajoran.gif").getImage(), 
                    new LaserCanon(Weapon.DIRECTION_UP, 2)
            );
            
            player2Ship.setShipContainer(this);
        }
        
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
        
        this.targets = new ArrayList();
        this.gameOver = false;
        
    }
    
    public void newLevel(Collection enemyShips) {
        targets.clear();
        shots.clear();
        addTarget(enemyShips);
    }
    
    public void addTarget(Target target) {
        this.targets.add(target);
    }
    
    public void addTarget(Collection targets) {
        this.targets.addAll(targets);
    }
    
    public PlayerShip getLocalPlayerShip() {
        return localPlayer;
    }
    
    public PlayerShip getNetworkPlayerShip() {
        return networkPlayer;
    }
    
    public PlayerShip getPlayer1Ship() {
        return player1Ship;
    }
    
    public PlayerShip getPlayer2Ship() {
        return player2Ship;
    }
    
    public void update(long elapsedTime) {
        
        Dimension screenDimention = 
            gameLoop.getScreenManager().getScreenDimension();
        
        
        player1Ship.update(elapsedTime);
        fixPlace(player1Ship);
        
        if (player2Ship != null) {
            player2Ship.update(elapsedTime);
            fixPlace(player2Ship);
        }
        
        // Update shots
        Iterator shotsItr = shots.iterator();
        while (shotsItr.hasNext()) {
            Sprite shot = (Sprite) shotsItr.next();
            shot.updatePosition(elapsedTime);
            
            /** TODO: find better algorithm to handle non
             * cubic sprites
             */
            if (shot.getX() + shot.getWidth() < 0 ||
                    shot.getX() > screenDimention.width ||
                    shot.getY() + shot.getHeight() < 0 ||
                    shot.getY() > screenDimention.height) {
                
                shotsItr.remove();
                
            }
            
        }
        
        // Update collisions
        player1Ship.processCollisions(targets);
        if (player2Ship != null) {
            player2Ship.processCollisions(targets);
        }
        
        shotsItr = shots.iterator();
        while (shotsItr.hasNext()) {
            Bullet shot = (Bullet) shotsItr.next();
            shot.processCollisions(targets);
            if (shot.isHit()) {
                shotsItr.remove();
            }
        }
        
        if (gameLoop.isNetworkGame()) {
            // Send the local player ship state
            getLocalPlayerShip().createPacket();
                
        }
        
        if (getLocalPlayerShip().isDestroyed()) {
            gameOver = true;
        }
        
    }
    
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
    
    private void fixPlace(Ship ship) {
        
        Dimension screenDimention = 
            gameLoop.getScreenManager().getScreenDimension();
        
        // If the ship exits the screen and still in the wrong 
        // direction, change its velocity so it will get back
        if (ship.getX() < 0 && ship.getDx() < 0) {
            ship.setX(0);
            ship.setDx(0);
        }
        if (ship.getX()+ ship.getSpriteImage().getWidth(null) > 
                screenDimention.width && ship.getDx() > 0) {
            ship.setX(screenDimention.width - 
                    ship.getSpriteImage().getWidth(null));
            ship.setDx(0);
        }
        if (ship.getY() < 0 && ship.getDy() < 0) {
            ship.setY(0);
            ship.setDy(0);
        }
        if (ship.getY() + ship.getSpriteImage().getHeight(null) > 
                screenDimention.height && ship.getDy() > 0) {
            ship.setY(screenDimention.height - 
                    ship.getSpriteImage().getHeight(null));
            ship.setDy(0);
        }
    }
    
    public void addShip(Ship ship) {}
    public void removeShip(Ship ship) {}
    
    public void addShoot(Bullet shoot) {
        shots.add(shoot);
    }
    
    public void removeShoot(Bullet shot) {
        /** TODO: */
    }
    
    public GameNetworkManager getNetworkManager() {
        return gameLoop.getGameNetworkManager();
    }
    
    public boolean isController() {
        return gameLoop.isController();
    }
    
    public boolean isNetworkGame() {
        return gameLoop.isNetworkGame();
    }
    
    public int getHandlerID() {
        return this.handlerID;
    }
    
    public void handlePacket(Packet packet) {
        
        if (packet instanceof BulletPacket) {
            BulletPacket bulletPacket = (BulletPacket)packet;
            
            BulletModel model = bulletPacket.getBulletModel();
            
            int ownerID = model.ownerID;
            
            if (ownerID == getNetworkPlayerShip().getHandlerID()) {
                Bullet bullet = new LaserBean(getNetworkPlayerShip(), 
                        model.x, model.y, 
                        model.dx, model.dy);
                
                addShoot(bullet);                
            }
            
        }
        else {
            System.out.println("PlayerManager received packet handler ID = " + 
                    packet.handlerID + " network handler = " + networkPlayer.getObjectID());
            // Let the ship handle it
            if (packet.handlerID == networkPlayer.getObjectID()) {
                getNetworkPlayerShip().handlePacket(packet);
            }
        }
        
    }
    
    public void createPacket() {
        
    }
    
    public boolean isGameOver() {
        return gameOver;
    }
    
    public void addBonus(Bonus bonus) {
        // Player ship doesn't release bonuses
    }
    
}

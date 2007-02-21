
package game;

import game.network.GameNetworkManager;
import game.network.packet.*;
import game.ship.*;
import game.ship.bonus.Bonus;
import game.ship.weapon.*;

import java.awt.Dimension;
import java.awt.Graphics;
import java.util.*;

public class EnemyShipsManager implements Renderable, 
		ShipContainer, PacketHandler {
    
    private final int handlerID = GameConstants.ENEMY_MANAGER_ID;
    
    private GameLoop gameLoop;
    
    private Map enemyShips;
    private Collection shots;
    private Collection bonuses;
    private Collection targets;
    
    public EnemyShipsManager(GameLoop gameLoop) {
        this.gameLoop = gameLoop;
        enemyShips = new HashMap();
        shots = new ArrayList();
        bonuses = new ArrayList();
        targets = new ArrayList();
        
    }
    
    public void newLevel(Map enemyShips) {
        setEnemyShips(enemyShips);
        shots.clear();
        bonuses.clear();
//        targets.clear();
    }
    
    public void addTarget(Target target) {
        targets.add(target);
    }
    
    public void addTarget(Collection targets) {
        targets.addAll(targets);
    }
    
    public void addShip(Ship ship) {
        ship.setShipContainer(this);
        enemyShips.put(new Integer(ship.getObjectID()), ship);
    }
    
    public void removeShip(Ship ship) {
        enemyShips.remove(ship);
    }
    
    public void addBonus(Bonus bonus) {
        bonuses.add(bonus);
    }
 
    private void setEnemyShips(Map enemyShips) {
        Iterator shipsItr = enemyShips.values().iterator();
        while (shipsItr.hasNext()) {
            Ship ship = (Ship) shipsItr.next();
            ship.setShipContainer(this);
        }
        this.enemyShips = enemyShips;
    }
    
    public void update(long elapsedTime){
        
        Dimension screenDimention = 
            gameLoop.getScreenManager().getScreenDimension();
        
        // Update ships
        Iterator shipsItr = enemyShips.values().iterator();
        while (shipsItr.hasNext()) {
            Ship ship = (Ship) shipsItr.next();
            
            if (ship.isDestroyed()) {
				shipsItr.remove();
            }
            else {
                
	            ship.update(elapsedTime);
	            
	            // If the ship exits the screen and still in the wrong 
	            // direction, change its velocity so it will get back
	            if (ship.getX() < 0 && ship.getDx() < 0) {
	                ship.setDx(-ship.getDx());
	            }
	            if (ship.getX()+ ship.getWidth() > 
	                    screenDimention.width && ship.getDx() > 0) {
	                ship.setDx(-ship.getDx());
	            }
	            if (ship.getY() < 0 && ship.getDy() < 0) {
	                ship.setDy(-ship.getDy());
	            }
	            if (ship.getY() + ship.getHeight() > 
	                    screenDimention.height && ship.getDy() > 0) {
	                ship.setDy(-ship.getDy());
	            }
	            
            }
        }
        
        // Update shots
        Iterator shotsItr = shots.iterator();
        while (shotsItr.hasNext()) {
            Bullet shot = (Bullet) shotsItr.next();
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
        
        // update bonuses
        Iterator bonusesItr = bonuses.iterator();
        while (bonusesItr.hasNext()) {
            Bonus bonus = (Bonus) bonusesItr.next();
            bonus.updatePosition(elapsedTime);
            
            /** TODO: find better algorithm to handle non
             * cubic sprites
             */
            if (bonus.getX() + bonus.getWidth() < 0 ||
                    bonus.getX() > screenDimention.width ||
                    bonus.getY() + bonus.getHeight() < 0 ||
                    bonus.getY() > screenDimention.height) {
                
                bonusesItr.remove();
                
            }
            
        }
        
        // Process Collisions
        shipsItr = enemyShips.values().iterator();
        while (shipsItr.hasNext()) {
            Ship ship = (Ship) shipsItr.next();
            ship.processCollisions(targets); 
        }
        
        shotsItr = shots.iterator();
        while (shotsItr.hasNext()) {
            Bullet shot = (Bullet) shotsItr.next();
            shot.processCollisions(targets);
            if (shot.isHit()) {
                shotsItr.remove();
            }

        }
        
        bonusesItr = bonuses.iterator();
        while (bonusesItr.hasNext()) {
            Bonus bonus = (Bonus) bonusesItr.next();
            bonus.processCollisions(targets);
            if (bonus.isHit()) {
                bonusesItr.remove();
            }

        }

    }

    
    public void render(Graphics g) {
        // Render ships
        Iterator itr = enemyShips.values().iterator();
        while (itr.hasNext()) {
            Sprite ship = (Sprite) itr.next();
            ship.render(g);
        }
        
        // Render shots
        Iterator shotsItr = shots.iterator();
        while (shotsItr.hasNext()) {
            Sprite shot = (Sprite) shotsItr.next();
            shot.render(g);
        }
        
        Iterator bonusesItr = bonuses.iterator();
        while (bonusesItr.hasNext()) {
            Sprite bonus = (Sprite) bonusesItr.next();
            bonus.render(g);
        }
        
    }
    
    public boolean isLevelFinished() {
        return enemyShips.isEmpty() && bonuses.isEmpty();
    }
    
    public boolean isController() {
        return gameLoop.isController();
    }
    
    public boolean isNetworkGame() {
        return gameLoop.isNetworkGame();
    }
    
    public GameNetworkManager getNetworkManager() {
        return gameLoop.getGameNetworkManager();
    }
    
    public void addShoot(Bullet shoot) {
        shots.add(shoot);
    }
    
    public void removeShoot(Bullet shoot) {
        /** TODO: better be called from the bullet instead of
         * removing it in the process collision loop.
         * update also in playershipmanager
         */
    }
    
    public void handlePacket(Packet packet) {
        
        if (packet instanceof BulletPacket) {
            BulletPacket bulletPacket = (BulletPacket)packet;
            
            BulletModel model = bulletPacket.getBulletModel();
            
            int ownerID = model.ownerID;
            Ship owningShip = (Ship) enemyShips.get(new Integer(ownerID));
            
            if (owningShip != null) {
                Bullet bullet = new LaserBean(owningShip, model.x, model.y, 
                        model.dx, model.dy);
                
                addShoot(bullet);                
            }
            
            packet.setConsumed(true);
            
        }
        else {
            // Let the ship handle it
            Integer handlerID = new Integer(packet.handlerID);
            Ship ship = (Ship) enemyShips.get(handlerID);
            if (ship != null) {
                ship.handlePacket(packet);
            }
        }

        
    }
    
    public void createPacket() {
        
    }
    
    public int getHandlerID() {
        return this.handlerID;
    }
    

}

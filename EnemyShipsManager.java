
package game;

import game.ship.*;

import java.awt.Graphics;
import java.util.*;

public class EnemyShipsManager implements Renderable, ShipContainer {
    
    private Collection enemyShips;
    private Collection targets;
    
    public EnemyShipsManager() {
        
        enemyShips = new ArrayList();
        targets = new ArrayList();
        
    }
    
    public void addTarget(Target target) {
        targets.add(target);
    }
    
    public void addTarget(Collection targets) {
        targets.addAll(targets);
    }
    
    public void addShip(Ship ship) {
        enemyShips.add(ship);
    }
    
    public void removeShip(Ship ship) {
        enemyShips.remove(ship);
    }
 
    public void setEnemyShips(Collection enemyShips) {
        this.enemyShips = enemyShips;
    }
    
    public void update(long elapsedTime){
        
        Iterator itr = enemyShips.iterator();
        while (itr.hasNext()) {
            Ship ship = (Ship) itr.next();
            
            if (ship.isDestroyed()) {
System.out.println("removeing ship... num of ships = " + enemyShips.size());                
				itr.remove();
            }
            else {
                
	            ship.update(elapsedTime);
	            
	            if (ship.getX() < 0) {
	                ship.setDx(-ship.getDx());
	            }
	            if (ship.getX()+ ship.getSpriteImage().getWidth(null) > 800) {
	                ship.setDx(-ship.getDx());
	            }
	            if (ship.getY() < 0) {
	                ship.setDy(-ship.getDy());
	            }
	            if (ship.getY() + ship.getSpriteImage().getHeight(null) > 600) {
	                ship.setDy(-ship.getDy());
	            }
	            
	            ship.processCollisions(targets);
            }
        }

    }
    
//    private void processCollisions() {
//        
//        Iterator enemyShipsItr = enemyShips.iterator();
//        while (enemyShipsItr.hasNext()) {
//            Ship ship = (Ship) enemyShipsItr.next();
//            int x0 = (int)Math.round(ship.getX());
//            int y0 = (int)Math.round(ship.getY());
//            int x1 = x0 + ship.getWidth();
//            int y1 = y0 + ship.getHeight();
//            
//            Iterator targetsItr = targets.iterator();
//            while (targetsItr.hasNext()) {
//                Target target = (Target) targetsItr.next();
//                if (target.isCollision(x0, y0, x1, y1)) {
//                    target.hit(ship.getDamage());
//                }
//            }
//            
//        }
//        
//    }
    
    public void render(Graphics g) {
        
        Iterator itr = enemyShips.iterator();
        while (itr.hasNext()) {
            Sprite ship = (Sprite) itr.next();
            ship.render(g);
        }
        
    }
    
    public boolean isLevelFinished() {
        return enemyShips.isEmpty();
    }
    

}

package game;

import java.awt.Graphics;
import java.util.*;

import game.ship.LaserCanon;
import game.ship.PlayerShip;
import game.ship.Ship;
import game.ship.Target;
import game.ship.Weapon;
import game.util.Log;

import javax.swing.ImageIcon;

public class PlayerManager {

    private PlayerShip playerShip;
    private PlayerShip player2Ship;
    private Collection targets;
    
    public PlayerManager(boolean twoPlayersGame) {
        playerShip = new PlayerShip(
                380, 540, 0, 0, 
                new ImageIcon(GameConstants.IMAGES_DIR+"player01.png").getImage(), 
                new LaserCanon(Weapon.DIRECTION_UP)
        );

        if (twoPlayersGame) {
            player2Ship = new PlayerShip(
                    600, 540, 0, 0,
                    new ImageIcon(GameConstants.IMAGES_DIR+"Bajoran.gif").getImage(), 
                    new LaserCanon(Weapon.DIRECTION_UP)
            );
        }
        
        targets = new ArrayList();
    }
    
    public void addTarget(Target target) {
        this.targets.add(target);
    }
    
    public void addTarget(Collection targets) {
        this.targets.addAll(targets);
    }
    
    public Ship getPlayerShip() {
        return playerShip;
    }
    
    public Ship getPlayer2Ship() {
        return player2Ship;
    }
    
    public void update(long elapsedTime) {
        playerShip.update(elapsedTime);
        playerShip.processCollisions(targets);
        
        if (player2Ship != null) {
            player2Ship.update(elapsedTime);
            player2Ship.processCollisions(targets);
        }
        
//        processCollisions();
    }
    
    public void render(Graphics g) {
        playerShip.render(g);
        
        if (player2Ship != null) {
            player2Ship.render(g);
        }
    }
    
//    private void processCollisions() {
//        
//        if (playerShip.isVulnerable()) {
//	        int x0 = (int)Math.round(playerShip.getX());
//	        int y0 = (int)Math.round(playerShip.getY());
//	        int x1 = x0 + playerShip.getWidth();
//	        int y1 = y0 + playerShip.getHeight();
//	
//	        Iterator targetsItr = targets.iterator();
//	        while (targetsItr.hasNext()) {
//	            Target target = (Target) targetsItr.next();
//	            if (target.isCollision(x0, y0, x1, y1)) {
//	                target.hit(playerShip.getDamage());
//	            }
//	        }
//        }
//    }
    
}

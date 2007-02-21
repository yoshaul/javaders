package game.ship;

import game.ship.bonus.Bonus;
import game.ship.weapon.Bullet;
import game.network.client.GameNetworkManager;

/**
 * The <code>ShipContainer</code> defines the methods that an
 * object containing and manages ships should implement.
 * Through this interface the ships can cummunicate with other
 * game objects.
 */
public interface ShipContainer {

    /**
     * Adds a new ship to the ship container.
     * @param ship	Ship to add.
     */
    public void addShip(Ship ship);
    
    /**
     * Adds a shot to the ship container shot collection.
     * @param shot	Shot to add.
     */
    public void addShot(Bullet shot);
    
    /**
     * Adds a bonus to the ship container bonuses.
     * @param bonus	Bonus to add.
     */
    public void addBonus(Bonus bonus);
    
    /**
     * Returns the network manager of the game.
     * @return	Game network manager.
     */
    public GameNetworkManager getNetworkManager();
    
    /**
     * Returns the network handler id of the ship container. 
     * @return Network handler id of the ship container.
     */
    public int getHandlerId();
    
    /**
     * Returns true if the game is a network game.
     * @return	True if in network game.
     */
    public boolean isNetworkGame();
    
    /**
     * Returns true if this machine is the controller machine.
     * @return True if this machine is the controller machine.
     */
    public boolean isController();
    
}

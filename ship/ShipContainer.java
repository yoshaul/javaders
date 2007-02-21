package game.ship;

import game.ship.bonus.Bonus;
import game.ship.weapon.Bullet;
import game.network.GameNetworkManager;

public interface ShipContainer {

    public void addShip(Ship ship);
    public void removeShip(Ship ship);
    
    public void addShoot(Bullet shoot);
    public void removeShoot(Bullet shot);
    
    public void addBonus(Bonus bonus);
    
    public GameNetworkManager getNetworkManager();
    
    public int getHandlerID();
    
    public boolean isNetworkGame();
    public boolean isController();
    
}

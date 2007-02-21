package game.ship;

import game.ship.bonus.Bonus;
import game.ship.bonus.PowerUp;
import game.ship.bonus.WeaponUpgrade;
import game.ship.weapon.Weapon;
import game.ship.weapon.WeaponFactory;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class EnemyShip extends Ship {

    private long initArmor;

    
    public EnemyShip(int objectID, int shipType, 
            float x, float y, float dx, float dy,
            ShipProperties properties) {
        
        super(objectID, shipType, x, y, dx, dy, properties.image, 
                WeaponFactory.getWeapon(properties.weaponType, Weapon.DIRECTION_DOWN), 
                properties.armor, properties.damage, 
                properties.hitScoreValue, properties.destroyScoreValue);
        
        this.initArmor = armor;
        
    }
    
    public EnemyShip(ShipModel shipModel) {
        super(shipModel);
        this.initArmor = ShipProperties.getShipProperties(
                shipModel.shipType).armor;
    }
    
    public void render(Graphics g) {
        super.render(g);
        
        if (isActive()) {
            Font font = new Font("SanSerif", Font.BOLD, 10);
            g.setColor(Color.GREEN);
            g.setFont(font);
            
            int armorLeft = (int) (((double)this.armor/this.initArmor)*100);
            
            g.drawString(armorLeft+"%", 
                    (int)Math.round(this.getX()), 
                    (int)Math.round(this.getY())+10);
        }
    }
    
    public void update(long elapsedTime) {
        
        if (shipContainer.isController()) {
            // Only the controller machine generate random events
            double rand = Math.random();
            if (rand > 0.95) {
                this.shoot();
            }
        }
        
        super.update(elapsedTime);
        
        if (shipContainer.isNetworkGame() && Math.random() > 0.9) {
            // Send state update
            createPacket();
        }
        
    }
    
    public void hit(Bonus bonus) {
        // Enemy ships don't consume bonuses
    }
    
    // Overide Ship destroy method
    public void destroy() {
        super.destroy();
        
        if (Math.random() > 1) {
            getShipContainer().addBonus(new PowerUp(getCenterX(), getCenterY(),
                    0.0f, 0.15f, 1));
            
        } else if (Math.random() > 0) {
            getShipContainer().addBonus(new WeaponUpgrade(getCenterX(), 
                    getCenterY(), WeaponFactory.TYPE_LASER_CANNON));
        }
        
    }

}

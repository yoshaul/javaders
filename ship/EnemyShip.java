package game.ship;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;

public class EnemyShip extends Ship {

    private long initArmor;

    public EnemyShip(double x, double y, double dx, double dy,
            Image image, Weapon gun, long armor, long damage) {
        
        super(x, y, dx, dy, image, gun, armor, damage);
        this.initArmor = armor;

    }
    
    public EnemyShip(double x, double y, double dx, double dy,
            ShipProperties properties) {
        
        this(x, y, dx, dy, properties.image, 
                properties.weapon, properties.armor, 
                properties.damage);
        
    }
    
    public void render(Graphics g) {
        super.render(g);
        
        if (isActive()) {
            Font font = new Font("SanSerif", Font.BOLD, 10);
            g.setColor(Color.GREEN);
            g.setFont(font);
            
            int armorLeft = (int) (((double)this.armor/this.initArmor)*100);
            
            g.drawString(armorLeft+"%", 
                    (int)Math.round(this.getX()), (int)Math.round(this.getY())+10);
        }
    }
    
    public void update(long elapsedTime) {
        
        double rand = Math.random();
        if (rand > 0.95) {
            this.shoot();
        }
        
        super.update(elapsedTime);
        
    }

}

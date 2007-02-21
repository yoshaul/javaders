package game.ship;

import game.sound.SoundFactory;
import game.util.Log;

import java.awt.Graphics;
import java.awt.Image;
import java.util.Collection;
import java.util.Iterator;


public abstract class Ship extends Sprite implements Target {

    private final static int STATE_NORMAL = 0;
    private final static int STATE_EXPLODING = 1;
    private final static int STATE_DESTROYED = 2;
    
    private ShipContainer shipContainer;
    
    /** The ship's armor */
    protected long armor;
    /** The damage the ship causes when colliding with a traget */ 
    private long damage;
    
    private int state = STATE_NORMAL;
    
    protected Weapon gun;
    //private Weapon secondWeapon;

    public Ship(double x, double y, double dx, double dy,
            Image image, Weapon gun, long armor, long damage) {
        super(x, y, dx, dy, image);
        this.gun = gun;
        this.armor = armor;
        this.damage = damage;
    }
    
    public Ship(double x, double y, double dx, double dy,
            ShipProperties properties) {
        
        this(x, y, dx, dy, properties.image, 
                properties.weapon, properties.armor, 
                properties.damage);
        
    }
    
    public void shoot() {
        gun.shoot(getCenterX(), getY());
    }
    
    public void processCollisions(Collection targets) {
        int x0 = (int)Math.round(this.getX());
        int y0 = (int)Math.round(this.getY());
        int x1 = x0 + this.getWidth();
        int y1 = y0 + this.getHeight();

        Iterator targetsItr = targets.iterator();
        while (targetsItr.hasNext()) {
            Target target = (Target) targetsItr.next();
            if (target.isCollision(x0, y0, x1, y1)) {
                target.hit(this.getDamage());
            }
        }
        
        if (gun != null) {
            gun.processCollisions(targets);
        }
    }
    
    public boolean isCollision(int x0, int y0, int x1, int y1) {
        
        if (state == STATE_DESTROYED) {
            return false;
        }
        else {
        
	        // get the pixel location of this ship
	        int s2x = (int)Math.round(this.getX());
	        int s2y = (int)Math.round(this.getY());
	        int s2x1 = s2x + this.getWidth();
	        int s2y1 = s2y + this.getHeight();
	
	        // check if the boundaries intersect
	        return ( x0 < s2x1 &&
	                 s2x < x1  &&
	                 y0 < s2y1 &&
	                 s2y < y1);
        }
    }
    
    public void hit(long damage) {
        if (state == STATE_NORMAL) {
	        
            SoundFactory.playSound("hit1.wav");
            
            armor -= damage;
	        if (armor <= 0) {
	            destroy();
	        }
        }
    }
    
    public void update(long elapsedTime) {
        if (isActive()) {
	        updatePosition(elapsedTime);
	        if (gun != null) {
	            gun.update(elapsedTime);
	        }
        }
    }
    
    public void render(Graphics g) {
        if (isActive()) {
	        super.render(g);
	        if (gun != null) {
	            gun.render(g);
	        }
        }
    }
    
    public void destroy() {
        SoundFactory.playSound("explode1.wav");
        this.setActive(false);
        this.state = STATE_DESTROYED;
    }
    
    public long getDamage() {
        return damage;
    }
    
    public void setShipContainer(ShipContainer container) {
        this.shipContainer = container;
    }
    
    public boolean isNormal() {
        return state == STATE_NORMAL;
    }
    
    public boolean isExploding() {
        return state == STATE_EXPLODING;
    }
    
    public boolean isDestroyed() {
        return state == STATE_DESTROYED;
    }
    
}

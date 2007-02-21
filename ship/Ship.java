package game.ship;

import game.network.GameNetworkManager;
import game.network.packet.*;
import game.ship.weapon.*;
import game.sound.SoundFactory;

import java.awt.Graphics;
import java.awt.Image;
import java.util.Collection;
import java.util.Iterator;


public abstract class Ship extends Sprite implements Target, PacketHandler {

    private final static int STATE_NORMAL = 0;
    private final static int STATE_EXPLODING = 1;
    private final static int STATE_DESTROYED = 2;
    
    protected ShipContainer shipContainer;
    
    protected int objectID; 
    private int shipType;
    
    /** The ship's armor */
    protected long armor;
    /** The damage the ship causes when colliding with a traget */ 
    private long damage;
    
    private long hitScoreValue;
    private long destroyScoreValue;
    
    private int state = STATE_NORMAL;
    
    protected Weapon weapon;
    //private Weapon secondWeapon;

    public Ship(int objectID, int shipType,
            float x, float y, float dx, float dy,
            Image image, Weapon gun, long armor, long damage, 
            long hitScoreValue, long destroyScoreValue) {
        
        super(x, y, dx, dy, image);
        this.objectID = objectID;
        this.shipType = shipType;
        this.weapon = gun;
        this.armor = armor;
        this.damage = damage;
        this.hitScoreValue = hitScoreValue;
        this.destroyScoreValue = destroyScoreValue;
        
        // Set this ship as the owner of the weapon
        this.weapon.setOwner(this);
    }
    
    public Ship(int objectID, int shipType, 
            float x, float y, float dx, float dy,
            ShipProperties properties) {

        
        this(objectID, shipType, x, y, dx, dy, properties.image, 
                WeaponFactory.getWeapon(properties.weaponType, 
                        properties.weaponLevel, properties.weaponDirection) ,
                properties.armor, properties.damage, 
                properties.hitScoreValue, properties.destroyScoreValue);
        
    }
    
    public Ship(ShipModel model) {
        this(model.objectID, model.shipType, 
                model.x, model.y, model.dx, model.dy);
    }
    
    public Ship(int objectID, int shipType, 
            float x, float y, float dx, float dy) {
        
        this(objectID, shipType, 
                x, y, dx, dy, 
                ShipProperties.getShipProperties(shipType));
        
    }
    
    public void shoot() {
        weapon.fire(getCenterX(), getY());
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
        
//        if (weapon != null) {
//            weapon.processCollisions(targets);
//        }
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
    
    public void hit(Bullet bullet) {
        if (state == STATE_NORMAL) {
	        
            SoundFactory.playSound("hit1.wav");
            long damage = bullet.getDamage();
            armor -= damage;
	        if (armor <= 0) {
	            destroy();
	        }
	        
	        // Add the score to the hitting player
	        if (bullet.getOwner() instanceof PlayerShip){
	            long score = (armor<=0) ? destroyScoreValue : hitScoreValue; 
	            PlayerShip ship = (PlayerShip) bullet.getOwner();
	            ship.addScore(score);
	        }
        }
    }
    
    public void update(long elapsedTime) {
        if (isActive()) {
	        updatePosition(elapsedTime);
        }
    }
    
    public void render(Graphics g) {
        if (isActive()) {
	        super.render(g);
        }
    }
    
    public void destroy() {
        SoundFactory.playSound("explode1.wav");
        this.setActive(false);
        this.state = STATE_DESTROYED;
    }
    
    protected void setWeapon(Weapon weapon) {
        this.weapon = weapon;
    }
    
    public long getDamage() {
        return this.damage;
    }
    
    public long getArmor() {
        return this.armor;
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
    
    public int getObjectID() {
        return this.objectID;
    }
    
    public ShipModel getShipModel() {
        return new ShipModel(objectID, shipType, 
                getX(), getY(), getDx(), getDy());
    }
    
    // Implement PacketHandler interface
    public void handlePacket(Packet packet) {
        
        if (packet instanceof ShipPacket) {
            ShipPacket shipPacket = (ShipPacket)packet;
            ShipState shipState = shipPacket.getShipState();
            
            setX(shipState.x);
            setY(shipState.y);
            setDx(shipState.dx);
            setDy(shipState.dy);
            armor = shipState.armor;
            state = shipState.state;
            
            packet.setConsumed(true);
        }
        
    }
    
    public void createPacket() {
        
        if (!shipContainer.isNetworkGame()) {
            return;
        }
        
        ShipState shipState = new ShipState(x, y, dx, dy, armor, state);
        
        GameNetworkManager netManager = 
            shipContainer.getNetworkManager();
        
        Packet shipPacket = new ShipPacket(netManager.getSenderID(),
                netManager.getReceiverID(), getHandlerID(), shipState);
        
        netManager.sendPacket(shipPacket);
    }
    
    public int getHandlerID() {
        return this.objectID;
    }
    
    public ShipContainer getShipContainer() {
        return shipContainer;
    }
}

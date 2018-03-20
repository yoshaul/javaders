/*
 * This file is part of Javaders.
 * Copyright (c) Yossi Shaul
 *
 * Javaders is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Javaders is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Javaders.  If not, see <http://www.gnu.org/licenses/>.
 */

package game.ship;

import game.network.client.GameNetworkManager;
import game.network.packet.Packet;
import game.network.packet.PacketHandler;
import game.network.packet.ShipPacket;
import game.ship.weapon.Bullet;
import game.ship.weapon.Weapon;
import game.ship.weapon.WeaponFactory;
import game.sound.SoundFactory;

import java.awt.*;
import java.util.Collection;

/**
 * The abstract <code>Ship</code> class is the base class for all the
 * ships in the game/
 */
public abstract class Ship extends Sprite implements Target, PacketHandler {

    private final static int STATE_NORMAL = 0;
    private final static int STATE_EXPLODING = 1;
    private final static int STATE_DESTROYED = 2;

    ShipContainer shipContainer;

    /**
     * Time passed since the last packet send
     */
    long timeSinceLastPacket;

    private int objectId;
    private int shipType;

    /**
     * The ship's armor
     */
    long armor;
    /**
     * The damage the ship causes when colliding with a traget
     */
    private long damage;

    /**
     * Max vertical and horizontal velocity of the ship
     */
    private float maxDX;
    private float maxDY;

    long damageScoreValue;
    long destroyScoreValue;

    int state = STATE_NORMAL;

    Weapon weapon;
    //private Weapon secondWeapon;

    /**
     * Construct a new ship.
     *
     * @param objectId          Network handler id of the ship
     * @param shipType          Type of the ship as defined in ShipProperties
     * @param x                 Vertical location of the ship (from left)
     * @param y                 Horizontal location of the ship (from top)
     * @param dx                Max vertical velocity (pixels/sec)
     * @param dy                Max horizontal velocity (pixels/sec)
     * @param image             Ship image
     * @param gun               Ship main weapon
     * @param armor             Ship armor
     * @param damage            Damage the ship cause to other ship when they collide
     * @param hitScoreValue     Score the ship gives per 1 damage unit
     * @param destroyScoreValue Score the ship gives when destroyed
     */
    Ship(int objectId, int shipType,
            float x, float y, float dx, float dy,
            Image image, Weapon gun, long armor, long damage,
            long hitScoreValue, long destroyScoreValue) {

        super(x, y, dx, dy, image);
        this.objectId = objectId;
        this.shipType = shipType;
        this.maxDX = dx;
        this.maxDY = dy;
        this.weapon = gun;
        this.armor = armor;
        this.damage = damage;
        this.damageScoreValue = hitScoreValue;
        this.destroyScoreValue = destroyScoreValue;

        // Set this ship as the owner of the weapon
        this.weapon.setOwner(this);

        this.timeSinceLastPacket = 0;
    }


    /**
     * Costruct a new ship from ship type.
     *
     * @param objectId Network handler id of the ship
     * @param shipType Type of the ship as defined in ShipProperties
     * @param x        Vertical location of the ship (from left)
     * @param y        Horizontal location of the ship (from top)
     * @param prop     Properties of the ship
     */
    private Ship(int objectId, int shipType, float x, float y,
            ShipProperties prop) {

        this(objectId, shipType, x, y, prop.getMaxDX(), prop.getMaxDY(),
                prop.getImage(), WeaponFactory.getWeapon(prop.getWeaponType(),
                        prop.getWeaponLevel(), prop.getWeaponDirection()),
                prop.getArmor(), prop.getDamage(),
                prop.getHitScoreValue(), prop.getDestroyScoreValue());

    }


    /**
     * Construct a new ship from a <code>ShipModel</code>.
     *
     * @param model ShipModel with the ship details
     * @see game.ship.ShipModel
     */
    public Ship(ShipModel model) {
        this(model.getObjectId(), model.getShipType(), model.getX(), model.getY(),
                ShipProperties.getShipProperties(model.getShipType()));
    }


    /**
     * Calls the main weapon to fire.
     */
    public void shoot() {
        weapon.fire(getCenterX(), getY());
    }

    /**
     * Check if this ship collides with one of the targets(ships).
     * If so hit it and with <code>damage</code>.
     * Note that this method is for ship-to-ship collision only.
     *
     * @param targets Collection of target ships.
     */
    public void processCollisions(Collection<Target> targets) {

        if (!active) {
            return;
        }

        int x0 = Math.round(this.getX());
        int y0 = Math.round(this.getY());
        int x1 = x0 + this.getWidth();
        int y1 = y0 + this.getHeight();

        for (Target target : targets) {
            if (target.isCollision(x0, y0, x1, y1)) {
                target.hit(this.getDamage());
            }
        }

    }

    /**
     * Returns true if this ship collide with the rectangle
     * (x0, y0), (x1, y1) (top-left and bottom-right respectively)
     */
    @Override
    public boolean isCollision(int x0, int y0, int x1, int y1) {

        if (state == STATE_DESTROYED) {
            return false;
        } else {

            // get the pixel location of this ship
            int s2x = Math.round(this.getX());
            int s2y = Math.round(this.getY());
            int s2x1 = s2x + this.getWidth();
            int s2y1 = s2y + this.getHeight();

            // check if the boundaries intersect
            return (x0 < s2x1 &&
                    s2x < x1 &&
                    y0 < s2y1 &&
                    s2y < y1);
        }
    }

    /**
     * Called by a game object to hit this ship.
     *
     * @param damage Amount of damage the hit cause
     */
    @Override
    public void hit(long damage) {
        if (state == STATE_NORMAL) {

            SoundFactory.playSound("hit1.wav");

            long actualDamage = Math.min(armor, damage);
            armor -= actualDamage;
            if (armor == 0) {
                destroy();
            }
        }
    }

    /**
     * Called by a bullet when it hits this ship.
     * If the owner of the bullet is a player ship we add
     * score to the owner ship.
     */
    @Override
    public void hit(Bullet bullet) {
        if (state == STATE_NORMAL) {
            long damage = bullet.getDamage();
            long actualDamage = Math.min(armor, damage);
            armor -= actualDamage;
            if (armor == 0) {
                destroy();
            }
        }
    }

    /**
     * Updates the ship's state.
     *
     * @param elapsedTime Time passed since the last update.
     */
    public void update(long elapsedTime) {
        if (isActive()) {
            timeSinceLastPacket += elapsedTime;
            updatePosition(elapsedTime);
        }
    }

    /**
     * Render the ship.
     */
    @Override
    public void render(Graphics g) {
        if (isActive()) {
            super.render(g);
        }
    }

    /**
     * Destroy this ship.
     */
    public void destroy() {
        SoundFactory.playSound("explode1.wav");
        this.setActive(false);
        this.state = STATE_DESTROYED;
    }

    /**
     * Sets the main weapon of the ship.
     *
     * @param weapon New main weapon
     */
    void setWeapon(Weapon weapon) {
        this.weapon = weapon;
    }

    /**
     * Returns the damage caused by this ship when it collides with
     * other game objects.
     *
     * @return Damage caused by this ship.
     */
    private long getDamage() {
        return this.damage;
    }

    /**
     * Returns the ship armot (hit points).
     *
     * @return The ship armor.
     */
    public long getArmor() {
        return this.armor;
    }

    /**
     * Returns the max vertical velocity of the ship.
     *
     * @return Max vertical velocity of the ship.
     */
    public float getMaxDX() {
        return this.maxDX;
    }

    /**
     * Returns the max horizontal velocity of the ship.
     *
     * @return Max horizontal velocity of the ship.
     */
    public float getMaxDY() {
        return this.maxDY;
    }

    public void setShipContainer(ShipContainer container) {
        this.shipContainer = container;
    }

    /**
     * Returns true if this ship state is in normal state (can fight).
     *
     * @return True if this ship is in normal state.
     */
    boolean isNormal() {
        return state == STATE_NORMAL;
    }

    /**
     * Returns true if this ship state is exploding.
     *
     * @return True if this ship is exploding.
     */
    public boolean isExploding() {
        return state == STATE_EXPLODING;
    }

    /**
     * Returns true if this ship state is destroyed.
     *
     * @return True if this ship is destroyed.
     */
    public boolean isDestroyed() {
        return state == STATE_DESTROYED;
    }

    /**
     * Returns <code>ShipModel</code> object of this ship.
     *
     * @return ShipModel object of this ship
     * @see game.ship.ShipModel
     */
    public ShipModel getShipModel() {
        return new ShipModel(objectId, shipType,
                getX(), getY(), getDx(), getDy());
    }

    /**
     * Returns <code>ShipState</code> object of this ship to send over
     * the network. This method is abstract since the player's ship
     * and enemy ships send different ShipState objects.
     *
     * @return ShipState object of this ship
     * @see game.ship.ShipState
     */
    protected abstract ShipState getShipState();

    /**
     * Handle incoming packet.
     * Called by the <code>ShipContainer</code> according to the ship
     * network id.
     */
    @Override
    public void handlePacket(Packet packet) {

        if (packet instanceof ShipPacket) {
            ShipPacket shipPacket = (ShipPacket) packet;
            ShipState shipState = shipPacket.getShipState();

            setX(shipState.getX());
            setY(shipState.getY());
            setDx(shipState.getDx());
            setDy(shipState.getDy());
            armor = shipState.getArmor();
            state = shipState.getState();

            packet.setConsumed(true);
        }

    }

    /**
     * Generates and sends a packet with the details of this ship.
     */
    @Override
    public void createPacket(GameNetworkManager netManager) {

        ShipState shipState = getShipState();

        Packet shipPacket = new ShipPacket(netManager.getSenderId(),
                netManager.getReceiverId(), getHandlerId(), shipState);

        netManager.sendPacket(shipPacket);

        timeSinceLastPacket = 0;
    }

    /**
     * Returns the network handler id of this ship.
     *
     * @return Network handler id
     */
    @Override
    public int getHandlerId() {
        return this.objectId;
    }

    /**
     * Returns the <code>ShipContainer</code> object containing this ship.
     *
     * @return ShipContainer object
     */
    public ShipContainer getShipContainer() {
        return shipContainer;
    }
}

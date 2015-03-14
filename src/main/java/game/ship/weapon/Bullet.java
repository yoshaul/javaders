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

package game.ship.weapon;

import game.GameConstants;
import game.network.client.GameNetworkManager;
import game.network.packet.BulletPacket;
import game.network.packet.Packet;
import game.network.packet.PacketHandler;
import game.ship.Ship;
import game.ship.Sprite;
import game.ship.Target;
import game.util.ResourceManager;

import java.awt.*;
import java.util.Collection;
import java.util.Iterator;

/**
 * The abstract Bullet class represents a bullet fired by
 * a ship's weapon.
 * Each bullet must have it's owner, meaning the ship that fired it
 */
public abstract class Bullet extends Sprite implements PacketHandler {

    private Ship owner;            // Ship that fired the bullet
    private long damage;        // The damage the bullet cause
    private boolean hit;        // True if this bullet hit some object

    /**
     * Construct a new Bullet
     *
     * @param owner     Reference to the ship that fired the bullet
     * @param x         Middle vertical location of the ship (from left)
     * @param y         Horizontal location of the ship (from top)
     * @param dx        Max vertical velocity (pixels/sec)
     * @param dy        Max horizontal velocity (pixels/sec)
     * @param imageName Name of the bullet image
     * @param damage    Damage the bullet cause
     */
    public Bullet(Ship owner, float x, float y, float dx, float dy,
                  String imageName, long damage) {

        super(x, y, dx, dy);
        Image image = ResourceManager.loadImage(GameConstants.IMAGES_DIR + imageName);
        this.setSpriteImage(image);
        // Center the bullet relative to the initial x value
        setX(x - ((float) image.getWidth(null) / 2));
        this.damage = damage;
        this.owner = owner;

    }


//    public Bullet(Ship owner, float x, float y, float dx, float dy, 
//            long damage) {
//        
//        super(x, y, dx, dy);
//        this.damage = damage;
//        this.owner = owner;
//        
//    }


//    public Bullet(BulletModel model, Ship owner) {
//        
//        this(owner, model.x, model.y, model.dx, model.dy, model.imageName, model.damage);
//        
////        super(model.x, model.y, model.dx, model.dy);
////        this.damage = model.damage;
////        this.owner = owner;
//        
//    }


    /**
     * Check if the bullet collides with any of the targets.
     * If it is hit the target and deliver the damage.
     *
     * @param targets Collection of targets to check.
     */
    public void processCollisions(Collection targets) {
        int x0 = Math.round(this.getX());
        int y0 = Math.round(this.getY());
        int x1 = x0 + this.getWidth();
        int y1 = y0 + this.getHeight();

        Iterator targetsItr = targets.iterator();
        while (targetsItr.hasNext() && !hit) {
            Target target = (Target) targetsItr.next();
            if (target.isCollision(x0, y0, x1, y1)) {
                target.hit(this);
                hit = true;
            }
        }
    }

    /**
     * Returns true if this bullet hit some target.
     *
     * @return True if this bullet hit some target.
     */
    public boolean isHit() {
        return hit;
    }

    /**
     * Returns the damage this bullet can cause.
     *
     * @return Bullet's damage
     */
    public long getDamage() {
        return this.damage;
    }

    /**
     * Returns the ship that fired this bullet.
     *
     * @return Ship that fired this bullet.
     */
    public Ship getOwner() {
        return this.owner;
    }


    //////////////////////////////////
    // PacketHandler implementation //
    //////////////////////////////////

    /**
     * Create and send bullet packet.
     */
    @Override
    public void createPacket(GameNetworkManager netManager) {
        // Prepare the bullet model
        BulletModel model = new BulletModel(this.getClass(),
                owner.getHandlerId(), x, y, dx, dy, damage);

        // Create the BulletPacket
        Packet packet = new BulletPacket(netManager.getSenderId(),
                netManager.getReceiverId(), owner.getHandlerId(), model);

        netManager.sendPacket(packet);

    }

    @Override
    public void handlePacket(Packet packet) {
        // Bullet is not handling any packets
    }

    /**
     * Returns the network handler id. The handler of bullet
     * packets is the ship container of the firing ship.
     */
    @Override
    public int getHandlerId() {
        return owner.getShipContainer().getHandlerId();
    }

}

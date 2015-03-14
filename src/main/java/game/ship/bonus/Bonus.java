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

package game.ship.bonus;

import game.GameConstants;
import game.network.packet.Packet;
import game.network.packet.PacketHandler;
import game.ship.Sprite;
import game.ship.Target;

import java.util.Collection;
import java.util.Iterator;

/**
 * The abstract Bonus class represents a bonus dropped by enemy ship.
 */
public abstract class Bonus extends Sprite implements PacketHandler {

    private boolean hit = false;    // Is this bonus hit a ship
    private static final float dx = 0.0f;    // Vertical velocity
    private static final float dy = 0.2f;    // Hotizontal velocity

    public Bonus(float x, float y) {

        super(x, y, dx, dy);
    }

    // Implement some of the PacketHandler methods

    public void handlePacket(Packet packet) {
        // Bonuses don't handle incoming packets
    }

    /**
     * The packet handler for all bonuses is the enemy
     * ships manager object.
     */
    public int getHandlerId() {
        return GameConstants.ENEMY_MANAGER_ID;
    }

    /**
     * Test is this bonus collides with one of the input targets.
     * If so, call the ship's <code>hit</code> method and mark as
     * hit.
     *
     * @param targets Collection of <code>Target</code> objects (player ship).
     */
    public void processCollisions(Collection<Target> targets) {
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
     * Returns true if this bonus was hit.
     *
     * @return True if this bonus was hit.
     */
    public boolean isHit() {
        return hit;
    }

}

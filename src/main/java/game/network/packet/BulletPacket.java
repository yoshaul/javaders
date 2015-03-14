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

package game.network.packet;

import game.ship.weapon.BulletModel;

/**
 * The bullet packet is sent when one of the ship fires
 * a new bullet. The packet contains the bullet details
 * in a <code>BulletModel</code> object.
 *
 * @see game.ship.weapon.BulletModel
 */
public class BulletPacket extends Packet {

    // Holds the bullt details including the bullet owner
    private BulletModel bulletModel;

    /**
     * Constructs a new <code>BulletPacket</code>.
     *
     * @param senderId     Session id of the sender
     * @param receiverId   Session id of the target user
     * @param firingShipId Id of the firing ship
     * @param bulletModel  Bullet details
     */
    public BulletPacket(Long senderId, Long receiverId,
                        int firingShipId, BulletModel bulletModel) {

        super(senderId, receiverId, firingShipId);
        this.bulletModel = bulletModel;
    }

    public BulletModel getBulletModel() {
        return this.bulletModel;
    }

}

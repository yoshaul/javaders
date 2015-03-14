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

/**
 * The <code>WeaponUpgradePacket</code> is sent whenever an enemy
 * ship drops a weapon upgrade bonus.
 */
public class WeaponUpgradePacket extends Packet {

    public float x, y;    // Location of the sprite
    public int weaponType;

    /**
     * Constructs a new <code>BulletPacket</code>.
     *
     * @param senderId   Session id of the sender
     * @param receiverId Session id of the target user
     * @param handlerId  Id of the network handler
     * @param weaponType Type of the weapon
     */
    public WeaponUpgradePacket(Long senderId, Long receiverId,
                               int handlerId, float x, float y, int weaponType) {

        super(senderId, receiverId, handlerId);
        this.x = x;
        this.y = y;
        this.weaponType = weaponType;
    }
}
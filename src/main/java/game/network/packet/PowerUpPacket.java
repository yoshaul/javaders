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
 * The <code>PowerUpPacket</code> is sent whenever an enemy
 * ship drops a power up bonus.
 */
public class PowerUpPacket extends Packet {

    public float x, y;    // Location of the powerup sprite
    public int powerUp;    // How much armor the bonus adds to the ship

    /**
     * Constructs a new <code>BulletPacket</code>.
     *
     * @param senderId   Session id of the sender
     * @param receiverId Session id of the target user
     * @param handlerId  Id of the network handler
     */
    public PowerUpPacket(Long senderId, Long receiverId,
                         int handlerId, float x, float y, int powerUp) {

        super(senderId, receiverId, handlerId);
        this.x = x;
        this.y = y;
        this.powerUp = powerUp;
    }
}

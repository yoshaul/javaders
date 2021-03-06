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
 * The <code>SystemPacket</code> is used as a general packet to
 * deliver short info (represented by an int) between the players.
 */
public class SystemPacket extends Packet {

    // Available types of a SystemPacket 
    public static final int TYPE_READY_TO_PLAY = 1;

    private int type;    // Type of the system packet

    /**
     * Construct a new <code>SystemPacket</code>.
     *
     * @param senderId   Session id of the sender
     * @param receiverId Session id of the target user
     * @param type       Type of the system message
     */
    public SystemPacket(Long senderId, Long receiverId, int type) {
        super(senderId, receiverId);
        this.type = type;
    }

    /**
     * Returns the system packet type
     *
     * @return packet type
     */
    public int getType() {
        return this.type;
    }

    public String toString() {
        return super.toString() +
                " type: " + type;

    }

}

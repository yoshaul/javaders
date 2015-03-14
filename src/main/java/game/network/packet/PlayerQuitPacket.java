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
 * The <code>PlayerQuitPacket</code> is sent when the
 * user quits at the middle of an online game.
 */
public class PlayerQuitPacket extends Packet {

    /**
     * Constructs a new <code>PlayerQuitPacket</code>.
     *
     * @param senderId   Session id of the sender
     * @param receiverId Session id of the target user
     */
    public PlayerQuitPacket(Long senderId, Long receiverId) {
        super(senderId, receiverId);
    }

}

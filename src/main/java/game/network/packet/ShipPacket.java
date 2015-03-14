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

import game.ship.ShipState;

/**
 * The <code>ShipPacket</code> is sent by ship (enemy or friendly)
 * and encapsulates the ship current state in a <code>ShipState</code> object.
 *
 * @see game.ship.ShipState
 */
public class ShipPacket extends Packet {

    // Current state of the ship sending this packet
    private ShipState shipState;

    /**
     * Constructs a new <code>ShipPacket</code>.
     *
     * @param senderId   Session id of the sender
     * @param receiverId Session id of the target user
     * @param handlerId  Id of the ship generating this packet
     * @param shipState  The ship's current state
     */
    public ShipPacket(Long senderId, Long receiverId,
                      int handlerId, ShipState shipState) {

        super(senderId, receiverId, handlerId);
        this.shipState = shipState;

    }

    /**
     * Return the ship state object.
     *
     * @return ShipState object.
     */
    public ShipState getShipState() {
        return this.shipState;
    }

}

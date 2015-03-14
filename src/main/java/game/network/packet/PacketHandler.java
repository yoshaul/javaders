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

import game.network.client.GameNetworkManager;

/**
 * Each object in the game that creates or handles packet
 * should implement this interface.
 */
public interface PacketHandler {

    /**
     * Create and send packet(s) via the network manager.
     *
     * @param netManager Network manager.
     */
    public void createPacket(GameNetworkManager netManager);

    /**
     * Handle the incoming packet.
     *
     * @param packet Incoming packet.
     */
    public void handlePacket(Packet packet);

    /**
     * Returns the object network handler id that handles
     * the packets for this object. It might be the same object
     * or a different object.
     *
     * @return Id of the packet handlet that should handle
     * incoming packets.
     */
    public int getHandlerId();

}

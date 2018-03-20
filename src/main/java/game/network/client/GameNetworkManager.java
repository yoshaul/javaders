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

package game.network.client;

import game.gamestate.GameState;
import game.network.packet.Packet;

/**
 * The <code>GameNetworkManager</code> interface defines the methods
 * that a network manager for the <b>running game</b> should implement.
 * The various game components access the network methods through this
 * interface only to make it easy to create different network managers
 * in the future.
 */
public interface GameNetworkManager {

    /**
     * Gather network input relevant to the game state.
     * The network manager should callback the game
     * state's <code>gatherInput</code> method.
     *
     * @param gameState Current game state.
     */
    void gatherInput(GameState gameState);

    /**
     * Send packet to the network player.
     *
     * @param packet Packet to send.
     */
    void sendPacket(Packet packet);

    /**
     * Handle incoming packet. The implementing manager usually
     * queue the incoming packets to be processed by the
     * <code>GameState</code> in the gather input stage.
     *
     * @param packet
     */
    void handlePacket(Packet packet);

    /**
     * Returns the first packet in the incomig packets queue
     * and remove it from the queue.
     */
    Packet getNextPacket();

    /**
     * Returns this player session id.
     */
    Long getSenderId();

    /**
     * Returns the network player session id.
     */
    Long getReceiverId();

    /**
     * Returns true if this user initiated the network game (i.e., sent
     * the invitation to play).
     */
    boolean isInviter();

    /**
     * Do some cleanup before exiting.
     */
    void cleanup();

}

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

import game.ship.ShipModel;

import java.util.Collection;

/**
 * The <code>NewLevelPacket</code> is created and sent by the
 * controller player (the player whose computer is controlling
 * the random and special events in a network game) before a new
 * level is started. This packet contains details needed for the new level.
 */
public class NewLevelPacket extends Packet {

    private Collection<ShipModel> enemyShipsModels;

    /**
     * Constructs a new <code>NewLevelPacket</code>. The handler id
     * is not set since the levels manager is waiting for this packet
     * on the other side.
     *
     * @param senderId         Session id of the sender
     * @param receiverId       Session id of the target user
     * @param enemyShipsModels Collection of <code>ShipModel</code>
     *                         objects of the enemy ships for the new level.
     */
    public NewLevelPacket(Long senderId, Long receiverId,
                          Collection<ShipModel> enemyShipsModels) {

        super(senderId, receiverId);
        this.enemyShipsModels = enemyShipsModels;
    }

    /**
     * Returns the enemy ships models.
     *
     * @return Collection of enemy ship models.
     */
    public Collection<ShipModel> getEnemyShipsModels() {
        return this.enemyShipsModels;
    }
}

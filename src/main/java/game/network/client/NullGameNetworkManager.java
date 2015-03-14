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

public class NullGameNetworkManager implements GameNetworkManager {

    @Override
    public void gatherInput(GameState gameState) {
        // TODO Auto-generated method stub

    }

    @Override
    public void sendPacket(Packet packet) {
        // TODO Auto-generated method stub

    }

    @Override
    public void handlePacket(Packet packet) {
        // TODO Auto-generated method stub

    }

    @Override
    public Packet getNextPacket() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Long getSenderId() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Long getReceiverId() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isInviter() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void cleanup() {
        // TODO Auto-generated method stub

    }

}

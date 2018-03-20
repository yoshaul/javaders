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

import game.GameMenu;
import game.highscore.HighScore;
import game.network.packet.InvitationPacket;
import game.network.packet.Packet;
import game.network.server.ejb.OnlinePlayerModel;

import java.util.List;

public class NullNetworkManager implements NetworkManager {

    public NullNetworkManager(GameMenu gameMenu) {

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
    public Long login(String user, String password) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void logout() {
        // TODO Auto-generated method stub

    }

    @Override
    public void signup(String user, String password, String email) {
        // TODO Auto-generated method stub

    }

    @Override
    public void sendInvitation(Long sessionId) {
        // TODO Auto-generated method stub

    }

    @Override
    public void cancelInvitation() {
        // TODO Auto-generated method stub

    }

    @Override
    public void acceptInvitations(boolean accept) {
        // TODO Auto-generated method stub

    }

    @Override
    public void sendInvitationReply(InvitationPacket originalInvitation,
            boolean accepted) {
        // TODO Auto-generated method stub

    }

    @Override
    public List<OnlinePlayerModel> getAvailablePlayers() {
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
    public void postHighScore(HighScore score) {
        // TODO Auto-generated method stub

    }

    @Override
    public HighScore[] getTopTenScores() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public HighScore[] getHighScores(int fromRank, int toRank) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public GameNetworkManager getGameNetworkManager() {
        // TODO Auto-generated method stub
        return null;
    }

}

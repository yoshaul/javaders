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
import game.network.InvalidLoginException;
import game.network.packet.InvitationPacket;
import game.network.packet.Packet;
import game.network.server.ejb.OnlinePlayerModel;

import java.util.List;

public class NullNetworkManager implements NetworkManager {

    public NullNetworkManager(GameMenu gameMenu) {

    }

    public void sendPacket(Packet packet) {
        // TODO Auto-generated method stub

    }

    public void handlePacket(Packet packet) {
        // TODO Auto-generated method stub

    }

    public Long login(String user, String password) throws NetworkException,
            InvalidLoginException {
        // TODO Auto-generated method stub
        return null;
    }

    public void logout() throws NetworkException {
        // TODO Auto-generated method stub

    }

    public void signup(String user, String password, String email)
            throws NetworkException {
        // TODO Auto-generated method stub

    }

    public void sendInvitation(Long sessionId) throws NetworkException {
        // TODO Auto-generated method stub

    }

    public void cancelInvitation() throws NetworkException {
        // TODO Auto-generated method stub

    }

    public void acceptInvitations(boolean accept) throws NetworkException {
        // TODO Auto-generated method stub

    }

    public void sendInvitationReply(InvitationPacket originalInvitation,
                                    boolean accepted) throws NetworkException {
        // TODO Auto-generated method stub

    }

    public List<OnlinePlayerModel> getAvailablePlayers() throws NetworkException {
        // TODO Auto-generated method stub
        return null;
    }

    public Long getSenderId() {
        // TODO Auto-generated method stub
        return null;
    }

    public Long getReceiverId() {
        // TODO Auto-generated method stub
        return null;
    }

    public boolean isInviter() {
        // TODO Auto-generated method stub
        return false;
    }

    public void postHighScore(HighScore score) throws NetworkException {
        // TODO Auto-generated method stub

    }

    public HighScore[] getTopTenScores() throws NetworkException {
        // TODO Auto-generated method stub
        return null;
    }

    public HighScore[] getHighScores(int fromRank, int toRank)
            throws NetworkException {
        // TODO Auto-generated method stub
        return null;
    }

    public GameNetworkManager getGameNetworkManager() throws NetworkException {
        // TODO Auto-generated method stub
        return null;
    }

}

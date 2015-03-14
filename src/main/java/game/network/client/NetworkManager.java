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

import game.highscore.HighScore;
import game.network.InvalidLoginException;
import game.network.packet.InvitationPacket;
import game.network.packet.Packet;
import game.network.server.ejb.OnlinePlayerModel;

import java.util.List;

/**
 * The <code>NetworkManager</code> interface defines the methods
 * that a network manager for the <b>game menu</b> (before starting
 * to play) needs to implement.
 * The various game components access the network methods through this
 * interface only to make it easy to create different network managers
 * in the future.
 * The methods that are called from the game components should
 * throw the checked <code>NetworkException</code>.
 */
public interface NetworkManager {

    /**
     * Send a packet to the network.
     *
     * @param packet Packet to send.
     */
    public void sendPacket(Packet packet);

    /**
     * Handle incoming packet.
     *
     * @param packet Incoming packet.
     */
    public void handlePacket(Packet packet);

    /**
     * Login to the game server.
     *
     * @param user     Username.
     * @param password Password.
     * @throws InvalidLoginException If details are wrong.
     * @return Session id of the player.
     */
    public Long login(String user, String password)
            throws NetworkException, InvalidLoginException;

    /**
     * Logout of the server.
     */
    public void logout() throws NetworkException;

    /**
     * Register to the game server.
     *
     * @param user     Username
     * @param password Password
     * @param email    Email (optional)
     */
    public void signup(String user, String password, String email)
            throws NetworkException;

    /**
     * Send invitation to play to an online player.
     *
     * @param sessionId Session id of the invitee.
     */
    public void sendInvitation(Long sessionId) throws NetworkException;

    /**
     * Cancel the invitation to the last user.
     */
    public void cancelInvitation() throws NetworkException;

    /**
     * Sets the accept invitations flag of the user in the server.
     *
     * @param accept True to accept, false to deny.
     */
    public void acceptInvitations(boolean accept)
            throws NetworkException;

    /**
     * Send a reply to an invitation.
     *
     * @param originalInvitation The invitation packet
     * @param accepted           True if accepted, false otherwise.
     */
    public void sendInvitationReply(InvitationPacket originalInvitation,
                                    boolean accepted) throws NetworkException;

    /**
     * Returns a list of <code>OnlinePlayerModel</code> with the
     * online players details.
     *
     * @return List of available players.
     */
    public List<OnlinePlayerModel> getAvailablePlayers() throws NetworkException;

    /**
     * Returns the logged user session id.
     */
    public Long getSenderId();

    /**
     * Returns the destination user session id.
     */
    public Long getReceiverId();

    /**
     * Returns true if this user initiated the network game (i.e., sent
     * the invitation to play).
     */
    public boolean isInviter();

    /**
     * Post the player score to the server.
     *
     * @param score
     * @throws NetworkException
     */
    public void postHighScore(HighScore score) throws NetworkException;

    /**
     * Returns the top ten scores from the server.
     */
    public HighScore[] getTopTenScores() throws NetworkException;

    /**
     * Returns the high scores from place <code>fromRank</code>
     * to <code>toRank</code> inclusive.
     *
     * @param fromRank Starting rank.
     * @param toRank   Ending rank
     */
    public HighScore[] getHighScores(int fromRank, int toRank)
            throws NetworkException;

    /**
     * Returns a network manager for the running game.
     */
    public GameNetworkManager getGameNetworkManager()
            throws NetworkException;
}


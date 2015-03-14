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
 * <code>InvitationPacket</code> is used to invite player for an
 * online game and to send replies for invitations to play.
 */
public class InvitationPacket extends Packet {

    private final String userName;     // User name of the sender
    private boolean isReply;  // Is it a reply to invitation
    private boolean accepted; // Is the invitation accepted
    private boolean cancelled; // Is the invitation cancelled

    /**
     * Construct a new <code>InvitationPacket</code>
     *
     * @param senderId   Session id of the inviter
     * @param receiverId Session id of the invitee
     * @param userName   Username of the inviter
     */
    public InvitationPacket(Long senderId, Long receiverId, String userName) {
        super(senderId, receiverId);
        this.userName = userName;
        this.cancelled = false;
    }

    public String toString() {
        return super.toString() +
                " User name: " + userName +
                " isReply: " + isReply +
                " accepted: " + accepted +
                " cancelled: " + cancelled;
    }

    public String getUserName() {
        return userName;
    }

    public boolean isReply() {
        return isReply;
    }

    public void setReply(boolean isReply) {
        this.isReply = isReply;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}

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

import javax.jms.Destination;

/**
 * This class extends the <code>InvitationPacket</code>
 * class to add the JMS reply destination.
 */
public class JMSInvitationPacket extends InvitationPacket {

    // We use the replyToDestination to save the jms reply
    // to destination and pass it from the network manager
    // to the jms connection manager but not over the network 
    private transient Destination replyToDestination;

    public JMSInvitationPacket(Long senderID, Long receiverID, String userName, Destination destination) {

        super(senderID, receiverID, userName);
        this.replyToDestination = destination;

    }

    public Destination getReplyToDestination() {
        return replyToDestination;
    }

    public void setReplyToDestination(Destination replyToDestination) {
        this.replyToDestination = replyToDestination;
    }
}

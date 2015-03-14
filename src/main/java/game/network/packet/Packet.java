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

import java.io.Serializable;

/**
 * The <code>Packet</code> class is used as base class to deliver
 * messages between two game clients.
 */
public abstract class Packet implements Serializable {

    public Long senderId;        // Session id of the sender
    public Long receiverId;        // Session id of the target

    // Id of the game object who should handle this packet
    public int handlerId;
    private boolean consumed = false;    // Is this packet consumed

    /**
     * Construct new packet with target handler id equals to
     * default meaning: no special or not yet exists object.
     *
     * @param senderId   Session id of the sender
     * @param receiverId Session id of the target user
     */
    public Packet(Long senderId, Long receiverId) {
        this(senderId, receiverId, -1);
    }

    /**
     * Construct new packet.
     *
     * @param senderId   Session id of the sender
     * @param receiverId Session id of the target user
     * @param handlerId  Id of the object that should handle this packet
     */
    public Packet(Long senderId, Long receiverId, int handlerId) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.handlerId = handlerId;
    }

    /**
     * Returns true if this packet was marked as consumed
     * by some of the game objects.
     */
    public boolean isConsumed() {
        return consumed;
    }

    /**
     * Mark the <i>consumed</i> state of this packet. Generally
     * an object that handled the packet can mark it as consumed
     * so other objects won't have to check it.
     *
     * @param consumed Consumed state.
     */
    public void setConsumed(boolean consumed) {
        this.consumed = consumed;
    }

    public String toString() {

        return "Class: " + getClass() + " SenderID: " + senderId +
                " ReceiverID: " + receiverId;
    }

}

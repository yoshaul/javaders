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
import game.network.packet.PlayerQuitPacket;
import game.util.Logger;

import javax.jms.JMSException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * The <code>J2EEGameNetworkManager</code> implements the
 * <code>GameNetworkManager</code> interface and uses JMS
 * and J2EE API for the communication.
 */
public class J2EEGameNetworkManager implements GameNetworkManager {

    private JMSGameMessageHandler jmsGameMessageHandler;
    private PacketsSenderThread sender;

    private List inputQueue;        // List of incoming Packets
    private Collection outputQueue;    // Collection of outgoing Packets

    private boolean inviter;
    private Long senderId;
    private Long receiverId;

    /**
     * Construct the J2EEGameNetworkManager
     *
     * @param jmsGameHandler JMS messages handler
     * @param senderId       User session id
     * @param receiverId     Network player session id
     * @param inviter        True if this machine is the inviter
     */
    public J2EEGameNetworkManager(
            JMSGameMessageHandler jmsGameHandler,
            Long senderId, Long receiverId, boolean inviter) {

        jmsGameHandler.setGameNetworkManager(this);
        this.jmsGameMessageHandler = jmsGameHandler;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.inviter = inviter;
        this.inputQueue = new ArrayList();
        this.outputQueue = new ArrayList();

        // Create and start the PacketsSenderThread
        sender = new PacketsSenderThread(outputQueue, jmsGameHandler);
        sender.start();

    }

    /**
     * Iterate on the incoming packets queue and pass each packet
     * to the game state to handle. If the game state consumed the
     * packet, remove it from the queue.
     * We remove the packet only if it's consumed since it might
     * happen that the packet is for the next state.
     */
    @Override
    public void gatherInput(GameState gameState) {

        // Synchronize on the input queue
        synchronized (inputQueue) {
            Iterator inputQueueItr = inputQueue.iterator();
            while (inputQueueItr.hasNext()) {
                Packet packet = (Packet) inputQueueItr.next();

                gameState.handlePacket(packet);

                if (packet.isConsumed()) {
                    inputQueueItr.remove();
                }
            }
        }
    }

    /**
     * @return First packet in the input queue and removes the packet.
     * Null if no packet in the input queue.
     */
    @Override
    public Packet getNextPacket() {
        Packet ret = null;
        synchronized (inputQueue) {
            if (!inputQueue.isEmpty()) {
                ret = (Packet) inputQueue.get(0);
                inputQueue.remove(0);
            }

            return ret;
        }
    }

    /**
     * Add the packet to the output queue and notify the thread
     * waiting on the output queue monitor.
     */
    @Override
    public void sendPacket(Packet packet) {

        synchronized (outputQueue) {
            outputQueue.add(packet);

            outputQueue.notifyAll();
        }

    }

    /**
     * Add message to the input queue to be proccessed by the
     * game state when gather input is called.
     */
    @Override
    public void handlePacket(Packet packet) {
        synchronized (inputQueue) {
            inputQueue.add(packet);
        }
    }

    /**
     * Returns the user session id.
     */
    @Override
    public Long getSenderId() {
        return this.senderId;
    }

    /**
     * Returns the network user session id.
     */
    @Override
    public Long getReceiverId() {
        return this.receiverId;
    }

    /**
     * Returns true if this machine initialized the network game.
     */
    @Override
    public boolean isInviter() {
        return this.inviter;
    }

    /**
     * Clean and release resources. Send quit packet to the other
     * player if game is in progress.
     */
    @Override
    public void cleanup() {

        // Stop the sender thread
        sender.stopSending();
        /** TODO: check if we need this check */
//        if (gameInProgress) {
        // Send player quit packet
        System.out.println("\nSending playerQuitPacket\n");
        PlayerQuitPacket packet = new PlayerQuitPacket(senderId, receiverId);
        try {
            jmsGameMessageHandler.sendMessage(packet);
        } catch (JMSException jmse) {
            Logger.exception(jmse);
        }

//        }
    }
}

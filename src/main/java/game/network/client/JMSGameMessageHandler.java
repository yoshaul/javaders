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

import game.network.packet.Packet;
import game.util.Logger;

import javax.jms.*;

/**
 * The <code>JMSGameMessageHandler</code> is used by the
 * <code>GameNetworkManager</code> to send and receive messages
 * from the JMS server.
 */
public class JMSGameMessageHandler implements MessageListener {

    Session session;        // JMS session
    Queue privateQueue;        // Temporary private queue of the player
    Destination destination;    // Network player destination queue
    MessageConsumer gameConsumer;
    MessageProducer gameProducer;
    GameNetworkManager gameNetworkManager;

    /**
     * Create the jms handler for the game.
     *
     * @param session JMS session.
     */
    public JMSGameMessageHandler(Session session) throws JMSException {

        this.session = session;

        // Create temporary queue for the online game messages
        privateQueue = session.createTemporaryQueue();

        // Create consumer and set this object as the listener
        gameConsumer = session.createConsumer(privateQueue);
        gameConsumer.setMessageListener(this);

    }

    /**
     * New message received from the JMS queue. send to the network
     * manager to handle.
     */
    @Override
    public void onMessage(Message message) {

        if (message instanceof ObjectMessage) {
            try {
                Packet packet = (Packet) ((ObjectMessage) message).getObject();
                while (gameNetworkManager == null) {
                    // Loop until the network manager is ready
                    // It might not be ready at the beginning of the game
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException ie) {
                        // Ignore
                    }
                }

                gameNetworkManager.handlePacket(packet);

            } catch (JMSException jmse) {
                Logger.exception(jmse);
            }
        }
    }

    /**
     * Send the packet to the network player's queue.
     *
     * @param packet Packet to send.
     */
    public void sendMessage(Packet packet) throws JMSException {
        // Create new object message
        ObjectMessage message = session.createObjectMessage();

        message.setObject(packet);

        gameProducer.send(message);

    }

    /**
     * Sets the JMS destination and create a message producer for
     * the destination.
     *
     * @param destination New JMS destination (of the network player)
     */
    public void setDestination(Destination destination) throws JMSException {
        this.destination = destination;
        this.gameProducer = session.createProducer(destination);

    }

    /**
     * Returns this player private listening queue for incoming
     * game messages.
     */
    public Destination getPrivateQueue() {
        return this.privateQueue;
    }

    /**
     * Sets the game network manager.
     */
    public void setGameNetworkManager(GameNetworkManager manager) {
        this.gameNetworkManager = manager;
    }


}

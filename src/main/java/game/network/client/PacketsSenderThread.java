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

import javax.jms.JMSException;
import java.util.Collection;
import java.util.Iterator;

/**
 * The packet sender thread is used to send game packets
 * in a different thread than the game loop thread.
 */
public class PacketsSenderThread extends Thread {

    /**
     * Collection of outgoing packets
     */
    private Collection outputQueue;

    private JMSGameMessageHandler jmsMessageHandler;
    private boolean stopped = false;

    /**
     * Construct the packets sender.
     *
     * @param outputQueue       Queue for the outgoing packets.
     * @param jmsMessageHandler JMS sender to send the packets.
     */
    public PacketsSenderThread(Collection outputQueue,
                               JMSGameMessageHandler jmsMessageHandler) {

        this.outputQueue = outputQueue;
        this.jmsMessageHandler = jmsMessageHandler;
    }

    /**
     * Loop untill stopped and send packets from the queue
     * when they are available.
     */
    @Override
    public void run() {
        while (!stopped) {
            try {
                synchronized (outputQueue) {
                    while (outputQueue.isEmpty()) {
                        outputQueue.wait();
                    }

                    // Get and send all messages
                    Iterator itr = outputQueue.iterator();
                    while (itr.hasNext()) {
                        Packet packet = (Packet) itr.next();
                        jmsMessageHandler.sendMessage(packet);
                        itr.remove();
                    }

                    outputQueue.notifyAll();
                    Thread.yield();
                }
            } catch (InterruptedException ie) {
                // Ignore and continue
            } catch (JMSException jmse) {
                Logger.exception(jmse);
            }
        }
    }

    /**
     * Stop the thread.
     */
    public void stopSending() {
        stopped = true;
    }

}

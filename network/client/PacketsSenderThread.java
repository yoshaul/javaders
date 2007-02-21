
package game.network.client;

import game.network.packet.Packet;
import game.util.Logger;

import java.util.Collection;
import java.util.Iterator;

import javax.jms.JMSException;

/**
 * The packet sender thread is used to send game packets
 * in a different thread than the game loop thread.
 */
public class PacketsSenderThread extends Thread {

    /** Collection of outgoing packets */
    private Collection outputQueue;
    
    private JMSGameMessageHandler jmsMessageHandler;
    private boolean stopped = false;
    
    /**
     * Construct the packets sender.
     * @param outputQueue	Queue for the outgoing packets.
     * @param jmsMessageHandler	JMS sender to send the packets.
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
            }
            catch (InterruptedException ie) {
                // Ignore and continue
            }
            catch (JMSException jmse) {
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

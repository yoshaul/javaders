package game.network;

import game.network.packet.Packet;

import java.util.Collection;
import java.util.Iterator;

public class PacketsSender implements Runnable {

    private Collection outputQueue; 
    private JMSGameListener jmsGameListener;
    
    public PacketsSender(Collection outputQueue, 
            JMSGameListener jmsGameListener) {
        
        this.outputQueue = outputQueue;
        this.jmsGameListener = jmsGameListener;
    }
    
    public void run() {
        
        while (true) {
            
            synchronized (outputQueue) {
                
                while (outputQueue.isEmpty()) {
                    try {
                        outputQueue.wait();
                    }
                    catch (InterruptedException ie) {
                        // Ignore and continue
                    }
                }
                
                // Get and send all messages
                Iterator itr = outputQueue.iterator();
                while (itr.hasNext()) {
                    Packet packet = (Packet) itr.next();
                    jmsGameListener.sendMessage(packet);
                    itr.remove();
                }
                
                outputQueue.notifyAll();
                
                Thread.yield();
            }
        }
    }
    
}

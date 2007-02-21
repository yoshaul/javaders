
package game.network.client;

import java.io.IOException;

/**
 * General NetworkException to ease the network exception
 * handling on the client side. 
 */
public class NetworkException extends IOException {

    public NetworkException(String message) {
        super(message);
    }
    
}

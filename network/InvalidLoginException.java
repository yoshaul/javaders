
package game.network;

/**
 * The <code>InvalidLoginException</code> is thrown by the server
 * side if the user tried to login with the wrong user name or password
 * and by the client if the ticket (the session id) received from server
 * was null. 
 */
public class InvalidLoginException extends Exception {

	public InvalidLoginException() {
		super("Invalid login information");
	}
	
	public InvalidLoginException(String message) {
	    super(message);
	}
	
}
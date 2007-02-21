
package game.network;

/**
 * @author Yossi Shaul
 *
 */
public class InvalidLoginException extends Exception {

	public InvalidLoginException() {
		super("Invalid login information");
	}
	
}
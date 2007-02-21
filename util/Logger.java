
package game.util;

import java.awt.Component;

import javax.swing.JOptionPane;

/**
 * The <code>Logger</code> class is a simple logging helper.
 * Most of the game errors and exceptions are sent to the Logger.
 */
public class Logger {

    private static boolean debug;	// debug mode flag
    
    /**
     * Initializes the Logger, check if in debug mode.
     * This method should be called once when the game starts.
     * (can also be used to redirect out streams)
     */
    public static void init(String debugStr) {
        debug = (debugStr != null && debugStr.equals("debug"));
    }
    
   
    /**
     * Print the message to the screen.
     * @param message	Message to display. 
     */
    public static void screen(String message) {
        System.out.println(message);
    }
    
    /**
     * Print the error to the screen.
     * @param error	Error message to display.
     */
    public static void error(String error) {
        System.err.println(error);
    }
    
    /**
     * Prints the stack trace of the exception to the screen.
     * @param e	Exception to print.
     */
    public static void exception(Exception e) {
        e.printStackTrace();
    }
    
    /**
     * Displays a <code>JOptionPane</code> error dialog.
     * @param parent	Parent dialog window.
     * @param message	Message to display.
     */
    public static void showErrorDialog(Component parent, String message) {
        
        JOptionPane.showMessageDialog(parent, message, 
                "Error", JOptionPane.ERROR_MESSAGE);
        
    }
    
    /**
     * Prints to the screen the prefix and the total used memory if in
     * debug mode.
     * @param prefix	Prefix message
     */
    public static void printMemoryUsage(String prefix) {
        if (isDebug()) {
            Runtime runtime = Runtime.getRuntime(); 
            long usedMemory = runtime.totalMemory() - runtime.freeMemory();
            screen(prefix + "\t" + usedMemory);
        }
    }
    
    /**
     * Returns true if the debug flasg is on. The debug flag is
     * taket from the system property.
     * @return	True if in debug mode
     */
    public static boolean isDebug() {
        return debug;
    }
    
}

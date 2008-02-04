
package game.util;

import game.GameConstants;

import java.awt.*;
import java.io.InputStream;
import java.net.URL;

import javax.swing.ImageIcon;

/**
 * The <code>ResourceManager</code> class contains some static methods
 * for various resource loading and management. 
 */
public class ResourceManager {

	private static ClassLoader cl = ResourceManager.class.getClassLoader();
    private static Font gameFont;
    private static Cursor gameCursor, invisibleCursor;
    
    static {
        init();
    }
    
    /**
     * Initialize and preload resources.
     */
    private static void init() {
        
        // Create the game font
        try {
        	String path = GameConstants.RESOURCES + "/" + GameConstants.GAME_FONT;
        	InputStream is = getResourceAsStream(path);
	        gameFont = Font.createFont(Font.TRUETYPE_FONT, is); 
        }
        catch (Exception e) {
//            Logger.exception(e);
            gameFont = new Font("Serif", Font.PLAIN, 1);
            throw new RuntimeException(e);
        }
        
        // Load the game cursors
        invisibleCursor = getCursor("");
        gameCursor = getCursor(GameConstants.IMAGES_DIR + 
                GameConstants.GAME_CURSOR);
        
    }
    
    public static URL getResource(String resource) {
    	return cl.getResource(resource);
    }
    
    public static InputStream getResourceAsStream(String resource) {
    	return cl.getResourceAsStream(resource);
    }
    
    /**
     * Returns the game font with the specified font style and size.
     * @param style	Style of the font (e.g., Font.BOLD).
     * @param size	Size of the font.
     * @return	Game font with the specified font style and size.
     */
    public static Font getFont(int style, int size) {

        return gameFont.deriveFont(style, size);
    }
    
    /**
     * Returns the game font with the PLAIN style and input size.
     * @param size Size of the font.
     * @return	Plain game font with the specified size. 
     */
    public static Font getFont(int size) {
        
        return getFont(Font.PLAIN, size);
    }
    
    /**
     * Returns a new <code>Cursor</code> object created from the input 
     * image name. Expects image of size 32x32 and places the hot spot in
     * the middle of the image.
     * If the requested cursor is invisible or the default one, return the
     * pre-created cursor.
     * @param imageName	Name of the image to be loaded as the cursor image
     * @return	A cursor
     */
    public static Cursor getCursor(String imageName) {
        Cursor cursor;
        
        if (imageName.equals("") && invisibleCursor != null) {
            cursor = invisibleCursor;
        } 
        else if (imageName.equals(
                GameConstants.GAME_CURSOR) && gameCursor != null) {
            cursor = gameCursor;
        }
        else {
        	Image image = imageName.equals("") ? 
        			new ImageIcon("").getImage() : loadImage(imageName);
            cursor = 
                Toolkit.getDefaultToolkit().createCustomCursor(
                		image , new Point(16, 16), imageName);
        }
        
        return cursor;
        
    }
    
    /**
     * Loads and returns an image.
     * @param imageName	Image name to load
     */
    public static Image loadImage(String imageName) {
//        return new ImageIcon(imageName).getImage();
    	return new ImageIcon(getResource(imageName)).getImage();
    }
}


package game.graphic;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * The graphics helper has some static methods to help
 * creating, loading and drawing graphics.
 */
public class GraphicsHelper {
    
    /**
     * Returns a compatible image for the configuration of the window 
     * with the specified width, heigh and transparency.
     * @param window	Window object
     * @param width		Image width
     * @param height	Image height
     * @param transparency	Image transparency
     * @return compatible image with the specified width, heigh
     * and transparency.
     */
    public static BufferedImage getCompatibleImage(Window window,
            int width, int height, int transparency) {

        GraphicsConfiguration gc = window.getGraphicsConfiguration();
        
        return getCompatibleImage(width, height, transparency, gc);
        
    }
    
    /**
     * Returns a compatible image for the default configuration 
     * with the specified width, heigh and transparency.
     * @param width		Image width
     * @param height	Image height
     * @param transparency	Image transparency
     * @return compatible image with the specified width, heigh
     * and transparency.
     */
    public static BufferedImage getCompatibleImage(
            int width, int height, int transparency) {

        GraphicsEnvironment ge = 
            GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsConfiguration gc = 
            ge.getDefaultScreenDevice().getDefaultConfiguration();
        
        return getCompatibleImage(width, height, transparency, gc);
        
    }
    
    /**
     * Returns a compatible image for the graphics configuration 
     * with the specified width, heigh and transparency.
     * @param width		Image width
     * @param height	Image height
     * @param transparency	Image transparency
     * @return compatible image with the specified width, heigh
     * and transparency.
     */
    private static BufferedImage getCompatibleImage(int width, 
            int height, int transparency, GraphicsConfiguration gc) {
        
        return gc.createCompatibleImage(width, height, transparency);
        
    }
    
    /**
     * Draws the text in the center of the image.
     * @param g		The graphics device
     * @param image	Image to draw on
     * @param text	Text to draw
     */
    public static void drawInMiddle(Graphics g, Image image, String text) {
        
		int width = image.getWidth(null);
		int height = image.getHeight(null);
		
        FontMetrics fm = g.getFontMetrics();
        int midX = (width - fm.stringWidth(text)) / 2;
        int midY = (height + fm.getHeight()/2) / 2;

        g.drawString(text, midX, midY);
		
    }
    
    public static void setAntialiasedText(Graphics2D g) {
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, 
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    }
    
}


package game;

import game.graphic.GraphicsHelper;
import game.util.Logger;
import game.util.ResourceManager;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JFrame;

/**
 * The <code>ScreenManager</code> class handles the graphic environment
 * and display settings. It holds the drawing area frame of the game 
 * and has some helper methods for grapihcs and images.
 * The screen manager uses a <code>BufferStrategy</code> to manage
 * the double buffering of the game frame (to prevent flickering).
 * The <code>ScreenManager</code> is a singleton so only one object exists.
 */
public class ScreenManager {

    private static ScreenManager screenManager;
    private GraphicsDevice gd;
    private DisplayMode oldDM;
    private boolean fullScreen = true;
    private JFrame gameFrame;
    private boolean debugMode;
    
    /**
     * Private constructor to allow only one instance of the 
     * <code>ScreenManager</code> class.
     */
    private ScreenManager() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
    	gd = ge.getDefaultScreenDevice();
    	oldDM = gd.getDisplayMode();
    	gameFrame = new JFrame();
    	debugMode = Logger.isDebug();
    }
    
    /**
     * Returns the single <code>ScreenManager</code> instance. 
     * @return	Screen manager instance
     */
    public static ScreenManager getInstance() {
        if (screenManager == null) {
            screenManager = new ScreenManager();
        }
        return screenManager;
    }
    
    /**
     * Sets full screen mode with the default display mode (screen 
     * resolution of 800x600, bit depth of 32 and the current screen
     * refresh rate.
     *
     */
    public void setFullScreen() {
        DisplayMode displayMode = new DisplayMode(800, 600, 32,
                DisplayMode.REFRESH_RATE_UNKNOWN);
        setFullScreen(displayMode);
    }
    
    /**
     * Sets full screen mode with the displayMode parameters.
     * If the current machine doesn't support full screen mode
     * we use an undecorated frame with the size 500x600.
     * @param displayMode Full screen display mode.
     */
    public void setFullScreen(DisplayMode displayMode) {
        
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        if (!debugMode) {
            gameFrame.setUndecorated(true);
        }
        gameFrame.setIgnoreRepaint(true);
        gameFrame.setResizable(false);
        gameFrame.setFocusable(true);
        gameFrame.requestFocus();

    	if (!debugMode && gd.isFullScreenSupported()) {

    	    gd.setFullScreenWindow(gameFrame);
    	    
    	    if(gd.isDisplayChangeSupported()) {
        	    gd.setDisplayMode(displayMode);
                // Fix for Mac OS X
                gameFrame.setSize(displayMode.getWidth(), 
                        displayMode.getHeight());
    	    }
    	} 
    	
    	else { // Full screen is not supported

    	    gameFrame.setSize(500,600);
    	    gameFrame.setVisible(true);
    	}
        
        // Create a buffer strategy for the game frame
        // Avoid potential deadlock in JDK 1.4
	    // The invokeAndWait cannot be called from event dispatcher
	    // thread meaning, not as an action of the AWT/Swing
        try {
            EventQueue.invokeAndWait(
                new Runnable() {
                    public void run() {
                        gameFrame.createBufferStrategy(2);
                    }
                }
            ); 
        }
        catch (InterruptedException ie) {
            Logger.exception(ie);
        }
        catch (InvocationTargetException  ite) {
            Logger.exception(ite);
        }
        
    }

    /**
     * Returns the game frame window.
     * @return	Game frame 
     */
    public JFrame getFullScreenWindow() {
        if (fullScreen) {
            return (JFrame) gd.getFullScreenWindow();    
        }
        else {
            return gameFrame;
        }
        
    }
    
    /**
     * Exits the full screen mode.
     */
    public void exitFullScreen() {
    	
        if (fullScreen) {
    	    if (gd.isDisplayChangeSupported()) {
    	        gd.setDisplayMode(oldDM);    
    	    }
          
            /** TODO: why is this throwing error? */ 
            Window window = gd.getFullScreenWindow();
            if (window != null) {
                window.dispose();
            }
            
        	gd.setFullScreenWindow(null);
        }
        else {
            gameFrame.setVisible(false);
            gameFrame.dispose();
        }
    	
    }
    
    /**
     * Returns the graphics object of the game frame.
     * @return	Graphics object of the game frame.
     */
    public Graphics2D getGraphics() {
        BufferStrategy bs = gameFrame.getBufferStrategy();
        return (Graphics2D) bs.getDrawGraphics();
    }
    
    /**
     * Show the contents of the game frame. This method should
     * be called after the current frame rendering is finished
     * to display the results. 
     */
    public void show() {
        BufferStrategy bs = gameFrame.getBufferStrategy();
        if (!bs.contentsLost()) {
            bs.show();
        }
    }
    
    /**
     * Returns the game frame screen dimensions.
     * @return The game frame screen dimension.
     */
    public Dimension getScreenDimension() {
        return gameFrame.getSize();
    }
    
    /**
     * Returns the game frame insets.
     * @return The game frame insets.
     */
    public Insets getScreenInsets() {
        return gameFrame.getInsets();
    }
    
    /**
     * Creates and returns a compatible image from the image found in
     * <code>imageName</code> image under the images folder. The source
     * image will automatically be resized to the game frame window if it's
     * to big.
     * @param imageName	Source image name
     * @param transparency Image transparency
     * @see java.awt.GraphicsConfiguration
     */
    public BufferedImage getCompatibleImage(String imageName, int transparency) {

        Image srcImage = ResourceManager.loadImage(
                GameConstants.IMAGES_DIR + imageName);
        
        int width = srcImage.getWidth(null);
        int height = srcImage.getHeight(null);
        
        BufferedImage compatibleImage = 
            getCompatibleImage(width, height, transparency);

        Graphics g = compatibleImage.getGraphics();
        g.drawImage(srcImage, 0, 0, width, height, null);
        g.dispose();
        
        return compatibleImage;
    }
    
    /**
     * Returns a compatible image with the specified width, heigh
     * and transparency.
     * @param width		Image width
     * @param height	Image height
     * @param transparency	Image transparency
     * @return compatible image with the specified width, heigh
     * and transparency.
     */
    public BufferedImage getCompatibleImage(int width, int height, 
            int transparency) {

        return GraphicsHelper.getCompatibleImage(gameFrame,
                width, height, transparency);
        
    }
    
    /**
     * Displays or hides the mouse cursor. 
     * @param show	True to show the cursor false to hide it.
     */
    public void showCursor(boolean show) {
        
        String cursorName = show ? GameConstants.GAME_CURSOR : "";
        
        gameFrame.setCursor(ResourceManager.getCursor(cursorName));
    }
    
}

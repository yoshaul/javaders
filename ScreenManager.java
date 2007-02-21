package game;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JComponent;
import javax.swing.JFrame;

public class ScreenManager {

    private GraphicsEnvironment ge;
    private GraphicsDevice gd;
    private DisplayMode oldDM;
    private boolean fullScreen;
    private JFrame gameFrame = new JFrame();
    
    public ScreenManager() {
    	ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
    	gd = ge.getDefaultScreenDevice();
    	oldDM = gd.getDisplayMode();
    }
    
    public void setFullScreen() {
        DisplayMode displayMode = new DisplayMode(800, 600, 32, 75);
        setFullScreen(displayMode);
    }
    
    public void setFullScreen(DisplayMode displayMode) {

        
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
//        gameFrame.setUndecorated(true);
        gameFrame.setIgnoreRepaint(true);
        gameFrame.setResizable(false);

//        gameFrame.setFocusable(true);
//        gameFrame.requestFocus();

//    	if (gd.isFullScreenSupported()) {
        if (false) {

//    	    synchronized(gameFrame.getTreeLock()) {	// Locks the frame done some problems
        	    gd.setFullScreenWindow(gameFrame);
        	    
        	    if(gd.isDisplayChangeSupported()) {
            	    gd.setDisplayMode(displayMode);
                    // Fix for Mac OS x
                    gameFrame.setSize(displayMode.getWidth(), 
                            displayMode.getHeight());
        	    }
    	} 
    	
    	else {
    	    /** TODO: */
    	    System.err.println("full screen is not supported!!!!!");  
    	    gameFrame.resize(500,600);
    	    gameFrame.show();
    	}
        
        // Avoid potential deadlock in JDK 1.4
	    // The invokeAndWait cannot be called from event dispatcher
	    // thread meaning not as an action of the AWT/Swing
        try {
            EventQueue.invokeAndWait(
                new Runnable() {
                    public void run() {
                        gameFrame.createBufferStrategy(2);    
                    }
                }
            );
        }
        catch (InterruptedException ex) {
            // Ignore
        }
        catch (InvocationTargetException  ex) {
            // Ignore
        }
    	
    }

    
    public JFrame getFullScreenWindow() {
        if (fullScreen) {
            return (JFrame) gd.getFullScreenWindow();    
        }
        else {
            return gameFrame;
        }
        
    }
    
//    public Graphics2D getGraphics() {
//        Window window = gd.getFullScreenWindow();
//        if (window != null) {
//            BufferStrategy strategy = window.getBufferStrategy();
//            return (Graphics2D)strategy.getDrawGraphics();
//        }
//        else {
//            return null;
//        }
//    }
    
    public void exitFullScreen() {
    	
        if (fullScreen) {
    	    if (gd.isDisplayChangeSupported()) {
    	        gd.setDisplayMode(oldDM);    
    	    }
          
            /** todo: why is this throwing error? */ 
//            Window window = gd.getFullScreenWindow();
//            if (window != null) {
//                window.dispose();
//            }
            
        	gd.setFullScreenWindow(null);
            
        }
        
        else {
            gameFrame.show(false);
            gameFrame.dispose();
        }
    	
    }
    
    public Graphics getGraphics() {
        BufferStrategy bs = gameFrame.getBufferStrategy();
        return bs.getDrawGraphics();
    }
    
    public void show() {
        BufferStrategy bs = gameFrame.getBufferStrategy();
        if (!bs.contentsLost()) {
            bs.show();
        }
    }
    
    public Dimension getScreenDimension() {
        /** TODO: deal with insets if not in full screen ? */
        return gameFrame.getSize();
    }
    
}

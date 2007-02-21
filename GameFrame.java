
package game;

import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import javax.swing.JFrame;

public class GameFrame extends JFrame {

    public GameFrame() {
        
        super("Game");
        
    	setIgnoreRepaint(true);
        setResizable(false);
        setUndecorated(true);

        setFocusable(true);
        requestFocus();
        
    	GraphicsEnvironment ge = 
    	    GraphicsEnvironment.getLocalGraphicsEnvironment();
    	GraphicsDevice gd = ge.getDefaultScreenDevice();

    	if (gd.isFullScreenSupported()) {
    	    /** TODO: maybe the sync will solve my problems... */
    	    synchronized(this.getTreeLock()) {
    	        DisplayMode dm = new DisplayMode(800, 600, 32, 75);
        	    gd.setFullScreenWindow(this);
        	    gd.setDisplayMode(dm);
        	    createBufferStrategy(2);    
    	    }
    	    
    	} 
    	else {
    	    /** TODO: */
    	    dispose();
    	}
        
    }
    
    public void exitFullScreen() {
        
    	GraphicsEnvironment ge = 
    	    GraphicsEnvironment.getLocalGraphicsEnvironment();
    	GraphicsDevice gd = ge.getDefaultScreenDevice();
    	
    	gd.setFullScreenWindow(null);
    	dispose();
    	
    }
    
}

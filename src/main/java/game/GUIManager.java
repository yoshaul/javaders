
package game;

import java.awt.*;
import java.lang.reflect.InvocationTargetException;

import javax.swing.*;

/**
 * The <code>GUIManager</code> is a helper class to make the Swing
 * components work with the active rendering used in the game when it
 * is running (not in the game menu).
 * This class installs a <code>RepaintManager</code> that ignores
 * the repaints from swing components so they won't interrupt the
 * game. We call the <code>render</code> method of this class when we
 * want to render game dialogs (which extend <code>JPanel</code>). 
 */
public class GUIManager {

    private GameLoop gameLoop;
    private ScreenManager screenManager;
    private RepaintManager oldRepaintManager;
    
    public GUIManager(GameLoop gameLoop, ScreenManager screenManager) {
        
        this.gameLoop = gameLoop;
        this.screenManager = screenManager;
        
        // Save the current RepaintManager to restore later
        oldRepaintManager = RepaintManager.currentManager(null);
        // Set new RepaintManager that ignores repainting
        RepaintManager.setCurrentManager(new NullRepaintManager());
        
        JFrame gameFrame = screenManager.getFullScreenWindow();
        
        Container container = gameFrame.getContentPane();
        	((JComponent)container).setOpaque(false);

        gameFrame.validate();
    }

    /**
     * Render the layered pane and all its sub components.
     */
    public void render(final Graphics g) {

        final JFrame gameFrame = screenManager.getFullScreenWindow();

        // Use the EventQueue.invokeAndWait to prevent deadlocks
        if (!SwingUtilities.isEventDispatchThread()) {
	        try {
	            EventQueue.invokeAndWait(
	                new Runnable() {
	                    public void run() {
	                        gameFrame.getLayeredPane().paintComponents(g);
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
        else {          
            gameFrame.getLayeredPane().paintComponents(g);    
        }

    }
    
    /**
     * Adds a dialog to the modal layer pane of the game frame
     * @param dialog Dialog to add
     */
    public void addDialog(JPanel dialog) {
        gameLoop.getScreenManager().getFullScreenWindow().
			getLayeredPane().add(dialog, JLayeredPane.MODAL_LAYER);
    }
    
    /**
     * Restores the original <code>RepaintManager</code>.
     */
    public void restoreRepaintManager() {
        RepaintManager.setCurrentManager(oldRepaintManager);
    }
    
    /**
     * We use the NullRepaintManager to disable all the repainting
     * since all the painting is done from the game loop
     */
	private class NullRepaintManager extends RepaintManager {
	
	    public NullRepaintManager() {
	        setDoubleBufferingEnabled(false);
	    }
	
	    public void addInvalidComponent(JComponent c) {
	        // do nothing
	    }
	
	    public void addDirtyRegion(JComponent c, int x, int y,
	        int w, int h)
	    {
	        // do nothing
	    }
	
	    public void markCompletelyDirty(JComponent c) {
	        // do nothing
	    }
	
	    public void paintDirtyRegions() {
	        // do nothing
	    }
	
	}
    
}

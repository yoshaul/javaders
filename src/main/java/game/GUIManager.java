/*
 * This file is part of Javaders.
 * Copyright (c) Yossi Shaul
 *
 * Javaders is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Javaders is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Javaders.  If not, see <http://www.gnu.org/licenses/>.
 */

package game;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;

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
        ((JComponent) container).setOpaque(false);

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
                EventQueue.invokeAndWait(() -> gameFrame.getLayeredPane().paintComponents(g));
            } catch (InterruptedException | InvocationTargetException ex) {
                // Ignore
            }
        } else {
            gameFrame.getLayeredPane().paintComponents(g);
        }

    }

    /**
     * Adds a dialog to the modal layer pane of the game frame
     *
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

        NullRepaintManager() {
            setDoubleBufferingEnabled(false);
        }

        @Override
        public void addInvalidComponent(JComponent c) {
            // do nothing
        }

        @Override
        public void addDirtyRegion(JComponent c, int x, int y,
                                   int w, int h) {
            // do nothing
        }

        @Override
        public void markCompletelyDirty(JComponent c) {
            // do nothing
        }

        @Override
        public void paintDirtyRegions() {
            // do nothing
        }

    }

}

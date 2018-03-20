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

package game.gui;

import game.highscore.HighScore;
import game.highscore.HighScoresManager;
import game.highscore.HighScoresRenderer;
import game.network.client.NetworkException;
import game.util.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;

/**
 * The <code>HighScoresDialog</code> displays the game high scores.
 * It can display the local high scores or the high scores from
 * the server. We can also clear the local high scores.
 */
public class HighScoresDialog extends GameDialog {

    private HighScoresManager highScoresManager;
    private HighScore[] highScores;
    private JButton closeButton, clearButton,
            localScoresButton, networkScoresButton;
    private JPanel renderPanel;

    /**
     * Construct the dialog.
     *
     * @param owner             Dialog owner frame.
     * @param modal             True if it is a modal dialog
     * @param highScoresManager The high scores manager..
     */
    public HighScoresDialog(JFrame owner, boolean modal,
                            HighScoresManager highScoresManager) {

        super(owner, modal);

        this.highScoresManager = highScoresManager;
        this.highScores = highScoresManager.getHighScores();

        createGUI();

    }

    /**
     * Create the dialog UI.
     */
    @Override
    protected void createGUI() {

        setVisible(false);
        setResizable(false);

        Container container = getContentPane();
        container.setLayout(new BorderLayout());

        renderPanel = createPanel(null);
        container.add(renderPanel, BorderLayout.CENTER);

        JPanel buttonsPanel = createPanel(new GridLayout(2, 2));

        closeButton = createButton("Close", "", BTN_SMALL_IMAGE);
        buttonsPanel.add(closeButton);

        clearButton = createButton("Clear",
                "Clears the local high scores", BTN_SMALL_IMAGE);
        buttonsPanel.add(clearButton);

        localScoresButton = createButton("Local HS",
                "Display the local high scores", BTN_SMALL_IMAGE);
        buttonsPanel.add(localScoresButton);

        networkScoresButton = createButton("Network HS",
                "Display the high scores from the server", BTN_SMALL_IMAGE);
        networkScoresButton.addActionListener(this);
        buttonsPanel.add(networkScoresButton);

        container.add(buttonsPanel, BorderLayout.SOUTH);

        setSize(350, 400);

        centralizeOnScreen();

    }

    /**
     * Override the paint method to paint the high scores on
     * the render panel.
     */
    @Override
    public void paint(Graphics g) {
        super.paint(g);

        Graphics rg = renderPanel.getGraphics();

        Rectangle bounds = new Rectangle(0, 0,
                renderPanel.getWidth(), renderPanel.getHeight());

        // Paint high scores on the render panel
        HighScoresRenderer.render(rg, bounds, highScores);

    }

    /**
     * React to user input.
     */
    @Override
    public void actionPerformed(ActionEvent event) {

        if (event.getSource() == closeButton) {
            setVisible(false);
        } else if (event.getSource() == clearButton) {
            highScoresManager.clearHighScores();
            try {
                highScoresManager.saveHighScores();
            } catch (IOException ioe) {
                Logger.exception(ioe);
            }
            repaint();
        } else if (event.getSource() == localScoresButton) {
            highScores = highScoresManager.getHighScores();
            repaint();
        } else if (event.getSource() == networkScoresButton) {
            getNetworkHighScores();
        }
    }

    /**
     * Gets the high scores from the server and displays them.
     */
    private void getNetworkHighScores() {
        try {
            highScores = highScoresManager.getNetworkHighScores(1, 10);
        } catch (NetworkException ne) {
            Logger.showErrorDialog(getOwner(), ne.getMessage());
        }

        repaint();
    }

}

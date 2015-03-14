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

import game.ScreenManager;
import game.input.InputManager;
import game.util.ResourceManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * The <code>QuitDialog</code> appears when the user clicks
 * on the quit button to verify she realy wants to quit.
 */
public class QuitDialog extends InGameDialog {

    private InputManager inputManager;

    private JButton yesButton, noButton;

    /**
     * Construct the dialog.
     *
     * @param screenManager Reference to the screen manager.
     * @param inputManager  Input manager.
     */
    public QuitDialog(ScreenManager screenManager,
                      InputManager inputManager) {

        super(screenManager, DEFAULT_BG_IMAGE);
        this.inputManager = inputManager;
        createGUI();

    }

    /**
     * Set up the dialog GUI.
     */
    protected void createGUI() {

        JLabel label = new JLabel("Leaving so soon?", SwingConstants.CENTER);
        label.setFont(ResourceManager.getFont(Font.BOLD, 16));

        this.add(label, BorderLayout.CENTER);

        JPanel buttonsPanel = new JPanel(new GridLayout(1, 2));
        buttonsPanel.setOpaque(false);

        yesButton = createButton("Yes", DEFAULT_BTN_IMAGE);
        noButton = createButton("No", DEFAULT_BTN_IMAGE);

        buttonsPanel.add(yesButton);
        buttonsPanel.add(noButton);

        this.add(buttonsPanel, BorderLayout.SOUTH);

        this.setSize(300, 100);

        centralizeOnScreen();

    }

    /**
     * Handle the user input.
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == noButton) {
            this.setVisible(false);
            screenManager.showCursor(false);
            inputManager.setQuit(false);
        } else if (e.getSource() == yesButton) {
            this.setVisible(false);
            screenManager.showCursor(false);
            inputManager.setQuit(true);
        }

    }

}

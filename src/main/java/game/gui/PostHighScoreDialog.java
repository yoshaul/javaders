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

import game.GameLoop;
import game.highscore.HighScore;
import game.highscore.HighScoresManager;
import game.network.client.NetworkException;
import game.util.ResourceManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * The <code>PostHighScoreDialog</code> appears when the player
 * finishes a game asking to post the score to the server.
 */
public class PostHighScoreDialog extends InGameDialog {

    private HighScoresManager highScoresManager;
    private JTextField nameField;
    private JButton yesButton, noButton;
    private boolean finished;
    private HighScore score;

    /**
     * Construct the dialog.
     *
     * @param gameLoop          Reference to the game loop
     * @param highScoresManager High scores manager object.
     */
    public PostHighScoreDialog(GameLoop gameLoop,
                               HighScoresManager highScoresManager) {

        super(gameLoop.getScreenManager(), DEFAULT_BG_IMAGE);
        this.highScoresManager = highScoresManager;

        createGUI();

    }

    /**
     * Set up the GUI.
     */
    private void createGUI() {

        JPanel labelPanel = new JPanel();
        labelPanel.setOpaque(false);

        JLabel label = new JLabel("Post your score to the server?");
        label.setFont(ResourceManager.getFont(Font.BOLD, 16));
        labelPanel.add(label);

        JPanel inputPanel = new JPanel(new FlowLayout());
        inputPanel.setOpaque(false);

        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setFont(ResourceManager.getFont(Font.BOLD, 16));
        inputPanel.add(nameLabel);

        nameField = new JTextField(10);
        nameField.setFont(ResourceManager.getFont(Font.PLAIN, 16));
        inputPanel.add(nameField);

        JPanel northPanel = new JPanel(new GridLayout(2, 1));
        northPanel.setOpaque(false);

        northPanel.add(labelPanel);
        northPanel.add(inputPanel);

        this.add(northPanel, BorderLayout.NORTH);

        JPanel buttonsPanel = new JPanel(new GridLayout(1, 2));
        buttonsPanel.setOpaque(false);

        yesButton = createButton("Yes", DEFAULT_BTN_IMAGE);
        noButton = createButton("No", DEFAULT_BTN_IMAGE);

        buttonsPanel.add(yesButton);
        buttonsPanel.add(noButton);

        this.add(buttonsPanel, BorderLayout.SOUTH);

        this.setSize(this.getPreferredSize());

        centralizeOnScreen();

    }

    /**
     * Show the dialog and set the current high score.
     *
     * @param score High score to post.
     */
    public void popPostHighScore(HighScore score) {
        this.score = score;
        nameField.setText(score.getPlayerName());
        this.setVisible(true);
    }

    /**
     * Returnd true when the dialog job is done (user clicked
     * to send of closed the dialog).
     *
     * @return True if this dialog job is done.
     */
    public boolean isFinished() {
        return finished;
    }

    /**
     * If <code>post</code> is true, send the score to the server and close
     * the dialog. Else just close the dialog.
     *
     * @param post True if the player chose to post the score.
     */
    private void post(boolean post) {
        if (post) {
            try {
                score.setPlayerName(nameField.getText());
                highScoresManager.postScoreToServer(score);
            } catch (NetworkException ne) {
                System.out.println(ne.getMessage());
            }
        }
        this.finished = true;
        this.setVisible(false);
    }

    /**
     * Hanlde the user input.
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == noButton) {
            post(false);
        } else if (e.getSource() == yesButton) {
            if (nameField.getText().equals("")) {
                return;
            }
            post(true);
        }

    }

}

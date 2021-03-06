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

package game.highscore;

import game.util.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * Simple class to test the HighScoresManager.
 * This class is not part of the game.
 */
public class HighScoresTester extends JFrame implements ActionListener {

    private JTextField name, score, level;
    private JTextArea highScoresArea;
    private JButton add, show, save, load;

    private HighScoresManager highScoresManager;

    public static void main(String args[]) {
        HighScoresTester tester = new HighScoresTester();
        tester.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private HighScoresTester() {
        highScoresManager = new HighScoresManager(4);
        setupGUI();
    }

    private void setupGUI() {

        Container container = getContentPane();
        container.setLayout(new BorderLayout());

        // Create input fields and labels
        JLabel nameLabel = new JLabel("Name:");
        name = new JTextField(10);
        JLabel scoreLabel = new JLabel("Score:");
        score = new JTextField(10);
        JLabel levelLabel = new JLabel("Level:");
        level = new JTextField(10);

        // Create panel for input fields and add filed to panel
        JPanel scorePanel = new JPanel(new FlowLayout());
        scorePanel.add(nameLabel);
        scorePanel.add(name);
        scorePanel.add(scoreLabel);
        scorePanel.add(score);
        scorePanel.add(levelLabel);
        scorePanel.add(level);

        // Create buttons to control high scores
        add = new JButton("Add");
        add.addActionListener(this);

        show = new JButton("Show");
        show.addActionListener(this);

        save = new JButton("Save");
        save.addActionListener(this);

        load = new JButton("Load");
        load.addActionListener(this);

        // Add the buttons to the buttons panel
        JPanel buttonsPanel = new JPanel(new GridLayout(2, 2));
        buttonsPanel.add(add);
        buttonsPanel.add(show);
        buttonsPanel.add(save);
        buttonsPanel.add(load);

        highScoresArea = new JTextArea(10, 40);
        highScoresArea.setEditable(false);

        container.add(scorePanel, BorderLayout.NORTH);
        container.add(new JScrollPane(highScoresArea), BorderLayout.CENTER);
        container.add(buttonsPanel, BorderLayout.SOUTH);

        pack();
        setVisible(true);
    }


    private void addScore() {
        String name = this.name.getText();
        long score = Long.parseLong(this.score.getText());
        int level = Integer.parseInt(this.level.getText());
        HighScore newScore = new HighScore(name, score, level);

        highScoresManager.addScore(newScore, false);

        showHighScores();

    }

    private void showHighScores() {
        highScoresArea.setText(highScoresManager.toString());
    }

    private void save() {
        try {
            highScoresManager.saveHighScores();
        } catch (IOException ioe) {
            Logger.error("Unable to save high scores to file", ioe);
        }
    }

    private void load() {
        try {
            highScoresManager.loadHighScores(true);
        } catch (Exception e) {
            Logger.error("Unable to load high scores from file", e);
        }
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == add) {
            addScore();
        } else if (event.getSource() == show) {
            showHighScores();
        } else if (event.getSource() == save) {
            save();
        } else if (event.getSource() == load) {
            load();
        }
    }

}

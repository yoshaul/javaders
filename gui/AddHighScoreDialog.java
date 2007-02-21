
package game.gui;

import game.GameLoop;
import game.gamestate.AddHighScoreState;
import game.highscore.HighScore;
import game.highscore.HighScoresManager;
import game.util.ResourceManager;

import java.awt.*;
import java.awt.event.ActionEvent;

import javax.swing.*;
import javax.swing.border.Border;

/**
 * The <code>AddHighScoreDialog</code> appears when the game
 * is over and the player made a new (local) high score.
 */
public class AddHighScoreDialog extends InGameDialog {

    private GameLoop gameLoop;
    private HighScoresManager highScoresManager;
    private AddHighScoreState parentState;
    
    private JTextField nameField;
    private JButton okButton, cancelButton;
    private long score;
    private int level;
    private boolean finished;
    
    /**
     * Construct the dialog.
     * @param gameLoop	Reference to the game loop.
     * @param parentState	Parent <code>GameState</code> (to update
     * the name the user entered).
     * @param highScoresManager	The high scores manager.
     */
    public AddHighScoreDialog(GameLoop gameLoop, 
            AddHighScoreState parentState, 
            HighScoresManager highScoresManager) {
        
        super(gameLoop.getScreenManager(), DEFAULT_BG_IMAGE);
        this.gameLoop = gameLoop;
        this.highScoresManager = highScoresManager;
        this.parentState = parentState;

        createGUI();
        
    }
    
    /**
     * Setup the GUI.
     */
    protected void createGUI() {
        
        JPanel inputPanel = new JPanel(new FlowLayout());
		inputPanel.setOpaque(false);
        
        JLabel nameLabel = new JLabel("Enter your name:");
        nameLabel.setFont(ResourceManager.getFont(Font.BOLD, 16));
        inputPanel.add(nameLabel);
        
        nameField = new JTextField(10);
        nameField.setFont(ResourceManager.getFont(Font.PLAIN, 16));
        inputPanel.add(nameField);
        
        this.add(inputPanel, BorderLayout.NORTH);
        
		JPanel buttonsPanel = new JPanel(new GridLayout(1, 2));
		buttonsPanel.setOpaque(false);
		
		okButton = createButton("OK", DEFAULT_BTN_IMAGE);
		cancelButton = createButton("Cancel", DEFAULT_BTN_IMAGE);
		
		buttonsPanel.add(okButton);
		buttonsPanel.add(cancelButton);

		this.add(buttonsPanel, BorderLayout.SOUTH);

        Border border = BorderFactory.createTitledBorder(
                "Congratulation! You made a new High Score");

        this.setBorder(border);
        
        this.setSize(this.getPreferredSize());
        
        centralizeOnScreen();

    }

    /**
     * Show the dialog and set the score details.
     * @param score	Score made by the player.
     * @param level	Level reached by the player.
     */
    public void addHighScore(long score, int level) {
        this.finished = false;
        this.score = score;
        this.level = level;
        this.setVisible(true);
        this.requestFocus();
    }
    
    /**
     * Perform the addition to the high scores table
     * if the player entered the name and clicked ok.
     */
    private void doAdd() {
        String name = nameField.getText();
        if (name.equals("")) {
            return;
        }
        HighScore newHighScore = new HighScore(name, score, level);
        highScoresManager.addScore(newHighScore, true);
        finished = true;
        parentState.setPlayerName(name);
        this.setVisible(false);
    }

    /**
     * Handle user input.
     */
    public void actionPerformed(ActionEvent e) {
        
        if (e.getSource() == cancelButton) {
            finished = true;
            this.setVisible(false);
        }
        
        else if (e.getSource() == okButton) {
            doAdd();
        }
        else if (e.getSource() == nameField) {
            doAdd();
        }
    }
    
    /**
     * Returnd true when the dialog job is done (user clicked 
     * to send of closed the dialog).
     * @return	True if this dialog job is done.
     */
    public boolean isFinished() {
        return finished;
    }

}


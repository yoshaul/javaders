package game.gui;

import java.awt.*;
import java.awt.event.*;

import game.GameLoop;
import game.highscore.HighScore;
import game.highscore.HighScoresManager;

import javax.swing.*;

public class PostHighScoreDialog extends JPanel implements ActionListener {

    private GameLoop gameLoop;
    private HighScoresManager highScoresManager;
    private JTextField nameField;
    private JButton yesButton, noButton;
    private boolean finished;
    private HighScore score;
    
    public PostHighScoreDialog(GameLoop gameLoop, 
            HighScoresManager highScoresManager) {
        
        super(new BorderLayout());
        this.gameLoop = gameLoop;
        this.highScoresManager = highScoresManager;
        
        createGUI();
        
    }

    protected void createGUI() {
		
        JPanel labelPanel = new JPanel();
        
        JLabel label = new JLabel("Post your score to the server?");
        labelPanel.add(label);
        
        JPanel inputPanel = new JPanel(new FlowLayout());
        
        JLabel nameLabel = new JLabel("Name");
        inputPanel.add(nameLabel);
        
        nameField = new JTextField(10);
        inputPanel.add(nameField);

        JPanel northPanel = new JPanel(new GridLayout(2, 1));
        northPanel.add(labelPanel);
        northPanel.add(inputPanel);
        
        this.add(northPanel, BorderLayout.NORTH);
        
		JPanel buttonsPanel = new JPanel(new GridLayout(1, 2));
		
		yesButton = new JButton("Yes");
		yesButton.addActionListener(this);

		noButton = new JButton("No");
		noButton.addActionListener(this);
		
		buttonsPanel.add(yesButton);
		buttonsPanel.add(noButton);

		this.add(buttonsPanel, BorderLayout.SOUTH);

        this.setVisible(false);
        this.setSize(this.getPreferredSize());
		
        // Center the dialog on the screen
        Dimension screenSize = 
            gameLoop.getScreenManager().getScreenDimension();
//    		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dialogSize = this.getSize();
        this.setLocation(
                Math.max(0,(screenSize.width - dialogSize.width) / 2), 
                Math.max(0,(screenSize.height - dialogSize.height) / 2));

    }

    public void popPostHighScore(HighScore score) {
        this.score = score;
        nameField.setText(score.getPlayerName());
        this.show(true);
    }
    
    public boolean isFinished() {
        return finished;
    }

    public void actionPerformed(ActionEvent e) {
        
        if (e.getSource() == noButton) {
            this.show(false);
            this.finished = true;
        }
        else if (e.getSource() == yesButton) {
            highScoresManager.postScoreToServer(score);
            this.finished = true;
            this.show(false);
        }

    }

}

package game.gui;

import game.GameLoop;
import game.highscore.HighScore;
import game.highscore.HighScoresManager;
import game.state.AddHighScoreState;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;


public class AddHighScoreDialog extends JPanel implements ActionListener {

    private GameLoop gameLoop;
    private HighScoresManager highScoresManager;
    private AddHighScoreState parentState;
    
    private JTextField nameField;
    private JButton okButton, cancelButton;
    private long score;
    private int level;
    private boolean finished;
    
    public AddHighScoreDialog(GameLoop gameLoop, 
            AddHighScoreState parentState, 
            HighScoresManager highScoresManager) {
        
        super(new BorderLayout());
//        super(owner, modal);
        this.gameLoop = gameLoop;
        this.highScoresManager = highScoresManager;
        this.parentState = parentState;

        createGUI();
        
    }
    

    protected void createGUI() {
//        Container contentPane = this.getContentPane();
//		contentPane.setLayout(new BorderLayout());
        
//		this.setUndecorated(true);
        
        JPanel inputPanel = new JPanel(new FlowLayout());
		
        JLabel nameLabel = new JLabel("Enter your name");
        inputPanel.add(nameLabel);
        
        nameField = new JTextField(10);
        inputPanel.add(nameField);
        
        this.add(inputPanel, BorderLayout.NORTH);
        
		JPanel buttonsPanel = new JPanel(new GridLayout(1, 2));
		
		okButton = new JButton("OK");
		okButton.addActionListener(this);

		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(this);
		
		
		buttonsPanel.add(okButton);
		buttonsPanel.add(cancelButton);

		this.add(buttonsPanel, BorderLayout.SOUTH);

        this.setVisible(false);
        this.setSize(this.getPreferredSize());
		
        // Center the dialog on the screen
        Dimension screenSize = 
            gameLoop.getScreenManager().getScreenDimension();
//		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dialogSize = this.getSize();
        this.setLocation(
                Math.max(0,(screenSize.width - dialogSize.width) / 2), 
                Math.max(0,(screenSize.height - dialogSize.height) / 2));

    }

    public void addHighScore(long score, int level) {
        this.finished = false;
        this.score = score;
        this.level = level;
        this.show(true);
        this.requestFocus();
    }

    public void actionPerformed(ActionEvent e) {
        
        if (e.getSource() == cancelButton) {
            finished = true;
            this.show(false);
        }
        
        else if (e.getSource() == okButton) {
            String name = nameField.getText();
            if (name.equals("")) {
                return;
            }
            HighScore newHighScore = new HighScore(name, score, level);
            highScoresManager.addScore(newHighScore, true);
            finished = true;
            parentState.setPlayerName(name);
            this.show(false);
        }
    }
    
    public boolean isFinished() {
        return finished;
    }

}


package game.gui;

import game.highscore.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;

import javax.swing.*;

public class HighScoresDialog extends GameDialog {

    private HighScoresManager highScoresManager;
    private HighScore[] highScores;
    private JButton closeButton, clearButton, 
    	localScoresButton, networkScoresButton;
    private JPanel renderPanel;
    
    public HighScoresDialog(JFrame owner, boolean modal, 
            HighScoresManager highScoresManager) {
        
        super(owner, modal);
        
        this.highScoresManager = highScoresManager;
        this.highScores = highScoresManager.getHighScores();
        
        createGUI();
        
    }
    
    protected void createGUI() {
        
        setVisible(false);
        
//        setUndecorated(true);
        setResizable(false);
        
        Container container = getContentPane();
        container.setLayout(new BorderLayout());
        
        renderPanel = new JPanel();
        
        JPanel buttonsPanel = new JPanel(new GridLayout(2,2));
        
        closeButton = new JButton("Close");
        closeButton.addActionListener(this);
        buttonsPanel.add(closeButton);
        
        clearButton = new JButton("Clear High Scores");
        clearButton.addActionListener(this);
        buttonsPanel.add(clearButton);
        
        localScoresButton = new JButton("Local High Scores");
        localScoresButton.addActionListener(this);
        buttonsPanel.add(localScoresButton);
        
        networkScoresButton = new JButton("Network High Scores");
        networkScoresButton.addActionListener(this);
        buttonsPanel.add(networkScoresButton);
        
        container.add(renderPanel, BorderLayout.CENTER);
        container.add(buttonsPanel, BorderLayout.SOUTH);
        
        setSize(350, 400);
        
        // Center the dialog on the screen
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dialogSize = this.getSize();
        this.setLocation(
                Math.max(0,(screenSize.width - dialogSize.width) / 2), 
                Math.max(0,(screenSize.height - dialogSize.height) / 2));
        
    }
    
    public void paint(Graphics g) {
        super.paint(g);
        
        Graphics rg = renderPanel.getGraphics();
        rg.setClip(0, 0, renderPanel.getWidth(), renderPanel.getHeight());
        // Paint high scores on the render panel
        HighScoresRenderer.render(rg, highScores);
        
    }
    
    public void actionPerformed(ActionEvent event) {
        
        if (event.getSource() == closeButton) {
            setVisible(false);
        }
        else if (event.getSource() == clearButton) {
            highScoresManager.clearHighScores();
            try {
                highScoresManager.saveHighScores();    
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
            repaint();
        }
        else if (event.getSource() == localScoresButton) {
            highScores = highScoresManager.getHighScores();
            repaint();
        }
        else if (event.getSource() == networkScoresButton) {
            highScores = highScoresManager.getNetworkHighScores(1, 10);
            repaint();
        }
        
    }
    
}

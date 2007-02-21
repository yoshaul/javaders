package game;

import game.gui.QuitD;
import game.highscore.HighScoresManager;
import game.input.InputManager;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;


public class GUIManager implements ActionListener {

    private GameLoop gameLoop;
    private ScreenManager screenManager;
    private InputManager inputManager;
    private HighScoresManager highScoresManager;
    
//    private AddHighScoreDialog addHighScoreDialog;
    private QuitD quitDialog;
    
    
    public GUIManager(GameLoop gameLoop, ScreenManager screenManager, 
            InputManager inputManager, HighScoresManager highScoresManeger) {
        
        this.gameLoop = gameLoop;
        this.highScoresManager = highScoresManeger;
        this.screenManager = screenManager;
        this.inputManager = inputManager;
        
        NullRepaintManager.install();
        
        JFrame gameFrame = screenManager.getFullScreenWindow();
        
        Container container = gameFrame.getContentPane();
        	((JComponent)container).setOpaque(false);

//        
//        quitDialog = new QuitD(gameFrame,false, inputManager);
//        
//        screenManager.getFullScreenWindow().
//    		getLayeredPane().add(quitDialog, JLayeredPane.MODAL_LAYER);
//        
        gameFrame.validate();
    }
    
    private JButton createButton(String label, String toolTip) {
        
        JButton button = new JButton(label);
        
      	// We use active repainting
        button.setIgnoreRepaint(true);
        button.setOpaque(false);
        // Don't steal keyboard focus
        button.setFocusable(false);	
        button.setBorder(null);
        // Don't draw button backgroung
        button.setContentAreaFilled(false);
        button.setToolTipText(toolTip);
        
        button.addActionListener(this);
        
        return button;
        
    }
    
    public void actionPerformed(ActionEvent event) {
        
        Object source = event.getSource();

        
    }
    
    
    public void render(Graphics g) {
        
        JFrame gameFrame = (JFrame)screenManager.getFullScreenWindow();
        
        gameFrame.getLayeredPane().paintComponents(g);

    }
    
    /**
     * Adds a dialog to the modal layer pane
     * @param dialog Dialog to add
     */
    public void addDialog(JPanel dialog) {
        gameLoop.getScreenManager().getFullScreenWindow().
			getLayeredPane().add(dialog, JLayeredPane.MODAL_LAYER);
    }
}

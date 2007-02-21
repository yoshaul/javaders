package game.gui;

import game.ScreenManager;
import game.input.InputManager;
import game.util.ResourceManager;

import java.awt.*;
import java.awt.event.ActionEvent;

import javax.swing.*;

/**
 * The <code>QuitDialog</code> appears when the user clicks
 * on the quit button to verify she realy wants to quit. 
 */
public class QuitDialog extends InGameDialog {

    private InputManager inputManager;
    
    private JButton yesButton, noButton;
    
    /**
     * Construct the dialog.
     * @param screenManager	Reference to the screen manager.
     * @param inputManager	Input manager.
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
    public void actionPerformed(ActionEvent e) {
        
        if (e.getSource() == noButton) {
            this.setVisible(false);
            screenManager.showCursor(false);
            inputManager.setQuit(false);
        }
        else if (e.getSource() == yesButton) {
            this.setVisible(false);
            screenManager.showCursor(false);
            inputManager.setQuit(true);
        }

    }

}

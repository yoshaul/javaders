package game.gui;

import game.input.InputManager;

import java.awt.*;
import java.awt.event.ActionEvent;

import javax.swing.*;


public class QuitDialog extends GameDialog {

    private InputManager inputManager;
    
    private JButton yesButton, noButton;
    
    public QuitDialog(JFrame owner, boolean modal, InputManager inputManager) {
        
        super(owner, modal);
        this.inputManager = inputManager;
        
    }
    

    protected void createGUI() {
        Container contentPane = this.getContentPane();
		contentPane.setLayout(new BorderLayout());
        
		this.setUndecorated(true);
		
        JLabel label = new JLabel("Leaving so soon?");
        
        contentPane.add(label, BorderLayout.NORTH);
        
		JPanel buttonsPanel = new JPanel(new GridLayout(1, 2));
		
		yesButton = new JButton("Yes");
		yesButton.addActionListener(this);

		noButton = new JButton("No");
		noButton.addActionListener(this);
		
		
		buttonsPanel.add(yesButton);
		buttonsPanel.add(noButton);

		contentPane.add(buttonsPanel, BorderLayout.SOUTH);

		this.pack();
		
        // Center the dialog on the screen
//        Dimension screenSize = 
//            gameLoop.getScreenManager().getScreenDimension();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dialogSize = this.getSize();
        this.setLocation(
                Math.max(0,(screenSize.width - dialogSize.width) / 2), 
                Math.max(0,(screenSize.height - dialogSize.height) / 2));

    }


    public void actionPerformed(ActionEvent e) {
        
        if (e.getSource() == noButton) {
            this.show(false);
            inputManager.setQuit(false);
        }
        else if (e.getSource() == yesButton) {
            this.show(false);
            inputManager.setQuit(true);
        }

    }

}

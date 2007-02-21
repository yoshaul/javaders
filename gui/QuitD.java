package game.gui;

import game.GameLoop;
import game.input.InputManager;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;


public class QuitD extends JPanel implements ActionListener {

    private GameLoop gameLoop;
    private InputManager inputManager;
    
    private JButton yesButton, noButton;
    
    public QuitD(GameLoop gameLoop, boolean modal, InputManager inputManager) {
        
        super(new BorderLayout());
//        super(owner, modal);
        this.gameLoop = gameLoop;
        this.inputManager = inputManager;
        
        createGUI();
        
    }
    

    protected void createGUI() {
//        Container contentPane = this.getContentPane();
//		contentPane.setLayout(new BorderLayout());
        
//		this.setUndecorated(true);
		
        JLabel label = new JLabel("Leaving so soon?");
        
        this.add(label, BorderLayout.NORTH);
        
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
//		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
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

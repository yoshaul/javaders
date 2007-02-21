package game.gui;

import game.Game;

import java.awt.*;
import java.awt.event.ActionEvent;

import javax.swing.*;
import javax.swing.border.BevelBorder;

public class LoginDialog extends GameDialog {

    private Game game;
    
    private JTextField userNameField;
    private JPasswordField passwordField;
    private JButton loginButton, cancelButton;
    private JLabel statusLabel;
    
    public LoginDialog(Game game) {
        
        super(game, true);
        
        this.game = game;
        
        this.setTitle("Login");
        
    }
    
    protected void createGUI() {
		
        Container contentPane = this.getContentPane();
		
		JPanel inputPanel = new JPanel(new GridLayout(2, 2));
		
		inputPanel.add(new JLabel("User Name"));
		userNameField = new JTextField();
		inputPanel.add(userNameField);
		
		inputPanel.add(new JLabel("Password"));
		passwordField = new JPasswordField();
		inputPanel.add(passwordField);
		
		contentPane.add(inputPanel, BorderLayout.NORTH);

		loginButton = new JButton("Login");
	    loginButton.addActionListener(this);
		
		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(this);
		
		JPanel buttonsPanel = new JPanel(new GridLayout(2, 1));
		buttonsPanel.add(loginButton);
		buttonsPanel.add(cancelButton);

		statusLabel = new JLabel("Enter username and password to login");
		statusLabel.setBorder(new BevelBorder(BevelBorder.LOWERED));
		statusLabel.setForeground(Color.RED);
		
		JPanel southPanel = new JPanel(new BorderLayout());
		southPanel.add(buttonsPanel, BorderLayout.NORTH);
		southPanel.add(statusLabel, BorderLayout.SOUTH);
		
		contentPane.add(southPanel, BorderLayout.SOUTH);
		
		this.pack();
		
        // Center the dialog on the screen
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dialogSize = this.getSize();
        this.setLocation(
                Math.max(0,(screenSize.width - dialogSize.width) / 2), 
                Math.max(0,(screenSize.height - dialogSize.height) / 2));
		
    }

	private void login() {
		String userName = userNameField.getText();
		String password = new String(passwordField.getPassword());
		passwordField.setText("");
		statusLabel.setText("Validating username and password");
//		try {
			Long ticket = game.getNetworkManager().login(userName, password);
			
			if (ticket == null) {
			    statusLabel.setText("Unable to log in ");
			} else {
				statusLabel.setText("Logged in successfully ticket = "
						+ ticket); 
				
				game.setLoggedUser(userName, ticket);
				
			}
//		}
//		catch (InvalidLoginException e) {
//			statusLabel.setText(e.getMessage());
//		}
//		catch (RemoteException e) {
//			statusLabel.setText("ERRORL: Remote exception occured");
//		}
	}
	
	public void actionPerformed(ActionEvent event) {
	    
	    if (event.getSource() == loginButton) {
	        
	        login();
	        
	    } else if (event.getSource() == cancelButton) {
	        // Clear the fields
	        userNameField.setText("");
	        passwordField.setText("");
	        this.setVisible(false);
	    }
	    
	}
	
    
}

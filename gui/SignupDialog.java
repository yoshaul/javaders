package game.gui;

import game.Game;

import java.awt.*;
import java.awt.event.ActionEvent;

import javax.swing.*;
import javax.swing.border.BevelBorder;

public class SignupDialog extends GameDialog {
    
    private Game game;
    
    private JTextField userNameField, emailField;
    private JPasswordField passwordField;
    private JButton signupButton, cancelButton;
    private JLabel statusLabel;
    
    public SignupDialog(Game game) {
        
        super(game, true);
        
        this.game = game;
        
        this.setTitle("Login");
        
    }
    
    protected void createGUI() {
		
        Container contentPane = this.getContentPane();
		
		JPanel inputPanel = new JPanel(new GridLayout(3, 2));
		
		// User name area
		inputPanel.add(new JLabel("User Name:"));
		userNameField = new JTextField();
		inputPanel.add(userNameField);
		
		// Password area
		inputPanel.add(new JLabel("Password:"));
		passwordField = new JPasswordField();
		inputPanel.add(passwordField);
		
		// Email area
		inputPanel.add(new JLabel("Email:"));
		emailField = new JTextField();
		inputPanel.add(emailField);
		
		contentPane.add(inputPanel, BorderLayout.NORTH);

		// Create buttons
		signupButton = new JButton("Signup");
		signupButton.addActionListener(this);
		
		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(this);
		
		JPanel buttonsPanel = new JPanel(new GridLayout(2, 1));
		buttonsPanel.add(signupButton);
		buttonsPanel.add(cancelButton);

		// Create status label 
		statusLabel = new JLabel("Enter details to signup");
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
    
	public void actionPerformed(ActionEvent event) {
	    
	    if (event.getSource() == signupButton) {
	        
	        signup();
	        
	    } else if (event.getSource() == cancelButton) {
	        // Clear the fields
	        userNameField.setText("");
	        passwordField.setText("");
	        emailField.setText("");
	        this.setVisible(false);
	    }
	    
	}
	
	private void signup() {
	    
		String userName = userNameField.getText();
		String password = new String(passwordField.getPassword());
		String email = emailField.getText();
		passwordField.setText("");
		statusLabel.setText("Creating new user....");

		// Register the new user
		game.getNetworkManager().signup(
	            userName, password, email);
	    
		// Login with the new user
		Long ticket = game.getNetworkManager().login(userName, password);
		
		if (ticket == null) {
		    statusLabel.setText("Unable to signup");

		} else {
			statusLabel.setText("Logged in successfully ticket = "
					+ ticket);
			
			game.setLoggedUser(userName, ticket);
			
		}
	}
	
}

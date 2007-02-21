package game;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.BevelBorder;

public class SignupDialog extends JDialog implements ActionListener {
    
    private Game game;
    
    // Login dialog
    private JTextField userNameField, emailField;
    private JPasswordField passwordField;
    private JButton signupButton, cancelButton;
    private JLabel statusLabel;
    
    public SignupDialog(Game game) {
        
        super(game, "Login", true);
        
        this.game = game;
        
        createGUI();
        
    }
    
    private void createGUI() {
		
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
	//		try {
			    String ticket = game.getNetworkManager().signup(
			            userName, password, email);
			    
				if (ticket == null) {
				    statusLabel.setText("Unable to signup");
	
				} else {
					statusLabel.setText("Logged in successfully ticket = "
							+ ticket);
					
					game.setLoggedUser(userName);
					
				}
			    
	//		}
//		catch (RemoteException e) {
//			statusLabel.setText("ERRORL: Remote exception occurred");
//		}
	    
	    
	}
	
	
	
}

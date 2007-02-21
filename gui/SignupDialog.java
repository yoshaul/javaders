package game.gui;

import game.GameMenu;
import game.network.InvalidLoginException;
import game.network.client.NetworkException;

import java.awt.*;
import java.awt.event.ActionEvent;

import javax.swing.*;
import javax.swing.border.BevelBorder;

/**
 * The <code>SignupDialog</code> enables the user to signup for
 * a new account. The user must supply user name and password.
 */
public class SignupDialog extends GameDialog {
    
    private GameMenu gameMenu;
    
    private JTextField userNameField, emailField;
    private JPasswordField passwordField;
    private JButton signupButton, cancelButton;
    private JLabel statusLabel;
    
    /**
     * Construct the dialog.
     * @param game	The game menu.
     */
    public SignupDialog(GameMenu game) {
        
        super(game, true);
        
        this.gameMenu = game;
        this.setTitle("Login");
        
    }
    
    /**
     * Create the dialog UI.
     */
    protected void createGUI() {
		
        Container contentPane = this.getContentPane();
		
		JPanel inputPanel = createPanel(new GridLayout(3, 2));
		
		// User name area
		inputPanel.add(createLabel(" User Name:"));
		userNameField = createTextField();
		inputPanel.add(userNameField);
		
		// Password area
		inputPanel.add(createLabel(" Password:"));
		passwordField = createPasswordField();
		inputPanel.add(passwordField);
		
		// Email area
		inputPanel.add(createLabel(" Email:"));
		emailField = createTextField();
		inputPanel.add(emailField);
		
		contentPane.add(inputPanel, BorderLayout.NORTH);

		// Create buttons
		signupButton = createButton("Signup", 
		        "Signup for a new account", BTN_SMALL_IMAGE);
		
		cancelButton = createButton("Cancel", "", BTN_SMALL_IMAGE);
		
		JPanel buttonsPanel = createPanel(new GridLayout(1, 2));
		buttonsPanel.add(signupButton);
		buttonsPanel.add(cancelButton);

		// Create status label 
		statusLabel = createLabel("Enter details to signup");
		statusLabel.setBorder(new BevelBorder(BevelBorder.LOWERED));
		statusLabel.setForeground(Color.RED);
		
		JPanel southPanel = createPanel(new BorderLayout());
		southPanel.add(buttonsPanel, BorderLayout.NORTH);
		southPanel.add(statusLabel, BorderLayout.SOUTH);
		
		contentPane.add(southPanel, BorderLayout.SOUTH);
		
		this.setSize(300, 150);
		
		centralizeOnScreen();
        
    }
    
    /**
     * Respond to user input.
     */
	public void actionPerformed(ActionEvent event) {
	    
	    if (event.getSource() == signupButton) {
	        
	        signup();
	        
	    } else if (event.getSource() == cancelButton) {
	        
	        hideDialog();
	    }
	    
	}
	
	/**
	 * Clear the input fields and hide the dialog.
	 */
	public void hideDialog() {
	    // Clear the fields
        userNameField.setText("");
        passwordField.setText("");
        emailField.setText("");
        super.hideDialog();
	}
	
	/** TODO: handle the case where user already exists here and in the bean */
	/**
	 * Sign up for a new account and login afterwards.
	 */
	private void signup() {
	    
		String userName = userNameField.getText();
		String password = new String(passwordField.getPassword());
		String email = emailField.getText();
		passwordField.setText("");
		statusLabel.setText("Creating new user....");

		try {
			// Register the new user
			gameMenu.getNetworkManager().signup(
		            userName, password, email);
			
			// Logout before trying login with the new user name
			gameMenu.logout();
			
			// Login with the new user to get session id
			Long ticket = gameMenu.getNetworkManager().login(userName, password);
			
			if (ticket == null) {
			    throw new InvalidLoginException("Error: null session id received");
			}

			statusLabel.setText(userName + " logged in successfully");
			
			// Set the logged user in the gameMenu 
			gameMenu.setLoggedUser(ticket);
			
			hideDialog();
				
		}
		catch (InvalidLoginException ile) {
		    statusLabel.setText(ile.getMessage());
		}
		catch (NetworkException ne) {
		    statusLabel.setText("Error connecting to server");
		}
	}
	
}

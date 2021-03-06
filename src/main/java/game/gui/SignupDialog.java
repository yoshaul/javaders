/*
 * This file is part of Javaders.
 * Copyright (c) Yossi Shaul
 *
 * Javaders is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Javaders is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Javaders.  If not, see <http://www.gnu.org/licenses/>.
 */

package game.gui;

import game.GameMenu;
import game.network.InvalidLoginException;
import game.network.client.NetworkException;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.ActionEvent;

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
     *
     * @param game The game menu.
     */
    public SignupDialog(GameMenu game) {

        super(game, true);

        this.gameMenu = game;
        this.setTitle("Login");

    }

    /**
     * Create the dialog UI.
     */
    @Override
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
    @Override
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
    @Override
    public void hideDialog() {
        // Clear the fields
        userNameField.setText("");
        passwordField.setText("");
        emailField.setText("");
        super.hideDialog();
    }

    /* TODO: handle the case where user already exists here and in the bean */
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

        } catch (InvalidLoginException ile) {
            statusLabel.setText(ile.getMessage());
        } catch (NetworkException ne) {
            statusLabel.setText("Error connecting to server");
        }
    }

}

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
 * The <code>LoginDialog</code> enables the user to supply a user
 * name and password for logging onto the server.
 */
public class LoginDialog extends GameDialog {

    private GameMenu gameMenu;

    private JTextField userNameField;
    private JPasswordField passwordField;
    private JButton loginButton, cancelButton;
    private JLabel statusLabel;

    /**
     * Construct the dialog.
     *
     * @param game The game menu.
     */
    public LoginDialog(GameMenu game) {

        super(game, true);

        this.gameMenu = game;
        this.setTitle("Login");
    }

    /**
     * Create the dialog UI.
     */
    protected void createGUI() {

        Container contentPane = this.getContentPane();
        JPanel inputPanel = createPanel(new GridLayout(2, 2));
        inputPanel.add(createLabel(" User Name:"));
        userNameField = createTextField();
        inputPanel.add(userNameField);

        inputPanel.add(createLabel(" Password:"));
        passwordField = createPasswordField();
        passwordField.addActionListener(this);
        inputPanel.add(passwordField);

        contentPane.add(inputPanel, BorderLayout.NORTH);

        loginButton = createButton("Login", "Login", BTN_SMALL_IMAGE);

        cancelButton = createButton("Cancel", "Cancel", BTN_SMALL_IMAGE);

        JPanel buttonsPanel = createPanel(new GridLayout(1, 2));
        buttonsPanel.add(loginButton);
        buttonsPanel.add(cancelButton);

        statusLabel = createLabel("Enter username and password to login");
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
     * Login to the server. Get the user name and password from
     * the input fields.
     * If successful update the <code>gameMenu</code> with the logged
     * users details (user name and ticket/session id).
     */
    private void login() {
        String userName = userNameField.getText();
        String password = new String(passwordField.getPassword());
        passwordField.setText("");
        statusLabel.setText("Validating username and password");
        try {
            Long ticket = gameMenu.getNetworkManager().login(userName, password);
            if (ticket == null) {
                throw new InvalidLoginException("Error: null session id received");
            }

            statusLabel.setText(userName + " logged in successfully");

            gameMenu.setLoggedUser(ticket);

            hideDialog();

        } catch (InvalidLoginException ile) {
            statusLabel.setText(ile.getMessage());
        } catch (NetworkException ne) {
            statusLabel.setText("Error connecting to server");
        }
    }

    /**
     * Hides the dialog and clears the input fields.
     */
    public void hideDialog() {
        // Clear the text and password fields
        userNameField.setText("");
        passwordField.setText("");
        super.hideDialog();
    }

    /**
     * Respond to user input.
     */
    public void actionPerformed(ActionEvent event) {
        Object source = event.getSource();
        if (source == loginButton || source == passwordField) {
            login();
        } else if (event.getSource() == cancelButton) {
            hideDialog();
        }
    }

}

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

package game;

import game.graphic.GraphicsHelper;
import game.gui.*;
import game.highscore.HighScoresManager;
import game.network.client.GameNetworkManager;
import game.network.client.NetworkException;
import game.network.client.NetworkManager;
import game.network.client.NullNetworkManager;
import game.network.packet.InvitationPacket;
import game.util.Logger;
import game.util.ResourceManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * The <code>GameMenu</code> hold the main method of the game.
 * The application starts from the menu represented by this
 * class. From here the player can start a single or network
 * game, log on to the server, signup and view the high scores.
 */
public class GameMenu extends JFrame implements ActionListener {

    private NetworkManager networkManager;
    private HighScoresManager highScoresManager;

    private JButton startButton, multiplayerButton,
            loginoutButton, exitButton, signupButton, highScoresButton;

    // Game menu dialogs
    private LoginDialog loginDialog;
    private SignupDialog signupDialog;
    private OKDialog okDialog;
    private AvailablePlayersDialog availablePlayersDialog;
    private HighScoresDialog highScoresDialog;
    private InvitationDialog invitationDialog;

    private Long sessionId = null;    // The network session id

    private boolean exited = false;
    private boolean startSingle = false;
    private boolean startNetworkGame = false;

    /**
     * Start the game.
     *
     * @param args No args
     */
    public static void main(String[] args) {
        Logger.init(args);
        GameMenu game = new GameMenu();
        game.start();
    }

    /**
     * Initialize the various objects and set up the GUI.
     */
    public GameMenu() {

        super("Super Game Menu");

        this.addWindowListener(
                new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        exitGame();
                    }
                }

        );

        // Initialize the managers
        networkManager = new NullNetworkManager(this);
        highScoresManager = new HighScoresManager(10);
        highScoresManager.setNetworkManager(networkManager);

        // Create the menu GUI
        createGUI();

        // Create game dialogs
        loginDialog = new LoginDialog(this);
        signupDialog = new SignupDialog(this);
        okDialog = new OKDialog(this);
        availablePlayersDialog = new AvailablePlayersDialog(this);
        highScoresDialog = new HighScoresDialog(this, true, highScoresManager);
        invitationDialog = new InvitationDialog(this, false, networkManager);

        this.setSize(230, 300);

        // Center the frame on the screen
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension windowSize = this.getSize();
        this.setLocation(
                Math.max(0, (screenSize.width - windowSize.width) / 2),
                Math.max(0, (screenSize.height - windowSize.height) / 2));

        this.setVisible(true);

    }

    /**
     * Run in the loop until state changed to start
     * a new game or exit the game
     */
    public void start() {

        while (!exited) {
            try {
                if (startSingle) {
                    startSinglePlayerGame();
                } else if (startNetworkGame) {
                    startNetworkGame();
                } else {
                    Thread.sleep(50);
                }
            } catch (InterruptedException ie) {
                // ignore
            }
        }

        exitGame();

    }

    /**
     * Start a single player game.
     */
    public void startSinglePlayerGame() {
        startSingle = false;
        startGame(false, true);
    }

    /**
     * Start a network game.
     */
    public void startNetworkGame() {
        startNetworkGame = false;
        // Start new network game. The inviter is the controller
        startGame(true, networkManager.isInviter());

    }

    /**
     * Start a new game.
     *
     * @param networkGame True if starting a network game
     * @param controller  True is this machine is the controller
     */
    public void startGame(boolean networkGame, boolean controller) {
        // Hide the game menu
        this.setVisible(false);

        if (sessionId != null) {
            // Don't accept invitations while playing
            try {
                networkManager.acceptInvitations(false);
            } catch (NetworkException ne) {
                Logger.exception(ne);
            }
        }

        GameNetworkManager gnm = null;
        if (networkGame) {
            try {
                gnm = networkManager.getGameNetworkManager();
            } catch (NetworkException ne) {
                Logger.exception(ne);
                return;    // don't start the game
            }
        }

        GameLoop gameLoop = new GameLoop(networkGame, controller,
                highScoresManager, gnm);

        Thread gameThread = new Thread(gameLoop);
        gameThread.start();
        try {
            // Join the game loop and wait until it's finished
            gameThread.join();
        } catch (InterruptedException ie) {
            Logger.exception(ie);
        }

        // Game finished
        if (sessionId != null) {
            // Accept invitations
            try {
                networkManager.acceptInvitations(true);
            } catch (NetworkException ne) {
                Logger.exception(ne);
            }
        }

        this.requestFocus();

        // Show the game menu
        this.setVisible(true);
    }

    /**
     * Setup the GUI for the game menu.
     */
    public void createGUI() {

        if (!Logger.isDebug()) {
            this.setUndecorated(true);
        }

        Container container = this.getContentPane();

        JPanel guiPanel = new JPanel(new GridLayout(8, 1, 5, 0));

        JLabel menuLabel = new JLabel("Game Menu", SwingConstants.CENTER);
        menuLabel.setFont(ResourceManager.getFont(Font.BOLD, 16));
        menuLabel.setForeground(Color.BLUE);
        guiPanel.add(menuLabel);

        startButton = createButton("Start Single Player",
                "Starts a new single player game");

        multiplayerButton = createButton("Start Multiplayer Game",
                "Starts a new multiplayer game");
        multiplayerButton.setEnabled(false);

        loginoutButton = createButton("Login", "Login");

        signupButton = createButton("Signup", "Signup");

        highScoresButton = createButton("High Scores", "View High Scores");

        exitButton = createButton("Exit", "Exit the game");

        guiPanel.add(startButton);
        guiPanel.add(multiplayerButton);
        guiPanel.add(loginoutButton);
        guiPanel.add(signupButton);
        guiPanel.add(highScoresButton);
        guiPanel.add(exitButton);

        guiPanel.setBackground(Color.BLACK);

        container.add(guiPanel, BorderLayout.CENTER);

        this.validate();

    }

    /**
     * Creates and returns a customized button.
     *
     * @param buttonText  Text to display on the button
     * @param toolTipText Button tooltip text
     * @return Customized button.
     */
    private JButton createButton(String buttonText,
                                 String toolTipText) {
        JButton button = new JButton();
        button.setToolTipText(toolTipText);
        button.addActionListener(this);

        // Change the button look
        customizeButton(button, buttonText);

        return button;
    }

    private void customizeButton(JButton button,
                                 String buttonText) {
        // Load the base image for the button
        Image srcImage = ResourceManager.loadImage(
                GameConstants.IMAGES_DIR + GameDialog.BTN_BIG_IMAGE);

        int width = srcImage.getWidth(null);
        int height = srcImage.getHeight(null);

        Font menuFont = ResourceManager.getFont(16);

        // Get a compatible translucent image
        Image image = GraphicsHelper.getCompatibleImage(this,
                width, height, Transparency.TRANSLUCENT);
        // Draw the source image and the button text 
        // on the tranlucent image with alpha composite of 0.8
        Graphics2D g = (Graphics2D) image.getGraphics();
        Composite alpha = AlphaComposite.getInstance(
                AlphaComposite.SRC_OVER, 0.8f);
        g.setComposite(alpha);
        g.drawImage(srcImage, 0, 0, null);
        g.setFont(menuFont);
        GraphicsHelper.drawInMiddle(g, image, buttonText);
        g.dispose();

        // Create an image icon for the default button image
        ImageIcon iconDefault = new ImageIcon(image);

        // Create a pressed image
        image = GraphicsHelper.getCompatibleImage(this,
                width, height, Transparency.TRANSLUCENT);
        g = (Graphics2D) image.getGraphics();
        alpha = AlphaComposite.getInstance(
                AlphaComposite.SRC_OVER, 0.9f);
        g.setComposite(alpha);
        // a bit lowered and to the right button
        g.drawImage(srcImage, 2, 2, null);
        g.setFont(menuFont);
        GraphicsHelper.drawInMiddle(g, image, buttonText);
        g.dispose();
        ImageIcon iconPressed = new ImageIcon(image);

        // Create disabled button image
        image = GraphicsHelper.getCompatibleImage(this,
                width, height, Transparency.TRANSLUCENT);
        g = (Graphics2D) image.getGraphics();
        alpha = AlphaComposite.getInstance(
                AlphaComposite.SRC_OVER, 0.3f);
        g.setComposite(alpha);
        g.drawImage(srcImage, 0, 0, null);
        g.setFont(menuFont);
        GraphicsHelper.drawInMiddle(g, image, buttonText);
        g.dispose();
        ImageIcon iconDisabled = new ImageIcon(image);

        button.setOpaque(false);
        button.setFocusPainted(false);
        button.setFocusable(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setMargin(new Insets(0, 0, 0, 0));
        button.setIcon(iconDefault);
        button.setPressedIcon(iconPressed);
        button.setDisabledIcon(iconDisabled);
        button.setSize(button.getPreferredSize());

    }

    /**
     * Show the login dialog.
     */
    private void popLoginDialog() {
        loginDialog.popDialog();
    }

    /**
     * Show the signup dialog.
     */
    private void popSignupDialod() {
        signupDialog.popDialog();
    }

    /**
     * Show the high scores dialog.
     */
    private void popHighScoresDialog() {
        highScoresDialog.popDialog();
    }

    /**
     * Show the available players dialog.
     */
    private void popAvailablePlayersDialog() {
        availablePlayersDialog.popDialog();
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        Object eventSource = event.getSource();
        if (eventSource == exitButton) {
            exited = true;

        } else if (eventSource == loginoutButton) {
            if (sessionId == null) {
                // User not logged in yet
                popLoginDialog();
            } else {
                // User logged in so we log out
                logout();
            }

        } else if (eventSource == startButton) {
            startSingle = true;

        } else if (eventSource == signupButton) {
            popSignupDialod();

        } else if (eventSource == multiplayerButton) {
            popAvailablePlayersDialog();

        } else if (eventSource == highScoresButton) {
            popHighScoresDialog();
        }
    }

    /**
     * Called by the login and sigup dialogs when the user succesfully
     * logs in.
     *
     * @param sessionId Session id of the user
     */
    public void setLoggedUser(Long sessionId) {
        try {
            this.sessionId = sessionId;
            customizeButton(loginoutButton, "Logout");
            loginoutButton.setToolTipText("Logout");
            // Enable the multiplayer button
            multiplayerButton.setEnabled(true);
            // Accept game invitations
            networkManager.acceptInvitations(true);
        } catch (NetworkException ne) {
            Logger.showErrorDialog(this, ne.getMessage());
        }
    }

    /**
     * Called if the user clicks on the logout button and by
     * the signup dialog if the signup completed successfully
     * before the user logs in with the new user name.
     */
    public void logout() {
        if (sessionId != null) {
            try {
                networkManager.acceptInvitations(false);
                networkManager.logout();
                System.out.println("Logged out successfully");
            } catch (NetworkException ne) {
                System.err.println(ne.getMessage());
            } finally {
                customizeButton(loginoutButton, "Login");
                loginoutButton.setToolTipText("Login");
                multiplayerButton.setEnabled(false);
                this.sessionId = null;
            }
        }
    }

    /**
     * Called when the user exites the game or closes
     * the window.
     */
    private void exitGame() {
        finalizeGame();
        System.exit(0);
    }

    /**
     * Releases any resources and updates the server if
     * logged in.
     */
    private void finalizeGame() {
        if (sessionId != null) {
            try {
                networkManager.acceptInvitations(false);
                networkManager.logout();
            } catch (NetworkException ne) {
                System.err.println(ne.getMessage());
            }
        }
    }

    /**
     * Returns the network manager.
     *
     * @return The network manager.
     */
    public NetworkManager getNetworkManager() {
        return this.networkManager;
    }

    /**
     * This method is called when network user accepts the invitation
     * to play or the local user accepts the invitation to play.
     *
     * @param start True to start a network game.
     */
    public void setStartMultiplayer(boolean start) {
        this.startNetworkGame = start;
    }

    /**
     * Invoked by the network manager when a reply to a previous invitation
     * to play sent by this user arrived.
     *
     * @param accepted True is the player accepted the invitation.
     * @param userName Name of the player
     */
    public void invitationAccepted(boolean accepted, String userName) {

        if (accepted) {

            okDialog.setText("User " + userName +
                    " accepted your invitation");
            okDialog.popDialog();

            availablePlayersDialog.hideDialog();

            // Start a new multiplayer game
            setStartMultiplayer(true);

        } else {

            okDialog.setText("User " + userName +
                    " rejected your invitation");
            okDialog.popDialog();

            availablePlayersDialog.setStatusText("User " +
                    userName + " rejected your invitation");
        }

        // reset the invitation status in the dialog
        availablePlayersDialog.reset();

    }

    /**
     * Invoked by the network manager when an invitation to play arrives
     * from an online player.
     *
     * @param invitation Invitation packet with the invitation details
     */
    public void invitationArrived(InvitationPacket invitation) {

        invitationDialog.invitationArrived(invitation);

    }

    /**
     * Invoked by the network manager when a previous invitation
     * to play was cancelled by the inviter.
     */
    public void invitationCancelled() {
        invitationDialog.invitationCancelled();
    }

}

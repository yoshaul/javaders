
package game;

import game.gui.*;
import game.highscore.HighScoresManager;
import game.network.*;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class Game extends JFrame implements ActionListener {

//    private ScreenManager screenManager;
//    private GUIManager guiManager;
    
    private NetworkManager networkManager;
    private HighScoresManager highScoresManager;

    // GUI components
    private JPanel guiPanel;
    private JButton startButton, multiplayerButton, 
    	loginButton, exitButton, signupButton, highScoresButton;
    
    private GameDialog loginDialog, signupDialog, 
    	availablePlayersDialog, highScoresDialog;
    
    private Long sessionId = null;
    
    private boolean exited = false;
    private boolean startSingle = false;
    private boolean startMultiplayer = false;
    
    public static void main(String[] args) {
        
        Game game = new Game();        
        
        game.start();

    }
    
    public Game() {
        
        super("Super Game");
        
//        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.addWindowListener(
            new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    releaseResources();
                    System.out.println("disposing window....");
                    dispose();
                    System.out.println("calling exit....");
                    System.exit(0);
                }
            }
            
        );
        
        
        
        networkManager = new J2EENetworkManager(this);
        highScoresManager = new HighScoresManager(10);
        highScoresManager.setNetworkManager(networkManager);
        
        createGUI();
        
        // Create game dialogs
        loginDialog = new LoginDialog(this);
        signupDialog = new SignupDialog(this);
        availablePlayersDialog = new AvailablePlayersDialog(this);
        highScoresDialog = new HighScoresDialog(this, true, highScoresManager);
        
        this.pack();
        
        // Center the frame on the screen
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension windowSize = this.getSize();
        this.setLocation(
                Math.max(0,(screenSize.width - windowSize.width) / 2), 
                Math.max(0,(screenSize.height - windowSize.height) / 2));
        
        this.setVisible(true);

    }
    
    public void start() {
        
        while(!exited) {
            try {
                if (startSingle) {
                    startSinglePlayerGame();
                } else if (startMultiplayer){
                    startMultiPlayerGame();
                } else {
                    Thread.sleep(30);
                }
            }
            catch (InterruptedException ie) {
                //
            }
        }
        releaseResources();
        System.exit(0);
        
    }
    
    private JButton createButton(String label, String toolTip) {
        
        JButton button = new JButton(label);
        
      	// We use active repainting
//        button.setIgnoreRepaint(true);
//        button.setOpaque(false);
        // Don't steal keyboard focus
//        button.setFocusable(false);	
//        button.setBorder(null);
        // Don't draw button backgroung
//        button.setContentAreaFilled(false);
        button.setToolTipText(toolTip);
        
        button.addActionListener(this);
        
        return button;
        
    }
    
    public void startSinglePlayerGame() {
        // start new game
        startSingle = false;
        startGame(false, true);
        
    }
    
    public void startMultiPlayerGame() {
        // start new game
        startMultiplayer = false;
        // Start new network game. The inviter is the controller
        startGame(true, networkManager.isInviter());
        
    }
    
    public void startGame(boolean multiPlayer, boolean controller) {
        // Hide the game menu
        this.setVisible(false);
        
        // Start a new game loop thread
        GameNetworkManager gnm = null;
        if (multiPlayer){
            
            JMSGameListener jmsGameListener = 
                ((J2EENetworkManager)networkManager).getJMSGameListener();
            
            gnm = new J2EEGameNetworkManager(/*gameLoop,*/
                    jmsGameListener, networkManager.getSenderID(),
                    networkManager.getReceiverID(), networkManager.isInviter());    
        }
        
        GameLoop gameLoop = new GameLoop(multiPlayer, controller, 
                highScoresManager, gnm);
        
        Thread gameThread = new Thread(gameLoop);
        gameThread.run();
        try {
            // Join the game loop and wait until it's finished
            gameThread.join();    
        }
        catch (InterruptedException ie) {
            System.err.println(ie.getMessage());
        }
        
        // Show the game menu
        this.setVisible(true);
    }
    
    
    public void createGUI() {
        
        Container container = this.getContentPane();
        
        guiPanel = new JPanel(new GridLayout(10, 1));
        
        startButton = createButton("Start Single Player", 
                "Starts a new single player game");
        
        multiplayerButton = createButton("Start Multiplayer Game", 
                "Starts a new multiplayer game");
        multiplayerButton.setEnabled(false);
        
        loginButton = createButton("Login", "Login");
        
        signupButton = createButton("Signup", "Signup");
        
        highScoresButton = createButton("High Scores", "View High Scores");
        
        exitButton = createButton("Exit", "Exit the game");
        
        guiPanel.add(startButton);
        guiPanel.add(multiplayerButton);
        guiPanel.add(loginButton);
        guiPanel.add(signupButton);
        guiPanel.add(highScoresButton);
        guiPanel.add(exitButton);
        
        guiPanel.setBackground(Color.BLUE);
        
        container.add(guiPanel, BorderLayout.CENTER);
        
        this.validate();
        
    }
    
    private void popLoginDialog() {
        System.out.println("poping login dialog");
        loginDialog.setVisible(true);
    }
    
    private void popSignupDialod() {
        System.out.println("poping signup dialog");
        signupDialog.setVisible(true);
    }
    
    private void popHighScoresDialog() {
        System.out.println(highScoresManager.toString());
        highScoresDialog.setVisible(true);
    }
    
    private void popAvailablePlayersDialog() {
        ((AvailablePlayersDialog)availablePlayersDialog).refresh();
        availablePlayersDialog.setVisible(true);
    }
    
    public void actionPerformed(ActionEvent event) {
	    
        if (event.getSource() == exitButton) {
            exited = true;
            
        } else if (event.getSource() == loginButton) {
            if (sessionId == null) {
                // User no logged in yet
                popLoginDialog();    
            } 
            else {
                // User logged in so we log out
                logout();
            }
            
            
        } else if (event.getSource() == startButton) {
            startSingle = true;
            
        } else if (event.getSource() == signupButton) {
            popSignupDialod();
            
        } else if (event.getSource() == multiplayerButton) {
            
            popAvailablePlayersDialog();
            
//            startMultiplayer = true;
        
    	} else if (event.getSource() == highScoresButton) {
    	    popHighScoresDialog();
    	}
	}
    
    public void setLoggedUser (String userName, Long sessionId) {
        
        this.sessionId = sessionId;
        loginButton.setText("Logout");
        multiplayerButton.setEnabled(true);
        networkManager.acceptInvitations(true, sessionId);
        loginDialog.setVisible(false);
        signupDialog.setVisible(false);
        System.out.println("User " + userName + " loggedin successfully. " +
        		"Session id = " + sessionId);
        
    }
    
    private void logout() {
        
        loginButton.setText("Login");
        multiplayerButton.setEnabled(false);
        networkManager.acceptInvitations(false, sessionId);
        networkManager.logout(sessionId);
        this.sessionId = null;
        System.out.println("Logged out successfully");
        
    }
    
    private void releaseResources() {
        System.out.println("Releasing resources....");
        if (sessionId != null) {
            networkManager.acceptInvitations(false, sessionId);
            networkManager.logout(sessionId); 
        }
    }
    
    public NetworkManager getNetworkManager() {
        return this.networkManager;
    }
    
    public void setStartMultiplayer(boolean start) {
        this.startMultiplayer = start;
    }
    
}

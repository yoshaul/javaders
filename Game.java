
package game;

import game.network.J2EENetworkManager;
import game.network.NetworkManager;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class Game extends JFrame implements ActionListener {

//    private ScreenManager screenManager;
//    private GUIManager guiManager;
    
    private NetworkManager networkManager;

    // GUI components
    private JPanel guiPanel;
    private JButton startButton, multiplayerButton, 
    	loginButton, exitButton, signupButton;
    
    private LoginDialog loginDialog;
    private SignupDialog signupDialog;
    
    private boolean exited = false;
    private boolean startSingle = false;
    private boolean startMultiplayer = false;
    
    public static void main(String[] args) {
        
        Game game = new Game();        
        
        game.start();

    }
    
    public Game() {
        
        super("Super Game");
        
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        createGUI();
        
        loginDialog = new LoginDialog(this);
        signupDialog = new SignupDialog(this);
        
        this.pack();
        
        // Center the frame on the screen
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension windowSize = this.getSize();
        this.setLocation(
                Math.max(0,(screenSize.width - windowSize.width) / 2), 
                Math.max(0,(screenSize.height - windowSize.height) / 2));
        
        
        networkManager = new J2EENetworkManager();
        
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
        startGame(false);
        
    }
    
    public void startMultiPlayerGame() {
        // start new game
        startMultiplayer = false;
        startGame(true);
        
    }
    
    public void startGame(boolean multiPlayer) {
        // Hide the game menu
        this.setVisible(false);
        
        // Start a new game loop thread
        GameLoop gameLoop = new GameLoop(multiPlayer);
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
        
        exitButton = createButton("Exit", "Exit the game");
        
        guiPanel.add(startButton);
        guiPanel.add(multiplayerButton);
        guiPanel.add(loginButton);
        guiPanel.add(signupButton);
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
    
    public void actionPerformed(ActionEvent event) {
	    
        if (event.getSource() == exitButton) {
            exited = true;
            
        } else if (event.getSource() == loginButton) {
            popLoginDialog();
            
        } else if (event.getSource() == startButton) {
            startSingle = true;
            
        } else if (event.getSource() == signupButton) {
            popSignupDialod();
            
        } else if (event.getSource() == multiplayerButton) {
            startMultiplayer = true;
        }
        
	}
    
    public void setLoggedUser (String userName) {
        
        System.out.println("User " + userName + " Loggedin");
        multiplayerButton.setEnabled(true);
        
    }
    
    public NetworkManager getNetworkManager() {
        return this.networkManager;
    }
    
}

package game.gamestate;

import game.GameLoop;
import game.LevelsManager;
import game.network.packet.*;
import game.ship.*;
import game.util.Logger;
import game.util.ResourceManager;

import java.awt.*;
import java.util.*;

/**
 * The <code>LoadingLevelState</code> is a game state that responsible
 * to load the next level either from the file (via the <code>LevelsManager
 * </code> or the network).
 * The actual work is done from a seperate thread to allow the state
 * processing input and rendering while the level is loaded.  
 */
public class LoadingLevelState implements GameState {

    private GameLoop gameLoop;
    private LevelsManager levelsManager;
    private long timeInState;
    private boolean levelLoaded;
    private boolean friendReady;
    private int nextGameState;
    private String loadingStr1, loadingStr2;
    private boolean finished;
    private boolean gameFinished;	// True if we finished the last level

    /**
     * Construct the game state.
     * @param gameLoop	Reference to the game loop.
     */
    public LoadingLevelState(GameLoop gameLoop) {
        this.gameLoop = gameLoop;
        this.levelsManager = gameLoop.getLevelsManager();
    }
    
    /**
     * Initialize state.
     */
    public void init() {
        // Nothing to initialize
    }

    /**
     * This method is called once when this state is set to be 
     * the active state. Start the loader thread.
     * @see game.gamestate.GameState init method
     */
    public void start() {
        timeInState = 0;
        levelLoaded = friendReady = finished = false;
        
        // The loading thread will change the status text
        loadingStr1 = loadingStr2 = "";
        
        // Load the new level in a new thread
        new Thread(new LevelLoaderThread()).start();
        
    }

    /**
     * Check for special input from the player (quit, pause, etc.)
     * Check for network input.
     */
    public void gatherInput(GameLoop gameLoop, long elapsedTime) {
        
        // Check if the user wants to quit
        gameLoop.getInputManager().gatherInput();
        
        // Check for network input
        if (gameLoop.isNetworkGame()) {
            gameLoop.getGameNetworkManager().gatherInput(this);;
        }
    }


    /**
     * Update the game state. Check if the level is loaded.
     * If no more levels left display a message and go to the
     * add high score state.
     */
    public void update(GameLoop gameLoop, long elapsedTime) {
        timeInState += elapsedTime;
        
        if (levelLoaded && 
                ((!gameLoop.isNetworkGame() && timeInState > 300) 
                        || friendReady)) {
            finished = true;
            nextGameState = GameState.GAME_STATE_RUNNING;    
        }
        else if (gameFinished && timeInState > 7000) {
            // Game finished, display message and goto
            // add high score state
            finished = true;
            nextGameState = GameState.GAME_STATE_HIGH_SCORE;
        }

    }

    /**
     * Render the loading message and dialogs if any. 
     */
    public void render(GameLoop gameLoop) {
        Graphics g = gameLoop.getScreenManager().getGraphics();

        
        gameLoop.getStaticObjectsManager().render(g);
        gameLoop.getPlayerManager().render(g);
        
        renderLoading(g);
        
        gameLoop.getGUIManager().render(g);
        
        gameLoop.getScreenManager().show();
    }
    
    /**
     * Handle incoming packet.
     * If this is a network game and this computer is not the 
     * controller it waits for the controller to send the level details.
     * Otherwise it waits for the ready signal after sending 
     * the level details.
     */
    public void handlePacket(Packet packet) {

        if (packet instanceof NewLevelPacket) {
            levelsManager.nextLevel();	// Update the level
            
            NewLevelPacket newLevel = (NewLevelPacket)packet;
            // Get the enemy ships models
            Collection enemyShipsModels = newLevel.getEnemyShipsModels();
            
            // Build Ships from the models
            Map enemyShips = new HashMap();
            Iterator modelsItr = enemyShipsModels.iterator();
            while (modelsItr.hasNext()) {
                ShipModel model = (ShipModel) modelsItr.next();
                EnemyShip ship = new EnemyShip(model);
                enemyShips.put(new Integer(ship.getHandlerId()), ship);
            }
            
            // Update the ship managers
            gameLoop.getEnemyShipsManager().newLevel(enemyShips);
            gameLoop.getPlayerManager().newLevel(enemyShips.values());
            
            // Signal the network player that this computer is ready to play
            Packet ready = new SystemPacket(gameLoop.getGameNetworkManager().getSenderId(),
                    gameLoop.getGameNetworkManager().getReceiverId(), 
                    SystemPacket.TYPE_READY_TO_PLAY);
            
            gameLoop.getGameNetworkManager().sendPacket(ready);
            
            levelLoaded = true;
            friendReady = true;
            
            packet.setConsumed(true);
            
        } 
        else if (packet instanceof SystemPacket) {
            int type = ((SystemPacket)packet).getType();
            if (type == SystemPacket.TYPE_READY_TO_PLAY) {
                friendReady = true;
            }
            
            packet.setConsumed(true);
        }
        else {
            // We consume packets anyway in the loading level state
            // to eliminate packets left from the previous level
            packet.setConsumed(true);
        }
    }
    
    /**
     * Returns true if the current game state is finished.
     * @return	True if the current state is finished
     */
    public boolean isFinished() {
        return finished;
    }
    
    /**
     * Returns the next game state after the current game state is finished.
     * @return Next game state.
     */
    public int getNextGameState() {
        return nextGameState;
    }
    
    /**
     * Returns the id of this game state
     * @return	Id of this game state
     */
    public int getGameStateId() {
        return GameState.GAME_STATE_LOADING;
    }
    
    /**
     * Renders loading message on the screen.
     */
    private void renderLoading(Graphics g) {
        
        // Paint anti-aliased text
        Graphics2D g2d = (Graphics2D)g;
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, 
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        
        Dimension screenDimention = 
            gameLoop.getScreenManager().getScreenDimension();
        
        g.setFont(ResourceManager.getFont(Font.BOLD, 20));
        g.setColor(Color.BLUE);
        
        FontMetrics metrics = g.getFontMetrics();
        
        int width = metrics.stringWidth(loadingStr1);
        int middleX = screenDimention.width / 2;
        int middleY = screenDimention.height / 2;
        
        g.drawString(loadingStr1 , middleX - width/2, middleY);
        
        width = metrics.stringWidth(loadingStr2);
        
        g.drawString(loadingStr2 , middleX - width/2, 
                middleY + metrics.getHeight() + 5);
        
    }
    
    /**
     * Sets the loading level message.
     * @param line1	First line to diplay
     * @param line2 Second line to display
     */
    private void setLoadingStr(String line1, String line2) {
        this.loadingStr1 = line1;
        this.loadingStr2 = line2;
    }
    
    // Private inner class that implements runnable and used to
    // load new level in a new thread
    private class LevelLoaderThread implements Runnable {
        
        public void run() {
           
            Logger.printMemoryUsage("New level memory usage before:");
            // Ask the garbage collector to run before starting a new level
            System.gc();
            Logger.printMemoryUsage("New level memory usage after:");

			if (levelsManager.isLastLevel()) {
			    gameFinished = true;
			    setLoadingStr("Congratulations!","You have finished the game!");
			    return;
			}

			int curLevel = levelsManager.getCurrentLevel();
            
            setLoadingStr("Loading level " + (curLevel+1) + ".... ", "");
            
            if (gameLoop.isController()) {
                // Only the controller machine loads the file and then send
                // the data to the other player
                Map enemyShips = levelsManager.loadNextLevel();
                gameLoop.getEnemyShipsManager().newLevel(enemyShips);
                gameLoop.getPlayerManager().newLevel(enemyShips.values());
                
                // If it's a network game 
                if (gameLoop.isNetworkGame()) {
                    
                    // Build ship models collection
                    Collection enemyShipsModels = new ArrayList(enemyShips.size());
                    Iterator enemyShipsItr = enemyShips.values().iterator();
                    while (enemyShipsItr.hasNext()) {
                        Ship ship = (Ship) enemyShipsItr.next();
                        ShipModel model = ship.getShipModel();
                        enemyShipsModels.add(model);
                    }
                    
                    // Send the level data to the network player
                    Packet newLevel = new NewLevelPacket(
                            gameLoop.getGameNetworkManager().getSenderId(),
                            gameLoop.getGameNetworkManager().getReceiverId(),
                            enemyShipsModels);
                    
                    gameLoop.getGameNetworkManager().sendPacket(newLevel);
                    
                    
                    // Signal ready to play
                    Packet ready = new SystemPacket(gameLoop.getGameNetworkManager().getSenderId(),
                            gameLoop.getGameNetworkManager().getReceiverId(), 
                            SystemPacket.TYPE_READY_TO_PLAY);
                    
                    gameLoop.getGameNetworkManager().sendPacket(ready);
                    
                    setLoadingStr("Level " + (curLevel+1) + " loaded.",
            			"Waiting for online player...");
                    
                }
                levelLoaded = true;

            }
            else if (gameLoop.isNetworkGame()) {
                levelsManager.loadLocalLevelData(curLevel+1);
                setLoadingStr("Loading level " + (curLevel+1) + ".... ",
    				"Waiting for online player data...");
            }
        }
        
    }	// end inner class LevelLoaderThread

}	// end class LoadingLevelState

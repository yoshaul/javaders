
package game;

import game.gamestate.*;
import game.highscore.HighScoresManager;
import game.input.InputManager;
import game.network.client.GameNetworkManager;

import java.util.*;

/**
 * The <code>GameLoop</code> is the object that runs the various
 * game states and switches from one game state to another when it's
 * finished.
 * This class initializes most of the game manager objects and gives
 * the various states the ability to access the managers.
 */
public class GameLoop implements Runnable {

    private ScreenManager screenManager;
    private GUIManager guiManager;
    private LevelsManager levelsManager;
    private InputManager inputManager;
    private GameNetworkManager gameNetworkManager;
    private StaticObjectsManager staticObjectsManager;
    private EnemyShipsManager enemyShipsManager;
    private PlayerManager playerManager;
    private HighScoresManager highScoresManager;
    private boolean networkGame;
    
    // True if objects random events are controlled from local machine
    private boolean controller;

    // The various game states
    private GameState curGameState, loadingState, runningState, 
    	addHighScoreState;
    
    private Map gameStatesById;
    
    /**
     * Construct the game loop and initialize objects.
     * @param networkGame	True if it's a network game.
     * @param controller	True if this machine is the controller.
     * @param highScoresManager	The high scores manager
     * @param gnm			Game network manager. Null in a 
     * single player game.
     */
    public GameLoop(boolean networkGame, boolean controller, 
            HighScoresManager highScoresManager, GameNetworkManager gnm) {
        
        this.networkGame = networkGame;
        this.controller = controller;
        this.highScoresManager = highScoresManager;
        this.gameNetworkManager = gnm;
        init();
    }
    
    /**
     * initialize the game managers.
     */
    private void init() {
        
        screenManager = ScreenManager.getInstance();
        screenManager.setFullScreen();
        guiManager = new GUIManager(this, screenManager);
        inputManager = new InputManager(this/*, playerManager.getLocalPlayerShip()*/);
        
        playerManager = new PlayerManager(this);
        
        screenManager.getFullScreenWindow().addKeyListener(inputManager);
        
        enemyShipsManager = new EnemyShipsManager(this);
        enemyShipsManager.addTarget(playerManager.getLocalPlayerShip());
        if (networkGame) {
            enemyShipsManager.addTarget(playerManager.getNetworkPlayerShip());
        }

        staticObjectsManager = new StaticObjectsManager(this);
        
        levelsManager = new LevelsManager(this);
        
        // Create the various game states and add them to the game
        // states list
        gameStatesById = new HashMap();
        loadingState = new LoadingLevelState(this);
        gameStatesById.put(new Integer(loadingState.getGameStateId()),
                loadingState);
        
        runningState = new GameRunningState(this);
        gameStatesById.put(new Integer(runningState.getGameStateId()),
                runningState);
        
        addHighScoreState = new AddHighScoreState(this);
        gameStatesById.put(new Integer(addHighScoreState.getGameStateId()), 
                addHighScoreState);
        
        // Init all the game states
        Iterator gameStatesItr = gameStatesById.values().iterator();
        while(gameStatesItr.hasNext()) {
            GameState gameState = (GameState)gameStatesItr.next();
            gameState.init();
        }
        
        // Set current game state to loading state
        curGameState = loadingState;
        
    }
    
    /**
     * The main loop iterates while the game is not finished and
     * calls the current state methods.
     */
    public void run() {

        long prevFrameTime = System.currentTimeMillis();
        curGameState.start();
        
        while(!inputManager.isQuit()) {

            long currFrameTime = System.currentTimeMillis();
            
            try {
                Thread.sleep(GameConstants.FRAME_SLEEP_TIME);
            }
            catch (InterruptedException ie) {
                //
            }
            
            long elapsedTime = currFrameTime - prevFrameTime;
            prevFrameTime = currFrameTime;
            
            curGameState.gatherInput(this, elapsedTime);
            curGameState.update(this, elapsedTime);
            curGameState.render(this);
            
            if (curGameState.isFinished()) {
                changeGameState();
            }
            
        }        
        
        finalizeGame();
        
    }	// end method run
    

    /**
     * Finalize the running game. Release resources.
     */
    private void finalizeGame() {
        if (isNetworkGame()) {
            gameNetworkManager.cleanup();
        }
        screenManager.exitFullScreen();
        guiManager.restoreRepaintManager();
    }
    
	/**
	 * Change the current game state.
	 * Take the next state from the finished state and 
	 * call it's start method.
	 */    
    private void changeGameState() {
        // Switch game state
        int nextStateId = curGameState.getNextGameState();
        curGameState = (GameState)
    		gameStatesById.get(new Integer(nextStateId));
        curGameState.start();
    }
    
    /**
     * Sets this machine to be the controller (if the controller
     * player quits the network game).
     */
    public void setController(boolean controller) {
        this.controller = controller;
    }
    
    /**
     * Returns true if this machine is the controller.
     */
    public boolean isController() {
        return this.controller;
    }
    
    /**
     * Sets the networkGame flag. (If the network player quits
     * we turn the flag off to stop sending packets).
     */
    public void setNetworkGame(boolean networkGame) {
        this.networkGame = networkGame;
    }
    
    /**
     * Returns true if it's a network game.
     */
    public boolean isNetworkGame() {
        return this.networkGame;
    }
    
    /**
     * Returns the game network manager.
     */
    public GameNetworkManager getGameNetworkManager() {
        return this.gameNetworkManager;
    }

    /**
     * Returns the game network manager.
     */
    public ScreenManager getScreenManager() {
        return screenManager;
    }
    
    /**
     * Returns the game network manager.
     */
    public PlayerManager getPlayerManager() {
        return playerManager;
    }
    
    /**
     * Returns the game network manager.
     */
    public LevelsManager getLevelsManager() {
        return this.levelsManager;
    }
    
    /**
     * Returns the enemy ships manager.
     */
    public EnemyShipsManager getEnemyShipsManager() {
        return this.enemyShipsManager;
    }
    
    /**
     * Returns the static objects manager.
     */
    public StaticObjectsManager getStaticObjectsManager() {
        return this.staticObjectsManager;
    }
    
    /**
     * Returns the GUI manager.
     */
    public GUIManager getGUIManager() {
        return this.guiManager;
    }
    
    /**
     * Returns the high scores manager.
     */
    public HighScoresManager getHighScoresManager() {
        return this.highScoresManager;
    }
    
    /**
     * Returns the input manager.
     */
    public InputManager getInputManager() {
        return this.inputManager;
    }
    
}

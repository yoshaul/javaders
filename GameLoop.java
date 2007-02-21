
package game;

import game.highscore.HighScoresManager;
import game.input.InputManager;
import game.network.GameNetworkManager;
import game.network.packet.*;
import game.state.*;

import java.awt.*;
import java.util.*;



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
    private boolean levelLoaded;
    private boolean networkGame;
    private boolean controller;	// True if objects random events are controlled from local machine
    private boolean friendReady;
    private GameState curGameState, loadingState, runningState, 
    	gameOverState, addHighScoreState;
    
    private Map gameStatesById;
    
    public GameLoop(boolean networkGame, boolean controller, 
            HighScoresManager highScoresManager, GameNetworkManager gnm) {
        
        this.networkGame = networkGame;
        this.controller = controller;
        this.highScoresManager = highScoresManager;
        this.gameNetworkManager = gnm;
        init();
    }
    
    private void init() {
        
        screenManager = new ScreenManager();
        
        screenManager.setFullScreen();
        guiManager = new GUIManager(this, screenManager, 
                inputManager, highScoresManager);
        
        playerManager = new PlayerManager(this);
        
        inputManager = new InputManager(this, playerManager.getLocalPlayerShip());
        screenManager.getFullScreenWindow().addKeyListener(inputManager);
        screenManager.getFullScreenWindow().addMouseListener(inputManager);
        
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
        
//        highScoresManager = new HighScoresManager(10);
        
    }
    
//    public void setNetworkManager(GameNetworkManager 
//            gameNetworkManager) {
//        
//        this.gameNetworkManager = gameNetworkManager;
//        
//    }

    public void run() {

        long prevFrameTime = System.currentTimeMillis();
        curGameState.start();
        
        while(!inputManager.isExited()/* && !playerManager.isGameOver()*/) {

//            if (enemyShipsManager.isLevelFinished() && curGameState == null) {
//                loadNextLevel();
//                prevFrameTime = System.currentTimeMillis();
//                curGameState = new LoadingLevelState();
//            }
            
            long currFrameTime = System.currentTimeMillis();
            
            try {
                Thread.sleep(30);
            }
            catch (InterruptedException ie) {
                //
            }
            
            long elapsedTime = currFrameTime - prevFrameTime;
            prevFrameTime = currFrameTime;
            
            curGameState.gatherInput(this, elapsedTime);
            curGameState.update(this, elapsedTime);
            curGameState.render(this);
            setGameState(curGameState.getNextGameState());

        }        
        screenManager.exitFullScreen();
        
    }	// end method run
    
    private void gatherInput(long elapsedTime) {
        
        inputManager.gatherInput(elapsedTime);

        if (networkGame) {
            gameNetworkManager.gatherInput(enemyShipsManager, playerManager);    
        }
        
    }
        
    private void render() {
        Graphics g = screenManager.getGraphics();

        staticObjectsManager.render(g);
        enemyShipsManager.render(g);
        playerManager.render(g);
        guiManager.render(g);

        g.dispose();

        screenManager.show();
        
    }
    
    private void newGame() {
        System.out.println("NEW GAME");
    }

    public void handlePacket(Packet packet) {
    

    }
    
    public boolean isController() {
        return this.controller;
    }
    
    public boolean isNetworkGame() {
        return this.networkGame;
    }
    
    public GameNetworkManager getGameNetworkManager() {
        return this.gameNetworkManager;
    }
    

    public ScreenManager getScreenManager() {
        return screenManager;
    }
    
    public PlayerManager getPlayerManager() {
        return playerManager;
    }
    
    public LevelsManager getLevelsManager() {
        return this.levelsManager;
    }
    
    public EnemyShipsManager getEnemyShipsManager() {
        return this.enemyShipsManager;
    }
    
    public StaticObjectsManager getStaticObjectsManager() {
        return this.staticObjectsManager;
    }
    
    public GUIManager getGUIManager() {
        return this.guiManager;
    }
    
    public HighScoresManager getHighScoresManager() {
        return this.highScoresManager;
    }
    
    public InputManager getInputManager() {
        return this.inputManager;
    }
    
    public void setGameState(int newGameStateId) {
        if (newGameStateId != curGameState.getGameStateId()) {
//            switch (newGameState) {
//                case GameState.GAME_STATE_LOADING:
//                    curGameState = loadingState;
//                    break;
//                case GameState.GAME_STATE_RUNNING:
//                    curGameState = runningState;
//                	break;
//                	
//                case GameState.GAME_STATE_HIGH_SCORE:
//                    curGameState = addHighScoreState;
//                	break;
//            }
            curGameState = (GameState)
            	gameStatesById.get(new Integer(newGameStateId));
            
            curGameState.start();
        }
    }
}

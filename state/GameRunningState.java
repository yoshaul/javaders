package game.state;

import java.awt.Graphics;

import game.EnemyShipsManager;
import game.GUIManager;
import game.GameLoop;
import game.LevelsManager;
import game.PlayerManager;
import game.ScreenManager;
import game.StaticObjectsManager;
import game.highscore.HighScoresManager;
import game.input.InputManager;
import game.network.GameNetworkManager;

public class GameRunningState implements GameState {

    private int nextGameState;
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
//    private boolean controller;	// True if objects random events are controlled from local machine

    
    public GameRunningState(GameLoop gameLoop) {
        this.screenManager = gameLoop.getScreenManager();
        this.guiManager = gameLoop.getGUIManager();
        this.levelsManager = gameLoop.getLevelsManager();
        this.inputManager = gameLoop.getInputManager();
        this.gameNetworkManager = gameLoop.getGameNetworkManager();
        this.staticObjectsManager = gameLoop.getStaticObjectsManager();
        this.enemyShipsManager = gameLoop.getEnemyShipsManager();
        this.playerManager = gameLoop.getPlayerManager();
        this.highScoresManager = gameLoop.getHighScoresManager();
        
        this.networkGame = gameLoop.isNetworkGame();
    }
    
    public void init() {
        // TODO Auto-generated method stub

    }

    public void cleanup() {
        // TODO Auto-generated method stub

    }

    public void pause() {
        // TODO Auto-generated method stub

    }

    public void resume() {
        // TODO Auto-generated method stub

    }

    public void start() {
        nextGameState = GameState.GAME_STATE_RUNNING;

    }

    public void gatherInput(GameLoop gameLoop, long elapsedTime) {
        
        inputManager.gatherInput(elapsedTime);

        if (networkGame) {
            gameNetworkManager.gatherInput(enemyShipsManager, playerManager);    
        }
        

    }

    public void update(GameLoop gameLoop, long elapsedTime) {
        if (!inputManager.isPaused() /*&& 
                !(networkGame && !gameNetworkManager.isReady())*/) {
                
                enemyShipsManager.update(elapsedTime);
                playerManager.update(elapsedTime);
        }
        
        if (enemyShipsManager.isLevelFinished()) {
            nextGameState = GameState.GAME_STATE_LOADING;
        }
        else if (playerManager.isGameOver()) {
          
          inputManager.setPaused(true);

          nextGameState = GAME_STATE_HIGH_SCORE;

        }
        
    }

    public void render(GameLoop gameLoop) {
        Graphics g = screenManager.getGraphics();

        staticObjectsManager.render(g);
        enemyShipsManager.render(g);
        playerManager.render(g);
        guiManager.render(g);

        g.dispose();

        screenManager.show();

    }
    
    public int getNextGameState() {
        return nextGameState;
    }
    
    public int getGameStateId() {
        return GameState.GAME_STATE_RUNNING;
    }

}

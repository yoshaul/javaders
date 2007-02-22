package game.gamestate;

import java.awt.Graphics;

import game.*;
import game.highscore.HighScoresManager;
import game.input.InputManager;
import game.network.client.GameNetworkManager;
import game.network.packet.Packet;

/**
 * The <code>GameRunningState</code> is the central game state. In this
 * state the game is in running mode, meaning the player is able to play
 * and game logic is running through the various manager objects. 
 */
public class GameRunningState implements GameState {

    private final static int INTERNAL_STATE_NORMAL = 1;
    private final static int INTERNAL_STATE_LEVEL_CLEARED = 2;
    
    private int nextGameState;
    private ScreenManager screenManager;
    private GUIManager guiManager;
    private InputManager inputManager;
    private GameNetworkManager gameNetworkManager;
    private StaticObjectsManager staticObjectsManager;
    private EnemyShipsManager enemyShipsManager;
    private PlayerManager playerManager;
    private HighScoresManager highScoresManager;
    private boolean networkGame;
    private boolean finished;
    private int internalState;
    private long timeInState;

    /**
     * Construct the game state.
     * @param gameLoop	Reference to the game loop.
     */
    public GameRunningState(GameLoop gameLoop) {
        this.screenManager = gameLoop.getScreenManager();
        this.guiManager = gameLoop.getGUIManager();
        this.inputManager = gameLoop.getInputManager();
        this.gameNetworkManager = gameLoop.getGameNetworkManager();
        this.staticObjectsManager = gameLoop.getStaticObjectsManager();
        this.enemyShipsManager = gameLoop.getEnemyShipsManager();
        this.playerManager = gameLoop.getPlayerManager();
        this.highScoresManager = gameLoop.getHighScoresManager();
        this.networkGame = gameLoop.isNetworkGame();
    }
    
    /**
     * Initialize state.
     */
    public void init() {
        // Nothing to initialize
    }

    /**
     * This method is called once when this state is set to be 
     * the active state.
     * @see game.gamestate.GameState init method
     */
    public void start() {
        finished = false;
        screenManager.showCursor(false);
        setInternalState(INTERNAL_STATE_NORMAL);
    }

    /**
     * Gather input from the player and from the network.
     */
    public void gatherInput(GameLoop gameLoop, long elapsedTime) {
        
        inputManager.gatherInput();
        if (!inputManager.isPaused()) {
            playerManager.gatherInput();
        }
        if (networkGame) {
            gameNetworkManager.gatherInput(this);    
        }

    }

    /**
     * Update the game state.
     * Most of the game logic starts from here. We call the ships 
     * managers update methods. 
     */
    public void update(GameLoop gameLoop, long elapsedTime) {
        if (!inputManager.isPaused()) {
            timeInState += elapsedTime;
            enemyShipsManager.update(elapsedTime);
            playerManager.update(elapsedTime);
        }
        
        if (!(internalState == INTERNAL_STATE_LEVEL_CLEARED) && 
                enemyShipsManager.isLevelFinished()) {
            setInternalState(INTERNAL_STATE_LEVEL_CLEARED);
        }
        else if (playerManager.isGameOver()) {
            finished = true;
            inputManager.setPaused(true);

            nextGameState = GAME_STATE_HIGH_SCORE;

        } else if (internalState == INTERNAL_STATE_LEVEL_CLEARED &&
                timeInState > 2000) {
            finished = true;
            nextGameState = GAME_STATE_LOADING;
        }
        
    }

    /**
     * Render the game state.
     */
    public void render(GameLoop gameLoop) {
        Graphics g = screenManager.getGraphics();

        staticObjectsManager.render(g);
        enemyShipsManager.render(g);
        playerManager.render(g);
        guiManager.render(g);

        g.dispose();

        screenManager.show();

    }

    /**
     * Handle network packet. This callback method is called
     * from the <code>GameNetworkManager</code> in the input
     * gathering part.
     */
    public void handlePacket(Packet packet) {
        enemyShipsManager.handlePacket(packet);
        playerManager.handlePacket(packet);
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
        return GameState.GAME_STATE_RUNNING;
    }
    
    /**
     * Sets the internal state to the new state and sets the time
     * in state to 0.
     * @param state	New internal state
     */
    private void setInternalState(int state) {
        this.internalState = state;
        this.timeInState = 0;
    }

}

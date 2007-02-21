package game.state;

import java.awt.Graphics;

import game.GUIManager;
import game.GameLoop;
import game.gui.AddHighScoreDialog;
import game.gui.PostHighScoreDialog;
import game.highscore.HighScore;
import game.highscore.HighScoresManager;

public class AddHighScoreState implements GameState {

    private final static int INTERNAL_STATE_ADD_HIGH_SCORE = 1;
    private final static int INTERNAL_STATE_POST_SCORE = 2;
    
    private GameLoop gameLoop;
    private AddHighScoreDialog addHighScoreDialog;
    private PostHighScoreDialog postScoreDialog;
    private HighScoresManager highScoresManager;
    private GUIManager guiManager;
    private String playerName;
    private long timeInState;
    private boolean levelLoaded;
    private int nextGameState;
    private int internalState;
    private long playerScore;
    private int level;
    
    public AddHighScoreState(GameLoop gameLoop) {
        this.gameLoop = gameLoop;
        this.highScoresManager = gameLoop.getHighScoresManager();
        this.guiManager = gameLoop.getGUIManager();
    }
    
    public void init() {
        
        addHighScoreDialog = new AddHighScoreDialog(gameLoop, this, 
                highScoresManager);

        guiManager.addDialog(addHighScoreDialog);
        
        postScoreDialog = new PostHighScoreDialog(gameLoop, 
                highScoresManager);
        
        guiManager.addDialog(postScoreDialog);
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
        timeInState = 0;
        nextGameState = GameState.GAME_STATE_HIGH_SCORE;
        
        playerScore = 
            gameLoop.getPlayerManager().getLocalPlayerShip().getScore();
        
        level = gameLoop.getLevelsManager().getCurrentLevel();
        
        if (highScoresManager.isHighScore(playerScore, level)) {
            
            internalState = INTERNAL_STATE_ADD_HIGH_SCORE;
            
            HighScoresManager highScoresManager = 
                gameLoop.getHighScoresManager();
            
            popAddHighScoreDialog(playerScore, level, highScoresManager);
        }
        else {
            internalState = INTERNAL_STATE_POST_SCORE;
            popPostScoreDialog(new HighScore(playerName, playerScore, level));
        }
    }

    public void gatherInput(GameLoop gameLoop, long elapsedTime) {
        
    }

    public void update(GameLoop gameLoop, long elapsedTime) {
        
        timeInState += elapsedTime;
        
        if (internalState == INTERNAL_STATE_ADD_HIGH_SCORE &&
                addHighScoreDialog.isFinished()) {
            /** TODO: show high scores */
            if (playerName != null) {
                internalState = INTERNAL_STATE_POST_SCORE;
                popPostScoreDialog(
                        new HighScore(playerName, playerScore, level));
            }
            else {
                gameLoop.getInputManager().setQuit(true);
            }
        }
        
        if (internalState == INTERNAL_STATE_POST_SCORE &&
                postScoreDialog.isFinished()) {
            
            gameLoop.getInputManager().setQuit(true);
            
        }

    }

    public void render(GameLoop gameLoop) {

        Graphics g = gameLoop.getScreenManager().getGraphics();
        
        gameLoop.getStaticObjectsManager().render(g);
        gameLoop.getEnemyShipsManager().render(g);
        gameLoop.getGUIManager().render(g);
        
        g.dispose();

        gameLoop.getScreenManager().show();
        
    }

    public int getNextGameState() {
        return nextGameState;
    }


    public int getGameStateId() {
        return GameState.GAME_STATE_HIGH_SCORE;
    }
    
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
    
    private void popAddHighScoreDialog(long score, int level, 
            HighScoresManager highScoresManager) {
        
        addHighScoreDialog.addHighScore(score, level);
        
    }
    
    private void popPostScoreDialog(HighScore score) {
        postScoreDialog.popPostHighScore(score);
    }

}

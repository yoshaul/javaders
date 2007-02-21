package game.state;

import java.awt.Graphics;

import game.GUIManager;
import game.GameLoop;
import game.gui.AddHighScoreDialog;
import game.highscore.HighScoresManager;

public class AddHighScoreState implements GameState {

    private GameLoop gameLoop;
    private AddHighScoreDialog addHighScoreDialog;
    private HighScoresManager highScoresManager;
    private GUIManager guiManager;
    private long timeInState;
    private boolean levelLoaded;
    private int nextGameState;
    
    public AddHighScoreState(GameLoop gameLoop) {
        this.gameLoop = gameLoop;
        this.highScoresManager = gameLoop.getHighScoresManager();
        this.guiManager = gameLoop.getGUIManager();
    }
    
    public void init() {
        
        addHighScoreDialog = new AddHighScoreDialog(gameLoop, true, 
                highScoresManager);

        guiManager.addDialog(addHighScoreDialog);
        
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
        
        long playerScore = 
            gameLoop.getPlayerManager().getLocalPlayerShip().getScore();
        
        int level = gameLoop.getLevelsManager().getCurrentLevel();
        
        HighScoresManager highScoresManager = gameLoop.getHighScoresManager();
        
        popAddHighScoreDialog(playerScore, level, highScoresManager);
        
    }

    public void gatherInput(GameLoop gameLoop, long elapsedTime) {
        
    }

    public void update(GameLoop gameLoop, long elapsedTime) {
        
        timeInState += elapsedTime;
        
        if (addHighScoreDialog.isFinished()) {
            /** TODO: show high scores */
//            nextGameState =
            gameLoop.getInputManager().setQuit(true);
//            gameLoop.getGUIManager().get
            
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
    
    private void popAddHighScoreDialog(long score, int level, 
            HighScoresManager highScoresManager) {
        
        addHighScoreDialog.addHighScore(score, level);
        
    }

}

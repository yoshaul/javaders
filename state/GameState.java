package game.state;

import game.GameLoop;

public interface GameState {

    public static final int GAME_STATE_RUNNING = 1;
    public static final int GAME_STATE_LOADING = 2;
    public static final int GAME_STATE_HIGH_SCORE = 3;
    
    public void init();
    public void cleanup();
    public void pause();
    public void resume();
    public void start();
    
    public void gatherInput(GameLoop gameLoop, long elapsedTime);
    public void update(GameLoop gameLoop, long elapsedTime);
    public void render(GameLoop gameLoop);
    
    public int getNextGameState();
    public int getGameStateId();
    
    
}

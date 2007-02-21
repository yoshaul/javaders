
package game;

import game.input.InputManager;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.util.Collection;


public class GameLoop implements Runnable {

    private ScreenManager screenManager;
//    private GUIManager guiManager;
    private Window gameWindow;
    private LevelsManager levelsManager;
    private InputManager inputManager;
    private StaticObjectsManager staticObjectsManager;
    private EnemyShipsManager enemyShipsManager;
    private PlayerManager playerManager;
    
    
    public GameLoop(boolean multiplayer) {
        init(multiplayer);
    }
    
    private void init(boolean multiplayer) {
        
        screenManager = new ScreenManager();
        
        screenManager.setFullScreen();
//        guiManager = new GUIManager(screenManager, inputManager);
        
        gameWindow = screenManager.getFullScreenWindow();

        playerManager = new PlayerManager(multiplayer);
        
        inputManager = new InputManager(playerManager.getPlayerShip());
        gameWindow.addKeyListener(inputManager);
        gameWindow.addMouseListener(inputManager);
        
        enemyShipsManager = new EnemyShipsManager();
        enemyShipsManager.addTarget(playerManager.getPlayerShip());
        if (multiplayer) {
            enemyShipsManager.addTarget(playerManager.getPlayerShip());    
        }

        staticObjectsManager = new StaticObjectsManager();
        
        levelsManager = new LevelsManager();

    }

    public void run() {

        long prevFrameTime = System.currentTimeMillis();
        
        while(!inputManager.isExited()) {

            if (enemyShipsManager.isLevelFinished()) {
                Collection enemyShips = levelsManager.loadNextLevel();
                enemyShipsManager.setEnemyShips(enemyShips);
                playerManager.addTarget(enemyShips);
            }
            
            long currFrameTime = System.currentTimeMillis();
            
            try {
                Thread.sleep(30);
            }
            catch (InterruptedException ie) {
                //
            }
            
            long elapsedTime = currFrameTime - prevFrameTime;
            prevFrameTime = currFrameTime;
            
            gatherInput(elapsedTime);
            
            if (!inputManager.isPaused()) {
                enemyShipsManager.update(elapsedTime);
                playerManager.update(elapsedTime);
            }
            
            render();
            
        }
        
        screenManager.exitFullScreen();
        
//        System.exit(0);

    }	// end method run
    
    private void gatherInput(long elapsedTime) {
        
        inputManager.gatherInput(elapsedTime);
        
    }
        
    private void render() {
        
        BufferStrategy bs = gameWindow.getBufferStrategy();
        Graphics g = bs.getDrawGraphics();
        
        staticObjectsManager.render(g);
        enemyShipsManager.render(g);
        playerManager.render(g);
//        guiManager.render(g);
        
        g.dispose();

        if (!bs.contentsLost()) {
            bs.show();
        }
        
    }

}

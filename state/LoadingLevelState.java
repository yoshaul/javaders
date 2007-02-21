package game.state;

import game.GameLoop;
import game.network.GameNetworkManager;
import game.network.packet.*;
import game.ship.*;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.*;

public class LoadingLevelState implements GameState {

    private GameLoop gameLoop;
    private long timeInState;
    private boolean levelLoaded;
    private boolean friendReady;
    private int nextGameState;

    
    public LoadingLevelState(GameLoop gameLoop) {
        this.gameLoop = gameLoop;
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
        timeInState = 0;
        levelLoaded = friendReady = false;
        nextGameState = getGameStateId();
        
        // Load the new level in a new thread
        new Thread(new LevelLoaderThread()).start();
        
    }


    public void gatherInput(GameLoop gameLoop, long elapsedTime) {
        
        if (gameLoop.isNetworkGame()) {
            GameNetworkManager gnm = gameLoop.getGameNetworkManager();
            
            Packet packet = gnm.getNextPacket();
            if (packet != null) {
                handlePacket(packet);
            }            
        }
        
    }


    public void update(GameLoop gameLoop, long elapsedTime) {
        timeInState += elapsedTime;
        
        if (levelLoaded && timeInState > 3000) {
            if (!gameLoop.isNetworkGame() || friendReady) {
                nextGameState = GameState.GAME_STATE_RUNNING;    
            }
        }

    }


    public void render(GameLoop gameLoop) {
        Graphics g = gameLoop.getScreenManager().getGraphics();

        // Paint anti-aliased text
        if (g instanceof Graphics2D) {
            Graphics2D g2d = (Graphics2D)g;
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, 
                    RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        }
        
        gameLoop.getStaticObjectsManager().renderLoading(g);
        gameLoop.getPlayerManager().render(g);
        
        gameLoop.getScreenManager().show();
    }
    
    public void handlePacket(Packet packet) {
        
        if (packet instanceof NewLevelPacket) {
            NewLevelPacket newLevel = (NewLevelPacket)packet;
            Collection enemyShipsModels = newLevel.getEnemyShipsModels();
            
            // Build Ships from the models
            Map enemyShips = new HashMap();
            Iterator modelsItr = enemyShipsModels.iterator();
            while (modelsItr.hasNext()) {
                ShipModel model = (ShipModel) modelsItr.next();
                EnemyShip ship = new EnemyShip(model);
                enemyShips.put(new Integer(ship.getObjectID()), ship);
            }
            
            gameLoop.getEnemyShipsManager().newLevel(enemyShips);
            gameLoop.getPlayerManager().newLevel(enemyShips.values());
            
//            gameLoop.getEnemyShipsManager().setEnemyShips(enemyShips);
//            gameLoop.getPlayerManager().addTarget(enemyShips.values());
            
            // Signal ready to play
            Packet ready = new SystemPacket(gameLoop.getGameNetworkManager().getSenderID(),
                    gameLoop.getGameNetworkManager().getReceiverID(), 
                    SystemPacket.TYPE_READY_TO_PLAY);
            
            gameLoop.getGameNetworkManager().sendPacket(ready);
            
//            gameLoop.getGameNetworkManager().setCurReady(true);
            
            levelLoaded = true;
            friendReady = true;
            
        } 
        else if (packet instanceof SystemPacket) {
            // Handle the packet immediately
            int type = ((SystemPacket)packet).getType();

            if (type == SystemPacket.TYPE_READY_TO_PLAY) {
                friendReady = true;
            }
        }
    }
    
    public int getNextGameState() {
        return nextGameState;
    }
    
    public int getGameStateId() {
        return GameState.GAME_STATE_LOADING;
    }
    
    
    // Private inner class that implements runnable and used to
    // load new level in a new thread
    private class LevelLoaderThread implements Runnable {
        
        public void run() {
            
            if (gameLoop.isNetworkGame()) {
                gameLoop.getGameNetworkManager().newLevel();
            }
            
            if (gameLoop.isController()) {
                // Only the controller machine loads the file and then send
                // the data to the other player
                Map enemyShips = gameLoop.getLevelsManager().loadNextLevel();
                gameLoop.getEnemyShipsManager().newLevel(enemyShips);
                gameLoop.getPlayerManager().newLevel(enemyShips.values());
                
//                gameLoop.getEnemyShipsManager().setEnemyShips(enemyShips);
//                gameLoop.getPlayerManager().addTarget(enemyShips.values());
                
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
                            gameLoop.getGameNetworkManager().getSenderID(),
                            gameLoop.getGameNetworkManager().getReceiverID(),
                            enemyShipsModels);
                    
                    gameLoop.getGameNetworkManager().sendPacket(newLevel);
                    
                    
                    // Signal ready to play
                    Packet ready = new SystemPacket(gameLoop.getGameNetworkManager().getSenderID(),
                            gameLoop.getGameNetworkManager().getReceiverID(), 
                            SystemPacket.TYPE_READY_TO_PLAY);
                    
                    gameLoop.getGameNetworkManager().sendPacket(ready);
                    
//                    gameLoop.getGameNetworkManager().setCurReady(true);
                    
                }
                levelLoaded = true;                
            }
        }
        
    }	// end inner class LevelLoaderThread

}	// end class LoadingLevelState

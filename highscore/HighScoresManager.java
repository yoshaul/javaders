package game.highscore;

import game.GameConstants;
import game.network.NetworkManager;

import java.io.*;

public class HighScoresManager {

    private final String HIGH_SCORES_FILE_NAME = 
        GameConstants.RESOURCES + "/" + "scores.dat";
    
    private int numOfHighScores;	// Max number of high scores
    private HighScore[] highScores;	// Array with all the high scores
    
    private NetworkManager networkManager;
    
    public HighScoresManager(int numOfHighScores) {
        
        this.numOfHighScores = numOfHighScores;
        this.highScores = new HighScore[numOfHighScores];
        
        try {
            loadHighScores(false);
        }
        catch (IOException ioe) {
            System.err.println("Unable to read the high scores file");
            ioe.printStackTrace();
        }
        catch (ClassNotFoundException cnfe) {
            System.err.println("Unable to read the high scores file");
            cnfe.printStackTrace();
        }
        
    }
    
    public void setNetworkManager(NetworkManager networkManager) {
        this.networkManager = networkManager;
    }
    
    /**
     * Returns array with all the high scores.
     */
    public HighScore[] getHighScores() {
        return this.highScores;
    }
    
    /**
     * Returns max number of high scores. 
     */
    public int getNumOfHighScores() {
        return numOfHighScores;
    }
    
    /**
     * Adds score to the high scores list. If the score is smaller or equals
     * to the current smallest score it is not added to the list. If the score
     * is not the smallest and the list is full we remove the last score.  
     * @param score	New high score to insert to the list.
     */
    public void addScore(HighScore score, boolean save) {
        int place = -1;
        boolean placeFound = false;
        for (int i = 0; !placeFound && i < highScores.length; i++) {
            if (highScores[i] == null || score.compareTo(highScores[i]) > 0) {
                placeFound = true;
                place = i;
            }
        }
        
        if (placeFound) {
            if (highScores[place] == null || place == highScores.length-1) {
                // If null or last place in array simply insert the new
                // high score to the proper place
                highScores[place] = score;
            }
            else {
                // Insert the new high score to the middle of the high scores
                // array. Move the lower scores one place. Remove the last one
                // if high scores exceeds numOfHighScores (the array length).
                for (int i = highScores.length - 1; i > place; i--) {
                    highScores[i] = highScores[i-1];
                }
                
                // Insert the new high score to the proper place
                highScores[place] = score;
            }
            
            if (save) {
	            // Save the high scores list to file
	            try {
	                saveHighScores();    
	            }
	            catch (IOException ioe) {
	                System.err.println("Unable to save the high scores file");
	                ioe.printStackTrace();
	            }
            }
            
        }

    }
    
    public boolean isHighScore(long score, int level) {
        return isHighScore(new HighScore(null, score, level));
    }
    
    /**
     * Returns true if the input high score has place in the high scores list.
     */
    public boolean isHighScore(HighScore score) {
        if (score.getScore() > 0 && 
                (highScores[numOfHighScores-1] == null ||
                highScores[numOfHighScores-1].compareTo(score) < 0)) {
            return true;
        }
        
        return false;
    }
    
    
    /**
     * Return array of <code>HighScore</code> objects containing the high
     * scores starting at <code>fromPlace</code> inclusive and ending at
     * <code>toPlace</code> inclusive.
     * Place 1 is the highest score.
     * @param fromPlace
     * @param toPlace
     * @return
     * @throws IllegalArgumentException If fromPlace is smaller then 1
     */
    public HighScore[] getHighScores(int fromPlace, int toPlace) 
    		throws IllegalArgumentException {
        
        if (fromPlace < 1 || toPlace > numOfHighScores) {
            throw new IllegalArgumentException("Input out of bounds");
        }
        
        if (fromPlace > toPlace) {
            throw new IllegalArgumentException("First argument must be " +
            		"smaller or equal to the second argument");
        }

        HighScore[] ret = new HighScore[toPlace-fromPlace+1];
        
        System.arraycopy(highScores, fromPlace-1, ret, 0, ret.length);
        
        return ret;
    }
    
    
    /**
     * Return array of <code>HighScore</code> objects containing the high
     * scores starting at <code>fromPlace</code> inclusive and ending at
     * <code>toPlace</code> inclusive.
     * Place 1 is the highest score.
     * @param fromPlace
     * @param toPlace
     * @return
     * @throws IllegalArgumentException If fromPlace is smaller then 1
     */
    public HighScore[] getNetworkHighScores(int fromPlace, int toPlace) {

        if (networkManager != null) {
            return networkManager.getHighScores(fromPlace, toPlace);
        }
        
        return null;

    }
    
    
    
    /**
     * Clears the high scores list.
     */
    public void clearHighScores() {
        for (int i = 0; i < highScores.length; i++) {
            highScores[i] = null;
        }
    }
    
    public void loadHighScores(boolean clearOldScores) throws 
    		IOException, ClassNotFoundException {
        
        if (clearOldScores) {
            clearHighScores();            
        }
        
        File scoresFile = new File(HIGH_SCORES_FILE_NAME);
        
        if (scoresFile.exists()) {
            ObjectInputStream ois = new ObjectInputStream(
                    new FileInputStream(scoresFile));
            
            HighScore score;
            try {
	            while ((score = (HighScore)ois.readObject()) != null) {
	                this.addScore(score, false);
	            }
            }
            catch (EOFException eofe) {
                // Reached the end of file
            }
            
            ois.close();
        }
        
    }
    
    public void saveHighScores() throws IOException {
        
        File scoresFile = new File(HIGH_SCORES_FILE_NAME);
        
        if (!scoresFile.exists()) {
            scoresFile.createNewFile();
        }
        
        ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(scoresFile));
        
        for (int i = 0; i < highScores.length && highScores[i] != null; i++) {
            oos.writeObject(highScores[i]);
        }
        
        oos.close();
    }
    
    public void postScoreToServer(HighScore score) {
        if (networkManager != null) {
            networkManager.postHighScore(score);
        }
    }
    
    public String toString() {
        
        String ret = "High Scores:\n" + 
        	"Place\t" + "Name\t\t" + "Score\t" + "Level\n\n";
        
        for (int i = 0; i < highScores.length && highScores[i] != null; i++) {
            ret += (i+1) + ".\t" + highScores[i] + "\n"; 
        }
        
        return ret;
    }
    
}

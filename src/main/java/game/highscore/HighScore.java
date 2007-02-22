
package game.highscore;

import java.io.Serializable;

/**
 * The <code>HighScore</code> class encapsulates a player
 * score details. 
 */
public class HighScore implements Serializable, Comparable {

    private String playerName;
    private long score;
    private int level;
    
    /**
     * Construct a new HighScore object.
     * @param playerName	Name of the player
     * @param score			Score the player reached
     * @param level			Level the player reached in the game.
     */
    public HighScore(String playerName, long score, int level) {
        
        this.playerName = playerName;
        this.score = score;
        this.level = level;
        
    }
    
    /**
     * Returns the level reached by the player.
     * @return Level reached by the player.
     */
    public int getLevel() {
        return level;
    }
    
    /**
     * Sets the level.
     * @param level	Level to set.
     */
    public void setLevel(int level) {
        this.level = level;
    }
    
    /**
     * Returns the player name.
     * @return	Player name.
     */
    public String getPlayerName() {
        return playerName;
    }
    
    /**
     * Sets the player name.
     * @param playerName	Player name.
     */
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
    
    /**
     * Returns the player score.
     * @return	The score
     */
    public long getScore() {
        return score;
    }
    
    /**
     * Sets the score.
     * @param score	Score to set.
     */
    public void setScore(long score) {
        this.score = score;
    }
    
    /**
     * Compare two high scores.
     * @see java.lang.Comparable
     */
    public int compareTo(Object obj) {
        
        HighScore score = (HighScore)obj;
        
        if (this.score < score.score) {
            return -1;
        }
        else if (this.score > score.score) {
            return 1;
        }
        else { // (this.score == score.score)
            if (this.level < score.level) {
                return -1;
            }
            else if (this.level > score.level) {
                return 1;
            }
            else {
                return 0;
            }
        }
    }
    
    public String toString() {
        return playerName + "\t\t" + score + "\t" + level; 
    }
}

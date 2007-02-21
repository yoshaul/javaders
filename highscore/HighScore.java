package game.highscore;

import java.io.Serializable;

public class HighScore implements Serializable, Comparable {

    private String playerName;
    private long score;
    private int level;
    
    public HighScore(String playerName, long score, int level) {
        
        this.playerName = playerName;
        this.score = score;
        this.level = level;
        
    }
    
    public int getLevel() {
        return level;
    }
    
    public void setLevel(int level) {
        this.level = level;
    }
    
    public String getPlayerName() {
        return playerName;
    }
    
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
    
    public long getScore() {
        return score;
    }
    
    public void setScore(long score) {
        this.score = score;
    }
    
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

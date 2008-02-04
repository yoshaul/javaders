
package game.highscore;

import game.util.ResourceManager;

import java.awt.*;

/**
 * The <code>HighScoresRenderer</code> class is used to render the high
 * scores given a <code>Graphics</code> object with bounding rectangle.
 */
public class HighScoresRenderer {

    /**
     * Render the input high scores using the gtaphic device.
     * @param g	Graphics object
     * @param highScores	High scores array
     */
    public static void render(Graphics g, Rectangle bounds, 
            HighScore[] highScores) {
        
        if (highScores == null) {
            // Create empty high scores array
            highScores = new HighScore[1];
        }
        
        // Get the number of occupied high scores
        int numberOfHighScores = 0;
        if (highScores[0] != null) {
            int i;
            for (i = 0; i < highScores.length && highScores[i] != null; i++) {
                // iterate
            }
            numberOfHighScores = i;
        }
    
        final int numColumns = 4;
        final int leftMargins = 10;	// 10 pixels from the left
        final int topMargins = 20;	// 20 pixels from the top
        
        int columnWidth = bounds.width / numColumns;

        // Each column takes different percentage of the screen width
        // Rank column is the narrowest and name is the widest
        int rankWidth = Math.round(bounds.width * 0.15f);
        int nameWidth = Math.round(bounds.width * 0.35f);
        int scoreWidth = Math.round(bounds.width * 0.30f);
        
        // Calculate the columns places
        int rankPlace = leftMargins;
        int namePlace = rankPlace + rankWidth;
        int scorePlace = namePlace + nameWidth;
        int levelPlace = scorePlace + scoreWidth;
        
        FontMetrics fm = g.getFontMetrics();
        int fontHeight = fm.getHeight();
        int horizontalSpace = fontHeight + 5;
        
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, bounds.width, bounds.height);
        
        g.setColor(Color.BLUE);
        
        g.setFont(ResourceManager.getFont(Font.BOLD, 16));
        // Draw headline
        g.drawString("Rank", 	rankPlace,  topMargins);
        g.drawString("Player", 	namePlace,  topMargins);
        g.drawString("Score", 	scorePlace, topMargins);
        g.drawString("Level", 	levelPlace, topMargins);
        
        g.setFont(ResourceManager.getFont(Font.PLAIN, 14));
        for (int i = 0; i < numberOfHighScores; i++) {
            
            // Draw the high score rank
            g.drawString((i+1)+"", rankPlace,
                    topMargins + horizontalSpace*(i+1));
            
            // Draw player name
            String playerName = highScores[i].getPlayerName();
            if (fm.stringWidth(playerName) > columnWidth) {
                // If string too long take only the first 7 chars
                // and add three dots
                playerName = playerName.substring(0, 7) + "...";
            }
            g.drawString(playerName, namePlace, 
                    topMargins + horizontalSpace*(i+1));
            
            // Draw score
            g.drawString(highScores[i].getScore()+"", scorePlace, 
                    topMargins + horizontalSpace*(i+1));
            
            // Draw level
            g.drawString(highScores[i].getLevel()+"", levelPlace, 
                    topMargins + horizontalSpace*(i+1));
        }
        
    }
    
}

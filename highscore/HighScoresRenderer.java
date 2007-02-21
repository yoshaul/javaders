package game.highscore;

import java.awt.*;

public class HighScoresRenderer {

    public static void render(Graphics g, HighScore[] highScores) {
        
        if (highScores == null) {
            // Create empty high scores array
            highScores = new HighScore[1];
        }
        
        // Get the number of occupied high scores
        int numberOfHighScores = 0;
        if (highScores[0] == null) {
            numberOfHighScores = 0;
        } else {
            int i;
            for (i = 0; i < highScores.length && highScores[i] != null; i++) {
                //
            }
            numberOfHighScores = i;
        }

        // Set the desired font if different from default font
        String family = "Serif";
        int style = Font.PLAIN;
        int size = 12;
        Font font = new Font(family, style, size);
        g.setFont(font);
    
        FontMetrics fontMetrics = g.getFontMetrics();

        int fontHeight = fontMetrics.getHeight();
        
        Rectangle bounds = g.getClipBounds();
        
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, bounds.width, bounds.height);
        
        g.setColor(Color.BLUE);
        
        // Draw headline
        g.drawString("Rank", 	10,  20);
        g.drawString("Player", 	60,  20);
        g.drawString("Score", 	160, 20);
        g.drawString("Level", 	260, 20);
        
        for (int i = 0; i < numberOfHighScores; i++) {
            
            // Draw the high score rank
            g.drawString((i+1)+"", 10, 20 + 20*(i+1));
            
            // Draw player name
            g.drawString(highScores[i].getPlayerName(), 60, 20 + 20*(i+1));
            
            // Draw score
            g.drawString(highScores[i].getScore()+"", 160, 20 + 20*(i+1));
            
            // Draw level
            g.drawString(highScores[i].getLevel()+"", 260, 20 + 20*(i+1));
        }
        
        
    }
    
}

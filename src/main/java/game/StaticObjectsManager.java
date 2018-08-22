/*
 * This file is part of Javaders.
 * Copyright (c) Yossi Shaul
 *
 * Javaders is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Javaders is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Javaders.  If not, see <http://www.gnu.org/licenses/>.
 */

package game;

import game.graphic.GraphicsHelper;
import game.ship.PlayerShip;

import java.awt.*;

/**
 * The <code>StaticObjectsManager</code> handles the static objects
 * that needs to be rendered on the screen (except for the game menus
 * which the <code>GUIManager</code> handles
 */
public class StaticObjectsManager {

    private GameLoop gameLoop;

    private Image bgImage;    // Background image
    private static final String defBGImageName = "bg2_1024.jpg";

    /**
     * Construct new StaticObjectsManager and load the
     * default background image.
     *
     * @param gameLoop Reference to the game loop.
     */
    public StaticObjectsManager(GameLoop gameLoop) {

        this.gameLoop = gameLoop;

        // Load the default background image
        setBackgroundImage(defBGImageName);

    }

    /**
     * Sets the background image.
     *
     * @param imageName Name of the image to load (from the images
     *                  directory).
     */
    public void setBackgroundImage(String imageName) {
        bgImage = gameLoop.getScreenManager().getCompatibleImage(
                imageName, Transparency.OPAQUE);
    }

    /**
     * Render static objects on the screen (background image,
     * player score, etc.).
     */
    public void render(Graphics g) {

        Dimension screenDimention =
                gameLoop.getScreenManager().getScreenDimension();

        // Draw background image
        g.drawImage(bgImage, 0, 0,
                screenDimention.width, screenDimention.height, null);

        // Draw player statistics
        PlayerShip player1Ship =
                gameLoop.getPlayerManager().getPlayer1Ship();

        PlayerShip player2Ship =
                gameLoop.getPlayerManager().getPlayer2Ship();


        g.setFont(new Font(null, Font.BOLD, 12));
        g.setColor(Color.GREEN);

        GraphicsHelper.setAntialiasedText((Graphics2D) g);

        int level = gameLoop.getLevelsManager().getCurrentLevel();
        g.drawString("Level " + level, 10, 15);

        if (player1Ship != null) {
            g.drawString("SCORE: " + player1Ship.getScore(), 10, screenDimention.height - 35);
            g.drawString("POWER: " + player1Ship.getArmor(), 10, screenDimention.height - 20);
        }

        if (player2Ship != null) {

            String scoreText = "SCORE: " + player2Ship.getScore();
            int textWidth = g.getFontMetrics().stringWidth(scoreText);
            int rightAlignment = Math.max(90, textWidth + 10);

            g.drawString(scoreText,
                    screenDimention.width - rightAlignment, screenDimention.height - 35);
            g.drawString("POWER: " + player2Ship.getArmor(),
                    screenDimention.width - rightAlignment, screenDimention.height - 20);
        }
    }

}

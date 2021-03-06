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

package game.ship.bonus;

import game.GameConstants;
import game.graphic.GraphicsHelper;
import game.network.client.GameNetworkManager;
import game.network.packet.Packet;
import game.network.packet.PowerUpPacket;
import game.util.ResourceManager;

import java.awt.*;


/**
 * The <code>PowerUp</code> class is a <code>Bonus</code> which
 * gives the player more armor.
 */
public class PowerUp extends Bonus {

    private static final String imageName = "red_ball.png";
    private int powerUp;    // How much armor to add

    /**
     * Construct a new power up.
     *
     * @param x       Vertical location
     * @param y       Horizontal location
     * @param powerUp Power to add to the ship
     */
    public PowerUp(float x, float y, int powerUp) {

        super(x, y);

        // Load the image for the button and draw the power
        // up on the image
        Image srcImage = ResourceManager.loadImage(
                GameConstants.IMAGES_DIR + imageName);

        Image image = GraphicsHelper.getCompatibleImage(
                srcImage.getWidth(null), srcImage.getHeight(null),
                Transparency.TRANSLUCENT);

        Graphics2D g = (Graphics2D) image.getGraphics();
        g.drawImage(srcImage, 0, 0, null);
        g.setFont(ResourceManager.getFont(16));
        g.setColor(Color.GREEN);
        GraphicsHelper.drawInMiddle(g, image, powerUp + "");
        g.dispose();

        this.setSpriteImage(image);

        this.powerUp = powerUp;

    }

    /**
     * Returns the power this bonus gives.
     *
     * @return The power this bonus gives.
     */
    public int getPowerUp() {
        return this.powerUp;
    }

    /**
     * Create a <code>PowerUpPacket</code> to send over the net.
     */
    @Override
    public void createPacket(GameNetworkManager netManager) {

        // Prepare the PowerUpPacket
        Packet packet = new PowerUpPacket(netManager.getSenderId(),
                netManager.getReceiverId(), GameConstants.ENEMY_MANAGER_ID,
                x, y, powerUp);

        netManager.sendPacket(packet);
    }

}

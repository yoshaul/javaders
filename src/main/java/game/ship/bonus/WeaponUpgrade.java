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
import game.network.client.GameNetworkManager;
import game.network.packet.Packet;
import game.network.packet.WeaponUpgradePacket;
import game.ship.weapon.WeaponFactory;
import game.util.ResourceManager;

import java.awt.*;

/**
 * The <code>WeaponUpgrade</code> class is a <code>Bonus</code> which
 * gives the player one weapon level upgrade or the opportunity to
 * switch the weapon of the ship.
 */
public class WeaponUpgrade extends Bonus {


    private int weaponType;    // Type of the weapon this bonus gives

    /**
     * Construct a new WeaponUpgrade
     *
     * @param x          Vertical location
     * @param y          Horizontal location
     * @param weaponType Type of the weapon this bonus gives
     */
    public WeaponUpgrade(float x, float y, int weaponType) {

        super(x, y);
        this.weaponType = weaponType;
        Image image = ResourceManager.loadImage(GameConstants.IMAGES_DIR +
                WeaponFactory.getWeaponImageByType(weaponType));
        setSpriteImage(image);

    }


    /**
     * Returns the weapon type this bonus represents.
     *
     * @return The weapon type this bonus represents.
     */
    public int getWeaponType() {
        return this.weaponType;
    }

    /**
     * Create a <code>WeaponUpgradePacket</code> to send over the net.
     */
    public void createPacket(GameNetworkManager netManager) {

        // Prepare the PowerUpPacket
        Packet packet = new WeaponUpgradePacket(netManager.getSenderId(),
                netManager.getReceiverId(), GameConstants.ENEMY_MANAGER_ID,
                x, y, weaponType);

        netManager.sendPacket(packet);
    }

}

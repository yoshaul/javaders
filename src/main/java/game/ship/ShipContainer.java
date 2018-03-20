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

package game.ship;

import game.network.client.GameNetworkManager;
import game.ship.bonus.Bonus;
import game.ship.weapon.Bullet;

/**
 * The <code>ShipContainer</code> defines the methods that an
 * object containing and manages ships should implement.
 * Through this interface the ships can cummunicate with other
 * game objects.
 */
public interface ShipContainer {

    /**
     * Adds a new ship to the ship container.
     *
     * @param ship Ship to add.
     */
    void addShip(Ship ship);

    /**
     * Adds a shot to the ship container shot collection.
     *
     * @param shot Shot to add.
     */
    void addShot(Bullet shot);

    /**
     * Adds a bonus to the ship container bonuses.
     *
     * @param bonus Bonus to add.
     */
    void addBonus(Bonus bonus);

    /**
     * Returns the network manager of the game.
     *
     * @return Game network manager.
     */
    GameNetworkManager getNetworkManager();

    /**
     * Returns the network handler id of the ship container.
     *
     * @return Network handler id of the ship container.
     */
    int getHandlerId();

    /**
     * Returns true if the game is a network game.
     *
     * @return True if in network game.
     */
    boolean isNetworkGame();

    /**
     * Returns true if this machine is the controller machine.
     *
     * @return True if this machine is the controller machine.
     */
    boolean isController();

}

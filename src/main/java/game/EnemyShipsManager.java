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

import game.network.client.GameNetworkManager;
import game.network.packet.*;
import game.ship.*;
import game.ship.bonus.Bonus;
import game.ship.bonus.PowerUp;
import game.ship.bonus.WeaponUpgrade;
import game.ship.weapon.Bullet;
import game.ship.weapon.BulletModel;
import game.ship.weapon.WeaponFactory;

import java.awt.*;
import java.util.*;

/**
 * The <code>EnemyShipsManager</code> manage the enemy ships and
 * their shots and bonuses. It also implements the <code>ShipContainer</code>
 * interface to allow the ships to communicate with it.
 */
public class EnemyShipsManager implements Renderable,
        ShipContainer, PacketHandler {

    private final int handlerID = GameConstants.ENEMY_MANAGER_ID;

    private GameLoop gameLoop;

    /**
     * Map of the enemy ships. Object id as key, Ship object as value
     */
    private Map<Integer, Ship> enemyShips;

    /**
     * Collection of active shots fired by the ships
     */
    private Collection<Bullet> shots;
    /**
     * Collection of active bonuses dropped by the ships
     */
    private Collection<Bonus> bonuses;
    /**
     * Collection of targets for the enemy ships (i.e., player ship(s))
     */
    private Collection<Target> targets;


    /**
     * Construct the EnemyShipsManager, init the collections.
     *
     * @param gameLoop Reference to the game loop
     */
    public EnemyShipsManager(GameLoop gameLoop) {
        this.gameLoop = gameLoop;
        enemyShips = new HashMap<Integer, Ship>();
        shots = new ArrayList<Bullet>();
        bonuses = new ArrayList<Bonus>();
        targets = new ArrayList<Target>();
    }

    /**
     * Get ready for a new level. Clear reminders from previous
     * level and set the enemy ships.
     *
     * @param enemyShips Map of enemy ships for the current level
     */
    public void newLevel(Map<Integer, Ship> enemyShips) {
        // Make sure no objects left from previous level
        this.shots.clear();
        this.bonuses.clear();
        this.enemyShips.clear();
        setEnemyShips(enemyShips);
    }

    /**
     * Adds a new target to the targets collection.
     *
     * @param target New target to add.
     */
    public void addTarget(Target target) {
        targets.add(target);
    }

    /**
     * Adds a collection of targets.
     *
     * @param targets Collection of Target objects.
     */
    public void addTarget(Collection<Target> targets) {
        targets.addAll(targets);
    }

    /**
     * Adds ship to the manager and sets its manager to be this object.
     *
     * @param ship Ship to add.
     */
    @Override
    public void addShip(Ship ship) {
        ship.setShipContainer(this);
        enemyShips.put(ship.getHandlerId(), ship);
    }

    /**
     * Adds bonus to the bonuses collection.
     *
     * @param bonus Bonus to add.
     */
    @Override
    public void addBonus(Bonus bonus) {
        bonuses.add(bonus);
    }

    /**
     * Adds shot to the shots collection.
     *
     * @param shot Shot to add.
     */
    @Override
    public void addShot(Bullet shot) {
        shots.add(shot);
    }

    /**
     * Sets the enemy ships managed by this object. Sets this object
     * to be the ships container.
     *
     * @param enemyShips New map of ships.
     */
    private void setEnemyShips(Map<Integer, Ship> enemyShips) {
        for (Ship ship : enemyShips.values()) {
            ship.setShipContainer(this);
        }
        this.enemyShips = enemyShips;
    }

    /**
     * Updates the state of all the managed objects.
     *
     * @param elapsedTime Time elapsed since last call to this method
     *                    in milliseconds.
     */
    public void update(long elapsedTime) {

        Dimension screenDimension =
                gameLoop.getScreenManager().getScreenDimension();

        Insets insets =
                gameLoop.getScreenManager().getScreenInsets();

        // Update ships
        Iterator<Ship> shipsItr = enemyShips.values().iterator();
        while (shipsItr.hasNext()) {
            Ship ship = shipsItr.next();

            if (ship.isDestroyed()) {
                // Remove the destroyed ship
                shipsItr.remove();
            } else {

                ship.update(elapsedTime);

                // If the ship exits the screen and still in the wrong
                // direction, change its velocity so it will get back
                if (ship.getX() < insets.left && ship.getDx() < 0) {
                    ship.setDx(-ship.getDx());
                }
                if (ship.getX() + ship.getWidth() >
                        screenDimension.width - insets.right
                        && ship.getDx() > 0) {
                    ship.setDx(-ship.getDx());
                }
                if (ship.getY() < insets.top && ship.getDy() < 0) {
                    ship.setDy(-ship.getDy());
                }
                if (ship.getY() + ship.getHeight() >
                        screenDimension.height - insets.bottom
                        && ship.getDy() > 0) {
                    ship.setDy(-ship.getDy());
                }

            }
        }

        // Update shots
        Iterator<Bullet> shotsItr = shots.iterator();
        while (shotsItr.hasNext()) {
            Bullet shot = shotsItr.next();
            shot.updatePosition(elapsedTime);
            if (isOutOfScreen(shot)) {
                shotsItr.remove();
            }
        }

        // Update bonuses
        Iterator<Bonus> bonusesItr = bonuses.iterator();
        while (bonusesItr.hasNext()) {
            Bonus bonus = bonusesItr.next();
            bonus.updatePosition(elapsedTime);
            if (isOutOfScreen(bonus)) {
                bonusesItr.remove();
            }
        }

        ////////////////////////
        // Process Collisions //
        ////////////////////////

        // Process ship-to-ship collisions 
        shipsItr = enemyShips.values().iterator();
        while (shipsItr.hasNext()) {
            Ship ship = shipsItr.next();
            ship.processCollisions(targets);
        }

        // Process shots to player ship(s) collisions
        shotsItr = shots.iterator();
        while (shotsItr.hasNext()) {
            Bullet shot = shotsItr.next();
            shot.processCollisions(targets);
            if (shot.isHit()) {
                shotsItr.remove();
            }

        }

        // Process bonuses to player ship(s) collisions
        bonusesItr = bonuses.iterator();
        while (bonusesItr.hasNext()) {
            Bonus bonus = bonusesItr.next();
            bonus.processCollisions(targets);
            if (bonus.isHit()) {
                bonusesItr.remove();
            }
        }

    }

    /**
     * Renders all relevant objects and data (ships, shots, etc.)
     */
    @Override
    public void render(Graphics g) {
        // Render ships
        for (Ship ship : enemyShips.values()) {
            ship.render(g);
        }

        // Render shots
        for (Bullet shot : shots) {
            shot.render(g);
        }

        for (Bonus bonus : bonuses) {
            bonus.render(g);
        }

    }

    /**
     * Return true if the sprite is off the screen bounds.
     *
     * @param sprite Sptite to test.
     * @return True if the sprite is off the screen bounds
     */
    private boolean isOutOfScreen(Sprite sprite) {
        Dimension screenDimension =
                gameLoop.getScreenManager().getScreenDimension();

        return sprite.getX() + sprite.getWidth() < 0 ||
                sprite.getX() > screenDimension.width ||
                sprite.getY() + sprite.getHeight() < 0 ||
                sprite.getY() > screenDimension.height;
    }

    /**
     * Returns true if the level is finished. The level is finished if
     * all the enemy ships destroyed and there are no active bonuses.
     *
     * @return True if the level is finished.
     */
    public boolean isLevelFinished() {
        return enemyShips.isEmpty() && bonuses.isEmpty();
    }

    /**
     * Returns true if this macine is the controller.
     */
    @Override
    public boolean isController() {
        return gameLoop.isController();
    }

    /**
     * Return true if this is a network game.
     */
    @Override
    public boolean isNetworkGame() {
        return gameLoop.isNetworkGame();
    }

    /**
     * Returns the game network manager. Null if this is not
     * a network game.
     */
    @Override
    public GameNetworkManager getNetworkManager() {
        return gameLoop.getGameNetworkManager();
    }

    /**
     * Handles incoming packets. If the packet should be handle by
     * the maneger than handle it otherwise search for a ship to
     * to handle it.
     *
     * @param packet Packet to handle
     */
    @Override
    public void handlePacket(Packet packet) {

        if (packet instanceof BulletPacket) {
            BulletPacket bulletPacket = (BulletPacket) packet;

            BulletModel model = bulletPacket.getBulletModel();

            Ship owningShip = enemyShips.get(
                    packet.getHandlerId());

            if (owningShip != null) { // The ship might be destroyed

                Bullet bullet = WeaponFactory.getBullet(model,
                        owningShip);

                addShot(bullet);
            }

            packet.setConsumed(true);

        } else if (packet instanceof PowerUpPacket) {
            PowerUpPacket powerPacket = (PowerUpPacket) packet;
            Bonus powerUp = new PowerUp(powerPacket.getX(),
                    powerPacket.getY(), powerPacket.getPowerUp());

            addBonus(powerUp);
            packet.setConsumed(true);
        } else if (packet instanceof WeaponUpgradePacket) {
            WeaponUpgradePacket wuPacket = (WeaponUpgradePacket) packet;
            Bonus weaponUpgrade = new WeaponUpgrade(wuPacket.getX(),
                    wuPacket.getY(), wuPacket.getWeaponType());

            addBonus(weaponUpgrade);
            packet.setConsumed(true);
        } else {
            // Check if one of the ships can handle it
            Integer handlerID = packet.getHandlerId();
            Ship ship = enemyShips.get(handlerID);
            if (ship != null) {
                ship.handlePacket(packet);
            }
        }

    }

    /**
     * Currently this object doesn't creates it's own packets
     */
    @Override
    public void createPacket(GameNetworkManager netManager) {
        // Currently this object doesn't creates it's own packets
    }

    /**
     * Returns the network handler id of this object.
     */
    @Override
    public int getHandlerId() {
        return this.handlerID;
    }

}

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

import game.network.packet.Packet;
import game.network.packet.ShipPacket;
import game.ship.bonus.Bonus;
import game.ship.bonus.PowerUp;
import game.ship.bonus.WeaponUpgrade;
import game.ship.weapon.Bullet;
import game.ship.weapon.Weapon;
import game.ship.weapon.WeaponFactory;
import game.sound.SoundFactory;
import game.util.Logger;

import java.awt.*;
import java.util.Collection;

/**
 * The <code>PlayerShip</code> class extends the abstract Ship class
 * and defines special behaviours for the player ship.
 */
public class PlayerShip extends Ship {

    private final long MAX_TIME_BETWEEN_PACKETS = 1000;

    private long score;            // Score the player made
    private boolean vulnerable = true;    // True if the player is vulnurable

    /**
     * Construct a new player ship.
     *
     * @see Ship#Ship(int, int, float, float, ShipProperties)
     */
    public PlayerShip(int objectId, int shipType,
                      float x, float y, ShipProperties prop) {

        super(objectId, shipType, x, y, prop.getMaxDX(), prop.getMaxDY(),
                prop.getImage(), WeaponFactory.getWeapon(
                        prop.getWeaponType(), prop.getWeaponLevel(),
                        prop.getWeaponDirection()),
                prop.getArmor(), prop.getDamage(),
                prop.getHitScoreValue(), prop.getDestroyScoreValue());

        if (Logger.isInvulnerable()) {
            vulnerable = false;
        }

    }

    /**
     * Override the update method. Call the super method and send
     * packet if necessary.
     *
     * @see Ship#update(long)
     */
    @Override
    public void update(long elapsedTime) {

        super.update(elapsedTime);

        if (shipContainer.isNetworkGame() &&
                timeSinceLastPacket > MAX_TIME_BETWEEN_PACKETS) {
            // Send state update
            createPacket(shipContainer.getNetworkManager());
        }

    }

    /**
     * Override the render methos. Call the super method and
     * render some more data.
     */
    @Override
    public void render(Graphics g) {

        if (isActive()) {
            super.render(g);

            // If not vulnerable draw a bounding sphere
            if (!vulnerable) {
                Graphics2D g2 = (Graphics2D) g;

                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);

                g.setColor(Color.GREEN);
                g.drawOval(Math.round(getX()) - 3,
                        Math.round(getY()) - 10, getWidth() + 6,
                        getHeight() + 20);
            }
        }

    }

    /**
     * Process ship-to-ship collisions only if this ship
     * is vulnerable.
     */
    @Override
    public void processCollisions(Collection targets) {

        if (vulnerable) {
            super.processCollisions(targets);
        }
    }

    /**
     * Returns true if the player ship is vulnerable.
     *
     * @return True if the player ship is vulnerable.
     */
    public boolean isVulnerable() {
        return vulnerable;
    }

    /**
     * Sets the vulnerable state of the ship.
     *
     * @param vulnerable True if ship vulnerable.
     */
    public void setVulnerable(boolean vulnerable) {
        this.vulnerable = vulnerable;
    }

    /**
     * Add score to the player.
     *
     * @param value Score to add.
     */
    public void addScore(long value) {
        score += value;
    }

    /**
     * Returns the player score.
     *
     * @return The player's score.
     */
    public long getScore() {
        return this.score;
    }

    /**
     * Returns the player's ship state.
     */
    @Override
    public ShipState getShipState() {
        return new PlayerShipState(x, y, dx, dy, armor, state, score);
    }

    /**
     * Hit the player ship if the ship is vulnerable.
     */
    @Override
    public void hit(long damage) {
        if (vulnerable) {
            super.hit(damage);
        }
    }

    /**
     * Hit only if vulnerable.
     */
    @Override
    public void hit(Bullet bullet) {
        if (vulnerable && isNormal()) {
            SoundFactory.playSound("playerHit.wav");
            super.hit(bullet);
        }
    }

    /**
     * Hit the player ship with a bonus.
     */
    @Override
    public void hit(Bonus bonus) {
        if (isNormal()) {
            if (bonus instanceof PowerUp) {
                PowerUp powerUp = (PowerUp) bonus;

                SoundFactory.playSound("bonus.wav");

                long power = powerUp.getPowerUp();
                armor += power;

            } else if (bonus instanceof WeaponUpgrade) {
                WeaponUpgrade weaponUpgrade = (WeaponUpgrade) bonus;

                SoundFactory.playSound("weapon_bonus.wav");

                int weaponType = weaponUpgrade.getWeaponType();

                if (weaponType == this.weapon.getWeaponType()) {
                    // Same weapon, increase the level by 1
                    weapon.upgradeWeapon();
                } else {
                    // Different weapon, just replace current weapon 
                    Weapon newWeapon = WeaponFactory.getWeapon(weaponType,
                            weapon.getWeaponLevel(), Weapon.DIRECTION_UP);

                    newWeapon.setOwner(this);

                    this.setWeapon(newWeapon);
                }
            }
        }
    }    // end method hit

    /**
     * Handle incoming packet.
     */
    @Override
    public void handlePacket(Packet packet) {

        super.handlePacket(packet);

        if (packet instanceof ShipPacket) {
            ShipPacket shipPacket = (ShipPacket) packet;
            PlayerShipState shipState =
                    (PlayerShipState) shipPacket.getShipState();
            this.score = shipState.getScore();
        }

    }

    /**
     * This method is called when the <code>PlayerShipManager</code>
     * wants to force the player ship to send packet on the next
     * update.
     */
    public void forcePacket() {
        // Set the time since last packet to the max time
        // to force the sending of a ship packet
        timeSinceLastPacket = MAX_TIME_BETWEEN_PACKETS;
    }

}

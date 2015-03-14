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

package game.ship.weapon;

import game.sound.SoundFactory;

/**
 * The <code>LaserCannon</code> weapon mainly fires lasers.
 */
public class LaserCannon extends AbstractWeapon {

    private static final long BASE_FIRING_RATE = 950;

    private long lastFiringTime;

    /**
     * Construct a new weapon.
     *
     * @param direction   Base horizontal direction of the weapon.
     * @param weaponLevel Level of the weapon.
     */
    public LaserCannon(int direction, int weaponLevel) {
        super(direction, weaponLevel,
                WeaponFactory.TYPE_LASER_CANNON, BASE_FIRING_RATE);
        this.lastFiringTime = 0;
        updateFiringRate();
    }

    /**
     * Fire a new bullet(s).
     */
    @Override
    public void fire(float x, float y) {

        long now = System.currentTimeMillis();
        long elapsedTime = now - lastFiringTime;

        if (elapsedTime >= firingRate) {
            lastFiringTime = now;

            if (weaponLevel == 1) {
                // Fire one laser up
                Bullet shoot = new LaserBeam(getOwner(), x, y, 0.0f,
                        direction * 0.2f);

                if (owner.getShipContainer().isNetworkGame()) {
                    // Create and send packet
                    shoot.createPacket(
                            owner.getShipContainer().getNetworkManager());
                }

                getOwner().getShipContainer().addShot(shoot);

                SoundFactory.playSound("enemylaser.wav");

            } else if (weaponLevel == 2) {
                // Fire two lasers up
                Bullet shoot1 = new LaserBeam(getOwner(), x - 6, y, 0.0f,
                        direction * 0.2f);

                Bullet shoot2 = new LaserBeam(getOwner(), x + 6, y, 0.0f,
                        direction * 0.2f);

                if (owner.getShipContainer().isNetworkGame()) {
                    // Create and send packet
                    shoot1.createPacket(
                            owner.getShipContainer().getNetworkManager());
                    shoot2.createPacket(
                            owner.getShipContainer().getNetworkManager());
                }

                getOwner().getShipContainer().addShot(shoot1);
                getOwner().getShipContainer().addShot(shoot2);

                SoundFactory.playSound("enemylaser.wav");
            } else if (weaponLevel == 3) {
                // Fire one blue laser up
                Bullet shoot = new BlueLaser(getOwner(), x, y, 0.0f,
                        direction * 0.2f);


                if (owner.getShipContainer().isNetworkGame()) {
                    // Create and send packet
                    shoot.createPacket(
                            owner.getShipContainer().getNetworkManager());
                }

                getOwner().getShipContainer().addShot(shoot);

                SoundFactory.playSound("enemylaser.wav");

            } else if (weaponLevel == 4) {
                // Fire two diagonal lasers and one up
                Bullet shoot1 = new LaserBeam(getOwner(), x - 6, y, 0.05f,
                        direction * 0.2f);

                Bullet shoot2 = new LaserBeam(getOwner(), x + 6, y, -0.05f,
                        direction * 0.2f);

                Bullet shoot3 = new LaserBeam(getOwner(), x, y, 0.0f,
                        direction * 0.2f);

                if (owner.getShipContainer().isNetworkGame()) {
                    // Create and send packet
                    shoot1.createPacket(
                            owner.getShipContainer().getNetworkManager());
                    shoot2.createPacket(
                            owner.getShipContainer().getNetworkManager());
                    shoot3.createPacket(
                            owner.getShipContainer().getNetworkManager());
                }

                getOwner().getShipContainer().addShot(shoot1);
                getOwner().getShipContainer().addShot(shoot2);
                getOwner().getShipContainer().addShot(shoot3);

                SoundFactory.playSound("enemylaser.wav");
            } else if (weaponLevel == 5) {
                // Fire two blue lasers up
                Bullet shoot1 = new BlueLaser(getOwner(), x - 6, y, 0.0f,
                        direction * 0.2f);

                Bullet shoot2 = new BlueLaser(getOwner(), x + 6, y, 0.0f,
                        direction * 0.2f);

                if (owner.getShipContainer().isNetworkGame()) {
                    // Create and send packet
                    shoot1.createPacket(
                            owner.getShipContainer().getNetworkManager());
                    shoot2.createPacket(
                            owner.getShipContainer().getNetworkManager());
                }

                getOwner().getShipContainer().addShot(shoot1);
                getOwner().getShipContainer().addShot(shoot2);

                SoundFactory.playSound("enemylaser.wav");
            } else if (weaponLevel >= 6) {
                // Fire two diagonal blue lasers and one up
                Bullet shoot1 = new BlueLaser(getOwner(), x - 6, y, 0.05f,
                        direction * 0.2f);

                Bullet shoot2 = new BlueLaser(getOwner(), x + 6, y, -0.05f,
                        direction * 0.2f);

                Bullet shoot3 = new BlueLaser(getOwner(), x, y, 0.0f,
                        direction * 0.2f);

                if (owner.getShipContainer().isNetworkGame()) {
                    // Create and send packet
                    shoot1.createPacket(
                            owner.getShipContainer().getNetworkManager());
                    shoot2.createPacket(
                            owner.getShipContainer().getNetworkManager());
                    shoot3.createPacket(
                            owner.getShipContainer().getNetworkManager());
                }

                getOwner().getShipContainer().addShot(shoot1);
                getOwner().getShipContainer().addShot(shoot2);
                getOwner().getShipContainer().addShot(shoot3);

                SoundFactory.playSound("enemylaser.wav");
            }

        }
    }    // end method fire

    /**
     * Add one to the weapon level.
     */
    @Override
    public void upgradeWeapon() {
        super.upgradeWeapon();
        updateFiringRate();

    }

    /**
     * Updates the firing rate of the weapon. Makes
     * the weapon faster as the level increases.
     */
    private void updateFiringRate() {
        if (weaponLevel > 1) {
            // Increase the firing rate
            firingRate = BASE_FIRING_RATE - weaponLevel * 15;
        }
    }

}

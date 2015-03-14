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

import game.ship.Ship;

/**
 * The <code>WeaponFactory</code> class is a factory for weapons.
 * A ship who want a weapon should ask for it from the WeaponFactory.
 * It also creates bullets from bullet model arrived from the newtork.
 */
public class WeaponFactory {

    /**
     * Type of weapons in the game
     */
    private static final int NUM_OF_PLAYER_WEAPONS = 2;
    public static final int TYPE_LASER_CANNON = 1;
    public static final int TYPE_FIRE_CANNON = 2;

    // Only enemy weapons
    public static final int TYPE_BALL_CANNON = 101;

    // Images for the weapon upgrades
    private static final String laserCannonImage = "laserUp.png";
    private static final String fireCannonImage = "fireUp.png";
    private static final String ballCannonImage = "ballUp.png";

    /**
     * Returns a random weapon type.
     */
    public static int getRandomWeaponType() {
        return 1 + (int) (Math.random() * NUM_OF_PLAYER_WEAPONS);
    }

    public static String getWeaponImageByType(int type) {
        switch (type) {
            case TYPE_LASER_CANNON:
                return laserCannonImage;
            case TYPE_FIRE_CANNON:
                return fireCannonImage;
            case TYPE_BALL_CANNON:
                return ballCannonImage;
            default:
                return "";
        }

    }

    /**
     * Returns a <code>Weapon</object>.
     *
     * @param weaponType  Type of the weapon
     * @param weaponLevel Weapon level
     * @param direction   Direction of the weapon
     * @return A weapon
     */
    public static Weapon getWeapon(int weaponType, int weaponLevel,
                                   int direction) {

        Weapon weapon = null;

        switch (weaponType) {
            case TYPE_LASER_CANNON:
                weapon = new LaserCannon(direction, weaponLevel);
                break;
            case TYPE_FIRE_CANNON:
                weapon = new FireCannon(direction, weaponLevel);
                break;
            case TYPE_BALL_CANNON:
                weapon = new BallCannon(direction, weaponLevel);
        }

        return weapon;

    }

    /**
     * This method is used to get a <code>Bullet</code> object from a
     * model sent via the network.
     * XXX: The method could be implemented with reflection but it's error
     * prone and hurts performance
     *
     * @param model Bullet model
     * @param owner Bullet's owning ship
     */
    public static Bullet getBullet(BulletModel model, Ship owner) {
        Bullet bullet = null;
        Class bulletClass = model.getBulletClass();
        if (bulletClass == LaserBeam.class) {
            bullet = new LaserBeam(
                    owner, model.getX(), model.getY(), model.getDx(), model.getDy());
        } else if (bulletClass == FireBullet.class) {
            bullet = new FireBullet(
                    owner, model.getX(), model.getY(), model.getDx(), model.getDy());
        } else if (bulletClass == BlueLaser.class) {
            bullet = new BlueLaser(
                    owner, model.getX(), model.getY(), model.getDx(), model.getDy());
        } else if (bulletClass == BallBullet.class) {
            bullet = new BallBullet(
                    owner, model.getX(), model.getY(), model.getDx(), model.getDy());
        } else if (bulletClass == BigBallBullet.class) {
            bullet = new BigBallBullet(
                    owner, model.getX(), model.getY(), model.getDx(), model.getDy());
        }

        return bullet;


        // Sample of doing the same with reflection
        // Assuming all the bullets implement constructor accepting BulletModel
        // and Ship as parameters and the BulletModel contains the bullet class
        /*
        try {
            Class [] params = new Class[]{BulletModel.class, Ship.class};
            Constructor cons = model.bulletClass.getConstructor(params);
            Object [] consParams = new Object[]{model, getNetworkPlayerShip()};
            Bullet bullet = (Bullet)cons.newInstance(consParams);
            addShoot(bullet);
    	}
        catch (Exception e) {
            e.printStackTrace();
        }
         */

    }

}

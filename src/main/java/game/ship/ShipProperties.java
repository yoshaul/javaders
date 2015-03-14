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

import game.GameConstants;
import game.ship.weapon.Weapon;
import game.ship.weapon.WeaponFactory;
import game.util.ResourceManager;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * The <code>ShipProperties</code> class holds the properties of all the
 * ships in the game.
 */
public class ShipProperties {

    // Various ship types
    public static final int SHIP_TYPE_1 = 1;
    public static final int SHIP_TYPE_2 = 2;
    public static final int SHIP_TYPE_3 = 3;
    public static final int SHIP_TYPE_4 = 4;

    public static final int ROBO_SHIP_TYPE_1 = 21;
    public static final int ROBO_SHIP_TYPE_2 = 22;
    public static final int ROBO_SHIP_TYPE_3 = 23;
    public static final int ROBO_SHIP_TYPE_4 = 24;
    public static final int ROBO_SHIP_TYPE_5 = 25;
    public static final int ROBO_SHIP_TYPE_6 = 26;
    public static final int ROBO_SHIP_TYPE_7 = 27;

    public static final int CARDROM_SHIP_TYPE_1 = 41;
    public static final int CARDROM_SHIP_TYPE_2 = 42;
    public static final int CARDROM_SHIP_TYPE_3 = 43;
    public static final int CARDROM_SHIP_TYPE_4 = 44;
    public static final int CARDROM_SHIP_TYPE_5 = 45;

    public static final int COLOR_SHIP_TYPE_1 = 61;
    public static final int COLOR_SHIP_TYPE_2 = 62;
    public static final int COLOR_SHIP_TYPE_3 = 63;
    public static final int COLOR_SHIP_TYPE_4 = 64;

    public static final int PLAYER_SHIP_TYPE_1 = 100;
    public static final int PLAYER_SHIP_TYPE_2 = 101;

    public static final int BOSS_SHIP_TYPE_1 = 201;
    public static final int BOSS_SHIP_TYPE_2 = 202;
    public static final int BOSS_SHIP_TYPE_3 = 203;
    public static final int BOSS_SHIP_TYPE_4 = 204;
    public static final int BOSS_SHIP_TYPE_5 = 205;

    private final static Map<Integer, ShipProperties> shipsProperties = initShipsProperties();

    private final float maxDX;
    private final float maxDY;
    private final long armor;
    private final long damage;
    private final long hitScoreValue;
    private final long destroyScoreValue;
    private final Image image;
    private final int weaponType;
    private final int weaponLevel;
    private final int weaponDirection;

    /**
     * Initialize the <code>ShipProperties</code> for the various ships.
     *
     * @return Map whit the ship type as key and ShipProperties as value.
     */
    private static Map<Integer, ShipProperties> initShipsProperties() {

        Map<Integer, ShipProperties> shipsProperties = new HashMap<>();

        final ShipProperties playerShipType1 = new ShipProperties(0.25f, 0.25f,
                20, 1, 10, 500, "player1.png", WeaponFactory.TYPE_FIRE_CANNON,
                1, Weapon.DIRECTION_UP);

        shipsProperties.put(PLAYER_SHIP_TYPE_1, playerShipType1);

        final ShipProperties playerShipType2 = new ShipProperties(0.25f, 0.25f,
                20, 1, 10, 500, "player2.png", WeaponFactory.TYPE_FIRE_CANNON,
                1, Weapon.DIRECTION_UP);

        shipsProperties.put(PLAYER_SHIP_TYPE_2, playerShipType2);

        final ShipProperties shipType1 = new ShipProperties(0.07f, 0.07f,
                20, 1, 10, 500, "ZamsAirspeeder.png",
                WeaponFactory.TYPE_LASER_CANNON, 1, Weapon.DIRECTION_DOWN);

        shipsProperties.put(SHIP_TYPE_1, shipType1);

        final ShipProperties shipType2 = new ShipProperties(0.09f, 0.09f,
                30, 1, 15, 1000, "RepublicGunship.png",
                WeaponFactory.TYPE_LASER_CANNON, 1, Weapon.DIRECTION_DOWN);

        shipsProperties.put(SHIP_TYPE_2, shipType2);

        final ShipProperties shipType3 = new ShipProperties(0.11f, 0.11f,
                40, 2, 20, 2500, "RepublicAssaultShip.png",
                WeaponFactory.TYPE_LASER_CANNON, 1, Weapon.DIRECTION_DOWN);

        shipsProperties.put(SHIP_TYPE_3, shipType3);

        final ShipProperties shipType4 = new ShipProperties(0.12f, 0.12f,
                50, 2, 30, 4500, "bajoran2.png",
                WeaponFactory.TYPE_LASER_CANNON, 2, Weapon.DIRECTION_DOWN);

        shipsProperties.put(SHIP_TYPE_4, shipType4);

        final ShipProperties bossShipType1 = new ShipProperties(0.07f, 0.07f,
                500, 2, 15, 50000, "boss1.png",
                WeaponFactory.TYPE_BALL_CANNON, 2, Weapon.DIRECTION_DOWN);

        shipsProperties.put(BOSS_SHIP_TYPE_1, bossShipType1);

        final ShipProperties roboShipType1 = new ShipProperties(0.05f, 0.05f,
                25, 2, 12, 550, "robo1.png",
                WeaponFactory.TYPE_LASER_CANNON, 1, Weapon.DIRECTION_DOWN);

        shipsProperties.put(ROBO_SHIP_TYPE_1, roboShipType1);

        final ShipProperties roboShipType2 = new ShipProperties(0.05f, 0.05f,
                35, 2, 15, 550, "robo2.png",
                WeaponFactory.TYPE_LASER_CANNON, 1, Weapon.DIRECTION_DOWN);

        shipsProperties.put(ROBO_SHIP_TYPE_2, roboShipType2);

        final ShipProperties roboShipType3 = new ShipProperties(0.11f, 0.11f,
                20, 1, 30, 800, "robo3.png",
                WeaponFactory.TYPE_LASER_CANNON, 2, Weapon.DIRECTION_DOWN);

        shipsProperties.put(ROBO_SHIP_TYPE_3, roboShipType3);

        final ShipProperties roboShipType4 = new ShipProperties(0.07f, 0.07f,
                65, 2, 20, 1000, "robo4.png",
                WeaponFactory.TYPE_LASER_CANNON, 2, Weapon.DIRECTION_DOWN);

        shipsProperties.put(ROBO_SHIP_TYPE_4, roboShipType4);

        final ShipProperties roboShipType5 = new ShipProperties(0.07f, 0.07f,
                75, 2, 15, 1500, "robo5.png",
                WeaponFactory.TYPE_LASER_CANNON, 3, Weapon.DIRECTION_DOWN);

        shipsProperties.put(ROBO_SHIP_TYPE_5, roboShipType5);

        final ShipProperties roboShipType6 = new ShipProperties(0.08f, 0.08f,
                110, 3, 11, 1500, "robo6.png",
                WeaponFactory.TYPE_LASER_CANNON, 2, Weapon.DIRECTION_DOWN);

        shipsProperties.put(ROBO_SHIP_TYPE_6, roboShipType6);

        final ShipProperties roboShipType7 = new ShipProperties(0.06f, 0.06f,
                150, 3, 20, 2500, "robo7.png",
                WeaponFactory.TYPE_LASER_CANNON, 4, Weapon.DIRECTION_DOWN);

        shipsProperties.put(ROBO_SHIP_TYPE_7, roboShipType7);

        final ShipProperties bossShipType2 = new ShipProperties(0.08f, 0.08f,
                750, 3, 15, 60000, "boss2.png",
                WeaponFactory.TYPE_BALL_CANNON, 4, Weapon.DIRECTION_DOWN);

        shipsProperties.put(BOSS_SHIP_TYPE_2, bossShipType2);

        final ShipProperties bossShipType3 = new ShipProperties(0.09f, 0.09f,
                1550, 3, 20, 75000, "boss3.png",
                WeaponFactory.TYPE_BALL_CANNON, 5, Weapon.DIRECTION_DOWN);

        shipsProperties.put(BOSS_SHIP_TYPE_3, bossShipType3);

        final ShipProperties cardromShipType1 = new ShipProperties(0.1f, 0.1f,
                90, 2, 15, 1000, "cardrom1.png",
                WeaponFactory.TYPE_LASER_CANNON, 4, Weapon.DIRECTION_DOWN);

        shipsProperties.put(CARDROM_SHIP_TYPE_1, cardromShipType1);

        final ShipProperties cardromShipType2 = new ShipProperties(0.08f, 0.08f,
                100, 2, 20, 1200, "cardrom2.png",
                WeaponFactory.TYPE_LASER_CANNON, 5, Weapon.DIRECTION_DOWN);

        shipsProperties.put(CARDROM_SHIP_TYPE_2, cardromShipType2);

        final ShipProperties cardromShipType3 = new ShipProperties(0.09f, 0.09f,
                120, 2, 20, 1500, "cardrom3.png",
                WeaponFactory.TYPE_LASER_CANNON, 5, Weapon.DIRECTION_DOWN);

        shipsProperties.put(CARDROM_SHIP_TYPE_3, cardromShipType3);

        final ShipProperties cardromShipType4 = new ShipProperties(0.1f, 0.1f,
                200, 2, 15, 2000, "cardrom4.png",
                WeaponFactory.TYPE_LASER_CANNON, 7, Weapon.DIRECTION_DOWN);

        shipsProperties.put(CARDROM_SHIP_TYPE_4, cardromShipType4);

        final ShipProperties cardromShipType5 = new ShipProperties(0.16f, 0.16f,
                60, 2, 25, 1500, "cardrom5.png",
                WeaponFactory.TYPE_LASER_CANNON, 5, Weapon.DIRECTION_DOWN);

        shipsProperties.put(CARDROM_SHIP_TYPE_5, cardromShipType5);

        final ShipProperties bossShipType4 = new ShipProperties(0.15f, 0.15f,
                2500, 2, 10, 100000, "boss4.png",
                WeaponFactory.TYPE_BALL_CANNON, 7, Weapon.DIRECTION_DOWN);

        shipsProperties.put(BOSS_SHIP_TYPE_4, bossShipType4);

        final ShipProperties colorShipType1 = new ShipProperties(0.17f, 0.17f,
                120, 1, 15, 2500, "color1.png",
                WeaponFactory.TYPE_LASER_CANNON, 5, Weapon.DIRECTION_DOWN);

        shipsProperties.put(COLOR_SHIP_TYPE_1, colorShipType1);

        final ShipProperties colorShipType2 = new ShipProperties(0.1f, 0.1f,
                220, 2, 20, 3500, "color2.png",
                WeaponFactory.TYPE_LASER_CANNON, 6, Weapon.DIRECTION_DOWN);

        shipsProperties.put(COLOR_SHIP_TYPE_2, colorShipType2);

        final ShipProperties colorShipType3 = new ShipProperties(0.1f, 0.1f,
                250, 2, 20, 4400, "color3.png",
                WeaponFactory.TYPE_LASER_CANNON, 7, Weapon.DIRECTION_DOWN);

        shipsProperties.put(COLOR_SHIP_TYPE_3, colorShipType3);

        final ShipProperties colorShipType4 = new ShipProperties(0.2f, 0.2f,
                530, 2, 20, 10000, "color4.png",
                WeaponFactory.TYPE_LASER_CANNON, 8, Weapon.DIRECTION_DOWN);

        shipsProperties.put(COLOR_SHIP_TYPE_4, colorShipType4);

        final ShipProperties bossShipType5 = new ShipProperties(0.16f, 0.16f,
                5000, 2, 10, 200000, "boss5.png",
                WeaponFactory.TYPE_BALL_CANNON, 10, Weapon.DIRECTION_DOWN);

        shipsProperties.put(BOSS_SHIP_TYPE_5, bossShipType5);

        return shipsProperties;
    }

    /**
     * Private constructor for the various ship properties.
     *
     * @param maxDX             Max vertical velocity of the ship (pixels/sec)
     * @param maxDY             Max horizontal velocity of the ship (pixels/sec)
     * @param armor             Armor of the ship (damage it can take)
     * @param damage            Damage the ship cause when it collide with objects
     * @param hitScoreValue     Score the ship gives per 1 damage unit
     * @param destroyScoreValue Score the ship gives when destroyed
     * @param imageName         Ship image name
     * @param weaponType        Predefined weapon type
     * @param weaponLevel       Level of the weapon
     * @param weaponDirection   Direction of the weapon
     */
    private ShipProperties(float maxDX, float maxDY, long armor, long damage, long hitScoreValue,
                           long destroyScoreValue, String imageName, int weaponType,
                           int weaponLevel, int weaponDirection) {
        this.maxDX = maxDX;
        this.maxDY = maxDY;
        this.armor = armor;
        this.damage = damage;
        this.hitScoreValue = hitScoreValue;
        this.destroyScoreValue = destroyScoreValue;
        this.image = ResourceManager.loadImage(
                GameConstants.IMAGES_DIR + imageName);
        this.weaponType = weaponType;
        this.weaponLevel = weaponLevel;
        this.weaponDirection = weaponDirection;

    }

    /**
     * Returns the <code>ShipProperties</code> object for the
     * specified type.
     *
     * @param shipType The ship type.
     */
    public static ShipProperties getShipProperties(int shipType) {
        return shipsProperties.get(shipType);
    }

    public float getMaxDX() {
        return maxDX;
    }

    public float getMaxDY() {
        return maxDY;
    }

    public long getArmor() {
        return armor;
    }

    public long getDamage() {
        return damage;
    }

    public long getHitScoreValue() {
        return hitScoreValue;
    }

    public long getDestroyScoreValue() {
        return destroyScoreValue;
    }

    public Image getImage() {
        return image;
    }

    public int getWeaponType() {
        return weaponType;
    }

    public int getWeaponLevel() {
        return weaponLevel;
    }

    public int getWeaponDirection() {
        return weaponDirection;
    }
}

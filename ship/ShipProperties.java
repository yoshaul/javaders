package game.ship;

import game.GameConstants;
import game.ship.weapon.Weapon;
import game.ship.weapon.WeaponFactory;
import game.util.ResourceManager;

import java.awt.Image;
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
    public static final int SHIP_TYPE_5 = 5;
    public static final int SHIP_TYPE_6 = 6;
    public static final int SHIP_TYPE_7 = 7;
    public static final int SHIP_TYPE_8 = 8;
    
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
    public static final int CARDROM_SHIP_TYPE_6 = 46;
    
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
    
    public static Map shipsProperties = initShipsProperties();

    public float maxDX, maxDY;
    public long armor;
    public long damage;
    public long hitScoreValue;
    public long destroyScoreValue;
    public Image image;
    public String imageName;
    public int weaponType;
    public int weaponLevel;
    public int weaponDirection;

    /**
     * Initialize the <code>ShipProperties</code> for the various ships.
     * @return	Map whit the ship type as key and ShipProperties as value.
     */
    private static Map initShipsProperties() {
        
        Map shipsProperties = new HashMap();
        
        final ShipProperties PlayerShipType1 = new ShipProperties(0.25f, 0.25f,
                20, 1, 10, 500, "player1.png", WeaponFactory.TYPE_FIRE_CANNON, 
                1, Weapon.DIRECTION_UP);

        shipsProperties.put(new Integer(PLAYER_SHIP_TYPE_1), PlayerShipType1);
        
        final ShipProperties PlayerShipType2 = new ShipProperties(0.25f, 0.25f,
                20, 1, 10, 500, "player2.png", WeaponFactory.TYPE_FIRE_CANNON, 
                1, Weapon.DIRECTION_UP);
        
        shipsProperties.put(new Integer(PLAYER_SHIP_TYPE_2), PlayerShipType2);
        
        final ShipProperties ShipType1 = new ShipProperties(0.07f, 0.07f,
                20, 1, 10, 500, "ZamsAirspeeder.png", 1, 1, Weapon.DIRECTION_DOWN);    
        
        shipsProperties.put(new Integer(SHIP_TYPE_1), ShipType1);
        
        final ShipProperties ShipType2 = new ShipProperties(0.09f, 0.09f,
                30, 1, 15, 1000, "RepublicGunship.png", 1, 1, Weapon.DIRECTION_DOWN);
        
        shipsProperties.put(new Integer(SHIP_TYPE_2), ShipType2);
        
        final ShipProperties ShipType3 = new ShipProperties(0.11f, 0.11f,
                40, 2, 20, 2500, "RepublicAssaultShip.png", 1, 1, Weapon.DIRECTION_DOWN);
        
        shipsProperties.put(new Integer(SHIP_TYPE_3), ShipType3);
        
        final ShipProperties ShipType4 = new ShipProperties(0.12f, 0.12f,
                50, 2, 30, 4500, "bajoran2.png", 2, 1, Weapon.DIRECTION_DOWN);
        
        shipsProperties.put(new Integer(SHIP_TYPE_4), ShipType4);
        
        final ShipProperties BossShipType1 = new ShipProperties(0.07f, 0.07f,
                500, 2, 15, 50000, "boss1.png", 2, 2, Weapon.DIRECTION_DOWN);        
        
        shipsProperties.put(new Integer(BOSS_SHIP_TYPE_1), BossShipType1);
        
        final ShipProperties RoboShipType1 = new ShipProperties(0.05f, 0.05f,
                25, 2, 12, 550, "robo1.png", 1, 1, Weapon.DIRECTION_DOWN);        
        
        shipsProperties.put(new Integer(ROBO_SHIP_TYPE_1), RoboShipType1);
        
        final ShipProperties RoboShipType2 = new ShipProperties(0.05f, 0.05f,
                35, 2, 15, 550, "robo2.png", 1, 1, Weapon.DIRECTION_DOWN);        
        
        shipsProperties.put(new Integer(ROBO_SHIP_TYPE_2), RoboShipType2);
        
        final ShipProperties RoboShipType3 = new ShipProperties(0.11f, 0.11f,
                20, 1, 30, 800, "robo3.png", 1, 2, Weapon.DIRECTION_DOWN);        
        
        shipsProperties.put(new Integer(ROBO_SHIP_TYPE_3), RoboShipType3);
        
        final ShipProperties RoboShipType4 = new ShipProperties(0.07f, 0.07f,
                65, 2, 20, 1000, "robo4.png", 1, 2, Weapon.DIRECTION_DOWN);        
        
        shipsProperties.put(new Integer(ROBO_SHIP_TYPE_4), RoboShipType4);
        
        final ShipProperties RoboShipType5 = new ShipProperties(0.07f, 0.07f,
                75, 2, 15, 1500, "robo5.png", 1, 3, Weapon.DIRECTION_DOWN);        
        
        shipsProperties.put(new Integer(ROBO_SHIP_TYPE_5), RoboShipType5);
        
        final ShipProperties RoboShipType6 = new ShipProperties(0.08f, 0.08f,
                110, 3, 11, 1500, "robo6.png", 1, 2, Weapon.DIRECTION_DOWN);        
        
        shipsProperties.put(new Integer(ROBO_SHIP_TYPE_6), RoboShipType6);
        
        final ShipProperties RoboShipType7 = new ShipProperties(0.06f, 0.06f,
                150, 3, 20, 2500, "robo7.png", 1, 4, Weapon.DIRECTION_DOWN);        
        
        shipsProperties.put(new Integer(ROBO_SHIP_TYPE_7), RoboShipType7);
        
        final ShipProperties BossShipType2 = new ShipProperties(0.08f, 0.08f,
                750, 3, 15, 60000, "boss2.png", 1, 4, Weapon.DIRECTION_DOWN);        
        
        shipsProperties.put(new Integer(BOSS_SHIP_TYPE_2), BossShipType2);
        
        final ShipProperties BossShipType3 = new ShipProperties(0.09f, 0.09f,
                1550, 3, 20, 75000, "boss3.png", 1, 5, Weapon.DIRECTION_DOWN);        
        
        shipsProperties.put(new Integer(BOSS_SHIP_TYPE_3), BossShipType3);
        
        final ShipProperties CardromShipType1 = new ShipProperties(0.1f, 0.1f,
                90, 2, 15, 1000, "cardrom1.png", 1, 4, Weapon.DIRECTION_DOWN);
        
        shipsProperties.put(new Integer(CARDROM_SHIP_TYPE_1), CardromShipType1);
        
        final ShipProperties CardromShipType2 = new ShipProperties(0.08f, 0.08f,
                100, 2, 20, 1200, "cardrom2.png", 1, 5, Weapon.DIRECTION_DOWN);
        
        shipsProperties.put(new Integer(CARDROM_SHIP_TYPE_2), CardromShipType2);
        
        final ShipProperties CardromShipType3 = new ShipProperties(0.09f, 0.09f,
                120, 2, 20, 1500, "cardrom3.png", 1, 5, Weapon.DIRECTION_DOWN);
        
        shipsProperties.put(new Integer(CARDROM_SHIP_TYPE_3), CardromShipType3);
        
        final ShipProperties CardromShipType4 = new ShipProperties(0.1f, 0.1f,
                200, 2, 15, 2000, "cardrom4.png", 1, 7, Weapon.DIRECTION_DOWN);
        
        shipsProperties.put(new Integer(CARDROM_SHIP_TYPE_4), CardromShipType4);
        
        final ShipProperties CardromShipType5 = new ShipProperties(0.16f, 0.16f,
                60, 2, 25, 1500, "cardrom5.png", 1, 5, Weapon.DIRECTION_DOWN);
        
        shipsProperties.put(new Integer(CARDROM_SHIP_TYPE_5), CardromShipType5);
        
        final ShipProperties BossShipType4 = new ShipProperties(0.15f, 0.15f,
                2500, 2, 10, 100000, "boss4.png", 1, 7, Weapon.DIRECTION_DOWN);        
        
        shipsProperties.put(new Integer(BOSS_SHIP_TYPE_4), BossShipType4);
        
        final ShipProperties ColorShipType1 = new ShipProperties(0.17f, 0.17f,
                120, 1, 15, 2500, "color1.png", 1, 5, Weapon.DIRECTION_DOWN);
        
        shipsProperties.put(new Integer(COLOR_SHIP_TYPE_1), ColorShipType1); 
        
        final ShipProperties ColorShipType2 = new ShipProperties(0.1f, 0.1f,
                220, 2, 20, 3500, "color2.png", 1, 6, Weapon.DIRECTION_DOWN);
        
        shipsProperties.put(new Integer(COLOR_SHIP_TYPE_2), ColorShipType2); 

        final ShipProperties ColorShipType3 = new ShipProperties(0.1f, 0.1f,
                250, 2, 20, 4400, "color3.png", 1, 7, Weapon.DIRECTION_DOWN);
        
        shipsProperties.put(new Integer(COLOR_SHIP_TYPE_3), ColorShipType3); 

        final ShipProperties ColorShipType4 = new ShipProperties(0.2f, 0.2f,
                530, 2, 20, 10000, "color4.png", 1, 8, Weapon.DIRECTION_DOWN);
        
        shipsProperties.put(new Integer(COLOR_SHIP_TYPE_4), ColorShipType4); 
        
        final ShipProperties BossShipType5 = new ShipProperties(0.16f, 0.16f,
                5000, 2, 10, 200000, "boss5.png", 1, 10, Weapon.DIRECTION_DOWN);        
        
        shipsProperties.put(new Integer(BOSS_SHIP_TYPE_5), BossShipType5);
        
        return shipsProperties;
        
    }
    
    /**
     * Private constructor for the various ship properties.
     * @param maxDX		Max vertical velocity of the ship (pixels/sec)
     * @param maxDY		Max horizontal velocity of the ship (pixels/sec)
     * @param armor		Armor of the ship (damage it can take)
     * @param damage	Damage the ship cause when it collide with objects
     * @param hitScoreValue		Score the ship gives per 1 damage unit
     * @param destroyScoreValue Score the ship gives when destroyed
     * @param imageName		Ship image name
     * @param weaponType	Predefined weapon type
     * @param weaponLevel	Level of the weapon
     * @param weaponDirection Direction of the weapon
     */
    private ShipProperties(float maxDX, float maxDY, long armor, long damage, 
            long hitScoreValue, long destroyScoreValue, String imageName, 
            int weaponType, int weaponLevel, int weaponDirection) {
        
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
     * @param shipType	The ship type.
     */
    public static ShipProperties getShipProperties(int shipType) {
        
        return (ShipProperties) shipsProperties.get(new Integer(shipType));
        
    }
    
}

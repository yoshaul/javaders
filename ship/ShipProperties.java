package game.ship;

import game.GameConstants;

import java.awt.Image;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;

public class ShipProperties {

    public static final int SHIP_TYPE_1 = 1;
    public static final int SHIP_TYPE_2 = 2;
    public static final int SHIP_TYPE_3 = 3;
    public static final int SHIP_TYPE_4 = 4;
    public static final int SHIP_TYPE_5 = 5;
    public static final int SHIP_TYPE_6 = 6;
    public static final int SHIP_TYPE_7 = 7;
    public static final int SHIP_TYPE_8 = 8;
    public static Map shipsProperties = initShipsProperties();

    public long armor;
    public long damage;
    public Image image;
    public String imageName;
    public int weaponType;
    public Weapon weapon;

    
    
    private static Map initShipsProperties() {
        
        
        final ShipProperties ShipType1 = 
            new ShipProperties(20, 1, "ship40px.png", 1);    
        final ShipProperties ShipType2 = 
            new ShipProperties(30, 1, "ship240px.png", 1);
        final ShipProperties ShipType3 = 
            new ShipProperties(40, 2, "Bajoran.gif", 1);
        final ShipProperties ShipType4 = 
            new ShipProperties(50, 2, "ship40px.png", 2);
        
        Map shipsProperties = new HashMap();
        
        shipsProperties.put(new Integer(SHIP_TYPE_1), ShipType1);
        shipsProperties.put(new Integer(SHIP_TYPE_2), ShipType2);
        shipsProperties.put(new Integer(SHIP_TYPE_3), ShipType3);
        shipsProperties.put(new Integer(SHIP_TYPE_4), ShipType4);
        
        
        return shipsProperties;
        
        
    }
    
    private ShipProperties(long armor, long damage, 
            String imageName, int weaponType) {
        
        this.armor = armor;
        this.damage = damage;
        this.image = new ImageIcon(GameConstants.IMAGES_DIR + imageName).getImage();
        this.weaponType = weaponType;
        this.weapon = new LaserCanon(Weapon.DIRECTION_DOWN);
        
    }
    
    public static ShipProperties getShipProperties(int shipType) {
        
        return (ShipProperties) shipsProperties.get(new Integer(shipType));
        
    }
    
}

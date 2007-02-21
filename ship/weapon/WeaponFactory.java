package game.ship.weapon;

public class WeaponFactory {

    public static final int TYPE_LASER_CANNON = 1;
    public static final int TYPE_FIRE_CANNON = 2;
    
    public static Weapon getWeapon(int weaponType, int direction) {
        return getWeapon(weaponType, 1, direction);
    }
    
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
        }
        
        return weapon;
        
    }
    
    
}

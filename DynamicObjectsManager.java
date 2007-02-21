//
//package game;
//
//import game.ship.*;
//
//import java.awt.Graphics;
//import java.util.*;
//
//import javax.swing.ImageIcon;
//
//public class DynamicObjectsManager implements Renderable {
//    
//    private List dynamicObjects;
//    private Sprite player;
//    
//    
//    public DynamicObjectsManager(Sprite player) {
//        
//        dynamicObjects = new ArrayList();
//        
//        for (int i = 0; i < 10; i++) {
//            dynamicObjects.add(new EnemyShip(                    
//                    Math.random()*800, Math.random()*500,
//                    (Math.random()-0.5)/5, (Math.random()-0.5)/5, 
//                    new ImageIcon("images/ship40px.png").getImage(), null));
//        }
//
//        this.player = player;
//        dynamicObjects.add(player);
//        
//    }
//    
//    public void updateObjects(long elapsedTime){
//        
//        Iterator itr = dynamicObjects.iterator();
//        while (itr.hasNext()) {
//            Ship ship = (Ship) itr.next();
//            ship.update(elapsedTime);
//            
//            if (ship.getX() < 0) {
//                ship.setDx(-ship.getDx());
//            }
//            if (ship.getX()+ ship.getSpriteImage().getWidth(null) > 800) {
//                ship.setDx(-ship.getDx());
//            }
//            if (ship.getY() < 0) {
//                ship.setDy(-ship.getDy());
//            }
//            if (ship.getY() + ship.getSpriteImage().getHeight(null) > 600) {
//                ship.setDy(-ship.getDy());
//            }
//        }
//
//    }
//    
//    public void render(Graphics g) {
//        
//        Iterator itr = dynamicObjects.iterator();
//        while (itr.hasNext()) {
//            Sprite ship = (Sprite) itr.next();
//            ship.render(g);
//        }
//        
//    }
//    
//    public Sprite getPlayer() {
//        return player;
//    }
//
//}

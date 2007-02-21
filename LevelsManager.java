
package game;

import java.io.File;
import java.io.IOException;
import java.util.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import game.ship.*;

//import org.xml.sax.*;

public class LevelsManager implements ErrorHandler {

    private Document xmlDocument;
    private int currentLevel = 0;
    private boolean levelFinished = true;
    
    private Sprite player;
    
    public LevelsManager() {
        loadXMLFile();
    }
    
    public Collection loadNextLevel() {
        currentLevel++;
        return loadLevel(currentLevel);
    }
    
    public Collection loadLevel(int levelNumber) {
System.out.println("loadLevel num = " + levelNumber);        
        levelFinished = false;
        Collection enemyShips = new ArrayList();
        Element level = null;
        boolean levelFound = false;
        // Get all the level nodes 
        NodeList levels = xmlDocument.getElementsByTagName("level");
        
        // Find the level node with id equals to levelNumber
        for(int i = 0; i < levels.getLength() && !levelFound; i++) {
            
            level = (Element) levels.item(i);
            // Get the attributes list
            NamedNodeMap attributes = level.getAttributes();
            // Get the levelNum attribute
            Node levelNum = attributes.getNamedItem("levelNum"); 
            if (levelNum.getNodeValue().equals(String.valueOf(levelNumber))) {
                levelFound = true;
            }
            
        }

        if (levelFound) {
	        // Get and create the ship types and ammount for the level
	        NodeList ships = level.getElementsByTagName("enemyShips");
	        int length = ships.getLength();
	        for (int i = 0; i < ships.getLength(); i++) {
	            
	            Element enemyShipsNode = (Element) ships.item(i);
	            Node shipTypeNode = 
	                enemyShipsNode.getElementsByTagName("shipType").item(0);
	            Node numOfShipsNode = 
	                enemyShipsNode.getElementsByTagName("numberOfShips").item(0);
	            
	            String typeStr = shipTypeNode.getFirstChild().getNodeValue();            
	            String numShipsStr = numOfShipsNode.getFirstChild().getNodeValue();
	            
	            int shipType = Integer.parseInt(typeStr);
	            int numOfShips = Integer.parseInt(numShipsStr);
	            
	            // Create the ship objects
	            for (int j = 0; j < numOfShips; j++) {
	              enemyShips.add(new EnemyShip(                    
	                      Math.random()*800, Math.random()*500,
	                      (Math.random()-0.5)/5, (Math.random()-0.5)/5, 
	                      ShipProperties.getShipProperties(shipType)));
	            }
	              
	        }
	        
        } else {
            
            // No more levels. Show game completed screen
            
            
        }

        return enemyShips;
        
    }
    
    public boolean isLevelFinished() {
        return levelFinished;
    }
    
    private void loadXMLFile() {
        DocumentBuilderFactory factory =
            DocumentBuilderFactory.newInstance();
        factory.setValidating(true);   
//        factory.setNamespaceAware(true);
        try {
           DocumentBuilder builder = factory.newDocumentBuilder();
           builder.setErrorHandler(this);
           this.xmlDocument = builder.parse(
                   new File(GameConstants.CONFIG_DIR+"levels.xml"));
 
           Element root = xmlDocument.getDocumentElement();
           
//           System.out.println("Root node name = " + root.getNodeName());
           Node node = null;
//           NodeList childNodes = root.getChildNodes();
           NodeList childNodes = root.getElementsByTagName("level");
//           System.out.println("Child size = " + childNodes.getLength());
           for (int i = 0; i < childNodes.getLength(); i++) {
               
               node = childNodes.item(i);
               
//               System.out.println("child " + i + " name: " + node.getNodeName() +
//                       " type: " + node.getNodeType() + " value: " + node.getNodeValue());

               NamedNodeMap attrList = node.getAttributes();
               
               if (attrList != null) {
//	               System.out.println("Attributes size = " + attrList.getLength());
	               
	               for (int j = 0; j < attrList.getLength(); j++) {
	                   node = attrList.item(j);
	                   
//	                   System.out.println("Attr name: " + node.getNodeName() + " " +
//	                   		"type: " + node.getNodeType() +
//	                   		" value: " + node.getNodeValue());
	               }
               }
           }
           
           
        } catch (SAXException sxe) {
           // Error generated during parsing
           Exception  x = sxe;
           if (sxe.getException() != null)
               x = sxe.getException();
           x.printStackTrace();

        } catch (ParserConfigurationException pce) {
            // Parser with specified options can't be built
            pce.printStackTrace();

        } catch (IOException ioe) {
           // I/O error
           ioe.printStackTrace();
        }
    }
    
    public void warning (SAXParseException saxpe) {
        System.err.println("SAXParseException warning");
        saxpe.printStackTrace();
    }
    
    public void error (SAXParseException saxpe) {
        System.err.println("SAXParseException error");
        saxpe.printStackTrace();
    }

    public void fatalError (SAXParseException saxpe) {
        System.err.println("SAXParseException fatalError");
        saxpe.printStackTrace();
    }

    
    
}

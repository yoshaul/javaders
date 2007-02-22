
package game;

import java.awt.Dimension;
import java.util.*;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.*;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import game.ship.*;
import game.util.Logger;
import game.util.ResourceManager;

/**
 * The <code>LevelsManager</code> class is used to load the levels
 * from an XML file.
 */
public class LevelsManager {

    private GameLoop gameLoop;

    private Document xmlDocument;
    private int currentLevel = 0;
    private int lastLevel;
//    private boolean levelFinished = true;

    /**
     * Construct a LevelManager and load the levels xml file.
     * @param gameLoop	Reference to the game loop
     */
    public LevelsManager(GameLoop gameLoop) {
        this.gameLoop = gameLoop;
        loadXMLFile();
    }

    /**
     * Returns the current level in the game.
     * @return The current level in the game.
     */
    public int getCurrentLevel() {
        return this.currentLevel;
    }

    /**
     * Returns true if the current level is the last one.
     * @return	True if the current level is the last level.
     */
    public boolean isLastLevel() {
        return lastLevel == currentLevel;
    }

    /**
     * This method is called to inform the level manager
     * to update to current level. It is called whenever
     * a packet with new level information arrives in a
     * network game.
     */
    public void nextLevel() {
        currentLevel++;
    }

    /**
     * Loads the next level from the xml file and returns the enemy
     * ships map.
     * @return	Map with the enemy ships for the level.
     */
    public Map loadNextLevel() {
        currentLevel++;
        return loadLevel(currentLevel);
    }

    /**
     * Loads the level number <code>levelNumber</code> from the
     * levels xml file.
     * @param levelNumber	Number of the level to load.
     * @return	Map of enemy ships
     */
    public Map loadLevel(int levelNumber) {

        int curObjectID = GameConstants.FIRST_ENEMY_SHIP_ID;
        Map enemyShips = new HashMap();

        Element level = getLevelElement(levelNumber);

        if (level == null) {
            throw new RuntimeException("Error loading the level" + levelNumber
                    + "from file");
        }

        // Load the background image of the current level
        loadLevelBGImage(level);

        Dimension screenDimention =
            gameLoop.getScreenManager().getScreenDimension();

        // Get and create the ship types and ammount for the level
        NodeList ships = level.getElementsByTagName("enemyShips");
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
            for (int j = 0; j < numOfShips; j++, curObjectID++) {
                enemyShips.put(new Integer(curObjectID),
                  new EnemyShip(curObjectID, shipType,
                          (float)(50+Math.random()*(screenDimention.width-50)),
                          (float)(50+Math.random()*screenDimention.height/2),
                      ShipProperties.getShipProperties(shipType)));
            }

        }

        return enemyShips;

    }	// end method loadLevel

    /**
     * Finds and returns the requested level node from the xml file.
     * @param levelNumber	Level to load.
     * @return	Element with the level details. Null if not found.
     */
    public Element getLevelElement(int levelNumber) {
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

        return level;
    }

    /**
     * Search for the backgroung image in the level element.
     * If exists, set the game backgroung image.
     * @param level	Elment with the level info
     */
    public void loadLevelBGImage(Element level) {
        Node bgImage = level.getElementsByTagName("backgroundImage").item(0);
        if (bgImage != null) {
            String bgImageName = bgImage.getFirstChild().getNodeValue();
            gameLoop.getStaticObjectsManager().
            	setBackgroundImage(bgImageName);
        }
    }

    /**
     * Loads data needed for the local not controller machine in a
     * network game.
     * @param levelNumber	Number of the level
     */
    public void loadLocalLevelData(int levelNumber) {
        Element level = getLevelElement(levelNumber);
        if (level != null) {
            loadLevelBGImage(level);
        }
    }


    /**
     * Loads and parses the levels XML file to the memory. If any error
     * occurs exit with the exeption.
     * Also checks what is the last level in the game.
     */
    private void loadXMLFile() {
        try {
            DocumentBuilderFactory factory =
                DocumentBuilderFactory.newInstance();
            factory.setValidating(false);
            DocumentBuilder builder = factory.newDocumentBuilder();
            // we must special entity resolver to find the dtd inside the jar
            builder.setEntityResolver(new LevelsEntityResolver());
            this.xmlDocument = builder.parse(
            		ResourceManager.getResourceAsStream(
            				GameConstants.CONFIG_DIR + "/levels.xml"));

            Element root = xmlDocument.getDocumentElement();

            Node lastLevelNode = root.getElementsByTagName("lastLevel").item(0);
            lastLevel = Integer.parseInt(
            		lastLevelNode.getAttributes().getNamedItem("levelNum").getNodeValue());
        }
        catch (Exception e) {
            // If any exception occures during the parsing exit the game
            Logger.exception(e);
            System.exit(-1);
        }
    }

    private class LevelsEntityResolver implements EntityResolver {

        public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
            if (systemId.endsWith("levels.dtd")) {
                return new InputSource(ResourceManager.getResourceAsStream(
                                GameConstants.CONFIG_DIR + "/levels.dtd"));
            }

            throw new IllegalArgumentException("Unknown systemId " + systemId);
        }
    }

}

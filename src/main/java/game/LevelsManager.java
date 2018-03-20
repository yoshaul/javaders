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

package game;

import game.ship.EnemyShip;
import game.ship.Ship;
import game.ship.ShipProperties;
import game.util.Logger;
import game.util.ResourceManager;
import org.w3c.dom.*;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

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
     *
     * @param gameLoop Reference to the game loop
     */
    public LevelsManager(GameLoop gameLoop) {
        this.gameLoop = gameLoop;
        loadXMLFile();
    }

    /**
     * Returns the current level in the game.
     *
     * @return The current level in the game.
     */
    public int getCurrentLevel() {
        return this.currentLevel;
    }

    /**
     * Returns true if the current level is the last one.
     *
     * @return True if the current level is the last level.
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
     *
     * @return Map with the enemy ships for the level.
     */
    public Map<Integer, Ship> loadNextLevel() {
        currentLevel++;
        return loadLevel(currentLevel);
    }

    /**
     * Loads the level number <code>levelNumber</code> from the
     * levels xml file.
     *
     * @param levelNumber Number of the level to load.
     * @return Map of enemy ships
     */
    private Map<Integer, Ship> loadLevel(int levelNumber) {

        int curObjectID = GameConstants.FIRST_ENEMY_SHIP_ID;
        Map<Integer, Ship> enemyShips = new HashMap<>();

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
                enemyShips.put(curObjectID,
                        new EnemyShip(curObjectID, shipType,
                                (float) (50 + Math.random() * (screenDimention.width - 50)),
                                (float) (50 + Math.random() * screenDimention.height / 2),
                                ShipProperties.getShipProperties(shipType)));
            }

        }

        return enemyShips;

    }    // end method loadLevel

    /**
     * Finds and returns the requested level node from the xml file.
     *
     * @param levelNumber Level to load.
     * @return Element with the level details. Null if not found.
     */
    private Element getLevelElement(int levelNumber) {
        Element level = null;
        boolean levelFound = false;
        // Get all the level nodes 
        NodeList levels = xmlDocument.getElementsByTagName("level");

        // Find the level node with id equals to levelNumber
        for (int i = 0; i < levels.getLength() && !levelFound; i++) {

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
     *
     * @param level Elment with the level info
     */
    private void loadLevelBGImage(Element level) {
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
     *
     * @param levelNumber Number of the level
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
        } catch (Exception e) {
            // If any exception occures during the parsing exit the game
            Logger.exception(e);
            System.exit(-1);
        }
    }

    private class LevelsEntityResolver implements EntityResolver {

        @Override
        public InputSource resolveEntity(String publicId, String systemId) {
            if (systemId.endsWith("levels.dtd")) {
                return new InputSource(ResourceManager.getResourceAsStream(
                        GameConstants.CONFIG_DIR + "/levels.dtd"));
            }

            throw new IllegalArgumentException("Unknown systemId " + systemId);
        }
    }

}

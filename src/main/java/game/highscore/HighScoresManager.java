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

package game.highscore;

import game.GameConstants;
import game.network.client.NetworkException;
import game.network.client.NetworkManager;
import game.util.Logger;

import java.io.*;

/**
 * The <code>HighScoresManager</code> class manages the game high
 * scores. It loads and saves the scores to a file.
 * We use this class to post high scores to the game server and
 * to get the best scores from the server.
 */
public class HighScoresManager {

    private final String HIGH_SCORES_FILE_NAME =
            GameConstants.RESOURCES + "/" + "scores.dat";

    private int numOfHighScores;    // Max number of high scores
    private HighScore[] highScores;    // Array with all the local high scores

    private NetworkManager networkManager;

    /**
     * Construct new HighScoresManager and load the high scores from a file.
     *
     * @param numOfHighScores Max number of high scores to hold.
     */
    public HighScoresManager(int numOfHighScores) {

        this.numOfHighScores = numOfHighScores;
        this.highScores = new HighScore[numOfHighScores];

        try {
            loadHighScores(false);
        } catch (Exception e) {
            Logger.error("Unable to read the high scores file", e);
        }
    }

    /**
     * Sets the network manager for server communication.
     *
     * @param networkManager The network manager object.
     */
    public void setNetworkManager(NetworkManager networkManager) {
        this.networkManager = networkManager;
    }

    /**
     * Returns array with all the high scores.
     */
    public HighScore[] getHighScores() {
        return this.highScores;
    }

    /**
     * Returns max number of high scores.
     */
    public int getNumOfHighScores() {
        return numOfHighScores;
    }

    /**
     * Adds score to the high scores list. If the score is smaller or equals
     * to the current smallest score it is not added to the list. If the score
     * is not the smallest and the list is full we remove the last score.
     *
     * @param score New high score to insert to the list.
     */
    public void addScore(HighScore score, boolean save) {
        int place = -1;
        boolean placeFound = false;
        for (int i = 0; !placeFound && i < highScores.length; i++) {
            if (highScores[i] == null || score.compareTo(highScores[i]) > 0) {
                placeFound = true;
                place = i;
            }
        }

        if (placeFound) {
            if (highScores[place] == null || place == highScores.length - 1) {
                // If null or last place in array simply insert the new
                // high score to the proper place
                highScores[place] = score;
            } else {
                // Insert the new high score to the middle of the high scores
                // array. Move the lower scores one place. Remove the last one
                // if high scores exceeds numOfHighScores (the array length).
                for (int i = highScores.length - 1; i > place; i--) {
                    highScores[i] = highScores[i - 1];
                }

                // Insert the new high score to the proper place
                highScores[place] = score;
            }

            if (save) {
                // Save the high scores list to file
                try {
                    saveHighScores();
                } catch (IOException ioe) {
                    Logger.error("Unable to save the high scores file", ioe);
                }
            }

        }

    }

    /**
     * Returns true if the input score and level has place in the
     * high scores list.
     */
    public boolean isHighScore(long score, int level) {
        return isHighScore(new HighScore(null, score, level));
    }

    /**
     * Returns true if the input high score has place in the
     * high scores list.
     */
    public boolean isHighScore(HighScore score) {
        return score.getScore() > 0 &&
                (highScores[numOfHighScores - 1] == null ||
                        highScores[numOfHighScores - 1].compareTo(score) < 0);

    }


    /**
     * Return array of <code>HighScore</code> objects containing the high
     * scores starting at <code>fromPlace</code> inclusive and ending at
     * <code>toPlace</code> inclusive.
     * Place 1 is the highest score.
     *
     * @param fromPlace
     * @param toPlace
     * @throws IllegalArgumentException If fromPlace is smaller then 1
     * @return Array of high scores
     */
    public HighScore[] getHighScores(int fromPlace, int toPlace)
            throws IllegalArgumentException {

        if (fromPlace < 1 || toPlace > numOfHighScores) {
            throw new IllegalArgumentException("Input out of bounds");
        }

        if (fromPlace > toPlace) {
            throw new IllegalArgumentException("First argument must be " +
                    "smaller or equal to the second argument");
        }

        HighScore[] ret = new HighScore[toPlace - fromPlace + 1];

        System.arraycopy(highScores, fromPlace - 1, ret, 0, ret.length);

        return ret;
    }


    /**
     * Return array of <code>HighScore</code> objects containing the high
     * scores starting at <code>fromPlace</code> inclusive and ending at
     * <code>toPlace</code> inclusive.
     * Place 1 is the highest score.
     *
     * @param fromPlace Place of the high score to start with
     * @param toPlace   Place of the high score to end with
     * @return Array of high scores from the server.
     * @throws IllegalArgumentException If <code>fromPlace</code> is
     *                                  smaller then 1 or smaller then <code>toPlace</code>
     */
    public HighScore[] getNetworkHighScores(int fromPlace, int toPlace)
            throws NetworkException, IllegalArgumentException {

        if (fromPlace < 1) {
            throw new IllegalArgumentException("Input out of bounds");
        }

        if (fromPlace < 1 || fromPlace > toPlace) {
            throw new IllegalArgumentException("First argument must be " +
                    "smaller or equal to the second argument");
        }

        if (networkManager == null) {
            throw new NetworkException("NetworkManager is null. " +
                    "Please restart the game");
        }

        return networkManager.getHighScores(fromPlace, toPlace);

    }

    /**
     * Clears the high scores list.
     */
    public void clearHighScores() {
        for (int i = 0; i < highScores.length; i++) {
            highScores[i] = null;
        }
    }

    /**
     * Loads the high scores from a file.
     *
     * @param clearOldScores True if we want to clear the high
     *                       scores array we hold in this object.
     * @throws IOException            Error reading the file.
     * @throws ClassNotFoundException Class of a serialized HighScore
     *                                cannot be found
     */
    public void loadHighScores(boolean clearOldScores) throws
            IOException, ClassNotFoundException {

        if (clearOldScores) {
            clearHighScores();
        }

        File scoresFile = new File(HIGH_SCORES_FILE_NAME);

        if (scoresFile.exists()) {
            ObjectInputStream ois = new ObjectInputStream(
                    new FileInputStream(scoresFile));

            HighScore score;
            try {
                while ((score = (HighScore) ois.readObject()) != null) {
                    this.addScore(score, false);
                }
            } catch (EOFException eofe) {
                // Reached the end of file
            }

            ois.close();
        }

    }

    /**
     * Saves the array of high scores to a file.
     *
     * @throws IOException Error saving to a file.
     */
    public void saveHighScores() throws IOException {

        File scoresFile = new File(HIGH_SCORES_FILE_NAME);

        if (!scoresFile.exists()) {
            scoresFile.createNewFile();
        }

        ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(scoresFile));

        for (int i = 0; i < highScores.length && highScores[i] != null; i++) {
            oos.writeObject(highScores[i]);
        }

        oos.close();
    }

    /**
     * Posts a score to the server via the network manager object.
     *
     * @param score Score to post
     * @throws NetworkException If the network manager doesn't exist or
     *                          error when trying to post the score.
     */
    public void postScoreToServer(HighScore score) throws NetworkException {

        if (networkManager == null) {
            throw new NetworkException("NetworkManager is not initialized");
        }

        networkManager.postHighScore(score);

    }

    public String toString() {

        String ret = "High Scores:\n" +
                "Place\t" + "Name\t\t" + "Score\t" + "Level\n\n";

        for (int i = 0; i < highScores.length && highScores[i] != null; i++) {
            ret += (i + 1) + ".\t" + highScores[i] + "\n";
        }

        return ret;
    }

}

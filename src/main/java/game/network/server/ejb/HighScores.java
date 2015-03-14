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

package game.network.server.ejb;

import game.highscore.HighScore;

import javax.ejb.EJBObject;
import java.rmi.RemoteException;

/**
 * The <code>HighScores</code> EJB manages the scores
 * posted by the game players.
 */
public interface HighScores extends EJBObject {

    /**
     * Adds the posted score to the high scores table
     *
     * @param score New score to add
     */
    public void postHighScore(HighScore score) throws RemoteException;

    /**
     * @return Array of HighScore objects containing the top ten
     * high scores.
     */
    public HighScore[] getTopTenScores() throws RemoteException;

    /**
     * @return Array of HighScore objects containing the scores
     * ranked between fromRank to toRank.
     * @throws RemoteException if exception occures or fromRank
     *                         is less then 1 or fromRank > toRank.
     */
    public HighScore[] getHighScores(int fromRank, int toRank)
            throws RemoteException;

}

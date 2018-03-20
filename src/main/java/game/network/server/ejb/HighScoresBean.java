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
import game.network.server.DBHelper;

import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The <code>HighScores</code> EJB manages the scores
 * posted by the game players.
 */
class HighScoresBean implements SessionBean {

    private SessionContext sessionContext;

    public HighScoresBean() {
        // SessionBean class must implement an empty constructor
    }
    
	/* SessionBean implementation */

    @Override
    public void ejbActivate() throws EJBException {
        // Not in use in stateless session beans
    }

    @Override
    public void ejbPassivate() throws EJBException {
        // Not in use in stateless session beans
    }

    @Override
    public void ejbRemove() throws EJBException {

    }

    @Override
    public void setSessionContext(SessionContext sessionContext) {

        this.sessionContext = sessionContext;
    }

	
	/* Home interface implementation */

    public void ejbCreate() {

    }

	
	/* Implement business methods */

    /**
     * Adds the posted score to the high scores table.
     *
     * @see HighScores#postHighScore(HighScore)
     */
    public void postHighScore(HighScore score) throws EJBException {

        Connection connection = null;
        try {

            Long scoreId = EJBHelper.getNextSeqId("high_score");

            connection = DBHelper.getConnection();

            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO high_score " +
                            "(score_id, player_name, score, level) " +
                            "values(?, ?, ?, ?)");

            ps.setLong(1, scoreId);
            ps.setString(2, score.getPlayerName());
            ps.setLong(3, score.getScore());
            ps.setInt(4, score.getLevel());

            ps.executeUpdate();

            ps.close();

        } catch (Exception e) {
            throw new EJBException(e);
        } finally {
            DBHelper.releaseConnection(connection);
        }

    }

    /**
     * @see HighScores#getTopTenScores()
     */
    public HighScore[] getTopTenScores() {
        return getHighScores(1, 10);
    }

    /**
     * @see HighScores#getHighScores(int, int)
     */
    private HighScore[] getHighScores(int fromRank, int toRank)
            throws EJBException {

        Connection connection = null;
        try {
            if (fromRank < 1) {
                throw new IllegalArgumentException("Input out of bounds");
            }

            if (fromRank > toRank) {
                throw new IllegalArgumentException("First argument must be " +
                        "smaller or equal to the second argument");
            }

            HighScore[] highScores = new HighScore[toRank - fromRank + 1];

            connection = DBHelper.getConnection();

            // Create SQL query that selects all the rows in the
            // high scores table ordered by score and level descending
            PreparedStatement ps = connection.prepareStatement(
                    "SELECT player_name, score, level " +
                            "FROM high_score " +
                            "ORDER BY score DESC, level DESC");

            ResultSet rs = ps.executeQuery();

            int curRank = 1;
            int count = 0;
            while (rs.next() && curRank <= toRank) {
                if (curRank >= fromRank) {
                    String playerName = rs.getString(1);
                    long score = rs.getLong(2);
                    int level = rs.getInt(3);

                    highScores[count] =
                            new HighScore(playerName, score, level);
                    count++;
                }
            }

            rs.close();
            ps.close();

            return highScores;

        } catch (SQLException sqlException) {
            throw new EJBException(sqlException);
        } finally {
            DBHelper.releaseConnection(connection);
        }
    }

}

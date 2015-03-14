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

import game.network.server.DBHelper;

import javax.ejb.*;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

public class OnlinePlayerBean implements EntityBean {

    private EntityContext entityContext;

    private Long sessionId;        // Primary key
    private String userName;    // Foreign key
    private long sessionStartTime;
    private boolean acceptInvitations;

    public OnlinePlayerBean() {
        // Must implement no arguments constructor
    }

    /**
     * Creates a new online player.
     *
     * @param userName User name of the player.
     * @return Primary key (session id).
     * @see OnlinePlayerHome#create(String)
     */
    public Long ejbCreate(String userName) throws CreateException {

        this.userName = userName;
        this.acceptInvitations = false;
        this.sessionStartTime = System.currentTimeMillis();

        Connection connection = null;
        try {
            sessionId = EJBHelper.getNextSeqId("online_player");

            connection = DBHelper.getConnection();

            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO online_player " +
                            "(session_id, user_name, session_start_time, accept_invitations) " +
                            "values(?, ?, ?, ?)");

            ps.setLong(1, sessionId);
            ps.setString(2, userName);
            ps.setLong(3, sessionStartTime);
            ps.setBoolean(4, acceptInvitations);

            ps.executeUpdate();

            ps.close();

            return sessionId;

        } catch (Exception exception) {
            throw new CreateException(exception.getMessage());
        } finally {
            DBHelper.releaseConnection(connection);
        }

    }

    // for the ejbCreate(String)
    public void ejbPostCreate(String userName) {
        // nothing to do
    }

    @Override
    public void ejbActivate() throws EJBException, RemoteException {
        this.sessionId = (Long) entityContext.getPrimaryKey();
    }

    @Override
    public void ejbPassivate() throws EJBException, RemoteException {
        sessionId = null;
    }

    /**
     * Load the online player's details from the database.
     */
    @Override
    public void ejbLoad() throws EJBException, RemoteException {

        Connection connection = null;
        try {

            Long sessionId = (Long) entityContext.getPrimaryKey();

            connection = DBHelper.getConnection();

            PreparedStatement ps = connection.prepareStatement(
                    "SELECT session_id, user_name, " +
                            "session_start_time, accept_invitations " +
                            "FROM online_player " +
                            "WHERE session_id = ?");

            ps.setLong(1, sessionId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                this.sessionId = rs.getLong(1);
                this.userName = rs.getString(2);
                this.sessionStartTime = rs.getLong(3);
                this.acceptInvitations = rs.getBoolean(4);
            } else {
                throw new EJBException("Session not found: " + sessionId);
            }

            rs.close();
            ps.close();

        } catch (SQLException sqlException) {
            throw new EJBException(sqlException);
        } finally {
            DBHelper.releaseConnection(connection);
        }

    }

    /**
     * Removes the online player from the database.
     * Should be called when the player logs out or quits the game.
     */
    @Override
    public void ejbRemove() throws
            RemoveException, EJBException, RemoteException {

        Connection connection = null;
        try {
            Long sessionId = (Long) entityContext.getPrimaryKey();

            connection = DBHelper.getConnection();

            PreparedStatement ps = connection.prepareStatement(
                    "DELETE FROM online_player " +
                            "WHERE session_id = ?");

            ps.setLong(1, sessionId);

            ps.executeUpdate();

        } catch (SQLException sqlException) {
            throw new RemoveException("Error while trying to remove " +
                    "online player id " + sessionId + "\n" +
                    sqlException.getMessage());
        } finally {
            DBHelper.releaseConnection(connection);
        }

    }

    /**
     * Store the details to the database.
     */
    @Override
    public void ejbStore() throws EJBException, RemoteException {

        Connection connection = null;
        try {
            connection = DBHelper.getConnection();

            PreparedStatement ps = connection.prepareStatement(
                    "UPDATE online_player " +
                            "SET user_name = ?, session_start_time = ?, " +
                            "accept_invitations = ? " +
                            "WHERE session_id = ?");

            ps.setString(1, userName);
            ps.setLong(2, sessionStartTime);
            ps.setBoolean(3, acceptInvitations);
            ps.setLong(4, sessionId);

            ps.executeUpdate();

            ps.close();

        } catch (SQLException sqlException) {
            throw new EJBException(sqlException);
        } finally {
            DBHelper.releaseConnection(connection);
        }

    }

    @Override
    public void setEntityContext(EntityContext entityContext)
            throws EJBException, RemoteException {

        this.entityContext = entityContext;

    }

    @Override
    public void unsetEntityContext() throws EJBException, RemoteException {

        entityContext = null;

    }

    /**
     * Find online player by primary key.
     */
    public Long ejbFindByPrimaryKey(Long sessionId)
            throws FinderException {

        boolean found;

        Connection connection = null;
        try {
            connection = DBHelper.getConnection();

            PreparedStatement ps = connection.prepareStatement(
                    "SELECT session_id " +
                            "FROM online_player " +
                            "WHERE session_id = ?");

            ps.setLong(1, sessionId);

            ResultSet rs = ps.executeQuery();

            found = rs.next();

            rs.close();
            ps.close();

            if (found) {
                return sessionId;
            } else {
                throw new FinderException("Session " + sessionId + " doesn't exist");
            }

        } catch (SQLException sqlException) {
            throw new EJBException(sqlException);
        } finally {
            DBHelper.releaseConnection(connection);
        }
    }

    /**
     * @see OnlinePlayerHome#findByAcceptInvitations()
     */
    public Collection ejbFindByAcceptInvitations()
            throws FinderException {

        Collection<Long> result = new ArrayList<Long>();

        Connection connection = null;
        try {
            connection = DBHelper.getConnection();

            PreparedStatement ps = connection.prepareStatement(
                    "SELECT session_id " +
                            "FROM online_player " +
                            "WHERE accept_invitations = ?");

            ps.setBoolean(1, true);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Long sessionId = rs.getLong(1);
                result.add(sessionId);
            }

            rs.close();
            ps.close();

            return result;

        } catch (SQLException sqlException) {
            throw new EJBException(sqlException);
        } finally {
            DBHelper.releaseConnection(connection);
        }
    }

    public OnlinePlayerModel getOnlinePlayerModel() {
        return new OnlinePlayerModel(sessionId, userName, sessionStartTime);
    }


    /* Implement business methods */

    /**
     * @see OnlinePlayer#setAcceptInvitations(boolean)
     */
    public void setAcceptInvitations(boolean acceptInvitations) {
        this.acceptInvitations = acceptInvitations;
    }

}

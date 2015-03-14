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

/**
 * The <code>SequenceFactory</code> is used to generate unique
 * sequence id for each table name (or any name).
 */
public class SequenceFactoryBean implements EntityBean {

    private EntityContext entityContext;

    private String tableName;    // Primary key
    private Long nextID;

    /**
     * Create new sequence generator.
     *
     * @param tableName Primary key.
     */
    public String ejbCreate(String tableName) throws CreateException {
        this.tableName = tableName;
        this.nextID = (long) 1;    // Start from id 1

        Connection connection = null;
        try {
            connection = DBHelper.getConnection();
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO sequence_factory " +
                            "(table_name, next_id) " +
                            "VALUES(?, ?)");

            ps.setString(1, tableName);
            ps.setLong(2, nextID);

            ps.executeUpdate();

            ps.close();

            return tableName;

        } catch (SQLException sqlException) {
            throw new CreateException(sqlException.getMessage());
        } finally {
            DBHelper.releaseConnection(connection);
        }

    }

    /**
     * For the ejbCreate(String)
     */
    public void ejbPostCreate(String tableName) {

    }

    /**
     * Set the primary key.
     */
    @Override
    public void ejbActivate() throws EJBException, RemoteException {
        this.tableName = (String) entityContext.getPrimaryKey();
    }

    /**
     * Unset the primary key.
     */
    @Override
    public void ejbPassivate() throws EJBException, RemoteException {
        this.tableName = null;
    }

    /**
     * Load the details from the database.
     */
    @Override
    public void ejbLoad() throws EJBException, RemoteException {
        Connection connection = null;
        try {
            String tableName = (String) entityContext.getPrimaryKey();

            connection = DBHelper.getConnection();
            PreparedStatement ps = connection.prepareStatement(
                    "SELECT table_name, next_id " +
                            "FROM sequence_factory " +
                            "WHERE table_name = ?");

            ps.setString(1, tableName);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                this.tableName = rs.getString(1);
                this.nextID = rs.getLong(2);
            } else {
                throw new EJBException("Table name not found: " + tableName);
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
     * Remove from the database.
     */
    @Override
    public void ejbRemove() throws RemoveException, EJBException,
            RemoteException {

        Connection connection = null;
        try {
            String tableName = (String) entityContext.getPrimaryKey();

            connection = DBHelper.getConnection();
            PreparedStatement ps = connection.prepareStatement(
                    "DELETE FROM sequence_factory " +
                            "WHERE table_name = ?");

            ps.setString(1, tableName);

            ps.executeUpdate();

        } catch (SQLException sqlException) {
            throw new RemoveException("Error while trying to remove " +
                    tableName + " from sequence_factory.\n" +
                    sqlException.getMessage());
        } finally {
            DBHelper.releaseConnection(connection);
        }

    }

    /**
     * Store details to the database.
     */
    @Override
    public void ejbStore() throws EJBException, RemoteException {
        Connection connection = null;
        try {
            connection = DBHelper.getConnection();
            PreparedStatement ps = connection.prepareStatement(
                    "UPDATE sequence_factory " +
                            "SET next_id = ? " +
                            "WHERE table_name = ?");

            ps.setLong(1, nextID);
            ps.setString(2, tableName);

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
        this.entityContext = null;

    }

    /**
     * Find the bean by the primary key.
     *
     * @param tableName Primary key
     */
    public String ejbFindByPrimaryKey(String tableName) throws FinderException {
        boolean found;

        Connection connection = null;
        try {
            connection = DBHelper.getConnection();
            PreparedStatement ps = connection.prepareStatement(
                    "SELECT table_name " +
                            "FROM sequence_factory " +
                            "WHERE table_name = ?");

            ps.setString(1, tableName);

            ResultSet rs = ps.executeQuery();

            found = rs.next();

            rs.close();
            ps.close();

            if (found) {
                return tableName;
            } else {
                throw new FinderException("Table " + tableName + " doesn't exist");
            }

        } catch (SQLException sqlException) {
            throw new EJBException(sqlException);
        } finally {
            DBHelper.releaseConnection(connection);
        }
    }
    
    
    /* Implement business methods */

    /**
     * @see SequenceFactory#getNextID()
     */
    public Long getNextID() {

        Long id = nextID;

        // Increment the id by 1
        nextID = nextID + 1;

        return id;

    }

}

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

import javax.ejb.EJBObject;
import java.rmi.RemoteException;

/**
 * The <code>Player</code> EJB holds the data of a registered player.
 */
public interface Player extends EJBObject {

    /**
     * Returns the user name (the primary key).
     *
     * @return The user name.
     */
    String getUserName() throws RemoteException;

    /**
     * Returns the user's passsword.
     *
     * @return The user's passsword.
     */
    String getPassword() throws RemoteException;

    /**
     * Sets the user's password.
     *
     * @param password Password to set.
     */
    void setPassword(String password) throws RemoteException;

    /**
     * Returns the user's email.
     *
     * @return The user's email.
     */
    String getEmail() throws RemoteException;

    /**
     * Sets the user's email.
     *
     * @param email Email to set.
     */
    void setEmail(String email) throws RemoteException;


}

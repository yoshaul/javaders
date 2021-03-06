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

import javax.ejb.CreateException;
import javax.ejb.EJBHome;
import javax.ejb.FinderException;
import java.rmi.RemoteException;

interface PlayerHome extends EJBHome {

    /**
     * Find player by primary key (user name)
     */
    Player findByPrimaryKey(String pk)
            throws RemoteException, FinderException;

    /**
     * Create a new user.
     *
     * @param pk       User name
     * @param password Password
     */
    Player create(String pk, String password)
            throws RemoteException, CreateException;

    /**
     * Create a new user.
     *
     * @param pk       User name
     * @param password Password
     * @param email    Email
     */
    Player create(String pk, String password, String email)
            throws RemoteException, CreateException;

}

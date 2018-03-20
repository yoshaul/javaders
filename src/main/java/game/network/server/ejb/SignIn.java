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

import game.network.InvalidLoginException;

import javax.ejb.EJBObject;
import java.rmi.RemoteException;

/**
 * The signin bean is used for users validation, logout and signups.
 */
public interface SignIn extends EJBObject {

    /**
     * Try to log in with the supplied username and password.
     *
     * @param userName User name
     * @param password Passwrod
     * @throws InvalidLoginException If the user or password are wrong.
     * @return Session id if logged in successfully
     */
    Long login(String userName, String password)
            throws RemoteException, InvalidLoginException;

    /**
     * Logout of the system.
     *
     * @param sessiondId Session id to finish.
     */
    void logout(Long sessiondId)
            throws RemoteException;

    /**
     * Signup a new user.
     *
     * @param userName User name
     * @param password Password
     * @param email    Email of the user (may be empty).
     * @throws RemoteException
     */
    void addUser(String userName, String password, String email)
            throws RemoteException;

}

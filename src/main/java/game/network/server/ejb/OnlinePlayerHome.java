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
import java.util.Collection;

public interface OnlinePlayerHome extends EJBHome {

    /**
     * Find online player by primary key (session id).
     */
    public OnlinePlayer findByPrimaryKey(Long pk)
            throws RemoteException, FinderException;

    /**
     * Find all the online players that accepts invitations.
     */
    public Collection findByAcceptInvitations()
            throws RemoteException, FinderException;

    /**
     * Create a new online player. Use the next sequence as
     * the session id.
     *
     * @param userName User name of the online player.
     */
    public OnlinePlayer create(String userName)
            throws RemoteException, CreateException;

}

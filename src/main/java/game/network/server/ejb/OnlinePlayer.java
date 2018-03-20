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
 * The <code>OnlinePlayer</code> represents an online player
 * that is logged in the system.
 * Each online player has its own primary pk (the session id).
 */
public interface OnlinePlayer extends EJBObject {

    /**
     * Sets the desire of the online player to allow
     * invitations to be sent to him.
     * The players will only see the online players with the
     * accept flag set to true in the online players list.
     *
     * @param accept True to accept false not to.
     */
    void setAcceptInvitations(boolean accept)
            throws RemoteException;

    /**
     * Returns a simple model of the online player which contains
     * its session id, name etc.
     *
     * @return OnlinePlayerModel of the player.
     * @see OnlinePlayerModel
     */
    OnlinePlayerModel getOnlinePlayerModel()
            throws RemoteException;

}

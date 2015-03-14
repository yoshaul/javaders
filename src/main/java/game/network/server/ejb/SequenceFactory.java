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
 * The <code>SequenceFactory</code> is used to generate unique
 * sequence id for each table name (or any name).
 */
public interface SequenceFactory extends EJBObject {

    /**
     * Returns the next dequence id.
     *
     * @return The ext sequence id.
     */
    public Long getNextID() throws RemoteException;

}

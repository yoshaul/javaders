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
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;
import java.rmi.RemoteException;

public class EJBHelper {

    /**
     * Returns the EJB home interface.
     *
     * @param jndiName  JNDI name of the bean.
     * @param homeClass Home class of the bean.
     * @throws NamingException If name couldn't be found.
     * @return EJB home interface.
     */
    public static EJBHome getEJBHome(String jndiName, Class homeClass)
            throws NamingException {

        InitialContext initialContext = new InitialContext();

        Object objref = initialContext.lookup(jndiName);

        return (EJBHome) PortableRemoteObject.narrow(objref, homeClass);
    }

    /**
     * Returns the next sequence id for the specified table.
     * The next sequence id is taken form the
     * <code>SequenceFactory</code> ejb.
     * If no sequence is available for the input table, create it.
     *
     * @param tableName Table name
     * @return Next sequence for the input table.
     */
    public static Long getNextSeqId(String tableName)
            throws RemoteException, NamingException, CreateException {

        SequenceFactoryHome sequenceFactoryHome = (SequenceFactoryHome)
                getEJBHome(JNDINames.SEQUENCE_FACTORY_BEAN,
                        SequenceFactoryHome.class);

        // find the sequence factory for the input table.
        // If not found create it.
        SequenceFactory sequenceFactory;
        try {
            sequenceFactory =
                    sequenceFactoryHome.findByPrimaryKey(tableName);
        } catch (FinderException fe) {
            sequenceFactory =
                    sequenceFactoryHome.create(tableName);
        }

        return sequenceFactory.getNextID();
    }

}

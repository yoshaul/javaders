
package game.network.server.ejb;

import java.rmi.RemoteException;

import javax.ejb.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;

public class EJBHelper {

    /**
     * Returns the EJB home interface.
     * @param jndiName		JNDI name of the bean.
     * @param homeClass		Home class of the bean.
     * @return	EJB home interface.
     * @throws NamingException	If name couldn't be found.
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
     * @param tableName	Table name
     * @return	Next sequence for the input table.
     */
    public static Long getNextSeqId(String tableName) 
    		throws RemoteException, NamingException, CreateException {
        
		SequenceFactoryHome sequenceFactoryHome = (SequenceFactoryHome)
			getEJBHome(JNDINames.SEQUENCE_FACTORY_BEAN, 
			        SequenceFactoryHome.class);
		
		// find the sequence factory for the input table.
		// If not found create it.
		SequenceFactory sequenceFactory = null;
		try {
			sequenceFactory = 
			    sequenceFactoryHome.findByPrimaryKey(tableName);
		}
		catch (FinderException fe) {
		    sequenceFactory = 
			    sequenceFactoryHome.create(tableName);
		}
		
		return sequenceFactory.getNextID();
    }
    
}

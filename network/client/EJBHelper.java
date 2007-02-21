package game.network.client;

import javax.ejb.EJBHome;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;

public class EJBHelper {

    public static EJBHome getEJBHome(String jndiName, Class homeClass)
    	throws NamingException {
        
        InitialContext initialContext = new InitialContext();
        
        Object objref = initialContext.lookup(jndiName);
        
        return (EJBHome) PortableRemoteObject.narrow(objref, homeClass);
    }
    
//    public static Object getEJBRemote(String jndiName, Class homeClass) {
//        
//        EJBHome ejbHome = getEJBHome(jndiName, homeClass);
//        
//		// create the enterprise bean instance
//		return ejbHome.
//        
//    }
    
}

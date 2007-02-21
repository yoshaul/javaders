package game.network;

import java.rmi.RemoteException;

import game.network.server.ejb.*;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;

// J2EE packages
import javax.ejb.CreateException;

public class J2EENetworkManager implements NetworkManager {
    
    private SignIn signIn;
    
    public void sendPacket(GamePacket packet) {
        
    }
    
    public GamePacket receivePacket() {
        return null;
    }
    
    
    public String login(String userName, String password) {
        
        String ticket = null;
        
		try {
			// create an initial naming context
			InitialContext initialContext = new InitialContext();
			
			// obtain the environment maming context of the application client
			Context env = (Context) initialContext.lookup("java:comp/env");
			
			// retrieve the object bound to the name ejb/SignInHome
			Object objref = env.lookup("ejb/SignIn");
			
			// narrow the context to a SignInHome object
			SignInHome signInHome = (SignInHome) 
				PortableRemoteObject.narrow(objref, SignInHome.class);

			// create the enterprise bean instance
			signIn = signInHome.create();
			
//			statusLabel.setText("SignIn bean created successfully");
		}
		catch (NamingException namingException) {
			namingException.printStackTrace();
		}
		catch (CreateException createException) {
			createException.printStackTrace();
		}
		catch (RemoteException remoteException) {
			remoteException.printStackTrace();
		}
        
        if (signIn != null) {
			try {
				ticket = signIn.validateUser(userName, password);
		
			}
			catch (InvalidLoginException e) {
				System.out.println(e.getMessage());
			}
			catch (RemoteException e) {
			    System.out.println("ERRORL: Remote exception occured");
			    e.printStackTrace();
			}
        }
		return ticket;
		
    }
    
    public String signup (String userName, String password, String email) {
        String ticket = null;
        
		try {
			// create an initial naming context
			InitialContext initialContext = new InitialContext();
			
			// obtain the environment maming context of the application client
			Context env = (Context) initialContext.lookup("java:comp/env");
			
			// retrieve the object bound to the name ejb/SignInHome
			Object objref = env.lookup("ejb/SignIn");
			
			// narrow the context to a SignInHome object
			SignInHome signInHome = (SignInHome) 
				PortableRemoteObject.narrow(objref, SignInHome.class);

			// create the enterprise bean instance
			signIn = signInHome.create();
			
//			statusLabel.setText("SignIn bean created successfully");
		}
		catch (NamingException namingException) {
			namingException.printStackTrace();
		}
		catch (CreateException createException) {
			createException.printStackTrace();
		}
		catch (RemoteException remoteException) {
			remoteException.printStackTrace();
		}
        
        if (signIn != null) {
			try {
				ticket = signIn.addUser(userName, password, email);
		
			}
//			catch (InvalidLoginException e) {
//				System.out.println(e.getMessage());
//			}
			catch (RemoteException e) {
			    System.out.println("ERRORL: Remote exception occured:");
			    e.printStackTrace();
			}
        }
		return ticket;
    }

}

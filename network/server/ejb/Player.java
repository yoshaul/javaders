package game.network.server.ejb;

import java.rmi.RemoteException;

import javax.ejb.EJBObject;

public interface Player extends EJBObject {
    
    public String getUserName() throws RemoteException;
    
    public String getPassword() throws RemoteException;
    public void setPassword(String password) throws RemoteException;
    
    public String getEmail() throws RemoteException;
    public void setEmail(String email) throws RemoteException;
    

}

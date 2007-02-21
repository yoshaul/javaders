
package game.network.server.ejb;

import game.network.InvalidLoginException;

import java.sql.*;

import javax.ejb.*;
import javax.naming.InitialContext;
import javax.sql.DataSource;

/**
 * @author Yossi Shaul
 *
 */
public class SignInBean implements SessionBean {

	private final String DBName = 
		"java:comp/env/jdbc/gameDB";
	
	private Connection connection;	
	private SessionContext sessionContext;
	

	/* SessionBean class must implement an empty constructor */
	public SignInBean() {}
	
	/* SessionBean interface implementation */
	
	public void ejbActivate() {}
	public void ejbPassivate() {}
	
	public void ejbRemove() {
	    try {
		    if (connection != null) {
			    connection.close();
		    }
		} 
		catch (SQLException sqlException) {
			sqlException.printStackTrace();
		}
	}

	public void setSessionContext(SessionContext sessionContext) {
		
		this.sessionContext = sessionContext;
	}
	
	
	/* SignInHome interface implementation */
	
	public void ejbCreate() throws CreateException {
		try {
			InitialContext initialContext = new InitialContext();

			DataSource ds = (DataSource) initialContext.lookup(DBName);
			connection = ds.getConnection();
		}
		
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/* SignIn interface implementation (business methods) */
	
	public String validateUser(String userName, String password) 
		throws EJBException, InvalidLoginException {
		
		String ticket = null;
		
		if (userName == null || password == null) {
			throw new InvalidLoginException();
		}

		try {
			PreparedStatement ps = connection.prepareStatement(
				"SELECT email " +
				"FROM player " +
				"WHERE user_name = ? AND password = ?");

			ps.setString(1, userName);
			ps.setString(2, password);
			
			ResultSet rs = ps.executeQuery();
			
			if (rs.next()) {
				ticket = rs.getString(1);
			} else {
				throw new InvalidLoginException();
			}
			
		}
		catch (SQLException sqlException) {
			throw new EJBException(sqlException);
		}
		
		return ticket;
	}
	
	public String addUser(String userName, String password, 
			String email) throws EJBException {
	    
		try {
			PreparedStatement ps = connection.prepareStatement(
					"INSERT INTO player " +
					"(user_name, password, email) " +
					"VALUES(?, ?, ?)");
		
			ps.setString(1, userName);
			ps.setString(2, password);
			ps.setString(3, email);
			
			ps.executeUpdate();
			
			ps.close();
			
		}
		catch (SQLException sqlException) {
			throw new EJBException(sqlException);
		}
		
		return userName;
	}

}	// end class SignInBean

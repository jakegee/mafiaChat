
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import exceptions.InvalidInformationException;
import exceptions.InvalidUserException;
import exceptions.UserExistsException;
import systemInterfaces.IDatabase;

public class DatabaseManager implements IDatabase {

	private String dbName;
	private Connection dbConn;
	private PreparedStatement createUser;
	
	public DatabaseManager() {
		
		System.setProperty("jdbc.drivers", "org.postgresql.Driver");

		try {
			Class.forName("org.postgresql.Driver");
			dbName = "jdbc:postgresql://mod-fund-databases.cs.bham.ac.uk/jgs630";
			dbConn = DriverManager.getConnection(dbName, "jgs630", "bakny4ne83");
		} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		} catch (ClassNotFoundException ex) {
			System.out.println("Driver not found");
		}
		
	}
			
	public void registerUser(String username, String password, String securityQuestion, String questionAnswer)  
		throws UserExistsException{
		
	try {
		PreparedStatement sameUser = dbConn.prepareStatement("SELECT username FROM users WHERE username LIKE ?");
		sameUser.setString(1, username);
		ResultSet rs = sameUser.executeQuery();
		if (rs.next()) {
			throw new UserExistsException(username);
		} 

		createUser = dbConn.prepareStatement("INSERT INTO users (username, password, securityQuestion, questionAnswer) VALUES (?,?,?,?)");
//		int userid = 1;
//			createUser.setInt(userid);
			createUser.setString(1, username);
			createUser.setString(2, password);
			createUser.setString(3, securityQuestion);
			createUser.setString(4, questionAnswer);
			createUser.executeUpdate();
//			userid++;
			
			
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void loginUser(String username, String password) throws InvalidUserException, InvalidInformationException {

		try {
			PreparedStatement userLogin = dbConn.prepareStatement("SELECT userid, username, password FROM users WHERE userid = ?");
			ResultSet rs = userLogin.executeQuery();
			while (rs.next()) {
				String usersName = rs.getString("username");
				String userPassword = rs.getString("password");

				System.out.println("username : " + username);
				System.out.println("password : " + password);
			} 


				
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}

	}

	@Override
	public String getSecurityQuestion(String username) throws InvalidUserException {
		/*	
		try {
			PreparedStatement exUser = dbConn.prepareStatement("SELECT username FROM users WHERE username LIKE ?");
			invalidUser.setString(1, username);
			ResultSet rs = invalidUser.executeQuery();
			if (rs.next()) {
				throw new InvalidUserException(username);
				
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}	**/
	}

	

	@Override
	public String checkQuestionAnswer(String username, String hintAnswer)
			throws InvalidUserException, InvalidInformationException {
		// TODO Auto-generated method stub
		return null;
	}
	
	public static void main(String[] args) {
		System.out.println("HELLO!!!!!");
	}
}
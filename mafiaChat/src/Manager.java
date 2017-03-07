package mafiaChat;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DatabaseManager implements IDatabase {

	private String dbName;
	private Connection dbConn;
	private PreparedStatement createUser;
	
	public Manager() {
		
		System.setProperty("jdbc.drivers", "org.postgresql.Driver");

		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException ex) {
			System.out.println("Driver not found");
		}

		try {
			dbName = "jdbc:postgresql://mod-fund-databases.cs.bham.ac.uk/jgs630";
			dbConn = DriverManager.getConnection(dbName, "jgs630", "bakny4ne83");
		}
			
		public void registerUser(String username, String password, String securityQuestion, String questionAnswer)  {
		
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
		}
	
	
		catch (FileNotFoundException e) {
			System.out.println("File not found");
		} catch (SQLException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			System.out.println(ex.getMessage() + "Error reading file");
		}
	}
}
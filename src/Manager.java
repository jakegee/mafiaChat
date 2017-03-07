package mafiaChat;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class Manager extends Database implements IDatabase {

	private String dbName;
	private Connection dbConn;
	private PreparedStatement createUser;
//	private PreparedStatement createHint;
	
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
			
		public void registerUser(String username, String password, String securityQuestion, String questionAnswer) {
			super(username, password, securityQuestion, questionAnswer);

		createUser = dbConn.prepareStatement("INSERT INTO users VALUES(?,?,?,?,?)");

		int userid = 1;

			createUser.setInt(1, userid);
			createUser.setString(2, "jake");
			createUser.setString(3, "123");
			createUser.setString(4, "how are you?");
			createUser.setString(5, "fine");
			createUser.executeUpdate();
			userid++;
		}
	
/**
		createHint = dbConn.prepareStatement("INSERT INTO passwordhints VALUES(?,?,?,?)");
		
		int hintid = 1;
			createUser.setInt(1, hintid);
			createUser.setString(2, question);
			createUser.setString(3, hint);
			createUser.setString(4, userid);
			createUser.executeUpdate();
			hintid++;
*/		
	
		catch (FileNotFoundException e) {
			System.out.println("File not found");
		} catch (SQLException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			System.out.println(ex.getMessage() + "Error reading file");
		}
	}
}
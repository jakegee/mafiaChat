
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.sql.*;

public class Database {
	
	public static void main(String[] args) {
		// TODO code application logic here

		System.setProperty("jdbc.drivers", "org.postgresql.Driver");

		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException ex) {
			System.out.println("Driver not found");
		}

		try {
			String dbName = "jdbc:postgresql://mod-fund-databases.cs.bham.ac.uk/jgs630";

			Connection dbConn = DriverManager.getConnection(dbName, "jgs630", "bakny4ne83");
			
			PreparedStatement createUser = dbConn.prepareStatement("INSERT INTO users VALUES(?,?,?)");
			PreparedStatement createHint = dbConn.prepareStatement("INSERT INTO passwordhints VALUES(?,?,?,?)");
			
			List<String> userinfo = new ArrayList<String>();
			List<String> hintinfo = new ArrayList<String>();
		}
		
		int userid = 1;
			createUser.setInt(1, userid);
			createUser.setString(2, username);
			createUser.setString(3, password);
			createUser.executeUpdate();
			userid++;
		
		int hintid = 1;
		for (String hint : userinfo) {
			createUser.setInt(1, hintid);
			createUser.setString(2, question);
			createUser.setString(3, hint);
			createUser.setString(4, userid);
			createUser.executeUpdate();
			userid++;
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
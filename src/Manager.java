package mafiaChat;

import java.sql.Connection;
import java.sql.DriverManager;

public class Manager implements IDatabase {

	public Manager() {
		
		System.setProperty("jdbc.drivers", "org.postgresql.Driver");

		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException ex) {
			System.out.println("Driver not found");
		}

		try {
			String dbName = "jdbc:postgresql://mod-fund-databases.cs.bham.ac.uk/jgs630";

			Connection dbConn = DriverManager.getConnection(dbName, "jgs630", "bakny4ne83");
		}
		
		catch (FileNotFoundException e) {
			System.out.println("File not found");
		} catch (SQLException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			System.out.println(ex.getMessage() + "Error reading file");
		}
	}
	public registerUser() {
		
	}

	m.register()
}

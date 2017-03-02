
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

			BufferedReader br = new BufferedReader(new FileReader("artists-songs-albums-tags.csv"));

			String dbName = "jdbc:postgresql://mod-fund-databases.cs.bham.ac.uk/jgs630";

			Connection dbConn = DriverManager.getConnection(dbName, "jgs630", "bakny4ne83");
			
			br.close();

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
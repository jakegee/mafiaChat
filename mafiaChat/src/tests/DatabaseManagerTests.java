package tests;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.Before;
import org.junit.Test;

import Client.Client;
import Server.DatabaseManager;
import Server.Server;
import exceptions.InvalidInformationException;
import exceptions.InvalidUserException;

public class DatabaseManagerTests {

	Statement stmt = null;
	private String dbName;
	private Connection dbConn;
	DatabaseManager dbmanager = new DatabaseManager();

	@Before
	public void setUp() throws Exception {

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
	@Test public void readtest1() throws SQLException {
	stmt=dbConn.createStatement();
	ResultSet rs = stmt.executeQuery("SELECT password FROM users WHERE user = 'jon'");
	
	while(rs.next()) {
		
//		int userid = rs.getInt(0);
//		String username = rs.getString(1);
//		String password = rs.getString(2);
		String question = rs.getString("question");
//		String answer = rs.getString("answer");
//		String output = DatabaseManager.getSecurityQuestion(question);

		assertEquals(question,"hi");
	
//		System.out.println("userid = " + userid);
//		System.out.println("username = " + username);
//		System.out.println("password = " + password);
		System.out.println("question = " + question);
//		System.out.println("answer = " + answer);
//		System.out.println();
	}
	}
	
	@Test public void readtest2() throws SQLException {
	stmt=dbConn.createStatement();
	ResultSet rs = stmt.executeQuery("SELECT answer FROM users WHERE question = 'hi'");
	
	while(rs.next()) {
		
//		int userid = rs.getInt(0);
//		String username = rs.getString(1);
//		String password = rs.getString(2);
//		String question = rs.getString("question");
		String answer = rs.getString("answer");
//		String output = DatabaseManager.getSecurityQuestion(question);

		assertEquals(answer,"no");
	
//		System.out.println("userid = " + userid);
//		System.out.println("username = " + username);
//		System.out.println("password = " + password);
//		System.out.println("question = " + question);
//		System.out.println("answer = " + answer);
//		System.out.println();
	}
	}
	
	@Test public void readtest3() throws SQLException {
	stmt=dbConn.createStatement();
	ResultSet rs = stmt.executeQuery("SELECT question FROM users WHERE user = 'jon'");
	
	while(rs.next()) {
		
//		int userid = rs.getInt(0);
//		String username = rs.getString(1);
//		String password = rs.getString(2);
		String question = rs.getString("question");
//		String answer = rs.getString("answer");
//		String output = DatabaseManager.getSecurityQuestion(question);

		assertEquals(question,"hi");
	
//		System.out.println("userid = " + userid);
//		System.out.println("username = " + username);
//		System.out.println("password = " + password);
		System.out.println("question = " + question);
//		System.out.println("answer = " + answer);
//		System.out.println();
	}
	}

	@Test public void readtest4() throws SQLException {
	stmt=dbConn.createStatement();

	ResultSet rs = stmt.executeQuery("SELECT question FROM users WHERE user = 'newUser'");
	while(rs.next()) {
		
//		int userid = rs.getInt(0);
//		String username = rs.getString(1);
//		String password = rs.getString(2);
		String question = rs.getString("question");
//		String answer = rs.getString("answer");
//		String output = DatabaseManager.getSecurityQuestion(question);
	
		assertEquals(question,"ques");
	}
	}
	
	@Test public void readtest5() throws SQLException {
	stmt=dbConn.createStatement();

	ResultSet rs = stmt.executeQuery("SELECT question FROM users WHERE user LIKE 'Ja'");
	while(rs.next()) {
		
//		int userid = rs.getInt(0);
//		String username = rs.getString(1);
//		String password = rs.getString(2);
		String question = rs.getString(3);
//		String answer = rs.getString("answer");
//		String output = DatabaseManager.getSecurityQuestion(question);

		assertEquals(question,"cool");
		System.out.println("question = " + question);

	}
	
	rs.close();
	stmt.close();
	dbConn.close();
	}
}


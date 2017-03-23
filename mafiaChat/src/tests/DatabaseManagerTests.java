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

/**
 * Short test-suite of SQL queries in order to test that DatabaseManager is reading
 * from the database correctly.
 * 
 * @author Team Nice
 * @version 18-03-2017
 */
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
			e.printStackTrace();
		} catch (ClassNotFoundException ex) {
			System.out.println("Driver not found");
		}
	}
	
	@Test public void readtest1() throws SQLException {
	stmt=dbConn.createStatement();
	ResultSet rs = stmt.executeQuery("SELECT username FROM users WHERE password = 'hello'");
	
	while(rs.next()) {
		String username = rs.getString("username");

		assertEquals(username,"jake");
		System.out.println("username = " + username);
		System.out.println();
	}
	}
	
	@Test public void readtest2() throws SQLException {
	stmt=dbConn.createStatement();
	ResultSet rs = stmt.executeQuery("SELECT username FROM users WHERE question = 'hi'");
	
	while(rs.next()) {
		String username = rs.getString("username");

		assertEquals(username,"jon");
		System.out.println("username = " + username);
		System.out.println();
	}
	}
	
	@Test public void readtest3() throws SQLException {
	stmt=dbConn.createStatement();
	ResultSet rs = stmt.executeQuery("SELECT username FROM users WHERE answer = 'no'");
	
	while(rs.next()) {
		String username = rs.getString("username");

		assertEquals(username,"jon");
		System.out.println("username = " + username);
		System.out.println();
	}
	}
	
	@Test public void readtest4() throws SQLException {
	stmt=dbConn.createStatement();
	ResultSet rs = stmt.executeQuery("SELECT password FROM users WHERE username = 'jake'");
	
	while(rs.next()) {
		String password = rs.getString("password");

		assertEquals(password,"hello");
		System.out.println("password = " + password);
		System.out.println();
	}
	}
	
	@Test public void readtest5() throws SQLException {
	stmt=dbConn.createStatement();
	ResultSet rs = stmt.executeQuery("SELECT password FROM users WHERE username = 'fozia'");
	
	while(rs.next()) {
		String password = rs.getString("password");

		assertEquals(password,"fozia");
		System.out.println("password = " + password);
		System.out.println();
	}
	}
	
	@Test public void readtest6() throws SQLException {
	stmt=dbConn.createStatement();
	ResultSet rs = stmt.executeQuery("SELECT password FROM users WHERE username = 'david'");
	
	while(rs.next()) {
		String password = rs.getString("password");

		assertEquals(password,"david");
		System.out.println("password = " + password);
		System.out.println();
	}
	}

	@Test public void readtest7() throws SQLException {
	stmt=dbConn.createStatement();
	ResultSet rs = stmt.executeQuery("SELECT question FROM users WHERE answer = 'no'");
	
	while(rs.next()) {

		String question = rs.getString("question");

		assertEquals(question,"hi");
		System.out.println("question = " + question);
		System.out.println();
	}
	}

	@Test public void readtest8() throws SQLException {
	stmt=dbConn.createStatement();

	ResultSet rs = stmt.executeQuery("SELECT question FROM users WHERE userid = 2");
	while(rs.next()) {
		
		String question = rs.getString("question");
	
		assertEquals(question,"this is a test question");
		System.out.println("question = " + question);
		System.out.println();
	}
	}
	
	@Test public void readtest9() throws SQLException {
	stmt=dbConn.createStatement();

	ResultSet rs = stmt.executeQuery("SELECT question FROM users WHERE userid = 1");
	while(rs.next()) {
		
		String question = rs.getString("question");

		assertEquals(question,"isianawesome");
		System.out.println("question = " + question);
		System.out.println();
	}
	}
	
	
	@Test public void readtest10() throws SQLException {
	stmt=dbConn.createStatement();
	ResultSet rs = stmt.executeQuery("SELECT answer FROM users WHERE question = 'hi'");
	
	while(rs.next()) {
		
		String answer = rs.getString("answer");

		assertEquals(answer,"no");
		System.out.println("answer = " + answer);
		System.out.println();
	}
	}
	
	
	@Test public void readtest11() throws SQLException {
	stmt=dbConn.createStatement();
	ResultSet rs = stmt.executeQuery("SELECT answer FROM users WHERE question = 'me'");
	
	while(rs.next()) {
		
		String answer = rs.getString("answer");

		assertEquals(answer,"me");
		System.out.println("answer = " + answer);
		System.out.println();
	}
	}
	
	
	@Test public void readtest12() throws SQLException {
	stmt=dbConn.createStatement();
	ResultSet rs = stmt.executeQuery("SELECT answer FROM users WHERE question = 'how is the weather'");
	
	while(rs.next()) {
		
		String answer = rs.getString("answer");

		assertEquals(answer,"okay");
		System.out.println("answer = " + answer);
		System.out.println();
	}
	
	rs.close();
	stmt.close();
	dbConn.close();
	}
}


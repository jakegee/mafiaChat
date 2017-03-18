package Server;

import java.awt.RenderingHints.Key;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

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
	
	public String hashing(String s) {
		int hash = 7;
		for (int i = 0; i < s.length(); i++) {
			hash = hash*31 + s.charAt(i);
		}
		return String.valueOf(hash);
	}
	public void registerUser(String username, String password, String securityQuestion, String questionAnswer)
			throws UserExistsException {

		try {
			PreparedStatement sameUser = dbConn.prepareStatement("SELECT username FROM users WHERE username LIKE ?");
			sameUser.setString(1, username);
			ResultSet rs = sameUser.executeQuery();
			if (rs.next()) {
				throw new UserExistsException(username);
			}
			
			createUser = dbConn.prepareStatement("INSERT INTO users (username, password, question, answer) VALUES (?,?,?,?)");

			createUser.setString(1, username);
			createUser.setString(2, password);
			createUser.setString(3, securityQuestion);
			createUser.setString(4, questionAnswer);
			createUser.executeUpdate();


		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}
	


	@Override
	public void loginUser(String username, String password) throws InvalidUserException, InvalidInformationException {

		try {
			PreparedStatement userLogin = dbConn
					.prepareStatement("SELECT password FROM users WHERE username = ?");
			userLogin.setString(1, username);
			ResultSet rs = userLogin.executeQuery();
			if (rs.next()) {
				String userPassword = rs.getString("password");
				
				if (!password.equals(userPassword)) {
					throw new InvalidInformationException("password");
				}

			} else {
				throw new InvalidUserException(username);
			}

		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	

	@Override
	public String getSecurityQuestion(String username) throws InvalidUserException {
		try {
			PreparedStatement getQuestion = dbConn
					.prepareStatement("SELECT question FROM users WHERE username = ?");
			getQuestion.setString(1, username);
			ResultSet rs = getQuestion.executeQuery();
			if (rs.next()) {
				String question = rs.getString("question");
				return question;
			} else {
					throw new InvalidUserException("username");
			}

		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return null;
	}

	@Override
	public String checkQuestionAnswer(String username, String hintAnswer)
			throws InvalidUserException, InvalidInformationException {
		
		try {
			PreparedStatement getPassword = dbConn
					.prepareStatement("SELECT password, answer FROM users WHERE username = ?");
			getPassword.setString(1, username);
			ResultSet rs = getPassword.executeQuery();
			
			if (rs.next()) {
				String secretAnswer = rs.getString("answer");
				String password = rs.getString("password");
				if (!hintAnswer.equals(secretAnswer)) {
					throw new InvalidInformationException("answer");
				} 
				return password;
			} else {
				throw new InvalidUserException(username);
			}
				
		} catch (SQLException ex) {
			ex.printStackTrace();
		}		
		return null;
	}


	public static void main(String[] args) {
		System.out.println("HELLO!!!!!");
	}
}
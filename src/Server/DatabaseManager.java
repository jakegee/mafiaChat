package Server;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.crypto.KeyGenerator;

import exceptions.InvalidInformationException;
import exceptions.InvalidUserException;
import exceptions.UserExistsException;
import systemInterfaces.IDatabase;

/**
 * DatabaseManager class contains the code required for querying and inserting
 * user information. The constructor contains the code for connecting to the
 * PostgreSQL database. The class contains a number of functions that are
 * essential for handling the user data.
 * 
 * @author Team Nice
 * @version 20-03-2017
 */
public class DatabaseManager implements IDatabase {

	private String dbName;
	private Connection dbConn;
	private PreparedStatement createUser;

	/**
	 * Constructor connects to the database and handles exceptions.
	 */
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

		try {
			KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");

		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			// md.update()
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Function for registering new users. Use of prepared statements queries
	 * the usernames from the table to check if one exists already. If it does
	 * then a username exception will be thrown. Otherwise, the under input will
	 * be inserted into the table as username, password, question, and answer
	 * respectively.
	 * 
	 * @param username
	 * User input for username
	 * @param password
	 * User input for password
	 * @param securityQuestion
	 * User input for security question to retrieve password
	 * @param questionAnswer
	 * User input for the answer to the security question to retrieve
	 * password
	 */
	public void registerUser(String username, String password, String securityQuestion, String questionAnswer)
			throws UserExistsException {

		try {
			PreparedStatement sameUser = dbConn.prepareStatement("SELECT username FROM users WHERE username LIKE ?");
			sameUser.setString(1, username);
			ResultSet rs = sameUser.executeQuery();
			if (rs.next()) {
				throw new UserExistsException(username);
			}

			createUser = dbConn
					.prepareStatement("INSERT INTO users (username, password, question, answer) VALUES (?,?,?,?)");

			createUser.setString(1, username);
			createUser.setString(2, password);
			createUser.setString(3, securityQuestion);
			createUser.setString(4, questionAnswer);
			createUser.executeUpdate();

		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Function loginUser will first check for a password in the database
	 * through a prepared statement. If the password matches the entered
	 * username the user can login, otherwise if the password does not match an
	 * exception is handled as well as when the username doesn't exist in the
	 * database.
	 * 
	 * @param username
	 * User input for username
	 * @param password
	 * User input for password
	 */
	@Override
	public void loginUser(String username, String password) throws InvalidUserException, InvalidInformationException {

		try {
			PreparedStatement userLogin = dbConn.prepareStatement("SELECT password FROM users WHERE username = ?");
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

	/**
	 * Function getSecurityQuestion will query the stored question in the
	 * database based on the entered username. Invalid usernames will be handled
	 * with an exception error.
	 * 
	 * @param username
	 * User input for username
	 */
	@Override
	public String getSecurityQuestion(String username) throws InvalidUserException {
		try {
			PreparedStatement getQuestion = dbConn.prepareStatement("SELECT question FROM users WHERE username = ?");
			getQuestion.setString(1, username);
			ResultSet rs = getQuestion.executeQuery();
			if (rs.next()) {
				String question = rs.getString("question");
				return question;
			} else {
				throw new InvalidUserException(username);
			}

		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return null;
	}

	/**
	 * Function checkQuestionAnswer uses a parepared statement to query the
	 * password and answer from the users table using the username inputed. The
	 * result checks to see if the answer entered is what is stored in the
	 * database before returning the password. Otherwise, exceptions are thrown
	 * for invalid answers or usernames.
	 * 
	 * @param username
	 *            User input for username
	 * @param hintAnswer
	 *            Data stored for password retrieval
	 */
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
}
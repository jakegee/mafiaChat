package Stubs;

import java.util.ArrayList;

import exceptions.InvalidInformationException;
import exceptions.InvalidUserException;
import exceptions.UserExistsException;
import systemInterfaces.IDatabase;

/**
 * DatabaseStub is a simple implementation of IDatabase
 * to allow testing the interactions with the Database.
 * 
 * @author Team Nice
 * @version 18-03-2017
 */
public class DatabaseStub implements IDatabase{
	private ArrayList<String[]> users;
	
	/**
	 * Constructor for instantiating an object of the DatabaseStub
	 * class
	 */
	public DatabaseStub() {
		users = new ArrayList<String[]>();
		users.add(new String[]{"testUser1", "abc", "the answer is password", "password"});
		users.add(new String[]{"testUser2", "def", "the answer is testhint", "testhint"});
		users.add(new String[]{"testUser3", "123", "the answer is other", "other"});
	}
	
	/**
	 * Function for registering a new user, checks whether the username
	 * is taken or not
	 * 
	 * @param username String representing a username to be added to the system
	 * @param password String representing the password associated with the 
	 * username
	 * @param securityQuestion String representing the security question associated with
	 * the user
	 * @throws UserExistsException Thrown if the username is already taken and therefore
	 * the user cannot be added to the system with the passed in username
	 */
	@Override
	public void registerUser(String username, String password, String securityQuestion, String questionAnswer)
			throws UserExistsException {
		for (String[] element : users) {
			if (element[0].equals(username)) {
				throw new UserExistsException(username);
			}
		}
		String[] newUser = {username, password, securityQuestion,questionAnswer};
		users.add(newUser);
	}

	/**
	 * Function for logging in a user, checks whether the username and password are
	 * a valid combination
	 * 
	 * @param username String representing a username to be added to the system
	 * @param password String representing the password associated with the 
	 * @throws InvalidUserException Thrown if the username is not a valid username
	 * within the database, contains a message which is sent back to the client
	 * @throws InvalidInformationException Thrown if the password passed in does
	 * not correspond to the username passed in
	 */
	@Override
	public void loginUser(String username, String password) throws InvalidUserException, InvalidInformationException {
		for (String[] element : users) {
			if (element[0].equals(username)) {
				if (element[1].equals(password)) {
					return;
				} else {
					throw new InvalidInformationException("password");
				}
			}
		}
		throw new InvalidUserException(username);
	}

	/**
	 * Function for returning the security question associated with a specific user
	 * specified by username
	 * 
	 * @param username The username specifying the client requesting their security
	 * question
	 * @return The security question associated with the username
	 * @throws InvalidUserException Thrown if the username is not a valid username
	 * within the database, contains a message which is sent back to the client
	 */
	@Override
	public String getSecurityQuestion(String username) throws InvalidUserException {
		for (String[] element : users) {
			if (element[0].equals(username)) {
				return element[2];
			}
		}
		throw new InvalidUserException(username);
	}

	/**
	 * Function for submitting an answer to the security question associated with
	 * a specific user specified by username
	 * 
	 * @param username The username specifying the client requesting their security
	 * question
	 * @param hintAnswer The submitted answer to the user's security question
	 * @return The password if the hintAnswer matches the hintAnswer corresponding to
	 * the user specified by username
	 * @throws InvalidUserException Thrown if the username is not a valid username
	 * within the database, contains a message which is sent back to the client
	 * @throws InvalidInformationException Thrown if the hintAnswer passed in does
	 * not correspond to the username passed in
	 */
	@Override
	public String checkQuestionAnswer(String username, String hintAnswer)
			throws InvalidUserException, InvalidInformationException {
		for (String[] element : users) {
			if (element[0].equals(username)) {
				if (element[3].equals(hintAnswer)) {
					return element[1];
				} else {
					throw new InvalidInformationException("answer");
				}
			}
		}
		throw new InvalidUserException(username);
	}

}

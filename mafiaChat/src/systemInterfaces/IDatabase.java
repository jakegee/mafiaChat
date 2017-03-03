package systemInterfaces;

import exceptions.InvalidInformationException;
import exceptions.InvalidUserException;
import exceptions.UserExistsException;

/**
 * IDatabase Interface for use by a Server, specifying what functions
 * a valid database for the system should support
 * 
 * @author Team Nice
 * @version 3-03-2017
 */
public interface IDatabase {
	
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
	public void registerUser(String username, String password, String securityQuestion,
			String questionAnswer)  throws UserExistsException;
	
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
	public void loginUser(String username, String password)
			throws InvalidUserException, InvalidInformationException;
	
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
	public String getSecurityQuestion(String username) 
			throws InvalidUserException;
	
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
	public String checkQuestionAnswer(String username, String hintAnswer) 
			throws InvalidUserException, InvalidInformationException;
}

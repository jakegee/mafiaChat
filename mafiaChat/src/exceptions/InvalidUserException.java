package exceptions;

public class InvalidUserException extends Exception {
	
	public InvalidUserException(String username) {
		super("User: " + username + " is not known to the system");
	}

}

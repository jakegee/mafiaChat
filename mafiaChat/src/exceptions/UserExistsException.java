package exceptions;

public class UserExistsException extends Exception{

	public UserExistsException(String username) {
		super(username + " is already taken");
	}
	
}

package exceptions;

public class InvalidInformationException extends Exception{

	public InvalidInformationException(String type) {
		super("Invalid " + type);
	}
	
}

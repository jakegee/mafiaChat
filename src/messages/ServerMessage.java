package messages;

/**
 * ServerMessage class which encapsulates the information passed from
 * the Server to the Client while the program is running.
 * 
 * @author Team Nice
 * @version 1-03-2017
 */
public class ServerMessage {
	// Enum for specifying the intention of the message
	//
	// Success represents if a login, register or retrieve password
	// is positive, with the text field containing the password in the
	// final case, and empty otherwise
	//
	// Error represents if a login, register or retrive password command
	// failed, with the text field containing the error message
	public enum messageType { PRIVATE, PUBLIC, CHAT, SUCCESS, ERROR, ADDLIVEUSER, REMOVELIVEUSER, RULES, LOGOUT};
	
	// Type of message
	public messageType type;
	// Text of message	
	public String messageText;
	
	/**
	 * Constructor for instantiating an object of the ServerMessage
	 * class
	 * 
	 * @param type Type of message to send to the Server
	 * @param messageText message for Server
	 */
	public ServerMessage(messageType type, String messageText) {
		this.type = type;
		this.messageText = messageText;
	}
}

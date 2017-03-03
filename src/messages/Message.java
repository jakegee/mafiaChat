package messages;

/**
 * Message class which encapsulates the information passed from the Client to
 * the Server while the program is running.
 * 
 * @author Team Nice
 * @version 1-03-2017
 */
public class Message {
	// Enum for specifying the intention of the message
	public enum messageType { MESSAGE, COMMAND, LOGIN, LOGOUT, REGISTER, PASSWORDHINT};
	
	// Text of message
	public messageType type;
	public String messageText;
	
	/**
	 * Constructor for instantiating an object of the Message
	 * class
	 * 
	 * @param type Type of message to send to the Client
	 * @param messageText message for Client
	 */
	public Message(messageType type, String messageText) {
		this.type = type;
		this.messageText = messageText;
	}
}

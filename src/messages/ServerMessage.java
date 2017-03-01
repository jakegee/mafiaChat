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
	public enum messageType { PRIVATE, PUBLIC, CHAT };
	
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

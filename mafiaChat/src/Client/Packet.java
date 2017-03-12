package Client;

import messages.Message;
import messages.Message.messageType;

/**
 * this class relays messages to server.
 * @author vishnu
 *
 */
public class Packet {

	private messageType type;
	private String message;

	public Packet(messageType type, String message) {
		this.type = type;
		this.message = message;
	}

	public messageType getType() {
		return type;
	}

	public String getMessage() {
		return message;
	}

	public Message setCommandMsg(){
		for (int i=0; i<message.length(); i++){
			if (message.charAt(0) == '/'){
				return new Message(Message.messageType.COMMAND, message);
			} 	
		}
		return new Message(Message.messageType.MESSAGE, message);
		
	}
	public static Message createLoginPacket(String username, String password, String secQuestion, String ans) {
		String msg = username + " " + password + " " + secQuestion + " " + ans;
		return new Message(Message.messageType.LOGIN, msg);
	}

	public static Message createAccountPacket(String username, String password, String secQuestion, String ans) {
		String msg = username + " " + password + " " + secQuestion + " " + ans;
		return new Message(Message.messageType.REGISTER, msg);
	}

	public Message createLogoutPacket() {
		return new Message(Message.messageType.LOGOUT, null);
	}

	
	public static Message createPassHintPacket(String hint) {
		return new Message(Message.messageType.PASSWORDHINT, hint);
	}

	
}

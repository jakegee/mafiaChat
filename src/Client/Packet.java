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

	public Packet setCommandMsg(){
		for (int i=0; i<message.length(); i++){
			if (message.charAt(0) == '/'){
				return new Packet(Message.messageType.COMMAND, message);
			} 	
		}
		return new Packet(Message.messageType.MESSAGE, message);
		
	}
	public static Packet createLoginPacket(String username, String password, String secQuestion, String ans) {
		String msg = username + " " + password + " " + secQuestion + " " + ans;
		return new Packet(Message.messageType.LOGIN, msg);
	}

	public static Packet createAccountPacket(String username, String password, String secQuestion, String ans) {
		String msg = username + " " + password + " " + secQuestion + " " + ans;
		return new Packet(Message.messageType.REGISTER, msg);
	}

	public Packet createLogoutPacket() {
		return new Packet(Message.messageType.LOGOUT, null);
	}

	
	public static Packet createPassHintPacket(String hint) {
		return new Packet(Message.messageType.PASSWORDHINT, hint);
	}

	
}

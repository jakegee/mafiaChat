package Client;

import java.io.Serializable;

/**
 * this class deals with messages sent between client and server.
 * @author vishnu
 *
 */
public class Message implements Serializable{
	private MsgType type;
	private String[] data;
	
	public Message (MsgType type, String[] data){
		this.type=type;
		this.data=data;
	}

	public MsgType getType() {
		return type;
	}

	public String[] getData() {
		return data;
	}
	
	public static Message createMsg(String msg){
		return new Message(MsgType.MSG, new String[]{msg});
	}
	
	public static Message loginMsg(String username, String password){
		return new Message(MsgType.LOGIN, new String[] {username, password});
	}
	
	public static Message commMsg(String msg){
		return new Message(MsgType.COMMAND, new String[] {msg});
		
	}
	public static Message logoutMsg(){
		return new Message (MsgType.LOGOUT, null);
	}
	
	public static Message registerMsg(String username, String password){
		return new Message(MsgType.REGISTER, new String[]{username,password});
	}
	
	public static Message passhintMsg(String msg){
		return new Message(MsgType.PASSHINT, new String[] {msg});
	}
	
	
}



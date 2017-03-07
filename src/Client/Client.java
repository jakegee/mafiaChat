package Client;



import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client extends SocketOrganiser{

	private SocketOrganiser socketOrg;
	private String username;
	private ObjectOutputStream outputStream;
	public static ChatWindow chatWindow = null;
	public static Client client;
	
	public static Client createClient(String host, int port, String username) throws ConnectException{
		try{
			Socket socket = new Socket(host,port);
			Client client = new Client(socket);
			client.username = username;
			return client;
		}catch (UnknownHostException e){
			
		}catch (IOException e){
			
		}
		return null;
	}
	
	public Client(Socket socket){
		super(socket);
	}
	
	/**
	 * this method deals with accepting messages.
	 */
	@Override
	public void acceptMsg(Message msg) {
		try {
			if (msg != null) {
				switch (msg.getType()) {
				case MSG:
					println(msg.getData()[0]);
					break;
				case COMMAND:
					println(msg.getData()[0]);
				case LOGOUT:
					if(!socketOrg.isDisconnected())
						disconnect();
						break;
				case LOGIN:
					
				case REGISTER:
					
				case PASSHINT:
					
				}
			}
		} catch (Exception e) {

		}
	}

	/**
	 * this method shuts down the GUI
	 */
	@Override
	public void disconnected() {
		chatWindow.disconnected();	
	}
	
	/**
	 * this method sends messages from clients.
	 */
	@Override
	public void sendMsg(String msg){
		String message = "[" + username + "]" + msg;
		super.sendMsg(message);
		println(message);
	}
	
	public Client getClient(){
		return client;
	}
	
	public static void println( String str){
		chatWindow.println(str);
	}
	
	
	
}


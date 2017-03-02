import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import messages.Message;
import messages.ServerMessage;

/**
 * Client class for sending and receivng messages to and from the Server
 * 
 * @author Team Nice
 * @version 2-03-2017
 */
public class Client {
	private Gson cGson;
	private Socket socket;
	private DataOutputStream out;
	private DataInputStream in;
	private String hostName = "localhost";
	private int portNumber = 8000;
	
	/**
	 * Constructor for instantiating an object of the Client class
	 */
	public Client() {
		GsonBuilder builder = new GsonBuilder();
		cGson = builder.create();
		try {
			Socket socket = new Socket(hostName, portNumber);
			out = new DataOutputStream(socket.getOutputStream());
			in = new DataInputStream(socket.getInputStream());
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Function which broadcasts a passed in Message object to
	 * the server
	 * 
	 * @param message Message to be transmitted to the server
	 */
	public void sendMessageToServer(Message message) {
		try {
			out.writeUTF(cGson.toJson(message));
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Function for retrieving a ServerMessage object passed to
	 * the Client by the Server
	 * 
	 * @param jsonText String received from server to be converted
	 * into a class
	 * @return ServerMessage object corresponding to the passed in
	 * String
	 */
	public ServerMessage decodeServerMessage(String jsonText) {
		return cGson.fromJson(jsonText, ServerMessage.class);
	}
	
	
}

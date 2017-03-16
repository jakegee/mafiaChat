package Client;

import messages.*;

import GUIs.chatGame;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.awt.EventQueue;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.DefaultListModel;

/**
 * this is the client class. this class sends messages to the server and to
 * other clients
 * 
 * @author vishnu
 *
 */
public class Client {

	/**
	 * private variables of the client class
	 */
	private Gson cGson;
	private Socket socket;
	private DataOutputStream outputStream;
	private DataInputStream inputStream;
	private String ip;
	private int port;
	private boolean online;
	private ListenerThread listener;
	

	/**
	 * this is the constructor for instantiating the instance of the client
	 * class
	 * 
	 * @param port
	 *            port of the server
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	@SuppressWarnings("resource")
	public Client(int port, String ip) throws UnknownHostException, IOException {
		GsonBuilder builder = new GsonBuilder();
		cGson = builder.create();

		Socket socket = new Socket(ip, port);
		inputStream = new DataInputStream(socket.getInputStream());
		outputStream = new DataOutputStream(socket.getOutputStream());

	}
	
	public class ListenerThread extends Thread {
		private chatGame window;
		
		public ListenerThread(chatGame window) {
			this.window = window;
		}
		
		public void printMessage(ServerMessage message) {
			DefaultListModel<String> model = (DefaultListModel<String>) window.listChat.getModel();
			model.addElement(message.messageText);
			//window.txtEnterMess.setText(message.messageText);
		}
		
		/**
		 * This method prints messages from server
		 * 
		 * @param message
		 *            from server
		 */
		public void serverMsgPrint(ServerMessage message) {
			window.txtServerMess.setText(message.messageText);
		}
		
		/**
		 * this method initializes reading the input stream from the server upon
		 * connecting.
		 */
		public void run() {
			try {
				while (true) {

					ServerMessage message = getResponse();
					System.out.println(message.messageText);

					switch (message.type) {

					case PRIVATE:
						serverMsgPrint(message);
						break;

					case PUBLIC:
						printMessage(message);
						break;

					case CHAT:
						printMessage(message);
						break;

					default:

						break;

					}
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

	public void spawnListenerThread(chatGame window) {
		this.listener = new ListenerThread(window);
		listener.start();
	}
	
	public void killListenerThread() {
		if (this.listener != null) {
			listener.interrupt();
		}
	}

	/**
	 * this method returns messages from the server.
	 * 
	 * @return
	 */
	public ServerMessage getResponse() {
		try {
			return decodeServerMessage(inputStream.readUTF());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * boolean true if online
	 * 
	 * @return boolean true if online
	 */
	public boolean getOnline() {
		return this.online;
	}

	/**
	 * This method sends out the client message.
	 * 
	 * @param jsonText
	 */
	public void sendClientMessage(String jsonText) {
		//if (this.online == true) {
			try {
				outputStream.writeUTF(jsonText);
				outputStream.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		//}

	}

	public ServerMessage decodeServerMessage(String jsonText) {
		return cGson.fromJson(jsonText, ServerMessage.class);
	}

	public ServerMessage createLoginPacket(String username, String password) throws IOException {
		String msg = username + "/" + password;

		String jsonText = cGson.toJson(new Message(Message.messageType.LOGIN, msg));

		outputStream.writeUTF(jsonText);
		outputStream.flush();

		ServerMessage response = this.getResponse();
		return response;

	}

	public ServerMessage createAccountPacket(String username, String password, String secQuestion, String ans) {
		String msg = username + "/" + password + "/" + secQuestion + "/" + ans;

		String jsonText = cGson.toJson(new Message(Message.messageType.REGISTER, msg));
		try {
			outputStream.writeUTF(jsonText);
			outputStream.flush();

			ServerMessage response = this.getResponse();
			return response;

		} catch (IOException ie) {
			return null;
		}
	}

	public void createLogoutPacket() throws IOException {
		String jsonText = cGson.toJson(new Message(Message.messageType.LOGOUT, null));
		sendClientMessage(jsonText);
		socket.close();
	}

	public ServerMessage forgottenPassword(String username, String answer) {
		String msg;
		if (answer == null) {
			msg = username;
		} else {
			msg = username + "/" + answer;
		}
		
		String jsonText = cGson.toJson(new Message(Message.messageType.PASSWORDHINT, msg));

		try {
			outputStream.writeUTF(jsonText);
			outputStream.flush();

			return this.getResponse();
			// print out on gui

		} catch (IOException ie) {

		}
		return null;
	}

	public void setCommandMsg(String message) {
		if (message == "" || message == null) {
			return;
		}
		if (message.charAt(0) == '/') {
			String jsonText = cGson.toJson(new Message(Message.messageType.COMMAND, message));
			sendClientMessage(jsonText);
		} else {
			String jsonText = cGson.toJson(new Message(Message.messageType.MESSAGE, message));
			sendClientMessage(jsonText);
		}

	}

	

}
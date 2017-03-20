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
import javax.swing.JOptionPane;

/**
 * this is the client class. this class sends messages to the server and to
 * other clients
 * 
 * @author nice
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

	/**
	 * this is the subclass ListenerThread which constantly reads input from server.
	 * @author nice
	 *
	 */
	public class ListenerThread extends Thread {
		private chatGame window;
		private DefaultListModel<String> modelMessage;
		private DefaultListModel<String> modelUser;
		private DefaultListModel<String> modelPrivate;

		/**
		 * this is the constructor of the class which uses all four variables as parameters.
		 * @param window
		 */
		public ListenerThread(chatGame window) {
			this.window = window;
			modelMessage = (DefaultListModel<String>) window.listChat.getModel();
			modelUser = (DefaultListModel<String>) window.listUsers.getModel();
			modelPrivate = (DefaultListModel<String>) window.listPrivate.getModel();
		}

		/**
		 * this method adds the list of users to the online users window.
		 * 
		 * @param message
		 *            the message containing the username.
		 */
		public void addUser(ServerMessage message) {
			modelUser.addElement(message.messageText);
		}

		/**
		 * this method removes username from lists of users if disconnected.
		 * 
		 * @param message
		 *            containing username that is to be removed.
		 */
		public void removeUser(ServerMessage message) {
			modelUser.removeElement(message.messageText);
		}

		/**
		 * this method prints the message on the GUI>
		 * 
		 * @param message
		 *            the message that is to be printed on GUI
		 */
		public void printMessage(ServerMessage message) {
			modelMessage.addElement(message.messageText);
		}

		/**
		 * This method prints messages from server onto the GUI
		 * 
		 * @param message
		 *            from server
		 */
		public void serverMsgPrint(ServerMessage message) {
			modelPrivate.addElement(message.messageText);
		}
		
		public void createDialogBox(ServerMessage message) {
			JOptionPane.showMessageDialog(null, message.messageText);
		}

		/**
		 * this method initializes reading the input stream from the server upon
		 * connecting. it gets the response from the server and separates the
		 * messages based on the type set by the ServerMessage class.
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

					case ADDLIVEUSER:
						addUser(message);
						break;

					case REMOVELIVEUSER:
						removeUser(message);
						break;
					
					case RULES:
						createDialogBox(message);
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

	/**
	 * this method initializes the ListenerThread class.
	 * @param window
	 */
	public void spawnListenerThread(chatGame window) {
		this.listener = new ListenerThread(window);
		listener.start();
	}

	/**
	 * this method interrupts the listener thread if it stops receiving 
	 */
	public void killListenerThread() {
		if (this.listener != null) {
			listener.interrupt();
		}
	}

	/**
	 * this method returns messages from the server.
	 * 
	 * @return responses from the  server
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
	 * This method sends out the client message.
	 * 
	 * @param jsonText
	 */
	public void sendClientMessage(String jsonText) {
		
		try {
			outputStream.writeUTF(jsonText);
			outputStream.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}

	/**
	 * this method decodes the messages from a JSON text to Java String.
	 * 
	 * @param jsonText
	 * @return
	 */
	public ServerMessage decodeServerMessage(String jsonText) {
		return cGson.fromJson(jsonText, ServerMessage.class);
	}

	/**
	 * this method sends a login packet to the server in the form of a Json text
	 * and returns the response which is then handled in the GUI class.
	 * 
	 * @param username
	 *            the username of the user
	 * @param password
	 *            the password of the user
	 * @return the response whether it is successful or not.
	 * @throws IOException
	 */
	public ServerMessage createLoginPacket(String username, String password) throws IOException {
		String msg = username + "/" + password;

		String jsonText = cGson.toJson(new Message(Message.messageType.LOGIN, msg));

		outputStream.writeUTF(jsonText);
		outputStream.flush();

		ServerMessage response = this.getResponse();
		return response;

	}

	/**
	 * This method creates a create account packet that will be sent to the
	 * server
	 * 
	 * @param username
	 *            the username entered by the user
	 * @param password
	 *            the password entered by the user
	 * @param secQuestion
	 *            the security question entered by the user.
	 * @param ans
	 *            the answer to the security question
	 * @return the response if the registration is successful or not.
	 */
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

	/**
	 * this method creates the log out packet when the user clicks log out on
	 * the GUI
	 * 
	 * @throws IOException
	 */
	public void createLogoutPacket() throws IOException {
		String jsonText = cGson.toJson(new Message(Message.messageType.LOGOUT, null));
		sendClientMessage(jsonText);
		socket.close();
	}

	/**
	 * this method creates a forgotten password packet if the user forgets the
	 * password.
	 * 
	 * @param username
	 *            the username of the user.
	 * @param answer
	 *            the answer to the question.
	 * @return the password
	 */
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

	/**
	 * this method sets the type of messages being sent by the user, whether it
	 * is a command (starts with "/") or a normal message
	 * 
	 * @param message
	 *            the message sent by the user
	 */
	public void setCommandMsg(String message) {
		if (message == null || message.isEmpty()) {
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
	
	public void getRules() {
		String jsonText = cGson.toJson(new Message(Message.messageType.RULES, null));
		
		try {
			outputStream.writeUTF(jsonText);
			outputStream.flush();
			
		} catch (IOException ie) {
		}
	}
	
	public static void main(String[] args) {
		try {
			Client client = new Client(8000, "localhost");
			System.out.println(client.forgottenPassword(" ", null).messageText);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
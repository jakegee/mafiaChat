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

/**
 * this is the client class. this class sends messages to the server and to
 * other clients
 * 
 * @author vishnu
 *
 */
public class Client implements Runnable {

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
	private chatGame window;
	

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
	public Client(int port) throws UnknownHostException, IOException {
		GsonBuilder builder = new GsonBuilder();
		cGson = builder.create();

		Socket socket = new Socket(ip, port);
		inputStream = new DataInputStream(socket.getInputStream());
		outputStream = new DataOutputStream(socket.getOutputStream());

	}

	/**
	 * this method initializes reading the input stream from the server upon
	 * connecting.
	 */
	@Override
	public void run() {
		try {
			while (true) {

				ServerMessage message = this.getResponse();
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
			this.online = false;
			e.printStackTrace();
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
	 * this method prints the message on the gui.
	 * 
	 * @param message
	 */
	public void printMessage(ServerMessage message) {
		try {
			message = decodeServerMessage(inputStream.readUTF());

			window.txtEnterMess.setText(message.messageText);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
		if (this.online == true) {
			try {
				outputStream.writeUTF(jsonText);
				outputStream.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	/**
	 * This method prints messages from server
	 * 
	 * @param message
	 *            from server
	 */
	public void serverMsgPrint(ServerMessage message) {
		try {
			message = decodeServerMessage(inputStream.readUTF());

			window.txtServerMess.setText(message.messageText);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public ServerMessage decodeServerMessage(String jsonText) {
		return cGson.fromJson(jsonText, ServerMessage.class);
	}

	public ServerMessage createLoginPacket(String username, String password) throws IOException {
		String msg = username + "/" + password;
//		window.txtUsername.setText(username);
//		window.txtPassword.setText(password);
		String jsonText = cGson.toJson(new Message(Message.messageType.LOGIN, msg));

		outputStream.writeUTF(jsonText);
		outputStream.flush();

		ServerMessage response = this.getResponse();
		if (response.type.equals("SUCCESS")) {
			System.out.println("Sign-in successful");
//			window.btnLogIn.setVisible(false);
//			window.Game.setVisible(true);
		} else if (response.type.equals("ERROR")) {
			System.out.println("Try again loser");
		}
		return response;

	}

	public ServerMessage createAccountPacket(String username, String password, String secQuestion, String ans) {
		String msg = username + "/" + password + "/" + secQuestion + "/" + ans;
//		window.textUsername.setText(username);
//		window.textPassword.setText(password);
//		window.txtSecurityQ.setText(secQuestion);
//		window.txtSecurityA.setText(ans);
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

	public void forgottenPassword(String username, String answer) {
		String msg = username;
		String jsonText = cGson.toJson(new Message(Message.messageType.PASSWORDHINT, msg));
		window.usernameEntry.setText(username);

		try {
			outputStream.writeUTF(jsonText);
			outputStream.flush();

			ServerMessage response = this.getResponse();
			// print out on gui

			String request = cGson.toJson(new Message(Message.messageType.PASSWORDHINT, answer));
			try {
				outputStream.writeUTF(request);
				outputStream.flush();
				if (this.getResponse().type.equals("SUCCESS")) {

					window.password.createDialog(response.messageText);
				} else if (this.getResponse().type.equals("ERROR")) {
					window.password.createDialog("Sorry question and answer don't match");
				}

			} catch (IOException ie) {

			}

		} catch (IOException ie) {

		}
	}

	public void setCommandMsg(String message) {
		for (int i = 0; i < message.length(); i++) {
			if (message.charAt(0) == '/') {
				String jsonText = cGson.toJson(new Message(Message.messageType.COMMAND, message));
				sendClientMessage(jsonText);
				window.txtEnterMess1.setText(message);
			} else {
				String jsonText = cGson.toJson(new Message(Message.messageType.MESSAGE, message));
				sendClientMessage(jsonText);
				window.txtEnterMess1.setText(message);
			}
		}

	}

	

}
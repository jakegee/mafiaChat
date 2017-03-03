import systemInterfaces.IGame;
import systemInterfaces.IServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.IntStream;

import messages.Message;
import messages.ServerMessage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Server class encapsulating the code required for the server side
 * of the system. The constructor contains the code which manages connections.
 * Connections are managed by the ClientHandler inner class which extends Threads.
 * The class then contains a suite of functions for interactions with the
 * CLientHandler Threads
 * 
 * @author Team Nice
 * @version 1-03-2017
 *
 */
public class Server implements IServer{

	private IGame game;
	private ServerSocket sSocket;
	private int port;
	private Gson sGson;
	private ClientHandler[] threads;
	private LinkedBlockingQueue<Socket> connections;
	private boolean relayChat;
	
	/**
	 * Constructor for instantiating an instance of the Server class
	 * 
	 * @param port Port on which the server will listen for incoming
	 * connection requests
	 * @param maxServerSize Number of simultaneous clients the Server
	 * will be able to support
	 */
	public Server(int port, int maxServerSize) {
		threads = new ClientHandler[maxServerSize];
		GsonBuilder builder = new GsonBuilder();
		sGson = builder.create();
		connections = new LinkedBlockingQueue<Socket>(5);
		
		for (int i = 0; i < threads.length; i++) {
			threads[i] = new ClientHandler(i, this);
			threads[i].start();
		}
		
		try {
			sSocket = new ServerSocket(port);
			System.out.println("Awaiting Connections");
			while (true) {
				Socket socket = sSocket.accept();
				System.out.println("Connection Found");
				try {
					connections.put(socket);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Inner class extending thread which populates the thread pool used in the
	 * Server. Continously waits for sockets and then handles communication with
	 * a single client throughout the time the client is connected to the system.
	 * 
	 * @author Team Nice
	 * @version 2-03-2017
	 */
	class ClientHandler extends Thread {
		private Socket socket;
		private DataInputStream in;
		private DataOutputStream out;
		private Gson gson;
		private int idNumber;
		private Server server;
		private boolean active;
		private boolean muted = false;

		/**
		 * Constructor for instantiating an object of the ClientHandler class
		 * 
		 * @param idNumber ID of Thread
		 * @param server Reference to server object which created the class
		 * in order to call the helper methods within that class.
		 */
		public ClientHandler(int idNumber, Server server) {
			super();
			GsonBuilder builder = new GsonBuilder();
			builder.setPrettyPrinting(); 
			gson = builder.create();
			this.idNumber = idNumber;
			this.server = server;
		}
		
		/**
		 * Function for sending a ServerMessage object represented by a String
		 * in JSON form to the client
		 * 
		 * @param JSONText Json representation of a ServerMessage object
		 */
		public void sendServerMessage(String JSONText) {
			if (this.active == true) {
				try {
					out.writeUTF(JSONText);
					out.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		/**
		 * @return ID of the ClientHandler object
		 */
		public int getIDNumber() {
			return this.idNumber;
		}
		
		/** 
		 * @param muted True if the player is to be muted, false
		 * otherwise
		 */
		public void setMuted(boolean muted) {
			this.muted = muted;
		}
		
		/**
		 * Method called upon instantiation, will run continuously throughout
		 * the operation of the server. 
		 */
		@Override
		public void run() {
			try {
				while(true) {
					try {
						this.active = false;
						this.socket = server.connections.take();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		
					this.in = new DataInputStream(socket.getInputStream());
					this.out = new DataOutputStream(socket.getOutputStream());
					this.active = true;
					
					while(true) {
						String input = in.readUTF();
						Message message = gson.fromJson(input, Message.class);
						System.out.println(message.messageText);
						if (message.type == Message.messageType.MESSAGE) {
							if (!this.muted) {
								server.relayChat(message, this.idNumber);
							}
						} else if (message.type == Message.messageType.COMMAND){
							sendGameCommand(message, idNumber);
						} else if (message.type == Message.messageType.LOGIN) {
							// Login logic here
						} else if (message.type == Message.messageType.REGISTER) {
							// Register logic here
						} else if (message.type == Message.messageType.LOGOUT) {
							// Logout logic here
						} else {
							System.out.println("Invalid Message type recieved #Panic");
						}
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				this.active = false;
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Function for passing on a command received within a ClientHandler
	 * Thread to the game object.
	 * 
	 * @param message The command to be parsed by the game file
	 * @param Origin ID of the ClientHandler which passed on the command
	 */
	public void sendGameCommand(Message message, int Origin) {
		game.handleMessage(message, Origin);
	}
	
	/**
	 * Function for relaying chat to every ClientHandler
	 * 
	 * @param message Message to be transmitted
	 * @param Origin ID of the ClientHandler which passed on the command
	 */
	public void relayChat(Message message, int Origin) {
		
		// TODO: Add userName support to specify which user sent the message
		
		String JSONText = sGson.toJson(new ServerMessage(
							ServerMessage.messageType.CHAT,
							message.messageText));

		for (ClientHandler serverThread : threads) {
			serverThread.sendServerMessage(JSONText);
		}
		
	}
	
	/**
	 * Function for sending a public message to all clients
	 * 
	 * @param message String to be sent to the chat window of all
	 * clients, will be marked as being from the Server
	 */
	@Override
	public void publicMessage(String message) {
		String JSONText = sGson.toJson(new ServerMessage(
				ServerMessage.messageType.PUBLIC, message));
		for (ClientHandler serverThread : threads) {
			serverThread.sendServerMessage(JSONText);
		}
	}

	/**
	 * Function for sending a message to a single specified client
	 * 
	 * @param message String to be sent to the chat window of the
	 * client specified by recipient, the message will be marked
	 * as being from the Server
	 * @param recipient int containing id of client that will recieve
	 * the message
	 */
	@Override
	public void privateMessage(String message, int recipient) {
		String JSONText = sGson.toJson(new ServerMessage(
				ServerMessage.messageType.PRIVATE, message));
			threads[recipient].sendServerMessage(JSONText);
	}

	/**
	 * Function for sending a message to the specified clients
	 * 
	 * @param message String to be sent to the chat window of the
	 * clients specified by recipients, the message will be marked
	 * as being from the Server
	 * @param recipients int array containing the ids of users who
	 * will recieve the message
	 */
	@Override
	public void privateMessage(String message, int[] recipients) {
		String JSONText = sGson.toJson(new ServerMessage(
				ServerMessage.messageType.PRIVATE, message));
		for (int recipient : recipients) {
			threads[recipient].sendServerMessage(JSONText);
		}
	}
	
	/**
	 * Function for muting or unmuting a player specified by playerID,
	 * muting prevents any chat entered by the player from being relayed
	 * but still allows the player to see the chat. This mutes one specific
	 * player whereas
	 * 
	 * @param playerID Player to be muted
	 * @param muted True if the player will be muted, false otherwise
	 */
	@Override
	public void setPlayerMuted(int playerID, boolean muted) {
		this.threads[playerID].setMuted(muted);
	}

	/**
	 * Function for unmuting all players, resets all muted variables to false
	 * and allows every client to relay chat, as long as chat is active
	 */
	@Override
	public void unMuteAllPlayers() {
		for (ClientHandler serverThread : this.threads) {
			serverThread.setMuted(false);
		}
	}
	
	/**
	 * Function for setting whether the chat should be active or not. Active
	 * chat is relayed between users, if it is set to false then no chat will
	 * be passed between clients
	 * 
	 * @param active True if chat is to be relayed between clients, false otherwise
	 */
	@Override
	public void setChatActive(boolean active) {
		this.relayChat = active;
	}
	
	public static void main(String[] args) {
		Server server = new Server(8000, 20);
	}

}

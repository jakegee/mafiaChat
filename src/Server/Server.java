package Server;
import systemInterfaces.Game;
import systemInterfaces.IDatabase;
import systemInterfaces.IGame;
import systemInterfaces.IServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import messages.Message;
import messages.ServerMessage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import Stubs.DatabaseStub;
import Stubs.GameStub;
import Game.*;
import exceptions.InvalidInformationException;
import exceptions.InvalidUserException;
import exceptions.UserExistsException;

/**
 * Server class encapsulating the code required for the server side
 * of the system. The constructor contains the code which manages connections.
 * Connections are managed by the ClientHandler inner class which extends Threads.
 * The class then contains a suite of functions for interactions with the
 * CLientHandler Threads
 * 
 * @author Team Nice
 * @version 18-03-2017
 *
 */
public class Server implements IServer{

	private Game game;
	private IDatabase database;
	
	private ServerSocket sSocket;
	private int port;
	private Gson sGson;
	private ClientHandler[] threads;
	private LinkedBlockingQueue<Socket> connections;
	private boolean relayChat;
	private ArrayList<String> currentUsers;
	private DateFormat df;
	private int maxServerSize;
	
	/**
	 * Constructor for instantiating an instance of the Server class
	 * 
	 * @param port Port on which the server will listen for incoming
	 * connection requests
	 * @param maxServerSize Number of simultaneous clients the Server
	 * will be able to support
	 */
	public Server(int port, int maxServerSize) {
		this.threads = new ClientHandler[maxServerSize];
		GsonBuilder builder = new GsonBuilder();
		this.sGson = builder.create();
		this.connections = new LinkedBlockingQueue<Socket>(5);
		this.game = new GameStub(this);
		this.database = new DatabaseManager();
		this.currentUsers = new ArrayList<String>();
		this.df = new SimpleDateFormat("HH:mm:ss");
		this.port = port;
		this.maxServerSize = maxServerSize;
	}
	
	public void startServerListening() {
	
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
			System.out.println("Invalid Port Number, Please Retry");
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
		private String username;
		private DateFormat df;
		private boolean inChat;

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
			this.gson = builder.create();
			this.idNumber = idNumber;
			this.server = server;
			this.username = null;
			this.df = new SimpleDateFormat("HH:mm:ss");
			this.inChat = false;
			this.active = false;
		}
		
		/**
		 * Function for sending a ServerMessage object represented by a String
		 * in JSON form to the client
		 * 
		 * @param JSONText Json representation of a ServerMessage object
		 */
		public synchronized void sendServerMessage(String JSONText) {
			if (this.active == true && this.inChat == true) {
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
		 * Function for sending a ServerMessage object represented by a String
		 * in JSON form to the client, even if the client's inChat variable
		 * is false
		 * 
		 * @param JSONText Json representation of a ServerMessage object
		 */
		public synchronized void sendLoginMessage(String JSONText) { 
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
		 * @return True if the thread has a client attached
		 * false otherwise
		 */
		public synchronized boolean getActive() {
			return this.active;
		}
		
		/**
		 * @param active Set the active variable within the thread
		 */
		public synchronized void setActive(boolean active) {
			this.active = active;
		}
		
		/**
		 * @return username attached to the client handler
		 */
		public synchronized String getUsername() {
			return this.username;
		}
		
		public synchronized void setUsername(String username) {
			this.username = username;
		}
		
		/**
		 * Function for checking if a passed in username is 
		 * equivalent to the username associated with the client
		 * 
		 * @param username
		 * @return
		 */
		public synchronized boolean isUsernameEqualTo(String username) {
			if (!active) {
				return false;
			}
			return username.equals(this.username);
		}
		
		/** 
		 * @param muted True if the player is to be muted, false
		 * otherwise
		 */
		public synchronized void setMuted(boolean muted) {
			this.muted = muted;
		}
		
		/**
		 * @return if the player is muted or not
		 */
		public synchronized boolean getMuted() {
			return this.muted;
		}
		
		/**
		 * @return If the player is currently in chat
		 */
		public synchronized boolean getInChat() {
			return this.inChat;
		}
		
		/**
		 * @param inChat set if the player is in chat or not
		 */
		public synchronized void setInChat(boolean inChat) {
			this.inChat = inChat;
		}
		
		/**
		 * Method called upon instantiation, will run continuously throughout
		 * the operation of the server. 
		 */
		@Override
		public void run() {
				while(true) {
					try {
						this.setActive(false);
						this.socket = server.connections.take();
		
					this.in = new DataInputStream(socket.getInputStream());
					this.out = new DataOutputStream(socket.getOutputStream());
					this.setActive(true);
					this.setInChat(false);
					String[] decode;
					
					while(this.active == true) {
						String input = in.readUTF();
						Message message = gson.fromJson(input, Message.class);
						System.out.println("New Message" + message.messageText);
						
						switch (message.type) {
				
							case MESSAGE : 
								if (!this.getMuted()) {
									String JSONText = sGson.toJson(new ServerMessage(
											ServerMessage.messageType.CHAT,
											df.format(new Date()) + " " +
											"<" + username + "> " + message.messageText));
									this.server.relayChat(JSONText);
								}
								break;
								
							case COMMAND : 
								sendGameCommand(message, idNumber);
								break;
								
							case LOGIN :
								System.out.println("Server Login Code executed");
								decode = message.messageText.split("/");
								try {
									if (server.currentUsers.contains(decode[0])) {
										sendLoginMessage(gson.toJson(new ServerMessage(
												ServerMessage.messageType.ERROR, "User already logged in")));
										continue;
									}
									database.loginUser(decode[0], decode[1]);
									this.setUsername(decode[0]);
									System.out.println(gson.toJson(new ServerMessage(
											ServerMessage.messageType.SUCCESS, "Welcome " + this.username)));
									sendLoginMessage(gson.toJson(new ServerMessage(
											ServerMessage.messageType.SUCCESS, "Welcome " + this.username)));
									
									this.setInChat(true);
									String JSONText = sGson.toJson(new ServerMessage(
											ServerMessage.messageType.ADDLIVEUSER,
											this.username));
									server.relayChat(JSONText);
									game.handleLogin(idNumber);
									
									for (String element : currentUsers) {
										JSONText = sGson.toJson(new ServerMessage(
												ServerMessage.messageType.ADDLIVEUSER,
												element));
										sendServerMessage(JSONText);
									}
									server.currentUsers.add(this.username);
									
								} catch (InvalidUserException e) {
									sendLoginMessage(gson.toJson(new ServerMessage(
											ServerMessage.messageType.ERROR, e.getMessage())));
								} catch (InvalidInformationException e) {
									sendLoginMessage(gson.toJson(new ServerMessage(
											ServerMessage.messageType.ERROR, e.getMessage())));
								} catch (ArrayIndexOutOfBoundsException e) {
									sendLoginMessage(gson.toJson(new ServerMessage(
											ServerMessage.messageType.ERROR, "All fields must contain text")));
								}
								break;
								
							case REGISTER :
								decode = message.messageText.split("/");
								try {
									database.registerUser(decode[0], decode[1], decode[2], decode[3]);
									sendLoginMessage(gson.toJson(new ServerMessage(
											ServerMessage.messageType.SUCCESS, "Register Successful")));
								} catch (UserExistsException e) {
									sendLoginMessage(gson.toJson(new ServerMessage(
											ServerMessage.messageType.ERROR, e.getMessage())));
								} catch (ArrayIndexOutOfBoundsException e) {
									sendLoginMessage(gson.toJson(new ServerMessage(
											ServerMessage.messageType.ERROR, "All fields must contain text")));
								}
								break;
								 
							case LOGOUT :
								String JSONText = sGson.toJson(new ServerMessage(
										ServerMessage.messageType.REMOVELIVEUSER,
										this.username));
								server.relayChat(JSONText);
								server.currentUsers.remove(this.getUsername());
								sendLoginMessage(gson.toJson(new ServerMessage(
										ServerMessage.messageType.LOGOUT, "")));
								this.setInChat(false);
								game.handleLogout(idNumber);
								this.setUsername(null);
								break;
								
							case PASSWORDHINT :
								decode = message.messageText.split("/");
								if (decode.length == 1) {
									System.out.println("Executing Password Hint Retrieval");
									try {
										sendLoginMessage(gson.toJson(new ServerMessage(
												ServerMessage.messageType.SUCCESS, database.getSecurityQuestion(decode[0]))));
									} catch (InvalidUserException e) {
										sendLoginMessage(gson.toJson(new ServerMessage(
												ServerMessage.messageType.ERROR, e.getMessage())));
									} catch (ArrayIndexOutOfBoundsException e) {
										sendLoginMessage(gson.toJson(new ServerMessage(
												ServerMessage.messageType.ERROR, "All fields must contain text")));
									}
								} else {
									System.out.println("Executing Security Question Guess");
									try {
										sendLoginMessage(gson.toJson(new ServerMessage(
												ServerMessage.messageType.SUCCESS, "Password: " + database.checkQuestionAnswer(
														decode[0], decode[1]))));
									} catch (InvalidUserException e) {
										sendLoginMessage(gson.toJson(new ServerMessage(
												ServerMessage.messageType.ERROR, e.getMessage())));
									} catch (InvalidInformationException e) {
										sendLoginMessage(gson.toJson(new ServerMessage(
												ServerMessage.messageType.ERROR, e.getMessage())));
									} catch (ArrayIndexOutOfBoundsException e) {
										sendLoginMessage(gson.toJson(new ServerMessage(
												ServerMessage.messageType.ERROR, "All fields must contain text")));
									}
								}
								break;
							
							case RULES :
								sendServerMessage(gson.toJson(new ServerMessage(
										ServerMessage.messageType.RULES, game.getRules())));
							
							default : 
								System.out.println("Invalid Message type recieved #Panic");
								break;
								
							}
						}
					
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						if (this.getUsername() != null) {
							server.currentUsers.remove(this.getUsername());
							String JSONText = sGson.toJson(new ServerMessage(
									ServerMessage.messageType.REMOVELIVEUSER,
									this.username));
							server.relayChat(JSONText);
							this.setActive(false);
							game.handleLogout(idNumber);
							this.setUsername(null);
						}
					}
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
	public synchronized void relayChat(String JSONText) {
		
		// TODO: Add userName support to specify which user sent the message

		for (ClientHandler serverThread : threads) {
			serverThread.sendServerMessage(JSONText);
		}
		
	}
	
	/**
	 * Function for sending a public message to all clients
	 * 
	 * @param message String to be sent to the chat window of all
	 * clients, will be marked as being from the Server
	 * 
	 * 											df.format(new Date()) + " " +
											"<" + username + "> " + message.messageText));
	 * 
	 */
	@Override
	public void publicMessage(String message) {
		String JSONText = sGson.toJson(new ServerMessage(
				ServerMessage.messageType.PUBLIC, df.format(new Date()) + " Server : " + message));
		relayChat(JSONText);
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
				ServerMessage.messageType.PRIVATE, df.format(new Date()) + " Private : " + message));
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
				ServerMessage.messageType.PRIVATE, df.format(new Date()) + " Private : " + message));
		for (int recipient : recipients) {
			threads[recipient].sendServerMessage(JSONText);
		}
	}
	
	 /**
	 * Function for sending a message to the specified clients
	 * 
	 * @param message String to be sent to the chat window of the
	 * clients specified by recipients, the message will be marked
	 * as being from the Server
	 * @param recipients Integer List containing the ids of users
	 * who will recieve the message
	 */
	@Override
	public void privateMessage(String message, List<Integer> recipients) {
		String JSONText = sGson.toJson(new ServerMessage(
				ServerMessage.messageType.PRIVATE, df.format(new Date()) + " Private : " + message));
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

	/**
	 * Funtion for getting all the ClientIDs which are active and connected to the
	 * Server
	 * 
	 * @return ArrayList containing all active users' ids
	 */
	@Override
	public ArrayList<Integer> getActiveClientIDs() {
		ArrayList<Integer> returnList = new ArrayList<Integer>();
		for (int i  = 0; i < threads.length; i++) {
			if (threads[i].inChat) {
				returnList.add(i);
			}
		}
		return returnList;
	}

	/**
	 * Function for returning the Username associated with a specific id,
	 * currently returns a String message if the search fails, may be changed
	 * to IllegalArgumentException if that is preferred
	 * 
	 * @param ID id to get the username from
	 * @return String containing the Username associated with the passed in id,
	 * or an error message if the client associated with the ID is not active or does
	 * not exist
	 */
	@Override
	public String getUsername(int ID) {
		try {
			return threads[ID].getUsername();
		} catch (NullPointerException e) {
			return "Username Not Set in Thread -- Thread may not be active";
		} catch (ArrayIndexOutOfBoundsException e) {
			return "ID specified is higher than the number of threads running on the Server";
		}
	}

	/**
	 * Function for returning the ID associated with a specific username. Returns -1
	 * if no users were found with the passed in username
	 * 
	 * @param username String containing the username to get the associated client of
	 * @return int representing the id of the client with the username specifed in the
	 * username String argument
	 */
	@Override
	public int getUserID(String username) {
		for (int i = 0; i < threads.length; i++ ) {
			if (threads[i].isUsernameEqualTo(username)) {
				return i;
			}
		}
		return -1;
	}
	
	@Override
	public void setGameObject(String gameClassName) throws ClassNotFoundException {
		try {
			Object newGame = Class.forName(gameClassName).getConstructor(IServer.class).newInstance(this);
			game = (Game) newGame;
			System.out.println("Successfully set game to " + gameClassName);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} 
	}
	
	public static void main(String[] args) {
		String[] args_ = {"RegularChat", "8000", "20"};
		int port;
		int serverSize;
		Server server;
		if ((port = Integer.parseInt(args_[1])) == -1) {
			System.out.println("Invalid port number " + args_[1]);
		} else if ((serverSize = Integer.parseInt(args_[2])) == -1) {
			System.out.println("Invalid server size " + args_[2]);
		} else {
			if (args_[0].equals("debug")) {
				server = new Server(port, serverSize);
				server.startServerListening();
			} else {
				server = new Server(port, serverSize);
				try {
					server.setGameObject("Game." + args_[0]);
					server.startServerListening();
				} catch (ClassNotFoundException e) {
					System.out.println("Class " + args_[0] + " is not a valid Class Name");
				}
			}
		}
	}

}

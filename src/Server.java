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
 * Server class encapsulating the code required for
 * the server side of the system
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
	
	public Server(int port, int maxServerSize) {
		threads = new ClientHandler[maxServerSize];
		GsonBuilder builder = new GsonBuilder();
		builder.setPrettyPrinting(); 
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
		IntStream intStream = Arrays.stream(recipients);
		for (ClientHandler serverThread : threads) {
			if (IntStream.of(recipients).anyMatch(x -> x == serverThread.getIDNumber())) {
				serverThread.sendServerMessage(JSONText);
			}
		}
	}
	
	public void sendGameCommand(Message message, int Origin) {
		game.handleMessage(message, Origin);
	}
	
	public void relayChat(Message message) {
		String JSONText = sGson.toJson(new ServerMessage(
							ServerMessage.messageType.CHAT,
							message.messageText));

		for (ClientHandler serverThread : threads) {
			serverThread.sendServerMessage(JSONText);
		}
		
	}
	
	class ClientHandler extends Thread {
		private Socket socket;
		private DataInputStream in;
		private DataOutputStream out;
		private Gson gson;
		private int idNumber;
		private Server server;
		private boolean active;

		public ClientHandler(int idNumber, Server server) {
			GsonBuilder builder = new GsonBuilder();
			builder.setPrettyPrinting(); 
			gson = builder.create();
			this.idNumber = idNumber;
			this.server = server;
		}
		
		public void sendServerMessage(String JSONText) {
			if (active == true) {
				System.out.println("Thread " + idNumber + " Standing by");
				try {
					out.writeUTF(JSONText);
					out.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		public int getIDNumber() {
			return this.idNumber;
		}
		
		public boolean isActive() {
			return this.active;
		}
		
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
							server.relayChat(message);
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
	
	public static void main(String[] args) {
		Server server = new Server(8000, 20);
	}

}

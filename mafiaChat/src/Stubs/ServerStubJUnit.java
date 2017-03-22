package Stubs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import messages.Message;
import systemInterfaces.Game;
import systemInterfaces.IServer;

/**
 * ServerStubJunit is a Server stub to allow simple testing
 * of game objects in JUnit Testing.
 * 
 * @author Team Nice
 * @version 18-03-2017
 */
public class ServerStubJUnit implements IServer{
	private Map<Integer, String> IDToUser; 
	private Map<String, Integer> userToID; 
	private ArrayList<Integer> activeClients;
	private Game game;
	private ArrayList<String> publicMessages;
	private ArrayList<String> privateMessages;
	private ArrayList<String> mutedPlayers;
	private boolean activeChat;
	
	/**
	 * Constructor for instantiating an object of the ServerStubJUnit
	 * class
	 * 
	 * @param testUsers Strings which will act as the usernames for the
	 * clients
	 */
	public ServerStubJUnit(String[] testUsers) {
		IDToUser = new HashMap<Integer, String>();
		userToID = new HashMap<String, Integer>();
		activeClients = new ArrayList<Integer>();
		publicMessages = new ArrayList<String>();
		privateMessages = new ArrayList<String>();
		mutedPlayers = new ArrayList<String>();
		activeChat = true;
		
		for (int i = 0; i < testUsers.length; i++) {
			if (testUsers[i].contains(" ")) {
				System.out.println("Error: test usernames cannot contain space in this stub, "
						+ "setting user to spaceless");
				testUsers[i] = "spaceless";
			}
			
			IDToUser.put(i, testUsers[i]);
			userToID.put(testUsers[i], i);
			activeClients.add(i);
		}
	}
	
	/**
	 * @return String ArrayList containing all public messages which
	 * have been sent to the server
	 */
	public ArrayList<String> getPublicMessages() {
		return publicMessages;
	}
	
	/**
	 * @return String ArrayList containing all private messages which
	 * have been sent to the server
	 */
	public ArrayList<String> getPrivateMessages() {
		return privateMessages;
	}
	
	/**
	 * @return String ArrayList containing all players who have been
	 * muted
	 */
	public ArrayList<String> getMutedPlayers() {
		return mutedPlayers;
	}
	
	/**
	 * @return boolean value indicating whether chat is active or not
	 */
	public boolean getActiveChat() {
		return activeChat;
	}
	
	/**
	 * Function for sending a public message to all clients
	 * 
	 * @param message String to be sent to the chat window of all
	 * clients, will be marked as being from the Server
	 */
	@Override
	public void publicMessage(String message) {
		publicMessages.add(message);
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
		privateMessages.add(IDToUser.get(recipient) + message);
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
		for (int recipient : recipients) {
			privateMessages.add(IDToUser.get(recipient) + message);
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
		if (muted) {
			mutedPlayers.add(IDToUser.get(playerID));
		} else {
			mutedPlayers.remove(IDToUser.get(playerID));
		}
	}

	/**
	 * Function for unmuting all players, resets all muted variables to false
	 * and allows every client to relay chat, as long as chat is active
	 */
	@Override
	public void unMuteAllPlayers() {
		mutedPlayers.clear();
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
		activeChat = active;
	}

	/**
	 * Funtion for getting all the ClientIDs which are active and connected to the
	 * Server
	 * 
	 * @return ArrayList containing all active users' ids
	 */
	@Override
	public ArrayList<Integer> getActiveClientIDs() {
		// TODO Auto-generated method stub
		return activeClients;
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
		return IDToUser.get(ID);
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
		if (userToID.containsKey(username)) {
			return userToID.get(username);
		} else {
			return -1;
		}
	}
	
	/**
	 * Function called to attach a game object to the ServerStub, due to
	 * how this is handled in the actual Server, this method is required to
	 * be called before the stub will respond properly
	 * 
	 * @param game Game object to be interacted with by the stub
	 */
	public void attachGameObject(Game game) {
		this.game = game;
	}
	
	/**
	 * Function for simulating the message sending a command to the
	 * attached game object
	 * 
	 * @param command Command message to be sent to the game object, as 
	 * would occur with the actual server, the message must be of the form
	 * "/command message", such as "/vote testUser2"
	 * @param user Username corresponding to the username stored within the
	 * serverStub who supposedly sent the message
	 */
	public void sendCommand(String command, String user) {
		if (command.charAt(0) != '/') {
			System.out.println("Invalid command, must start with / ");
		} else {
			if (userToID.containsKey(user)) {
				Message message = new Message(Message.messageType.COMMAND, command);
				game.handleMessage(message, userToID.get(user).intValue());
			} else {
				System.out.println("User: " + user + " is not known to the system");
			}
		}
	}
	
	/**
	 * Function for simulating the message sending a command to the
	 * attached game object
	 * 
	 * @param command Command message to be sent to the game object, as 
	 * would occur with the actual server, the message must be of the form
	 * "/command message", such as "/vote testUser2"
	 * @param user ID corresponding to the user stored within the
	 * serverStub who supposedly sent the message
	 */
	public void sendCommand(String command, int userID) {
		if (command.charAt(0) != '/') {
			System.out.println("Invalid command, must start with / ");
		} else {
			if (IDToUser.containsKey(userID)) {
				Message message = new Message(Message.messageType.COMMAND, command);
				game.handleMessage(message, userID);
			} else {
				System.out.println("User: " + userID + " is not known to the system");
			}
		}
	}
	
	@Override
	public void setGameObject(String string) throws ClassNotFoundException {
		// TODO Auto-generated method stub
	}

	@Override
	public void privateMessage(String message, List<Integer> recipients) {
		for (int recipient : recipients) {
			privateMessages.add(IDToUser.get(recipient) + message);
		}
	}
	
}

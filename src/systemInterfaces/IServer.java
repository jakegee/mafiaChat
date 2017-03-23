package systemInterfaces;

import java.util.ArrayList;
import java.util.List;

/**
 * IServer Interface for use by a Game object, specifying what every
 * server class must contain,
 * 
 * @author Team Nice
 * @version 1-03-2017
 */
public interface IServer {

	/**
	 * Function for sending a public message to all clients
	 * 
	 * @param message String to be sent to the chat window of all
	 * clients, will be marked as being from the Server
	 */
	public void publicMessage(String message);

	/**
	 * Function for sending a message to a single specified client
	 * 
	 * @param message String to be sent to the chat window of the
	 * client specified by recipient, the message will be marked
	 * as being from the Server
	 * @param recipient int containing id of client that will recieve
	 * the message
	 */
	public void privateMessage(String message, int recipient);
	
	/**
	 * Function for sending a message to the specified clients
	 * 
	 * @param message String to be sent to the chat window of the
	 * clients specified by recipients, the message will be marked
	 * as being from the Server
	 * @param recipients int array containing the ids of users who
	 * will recieve the message
	 */
	public void privateMessage(String message, int[] recipients);
	
	 /**
	 * Function for sending a message to the specified clients
	 * 
	 * @param message String to be sent to the chat window of the
	 * clients specified by recipients, the message will be marked
	 * as being from the Server
	 * @param recipients Integer List containing the ids of users
	 * who will recieve the message
	 */
	public void privateMessage(String message, List<Integer> recipients);
	
	/**
	 * Function for muting or unmuting a player specified by playerID,
	 * muting prevents any chat entered by the player from being relayed
	 * but still allows the player to see the chat. This mutes one specific
	 * player whereas
	 * 
	 * @param playerID Player to be muted
	 * @param muted True if the player will be muted, false otherwise
	 */
	public void setPlayerMuted(int playerID, boolean muted);
	
	/**
	 * Function for unmuting all players, resets all muted variables to false
	 * and allows every client to relay chat, as long as chat is active
	 */
	public void unMuteAllPlayers();
	
	/**
	 * Function for setting whether the chat should be active or not. Active
	 * chat is relayed between users, if it is set to false then no chat will
	 * be passed between clients
	 * 
	 * @param active True if chat is to be relayed between clients, false otherwise
	 */
	public void setChatActive(boolean active);
	
	/**
	 * Funtion for getting all the ClientIDs which are active and connected to the
	 * Server
	 * 
	 * @return ArrayList containing all active users' ids
	 */
	public ArrayList<Integer> getActiveClientIDs();
	
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
	public String getUsername(int ID);
	
	/**
	 * Function for returning the ID associated with a specific username. Returns -1
	 * if no users were found with the passed in username
	 * 
	 * @param username String containing the username to get the associated client of
	 * @return int representing the id of the client with the username specifed in the
	 * username String argument
	 */
	public int getUserID(String username);

	/**
	 * Function called to set the Game Object within the Server class, allows the
	 * changing of games without having to restart the server
	 * 
	 * @param string String representing a game which is known to the system and
	 * stored within the Game package
	 * @throws ClassNotFoundException Thrown if the game is not a valid classname
	 * within the system
	 */
	public void setGameObject(String classname) throws ClassNotFoundException;
	
}
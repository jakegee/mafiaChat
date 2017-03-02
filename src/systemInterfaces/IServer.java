package systemInterfaces;

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
	 * Function for muting or unmuting a player specified by playerID,
	 * muting prevents any chat entered by the player from being relayed
	 * but still allows the player to see the chat
	 * 
	 * @param playerID Player to be muted
	 * @param muted True if the player will be muted, false otherwise
	 */
	public void setPlayerMuted(int playerID, boolean muted);
}

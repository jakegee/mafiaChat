package systemInterfaces;

import messages.Message;

/**
 * IGame Interface for use by a Server, specifying what every game
 * class must contain.
 * 
 * @author Team Nice
 * @version 1-03-2017
 */
public interface IGame {
	
	/**
	 * @return boolean value which specfies whether chat should be
	 *  relayed at the current time.
	 */
	public boolean getChatRelayed();
	
	/**
	 * Function which is called by the Server whenever a command is
	 * received from a client
	 * 
	 * @param message String representing the command passed in
	 * as well as the rest of the text passed in to the client
	 * @param origin int representing the user which entered the command
	 */
	public void handleMessage (Message message, int origin);

}

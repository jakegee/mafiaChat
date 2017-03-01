import systemInterfaces.IGame;
import systemInterfaces.IServer;
import messages.Message;
import messages.ServerMessage;

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
	
	/**
	 * Function for sending a public message to all clients
	 * 
	 * @param message String to be sent to the chat window of all
	 * clients, will be marked as being from the Server
	 */
	@Override
	public void publicMessage(String message) {
		ServerMessage newMessage = new ServerMessage(
				ServerMessage.messageType.PUBLIC, message);	
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
		ServerMessage newMessage = new ServerMessage(
				ServerMessage.messageType.PRIVATE, message);
		
	}

}

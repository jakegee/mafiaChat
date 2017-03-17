package systemInterfaces;

import messages.Message;

public abstract class Game {
	protected IServer server;
	
	public Game(IServer server) {
		this.server = server;
	}
	
	/**
	 * Function which is called by the Server whenever a command is
	 * received from a client
	 * 
	 * @param message String representing the command passed in
	 * as well as the rest of the text passed in to the client
	 * @param origin int representing the user which entered the command
	 */
	public abstract void handleMessage (Message message, int origin);
	
}

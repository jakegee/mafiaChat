package Stubs;

import messages.Message;
import systemInterfaces.Game;
import systemInterfaces.IServer;

/**
 * GameStub is a simple extension of the Game class in order
 * to simplify testing
 * 
 * @author Team Nice
 * @version 18-03-2017
 */
public class GameStub extends Game {
	private boolean activeChat;
	
	/**
	 * Constructor for instantiating an object of the GameStub
	 * class
	 * 
	 * @param server Server object to be tied to the GameStub object
	 */
	public GameStub(IServer server) {
		super(server);
		activeChat = true;
	}

	/**
	 * Function which is called by the Server whenever a command is
	 * received from a client
	 * 
	 * @param message String representing the command passed in
	 * as well as the rest of the text passed in to the client
	 * @param origin int representing the user which entered the command
	 */
	@Override
	public synchronized void handleMessage(Message message, int origin) {
		String[] messageDecode = this.decodeMessage(message);
		int receiver;
		
		switch (messageDecode[0]) {
		case "/public" :
			server.publicMessage(messageDecode[1] + " sent by user " + origin);
			break;
			
		case "/private" :
			String remText = messageDecode[1];
			String messageForUser;
			String specifiedUser;
			
			int charPosition = remText.indexOf(" ");
			if (charPosition != -1) {
				messageForUser = remText.substring(charPosition + 1);
				specifiedUser = remText.substring(0, charPosition);
			} else {
				server.privateMessage(" Invalid private message (" + remText + ")", origin);
				return;
			}
			if ((receiver = server.getUserID(specifiedUser)) != -1) {
				server.privateMessage(" " + messageForUser  + " sent by user " + origin, receiver);
			} else {
				server.privateMessage(" " + "Invalid User " + specifiedUser, origin);
			}
			break;
			
		case "/mute" :
			if ((receiver = server.getUserID(messageDecode[1])) != -1) {
				server.setPlayerMuted(receiver, true);
				server.privateMessage(" Successfully muted " + messageDecode[1], origin);
			} else {
				server.privateMessage(" " + "Invalid User " + messageDecode[1], origin);
			}
			break;
		
		case "/unmute" :
			if ((receiver = server.getUserID(messageDecode[1])) != -1) {
				server.setPlayerMuted(receiver, false);
				server.privateMessage(" Successfully un-muted " + messageDecode[1], origin);
			} else {
				server.privateMessage(" " + "Invalid User " + messageDecode[1], origin);
			}
			break;
		
		case "/chat" :
			this.activeChat = !this.activeChat;
			server.setChatActive(this.activeChat);
			break;
		}
	}
	
}

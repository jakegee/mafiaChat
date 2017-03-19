package Game;

import messages.Message;
import systemInterfaces.Game;
import systemInterfaces.IServer;

public class RegularChat extends Game {

	public RegularChat(IServer server) {
		super(server);
		// TODO Auto-generated constructor stub
	}

	@Override
	public synchronized void handleMessage(Message message, int origin) {
		String[] messageDecode = this.decodeMessage(message);
		int receiver;
		
		switch (messageDecode[0]) {
		case "/public" :
			server.publicMessage( "<c> " + messageDecode[1]);
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
				server.privateMessage("Invalid private message (" + remText + ")", origin);
				return;
			}
			if ((receiver = server.getUserID(specifiedUser)) != -1) {
				server.privateMessage("<" + server.getUsername(origin)  + "> " + messageForUser, receiver);
			} else {
				server.privateMessage("Invalid User " + specifiedUser, origin);
			}
			break;
			
		/*	
		case "/game" :
			try {
				server.setGameObject("Game." + messageDecode[1]);
				server.publicMessage("Game is now set to " + messageDecode[1]);
			} catch (ClassNotFoundException e) {
				server.privateMessage("Game: " + messageDecode[1] + " is not known", origin);
			}
			break;
		*/
		
		case "/time":
			int charPos = messageDecode[1].indexOf(" ");
			String messageAfterTimer;
			String specifiedTime;
			
			if (charPos != -1) {
				messageAfterTimer = messageDecode[1].substring(charPos + 1);
				specifiedTime = messageDecode[1].substring(0, charPos);
			} else {
				server.privateMessage("Invalid time message (" + messageDecode[1] + ")", origin);
				return;
			}
			
			int delay;
			if ((delay = Integer.parseInt(specifiedTime)) != -1) {
				this.createTimerEvent(delay, messageAfterTimer);
			} else {
				server.privateMessage("Invalid submitted time " + specifiedTime, origin);
			}
			break;
		
		default:
			//server.privateMessage("Invalid command", origin);
			break;
		}
		
		super.handleMessage(message, origin);
		
	}
	
	@Override
	protected void handleTimerEvent(String message, int delay) {
		super.handleTimerEvent(message, delay);
		server.publicMessage("<t> " + message + " (Sent " + delay + " second(s) ago)");
	}
	
}

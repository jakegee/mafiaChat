package systemInterfaces;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import messages.Message;

/**
 * Abstract Game class, which is the parent class to all games which are
 * playable by the system. Contains convenience methods for use by developers
 * of the games, and a suite of functions which should be overriden to provide
 * the desired functionality within a game.
 * 
 * @author Team nice
 * @version 23-03-2017
 */
public abstract class Game {
	protected IServer server;
	protected ArrayList<Integer> users;
	private Timer timer;
	private Map<String, Integer> gameChoice;
	private Map<String, String> playerChoice;
	private int activePlayers;
    protected boolean gameInProgress = false;
    protected ArrayList<Integer> nonGameUsers;
	
	protected String rules;
	
	/**
	 * Constructor for instantiating an object of the Game class
	 * 
	 * @param server Server to attach to the Game object in order
	 * to send messages to the clients
	 */
	public Game(IServer server) {
		this.server = server;
		this.gameChoice = new HashMap<String, Integer>();
		this.playerChoice = new HashMap<String, String>();
		this.nonGameUsers = new ArrayList<Integer>();
		this.rules = "The writer of this game has not provided rules";
	}
	
	
	/**
	 * Function which is called by the Server whenever a command is
	 * received from a client
	 * 
	 * @param message String representing the command passed in
	 * as well as the rest of the text passed in to the client
	 * @param origin int representing the user which entered the command
	 */
	public void handleMessage (Message message, int origin) {
		String[] messageDecode = decodeMessage(message);
		
		switch (messageDecode[0]) {
		case "/game" :
			try {
				String user = server.getUsername(origin);
				if (messageDecode[1] != playerChoice.get(user)) {
					server.publicMessage("User: <" + user + "> voted to change game to " + messageDecode[1]);
					int currentVal;
					try {
						currentVal = gameChoice.get(messageDecode[1]);
					} catch (NullPointerException e) {
						currentVal = 0;
					}
					if (++currentVal >= Math.ceil(server.getActiveClientIDs().size()/2.0)) {
						server.setGameObject("Game." + messageDecode[1]);
						server.publicMessage("Game set to " + messageDecode[1]);
					}
					gameChoice.put(messageDecode[1], currentVal);
				} else {
					server.privateMessage("You already voted for " + messageDecode[1], origin);
				}
			} catch (ClassNotFoundException e) {
				server.publicMessage("Game: " + messageDecode[1] + " is not known");
			}
			break;
		
		case "/badgers" :
			String remText = messageDecode[1];
			String messageForUser;
			int receiver;
			if ((receiver = server.getUserID(remText)) != -1) {
				server.publicMessage("Badgers have been dispatched to take out " + messageDecode[1]);
			} else {
				server.privateMessage("Invalid User " + messageDecode[1], origin);
			}
			break;
		
		default :
			server.privateMessage("Invalid Command", origin);
			break;
		}
		
	}
	
	/**
	 * Function called when the user requests the rules of the game,
	 * default behaviour is to return the rules string
	 * 
	 * @return String containing the rules to be displayed to the
	 * user
	 */
	public String getRules() {
		return rules;
	}
	
	/**
	 * Non-blocking function for creating a Timer Event which sends the message String
	 * to the handleTimerEvent function after the secondsTillEvent specified
	 * 
	 * @param secondsTillEvent Seconds until handleTimerEvent function is called
	 * @param message Message to be sent to handleTimerEvent function
	 */
	protected void createTimerEvent(int secondsTillEvent, String message) {
		this.timer = new Timer();
		timer.schedule(new TimerEventCaller(message, secondsTillEvent), secondsTillEvent * 1000);
	}
	
	/**
	 * Function to handle the login of the user specified by origin
	 * 
	 * @param origin User which just logged in to the chat window
	 */
	public void handleLogin(int origin) {
		if (gameInProgress) {
			server.setPlayerMuted(origin, true);
			server.privateMessage("As game is in progress you have been muted ", origin);
			nonGameUsers.add(origin);
		}
	}
	
	/**
	 * Function which decodes an instance of the Message class
	 * 
	 * @param message Instance of the Message Class
	 * @return String array of size two, with the first String
	 * being the command, and the second being the rest of the message
	 */
	protected String[] decodeMessage(Message message) {
		String text = message.messageText;
		String command;
		String remText;
		int charPosition = text.indexOf(" ");
		
		if (charPosition != -1) {
			remText = text.substring(charPosition + 1);
			command = text.substring(0, charPosition);
		} else {
			command = text;
			remText = "";
		}
		return new String[]{command, remText};
		
	}
	
	/**
	 * Function called by the createTimerEvent, should be overriden
	 * however super.handleTimerEvent should be called.
	 * 
	 * @param message Message sent from createTimerEvent
	 */
	protected void handleTimerEvent(String message, int delay) {
		this.timer.cancel();
		server.publicMessage("<t> " + message + " (Sent " + delay + " second(s) ago)");
	}
	
	/**
	 * Function called by server when a user logs out of the system
	 * 
	 * @param origin ID of the user which logged out
	 */
	public void handleLogout(int origin) {
		
	}
	
	/**
	 * Inner class TimerEventCaller which extends TimerTask,
	 * handles calling of handleTimerEvent at the time specified
	 * in createTimerEvent
	 * 
	 * @author Team Nice
	 * @version 17-03-2017
	 */
	private class TimerEventCaller extends TimerTask {
		private String message;
		private int delay;
		
		/**
		 * Constructor for instantiating an object of the 
		 * TimerEventCaller class
		 * 
		 * @param message Message to send to the handleTimerEvent
		 * function
		 */
		public TimerEventCaller(String message, int delay) {
			this.message = message;
			this.delay = delay;
		}
		
		/**
		 * Function called by the thread spawned in handleTimerEvent
		 * when it is time to handle the message
		 */
		@Override
		public void run() {
			handleTimerEvent(message, delay);
		}
		
	}
	
}

package systemInterfaces;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import messages.Message;

public abstract class Game {
	protected IServer server;
	protected ArrayList<Integer> users;
	private Timer timer;
	
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
	
	/**
	 * Non-blocking function for creating a Timer Event which sends the message String
	 * to the handleTimerEvent function after the secondsTillEvent specified
	 * 
	 * @param secondsTillEvent Seconds until handleTimerEvent function is called
	 * @param message Message to be sent to handleTimerEvent function
	 */
	protected void createTimerEvent(int secondsTillEvent, String message) {
		this.timer = new Timer();
		timer.schedule(new TimerEventCaller(message), secondsTillEvent * 1000);
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
	protected void handleTimerEvent(String message) {
		this.timer.cancel();
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
		
		/**
		 * Constructor for instantiating an object of the 
		 * TimerEventCaller class
		 * 
		 * @param message Message to send to the handleTimerEvent
		 * function
		 */
		public TimerEventCaller(String message) {
			this.message = message;
		}
		
		/**
		 * Function called by the thread spawned in handleTimerEvent
		 * when it is time to handle the message
		 */
		@Override
		public void run() {
			handleTimerEvent(message);
		}
		
	}
	
}

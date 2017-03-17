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
	
	protected void createTimerEvent(int secondsTillEvent, String message) {
		this.timer = new Timer();
		timer.schedule(new TimerEventCaller(message), secondsTillEvent * 1000);
	}
	
	protected void handleTimerEvent(String message) {
		this.timer.cancel();
	}
	
	private class TimerEventCaller extends TimerTask {
		private String message;
		
		public TimerEventCaller(String message) {
			this.message = message;
		}
		
		@Override
		public void run() {
			handleTimerEvent(message);
		}
		
	}
	
}

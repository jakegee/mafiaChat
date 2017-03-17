package systemInterfaces;

import java.util.Timer;
import java.util.TimerTask;

import messages.Message;

public abstract class Game {
	protected IServer server;
	private Timer timer;
	
	public Game(IServer server) {
		this.server = server;
		this.timer = new Timer();
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
		timer.schedule(new TimerEventCaller(message), secondsTillEvent * 1000);
	}
	
	protected void createTimerEvent(int[] secondsTillEvent, String message) {
		
	}
	
	private void handlePrivateTimerEvent(String message, int timeTillFinalCall) {
		
	}
	
	protected abstract void handleTimerEvent(String message);
	
	private class privateTimer extends TimerTask {
		private String message;
		private int finalCallTime;
		
		public privateTimer(String message, int finalCallTime) {
			this.message = message;
			this.finalCallTime = finalCallTime;
		}
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			
		}
		
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

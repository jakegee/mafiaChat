package Stubs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import messages.Message;
import systemInterfaces.Game;
import systemInterfaces.IServer;

public class ServerStubCMD implements IServer{
	private Map<Integer, String> IDToUser; 
	private Map<String, Integer> userToID; 
	private ArrayList<Integer> activeClients;
	private Game game;
			
	public ServerStubCMD(String[] testUsers) {
			IDToUser = new HashMap<Integer, String>();
			userToID = new HashMap<String, Integer>();
			activeClients = new ArrayList<Integer>();
			
			for (int i = 0; i < testUsers.length; i++) {
				if (testUsers[i].contains(" ")) {
					System.out.println("Error: cannot contain space in this stub, setting user to spaceless");
					testUsers[i] = "spaceless";
				}
				
				IDToUser.put(i, testUsers[i]);
				userToID.put(testUsers[i], i);
				activeClients.add(i);
			}
	}
	
	public void attachGameObject(Game game) {
		this.game = game;
	}
	
	public void sendCommand(String command, String user) {
		if (command.charAt(0) != '/') {
			System.out.println("Invalid command, must start with / ");
		} else {
			if (userToID.containsKey(user)) {
				Message message = new Message(Message.messageType.COMMAND, command);
				game.handleMessage(message, userToID.get(user).intValue());
			} else {
				System.out.println("User: " + user + " is not known to the system");
			}
		}
	}
	
	public void sendCommand(String command, int userID) {
		if (command.charAt(0) != '/') {
			System.out.println("Invalid command, must start with / ");
		} else {
			if (IDToUser.containsKey(userID)) {
				Message message = new Message(Message.messageType.COMMAND, command);
				game.handleMessage(message, userID);
			} else {
				System.out.println("User: " + userID + " is not known to the system");
			}
		}
	}
	
	@Override
	public void publicMessage(String message) {
		System.out.println("Public Message: " + message);
		
	}

	@Override
	public void privateMessage(String message, int recipient) {
		// TODO Auto-generated method stub
		System.out.println("Private Message: " + message + " ////// sent to user " + IDToUser.get(recipient) + " ( " + recipient + " ) ");
	}

	@Override
	public void privateMessage(String message, int[] recipients) {
		for (int recipient : recipients) {
			System.out.println("Private Message: " + message + "////// sent to user " + IDToUser.get(recipient) + " ( " + recipient + " ) ");
		}
		
	}

	@Override
	public void setPlayerMuted(int playerID, boolean muted) {
		System.out.println("Player " + IDToUser.get(playerID) + " ( " + playerID + " )  has been muted");
		
	}

	@Override
	public void unMuteAllPlayers() {
		System.out.println("All players muted");
		
	}

	@Override
	public void setChatActive(boolean active) {
		System.out.println("Chat set to " + active);
		
	}

	@Override
	public ArrayList<Integer> getActiveClientIDs() {
		return activeClients;
	}

	@Override
	public String getUsername(int ID) {
		return IDToUser.get(ID);
	}

	@Override
	public int getUserID(String username) {
		if (userToID.containsKey(username)) {
			return userToID.get(username);
		} else {
			return -1;
		}
	}
	
	public void commandLineControl() {
		Scanner scanner = new Scanner(System.in);
		System.out.println("TestStub// Enter command for system in the order (User) /(command) (message) \n"
		 		+ "For Example: testUser /vote testUser2");
		String command;
		String intendedUser;
		while (true) {
			String commandString = scanner.nextLine();
			int charPosition = commandString.indexOf(" ");
			
			if (commandString.equals("exit")) {
				break;
			}
			
			if (charPosition != -1) {
				command = commandString.substring(charPosition + 1);
				intendedUser = commandString.substring(0, charPosition);
			} else {
				System.out.println("TestStub// Invalid input");
				continue;
			}
			
			try {
				this.sendCommand(command, Integer.parseInt(intendedUser));
			} catch (NumberFormatException e) {
				this.sendCommand(command, intendedUser);
			}
	 
		}
	}
	
	public void testControl() {
		
	}

}

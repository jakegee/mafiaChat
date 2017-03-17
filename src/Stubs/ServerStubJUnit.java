package Stubs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import messages.Message;
import systemInterfaces.Game;
import systemInterfaces.IServer;

public class ServerStubJUnit implements IServer{
	private Map<Integer, String> IDToUser; 
	private Map<String, Integer> userToID; 
	private ArrayList<Integer> activeClients;
	private Game game;
	private ArrayList<String> publicMessages;
	private ArrayList<String> privateMessages;
	private ArrayList<String> mutedPlayers;
	private boolean activeChat;
			
	public ServerStubJUnit(String[] testUsers) {
		IDToUser = new HashMap<Integer, String>();
		userToID = new HashMap<String, Integer>();
		activeClients = new ArrayList<Integer>();
		publicMessages = new ArrayList<String>();
		privateMessages = new ArrayList<String>();
		mutedPlayers = new ArrayList<String>();
		activeChat = true;
		
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
	
	public ArrayList<String> getPublicMessages() {
		return publicMessages;
	}
	
	public ArrayList<String> getPrivateMessages() {
		return privateMessages;
	}
	
	public ArrayList<String> getMutedPlayers() {
		return mutedPlayers;
	}
	
	public boolean getActiveChat() {
		return activeChat;
	}
	
	@Override
	public void publicMessage(String message) {
		publicMessages.add(message);
	}

	@Override
	public void privateMessage(String message, int recipient) {
		privateMessages.add(IDToUser.get(recipient) + message);
	}

	@Override
	public void privateMessage(String message, int[] recipients) {
		for (int recipient : recipients) {
			privateMessages.add(IDToUser.get(recipient) + message);
		}
	}

	@Override
	public void setPlayerMuted(int playerID, boolean muted) {
		if (muted) {
			mutedPlayers.add(IDToUser.get(playerID));
		} else {
			mutedPlayers.remove(IDToUser.get(playerID));
		}
	}

	@Override
	public void unMuteAllPlayers() {
		mutedPlayers.clear();
	}

	@Override
	public void setChatActive(boolean active) {
		activeChat = active;
	}

	@Override
	public ArrayList<Integer> getActiveClientIDs() {
		// TODO Auto-generated method stub
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
	
}

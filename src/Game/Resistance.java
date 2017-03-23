package Game;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadLocalRandom;

import Stubs.ServerStubCMD;
import messages.Message;
import systemInterfaces.Game;
import systemInterfaces.IGame;
import systemInterfaces.IServer;

/**
 * MafiaChat implementation of Don Eskridge's The Resistance, to be 
 * attached to a MafiaChat server object.
 * 
 * @author Team Mafia
 * @version 22-03-2017
 */
public class Resistance extends Game{
	
	private int numberOfSuccesses;
	private boolean activeTimer;
	
	private ArrayList<String> selectedSquad;
	private Queue<String> leaderQueue;
	private int currentLeader;
	
	private ArrayList<Integer> users;
	
	private static final Map<Integer, int[]> gameConfig;
    static
    {
        gameConfig = new HashMap<Integer, int[]>();
        gameConfig.put(5 , new int[] {3, 2, 2, 3, 2, 3, 3, 1});
        gameConfig.put(6 , new int[] {4, 2, 2, 3, 4, 3, 4, 1});
        gameConfig.put(7 , new int[] {4, 3, 2, 3, 3, 4, 4, 2});
        gameConfig.put(8 , new int[] {5, 3, 3, 4, 4, 5, 5, 2});
        gameConfig.put(9 , new int[] {6, 3, 3, 4, 4, 5, 5, 2});
        gameConfig.put(10 , new int[] {6, 4, 3, 4, 4, 5, 5, 2});
    }
    private int[] currentConfig;
	
	private static final int minPlayers = 5;
	private static final int maxPlayers = 10;
	
	private Mission mission;
	private GameState state;
	private ArrayList<Integer> voted;
	
	/**
	 * Constructor for instantiating an object of the Resistance class
	 * 
	 * @param server Server to send commands to
	 */
	public Resistance(IServer server) {
		super(server);
		state = GameState.GAMESTART;
	}
	
	private enum GameState {GAMESTART, SQUAD_SELECTION, SQUAD_VOTE, SQUAD_ATTEMPT};
	//private enum Mission {MISSION1, MISSION2, MISSION3, MISSION4, MISSION5};

	private ArrayList<Integer> spies;
	
	/**
	 * Function called to setUp the game and prepare for playing,
	 * after a start command is issued.
	 */
	public void setUpGame() {
		gameInProgress = true;
		currentConfig = gameConfig.get(users.size());
		mission = new Mission(users.size());
		state = GameState.SQUAD_SELECTION;
		voted = new ArrayList<Integer>();
		spies = new ArrayList<Integer>();
		leaderQueue = new LinkedBlockingQueue<String>(users.size());
		server.publicMessage("Welcome to the Resistance");
		
		String messageForSpies = "You are a spy, the spies are: ";
		for (int i = 0; i < currentConfig[1]; i++) {
			while(true) {
				int randomNum = ThreadLocalRandom.current().nextInt(0, users.size());
				if (!spies.contains(randomNum)) {
					spies.add(randomNum);
					messageForSpies += server.getUsername(randomNum) + " ";
					break;
				}
			}
			
		}
		for (int element : spies) {
			server.privateMessage(messageForSpies, element);
		}
		
		ArrayList<Integer> allocated = new ArrayList<Integer>();
		while(true) {
			int randomNum =  ThreadLocalRandom.current().nextInt(0, users.size());
			if (!allocated.contains(randomNum)) {
				leaderQueue.add(server.getUsername(randomNum));
				if (leaderQueue.size() == users.size()) {
					break;
				}
				allocated.add(randomNum);
			}
		}
		nextLeader();
	}
	
	/**
	 * Function called when the nextLeader is required, after a mission is completed
	 * or a vote is failed
	 */
	public void nextLeader() {
		String leader = leaderQueue.poll();
		leaderQueue.add(leader);
		currentLeader = server.getUserID(leader);
		voted = new ArrayList<Integer>();
		server.publicMessage("All hail the new Leader " + leader);
		server.publicMessage("The subsequent Leader will be " + leaderQueue.peek());
	}
	
	/**
	 * Inner class mission which encapsulates all commands which are issued
	 * 
	 * @author Team Mafia
	 * @version 21-03-2017
	 *
	 */
	public class Mission {
		private int numberOfPlayers;
		private ArrayList<Integer> selectedSquad;
		private int squadSize;
		private int missionNumber;
		private int votes;
		private int negVotes;
		private ArrayList<Boolean> missionSuccess;
		private int failedVotes;
		private int failedMissions;
		
		/**
		 * Constructor for instantiating an object of the Mission
		 * class
		 * 
		 * @param numberOfPlayers Number of players in the game
		 */
		public Mission(int numberOfPlayers ) {
			this.numberOfPlayers = numberOfPlayers;
			this.missionNumber = 1;
			this.selectedSquad = new ArrayList<Integer>();
			this.squadSize = currentConfig[missionNumber + 1];
			this.votes = 0;
			this.failedVotes = 0;
			this.failedMissions = 0;
			this.missionSuccess = new ArrayList<Boolean>();
			server.publicMessage("Current mission requires " + squadSize + " team members, "
					+ "and " + currentConfig[7] + " downvote(s) to fail");
		}
		
		/**
		 * Function called when the leader requests the addition
		 * of a new squad member to the squad
		 * 
		 * @param user User to be added to the squad
		 */
		public void addSquadMember(int user) {
			selectedSquad.add(user);
			server.publicMessage(server.getUsername(user) + " has been added to the squad");
			if (selectedSquad.size() == squadSize) {
				String message = "Submitted Team is [ ";
				for (int element : selectedSquad) {
					message += server.getUsername(element) + " ";
				}
				server.publicMessage(message + "] Cast votes now");
				state = GameState.SQUAD_VOTE;
			}
		}
		
		/**
		 * Function called when the leader requests the removal of a squad
		 * member from the squad
		 * 
		 * @param user User to be removed from the squad
		 */
		public void removeSquadMember(int user) {
			if (selectedSquad.contains(user)) {
				selectedSquad.remove(user);
				server.publicMessage(server.getUsername(user) + "has been removed from the squad");
			} else {
				server.publicMessage("Leader attempted to remove " + user + " who wasn't even in the squad");
			}
		}
		
		/**
		 * Function called when the next mission is called
		 */
		public void nextMission() {
			server.publicMessage(missionNumber + " missions have passed");
			server.publicMessage(failedMissions + " failed so far");
			this.missionNumber++;
			this.selectedSquad = new ArrayList<Integer>();
			this.squadSize = currentConfig[missionNumber + 1];
			this.missionSuccess = new ArrayList<Boolean>();
			state = GameState.SQUAD_SELECTION;
			this.votes = 0;
			this.negVotes = 0;
			server.publicMessage("Current mission requires " + squadSize + " team members, "
					+ "and " + currentConfig[7] + " downvote(s) to fail");
			nextLeader();
		}
		
		/**
		 * Function called when the mission is required to be repeated, after the squad 
		 * has been rejected
		 */
		public void repeatMission() {
			selectedSquad = new ArrayList<Integer>();
			missionSuccess = new ArrayList<Boolean>();
			state = GameState.SQUAD_SELECTION;
			this.votes = 0;
			this.negVotes = 0;
			nextLeader();
		}
		
		/**
		 * Function called when the mission is s
		 * 
		 * @param success
		 * @param origin
		 */
		public void addSubmittedSuccessFail(boolean success, int origin) {
			if (selectedSquad.contains(origin)) {
				missionSuccess.add(success);
				server.privateMessage("Vote successfully counted", origin);
				if (missionSuccess.size() == squadSize) {
					evaluateMission();
				}
			} else {
				server.privateMessage("You are not part of the current squad, vote not included", origin);
			}
		}
		
		public void squadVote(boolean vote) {
			if (vote) {
				votes++;				
			} else {
				negVotes++;
			}
			
			
			if ( votes >= Math.ceil(numberOfPlayers/2.0)) {
				state = GameState.SQUAD_ATTEMPT;
				server.publicMessage("Squad Accepted, Squad submit /vote for pass and /downvote for failure");
				voted.clear();
			} else if (negVotes >= Math.ceil(numberOfPlayers/2)) {
				failedVotes++;
				if (failedVotes >= 5) {
					server.publicMessage("5 votes failed, spies win");
					state = GameState.GAMESTART;
					gameInProgress = false;
				} else {
					server.publicMessage("Vote failed, " + failedVotes + " failed votes so far, "
							+ (5 - failedVotes) + " failed votes until spies win");
					repeatMission();
					voted.clear();
				}
			} else {
				server.publicMessage("Currently " + votes + " votes and " + negVotes + " against votes");
			}
		}
		
		public void evaluateMission() {
			int failures = Collections.frequency(missionSuccess, false);
			if (missionNumber == 4 && currentConfig[7] == 2) {
				if (failures >= 2) {
					server.publicMessage("Mission failed with " + failures + " failures");
					failedMissions++;
				} else {
					server.publicMessage("Mission succeeded with " + failures + "failures");
				}
			} else {
				if (failures >= 1) {
					server.publicMessage("Mission failed with " + failures + " failures");
					failedMissions++;
				} else {
					server.publicMessage("Mission succeded with 0 failures");
				}
			}
			
			if (failedMissions >= 3) {
				server.publicMessage("3 Failures, Spies win");
				state = GameState.GAMESTART;
				gameInProgress = false;
				return;
			}
			
			if (missionNumber == 5) {
				server.publicMessage("Resistance wins with only " + failedVotes + " failutes");
				state = GameState.GAMESTART;
				gameInProgress = false;
				return;
			}
			
			nextMission();
			voted.clear();
			
		}
		
	}
	
	@Override
	public synchronized void handleMessage(Message message, int origin) {
		String text = message.messageText;
		String command;
		String remText;
		int charPosition = text.indexOf(" ");
		
		if (nonGameUsers.contains(origin)) {
			server.privateMessage("You are not part of the game and cannot send commands", origin);
			return;
		}
		
		if (charPosition != -1) {
			remText = text.substring(charPosition + 1);
			command = text.substring(0, charPosition);
		} else {
			command = text;
			remText = "";
		}
		
		boolean handled = false;
		switch(state) {
		
			case GAMESTART :
				if (command.equals("/start")) {
					handled = true;
					users = server.getActiveClientIDs();
					if (users.size() < minPlayers) {
						server.publicMessage("Start Failed, at least " + (minPlayers - users.size()) + " player(s) still needed");
					} else if (users.size() > maxPlayers) {
						server.publicMessage("Start Failed, " + (maxPlayers - users.size()) + " too many user(s)");
					} else {
						setUpGame();
					}
				}
				break;
				
			case SQUAD_SELECTION :
				if (origin == currentLeader) {
					if (command.equals("/add")) {
						handled = true;
						int result = server.getUserID(remText);
						if (result == -1) {
							server.privateMessage("No valid user " + remText, origin);
						} else {
							mission.addSquadMember(server.getUserID(remText));
						}
					} else if (command.equals("/remove")) {
						handled = true;
						int result = server.getUserID(remText);
						if (result == -1) {
							server.privateMessage("No valid user " + remText, origin);
						} else {
							mission.removeSquadMember(server.getUserID(remText));
						}
					}
				} else {
					server.privateMessage("Only the leader can vote during squad selection", origin);
					handled = true;
				}
				break;
			
			case SQUAD_VOTE :
				if (command.equals("/vote")) {
					handled = true;
					if (voted.contains(origin)) {
						server.privateMessage("You already voted", origin);
					} else {
						server.publicMessage(server.getUsername(origin) + " voted yes");
						mission.squadVote(true);
						voted.add(origin);
					}
				} else if (command.equals("/downvote")) {
					handled = true;
					if (voted.contains(origin)) {
						server.privateMessage("You already voted", origin);
					} else {
						server.publicMessage(server.getUsername(origin) + " voted no");
						mission.squadVote(false);
						voted.add(origin);
					}
				} else {
					server.privateMessage("Unrecognised command " + command, origin);
				}
				break;
				
			case SQUAD_ATTEMPT :
				if (command.equals("/vote")) {
					handled = true;
					if (voted.contains(origin)) {
						server.privateMessage("You already voted", origin);
					} else {
						mission.addSubmittedSuccessFail(true, origin);
					}
				} else if (command.equals("/downvote")) {
					handled = true;
					if (voted.contains(origin)) {
						server.privateMessage("You already voted", origin);
					} else {
						mission.addSubmittedSuccessFail(false, origin);
					}
				} else {
					server.privateMessage("Unrecognised command " + command, origin);
				}
				break;	
		}
		if (!handled) {
			super.handleMessage(message, origin);
		}
	}
	
	@Override
	public void handleLogout(int origin) {
		server.publicMessage("User: " + server.getUsername(origin) + " has left, "
				+ " game cannot continue as number of players has changed, re-starting game");
		gameInProgress = false;
		state = GameState.GAMESTART;
	}
	
	@Override
	public String getRules() {
		
		switch (state) {
		// {GAMESTART, SQUAD_SELECTION, SQUAD_VOTE, SQUAD_ATTEMPT};
		case GAMESTART:
			return "Welcome to Resistance, to start the game type /start\n"
			+ "Resistance is a game consisting of 5 missions,\n"
			+ "each mission has three sections: \n"
			+ "- squad selection\n"
			+ "- squad vote\n"
			+ "- squad attempt\n"
			+ "A leader choses who goes on the missions, then the whole\n"
			+ "team votes on whether the mission should go, then the \n"
			+ "squad attempt the mission, and can either submit a pass or fail\n"
			+ "the spies have to cause 3 out of the 5 missions to fail, or make\n"
			+ "the resistance fall to in-fighting by failing 5 votes for squads";
		
		case SQUAD_SELECTION:
			return "The game is currently in the Squad Selection section,\n"
					+ "the team should be discussing who to send on the current mission\n"
					+ "the current leader ultimately decides who will go on this mission, and\n"
					+ "adds players to the team using '/add username' and can remove players\n"
					+ "using '/remove username' where username is the name of the player";
	
		case SQUAD_VOTE:
			return "The game is currently in the Squad Vote section,\n"
					+ "The team can now vote on whether the current squad assigned to the\n"
					+ "mission should go on the mission, vote for the team with /vote and vote\n"
					+ "against the team with /downvote.\n"
					+ "Note: If 5 votes fail throughout the game, the spies win";
			
		case SQUAD_ATTEMPT:
			return "The game is currently in the Squad Attempt section, \n"
					+ "The selected squad now attempt the mission, to submit a success\n"
					+ "for the mission, send /vote, to submit a failure for the mission\n"
					+ "send /downvote";
			
		default:
			return rules;
			
		}
	}
	
	public static void main(String[] args) {
		ServerStubCMD theStub = new ServerStubCMD(new String[] {"Debbie", "Arnold", "Dave", "CoolVishnu", "AMouse"});
		Game game = new Resistance(theStub);
		theStub.attachGameObject(game);
		theStub.commandLineControl();
	}

}

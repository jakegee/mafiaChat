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
	
	public Resistance(IServer server) {
		super(server);
		state = GameState.GAMESTART;
	}
	
	private enum GameState {GAMESTART, SQUAD_SELECTION, SQUAD_VOTE, SQUAD_ATTEMPT};
	//private enum Mission {MISSION1, MISSION2, MISSION3, MISSION4, MISSION5};

	private ArrayList<Integer> spies;
	
	public void setUpGame() {
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
	
	public void nextLeader() {
		String leader = leaderQueue.poll();
		leaderQueue.add(leader);
		currentLeader = server.getUserID(leader);
		voted = new ArrayList<Integer>();
		server.publicMessage("All hail the new Leader " + leader);
		server.publicMessage("The subsequent Leader will be " + leaderQueue.peek());
	}
	
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
		
		public Mission(int numberOfPlayers ) {
			this.numberOfPlayers = numberOfPlayers;
			this.missionNumber = 1;
			this.selectedSquad = new ArrayList<Integer>();
			this.squadSize = currentConfig[missionNumber + 1];
			this.votes = 0;
			this.failedVotes = 0;
			this.failedMissions = 0;
			this.missionSuccess = new ArrayList<Boolean>();
		}
		
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
		
		public void removeSquadMember(int user) {
			if (selectedSquad.contains(user)) {
				selectedSquad.remove(user);
				server.publicMessage(server.getUsername(user) + "has been removed from the squad");
			} else {
				server.publicMessage("Leader attempted to remove " + user + " who wasn't even in the squad");
			}
		}
		
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
		
		public void repeatMission() {
			selectedSquad = new ArrayList<Integer>();
			missionSuccess = new ArrayList<Boolean>();
			state = GameState.SQUAD_SELECTION;
			this.votes = 0;
			this.negVotes = 0;
			nextLeader();
		}
		
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
				server.publicMessage("Vote failed, " + failedVotes + " failed votes so far, "
						+ (5 - failedVotes) + " failed votes until spies win");
				repeatMission();
				voted.clear();
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
				return;
			}
			
			if (missionNumber == 5) {
				server.publicMessage("Resistance wins with only " + failedVotes + " failutes");
				state = GameState.GAMESTART;
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
		
		if (charPosition != -1) {
			remText = text.substring(charPosition + 1);
			command = text.substring(0, charPosition);
		} else {
			command = text;
			remText = "";
		}
		
		
		switch(state) {
		
			case GAMESTART :
				if (command.equals("/start")) {
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
						int result = server.getUserID(remText);
						if (result == -1) {
							server.privateMessage("No valid user " + remText, origin);
						} else {
							mission.addSquadMember(server.getUserID(remText));
						}
					} else if (command.equals("/remmove")) {
						int result = server.getUserID(remText);
						if (result == -1) {
							server.privateMessage("No valid user " + remText, origin);
						} else {
							mission.removeSquadMember(server.getUserID(remText));
						}
					}
				} else {
					server.privateMessage("Only the leader can vote during squad selection", origin);
				}
				break;
			
			case SQUAD_VOTE :
				if (command.equals("/vote")) {
					if (voted.contains(origin)) {
						server.privateMessage("You already voted", origin);
					} else {
						server.publicMessage(server.getUsername(origin) + " voted yes");
						mission.squadVote(true);
						voted.add(origin);
					}
				} else if (command.equals("/downvote")) {
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
					if (voted.contains(origin)) {
						server.privateMessage("You already voted", origin);
					} else {
						mission.addSubmittedSuccessFail(true, origin);
					}
				} else if (command.equals("/downvote")) {
					if (voted.contains(origin)) {
						server.privateMessage("You already voted", origin);
					} else {
						mission.addSubmittedSuccessFail(false, origin);
					}
				} else {
					server.privateMessage("Unrecognised command " + command, origin);
				}
				break;
		
			// {GAMESTART, SQUAD_SELECTION, SQUAD_VOTE, SQUAD_ATTEMPT, GAMEEND};
			
		}
	}
	
	public static void main(String[] args) {
		ServerStubCMD theStub = new ServerStubCMD(new String[] {"Debbie", "Arnold", "Dave", "CoolVishnu", "AMouse"});
		Game game = new Resistance(theStub);
		theStub.attachGameObject(game);
		theStub.commandLineControl();
	}

}

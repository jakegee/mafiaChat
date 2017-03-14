import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import messages.Message;
import systemInterfaces.IGame;
import systemInterfaces.IServer;

import org.apache.commons.collections4.OrderedBidiMap;
import org.apache.commons.collections4.bidimap.TreeBidiMap;

public class Mafia implements IGame {
    private IServer server;

    TreeBidiMap<Integer, String> players; // experimental way to deal with
					  // storing
    // player ids/usernames

    private boolean nightVoteInProgress = false;
    private boolean elimDayVoteInProgress = false;

    private ArrayList<Integer> playersLeftToVote; // could use to private
						  // message the users to tell
						  // them that they still need
						  // to vote
						  // this could be attached to
						  // the timer

    private boolean day = true;

    // private ArrayList<Integer> containing the players who have used /ready
    private ArrayList<Integer> ready;
    private ArrayList<Integer> votedStart;
    private ArrayList<Integer> mafia; // might make type map
    private int[] mafiaID; // placeholder
    private ArrayList<Integer> innocentsID; // might make type map
    private ArrayList<Integer> elimDay;
    private ArrayList<Integer> save;
    // private ArrayList<Integer> playerIDs;
    private HashMap<Integer, String> eliminate;
    private ArrayList<Integer> dayVote;
    private ArrayList<Integer> nightVote;
    private Integer playerOnTrialID = null;
    private Random mafiaPicker;
    
    //how timer was used in the GuiChatroom for OperatingSystemsAndNetworksEx2
//	timer = new Timer();
//	timer.scheduleAtFixedRate(new TimerTask() {
//		@Override
//		public void run() {
//			ChatClientApp.frame.client.get_message();
//		}
//	}, 1000, 2000);

    /**
     * The handleMessage method takes in the message and processes the command
     * based on what has been typed after the "/". This method first checks
     * whether it is day or night as if it is night there is only one command
     * accepted.
     * 
     * @param message
     *            is of type message where the type of message is of type
     *            COMMAND.
     * @param origin
     *            is the userID of type int for the person who sent the command
     *            message.
     */
    @Override
    public synchronized void handleMessage(Message message, int origin) {
	String text = message.messageText;
	String command = text.substring(0, text.indexOf(' '));
	String remText = text.substring(text.indexOf(' ') + 1);

	if (day == true) {

	    if (command == "/ready") { // no trailing text after /ready
		if (!remText.isEmpty()) {
		    server.privateMessage("The command \"/ready\" cannnot have characters after it", origin);
		} else {
		    ready(origin);
		}

	    } else if (command == "/unready") {
		if (!remText.isEmpty()) {
		    server.privateMessage("The command \"/unready\" cannnot have characters after it", origin);
		} else {
		    unready(origin);
		}

	    } else if (command == "/start") {// no trailing text after /start

		if (!remText.isEmpty()) {
		    server.privateMessage("The command \"/start\" cannnot have characters after it", origin);
		} else {
		    voteStart(origin);
		}

	    } else if (command == "/elim") { // space between /elim and player
					     // name, no trailing text after
					     // player name
		if (remText.isEmpty() || remText.contains(" ")) {
		    server.privateMessage(
			    "The command \"/elim\" needs to be followed by a player's name with no spaces/characters thereafter",
			    origin);
		} else {
		    elimDay(remText, origin);
		}

	    } else if (command == "/save") {
		if (remText.isEmpty() || remText.contains(" ")) {
		    server.privateMessage(
			    "The command \"/save\" needs to be followed by a player's name with no spaces/characters thereafter",
			    origin);
		} else {
		    save(remText, origin);
		}

	    } else if (command == "/night") {
		if (!remText.isEmpty()) {
		    server.privateMessage("The command \"/night\" cannnot have characters after it", origin);
		} else {
		    voteNight(origin);
		}

	    } else if (command == "/day") {
		if (!remText.isEmpty()) {
		    server.privateMessage("The command \"/day\" cannnot have characters after it", origin);
		} else {
		    voteDay(origin);
		}

	    } else {
		server.privateMessage(message + " does not contain a valid command", origin);
	    }

	} else {

	    if (command == "/elim") {
		if (remText.isEmpty() || remText.contains(" ")) {
		    server.privateMessage(
			    "The command \"/elim\" needs to be followed by a player's name with no spaces/characters thereafter",
			    origin);
		} else {
		    elimNight(remText, origin);
		}
	    } else {
		server.privateMessage(
			"The only valid command during the night is \"elim\" followed a player's name (separated by a space)",
			origin);
	    }
	}

    }

    /**
     * 
     * @param origin
     *            is the userID of type int for the person who sent the command
     *            message.
     */
    public void ready(int origin) { // enforce a max amount of players?, 16 seemed to be the maximum allowed in original game
	if (!ready.contains(origin)) { // ensures players aren't added more than
				       // once
	  
	    if(ready.size() < 16){
	    ready.add(origin);

	    server.publicMessage("number of players ready: " + ready.size());
	    // possibly extend to mention which players are ready

	    if (ready.size() >= 6) {
		int[] readyArray = ready.stream().mapToInt(i -> i).toArray();

		// not sure if this should be public or private
		server.privateMessage(
			"There are enough players to start the game. Use the command" + " \"/start\" to vote to start",
			readyArray);
	    }
	    
	    if(ready.size() == 15) {
		server.publicMessage("The maximum amount of players for mafia has been reached");
	    }
	    
	    } else{
		server.privateMessage("The maximum amount of players has already been reached.", origin);
	    }
	} else {
	    server.privateMessage("you are already set as ready", origin);
	}
    }

    private void unready(int origin) {
	if (ready.contains(origin)) { // ensures players aren't added more than
	    // once
	    ready.remove(origin);

	    server.publicMessage(
		    players.get(origin) + " has unreadied, the number of players ready is now: " + ready.size());
	    // possibly extend to mention which players are ready

	    if (ready.size() == 5) {
		int[] readyArray = ready.stream().mapToInt(i -> i).toArray();

		server.privateMessage("There are no longer enough players to start the game, start vote has been reset",
			readyArray);
	    }

	    if (ready.size() < 6) {
		server.publicMessage("There needs to be " + (6 - ready.size())
			+ " players to ready up before the game of Mafia can be " + "voted to start");
	    }
	} else {
	    server.privateMessage("you weren't set as ready to begin with", origin);
	}

    }

    /**
     * 
     * @param origin
     *            is the userID of type int for the person who sent the command
     *            message.
     */
    public void voteStart(int origin) {
	if (!votedStart.contains(origin) && ready.contains(origin)) { // ensures players aren't added more
					    // than once
	    votedStart.add(origin);
	    if (votedStart.size() < ready.size()) {
		server.publicMessage("number of players that want to start: " + ready.size());
		// possibly extend message to mention who has voted to start
	    } else {
		gameStart();
	    }
	} else if (votedStart.contains(origin)){
	    server.privateMessage("you have already voted to start", origin);
	} else {
	    server.privateMessage("you need to be set to ready before you can vote to start", origin);
	}
    }

    private void gameStart() { // TODO: implement muting players that are not in
			       // the game
	ready.clear();

	for (int i = 0; i < votedStart.size(); i++) {
	    int id = votedStart.get(i);
	    // playerIDs.add(id);
	    players.put(id, server.getUsername(id));

	}

	int numMafia = Math.round(votedStart.size() / 3);

	for (int j = 0; j < numMafia; j++) {
	    int index = mafiaPicker.nextInt(votedStart.size());

	    int id = votedStart.get(index);
	    mafia.add(id);
	    votedStart.remove(index);
	}

	for (int k = 0; k < votedStart.size(); k++) {

	    int id = votedStart.get(k);

	    innocentsID.add(id);
	}

	votedStart.clear();

	int[] innocentIDArray = innocentsID.stream().mapToInt(i -> i).toArray();
	server.privateMessage("you are an innocent", innocentIDArray);

	int[] mafiaIDArray = innocentsID.stream().mapToInt(i -> i).toArray();
	String[] mafiaNameArray = new String[mafiaIDArray.length];

	for (int l = 0; l < mafiaIDArray.length; l++) {
	    mafiaNameArray[l] = server.getUsername(mafiaIDArray[l]);
	}

	// currently uses the default toSting method for array. Also i think it
	// will display the id numbers rather than the players names - need to
	// convert if so would be good to only show only the other mafia members
	// rather than including the players own name in the list
	server.privateMessage("you are one of the mafia, the mafia members (including " + "you) are " + mafiaIDArray,
		mafiaIDArray);
    }

    /**
     * The elimDay method either starts the vote to eliminate a player during
     * the day or adds subsequent votes to the total number of votes for the
     * player on trial. It also takes the origin of the message and checks
     * whether the player it corresponds to has already voted to eliminate said
     * player (in which case doesn't add their vote) and checks if they have
     * already voted to save said payer (in which case it removes their vote
     * from the save vote and adds their vote to the eliminate vote).
     * 
     * @param player
     *            is the name of the player being voted to be eliminated as type
     *            String
     * @param origin
     *            is the id of the player making the vote
     */
    private void elimDay(String player, int origin) {
	OrderedBidiMap<String, Integer> invPlayers = players.inverseBidiMap();
	// int playerID = server.getUserID(player);
	int playerID = invPlayers.get(player);

	//if (nightVote.size() > 0) {
	if (nightVoteInProgress) {
	    server.privateMessage("cannot vote to eliminate a player while there is a vote to change the game to night",
		    origin);

	    // } else if (!playerIDs.contains(playerID)) { // this is for when
	    // using ArrayList of playerIDs
	} else if (!players.containsValue(player)) {
	    server.privateMessage(player + " is not in this game/has already been eliminated", origin);

	} else if (playerID == origin) {
	    server.privateMessage("you cannot vote for yourself", origin);

	} else if (playerOnTrialID == null) {
	    elimDayVoteInProgress = true;
	    playerOnTrialID = playerID;
	    elimDay.add(origin); // not checked if this will eliminate because
	    // assumed that 1 vote is not enough.
	    // there could be a deadlock when two players are
	    // left (might change the mafia win condition to
	    // address this)

	} else {
	    if (playerOnTrialID != playerID) {
		server.privateMessage("cannot vote for " + player + " while the vote for "
			+ server.getUsername(playerOnTrialID) + " is in progress", origin);
	    } else {
		if (save.contains(origin)) {
		    save.remove(origin);
		}

		elimDay.add(origin);
		checkElim(origin);
	    }
	}

    }

    /**
     * The checkElim method checks to see if there are enough votes to eliminate
     * the suscipiousPlayer. If the number of elim votes has passed half the
     * number of players left, then the suscipiousPlayer is eliminated. If this
     * is not the case then the method sends a public message stating who has
     * voted for the suscipiousPlayer.
     * 
     * @param origin
     *            is the userID of type int for the person who sent the command
     *            message.
     */
    private void checkElim(int origin) { // does this need to be synchronized?

	// if (elimDay.size() > playerIDs.size() / 2) {
	if (elimDay.size() > players.size() / 2) {
	    eliminateDay();

	} else {
	    server.publicMessage(
		    players.get(origin) + " has voted to eliminate " + server.getUsername(playerOnTrialID));
	}

    }

    /**
     * The eliminate method removes the player whose username matches the
     * suscipiousPlayer field variable from the game. This involves clearing the
     * elim and save voting lists, removing the player from the list of active
     * players,removing them from the list of innocent/mafia and muting the
     * player in chat.
     */
    private void eliminateDay() {

	// playerIDs.remove(playerOnTrialID);
	players.remove(playerOnTrialID);

	if (mafia.contains(playerOnTrialID)) {
	    mafia.remove(playerOnTrialID);

	} else {
	    innocentsID.remove(playerOnTrialID);
	}
	
	server.setPlayerMuted(playerOnTrialID, true);
	
	playerOnTrialID = null;	
	elimDay.clear();
	save.clear();
	elimDayVoteInProgress = false;
	
	checkWin();

    }

    /**
     * The save method is used to vote against eliminating the current player on
     * trial. The method takes the origin of the message and checks whether the
     * player it corresponds to has already voted (in which case doesn't add
     * their vote) and checks if they had previously voted to eliminate said
     * player (in which case it removes their vote to eliminate the player and
     * adds their vote to save the player).
     * 
     * @param origin
     *            is the userID of type int for the person who sent the command
     *            message.
     */
    private void save(String player, int origin) {// suspicious player doesn't
						  // need to vote for themselves
	OrderedBidiMap<String, Integer> invPlayers = players.inverseBidiMap();
	// int playerID = server.getUserID(player);
	int playerID = invPlayers.get(player);

	if (playerOnTrialID == null) {
	    server.privateMessage("you cannot vote to save someone when there is no-one on trial", origin);

	    // } else if (!playerIDs.contains(playerID)) {
	} else if (!players.containsValue(playerID)) {
	    server.privateMessage(player + " is not in this game/has already been eliminated", origin);

	} else if (playerID == origin) {
	    server.privateMessage("you don't need to vote to save yourself", origin);

	} else {
	    if (playerOnTrialID != playerID) {
		server.privateMessage("cannot vote for " + player + " while the vote for "
			+ server.getUsername(playerOnTrialID) + " is in progress", origin);
	    } else {
		if (elimDay.contains(origin)) {
		    elimDay.remove(origin);
		}

		save.add(origin);
		checkElim(origin);
	    }
	}

    }

    /**
     * The voteNight method either starts the vote to change the game to night
     * or adds subsequent votes to the total number of night votes. It also
     * takes the origin of the message and checks whether the player it
     * corresponds to has already voted to change to night (in which case
     * doesn't add their vote) and checks if they have already voted for day (in
     * which case it removes their vote from night and adds their vote to
     * night).
     * 
     * @param origin
     *            is the userID of type int for the person who sent the command
     *            message.
     */
    private void voteNight(int origin) {
	// if (playerOnTrialID != null) {
	if (elimDayVoteInProgress) {
	    server.privateMessage("you cannot start a vote for it to be night when there is a vote in progress "
		    + "to eliminate a player", origin);

	} else if (nightVote.contains(origin)) {
	    server.privateMessage("you have already voted to change the game to night", origin);

	} else {
	    nightVoteInProgress = true;

	    if (dayVote.contains(origin)) {
		dayVote.remove(origin);
	    }

	    nightVote.add(origin);
	    checkNight(origin);
	}
    }

    /**
     * The voteDay method is used to vote against the vote to change the game to
     * night. The method takes the origin of the message and checks whether the
     * player it corresponds to has already voted to keep it day (in which case
     * doesn't add their vote) and checks if they have already voted for night
     * (in which case it removes their vote from night and adds their vote to
     * day).
     * 
     * @param origin
     *            is the userID of type int for the person who sent the command
     *            message.
     */
    private void voteDay(int origin) {
	if (!nightVoteInProgress) {
	    server.privateMessage("cannot use this command when there isn't a vote to change to night", origin);

	} else if (dayVote.contains(origin)) {
	    server.privateMessage("you have already voted to keep the game in the day phase", origin);

	} else {
	    if (nightVote.contains(origin)) {
		nightVote.remove(origin);
	    }

	    dayVote.add(origin);
	    checkDay(origin);
	}
    }

    /**
     * The checkDay method checks if the votes for to keep the game as day, are
     * in the majority. If so then the vote for night is unsuccessful and the
     * votes cleared.
     * 
     * @param origin
     *            is the userID of type int for the person who sent the command
     *            message.
     */
    private void checkDay(int origin) {
	server.publicMessage(players.get(origin) + " has voted for it to remain day");

	// if (dayVote.size() > playerIDs.size() / 2) {
	if (dayVote.size() > players.size() / 2) {
	    dayVote.clear();
	    nightVote.clear();
	    server.publicMessage("Majority vote reached for it to remain day");
	}
    }

    /**
     * The checkNight method checks if the votes to change the game to night,
     * are in the majority. If so then the vote for night is successful, the
     * votes cleared, game changed to night and chat set to inactive.
     * 
     * @param origin
     *            is the userID of type int for the person who sent the command
     *            message.
     */
    private void checkNight(int origin) {
	server.publicMessage(players.get(origin) + " has voted for it to remain night");

	// if (nightVote.size() > playerIDs.size() / 2) {
	if (nightVote.size() > players.size() / 2) {
	    nightVote.clear();
	    dayVote.clear();
	    server.publicMessage("Majority vote reached for it to change to night, all players now muted");
	    // need to make sure that muting all the players doesn't stop the
	    // mafia from voting

	    server.setChatActive(false); // wondering if it matters what order
	    day = false; 		// these are in
	    nightVoteInProgress = false;
	}
    }

    /**
     * 
     * @param player
     *            is the name of the player being voted to be eliminated as type
     *            String
     * @param origin
     *            is the id of the player making the vote
     */
    public void elimNight(String player, int origin) {
	OrderedBidiMap<String, Integer> invPlayers = players.inverseBidiMap();
	// int playerID = server.getUserID(player);
	int playerID = invPlayers.get(player);

	if (innocentsID.contains(origin)) {
	    server.privateMessage(player + "As an innocent you are not active during the night", origin);

	    // } else if (!playerIDs.contains(player)) {
	} else if (!players.containsValue(player)) {
	    server.privateMessage(player + " is not in this game/has already been eliminated", origin);

	} else if (playerID == origin) { // not sure about keeping this in
	    server.privateMessage("you cannot vote for yourself", origin);

	} else if (mafia.contains(player)) { // wondering whether it's better to
					     // leave this out
	    server.privateMessage("you cannot vote for another mafia player", origin);

	} else if (eliminate.containsKey(origin)) {
	    server.privateMessage("you can only vote for a valid player once", origin);

	} else {
	    // TODO figure how to handle night vote
	    eliminate.put(origin, player);
	    checkElimNight(origin);
	}
    }

    private void checkElimNight(int origin) {
	String[] votes = (String[]) eliminate.values().toArray();

	if (eliminate.size() < mafia.size()) {
	    server.privateMessage("waiting for " + (mafia.size() - votes.length) + "more mafia to vote", origin);

	} else if (eliminate.size() == mafia.size()) {
	    String victim = votes[0];

	    Boolean unique = true;

	    if (mafia.size() > 1) {
		for (int i = 1; i < votes.length; i++) {
		    if (!votes[i].equals(victim)) {
			unique = false;
		    }
		}
	    }

	    if (unique.equals(false)) {
		int victimID = server.getUserID(victim);

		players.remove(victimID);
		innocentsID.remove(victimID);
		server.privateMessage("you have been killed in the night", victimID);
		server.publicMessage("As dawn breaks, you wake to find that " + victim + " was killed last night");
	    } else {
		server.publicMessage("As dawn breaks, you wake to find that no-one was killed last night");
		server.privateMessage("In order to kill an innocent during the night, the same person needs"
			+ " to be chosen by all the mafia players", mafiaID);
	    }
	    eliminate.clear();

	    day = true;
	    server.setChatActive(true);
	} else {

	}

    }
    
    public void checkWin(){
	if (mafia.size() == innocentsID.size()){
	    server.publicMessage("The Mafia win");
	} else if (mafia.size() == 0){
	    server.publicMessage("The innocents win");
	} else {
	    
	}
    }

    public void assignPoints() { // only assign points to the survivors?

    }
}

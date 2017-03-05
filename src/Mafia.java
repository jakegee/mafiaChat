import java.util.ArrayList;
import java.util.Random;
import java.util.TreeMap;

import messages.Message;
import systemInterfaces.IGame;
import systemInterfaces.IServer;

public class Mafia implements IGame {
    private IServer server;

    // enum for setting the day/night phases of the game, not sure how to use if
    // statements
    // don't know why eclipse auto-formats it like this
    // private enum phase {
    // DAY, NIGHT
    // };

    private boolean day = true;
    
    private boolean voteInProgress = false;  //not yet used

    // array list containing the players who have used /ready
    private ArrayList<Integer> ready;
    private ArrayList<Integer> votedStart;
    private ArrayList<String> mafia; // might make type map
    private int[] mafiaID; //placeholder
    private ArrayList<String> innocent; // might make type map
    private ArrayList<String> elimDay;
    private ArrayList<String> elimNight;
    private ArrayList<String> save;
    private ArrayList<String> players;
    private TreeMap<String, Integer> eliminate;
    private ArrayList<String> dayVote;
    private ArrayList<String> nightVote;
    private String suspiciousPlayer;
    private Random mafiaPicker;

    // NOTE: Sorry I forgot that this has been moved to the Server class,
    // just call setChatActive(boolean active) if you want to activate or
    // deactivate this. This function is not used by the Server
    public boolean getChatRelayed() {
	// TODO Auto-generated method stub
	return false;
    }

    /**
     * Takes in the message and processes the command based on what has been
     * typed after the "/"
     */
    @Override
    public void handleMessage(Message message, int origin) {
	// TODO Auto-generated method stub
	String text = message.messageText;
	String command = text.substring(0, text.indexOf(' '));
	String remText = text.substring(text.indexOf(' ') + 1);

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

	} else if (command == "/elim") {
	    if (remText.isEmpty() || remText.contains(" ")) {
		server.privateMessage(
			"The command \"/elim\" needs to be followed by a player's name with no spaces/characters thereafter",
			origin);
	    } else {

		if (day == true) {
		    elimDay(remText, origin);
		} else {
		    elimNight(remText, origin);
		}

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

	}

    }

    private void voteDay(int origin) {
	// TODO Auto-generated method stub
	String user = server.getUsername(origin);

	if (day == false) {
	    server.privateMessage("cannot vote for this during night", origin);
	} else if (nightVote.isEmpty()) {
	    server.privateMessage("cannot use this command when there isn't a vote to change to night", origin);
	} else {
	    if (nightVote.contains(user)) {
		nightVote.remove(user);
	    }

	    dayVote.add(user);
	    checkDay(origin);
	}
    }

    private void checkDay(int origin) {
	// TODO Auto-generated method stub

	server.publicMessage(server.getUsername(origin) + " has voted for it to remain day");

	if (dayVote.size() > players.size() / 2) {
	    dayVote.clear();
	    nightVote.clear();
	    server.publicMessage("Majority vote reached for it to remain day");
	}
    }

    private void voteNight(int origin) {
	// TODO Auto-generated method stub
	String user = server.getUsername(origin);

	if (day == false) {
	    server.privateMessage("it is already night", origin);
	} else if (nightVote.contains(user)) {
	    server.privateMessage("you have already voted to change the game to night", origin);
	} else {
	    if (dayVote.contains(user)) {
		dayVote.remove(user);
	    }

	    nightVote.add(user);
	    checkNight(origin);
	}
    }

    private void checkNight(int origin) {
	// TODO Auto-generated method stub

	server.publicMessage(server.getUsername(origin) + " has voted for it to remain night");

	if (nightVote.size() > players.size() / 2) {
	    nightVote.clear();
	    dayVote.clear();
	    server.publicMessage("Majority vote reached for it to change to night, all players now muted");
	    // need to make sure that muting all the players doesn't stop the
	    // mafia from voting
	    day = false;
	    server.setChatActive(false);
	}
    }

    /**
     * The elimDay method is for elimination votes during the day
     * 
     * @param player
     *            is the name of the player being voted to be eliminated as type
     *            String
     * @param origin
     *            is the id of the player making the vote
     */
    private void elimDay(String player, int origin) {

	// not sure whether to allow player to vote eliminate themselves
	// TODO create check for if someone has voted to save the player
	String user = server.getUsername(origin);

	if (!players.contains(player)) {
	    server.privateMessage(player + " is not in this game/has already been eliminated", origin);
	} else if (player == user) {
	    server.privateMessage("you cannot vote for yourself", origin);
	} else if (suspiciousPlayer == null) {
	    suspiciousPlayer = player;
	    elimDay.add(user); // not checked if this will eliminate because
			    // assumed that 1 vote is not enough.
			    // there could be a deadlock when two players are
			    // left (might change the mafia win condition to
			    // address this)

	} else {
	    if (suspiciousPlayer != player) {
		server.privateMessage(
			"cannot vote for " + player + " while the vote for " + suspiciousPlayer + " is in progress",
			origin);
	    } else {
		if (save.contains(user)) {
		    save.remove(user);
		}

		elimDay.add(user);
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
     */
    private void checkElim(int origin) { // does this need to be synchronized?
	// TODO Auto-generated method stub
	if (elimDay.size() > players.size() / 2) {
	    eliminateDay();
	} else {
	    server.publicMessage(server.getUsername(origin) + " has voted to eliminate " + suspiciousPlayer);
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

	elimDay.clear();
	save.clear();

	players.remove(suspiciousPlayer);

	if (mafia.contains(suspiciousPlayer)) {
	    mafia.remove(suspiciousPlayer);
	} else {
	    innocent.remove(suspiciousPlayer);
	}
	int elimID = server.getUserID(suspiciousPlayer);

	server.setPlayerMuted(elimID, true);
	suspiciousPlayer = null;

    }

    private void save(String player, int origin) {// suspicious player doesn't
						  // need to vote for themselves

	String user = server.getUsername(origin);

	if (!players.contains(player)) {
	    server.privateMessage(player + " is not in this game/has already been eliminated", origin);
	} else if (player == user) {
	    server.privateMessage("you don't need to vote to save yourself", origin);
	} else if (suspiciousPlayer == null) {
	    suspiciousPlayer = player;
	    save.add(user); // not checked if this will eliminate because
			    // assumed that 1 vote is not enough.
			    // there could be a deadlock when two players are
			    // left (might change the mafia win condition to
			    // address this)

	} else {
	    if (suspiciousPlayer != player) {
		server.privateMessage(
			"cannot vote for " + player + " while the vote for " + suspiciousPlayer + " is in progress",
			origin);
	    } else {
		if (elimDay.contains(user)) {
		    elimDay.remove(user);
		}

		save.add(user);
		checkElim(origin);
	    }
	}

    }

    public void elimNight(String player, int origin) {
	String user = server.getUsername(origin);
	
	if (innocent.contains(user)){
	    server.privateMessage(player + "As an innocent you are not active during the night", origin);
	    
    	} else if (!players.contains(player)) {
	    server.privateMessage(player + " is not in this game/has already been eliminated", origin);
	    
	} else if (player == user) {
	    server.privateMessage("you cannot vote for yourself", origin);
	    
	} else if (mafia.contains(player)){	    
	    server.privateMessage("you cannot vote for another mafia player", origin); //not sure if will keep this
	
	}else {
	   //TODO figure out how to handle the elimination vote during the night
	    eliminate.put(player, origin);
	    checkElimNight();
	}
    }

    private void checkElimNight() {
	// TODO Auto-generated method stub
	if (eliminate.size() == 1){
	   String victim = eliminate.firstKey();
	   
	   players.remove(victim);
	   innocent.remove(victim);
	   server.privateMessage("you have been killed in the night", server.getUserID(victim));
	   server.publicMessage("As dawn breaks, you wake to find that " + victim + " was killed last night");	    
	    
	}else {
	    server.publicMessage("As dawn breaks, you wake to find that no-one was killed last night");
	    server.privateMessage("In order to kill an innocent during the night, the same person needs"
	    	+ " to be chosen by all the mafia players", mafiaID);
	}
	eliminate.clear();
	
	day = true;
	server.setChatActive(true);
    }

    private void unready(int origin) {
	// TODO Auto-generated method stub
	if (ready.contains(origin)) { // ensures players aren't added more than
	    // once
	    ready.remove(origin);

	    server.publicMessage(
		    "player: " + origin + "has unreadied, the number of players ready is now: " + ready.size());
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

    public void ready(int origin) {
	if (!ready.contains(origin)) { // ensures players aren't added more than
				       // once
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
	} else {
	    server.privateMessage("you are already set as ready", origin);
	}
    }

    public void voteStart(int origin) {
	if (!votedStart.contains(origin)) { // ensures players aren't added more
					    // than once
	    votedStart.add(origin);
	    if (votedStart.size() < ready.size()) {
		server.publicMessage("number of players that want to start: " + ready.size());
		// possibly extend message to mention who has voted to start
	    } else {
		gameStart();
	    }
	} else {
	    server.privateMessage("you have already voted to start", origin);
	}
    }

    private void gameStart() { // TODO: implement muting players that are not in
			       // the game
	ready.clear();

	for (int i = 0; i < votedStart.size(); i++) {
	    int id = votedStart.get(i);
	    players.add(server.getUsername(id));

	}

	int numMafia = Math.round(votedStart.size() / 3);

	for (int j = 0; j < numMafia; j++) {
	    int index = mafiaPicker.nextInt(votedStart.size());

	    int id = votedStart.get(index);
	    String name = server.getUsername(id);
	    mafia.add(name);
	    votedStart.remove(index);
	}

	for (int k = 0; k < votedStart.size(); k++) {

	    int id = votedStart.get(k);
	    String name = server.getUsername(id);

	    innocent.add(name);
	}

	votedStart.clear();

	int[] innocentArray = innocent.stream().mapToInt(i -> server.getUserID(i)).toArray();
	server.privateMessage("you are an innocent", innocentArray);

	int[] mafiaArray = innocent.stream().mapToInt(i -> server.getUserID(i)).toArray();

	// currently uses the default toSting method for array. Also i think it
	// will display the id numbers rather than the players names - need to
	// convert if so would be good to only show only the other mafia members
	// rather than including the players own name in the list
	server.privateMessage("you are one of the mafia, the mafia members (including " + "you) are " + mafiaArray,
		mafiaArray);
    }

}

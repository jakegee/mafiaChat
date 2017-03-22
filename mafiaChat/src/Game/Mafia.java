package Game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ThreadLocalRandom;

import messages.Message;
import systemInterfaces.Game;
import systemInterfaces.IGame;
import systemInterfaces.IServer;

import org.apache.commons.collections4.OrderedBidiMap;
import org.apache.commons.collections4.bidimap.TreeBidiMap;

public class Mafia extends Game {

    TreeBidiMap<Integer, String> players;

    private boolean nightVoteInProgress = false;
    private boolean elimDayVoteInProgress = false;

    private boolean day = true;

    private ArrayList<Integer> ready;
    private ArrayList<Integer> votedStart;
    private String[] mafiaAtStart;
    private ArrayList<Integer> mafia;
    private ArrayList<Integer> innocentIDs;
    private ArrayList<Integer> elimDay;
    private ArrayList<Integer> save;

    private HashMap<Integer, String> eliminate;
    private ArrayList<Integer> dayVote;
    private ArrayList<Integer> nightVote;
    private Integer playerOnTrialID = null;

    private Timer dayElimTimer;
    private Timer nightVoteTimer;
    private Timer nightElimTimer;
    private Timer reminder;

    public Mafia(IServer server) {
	super(server);
	ready = new ArrayList<>();
	votedStart = new ArrayList<>();
	elimDay = new ArrayList<>();
	save = new ArrayList<>();
	nightVote = new ArrayList<>();
	dayVote = new ArrayList<>();

	rules = "send \"/ready\" to ready up for mafia \n" + "send \"/unready\" to cancel the above \n"
		+ "one there are at least 6 users ready, you can send \"/start\" \n" + "to vote to start the game\n"
		+ "once all users who are ready have voted to start, the game begins\n"
		+ "when game starts you will be assigned to either the innocent or\n"
		+ "mafia team, this will be send to you privately.\n"
		+ "If you are on the mafia team you will also be told who the other mafia players are.\n"
		+ "If you are innocent you are only informed of your own role\n"
		+ "All players are informed of how many mafia there are in the game "
		+ "which is a 1/3 (to nearest whole number) of the players."
		+ "Mafia has a day and night phase, where the game starts in the day\n"
		+ "In the day chat can be used normally, where at any point a player can\n"
		+ "start a vote to either\n eliminate another player or to change the game to night\n"
		+ "Once a vote is in session it needs to complete before another vote can be started\n"
		+ "there is a timer on how long a vote can last which when this runs out, only the votes\n"
		+ "that have been made are counted\n" + "The commands that are valid during the day are: \n"
		+ "\"elim [playername]\" in order to put a player on trial or if there is already a player on trial\n"
		+ "this command will add your vote to eliminate them.\n"
		+ "\"/save\" to vote to save the player who is currently on trial.\n "
		+ "If a player is eliminated, they are unable to vote and are muted so they can't continue to influence "
		+ "the game. They can still observer however" + "\"/night\" to vote to change the game to night\n"
		+ "\"/day\" to vote to keep the game as day\n"
		+ "If the vote to change the game to night is successful, the game is changed to night, where chat is\n"
		+ "disabled and the mafai attempt to eliminate an innocent. \n"
		+ "During the night the only valid command for the mafia is: \n"
		+ "\"/elim [playername]\" where this vote fails if the timer runs out or any of the mafia vote to eliminate\n an"
		+ "innocent who is different from the one that the previous mafia have voted for\n"
		+ "after the mafia vote during the night has completed the game is return to day\n"
		+ "The victory conditions for the teams are as follows: \n"
		+ "The innocent must eliminate all the mafia "
		+ "The mafia must eliminate enough of the innocents to be equal in number"
		+ "A player leaving the game will result in them being counted as eliminated.";

    }

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
    public void handleMessage(Message message, int origin) {

	String text = message.messageText;
	String remText = "";
	String command = "";

	if (text.indexOf(' ') < 0) {
	    remText = "";
	    command = message.messageText;
	} else {
	    command = message.messageText.substring(0, text.indexOf(' '));
	    remText = text.substring(text.indexOf(' ') + 1);
	}

	if (!gameInProgress) {

	    if (command.equals("/ready")) { // no trailing text after /ready
		if (!remText.isEmpty()) {
		    server.privateMessage("The command \"/ready\" cannnot have characters after it", origin);
		} else {
		    ready(origin);
		}

	    } else if (command.equals("/unready")) {
		if (!remText.isEmpty()) {
		    server.privateMessage("The command \"/unready\" cannnot have characters after it", origin);
		} else {
		    unready(origin);
		}

	    } else if (command.equals("/start")) {// no trailing text after
						  // /start

		if (!remText.isEmpty()) {
		    server.privateMessage("The command \"/start\" cannnot have characters after it", origin);
		} else {
		    voteStart(origin);
		}
	    } else {
		server.privateMessage(message.messageText + " does not contain a valid command", origin);
	    }

	} else {
	    if (day == true) {

		if (command.equals("/elim")) { // space between /elim and player
		    // name, no trailing text after
		    // player name
		    if (remText.isEmpty() || remText.contains(" ")) {
			server.privateMessage(
				"The command \"/elim\" needs to be followed by a player's name with no spaces/characters thereafter",
				origin);
		    } else {
			elimDayVote(remText, origin);
		    }

		} else if (command.equals("/save")) {
		    if (remText.isEmpty() || remText.contains(" ")) {
			server.privateMessage(
				"The command \"/save\" needs to be followed by a player's name with no spaces/characters thereafter",
				origin);
		    } else {
			saveVote(remText, origin);
		    }

		} else if (command.equals("/night")) {
		    if (!remText.isEmpty()) {
			server.privateMessage("The command \"/night\" cannnot have characters after it", origin);
		    } else {
			voteNight(origin);
		    }

		} else if (command.equals("/day")) {
		    if (!remText.isEmpty()) {
			server.privateMessage("The command \"/day\" cannnot have characters after it", origin);
		    } else {
			voteDay(origin);
		    }

		} else {
		    server.privateMessage(message.messageText + " does not contain a valid command", origin);
		}

	    } else {

		if (command.equals("/elim")) {
		    if (remText.isEmpty() || remText.contains(" ")) {
			server.privateMessage(
				"The command \"/elim\" needs to be followed by a player's name with no spaces/characters thereafter",
				origin);
		    } else {
			elimNightVote(remText, origin);
		    }
		} else {
		    server.privateMessage(
			    "The only valid command during the night is \"/elim\" followed a player's name (separated by a space)",
			    origin);
		}
	    }
	}

    }

    /**
     * The ready method
     * 
     * @param origin
     *            is the userID of type int for the person who sent the command
     *            message.
     */
    public void ready(int origin) { // enforce a max amount of players?, 16
				    // seemed to be the maximum allowed in
				    // original game
	if (!ready.contains(origin)) { // ensures players aren't added more than
				       // once

	    if (ready.size() < 16) {
		ready.add(origin);

		server.publicMessage(server.getUsername(origin) + " has been set to ready");
		server.publicMessage("number of players ready: " + ready.size());

		if (ready.size() >= 6) {

		    // not sure if this should be public or private
		    server.privateMessage("There are enough players to start the game. Use the command"
			    + " \"/start\" to vote to start", ready);

		    // votedStart = new ArrayList<>();
		}

		if (ready.size() == 16) {
		    server.publicMessage("The maximum amount of players for mafia has been reached");
		}

	    } else {
		server.privateMessage("The maximum amount of players (16) has already been reached.", origin);
	    }
	} else {
	    server.privateMessage("you are already set as ready", origin);
	}
    }

    private void unready(int origin) {
	if (ready.contains(origin)) { // ensures players aren't added more than
	    // once

	    int index = ready.indexOf(origin);
	    ready.remove(index);

	    server.publicMessage(server.getUsername((origin)) + " has unreadied, the number of players ready is now: "
		    + ready.size());
	    // possibly extend to mention which players are ready

	    if (ready.size() == 5) {

		server.privateMessage("There are no longer enough players to start the game, start vote has been reset",
			ready);

		votedStart.clear();
	    } else if (ready.size() >= 6) {
		if (votedStart.contains(origin)) {
		    int voteIndex = votedStart.indexOf(origin);
		    votedStart.remove(voteIndex);
		}
	    }

	    if (ready.size() < 6) {
		server.publicMessage("There needs to be " + (6 - ready.size())
			+ " more players to ready up before the game of Mafia can be " + "voted to start");
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
    private void voteStart(int origin) {
	// ensures that players aren't added more than once
	if (!votedStart.contains(origin) && ready.contains(origin) && ready.size() >= 6) {

	    votedStart.add(origin);
	    if (votedStart.size() == ready.size() && votedStart.size() >= 6) {
		server.publicMessage(
			"The game has started, it is currently day and the number of players is " + votedStart.size());
		gameStart();

	    } else {
		server.publicMessage("number of players that want to start: " + votedStart.size());
		// possibly extend message to mention who has voted to start
	    }

	} else if (votedStart.contains(origin)) {
	    server.privateMessage("you have already voted to start", origin);
	} else if (!ready.contains(origin)) {
	    server.privateMessage("you need to be set to ready before you can vote to start", origin);
	} else if (ready.size() < 6) {
	    server.privateMessage("There are not enough users ready to start the vote", origin);
	}
    }

    private void gameStart() {
	ready.clear();

	players = new TreeBidiMap<>();
	mafia = new ArrayList<>();
	innocentIDs = new ArrayList<>();

	for (int i = 0; i < votedStart.size(); i++) {
	    int id = votedStart.get(i);
	    players.put(id, server.getUsername(id));

	}

	int numMafia = (int) Math.round((double) votedStart.size() / 3);
	mafiaAtStart = new String[numMafia];

	for (int j = 0; j < numMafia; j++) {

	    int index = ThreadLocalRandom.current().nextInt(votedStart.size());
	    int id = votedStart.get(index);

	    mafia.add(id);
	    mafiaAtStart[j] = server.getUsername(id);
	    votedStart.remove(index);
	}

	for (int k = 0; k < votedStart.size(); k++) {

	    int id = votedStart.get(k);

	    innocentIDs.add(id);
	}

	votedStart.clear();

	server.privateMessage("you are an innocent", innocentIDs);

	String message = "you are one of the mafia, the mafia members (including " + "you) are:";

	for (int i = 0; i < numMafia; i++) {
	    message += " " + server.getUsername(mafia.get(i));
	}

	// currently uses the default toSting method for array. Also i think it
	// will display the id numbers rather than the players names - need to
	// convert if so would be good to only show only the other mafia members
	// rather than including the players own name in the list
	server.privateMessage(message, mafia);

	day = true;
	gameInProgress = true;
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
    private void elimDayVote(String player, int origin) {

	if (players.containsValue(player)) {
	    OrderedBidiMap<String, Integer> invPlayers = players.inverseBidiMap();

	    int playerID = invPlayers.get(player);

	    if (nightVoteInProgress) {
		server.privateMessage(
			"cannot vote to eliminate a player while there is a vote to change the game to night", origin);

	    } else if (playerID == origin) {
		server.privateMessage("you cannot vote for yourself", origin);

	    } else if (elimDay.contains(origin)) {
		server.privateMessage("you have already voted to start", origin);

	    } else if (!players.containsKey(origin)) {
		server.privateMessage("you are not in the game, so cannot participate", origin);

	    } else if (playerOnTrialID == null) {
		elimDayVoteInProgress = true;
		playerOnTrialID = playerID;
		elimDay.add(origin);
		server.publicMessage(players.get(origin) + " has started a vote to eliminate "
			+ server.getUsername(playerOnTrialID));

		TimerTask dayElimVoteTimeout = new TimerTask() {
		    @Override
		    public void run() {
			dayElimVoteTimeout();

		    }
		};

		TimerTask public5s = new TimerTask() {
		    @Override
		    public void run() {
			server.publicMessage("5 seconds left to vote");

		    }
		};

		dayElimTimer = new Timer();
		reminder = new Timer();

		reminder.schedule(public5s, 25000);
		dayElimTimer.schedule(dayElimVoteTimeout, 30000);

	    } else {
		if (playerOnTrialID != playerID) {
		    server.privateMessage("cannot vote for " + player + " while the vote for "
			    + server.getUsername(playerOnTrialID) + " is in progress", origin);
		} else {
		    if (save.contains(origin)) {
			int index = save.indexOf(origin);
			save.remove(index);
		    }

		    elimDay.add(origin);
		    server.publicMessage(
			    players.get(origin) + " has voted to eliminate " + server.getUsername(playerOnTrialID));
		    checkElim();
		}
	    }
	} else if (!players.containsValue(player)) {
	    server.privateMessage(player + " is not in this game/has already been eliminated", origin);

	}

    }

    /**
     * The checkElim method checks to see if there are enough votes to eliminate
     * the suscipiousPlayer. If the number of elim votes has passed half the
     * number of players left, then the suscipiousPlayer is eliminated. If this
     * is not the case then the method sends a public message stating who has
     * voted for the suscipiousPlayer.
     */
    private void checkElim() {

	if (elimDay.size() > players.size() / 2) {
	    eliminateDay();
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
	dayElimTimer.cancel();
	dayElimTimer.purge();

	reminder.cancel();
	reminder.purge();

	players.remove(playerOnTrialID);

	if (mafia.contains(playerOnTrialID)) {
	    int index = mafia.indexOf(playerOnTrialID);
	    mafia.remove(index);

	} else {
	    int index = innocentIDs.indexOf(playerOnTrialID);
	    innocentIDs.remove(index);
	}

	server.setPlayerMuted(playerOnTrialID, true);

	server.publicMessage(server.getUsername(playerOnTrialID) + " has been eliminated");

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
    private void saveVote(String player, int origin) { // suspicious player do
						       // need
						       // to vote for themselves

	if (players.containsValue(player)) {
	    OrderedBidiMap<String, Integer> invPlayers = players.inverseBidiMap();
	    int playerID = invPlayers.get(player);

	    if (playerOnTrialID == null) {
		server.privateMessage("you cannot vote to save someone when there is no-one on trial", origin);

	    } else {
		if (playerOnTrialID != playerID) {
		    server.privateMessage("cannot vote for " + player + " while the vote for "
			    + server.getUsername(playerOnTrialID) + " is in progress", origin);

		} else if (!players.containsKey(origin)) {
		    server.privateMessage("you are not in the game, so cannot participate", origin);

		} else {
		    if (elimDay.contains(origin)) {
			int index = elimDay.indexOf(origin);
			elimDay.remove(index);
		    }

		    save.add(origin);
		    server.publicMessage(
			    players.get(origin) + " has voted to save " + server.getUsername(playerOnTrialID));

		    checkSave();
		}
	    }
	} else if (!players.containsValue(player)) {
	    server.privateMessage(player + " is not in this game/has already been eliminated", origin);

	}

    }

    private void checkSave() {
	if (save.size() > players.size() / 2) {
	    saved();
	} else if (save.size() == players.size() / 2 && players.size() % 2 == 0) {
	    saved();
	}

    }

    private void saved() {

	dayElimTimer.cancel();
	dayElimTimer.purge();

	server.publicMessage(server.getUsername(playerOnTrialID) + " has been saved");

	playerOnTrialID = null;
	elimDay.clear();
	save.clear();
	elimDayVoteInProgress = false;

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

	} else if (!players.containsKey(origin)) {
	    server.privateMessage("you are not in the game, so cannot participate", origin);

	} else {
	    nightVoteInProgress = true;

	    if (dayVote.contains(origin)) {
		int index = dayVote.indexOf(origin);
		dayVote.remove(index);
	    }

	    nightVote.add(origin);
	    server.publicMessage(players.get(origin) + " has voted for it to change to night");

	    if (nightVote.size() == 1) {
		TimerTask nightVoteTimeout = new TimerTask() {

		    @Override
		    public void run() {
			server.publicMessage("Timer started, 20s left for night vote");
			nightVoteTimeout(); // this might need a catch

		    }
		};

		nightVoteTimer = new Timer();

		nightVoteTimer.schedule(nightVoteTimeout, 30000);
	    } else {
		checkNight();
	    }

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

	} else if (!players.containsKey(origin)) {
	    server.privateMessage("you are not in the game, so cannot participate", origin);

	} else {
	    if (nightVote.contains(origin)) {
		int index = nightVote.indexOf(origin);
		nightVote.remove(index);
	    }

	    dayVote.add(origin);
	    server.publicMessage(players.get(origin) + " has voted for it to remain day");
	    checkDay();
	}
    }

    /**
     * The checkDay method checks if the votes for to keep the game as day, are
     * in the majority. If so then the vote for night is unsuccessful and the
     * votes cleared.
     */
    private void checkDay() {

	if (dayVote.size() > players.size() / 2) {
	    day();
	} else if (dayVote.size() == players.size() / 2 && players.size() % 2 == 0) {
	    day();
	}
    }

    private void day() {
	nightVoteTimer.cancel();
	nightVoteTimer.purge();

	dayVote.clear();
	nightVote.clear();
	nightVoteInProgress = false;
	server.publicMessage("Majority vote reached for it to remain day");
    }

    /**
     * The checkNight method checks if the votes to change the game to night,
     * are in the majority. If so then the vote for night is successful, the
     * votes cleared, game changed to night and chat set to inactive.
     */
    private void checkNight() {

	if (nightVote.size() > players.size() / 2) {
	    night();
	}
    }

    private void night() {
	nightVoteTimer.cancel();
	nightVoteTimer.purge();

	nightVote.clear();
	dayVote.clear();
	server.publicMessage("Majority vote reached for it to change to night, all players now muted");

	server.setChatActive(false);
	day = false;
	nightVoteInProgress = false;
	eliminate = new HashMap<>();

	TimerTask nightElimTimeout = new TimerTask() {

	    @Override
	    public void run() {
		nightElimTimeout(); // this might need a catch

	    }
	};

	nightElimTimer = new Timer();

	nightElimTimer.schedule(nightElimTimeout, 20000);

    }

    /**
     * 
     * @param player
     *            is the name of the player being voted to be eliminated as type
     *            String
     * @param origin
     *            is the id of the player making the vote
     */
    private void elimNightVote(String player, int origin) {

	if (players.containsValue(player)) {
	    OrderedBidiMap<String, Integer> invPlayers = players.inverseBidiMap();
	    int playerID = invPlayers.get(player);

	    if (!mafia.contains(origin)) {
		server.privateMessage("Only mafia are active during the night", origin);

	    } else if (playerID == origin) {
		server.privateMessage("you cannot vote for yourself", origin);

	    } else if (mafia.contains(playerID)) { // wondering whether it's
						   // better to
						   // leave this out
		server.privateMessage("you cannot vote for another mafia player", origin);

	    } else if (eliminate.containsKey(origin)) {
		server.privateMessage("you can only vote for a valid player once", origin);

	    } else {
		eliminate.put(origin, player);
		checkElimNight(origin);
	    }
	} else if (!players.containsValue(player)) {
	    server.privateMessage(player + " is not in this game/has already been eliminated", origin);
	}
    }

    private void checkElimNight(int origin) {
	Object[] elimArr = eliminate.values().toArray();

	String[] votes = new String[elimArr.length];

	for (int i = 0; i < elimArr.length; i++) {
	    votes[i] = (String) elimArr[i];
	}

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

	    if (unique.equals(true)) {
		elimNight(victim);
	    } else {
		failNight();
	    }
	    eliminate.clear();

	    day = true;
	    server.setChatActive(true);
	}

    }

    private void elimNight(String victim) {
	nightElimTimer.cancel();
	nightElimTimer.purge();

	int victimID = server.getUserID(victim);

	players.remove(victimID);
	int index = innocentIDs.indexOf(victimID);
	innocentIDs.remove(index);
	server.privateMessage("you have been killed in the night", victimID);
	server.publicMessage("As dawn breaks, you wake to find that " + victim + " was killed last night");

	checkWin();
    }

    private void failNight() {
	nightElimTimer.cancel();
	nightElimTimer.purge();

	server.publicMessage("As dawn breaks, you wake to find that no-one was killed last night");

	server.privateMessage("In order to kill an innocent during the night, the same person needs"
		+ " to be chosen by all the mafia players", mafia);
    }

    private void checkWin() {
	if (mafia.size() == innocentIDs.size()) {
	    server.publicMessage("The Mafia win");
	    gameEnd();

	} else if (mafia.size() == 0) {
	    server.publicMessage("The innocents win");
	    gameEnd();
	}

    }

    private void gameEnd() { // could make public to force the game to end

	String message = "The mafia were:";

	for (int i = 0; i < mafiaAtStart.length; i++) {
	    message += " " + mafiaAtStart[i];
	}

	server.publicMessage(message);

	gameInProgress = false;
	players.clear();
	mafia.clear();
	innocentIDs.clear();
	server.unMuteAllPlayers();
    }

    public synchronized void dayElimVoteTimeout() {
	if (playerOnTrialID == null) {

	} else {
	    if (elimDay.size() > save.size()) {
		eliminateDay();
	    } else {
		saved();
	    }

	}
    }

    public synchronized void nightVoteTimeout() {
	if (nightVote.size() > dayVote.size()) {
	    night();
	} else {
	    day();
	}

    }

    public synchronized void nightElimTimeout() {

	server.privateMessage("Not all mafia voted before the timer so noone was killed during the night", mafia);

	eliminate.clear();
	day = true;
	server.setChatActive(true);

	failNight();
    }

    @Override
    public synchronized void handleLogout(int leaverID) {

	if (!gameInProgress) {
	    if (ready.contains(leaverID)) {
		unready(leaverID);
	    }
	    return;
	}

	String leaver = server.getUsername(leaverID);

	if (players.containsKey(leaverID)) {

	    server.publicMessage(leaver + " has been eliminated due to leaving the game");

	    if (elimDayVoteInProgress) {

		if (playerOnTrialID == leaverID) {
		    dayElimTimer.cancel();
		    dayElimTimer.purge();

		    playerOnTrialID = null;
		    elimDay.clear();
		    save.clear();
		    elimDayVoteInProgress = false;

		    server.publicMessage(leaver
			    + " leaving has caused the now redundant votes to eliminate/save them to be cancelled");

		} else if (elimDay.contains(leaverID)) {
		    elimDay.remove(leaverID);
		    checkSave();

		} else if (save.contains(leaverID)) {
		    save.remove(leaverID);
		    checkElim();
		}
	    } else if (nightVoteInProgress) {

		if (nightVote.contains(leaverID)) {
		    nightVote.remove(leaverID);
		    checkDay();

		} else if (dayVote.contains(leaverID)) {
		    dayVote.remove(leaverID);
		    checkNight();
		}
	    } else if (day == false) {
		if (eliminate.containsKey(leaver)) {

		    server.privateMessage(
			    leaver + " leaving the game has made them an invalid target, all votes need to be resubmitted",
			    mafia);

		    nightElimTimer.cancel();
		    nightElimTimer.purge();
		    eliminate.clear();

		    TimerTask nightElimTimeout = new TimerTask() {

			@Override
			public void run() {
			    nightElimTimeout(); // this might need a catch

			}
		    };

		    nightElimTimer = new Timer();

		    nightElimTimer.schedule(nightElimTimeout, 20000);

		} else if (mafia.contains(leaverID)) {

		    if (eliminate.containsKey(leaverID)) {
			eliminate.remove(leaverID);
		    }
		}
	    }

	    players.remove(leaverID);

	    if (innocentIDs.contains(leaverID)) {
		int index = innocentIDs.indexOf(leaverID);
		innocentIDs.remove(index);

	    } else {
		int index = innocentIDs.indexOf(leaverID);
		mafia.remove(index);
	    }
	}
    }

    /**
     * @return the players
     */
    public TreeBidiMap<Integer, String> getPlayers() {
	return players;
    }

    /**
     * @return the nightVoteInProgress
     */
    public boolean isNightVoteInProgress() {
	return nightVoteInProgress;
    }

    /**
     * @return the elimDayVoteInProgress
     */
    public boolean isElimDayVoteInProgress() {
	return elimDayVoteInProgress;
    }

    /**
     * @return the day
     */
    public boolean isDay() {
	return day;
    }

    /**
     * @return the gameInProgress
     */
    public boolean isGameInProgress() {
	return gameInProgress;
    }

    /**
     * @return the ready
     */
    public ArrayList<Integer> getReady() {
	return ready;
    }

    /**
     * @return the votedStart
     */
    public ArrayList<Integer> getVotedStart() {
	return votedStart;
    }

    /**
     * @return the mafiaAtStart
     */
    public String[] getMafiaAtStart() {
	return mafiaAtStart;
    }

    /**
     * @return the mafia
     */
    public ArrayList<Integer> getMafia() {
	return mafia;
    }

    /**
     * @return the innocentIDs
     */
    public ArrayList<Integer> getInnocentIDs() {
	return innocentIDs;
    }

    /**
     * @return the elimDay
     */
    public ArrayList<Integer> getElimDay() {
	return elimDay;
    }

    /**
     * @return the save
     */
    public ArrayList<Integer> getSave() {
	return save;
    }

    /**
     * @return the eliminate
     */
    public HashMap<Integer, String> getEliminate() {
	return eliminate;
    }

    /**
     * @return the dayVote
     */
    public ArrayList<Integer> getDayVote() {
	return dayVote;
    }

    /**
     * @return the nightVote
     */
    public ArrayList<Integer> getNightVote() {
	return nightVote;
    }

    /**
     * @return the playerOnTrialID
     */
    public Integer getPlayerOnTrialID() {
	return playerOnTrialID;
    }

    @Override
    public String getRules() {
	return rules;
    }

}

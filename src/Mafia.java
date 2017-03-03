import java.util.ArrayList;
import java.util.Random;

import messages.Message;
import systemInterfaces.IGame;
import systemInterfaces.IServer;

public class Mafia implements IGame {
    private IServer server;

    // enum for setting the day/night phases of the game
    // don't know why eclipse auto-formats it like this
    private enum phase {
	DAY, NIGHT
    };

    // array list containing the players who have used /ready
    private ArrayList<Integer> ready;
    private ArrayList<Integer> votedStart;
    private ArrayList<Integer> mafia; // might make type map
    private ArrayList<Integer> innocent; // might make type map

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

	} else if (command == "/save") {

	} else if (command == "/night") {

	} else if (command == "/day") {

	} else {

	}

    }

    private void unready(int origin) {
	// TODO Auto-generated method stub
	if (ready.contains(origin)) { // ensures players aren't added more than
	    // once
	    ready.remove(origin);

	    server.publicMessage("player: " + origin + "has unreadied, the number of players ready is now: " + ready.size());
	    // possibly extend to mention which players are ready

	    if (ready.size() == 5) {
		int[] readyArray = ready.stream().mapToInt(i -> i).toArray();

		server.privateMessage(
			"There are no longer enough players to start the game, start vote has been reset", readyArray);
	    }
	    
	    if (ready.size() <6){
		server.publicMessage("There needs to be " + (6 - ready.size()) + " players to ready up before the game of Mafia can be "
			+ "voted to start");
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

		//not sure if this should be public or private
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
					    // than
	    // once
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

    private void gameStart() {
	// TODO Auto-generated method stub
	ready.clear();

	int numMafia = Math.round(votedStart.size() / 3);

	for (int i = 0; i < numMafia; i++) {
	    int index = mafiaPicker.nextInt(votedStart.size());
	    mafia.add(votedStart.get(index));
	    votedStart.remove(index);
	}

	for (int j = 0; j < votedStart.size(); j++) {
	    innocent.add(votedStart.get(j));
	    votedStart.remove(j);
	}

	votedStart.clear();

	int[] innocentArray = innocent.stream().mapToInt(i -> i).toArray();
	server.privateMessage("you are an innocent", innocentArray);

	int[] mafiaArray = innocent.stream().mapToInt(i -> i).toArray();

	// currently uses the default toSting method for array. Also i think it
	// will
	// display the id numbers rather than the players names - need to
	// convert if so
	// would be good to only show only the other mafia members rather than
	// including
	// the players own name in the list
	server.privateMessage("you are one of the mafia, the mafia members (including " + "you) are " + mafiaArray,
		mafiaArray);
    }

}

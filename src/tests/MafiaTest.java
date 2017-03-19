package tests;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import Client.Client;
import GUIs.chatGame;
import Game.Mafia;
import Stubs.ServerStubJUnit;
import systemInterfaces.Game;

// apparently you can't junit test methods when it requires client and server
// Don't delete this class just yet incase it is needed later

public class MafiaTest {
    private ServerStubJUnit serverStub;

    public int port;
    public String host;
    public Mafia mafia;

    private int client1ID;
    private int client2ID;
    private int client3ID;
    private int client4ID;
    private int client5ID;
    private int client6ID;
    private int client7ID;
    private int client8ID;

    @Before
    public void setUp() {

	serverStub = new ServerStubJUnit(new String[] { "client1", "client2", "client3", "client4", "client5",
		"client6", "client7", "client8", "client9", "client10", "client11", "client12", "client13", "client14",
		"client15", "client16", "client17" });
	mafia = new Mafia(serverStub);
	serverStub.attachGameObject(mafia);
	// port = 8000;
	//
	// serverThread = new ServerThread(8000, 20);
	// serverThread.run();

	// currentUsers = server.getActiveClientIDs();
	// try {
	client1ID = serverStub.getUserID("client1");
	client2ID = serverStub.getUserID("client2");
	client3ID = serverStub.getUserID("client3");
	client4ID = serverStub.getUserID("client4");
	client5ID = serverStub.getUserID("client5");
	client6ID = serverStub.getUserID("client6");
	client7ID = serverStub.getUserID("client7");
	client8ID = serverStub.getUserID("client8");

    }

    public void gameStart6Players() {
	serverStub.sendCommand("/ready", "client1");
	serverStub.sendCommand("/ready", "client2");
	serverStub.sendCommand("/ready", "client3");
	serverStub.sendCommand("/ready", "client4");
	serverStub.sendCommand("/ready", "client5");
	serverStub.sendCommand("/ready", "client6");

	serverStub.sendCommand("/start", "client1");
	serverStub.sendCommand("/start", "client2");
	serverStub.sendCommand("/start", "client3");
	serverStub.sendCommand("/start", "client4");
	serverStub.sendCommand("/start", "client5");
	serverStub.sendCommand("/start", "client6");
    }

    public void successNightVote6Players() {
	gameStart6Players();

	serverStub.sendCommand("/night", "client6");
	serverStub.sendCommand("/night", "client2");
	serverStub.sendCommand("/night", "client4");
	serverStub.sendCommand("/night", "client5");
    }

    public void gameStart3Mafia() {
	serverStub.sendCommand("/ready", "client1");
	serverStub.sendCommand("/ready", "client2");
	serverStub.sendCommand("/ready", "client3");
	serverStub.sendCommand("/ready", "client4");
	serverStub.sendCommand("/ready", "client5");
	serverStub.sendCommand("/ready", "client6");
	serverStub.sendCommand("/ready", "client7");
	serverStub.sendCommand("/ready", "client8");

	serverStub.sendCommand("/start", "client1");
	serverStub.sendCommand("/start", "client2");
	serverStub.sendCommand("/start", "client3");
	serverStub.sendCommand("/start", "client4");
	serverStub.sendCommand("/start", "client5");
	serverStub.sendCommand("/start", "client6");
	serverStub.sendCommand("/start", "client7");
	serverStub.sendCommand("/start", "client8");
    }

    public void successNightVote3Mafia() {
	gameStart3Mafia();

	serverStub.sendCommand("/night", "client6");
	serverStub.sendCommand("/night", "client2");
	serverStub.sendCommand("/night", "client4");
	serverStub.sendCommand("/night", "client5");
	serverStub.sendCommand("/night", "client3");

    }

    public void end6PlayerInnocentWin() {
	int mafia1ID = (int) mafia.getMafia().get(0);
	int mafia2ID = (int) mafia.getMafia().get(1);
	String mafia1 = serverStub.getUsername(mafia1ID);
	String mafia2 = serverStub.getUsername(mafia2ID);

	int innocent1ID = (int) mafia.getInnocentIDs().get(0);
	int innocent2ID = (int) mafia.getInnocentIDs().get(1);
	int innocent3ID = (int) mafia.getInnocentIDs().get(2);
	int innocent4ID = (int) mafia.getInnocentIDs().get(3);
	String innocent1 = serverStub.getUsername(innocent1ID);
	String innocent2 = serverStub.getUsername(innocent2ID);
	String innocent3 = serverStub.getUsername(innocent3ID);
	String innocent4 = serverStub.getUsername(innocent4ID);

	serverStub.sendCommand("/elim " + mafia1, innocent1);
	serverStub.sendCommand("/elim " + mafia1, innocent2);
	serverStub.sendCommand("/elim " + mafia1, innocent3);
	serverStub.sendCommand("/elim " + mafia1, innocent4);

	serverStub.sendCommand("/elim " + mafia2, innocent1);
	serverStub.sendCommand("/elim " + mafia2, innocent2);
	serverStub.sendCommand("/elim " + mafia2, innocent3);
	serverStub.sendCommand("/elim " + mafia2, innocent4);

    }

    public void end6PlayerMafiaWin() {
	int mafia1ID = (int) mafia.getMafia().get(0);
	int mafia2ID = (int) mafia.getMafia().get(1);
	String mafia1 = serverStub.getUsername(mafia1ID);
	String mafia2 = serverStub.getUsername(mafia2ID);

	int innocent1ID = (int) mafia.getInnocentIDs().get(0);
	int innocent2ID = (int) mafia.getInnocentIDs().get(1);
	int innocent3ID = (int) mafia.getInnocentIDs().get(2);
	int innocent4ID = (int) mafia.getInnocentIDs().get(3);
	String innocent1 = serverStub.getUsername(innocent1ID);
	String innocent2 = serverStub.getUsername(innocent2ID);
	String innocent3 = serverStub.getUsername(innocent3ID);
	String innocent4 = serverStub.getUsername(innocent4ID);

	serverStub.sendCommand("/elim " + innocent1, mafia1);
	serverStub.sendCommand("/elim " + innocent1, mafia2);
	serverStub.sendCommand("/elim " + innocent1, innocent3);
	serverStub.sendCommand("/elim " + innocent1, innocent4);

	serverStub.sendCommand("/elim " + innocent2, mafia1);
	serverStub.sendCommand("/elim " + innocent2, mafia2);
	serverStub.sendCommand("/elim " + innocent2, innocent3);
	serverStub.sendCommand("/elim " + innocent2, innocent4);

    }

    /**
     * Test shows that the array containing the IDs of the active clients is not
     * empty at start.
     */
    @Test
    public void testActiveClients() {
	assertFalse(serverStub.getActiveClientIDs().isEmpty());
    }

    /**
     * Test shows that the /ready command correctly adds the user's id to the
     * ready array stored in the Mafia class.
     */
    @Test
    public void testReady() {
	serverStub.sendCommand("/ready", "client1");

	assertTrue(mafia.getReady().contains(client1ID));
    }

    @Test
    public void testReady2() {
	serverStub.sendCommand("/ready", "client1");
	serverStub.sendCommand("/ready", "client1");

	assertEquals(1, mafia.getReady().size());
    }
    
    @Test
    public void testReady3(){
	serverStub.sendCommand("/ready", "client1");
	serverStub.sendCommand("/ready", "client2");
	serverStub.sendCommand("/ready", "client3");
	serverStub.sendCommand("/ready", "client4");
	serverStub.sendCommand("/ready", "client5");
	serverStub.sendCommand("/ready", "client6");
	serverStub.sendCommand("/ready", "client7");
	serverStub.sendCommand("/ready", "client8");
	serverStub.sendCommand("/ready", "client9");
	serverStub.sendCommand("/ready", "client10");
	serverStub.sendCommand("/ready", "client11");
	serverStub.sendCommand("/ready", "client12");
	serverStub.sendCommand("/ready", "client13");
	serverStub.sendCommand("/ready", "client14");
	serverStub.sendCommand("/ready", "client15");
	serverStub.sendCommand("/ready", "client16");	
	serverStub.sendCommand("/ready", "client17");
	
	assertEquals(16, mafia.getReady().size());
	assertFalse(mafia.getReady().contains("client17"));
	
    }

    /**
     * Test shows that the /unready command correctly removes the user's id from
     * the ready array in the Mafia class.
     */
    @Test
    public void testUnready() {
	serverStub.sendCommand("/ready", "client1");
	serverStub.sendCommand("/unready", "client1");

	assertTrue(mafia.getReady().isEmpty());
    }

    @Test
    public void testUnready2() {
	serverStub.sendCommand("/ready", "client1");
	serverStub.sendCommand("/ready", "client2");
	serverStub.sendCommand("/unready", "client1");

	assertFalse(mafia.getReady().contains(client1ID));
    }
    
    @Test
    public void testUnready3() {
	serverStub.sendCommand("/unready", "client4");

	assertTrue(mafia.getReady().isEmpty());
    }

    @Test
    public void testUnready4() {
	serverStub.sendCommand("/ready", "client1");
	serverStub.sendCommand("/unready", "client2");

	assertTrue(mafia.getReady().contains(client1ID));
    }

    @Test
    public void testStart() {
	serverStub.sendCommand("/ready", "client1");
	serverStub.sendCommand("/start", "client1");

	assertFalse(mafia.getVotedStart().contains(client1ID));
    }

    @Test
    public void testStart2() {
	serverStub.sendCommand("/ready", "client1");
	serverStub.sendCommand("/ready", "client2");
	serverStub.sendCommand("/ready", "client3");
	serverStub.sendCommand("/ready", "client4");
	serverStub.sendCommand("/ready", "client5");
	serverStub.sendCommand("/ready", "client6");

	serverStub.sendCommand("/start", "client3");

	assertTrue(mafia.getVotedStart().contains(client3ID));

    }

    @Test
    public void testUnready5() {
	serverStub.sendCommand("/ready", "client1");
	serverStub.sendCommand("/ready", "client2");
	serverStub.sendCommand("/ready", "client3");
	serverStub.sendCommand("/ready", "client4");
	serverStub.sendCommand("/ready", "client5");
	serverStub.sendCommand("/ready", "client6");

	serverStub.sendCommand("/start", "client3");
	serverStub.sendCommand("/unready", "client3");

	assertTrue(mafia.getVotedStart().isEmpty());
	assertFalse(mafia.getReady().contains(client3ID));
    }

    @Test
    public void testUnready6() {
	serverStub.sendCommand("/ready", "client1");
	serverStub.sendCommand("/ready", "client2");
	serverStub.sendCommand("/ready", "client3");
	serverStub.sendCommand("/ready", "client4");
	serverStub.sendCommand("/ready", "client5");
	serverStub.sendCommand("/ready", "client6");

	serverStub.sendCommand("/start", "client3");
	serverStub.sendCommand("/start", "client5");
	serverStub.sendCommand("/unready", "client3");

	assertTrue(mafia.getVotedStart().isEmpty());
	assertFalse(mafia.getReady().contains(client3ID));
    }

    @Test
    public void testUnready7() {
	serverStub.sendCommand("/ready", "client1");
	serverStub.sendCommand("/ready", "client2");
	serverStub.sendCommand("/ready", "client3");
	serverStub.sendCommand("/ready", "client4");
	serverStub.sendCommand("/ready", "client5");
	serverStub.sendCommand("/ready", "client6");
	serverStub.sendCommand("/ready", "client7");

	serverStub.sendCommand("/start", "client3");
	serverStub.sendCommand("/start", "client5");
	serverStub.sendCommand("/unready", "client2");

	assertTrue(mafia.getVotedStart().contains(client5ID));
	assertTrue(mafia.getVotedStart().contains(client3ID));
	assertFalse(mafia.getReady().contains(client2ID));
    }

    @Test
    public void testStart3() {
	gameStart6Players();

	assertTrue(mafia.isGameInProgress());
	assertTrue(mafia.isDay());

	assertTrue(mafia.getReady().isEmpty());
	assertTrue(mafia.getVotedStart().isEmpty());
	assertEquals(2, mafia.getMafia().size());
	assertEquals(4, mafia.getInnocentIDs().size());
	assertEquals(6, mafia.getPlayers().size());

    }

    @Test
    public void testStart4() {
	serverStub.sendCommand("/ready", "client1");
	serverStub.sendCommand("/ready", "client2");
	serverStub.sendCommand("/ready", "client3");
	serverStub.sendCommand("/ready", "client4");
	serverStub.sendCommand("/ready", "client5");
	serverStub.sendCommand("/ready", "client6");
	serverStub.sendCommand("/ready", "client7");

	serverStub.sendCommand("/start", "client1");
	serverStub.sendCommand("/start", "client2");
	serverStub.sendCommand("/start", "client3");
	serverStub.sendCommand("/start", "client4");
	serverStub.sendCommand("/start", "client5");
	serverStub.sendCommand("/start", "client6");

	assertFalse(mafia.isGameInProgress());

    }

    @Test
    public void testElimDayVote() {
	gameStart6Players();
	serverStub.sendCommand("/elim client4", "client1");

	assertEquals(client4ID, (int) mafia.getPlayerOnTrialID());
	assertEquals(1, mafia.getElimDay().size());

    }

    @Test
    public void testElimDayVote2() {
	gameStart6Players();
	serverStub.sendCommand("/elim client4", "client1");
	serverStub.sendCommand("/elim client4", "client1");

	assertEquals(client4ID, (int) mafia.getPlayerOnTrialID());
	assertEquals(1, mafia.getElimDay().size());
	assertTrue(mafia.isElimDayVoteInProgress());

    }

    @Test
    public void testElimDayVote3() {
	gameStart6Players();
	serverStub.sendCommand("/elim client4", "client1");
	serverStub.sendCommand("/elim client2", "client3");

	assertEquals(client4ID, (int) mafia.getPlayerOnTrialID());
	assertEquals(1, mafia.getElimDay().size());
	assertTrue(mafia.isElimDayVoteInProgress());
    }

    @Test
    public void testSaveVote() {
	gameStart6Players();
	serverStub.sendCommand("/elim client4", "client1");
	serverStub.sendCommand("/save client4", "client3");

	assertEquals(client4ID, (int) mafia.getPlayerOnTrialID());
	assertEquals(1, mafia.getElimDay().size());
	assertEquals(1, mafia.getSave().size());
	assertTrue(mafia.isElimDayVoteInProgress());
    }

    @Test
    public void testSaveVote2() {
	gameStart6Players();
	serverStub.sendCommand("/elim client4", "client6");
	serverStub.sendCommand("/save client4", "client6");

	assertEquals(client4ID, (int) mafia.getPlayerOnTrialID());
	assertTrue(mafia.getElimDay().isEmpty());
	assertEquals(1, mafia.getSave().size());
	assertTrue(mafia.isElimDayVoteInProgress());
    }

    @Test
    public void testSaveVote3() {
	gameStart6Players();
	serverStub.sendCommand("/elim client4", "client1");
	serverStub.sendCommand("/elim client4", "client6");
	assertEquals(2, mafia.getElimDay().size());

	serverStub.sendCommand("/save client4", "client6");

	assertEquals(client4ID, (int) mafia.getPlayerOnTrialID());
	assertEquals(1, mafia.getElimDay().size());
	assertEquals(1, mafia.getSave().size());
	assertTrue(mafia.isElimDayVoteInProgress());
    }

    @Test
    public void testSaveVote4() {
	gameStart6Players();
	serverStub.sendCommand("/save client2", "client5");

	assertTrue(mafia.getPlayerOnTrialID() == null);
	assertTrue(mafia.getElimDay().isEmpty());
	assertTrue(mafia.getSave().isEmpty());
	assertFalse(mafia.isElimDayVoteInProgress());
    }

    @Test
    public void testSaveVote5() {
	gameStart6Players();
	serverStub.sendCommand("/elim client4", "client6");
	serverStub.sendCommand("/save client4", "client6");

	assertEquals(client4ID, (int) mafia.getPlayerOnTrialID());
	assertTrue(mafia.getElimDay().isEmpty());
	assertEquals(1, mafia.getSave().size());
	assertTrue(mafia.isElimDayVoteInProgress());
    }

    @Test
    public void testElimDayVote4() {
	gameStart6Players();
	serverStub.sendCommand("/elim client1", "client6");
	serverStub.sendCommand("/elim client1", "client2");
	serverStub.sendCommand("/elim client1", "client4");

	assertEquals(client1ID, (int) mafia.getPlayerOnTrialID());
	assertEquals(3, mafia.getElimDay().size());
	assertTrue(mafia.isElimDayVoteInProgress());
    }

    @Test
    public void testElimDayVote5() {
	gameStart6Players();
	serverStub.sendCommand("/elim client1", "client6");
	serverStub.sendCommand("/elim client1", "client2");
	serverStub.sendCommand("/elim client1", "client4");
	serverStub.sendCommand("/elim client1", "client5");

	assertTrue(mafia.getPlayerOnTrialID() == null);
	assertFalse(mafia.getPlayers().containsKey(client1ID));
	assertFalse(mafia.getInnocentIDs().contains(client1ID));
	assertFalse(mafia.getMafia().contains(client1ID));
	assertFalse(mafia.isElimDayVoteInProgress());
	assertTrue(serverStub.getMutedPlayers().contains("client1"));
    }

    @Test
    public void testElimDayVote6() {
	gameStart6Players();
	serverStub.sendCommand("/night", "client3");
	serverStub.sendCommand("/elim client1", "client6");

	assertEquals(1, mafia.getNightVote().size());
	assertTrue(mafia.getElimDay().isEmpty());
	assertFalse(mafia.isElimDayVoteInProgress());
    }

    @Test
    public void testElimDayVote7() {
	gameStart6Players();
	serverStub.sendCommand("/elim client7", "client6");

	assertTrue(mafia.getElimDay().isEmpty());
	assertFalse(mafia.isElimDayVoteInProgress());
    }
    
    @Test
    public void testElimDayVote8() {
	gameStart6Players();
	serverStub.sendCommand("/elim client6", "client6");

	assertTrue(mafia.getElimDay().isEmpty());
	assertFalse(mafia.isElimDayVoteInProgress());
    }
    
    

    @Test
    public void testSaveVote6() {
	gameStart6Players();
	serverStub.sendCommand("/elim client4", "client6");
	serverStub.sendCommand("/save client4", "client3");
	serverStub.sendCommand("/save client4", "client5");
	serverStub.sendCommand("/save client4", "client2");

	assertEquals(client4ID, (int) mafia.getPlayerOnTrialID());
	assertEquals(1, mafia.getElimDay().size());
	assertEquals(3, mafia.getSave().size());
	assertTrue(mafia.isElimDayVoteInProgress());
    }

    @Test
    public void testSaveVote7() {
	gameStart6Players();

	serverStub.sendCommand("/elim client4", "client6");
	serverStub.sendCommand("/save client4", "client3");
	serverStub.sendCommand("/save client4", "client5");
	serverStub.sendCommand("/save client4", "client2");
	serverStub.sendCommand("/save client4", "client1");

	assertTrue(mafia.getPlayerOnTrialID() == null);
	assertTrue(mafia.getElimDay().isEmpty());
	assertTrue(mafia.getSave().isEmpty());
	assertFalse(mafia.isElimDayVoteInProgress());
    }

    @Test
    public void testSaveVote8() {
	gameStart6Players();
	serverStub.sendCommand("/save client7", "client2");

	assertTrue(mafia.getPlayerOnTrialID() == null);
	assertTrue(mafia.getElimDay().isEmpty());
	assertTrue(mafia.getSave().isEmpty());
	assertFalse(mafia.isElimDayVoteInProgress());
    }

    @Test
    public void testNightVote() {
	gameStart6Players();

	serverStub.sendCommand("/night", "client1");

	assertTrue(mafia.isDay());
	assertEquals(1, mafia.getNightVote().size());
	assertTrue(mafia.isNightVoteInProgress());

    }

    @Test
    public void testNightVote2() {
	gameStart6Players();

	serverStub.sendCommand("/night", "client1");
	serverStub.sendCommand("/night", "client1");

	assertTrue(mafia.isDay());
	assertEquals(1, mafia.getNightVote().size());

    }

    @Test
    public void testDayVote() {
	gameStart6Players();

	serverStub.sendCommand("/night", "client1");
	serverStub.sendCommand("/day", "client3");

	assertEquals(1, mafia.getNightVote().size());
	assertEquals(1, mafia.getDayVote().size());
    }

    @Test
    public void testDayVote2() {
	gameStart6Players();

	serverStub.sendCommand("/night", "client1");
	serverStub.sendCommand("/night", "client6");
	assertEquals(2, mafia.getNightVote().size());

	serverStub.sendCommand("/day", "client6");

	assertEquals(1, mafia.getNightVote().size());
	assertEquals(1, mafia.getDayVote().size());
    }

    @Test
    public void testDayVote3() {
	gameStart6Players();

	serverStub.sendCommand("/day", "client5");

	assertTrue(mafia.getNightVote().isEmpty());
	assertTrue(mafia.getDayVote().isEmpty());
	assertFalse(mafia.isNightVoteInProgress());
    }

    @Test
    public void testDayVote4() {
	gameStart6Players();

	serverStub.sendCommand("/night", "client6");
	serverStub.sendCommand("/day", "client6");

	assertEquals(0, mafia.getNightVote().size());
	assertEquals(1, mafia.getDayVote().size());
    }

    @Test
    public void testNightVote3() {
	gameStart6Players();

	serverStub.sendCommand("/night", "client6");
	serverStub.sendCommand("/night", "client2");
	serverStub.sendCommand("/night", "client4");

	assertTrue(mafia.isDay());
	assertEquals(3, mafia.getNightVote().size());
    }

    @Test
    public void testNightVote4() {
	gameStart6Players();

	serverStub.sendCommand("/night", "client6");
	serverStub.sendCommand("/night", "client2");
	serverStub.sendCommand("/night", "client4");
	serverStub.sendCommand("/night", "client5");

	assertFalse(mafia.isDay());
	assertTrue(mafia.getDayVote().isEmpty());
	assertTrue(mafia.getNightVote().isEmpty());
	assertFalse(mafia.isNightVoteInProgress());
	assertFalse(serverStub.getActiveChat());
    }

    @Test
    public void testDayVote5() {
	gameStart6Players();

	serverStub.sendCommand("/night", "client6");
	serverStub.sendCommand("/day", "client3");
	serverStub.sendCommand("/day", "client5");
	serverStub.sendCommand("/day", "client2");

	assertTrue(mafia.isDay());
	assertEquals(3, mafia.getDayVote().size());
	assertEquals(1, mafia.getNightVote().size());
	assertTrue(mafia.isNightVoteInProgress());
    }

    @Test
    public void testDayVote6() {
	gameStart6Players();

	serverStub.sendCommand("/night", "client6");
	serverStub.sendCommand("/day", "client3");
	serverStub.sendCommand("/day", "client5");
	serverStub.sendCommand("/day", "client2");
	serverStub.sendCommand("/day", "client1");

	assertTrue(mafia.isDay());
	assertTrue(mafia.getDayVote().isEmpty());
	assertTrue(mafia.getNightVote().isEmpty());
	assertFalse(mafia.isNightVoteInProgress());
    }

    @Test
    public void testNightVote5() {
	gameStart6Players();

	serverStub.sendCommand("/elim client1", "client6");
	serverStub.sendCommand("/night", "client3");

	assertEquals(1, mafia.getElimDay().size());
	assertTrue(mafia.getNightVote().isEmpty());
	assertFalse(mafia.isNightVoteInProgress());
    }

    @Test
    public void testElimNightVote() {
	successNightVote6Players();

	int innocentID = (int) mafia.getInnocentIDs().get(0);
	String innocent = serverStub.getUsername(innocentID);

	int mafia1ID = (int) mafia.getMafia().get(0);
	int mafia2ID = (int) mafia.getMafia().get(1);
	String mafia1 = serverStub.getUsername(mafia1ID);
	String mafia2 = serverStub.getUsername(mafia2ID);

	serverStub.sendCommand("/elim " + innocent, mafia1);
	serverStub.sendCommand("/elim " + innocent, mafia2);

	assertTrue(mafia.isDay());
	assertFalse(mafia.getPlayers().containsValue(innocent));
	assertFalse(mafia.getInnocentIDs().contains(innocent));
	assertTrue(mafia.getEliminate().isEmpty());
	assertTrue(serverStub.getActiveChat());
    }

    @Test
    public void testElimNightVote2() {
	successNightVote6Players();

	int mafia1ID = (int) mafia.getMafia().get(0);
	String mafia1 = serverStub.getUsername(mafia1ID);

	serverStub.sendCommand("/elim " + mafia1, mafia1);

	assertFalse(mafia.isDay());
	assertTrue(mafia.getEliminate().isEmpty());
    }

    @Test
    public void testElimNightVote3() {
	successNightVote6Players();

	int innocentID = (int) mafia.getInnocentIDs().get(0);
	String innocent = serverStub.getUsername(innocentID);

	int mafia1ID = (int) mafia.getMafia().get(0);
	int mafia2ID = (int) mafia.getMafia().get(1);
	String mafia1 = serverStub.getUsername(mafia1ID);
	String mafia2 = serverStub.getUsername(mafia2ID);

	serverStub.sendCommand("/elim " + mafia1, mafia2);
	serverStub.sendCommand("/elim " + innocent, mafia1);

	assertFalse(mafia.isDay());
	assertFalse(mafia.getInnocentIDs().contains(mafia1ID));
	assertTrue(mafia.getPlayers().containsValue(innocent));
    }

    @Test
    public void testElimNightVote4() {
	successNightVote6Players();

	int innocent1ID = (int) mafia.getInnocentIDs().get(0);
	int innocent2ID = (int) mafia.getInnocentIDs().get(1);
	String innocent1 = serverStub.getUsername(innocent1ID);
	String innocent2 = serverStub.getUsername(innocent2ID);

	int mafia1ID = (int) mafia.getMafia().get(0);
	int mafia2ID = (int) mafia.getMafia().get(1);
	String mafia1 = serverStub.getUsername(mafia1ID);
	String mafia2 = serverStub.getUsername(mafia2ID);

	serverStub.sendCommand("/elim " + innocent1, mafia1);
	serverStub.sendCommand("/elim " + innocent2, mafia2);

	assertTrue(mafia.isDay());
	assertTrue(mafia.getPlayers().containsValue(innocent1));
	assertTrue(mafia.getPlayers().containsValue(innocent2));
	assertTrue(mafia.getInnocentIDs().contains(innocent1ID));
	assertTrue(mafia.getInnocentIDs().contains(innocent2ID));
	assertTrue(mafia.getEliminate().isEmpty());
	assertTrue(serverStub.getActiveChat());
    }

    @Test
    public void testElimNightVote5() {
	successNightVote3Mafia();

	int innocent1ID = (int) mafia.getInnocentIDs().get(0);
	int innocent2ID = (int) mafia.getInnocentIDs().get(1);
	String innocent1 = serverStub.getUsername(innocent1ID);
	String innocent2 = serverStub.getUsername(innocent2ID);

	int mafia1ID = (int) mafia.getMafia().get(0);
	int mafia2ID = (int) mafia.getMafia().get(1);
	int mafia3ID = (int) mafia.getMafia().get(2);
	String mafia1 = serverStub.getUsername(mafia1ID);
	String mafia2 = serverStub.getUsername(mafia2ID);
	String mafia3 = serverStub.getUsername(mafia3ID);

	serverStub.sendCommand("/elim " + innocent1, mafia1);
	serverStub.sendCommand("/elim " + innocent2, mafia2);
	serverStub.sendCommand("/elim " + innocent2, mafia3);

	assertTrue(mafia.isDay());
	assertTrue(mafia.getPlayers().containsValue(innocent1));
	assertTrue(mafia.getPlayers().containsValue(innocent2));
	assertTrue(mafia.getInnocentIDs().contains(innocent1ID));
	assertTrue(mafia.getInnocentIDs().contains(innocent2ID));
	assertTrue(mafia.getEliminate().isEmpty());
	assertTrue(serverStub.getActiveChat());
    }

    @Test
    public void gameEnd() {
	gameStart6Players();
	end6PlayerInnocentWin();

	assertFalse(mafia.isGameInProgress());
	assertTrue(serverStub.getMutedPlayers().isEmpty());
	assertTrue(serverStub.getActiveChat());
    }

    @Test
    public void gameEnd2() {
	gameStart6Players();
	end6PlayerMafiaWin();

	assertFalse(mafia.isGameInProgress());
	assertTrue(serverStub.getMutedPlayers().isEmpty());
	assertTrue(serverStub.getActiveChat());
    }

    @Test
    public void testMafiaNum() {
	gameStart6Players();
	assertEquals(2, mafia.getMafia().size());
	end6PlayerInnocentWin();

	gameStart3Mafia();
	assertEquals(3, mafia.getMafia().size());
    }

    @Test
    public void testMafiaRandom() {

	boolean random = true;

	gameStart6Players();
	String mafia1 = mafia.getMafia().toString();
	end6PlayerInnocentWin();

	gameStart6Players();
	String mafia2 = mafia.getMafia().toString();
	end6PlayerInnocentWin();

	gameStart6Players();
	String mafia3 = mafia.getMafia().toString();
	end6PlayerInnocentWin();

	System.out.println(mafia1);
	System.out.println(mafia2);
	System.out.println(mafia3);

	if (mafia1.equals(mafia2) && mafia2.equals(mafia3)) {
	    random = false;
	}

	assertTrue(random);
    }
}

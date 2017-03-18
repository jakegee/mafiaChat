import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import Client.Client;
import GUIs.chatGame;
import Stubs.ServerStubJUnit;
import systemInterfaces.Game;

// apparently you can't junit test methods when it requires client and server
// Don't delete this class just yet incase it is needed later

public class MafiaTest {
    private ServerStubJUnit serverStub;

    // private ServerThread serverThread;
    //
    // private Client client1;
    // private Client client2;
    // private Client client3;
    // private Client client4;
    // private Client client5;
    // private Client client6;

    // private ArrayList<Integer> activeClientIDs;

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

    @Before
    public void setUp() {

	serverStub = new ServerStubJUnit(new String[] { "client1", "client2", "client3", "client4", "client5", "client6", "client7" });
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
	//
	// client1.createAccountPacket("client1", "client1", " ", " ");
	// client2.createAccountPacket("client2", "client2", " ", " ");
	// client3.createAccountPacket("client3", "client3", " ", " ");
	// client4.createAccountPacket("client4", "client4", " ", " ");
	// client5.createAccountPacket("client5", "client5", " ", " ");
	// client6.createAccountPacket("client6", "client6", " ", " ");
	//
	// client1.createLoginPacket("client1", "client1");
	// client2.createLoginPacket("client2", "client2");
	// client3.createLoginPacket("client3", "client3");
	// client4.createLoginPacket("client4", "client4");
	// client5.createLoginPacket("client5", "client5");
	// client6.createLoginPacket("client6", "client6");
	//
	// activeClientIDs = server.getActiveClientIDs();
	//
	// } catch (IOException e) {
	// e.printStackTrace();
	// }

    }

    /**
     * Test shows that the array containing the IDs of the active clients is not
     * empty at start.
     */
    @Test
    public void test1() {
	assertFalse(serverStub.getActiveClientIDs().isEmpty());
    }

    /**
     * Test shows that the /ready command correctly adds the user's id to the
     * ready array stored in the Mafia class.
     */
    @Test
    public void test2() {
	serverStub.sendCommand("/ready", "client1");

	assertTrue(mafia.ready.contains(client1ID));
    }

    /**
     * Test shows that the /unready command correctly removes the user's id from
     * the ready array in the Mafia class.
     */
    @Test
    public void test3() {
	serverStub.sendCommand("/ready", "client1");
	serverStub.sendCommand("/unready", "client1");

	assertTrue(mafia.ready.isEmpty());
    }

    @Test
    public void test4() {
	serverStub.sendCommand("/ready", "client1");
	serverStub.sendCommand("/ready", "client2");
	serverStub.sendCommand("/unready", "client1");

	assertFalse(mafia.ready.contains(client1ID));
    }

    @Test
    public void test5() {
	serverStub.sendCommand("/ready", "client1");
	serverStub.sendCommand("/ready", "client1");

	assertEquals(1, mafia.ready.size());
    }

    @Test
    public void test6() {
	serverStub.sendCommand("/ready", "client1");
	serverStub.sendCommand("/unready", "client2");

	assertTrue(mafia.ready.contains(client1ID));
    }

    @Test
    public void test7() {
	serverStub.sendCommand("/ready", "client1");
	serverStub.sendCommand("/start", "client1");

	assertFalse(mafia.votedStart.contains(client1ID));
    }

    @Test
    public void test8() {
	serverStub.sendCommand("/ready", "client1");
	serverStub.sendCommand("/ready", "client2");
	serverStub.sendCommand("/ready", "client3");
	serverStub.sendCommand("/ready", "client4");
	serverStub.sendCommand("/ready", "client5");
	serverStub.sendCommand("/ready", "client6");
	
	serverStub.sendCommand("/start", "client3");
	
	assertTrue(mafia.votedStart.contains(client3ID));

    }
    
    @Test
    public void test9() {
	serverStub.sendCommand("/ready", "client1");
	serverStub.sendCommand("/ready", "client2");
	serverStub.sendCommand("/ready", "client3");
	serverStub.sendCommand("/ready", "client4");
	serverStub.sendCommand("/ready", "client5");
	serverStub.sendCommand("/ready", "client6");
	
	serverStub.sendCommand("/start", "client3");
	serverStub.sendCommand("/unready", "client3");
	
	assertFalse(mafia.votedStart.contains(client3ID));
	assertFalse(mafia.ready.contains(client3ID));
    }
    
    @Test
    public void test10() {
	serverStub.sendCommand("/ready", "client1");
	serverStub.sendCommand("/ready", "client2");
	serverStub.sendCommand("/ready", "client3");
	serverStub.sendCommand("/ready", "client4");
	serverStub.sendCommand("/ready", "client5");
	serverStub.sendCommand("/ready", "client6");
	
	serverStub.sendCommand("/start", "client3");
	serverStub.sendCommand("/start", "client5");
	serverStub.sendCommand("/unready", "client3");
	
	assertTrue(mafia.votedStart.isEmpty());
	assertFalse(mafia.ready.contains(client3ID));
    }
    
    @Test
    public void test11(){
	serverStub.sendCommand("/ready", "client1");
	serverStub.sendCommand("/ready", "client2");
	serverStub.sendCommand("/ready", "client3");
	serverStub.sendCommand("/ready", "client4");
	serverStub.sendCommand("/ready", "client5");
	serverStub.sendCommand("/ready", "client6");
	serverStub.sendCommand("/ready", "client7");
	
	serverStub.sendCommand("/start", "client3");
	serverStub.sendCommand("/start", "client5");
	serverStub.sendCommand("/unready", "client3");
	
	assertTrue(mafia.votedStart.contains(client5ID));
	assertFalse(mafia.votedStart.contains(client3ID));
	assertFalse(mafia.ready.contains(client3ID));
    }
    
    @Test
    public void test12(){
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
	
	assertTrue(mafia.gameInProgress);
	
    }

    // @After
    // public void logout() {
    //
    // try {
    // client1.createLogoutPacket();
    // client2.createLogoutPacket();
    // client3.createLogoutPacket();
    // client4.createLogoutPacket();
    // client5.createLogoutPacket();
    // client6.createLogoutPacket();
    //
    // serverThread.interrupt();
    //
    // } catch (IOException e) {
    // e.printStackTrace();
    // }
    // }

}

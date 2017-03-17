import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import Client.Client;
import GUIs.chatGame;

// apparently you can't junit test methods when it requires client and server
// Don't delete this class just yet incase it is needed later

public class MafiaTest {
    private Server server;
    private ArrayList<String> currentUsers;
    
    private ServerThread serverThread;

    private Client client1;
    private Client client2;
    private Client client3;
    private Client client4;
    private Client client5;
    private Client client6;

    private ArrayList<Integer> activeClientIDs;

    public int port;
    public String host;

    @Before
    public void setUp() {
	port = 8000;
	
	serverThread = new ServerThread(8000, 20);
	serverThread.run();

	
	// currentUsers = server.getActiveClientIDs();
	try {
	    client1 = new Client(port, "localhost");
	    client2 = new Client(port, "localhost");
	    client3 = new Client(port, "localhost");
	    client4 = new Client(port, "localhost");
	    client5 = new Client(port, "localhost");
	    client6 = new Client(port, "localhost");

	    client1.createAccountPacket("client1", "client1", " ", " ");
	    client2.createAccountPacket("client2", "client2", " ", " ");
	    client3.createAccountPacket("client3", "client3", " ", " ");
	    client4.createAccountPacket("client4", "client4", " ", " ");
	    client5.createAccountPacket("client5", "client5", " ", " ");
	    client6.createAccountPacket("client6", "client6", " ", " ");

	    client1.createLoginPacket("client1", "client1");
	    client2.createLoginPacket("client2", "client2");
	    client3.createLoginPacket("client3", "client3");
	    client4.createLoginPacket("client4", "client4");
	    client5.createLoginPacket("client5", "client5");
	    client6.createLoginPacket("client6", "client6");

	    activeClientIDs = server.getActiveClientIDs();

	} catch (IOException e) {
	    e.printStackTrace();
	}

    }

    @Test
    public void test1() {
	assertFalse(activeClientIDs.isEmpty());

    }


    @After
    public void logout() {

	try {
	    client1.createLogoutPacket();
	    client2.createLogoutPacket();
	    client3.createLogoutPacket();
	    client4.createLogoutPacket();
	    client5.createLogoutPacket();
	    client6.createLogoutPacket();
	    
	    serverThread.interrupt();

	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

}

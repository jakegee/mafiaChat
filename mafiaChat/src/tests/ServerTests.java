package tests;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import Stubs.ClientStub;
import Stubs.DatabaseStub;
import messages.ServerMessage;

/**
 * Test suite for testing the server class using a modified variant
 * of the client. The loaded server makes use of a DatabaseStub instance
 * as well as a GameStub. Therefore the GameandServerStubTests as well as
 * the DatabaseStubTest should be confirmed to be working before running
 * these tests.
 * 
 * The test revolves around 3 clients, two of which are in chat, and one
 * who is trying to login
 * 
 * @author Team Nice
 * @version 18-03-2017
 *
 */
public class ServerTests {
	private ServerThread sThread;
	private ClientStub cStub;
	private int port = 4912;
	
	@Before
    public void setUp() throws IOException {
		sThread = new ServerThread(port, 20, "debug");
		sThread.start();
		
		this.cStub = new ClientStub("localhost", port);
		/*
		cStub3 =  new ClientStub("localhost", port);
		cStub3.createLoginPacket("testUser3", "123");
		cStub3.spawnListenerThread();
		*/
	}
	/*
	users.add(new String[]{"testUser1", "abc", "the answer is password", "password"});
	users.add(new String[]{"testUser2", "def", "the answer is testhint", "testhint"});
	users.add(new String[]{"testUser3", "123", "the answer is other", "other"});
	*/
	
	/**
	 * Test to determine if a valid login message returns a success
	 * response
	 * @throws IOException 
	 */
	@Test
	public void test1() throws IOException {
		ServerMessage message = cStub.createLoginPacket("testUser2", "def");
		assertEquals("Welcome testUser2", message.messageText);
	}
	
	/**
	 * Test to determine if an invalid login message returns a failure response
	 * if the user is already logged in
	 * @throws IOException 
	 */
	@Test
	public void test2() throws IOException {
		ServerMessage message = cStub.createLoginPacket("testUser2", "def");
		assertEquals("User already logged in", message.messageText);
	}
	
	/**
	 * Test to determine if an invalid login message returns a failure response
	 * if an invalid user is passed in
	 * @throws IOException 
	 */
	@Test
	public void test3() throws IOException {
		ServerMessage message = cStub.createLoginPacket("invalidUser", "abc");
		assertEquals("User: invalidUser is not known to the system", message.messageText);
	}
	
	/**
	 * Test to determine if an invalid login message returns a failure response
	 * if an invalid password is passed in 
	 * @throws IOException 
	 */
	@Test
	public void test4() throws IOException {
		ServerMessage message = cStub.createLoginPacket("testUser1", "abcdef");
		assertEquals("Invalid password", message.messageText);
	}
	
	/**
	 * Test to determine if register returns a success response 
	 * if a valid user is passed in
	 * @throws IOException 
	 */
	@Test
	public void test5() throws IOException {
		ServerMessage message = cStub.createAccountPacket("newUser", "abc", "ques", "ans");
		assertEquals("Register Successful", message.messageText);
	}
	
	/**
	 * Test to determine if register returns a failure response if
	 * a taken username is passed in
	 * @throws IOException 
	 */
	@Test
	public void test6() throws IOException {
		ServerMessage message = cStub.createAccountPacket("testUser1", "abc", "ques", "ans");
		assertEquals("testUser1 is already taken", message.messageText);
	}
	
	/**
	 * Test to determine if passwordHint returns the password of a user
	 * if the security question is requested
	 * @throws IOException 
	 */
	@Test
	public void test7() throws IOException {
		ServerMessage message = cStub.forgottenPassword("testUser1", null);
		assertEquals("the answer is password", message.messageText);
	}
	
	/**
	 * Test to determine if a success message is sent if the answer is 
	 * submitted correctly
	 * @throws IOException 
	 */
	@Test
	public void test8() throws IOException {
		ServerMessage message = cStub.forgottenPassword("testUser1", "password");
		assertEquals("Password: abc", message.messageText);
	}
	
	/**
	 * Test to determine if a failure message is sent if an invalid user is
	 * submitted
	 * @throws IOException 
	 */
	@Test
	public void test9() throws IOException {
		ServerMessage message = cStub.forgottenPassword("invalidUser", null);
		assertEquals("User: invalidUser is not known to the system", message.messageText);
	}
	
	/**
	 * Test to determine if a failure message is sent if an invalid answer
	 * is submitted
	 * @throws IOException 
	 */
	@Test
	public void test10() throws IOException {
		ServerMessage message = cStub.forgottenPassword("testUser1", "notpassword");
		assertEquals("Invalid answer", message.messageText);
	}	
	
	/**
	 * Test to determine if a failure message is sent if an invalid answer
	 * is submitted
	 * @throws IOException 
	 */
	@Test
	public void test11() throws IOException {
		ServerMessage message = cStub.forgottenPassword("testUser1", "notpassword");
		assertEquals("Invalid answer", message.messageText);
	}	

	/**
	 * Test to determine if message are successfully relayed back to the client
	 * @throws IOException 
	 */
	@Test
	public void test12() throws IOException {
		cStub.spawnListenerThread();
		cStub.setCommandMsg("hi all");
		assertEquals("hi all",
				cStub.getChatMessages().get(cStub.getChatMessages().size()));
	}	
	
	/**
	 * Test to determine if commands are successfully sent to the game class
	 * @throws IOException 
	 */
	@Test
	public void test13() throws IOException {
		cStub.spawnListenerThread();
		cStub.setCommandMsg("/public test public message");
		assertEquals(" test public message sent by user 0",
				cStub.getPublicMessages().get(cStub.getPublicMessages().size()));
	}	
	
	/**
	 * Test to determine if private messages are successfully sent from the game
	 * class to the server
	 * @throws IOException 
	 */
	@Test
	public void test14() throws IOException {
		cStub.spawnListenerThread();
		cStub.setCommandMsg("/private testUser1 poop");
		assertEquals(" test private message sent by user 0",
				cStub.getPrivateMessages().get(cStub.getPrivateMessages().size()));
		
	}	
	
	
	//users.add(new String[]{"testUser1", "abc", "the answer is password", "password"});
	
}

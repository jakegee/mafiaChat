package tests;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import Stubs.DatabaseStub;
import Stubs.GameStub;
import Stubs.ServerStubJUnit;
import messages.Message;

/**
 * Suite for testing the ServerStubJUnit classes and GameStub class.
 * As they are tied together, and require each other, they are included
 * together
 * 
 * @author Team Nice
 * @version 18-03-2017
 */
public class GameandServerStubTests {
	private ServerStubJUnit sStub;
	private GameStub gStub;

	/**
	 * Set up of a ServerStub and a GameStub for testing
	 */
	@Before
    public void setUp() {
		sStub = new ServerStubJUnit(new String[]{"testUser1", "testUser2", "testUser3"});
		gStub = new GameStub(sStub);
		sStub.attachGameObject(gStub);
	}
	
	/**
	 * Test that sending a public message through the serverStubs' send command function
	 * actually sends out a public message
	 */
	@Test
	public void test1() {
		sStub.sendCommand("/public test public message", 0);
		assertEquals("test public message sent by user 0", 
				sStub.getPublicMessages().get(sStub.getPublicMessages().size() -1));
	}
	
	/**
	 * Test that sending a public message through the serverStubs' send command function
	 * does not also send a private message
	 */
	@Test
	public void test2() {
		int currentSize = sStub.getPrivateMessages().size();
		sStub.sendCommand("/public test public message", 0);
		assertEquals(currentSize, sStub.getPrivateMessages().size());
	}
	
	/**
	 * Test that sending a public message through the serverStubs' send command function
	 * does not activate any muting functions
	 */
	@Test
	public void test3() {
		int currentSize = sStub.getMutedPlayers().size();
		sStub.sendCommand("/public test public message", 0);
		assertTrue(currentSize == sStub.getMutedPlayers().size());
	}
	
	/**
	 * Test that sending a public message through the serverStubs' send command function
	 * does not activate any chat active/deactive functions
	 */
	@Test
	public void test4() {
		boolean currentState = sStub.getActiveChat();
		sStub.sendCommand("/public test public message", 0);
		assertTrue(currentState == sStub.getActiveChat());
	}
	
	/**
	 * Test that sending a private message through the serverStubs' send command function
	 * actually sends out a private message
	 */
	@Test
	public void test5() {
		sStub.sendCommand("/private testUser2 test private message", 0);
		assertEquals("testUser2 test private message sent by user 0", 
				sStub.getPrivateMessages().get(sStub.getPrivateMessages().size() -1));
	}
	
	/**
	 * Test that sending a private message through the serverStubs' send command function
	 * does not also send a public message
	 */
	@Test
	public void test6() {
		int currentSize = sStub.getPublicMessages().size();
		sStub.sendCommand("/private testUser2 test private message", 0);
		assertEquals(currentSize, sStub.getPublicMessages().size());
	}
	
	/**
	 * Test that sending a private message through the serverStubs' send command function
	 * does not activate any muting functions
	 */
	@Test
	public void test7() {
		int currentSize = sStub.getMutedPlayers().size();
		sStub.sendCommand("/private testUser2 test private message", 0);
		assertTrue(currentSize == sStub.getMutedPlayers().size());
	}
	
	/**
	 * Test that sending a private message through the serverStubs' send command function
	 * does not activate any chat active/deactive functions
	 */
	@Test
	public void test8() {
		boolean currentState = sStub.getActiveChat();
		sStub.sendCommand("/private testUser2 test private message", 0);
		assertTrue(currentState == sStub.getActiveChat());
	}
	
	/**
	 * Test that sending a mute function will mute a player
	 */
	@Test
	public void test9() {
		sStub.sendCommand("/mute testUser2", 0);
		assertTrue(sStub.getMutedPlayers().contains("testUser2"));
	}
	
	/**
	 * Test that sending an unmute function will unmute a player
	 */
	@Test
	public void test10() {
		sStub.sendCommand("/mute testUser1", 0);
		sStub.sendCommand("/unmute testUser1", 0);
		assertTrue(!sStub.getMutedPlayers().contains("testUser1"));
	}
	
	/**
	 * Test that chat can be toggled
	 */
	@Test
	public void test11() {
		boolean currentState = sStub.getActiveChat();
		sStub.sendCommand("/chat", 0);
		assertEquals(currentState, !sStub.getActiveChat());
	}
	
	/**
	 * Test 2 that chat can be toggled
	 */
	@Test
	public void test12() {
		boolean currentState = sStub.getActiveChat();
		sStub.sendCommand("/chat", 0);
		sStub.sendCommand("/chat", 2);
		assertEquals(currentState, sStub.getActiveChat());
	}
	
	/**
	 * Test that sending a private message through the serverStubs' send command function
	 * and using an invalid username will produce a private error message
	 */
	@Test
	public void test13() {
		sStub.sendCommand("/private invalidUser should fail", 0);
		assertEquals("testUser1 Invalid User invalidUser",
				sStub.getPrivateMessages().get(sStub.getPrivateMessages().size() -1));
	}
	
	/**
	 * Test that sending a private message through the serverStubs' send command function
	 * using only one word after the command fails, even if it is a valid username
	 */
	@Test
	public void test14() {
		sStub.sendCommand("/private testUser2", 0);
		assertEquals("testUser1 Invalid private message (testUser2)",
				sStub.getPrivateMessages().get(sStub.getPrivateMessages().size() -1));
	}
	
	/**
	 * Test that sending a private message through the serverStubs' send command function
	 * using only one word after the command fails, if it is not a valid username
	 */
	@Test
	public void test15() {
		sStub.sendCommand("/private invalidUser", 0);
		assertEquals("testUser1 Invalid private message (invalidUser)",
				sStub.getPrivateMessages().get(sStub.getPrivateMessages().size() -1));
	}
	
	/**
	 * Test that sending a mute command with an invalid player as an argument produces
	 * an error private message for the user who sent the command
	 */
	@Test
	public void test16() {
		sStub.sendCommand("/mute invalidUser", 0);
		assertEquals("testUser1 Invalid User invalidUser",
				sStub.getPrivateMessages().get(sStub.getPrivateMessages().size() -1));
	}
	
	/**
	 * Test that sending a mute command with a valid player produces a
	 * success message
	 */
	@Test
	public void test17() {
		sStub.sendCommand("/mute testUser2", 0);
		assertEquals("testUser1 Successfully muted testUser2",
				sStub.getPrivateMessages().get(sStub.getPrivateMessages().size() -1));
	}
	
	/**
	 * Test that sending a mute command with an invalid player as an argument
	 * does not modify the serverStub's mutedPlayer list
	 */
	@Test
	public void test18() {
		int currentSize = sStub.getMutedPlayers().size();
		sStub.sendCommand("/mute invalidUser", 0);
		assertEquals(currentSize, sStub.getMutedPlayers().size());
	}
	
	/**
	 * Test that sending an unmute command with an invalid player as an argument produces
	 * an error private message for the user who sent the command
	 */
	@Test
	public void test19() {
		sStub.sendCommand("/unmute invalidUser", 0);
		assertEquals("testUser1 Invalid User invalidUser",
				sStub.getPrivateMessages().get(sStub.getPrivateMessages().size() -1));
	}
	
	/**
	 * Test that sending an unmute command with an invalid player as an argument
	 * does not modify the serverStub's mutedPlayer list
	 */
	@Test
	public void test20() {
		sStub.sendCommand("/mute invalidUser", 0);
		int currentSize = sStub.getMutedPlayers().size();
		sStub.sendCommand("/unmute invalidUser", 0);
		assertEquals(currentSize, sStub.getMutedPlayers().size());
	}
	
	/**
	 * Test that sending an unmute function with a valid username will send
	 * a successful unmute
	 */
	@Test
	public void test21() {
		sStub.sendCommand("/unmute testUser2", 0);
		assertEquals("testUser1 Successfully un-muted testUser2",
				sStub.getPrivateMessages().get(sStub.getPrivateMessages().size() -1));
	}
	
	/**
	 * Test that sending an unmute command with a valid player produces a
	 * success message
	 */
	@Test
	public void test22() {
		sStub.sendCommand("/mute testUser2", 0);
		sStub.sendCommand("/unmute testUser2", 0);
		assertEquals("testUser1 Successfully un-muted testUser2",
				sStub.getPrivateMessages().get(sStub.getPrivateMessages().size() -1));
	}
	
	/**
	 * Test that sending an mute command with a valid player produces a
	 * success message even if the player is muted
	 */
	@Test
	public void test23() {
		int currentSize = sStub.getPrivateMessages().size();
		sStub.sendCommand("/mute testUser2", 0);
		sStub.sendCommand("/mute testUser2", 0);
		assertTrue(sStub.getPrivateMessages().size() == currentSize + 2);
	}
	
	
	
}

package tests;

import Server.DatabaseManager;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import Stubs.DatabaseStub;
import exceptions.InvalidInformationException;
import exceptions.InvalidUserException;
import exceptions.UserExistsException;

/**
 * Short test-suite to ensure the databaseManager works as it should, adapted
 * from the DatabaseStubTests
 * 
 * @author Team Nice
 * @version 18-03-2017
 */
public class DatabaseTests {
	private DatabaseManager dbStub;
	
	/*
	 * INITIAL STATE OF TESTS:
	 * 
	 * - DROP TABLES
	 * - ADD 3 USERS
	 *	"testUser1", "abc", "the answer is password", "password"
	 *	"testUser2", "def", "the answer is testhint", "testhint"
	 *	"testUser3", "123", "the answer is other", "other"
	 * 
	 *  NOTE: This must be repeated if the tests are to be run
	 * again
	 * 
	 */
	
	@Before
    public void setUp() {
		dbStub = new DatabaseManager();
	}
	
	/**
	 * Check whether a valid user can be logged in and no errors are thrown
	 */
    @Test
    public void test1() throws InvalidUserException, InvalidInformationException {
		dbStub.loginUser("testUser1", "abc");
    }
    
    /**
     * Check whether an InvalidUserException is thrown when an unknown user attempts to log-in
     */
    @Test (expected=InvalidUserException.class)
    public void test2() throws InvalidUserException, InvalidInformationException {
		dbStub.loginUser("testUser4", "abc");
    }
    
    /**
     * Check whether an InvalidInformationException is thrown when a known user enters the wrong
     * password
     */
    @Test (expected=InvalidInformationException.class)
    public void test3() throws InvalidUserException, InvalidInformationException {
		dbStub.loginUser("testUser1", "abcd");
    }
    
    /**
     * Check whether a user can be registered and then can be successfully logged in
     */
    @Test
    public void test4() throws UserExistsException, InvalidUserException, InvalidInformationException {
    	dbStub.registerUser("test", "testpass", "testhint", "testanswer");
    	dbStub.loginUser("test", "testpass");
    }
    
    /**
     * Check whether a register operation with an already existing user will throw a UserExistsException
     */
    @Test (expected=UserExistsException.class)
    public void test5() throws UserExistsException {
    	dbStub.registerUser("testUser1", "testpass", "testhint", "testanswer");
    }
    
    /**
     * Check whether a getSecurityQuestion operation returns the expected result for 
     */
    @Test 
    public void test6() throws InvalidUserException {
    	assertEquals("the answer is password", dbStub.getSecurityQuestion("testUser1"));
    }
	
    /**
     * Check whether a getSecurityQuestion operation returns an error for an invalid user
     */
    @Test (expected=InvalidUserException.class)
    public void test7() throws InvalidUserException {
    	dbStub.getSecurityQuestion("testUser4");
    }
    
    /**
     * Check whether a checkQuestionAnswer operation returns the password for a valid answer
     */
    @Test 
    public void test8() throws InvalidUserException, InvalidInformationException {
    	dbStub.checkQuestionAnswer("testUser2", "testhint");
    }   
    
    /**
     * Check whether a checkQuestionAnswer operation returns an error for an invalid user
     */
    @Test (expected=InvalidUserException.class)
    public void test9() throws InvalidUserException, InvalidInformationException {
    	dbStub.checkQuestionAnswer("testUser4", "testhint");
    }   
    
    /**
     * Check whether a checkQuestionAnswer operation returns an error for an invalid answer
     */
    @Test (expected=InvalidInformationException.class)
    public void test10() throws InvalidUserException, InvalidInformationException {
    	dbStub.checkQuestionAnswer("testUser2", "nottesthint");
    }  
}

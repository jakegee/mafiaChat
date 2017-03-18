package tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import Stubs.DatabaseStub;
import exceptions.InvalidInformationException;
import exceptions.InvalidUserException;
import exceptions.UserExistsException;

/**
 * Short test-suite to ensure the databaseStub works as it should, for 
 * use in other tests
 * 
 * @author Team Nice
 *
 */
public class DatabaseStubTests {
	private DatabaseStub dbStub;
	
	@Before
    public void setUp() {
		dbStub = new DatabaseStub();
		//users.add(new String[]{"testUser1", "abc", "the answer is password", "password"});
		//users.add(new String[]{"testUser2", "def", "the answer is testhint", "testhint"});
		//users.add(new String[]{"testUser3", "123", "the answer is other", "other"});
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
     * Check whether a getSecurityQuestion operation 
     */
    @Test (expected=UserExistsException.class)
    public void test6() throws UserExistsException {
    	dbStub.registerUser("testUser1", "testpass", "testhint", "testanswer");
    }
	
}

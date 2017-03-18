package Stubs;

import java.util.ArrayList;

import exceptions.InvalidInformationException;
import exceptions.InvalidUserException;
import exceptions.UserExistsException;
import systemInterfaces.IDatabase;

public class DatabaseStub implements IDatabase{
	private ArrayList<String[]> users;
	
	public DatabaseStub() {
		users = new ArrayList<String[]>();
		users.add(new String[]{"testUser1", "abc", "the answer is password", "password"});
		users.add(new String[]{"testUser2", "def", "the answer is testhint", "testhint"});
		users.add(new String[]{"testUser3", "123", "the answer is other", "other"});
	}
	
	@Override
	public void registerUser(String username, String password, String securityQuestion, String questionAnswer)
			throws UserExistsException {
		for (String[] element : users) {
			if (element[0].equals(username)) {
				throw new UserExistsException(username);
			}
		}
		String[] newUser = {username, password, securityQuestion,questionAnswer};
		users.add(newUser);
	}

	@Override
	public void loginUser(String username, String password) throws InvalidUserException, InvalidInformationException {
		for (String[] element : users) {
			if (element[0].equals(username)) {
				if (element[1].equals(password)) {
					return;
				} else {
					throw new InvalidInformationException("password");
				}
			}
		}
		throw new InvalidUserException(username);
	}

	@Override
	public String getSecurityQuestion(String username) throws InvalidUserException {
		for (String[] element : users) {
			if (element[0].equals(username)) {
				return element[2];
			}
		}
		throw new InvalidUserException(username);
	}

	@Override
	public String checkQuestionAnswer(String username, String hintAnswer)
			throws InvalidUserException, InvalidInformationException {
		for (String[] element : users) {
			if (element[0].equals(username)) {
				System.out.println(element[3]);
				System.out.println(hintAnswer);
				if (element[3].equals(hintAnswer)) {
					return element[1];
				} else {
					throw new InvalidInformationException("answer");
				}
			}
		}
		throw new InvalidUserException(username);
	}
	
	public static void main(String[] args) {
		DatabaseStub stub = new DatabaseStub();
		try {
			String returnString = stub.getSecurityQuestion("testUser2");
			String password = stub.checkQuestionAnswer("testUser2", "testhint");
			System.out.println(password);
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		
		
		
	}

}

package tests;
import Server.Server;

/**
 * ServerThread class is a extension of Thread that launches a 
 * server on a separate Thread when the Thread is started
 * 
 * Note: The Server will not actually start listening for clients
 * until start is called on the thread
 * 
 * @author Team Nice
 * @version 18-03-2017
 */
public class ServerThread extends Thread {
    private Server server;

    /**
     * Constructor for instantiating an object of the ServerThread class
     * 
     * @param port Port on which the server should be listening
     * @param maxServerSize Number of threads to store within the servers'
     * threadpool for clients to connect to
     * @param gameClassToTestWith Game class to bind to the server, if debug
     * is specifed gamestub will be used, any valid game within the game package
     * can be used, and if an invalid name is passed in, the system defaults
     * to debug
     */
    public ServerThread(int port, int maxServerSize, String gameClassToTestWith) {
    	this.server = new Server(port, maxServerSize);
		if (!gameClassToTestWith.equals("debug")) {
			try {
				server.setGameObject("Game." + gameClassToTestWith);
				server.startServerListening();
			} catch (ClassNotFoundException e) {
				System.out.println("Class " + gameClassToTestWith + " is not a valid Game Class Name");
			}
		}
    }
    
    /**
     * Function called when the thread is started
     */
    @Override
    public void run() {
    	this.server.startServerListening();
    }

}
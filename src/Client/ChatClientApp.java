package Client;

import java.awt.EventQueue;
import java.io.IOException;
import java.net.ConnectException;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

import GUIs.*;
import Server.Server;

/**
 * this class creates an instance of the Client and chatGame class and initializes the application.
 * @author nice
 *
 */
public class ChatClientApp {
	
	/**
	 * these are the variables of the class.
	 */
	public Client client;
	public int port;
	public String host;
	public chatGame window;

	/**
	 * this is the constructor for making an instance of the Client and chatGame classes.
	 */
	public ChatClientApp(){
		try {
			client = new Client(port, host);
			window = new chatGame(client);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, "Server Connection Failed: Cannot Find Server, please Restart");
		}
		
	}
	
	/**
	 * this is the main method which executes the application.
	 * @param args
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public static void main(String[] args) throws UnknownHostException, IOException {
	
		String[] args_ = {"localhost", "8000"};
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				//try {
					
					int port = 8000;
					String host = "localhost";
					Client client;
					try {
						client = new Client(Integer.parseInt(args_[1]), args_[0]);
						chatGame window = new chatGame(client);
						window.GAME.setVisible(true);
					} catch (UnknownHostException e) {
						JOptionPane.showMessageDialog(null, "Unknown Host, Server inaccessible");
					} catch (IOException e) {
						JOptionPane.showMessageDialog(null, "Server not currently active or Invalid Port, \n"
								+ "please double check port number");
					} catch (ArrayIndexOutOfBoundsException e) {
						JOptionPane.showMessageDialog(null, "Must include both host and port arguments");
					} catch (NumberFormatException e) {
						JOptionPane.showMessageDialog(null, "Port must be a valid int, " + args_[1] + " is not");
					}
			}
		});
	}

}

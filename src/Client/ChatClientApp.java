package Client;

import java.awt.EventQueue;
import java.io.IOException;
import java.net.ConnectException;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

import GUIs.*;

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

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				//try {
					
					int port = 8000;
					String host = "localhost";
					Client client;
					try {
						client = new Client(port, host);
						chatGame window = new chatGame(client);
						window.GAME.setVisible(true);
					} catch (UnknownHostException e) {
						JOptionPane.showMessageDialog(null, "Unknown Host, Server inaccessible");
					} catch (IOException e) {
						JOptionPane.showMessageDialog(null, "Server not currently active or Invalid Port, \n"
								+ "please double check port number");
					}
			}
		});
	}

}

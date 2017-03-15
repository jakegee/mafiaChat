package Client;

import java.awt.EventQueue;
import java.io.IOException;
import java.net.UnknownHostException;

import GUIs.*;

public class ChatClientApp {
	public static Client client;
	public int port;
	public chatGame window;

	
	public ChatClientApp(){
		try {
			
			window = new chatGame();
			client = new Client(port);
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	
	public static void main(String[] args) throws UnknownHostException, IOException {

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					
					int port = 8000;
					Client client = new Client(port);
					chatGame window = new chatGame();
					window.GAME.setVisible(true);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}

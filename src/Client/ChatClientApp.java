package Client;

import java.awt.EventQueue;
import java.io.IOException;
import java.net.UnknownHostException;

import GUIs.*;

public class ChatClientApp {
	public Client client;
	public int port;
	public String host;
	public chatGame window;

	
	public ChatClientApp(){
		try {
			client = new Client(port, host);
			window = new chatGame(client);
			
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
					String host = "127.0.0.1";
					Client client = new Client(port, host);
					chatGame window = new chatGame(client);
					window.GAME.setVisible(true);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}

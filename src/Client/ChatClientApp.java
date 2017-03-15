package Client;

import java.io.IOException;

import GUIs.*;

public class ChatClientApp {
	public Client client;
	public int port;
	public chatGame window;
	public static ChatClientApp frame;
	
	public ChatClientApp(){
		try {
			int port = 8000;
			window = new chatGame();
			client = new Client(port);
			frame = null;
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}

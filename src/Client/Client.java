package Client;


import messages.*;

import messages.Message.messageType;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import exceptions.InvalidInformationException;
import exceptions.InvalidUserException;
import exceptions.UserExistsException;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;



public class Client {

	private Gson cGson;
	private Socket socket;
	private DataOutputStream outputStream;
	private DataInputStream inputStream;
	private String ip;
	private int port=8000;
	private boolean online;
	

	public Client(int port) throws UnknownHostException, IOException{
		GsonBuilder builder = new GsonBuilder();
		cGson = builder.create();
		Socket socket = new Socket (ip, port);
		inputStream = new DataInputStream(socket.getInputStream());
		outputStream = new DataOutputStream(socket.getOutputStream());
		
	}

	
	public void start() {
		try {
			while(true) {
			
				this.online = true;
				
				while(this.online == true) {
					String input = inputStream.readUTF();
					ServerMessage message = cGson.fromJson(input, ServerMessage.class);
					System.out.println(message.messageText);
					
					switch (message.type) {
			
						case PRIVATE : 
							getResponse();
							break;
							
						case PUBLIC : 
							getResponse();
							break;
							
						case CHAT :
							getResponse();
							break;
						
						default : 
							
							break;
							
					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			this.online = false;
			e.printStackTrace();
		}
	}
	

	public void setCommandMsg(String message){
		for (int i=0; i<message.length(); i++){
			if (message.charAt(0) == '/'){
				String jsonText=  cGson.toJson(new Message(Message.messageType.COMMAND, message));
				sendClientMessage(jsonText);
			} else {
				String jsonText = cGson.toJson(new Message(Message.messageType.MESSAGE, message));
				sendClientMessage(jsonText);
			}
		}
		
	}
	
	public ServerMessage getResponse(){
		try {
			return decodeServerMessage(inputStream.readUTF());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
	public void createLoginPacket(String username, String password, String secQuestion, String ans) throws IOException {
		String msg = username + " " + password + " " + secQuestion + " " + ans;
		String jsonText = cGson.toJson(new Message(Message.messageType.LOGIN, msg));
		
		outputStream.writeUTF(jsonText);
		outputStream.flush();
		
		String input = inputStream.readUTF();
		ServerMessage response = this.getResponse();
		if(response.type.equals("SUCCESS")){
			System.out.println("Sign-in successful");
		}else if (response.type.equals("ERROR")){
			System.out.println("Try again loser");
		}
		
	}

	public void createAccountPacket(String username, String password, String secQuestion, String ans) {
		String msg = username + " " + password + " " + secQuestion + " " + ans;
		String jsonText = cGson.toJson(new Message(Message.messageType.REGISTER, msg));
		try {
			outputStream.writeUTF(jsonText);
			outputStream.flush();

			
			ServerMessage response = this.getResponse();
			if (response.type.equals("SUCCESS")) {
				System.out.println("Sign-in successful");
			} else if (response.type.equals("ERROR")) {
				
				System.out.println("Sorry mate username taken. Try again");
			}

		} catch (IOException ie) {

		}
	}

	public void createLogoutPacket() {
		String jsonText = cGson.toJson(new Message(Message.messageType.LOGOUT, null));
		sendClientMessage(jsonText);
	}

	
	public void createPassHintPacket(String username, String question, String answer) {
		String msg = username + " " + question + " " + answer;
		String jsonText= cGson.toJson(new Message(Message.messageType.PASSWORDHINT, msg));
		try{
			outputStream.writeUTF(jsonText);
			outputStream.flush();
			
			
			ServerMessage response = this.getResponse();
			if (response.type.equals("SUCCESS")){
				System.out.println(response);
			} else if (response.type.equals("SUCCESS")){
				System.out.println("Sorry question and answer don't match");
			}
		}catch (IOException ie){
			
		}
	}
	
	public boolean getOnline(){
		return this.online;
	}
	
	
	public void sendClientMessage(String jsonText){
		if (this.online == true) {
			try {
				outputStream.writeUTF(jsonText);
				outputStream.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	public void acceptMsg(){
		
	}
	
	public ServerMessage decodeServerMessage(String jsonText) {
		return cGson.fromJson(jsonText, ServerMessage.class);
	}

	public static void main(String[] args) throws UnknownHostException, IOException{
		GsonBuilder builder = new GsonBuilder();
		String ip = "10.20.216.120";
		int port = 8000;
		Gson cGson = builder.create();
		Socket socket = new Socket (ip, port);
		DataInputStream inputStream = new DataInputStream(socket.getInputStream());
		DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
		Client client = new Client(8000);
		client.start();
		
	}
}
package Client;


import messages.*;
import messages.Message.messageType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * this class reads and writes data and organises/cleans up sockets when client disconnects
 * @author vishnu
 *
 */
public abstract class SocketOrganiser implements Runnable{
	private Socket socket;
	private ObjectOutputStream outputStream;
	private ObjectInputStream inputStream;
	private boolean connected;
	private  boolean disconnected;
	
	public SocketOrganiser (Socket socket) {
		try {
			this.socket = socket;
			connected = false;
			disconnected = false;
			outputStream = new ObjectOutputStream(socket.getOutputStream());
			inputStream = new ObjectInputStream(socket.getInputStream());
			
			new Thread(this).start();
		} catch(IOException e){
			Organise();
		}
		
	}
	
	/**
	 * this method shuts down connections when clients want to quit or is disconnected.
	 */
	public void Organise(){
		if (!socket.isOutputShutdown()){
			sendLogout();
		} if (inputStream != null){
			try{
				inputStream.close();
			}catch (IOException e){
				
			}
			
		}if (outputStream != null){
			try{
				outputStream.close();
			} catch (Exception e){
				
			}
		}if (socket != null){
			if (!socket.isClosed()){
				try {
					socket.close();
				}catch (IOException e){
					
				}
			}
		}
		setConnected(false);
		
	}

	/**
	 * method that reads data from server.
	 */
	@Override
	public void run() {
		try{
			setConnected(true);
			while (!disconnected){
				if (!socket.isClosed()){
					ServerMessage msg = (ServerMessage) inputStream.readObject();
					acceptMsg(msg);
				} else {
					try{
						Thread.sleep(200);
					}catch (InterruptedException ie){
						
					}
				}
			}
		}catch (Exception e){
			
		}finally {
			Organise();
		}
		
	}
	
	
	/**
	 * this method converts strings into type message and calls the sendData() method.
	 * @param msg
	 */
	public  void sendMsg(String msg){
		Message temp = new Message(messageType.MESSAGE, msg);
		sendData(temp);
	}
	
	/**
	 * this method calls the sendData() method.
	 * @param msg
	 */
	public void sendMsg(Message msg){
		sendData(msg);
	}
	
	/**
	 * this method sends data to the server.
	 * @param data
	 */
	private void sendData(Message data){
		try{
			outputStream.writeObject(data);
		}catch (IOException e){
			Client.println("Couldn't write to socket: " + e.getMessage());
		}
	}
	
	public boolean isConnected(){
		return connected;
	}
	
	/**
	 * setter method for boolean connected.
	 * @param connected
	 */
	public void setConnected (boolean connected){
		this.connected=connected;
	}
	
	/**
	 * 
	 * @return true if in the process of disconnecting and still connected.
	 */
	public boolean isDisconnected(){
		return disconnected;
	}

	/**
	 * this method disconnects the client from the server.
	 */
	public void disconnect(){
		this.disconnected= true;
		sendLogout();
	}
	
	/**
	 * method that logs clients out.
	 */
	public void sendLogout(){
		sendData(new Message(messageType.LOGOUT, null));
	}
	
	/**
	 * this method alerts the class when a socket is closed.
	 */
	public abstract void disconnected();

	/**
	 * this method handles each line of messsage received
	 * @param msg
	 */
	public abstract void acceptMsg(ServerMessage msg);


}





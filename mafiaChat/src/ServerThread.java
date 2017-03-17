//this class was part of the attempt to junit test the game methods


public class ServerThread extends Thread {
    private Server server;

    public ServerThread(int port, int maxServerSize) {
	this.server = new Server(port, maxServerSize);
    }

}
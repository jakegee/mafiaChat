import messages.Message;
import systemInterfaces.Game;
import systemInterfaces.IGame;
import systemInterfaces.IServer;

public class TestGame extends Game{
	
	public TestGame(IServer server) {
		super(server);
	}

	@Override
	public void handleMessage(Message message, int origin) {
		String text = message.messageText;
		String command;
		String remText;
		int charPosition = text.indexOf(" ");
		
		if (charPosition != -1) {
			remText = text.substring(charPosition + 1);
			command = text.substring(0, charPosition);
		} else {
			command = text;
		}
		
		if (command.equals("/ready")) {
			server.privateMessage("You sent a ready request", origin);
		} else if (command.equals("/poop")) {
			server.publicMessage(server.getUsername(origin) + " sent poop");
		} else if (command.equals("/lovephil")){
			server.publicMessage(server.getUsername(origin) + " loves phil, don't tell coolfozia22");
		} else {
			server.privateMessage("Unrecognised Command", origin);
		}
		
		
	}

}

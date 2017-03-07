package GUIs;

import java.awt.event.*;
import java.io.*;
import java.awt.*;
import javax.swing.JFrame;

public class ChatClient extends JFrame
implements WindowListener, MouseListener, KeyListener
{

	private TextArea messageArea = null;
	private TextField sendArea = null;
	private String userName = null;
	
	
	ChatClient (String s)
	{
		super(s);
		
		this.addWindowListener(this);
		this.setSize(800,600);
		this.setResizable(true);
		this.setLayout(new BorderLayout());
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		messageArea =  new TextArea();
		messageArea.setEditable(false);
		this.add(messageArea, "Center");
		messageArea.setFont(new Font("Arial", Font.PLAIN, 17));
		
		Panel p = new Panel();
		p.setLayout(new FlowLayout());
		
		sendArea =  new TextField(30);
		sendArea.addKeyListener(this);
		sendArea.setFont(new Font("Arial", Font.PLAIN, 17));
		
		p.add(sendArea);
		p.setBackground(new Color(221,221,221));
		Button send =  new Button("Send");
		send.addMouseListener(this);
		
		p.add(send);
		
		Button clear =  new Button("clear");
		clear.addMouseListener(this);
		
		p.add(clear);
		
		this.add(p,"South");
		this.setVisible(true);
		sendArea.requestFocus();

		
	}
	
	public static void main(String[] args) {
		ChatClient c = new ChatClient ("Chat Client v1.0");
		
	}
	
	
	@Override
	public void keyTyped(KeyEvent e) {
	
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
	
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
	
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
	
		
	}

	@Override
	public void windowOpened(WindowEvent e) {

		
	}

	@Override
	public void windowClosing(WindowEvent e) {
	
		
	}

	@Override
	public void windowClosed(WindowEvent e) {
		
		
	}

	@Override
	public void windowIconified(WindowEvent e) {
	
		
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
	
		
	}

	@Override
	public void windowActivated(WindowEvent e) {

		
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		
		
	}
	
	
}
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosing(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	
}
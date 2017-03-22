package GUIs;

import Client.*;
import messages.*;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JTextField;
import java.awt.Image;
import java.awt.ScrollPane;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.awt.event.ActionEvent;
import java.awt.CardLayout;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;
import javax.swing.JTextArea;
import javax.swing.JScrollBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JComboBox;


/**
 * This class is for the GUI -  all screen within the chat game are made from JPanels in this class.
 * 
 * @author Fozia Mehboob
 *
 */
public class chatGame {
	public javax.swing.JScrollPane jScrollPane1;
	public JFrame GAME;
	public JTextField usernameEntry;
	public JTextField txtUsername;
	public JTextField textUsername;
	public JPasswordField txtPassword;
	public JTextField txtSecurityQ;
	public JTextField txtSecurityA;
	public JPasswordField textPassword;
	public JTextField securityAnswer;
	public String[] messageStrings;
	public JComboBox cmbMessageList;
	public Client client;
	public chatGame reference;
	public JList listChat;
	public JList listUsers;
	public JList listPrivate;



	
	/**
	 * Create the application.
	 */
	public chatGame(Client client) {
		initialize();
		this.client = client;
		this.reference = this;
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {

		//set size and details of game.
		GAME = new JFrame();
		GAME.setTitle("Mafia Chat");
		GAME.setBounds(60, 60, 1390, 820);
		GAME.setMinimumSize(new Dimension(1390, 820));
		GAME.setMaximumSize(new Dimension(1390, 820));
		GAME.setResizable(false);
		GAME.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//images
		Image img = new ImageIcon(this.getClass().getResource("/LOGO.png")).getImage();
		Image img2 = new ImageIcon(this.getClass().getResource("/bigChat.png")).getImage();
		Image img3 = new ImageIcon(this.getClass().getResource("/dare-to-play.png")).getImage();
		
		//layout
		GAME.getContentPane().setLayout(new CardLayout(0, 0));


		///////////////////////////////////////////////////////////////////////////////////////////////
		//welcome screen panel
		JPanel Welcome = new JPanel();
		Welcome.setBackground(Color.BLACK);
		GAME.getContentPane().add(Welcome, "name_30392538864818");
		Welcome.setLayout(null);

		//login screen panel
		JPanel Login = new JPanel();
		Login.setBackground(Color.BLACK);
		GAME.getContentPane().add(Login, "name_30392587252988");
		Login.setLayout(null);

		//signIn panel
		JPanel SignIn = new JPanel();
		SignIn.setBackground(Color.BLACK);
		SignIn.setForeground(Color.BLACK);
		GAME.getContentPane().add(SignIn, "name_30392612646698");
		SignIn.setLayout(null);

		//game panel
		JPanel Game = new JPanel();
		Game.setBackground(Color.BLACK);
		Game.setForeground(Color.BLACK);
		GAME.getContentPane().add(Game, "name_30392640535777");
		Game.setLayout(null);

		//forgottenPassword panel
		JPanel ForgotPassword = new JPanel();
		ForgotPassword.setBackground(Color.BLACK);
		GAME.getContentPane().add(ForgotPassword, "name_124435419567369");
		ForgotPassword.setLayout(null);

		/////////////////////////////////////////////////////////////////////////////////////////

		//GAME PANEL COMPONENTS

		//logo image bottom of screen
		JLabel logoSignIn1 = new JLabel("");
		logoSignIn1.setBounds(10, 598, 170, 150);
		Image SigninLogo1 = img2.getScaledInstance(logoSignIn1.getWidth(),
				logoSignIn1.getHeight(), Image.SCALE_SMOOTH);
		logoSignIn1.setIcon(new ImageIcon(SigninLogo1));
		SignIn.add(logoSignIn1);

		//logo image bottom of screen
		JLabel logoSignIn2 = new JLabel("");
		logoSignIn2.setBounds(1186, 598, 170, 150);
		Image SigninLogo2 = img2.getScaledInstance(logoSignIn2.getWidth(),
				logoSignIn2.getHeight(), Image.SCALE_SMOOTH);
		logoSignIn2.setIcon(new ImageIcon(SigninLogo2));
		SignIn.add(logoSignIn2);

		//scroll bar for input text with text box
		listChat = new JList<String>();
		//listChat.setValueIsAdjusting(true);
		listChat.setModel(new DefaultListModel<String>());
		
		JScrollPane scrollPane = new JScrollPane(listChat);
		scrollPane.setBounds(21,115,911,555);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {  
			private int prevMax = 0;
	        public void adjustmentValueChanged(AdjustmentEvent e) {  
	           if (e.getAdjustable().getMaximum() != prevMax) {
	        	   prevMax = e.getAdjustable().getMaximum();
	        	   e.getAdjustable().setValue(e.getAdjustable().getMaximum());
	           }
	        }
	    });
		Game.add(scrollPane);

		//scroll bar for input text with text box
		JScrollPane scrollPane1 = new JScrollPane();
		scrollPane1.setBounds(21,682,911,80);
		scrollPane1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		Game.add(scrollPane1);
		JTextArea txtEnterMess1 = new JTextArea();
		scrollPane1.setViewportView(txtEnterMess1);
		txtEnterMess1.setLineWrap(true);
		
		txtEnterMess1.addKeyListener(new KeyAdapter() {
		      public void keyPressed(KeyEvent evt) {
		          int key = evt.getKeyCode();
		          if (key == KeyEvent.VK_ENTER) {
					client.setCommandMsg(txtEnterMess1.getText());
					txtEnterMess1.setText("");
					return;
		          } else {
		        	  if(txtEnterMess1.getText().equals("\n")) {
		        		  txtEnterMess1.setText("");
		        	  }
		          }
		        }
		      });


		listPrivate = new JList<String>();
		listPrivate.setModel(new DefaultListModel<String>());
		
		JScrollPane scrollPrivate = new JScrollPane(listPrivate);
		scrollPrivate.setBounds(950, 395, 412, 268);
		scrollPrivate.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		Game.add(scrollPrivate);

		//text area to view online users
		listUsers = new JList<String>();
		listUsers.setModel(new DefaultListModel<String>());
		
		JScrollPane scrollOnlineUsers = new JScrollPane(listUsers);
		scrollOnlineUsers.setBounds(950, 120, 412, 224);
		scrollOnlineUsers.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		Game.add(scrollOnlineUsers);

		//heading for online users
		JLabel lblOnlineUsers = new JLabel("ONLINE USERS");
		lblOnlineUsers.setFont(new Font("Silom", Font.PLAIN, 18));
		lblOnlineUsers.setForeground(Color.WHITE);
		lblOnlineUsers.setBounds(1220, 80, 179, 26);
		Game.add(lblOnlineUsers);

		//heading for server messages
		JLabel lblServerMessages = new JLabel("SERVER MESSAGES");
		lblServerMessages.setFont(new Font("Silom", Font.PLAIN, 18));
		lblServerMessages.setForeground(Color.WHITE);
		lblServerMessages.setBounds(1180, 365, 179, 26);
		Game.add(lblServerMessages);

		//send button
		JButton Send = new JButton("SEND");
		Send.setToolTipText("Click to send message");
		Send.setBackground(Color.GRAY);
		Send.setFont(new Font("Silom", Font.PLAIN, 40));
		Send.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				client.setCommandMsg(txtEnterMess1.getText());
				txtEnterMess1.setText("");
			}
		});
		
		Send.setForeground(Color.BLUE);
		Send.setBounds(941, 682, 154, 90);
		Game.add(Send);

		JLabel titleGame = new JLabel("");
		titleGame.setBounds(135, 20, 1050, 100);
		Image a = img.getScaledInstance(titleGame.getWidth(),
				titleGame.getHeight(), Image.SCALE_SMOOTH);
		titleGame.setIcon(new ImageIcon(a));
		Game.add(titleGame);
		
		JButton btnRules = new JButton("Rules");
		btnRules.setToolTipText("Click to send message");
		btnRules.setForeground(Color.BLUE);
		btnRules.setFont(new Font("Silom", Font.PLAIN, 20));
		btnRules.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				client.getRules();
			}
		});
		
		btnRules.setBackground(Color.GRAY);
		btnRules.setBounds(1105, 700, 126, 72);
		Game.add(btnRules);
		
		JButton btnLogOut = new JButton("Log Out");
		btnLogOut.setToolTipText("Click to send message");
		btnLogOut.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				try {
					client.createLogoutPacket();
					Welcome.setVisible(true);
					Game.setVisible(false);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnLogOut.setForeground(Color.BLUE);
		btnLogOut.setFont(new Font("Silom", Font.PLAIN, 20));
		btnLogOut.setBackground(Color.GRAY);
		btnLogOut.setBounds(1235, 700, 126, 72);
		Game.add(btnLogOut);
		
		Game.getRootPane().setDefaultButton(Send);
		
		//////////////////////////////////////////////////////////////////////////////////////////////////////

		//WELCOME SCREEN COMPONENTS
		//title 
		JLabel titleWelcome = new JLabel("");
		titleWelcome.setBounds(135, 20, 1050, 180);
		Image logoW = img.getScaledInstance(titleWelcome.getWidth(),
				titleWelcome.getHeight(), Image.SCALE_SMOOTH);
		titleWelcome.setIcon(new ImageIcon(logoW));
		Welcome.add(titleWelcome);


		//dare to play text
		JLabel dareTxt = new JLabel("");
		dareTxt.setBounds(480, 525, 440, 180);
		Image one = img3.getScaledInstance(dareTxt.getWidth(),
				dareTxt.getHeight(), Image.SCALE_SMOOTH);
		dareTxt.setIcon(new ImageIcon(one));
		Welcome.add(dareTxt);

		//enter button
		JButton btnEnter = new JButton("ENTER");
		btnEnter.setFont(new Font("Silom", Font.BOLD, 28));
		btnEnter.setToolTipText("Press to enter game");
		btnEnter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Welcome.setVisible(false);
				Login.setVisible(true);
			}
		});
		btnEnter.setForeground(Color.BLUE);
		btnEnter.setBounds(550, 710, 240, 50);
		Welcome.add(btnEnter);

		//main logo
		JLabel msnLogo = new JLabel("");
		msnLogo.setBounds(440, 157, 518, 390);
		Image imgage = img2.getScaledInstance(msnLogo.getWidth(),
				msnLogo.getHeight(), Image.SCALE_SMOOTH);
		msnLogo.setIcon(new ImageIcon (imgage));
		Welcome.add(msnLogo);

		///////////////////////////////////////////////////////////////////////////////////////////////////////////		
		//LOGIN SCREEN COMPONENTS
		
		//label for password text
		JLabel lblPassword = new JLabel("Enter Password");
		lblPassword.setBounds(246, 272, 563, 138);
		lblPassword.setForeground(Color.WHITE);
		lblPassword.setFont(new Font("Silom", Font.PLAIN, 46));
		Login.add(lblPassword);

		//label for username text
		JLabel label = new JLabel("Enter Username");
		label.setBounds(246, 200, 563, 138);
		label.setForeground(Color.WHITE);
		Login.add(label);
		label.setFont(new Font("Silom", Font.PLAIN, 46));

		//login button
		JButton btnLogIn = new JButton("Log In");
		btnLogIn.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				try {
					//client.run(); <- why call run?
					String username = txtUsername.getText();
					String password = new String(txtPassword.getPassword());
					ServerMessage response = client.createLoginPacket(username, password);
					if (response.type == ServerMessage.messageType.SUCCESS) {
						Login.setVisible(false);
						Game.setVisible(true);
						client.spawnListenerThread(reference);
					}
					JOptionPane.showMessageDialog(null, response.messageText);	
				}
				catch(Exception e1){
					JOptionPane.showMessageDialog(null, e1);					
				}
			}
		});
		btnLogIn.setToolTipText("Press to login to game");
		btnLogIn.setFont(new Font("Silom", Font.PLAIN, 26));
		btnLogIn.setBounds(582, 442, 240, 50);
		btnLogIn.setForeground(Color.BLUE);
		Login.add(btnLogIn);

		//signup button
		JButton btnSignUp = new JButton("Sign Up");
		btnSignUp.setToolTipText("Press to register new user");
		btnSignUp.setFont(new Font("Silom", Font.PLAIN, 26));
		btnSignUp.setBounds(582, 510, 240, 50);
		btnSignUp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
					Login.setVisible(false);
					SignIn.setVisible(true);
			}
		});
		btnSignUp.setForeground(Color.BLUE);
		Login.add(btnSignUp);

		//label
		txtUsername = new JTextField();
		txtUsername.setBounds(723, 240, 515, 50);
		txtUsername.setFont(new Font("Futura", Font.PLAIN, 13));
		txtUsername.setColumns(10);
		Login.add(txtUsername);


		JLabel titleLogin = new JLabel("");
		titleLogin.setBounds(135, 20, 1050, 100);
		Image logoLog = img.getScaledInstance(titleLogin.getWidth(),
				titleLogin.getHeight(), Image.SCALE_SMOOTH);
		titleLogin.setIcon(new ImageIcon(logoLog));
		Login.add(titleLogin);

		txtPassword = new JPasswordField();
		txtPassword.setBounds(723, 312, 515, 50);
		Login.add(txtPassword);

		JLabel LogoSignIn = new JLabel("");
		LogoSignIn.setBounds(10, 598, 170, 150);
		Image logoSign = img2.getScaledInstance(LogoSignIn.getWidth(),
				LogoSignIn.getHeight(), Image.SCALE_SMOOTH);
		LogoSignIn.setIcon(new ImageIcon(logoSign));
		Login.add(LogoSignIn);




		JLabel LogoSignIn2 = new JLabel("");
		LogoSignIn2.setBounds(1186, 598, 170, 150);
		Image logoSign2 = img2.getScaledInstance(LogoSignIn2.getWidth(),
				LogoSignIn2.getHeight(), Image.SCALE_SMOOTH);
		LogoSignIn2.setIcon(new ImageIcon(logoSign2));
		Login.add(LogoSignIn2);

		JButton btnForgottonPassword = new JButton("Forgotten Password");
		btnForgottonPassword.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Login.setVisible(false);
				ForgotPassword.setVisible(true);
			}
		});
		btnForgottonPassword.setToolTipText("Click here to help retrieve password");
		btnForgottonPassword.setForeground(Color.BLUE);
		btnForgottonPassword.setFont(new Font("Silom", Font.PLAIN, 20));
		btnForgottonPassword.setBounds(562, 680, 290, 50);
		Login.add(btnForgottonPassword);

		///////////////////////////////////////////////////////////////////////////////////

		//Sign-in Panel

		JLabel lblEnterName = new JLabel("Enter Username");
		lblEnterName.setForeground(Color.WHITE);
		lblEnterName.setFont(new Font("Silom", Font.PLAIN, 38));
		lblEnterName.setBounds(210, 200, 500, 50);
		SignIn.add(lblEnterName);

		JLabel lblEnterPassword = new JLabel("Enter Password");
		lblEnterPassword.setForeground(Color.WHITE);
		lblEnterPassword.setFont(new Font("Silom", Font.PLAIN, 38));
		lblEnterPassword.setBounds(210, 280, 500, 50);
		SignIn.add(lblEnterPassword);

		textUsername = new JTextField();
		textUsername.setBounds(640, 200, 500, 50);
		SignIn.add(textUsername);
		textUsername.setColumns(10);

		JButton btnRegister = new JButton("Register");
		btnRegister.setToolTipText("Submit registration");
		btnRegister.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try{
					System.out.println("Entering Action Event");
					String username = textUsername.getText();
					String password = new String (textPassword.getPassword());
					String question = txtSecurityQ.getText();
					String answer = txtSecurityA.getText();
					ServerMessage response = client.createAccountPacket(username,
							password, question, answer);
					if (response.type == ServerMessage.messageType.SUCCESS) {
						Login.setVisible(true);
						SignIn.setVisible(false);
					}
					JOptionPane.showMessageDialog(null, response.messageText);
				
				}catch(Exception e1){
					JOptionPane.showMessageDialog(null, e1);
				}
			}
		});
		btnRegister.setForeground(Color.BLUE);
		btnRegister.setFont(new Font("Silom", Font.PLAIN, 28));
		btnRegister.setBounds(582, 570, 240, 50);
		SignIn.add(btnRegister);

		JLabel titleSignIn = new JLabel("");
		titleSignIn.setBounds(135, 20, 1050, 100);
		Image newimg = img.getScaledInstance(titleSignIn.getWidth(),
				titleSignIn.getHeight(), Image.SCALE_SMOOTH);
		titleSignIn.setIcon(new ImageIcon(newimg));
		SignIn.add(titleSignIn);

		JLabel lblSecurityQ = new JLabel("Set security question");
		lblSecurityQ.setFont(new Font("Silom", Font.PLAIN, 38));
		lblSecurityQ.setForeground(Color.WHITE);
		lblSecurityQ.setBounds(210, 360, 500, 50);
		SignIn.add(lblSecurityQ);

		txtSecurityQ = new JTextField();
		txtSecurityQ.setBounds(640, 360, 500, 50);
		SignIn.add(txtSecurityQ);
		txtSecurityQ.setColumns(10);

		JButton btnBack = new JButton("Back");
		btnBack.setToolTipText("To login screen");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SignIn.setVisible(false);
				Login.setVisible(true);
			}
		});
		btnBack.setFont(new Font("Silom", Font.PLAIN, 18));
		btnBack.setForeground(Color.BLUE);
		btnBack.setBounds(642, 630, 99, 39);
		SignIn.add(btnBack);

		JLabel lblEnterHint = new JLabel("Set your answer");
		lblEnterHint.setFont(new Font("Silom", Font.PLAIN, 38));
		lblEnterHint.setForeground(Color.WHITE);
		lblEnterHint.setBounds(210, 440, 500, 50);
		SignIn.add(lblEnterHint);

		txtSecurityA = new JTextField();
		txtSecurityA.setColumns(10);
		txtSecurityA.setBounds(640, 440, 500, 50);
		SignIn.add(txtSecurityA);

		textPassword = new JPasswordField();
		textPassword.setBounds(640, 280, 500, 50);
		SignIn.add(textPassword);
		
		
		/////////////////////////////////////////////////////////////////////////////////////////////////
		
		//ForgottenPassword Panel

		JLabel titleForgotten = new JLabel("");
		titleForgotten.setBounds(135, 20, 1050, 100);
		Image Logo1 = img.getScaledInstance(titleForgotten.getWidth(),
				titleForgotten.getHeight(), Image.SCALE_SMOOTH);
		titleForgotten.setIcon(new ImageIcon(Logo1));
		ForgotPassword.add(titleForgotten);


		JLabel lblUser = new JLabel("Enter Username");
		lblUser.setBounds(260, 189, 328, 58);
		lblUser.setForeground(Color.WHITE);
		ForgotPassword.add(lblUser);
		lblUser.setFont(new Font("Silom", Font.PLAIN, 28));

		usernameEntry = new JTextField();
		usernameEntry.setBounds(756, 189, 500, 45);
		usernameEntry.setFont(new Font("Futura", Font.PLAIN, 24));
		usernameEntry.setColumns(10);
		ForgotPassword.add(usernameEntry);

		securityAnswer = new JTextField();
		securityAnswer.setFont(new Font("Futura", Font.PLAIN, 24));
		securityAnswer.setColumns(10);
		securityAnswer.setBounds(756, 372, 500, 50);
		ForgotPassword.add(securityAnswer);

		JLabel lblEnterTheSet = new JLabel("Enter the set security answer");
		lblEnterTheSet.setForeground(Color.WHITE);
		lblEnterTheSet.setFont(new Font("Silom", Font.PLAIN, 28));
		lblEnterTheSet.setBounds(260, 381, 3505, 58);
		ForgotPassword.add(lblEnterTheSet);

		JLabel msnForgotten1 = new JLabel("");
		msnForgotten1.setBounds(10, 598, 170, 150);
		Image LogPassword1 = img2.getScaledInstance(msnForgotten1.getWidth(),
				msnForgotten1.getHeight(), Image.SCALE_SMOOTH);
		msnForgotten1.setIcon(new ImageIcon(LogPassword1));
		ForgotPassword.add(msnForgotten1);


		JLabel msnForgotten2 = new JLabel("");
		msnForgotten2.setBounds(1186, 598, 170, 150);
		Image LogPassword11 = img2.getScaledInstance(msnForgotten2.getWidth(),
				msnForgotten2.getHeight(), Image.SCALE_SMOOTH);
		msnForgotten2.setIcon(new ImageIcon(LogPassword11));
		ForgotPassword.add(msnForgotten2);

		JButton btnReturnToLogin = new JButton("Return to Login screen");
		btnReturnToLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ForgotPassword.setVisible(false);
				Login.setVisible(true);
			}
		});
		btnReturnToLogin.setToolTipText("Press to login to game");
		btnReturnToLogin.setForeground(Color.BLUE);
		btnReturnToLogin.setFont(new Font("Silom", Font.PLAIN, 20));
		btnReturnToLogin.setBounds(526, 537, 280, 44);
		ForgotPassword.add(btnReturnToLogin);

		JButton btnDisplaySecurityQuestion = new JButton("Get Question");
		btnDisplaySecurityQuestion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try{
					String username = usernameEntry.getText();
					System.out.println("Get Question Called");
					ServerMessage response = client.forgottenPassword(username, null);
					JOptionPane.showMessageDialog(null, response.messageText);
               }catch (Exception e1){
            	   
               }
			}
		});
		btnDisplaySecurityQuestion.setToolTipText("Click here to display password");
		btnDisplaySecurityQuestion.setForeground(Color.BLUE);
		btnDisplaySecurityQuestion.setFont(new Font("Silom", Font.PLAIN, 20));
		btnDisplaySecurityQuestion.setBounds(975, 236, 280, 44);
		ForgotPassword.add(btnDisplaySecurityQuestion);

		JButton btnSubmitSecurityAnswer = new JButton("Submit Security Answer");
		btnSubmitSecurityAnswer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try{
					String username = usernameEntry.getText();
					String answer = securityAnswer.getText();
					System.out.println("Submit Question Called");
					ServerMessage response = client.forgottenPassword(username, answer);
					if (response.type == ServerMessage.messageType.SUCCESS) {
						Login.setVisible(true);
						ForgotPassword.setVisible(false);
					}
					JOptionPane.showMessageDialog(null, response.messageText);
               }catch (Exception e1){
            	   
               }
			}
		});
		btnSubmitSecurityAnswer.setToolTipText("Click here to display password");
		btnSubmitSecurityAnswer.setForeground(Color.BLUE);
		btnSubmitSecurityAnswer.setFont(new Font("Silom", Font.PLAIN, 20));
		btnSubmitSecurityAnswer.setBounds(975, 424, 280, 44); 
		ForgotPassword.add(btnSubmitSecurityAnswer);
	}


}

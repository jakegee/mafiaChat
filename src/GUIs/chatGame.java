package GUIs;


import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JTextField;
import java.awt.Image;

import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.CardLayout;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
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
	public JScrollPane scrollB;
	public JScrollBar Scroll;
	public JTextField securityQ;
	public JTextField securityAnswer;
	public String[] messageStrings;
	public JComboBox cmbMessageList;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					chatGame window = new chatGame();
					window.GAME.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public chatGame() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		//set size and details of game.
		GAME = new JFrame();
		GAME.setTitle("Mafia Chat");
		GAME.setBounds(100, 100, 550, 650);
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
		
		//drop down menu
		messageStrings  = new String [] {"Options","Rules", "Highest Scrores", "Quit Chat", "Log out"};
		cmbMessageList = new JComboBox(messageStrings);
		cmbMessageList.setToolTipText("click drop down to view options");
		cmbMessageList.setBounds(415, -11, 135, 50);
		JLabel lblDropDown =  new JLabel();
		Game.add(lblDropDown);
		Game.add(cmbMessageList);

		//logo image bottom of screen
		JLabel logoSignIn1 = new JLabel("");
		logoSignIn1.setBounds(6, 558, 88, 64);
		Image SigninLogo1 = img2.getScaledInstance(logoSignIn1.getWidth(),logoSignIn1.getHeight(), Image.SCALE_SMOOTH);
		logoSignIn1.setIcon(new ImageIcon(SigninLogo1));
		SignIn.add(logoSignIn1);

		//logo image bottom of screen
		JLabel logoSignIn2 = new JLabel("");
		logoSignIn2.setBounds(456, 558, 88, 64);
		Image SigninLogo2 = img2.getScaledInstance(logoSignIn2.getWidth(),logoSignIn2.getHeight(), Image.SCALE_SMOOTH);
		logoSignIn2.setIcon(new ImageIcon(SigninLogo2));
		SignIn.add(logoSignIn2);
		
//		JLabel logo = new JLabel("New label");
//		logo.setBounds(0, 6, 188, 79);
		
		//scroll bar to scroll through all users messages
		JScrollPane scrlAllMess = new JScrollPane();
		scrlAllMess.setBounds(28,105,339,445);
		scrlAllMess.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		Game.add(scrlAllMess);

		//text areas to view all users messages
		JTextArea txtServerMess = new JTextArea();
		txtServerMess.setToolTipText("Messages received from Server");
		txtServerMess.setEditable(false);
		txtServerMess.setLineWrap(true);
		txtServerMess.setBounds(379, 365, 160, 185);
		Game.add(txtServerMess);

		//text area to view online users
		JTextArea txtOnlineUsers = new JTextArea();
		txtOnlineUsers.setToolTipText("View onliner users");
		txtOnlineUsers.setEditable(false);
		txtOnlineUsers.setLineWrap(true);
		txtOnlineUsers.setBounds(379, 107, 158, 229);
		Game.add(txtOnlineUsers);

		//heading for online users
		JLabel lblOnlineUsers = new JLabel("ONLINE USERS");
		lblOnlineUsers.setFont(new Font("Silom", Font.PLAIN, 13));
		lblOnlineUsers.setForeground(Color.WHITE);
		lblOnlineUsers.setBounds(395, 94, 115, 16);
		Game.add(lblOnlineUsers);

		//heading for server messages
		JLabel lblServerMessages = new JLabel("SERVER MESSAGES");
		lblServerMessages.setFont(new Font("Silom", Font.PLAIN, 13));
		lblServerMessages.setForeground(Color.WHITE);
		lblServerMessages.setBounds(395, 346, 135, 16);
		Game.add(lblServerMessages);

		//send button
		JButton Send = new JButton("SEND");
		Send.setToolTipText("Click to send message");
		Send.setBackground(Color.GRAY);
		Send.setFont(new Font("Silom", Font.PLAIN, 13));
		Send.setForeground(Color.BLUE);
		Send.setBounds(442, 562, 87, 60);
		Game.add(Send);

		
		//scroll bar for input text with text box
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(28,562,402,60);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		Game.add(scrollPane);
		JTextArea txtEnterMess = new JTextArea();
		scrollPane.setViewportView(txtEnterMess);
		txtEnterMess.setLineWrap(true);
		//JTextArea textArea12 = new JTextArea();
		//textArea12.setBounds(28, 107, 310, 441);
		//Game.add(textArea12);
		//textArea12.setLineWrap(true);
		
		//WELCOME SCREEN COMPONENTS
		//title 
		JLabel titleWelcome = new JLabel("");
		titleWelcome.setBounds(22, 20, 500, 90);
		Image logoW = img.getScaledInstance(titleWelcome.getWidth(),titleWelcome.getHeight(), Image.SCALE_SMOOTH);
		titleWelcome.setIcon(new ImageIcon(logoW));
		Welcome.add(titleWelcome);
		
		
		//dare to play text
		JLabel dareTxt = new JLabel("");
		dareTxt.setBounds(133, 425, 271, 76);
		Image one = img3.getScaledInstance(dareTxt.getWidth(),dareTxt.getHeight(), Image.SCALE_SMOOTH);
		dareTxt.setIcon(new ImageIcon(one));
		Welcome.add(dareTxt);

		//enter button
		JButton btnEnter = new JButton("ENTER");
		btnEnter.setFont(new Font("Silom", Font.BOLD, 20));
		btnEnter.setToolTipText("Press to enter game");
		btnEnter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "Welcome to Mafia Chat");
				Welcome.setVisible(false);
				Login.setVisible(true);
			}
		});
		btnEnter.setForeground(Color.BLUE);
		btnEnter.setBounds(163, 512, 195, 40);
		Welcome.add(btnEnter);

		//main logo
		JLabel msnLogo = new JLabel("");
		msnLogo.setBounds(116, 157, 308, 256);
		Image imgage = img2.getScaledInstance(msnLogo.getWidth(),msnLogo.getHeight(), Image.SCALE_SMOOTH);
		msnLogo.setIcon(new ImageIcon (imgage));
		Welcome.add(msnLogo);

		//LOGIN SCREEN COMPONENTS
		//label for password text
		JLabel lblPassword = new JLabel("Enter Password");
		lblPassword.setBounds(56, 272, 165, 47);
		lblPassword.setForeground(Color.WHITE);
		lblPassword.setFont(new Font("Silom", Font.PLAIN, 18));
		Login.add(lblPassword);

		//label for username text
		JLabel label = new JLabel("Enter Username");
		label.setBounds(56, 200, 163, 38);
		label.setForeground(Color.WHITE);
		Login.add(label);
		label.setFont(new Font("Silom", Font.PLAIN, 18));

		//login button
		JButton btnLogIn = new JButton("Log In");
		btnLogIn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Login.setVisible(false);
				Game.setVisible(true);

			}
		});
		btnLogIn.setToolTipText("Press to login to game");
		btnLogIn.setFont(new Font("Silom", Font.PLAIN, 18));
		btnLogIn.setBounds(212, 362, 116, 44);
		btnLogIn.setForeground(Color.BLUE);
		Login.add(btnLogIn);

		//signup button
		JButton btnSignUp = new JButton("Sign Up");
		btnSignUp.setToolTipText("Press to register new user");
		btnSignUp.setFont(new Font("Silom", Font.PLAIN, 18));
		btnSignUp.setBounds(212, 430, 116, 44);
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
		txtUsername.setBounds(283, 188, 213, 50);
		txtUsername.setFont(new Font("Futura", Font.PLAIN, 13));
		txtUsername.setColumns(10);
		Login.add(txtUsername);


		JLabel titleLogin = new JLabel("");
		titleLogin.setBounds(22, 20, 500, 90);
		Image logoLog = img.getScaledInstance(titleLogin.getWidth(),titleLogin.getHeight(), Image.SCALE_SMOOTH);
		titleLogin.setIcon(new ImageIcon(logoLog));
		Login.add(titleLogin);

		txtPassword = new JPasswordField();
		txtPassword.setBounds(281, 269, 215, 50);
		Login.add(txtPassword);

		JLabel LogoSignIn = new JLabel("");
		LogoSignIn.setBounds(6, 558, 88, 64);
		Image logoSign = img2.getScaledInstance(LogoSignIn.getWidth(),LogoSignIn.getHeight(), Image.SCALE_SMOOTH);
		LogoSignIn.setIcon(new ImageIcon(logoSign));
		Login.add(LogoSignIn);




		JLabel LogoSignIn2 = new JLabel("");
		LogoSignIn2.setBounds(456, 558, 88, 64);
		Image logoSign2 = img2.getScaledInstance(LogoSignIn2.getWidth(),LogoSignIn2.getHeight(), Image.SCALE_SMOOTH);
		LogoSignIn2.setIcon(new ImageIcon(logoSign2));
		Login.add(LogoSignIn2);

		JButton btnForgottonPassword = new JButton("Forgotton Password");
		btnForgottonPassword.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Login.setVisible(false);
				ForgotPassword.setVisible(true);
			}
		});
		btnForgottonPassword.setToolTipText("Click here to help retrieve password");
		btnForgottonPassword.setForeground(Color.BLUE);
		btnForgottonPassword.setFont(new Font("Silom", Font.PLAIN, 13));
		btnForgottonPassword.setBounds(175, 584, 179, 38);
		Login.add(btnForgottonPassword);


		JLabel lblEnterName = new JLabel("Enter Name");
		lblEnterName.setForeground(Color.WHITE);
		lblEnterName.setFont(new Font("Silom", Font.PLAIN, 18));
		lblEnterName.setBounds(19, 178, 189, 29);
		SignIn.add(lblEnterName);

		JLabel lblEnterPassword = new JLabel("Enter Password");
		lblEnterPassword.setForeground(Color.WHITE);
		lblEnterPassword.setFont(new Font("Silom", Font.PLAIN, 18));
		lblEnterPassword.setBounds(17, 257, 213, 22);
		SignIn.add(lblEnterPassword);

		textUsername = new JTextField();
		textUsername.setBounds(282, 167, 213, 54);
		SignIn.add(textUsername);
		textUsername.setColumns(10);

		JButton btnRegister = new JButton("Register");
		btnRegister.setToolTipText("Submit registration");
		btnRegister.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SignIn.setVisible(false);
				Login.setVisible(true);

			}
		});
		btnRegister.setForeground(Color.BLUE);
		btnRegister.setFont(new Font("Silom", Font.PLAIN, 20));
		btnRegister.setBounds(214, 524, 142, 44);
		SignIn.add(btnRegister);

		JLabel titleGame = new JLabel("");
		titleGame.setBounds(22, 20, 500, 90);
		Image a = img.getScaledInstance(titleGame.getWidth(),titleGame.getHeight(), Image.SCALE_SMOOTH);
		titleGame.setIcon(new ImageIcon(a));
		Game.add(titleGame);


		JLabel titleSignIn = new JLabel("");
		titleSignIn.setBounds(22, 20, 500, 90);
		Image newimg = img.getScaledInstance(titleSignIn.getWidth(),titleSignIn.getHeight(), Image.SCALE_SMOOTH);
		titleSignIn.setIcon(new ImageIcon(newimg));
		SignIn.add(titleSignIn);

		JLabel lblSecurityQ = new JLabel("Set security question");
		lblSecurityQ.setFont(new Font("Silom", Font.PLAIN, 18));
		lblSecurityQ.setForeground(Color.WHITE);
		lblSecurityQ.setBounds(17, 336, 244, 35);
		SignIn.add(lblSecurityQ);

		txtSecurityQ = new JTextField();
		txtSecurityQ.setBounds(282, 325, 213, 54);
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
		btnBack.setFont(new Font("Silom", Font.PLAIN, 13));
		btnBack.setForeground(Color.BLUE);
		btnBack.setBounds(247, 580, 69, 29);
		SignIn.add(btnBack);

		JLabel lblEnterHint = new JLabel("Set your answer");
		lblEnterHint.setFont(new Font("Silom", Font.PLAIN, 18));
		lblEnterHint.setForeground(Color.WHITE);
		lblEnterHint.setBounds(22, 424, 229, 26);
		SignIn.add(lblEnterHint);

		txtSecurityA = new JTextField();
		txtSecurityA.setColumns(10);
		txtSecurityA.setBounds(282, 412, 213, 54);
		SignIn.add(txtSecurityA);

		textPassword = new JPasswordField();
		textPassword.setBounds(280, 246, 215, 50);
		SignIn.add(textPassword);


		JLabel titleForgotten = new JLabel("");
		titleForgotten.setBounds(22, 20, 500, 90);
		Image Logo1 = img.getScaledInstance(titleForgotten.getWidth(),titleForgotten.getHeight(), Image.SCALE_SMOOTH);
		titleForgotten.setIcon(new ImageIcon(Logo1));
		ForgotPassword.add(titleForgotten);


		JLabel lblUser = new JLabel("Enter Username");
		lblUser.setBounds(6, 193, 163, 38);
		lblUser.setForeground(Color.WHITE);
		ForgotPassword.add(lblUser);
		lblUser.setFont(new Font("Silom", Font.PLAIN, 13));

		usernameEntry = new JTextField();
		usernameEntry.setBounds(256, 186, 288, 45);
		usernameEntry.setFont(new Font("Futura", Font.PLAIN, 13));
		usernameEntry.setColumns(10);
		ForgotPassword.add(usernameEntry);

		JLabel lblSecurityQuestion = new JLabel("Security question");
		lblSecurityQuestion.setFont(new Font("Silom", Font.PLAIN, 13));
		lblSecurityQuestion.setForeground(Color.WHITE);
		lblSecurityQuestion.setBounds(6, 283, 265, 35);
		ForgotPassword.add(lblSecurityQuestion);

		securityQ = new JTextField();
		securityQ.setFont(new Font("Futura", Font.PLAIN, 13));
		securityQ.setColumns(10);
		securityQ.setBounds(256, 269, 288, 50);
		ForgotPassword.add(securityQ);

		securityAnswer = new JTextField();
		securityAnswer.setFont(new Font("Futura", Font.PLAIN, 13));
		securityAnswer.setColumns(10);
		securityAnswer.setBounds(256, 359, 288, 50);
		ForgotPassword.add(securityAnswer);

		JLabel lblEnterTheSet = new JLabel("Enter the set security answer");
		lblEnterTheSet.setForeground(Color.WHITE);
		lblEnterTheSet.setFont(new Font("Silom", Font.PLAIN, 13));
		lblEnterTheSet.setBounds(6, 376, 265, 35);
		ForgotPassword.add(lblEnterTheSet);

		JButton btnDisplayPassword = new JButton("Display Password");
		btnDisplayPassword.setToolTipText("Click here to display password");
		btnDisplayPassword.setForeground(Color.BLUE);
		btnDisplayPassword.setFont(new Font("Silom", Font.PLAIN, 16));
		btnDisplayPassword.setBounds(156, 461, 233, 44);
		ForgotPassword.add(btnDisplayPassword);

		JLabel msnForgotten1 = new JLabel("");
		msnForgotten1.setBounds(6, 558, 88, 64);
		Image LogPassword1 = img2.getScaledInstance(msnForgotten1.getWidth(),msnForgotten1.getHeight(), Image.SCALE_SMOOTH);
		msnForgotten1.setIcon(new ImageIcon(LogPassword1));
		ForgotPassword.add(msnForgotten1);


		JLabel msnForgotten2 = new JLabel("");
		msnForgotten2.setBounds(456, 558, 88, 64);
		Image LogPassword11 = img2.getScaledInstance(msnForgotten2.getWidth(),msnForgotten2.getHeight(), Image.SCALE_SMOOTH);
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
		btnReturnToLogin.setFont(new Font("Silom", Font.PLAIN, 16));
		btnReturnToLogin.setBounds(156, 517, 233, 44);
		ForgotPassword.add(btnReturnToLogin);
	}
}

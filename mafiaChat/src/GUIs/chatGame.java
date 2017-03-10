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

public class chatGame {
	private javax.swing.JScrollPane jScrollPane1;
	private JFrame GAME;
	private JTextField textField;
	private JTextField textField_2;
	private JPasswordField passwordField;
	private JTextField textField_1;
	private JTextField textField_4;
	private JPasswordField passwordField_1;
	private JScrollPane scrollB;
	private JScrollBar Scroll;
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

		GAME = new JFrame();
		GAME.setTitle("Mafia Chat");
		GAME.setBounds(100, 100, 550, 650);
		GAME.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Image img = new ImageIcon(this.getClass().getResource("/LOGO.png")).getImage();
		Image img2 = new ImageIcon(this.getClass().getResource("/bigChat.png")).getImage();
		Image img3 = new ImageIcon(this.getClass().getResource("/dare-to-play.png")).getImage();
		GAME.getContentPane().setLayout(new CardLayout(0, 0));

		JPanel Welcome = new JPanel();
		Welcome.setBackground(Color.BLACK);
		GAME.getContentPane().add(Welcome, "name_30392538864818");
		Welcome.setLayout(null);

		JPanel Login = new JPanel();
		Login.setBackground(Color.BLACK);
		GAME.getContentPane().add(Login, "name_30392587252988");
		Login.setLayout(null);

		JPanel SignIn = new JPanel();
		SignIn.setBackground(Color.BLACK);
		SignIn.setForeground(Color.BLACK);
		GAME.getContentPane().add(SignIn, "name_30392612646698");
		SignIn.setLayout(null);

		JPanel Game = new JPanel();
		Game.setBackground(Color.BLACK);
		Game.setForeground(Color.BLACK);
		GAME.getContentPane().add(Game, "name_30392640535777");
		Game.setLayout(null);


		JLabel logoSignIn1 = new JLabel("");
		logoSignIn1.setBounds(6, 558, 88, 64);
		Image SigninLogo1 = img2.getScaledInstance(logoSignIn1.getWidth(),logoSignIn1.getHeight(), Image.SCALE_SMOOTH);
		logoSignIn1.setIcon(new ImageIcon(SigninLogo1));
		SignIn.add(logoSignIn1);



		JLabel logoSignIn2 = new JLabel("");
		logoSignIn2.setBounds(456, 558, 88, 64);
		Image SigninLogo2 = img2.getScaledInstance(logoSignIn2.getWidth(),logoSignIn2.getHeight(), Image.SCALE_SMOOTH);
		logoSignIn2.setIcon(new ImageIcon(SigninLogo2));
		SignIn.add(logoSignIn2);



		JLabel logo = new JLabel("New label");
		logo.setBounds(0, 6, 188, 79);





		JScrollPane scr = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);// Add your text area to scroll pane 
		scr.setBounds(30, 102, 327, 448);// You have to set bounds for all the controls and containers incas eof null layout
		Game.add(scr);// Add you scroll pane to container

		JLabel panel = new JLabel("");
		scr.setRowHeaderView(panel);
		Image imageG = img.getScaledInstance(panel.getWidth(),panel.getHeight(), Image.SCALE_SMOOTH);
		panel.setIcon(new ImageIcon(imageG));





		JTextArea serverText = new JTextArea();
		serverText.setEditable(false);
		serverText.setLineWrap(true);
		serverText.setBounds(369, 365, 160, 185);
		Game.add(serverText);

		JTextArea onlineUsers = new JTextArea();
		onlineUsers.setEditable(false);
		onlineUsers.setLineWrap(true);
		onlineUsers.setBounds(371, 105, 158, 229);
		Game.add(onlineUsers);

		JLabel lblOnlineUsers = new JLabel("ONLINE USERS");
		lblOnlineUsers.setFont(new Font("Silom", Font.PLAIN, 13));
		lblOnlineUsers.setForeground(Color.WHITE);
		lblOnlineUsers.setBounds(401, 90, 115, 16);
		Game.add(lblOnlineUsers);

		JLabel lblServerMessages = new JLabel("SERVER MESSAGES");
		lblServerMessages.setFont(new Font("Silom", Font.PLAIN, 13));
		lblServerMessages.setForeground(Color.WHITE);
		lblServerMessages.setBounds(381, 346, 135, 16);
		Game.add(lblServerMessages);

		JButton Send = new JButton("SEND");
		Send.setBackground(Color.GRAY);
		Send.setFont(new Font("Silom", Font.PLAIN, 13));
		Send.setForeground(Color.BLUE);
		Send.setBounds(442, 562, 87, 60);
		Game.add(Send);
		JScrollPane scrollPane = new JScrollPane();

		scrollPane.setBounds(28,562,402,60);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		Game.add(scrollPane);



		JTextArea textArea = new JTextArea();
		scrollPane.setViewportView(textArea);
		textArea.setLineWrap(true);

		JMenuItem mntmMenu = new JMenuItem("Menu");
		mntmMenu.setBounds(391, 6, 153, 19);
		Game.add(mntmMenu);






		JLabel logoWelcome = new JLabel("");
		logoWelcome.setBounds(22, 20, 500, 90);
		Image logoW = img.getScaledInstance(logoWelcome.getWidth(),logoWelcome.getHeight(), Image.SCALE_SMOOTH);
		logoWelcome.setIcon(new ImageIcon(logoW));
		Welcome.add(logoWelcome);

		JLabel text1 = new JLabel("");
		text1.setBounds(133, 425, 271, 76);
		Image one = img3.getScaledInstance(text1.getWidth(),text1.getHeight(), Image.SCALE_SMOOTH);
		text1.setIcon(new ImageIcon(one));
		Welcome.add(text1);

		JButton btnEnter = new JButton("ENTER");
		btnEnter.setFont(new Font("Silom", Font.BOLD, 20));
		btnEnter.setToolTipText("Press to enter game");
		btnEnter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Welcome.setVisible(false);
				Login.setVisible(true);
			}
		});
		btnEnter.setForeground(Color.BLUE);
		btnEnter.setBounds(163, 512, 195, 40);
		Welcome.add(btnEnter);

		JLabel msn = new JLabel("");
		msn.setBounds(116, 157, 308, 256);
		Image imgage = img2.getScaledInstance(msn.getWidth(),msn.getHeight(), Image.SCALE_SMOOTH);
		msn.setIcon(new ImageIcon (imgage));
		Welcome.add(msn);




		JLabel label_1 = new JLabel("Enter Password");
		label_1.setBounds(56, 272, 165, 47);
		label_1.setForeground(Color.WHITE);
		label_1.setFont(new Font("Silom", Font.PLAIN, 18));
		Login.add(label_1);

		JLabel label = new JLabel("Enter Username");
		label.setBounds(56, 200, 163, 38);
		label.setForeground(Color.WHITE);
		Login.add(label);
		label.setFont(new Font("Silom", Font.PLAIN, 18));

		JButton button = new JButton("Log In");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Login.setVisible(false);
				Game.setVisible(true);

			}
		});
		button.setToolTipText("Press to login to game");
		button.setFont(new Font("Silom", Font.PLAIN, 18));
		button.setBounds(212, 362, 116, 44);
		button.setForeground(Color.BLUE);
		Login.add(button);

		JButton button_1 = new JButton("Sign Up");
		button_1.setToolTipText("Press to register new user");
		button_1.setFont(new Font("Silom", Font.PLAIN, 18));
		button_1.setBounds(212, 430, 116, 44);
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Login.setVisible(false);
				SignIn.setVisible(true);
			}
		});
		button_1.setForeground(Color.BLUE);
		Login.add(button_1);

		textField = new JTextField();
		textField.setBounds(283, 188, 213, 50);
		textField.setFont(new Font("Futura", Font.PLAIN, 13));
		textField.setColumns(10);
		Login.add(textField);


		JLabel logoLogin = new JLabel("");
		logoLogin.setBounds(22, 20, 500, 90);
		Image logoLog = img.getScaledInstance(logoLogin.getWidth(),logoLogin.getHeight(), Image.SCALE_SMOOTH);
		logoLogin.setIcon(new ImageIcon(logoLog));
		Login.add(logoLogin);

		passwordField = new JPasswordField();
		passwordField.setBounds(281, 269, 215, 50);
		Login.add(passwordField);

		JLabel LogoSignIn = new JLabel("");
		LogoSignIn.setBounds(6, 558, 88, 64);
		Image logoSign = img2.getScaledInstance(LogoSignIn.getWidth(),LogoSignIn.getHeight(), Image.SCALE_SMOOTH);
		LogoSignIn.setIcon(new ImageIcon(logoSign));
		Login.add(LogoSignIn);


		JLabel LogoSignIn2 = new JLabel("");
		LogoSignIn2.setBounds(462, 558, 88, 64);
		Image logoSign2 = img2.getScaledInstance(LogoSignIn2.getWidth(),LogoSignIn2.getHeight(), Image.SCALE_SMOOTH);
		LogoSignIn2.setIcon(new ImageIcon(logoSign2));
		Login.add(LogoSignIn2);


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

		textField_2 = new JTextField();
		textField_2.setBounds(282, 167, 213, 54);
		SignIn.add(textField_2);
		textField_2.setColumns(10);

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



		JLabel label_2 = new JLabel("");
		label_2.setBounds(22, 20, 500, 90);
		Image newimg = img.getScaledInstance(label_2.getWidth(),label_2.getHeight(), Image.SCALE_SMOOTH);
		label_2.setIcon(new ImageIcon(newimg));
		SignIn.add(label_2);

		JLabel lblEnterPasswordHint = new JLabel("Set security question");
		lblEnterPasswordHint.setFont(new Font("Silom", Font.PLAIN, 18));
		lblEnterPasswordHint.setForeground(Color.WHITE);
		lblEnterPasswordHint.setBounds(17, 336, 244, 35);
		SignIn.add(lblEnterPasswordHint);

		textField_1 = new JTextField();
		textField_1.setBounds(282, 325, 213, 54);
		SignIn.add(textField_1);
		textField_1.setColumns(10);

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

		textField_4 = new JTextField();
		textField_4.setColumns(10);
		textField_4.setBounds(282, 412, 213, 54);
		SignIn.add(textField_4);

		passwordField_1 = new JPasswordField();
		passwordField_1.setBounds(280, 246, 215, 50);
		SignIn.add(passwordField_1);





	}
}

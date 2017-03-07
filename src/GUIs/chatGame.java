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

public class chatGame {

	private JFrame GAME;
	private JTextField textField;
	private JTextField textField_2;
	private JTextField textField_3;
	private JPasswordField passwordField;
	private JTextField textField_1;

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
		GAME.setBounds(100, 100, 450, 300);
		GAME.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GAME.getContentPane().setLayout(new CardLayout(0, 0));
		Image img = new ImageIcon(this.getClass().getResource("logo.png")).getImage();
		
		JPanel Welcome = new JPanel();
		Welcome.setBackground(Color.BLACK);
		GAME.getContentPane().add(Welcome, "name_53180384765910");
		Welcome.setLayout(null);

		JPanel Login = new JPanel();
		Login.setBackground(Color.BLACK);
		GAME.getContentPane().add(Login, "name_52930319015976");
		Login.setLayout(null);
		
		JPanel SignIn = new JPanel();
		SignIn.setBackground(Color.BLACK);
		SignIn.setForeground(Color.BLACK);
		GAME.getContentPane().add(SignIn, "name_52930328695446");
		SignIn.setLayout(null);
		
		
		JLabel logoWelcome = new JLabel("");
		logoWelcome.setBounds(23, 46, 404, 64);
		Image logoW = img.getScaledInstance(logoWelcome.getWidth(),logoWelcome.getHeight(), Image.SCALE_SMOOTH);
		logoWelcome.setIcon(new ImageIcon(logoW));
		Welcome.add(logoWelcome);
		
		JLabel lblDareToPlay = new JLabel("Dare to play...");
		lblDareToPlay.setFont(new Font("Futura", Font.PLAIN, 16));
		lblDareToPlay.setForeground(Color.WHITE);
		lblDareToPlay.setBounds(157, 136, 222, 16);
		Welcome.add(lblDareToPlay);
		
		JButton btnEnter = new JButton("ENTER");
		btnEnter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Welcome.setVisible(false);
				Login.setVisible(true);
			}
		});
		btnEnter.setForeground(Color.BLUE);
		btnEnter.setBounds(159, 186, 117, 29);
		Welcome.add(btnEnter);
		
		
		
			
			JLabel label_1 = new JLabel("Enter Password");
			label_1.setBounds(53, 137, 133, 19);
			label_1.setForeground(Color.WHITE);
			label_1.setFont(new Font("Futura", Font.PLAIN, 18));
			Login.add(label_1);
			
			JLabel label = new JLabel("Enter Username");
			label.setBounds(53, 96, 136, 23);
			label.setForeground(Color.WHITE);
			Login.add(label);
			label.setFont(new Font("Futura", Font.PLAIN, 18));
			
			JButton button = new JButton("Log In");
			button.setFont(new Font("Lucida Grande", Font.PLAIN, 14));
			button.setBounds(172, 168, 92, 29);
			button.setForeground(Color.BLUE);
			Login.add(button);
			
			JButton button_1 = new JButton("Sign Up");
			button_1.setFont(new Font("Lucida Grande", Font.PLAIN, 14));
			button_1.setBounds(172, 209, 92, 29);
			button_1.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Login.setVisible(false);
					SignIn.setVisible(true);
				}
			});
			button_1.setForeground(Color.BLUE);
			Login.add(button_1);
			
			textField = new JTextField();
			textField.setBounds(210, 95, 181, 28);
			textField.setFont(new Font("Futura", Font.PLAIN, 13));
			textField.setColumns(10);
			Login.add(textField);
			
					
					JLabel panel = new JLabel("");
					panel.setBounds(22, 22, 404, 47);
					Image logo = img.getScaledInstance(panel.getWidth(),panel.getHeight(), Image.SCALE_SMOOTH);
					panel.setIcon(new ImageIcon(logo));
					Login.add(panel);
					
					passwordField = new JPasswordField();
					passwordField.setBounds(210, 135, 181, 26);
					Login.add(passwordField);
		
		
		
		JLabel lblEnterName = new JLabel("Enter Name");
		lblEnterName.setForeground(Color.WHITE);
		lblEnterName.setFont(new Font("Futura", Font.PLAIN, 14));
		lblEnterName.setBounds(28, 86, 116, 22);
		SignIn.add(lblEnterName);
		
		JLabel lblEnterPassword = new JLabel("Enter Password");
		lblEnterPassword.setForeground(Color.WHITE);
		lblEnterPassword.setFont(new Font("Futura", Font.PLAIN, 14));
		lblEnterPassword.setBounds(28, 120, 127, 22);
		SignIn.add(lblEnterPassword);
		
		textField_2 = new JTextField();
		textField_2.setBounds(191, 86, 174, 22);
		SignIn.add(textField_2);
		textField_2.setColumns(10);
		
		textField_3 = new JTextField();
		textField_3.setColumns(10);
		textField_3.setBounds(191, 120, 174, 22);
		SignIn.add(textField_3);
		
		JButton btnRegister = new JButton("Register");
		btnRegister.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SignIn.setVisible(false);
				Login.setVisible(true);
				
			}
		});
		btnRegister.setForeground(Color.BLUE);
		btnRegister.setFont(new Font("Futura", Font.PLAIN, 14));
		btnRegister.setBounds(155, 190, 117, 29);
		SignIn.add(btnRegister);
		
		
		
		JLabel label_2 = new JLabel("");
		label_2.setBounds(28, 19, 399, 60);
		Image newimg = img.getScaledInstance(label_2.getWidth(),label_2.getHeight(), Image.SCALE_SMOOTH);
		label_2.setIcon(new ImageIcon(newimg));
		SignIn.add(label_2);
		
		JLabel lblEnterPasswordHint = new JLabel("Enter Password Hint");
		lblEnterPasswordHint.setFont(new Font("Futura", Font.PLAIN, 14));
		lblEnterPasswordHint.setForeground(Color.WHITE);
		lblEnterPasswordHint.setBounds(28, 158, 156, 16);
		SignIn.add(lblEnterPasswordHint);
		
		textField_1 = new JTextField();
		textField_1.setBounds(191, 153, 174, 26);
		SignIn.add(textField_1);
		textField_1.setColumns(10);
		
		JButton btnBack = new JButton("Back");
		btnBack.setFont(new Font("Futura", Font.PLAIN, 10));
		btnBack.setForeground(Color.BLUE);
		btnBack.setBounds(6, 249, 46, 29);
		SignIn.add(btnBack);
		
	
		
	}
}

package GUIs;


import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextField;
import java.awt.Font;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JButton;

public class Frame2 {

	private JFrame frame;
	private JTextField txtEnterUsername;
	private JTextField txtEnterPassword;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Frame2 window = new Frame2();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Frame2() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setBackground(new Color(0, 128, 128));
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		txtEnterUsername = new JTextField();
		txtEnterUsername.setFont(new Font("Futura", Font.PLAIN, 13));
		txtEnterUsername.setBounds(216, 92, 104, 26);
		frame.getContentPane().add(txtEnterUsername);
		txtEnterUsername.setColumns(10);
		
		txtEnterPassword = new JTextField();
		txtEnterPassword.setFont(new Font("Futura", Font.PLAIN, 13));
		txtEnterPassword.setBounds(216, 128, 104, 26);
		frame.getContentPane().add(txtEnterPassword);
		txtEnterPassword.setColumns(10);
		
		JLabel lblEnterUsername = new JLabel("Enter Username");
		lblEnterUsername.setFont(new Font("Futura", Font.PLAIN, 13));
		lblEnterUsername.setBounds(83, 97, 109, 16);
		frame.getContentPane().add(lblEnterUsername);
		
		JLabel lblEnterPassword = new JLabel("Enter Password");
		lblEnterPassword.setFont(new Font("Futura", Font.PLAIN, 13));
		lblEnterPassword.setBounds(83, 133, 104, 16);
		frame.getContentPane().add(lblEnterPassword);
		
		JLabel lblMafiaChat = new JLabel("MAFIA CHAT");
		lblMafiaChat.setFont(new Font("Futura", Font.PLAIN, 60));
		lblMafiaChat.setBounds(51, 21, 393, 59);
		frame.getContentPane().add(lblMafiaChat);
		
		JButton btnLogIn = new JButton("Log In");
		btnLogIn.setForeground(new Color(0, 128, 128));
		btnLogIn.setBounds(145, 179, 117, 29);
		frame.getContentPane().add(btnLogIn);
		
		JButton btnSignUp = new JButton("Sign Up");
		btnSignUp.setForeground(new Color(0, 128, 128));
		btnSignUp.setBounds(145, 220, 117, 29);
		frame.getContentPane().add(btnSignUp);
	}
}

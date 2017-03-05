package GUIs;


import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.Color;
import javax.swing.JTextField;
import javax.swing.JLabel;

public class Frame1 {

	private JFrame frmSignup;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Frame1 window = new Frame1();
					window.frmSignup.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Frame1() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmSignup = new JFrame();
		frmSignup.setTitle("SignUp");
		frmSignup.getContentPane().setBackground(new Color(0, 128, 128));
		frmSignup.setBounds(100, 100, 450, 300);
		frmSignup.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmSignup.getContentPane().setLayout(null);
		
		JButton btnSignIn = new JButton("Submit");
		btnSignIn.setForeground(new Color(0, 128, 128));
		btnSignIn.setFont(new Font("Futura", Font.PLAIN, 13));
		btnSignIn.setBounds(300, 235, 144, 37);
		frmSignup.getContentPane().add(btnSignIn);
		
		textField = new JTextField();
		textField.setBounds(246, 46, 198, 37);
		frmSignup.getContentPane().add(textField);
		textField.setColumns(10);
		
		JLabel lblPleaseEnterYour = new JLabel("Enter your name");
		lblPleaseEnterYour.setFont(new Font("Futura", Font.PLAIN, 13));
		lblPleaseEnterYour.setBackground(new Color(192, 192, 192));
		lblPleaseEnterYour.setForeground(new Color(0, 0, 0));
		lblPleaseEnterYour.setBounds(6, 54, 189, 20);
		frmSignup.getContentPane().add(lblPleaseEnterYour);
		
		JLabel lblPleaseEnterYour_1 = new JLabel("Enter your Password");
		lblPleaseEnterYour_1.setFont(new Font("Futura", Font.PLAIN, 13));
		lblPleaseEnterYour_1.setBounds(6, 95, 189, 20);
		frmSignup.getContentPane().add(lblPleaseEnterYour_1);
		
		textField_1 = new JTextField();
		textField_1.setBounds(246, 87, 198, 37);
		frmSignup.getContentPane().add(textField_1);
		textField_1.setColumns(10);
		
		JLabel lblPleaseEnterYour_2 = new JLabel("Enter your password hint");
		lblPleaseEnterYour_2.setForeground(Color.BLACK);
		lblPleaseEnterYour_2.setFont(new Font("Futura", Font.PLAIN, 13));
		lblPleaseEnterYour_2.setBackground(Color.LIGHT_GRAY);
		lblPleaseEnterYour_2.setBounds(6, 127, 198, 33);
		frmSignup.getContentPane().add(lblPleaseEnterYour_2);
		
		textField_2 = new JTextField();
		textField_2.setColumns(10);
		textField_2.setBounds(246, 125, 198, 37);
		frmSignup.getContentPane().add(textField_2);
		
		JLabel lblPleaseNoteThat = new JLabel("Please note: ");
		lblPleaseNoteThat.setFont(new Font("Futura", Font.PLAIN, 13));
		lblPleaseNoteThat.setBounds(9, 161, 79, 37);
		frmSignup.getContentPane().add(lblPleaseNoteThat);
		
		JLabel lblAllInformationWill = new JLabel("All information will be stored on the Mafia Game Database.");
		lblAllInformationWill.setFont(new Font("Futura", Font.PLAIN, 13));
		lblAllInformationWill.setBounds(89, 172, 369, 16);
		frmSignup.getContentPane().add(lblAllInformationWill);
	}
}

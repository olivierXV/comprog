package project;

import java.sql.*;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Login extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField username;
	private JPasswordField password;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Login frame = new Login();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 * @throws ClassNotFoundException 
	 * @throws SQLException 
	 */
	public Login() throws ClassNotFoundException, SQLException {
		setBackground(new Color(54, 54, 54));
		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/login","root","xarpeius");
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setForeground(new Color(255, 255, 255));
		contentPane.setBackground(new Color(36, 36, 36));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Username");
		lblNewLabel.setFont(new Font("Cantarell", Font.PLAIN, 15));
		lblNewLabel.setForeground(new Color(255, 255, 255));
		lblNewLabel.setBounds(100, 25, 115, 20);
		contentPane.add(lblNewLabel);
		
		JLabel lblPassword = new JLabel("Password");
		lblPassword.setForeground(Color.WHITE);
		lblPassword.setFont(new Font("Cantarell", Font.PLAIN, 15));
		lblPassword.setBounds(100, 104, 115, 20);
		contentPane.add(lblPassword);
		
		JButton btnLogin = new JButton("Log in");
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Statement s = con.createStatement();
					String query = "select * from login.login where username='"+username.getText()+"' and password='"+password.getText()+"'";
					ResultSet rs = s.executeQuery(query);
					
					if (username.getText() == null || password.getText() == null) {
						JOptionPane.showMessageDialog(null, "Please fill out all fields");
					} else {
						if (rs.next()) {
							JOptionPane.showMessageDialog(null, "Log in successful.");
						} else {
							JOptionPane.showMessageDialog(null, "Please try again.");
						}
					}
				}catch (Exception e1) {
					// TODO: handle exception
				}
			}
		});
		btnLogin.setForeground(new Color(255, 255, 255));
		btnLogin.setBackground(new Color(54, 54, 54));
		btnLogin.setFont(new Font("Cantarell", Font.PLAIN, 15));
		btnLogin.setBounds(115, 194, 101, 30);
		contentPane.add(btnLogin);
		
		username = new JTextField();
		username.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!(password.getText()==null)) {
					btnLogin.doClick();
				} else {
					password.requestFocusInWindow();
				}
			}
		});
		username.setForeground(new Color(255, 255, 255));
		username.setBackground(new Color(54, 54, 54));
		username.setFont(new Font("Cantarell", Font.PLAIN, 15));
		username.setBounds(100, 57, 250, 35);
		contentPane.add(username);
		username.setColumns(10);
		
		password = new JPasswordField();
		password.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnLogin.doClick();
			}
		});
		password.setForeground(new Color(255, 255, 255));
		password.setBackground(new Color(54, 54, 54));
		password.setFont(new Font("Cantarell", Font.PLAIN, 15));
		password.setColumns(10);
		password.setBounds(100, 136, 250, 35);
		contentPane.add(password);
		
		JButton btnClear = new JButton("Clear");
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int result = JOptionPane.showConfirmDialog(null, "Are you sure you want to clear?", "Confirm Clearing?", JOptionPane.YES_NO_OPTION);
				if (result == JOptionPane.YES_OPTION) {
					username.setText(null);
					password.setText(null);
				}
			}
		});
		btnClear.setForeground(Color.WHITE);
		btnClear.setFont(new Font("Cantarell", Font.PLAIN, 15));
		btnClear.setBackground(new Color(54, 54, 54));
		btnClear.setBounds(230, 194, 101, 30);
		contentPane.add(btnClear);
	}
}

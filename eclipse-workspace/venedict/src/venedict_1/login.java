package venedict_1;

import java.awt.EventQueue;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.UIManager;
import java.awt.Font;

public class login extends JFrame {

	HashMap<String,String> logi = new HashMap<String, String>();
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtuser;
	private JPasswordField pdlpass;
	private JLabel lblwrong;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					login frame = new login();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public login() {
		logi.put("venz", "123456");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 321);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBackground(UIManager.getColor("Button.darkShadow"));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Username");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 17));
		lblNewLabel.setBounds(43, 28, 93, 32);
		contentPane.add(lblNewLabel);
		
		JLabel lblPassword = new JLabel("Password:");
		lblPassword.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblPassword.setBounds(43, 82, 93, 32);
		contentPane.add(lblPassword);
		
		txtuser = new JTextField();
		txtuser.setBounds(142, 25, 229, 39);
		contentPane.add(txtuser);
		txtuser.setColumns(10);
		
		pdlpass = new JPasswordField();
		pdlpass.setBounds(142, 82, 230, 32);
		contentPane.add(pdlpass);
		
		JLabel lblwrong = new JLabel("");
		lblwrong.setBounds(43, 125, 321, 53);
		contentPane.add(lblwrong);
	
		
		JButton btnReset = new JButton("Reset");
		btnReset.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnReset.setBackground(UIManager.getColor("CheckBox.foreground"));
		btnReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtuser.setText("");
				pdlpass.setText("");
			}
		});
		btnReset.setBounds(43, 187, 130, 53);
		contentPane.add(btnReset);
		
		JButton btnLogin = new JButton("Login");
		btnLogin.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String user = txtuser.getText();
				String pass = pdlpass.getText();
				if (logi.containsKey(user)&& logi.containsValue(pass)) {
					mainmenu main = new mainmenu();
					JOptionPane.showMessageDialog(btnLogin, "Welcome back");
					main.setVisible(true);
					dispose();
				}
				else {
					JOptionPane.showMessageDialog(btnLogin, "incorrect Username or Password");
				}
				
			}
		});
		btnLogin.setBounds(227, 187, 130, 53);
		contentPane.add(btnLogin);
	}
}

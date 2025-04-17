package venedict_1;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;

public class mainmenu extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					mainmenu frame = new mainmenu();
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
	public mainmenu() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnactivty1 = new JButton("Activity 1");
		btnactivty1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				grading grador = new grading();
				grador.setVisible(true);
				dispose();
				
			}
		});
		btnactivty1.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnactivty1.setBounds(20, 52, 120, 50);
		contentPane.add(btnactivty1);
		
		JButton btnactivity2 = new JButton("Activity 2");
		btnactivity2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				legalage legal = new legalage();
				legal.setVisible(true);
				dispose();
			}
		});
		btnactivity2.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnactivity2.setBounds(259, 52, 120, 50);
		contentPane.add(btnactivity2);
		
		JButton btnactivity3 = new JButton("Activity 3");
		btnactivity3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				gradingSystem gradesys = new gradingSystem();
				gradesys.setVisible(true);
				dispose();
			}
		});
		btnactivity3.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnactivity3.setBounds(20, 126, 120, 50);
		contentPane.add(btnactivity3);
		
		JButton btnactivity4 = new JButton("Activity 4");
		btnactivity4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fahrenheit fahren = new fahrenheit();
				fahren.setVisible(true);
				dispose();
			}
		});
		btnactivity4.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnactivity4.setBounds(259, 126, 120, 50);
		contentPane.add(btnactivity4);
		
		JButton btnexit = new JButton("Exit");
		btnexit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnexit.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnexit.setBounds(20, 187, 120, 50);
		contentPane.add(btnexit);
		
		JLabel lblNewLabel = new JLabel("Main Menu");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblNewLabel.setBounds(141, 0, 127, 50);
		contentPane.add(lblNewLabel);
		
		JButton btnlogout = new JButton("logout");
		btnlogout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				login log = new login();
				log.setVisible(true);
				dispose();
			}
		});
		btnlogout.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnlogout.setBounds(259, 187, 120, 50);
		contentPane.add(btnlogout);
	}
}

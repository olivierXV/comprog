package venedict_1;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class legalage extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtname;
	private JTextField txtage;
	private JLabel lbllegal;
	private JButton btnBack;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					legalage frame = new legalage();
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
	public legalage() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 543, 335);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Enter your name :");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 28));
		lblNewLabel.setBounds(10, 11, 288, 38);
		contentPane.add(lblNewLabel);
		
		JLabel lblEnterYoureAge = new JLabel("Enter your age :");
		lblEnterYoureAge.setFont(new Font("Tahoma", Font.BOLD, 28));
		lblEnterYoureAge.setBounds(10, 60, 275, 38);
		contentPane.add(lblEnterYoureAge);
		
		txtname = new JTextField();
		txtname.setBounds(295, 11, 201, 38);
		contentPane.add(txtname);
		txtname.setColumns(10);
		
		txtage = new JTextField();
		txtage.setColumns(10);
		txtage.setBounds(295, 67, 201, 38);
		contentPane.add(txtage);
		
		lbllegal = new JLabel("");
		lbllegal.setFont(new Font("Tahoma", Font.BOLD, 28));
		lbllegal.setBounds(20, 109, 476, 62);
		contentPane.add(lbllegal);
		
		JButton btnsubmit = new JButton("Submit");
		btnsubmit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String name = txtname.getText();
				int age = Integer.parseInt(txtage.getText());
				if (age >= 18 ) {
					lbllegal.setText("Your Legal");
				}
				else {
					lbllegal.setText("Your Non Legal");
				}
			}
		});
		btnsubmit.setBounds(10, 174, 185, 55);
		contentPane.add(btnsubmit);
		
		JButton btnNewButton_1 = new JButton("Clear");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtname.setText(null);
				txtage.setText(null);
				lbllegal.setText(null);
			}
		});
		btnNewButton_1.setBounds(311, 174, 185, 55);
		contentPane.add(btnNewButton_1);
		
		btnBack = new JButton("Back");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mainmenu main = new mainmenu();
				main.setVisible(true);
				dispose();
			}
		});
		btnBack.setBounds(162, 234, 185, 55);
		contentPane.add(btnBack);
	}
}

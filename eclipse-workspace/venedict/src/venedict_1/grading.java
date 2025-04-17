package venedict_1;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;
import java.awt.SystemColor;

public class grading extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txfisrtname;
	private JTextField txlastname;
	private JLabel lbtotal;
	private JButton btnBack;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					grading frame = new grading();
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
	public grading() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 495, 394);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBackground(SystemColor.controlShadow);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lbfirstname = new JLabel("Enter your name:");
		lbfirstname.setFont(new Font("Yu Gothic Medium", Font.BOLD, 18));
		lbfirstname.setBounds(10, 51, 201, 31);
		contentPane.add(lbfirstname);
		
		JLabel lblastname = new JLabel("Section :");
		lblastname.setFont(new Font("Yu Gothic Medium", Font.BOLD, 18));
		lblastname.setBounds(10, 111, 201, 31);
		contentPane.add(lblastname);
		
		txfisrtname = new JTextField();
		txfisrtname.setBounds(170, 49, 286, 31);
		contentPane.add(txfisrtname);
		txfisrtname.setColumns(10);
		
		txlastname = new JTextField();
		txlastname.setColumns(10);
		txlastname.setBounds(170, 109, 286, 31);
		contentPane.add(txlastname);
		
		JButton btnNewButton = new JButton("Submit");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String firstname = txfisrtname.getText();
				String lastname = txlastname.getText();
				lbtotal.setText("You're name is   " + firstname + "  and your're section is " + lastname + " "  );
				
				
				
			}
		});
		
		btnNewButton.setBounds(33, 204, 157, 45);
		contentPane.add(btnNewButton);
		
		JButton btnReset = new JButton("Clear");
		btnReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txfisrtname.setText("");
				txlastname.setText("");
				lbtotal.setText("");
			}
		});
		btnReset.setBounds(267, 204, 157, 45);
		contentPane.add(btnReset);
		
		 lbtotal = new JLabel("");
		lbtotal.setBounds(20, 140, 408, 56);
		contentPane.add(lbtotal);
		
		btnBack = new JButton("Back");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mainmenu main = new mainmenu();
				main.setVisible(true);
				dispose();
			}
		});
		btnBack.setBounds(147, 266, 157, 45);
		contentPane.add(btnBack);
	}
}
